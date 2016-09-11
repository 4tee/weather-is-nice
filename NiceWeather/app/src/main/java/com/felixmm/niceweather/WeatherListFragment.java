package com.felixmm.niceweather;


import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.felixmm.niceweather.adapter.WeatherAdapter;
import com.felixmm.niceweather.async.FetchWeatherAsyncTask;
import com.felixmm.niceweather.persistence.DataStruct;

public class WeatherListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = WeatherListFragment.class.getSimpleName();
    private static final int LOADER_ID = 1;

    WeatherAdapter wAdapter;
    ListView weatherList;

    public static Fragment newInstance(Bundle bundle) {
        WeatherListFragment fragment = new WeatherListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // we have our own custom adapter defined to display results in the listview.
        // We change our adapter to cursor loader since it can help us to work with database,
        // manage our cursor and run on different thread.
        wAdapter = new WeatherAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_weather_list, container, false);

        // Get reference from root view and set adapter on the list
        weatherList = (ListView) rootView.findViewById(R.id.listview_weatherOutlook);
        weatherList.setAdapter(wAdapter);
        weatherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(pos);

                if (cursor != null) {
                    long dt = cursor.getLong(DataStruct.WeatherTable.COL_INDEX_DATE);
                    Uri weatherWithDateUri = DataStruct.WeatherTable.buildWeatherUriWithDate(dt);

                    Intent dayActivityIntent = new Intent(getActivity(), DayActivity.class);
                    dayActivityIntent.setData(weatherWithDateUri);
                    startActivity(dayActivityIntent);
                }
            }
        });



        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        Location mLocation = getArguments().getParcelable(MainActivity.LOCATION_KEY);
        new FetchWeatherAsyncTask(getActivity(), mLocation).execute();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getActivity(),
                DataStruct.WeatherTable.CONTENT_URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {
        wAdapter.swapCursor(newCursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        wAdapter.swapCursor(null);
    }
}
