package com.xceptance.xtc.mondaev.cells;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import com.xceptance.xtc.mondaev.Data;

/**
 * Ok, get us the PXX value
 */
public class LongPXXCell extends Cell<Long>
{
	private List<Long> values = new ArrayList<>(1024);
	private final int pXX;

	public LongPXXCell(
			String columnName,
			Predicate<Data> filter,
			Function<Data, Long> extractor,
			Function<Long, String> formatter,
			final int pXX)
	{
		super(columnName, filter, extractor, formatter);
		this.pXX = pXX;
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
			values.add(v);
		}
    }

	@Override
	public String value()
	{
		// sort and get us the value number x
		Collections.sort(values);

		if (values.size() > 0)
		{
			final var pos = values.size() / 100 * pXX;
			return formatter.apply(values.get(pos));
		}
		else
		{
			return NOTAVAILABLE;
		}
	}
}
