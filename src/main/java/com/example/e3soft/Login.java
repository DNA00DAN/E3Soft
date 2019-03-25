package com.example.e3soft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {

    private Button loginBtn, exitBtn;

    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        exitBtn = (Button) findViewById(R.id.extBtn);

        username = (EditText) findViewById(R.id.usernameTxt);
        password = (EditText) findViewById(R.id.passwordTxt);

        loginBtn.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                Intent startNewAtivityOpen = new Intent(Login.this, Home.class);

                startActivity(startNewAtivityOpen);
                finish();
            }
        });

        exitBtn.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                finish();

            }
        });

    }
}

