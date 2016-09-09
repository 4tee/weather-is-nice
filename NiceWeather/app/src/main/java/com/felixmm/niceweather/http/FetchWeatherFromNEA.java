package com.felixmm.niceweather.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class FetchWeatherFromNEA {

    public static final String TAG = "FetchWeatherFromNEA";

    public static void fetch2HoursNowCast() {

        String baseUrl = "http://api.nea.gov.sg/api/WebAPI/";
        String dataset_para = "?dataset=2hr_nowcast";
        String key_para = "&keyref=781CF461BB6606AD1260F4D81345157FFB61302874DE827F";

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String xmlResponseBody = null;

        try {
            // Construct the URL for the query
            URL url = new URL(baseUrl.concat(dataset_para).concat(key_para));

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
                return;
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
                return;
            }
            xmlResponseBody = buffer.toString();
            Log.d(TAG, "Yeah!! I got " + xmlResponseBody);
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
    }
}
