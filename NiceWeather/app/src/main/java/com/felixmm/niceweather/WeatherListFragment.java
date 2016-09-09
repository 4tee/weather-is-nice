package com.felixmm.niceweather;


import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.felixmm.niceweather.http.FetchWeatherFromOWM;

import java.util.ArrayList;
import java.util.Arrays;

public class WeatherListFragment extends Fragment {

    private static final String TAG = WeatherListFragment.class.getSimpleName();

    ArrayList<String> sampleString = new ArrayList<>(Arrays.asList(
            "Wednesday; few clouds; Temp: 29.03C-31.71C; Hum:84%",
            "Thursday; moderate rain; Temp: 27.12C-29.07C; Hum:100%",
            "Friday; light rain; Temp: 28.96C-31.79C; Hum:90%",
            "Saturday; Fair; Temp: 32C-23C; Hum:80%"
    ));

    ArrayAdapter<String> mAdapter;
    ListView weatherList;

    public static Fragment newInstance(Bundle bundle) {
        WeatherListFragment fragment = new WeatherListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_weather_list, container, false);

        // Set sample adapter
       mAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.fragment_weather_list_item,
                R.id.weather_list_item_textview,
                sampleString
        );

        // Get reference from root view and set adapter on the list
        weatherList = (ListView) rootView.findViewById(R.id.listview_weatherOutlook);
        weatherList.setAdapter(mAdapter);
        weatherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                String dayWeather = mAdapter.getItem(pos);

                Intent dayActivityIntent = new Intent(getActivity(), DayActivity.class);
                dayActivityIntent.putExtra(MainActivity.DAY_INTENT_KEY, dayWeather);
                startActivity(dayActivityIntent);
            }
        });

        // Execute AsyncTask
        new FetchWeatherTask().execute();

        return rootView;
    }



    public class FetchWeatherTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
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
        protected void onPostExecute(String[] dailyWeather) {
            if (null != dailyWeather)
            {
                mAdapter.clear();
                for (String weather : dailyWeather)
                    mAdapter.add(weather);
            }
        }
    }
}
