package com.example.valetappsec;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.valetappsec.Json.ImportJson;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.valetappsec.GlobalVairable.captainClientTransfer;
import static com.example.valetappsec.GlobalVairable.globalText;
import static com.example.valetappsec.GlobalVairable.singUpUserTableGlobal;

public class MainValetActivity extends AppCompatActivity implements View.OnClickListener {
LinearLayout startReq,shareApp,profile,yourLocation;
TextView user,startReqText;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    ValetDatabase valetDatabase;
    CircleImageView carPic;
    SweetAlertDialog swASingUp;
    protected static final String TAG = "LocationOnOff";

    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.valet_main_activity);
        initialization();

    }

    private void initialization() {
        startReq=findViewById(R.id.startReq);
        shareApp=findViewById(R.id.shareApp);
        profile=findViewById(R.id.profile);
        user=findViewById(R.id.user);
        startReqText=findViewById(R.id.startReqText);
        carPic=findViewById(R.id.carPicC);
        globalText=startReqText;
        ImportJson importJson=new ImportJson(MainValetActivity.this);
     //   importJson.getImage(2);
        valetDatabase=new ValetDatabase(MainValetActivity.this);
        yourLocation=findViewById(R.id.yourLocation);
        user.setText("Welcome "+singUpUserTableGlobal.getUserName());
        startReq.setOnClickListener(this);
        shareApp.setOnClickListener(this);
        profile.setOnClickListener(this);
        yourLocation.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startReq:


                if ( Build.VERSION.SDK_INT >= 23){
                    if (ActivityCompat.checkSelfPermission(MainValetActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED  ){
                        requestPermissions(new String[]{
                                        android.Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_CODE_ASK_PERMISSIONS);
                        return ;
                    }
                }

                //Request();
               // locApi();
                getCorrectLoc();
                break;
            case R.id.shareApp:
                shareApp();
                break;
            case R.id.profile:
                Intent ProfileActivity=new Intent(MainValetActivity.this,ProfileActivity.class);
                startActivity(ProfileActivity);
                break;
            case R.id.yourLocation:
                String address=valetDatabase.getAllSetting();
                if(!TextUtils.isEmpty(address)) {
                    if (!TextUtils.isEmpty(captainClientTransfer.getParkingLocation())) {
                        if (captainClientTransfer.getParkingLocation().contains("lat/lng:")) {
                            Intent lOCATIONActivity = new Intent(MainValetActivity.this, LocationMapsActivity.class);
                            startActivity(lOCATIONActivity);
                        } else {
                            Toast.makeText(this, "Your Car Is Not Parking", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Your Car Is Not Parking", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "Your Car Is Not Parking", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    void getCorrectLoc(){

        swASingUp = new SweetAlertDialog(MainValetActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        swASingUp.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
        swASingUp.setTitleText("Please Wait until get location " );
        swASingUp.setCancelable(false);
        swASingUp.show();
        int isIn=5;
//        for (int i=0;i<=isIn;i++) {
        Location location = getLastKnownLocation();
        try {
            double  v1 = location.getLatitude();
            double  v2 = location.getLongitude();

            Log.e("loc12"," "+isIn);

            Intent intent=new Intent(MainValetActivity.this,DriverMapsActivity.class);
            startActivity(intent);

            swASingUp.dismissWithAnimation();
            //isIn=i;
//                break;
        } catch (Exception e) {
            isIn++;
            Log.e("loc133", " " + isIn);
            swASingUp.dismissWithAnimation();
           SweetAlertDialog swASing_ = new SweetAlertDialog(MainValetActivity.this, SweetAlertDialog.WARNING_TYPE);
            swASing_.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            swASing_.setTitleText("Please Try Again (Can not get Location)!!");
            swASing_.setCancelable(true);
            swASing_.show();
//                if(isIn==1000) {
//
//                    Toast.makeText(MainValetActivity.this, "Can Not Get Your Location Please Try Again ", Toast.LENGTH_SHORT).show();
//               swASingUp.dismissWithAnimation();
////                break;
//                }
//            }
        }

    }

    private Location getLastKnownLocation() {
        Location l=null;
        LocationManager mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                l = mLocationManager.getLastKnownLocation(provider);

            }
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    void Request(){
        Intent mapsActivity=new Intent(MainValetActivity.this,DriverMapsActivity.class);
        startActivity(mapsActivity);
    }

    void shareApp(){

//        Intent sendIntent = new Intent();
////        sendIntent.setAction(Intent.ACTION_SEND);
////        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
////        sendIntent.setType("text/plain");
////
////        Intent shareIntent = Intent.createChooser(sendIntent, null);
////        startActivity(shareIntent);


        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
        i.putExtra(Intent.EXTRA_TEXT, "http://www.url.com");
        startActivity(Intent.createChooser(i, "Share URL"));
    }


    public void shareWhatsAppA(File pdfFile, int pdfExcel, List<String> filePaths){
        try {

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            Uri uri = Uri.fromFile(pdfFile);
            Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            if (pdfFile.exists()) {
                if (pdfExcel == 1) {
                    sendIntent.setType("application/excel");
                } else if (pdfExcel == 2) {
                    sendIntent.setType("plain/text");//46.185.208.4
                }
                //  sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(String.valueOf(uri)));

                sendIntent.putExtra(Intent.EXTRA_SUBJECT, " Sharing File...");
               // sendIntent.putExtra(Intent.EXTRA_TEXT, pdfFile.getName() + " Sharing File");

                sendIntent.putExtra(Intent.EXTRA_TEXT, pdfFile.getName() + " Valet app Sharing ");

                ArrayList<Uri> uris = new ArrayList<Uri>();
                //convert from paths to Android friendly Parcelable Uri's
                for (String file : filePaths)
                {
                    File fileIn = new File(file);
                    Uri u = Uri.fromFile(fileIn);
                    uris.add(u);
                }
                sendIntent.putExtra(Intent.EXTRA_STREAM, uris);

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

            }
        }catch (Exception e){
            Log.e("drk;d","dfrtr"+e.toString());
            Toast.makeText(MainValetActivity.this, "Storage Permission"+e.toString(), Toast.LENGTH_SHORT).show();
        }

        //  deleteTempFolder(pdfFile.getPath());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 Request();
                } else {
                    // Permission Denied
                    Toast.makeText( this,"Please Enable Access to Location " , Toast.LENGTH_SHORT)
                            .show();
                }
                break;


            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void fillImage(){
       carPic.setImageBitmap(singUpUserTableGlobal.getCarPicBitmap());
    }
    void locApi() {
        final LocationManager manager = (LocationManager) MainValetActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(MainValetActivity.this)) {
            Toast.makeText(MainValetActivity.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
            // finish();
        }
        // Todo Location Already on  ... end

        if (!hasGPSDevice(MainValetActivity.this)) {
            Toast.makeText(MainValetActivity.this, "Gps not Supported", Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(MainValetActivity.this)) {
            Log.e("keshav", "Gps already enabled");
            Toast.makeText(MainValetActivity.this, "Gps not enabled", Toast.LENGTH_SHORT).show();
            googleApiClient=null;
            enableLoc();
        } else {
            Log.e("keshav", "Gps already enabled");
            Request();
            Toast.makeText(MainValetActivity.this, "Gps already enabled", Toast.LENGTH_SHORT).show();
        }
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
            googleApiClient = new GoogleApiClient.Builder(MainValetActivity.this)
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
                                status.startResolutionForResult(MainValetActivity.this, REQUEST_LOCATION);

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

        IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_LOCATION) {
            if (Result != null) {

            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }else {
            switch (resultCode) {
                case Activity.RESULT_OK:
                   Request();

                    Toast.makeText(MainValetActivity.this, "Ok-", Toast.LENGTH_SHORT).show();

                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(MainValetActivity.this, "cancel-", Toast.LENGTH_SHORT).show();
                    Log.e("GPS","User denied to access location");
                    break;
            }

        }
    }



}