import org.example.DataStreamHandle;
import org.example.DrawChart;
import org.example.MedianSelection;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 Some tests for this project
 */
class MedianSelectionTest {

    /**
     * My API key used to access the Alpha Vantage API.
     */
    private final String apiKey = "MMURKQJ54TWWHVVP";

    /**
     * The name of the equity selected
     */
    private final String symbolName = "IBM";

    /**
     * Declare DataStreamHandle
     */
    private DataStreamHandle dataStreamHandle;

    /**
     * Set up dataStreamHandle for reuse in test cases.
     */
    @BeforeEach
    void setUp() {
        dataStreamHandle = new DataStreamHandle(apiKey, symbolName, DataStreamHandle.Interval.SIXTY_MIN, DataStreamHandle.OutputSize.COMPACT);
    }

    /**
     * Test the add() and balance() methods of the MedianSelection class by adding elements and checking the size of the
     * min and max heaps.
     */
    @Test
    public void testAddAndBalance() {
        MedianSelection medianSelection = new MedianSelection();
        medianSelection.add(5.0);
        medianSelection.add(10.0);
        medianSelection.add(15.0);
        medianSelection.add(20.0);
        medianSelection.add(25.0);
        assertEquals(3, medianSelection.getMinHeap().size());
        assertEquals(2, medianSelection.getMaxHeap().size());
    }

    /**
     * Test the getMedian() method of the MedianSelection class by adding elements and checking the returned median value.
     */
    @Test
    public void testGetMedianCase1() {
        MedianSelection medianSelection = new MedianSelection();

        medianSelection.add(5.0);
        double median = medianSelection.getMedian();
        assertEquals(5.0, median);

        medianSelection.add(10.0);
        median = medianSelection.getMedian();
        assertEquals(7.5, median);

        medianSelection.add(15.0);
        median = medianSelection.getMedian();
        assertEquals(10.0, median);

        medianSelection.add(20.0);
        median = medianSelection.getMedian();
        assertEquals(12.5, median);

        medianSelection.add(25.0);
        median = medianSelection.getMedian();
        assertEquals(15.0, median);
    }

    /**
     * Test the getMedian() method of the MedianSelection class with NaN value and checks if the result is NaN.
     */
    @Test
    public void testGetMedianCase2() {
        MedianSelection ms = new MedianSelection();
        ms.add(Double.NaN);
        assertTrue(Double.isNaN(ms.getMedian()));
    }

    /**
     * Test the getUrl() method of the DataStreamHandle class by checking if the constructed url is correct.
     */
    @Test
    void testGetUrl() {
        String expectedUrl = "https://www.alphavantage.co/query?symbol=" +
                symbolName + "&outputsize=compact&apikey=" +
                apiKey + "&function=TIME_SERIES_INTRADAY&interval=60min";
        assertEquals(expectedUrl, dataStreamHandle.getUrl());
    }

    /**
     * Test the getRequestData() method of the DataStreamHandle class by checking if the result is not null and has the desired data.
     */
    @Test
    void testGetRequestDate() throws IOException {
        JSONObject result = dataStreamHandle.getRequestData();
        assertNotNull(result);

        List<String> keys = new ArrayList<>(result.keySet());
        result = result.getJSONObject(keys.get(0));
        assertTrue(result.length() > 0);
    }

    /**
     * Test transformData() method of the DataStreamHandle class by checking the data stream by whether the data has been transformed into a map.
     * @throws IOException if there is an error connecting to the URL or reading the response
     */
    @Test
    void testTransformData() throws IOException {
        Map<LocalDate, List<Double>> dataMap = dataStreamHandle.transformData();
        assertNotNull(dataMap);
        assertFalse(dataMap.isEmpty());
    }

    /**
     * Test getMedian() method of the DataStreamHandle class by checking the median value
     * @throws IOException if there is an error connecting to the URL or reading the response
     */
    @Test
    void testGetMedianInStream() throws IOException {
        Map<LocalDate, Double> result = dataStreamHandle.getMedian();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    /**
     * Test validate() method of the DataStreamHandle class by checking whether an exception is thrown
     */
    @Test
    void testValidate() {
        List<Double> emptyList = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, ()-> dataStreamHandle.validate(emptyList));
    }

    /**
     * Test init() method of the DrawChart class by checking if it draws chart successfully
     */
    @Test
    void testInit() {
        // Sample chart for testing
        Map<LocalDate, Double> data = new HashMap<>();
        data.put(LocalDate.of(2023, 3, 1), 10.0);
        data.put(LocalDate.of(2023, 3, 2), 15.0);
        data.put(LocalDate.of(2023, 3, 3), 20.0);
        data.put(LocalDate.of(2023, 3, 4), 25.0);
        data.put(LocalDate.of(2023, 3, 5), 30.0);
        DrawChart drawChart = new DrawChart(symbolName, data, "Test Title", "Test X Label", "Test Y Label");
        assertNotNull(drawChart);
    }

    /**
     * Test whether a DrawChart object can be initialized with null data.
     */
    @Test
    void testNullDataForChart() {
        assertThrows(NullPointerException.class, () -> new DrawChart(symbolName, null, "Test Title", "Test X Label", "Test Y Label"));
    }

}