package com.example.qrde;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
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


public class DetailsActivity extends AppCompatActivity{
    TextView txtCustID;
    TextView txtAmount;
    TextView txtName;
    TextView txtAccount;
    Button buttonHome;
    TableLayout tableDetails;
    String userID;
    RequestQueue queue;
    String baseURL;

    @Override
    public void onBackPressed() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        final Context context = this;
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        baseURL = intent.getStringExtra("baseURL");

        txtCustID = (TextView) findViewById(R.id.customerID);
        txtName = (TextView) findViewById(R.id.customerName);
        txtAmount = (TextView) findViewById(R.id.customerAmount);
        txtAccount = (TextView) findViewById(R.id.accountNo);
        buttonHome = (Button) findViewById(R.id.btnHome);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("baseURL", baseURL);
                startActivity(intent);
            }
        });

        getCustomerAccountDetails(userID);
        init(userID);
    }

    public void init(String custID) {
        queue = Volley.newRequestQueue(this);
        final String[] url = {baseURL + "getaccountsummary?custID=" + custID};
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
        tableDetails = (TableLayout) findViewById(R.id.displayDetails);
        TableRow row= new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);

        TableRow tbrow0 = new TableRow(this);

        TextView tv0 = new TextView(this);
        tv0.setText(" Tx ID ");
        tv0.setTextColor(Color.BLACK);
        tbrow0.addView(tv0);

        TextView tv1 = new TextView(this);
        tv1.setText(" Tx Type ");
        tv1.setTextColor(Color.BLACK);
        tbrow0.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText(" Amount ");
        tv2.setTextColor(Color.BLACK);
        tbrow0.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText(" Comment ");
        tv3.setTextColor(Color.BLACK);
        tbrow0.addView(tv3);

        TextView tv4 = new TextView(this);
        tv4.setText(" Date ");
        tv4.setTextColor(Color.BLACK);
        tbrow0.addView(tv4);

        tableDetails.addView(tbrow0);
        try {
            JSONObject jsonObject = new JSONObject(strResp);
            JSONArray dataArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataobj = dataArray.getJSONObject(i);
                int Slno = i + 1;
                String TXID = dataobj.getString("id");
                String TXType = dataobj.getString("txtype");
                String Amount = dataobj.getString("amount");
                String Date = dataobj.getString("txdate");
                String Comment = dataobj.getString("comments");

                Log.e("Slno", Slno + TXID + TXType + Amount + Date + Comment);

                TableRow tbrow = new TableRow(this);

                TextView t1v = new TextView(this);
                t1v.setText("" + TXID);
                t1v.setTextColor(Color.GRAY);
                t1v.setGravity(Gravity.CENTER);
                tbrow.addView(t1v);

                TextView t2v = new TextView(this);
                t2v.setText("" + TXType);
                t2v.setTextColor(Color.GRAY);
                t2v.setGravity(Gravity.CENTER);
                tbrow.addView(t2v);

                TextView t3v = new TextView(this);
                t3v.setText("Rs." + Amount);
                t3v.setTextColor(Color.GRAY);
                t3v.setGravity(Gravity.CENTER);
                tbrow.addView(t3v);

                TextView t4v = new TextView(this);
                t4v.setText("" + Comment);
                t4v.setTextColor(Color.GRAY);
                t4v.setGravity(Gravity.CENTER);
                tbrow.addView(t4v);

                TextView t5v = new TextView(this);
                t5v.setText("" + Date);
                t5v.setTextColor(Color.GRAY);
                t5v.setGravity(Gravity.CENTER);
                tbrow.addView(t5v);

                tableDetails.addView(tbrow);
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    private void getCustomerAccountDetails(String custID) {
        queue = Volley.newRequestQueue(this);
        final String[] url = { baseURL + "getcustomerdetails?custID=" + custID};
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
                                txtName.setText(separated[0]);
                                txtCustID.setText(separated[1]);
                                txtAmount.setText(separated[2]);
                                txtAccount.setText(separated[3]);
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
}

