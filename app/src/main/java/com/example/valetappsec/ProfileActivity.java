package com.example.valetappsec;


import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.valetappsec.Json.ImportJson;

import static com.example.valetappsec.GlobalVairable.singUpUserTableGlobal;

public class ProfileActivity extends AppCompatActivity {
    EditText userName,password,email,phoneNo,carType,carModel,carColor,carLot;
    ImageView carPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.valet_profile_activity);
        initialization();

    }

    private void initialization() {
        userName=findViewById(R.id.userName);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);
        phoneNo=findViewById(R.id.phoneNo);
        carType=findViewById(R.id.carType);
        carModel=findViewById(R.id.carModel);
        carColor=findViewById(R.id.carColor);
        carLot=findViewById(R.id.carLot);
        carPic=findViewById(R.id.carPic);

        ImportJson importJson=new ImportJson(ProfileActivity.this);
       // importJson.getImage(1);
        userName.setText(singUpUserTableGlobal.getUserName());
        password.setText(singUpUserTableGlobal.getPassword());
        email.setText(singUpUserTableGlobal.getEmail());
        phoneNo.setText(singUpUserTableGlobal.getPhoneNo());
        carType.setText(singUpUserTableGlobal.getCarType());
        carModel.setText(singUpUserTableGlobal.getCarModel());
        carColor.setText(singUpUserTableGlobal.getCarColor());
        carLot.setText(singUpUserTableGlobal.getCarLot());

    }

    public void fillImage() {
        carPic.setImageBitmap(singUpUserTableGlobal.getCarPicBitmap());
    }
}
