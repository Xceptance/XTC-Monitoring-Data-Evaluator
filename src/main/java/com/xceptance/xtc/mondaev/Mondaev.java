package com.xceptance.xtc.mondaev;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.opencsv.bean.CsvToBeanBuilder;
import com.xceptance.xtc.mondaev.Database.Result;
import com.xceptance.xtc.mondaev.cells.Cell;
import com.xceptance.xtc.mondaev.cells.CountCell;
import com.xceptance.xtc.mondaev.cells.LongAvgCell;
import com.xceptance.xtc.mondaev.cells.LongMaxCell;
import com.xceptance.xtc.mondaev.cells.LongPXXCell;
import com.xceptance.xtc.mondaev.cells.StaticStringCell;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "Mondaev", version = "Mondaev 2.0", mixinStandardHelpOptions = true)
public class Mondaev implements Runnable
{
	private final Logger logger = Logger.getLogger(Mondaev.class.getName());

	@Option(names = { "-d", "--directory" }, required = true, description = "The input directory with monitoring files. All CSVs will be read.")
	String directory;

	@Override
	public void run()
	{
		// get us all files that are CSV in the directory, no traversal of dirs
		final BiPredicate<Path, BasicFileAttributes>  matcher = (path, attr) -> {
			final var b = attr.isRegularFile() && path.getFileName().toString().endsWith(".csv");
			return b;
		};

		final List<Path> files;
		try
		{
			files = Files.find(Path.of(directory), 1, matcher).toList();
		}
		catch (IOException e)
		{
			System.err.println("Something is wrong with the given directory: " + Path.of(directory));
			return;
		}

		// our data home
		final var database = new Database();

		// ok, we got all data, now we need to know the min and max date to later
		// figure out from which to which month we want to go
		LocalDateTime min = LocalDateTime.MAX;
		LocalDateTime max = LocalDateTime.MIN;

		// load file data
		for (var file : files)
		{
			try
			{
				final long start = System.currentTimeMillis();

				var rows = new CsvToBeanBuilder<Data>(new BufferedReader(new FileReader(file.toFile())))
						.withSeparator(',')
						.withQuoteChar('"')
						.withThrowExceptions(false)
						.withOrderedResults(true)
						.withType(Data.class).build().parse();

				// all into our list;
				int count = 0;
				for (var row : rows)
				{
					var startTime = row.startTime;

					min = min.compareTo(startTime) < 0 ? min : startTime;
					max = max.compareTo(startTime) > 0 ? max : startTime;

					database.add(row);
					count++;
				}

				System.out.format("Read %s with %d lines in %d ms.%n", file, count, System.currentTimeMillis() - start);
			}
			catch (Exception e)
			{
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}

		System.out.format("%n=== Analyzing from %s to %s%n", min, max);

		// all locations

		// all scenarios and locations
		final var locations = database.locations();
		final var scenarios = database.scenarios();

		System.out.println("=== Locations");
		locations.forEach(System.out::println);

		System.out.println("=== Scenarios");
		scenarios.forEach(System.out::println);

		// we spin over scenarios first and create two column for the locations
		for (String scenario : scenarios)
		{
			// now we need the locations
			for (String location : locations)
			{
				// ok, give us the prefiltered data set
				final var result = database.where(
						Database.byLocationFilter(location)
						.and(Database.byScenarioFilter(scenario)));

				// now, we have all things we can give it a try with
				// columns by year-month
				var current = min.truncatedTo(ChronoUnit.DAYS);
				int line = 0;
				while (current.compareTo(max) <= 0)
				{
					// ok, our date us the row key
					final var timeFilteredResult = result.where(
							Database.byTimeFilter(current.getYear(), current.getMonthValue()));

					// sweet, we have change what we need, so let's do the column magic
					processRow(
							scenario, location,
							String.format("%s%02d", current.getYear(), current.getMonthValue()),
							timeFilteredResult,
							line++ == 0);

					current = current.plusMonths(1);
				}

				System.out.println();
			}
		}
	}

	private void processRow(
			final String scenario, final String location,
			final String rowKey, final Result result, final boolean printHeader)
	{
		final Function<Long, String> LONGFORMATTER = l -> String.format("%d", l);
		final Function<Double, String> DOUBLEFORMATTER = l -> String.format("%.1f", l);

		final List<Cell<?>> columns = List.of(
				new StaticStringCell(
						"Year/Month",
						d -> true,
						d -> "",
						s -> rowKey
						),
				new StaticStringCell(
						"Scenario",
						d -> true,
						d -> "",
						s -> scenario
						),
				new StaticStringCell(
						"Location",
						d -> true,
						d -> "",
						s -> location
						),
				new CountCell(
						"Total",
						d -> true,
						d -> 1l,
						LONGFORMATTER
						),
				new CountCell(
						"Success",
						d -> d.success(),
						d -> 1l,
						LONGFORMATTER
						),
				new CountCell(
						"Failed",
						d -> d.failed(),
						d -> 1l,
						LONGFORMATTER
						),
				new CountCell(
						"Total ex QP",
						d ->  d.outsideQuietPeriod(),
						d -> 1l,
						LONGFORMATTER
						),
				new CountCell(
						"Success ex QP",
						d -> d.success() && d.outsideQuietPeriod(),
						d -> 1l,
						LONGFORMATTER
						),
				new CountCell(
						"Failed ex QP",
						d -> d.failed() && d.outsideQuietPeriod(),
						d -> 1l,
						LONGFORMATTER
						),
				// Runtime
				new LongAvgCell(
						"Avg Request Runtime Success ex QP",
						d -> d.success() && d.outsideQuietPeriod() && d.requestRuntimeLimit > 0,
						d -> (long) d.requestRuntime,
						LONGFORMATTER
						),
				new LongPXXCell(
						"P95 Request Runtime Success ex QP",
						d -> d.success() && d.outsideQuietPeriod() && d.requestRuntimeLimit > 0,
						d -> (long) d.requestRuntime,
						LONGFORMATTER,
						95
						),
				new LongMaxCell(
						"Max Request Runtime Success ex QP",
						d -> d.success() && d.outsideQuietPeriod() && d.requestRuntimeLimit > 0,
						d -> (long) d.requestRuntime,
						LONGFORMATTER
						),
				new LongMaxCell(
						"Max Request Runtime Failed ex QP",
						d -> d.failed() && d.outsideQuietPeriod() && d.requestRuntimeLimit > 0,
						d -> (long) d.requestRuntime,
						LONGFORMATTER
						),

				// FCP
				new LongAvgCell(
						"Avg FCP ex QP",
						d -> d.success() && d.outsideQuietPeriod() && d.firstContentfulPaintEventLimit > 0,
						d -> (long) d.firstContentfulPaintEvent,
						LONGFORMATTER
						),
				new LongPXXCell(
						"P95 FCP ex QP",
						d -> d.success() && d.outsideQuietPeriod() && d.firstContentfulPaintEventLimit > 0,
						d -> (long) d.firstContentfulPaintEvent,
						LONGFORMATTER,
						95
						),
				new LongMaxCell(
						"Max FCP ex QP",
						d -> d.success() && d.outsideQuietPeriod() && d.firstContentfulPaintEventLimit > 0,
						d -> (long) d.firstContentfulPaintEvent,
						LONGFORMATTER
						),
				new LongMaxCell(
						"Max FCP ex QP",
						d -> d.failed() && d.outsideQuietPeriod() && d.firstContentfulPaintEventLimit > 0,
						d -> (long) d.firstContentfulPaintEvent,
						LONGFORMATTER
						),
				// LCP
				new LongAvgCell(
						"Avg LCP Success ex QP",
						d -> d.success() && d.outsideQuietPeriod() && d.lcpLimit > 0,
						d -> (long) d.lcp,
						LONGFORMATTER
						),
				new LongPXXCell(
						"P95 LCP ex QP",
						d -> d.success() && d.outsideQuietPeriod() && d.lcpLimit > 0,
						d -> (long) d.lcp,
						LONGFORMATTER,
						95
						),
				new LongMaxCell(
						"Max LCP ex QP",
						d -> d.success() && d.outsideQuietPeriod() && d.lcpLimit > 0,
						d -> (long) d.lcp,
						LONGFORMATTER
						),
				new LongMaxCell(
						"Max LCP Failed ex QP",
						d -> d.failed() && d.outsideQuietPeriod() && d.lcpLimit > 0,
						d -> (long) d.lcp,
						LONGFORMATTER
						),

				// TTFB
				new LongAvgCell(
						"Avg TTFB Success ex QP",
						d -> d.success() && d.outsideQuietPeriod() && d.ttfbLimit > 0,
						d -> (long) d.ttfb,
						LONGFORMATTER
						),
				new LongPXXCell(
						"P95 TTFB ex QP",
						d -> d.success() && d.outsideQuietPeriod() && d.ttfbLimit > 0,
						d -> (long) d.ttfb,
						LONGFORMATTER,
						95
						),
				new LongMaxCell(
						"Max TTFB ex QP",
						d -> d.success() && d.outsideQuietPeriod() && d.ttfbLimit > 0,
						d -> (long) d.ttfb,
						LONGFORMATTER
						),
				new LongMaxCell(
						"Max TTFB Failed ex QP",
						d -> d.failed() && d.outsideQuietPeriod() && d.ttfbLimit > 0,
						d -> (long) d.ttfb,
						LONGFORMATTER
						)
				);

		for (var row : result.data())
		{
			for (var column : columns)
			{
				column.process(row);
			}
		}

		// header
		if (printHeader)
		{
			System.out.println(columns.stream().map(Cell::header).collect(Collectors.joining(",")));
		}

		// values
		System.out.println(columns.stream().map(Cell::value).collect(Collectors.joining(",")));

		//		for (var s : buckets.entrySet().stream().map(e -> e.getValue()).sorted().toList())
		//		{
		//			for (var l : locations)
		//			{
		//				var joiner = new StringJoiner(",");
		//
		//				// values
		//				for (var col : cols)
		//				{
		//					joiner.add(col.apply(s, l));
		//				}
		//				System.out.println(joiner);
		//			}
		//		}
	}

	public static void main( String[] args )
	{
		int exitCode = new CommandLine(new Mondaev()).execute(args);
		System.exit(exitCode);
	}
}
