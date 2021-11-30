package com.example.valetappsec;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.valetappsec.Adapter.ListAdapterOrder;
import com.example.valetappsec.Adapter.ListAdapterSearch;
import com.example.valetappsec.Json.ExportJson;
import com.example.valetappsec.Json.ImportJson;
import com.example.valetappsec.Model.CaptainClientTransfer;
import com.example.valetappsec.Model.ClientOrder;
import com.example.valetappsec.Model.ValetFireBaseItem;
import com.example.valetappsec.Model.ValetFireBaseItem2;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.valetappsec.GlobalVairable.arriveTime;
import static com.example.valetappsec.GlobalVairable.captainClientTransfer;
import static com.example.valetappsec.GlobalVairable.captainId;
import static com.example.valetappsec.GlobalVairable.clientOrders;
import static com.example.valetappsec.GlobalVairable.context;
import static com.example.valetappsec.GlobalVairable.globalText;
import static com.example.valetappsec.GlobalVairable.isOk;
import static com.example.valetappsec.GlobalVairable.singUpUserTableGlobal;


public class DriverMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    // Button requestButton,scanBarcode;
    public static List<LatLng> LatLngListMarker;
    private LatLngBounds.Builder builder;
    LatLngBounds bounds;
    boolean flag = false;
    Timer timer;
    List<String>searchArray;
    ListAdapterSearch listAdapterSearch;
    RatingBar ratingBar;
    double v1 = 31.951110, v2 = 35.917270, a1 = 0.0, a2 = 0.0;
    SweetAlertDialog pdaSweet;
    String QrGenarater="";
    LinearLayout firstDialogLocation, secDialogLocation, _3DialogLocation, _4DialogLocation, _5DialogLocation, _6DialogLocation, _7DialogLocation, _8DialogLocation
            ,_9DialogLocation,_10DialogLocation,_11DialogLocation,_12DialogLocation,_13DialogLocation,_14DialogLocation,_15DialogLocation,selectLinear,_42DialogLocation,_161DialogLocation;
    TextView fromLoc, toLoc,arriveTimeText,reming,valuePay,captainNameText,captainName,phoneNo;
    EditText whereGo,amountValue,noteRate;
    RadioButton cashPayment, walletPayment;
    Button iNeedCaptain,scanQRCode,returnButton,Pay,rating,callInfo,callParking,navigation,Arrive,calls,End,endRequest,endRequestList,late;
    int flafOpen = 0;
    private static final int SELECT_IMAGE = 3;
    private Uri fileUri;
    Bitmap YourPicBitmap1=null;
    //    Button go;
    ListView list;
    ValetDatabase valetDatabase;
    Timer T;
    ImportJson importJson;
    private static final int REQUEST_PHONE_CALL = 1;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 0x00AF;

    ExportJson exportJson;
    GlobalVairable globalVairable;
    ImageView barcode;
   static ListAdapterOrder listAdapterOrder;
//    SearchView searchView ;
TextView searchView ,timeCountDown;
ImageView cancelSearch;
    int flagIp=0;
    FirebaseDatabase db;
    DatabaseReference databaseReference;
    ListView searchList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.valet_driver_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        initialization();

