package com.example.qrde;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

public class PaymentsActivity extends AppCompatActivity {
    Button buttonGenerateQR;
    Button buttonHome;
    String userID;
    String baseURL;
    ImageView qrImage;
    ImageView speak;
    ImageView speak2;
    ImageView speak3;
    ImageView speak4;
    ImageView speak5;
    ImageView speak6;
    ImageView speak7;
    ImageView speak8;
    ImageView speak9;
    RequestQueue queue;

    EditText editName;
    EditText editAccount;
    EditText editIFSC;
    EditText editAadhar;
    EditText editMMID;
    EditText editUPIID;
    EditText editPhone;
    EditText editAmount;
    EditText editBillNo;
    EditText editTxid;

    private final int REQ_CODE = 100;
    private final int REQ_CODE2 = 101;
    private final int REQ_CODE3 = 102;
    private final int REQ_CODE4 = 103;
    private final int REQ_CODE5 = 104;
    private final int REQ_CODE6 = 105;
    private final int REQ_CODE7 = 106;
    private final int REQ_CODE8 = 107;
    private final int REQ_CODE9 = 108;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        baseURL = intent.getStringExtra("baseURL");

        addListenerOnButton();
    }

    public void addListenerOnButton() {
        final Context context = this;
        buttonGenerateQR = (Button) findViewById(R.id.btnGenerateQR);
        buttonHome = (Button) findViewById(R.id.btnHome);
        editName = (EditText) findViewById(R.id.editTextName);
        editAadhar = (EditText) findViewById(R.id.editTextAadhar);
        editIFSC = (EditText) findViewById(R.id.editTextIFSC);
        editAccount = (EditText) findViewById(R.id.editTextAcctNo);
        editMMID = (EditText) findViewById(R.id.editTextMMID);
        editUPIID = (EditText) findViewById(R.id.editTextUPIID);
        editAmount = (EditText) findViewById(R.id.editTextAmount);
        editBillNo = (EditText) findViewById(R.id.editTextBill);
        editTxid = (EditText) findViewById(R.id.editTextTxid);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("baseURL", baseURL);
                startActivity(intent);
            }
        });

        buttonGenerateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                EnableTransaction();
            }
        });
    }

    private void EnableTransaction(){
        setTransactionDetails();
        getTransactionID();
    }

    private void setTransactionDetails() {
        queue = Volley.newRequestQueue(this);
        final String[] url = { baseURL + "settransactionentry?"
                + "acctNo=" + editAccount.getText().toString() + "&amount=" + editAmount.getText().toString() + "&comments=AccountTransfer"};
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
                                editTxid.setText("Transaction created successfully");
                            else
                                editTxid.setText("Transaction creation unsuccessful");
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
                                editTxid.setText(dataobj.getString("txid"));
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
        final JSONObject outputObj = new JSONObject();
        try {
            String name_e, account_num_e, IFSC_e, aadhar_num_e, mmid_e, UPI_ID_e, phone_num_e, bill_no_e, amount_e, txid_e;
            name_e = Encryption.encrypt(editName.getText().toString());
            IFSC_e = Encryption.encrypt(editIFSC.getText().toString());
            aadhar_num_e = Encryption.encrypt(editAadhar.getText().toString());
            mmid_e = Encryption.encrypt(editMMID.getText().toString());
            UPI_ID_e = Encryption.encrypt(editUPIID.getText().toString());
            account_num_e = Encryption.encrypt(editAccount.getText().toString());
            phone_num_e = Encryption.encrypt(editPhone.getText().toString());
            bill_no_e = Encryption.encrypt(editBillNo.getText().toString());
            amount_e = Encryption.encrypt(editAmount.getText().toString());
            txid_e = Encryption.encrypt(editTxid.getText().toString());

            outputObj.put("name", name_e);
            outputObj.put("account_num", account_num_e);
            outputObj.put("IFSC", IFSC_e);
            outputObj.put("aadhar_num", aadhar_num_e);
            outputObj.put("MMID", mmid_e);
            outputObj.put("UPI_ID", UPI_ID_e);
            outputObj.put("phone_num", phone_num_e);
            outputObj.put("bill_number", bill_no_e);
            outputObj.put("amount", amount_e);
            outputObj.put("txID", txid_e);
            outputObj.put("transaction_type", "TX");

            System.out.println(outputObj.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Intent intent = new Intent(PaymentsActivity.this, GeneratedQRActivity.class);
            intent.putExtra("message", outputObj.toString());
            intent.putExtra("userID", userID);
            intent.putExtra("baseURL", baseURL);
            startActivity(intent);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}