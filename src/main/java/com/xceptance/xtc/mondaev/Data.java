package com.xceptance.xtc.mondaev;

import java.time.LocalDateTime;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

public class Data
{
    // we might get ints as double such as 4564.0
    private static final String DEFAULT_LOCALE = "en-US";

    // 0 Start Time
    @CsvBindByName(column = "Start Time", required = true)
    @CsvDate("yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]'Z'")
    public LocalDateTime startTime;

    // Duration
    @CsvBindByName(column = "Duration", required = true)
    public int duration;

    // Scenario
    @CsvBindByName(column = "Scenario", required = true)
    private String scenario;
    // our final presentation in uppercase
    private String scenarioNormalized;

    public String scenario()
    {
    	if (this.scenarioNormalized == null)
    	{
    		this.scenarioNormalized = scenario.trim().toUpperCase();
    	}
    	return this.scenarioNormalized;
    }

    // Executor Location
    @CsvBindByName(column = "Executor Location", required = true)
    public String location;

    // Result
    @CsvBindByName(column = "Result")
    private String result;
    public boolean success()
    {
        return result.equalsIgnoreCase("Success");
    }
    public boolean failed()
    {
        return !success();
    }

    // Within Quiet Period
    @CsvBindByName(column = "Within Quiet Period")
    private String withinQuietPeriod;
    public boolean withinQuietPeriod()
    {
        return withinQuietPeriod.equalsIgnoreCase("Yes");
    }
    public boolean outsideQuietPeriod()
    {
        return !withinQuietPeriod();
    }

    // Reason
    @CsvBindByName(column = "Reason")
    public String failureReason;

    // Failed Action Name
    @CsvBindByName(column = "Failed Action Name")
    public String failedAction;

    // Failure Message
    @CsvBindByName(column = "Failure Message")
    public String failureMessage;

    // Action Runtime Limit
    @CsvBindByName(column = "Action Runtime Limit", locale = DEFAULT_LOCALE)
    public int actionRuntimeLimit = -1;

    // Action Runtime Value
    @CsvBindByName(column = "Action Runtime Value", locale = DEFAULT_LOCALE)
    public int actionRuntime;

    // Transaction Runtime Limit
    @CsvBindByName(column = "Transaction Runtime Limit", locale = DEFAULT_LOCALE)
    public int transactionRuntimeLimit = -1;

    // Transaction Runtime Value
    @CsvBindByName(column = "Transaction Runtime Value", locale = DEFAULT_LOCALE)
    public int transactionRuntime;

    // DOMContentLoaded Event Limit
    @CsvBindByName(column = "DOMContentLoaded Event Limit", locale = DEFAULT_LOCALE)
    public int domContentLoadedEventLimit = -1;

    // DOMContentLoaded Event Value
    @CsvBindByName(column = "DOMContentLoaded Event Value", locale = DEFAULT_LOCALE)
    public int domContentLoadedEvent;

    // Load Event Limit
    @CsvBindByName(column = "Load Event Limit", locale = DEFAULT_LOCALE)
    public int loadEventLimit = -1;

    // Load Event Value
    @CsvBindByName(column = "Load Event Value", locale = DEFAULT_LOCALE)
    public int loadEvent;

    // FirstPaint Event Limit
    @CsvBindByName(column = "FirstPaint Event Limit", locale = DEFAULT_LOCALE)
    public int firstPaintEventLimit = -1;

    // FirstPaint Event Value
    @CsvBindByName(column = "FirstPaint Event Value", locale = DEFAULT_LOCALE)
    public int firstPaintEvent;

    // FirstContentfulPaint Event Limit
    @CsvBindByName(column = "FirstContentfulPaint Event Limit", locale = DEFAULT_LOCALE)
    public int firstContentfulPaintEventLimit = -1;

    // FirstContentfulPaint Event Value
    @CsvBindByName(column = "FirstContentfulPaint Event Value", locale = DEFAULT_LOCALE)
    public int firstContentfulPaintEvent;

    // Request Runtime Limit
    @CsvBindByName(column = "Request Runtime Limit", locale = DEFAULT_LOCALE)
    public int requestRuntimeLimit = -1;

    // Request Runtime Value
    @CsvBindByName(column = "Request Runtime Value", locale = DEFAULT_LOCALE)
    public int requestRuntime;

    // Request Errors Limit
    @CsvBindByName(column = "Request Errors Limit", locale = DEFAULT_LOCALE)
    public int requestErrorsLimit = -1;

    // Request Errors Value
    @CsvBindByName(column = "Request Errors Value", locale = DEFAULT_LOCALE)
    public int requestErrors;

    //
    @CsvBindByName(column = "Cumulative Layout Shift Limit", locale = DEFAULT_LOCALE)
    public double clsLimit = -1;

    //
    @CsvBindByName(column = "Cumulative Layout Shift Value", locale = DEFAULT_LOCALE)
    public double cls = -1;

    //
    @CsvBindByName(column = "First Contentful Paint Limit", locale = DEFAULT_LOCALE)
    public int fcpLimit = -1;

    //
    @CsvBindByName(column = "First Contentful Paint Value", locale = DEFAULT_LOCALE)
    public int fcp = -1;

    //
    @CsvBindByName(column = "First Input Delay Limit", locale = DEFAULT_LOCALE)
    public int fidLimit = -1;

    //
    @CsvBindByName(column = "First Input Delay Value", locale = DEFAULT_LOCALE)
    public int fid = -1;

    //
    @CsvBindByName(column = "Interaction to Next Paint Limit", locale = DEFAULT_LOCALE)
    public int inpLimit = -1;

    //
    @CsvBindByName(column = "Interaction to Next Paint Value", locale = DEFAULT_LOCALE)
    public int inp = -1;

    //
    @CsvBindByName(column = "Largest Contentful Paint Limit", locale = DEFAULT_LOCALE)
    public int lcpLimit = -1;

    //
    @CsvBindByName(column = "Largest Contentful Paint Value", locale = DEFAULT_LOCALE)
    public int lcp = -1;

    //
    @CsvBindByName(column = "Time to First Byte Limit", locale = DEFAULT_LOCALE)
    public int ttfbLimit = -1;

    //
    @CsvBindByName(column = "Time to First Byte Value", locale = DEFAULT_LOCALE)
    public int ttfb = -1;

}