//        ValetFireBaseItem valetFireBaseItem=new ValetFireBaseItem();
//
//        valetFireBaseItem.setStatus("-1");
//        valetFireBaseItem.setIfReturn("-1");
//        valetFireBaseItem.setRawIdActivate("-1");
//        valetFireBaseItem.setUserId(singUpUserTableGlobal.getId());
//        valetFireBaseItem.setLongLocation("-1");
//        valetFireBaseItem.setLatLocation("-1");
//        writeInFireBase(valetFireBaseItem);
//
//

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.e("textViewListner 3","q = ");
                    if (flafOpen == 0) {
                        searchFunction(0);
                    }
                    return true;
                }
                return false;
            }
        });


        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s.toString())) {
                    Log.e("textViewListner 2","q = ");
                    if(cancelSearch.getVisibility()==View.GONE) {
                        cancelSearch.setVisibility(View.VISIBLE);
                    }

                    searchArray = valetDatabase.getLocation(s.toString());
                    searchView();
                }else{
                    cancelSearch.setVisibility(View.GONE);

                    searchList.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Log.e("textViewListner 3","q = "+query);
//                searchFunction();
//
//                if(!TextUtils.isEmpty(query)){
//                    Log.e("textViewListner","q = "+query);
//
//                }
//
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });

        mapFragment.getMapAsync(this);




    }

   void searchFunction(int isAdd){
        // on below line we are getting the
        // location name from search view.
//                if (flafOpen == 0) {
       if(isAdd==0) {
           valetDatabase.addLocation(searchView.getText().toString());
       }
       searchList.setVisibility(View.GONE);
        String location = searchView.getText().toString();

        // below line is to create a list of address
        // where we will store the list of all address.
        List<Address> addressList = null;

        // checking if the entered location is null or not.
        if (location != null || location.equals("")) {
            // on below line we are creating and initializing a geo coder.
            Geocoder geocoder = new Geocoder(DriverMapsActivity.this);
            try {
                // on below line we are getting location from the
                // location name and adding that location to address list.
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // on below line we are getting the location
            // from our list a first position.
            try {
                Address address = addressList.get(0);


                // on below line we are creating a variable for our location
                // where we will add our locations latitude and longitude.
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                // on below line we are adding marker to that position.
                mMap.addMarker(new MarkerOptions().position(latLng).title(location));

                selectLinear.setVisibility(View.GONE);
                // below line is to animate camera to that position.
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));

                LatLngListMarker.clear();
                LatLng latLng2 = new LatLng(v1, v2);
                LatLngListMarker.add(latLng2);
                LatLngListMarker.add(latLng);

                firstDialogLocations();

            } catch (Exception e) {
                Toast.makeText(DriverMapsActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initialization() {

        //  requestButton=findViewById(R.id.requestButton);
//        scanBarcode=findViewById(R.id.scanBarcode);
        //     requestButton.setOnClickListener(onClickListener);
//        scanBarcode.setOnClickListener(onClickListener);

//        scanBarcode.setVisibility(View.GONE);
        // requestButton.setVisibility(View.VISIBLE);

        try {

            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            String locationProvider = LocationManager.NETWORK_PROVIDER;
            // I suppressed the missing-permission warning because this wouldn't be executed in my
            // case without location services being enabled
            //  @SuppressLint("MissingPermission")
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
            android.location.Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

            try {
                v1 = lastKnownLocation.getLatitude();
                v2 = lastKnownLocation.getLongitude();
            } catch (Exception e) {
                v1 = 31.951110;
                v2 = 35.917270;
            }

        }catch (Exception e){
            Log.e("LocationLanLag", "  Exception");
        }
        Log.e("LocationLanLag", "  loo");
        Log.e("LocationLanLag", "  n  " + v1 + "   " + v2);

        cancelSearch=findViewById(R.id.cancelSearch);
        listAdapterSearch=new ListAdapterSearch();
        LatLngListMarker = new ArrayList<>();
        LatLngListMarker.clear();
        LatLng latLng = new LatLng(v1, v2);
        LatLngListMarker.add(latLng);
        builder = new LatLngBounds.Builder();
        late=findViewById(R.id.late);
        searchList=findViewById(R.id.searchList);
        searchList.setVisibility(View.GONE);
        searchView = findViewById(R.id.idSearchView);
        firstDialogLocation = findViewById(R.id.firstDialogLocation);
        secDialogLocation = findViewById(R.id.secDialogLocation);
        searchArray=new ArrayList<>();



//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                if(!TextUtils.isEmpty(newText)){
//                    Log.e("textViewListner","q = "+newText);
//                   searchArray= valetDatabase.getLocation(newText);
//                    searchView();
//                }
//
//
//                return false;
//            }
//        });


        timeCountDown=findViewById(R.id.timeCountDown);
        firstDialogLocation.setVisibility(View.GONE);
        secDialogLocation.setVisibility(View.GONE);
        endRequestList=findViewById(R.id.endRequestList);
        fromLoc = findViewById(R.id.fromLoc);
        toLoc = findViewById(R.id.toLoc);
        iNeedCaptain = findViewById(R.id.iNeedCaptain);
        cashPayment = findViewById(R.id.cashPayment);
        walletPayment = findViewById(R.id.walletPayment);
        whereGo = findViewById(R.id.whereGo);
        _3DialogLocation = findViewById(R.id._3DialogLocation);//captain list
        _4DialogLocation = findViewById(R.id._4DialogLocation);
        _5DialogLocation = findViewById(R.id._5DialogLocation);
        _6DialogLocation = findViewById(R.id._6DialogLocation);
        _7DialogLocation = findViewById(R.id._7DialogLocation);
        _8DialogLocation = findViewById(R.id._8DialogLocation);//return
        _9DialogLocation = findViewById(R.id._9DialogLocation);//pleaseWait
        _10DialogLocation = findViewById(R.id._10DialogLocation);//please Wait 2
        _11DialogLocation = findViewById(R.id._11DialogLocation);//after time
        _12DialogLocation =findViewById(R.id._12DialogLocation);//qr
        _13DialogLocation =findViewById(R.id._13DialogLocation);//pay
        _14DialogLocation =findViewById(R.id._14DialogLocation);//wait
        _15DialogLocation =findViewById(R.id._15DialogLocation);//rate
        _42DialogLocation=findViewById(R.id._42DialogLocation);//direction
        _161DialogLocation=findViewById(R.id._161DialogLocation);
        End=findViewById(R.id.End);
        Arrive=findViewById(R.id.Arrive);
        callParking=findViewById(R.id.callParking);
        calls=findViewById(R.id.calls);
        selectLinear=findViewById(R.id.selectLinear);
        callInfo=findViewById(R.id.callInfo);
        phoneNo=findViewById(R.id.phoneNo);
        captainName=findViewById(R.id.captainName);
        barcode=findViewById(R.id.barcode);
        rating=findViewById(R.id.rating);
        noteRate=findViewById(R.id.noteRate);
        amountValue=findViewById(R.id.amountValue);
        ratingBar=findViewById(R.id.ratingBar);
        reming=findViewById(R.id.reming);
        captainNameText=findViewById(R.id.captainNameText);
        globalVairable=new GlobalVairable(DriverMapsActivity.this);
        returnButton=findViewById(R.id.returnButton);
        valuePay=findViewById(R.id.valuePay);
      //  go = findViewById(R.id.go);
        list = findViewById(R.id.orderList);
        scanQRCode=findViewById(R.id.scanQRCode);
        listOfView(0);
        arriveTimeText=findViewById(R.id.arriveTimeText);
        navigation=findViewById(R.id.navigation);
        Pay=findViewById(R.id.Pay);
        exportJson =new ExportJson(DriverMapsActivity.this);
        valetDatabase=new ValetDatabase(DriverMapsActivity.this);
        String ids=valetDatabase.getAllSetting();
        cancelSearch.setVisibility(View.GONE);

        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchView.setText("");
                cancelSearch.setVisibility(View.GONE);

            }
        });
        if(TextUtils.isEmpty(ids)){
            listOfView(0);
            flafOpen=0;
            selectLinear.setVisibility(View.VISIBLE);
        }else {
            listOfView(0);
            flafOpen=1;
            selectLinear.setVisibility(View.GONE);

        }

        importJson=new ImportJson(DriverMapsActivity.this);
        endRequest=findViewById(R.id.endRequest);

//        T = new Timer();
//        T.schedule(new TimerTask() {
//            @Override
//            public void run() {
//
//                if(isOk) {
//                    importJson.getStatuss();
//                }
//            }
//
//        }, 0, 1000);
//


        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query=databaseReference.child("ValetAppRealDB").child("StatusValetGroup").child(singUpUserTableGlobal.getId()+"_Client").child("status");

        Query query2=databaseReference.child("ValetAppRealDB").child("StatusValetGroup").child(singUpUserTableGlobal.getId()+"_Client");
//        query2.

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String mychild = dataSnapshot.getValue().toString();
                    // textView.setText(mychild);
//                     ValetFireBaseItem2 mychild = dataSnapshot.getValue(ValetFireBaseItem2.class);

//                     captainId=mychild.getCaptainId();
//                    ListShow(mychild.getStatus());

                    if(mychild.equals("12")){
                        Intent intent =new Intent(DriverMapsActivity.this,DriverMapsActivity.class);

                        showNotification(DriverMapsActivity.this," The Captain Arrive  ","Captain Arrive To Client",intent);

                    }

                    importJson.getStatuss();

                  //  Toast.makeText(DriverMapsActivity.this, "succ " + "mychild", Toast.LENGTH_SHORT).show();

                }catch (Exception y){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        query.addValueEventListener(listener);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query3=databaseReference.child("ValetAppRealDB").child("StatusValetGroup").child(singUpUserTableGlobal.getId()+"_CarInWay");

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

                    if(mychild.equals("1")){
                        Intent intent =new Intent(DriverMapsActivity.this,DriverMapsActivity.class);

                            showNotification(DriverMapsActivity.this,"  Captain   ","Your Car Is On The Way",intent);

                            writeInFireBaseCaptainWay(singUpUserTableGlobal.getId());
                    }

                }catch (Exception y){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        query3.addValueEventListener(listener2);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query67=databaseReference.child("ValetAppRealDB").child("StatusValetGroup").child(singUpUserTableGlobal.getId()+"_QRCode");

//        query2.

        ValueEventListener listener67 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    String mychild = dataSnapshot.getValue().toString();
                    QrGenarater=mychild;
                    // textView.setText(mychild);
//                     ValetFireBaseItem2 mychild = dataSnapshot.getValue(ValetFireBaseItem2.class);

//                     captainId=mychild.getCaptainId();
//                    ListShow(mychild.getStatus());



                }catch (Exception y){
                    QrGenarater="";
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        query67.addValueEventListener(listener67);



        listAdapterOrder = new ListAdapterOrder(DriverMapsActivity.this, clientOrders);
       // list.setAdapter(listAdapterOrder);


        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( Build.VERSION.SDK_INT >= 23){
                    if (ActivityCompat.checkSelfPermission(DriverMapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED  ){
                        requestPermissions(new String[]{
                                        android.Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_CODE_ASK_PERMISSIONS);
                        return ;
                    }
                }

                Directions();
            }
        });

        iNeedCaptain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CaptainClientTransfer cp = new CaptainClientTransfer();
                int pay = 0;
                String go = "null";
                if (cashPayment.isChecked()) {
                    pay = 0;  //cash
                } else {
                    pay = 1;  //wallet
                }

                if (!whereGo.getText().toString().equals("")) {
                    go = whereGo.getText().toString();
                } else {
                    go = "null";
                }

                cp.setClientId(Integer.parseInt(singUpUserTableGlobal.getId()));
                cp.setClientName(singUpUserTableGlobal.getUserName());
                cp.setClientPhoneNo(singUpUserTableGlobal.getPhoneNo());
                cp.setFromLoc("" + LatLngListMarker.get(0));
                cp.setToLocation("" + LatLngListMarker.get(1));
                cp.setLocationName(go);
                cp.setPaymentType(pay);
                cp.setCaptainName("");
                cp.setCaptainId(0);
                cp.setCaptainPhoneNo("");
                cp.setStatus(0);
                cp.setRate("0");
                cp.setTimeOfArraive("");
                cp.setDateOfTranse("");
                cp.setTimeIn("");
                cp.setTimeout("");
                cp.setTimeParkIn("");
                cp.setTimeParkOut("");
                ExportJson exportJson = new ExportJson(DriverMapsActivity.this);
                exportJson.CaptainTransfer(DriverMapsActivity.this, cp);


            }
        });
