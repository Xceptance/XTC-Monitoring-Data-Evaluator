package com.xceptance.xtc.mondaev;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A statistic value on Data
 * @author rschwietzke
 *
 */
public class Column<T> implements BiFunction<ScenarioStatistic, String, String>
{
    public final String name;
    private final Predicate<Data> filter;
    private final BiFunction<ScenarioStatistic, Predicate<Data>, T> aggregator;
    private final Function<T, String> formatter;

    public Column(String name,
                    Predicate<Data> filter,
                    BiFunction<ScenarioStatistic, Predicate<Data>, T> aggregator,
                    Function<T, String> formatter)
    {
        this.name = name;
        this.filter = filter;
        this.aggregator = aggregator;
        this.formatter = formatter;
    }

    @Override
    public String apply(ScenarioStatistic s, String location)
    {
        return formatter.apply(
                        aggregator.apply(
                        s,
                        filter.and(d -> d.location.equals(location))));
    }
}
