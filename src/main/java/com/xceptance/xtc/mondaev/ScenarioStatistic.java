package com.xceptance.xtc.mondaev;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.util.function.Function;
import java.util.function.Predicate;

public class ScenarioStatistic implements Comparable<ScenarioStatistic>
{
    public static Predicate<Data> SUCCESS = d -> d.success();
    public static Predicate<Data> FAILED = d -> d.failed();
    public static Predicate<Data> OUTSIDE_QUIETPERIOD = d -> !d.withinQuietPeriod();

    private String scenario;

    private List<Data> data = new ArrayList<>();

    public ScenarioStatistic(final String scenario)
    {
        this.scenario = scenario;
    }

    public ScenarioStatistic add(Data d)
    {
        data.add(d);
        return this;
    }

    /**
     * Return the scenario name
     */
    public String scenario()
    {
        return scenario;
    }

    /**
     * Return the scenario name
     */
    public String location(Predicate<Data> filter)
    {
        return data.stream().filter(filter).map(d -> d.location).findFirst().orElse("N/A");
    }

    public List<String> entries(Predicate<Data> filter, Function<Data, String> supplier)
    {
        return data.stream().filter(filter).map(supplier).toList();
    }

    /**
     * Total entries
     */
    public long count(Predicate<Data> filter)
    {
        return data.stream().filter(filter).count();
    }

    /**
     * Avg
     */
    public OptionalDouble avg(Predicate<Data> filter, Function<Data, Double> supplier)
    {
        return data.stream().filter(filter).map(supplier).mapToLong(d -> (long) d.doubleValue()).average();
    }

    /**
     * Max
     */
    public OptionalLong max(Predicate<Data> filter, Function<Data, Double> supplier)
    {
        return data.stream().filter(filter).map(supplier).mapToLong(d -> (long) d.doubleValue()).max();
    }

    /**
     * PXX
     */
    public OptionalLong pXX(int percentile, Predicate<Data> filter, Function<Data, Double> supplier)
    {
        var l = data.stream().filter(filter).map(supplier).map(d -> (long) d.doubleValue()).sorted().toList();
        final int pos = (int)(l.size() * ((double)(percentile/100)));

        return l.size() > 0 ? OptionalLong.of(l.get(pos)) : OptionalLong.empty();
    }

    @Override
    public int compareTo(ScenarioStatistic o)
    {
        return this.scenario.compareTo(o.scenario);
    }
}
