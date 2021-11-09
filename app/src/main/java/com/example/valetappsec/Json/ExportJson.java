package com.example.valetappsec.Json;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.valetappsec.DriverMapsActivity;
import com.example.valetappsec.Model.CaptainClientTransfer;
import com.example.valetappsec.Model.ClientOrder;
import com.example.valetappsec.Model.SingUpClientModel;
import com.example.valetappsec.Model.ValetFireBaseItem;
import com.example.valetappsec.SingUpValetActivityApp;
import com.example.valetappsec.ValetDatabase;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.valetappsec.GlobalVairable.captainClientTransfer;
import static com.example.valetappsec.GlobalVairable.captainId;
import static com.example.valetappsec.GlobalVairable.ids;
import static com.example.valetappsec.GlobalVairable.isOk;
import static com.example.valetappsec.GlobalVairable.singUpUserTableGlobal;

public class ExportJson {

    Context context;
    SweetAlertDialog swASingUp, swAsPay, swAsRate, sweetAlertDialogStatus,swANote, swATrans, sweetAlertDialogStatusDelete,sweetAlertDialogStatusDeleteBefore;
    ValetDatabase valetDatabase;
    String URL_TO_HIT;

    public ExportJson(Context context) {
        this.context = context;
        valetDatabase = new ValetDatabase(context);
    }

    public void SingUpCaptain(Context context, SingUpClientModel singUpClientModel) {

        new SingUpClientAsync(context, singUpClientModel).execute();

    }


    public void updateStatus(Context context, String CPS, String CT) {
        new updateStatus(context, CPS, CT).execute();
    }

    public void updateStatusDelete(Context context,String reason) {
        new updateDeleteRequest(context,reason).execute();
    }

    public void updateStatusDeleteBeforeAcc(Context context,String reason) {
        new updateDeleteRequestBeforeAcc(context,reason).execute();
    }


    public void updateStatuPay(Context context, String CPS, String CT, String AValue, String PValue, String RValue) {
        new updatePayStatus(context, CPS, CT, AValue, RValue, PValue).execute();
    }

    public void updateStatusRate(Context context, String CPS, String CT, String rate, String note) {
        new updateRateStatus(context, CPS, CT, rate, note).execute();
    }


    public void CaptainTransfer(Context context, CaptainClientTransfer captainClientTransfer) {

        new CaptainTransferAsync(context, captainClientTransfer).execute();

    }
    public void lateNote(String note) {

        new LateTableAsync(note).execute();

    }

    private class SingUpClientAsync extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;


        JSONObject jsonObject;
        SingUpClientModel singUpClientModel;

