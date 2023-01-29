package com.example.qrde;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

public class NewUserActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button buttonHome;
    Button buttonCreateUser;
    Spinner spinner;
    RequestQueue queue;
    EditText txtName;
    EditText txtEMail;
    EditText txtMob;
    EditText txtPwd1;
    EditText txtPwd2;
    TextView txtMsg;
    String baseURL;
    String customerID;

    @Override
    public void onBackPressed() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newuser);
        Intent intent = getIntent();
        baseURL = intent.getStringExtra("baseURL");
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        final Context context = this;
        buttonCreateUser = (Button) findViewById(R.id.btnCreateUser);
        buttonHome = (Button) findViewById(R.id.btnHome);
        spinner = (Spinner) findViewById(R.id.spinner);
        txtName = (EditText) findViewById(R.id.editTextName);
        txtEMail = (EditText) findViewById(R.id.editTextEmail);
        txtMob = (EditText) findViewById(R.id.editTextMobile);
        txtPwd1 = (EditText) findViewById(R.id.editTextPwd1);
        txtPwd2 = (EditText) findViewById(R.id.editTextPwd2);
        txtMsg = (TextView) findViewById(R.id.txtViewMsg);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, HomeActivity.class);
                startActivity(intent);
            }
        });

        buttonCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(validateData()){
                    setCustomerDetails();
                }
            }
        });
        spinner.setOnItemSelectedListener(this);
        getCustomers();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        if (position > 0) {
            String custID = parent.getItemAtPosition(position).toString();
            customerID = custID;
            getCustomerName(custID);
        }
    }

    private void getCustomerName(String custID) {
        queue = Volley.newRequestQueue(this);
        final String[] url = { baseURL + "getcustomername?custID=" + custID};
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url[0], null,
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
                                txtName.setText(dataobj.getString("CustomerName"));
                            }
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    private void getCustomers() {
        queue = Volley.newRequestQueue(this);
        final String[] url = { baseURL + "getcustomers"};
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url[0], null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            Log.e("Response", response.getString("data"));
                            Log.e("Response", response.toString());
                            parseInfo(response.toString());
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

    public void parseInfo(String strResp) {
        List<String> customersList = new ArrayList<>(100);
        //Log.e("Data", txtHidden.getText().toString());
        try {
            JSONObject jsonObject = new JSONObject(strResp);
            Log.e("parseInfo", jsonObject.getString("data"));
            JSONArray dataArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                String customerID;
                JSONObject dataobj = dataArray.getJSONObject(i);
                customerID = dataobj.getString("customerID");
                Log.e("customerID", customerID);
                customersList.add(customerID);
            }
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, customersList);
            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        } catch (Exception e){
            Log.e("Exception", e.getMessage());
        }
    }

    public boolean validateData() {
        if(txtEMail.getText().toString().equals("")) {
            txtEMail.setError("E-Mail mandatory");
            txtEMail.requestFocus();
            return false; }
        else if(txtMob.getText().toString().equals("")) {
            txtMob.setError("Mobile mandatory");
            txtMob.requestFocus();
            return false;}
        else if(txtPwd1.getText().toString().equals("")) {
            txtPwd1.setError("Password 1 mandatory");
            txtPwd1.requestFocus();
            return false;}
        else if(txtPwd2.getText().toString().equals("")) {
            txtPwd2.setError("Password 2 mandatory");
            txtPwd2.requestFocus();
            return false;}
        else if(!(txtPwd1.getText().toString().equals(txtPwd2.getText().toString()))) {
            txtPwd1.setError("Passwords not matched");
            txtPwd1.requestFocus();
            return false;}
        return true;
    }

    private void setCustomerDetails() {
        queue = Volley.newRequestQueue(this);
        final String[] url = { baseURL + "setcustomerdetails?custID=" + customerID + "&eMail=" + txtEMail.getText().toString() + "&mobile=" + txtMob.getText().toString() + "&pwd=" + txtPwd1.getText().toString()};
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url[0], null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            Log.e("Response", response.getString("data"));
                            Log.e("Response", response.toString());
                            if(response.getString("data").equals("true"))
                                txtMsg.setText("User created successfully");
                            else
                                txtMsg.setText("User creation unsuccessful");
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