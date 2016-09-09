package com.felixmm.niceweather;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class DayActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_activity_container);

        loadDayFragment();
    }

    private void loadDayFragment() {
        Bundle bundle = new Bundle();
        Fragment dayFragment = DayFragment.newInstance(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.dayContainer, dayFragment);
        ft.commit();
    }
}
