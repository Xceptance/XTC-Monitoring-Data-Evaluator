package com.xceptance.xtc.mondaev.cells;

import java.util.function.Function;
import java.util.function.Predicate;

import com.xceptance.xtc.mondaev.Data;

/**
 * A base cell value concept
 */
public class Cell<T>
{
	public final String NOTAVAILABLE = "N/A";
	
    public final String columnName;
    final Predicate<Data> filter;
    final Function<Data, T> extractor;
    final Function<T, String> formatter;

    /**
     *
     * @param columnName the name of the col it belongs too
     * @param filter any filter to apply before extracting data
     * @param extractor which data to use
     * @param formatter how to format our result
     */
    public Cell(String columnName,
                    Predicate<Data> filter,
                    Function<Data, T> extractor,
                    Function<T, String> formatter)
    {
        this.columnName = columnName;
        this.filter = filter;
        this.extractor = extractor;
        this.formatter = formatter;
    }

	public String header()
	{
		return columnName;
	}

    /**
     * Takes a pieces of data and does stuff with it
     * @param row
     */
    public void process(final Data row)
    {

    }

    /**
     * Returns the resulting value formatted by the
     * formatter
     */
    public String value()
    {
    	return "N/A";
    }
}