//        go.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ImportJson importJson = new ImportJson(DriverMapsActivity.this);
//                importJson.getOrder();
//
//                //_3DialogLocation.setVisibility(View.VISIBLE);
//
//            }
//        });


        endRequestList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(DriverMapsActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("Delete Request ");
                sweetAlertDialog.setContentText("Do You Want To Delete The Request?");
                sweetAlertDialog.setConfirmText("Delete");
                sweetAlertDialog.setCancelText("Cancel");
                sweetAlertDialog.setCancelClickListener( new SweetAlertDialog.OnSweetClickListener(){
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
                sweetAlertDialog.setCanceledOnTouchOutside(false);
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        SettingDialog(2);
                        sDialog.dismissWithAnimation();
                    }
                });
                sweetAlertDialog.show();

            }
        });



        endRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(DriverMapsActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("Delete Request ");
                sweetAlertDialog.setContentText("Do You Want To Delete The Request?");
                sweetAlertDialog.setConfirmText("Delete");
                sweetAlertDialog.setCancelText("Cancel");
                sweetAlertDialog.setCancelClickListener( new SweetAlertDialog.OnSweetClickListener(){
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
                sweetAlertDialog.setCanceledOnTouchOutside(false);
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        SettingDialog(2);
                        sDialog.dismissWithAnimation();
                    }
                });
                sweetAlertDialog.show();





            }
        });
        scanQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readBarCode();
            }
        });

