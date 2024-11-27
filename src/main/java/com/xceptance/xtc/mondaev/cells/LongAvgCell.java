package com.xceptance.xtc.mondaev.cells;

import java.util.function.Function;
import java.util.function.Predicate;

import com.xceptance.xtc.mondaev.Data;

/**
 * We are counting what we get from the extractor
 */
public class LongAvgCell extends Cell<Long>
{
	private long total;
	private long count;

	public LongAvgCell(
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
			final var v = extractor.apply(row);
			total += v;
			count++;
		}
    }

	@Override
	public String value()
	{
		return count > 0 ? formatter.apply(this.total / count) : NOTAVAILABLE;
	}
}
