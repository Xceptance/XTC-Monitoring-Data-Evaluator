package com.xceptance.xtc.mondaev.cells;

import java.util.function.Function;
import java.util.function.Predicate;

import com.xceptance.xtc.mondaev.Data;

/**
 * That is an static cell that just executes its formatter without
 * any data
 */
public class StaticStringCell extends Cell<String>
{
	private String value;

	public StaticStringCell(
			String columnName,
			Predicate<Data> filter,
			Function<Data, String> extractor,
			Function<String, String> formatter)
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
    }

	@Override
	public String value()
	{
		return formatter.apply("");
	}
}
