package com.example.valetappsec;

import android.content.Intent;
import android.content.pm.PackageManager;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.valetappsec.GlobalVairable.captainClientTransfer;
import static com.example.valetappsec.GlobalVairable.globalText;
import static com.example.valetappsec.GlobalVairable.singUpUserTableGlobal;

public class MainValetActivity extends AppCompatActivity implements View.OnClickListener {
LinearLayout startReq,shareApp,profile,yourLocation;
TextView user,startReqText;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    ValetDatabase valetDatabase;

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
        globalText=startReqText;
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

                Request();

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
}