package com.example.valetappsec;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.valetappsec.Adapter.ListAdapterOrder;
import com.example.valetappsec.Json.ExportJson;
import com.example.valetappsec.Json.ImportJson;
import com.example.valetappsec.Model.CaptainClientTransfer;
import com.example.valetappsec.Model.ClientOrder;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.valetappsec.GlobalVairable.arriveTime;
import static com.example.valetappsec.GlobalVairable.captainClientTransfer;
import static com.example.valetappsec.GlobalVairable.captainId;
import static com.example.valetappsec.GlobalVairable.clientOrders;
import static com.example.valetappsec.GlobalVairable.isOk;
import static com.example.valetappsec.GlobalVairable.singUpUserTableGlobal;


public class LocationMapsActivity extends FragmentActivity  implements OnMapReadyCallback {

    private GoogleMap mMap;
    // Button requestButton,scanBarcode;
    public static List<LatLng> LatLngListMarker;
    private LatLngBounds.Builder builder;
    LatLngBounds bounds;
    boolean flag = false;
    Timer timer;
    RatingBar ratingBar;
    double v1 = 0, v2 = 0, a1 = 0.0, a2 = 0.0;
    SweetAlertDialog pdaSweet;
    LinearLayout _8DialogLocation;

    Button carDirection;
   // Button callInfo,callParking;
    int flafOpen = 0;
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
   Button searchViewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.valet_driver_map_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);





        mapFragment.getMapAsync(this);

        initialization();


    }

    private void initialization() {

        //  requestButton=findViewById(R.id.requestButton);
//        scanBarcode=findViewById(R.id.scanBarcode);
        //     requestButton.setOnClickListener(onClickListener);
//        scanBarcode.setOnClickListener(onClickListener);

//        scanBarcode.setVisibility(View.GONE);
        // requestButton.setVisibility(View.VISIBLE);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;
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
        }
        Log.e("LocationLanLag", "  loo");
        Log.e("LocationLanLag", "  n  " + v1 + "   " + v2);

        LatLngListMarker = new ArrayList<>();
        LatLngListMarker.clear();
        LatLng latLng = new LatLng(v1, v2);
        LatLngListMarker.add(latLng);
        builder = new LatLngBounds.Builder();
        carDirection=findViewById(R.id.direc);

        carDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( Build.VERSION.SDK_INT >= 23){
                    if (ActivityCompat.checkSelfPermission(LocationMapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
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
        _8DialogLocation = findViewById(R.id._8DialogLocation);//return

      //  callInfo=findViewById(R.id.callInfo);


        globalVairable=new GlobalVairable(LocationMapsActivity.this);

        //listOfView(0);

        exportJson =new ExportJson(LocationMapsActivity.this);
        valetDatabase=new ValetDatabase(LocationMapsActivity.this);
        String ids=valetDatabase.getAllSetting();

//        searchViewImage=findViewById(R.id.searchViewImage);
//searchViewImage.setOnClickListener(onClickListener);


//        if(TextUtils.isEmpty(ids)){
//            listOfView(0);
//            flafOpen=0;
//            selectLinear.setVisibility(View.VISIBLE);
//        }else {
//            listOfView(0);
//            flafOpen=1;
//            selectLinear.setVisibility(View.GONE);
//
//        }

        importJson=new ImportJson(LocationMapsActivity.this);

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

//
//        callInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Call("0786812709");
//
//            }
//        });


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
//
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

//                case R.id.searchViewImage:
//                 //  loc();
//                    break;

            }
        }
    };

