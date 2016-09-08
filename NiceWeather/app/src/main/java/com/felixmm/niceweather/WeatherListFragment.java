package com.felixmm.niceweather;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class WeatherListFragment extends Fragment {

    ArrayList<String> sampleString = new ArrayList<>(Arrays.asList(
            "Wednesday; Fair; Temp: 32C-23C; Hum:95%-55%",
            "Thursday; Rain; Temp: 32C-23C; Hum:90%-50%",
            "Friday; Partly Cloudy; Temp: 32C-23C; Hum:85%-45%",
            "Saturday; Fair; Temp: 32C-23C; Hum:80%-40%"
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

        return rootView;
    }
}
