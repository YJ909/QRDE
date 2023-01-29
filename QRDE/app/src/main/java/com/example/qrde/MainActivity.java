package com.example.qrde;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button buttonATM;
    Button buttonTransfer;
    Button buttonScanQR;
    Button buttonGetDetails;
    Button buttonLogout;
    String userID;
    String baseURL;
    String switchURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        baseURL = intent.getStringExtra("baseURL");
        switchURL = intent.getStringExtra("switchURL");

        addListenerOnButton();
    }

    public void addListenerOnButton() {
        final Context context = this;
        buttonATM = (Button) findViewById(R.id.btnATM);
        buttonTransfer = (Button) findViewById(R.id.btnTransfer);
        buttonScanQR = (Button) findViewById(R.id.btnScanQR);
        buttonGetDetails = (Button) findViewById(R.id.btnDetails);
        buttonLogout = (Button) findViewById(R.id.btnLogout);

        buttonATM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, ATMActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("baseURL", baseURL);
                intent.putExtra("switchURL", switchURL);
                startActivity(intent);
            }
        });

        buttonTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, PaymentsActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("baseURL", baseURL);
                intent.putExtra("switchURL", switchURL);
                startActivity(intent);
            }
        });

        buttonScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, ScanActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("baseURL", baseURL);
                intent.putExtra("switchURL", switchURL);
                startActivity(intent);
            }
        });

        buttonGetDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("baseURL", baseURL);
                intent.putExtra("switchURL", switchURL);
                startActivity(intent);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
