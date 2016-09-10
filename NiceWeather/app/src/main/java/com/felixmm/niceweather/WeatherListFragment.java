package com.felixmm.niceweather;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.felixmm.niceweather.adapter.WeatherAdapter;
import com.felixmm.niceweather.http.FetchWeatherFromOWM;
import com.felixmm.niceweather.persistence.DataStruct;
import com.felixmm.niceweather.persistence.DbHelper;

import java.util.Vector;

public class WeatherListFragment extends Fragment {

    private static final String TAG = WeatherListFragment.class.getSimpleName();

    WeatherAdapter wAdapter;
    ListView weatherList;
    DbHelper dbHelper;

    public static Fragment newInstance(Bundle bundle) {
        WeatherListFragment fragment = new WeatherListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_weather_list, container, false);

        // Load data from database.
        dbHelper = new DbHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DataStruct.WeatherTable.TABLE_NAME,
                null, // columns
                null, // selection
                null, // selectionArgs
                null, // groupby
                null, // having
                null); // sort order

        // we have our own custom adapter defined to display results in the listview.
        wAdapter = new WeatherAdapter(getActivity(), cursor, 0);

        // Get reference from root view and set adapter on the list
        weatherList = (ListView) rootView.findViewById(R.id.listview_weatherOutlook);
        weatherList.setAdapter(wAdapter);

//        weatherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
//                String dayWeather = mAdapter.getItem(pos);
//
//                Intent dayActivityIntent = new Intent(getActivity(), DayActivity.class);
//                dayActivityIntent.putExtra(MainActivity.DAY_INTENT_KEY, dayWeather);
//                startActivity(dayActivityIntent);
//            }
//        });

        // Execute AsyncTask
        new FetchWeatherTask().execute();

        return rootView;
    }



    public class FetchWeatherTask extends AsyncTask<Void, Void, Vector<ContentValues>> {

        @Override
        protected Vector<ContentValues> doInBackground(Void... params) {
            // fetch weather from NEA site
            int noOfDay = 5;

            Location mLocation = getArguments().getParcelable(MainActivity.LOCATION_KEY);
            if (mLocation != null) {
                double lat = mLocation.getLatitude();
                double lon = mLocation.getLongitude();

                return FetchWeatherFromOWM.getDailyWeatherJson(noOfDay, lat, lon);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Vector<ContentValues> dailyWeather) {
            if (null != dailyWeather)
            {
                if (dailyWeather.size() > 0) {
                    ContentValues[] cvArray = new ContentValues[dailyWeather.size()];
                    dailyWeather.toArray(cvArray);
                    dbHelper.deleteRows(DataStruct.WeatherTable.TABLE_NAME,null,null);
                    dbHelper.bulkInsert(cvArray);
                }
            }
        }
    }
}
