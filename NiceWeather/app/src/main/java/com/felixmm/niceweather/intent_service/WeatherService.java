package com.felixmm.niceweather.intent_service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

import com.felixmm.niceweather.http.FetchWeatherFromOWM;
import com.felixmm.niceweather.persistence.DataStruct;

import java.util.Vector;

public class WeatherService extends IntentService {

    public static final String EXTRA_NO_OF_DAY = "noOfDay";
    public static final String EXTRA_LATITUDE = "Latitude";
    public static final String EXTRA_LONGITUDE = "Longitude";

    public WeatherService() {
        super("NiceWeather");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        int noOfDay = intent.getIntExtra(EXTRA_NO_OF_DAY, 7);
        double lat = intent.getDoubleExtra(EXTRA_LATITUDE, 0.0);
        double lon = intent.getDoubleExtra(EXTRA_LONGITUDE, 0.0);

        Vector<ContentValues> cvVector = FetchWeatherFromOWM.getDailyWeatherJson(noOfDay, lat, lon);
        if (cvVector != null && cvVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cvVector.size()];
            cvVector.toArray(cvArray);

            getContentResolver().delete(DataStruct.WeatherTable.CONTENT_URI,null,null);
            getContentResolver().bulkInsert(DataStruct.WeatherTable.CONTENT_URI,
                    cvArray);
        }
    }
}
