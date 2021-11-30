package com.example.valetappsec.Json;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.valetappsec.DriverMapsActivity;
import com.example.valetappsec.LogInActivity;
import com.example.valetappsec.MainValetActivity;
import com.example.valetappsec.Model.ClientOrder;
import com.example.valetappsec.Model.SingUpClientModel;
import com.example.valetappsec.Model.ValetFireBaseItem;
import com.example.valetappsec.ProfileActivity;
import com.example.valetappsec.ValetDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.valetappsec.GlobalVairable.arriveTime;
import static com.example.valetappsec.GlobalVairable.captainClientTransfer;
import static com.example.valetappsec.GlobalVairable.captainId;
import static com.example.valetappsec.GlobalVairable.clientOrders;
import static com.example.valetappsec.GlobalVairable.globalText;
import static com.example.valetappsec.GlobalVairable.ids;
import static com.example.valetappsec.GlobalVairable.isOk;
import static com.example.valetappsec.GlobalVairable.singUpUserTableGlobal;

public class ImportJson {
    SweetAlertDialog swALogIn,swAStusus,swAStususRej;
Context context;
    String URL_TO_HIT;
    ValetDatabase valetDatabase;
    public ImportJson(Context context) {
        this.context=context;
        valetDatabase=new ValetDatabase(context);
    }

    public void logInAuth(String userName,String password){
        new LogInAuthAsync(userName,password).execute();
    }

    public void getOrder(){
        new GetOrder().execute();
    }

    public  void getRaw(){
        new getRaw().execute();
    }
    public  void getImage(int flag){
        new BitmapImage2(flag).execute();
    }

    public void updateStatus(Context context,ClientOrder clientOrder){
        new updateStatus(context,clientOrder).execute();
    }

    public void updateStatusRej(Context context,ClientOrder clientOrder){
        new updateStatusRej(context,clientOrder).execute();
    }



    public void getStatuss(){
        new GetStatus().execute();
    }

    private class LogInAuthAsync extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        private String userName = "",password="";


        public LogInAuthAsync(String userName,String password) {
            this.userName = userName;
            this.password=password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swALogIn = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            swALogIn.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            swALogIn.setTitleText("PleaseWait" );
            swALogIn.setCancelable(false);
            try {
                swALogIn.show();
            }catch (Exception e){

            }
            isOk=false;
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String ip =valetDatabase.getAllIPSetting();
                String link = "http://"+ip+"/api/ValCaptain/getClientAuthuraization?";


                String data = "userName="+userName+
                        "&password="+password;
                URL url = new URL(link+data );


//
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setRequestMethod("GET");
//                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes(data);
//                wr.flush();
//                wr.close();
                Log.e("url____",""+link+data);

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "TAG_itemSwitch -->" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            swALogIn.dismissWithAnimation();
            JSONObject result = null;
            String impo = "";
            boolean found=false;
            if (s != null) {
                if (s.contains("PhoneNo")) {
                    s=s.replace("<br /><b>Warning</b>:  oci_connect(): OCI_SUCCESS_WITH_INFO: ORA-28002: the password will expire within 7 days in <b>C:\\xampp\\htdocs\\importt.php</b> on line <b>3</b><br />","");

                    try {
                        JSONObject j=new JSONObject(s);

                        SingUpClientModel singUpUserTable=new SingUpClientModel();


                        //{
                        //    "id": 1,
                        //    "UserName": "rawan",
                        //    "Password": "1234",
                        //    "Email": "raw@gmai.com",
                        //    "PhoneNo": " 962786812709",
                        //    "CarType": "ford",
                        //    "CarModel": "viosion",
                        //    "CarColor": "red",
                        //    "CarLot": "2554k",
                        //    "CarPic": "pa"
                        //}
                        singUpUserTable.setId(j.getString("id"));
                        singUpUserTable.setUserName(j.getString("UserName"));
                        singUpUserTable.setPassword(j.getString("Password"));
                        singUpUserTable.setEmail(j.getString("Email"));
                        singUpUserTable.setPhoneNo(j.getString("PhoneNo"));
                        singUpUserTable.setCarType(j.getString("CarType"));
                        singUpUserTable.setCarModel(j.getString("CarModel"));
                        singUpUserTable.setCarColor(j.getString("CarColor"));
                        singUpUserTable.setCarLot(j.getString("CarLot"));
                        singUpUserTable.setCarPic(j.getString("CarPic"));

//                        if(found){
                        LogInActivity logInValet=(LogInActivity) context;

                        singUpUserTableGlobal=singUpUserTable;
                        logInValet.intentToMain();
                        getRaw();

                        isOk=true;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                } else {
                    Toast.makeText(context, "User Name Or Password inCorrect", Toast.LENGTH_SHORT).show();
                    isOk=true;
                    Log.e("onPostExecute", "" + s.toString());
                }
            }else  {  isOk=true;}
        }
    }


