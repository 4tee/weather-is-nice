package com.felixmm.niceweather.http;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class FetchWeatherFromOWM {

    public static final String TAG = FetchWeatherFromOWM.class.getSimpleName();


    private static String formatHighLows(double high, double low) {
        return high + "-" + low + "C";
    }

    private static String[] getDailyWeatherDataFromJson(String jsonStr, int noOfDays)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String JSON_LIST = "list";
        final String JSON_HUMIDITY = "humidity";
        final String JSON_WEATHER = "weather";
        final String JSON_TEMPERATURE = "temp";
        final String JSON_MAX = "max";
        final String JSON_MIN = "min";
        final String JSON_DESCRIPTION = "description";
        final String JSON_DATETIME = "dt";

        JSONObject forecastJson = new JSONObject(jsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(JSON_LIST);

        String[] resultStrs = new String[noOfDays];
        for(int i = 0; i < weatherArray.length(); i++) {
            //our format = "Wednesday; few clouds; Temp:29.03C-31.71C; Hum:84%",
            String day;
            String description;
            String highAndLow;
            String humidity;

            // Get the JSON object representing the day
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            // open weather map reports the date as a unix timestamp (seconds)
            // convert it to milliseconds to convert to a Date object
            long dt = dayForecast.getLong(JSON_DATETIME) * 1000;
            Date date = new Date(dt);
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE",Locale.getDefault());
            day = formatter.format(date);

            int humidityInt = dayForecast.getInt(JSON_HUMIDITY);
            humidity = Integer.toString(humidityInt);

            // description is in a child array called "weather", which is 1 element long.
            JSONObject weatherObject = dayForecast.getJSONArray(JSON_WEATHER).getJSONObject(0);
            description = weatherObject.getString(JSON_DESCRIPTION);

            // Temperatures are in a child object called "temp".  Try not to name variables
            // "temp" when working with temperature.  It confuses everybody.
            JSONObject temperatureObject = dayForecast.getJSONObject(JSON_TEMPERATURE);
            double high = temperatureObject.getDouble(JSON_MAX);
            double low = temperatureObject.getDouble(JSON_MIN);

            highAndLow = formatHighLows(high, low);
            //"Wednesday; few clouds; Temp:29.03C-31.71C; Hum:84%",
            resultStrs[i] = day + "; " + description + "; Temp:" + highAndLow + "; Hum:" + humidity;
        }

        for (String s : resultStrs) {
            Log.v(TAG, s);
        }

        return resultStrs;

    }

    public static String[] getDailyWeatherJson(int noOfDay, double latitude, double longitude) {

        final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
        final String LAT_PARAM = "lat";
        final String LON_PARAM = "lon";
        final String APPID_PARAM = "appid";
        final String UNITS_PARAM = "units";
        final String DAYS_PARAM = "cnt";

        Uri neaUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(LAT_PARAM, Double.toString(latitude))
                .appendQueryParameter(LON_PARAM, Double.toString(longitude))
                .appendQueryParameter(DAYS_PARAM, Integer.toString(noOfDay))
                .appendQueryParameter(APPID_PARAM, "fac739a8dc93fb7d0d3207342873166f")
                .appendQueryParameter(UNITS_PARAM, "metric")
                .build();
        Log.d(TAG, neaUri.toString());

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String jsonResponseStr = null;

        try {
            // Construct the URL for the query
            URL url = new URL(neaUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                Log.d(TAG, "inputStream is null");
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // Copy the line into the string buffer
                buffer.append(line);
            }

            if (buffer.length() == 0) {
                // Stream was empty.
                Log.d(TAG, "inputStream is empty and nothing to parse.");
                return null;
            }
            jsonResponseStr = buffer.toString();

        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
        } finally{
            // Even if something terrible happen, just wrap things nicely.
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }


        // parse the result into Json object
        try {
            return getDailyWeatherDataFromJson(jsonResponseStr, 5);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }
}
