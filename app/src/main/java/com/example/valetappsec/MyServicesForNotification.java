package com.example.valetappsec;

import static com.example.valetappsec.GlobalVairable.singUpUserTableGlobal;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.valetappsec.Model.UserService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class MyServicesForNotification extends Service {
    private static final String TAG = "BackgroundFireServiceS";
    MediaPlayer player;
    FirebaseDatabase db;
    static String id;
    public static final String ServiceIntent = "MyServices";
    public static boolean ServiceWork = true;
    ValetDatabase valetDatabase;
    Timer timer ;
    UserService userService;
    DatabaseReference databaseReference;

    public IBinder onBind(Intent arg0) {
        Log.e(TAG, "onBind()");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(MyServicesForNotification.this);
        valetDatabase = new ValetDatabase(this);
        //userService=new UserService();
        id=valetDatabase.getAllUser();

        Log.e(TAG, "onCreate() , service started...");

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        //requestLocationUpdates();
        allTaskInFireBase();
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
        allTaskInFireBase();
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
        allTaskInFireBase();
        Log.e(TAG, "In onTaskRemoved");
    }


    void allTaskInFireBase(){

        id=valetDatabase.getAllUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("ValetAppRealDB").child("StatusValetGroup").child(id + "_Client").child("status");

        Query query2 = databaseReference.child("ValetAppRealDB").child("StatusValetGroup").child(id + "_Client");

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String mychild = dataSnapshot.getValue().toString();
                    // textView.setText(mychild);
//                     ValetFireBaseItem2 mychild = dataSnapshot.getValue(ValetFireBaseItem2.class);

//                     captainId=mychild.getCaptainId();
//                    ListShow(mychild.getStatus());

                    if (mychild.equals("12")) {
                        Intent intent = new Intent(MyServicesForNotification.this, DriverMapsActivity.class);

                        showNotification(MyServicesForNotification.this, " The Captain Arrive  ", "Captain Arrive To Client", intent);

                    }

                    //importJson.getStatuss();

                    //  Toast.makeText(DriverMapsActivity.this, "succ " + "mychild", Toast.LENGTH_SHORT).show();

                } catch (Exception y) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        query.addValueEventListener(listener);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query3 = databaseReference.child("ValetAppRealDB").child("StatusValetGroup").child(id + "_CarInWay");

//        query2.

        ValueEventListener listener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String mychild = dataSnapshot.getValue().toString();
                    // textView.setText(mychild);
//                     ValetFireBaseItem2 mychild = dataSnapshot.getValue(ValetFireBaseItem2.class);

//                     captainId=mychild.getCaptainId();
//                    ListShow(mychild.getStatus());

                    if (mychild.equals("1")) {
                        Intent intent = new Intent(MyServicesForNotification.this, DriverMapsActivity.class);

                        showNotification(MyServicesForNotification.this, "  Captain   ", "Your Car Is On The Way", intent);

                        writeInFireBaseCaptainWay(id);
                    }

                } catch (Exception y) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        query3.addValueEventListener(listener2);

    }


    public void writeInFireBaseCaptainWay(String clientId) {
        id=valetDatabase.getAllUser();
        db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("ValetAppRealDB");
        databaseReference.child("StatusValetGroup").child(id + "_CarInWay").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //       Toast.makeText(DriverMapsActivity.this, "Successful", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-03";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }


}