//        button1.setOnClickListener(new OnClickListener(){
//
//            @Override
//            public void onClick(View arg0) {
//               // String number=edittext1.getText().toString();
//
//            }
//
//        });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportJson.updateStatus(DriverMapsActivity.this,"8","9");
            }
        });

        Arrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportJson.updateStatus(DriverMapsActivity.this,"151","161");
            }
        });

        End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(DriverMapsActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("Delete Request ");
                sweetAlertDialog.setContentText("Do You Want To Delete The Request?");
                sweetAlertDialog.setConfirmText("Delete");
                sweetAlertDialog.setCancelText("Cancel");
                sweetAlertDialog.setCancelClickListener( new SweetAlertDialog.OnSweetClickListener(){
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
                sweetAlertDialog.setCanceledOnTouchOutside(false);
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        SettingDialog(1);
                        sDialog.dismissWithAnimation();
                    }
                });
                sweetAlertDialog.show();





            }
        });

        late.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LateDialog();
            }
        });

        amountValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().equals("")){
                    reCalculate();
                }else {
                    reming.setText("0.0");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                if(!TextUtils.isEmpty(amountValue.getText().toString())) {
                    if(Double.parseDouble(amountValue.getText().toString())>=Double.parseDouble(valuePay.getText().toString())) {
                        exportJson.updateStatuPay(DriverMapsActivity.this, "13", "14", amountValue.getText().toString(),
                                valuePay.getText().toString(), reming.getText().toString());


                    }else {
                        amountValue.setError("Not Enough");
                        Toast.makeText(DriverMapsActivity.this, "Money Is Not Enough", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               float rate= ratingBar.getRating();
                exportJson.updateStatusRate(DriverMapsActivity.this, "15", "16",""+rate,
                        noteRate.getText().toString());

                noteRate.setText("");
                ratingBar.setRating(0);

            }
        });

        callInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call("0786812709");

            }
        });

        callParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call("0786812709");

            }
        });

        calls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call("0786812709");

            }
        });

//        location(0);
    }

    public void searchText(int index){


        searchView.setText(searchArray.get(index));
        searchList.setVisibility(View.GONE);
        if (flafOpen == 0) {
            searchFunction(1);
        }
    }

    public  void searchView (){
        if(searchArray.size()!=0){
            searchList.setVisibility(View.VISIBLE);
            listAdapterSearch=new ListAdapterSearch(DriverMapsActivity.this,searchArray);
            searchList.setAdapter(listAdapterSearch);
           // searchFunction();
        }else {
            searchArray.clear();
            searchList.setVisibility(View.GONE);

        }


    }
    void CountTime() {

        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                timeCountDown.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext

            }

            public void onFinish() {
                timeCountDown.setText("00:00:00");
               // regenaratQr.setVisibility(View.VISIBLE);
                late.setBackground(getResources().getDrawable(R.drawable.bac_search_black));
                late.setEnabled(true);
            }

        }.start();

    }

    void CallPer (){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+captainClientTransfer.getCaptainPhoneNo()));
        startActivity(callIntent);
    }

    void Call (String number){

        if(isPermissionGranted()){
            // You can use the API that requires the permission.
            CallPer();
        }else {
            Toast.makeText(this, "Please Enable Call Phone Permission", Toast.LENGTH_SHORT).show();
        }


    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.requestButton:
//                   //barcodeOpen(); progress dialog
////                    if(LatLngListMarker.size()==1) {
//                        openProgressDialog();
////                    }else {
////                        Toast.makeText(DriverMapsActivity.this, "Please Where are You Go ??", Toast.LENGTH_SHORT).show();
////                    }
//
//                    break;

//                case R.id.scanBarcode:
//                    readBarCode();
//                    break;

            }
        }
    };
    public void readBarCode() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED) {
            //Log.d("","Permission not available requesting permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_USE_CAMERA);
        } else {
            //Log.d(TAG,"Permission has already granted");
            barcodeReadPer();
        }

    }

    public void barcodeReadPer() {
//        barCodTextTemp = itemCodeText;
        Log.e("barcode_099", "in");
        IntentIntegrator intentIntegrator = new IntentIntegrator(DriverMapsActivity.this);
        intentIntegrator.setDesiredBarcodeFormats(intentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setPrompt("SCAN");
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (Result != null) {
            if (Result.getContents() == null) {
                Log.d("MainActivity", "cancelled scan");
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
//                TostMesage(getResources().getString(R.string.cancel));
            } else {

                Log.e("MainActivity", "" + Result.getContents());
              //  Toast.makeText(this, "Scan ___" + Result.getContents()+"   "+QrGenarater, Toast.LENGTH_SHORT).show();

                String result=Result.getContents().toString();
                if(result.equals(QrGenarater)){

                    if(flagIp==1) {
                        exportJson.updateStatus(DriverMapsActivity.this, "6", "7");
                    }else if(flagIp==2){
                        exportJson.updateStatus(DriverMapsActivity.this,"12","13");
                    }

                }else {
                    Toast.makeText(this, "Not Same Captain Please Try Again !!!", Toast.LENGTH_SHORT).show();
                }
                
//                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(DriverMapsActivity.this, SweetAlertDialog.SUCCESS_TYPE);
//                sweetAlertDialog.setTitleText("Barcode Reader");
//                sweetAlertDialog.setContentText("The identity of the captain has been confirmed");
//                sweetAlertDialog.setConfirmText("Ok");
//                sweetAlertDialog.setCanceledOnTouchOutside(false);
//                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @SuppressLint("WrongConstant")
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        finish();
////      startRequest.setVisibility(View.GONE);
////        ReqRequest.setVisibility(View.VISIBLE);
//                        sDialog.dismissWithAnimation();
//                    }
//                });
//                sweetAlertDialog.show();
                Log.e("SweetAlertDialog 724", "" + "JSONTask");
            }


        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }


    void tracking() {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (flag) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // globelFunction.getSalesManInfo(SalesmanMapsActivity.this,2);
                            v1 = v1 + 0.000010;
                            v2 = v2 + 0.000010;

                            Log.e("Location", "loc" + v1 + "  " + v2 + "   " + a1 + "   " + a2);
                            LatLng latLng = new LatLng(v1, v2);
                            LatLng latLng2 = new LatLng(a1, a2);
                            LatLngListMarker.clear();
                            LatLngListMarker.add(latLng);
                            LatLngListMarker.add(latLng2);
                            location(1);

                        }
                    });

                }
