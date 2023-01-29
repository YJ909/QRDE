package com.example.qrde;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    Button buttonLogin;
    Button buttonHome;
    EditText txtCustID;
    EditText txtPassword;
    TextView txtMsg;

    RequestQueue queue;
    String baseURL;
    String switchURL;

    @Override
    public void onBackPressed() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        baseURL = intent.getStringExtra("baseURL");
        switchURL = intent.getStringExtra("switchURL");
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        final Context context = this;
        txtCustID = (EditText) findViewById(R.id.custID);
        txtPassword = (EditText) findViewById(R.id.pwdKey);
        buttonLogin = (Button) findViewById(R.id.btnLogin);
        buttonHome = (Button) findViewById(R.id.btnHome);
        txtMsg = (TextView) findViewById(R.id.txtMsg);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, HomeActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final String strPwd = txtPassword.getText().toString();
                final String userID = txtCustID.getText().toString();
                if(validateData()) {
                    verifyLogin(userID, strPwd);
                }
            }
        });
    }

    public boolean validateData() {
        if(txtPassword.getText().toString().equals("")) {
            txtPassword.setError("Password mandatory");
            txtPassword.requestFocus();
            return false; }
        else if(txtCustID.getText().toString().equals("")) {
            txtCustID.setError("Customer ID mandatory");
            txtCustID.requestFocus();
            return false;}
        return true;
    }

    private void verifyLogin(final String userID, final String strPwd) {
        queue = Volley.newRequestQueue(this);
        final String url = baseURL + "authenticate?custID=" + userID + "&pwd=" + strPwd ;
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            Log.e("Response", response.getString("data"));
                            Log.e("Response", response.toString());
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataobj = dataArray.getJSONObject(i);
                                txtMsg.setText(dataobj.getString("pwd"));
                            }
                            if(txtMsg.getText().toString().equals(strPwd)){
                                txtMsg.setText("Authentication successful");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("userID", userID);
                                intent.putExtra("baseURL", baseURL);
                                intent.putExtra("switchURL", switchURL);
                                startActivity(intent);
                            }
                            else {
                                txtMsg.setText("Invalid login credentials entered !");
                            }
                            Log.e("Response", response.getString("data"));
                        } catch (JSONException e) {
                            Log.e("Response", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error.Response", error.toString());
                    }
                }
        );

        // add it to the RequestQueue
        queue.add(getRequest);
    }

}
