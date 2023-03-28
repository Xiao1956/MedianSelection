package org.example;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * DataStreamHandle class is responsible for requesting data from the Alpha Vantage API,
 * processing the data, calling the Median Selection class to obtain the relevant median,
 * and then processing the data into the format required by JFreeChart.
 */
public class DataStreamHandle {

    /**
     * These parameters to be used for the API call.
     */

    /**
     * Url of Alpha Vantage website
     * */
    private String apiPrefix = "https://www.alphavantage.co";

    /**
     * The time series chosen
     * */
    private String funcName = "TIME_SERIES_INTRADAY";

    /**
     * Initialise params
     * */
    private Map<String, String> params = new HashMap<>();

    /**
     * Time interval between two consecutive data points in the time series
     * */
    public static enum Interval {
        ONE_MIN("1min"),
        FIVE_MIN("5min") ,
        FIFTEEN_MIN("15min"),
        THIRTY_MIN("30min"),
        SIXTY_MIN("60min");

        private String interval;
        Interval(String interval) {
            this.interval = interval;
        }

        @Override
        public String toString() {
            return this.interval;
        }
    };

    /**
     * The data size of API call
     * */
    public static enum OutputSize {
        COMPACT("compact"),
        FULL("full");

        private String outputSize;
        OutputSize(String outputSize) {
            this.outputSize = outputSize;
        }

        @Override
        public String toString() {
            return this.outputSize;
        }
    }


    /**
     * Constructor for DataStreamHandle class
     * @param apiKey my API key used to access the Alpha Vantage API
     * @param symbolName the name of the stock selected
     * @param interval the time interval for the API call
     * @param outputSize the output size for the API call
     */
    public DataStreamHandle(String apiKey,  String symbolName, Interval interval, OutputSize outputSize) {
        this.params.put("function", this.funcName);
        this.params.put("symbol", symbolName);
        this.params.put("interval", interval.toString());
        this.params.put("outputsize", outputSize.toString());
        this.params.put("apikey", apiKey);
    }

    /**
     * Get the URL for the API call.
     * @return the URL for the API call
     */
    public String getUrl() {
        StringBuilder url = new StringBuilder();
        url.append(apiPrefix + "/query?");
        int index = 0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            url.append(entry.getKey());
            url.append("=");
            url.append(entry.getValue());
            if (index < params.entrySet().size() - 1) {
                url.append("&");
            }
            index++;
        }
        return url.toString();
    }

    /**
     * Get the response data as a JSON object
     * @return the JSON object representing the response
     * @throws IOException if there is an error connecting to the URL or reading the response
     */
    public JSONObject getRequestData() throws IOException {
        URL url = new URL(getUrl());
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Accept", "application/json");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
        String data;
        StringBuilder response = new StringBuilder();
        while ((data = bufferedReader.readLine()) != null) {
            response.append(data);
        }
        httpURLConnection.disconnect();

        JSONObject result = new JSONObject(response.toString());
        System.out.println("Response data: "+ result);
        return result;
    }

    /**
     * Transform the request data obtained from getRequestData() into a map of local dates and corresponding lists of closing prices.
     * @return the map of local dates and corresponding lists of closing prices
     * @throws IOException if there is an error connecting to the URL or reading the response
     */
    public  Map<LocalDate, List<Double>> transformData() throws IOException {
        JSONObject tmp = getRequestData();
        List<String> keys = new ArrayList<>(tmp.keySet());
        JSONObject result = tmp.getJSONObject(keys.get(0));

        Map<LocalDate, List<Double>> dataMap = new HashMap<>();
        for (String key : result.keySet()) {
            JSONObject data = result.getJSONObject(key);
            LocalDateTime dateTime = LocalDateTime.parse(key, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDate date = dateTime.toLocalDate();
            List<Double> dataList = dataMap.get(date);
            if (dataList == null) {
                dataList = new ArrayList<>();
                dataMap.put(date, dataList);
            }
            dataList.add(Double.parseDouble(data.getString("4. close")));
        }
        return dataMap;
    }


    /**
     * Get the computed median price for each date
     * @return the map of local dates and corresponding median prices
     * @throws IOException if there is an error connecting to the URL or reading the response
     */
    public Map<LocalDate, Double> getMedian() throws IOException {
        Map<LocalDate, List<Double>> dataMap = transformData();
        Map<LocalDate, Double> result = new HashMap<>();
        dataMap.forEach((date, dataList) -> {
            validate(dataList);
            MedianSelection medianSelection = new MedianSelection();
            for (Double value : dataList) {
                medianSelection.add(value);
            }
            result.put(date, medianSelection.getMedian());
        });
        System.out.println("Median: " + result);
        return result;
    }

    /**
     * Validate the input list to ensure it is not null or empty.
     * @param nums the list of numbers to validate
     * @throws IllegalArgumentException if the list is null or empty
     */
    public void validate( List<Double> nums) {
        if (nums == null || nums.size() == 0) {
            throw new IllegalArgumentException("Dataset is illegal");
        }
    }
}
