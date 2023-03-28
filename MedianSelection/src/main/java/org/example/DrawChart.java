package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

/**
 * Draw and display a time series chart
 */
public class DrawChart {

    /**
     * Declare jFreeChart
     */
    private JFreeChart jFreeChart;

    /**
     * Constructor for DrawChart class
     * Construct a new DrawChart object with the specified name, data, title, xLabel, and yLabel.
     * @param name the name of the time series.
     * @param data the data as a map of LocalDate keys and Double values.
     * @param title the title of the chart.
     * @param xLabel the label for the X-axis.
     * @param yLabel the label for the Y-axis.
     */
    public DrawChart(String name, Map<LocalDate, Double> data, String title, String xLabel, String yLabel) {
        this.jFreeChart = init(name, data, title, xLabel, yLabel);
    }

    /**
     * Initialize and returns a new JFreeChart object
     * @param name the name of the time series.
     * @param data the data as a map of LocalDate keys and Double values.
     * @param title the title of the chart.
     * @param xLabel the label for the X-axis.
     * @param yLabel the label for the Y-axis.
     * @return a new JFreeChart object.
     */
    private JFreeChart init(String name, Map<LocalDate, Double> data, String title, String xLabel, String yLabel) {
        TimeSeries timeSeries = new TimeSeries(name);
        data.forEach((date, median) -> {
            timeSeries.add(new Day(date.getDayOfMonth(), date.getMonthValue(), date.getYear()), median);
        });
        TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
        timeSeriesCollection.addSeries(timeSeries);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                title,
                xLabel,
                yLabel,
                timeSeriesCollection
        );

        XYPlot plot = chart.getXYPlot();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
        DateAxis xAxis = (DateAxis) plot.getDomainAxis();
        xAxis.setDateFormatOverride(dateFormat);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.decode("#f55905"));
        renderer.setDefaultShapesVisible(true);
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);
        chart.getLegend().setFrame(BlockBorder.NONE);

        return chart;
    }

    /**
     * Display the chart in a new frame
     * @param frameTitle the title of the chart frame.
     */
    public void showChart(String frameTitle ) {
        ChartFrame frame = new ChartFrame(frameTitle, jFreeChart);
        frame.pack();
        frame.setVisible(true);
    }
}
