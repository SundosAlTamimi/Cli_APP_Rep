package com.example.valetappsec;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.valetappsec.Json.ImportJson;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    Button logInButton;
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
        Intent logInIntent=new Intent(LogInActivity.this, MainValetActivity.class);
        startActivity(logInIntent);
    }
}
