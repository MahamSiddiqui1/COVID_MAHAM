package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SyncDataActivities;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.Adt_SyncData.Adt_SyncItems;
import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SyncChildHifazitiTeekey_Activity extends AppCompatActivity {

    Context ctx = SyncChildHifazitiTeekey_Activity.this;
    ListView lv;
    ImageView iv_navigation_drawer, iv_sync;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
    Adt_SyncItems adt;
    Dialog alertDialog;
    private Handler mHandler = new Handler();
    TextView tv_record;
    String mData[][], pos_value;
    String temp = "0";
    LinearLayout ll_pbProgress;

    String smsNumber;
    Dialog progressDialog;
    long mLastClickTime = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_khandan_reg_);

        temp="0";
        //ListView
        lv = findViewById(R.id.lv);

        //TextView
        tv_record = findViewById(R.id.tv_record);
        TextView tv = findViewById(R.id.tv);
        tv.setText("سینک بچے کے حفاظتی ٹیکے");

        //        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_sync = findViewById(R.id.iv_sync);

        iv_navigation_drawer.setVisibility(View.GONE);

        ll_pbProgress = findViewById(R.id.ll_pbProgress);
        ll_pbProgress.setVisibility(View.VISIBLE);


        //Get shared SMS Number
        try {
            SharedPreferences prefelse = ctx.getApplicationContext().getSharedPreferences("SMS_Number", 0); // 0 - for private mode
            String shared_sms_number = prefelse.getString("sms_number", null); // getting String
            smsNumber = shared_sms_number;
            Log.d("000105", "USER UID: " + shared_sms_number);

        } catch (Exception e) {
            Log.d("000105", "Shared Err:" + e.getMessage());
        }


        /*adt = new Adt_SyncItems(ctx, arrayList);
        lv.setAdapter(adt);
        new Task().execute();*/


        iv_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                if (temp.equalsIgnoreCase("0")) {
                    alertDialog = new Dialog(ctx);
                    LayoutInflater layout = LayoutInflater.from(ctx);
                    final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

                    alertDialog.setContentView(dialogView);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    alertDialog.show();

                    Log.d("000105", "IFFFF");
                } else {
                    Log.d("000105", "ELssssss");
                }

                if (isConnectingToInternet()) {
                    if (temp.equalsIgnoreCase("0")) {
                        Toast.makeText(ctx, "Data sending through Internet.", Toast.LENGTH_SHORT).show();
                        Log.d("000105", "IFFFF 2");
                    } else {
                        Log.d("000105", "ELssssss 2");
                    }
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                if (arrayList.size() > 0) {

                                    sendPostRequest_Overall(arrayList.get(0).get("member_uid"), arrayList.get(0).get("vaccine_id"),
                                            arrayList.get(0).get("record_data"), arrayList.get(0).get("metadata"), arrayList.get(0).get("added_by"), arrayList.get(0).get("added_on"));

                                } else {
                                    alertDialog.dismiss();
                                    onBackPressed();
                                    //  Toast.makeText(ctx, "No data found", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                alertDialog.dismiss();
                                Log.d("000105", "Error:" + e.getMessage());
                            }
                        }
                    }, 1000);


                } else {
                    if (temp.equalsIgnoreCase("0")) {
                        Toast.makeText(ctx, "Data sending through SMS.", Toast.LENGTH_SHORT).show();

                        Log.d("000105", "IFFFF OFFLINE");
                    } else {
                        Log.d("000105", "ELssssss OFFLINE");
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendSMSMethod_Overall(arrayList.get(0).get("member_uid"), arrayList.get(0).get("vaccine_id"), arrayList.get(0).get("record_data"), arrayList.get(0).get("metadata"), arrayList.get(0).get("added_by"), arrayList.get(0).get("added_on"));

                        }
                    }, 1500);

                  //  Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                }


            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Log.d("000105", " mLastClickTime: " + mLastClickTime);



                if (isConnectingToInternet()) {
                    Toast.makeText(ctx, "Data sending through Internet.", Toast.LENGTH_SHORT).show();

                    try {
                        alertDialog = new Dialog(ctx);
                        LayoutInflater layout = LayoutInflater.from(ctx);
                        final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

                        alertDialog.setContentView(dialogView);
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialog.show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                pos_value = String.valueOf(position);
                                //sendPostRequest(mData[position][1], mData[position][2], mData[position][3], mData[position][8], mData[position][9], mData[position][11]);
                                sendPostRequest(arrayList.get(position).get("member_uid"), arrayList.get(position).get("vaccine_id"), arrayList.get(position).get("record_data"), arrayList.get(position).get("metadata"), arrayList.get(position).get("added_by"), arrayList.get(position).get("added_on"));


                            }
                        }, 1000);

                    } catch (Exception e) {
                        Log.d("000105", "Catach Err: " + e.getMessage());
                    }

                } else {

                    Toast.makeText(ctx, "Data sending through SMS.", Toast.LENGTH_SHORT).show();

                    pos_value = String.valueOf(position);
                   // sendSMSMethod(mData[position][1], mData[position][2], mData[position][3], mData[position][8], mData[position][9], mData[position][11]);
                    sendSMSMethod(arrayList.get(position).get("member_uid"), arrayList.get(position).get("vaccine_id"), arrayList.get(position).get("record_data"), arrayList.get(position).get("metadata"), arrayList.get(position).get("added_by"), arrayList.get(position).get("added_on"));

                    // Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("000105", "onResume !!!!!!!!!!!! ");

        try{
            adt = new Adt_SyncItems(ctx, arrayList);
            lv.setAdapter(adt);
            new Task().execute();
        }catch (Exception e)
        {
            Log.d("000105", "onResume Error: " +e.getMessage());

        }
    }

    private void sendPostRequest(final String member_uid, final String vacine_uid, final String record_data, final String data, final String added_by, final String added_on) {

        final String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/child/vaccinations";

        Log.d("000105", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {
                    //   Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {
                        Log.d("000105", "Response:    " + response);

                        Lister ls = new Lister(ctx);
                       ls.createAndOpenDB();

                        String update_record = "UPDATE CVACCINATION SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + member_uid + "'AND added_on= '" + added_on + "'AND vaccine_id= '" + vacine_uid + "'";

                        ls.executeNonQuery(update_record);

                        final Snackbar snackbar = Snackbar.make(findViewById(R.id.sync_khandan_layout), "ڈیٹا سنک ہوگیا ہے.", Snackbar.LENGTH_SHORT);
                        View mySbView = snackbar.getView();
                        mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                        TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(16);
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                        }
                        snackbar.setDuration(3000);
                        snackbar.show();

                        arrayList.remove(Integer.parseInt(pos_value));
                        if (arrayList.size() > 0) {
                            alertDialog.dismiss();
                            adt.notifyDataSetChanged();

                        } else {
                            adt.notifyDataSetChanged();
                            alertDialog.dismiss();
                            tv_record.setVisibility(View.VISIBLE);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    finish();
                                }
                            },1500);
                        }


                    } else {
                        alertDialog.dismiss();
                        Log.d("000105", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, "ڈیٹا سروس پر سینک نہیں ہوا.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    alertDialog.dismiss();
                    Log.d("000105", "Err: " + e.getMessage());
                    Toast.makeText(ctx, "ڈیٹا سینک نہیں ہوا", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000105", "onErrorResponse: " + error.getMessage());

                String string = new String(error.networkResponse.data);
                Log.d("000105", "onErrorResponse String: " + string);
                Log.d("000105", "onErrorResponse Status: " + error.networkResponse.statusCode);

                if (string.contains("Duplicate entry"))
                {
                    Log.d("000105", "Duplicate Entry YES !!!!!!!!: ");
                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.sync_khandan_layout), "ڈیٹا سروس پر پہلے سے سینک ہے.", Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(16);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                    }
                    snackbar.setDuration(4000);
                    snackbar.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            arrayList.remove(Integer.parseInt(pos_value));
                            if (arrayList.size()> 0)
                            {
                                alertDialog.dismiss();
                                adt.notifyDataSetChanged();
                            }
                            else{
                                adt.notifyDataSetChanged();
                                alertDialog.dismiss();
                                tv_record.setVisibility(View.VISIBLE);

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        finish();
                                    }
                                },1500);

                            }
                        }
                    },2500);

                }
                else{
                    alertDialog.dismiss();
                    Log.d("000105", "Duplicate Entry NOT: ");
                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.sync_khandan_layout), "ڈیٹا سینک نہیں ہوا.", Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(16);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                    }
                    snackbar.setDuration(3000);
                    snackbar.show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("member_id", member_uid);
                params.put("vaccine_id", vacine_uid);
                params.put("type", arrayList.get(Integer.parseInt(pos_value)).get("type"));
                params.put("record_data", record_data);
                params.put("vaccinated_on", arrayList.get(Integer.parseInt(pos_value)).get("vaccinated_on"));
                params.put("image_location", arrayList.get(Integer.parseInt(pos_value)).get("image_location"));
                params.put("metadata", data);
                params.put("added_by", added_by);
                params.put("added_on", added_on);


                Log.d("000105", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000105", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }


    private void sendPostRequest_Overall(final String member_uid, final String vacine_uid, final String record_data, final String data, final String added_by, final String added_on) {

        final String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/child/vaccinations";

        Log.d("000105", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {
                    //   Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {
                        Log.d("000105", "Response:    " + response);

                        Lister ls = new Lister(ctx);
                       ls.createAndOpenDB();

                        String update_record = "UPDATE CVACCINATION SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + member_uid + "'AND added_on= '" + added_on + "'AND vaccine_id= '" + vacine_uid + "'";

                        ls.executeNonQuery(update_record);

                        Toast.makeText(ctx, "ڈیٹا سنک ہوگیا ہے.", Toast.LENGTH_SHORT).show();

                        arrayList.remove(0);
                        adt.notifyDataSetChanged();

                        reload();


                    } else {
                        alertDialog.dismiss();
                        Log.d("000105", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, "ڈیٹا سروس پر سینک نہیں ہوا.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000105", "Err: " + e.getMessage());
                    Toast.makeText(ctx, "ڈیٹا سینک نہیں ہوا", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000105", "onErrorResponse: " + error.getMessage());

                String string = new String(error.networkResponse.data);
                Log.d("000105", "onErrorResponse String: " + string);
                Log.d("000105", "onErrorResponse Status: " + error.networkResponse.statusCode);

                if (string.contains("Duplicate entry"))
                {
                    Log.d("000105", "Duplicate Entry YES !!!!!!!!: ");

                    Toast.makeText(ctx, "ڈیٹا سروس پر پہلے سے سینک ہے.", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            arrayList.remove(0);
                            adt.notifyDataSetChanged();

                            reload();

                        }
                    },2000);

                }
                else{
                    Log.d("000105", "Duplicate Entry NOT: ");
                    Toast.makeText(ctx, "ڈیٹا سینک نہیں ہوا.", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    temp = "0";
                    finish();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("member_id", member_uid);
                params.put("vaccine_id", vacine_uid);
                params.put("type", arrayList.get(0).get("type"));
                params.put("record_data", record_data);
                params.put("vaccinated_on", arrayList.get(0).get("vaccinated_on"));
                params.put("image_location", arrayList.get(0).get("image_location"));
                params.put("metadata", data);
                params.put("added_by", added_by);
                params.put("added_on", added_on);

                Log.d("000105", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000105", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }



    public void sendSMSMethod(final String member_uid, final String vacine_uid, final String record_data, final String data, final String added_by, final String added_on) {

        try {


            JSONObject jobj = new JSONObject();
            jobj.put("member_id", member_uid);
            jobj.put("vaccine_id", vacine_uid);
            jobj.put("type", arrayList.get(Integer.parseInt(pos_value)).get("type"));
            jobj.put("record_data", record_data);
            jobj.put("vaccinated_on", arrayList.get(Integer.parseInt(pos_value)).get("vaccinated_on"));
            jobj.put("image_location", arrayList.get(Integer.parseInt(pos_value)).get("image_location"));
            jobj.put("metadata", data);
            jobj.put("added_by", added_by);
            jobj.put("added_on", added_on);


            String toencode_sms = getBase64String(String.valueOf(jobj));

            Log.d("000951", "JSON DATA:  " + jobj.toString());


            String uuid = UUID.randomUUID().toString().replace("-", "");
            final String sms_data = "HAYATPK" + "|" + uuid + "|" + "CVACCINE" + "|" + toencode_sms;
            Log.d("000951", "SMS DATA:  " + sms_data);


            TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            int simState = telMgr.getSimState();
            switch (simState) {
                case TelephonyManager.SIM_STATE_ABSENT:
                    // do something
                    //Toast.makeText(getApplicationContext(), "Data is not synced, there is no sim in your phone.", Toast.LENGTH_LONG).show();

                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.sync_khandan_layout), "آپ کے فون میں کوئی سم موجود نہیں.", Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(16);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                    snackbar.setDuration(3000);
                    snackbar.show();


                    break;
                case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                    // do something
                    Toast.makeText(getApplicationContext(), "SIM_STATE_NETWORK_LOCKED", Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                    // do something
                    Toast.makeText(getApplicationContext(), "SIM_STATE_PIN_REQUIRED", Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                    // do something
                    Toast.makeText(getApplicationContext(), "SIM_STATE_PUK_REQUIRED", Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.USSD_RETURN_FAILURE:
                    Toast.makeText(getApplicationContext(), "USSD_RETURN_FAILURE", Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.USSD_ERROR_SERVICE_UNAVAIL:
                    Toast.makeText(getApplicationContext(), "USSD_ERROR_SERVICE_UNAVAIL", Toast.LENGTH_LONG).show();

                    break;

                case TelephonyManager.SIM_STATE_READY:
                    // do something

                    Log.d("000951", "3");
                    progressDialog = new Dialog(ctx);
                    LayoutInflater layout = LayoutInflater.from(ctx);
                    final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

                    progressDialog.setContentView(dialogView);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    progressDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                    ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
                    ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
                    Log.d("000951", "4");

                    PendingIntent piSent = PendingIntent.getBroadcast(SyncChildHifazitiTeekey_Activity.this, 0, new Intent("SMS_SENT"), 0);
                    PendingIntent piDelivered = PendingIntent.getBroadcast(SyncChildHifazitiTeekey_Activity.this, 0, new Intent("SMS_DELIVERED"), 0);
                    // sms.sendTextMessage(phone, null, message, piSent, piDelivered);
                    try {
                        SmsManager sms = SmsManager.getDefault();
                        ArrayList<String> mSMSMessage = sms.divideMessage(sms_data);

                        for (int in = 0; in < mSMSMessage.size(); in++) {
                            sentPendingIntents.add(in, piSent);
                            deliveredPendingIntents.add(in, piDelivered);
                        }

                        int numberofsmsDel = mSMSMessage.size();
                        sms.sendMultipartTextMessage(smsNumber, null, mSMSMessage, sentPendingIntents, deliveredPendingIntents);

                        Log.d("000951", "SMS SENT SUCCESSFULLY !!!!!!!!!!");
                        //  Toast.makeText(this, "SMS has been sent.", Toast.LENGTH_SHORT).show();

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();
                        String is_synced="0,3";
                        String update_record = "UPDATE CVACCINATION SET " +
                                "is_synced='" + String.valueOf(is_synced) + "' " +
                                "WHERE member_uid = '" + member_uid + "'AND added_on= '" + added_on + "'AND vaccine_id= '" + vacine_uid + "'";

                        ls.executeNonQuery(update_record);


                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d("000951", "Data Update:  " + update_record);
                        Log.d("000951", "Query Update:  " + res);

                        final Snackbar snackbar = Snackbar.make(findViewById(R.id.sync_khandan_layout), "ڈیٹا سنک ہوگیا ہے.", Snackbar.LENGTH_SHORT);
                        View mySbView = snackbar.getView();
                        mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                        TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(16);
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                        }
                        snackbar.setDuration(3000);
                        snackbar.show();

                        arrayList.remove(Integer.parseInt(pos_value));
                        if (arrayList.size() > 0) {
                            progressDialog.dismiss();
                            adt.notifyDataSetChanged();

                        } else {
                            adt.notifyDataSetChanged();
                            progressDialog.dismiss();
                            tv_record.setVisibility(View.VISIBLE);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    finish();
                                }
                            }, 1500);
                        }

                    }  catch (Exception e) {
                        progressDialog.dismiss();
                        Log.d("000951", "Exception Sending faild " + e);
                        e.printStackTrace();
                        final Snackbar snackbar = Snackbar.make(findViewById(R.id.sync_khandan_layout), "SMS sending permission failed.", Snackbar.LENGTH_SHORT);
                                View mySbView = snackbar.getView();
                                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                textView.setTextSize(16);
                                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close_black_24dp, 0);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                }
                                snackbar.setDuration(3000);
                                snackbar.show();
                    }
                        }
                    },1500);


                    break;

                case TelephonyManager.SIM_STATE_UNKNOWN:
                    // do something
                    Toast.makeText(getApplicationContext(), "SIM_STATE_UNKNOWN", Toast.LENGTH_SHORT).show();
                    break;
            }

            Log.d("000951", "5");


        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("000951", "Error:: " + e.getMessage());
        }
    }


    public void sendSMSMethod_Overall(final String member_uid, final String vacine_uid, final String record_data, final String data, final String added_by, final String added_on) {

        try {

            JSONObject jobj = new JSONObject();
            jobj.put("member_id", member_uid);
            jobj.put("vaccine_id", vacine_uid);
            jobj.put("type", arrayList.get(0).get("type"));
            jobj.put("record_data", record_data);
            jobj.put("vaccinated_on", arrayList.get(0).get("vaccinated_on"));
            jobj.put("image_location", arrayList.get(0).get("image_location"));
            jobj.put("metadata", data);
            jobj.put("added_by", added_by);
            jobj.put("added_on", added_on);

            String toencode_sms = getBase64String(String.valueOf(jobj));

            Log.d("000951", "JSON DATA:  " + jobj.toString());

            String uuid = UUID.randomUUID().toString().replace("-", "");
            final String sms_data = "HAYATPK" + "|" + uuid + "|" + "CVACCINE" + "|" + toencode_sms;
            Log.d("000951", "SMS DATA:  " + sms_data);


            TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            int simState = telMgr.getSimState();
            switch (simState) {
                case TelephonyManager.SIM_STATE_ABSENT:
                    // do something
                    //Toast.makeText(getApplicationContext(), "Data is not synced, there is no sim in your phone.", Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();

                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.sync_khandan_layout), "آپ کے فون میں کوئی سم موجود نہیں.", Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(16);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                    snackbar.setDuration(3000);
                    snackbar.show();
                    temp = "0";
                    break;

                case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                    // do something
                    Toast.makeText(getApplicationContext(), "SIM_STATE_NETWORK_LOCKED", Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                    temp = "0";
                    break;

                case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                    // do something
                    Toast.makeText(getApplicationContext(), "SIM_STATE_PIN_REQUIRED", Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                    temp = "0";
                    break;

                case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                    // do something
                    Toast.makeText(getApplicationContext(), "SIM_STATE_PUK_REQUIRED", Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                    temp = "0";
                    break;

                case TelephonyManager.USSD_RETURN_FAILURE:
                    Toast.makeText(getApplicationContext(), "USSD_RETURN_FAILURE", Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                    temp = "0";
                    break;

                case TelephonyManager.USSD_ERROR_SERVICE_UNAVAIL:
                    Toast.makeText(getApplicationContext(), "USSD_ERROR_SERVICE_UNAVAIL", Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                    temp = "0";
                    break;

                case TelephonyManager.SIM_STATE_READY:
                    // do something

                    Log.d("000951", "3");
                    /*alertDialog = new Dialog(ctx);
                    LayoutInflater layout = LayoutInflater.from(ctx);
                    final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);
                    alertDialog.setContentView(dialogView);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    alertDialog.show();*/

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                    ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
                    ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
                    Log.d("000951", "4");

                    PendingIntent piSent = PendingIntent.getBroadcast(SyncChildHifazitiTeekey_Activity.this, 0, new Intent("SMS_SENT"), 0);
                    PendingIntent piDelivered = PendingIntent.getBroadcast(SyncChildHifazitiTeekey_Activity.this, 0, new Intent("SMS_DELIVERED"), 0);
                    // sms.sendTextMessage(phone, null, message, piSent, piDelivered);
                    try {
                        SmsManager sms = SmsManager.getDefault();
                        ArrayList<String> mSMSMessage = sms.divideMessage(sms_data);

                        for (int in = 0; in < mSMSMessage.size(); in++) {
                            sentPendingIntents.add(in, piSent);
                            deliveredPendingIntents.add(in, piDelivered);
                        }

                        int numberofsmsDel = mSMSMessage.size();
                        sms.sendMultipartTextMessage(smsNumber, null, mSMSMessage, sentPendingIntents, deliveredPendingIntents);

                        Log.d("000951", "SMS SENT SUCCESSFULLY !!!!!!!!!!");
                        //  Toast.makeText(this, "SMS has been sent.", Toast.LENGTH_SHORT).show();


                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();
                        String is_synced="0,3";
                        String update_record = "UPDATE CVACCINATION SET " +
                                "is_synced='" + String.valueOf(is_synced) + "' " +
                                "WHERE member_uid = '" + member_uid + "'AND added_on= '" + added_on + "'AND vaccine_id= '" + vacine_uid + "'";

                        ls.executeNonQuery(update_record);

                        Toast.makeText(ctx, "ڈیٹا سنک ہوگیا ہے.", Toast.LENGTH_SHORT).show();

                        arrayList.remove(0);
                        adt.notifyDataSetChanged();

                        //reload();

                        if (arrayList.size() > 0) {
                            temp = "1";
                            iv_sync.performClick();

                        } else {
                            alertDialog.dismiss();
                            tv_record.setVisibility(View.VISIBLE);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    finish();
                                }
                            }, 1500);
                        }

                    } catch (Exception e) {
                        alertDialog.dismiss();
                        temp = "0";
                        Log.d("000951", "Exception Sending faild " + e);
                        e.printStackTrace();
                        Toast.makeText(ctx, "SMS sending failed...", Toast.LENGTH_SHORT).show();
                    }
                        }
                    }, 1500);


                    break;

                case TelephonyManager.SIM_STATE_UNKNOWN:
                    // do something
                    Toast.makeText(getApplicationContext(), "SIM_STATE_UNKNOWN", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    temp = "0";
                    break;
            }

            Log.d("000951", "5");


        } catch (Exception e) {
            alertDialog.dismiss();
            temp = "0";
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("000951", "Error:: " + e.getMessage());
        }
    }

    public static String getBase64String(String jsonString) {

        String base64 = "";
        try {
            byte[] data = (jsonString + "").getBytes("UTF-8");
            base64 = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e("000951", "Exception: " + e.getMessage());
        }
      //  Log.d("000951", "getBase64String " + base64);

        return base64;

    }



    private void read_data() {
        arrayList.clear();

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            mData = ls.executeReader("Select *from CVACCINATION where is_synced IN ('0','0,3')");
            Log.d("000105", "Data: " + mData.length);

            HashMap<String, String> map;
            for (int i = 0; i < mData.length; i++) {

                map = new HashMap<>();

               /* Log.d("000105", "ID: " + mData[i][0]);*/
                Log.d("000105", "member_uid: " + mData[i][1]);
                Log.d("000105", "vaccine_id: " + mData[i][2]);
                Log.d("000105", "record_data: " + mData[i][3]);
                Log.d("000105", "type: " + mData[i][4]);
                //Log.d("000105", "due_date: " + mData[i][5]);
               // Log.d("000105", "vaccinated_on: " + mData[i][6]);
                //Log.d("000105", "image_location: " + mData[i][7]);
               // Log.d("000105", "metadata: " + mData[i][8]);
                /*Log.d("000105", "added_by: " + mData[i][9]);
                Log.d("000105", "is_synced: " + mData[i][10]);
                Log.d("000105", "added_on: " + mData[i][11]);*/

                map.put("Id", "" + mData[i][0]);
                map.put("member_uid", "" + mData[i][1]);
                map.put("vaccine_id", "" + mData[i][2]);
                map.put("record_data", "" + mData[i][3]);
                map.put("type", "" + mData[i][4]);
                map.put("due_date", "" + mData[i][5]);
                map.put("vaccinated_on", "" + mData[i][6]);
                map.put("image_location", "" + mData[i][7]);
                map.put("metadata", "" + mData[i][8]);
                map.put("added_by", "" + mData[i][9]);
                map.put("is_synced", "" + mData[i][10]);
                map.put("added_on", "" + mData[i][11]);

                String mData_name[][] = ls.executeReader("Select full_name, 'Membr' as Table_Type from MEMBER where uid = '" + mData[i][1] + "'" +
                        " UNION ALL" +
                        " Select full_name , 'D_Mem' FROM DELETE_MEMBER where uid= '" + mData[i][1] + "'");

                if (mData_name != null) {
                    Log.d("000105", "IF ***********: ");
                    Log.d("000105", "Child Name: " + mData_name[0][0]);
                    Log.d("000105", "Table Type: " + mData_name[0][1]);
                    map.put("name", "" + mData_name[0][0]);

                } else {
                    Log.d("000105", "ELSEEEEEE +++++++++++++: ");
                    map.put("name", "" + "Unknown Name");
                }


                if (mData[i][11] == null) {
                    map.put("datetime", "" + "null");
                } else {
                    Date date = new Date(Long.parseLong(mData[i][11]));
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    String formatted = format.format(date);
                    Log.d("000105", "datetime: " + formatted);

                    map.put("datetime", "" + formatted);
                }


                arrayList.add(map);
            }

           /* adt = new Adt_SyncItems(ctx, arrayList);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);*/


        } catch (Exception e) {
            Log.d("000105", "Error: " + e.getMessage());
            iv_sync.setVisibility(View.GONE);
            //  tv_record.setVisibility(View.VISIBLE);
        }
    }

    public void reload() {

        if (arrayList.size() > 0) {
            temp = "1";
            iv_sync.performClick();
        } else {
            temp = "0";
            alertDialog.dismiss();
            tv_record.setVisibility(View.VISIBLE);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    finish();
                }
            },1500);
        }

    }

    class Task extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            Log.d("000105", "ON PREEEEE: ");
            ll_pbProgress.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Log.d("000105", "ON BACkGROUND: ");
                read_data();
                Thread.sleep(1500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("000105", "ON EXECUTE: ");
            ll_pbProgress.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            adt.notifyDataSetChanged();
            super.onPostExecute(result);
        }

    }

 /*   @Override
    protected void onResume() {
        super.onResume();


        arrayList.clear();

        try {
            Lister ls = new Lister(ctx);
           ls.createAndOpenDB();

            mData = ls.executeReader("Select *from CVACCINATION where is_synced = '0'");
            Log.d("000105", "Data: " + mData.length);

            HashMap<String, String> map;
            for (int i = 0; i < mData.length; i++) {

                map = new HashMap<>();

                Log.d("000105", "ID: " + mData[i][0]);
                Log.d("000105", "member_uid: " + mData[i][1]);
                Log.d("000105", "vaccine_id: " + mData[i][2]);
                Log.d("000105", "record_data: " + mData[i][3]);
                Log.d("000105", "type: " + mData[i][4]);
                Log.d("000105", "due_date: " + mData[i][5]);
                Log.d("000105", "vaccinated_on: " + mData[i][6]);
                Log.d("000105", "image_location: " + mData[i][7]);
                Log.d("000105", "metadata: " + mData[i][8]);
                Log.d("000105", "added_by: " + mData[i][9]);
                Log.d("000105", "is_synced: " + mData[i][10]);
                Log.d("000105", "added_on: " + mData[i][11]);

                map.put("Id", "" + mData[i][0]);
                map.put("member_uid", "" + mData[i][1]);
                map.put("vaccine_id", "" + mData[i][2]);
                map.put("record_data", "" + mData[i][3]);
                map.put("type", "" + mData[i][4]);
                map.put("due_date", "" + mData[i][5]);
                map.put("vaccinated_on", "" + mData[i][6]);
                map.put("image_location", "" + mData[i][7]);
                map.put("metadata", "" + mData[i][8]);
                map.put("added_by", "" + mData[i][9]);
                map.put("is_synced", "" + mData[i][10]);
                map.put("added_on", "" + mData[i][11]);

                String mData_name[][] = ls.executeReader("Select full_name from MEMBER where uid = '" + mData[i][1] + "'");
                for (int j = 0; j < mData_name.length; j++) {

                    Log.d("000105", "Child Name: " + mData_name[j][0]);
                    map.put("name", "" + mData_name[j][0]);
                }

                if (mData[i][11] == null) {
                    map.put("datetime", "" + "null");
                } else {
                    Date date = new Date(Long.parseLong(mData[i][11]));
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    String formatted = format.format(date);
                    Log.d("000105", "datetime: " + formatted);

                    map.put("datetime", "" + formatted);
                }


                arrayList.add(map);
            }

            adt = new Adt_SyncItems(ctx, arrayList);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);


        } catch (Exception e) {
            Log.d("000105", "Error: " + e.getMessage());
            iv_sync.setVisibility(View.GONE);
            tv_record.setVisibility(View.VISIBLE);
        }
    }*/

    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

}
