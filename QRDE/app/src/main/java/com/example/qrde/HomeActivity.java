package com.example.qrde;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    Button buttonNewUser;
    Button buttonLogin;

    String baseURL = "http://192.168.43.205:5000/";
    String switchURL = "http://192.168.43.205:5100/";

    @Override
    public void onBackPressed() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        addListenerOnButton();
    }

    public void addListenerOnButton() {
        final Context context = this;
        buttonNewUser = (Button) findViewById(R.id.btnNewUser);
        buttonLogin = (Button) findViewById(R.id.btnLogin);

        buttonNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(HomeActivity.this, NewUserActivity.class);
                intent.putExtra("baseURL", baseURL);
                //Intent intent = new Intent(context, NewUserActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.putExtra("baseURL", baseURL);
                intent.putExtra("switchURL", switchURL);
                startActivity(intent);
            }
        });
    }
}