//
            }

        }, 0, 1000);
    }

    void openProgressDialog() {
        pdaSweet = new SweetAlertDialog(DriverMapsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pdaSweet.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
        pdaSweet.setTitleText("Process...");
        pdaSweet.setCancelable(false);
        pdaSweet.show();

        for (int i = 0; i < 300000000; i++) {

        }
        pdaSweet.dismissWithAnimation();

//openDialogOfCaption();

    }


//    void openDialogOfCaption(){
//
//
//        final Dialog dialog = new Dialog(com.example.valetapp.DriverMapsActivity.this,R.style.Theme_Dialog);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.caption_dialog_info);
//
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        TextView accept=dialog.findViewById(R.id.accept);
//        accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                scanBarcode.setVisibility(View.VISIBLE);
//                requestButton.setVisibility(View.GONE);
//                tracking();
// dialog.dismiss();
//            }
//        });
//        dialog.show();
//
//    }
//    void barcodeOpen (){
//        final Dialog dialog = new Dialog(DriverMapsActivity.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.qr_);
//
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        Button endParking=dialog.findViewById(R.id.endParking);
//        endParking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
////                Intent intent =new Intent(MapsActivity.this,MainValetActivity.class);
////                startActivity(intent);
//            }
//        });
//        dialog.show();
//    }

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
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        mMap = googleMap;
     //  initialization();
location(0);
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//
//            @Override
//            public void onMapClick(LatLng latLng) {
//
//                // Creating a marker
//                MarkerOptions markerOptions = new MarkerOptions();
//
//                // Setting the position for the marker
//                markerOptions.position(latLng);
//
//                // Setting the title for the marker.
//                // This will be displayed on taping the marker
//                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
//
//                Log.e("locationss",""+latLng.latitude + " : " + latLng.longitude);
//                // Clears the previously touched position
//                googleMap.clear();
//
//                // Animating to the touched position
//                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//
//                // Placing a marker on the touched position
//                googleMap.addMarker(markerOptions);
//            }
//        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                if (flafOpen == 0) {
                    // Already two locations
//                    if (LatLngListMarker.size() > 1) {
                        LatLngListMarker.clear();
                        mMap.clear();
//                    }

                    // Adding new item to the ArrayList
                    selectLinear.setVisibility(View.GONE);

                    LatLng latLng = new LatLng(v1, v2);
                    LatLngListMarker.add(latLng);
                    LatLngListMarker.add(point);
                    a1 = point.latitude;
                    a2 = point.longitude;
                    // Creating MarkerOptions

                    MarkerOptions options = new MarkerOptions();

                    // Setting the position of the marker
                    options.position(point);


                    /**
                     * For the start location, the color of marker is GREEN and
                     * for the end location, the color of marker is RED.
                     */
                    if (LatLngListMarker.size() == 1) {
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    } else if (LatLngListMarker.size() == 2) {
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }

                    // Add new marker to the Google Map Android API V2
                    // mMap.addMarker(options);

                    // Checks, whether start and end locations are captured
//                if(LatLngListMarker.size() >= 2){
//                    mOrigin = LatLngListMarker.get(0);
//                    mDestination = LatLngListMarker.get(1);
//                    drawRoute();
//                }


                    location(0);

                    firstDialogLocations();
                }
            }
        });
     //   location(0);
    }


    String getNameFromLocation (double Lat,double Long){

        String name ="";
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(Lat, Long, 1);
        } catch (IOException e) {
            Log.e("whereGo","Error");
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
           name=""+addresses.get(0).getAddressLine(0);
            Log.e("whereGo",""+addresses.get(0).getCountryName());
        }
        else {
            // do your stuff
            Log.e("whereGo","noThinfg");
        }

        return  name;

    }

    private void firstDialogLocations() {
        if (LatLngListMarker.size() != 0) {
            String firName=getNameFromLocation(LatLngListMarker.get(0).latitude,LatLngListMarker.get(0).longitude);
            String toName=getNameFromLocation(LatLngListMarker.get(1).latitude,LatLngListMarker.get(1).longitude);

            fromLoc.setText(firName);
            toLoc.setText(toName);
        }

        if (secDialogLocation.getVisibility() != View.VISIBLE) {
            firstDialogLocation.setVisibility(View.VISIBLE);
        }
    }

    void drawPolyLine(){
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(LatLngListMarker.get(0), LatLngListMarker.get(1))
                .width(3)
                .color(Color.RED));
    }

    public void location(int move) {

        try {

            if (move == 1) {

                mMap.clear();
            }
        } catch (Exception e) {
            Log.e("Problem", "problennnn" + e.getMessage());
        }

        // Add a marker in Sydney and move the camera
        Log.e("mmmmmm", "locationCall  "+LatLngListMarker.size()+"  "+move);
        LatLng sydney = null;
//        try {

            for (int i = 0; i < LatLngListMarker.size(); i++) {

                //if (!salesManInfosList.get(i).getLatitudeLocation().equals("0") && !salesManInfosList.get(i).getLongitudeLocation().equals("0")) {
                sydney = LatLngListMarker.get(i);
String name="";
                if(i==0){
                    name="From : "+getNameFromLocation(LatLngListMarker.get(i).latitude,LatLngListMarker.get(i).longitude);
                }else {
                    name="To : "+getNameFromLocation(LatLngListMarker.get(i).latitude,LatLngListMarker.get(i).longitude);
                }

                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconSize())).position(sydney).title(name));
                builder.include(sydney);
                //}
            }
