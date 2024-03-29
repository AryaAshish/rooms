package com.aryan.android.kangaroocust;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowLocationOnMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    Location vehicleLocation;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                centerMapOnLocation(lastKnownLocation,"Your location");

                Toast.makeText(this, "click on the parking address marker to get the directions", Toast.LENGTH_SHORT).show();

            }

        }
        else {

            finish();
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();

        }

    }

    public void centerMapOnLocation(Location location, String title){

        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());

        // mMap.addMarker(new MarkerOptions().position(userLocation).title(title)).showInfoWindow();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_location_on_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        vehicleLocation = new Location(LocationManager.GPS_PROVIDER);
        vehicleLocation.setLatitude(Double.parseDouble(HotelActivity_1.locationLatitude));
        vehicleLocation.setLongitude(Double.parseDouble(HotelActivity_1.locationLongitude));

        LatLng vehicleLatLng = new LatLng(vehicleLocation.getLatitude(),vehicleLocation.getLongitude());

        mMap.addMarker(new MarkerOptions().position(vehicleLatLng).title(HotelActivity_1.selectedAddress)).showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vehicleLatLng,18));

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

              //  centerMapOnLocation(location,"Your location");

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

        if (Build.VERSION.SDK_INT < 23){

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

            Toast.makeText(this, "click on the parking address marker to get the directions", Toast.LENGTH_SHORT).show();

        }
        else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

          //      centerMapOnLocation(lastKnownLocation,"Your location");

                Toast.makeText(this, "click on the parking address marker to get the directions", Toast.LENGTH_SHORT).show();

            }
            else {

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            }

        }

    }
}