//    public void loc(){
//        String location = searchView.getQuery().toString();
//
//        // below line is to create a list of address
//        // where we will store the list of all address.
//        List<Address> addressList = null;
//
//        Log.e("searchView"," searchView =");
//        // checking if the entered location is null or not.
//        if (location != null || location.equals("")) {
//            // on below line we are creating and initializing a geo coder.
//            Geocoder geocoder = new Geocoder(LocationMapsActivity.this);
//            Log.e("searchView", " searchView = 1 ");
//            try {
//                // on below line we are getting location from the
//                // location name and adding that location to address list.
//                addressList = geocoder.getFromLocationName(location, 1);
//                Log.e("searchView", " searchView =2 ");
//
//            } catch (IOException e) {
//                Log.e("searchView", " searchView = 3 ");
//
//                e.printStackTrace();
//            }
//            // on below line we are getting the location
//            // from our list a first position.
//            Address address = addressList.get(0);
//            Log.e("searchView", " searchView = 4 ");
//
//            // on below line we are creating a variable for our location
//            // where we will add our locations latitude and longitude.
//            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//
//            // on below line we are adding marker to that position.
//            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
//
//            // below line is to animate camera to that position.
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//        }
//    }
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
        IntentIntegrator intentIntegrator = new IntentIntegrator(LocationMapsActivity.this);
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
                Toast.makeText(this, "Scan ___" + Result.getContents()+"   "+captainId, Toast.LENGTH_SHORT).show();

                String result=Result.getContents().toString();
                if(result.equals(captainId)){

                    exportJson.updateStatus(LocationMapsActivity.this,"6","7");


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
        pdaSweet = new SweetAlertDialog(LocationMapsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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

//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng point) {
//
//                if (flafOpen == 0) {
//                    // Already two locations
//                    if (LatLngListMarker.size() > 1) {
//                        LatLngListMarker.clear();
//                        mMap.clear();
//                    }
//
//                    // Adding new item to the ArrayList
//                   // selectLinear.setVisibility(View.GONE);
//
//                    LatLng latLng = new LatLng(v1, v2);
//                    LatLngListMarker.add(latLng);
//                    LatLngListMarker.add(point);
//                    a1 = point.latitude;
//                    a2 = point.longitude;
//                    // Creating MarkerOptions
//                    MarkerOptions options = new MarkerOptions();
//
//                    // Setting the position of the marker
//                    options.position(point);
//
//
//                    /**
//                     * For the start location, the color of marker is GREEN and
//                     * for the end location, the color of marker is RED.
//                     */
//                    if (LatLngListMarker.size() == 1) {
//                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                    } else if (LatLngListMarker.size() == 2) {
//                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                    }
//
//                    // Add new marker to the Google Map Android API V2
//                    // mMap.addMarker(options);
//
//                    // Checks, whether start and end locations are captured
////                if(LatLngListMarker.size() >= 2){
////                    mOrigin = LatLngListMarker.get(0);
////                    mDestination = LatLngListMarker.get(1);
////                    drawRoute();
////                }
//
//
//                    location(0);
//
//                    //firstDialogLocations();
//                }
//            }
//        });
        //location(0);
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
        Log.e("mmmmmm", "locationCall");
        LatLng sydney = null;
//        try {


//            for (int i = 0; i < LatLngListMarker.size(); i++) {

                //if (!salesManInfosList.get(i).getLatitudeLocation().equals("0") && !salesManInfosList.get(i).getLongitudeLocation().equals("0")) {
        try {
            String a = captainClientTransfer.getParkingLocation().replace("lat/lng:", "").replace("(", "").replace(")", "").replace(" ", "");

            Log.e("hh", "" + a);

            String[] latLong = a.split(",");
            Log.e("hh[] = ", "" + latLong[0] + "/" + latLong[1]);
            LatLng lat = new LatLng(Double.parseDouble(latLong[0]), Double.parseDouble(latLong[1]));
            sydney = lat;
        }catch (Exception e){
           // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));
        }
        try {
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconSize())).position(sydney).title("My Location"));
            builder.include(sydney);

                //}
//            }
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
                bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 20);
                mMap.animateCamera(cu);
            } catch (Exception e) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 20));
            }
        }

        }catch (Exception e){
            Toast.makeText(this, "Your Car Not Parking ", Toast.LENGTH_SHORT).show();
        }
        flag = true;
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 0));
//
//        );
    }


    Bitmap iconSize() {
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        return smallMarker;
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
            if (checkSelfPermission(Manifest.permission.CALL_PHONE)
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


}
