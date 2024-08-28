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
    public String scenario;

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
    public int actionRuntimeLimit;

    // Action Runtime Value
    @CsvBindByName(column = "Action Runtime Value", locale = DEFAULT_LOCALE)
    public int actionRuntime;

    // Transaction Runtime Limit
    @CsvBindByName(column = "Transaction Runtime Limit", locale = DEFAULT_LOCALE)
    public int transactionRuntimeLimit;

    // Transaction Runtime Value
    @CsvBindByName(column = "Transaction Runtime Value", locale = DEFAULT_LOCALE)
    public int transactionRuntime;

    // DOMContentLoaded Event Limit
    @CsvBindByName(column = "DOMContentLoaded Event Limit", locale = DEFAULT_LOCALE)
    public int domContentLoadedEventLimit;

    // DOMContentLoaded Event Value
    @CsvBindByName(column = "DOMContentLoaded Event Value", locale = DEFAULT_LOCALE)
    public int domContentLoadedEvent;

    // Load Event Limit
    @CsvBindByName(column = "Load Event Limit", locale = DEFAULT_LOCALE)
    public int loadEventLimit;

    // Load Event Value
    @CsvBindByName(column = "Load Event Value", locale = DEFAULT_LOCALE)
    public int loadEvent;

    // FirstPaint Event Limit
    @CsvBindByName(column = "FirstPaint Event Limit", locale = DEFAULT_LOCALE)
    public int firstPaintEventLimit;

    // FirstPaint Event Value
    @CsvBindByName(column = "FirstPaint Event Value", locale = DEFAULT_LOCALE)
    public int firstPaintEvent;

    // FirstContentfulPaint Event Limit
    @CsvBindByName(column = "FirstContentfulPaint Event Limit", locale = DEFAULT_LOCALE)
    public int firstContentfulPaintEventLimit;

    // FirstContentfulPaint Event Value
    @CsvBindByName(column = "FirstContentfulPaint Event Value", locale = DEFAULT_LOCALE)
    public int firstContentfulPaintEvent;

    // Request Runtime Limit
    @CsvBindByName(column = "Request Runtime Limit", locale = DEFAULT_LOCALE)
    public int requestRuntimeLimit;

    // Request Runtime Value
    @CsvBindByName(column = "Request Runtime Value", locale = DEFAULT_LOCALE)
    public int requestRuntime;

    // Request Errors Limit
    @CsvBindByName(column = "Request Errors Limit", locale = DEFAULT_LOCALE)
    public int requestErrorsLimit;

    // Request Errors Value
    @CsvBindByName(column = "Request Errors Value", locale = DEFAULT_LOCALE)
    public int requestErrors;
}