    private class getRaw extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        private String userName = "",password="";


        public getRaw() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            swALogIn = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//            swALogIn.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
//            swALogIn.setTitleText("PleaseWait" );
//            swALogIn.setCancelable(false);
//            swALogIn.show();
            isOk=false;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String ip =valetDatabase.getAllIPSetting();
                String link = "http://"+ip+"/api/ValCaptain/getClientEndRawId?";


                String data = "ClientEndRaw="+singUpUserTableGlobal.getId();

                URL url = new URL(link+data );


//
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.setRequestMethod("GET");
//                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes(data);
//                wr.flush();
//                wr.close();
                Log.e("url____",""+link+data);

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                while ((JsonResponse = bufferedReader.readLine()) != null) {
                    stringBuffer.append(JsonResponse + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                Log.e("tag", "TAG_itemSwitch -->" + stringBuffer.toString());

                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("tag", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

           // swALogIn.dismissWithAnimation();

            if (s != null) {
                if (s.contains("id")) {


                    try{
                        valetDatabase.delete();
                    }catch (Exception r){

                    }

                    String id=s.replace("id ","").replaceAll("\"","").replaceAll("\\n","");
                   if(!TextUtils.isEmpty(id)) {
                        valetDatabase.addSetting(id);
                    }else {
                        valetDatabase.addSetting("-1");
                        valetDatabase.delete();
                    }

                    isOk=true;
                } else {
                    Toast.makeText(context, "User Name Or Password inCorrect", Toast.LENGTH_SHORT).show();
                    isOk=true;
                    Log.e("onPostExecute", "" + s.toString());
                }
            }else {  isOk=true;}
        }
    }

    private class GetOrder extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            isOk=false;

        }

        @Override
        protected String doInBackground(String... params) {

            try {

//                if (!ipAddress.equals("")) {


                String id=valetDatabase.getAllSetting();
                String ip =valetDatabase.getAllIPSetting();
                URL_TO_HIT = "http://"+ip.trim() +"/api/ValCaptain/getCaptainStatus?idClient="+singUpUserTableGlobal.getId()+"&idraw="+id.replace(" ","").replaceAll("\"","");
//                }
            } catch (Exception e) {
                Log.e("URL_TO_HIT111", "JsonResponse\t" + URL_TO_HIT);
            }


            Log.e("URL_TO_HIT", "JsonResponse\t" + URL_TO_HIT);
            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(URL_TO_HIT));

//

                HttpResponse response = client.execute(request);


                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();


                JsonResponse = sb.toString();
                Log.e("tag_allcheques", "JsonResponse\t" + JsonResponse);

                return JsonResponse;


            }//org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex) {
                ex.printStackTrace();
