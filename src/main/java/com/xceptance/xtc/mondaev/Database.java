package com.xceptance.xtc.mondaev;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;
import java.util.function.Function;
import java.util.function.Predicate;

public class Database
{
	private final List<Data> data = new ArrayList<>(100_000);

	public Predicate<Data> dateSelector(final int year, final int month)
	{
		return d -> d.startTime.getMonthValue() == month && d.startTime.getYear() == year;
	}

	/**
	 * Add a new data block
	 */
	public void add(final Data d)
	{
		data.add(d);
	}

	/**
	 * Return the distinct locations
	 */
	public List<String> locations()
	{
		return data.stream().map(d -> d.location).sorted().distinct().toList();
	}

	/**
	 * Return the distinct scenarios
	 */
	public List<String> scenarios()
	{
		return data.stream().map(d -> d.scenario()).sorted().distinct().toList();
	}

	/**
	 * Returns a preselected result we can iterate over to refine things
	 */
	public Result where(final Predicate<Data> condition)
	{
		return new Result(data.stream().filter(condition).toList());
	}

	public static Predicate<Data> byLocationFilter(final String location)
	{
		return d -> d.location.equals(location);
	}

	public static Predicate<Data> byScenarioFilter(final String scenario)
	{
		return d -> d.scenario().equals(scenario);
	}

	public static Predicate<Data> byTimeFilter(final int year, final int month)
	{
		return d -> d.startTime.getMonthValue() == month && d.startTime.getYear() == year;
	}

	/**
	 * PXX
	 */
	public OptionalLong pXX(int percentile, Predicate<Data> filter, Function<Data, Long> supplier)
	{
		var l = data.stream().filter(filter).map(supplier).sorted().toList();
		final int pos = (int)((double)l.size() * (percentile/100.0));

		return l.size() > 0 ? OptionalLong.of(l.get(pos)) : OptionalLong.empty();
	}

	public record Result(List<Data> data)
	{
		public Result where(final Predicate<Data> condition)
		{
			return new Result(data.stream().filter(condition).toList());
		}
	}
}
