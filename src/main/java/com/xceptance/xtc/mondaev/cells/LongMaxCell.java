package com.xceptance.xtc.mondaev.cells;

import java.util.function.Function;
import java.util.function.Predicate;

import com.xceptance.xtc.mondaev.Data;

/**
 * Get us the max
 */
public class LongMaxCell extends Cell<Long>
{
	private long value = Long.MIN_VALUE;

	public LongMaxCell(
			String columnName,
			Predicate<Data> filter,
			Function<Data, Long> extractor,
			Function<Long, String> formatter)
	{
		super(columnName, filter, extractor, formatter);
	}


    /**
     * Takes a pieces of data and does stuff with it
     * @param row
     */
	@Override
    public void process(final Data row)
    {
		if (filter.test(row))
		{
			value = Math.max(value, extractor.apply(row));
		}
    }

	@Override
	public String value()
	{
		return value != Long.MIN_VALUE ? formatter.apply(value) : NOTAVAILABLE;
	}
}