//                progressDialog.dismiss();

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {

                        Toast.makeText(context, "Ip Connection Failed ", Toast.LENGTH_LONG).show();
                    }
                });


                return null;
            } catch (Exception e) {
                e.printStackTrace();
//                progressDialog.dismiss();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject result = null;
            String impo = "";
            if (s != null) {
                if (s.contains("ClientName")) {

                    s=s.replace("<br /><b>Warning</b>:  oci_connect(): OCI_SUCCESS_WITH_INFO: ORA-28002: the password will expire within 7 days in <b>C:\\xampp\\htdocs\\importt.php</b> on line <b>3</b><br />","");

                    Gson gson = new Gson();
                    try {
                        JSONArray jsonArray=new JSONArray(s);

                        Type collectionType = new TypeToken<Collection<ClientOrder>>(){}.getType();
                        Collection<ClientOrder> enums = gson.fromJson(s, collectionType);

//                    CaptainClientTransfer gsonObj = gson.fromJson(jsonArray.getJSONObject().toString(), CaptainClientTransfer.class);
                        clientOrders.clear();
                        // captainClientTransfers.addAll(enums.getOrderList());
                        clientOrders= (List<ClientOrder>) enums;
                        DriverMapsActivity mainValetActivity = (DriverMapsActivity) context;
                        mainValetActivity.listOfClient();
                        isOk=true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    clientOrders.clear();
                    DriverMapsActivity mainValetActivity = (DriverMapsActivity) context;
                    mainValetActivity.listOfClientClear();
                    Log.e("onPostExecute", "" + s.toString());
                isOk=true;

                }
            }else {  isOk=true;}
        }

    }

    private class GetStatus extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }

        @Override
        protected String doInBackground(String... params) {

            try {

//                if (!ipAddress.equals("")) {

                ValetDatabase valetDatabase=new ValetDatabase(context);
                String id=valetDatabase.getAllSetting();
                String ip =valetDatabase.getAllIPSetting();
                URL_TO_HIT = "http://"+ip.trim() +"/api/ValCaptain/getStatusC_T_id?idStatus="+id.replace(" ","").replaceAll("\"","");
//                }
            } catch (Exception e) {
                Log.e("URL_TO_HIT111", "JsonResponse\t" + URL_TO_HIT);
            }


            Log.e("URL_TO_HIT", "JsonResponse\t" + URL_TO_HIT);
            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(URL_TO_HIT));

//

                HttpResponse response = client.execute(request);


                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();


                JsonResponse = sb.toString();
                Log.e("tag_allcheques", "JsonResponse\t" + JsonResponse);

                return JsonResponse;


            }//org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex) {
                ex.printStackTrace();
