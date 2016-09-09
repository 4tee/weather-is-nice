package com.felixmm.niceweather;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements
        ConnectionCallbacks,
        OnConnectionFailedListener {

    protected static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 1;

    protected GoogleApiClient mGoogleApiClient;
    protected Location lastKnownLocation;


    private void loadWeatherListFragment() {
        Fragment userFragment = WeatherListFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, userFragment);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_container);

        startLocationService();

//        if (savedInstanceState == null) {
//            loadWeatherListFragment();
//            buildGoogleApiClient();
//        }
    }

    private void startLocationService() {
        if (!checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) ||
                !checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            requestPermission();
        } else {
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    private boolean checkPermission(String permission) {
        int result = ActivityCompat.checkSelfPermission(this, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                }, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startLocationService();

                } else {

                    Snackbar.make(
                            this.findViewById(R.id.container),
                            "Permission Denied, Please allow to proceed!",
                            Snackbar.LENGTH_LONG)
                            .show();
                }
                break;
        }
    }

    /**
     * Run when the GoogleApiClient object is connected.
     * @param connectBundle
     */
    @Override
    public void onConnected(@Nullable Bundle connectBundle) {

        if (!checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermission();
            return;
        }

        lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastKnownLocation != null)
        {
            Log.d(TAG, "latitude: " + lastKnownLocation.getLatitude());
            Log.d(TAG, "longitude: " + lastKnownLocation.getLongitude());

        } else {
            Log.d(TAG, "lastKnownLocation is null");
            Snackbar.make(
                    this.findViewById(R.id.container),
                    "No location detected! Make sure location is enabled.",
                    Snackbar.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        // for some reasons, in case the connection drop, reconnect.
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // connection failed
        Log.d(TAG, "connection failed: " + connectionResult.getErrorMessage());
    }
}