//        }catch (Exception e){
//            LatLng latLng = new LatLng(0, 0);
//
//            LatLngListMarker.add(latLng);
//            sydney = LatLngListMarker.get(i);
//
//            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconSize())).position(sydney).title("aaa"));
//            builder.include(sydney);
//        }
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(sydney));
        if (move == 0) {
            try {
//                mMap.clear();
                bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
                mMap.animateCamera(cu);
            } catch (Exception e) {
                Log.e("Problem33", "problennnn 2 " + e.getMessage());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));
            }
        }
        flag = true;
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 0
//
//        );
    }

    public void reCalculate(){

        if(!TextUtils.isEmpty(amountValue.getText().toString())){
                String val=valuePay.getText().toString();
            String val2=amountValue.getText().toString();
               double  result=Double.parseDouble(val)-Double.parseDouble(val2);
               reming.setText(result+"");
        }else {

            amountValue.setError("Required !!");


        }
    }

    public void VisibilityLinear() {
        flafOpen = 1;
        secDialogLocation.setVisibility(View.VISIBLE);
        firstDialogLocation.setVisibility(View.GONE);
    }

    Bitmap iconSize() {
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.car_icon);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        return smallMarker;
    }

    public void listOfClient() {
        listOfView(3);
        listAdapterOrder.setItemsList(clientOrders);
        list.setAdapter(listAdapterOrder);
        if(_3DialogLocation.getVisibility()==View.GONE) {
            _3DialogLocation.setVisibility(View.VISIBLE);
        }
    }

    public void listOfClientClear() {
        listOfView(2);
        listAdapterOrder.setItemsList(clientOrders);
        list.setAdapter(listAdapterOrder);
        _3DialogLocation.setVisibility(View.GONE);
        secDialogLocation.setVisibility(View.VISIBLE);

    }

    public synchronized void refreshData(List<ClientOrder> items) {
        clientOrders = items;
        list.setAdapter(listAdapterOrder);
       // listAdapterOrder.notifyDataSetChanged();
    }

    public void ListShow(String id){

        switch (id){

            case "1":
                break;

            case "2":
                if(secDialogLocation.getVisibility()==View.GONE) {
                    listOfView(0);
                    secDialogLocation.setVisibility(View.VISIBLE);
                }
                break;

            case "3":
                ImportJson importJson = new ImportJson(DriverMapsActivity.this);
                importJson.getOrder();
                if(_3DialogLocation.getVisibility()==View.GONE) {
                    listOfView(0);
                    _3DialogLocation.setVisibility(View.VISIBLE);
                    importJson.getOrder();
                }else {
                    importJson.getOrder();
                }

                break;

            case "4":
                if(_42DialogLocation.getVisibility()==View.GONE) {
                    listOfView(0);
                    _42DialogLocation.setVisibility(View.VISIBLE);
                }
                break;

            case "5":
                if(_4DialogLocation.getVisibility()==View.GONE) {
                    listOfView(0);
                    arriveTimeText.setText(arriveTime);
                    _4DialogLocation.setVisibility(View.VISIBLE);
                }
                break;
            case "161":
                if(_161DialogLocation.getVisibility()==View.GONE) {
                    listOfView(0);

                    _161DialogLocation.setVisibility(View.VISIBLE);
                }
                break;
            case "6":
                if(_5DialogLocation.getVisibility()==View.GONE) {
                    listOfView(0);
                    captainName.setText(captainClientTransfer.getCaptainName());
                    phoneNo.setText(captainClientTransfer.getCaptainPhoneNo());
                    flagIp=1;
                    _5DialogLocation.setVisibility(View.VISIBLE);
                }
                break;

            case "7":
                if(_6DialogLocation.getVisibility()==View.GONE) {
                    listOfView(0);
                    _6DialogLocation.setVisibility(View.VISIBLE);
                }
                break;

            case "8":
                if(_8DialogLocation.getVisibility()==View.GONE) {
                    listOfView(0);
                    globalText.setText("I need My Car");

                    _8DialogLocation.setVisibility(View.VISIBLE);
                }
                break;
            case "9":
                if(_9DialogLocation.getVisibility()==View.GONE) {
                    listOfView(0);
                    late.setBackground(getResources().getDrawable(R.drawable.bac_not_active));
                    late.setEnabled(false);
                    CountTime();
                    _9DialogLocation.setVisibility(View.VISIBLE);
                }
                break;
            case "10":
                if(_10DialogLocation.getVisibility()==View.GONE) {
                    listOfView(0);
                    _10DialogLocation.setVisibility(View.VISIBLE);
                }
                break;

//            case "11":
//                if(_11DialogLocation.getVisibility()==View.GONE) {
//                    _11DialogLocation.setVisibility(View.VISIBLE);
//                }
//                break;


            case "12":
                if(_5DialogLocation.getVisibility()==View.GONE) { //_12
                    listOfView(0);
                    flagIp=2;
                    //generatorQRCode();
                    _5DialogLocation.setVisibility(View.VISIBLE);
                }
                break;

            case "13":
                if(_13DialogLocation.getVisibility()==View.GONE) {
                    listOfView(0);
                    // valuePay.setText("");
                    amountValue.setText("45.54");
                    reming.setText("");
                    _13DialogLocation.setVisibility(View.VISIBLE);
                }
                break;
            case "14":
                if(_14DialogLocation.getVisibility()==View.GONE) {
                    listOfView(0);
                    _14DialogLocation.setVisibility(View.VISIBLE);
                }
                break;

            case "15":
                if(_15DialogLocation.getVisibility()==View.GONE) {
                    listOfView(0);
                    captainNameText.setText(captainClientTransfer.getCaptainName());
                    _15DialogLocation.setVisibility(View.VISIBLE);
                }
                break;

            case "16":

                break;
        }
    }


    public void generatorQRCode(){

        try {
            Bitmap bitmaps = globalVairable.encodeAsBitmap(""+singUpUserTableGlobal.getId(), BarcodeFormat.QR_CODE, 200, 200);
            barcode.setImageBitmap(bitmaps);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    public void returnGo(){
        flafOpen=0;
    }
    public void listOfView(int y) {

        switch (y) {
            case 0:
                GoneApp();

                break;
            case 1:
                GoneApp();

                firstDialogLocation.setVisibility(View.VISIBLE);

                break;
            case 2:
                GoneApp();

                secDialogLocation.setVisibility(View.VISIBLE);

                break;
            case 3:
                GoneApp();

                _3DialogLocation.setVisibility(View.VISIBLE);

                break;
            case 4:
                GoneApp();

                _42DialogLocation.setVisibility(View.VISIBLE);

                break;
            case 5:
                GoneApp();

                _4DialogLocation.setVisibility(View.VISIBLE);

                break;
            case 6:
                GoneApp();

                _5DialogLocation.setVisibility(View.VISIBLE);

                break;
            case 7:
                GoneApp();

                _6DialogLocation.setVisibility(View.VISIBLE);

                break;
            case 8:
                GoneApp();

                _7DialogLocation.setVisibility(View.VISIBLE);

                break;
            case 9:
                GoneApp();

                _8DialogLocation.setVisibility(View.VISIBLE);

                break;
            case 10:
                GoneApp();

                _9DialogLocation.setVisibility(View.VISIBLE);

                break;
            case 11:
                GoneApp();

                _10DialogLocation.setVisibility(View.VISIBLE);

                break;
            case 12:
                GoneApp();

                _11DialogLocation.setVisibility(View.VISIBLE);

                break;
            case 13:
                GoneApp();

                _12DialogLocation.setVisibility(View.VISIBLE);

                break;
            case 14:
                GoneApp();

                _13DialogLocation.setVisibility(View.VISIBLE);

                break;
            case 15:
                GoneApp();

                _14DialogLocation.setVisibility(View.VISIBLE);

                break;
            case 16:
                GoneApp();
                _15DialogLocation.setVisibility(View.VISIBLE);

                break;

            case 555:
                GoneApp();
                finish();

                break;


        }


    }

    void GoneApp(){
        selectLinear.setVisibility(View.GONE);
        firstDialogLocation.setVisibility(View.GONE);
        secDialogLocation.setVisibility(View.GONE);
        _3DialogLocation.setVisibility(View.GONE);
        _4DialogLocation.setVisibility(View.GONE);
        _5DialogLocation.setVisibility(View.GONE);
        _6DialogLocation.setVisibility(View.GONE);
        _7DialogLocation.setVisibility(View.GONE);
        _8DialogLocation.setVisibility(View.GONE);
        _9DialogLocation.setVisibility(View.GONE);
        _10DialogLocation.setVisibility(View.GONE);
        _11DialogLocation.setVisibility(View.GONE);
        _12DialogLocation.setVisibility(View.GONE);
        _13DialogLocation.setVisibility(View.GONE);
        _14DialogLocation.setVisibility(View.GONE);
        _15DialogLocation.setVisibility(View.GONE);
        _42DialogLocation.setVisibility(View.GONE);
        _161DialogLocation.setVisibility(View.GONE);
        globalText.setText("Start Request");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Directions();
                } else {
                    // Permission Denied
                    Toast.makeText( this,"Please Enable Access to Location " , Toast.LENGTH_SHORT)
                            .show();
                }
                break;

            case REQUEST_PHONE_CALL :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CallPer();
                } else {
                    Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;

            case MY_PERMISSIONS_REQUEST_USE_CAMERA:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //  Log.d(TAG,"permission was granted! Do your stuff");
                    barcodeReadPer();
                } else {
                    Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
                    // Log.d(TAG,"permission denied! Disable the function related with permission.");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void Directions(){
        String a=captainClientTransfer.getToLocation().replace("lat/lng:","").replace("(","").replace(")","").replace(" ","");

        Log.e("hh",""+a);

        String[] latLong =a.split(",");
        Log.e("hh[] = ",""+latLong[0]+"/"+latLong[1]);
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+latLong[0]+","+latLong[1] + "&mode=d");
        Intent mapIntent =new  Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }else {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr="+latLong[0]+","+latLong[1]));
            startActivity(intent);
        }
    }


    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }




    public void writeInFireBaseClient(ValetFireBaseItem valetFireBase){
        ValetFireBaseItem valetFireBaseItem=valetFireBase;
        db= FirebaseDatabase.getInstance();
        databaseReference=db.getReference("ValetAppRealDB");
        databaseReference.child("StatusValetGroup").child(singUpUserTableGlobal.getId()+"_Client").setValue(valetFireBaseItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
          //      Toast.makeText(DriverMapsActivity.this, "Successful", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void writeInFireBaseIneedCap(String value){
        db= FirebaseDatabase.getInstance();
        databaseReference=db.getReference("ValetAppRealDB");
//        databaseReference.child("StatusValetGroup").child("INeedCaptain").setValue("0");

        databaseReference.child("StatusValetGroup").child("INeedCaptain").setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
        //        Toast.makeText(DriverMapsActivity.this, "Successful", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void writeInFireBaseDeleteRequest(String value){
        db= FirebaseDatabase.getInstance();
        databaseReference=db.getReference("ValetAppRealDB");

        databaseReference.child("StatusValetGroup").child("DeleteRequest").setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
       //        Toast.makeText(DriverMapsActivity.this, "Successful", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void updateInFireBaseClient(String valetFireBase ,String captName ,String captId,String captPhoneNo){

        db= FirebaseDatabase.getInstance();
        databaseReference=db.getReference("ValetAppRealDB");
        databaseReference.child("StatusValetGroup").child(singUpUserTableGlobal.getId()+"_Client").child("status").setValue(valetFireBase).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
       //         Toast.makeText(DriverMapsActivity.this, "Successful", Toast.LENGTH_SHORT).show();
            }
        });

        if(valetFireBase.equals("4")){

            Log.e("bba","jh"+captId+"  "+captName);
            databaseReference.child("StatusValetGroup").child(singUpUserTableGlobal.getId()+"_Client").child("captainName").setValue(captName);
            databaseReference.child("StatusValetGroup").child(singUpUserTableGlobal.getId()+"_Client").child("captainId").setValue(captId);
            databaseReference.child("StatusValetGroup").child(singUpUserTableGlobal.getId()+"_Client").child("captainPhoneNo").setValue(captPhoneNo);

        }


