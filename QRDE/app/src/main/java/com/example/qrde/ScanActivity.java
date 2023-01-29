package com.example.qrde;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class ScanActivity extends AppCompatActivity {
    private IntentIntegrator qrScan;
    TextView txtReqDtls;
    TextView txtReqRslts;
    Button buttonHome;
    String userID;
    String baseURL;
    String switchURL;

    String name;
    String accountNo;
    String IFSC;
    String aadharNo;
    String MMID;
    String UPIID;
    String phoneNo;
    String billNo;
    String amount;
    String txid;
    String transactionType;

    RequestQueue queue;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        baseURL = intent.getStringExtra("baseURL");
        switchURL = intent.getStringExtra("switchURL");

        final Context context = this;

        txtReqDtls = (EditText) findViewById(R.id.txtRequestDetails);
        txtReqRslts = (EditText) findViewById(R.id.txtRequestResult);
        buttonHome = (Button) findViewById(R.id.btnHome);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("baseURL", baseURL);
                intent.putExtra("switchURL", switchURL);
                startActivity(intent);
            }
        });

        qrScan = new IntentIntegrator(this);
        qrScan.initiateScan();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());

                    transactionType = obj.getString("transaction_type");

                    if (transactionType.equals("ATM")) {
                        name = Decryption.decrypt(obj.getString("name"));
                        accountNo = Decryption.decrypt(obj.getString("account_num"));
                        IFSC = Decryption.decrypt(obj.getString("IFSC"));
                        amount = Decryption.decrypt(obj.getString("amount"));
                        txid = Decryption.decrypt(obj.getString("txID"));
                        String[] separated = txid.split(":");
                        txid = separated[1];
                        aadharNo = "";
                        MMID = "";
                        UPIID = "";
                        phoneNo = "";
                        billNo = "";
                    } else {
                        name = Decryption.decrypt(obj.getString("name"));
                        accountNo = Decryption.decrypt(obj.getString("account_num"));
                        IFSC = Decryption.decrypt(obj.getString("IFSC"));
                        amount = Decryption.decrypt(obj.getString("amount"));
                        aadharNo = Decryption.decrypt(obj.getString("aadhar_num"));
                        MMID = Decryption.decrypt(obj.getString("MMID"));
                        UPIID = Decryption.decrypt(obj.getString("UPI_ID"));
                        phoneNo = Decryption.decrypt(obj.getString("phone_num"));
                        billNo = Decryption.decrypt(obj.getString("bill_number"));
                        txid = Decryption.decrypt(obj.getString("txID"));
                        String[] separated = txid.split(":");
                        txid = separated[1];
                    }
                    enableTransactions(accountNo, IFSC, amount, txid);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.i("Infor", "onActivityResult: " + e.getMessage());
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void enableTransactions(String acctNo, String IFSC, String amt, String txid) {
        queue = Volley.newRequestQueue(this);
        final String[] url = {baseURL + "gettransactionstatus?txid=" + txid};
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
                                txtReqRslts.setText(dataobj.getString("txstatus"));
                            }
                            enableTransaction();
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

    private void enableTransaction(){
        //final String crURL = "";
        String stat = txtReqRslts.getText().toString();
        if (stat.equals("0")) {

            queue = Volley.newRequestQueue(this);
            final String[] url = {baseURL + "getifsc?txid=" + txid};
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
                                    txtReqRslts.setText(dataobj.getString("IFSC"));
                                }
                                String stat = txtReqRslts.getText().toString();
                                if(stat.contains("same")){
                                enableTransactionSame();}
                                else{
                                enableTransactionSwitch();}
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
        else txtReqRslts.setText("Transaction already completed!");
    }

    private void enableTransactionSame(){
        String comm = "";
        if (transactionType.equals("ATM")) comm = "ATMWithdrawal";
        else comm = "AccountTransfer";

        queue = Volley.newRequestQueue(this);
        final String[] url = {baseURL + "drabc?accountNo=" + accountNo + "&amount=" + amount + "&comm=" + comm + "&txid=" + txid};
        final String crURL = baseURL + "crabc?accountNo=" + accountNo + "&amount=" + amount + "&comm=" + comm + "&txid=" + txid;
        //var requestStr = " "?accountNo=" + accountNo + "&amount=" + amount + "&comm=" + comments + "&txid=" + txid;
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url[0], null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            Log.e("Response", response.getString("data"));
                            String txt = txtReqRslts.getText().toString();
                            txtReqRslts.setText(txt + response.getString("data"));
                            if (!transactionType.equals("ATM")) {
                                EnableCredit(crURL);
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

    private void EnableCreditSame(){

    }

    private void enableTransactionSwitch(){
        String drURL;
        String comm = "";
        if (transactionType.equals("ATM")) comm = "ATMWithdrawal";
        else comm = "AccountTransfer";

        drURL = switchURL + "switch?acctNo=" + accountNo + "&ifsc=" + IFSC + "&amt=" + amount + "&txid=" + txid + "&comm=" + comm + "&trtype=dr";
        final String crURL = switchURL + "switch?acctNo=" + accountNo + "&ifsc=" + IFSC + "&amt=" + amount + "&comm=" + comm + "&trtype=cr";

        // prepare the dr Request
        JsonObjectRequest getDrRequest = new JsonObjectRequest(Request.Method.GET, drURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            Log.e("Response", response.getString("data"));
                            Log.e("Response", response.toString());
                            String txt = txtReqRslts.getText().toString();
                            txtReqRslts.setText(txt + response.getString("data"));
                            if (!transactionType.equals("ATM")) {
                                EnableCredit(crURL);
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
        queue.add(getDrRequest);

    }

    private void EnableCredit(String crURL){
        if (!crURL.equals("")) {
            // prepare the cr Request
            JsonObjectRequest getCrRequest = new JsonObjectRequest(Request.Method.GET, crURL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // display response
                            try {
                                Log.e("Response", response.getString("data"));
                                Log.e("Response", response.toString());
                                String txt = txtReqRslts.getText().toString();
                                txtReqRslts.setText(txt + "\n" + response.getString("data"));
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
            queue.add(getCrRequest);
        }
    }
}

