# XTC Monitoring Data Extractor

## Build
Just a `mvn verify` should do it.

## Run
`java -jar target/xtc-monitoring-data-evaluator-1.0-SNAPSHOT-jar-with-dependencies.jar` 

### Parameters

The filename has to be the extracted XTC monitoring CSV.

```
Usage: Mondaev [-hV] -f=<fileName>
  -f, --filename=<fileName>
                  The input file of a certain month of monitoring
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
```

## Requirements
Java 17

## License
APL 2.0
