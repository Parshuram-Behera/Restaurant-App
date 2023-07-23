package com.parshuram.orderplacingapp.activities;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parshuram.orderplacingapp.R;
import com.parshuram.orderplacingapp.fragments.HomeFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = findViewById(R.id.frameLayout);

        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setBackground(null);
        navigationView.getMenu().getItem(2).setEnabled(false);

        FloatingActionButton fab = findViewById(R.id.navigationFavIcon);
        int iconColor = ContextCompat.getColor(this, R.color.white);
        fab.setColorFilter(iconColor);



        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {

            getLastLocation();
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Location permission not granted, return.
            return;
        }
        fusedLocationClient.getLastLocation().addOnCompleteListener(this, task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Location location = task.getResult();
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String address = getAddressFromCoordinates(latitude, longitude);

//                passing all the details to home fragment as bundle
                Bundle bundle = new Bundle();
                bundle.putString("address", address);
                bundle.putDouble("latitude", latitude);
                bundle.putDouble("longitude", longitude);

                HomeFragment fragment = new HomeFragment();
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();

            } else {
                Toast.makeText(MainActivity.this, "Turn On Location", Toast.LENGTH_SHORT).show();
                // Location not available, so i am requesting  now location  updates.

                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                fusedLocationClient.requestLocationUpdates(
                        LocationRequest.create()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

//           we will get auto location updated in every 5 minutes
                                .setInterval(5*60*1000)
                                .setFastestInterval(5*60*1000)
                        , new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {


                                if (locationResult != null && locationResult.getLastLocation() != null) {
                                    Location location = locationResult.getLastLocation();

                                    //         here i'm passing fetched location details stored in locationResult  to the Fragment

                                    double latitude = location.getLatitude();
                                    double longitude = location.getLongitude();
                                    String address = getAddressFromCoordinates(latitude, longitude);

                                    Bundle bundle = new Bundle();
                                    bundle.putString("address", address);
                                    bundle.putDouble("latitude", latitude);
                                    bundle.putDouble("longitude", longitude);

                                    HomeFragment fragment = new HomeFragment();
                                    fragment.setArguments(bundle);

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.frameLayout, fragment);
                                    fragmentTransaction.commit();

                                }
                            }
                        },
//           Here I'm running the location call back in main thread , as we have to update ui also
                        getMainLooper()
                );
            }
        });
    }

    private String getAddressFromCoordinates(double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            } else {
                Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show();
            }


        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error fetching address", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getLastLocation();

            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
