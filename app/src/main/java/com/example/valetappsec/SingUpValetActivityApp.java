package com.example.valetappsec;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.valetappsec.Json.ExportJson;
import com.example.valetappsec.Json.ImportJson;
import com.example.valetappsec.Model.SingUpClientModel;

import java.io.ByteArrayOutputStream;

public class SingUpValetActivityApp extends AppCompatActivity implements View.OnClickListener {

    Button next,singUp;
    EditText userName,phoneNo,password,email,
             carType,carModel,carLot,carColor;

    LinearLayout sing_up_user_info,sing_up_car_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sing_up_layout);
        initialization();



    }

    private void initialization() {
        next=findViewById(R.id.btn_next);
        singUp=findViewById(R.id.singUp);
        userName=findViewById(R.id.userName);
        phoneNo=findViewById(R.id.phoneNo);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);
        carType=findViewById(R.id.carType);
        carModel=findViewById(R.id.carModel);
        carLot=findViewById(R.id.carLot);
        carColor=findViewById(R.id.carColor);

        sing_up_user_info=findViewById(R.id.sing_up_user_info);
        sing_up_car_info=findViewById(R.id.sing_up_car_info);

        next.setOnClickListener(this);
        singUp.setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()){

            case R.id.btn_next:

                checkNext();

                break;
            case R.id.singUp:

                saveClient();
                break;


        }
    }


    private void checkNext(){

        if (!TextUtils.isEmpty(userName.getText().toString())) {
            if (!TextUtils.isEmpty(password.getText().toString())) {
                if (!TextUtils.isEmpty(email.getText().toString())) {
                    if (!TextUtils.isEmpty(phoneNo.getText().toString())) {
                        sing_up_user_info.setVisibility(View.GONE);
                        sing_up_car_info.setVisibility(View.VISIBLE);
                    } else {
                        phoneNo.setError("Required ");
                    }
                } else {
                    email.setError("Required ");
                }
            } else {
                password.setError("Required ");
            }
        } else {
            userName.setError("Required ");
        }

    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private void saveClient() {
        if (!TextUtils.isEmpty(carType.getText().toString())) {
            if (!TextUtils.isEmpty(carModel.getText().toString())) {
                if (!TextUtils.isEmpty(carColor.getText().toString())) {
                    if (!TextUtils.isEmpty(carLot.getText().toString())) {

                        SingUpClientModel singUpClientModel=new SingUpClientModel();
                        singUpClientModel.setUserName(userName.getText().toString());
                        singUpClientModel.setPhoneNo(phoneNo.getText().toString());
                        singUpClientModel.setEmail(email.getText().toString());
                        singUpClientModel.setPassword(password.getText().toString());
                        singUpClientModel.setCarType(carType.getText().toString());
                        singUpClientModel.setCarColor(carColor.getText().toString());
                        singUpClientModel.setCarLot(carLot.getText().toString());
                        singUpClientModel.setCarModel(carModel.getText().toString());
                        singUpClientModel.setCarPic("null");

                        ExportJson exportJson=new ExportJson(SingUpValetActivityApp.this);
                       exportJson.SingUpCaptain(SingUpValetActivityApp.this,singUpClientModel);


                    } else {
                        carLot.setError("Required ");
                    }
                } else {
                    carColor.setError("Required ");
                }
            } else {
                carModel.setError("Required ");
            }
        } else {
            carType.setError("Required ");
        }
    }
}
