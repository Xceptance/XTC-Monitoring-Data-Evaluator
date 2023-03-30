package com.xceptance.xtc.mondaev;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.opencsv.bean.CsvToBeanBuilder;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "Mondaev", version = "Mondaev 1.0", mixinStandardHelpOptions = true)
public class Mondaev implements Runnable
{
    private final Logger logger = Logger.getLogger(Mondaev.class.getName());

    @Option(names = { "-f", "--filename" }, required = true, description = "The input file of a certain month of monitoring")
    String fileName;

    @Override
    public void run()
    {
        // our data home
        final Map<String, ScenarioStatistic> buckets = new HashMap<>();

        // load file data
        try
        {
            var csvStream = new CsvToBeanBuilder<Data>(new FileReader(fileName))
                            .withSeparator(',')
                            .withQuoteChar('"')
                            .withThrowExceptions(false)
                            .withOrderedResults(true)
                            .withType(Data.class).build().stream();

            // parse data into objects, fill into buckets by Name
            csvStream.forEach(d ->
            {
                buckets.compute(d.scenario, (k, v) -> v == null ? new ScenarioStatistic(k) : v.add(d));
            });
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        // now, we have all things in scenario buckets, we can start to analyze it

        // what scenarios do we have
        System.out.println("==== Scenarios");
        var scenarios = buckets.entrySet().stream().map(e -> e.getValue().scenario()).sorted().distinct().toList();
        scenarios.forEach(System.out::println);

        // what locations do we have
        System.out.println("==== Locations");
        var locations = buckets.entrySet().stream()
                        .flatMap(e -> e.getValue().entries(d -> true, d -> d.location).stream())
                        .sorted()
                        .distinct()
                        .toList();
        locations.forEach(System.out::println);

        var cols = List.of(
                        new Column<String>("Scenario", d -> true, (s, f) -> s.scenario(), s -> s),
                        new Column<String>("Location", d -> true, (s, f) -> s.location(f), s -> s),
                        new Column<Long>("Total", d -> true, (s, f) -> s.count(f), s -> Long.toString(s)),
                        new Column<Long>("Success", ScenarioStatistic.SUCCESS, (s, f) -> s.count(f), s -> Long.toString(s)),
                        new Column<Long>("Failed", ScenarioStatistic.FAILED, (s, f) -> s.count(f), s -> Long.toString(s)),
                        new Column<Long>("Total ex QP", ScenarioStatistic.OUTSIDE_QUIETPERIOD, (s, f) -> s.count(f), s -> Long.toString(s)),
                        new Column<Long>("Success ex QP", ScenarioStatistic.SUCCESS.and(ScenarioStatistic.OUTSIDE_QUIETPERIOD), (s, f) -> s.count(f), s -> Long.toString(s)),
                        new Column<Long>("Failed ex QP", ScenarioStatistic.FAILED.and(ScenarioStatistic.OUTSIDE_QUIETPERIOD), (s, f) -> s.count(f), s -> Long.toString(s)),
                        // Runtime
                        new Column<OptionalDouble>("Avg Runtime Success ex QP",
                                        ScenarioStatistic.SUCCESS.and(ScenarioStatistic.OUTSIDE_QUIETPERIOD).and(d -> d.requestRuntimeLimit > 0),
                                        (s, f) -> s.avg(f, d -> (long) d.requestRuntime),
                                        v -> v.isPresent() ? String.valueOf(Math.round(v.getAsDouble())) : "N/A"),
                        new Column<OptionalLong>("P95 Runtime Success ex QP",
                                        ScenarioStatistic.SUCCESS.and(ScenarioStatistic.OUTSIDE_QUIETPERIOD).and(d -> d.requestRuntimeLimit > 0),
                                        (s, f) -> s.pXX(95, f, d -> (long) d.requestRuntime),
                                        v -> v.isPresent() ? String.valueOf(v.getAsLong()) : "N/A"),
                        new Column<OptionalLong>("Max Runtime ex QP",
                                        ScenarioStatistic.OUTSIDE_QUIETPERIOD.and(d -> d.requestRuntimeLimit > 0),
                                        (s, f) -> s.max(f, d -> (long) d.requestRuntime),
                                        v -> v.isPresent() ? String.valueOf(v.getAsLong()) : "N/A"),
                        // FCP
                        new Column<OptionalDouble>("Avg FCP Success ex QP",
                                        ScenarioStatistic.SUCCESS.and(ScenarioStatistic.OUTSIDE_QUIETPERIOD).and(d -> d.firstContentfulPaintEventLimit > 0),
                                        (s, f) -> s.avg(f, d -> (long) d.firstContentfulPaintEvent),
                                        v -> v.isPresent() ? String.valueOf(Math.round(v.getAsDouble())) : "N/A"),
                        new Column<OptionalLong>("P95 FCP Success ex QP",
                                        ScenarioStatistic.SUCCESS.and(ScenarioStatistic.OUTSIDE_QUIETPERIOD).and(d -> d.firstContentfulPaintEventLimit > 0),
                                        (s, f) -> s.pXX(95, f, d -> (long) d.firstContentfulPaintEvent),
                                        v -> v.isPresent() ? String.valueOf(v.getAsLong()) : "N/A"),
                        new Column<OptionalLong>("Max FCP ex QP",
                                        ScenarioStatistic.OUTSIDE_QUIETPERIOD.and(d -> d.firstContentfulPaintEventLimit > 0),
                                        (s, f) -> s.max(f, d -> (long) d.firstContentfulPaintEvent),
                                        v -> v.isPresent() ? String.valueOf(v.getAsLong()) : "N/A"),

                        // DomContentLoaded
                        new Column<OptionalDouble>("Avg DCL Success ex QP",
                                        ScenarioStatistic.SUCCESS.and(ScenarioStatistic.OUTSIDE_QUIETPERIOD).and(d -> d.domContentLoadedEventLimit > 0),
                                        (s, f) -> s.avg(f, d -> (long) d.domContentLoadedEvent),
                                        v -> v.isPresent() ? String.valueOf(Math.round(v.getAsDouble())) : "N/A"),
                        new Column<OptionalLong>("P95 DCL Success ex QP",
                                        ScenarioStatistic.SUCCESS.and(ScenarioStatistic.OUTSIDE_QUIETPERIOD).and(d -> d.domContentLoadedEventLimit > 0),
                                        (s, f) -> s.pXX(95, f, d -> (long) d.domContentLoadedEvent),
                                        v -> v.isPresent() ? String.valueOf(v.getAsLong()) : "N/A"),
                        new Column<OptionalLong>("Max DCL ex QP",
                                        ScenarioStatistic.OUTSIDE_QUIETPERIOD.and(d -> d.domContentLoadedEventLimit > 0),
                                        (s, f) -> s.max(f, d -> (long) d.domContentLoadedEvent),
                                        v -> v.isPresent() ? String.valueOf(v.getAsLong()) : "N/A"),

                        // loadevent
                        new Column<OptionalDouble>("Avg LoadEvent Success ex QP",
                                        ScenarioStatistic.SUCCESS.and(ScenarioStatistic.OUTSIDE_QUIETPERIOD).and(d -> d.loadEventLimit > 0),
                                        (s, f) -> s.avg(f, d -> (long) d.loadEvent),
                                        v -> v.isPresent() ? String.valueOf(Math.round(v.getAsDouble())) : "N/A"),
                        new Column<OptionalLong>("P95 LoadEvent Success ex QP",
                                        ScenarioStatistic.SUCCESS.and(ScenarioStatistic.OUTSIDE_QUIETPERIOD).and(d -> d.loadEventLimit > 0),
                                        (s, f) -> s.pXX(95, f, d -> (long) d.loadEvent),
                                        v -> v.isPresent() ? String.valueOf(v.getAsLong()) : "N/A"),
                        new Column<OptionalLong>("Max LoadEvent ex QP",
                                        ScenarioStatistic.OUTSIDE_QUIETPERIOD.and(d -> d.loadEventLimit > 0),
                                        (s, f) -> s.max(f, d -> (long) d.loadEvent),
                                        v -> v.isPresent() ? String.valueOf(v.getAsLong()) : "N/A")
                        );

        System.out.println();
        System.out.println("==== Final");

        // header
        System.out.println(cols.stream().map(d -> d.name).collect(Collectors.joining(",")));

        for (var s : buckets.entrySet().stream().map(e -> e.getValue()).sorted().toList())
        {
            for (var l : locations)
            {
                var joiner = new StringJoiner(",");

                // values
                for (var col : cols)
                {
                    joiner.add(col.apply(s, l));
                }
                System.out.println(joiner);
            }
        }
    }

    public static void main( String[] args )
    {
        int exitCode = new CommandLine(new Mondaev()).execute(args);
        System.exit(exitCode);
    }
}
