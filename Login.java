package com.neews.sense_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    EditText et_username, et_password;
    Button btn_login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        et_username = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_password);

        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (et_username.getText().toString().equals("admin") &&
                        et_password.getText().toString().equals("admin")) {
                    //Toast.makeText(getApplicationContext(), "Redirecting...",
                    //  Toast.LENGTH_SHORT).show();
                    Intent I=new Intent(Login.this,Diagonastics.class);
                    startActivity(I);
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Credentials",
                            Toast.LENGTH_SHORT).show();


                }
            }});}

  /*  protected void onStart(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }*/
   /* public void btn_login1(View view) {
        if (et_username.getText().toString().equals("admin") &&
                et_password.getText().toString().equals("admin")) {
            //Toast.makeText(getApplicationContext(), "Redirecting...",
                  //  Toast.LENGTH_SHORT).show();
            Intent I=new Intent(Login.this, RegistrationPage.class);
            startActivity(I);
        } else {
            Toast.makeText(getApplicationContext(), "Wrong Credentials",
                    Toast.LENGTH_SHORT).show();


        }

    }*/


        }
