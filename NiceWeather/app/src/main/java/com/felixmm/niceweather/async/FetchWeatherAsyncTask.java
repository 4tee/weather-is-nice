package com.felixmm.niceweather.async;

import android.content.ContentValues;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import com.felixmm.niceweather.http.FetchWeatherFromOWM;
import com.felixmm.niceweather.persistence.DataStruct;

import java.util.Vector;

/**
 * Created by rhymes_mcpro on 10/9/16.
 */
public class FetchWeatherAsyncTask extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private Location mLocation;

    public FetchWeatherAsyncTask(Context context, Location location) {
        mContext = context;
        mLocation = location;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        // fetch weather from NEA site
        int noOfDay = 5;

//        Location mLocation = getArguments().getParcelable(MainActivity.LOCATION_KEY);
        if (mLocation != null) {
            double lat = mLocation.getLatitude();
            double lon = mLocation.getLongitude();

            Vector<ContentValues> cvVector = FetchWeatherFromOWM.getDailyWeatherJson(noOfDay, lat, lon);
            if (cvVector != null && cvVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cvVector.size()];
                cvVector.toArray(cvArray);

                mContext.getContentResolver().bulkInsert(DataStruct.WeatherTable.CONTENT_URI,
                        cvArray);
            }
        }
        return null;
    }
}
