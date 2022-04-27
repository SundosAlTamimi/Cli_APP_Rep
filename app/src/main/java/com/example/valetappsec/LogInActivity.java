package com.example.valetappsec;

import static com.example.valetappsec.GlobalVairable.singUpUserTableGlobal;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.valetappsec.Json.ImportJson;
import com.example.valetappsec.Model.UserService;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    Button logInButton;
    ValetDatabase valetDatabase;
    EditText  userName,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.valet_log_in_activity);

        initialization();
    }


    private void initialization() {

        logInButton=findViewById(R.id.logInButton);
        userName=findViewById(R.id.userNameLogIn);
        password=findViewById(R.id.passwordLogIn);
valetDatabase=new ValetDatabase(this);
        logInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.logInButton:

                if (!TextUtils.isEmpty(userName.getText().toString())) {
                    if (!TextUtils.isEmpty(password.getText().toString())) {
                        ImportJson importJson = new ImportJson(LogInActivity.this);
                        importJson.logInAuth(userName.getText().toString(), password.getText().toString());
                    } else {
                        password.setError("Required !");
                    }
                } else {
                    userName.setError("Required !");
                }

                break;
        }
    }

    public void intentToMain() {

        try {
            valetDatabase.deleteUser();
        }catch (Exception e){

        }

        UserService userService=new UserService();
        userService.setUserid(singUpUserTableGlobal.getId());
        userService.setUserName(singUpUserTableGlobal.getUserName());
        userService.setUserPhoneNo(singUpUserTableGlobal.getPhoneNo());
        userService.setOnOff("0");
        valetDatabase.addUserService(userService);

        Log.e("MyServicesFor",""+isMyServiceRunning(MyServicesForNotification.class));

        if(!isMyServiceRunning(MyServicesForNotification.class)) {
            startService(new Intent(LogInActivity.this, MyServicesForNotification.class));
            Log.e("MyServicesFor_2", "" + isMyServiceRunning(MyServicesForNotification.class));
        }
        Intent logInIntent=new Intent(LogInActivity.this, MainValetActivity.class);
        startActivity(logInIntent);
    }





    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