//                progressDialog.dismiss();

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {

                        Toast.makeText(context, "Ip Connection Failed ", Toast.LENGTH_LONG).show();
                    }
                });


                return null;
            } catch (Exception e) {
                e.printStackTrace();
//                progressDialog.dismiss();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject result = null;
            String impo = "";
            if (s != null) {
                if (s.contains("ClientName")) {

                    s=s.replace("<br /><b>Warning</b>:  oci_connect(): OCI_SUCCESS_WITH_INFO: ORA-28002: the password will expire within 7 days in <b>C:\\xampp\\htdocs\\importt.php</b> on line <b>3</b><br />","");

                    Gson gson = new Gson();
//{"id":3038,"ClientName":"1","ClientPhoneNo":"q","ClientId":3013,"FromLoc":"lat/lng: (0.0,0.0)",
// "ToLocation":"lat/lng: (31.45160740990828,36.635393388569355)","LocationName":"null","PaymentType":0,
// "CaptainName":null,"CaptainId":2010,"CaptainPhoneNo":null,"Status":5,"Rate":"0","TimeOfArraive":"'3:4:5'",
// "DateOfTranse":"10/6/2021 5:45:57 AM","TimeIn":null,"Timeout":null,"TimeParkIn":null,"TimeParkOut":null,
// "StatusRaw":"15","PayValue":"45.54","AmountValue":"45.54","remainValue":"0.0"}

                    try {
                        JSONObject d=new JSONObject(s);
                        String va=d.getString("StatusRaw");
                        arriveTime=d.getString("TimeOfArraive");
                        captainId=d.getString("CaptainId");
                       // captainClientTransfer=null;
                        captainClientTransfer.setId(Integer.parseInt(d.getString("id")));
                        captainClientTransfer.setClientName(d.getString("ClientName"));
                        captainClientTransfer.setClientPhoneNo( d.getString("ClientPhoneNo"));
                        captainClientTransfer.setClientId( d.getInt("ClientId"));
                        captainClientTransfer.setFromLoc( d.getString("FromLoc"));
                        captainClientTransfer.setToLocation( d.getString("ToLocation"));
                        captainClientTransfer.setLocationName( d.getString("LocationName"));
                        captainClientTransfer.setCaptainName( d.getString("CaptainName"));
                        captainClientTransfer.setCaptainPhoneNo( d.getString("CaptainPhoneNo"));
                        captainClientTransfer.setStatus( d.getInt("Status"));
                        captainClientTransfer.setRate( d.getString("Rate"));
                        captainClientTransfer.setTimeOfArraive( d.getString("TimeOfArraive"));
                        captainClientTransfer.setDateOfTranse( d.getString("DateOfTranse"));
                        captainClientTransfer.setTimeIn( d.getString("TimeIn"));
                        captainClientTransfer.setTimeout( d.getString("Timeout"));
                        captainClientTransfer.setTimeParkIn( d.getString("TimeParkIn"));
                        captainClientTransfer.setTimeParkOut( d.getString("TimeParkOut"));
                        captainClientTransfer.setStatusRaw( d.getString("StatusRaw"));
                        captainClientTransfer.setParkingLocation( d.getString("ParkingLocation"));
                        try {
                            captainClientTransfer.setPayVal( d.getDouble("PayValue"));
                            captainClientTransfer.setAmountVal( d.getDouble("AmountValue"));
                            captainClientTransfer.setReVal( d.getDouble("remainValue"));
                        }catch (Exception e){
                            captainClientTransfer.setPayVal( 0);
                            captainClientTransfer.setAmountVal(0);
                            captainClientTransfer.setReVal( 0);
                        }


                        DriverMapsActivity mainValetActivity = (DriverMapsActivity) context;
                        if(va.equals("8")){
                            globalText.setText("I need My Car");
                        }
                        mainValetActivity.ListShow(va);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                } else Log.e("onPostExecute", "" + s.toString());
            }
        }

    }

    private class updateStatus extends AsyncTask<String, String, String> {

        Context context;
        ClientOrder clientOrder;
        public updateStatus(Context context,ClientOrder clientOrder) {
        this.context=context;
        this.clientOrder=clientOrder;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            swAStusus = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            swAStusus.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            swAStusus.setTitleText("PleaseWait" );
            swAStusus.setCancelable(false);
            swAStusus.show();
            isOk=false;

        }

        @Override
        protected String doInBackground(String... params) {

            try {

//                if (!ipAddress.equals("")) {   //int idT, int idCaptain, int idS, int idClient, int status
                String ip =valetDatabase.getAllIPSetting();
                URL_TO_HIT = "http://"+ip.trim() +"/api/ValCaptain/updateStatusCaptainStatusTransfer?idT="+clientOrder.getTransferId()+"&idS="+clientOrder.getId()+"&idCaptain="+clientOrder.getCaptainId()+"&idClient="+singUpUserTableGlobal.getId()+"&status=1";
//                }
            } catch (Exception e) {
                Log.e("URL_TO_HIT99", "JsonResponse\t" + URL_TO_HIT);
            }


            Log.e("URL_TO_HITnew", "JsonResponse\t" + URL_TO_HIT);
            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPut request = new HttpPut();
                request.setURI(new URI(URL_TO_HIT));

//

                HttpResponse response = client.execute(request);


                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();


                JsonResponse = sb.toString();
                Log.e("tag_allcheques", "JsonResponse\t" + JsonResponse);

                return JsonResponse;


            }//org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex) {
                ex.printStackTrace();
//                progressDialog.dismiss();

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {

                        Toast.makeText(context, "Ip Connection Failed ", Toast.LENGTH_LONG).show();
                    }
                });


                return null;
            } catch (Exception e) {
                e.printStackTrace();
//                progressDialog.dismiss();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject result = null;
            String impo = "";
            if (s != null) {
                if (s.contains("Success_updating_status")) {

                    DriverMapsActivity mainValetActivity = (DriverMapsActivity) context;
                    mainValetActivity.listOfView(2);
                    swAStusus.dismissWithAnimation();

                    mainValetActivity.updateInFireBaseClient("4",clientOrder.getCaptainName(),clientOrder.getCaptainId()+"",clientOrder.getCaptainPhoneNo());

                    mainValetActivity.UpdateInFireBaseCaptain("3",clientOrder.getCaptainId()+"");

                    isOk=true;
                } else {
                    Log.e("onPostExecute", "" + s.toString());
                    swAStusus.dismissWithAnimation();
                isOk=true;}
            }else {
                swAStusus.dismissWithAnimation();
                isOk=true;}
        }

    }

    private class updateStatusRej extends AsyncTask<String, String, String> {

        Context context;
        ClientOrder clientOrder;
        public updateStatusRej(Context context,ClientOrder clientOrder) {
            this.context=context;
            this.clientOrder=clientOrder;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swAStususRej = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            swAStususRej.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            swAStususRej.setTitleText("PleaseWait" );
            swAStususRej.setCancelable(false);
            swAStususRej.show();
            isOk=false;

        }

        @Override
        protected String doInBackground(String... params) {

            try {

//                if (!ipAddress.equals("")) {   //int idT, int idCaptain, int idS, int idClient, int status
                String ip =valetDatabase.getAllIPSetting();
                URL_TO_HIT = "http://"+ip.trim() +"/api/ValCaptain/updateStatusCaptainStatusRej?idT="+clientOrder.getTransferId()+"&idS="+clientOrder.getId()+"&idCaptain="+clientOrder.getCaptainId()+"&idClient="+singUpUserTableGlobal.getId()+"&statusRej=2";
//                }
            } catch (Exception e) {
                Log.e("URL_TO_HIT99", "JsonResponse\t" + URL_TO_HIT);
            }


            Log.e("URL_TO_HITnew", "JsonResponse\t" + URL_TO_HIT);
            try {

                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPut request = new HttpPut();
                request.setURI(new URI(URL_TO_HIT));

//

                HttpResponse response = client.execute(request);


                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();


                JsonResponse = sb.toString();
                Log.e("tag_allcheques", "JsonResponse\t" + JsonResponse);

                return JsonResponse;


            }//org.apache.http.conn.HttpHostConnectException: Connection to http://10.0.0.115 refused
            catch (HttpHostConnectException ex) {
                ex.printStackTrace();
//                progressDialog.dismiss();

                Handler h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {

                        Toast.makeText(context, "Ip Connection Failed ", Toast.LENGTH_LONG).show();
                    }
                });


                return null;
            } catch (Exception e) {
                e.printStackTrace();
//                progressDialog.dismiss();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject result = null;
            String impo = "";
            if (s != null) {
                if (s.contains("Success_updating_status_rej")) {



                    DriverMapsActivity mainValetActivity = (DriverMapsActivity) context;
                   // mainValetActivity.listOfView(0);
                    swAStususRej.dismissWithAnimation();
                    mainValetActivity.UpdateInFireBaseCaptain("-1",""+clientOrder.getCaptainId());
                    mainValetActivity.updateInFireBaseClients("0");
                    mainValetActivity.updateInFireBaseClients("3");

                    isOk=true;
                } else {
                    swAStususRej.dismissWithAnimation();
                    Log.e("onPostExecute", "" + s.toString());
                    isOk=true;
                }
            }else {
                swAStususRej.dismissWithAnimation();
                isOk=true;}
        }

    }


    private class BitmapImage2 extends AsyncTask<String, String, String> {
int flag=0;
        public BitmapImage2(int flag) {
            this.flag=flag;
        }

        @Override
        protected String doInBackground(String... pictures) {

            try {

//                if (!singUpUserTableGlobal.getCRIMINAL_RECORE_PIC().equals("null")) {
                String ip=valetDatabase.getAllIPSetting();
                for (int i = 0; i < 1; i++) {
                    Bitmap bitmap = null;
                    URL url;
                    switch (i) {
                        case 0:
                            if (singUpUserTableGlobal.getCarPic() != null) {//http://192.168.2.17:8088/woody/images/2342_img_1.png
                                url = new URL("http://" + ip + "/imagesFile/" +singUpUserTableGlobal.getId()+"_CLIENT_CAR_PIC.jpg");
                                try {
                                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                    singUpUserTableGlobal.setCarPicBitmap(bitmap);
                                } catch (Exception e) {
//                                        pictures[0].setPic11(bitmap);
                                }
                            }
                            break;

                    }
                }

//                    Bitmap finalBitmap = bitmap;


//                }
            } catch (Exception e) {
                Log.e("fromclass2", "exception:doInBackground " + e.getMessage());
                return "exception";
            }
            return "null";// BitmapFactory.decodeStream(in);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("fromclass2", "exception:onPostExecute: " + s);
            try {
                if(flag==1) {
                    ProfileActivity profileActivate = (ProfileActivity) context;
                    profileActivate.fillImage();
                }else if(flag==2){
                    MainValetActivity profileActivate = (MainValetActivity) context;
                    profileActivate.fillImage();
                }
            }catch (Exception e){}

            if (s.contains("exception"))
                Toast.makeText(context, "No image found!", Toast.LENGTH_SHORT).show();

        }
    }

}
