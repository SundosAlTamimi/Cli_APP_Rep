package com.example.valetappsec;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

public class ValetDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION =4;//version Db
    private static final String DATABASE_Name = "ValetDBase";//name Db

    static SQLiteDatabase Idb;

    //___________________________________________________________________________________
    private static final String SETTING_TABLE = "SETTING_TABLE";

    private static final String ID_RAW = "ID_RAW";


    //___________________________________________________________________________________
    private static final String SETTING_IP_TABLE = "SETTING_IP_TABLE";

    private static final String IP_RAW = "IP_RAW";
    private static final String ACTIVITY = "ACTIVITY";

    //_________________________________________________________________________________
//___________________________________________________________________________________
    private static final String LOCATION_HISTORY_TABLE = "LOCATION_HISTORY_TABLE";

    private static final String LOCATION_NAME = "LOCATION_NAME";
   // private static final String ACTIVITY = "ACTIVITY";

    //_________________________________________________________________________________
    public ValetDatabase(Context context) {
        super(context, DATABASE_Name, null, DATABASE_VERSION);
    }
    //__________________________________________________________________________________


    @Override
    public void onCreate(SQLiteDatabase Idb) {

        String CREATE_TABLE_ITEM_CARD = "CREATE TABLE " + SETTING_TABLE + "("
                + ID_RAW + " NVARCHAR" + ")";
        Idb.execSQL(CREATE_TABLE_ITEM_CARD);

        String CREATE_TABLE_SETTING_IP = "CREATE TABLE " + SETTING_IP_TABLE + "("
                + IP_RAW + " NVARCHAR" + ","
                + ACTIVITY + " NVARCHAR" + ")";
        Idb.execSQL(CREATE_TABLE_SETTING_IP);

        String CREATE_TABLE_LOCATION = "CREATE TABLE " + LOCATION_HISTORY_TABLE + "("
                + LOCATION_NAME + " NVARCHAR" + ")";
        Idb.execSQL(CREATE_TABLE_LOCATION);

//=========================================================================================

//=========================================================================================

    }

    @Override
    public void onUpgrade(SQLiteDatabase Idb, int oldVersion, int newVersion) {

        try{
            String CREATE_TABLE_SETTING_IP = "CREATE TABLE " + SETTING_IP_TABLE + "("
                    + IP_RAW + " NVARCHAR" + ","
                    + ACTIVITY + " NVARCHAR" + ")";
            Idb.execSQL(CREATE_TABLE_SETTING_IP);
        }catch (Exception e){

        }


        try{
            String CREATE_TABLE_LOCATION = "CREATE TABLE " + LOCATION_HISTORY_TABLE + "("
                    + LOCATION_NAME + " NVARCHAR" + ")";
            Idb.execSQL(CREATE_TABLE_LOCATION);
        }catch (Exception e){

        }

    }


    public void addSetting(String id) {
        Idb = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(ID_RAW,id );

        Idb.insert(SETTING_TABLE, null, values);
        Idb.close();
    }
    public void addLocation(String location) {
        Idb = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(LOCATION_NAME,location);

        Idb.insert(LOCATION_HISTORY_TABLE, null, values);
        Idb.close();
    }

    public void addIpSetting(String ip,String active) {
        Idb = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(IP_RAW,ip );
        values.put(ACTIVITY,active );
        Idb.insert(SETTING_IP_TABLE, null, values);
        Idb.close();
    }

    public String getAllSetting() {

        String selectQuery = "SELECT  * FROM " + SETTING_TABLE;
        Idb = this.getWritableDatabase();
        Cursor cursor = Idb.rawQuery(selectQuery, null);
        String idRaw="";
        if (cursor.moveToFirst()) {
            do {

                idRaw=cursor.getString(0);

            } while (cursor.moveToNext());
        }
        return idRaw;
    }

    public List<String> getLocation(String locSearch) {

        String selectQuery = "SELECT  * FROM " + LOCATION_HISTORY_TABLE +" where "+ LOCATION_NAME +
                " LIKE  '"+locSearch+"%'";
        Idb = this.getWritableDatabase();
        Cursor cursor = Idb.rawQuery(selectQuery, null);
       List<String> location=new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {

                location.add(cursor.getString(0));

            } while (cursor.moveToNext());
        }
        return location;
    }

    public String getAllIPSetting() {

        String selectQuery = "SELECT  * FROM " + SETTING_IP_TABLE;
        Idb = this.getWritableDatabase();
        Cursor cursor = Idb.rawQuery(selectQuery, null);
        String idRaw="";
        if (cursor.moveToFirst()) {
            do {

                idRaw=cursor.getString(0);

            } while (cursor.moveToNext());
        }
        return idRaw;
    }


    public String getAllaCTIVESetting() {

        String selectQuery = "SELECT  * FROM " + SETTING_IP_TABLE;
        Idb = this.getWritableDatabase();
        Cursor cursor = Idb.rawQuery(selectQuery, null);
        String idRaw="";
        if (cursor.moveToFirst()) {
            do {

                idRaw=cursor.getString(1);

            } while (cursor.moveToNext());
        }
        return idRaw;
    }

    public void delete() {
        Idb= this.getWritableDatabase();
        Idb.execSQL("DELETE FROM "+SETTING_TABLE); //delete all rows in a table
        Idb.close();
    }

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫","."));
        return newValue;
    }

    public void updateiP(String ipOld, String IP) {
        Idb = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(IP_RAW, IP);
        Idb.update(SETTING_IP_TABLE, values, IP_RAW + " = '" + ipOld + "'", null);
    }

    public void updateaCTIVE(String ipOld, String ACTIVITY) {
        Idb = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ACTIVITY, ACTIVITY);
        Idb.update(SETTING_IP_TABLE, values, ACTIVITY + " = '" + ipOld + "'", null);
    }
}
