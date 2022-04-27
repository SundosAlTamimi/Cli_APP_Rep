package com.example.valetappsec;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button signInButton, singUp;
    TextView setting;
    ValetDatabase valetDatabase;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
        clockwise();

    }

    private void initialization() {

        signInButton = findViewById(R.id.signInButton);
        singUp = findViewById(R.id.singUp);
        setting = findViewById(R.id.setting);
        valetDatabase = new ValetDatabase(MainActivity.this);
        signInButton.setOnClickListener(this);
        singUp.setOnClickListener(this);
        setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.signInButton:

                if(isIpInSetting()) {
                locApi();
                }else {
                    Toast.makeText(this, "Please Write Ip Address In Setting ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.singUp:
                if(isIpInSetting()) {

                    Intent singUpIntent = new Intent(MainActivity.this, SingUpValetActivityApp.class);
                    startActivity(singUpIntent);

                }else {
                    Toast.makeText(this, "Please Write Ip Address In Setting ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.setting:
                SettingDialog();
                break;
        }
    }

    void locApi() {
        final LocationManager manager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(MainActivity.this)) {
            Toast.makeText(MainActivity.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
            // finish();
        }
        // Todo Location Already on  ... end

        if (!hasGPSDevice(MainActivity.this)) {
            Toast.makeText(MainActivity.this, "Gps not Supported", Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(MainActivity.this)) {
            Log.e("keshav", "Gps already enabled");
            Toast.makeText(MainActivity.this, "Gps not enabled", Toast.LENGTH_SHORT).show();
            googleApiClient=null;
            enableLoc();
        } else {
            Log.e("keshav", "Gps already enabled");
            Request();
            Toast.makeText(MainActivity.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
        }
    }


    private void Request() {
        Intent logInIntent = new Intent(MainActivity.this, LogInActivity.class);
        startActivity(logInIntent);
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            client.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

//Get a reference to the database, so your app can perform read and write operations//
                    Location location = locationResult.getLastLocation();
                    if (location != null) {

//                        v1=location.getLatitude();
//                        v2=location.getLongitude();
//                        LatLngListMarker.clear();
//                        LatLngListMarker.add(new LatLng(v1,v2));
                        // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(v1,v2), 20));
                        //location(0);
//Save the location data to the database//


                    }
                }
            }, null);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);

                                //  finish();
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION) {

            switch (resultCode) {
                case Activity.RESULT_OK:
                    Request();

                    Toast.makeText(MainActivity.this, "Ok-", Toast.LENGTH_SHORT).show();

                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(MainActivity.this, "cancel-", Toast.LENGTH_SHORT).show();
                    Log.e("GPS", "User denied to access location");
                    break;
            }

        }
    }


    boolean isIpInSetting() {
        String ips = valetDatabase.getAllIPSetting();
        if (!TextUtils.isEmpty(ips)) {
            return true;
        } else {
            return false;
        }
    }


    void SettingDialog() {
        Dialog packingListDialog = new Dialog(MainActivity.this,R.style.Theme_Dialog);
        packingListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        packingListDialog.setContentView(R.layout.setting_dialog);
        packingListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText ip;
        Button accept, cancel;

        ip = packingListDialog.findViewById(R.id.ip);
        accept = packingListDialog.findViewById(R.id.accept);
        cancel = packingListDialog.findViewById(R.id.cancel);

        String ips = valetDatabase.getAllIPSetting();
        if (!TextUtils.isEmpty(ips)) {
            ip.setText(ips);
        } else {

        }


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(ip.getText().toString())) {
                    if (!TextUtils.isEmpty(ips)) {
                        valetDatabase.updateiP(ips, ip.getText().toString());
                    }else {
                        valetDatabase.addIpSetting(ip.getText().toString(), "0");
                    }
                    packingListDialog.dismiss();
                } else {
                    ip.setError("Required1");
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                packingListDialog.dismiss();

            }
        });

        packingListDialog.show();
    }

    public void clockwise(){
//        ImageView image = (findViewById(R.id.imageView);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.clockwise);
        signInButton.startAnimation(animation);
        singUp.startAnimation(animation);
    }
}