package com.felixmm.niceweather;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.felixmm.niceweather.http.FetchWeatherFromOWM;

import java.util.ArrayList;
import java.util.Arrays;

public class WeatherListFragment extends Fragment {

    ArrayList<String> sampleString = new ArrayList<>(Arrays.asList(
            "Wednesday; few clouds; Temp: 29.03C-31.71C; Hum:84%",
            "Thursday; moderate rain; Temp: 27.12C-29.07C; Hum:100%",
            "Friday; light rain; Temp: 28.96C-31.79C; Hum:90%",
            "Saturday; Fair; Temp: 32C-23C; Hum:80%"
    ));

    public static Fragment newInstance() {
        return new WeatherListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_weather_list, container, false);

        // Set sample adapter
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.fragment_weather_list_item,
                R.id.weather_list_item_textview,
                sampleString
        );

        // Get reference from root view and set adapter on the list
        ListView weatherList = (ListView) rootView.findViewById(R.id.listview_weatherOutlook);
        weatherList.setAdapter(mAdapter);

        // Execute AsyncTask
        new FetchWeatherTask().execute();

        return rootView;
    }



    public class FetchWeatherTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // fetch weather from NEA site
            int noOfDay = 5;
            double lat = 1.31500000;
            double lon = 103.76000000;

            return FetchWeatherFromOWM.getDailyWeatherJson(noOfDay, lat, lon);
        }
    }
}
