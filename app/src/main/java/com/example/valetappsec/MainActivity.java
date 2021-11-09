package com.example.valetappsec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button signInButton, singUp;
    TextView setting;
    ValetDatabase valetDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();

    }

    private void initialization() {

        signInButton = findViewById(R.id.signInButton);
        singUp = findViewById(R.id.singUp);
        setting = findViewById(R.id.setting);
        valetDatabase = new ValetDatabase(MainActivity.this);
        signInButton.setOnClickListener(this);
        singUp.setOnClickListener(this);
        setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.signInButton:

                if(isIpInSetting()) {
                    Intent logInIntent = new Intent(MainActivity.this, LogInActivity.class);
                    startActivity(logInIntent);
                }else {
                    Toast.makeText(this, "Please Write Ip Address In Setting ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.singUp:
                if(isIpInSetting()) {
                Intent singUpIntent = new Intent(MainActivity.this, SingUpValetActivityApp.class);
                startActivity(singUpIntent);
                }else {
                    Toast.makeText(this, "Please Write Ip Address In Setting ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.setting:
                SettingDialog();
                break;
        }
    }

    boolean isIpInSetting() {
        String ips = valetDatabase.getAllIPSetting();
        if (!TextUtils.isEmpty(ips)) {
            return true;
        } else {
            return false;
        }
    }


    void SettingDialog() {
        Dialog packingListDialog = new Dialog(MainActivity.this,R.style.Theme_Dialog);
        packingListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        packingListDialog.setContentView(R.layout.setting_dialog);
        packingListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText ip;
        Button accept, cancel;

        ip = packingListDialog.findViewById(R.id.ip);
        accept = packingListDialog.findViewById(R.id.accept);
        cancel = packingListDialog.findViewById(R.id.cancel);

        String ips = valetDatabase.getAllIPSetting();
        if (!TextUtils.isEmpty(ips)) {
            ip.setText(ips);
        } else {

        }


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(ip.getText().toString())) {
                    if (!TextUtils.isEmpty(ips)) {
                        valetDatabase.updateiP(ips, ip.getText().toString());
                    }else {
                        valetDatabase.addIpSetting(ip.getText().toString(), "0");
                    }
                    packingListDialog.dismiss();
                } else {
                    ip.setError("Required1");
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