        public SingUpClientAsync(Context context, SingUpClientModel singUpClientModel) {

            this.singUpClientModel = singUpClientModel;
            this.jsonObject = jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            swASingUp = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            swASingUp.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            swASingUp.setTitleText("PleaseWait");
            swASingUp.setCancelable(false);
            swASingUp.show();

            isOk = false;

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String ip = valetDatabase.getAllIPSetting();//192.168.1.101:81
                String link = "http://" + ip + "/api/ValCaptain/saveClientUser?";


                String data = "userName=" + singUpClientModel.getUserName()
                        + "&password=" + singUpClientModel.getPassword()
                        + "&email=" + singUpClientModel.getEmail()
                        //  + "&Activiat=" + "0"
                        + "&phoneNo=" + singUpClientModel.getPhoneNo()
                        + "&carType=" + singUpClientModel.getCarType()
                        + "&carModel=" + singUpClientModel.getCarModel()
                        + "&carColor=" + singUpClientModel.getCarColor()
                        + "&carLot=" + singUpClientModel.getCarLot()
                        + "&carPic=" + singUpClientModel.getCarPic();

                URL url = new URL(link + data);


                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

//                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes(data);
//                wr.flush();
//                wr.close();
                Log.e("url____", "" + link + data);

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
            swASingUp.dismissWithAnimation();

            if (s != null) {
                if (s.contains("Success_Add_Client")) {
                    Log.e("salesManInfo", "NEW_CAPTAINS SUCCESS\t" + s.toString());
                    Toast.makeText(context, "NEW CAPTAINS SUCCESS", Toast.LENGTH_SHORT).show();
                    SingUpValetActivityApp singUpValet = (SingUpValetActivityApp) context;
                    singUpValet.finish();
                    isOk = true;

                } else {

//                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
//                            .setTitleText("Sing Up Fail Please Try Again !!!")
//                            .setContentText("")
////                            .setCancelButton("cancel", new SweetAlertDialog.OnSweetClickListener() {
////                                @Override
////                                public void onClick(SweetAlertDialog sweetAlertDialog) {
////                                    sweetAlertDialog.dismissWithAnimation();
////                                }
////                            })
//                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//
//                                    sweetAlertDialog.dismissWithAnimation();
//
//
//                                }
//                            })
//                            .show();
                    isOk = true;
                }
            } else {
                isOk = true;
            }
        }
    }

    private class CaptainTransferAsync extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

        JSONObject jsonObject;
        CaptainClientTransfer captainClientTransfer;

        public CaptainTransferAsync(Context context, CaptainClientTransfer captainClientTransfer) {

            this.captainClientTransfer = captainClientTransfer;
            this.jsonObject = jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            swATrans = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            swATrans.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            swATrans.setTitleText("PleaseWait");
            swATrans.setCancelable(false);
            swATrans.show();

            isOk = false;

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                valetDatabase = new ValetDatabase(context);
                String ip = valetDatabase.getAllIPSetting();//192.168.1.101:81

                String link = "http://" + ip + "/api/ValCaptain/SaveTransfer?";


                String data = "clientName=" + captainClientTransfer.getClientName()
                        + "&clientId=" + captainClientTransfer.getClientId()
                        + "&clientPhoneNo=" + captainClientTransfer.getClientPhoneNo()
                        + "&fromLoc=" + captainClientTransfer.getFromLoc()
                        + "&toLoc=" + captainClientTransfer.getToLocation()
                        + "&LocName=" + captainClientTransfer.getLocationName()
                        + "&paymentType=" + captainClientTransfer.getPaymentType()
                        + "&captainName=" + captainClientTransfer.getCaptainName()
                        + "&captainId=" + captainClientTransfer.getCaptainId()
                        + "&captainPhoneNo=" + captainClientTransfer.getCaptainPhoneNo()
                        + "&Rate=" + captainClientTransfer.getRate()
                        + "&TimeOfArraive=" + captainClientTransfer.getTimeOfArraive()
                        + "&DateOfTrans=" + captainClientTransfer.getDateOfTranse()
                        + "&TimeIn=" + captainClientTransfer.getTimeIn()
                        + "&TimeOut=" + captainClientTransfer.getTimeout()
                        + "&TimeParkIn=" + captainClientTransfer.getTimeParkIn()
                        + "&TimeParkOut=" + captainClientTransfer.getTimeParkOut();

                URL url = new URL(link + data);


                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

//                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes(data);
//                wr.flush();
//                wr.close();
                Log.e("url____", "" + link + data);

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


            if (s != null) {
                if (s.contains("Success_Add_Transfer")) {
                    Log.e("salesManInfo", "Success_Add_Transfer SUCCESS\t" + s.toString());
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    DriverMapsActivity driverMapsActivity = (DriverMapsActivity) context;
                    driverMapsActivity.VisibilityLinear();
                    valetDatabase = new ValetDatabase(context);
                    ids = s.replace("Success_Add_Transfer", "").trim();
                    try {

                        valetDatabase.delete();
                    } catch (Exception e) {
                    }


                    valetDatabase.addSetting(ids);
                    swATrans.dismissWithAnimation();

                    ValetFireBaseItem valetFireBaseItem = new ValetFireBaseItem();

                    valetFireBaseItem.setStatus("2");
                    valetFireBaseItem.setIfReturn("0");
                    valetFireBaseItem.setRawIdActivate(ids);
                    valetFireBaseItem.setUserId(singUpUserTableGlobal.getId());
                    valetFireBaseItem.setLongLocation("-1");
                    valetFireBaseItem.setLatLocation("-1");
                    valetFireBaseItem.setCaptainPhoneNo("-1");
                    valetFireBaseItem.setCaptainName("");
                    valetFireBaseItem.setCaptainId("");
                    driverMapsActivity.writeInFireBaseClient(valetFireBaseItem);
                    driverMapsActivity.writeInFireBaseIneedCap("0");
                    driverMapsActivity.writeInFireBaseIneedCap("1");
                    isOk = true;
                } else {

//                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
//                            .setTitleText("Sing Up Fail Please Try Again !!!")
//                            .setContentText("")
////                            .setCancelButton("cancel", new SweetAlertDialog.OnSweetClickListener() {
////                                @Override
////                                public void onClick(SweetAlertDialog sweetAlertDialog) {
////                                    sweetAlertDialog.dismissWithAnimation();
////                                }
////                            })
//                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//
//                                    sweetAlertDialog.dismissWithAnimation();
//
//
//                                }
//                            })
//                            .show();
                    swATrans.dismissWithAnimation();
                    isOk = true;


                }
            } else {
                swATrans.dismissWithAnimation();
                isOk = true;
            }
        }
    }


    private class LateTableAsync extends AsyncTask<String, String, String> {
        private String JsonResponse = null;
        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;

    String note;

        public LateTableAsync(String note) {
            this.note=note;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            swANote = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            swANote.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            swANote.setTitleText("PleaseWait");
            swANote.setCancelable(false);
            swANote.show();

            isOk = false;

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                valetDatabase = new ValetDatabase(context);
                String ip = valetDatabase.getAllIPSetting();//192.168.1.101:81
                String idTr = valetDatabase.getAllSetting();//192.168.1.101:81
//saveLateNote(int idCap, int  idClients, int idTranc , String CaptainPhoneNo, String ClientPhoneNo,
//            String note)
                String link = "http://" + ip + "/api/ValCaptain/saveLateNote?";


                String data = "idCap=" + captainId
                        + "&idClients=" + singUpUserTableGlobal.getId()
                        + "&idTranc=" + idTr
                        + "&CaptainPhoneNo=" +captainClientTransfer.getCaptainPhoneNo()
                        + "&ClientPhoneNo=" +singUpUserTableGlobal.getPhoneNo()
                        + "&note=" +note.trim().replace(" ","%20");

                URL url = new URL(link + data);


                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");

//                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes(data);
//                wr.flush();
//                wr.close();
                Log.e("url____", "" + link + data);

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


            if (s != null) {
                if (s.contains("Success_Add_NOTE")) {
                    Toast.makeText(context, "Success Add NOTE", Toast.LENGTH_SHORT).show();
                    swANote.dismissWithAnimation();
                    isOk = true;
                } else {
                    swANote.dismissWithAnimation();
                    isOk = true;


                }
            } else {
                swANote.dismissWithAnimation();
                isOk = true;
            }
        }
    }

    private class updateStatus extends AsyncTask<String, String, String> {

        JSONObject jsonObject;
        ClientOrder clientOrder;
        String Cp = "";
        String Ct = "";

        public updateStatus(Context context, String Cp, String Ct) {

            this.Cp = Cp;
            this.Ct = Ct;
            this.clientOrder = clientOrder;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sweetAlertDialogStatus = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialogStatus.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            sweetAlertDialogStatus.setTitleText("PleaseWait");
            sweetAlertDialogStatus.setCancelable(false);
            sweetAlertDialogStatus.show();
            isOk = false;

        }

        @Override
        protected String doInBackground(String... params) {
            String cap="";
            try {

//                if (!ipAddress.equals("")) {   //int idT, int idCaptain, int idS, int idClient, int status

                valetDatabase = new ValetDatabase(context);
                 cap = valetDatabase.getAllSetting();
                String ip = valetDatabase.getAllIPSetting();//192.168.1.101:81

                URL_TO_HIT = "http://" + ip.trim() + "/api/ValCaptain/updateStatusRawsClient?"
                        + "idTrans2=" + cap.replace(" ", "").replaceAll("\"", "")
                        + "&IdCaptains=" + captainId
                        + "&rawS2=" + Cp
                        + "&rawT2=" + Ct;
//                }
            } catch (Exception e) {
                Log.e("URL_TO_HIT99", "JsonResponse\t" + URL_TO_HIT +"  "+cap);
            }


            Log.e("URL_TO_HITnew", "JsonResponse\t" + URL_TO_HIT+"   "+cap);
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
            //   swASingUp.dismissWithAnimation();
            if (s != null) {
                if (s.contains("Success_updating_status_Raw")) {
//                    Log.e("salesManInfo", "NEW_CAPTAINS SUCCESS\t" + s.toString());
//                    Toast.makeText(context, "NEW CAPTAINS SUCCESS", Toast.LENGTH_SHORT).show();
                    DriverMapsActivity captainMapsActivity = (DriverMapsActivity) context;
//                    captainMapsActivity.listAvilable(2);
//                    captainDatabase=new CaptainDatabase(context);
//                    ids=s.replace("Success_Add_CaptainStatus" ,"").trim();
//                    captainDatabase.delete();
//                    captainDatabase.addSetting(ids);

                    isOk = true;


                    captainMapsActivity.UpdateInFireBaseCaptain(Cp,captainId);

                    captainMapsActivity.updateInFireBaseClients(Ct);

                    if(Cp.equals("8")){
                        captainMapsActivity.UpdateInFireBaseCaptainReturn("0",captainId);
                        captainMapsActivity.UpdateInFireBaseCaptainReturn("1",captainId);
                    }

                    sweetAlertDialogStatus.dismissWithAnimation();



                } else {
                    sweetAlertDialogStatus.dismissWithAnimation();
                    isOk = true;
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Scan Fail Please Try Again !!!")
                            .setContentText("")
//                            .setCancelButton("cancel", new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    sweetAlertDialog.dismissWithAnimation();
//                                }
//                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                    sweetAlertDialog.dismissWithAnimation();


                                }
                            })
                            .show();

                }
            } else {
                sweetAlertDialogStatus.dismissWithAnimation();
                isOk = true;
            }
        }

    }

    private class updatePayStatus extends AsyncTask<String, String, String> {

        JSONObject jsonObject;
        ClientOrder clientOrder;
        String Cp = "";
        String Ct = "";
        String AmountValue = "";
        String PayValue = "";
        String ReminingValue = "";

        public updatePayStatus(Context context, String Cp, String Ct, String amountValue, String remaningValue, String payValue) {

            this.Cp = Cp;
            this.Ct = Ct;
            this.AmountValue = amountValue;
            this.PayValue = payValue;
            this.ReminingValue = remaningValue;

            this.clientOrder = clientOrder;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            swAsPay = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            swAsPay.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            swAsPay.setTitleText("PleaseWait");
            swAsPay.setCancelable(false);
            swAsPay.show();


            isOk = false;

        }

        @Override
        protected String doInBackground(String... params) {

            try {

//                if (!ipAddress.equals("")) {   //int idT, int idCaptain, int idS, int idClient, int status

                valetDatabase = new ValetDatabase(context);
                String ip = valetDatabase.getAllIPSetting();//192.168.1.101:81

                String cap = valetDatabase.getAllSetting();
                URL_TO_HIT = "http://" + ip.trim() + "/api/ValCaptain/updateStatusForPayClient?"
                        + "idTrans3=" + cap.replace(" ", "").replaceAll("\"", "")
                        + "&IdCaptain3=" + captainId
                        + "&rawS3=" + Cp
                        + "&rawT3=" + Ct
                        + "&Value=" + AmountValue
                        + "&payVal=" + PayValue
                        + "&remainValue=" + ReminingValue;
//                } captainMapsActivity.updateInFireBaseClients(Ct);
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
            //   swASingUp.dismissWithAnimation();

            if (s != null) {
                if (s.contains("Success_updating_status_Raw")) {
//                    Log.e("salesManInfo", "NEW_CAPTAINS SUCCESS\t" + s.toString());
//                    Toast.makeText(context, "NEW CAPTAINS SUCCESS", Toast.LENGTH_SHORT).show();
                    DriverMapsActivity captainMapsActivity = (DriverMapsActivity) context;
//                    captainMapsActivity.listAvilable(2);
//                    captainDatabase=new CaptainDatabase(context);
//                    ids=s.replace("Success_Add_CaptainStatus" ,"").trim();


                    isOk = true;
                    captainMapsActivity.UpdateInFireBaseCaptain(Cp,captainId);

                    captainMapsActivity.updateInFireBaseClients(Ct);
                    swAsPay.dismissWithAnimation();
                } else {
                    swAsPay.dismissWithAnimation();
                    isOk = true;
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Scan Fail Please Try Again !!!")
                            .setContentText("")
//                            .setCancelButton("cancel", new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    sweetAlertDialog.dismissWithAnimation();
//                                }
//                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                    sweetAlertDialog.dismissWithAnimation();


                                }
                            })
                            .show();

                }
            } else {
                swAsPay.dismissWithAnimation();
                isOk = true;
            }
        }

    }


    private class updateRateStatus extends AsyncTask<String, String, String> {

        JSONObject jsonObject;
        ClientOrder clientOrder;
        String Cp = "";
        String Ct = "";
        String Rate = "";
        String noteRate = "";

        public updateRateStatus(Context context, String Cp, String Ct, String Rate, String noteRate) {

            this.Cp = Cp;
            this.Ct = Ct;
            this.Rate = Rate;
            this.noteRate = noteRate;

            this.clientOrder = clientOrder;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            swAsRate = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            swAsRate.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            swAsRate.setTitleText("PleaseWait");
            swAsRate.setCancelable(false);
            swAsRate.show();

            isOk = false;

        }

        @Override
        protected String doInBackground(String... params) {

            try {

//                if (!ipAddress.equals("")) {   //int idT, int idCaptain, int idS, int idClient, int status

                valetDatabase = new ValetDatabase(context);
                String cap = valetDatabase.getAllSetting();
                String ip = valetDatabase.getAllIPSetting();//192.168.1.101:81

                URL_TO_HIT = "http://" + ip.trim() + "/api/ValCaptain/updateStatusForRateClient?"
                        + "idTRate=" + cap.replace(" ", "").replaceAll("\"", "")
                        + "&IdCapRate=" + captainId
                        + "&rawTR=" + Cp
                        + "&rawSR=" + Ct
                        + "&Rate=" + Rate
                        + "&noteRate=" + noteRate;
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
            //   swASingUp.dismissWithAnimation();

            if (s != null) {
                if (s.contains("Success_updating_status_Raw")) {
//                    Log.e("salesManInfo", "NEW_CAPTAINS SUCCESS\t" + s.toString());
//                    Toast.makeText(context, "NEW CAPTAINS SUCCESS", Toast.LENGTH_SHORT).show();
//                    CaptainMapsActivity captainMapsActivity = (CaptainMapsActivity) context;
//                    captainMapsActivity.listAvilable(2);
//                    captainDatabase=new CaptainDatabase(context);
//                    ids=s.replace("Success_Add_CaptainStatus" ,"").trim();
//                    captainDatabase.delete();
//                    captainDatabase.addSetting(ids);

                    valetDatabase.delete();
                    DriverMapsActivity driverMapsActivity = (DriverMapsActivity) context;
                    driverMapsActivity.returnGo();
                    driverMapsActivity.listOfView(0);

                    driverMapsActivity.UpdateInFireBaseCaptain(Cp,captainId);

                    driverMapsActivity.updateInFireBaseClients(Ct);
                    isOk = true;
                    swAsRate.dismissWithAnimation();
                } else {

                    isOk = true;
                    swAsRate.dismissWithAnimation();
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Scan Fail Please Try Again !!!")
                            .setContentText("")
//                            .setCancelButton("cancel", new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    sweetAlertDialog.dismissWithAnimation();
//                                }
//                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                    sweetAlertDialog.dismissWithAnimation();


                                }
                            })
                            .show();

                }
            } else {
                swAsRate.dismissWithAnimation();
                isOk = true;
            }
        }

    }

    private class updateDeleteRequest extends AsyncTask<String, String, String> {

        JSONObject jsonObject;
        ClientOrder clientOrder;
        String Cp = "";
        String Ct = "";

        String reason;
        public updateDeleteRequest(Context context,String reason) {


            this.reason=reason;
            this.clientOrder = clientOrder;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sweetAlertDialogStatusDelete = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialogStatusDelete.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            sweetAlertDialogStatusDelete.setTitleText("PleaseWait");
            sweetAlertDialogStatusDelete.setCancelable(false);
            sweetAlertDialogStatusDelete.show();
            isOk = false;

        }

        @Override
        protected String doInBackground(String... params) {

            try {

//                if (!ipAddress.equals("")) {   //int idT, int idCaptain, int idS, int idClient, int status

                valetDatabase = new ValetDatabase(context);
                String cap = valetDatabase.getAllSetting();
                String ip = valetDatabase.getAllIPSetting();//192.168.1.101:81

                URL_TO_HIT = "http://" + ip.trim() + "/api/ValCaptain/updateStatusForDeleteRequest?"
                        + "idTDelete=" + cap.replace(" ", "").replaceAll("\"", "")
                        + "&IdCapDelete=" + captainId
                +"&cancelRes="+reason.trim().replace(" ","%20");

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
            //   swASingUp.dismissWithAnimation();
            if (s != null) {
                if (s.contains("Success_updating_status_Raw")) {
//                    Log.e("salesManInfo", "NEW_CAPTAINS SUCCESS\t" + s.toString());
//                    Toast.makeText(context, "NEW CAPTAINS SUCCESS", Toast.LENGTH_SHORT).show();
                    DriverMapsActivity driverMapsActivity = (DriverMapsActivity) context;
                    driverMapsActivity.listOfView(555);
//                    ids=s.replace("Success_Add_CaptainStatus" ,"").trim();
                    valetDatabase = new ValetDatabase(context);
                    valetDatabase.delete();

                    driverMapsActivity.UpdateInFireBaseCaptain("555",captainId);
                    driverMapsActivity.deleteClient();

                    isOk = true;
                    sweetAlertDialogStatusDelete.dismissWithAnimation();
                } else {
                    sweetAlertDialogStatusDelete.dismissWithAnimation();
                    isOk = true;
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Delete Fail Please Try Again !!!")
                            .setContentText("")
//                            .setCancelButton("cancel", new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    sweetAlertDialog.dismissWithAnimation();
//                                }
//                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                    sweetAlertDialog.dismissWithAnimation();


                                }
                            })
                            .show();

                }
            } else {
                sweetAlertDialogStatusDelete.dismissWithAnimation();
                isOk = true;
            }
        }

    }

    private class updateDeleteRequestBeforeAcc extends AsyncTask<String, String, String> {

        JSONObject jsonObject;
        ClientOrder clientOrder;
        String Cp = "";
        String Ct = "";
        String reason;

        public updateDeleteRequestBeforeAcc(Context context,String reason) {


            this.reason =reason;
            this.clientOrder = clientOrder;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sweetAlertDialogStatusDeleteBefore = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialogStatusDeleteBefore.getProgressHelper().setBarColor(Color.parseColor("#FDD835"));
            sweetAlertDialogStatusDeleteBefore.setTitleText("PleaseWait");
            sweetAlertDialogStatusDeleteBefore.setCancelable(false);
            sweetAlertDialogStatusDeleteBefore.show();
            isOk = false;

        }

        @Override
        protected String doInBackground(String... params) {

            try {

//                if (!ipAddress.equals("")) {   //int idT, int idCaptain, int idS, int idClient, int status

                valetDatabase = new ValetDatabase(context);
                String cap = valetDatabase.getAllSetting();
                String ip = valetDatabase.getAllIPSetting();//192.168.1.101:81

                URL_TO_HIT = "http://" + ip.trim() + "/api/ValCaptain/updateStatusForDeleteRequestBeforAcc?"
                        + "idTDeleteBefor=" + cap.replace(" ", "").replaceAll("\"", "")+
                "&cancelResBef="+(reason.trim().replace(" ","%20"));

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
            //   swASingUp.dismissWithAnimation();
            if (s != null) {
                if (s.contains("Success_updating_status_Raw")) {
//                    Log.e("salesManInfo", "NEW_CAPTAINS SUCCESS\t" + s.toString());
//                    Toast.makeText(context, "NEW CAPTAINS SUCCESS", Toast.LENGTH_SHORT).show();
                    DriverMapsActivity driverMapsActivity = (DriverMapsActivity) context;
                    driverMapsActivity.listOfView(555);
//                    ids=s.replace("Success_Add_CaptainStatus" ,"").trim();
                    valetDatabase = new ValetDatabase(context);
                    valetDatabase.delete();

                    driverMapsActivity.UpdateInFireBaseCaptain("555",captainId);
                    driverMapsActivity.writeInFireBaseDeleteRequest("0");
                    driverMapsActivity.writeInFireBaseDeleteRequest("1");
                    driverMapsActivity.deleteClient();

                    isOk = true;

                    sweetAlertDialogStatusDeleteBefore.dismissWithAnimation();
                } else {
                    sweetAlertDialogStatusDeleteBefore.dismissWithAnimation();
                    isOk = true;
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Delete Fail Please Try Again !!!")
                            .setContentText("")
//                            .setCancelButton("cancel", new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    sweetAlertDialog.dismissWithAnimation();
//                                }
//                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                    sweetAlertDialog.dismissWithAnimation();


                                }
                            })
                            .show();

                }
            } else {
                sweetAlertDialogStatusDeleteBefore.dismissWithAnimation();
                isOk = true;
            }
        }

    }
}
