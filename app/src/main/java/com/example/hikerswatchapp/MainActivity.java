package com.example.hikerswatchapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView addressView, latitudeView, longitudeView, accuracyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addressView = findViewById(R.id.addressText);
        latitudeView = findViewById(R.id.latitudeText);
        longitudeView = findViewById(R.id.longitudeText);
        accuracyView = findViewById(R.id.accuracyText);

        final DecimalFormat decimalFormat = new DecimalFormat(".00");

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("LOCATION", location.toString());
                latitudeView.setText("Latitude : " + Double.toString(Double.parseDouble(decimalFormat.format(location.getLatitude()))));
                longitudeView.setText("Longitude : " + Double.parseDouble(decimalFormat.format(location.getLongitude())));
                accuracyView.setText("Accuracy : " + Double.toString(Double.parseDouble(decimalFormat.format(location.getAccuracy()))));

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    Log.i("ADDRESS", addresses.toString());
                    String locationAddresses[] = addresses.get(0).getAddressLine(0).toString().split(",") ;
                    String address ="";
                    for (String s : locationAddresses) {
                        address += s+"\n";
                    }
                    addressView.setText("Address : " + address);
                        Log.i("ADDRESS 2ND STEP", address);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0 ,0 , locationListener);

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latitudeView.setText("Latitude : " + Double.toString(Double.parseDouble(decimalFormat.format(lastKnownLocation.getLatitude()))));
            longitudeView.setText("Longitude : " + Double.parseDouble(decimalFormat.format(lastKnownLocation.getLongitude())));
            accuracyView.setText("Accuracy : " + Double.toString(Double.parseDouble(decimalFormat.format(lastKnownLocation.getAccuracy()))));

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(lastKnownLocation.getLatitude(),
                        lastKnownLocation.getLongitude(), 1);

                Log.i("ADDRESS", addresses.toString());
                String locationAddresses[] = addresses.get(0).getAddressLine(0).toString().split(",") ;
                String address ="";
                for (String s : locationAddresses) {
                    address += s+"\n";
                }
                addressView.setText("Address : " + address);
                Log.i("ADDRESS 2ND STEP", address);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
                }
            }
        }
    }
}
