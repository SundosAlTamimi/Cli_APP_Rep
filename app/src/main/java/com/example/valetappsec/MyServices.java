package com.example.valetappsec;

import static com.example.valetappsec.GlobalVairable.context;
import static com.example.valetappsec.GlobalVairable.singUpUserTableGlobal;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class MyServices extends Service {
    private static final String TAG = "BackgroundSoundServiceS";
    MediaPlayer player;
    FirebaseDatabase db;
    static String id;
    public static final String ServiceIntent = "MyServices";
    public static boolean ServiceWork = true;
    ValetDatabase valetDatabase;
    Timer timer ;

    public IBinder onBind(Intent arg0) {
        Log.e(TAG, "onBind()");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        id = singUpUserTableGlobal.getId();
        FirebaseApp.initializeApp(MyServices.this);
        valetDatabase = new ValetDatabase(this);
        timer = new Timer();
//        buildNotification();
        //   requestLocationUpdates();
        Log.e(TAG, "onCreate() , service started...");

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        requestLocationUpdates();

        return START_STICKY;

    }


    public IBinder onUnBind(Intent arg0) {
        Log.e(TAG, "onUnBind()");
        return null;
    }

    @Override
    public boolean stopService(Intent name) {
        // TODO Auto-generated method stub


        return super.stopService(name);

    }

    public void onPause() {
        Log.e(TAG, "onPause()");
    }

    @Override
    public void onDestroy() {

        requestLocationUpdates();
        Log.e(TAG, "onCreate() , service stopped...");
    }

    @Override
    public void onLowMemory() {
        Log.e(TAG, "onLowMemory()");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        //  onStop();
//        startForegroundService(new Intent(MyService.this,MyService.class));

        Log.e(TAG, "In onTaskRemoved");
    }

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();

//Specify how often your app should request the device’s location//

        request.setInterval(10000);
//Get the most accurate location data available//

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        // databaseReference.child("ValetAppRealDB").child("StatusValetGroup").child(singUpUserTableGlobal.getId()+"_VALET").child("ifReturn");

        final String path = ("firebase_path");
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

//If the app currently has access to the location permission...//

        if (permission == PackageManager.PERMISSION_GRANTED) {

//...then request location updates//

            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

//Get a reference to the database, so your app can perform read and write operations//
                    Location location = locationResult.getLastLocation();
                    if (location != null) {

//Save the location data to the database//

                        String onOff = "0";
                        try {
                            onOff = valetDatabase.getAllUserOnOff();
                        } catch (Exception e) {
                            onOff = "0";
                        }
                        if (onOff.equals("1")) {
                            writeInFireBaseCaptainLate(location.getLatitude() + "/" + location.getLongitude());
                        } else {
                            Log.e("location_tr", "Tracking location Stop");

                        }
                    }
                }
            }, null);
        }
    }

    void timers() {
    TimerTask t = new TimerTask() {
        @Override
        public void run() {


            LocationRequest request = new LocationRequest();

//Specify how often your app should request the device’s location//

            request.setInterval(10000);
//Get the most accurate location data available//

            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(MyServices.this);
            // databaseReference.child("ValetAppRealDB").child("StatusValetGroup").child(singUpUserTableGlobal.getId()+"_VALET").child("ifReturn");

            final String path = ("firebase_path");
            int permission = ContextCompat.checkSelfPermission(MyServices.this,
                    Manifest.permission.ACCESS_FINE_LOCATION);

//If the app currently has access to the location permission...//

            if (permission == PackageManager.PERMISSION_GRANTED) {

//...then request location updates//

                client.requestLocationUpdates(request, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {

//Get a reference to the database, so your app can perform read and write operations//
                        Location location = locationResult.getLastLocation();
                        if (location != null) {

//Save the location data to the database//

                            String onOff = "0";
                            try {
                                onOff = valetDatabase.getAllUserOnOff();
                            } catch (Exception e) {
                                onOff = "0";
                            }
                            if (onOff.equals("1")) {
                                writeInFireBaseCaptainLate(location.getLatitude() + "/" + location.getLongitude());
                            } else {
                                Log.e("location_tr", "Tracking location Stop");

                            }
                        }
                    }
                }, null);
            }

        }
    };
timer.scheduleAtFixedRate(t,1000,1000);
}


    public void writeInFireBaseCaptainLate(String location){
        db= FirebaseDatabase.getInstance();
        String userId="";
        try{
            userId=valetDatabase.getAllUser();

        }catch (Exception e){
userId="";
        }
        DatabaseReference databaseReference=db.getReference("ValetAppRealDB");
        databaseReference.child("StatusValetGroup").child(userId+"_LocationTracking").setValue(location).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //       Toast.makeText(CaptainMapsActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                Log.e("location_tr","Tracking location Successful");
            }
        });
    }
}