//        databaseReference.child("ValetAppRealDB/StatusValetGroup/"+singUpUserTableGlobal.getId()+"_Client"+"/status").setValue("red");

    }

    public void updateInFireBaseClients(String valetFireBase ){

        db= FirebaseDatabase.getInstance();
        databaseReference=db.getReference("ValetAppRealDB");
        databaseReference.child("StatusValetGroup").child(singUpUserTableGlobal.getId()+"_Client").child("status").setValue(valetFireBase).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
     //           Toast.makeText(DriverMapsActivity.this, "Successful", Toast.LENGTH_SHORT).show();
            }
        });

    }



    public void UpdateInFireBaseCaptain(String valetFireBase,String captainId){
//        ValetFireBaseItem valetFireBaseItem=valetFireBase;
        db= FirebaseDatabase.getInstance();
        databaseReference=db.getReference("ValetAppRealDB");
        databaseReference.child("StatusValetGroup").child(captainId+"_VALET").child("status").setValue(valetFireBase).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
        //        Toast.makeText(DriverMapsActivity.this, "Successful", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void UpdateInFireBaseCaptainReturn(String valetFireBase,String captainId){
//        ValetFireBaseItem valetFireBaseItem=valetFireBase;
        db= FirebaseDatabase.getInstance();
        databaseReference=db.getReference("ValetAppRealDB");
        databaseReference.child("StatusValetGroup").child(captainId+"_VALET").child("ifReturn").setValue(valetFireBase).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
          //      Toast.makeText(DriverMapsActivity.this, "Successful", Toast.LENGTH_SHORT).show();
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

    public void writeInFireBaseCaptainWay( String clientId){
        db= FirebaseDatabase.getInstance();
        databaseReference=db.getReference("ValetAppRealDB");
        databaseReference.child("StatusValetGroup").child(singUpUserTableGlobal.getId()+"_CarInWay").setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
         //       Toast.makeText(DriverMapsActivity.this, "Successful", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void deleteClient(){
        databaseReference=db.getReference("ValetAppRealDB");
        databaseReference.child("StatusValetGroup").child(singUpUserTableGlobal.getId()+"_Client").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(DriverMapsActivity.this, "Successful Delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void writeInFireBaseCaptainLate(String note){
        db= FirebaseDatabase.getInstance();
        databaseReference=db.getReference("ValetAppRealDB");
        databaseReference.child("StatusValetGroup").child(captainId+"_LATE").setValue(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
         //       Toast.makeText(DriverMapsActivity.this, "Successful", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void SettingDialog(int flag) {
        Dialog packingListDialog = new Dialog(DriverMapsActivity.this,R.style.Theme_Dialog);
        packingListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        packingListDialog.setContentView(R.layout.dialog_cancel_reson);
        packingListDialog.setCancelable(false);
        packingListDialog.setCanceledOnTouchOutside(false);
        packingListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText reason;
        Button accept, cancel;

        reason = packingListDialog.findViewById(R.id.reason);
        accept = packingListDialog.findViewById(R.id.accept);
        cancel = packingListDialog.findViewById(R.id.rej);


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(reason.getText().toString())) {

                    switch (flag){
                        case 1:
                            exportJson.updateStatusDelete(DriverMapsActivity.this,reason.getText().toString());

                            break;
                        case 2:
                            exportJson.updateStatusDeleteBeforeAcc(DriverMapsActivity.this,reason.getText().toString());

                            break;

                    }

                    packingListDialog.dismiss();
                } else {
                    reason.setError("Required !");
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

    void LateDialog() {
        Dialog packingListDialog = new Dialog(DriverMapsActivity.this,R.style.Theme_Dialog);
        packingListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        packingListDialog.setContentView(R.layout.dialog_note_reson);
        packingListDialog.setCancelable(false);
        packingListDialog.setCanceledOnTouchOutside(false);
        packingListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText reason;
        Button accept, cancel;

        reason = packingListDialog.findViewById(R.id.reason);
        accept = packingListDialog.findViewById(R.id.accept);
        cancel = packingListDialog.findViewById(R.id.rej);


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(reason.getText().toString())) {

                   exportJson.lateNote(reason.getText().toString());
                    writeInFireBaseCaptainLate(reason.getText().toString()+"/"+singUpUserTableGlobal.getUserName());

                    packingListDialog.dismiss();
                } else {
                    reason.setError("Required !");
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



}
