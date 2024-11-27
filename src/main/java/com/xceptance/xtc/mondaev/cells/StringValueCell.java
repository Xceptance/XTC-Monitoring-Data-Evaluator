package com.xceptance.xtc.mondaev.cells;

import java.util.function.Function;
import java.util.function.Predicate;

import com.xceptance.xtc.mondaev.Data;

/**
 * Just a single value that stays unchanged, will complain if not
 */
public class StringValueCell extends Cell<String>
{
	private String value;

	public StringValueCell(
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
		if (filter.test(row))
		{
			final var v = extractor.apply(row);
			if (this.value == null)
			{
				this.value = v;
			}
			else
			{
				// same?
				if (!this.value.equals(v))
				{
					throw new RuntimeException(
							String.format("Value of %s changed from %s tpo %s",
									this.columnName, this.value, v));
				}
			}
		}
    }

	@Override
	public String value()
	{
		return formatter.apply(this.value);
	}
}
