package org.example;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

/**
 * The Main class is responsible for running the program and generating a stock price chart.
 */
public class Main {

    /**
     * The main method is the entry point of the program and is responsible for calling other methods to fetch data and generate the chart.
     * @param args An array of command-line arguments passed to the program
     * @throws IOException If an error occurs when fetching data.
     */
    public static void main(String[] args) throws IOException {

        String apiKey = "MMURKQJ54TWWHVVP";
        String symbolName = "IBM";

        DataStreamHandle dataStreamHandle = new DataStreamHandle(
                apiKey,
                symbolName,
                DataStreamHandle.Interval.THIRTY_MIN,
                DataStreamHandle.OutputSize.FULL
        );

        // Get the median stock price data
        Map<LocalDate, Double> data = dataStreamHandle.getMedian();

        // Create a chart to display the stock price data
        DrawChart drawChart = new DrawChart(symbolName, data, "Stock Price Chart", "Date", "Price");

        // Show the chart
        drawChart.showChart("My Chart");
    }
}