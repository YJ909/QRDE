package com.example.qrde;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class ATMActivity extends AppCompatActivity {
    EditText editName;
    EditText editAccount;
    EditText editIFSC;
    EditText editAmount;
    EditText editTxID;
    Button buttonGenQR;
    Button buttonBack;
    String userID;
    String baseURL;
    RequestQueue queue;

    @Override
    public void onBackPressed() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        baseURL = intent.getStringExtra("baseURL");

        addListenerOnButton();
    }

    public void addListenerOnButton() {
        final Context context = this;
        buttonBack = (Button) findViewById(R.id.btnHome);
        buttonGenQR = (Button) findViewById(R.id.btnGenerateQR);
        editName = (EditText) findViewById(R.id.editTextName);
        editAccount = (EditText) findViewById(R.id.editTextAccount);
        editAmount = (EditText) findViewById(R.id.editTextAmount);
        editIFSC = (EditText) findViewById(R.id.editTextIFSC);
        editTxID = (EditText) findViewById(R.id.editTextTxID);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("baseURL", baseURL);
                startActivity(intent);
            }
        });

        buttonGenQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                EnableTransaction();
                //getTransactionID();

            }
        });

        getCustomerAccountDetails(userID);
    }

    private void getCustomerAccountDetails(String custID) {
        queue = Volley.newRequestQueue(this);
        final String[] url = { baseURL + "getaccountdetails?custID=" + custID};
        try{
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
                                String details = dataobj.getString("dtls");
                                String[] separated = details.split(":");
                                editName.setText(separated[0]);
                                editAccount.setText(separated[1]);
                                editIFSC.setText(separated[2]);
                            }
                        } catch (JSONException e) {

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
        } catch (Exception e) {
            Log.e("Response", e.getMessage());
        }
    }

    private void EnableTransaction(){
        setTransactionDetails();
        getTransactionID();
    }

    private void setTransactionDetails() {
        queue = Volley.newRequestQueue(this);
        final String[] url = { baseURL + "settransactionentry?"
                + "acctNo=" + editAccount.getText().toString() + "&amount=" + editAmount.getText().toString() + "&comments=ATMWithdrawal"};
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
                                editTxID.setText("Transaction created successfully");
                            else
                                editTxID.setText("Transaction creation unsuccessful");
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

    private void getTransactionID() {
        queue = Volley.newRequestQueue(this);
        final String[] url = { baseURL + "gettransactionentry"};
        try{
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
                            JSONObject dataobj = dataArray.getJSONObject(0);
                            String details = dataobj.getString("txid");
                            //String[] separated = details.split(":");
                            editTxID.setText(details);
                            enableQRGen();
                        } catch (JSONException e) {
                            //Log.e("Response", e.getMessage());
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
        } catch (Exception e) {
            Log.e("Response", e.getMessage());
        }
    }

    private void enableQRGen(){
        final JSONObject outputObj = new JSONObject();
        try {
            String name, account_num, IFSC, amount, txid;
            name = Encryption.encrypt(editName.getText().toString());
            account_num = Encryption.encrypt(editAccount.getText().toString());
            IFSC = Encryption.encrypt(editIFSC.getText().toString());
            amount = Encryption.encrypt(editAmount.getText().toString());
            txid = Encryption.encrypt(editTxID.getText().toString());//editTxID.getText().toString());
            outputObj.put("name", name);
            outputObj.put("account_num", account_num);
            outputObj.put("IFSC", IFSC);
            outputObj.put("amount", amount);
            outputObj.put("txID", txid);
            outputObj.put("transaction_type", "ATM");
            System.out.println(outputObj.toString());

            Intent intent = new Intent(ATMActivity.this, GeneratedQRActivity.class);
            intent.putExtra("message", outputObj.toString());
            intent.putExtra("userID", userID);
            intent.putExtra("baseURL", baseURL);
            startActivity(intent);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
