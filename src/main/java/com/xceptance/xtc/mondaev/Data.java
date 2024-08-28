package com.xceptance.xtc.mondaev;

import java.time.LocalDateTime;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

public class Data
{
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
    @CsvBindByName(column = "Action Runtime Limit")
    public double actionRuntimeLimit;

    // Action Runtime Value
    @CsvBindByName(column = "Action Runtime Value")
    public double actionRuntime;

    // Transaction Runtime Limit
    @CsvBindByName(column = "Transaction Runtime Limit")
    public double transactionRuntimeLimit;

    // Transaction Runtime Value
    @CsvBindByName(column = "Transaction Runtime Value")
    public double transactionRuntime;

    // DOMContentLoaded Event Limit
    @CsvBindByName(column = "DOMContentLoaded Event Limit")
    public double domContentLoadedEventLimit;

    // DOMContentLoaded Event Value
    @CsvBindByName(column = "DOMContentLoaded Event Value")
    public double domContentLoadedEvent;

    // Load Event Limit
    @CsvBindByName(column = "Load Event Limit")
    public double loadEventLimit;

    // Load Event Value
    @CsvBindByName(column = "Load Event Value")
    public double loadEvent;

    // FirstPaint Event Limit
    @CsvBindByName(column = "FirstPaint Event Limit")
    public double firstPaintEventLimit;

    // FirstPaint Event Value
    @CsvBindByName(column = "FirstPaint Event Value")
    public double firstPaintEvent;

    // FirstContentfulPaint Event Limit
    @CsvBindByName(column = "FirstContentfulPaint Event Limit")
    public double firstContentfulPaintEventLimit;

    // FirstContentfulPaint Event Value
    @CsvBindByName(column = "FirstContentfulPaint Event Value")
    public double firstContentfulPaintEvent;

    // Request Runtime Limit
    @CsvBindByName(column = "Request Runtime Limit")
    public double requestRuntimeLimit;

    // Request Runtime Value
    @CsvBindByName(column = "Request Runtime Value")
    public double requestRuntime;

    // Request Errors Limit
    @CsvBindByName(column = "Request Errors Limit")
    public double requestErrorsLimit;

    // Request Errors Value
    @CsvBindByName(column = "Request Errors Value")
    public double requestErrors;
}
