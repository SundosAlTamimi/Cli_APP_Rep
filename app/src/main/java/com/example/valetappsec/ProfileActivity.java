package com.example.valetappsec;


import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.valetappsec.GlobalVairable.singUpUserTableGlobal;

public class ProfileActivity extends AppCompatActivity {
    EditText userName,password,email,phoneNo,carType,carModel,carColor,carLot;
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


        userName.setText(singUpUserTableGlobal.getUserName());
        password.setText(singUpUserTableGlobal.getPassword());
        email.setText(singUpUserTableGlobal.getEmail());
        phoneNo.setText(singUpUserTableGlobal.getPhoneNo());
        carType.setText(singUpUserTableGlobal.getCarType());
        carModel.setText(singUpUserTableGlobal.getCarModel());
        carColor.setText(singUpUserTableGlobal.getCarColor());
        carLot.setText(singUpUserTableGlobal.getCarLot());

    }

}
