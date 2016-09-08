package com.felixmm.niceweather;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_container);
        if (savedInstanceState == null) {
            loadWeatherListFragment();
        }
    }


    private void loadWeatherListFragment() {
        Fragment userFragment = WeatherListFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, userFragment);
        ft.commit();
    }

}
