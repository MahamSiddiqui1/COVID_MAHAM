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
import com.akdndhrc.covid_module.CustomClass.KhandanClass;
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

public class SyncKhandanReg_Activity extends AppCompatActivity {

    Context ctx = SyncKhandanReg_Activity.this;
    ListView lv;
    ImageView iv_navigation_drawer, iv_sync;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
    Adt_SyncItems adt;
    Dialog alertDialog, progressDialog;
    private Handler mHandler = new Handler();
    TextView tv_record;
    String mData[][];
    int pos_value;
    String temp = "0";
    int valueforloop = 0;
    LinearLayout ll_pbProgress;
    //ProgressDialog progressDialog;
    String smsNumber;
    ArrayList<KhandanClass> arrayListKhandan = new ArrayList<>();
    KhandanClass kc;
    long mLastClickTime = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_khandan_reg_);


        temp = "0";
        //ListView
        lv = findViewById(R.id.lv);

        //TextView
        tv_record = findViewById(R.id.tv_record);

        TextView tv = findViewById(R.id.tv);
        tv.setText("سینک خاندان رجسٹریشن");

        //ImageView
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
            Log.d("000100", "SMS Number: " + shared_sms_number);

        } catch (Exception e) {
            Log.d("000100", "Shared Err:" + e.getMessage());
        }



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

                    Log.d("000100", "IFFFF");
                } else {
                    Log.d("000100", "ELssssss");
                }

                if (isConnectingToInternet()) {

                    if (temp.equalsIgnoreCase("0")) {
                        Toast.makeText(ctx, "Data sending through Internet.", Toast.LENGTH_SHORT).show();
                        Log.d("000100", "IFFFF 2");
                    } else {
                        Log.d("000100", "ELssssss 2");
                    }


                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                if (arrayList.size() > 0) {

                                    kc = arrayListKhandan.get(0);
                                    sendPostRequest_overall(arrayList.get(0).get("manual_id"), arrayList.get(0).get("uid"), arrayList.get(0).get("province_id"), arrayList.get(0).get("district_id"),
                                            arrayList.get(0).get("subdistrict_id"), arrayList.get(0).get("uc_id"), arrayList.get(0).get("village_id"), arrayList.get(0).get("name"), arrayList.get(0).get("family_address"),
                                            arrayList.get(0).get("water_source"), arrayList.get(0).get("toilet_facility"), arrayList.get(0).get("added_by"), arrayList.get(0).get("added_on"));

                                } else {
                                    alertDialog.dismiss();
                                    onBackPressed();
                                    //  Toast.makeText(ctx, "No data found", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                alertDialog.dismiss();
                                Log.d("000100", "Error:" + e.getMessage());
                            }
                        }
                    }, 1000);


                } else {
                    if (temp.equalsIgnoreCase("0")) {
                        Toast.makeText(ctx, "Data sending through SMS.", Toast.LENGTH_SHORT).show();

                        Log.d("000100", "IFFFF OFFLINE");
                    } else {
                        Log.d("000100", "ELssssss OFFLINE");
                    }
                    /*if (temp.equalsIgnoreCase("1")) {
                        alertDialog.dismiss();
                        Log.d("000100", "IFFFF 1111");
                    } else {
                        Log.d("000100", "ELSEE 1111");
                    }*/

                    //Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں۔", Toast.LENGTH_SHORT).show();

                    kc = arrayListKhandan.get(0);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendSMSMethod_overall(arrayList.get(0).get("manual_id"), arrayList.get(0).get("uid"), arrayList.get(0).get("province_id"), arrayList.get(0).get("district_id"),
                                    arrayList.get(0).get("subdistrict_id"), arrayList.get(0).get("uc_id"), arrayList.get(0).get("village_id"), arrayList.get(0).get("name"), arrayList.get(0).get("family_address"),
                                    arrayList.get(0).get("water_source"), arrayList.get(0).get("toilet_facility"), arrayList.get(0).get("added_by"), arrayList.get(0).get("added_on"));
                        }
                    }, 1500);


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
                Log.d("000100", " mLastClickTime: " + mLastClickTime);

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

//                                Log.d("000100", "Name: " + arrayListKhandan.get(position).family_head_name);
                                pos_value = position;
                                kc = arrayListKhandan.get(position);
                               /* sendPostRequest(mData[position][1], mData[position][2], mData[position][3], mData[position][4], mData[position][5], mData[position][6], mData[position][7], mData[position][8], mData[position][9],
                                        mData[position][10], mData[position][11], mData[position][12], mData[position][14]);*/

                                sendPostRequest(arrayListKhandan.get(position).manual_id, arrayListKhandan.get(position).uid, arrayListKhandan.get(position).province_id,
                                        arrayListKhandan.get(position).district_id, arrayListKhandan.get(position).subdistrict_id, arrayListKhandan.get(position).uc_id, arrayListKhandan.get(position).village_id,
                                        arrayListKhandan.get(position).family_head_name, arrayListKhandan.get(position).family_address, arrayListKhandan.get(position).water_source,
                                        arrayListKhandan.get(position).toilet_facility, arrayListKhandan.get(position).added_by, arrayListKhandan.get(position).added_on);


                            }
                        }, 1000);

                    } catch (Exception e) {
                        Log.d("000100", "Catach Err: " + e.getMessage());
                    }

                } else {

                    Toast.makeText(ctx, "Data sending through SMS.", Toast.LENGTH_SHORT).show();


                    pos_value = position;
                    Log.d("000100", "POSITION: " + pos_value);

                    kc = arrayListKhandan.get(position);
                  /*  sendSMSMethod(mData[position][1], mData[position][2], mData[position][3], mData[position][4], mData[position][5], mData[position][6], mData[position][7], mData[position][8], mData[position][9],
                            mData[position][10], mData[position][11], mData[position][12], mData[position][14]);*/

                   /* sendSMSMethod(arrayListKhandan.get(position).manual_id, arrayListKhandan.get(position).uid, arrayListKhandan.get(position).province_id,
                            arrayListKhandan.get(position).district_id, arrayListKhandan.get(position).subdistrict_id, arrayListKhandan.get(position).uc_id, arrayListKhandan.get(position).village_id,
                            arrayListKhandan.get(position).family_head_name, arrayListKhandan.get(position).family_address, arrayListKhandan.get(position).water_source,
                            arrayListKhandan.get(position).toilet_facility,arrayListKhandan.get(position).added_by, arrayListKhandan.get(position).added_on);*/


                    sendSMSMethod(arrayList.get(position).get("manual_id"), arrayList.get(position).get("uid"), arrayList.get(position).get("province_id"), arrayList.get(position).get("district_id"),
                            arrayList.get(position).get("subdistrict_id"), arrayList.get(position).get("uc_id"), arrayList.get(position).get("village_id"), arrayList.get(position).get("name"), arrayList.get(position).get("family_address"),
                            arrayList.get(position).get("water_source"), arrayList.get(position).get("toilet_facility"), arrayList.get(position).get("added_by"), arrayList.get(position).get("added_on"));


                   /* kc = arrayListKhandan.get(position);
                    Log.d("000100", "Name: " + kc.family_head_name);
                    arrayListKhandan.remove(position);
                    arrayList.remove(position);
                    adt.notifyDataSetChanged();*/
                }
            }

        });
    }



    @Override
    protected void onResume() {
        super.onResume();

        Log.d("000100", "onResume !!!!!!!!!!!! ");

        try{
            adt = new Adt_SyncItems(ctx, arrayList);
            lv.setAdapter(adt);
            new Task().execute();
        }catch (Exception e)
        {
            Log.d("000100", "onResume Error: " +e.getMessage());

        }
    }


    private void sendPostRequest(final String manual_id, final String uid, final String province_id, final String district_id,
                                 final String subdistrict_id, final String uc_id, final String village_id, final String family_head_name,
                                 final String family_address, final String water_source, final String toilet_facility,
                                 final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/family/";


        Log.d("000100", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    // Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000100", "Response:    " + response);
                        
                           /* String is_synced="0";
                        if (arrayList.get(pos_value).get("is_synced").equalsIgnoreCase("0"))
                        {
                            Log.d("000100", "IF isSynced 0 ");
                            is_synced="1,0";
                        }
                        else   if (arrayList.get(pos_value).get("is_synced").equalsIgnoreCase("0,3"))
                        {
                            Log.d("000100", "ELSE IF isSynced 0,3 ");
                            is_synced="1,3";
                        }
                        else {
                            Log.d("000100", "ELSE isSynced");
                            is_synced="0";
                        }*/

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE KHANDAN SET " +
                                "is_synced='" + (1) + "' " +
                                "WHERE uid = '" + uid + "'";
                        ls.executeNonQuery(update_record);

                        // Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();
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

                        arrayList.remove(pos_value);
                        arrayListKhandan.remove(pos_value);
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
                            }, 1500);
                        }


                    } else {
                        alertDialog.dismiss();
                        Log.d("000100", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    alertDialog.dismiss();
                    Log.d("000100", "Err: " + e.getMessage());
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000100", "onErrorResponse: " + error.getMessage());
                // Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
               //Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();

                String string = new String(error.networkResponse.data);
                Log.d("000100", "onErrorResponse String: " + string);
                Log.d("000100", "onErrorResponse Status: " + error.networkResponse.statusCode);

                if (string.contains("Duplicate entry"))
                {
                    Log.d("000100", "Duplicate Entry YES !!!!!!!!: ");
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

                            arrayList.remove(pos_value);
                            arrayListKhandan.remove(pos_value);
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
                                }, 1500);
                            }
                        }
                    },2500);

                }
                else{
                    alertDialog.dismiss();
                    Log.d("000100", "Duplicate Entry NOT: ");
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
                params.put("manual_id", manual_id);
                params.put("uid", uid);
                params.put("province_id", province_id);
                params.put("country_id", "0092");
                params.put("district_id", district_id);
                params.put("tehsil_id", subdistrict_id);
                params.put("uc_id", uc_id);
                params.put("village_id", village_id);
                params.put("family_head_name", family_head_name);
                params.put("family_address", family_address);
                params.put("water_source", water_source);
                params.put("toilet_facility", toilet_facility);
                params.put("added_by", added_by);
                params.put("added_on", added_on);

                Log.d("000100", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000100", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }


    private void sendPostRequest_overall(final String manual_id, final String uid, final String province_id, final String district_id,
                                         final String subdistrict_id, final String uc_id, final String village_id, final String family_head_name,
                                         final String family_address, final String water_source, final String toilet_facility,
                                         final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/family/";

        Log.d("000100", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    // Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {


                        Log.d("000100", "Response:    " + response);

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE KHANDAN SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE uid = '" + uid + "'";
                        ls.executeNonQuery(update_record);

                        Toast.makeText(ctx, "ڈیٹا سنک ہوگیا ہے.", Toast.LENGTH_SHORT).show();

                        kc = arrayListKhandan.get(0);
                        arrayListKhandan.remove(0);
                        arrayList.remove(0);

                        adt.notifyDataSetChanged();

                        reload();


                    } else {
                        alertDialog.dismiss();
                        Log.d("000100", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    alertDialog.dismiss();
                    Log.d("000100", "Err: " + e.getMessage());
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000100", "onErrorResponse: " + error.getMessage());
                //Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();


                String string = new String(error.networkResponse.data);
                Log.d("000100", "onErrorResponse String: " + string);
                Log.d("000100", "onErrorResponse Status: " + error.networkResponse.statusCode);

                if (string.contains("Duplicate entry"))
                {
                    Log.d("000100", "Duplicate Entry YES !!!!!!!!: ");

                    Toast.makeText(ctx, "ڈیٹا سروس پر پہلے سے سینک ہے.", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            kc = arrayListKhandan.get(0);
                            arrayListKhandan.remove(0);
                            arrayList.remove(0);

                            adt.notifyDataSetChanged();

                            reload();

                        }
                    },2000);

                }
                else{
                    Log.d("000100", "Duplicate Entry NOT: ");
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
                params.put("manual_id", manual_id);
                params.put("uid", uid);
                params.put("province_id", province_id);
                params.put("country_id", "0092");
                params.put("district_id", district_id);
                params.put("tehsil_id", subdistrict_id);
                params.put("uc_id", uc_id);
                params.put("village_id", village_id);
                params.put("family_head_name", family_head_name);
                params.put("family_address", family_address);
                params.put("water_source", water_source);
                params.put("toilet_facility", toilet_facility);
                params.put("added_by", added_by);
                params.put("added_on", added_on);

                Log.d("000100", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000100", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }


    public void sendSMSMethod(final String manual_id, final String uid, final String province_id, final String district_id,
                              final String subdistrict_id, final String uc_id, final String village_id, final String family_head_name,
                              final String family_address, final String water_source, final String toilet_facility,
                              final String added_by, final String added_on) {

        try {


            JSONObject jobj = new JSONObject();
            jobj.put("manual_id", manual_id);
            jobj.put("uid", uid);
            jobj.put("province_id", province_id);
            jobj.put("district_id", district_id);
            jobj.put("subdistrict_id", subdistrict_id);
            jobj.put("uc_id", uc_id);
            jobj.put("village_id", village_id);
            jobj.put("family_head_name", family_head_name);
            jobj.put("family_address", family_address);
            jobj.put("water_source", water_source);
            jobj.put("toilet_facility", toilet_facility);
            jobj.put("added_by", added_by);
            jobj.put("added_on", added_on);

            Log.d("000100", "JSON DATA:  " + jobj.toString());

            String toencode_sms = getBase64String(String.valueOf(jobj));

            String uuid = UUID.randomUUID().toString().replace("-", "");
            final String sms_data = "HayatPK" + "|" + uuid + "|" + "KHANDAN" + "|" + toencode_sms;
            Log.d("000100", "SMS DATA:  " + sms_data);


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

                    Log.d("000100", "3");
                    /*progressDialog = new ProgressDialog(SyncKhandanReg_Activity.this,
                            android.R.style.Theme_DeviceDefault_Light_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("برائے مہربانی انتظار کریں ..");
                    progressDialog.show();*/

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
                            Log.d("000100", "4");

                            PendingIntent piSent = PendingIntent.getBroadcast(SyncKhandanReg_Activity.this, 0, new Intent("SMS_SENT"), 0);
                            PendingIntent piDelivered = PendingIntent.getBroadcast(SyncKhandanReg_Activity.this, 0, new Intent("SMS_DELIVERED"), 0);
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

                                Log.d("000100", "SMS SENT SUCCESSFULLY !!!!!!!!!!");
                                //  Toast.makeText(this, "SMS has been sent.", Toast.LENGTH_SHORT).show();

                                Lister ls = new Lister(ctx);
                                ls.createAndOpenDB();

                                String is_synced="0,3";
                                String update_record = "UPDATE KHANDAN SET " +
                                        "is_synced='" + String.valueOf(is_synced) + "' " +
                                        "WHERE uid = '" + uid + "'";
                                Boolean res = ls.executeNonQuery(update_record);
                                Log.d("000100", "Data Update:  " + update_record);
                                Log.d("000100", "Query Update:  " + res);

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

                                Log.d("000100", "POS REMOVED:  " + pos_value);

                                arrayList.remove(pos_value);
                                arrayListKhandan.remove(pos_value);
                                arrayList.remove(true);

                                if (arrayList.size() > 0) {
                                    progressDialog.dismiss();
                                    adt.notifyDataSetChanged();

                                  /*  finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(getIntent());
                                    overridePendingTransition(0, 0);*/

                                    Log.d("000100", "IF: ");

                                } else {
                                    adt.notifyDataSetChanged();
                                    progressDialog.dismiss();
                                    tv_record.setVisibility(View.VISIBLE);

                                    Log.d("000100", "ELSE: ");

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            finish();
                                        }
                                    }, 1000);
                                }

                            } catch (Exception e) {
                                progressDialog.dismiss();
                                Log.d("000100", "Exception Sending faild " + e);
                                e.printStackTrace();
                               // Toast.makeText(SyncKhandanReg_Activity.this, "SMS sending failed...", Toast.LENGTH_SHORT).show();
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
                    }, 1500);


                    break;

                case TelephonyManager.SIM_STATE_UNKNOWN:
                    // do something
                    Toast.makeText(getApplicationContext(), "SIM_STATE_UNKNOWN", Toast.LENGTH_SHORT).show();
                    break;
            }

            Log.d("000100", "5");

        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("000100", "Error:: " + e.getMessage());
        }
    }


    public void sendSMSMethod_overall(final String manual_id, final String uid, final String province_id, final String district_id,
                                      final String subdistrict_id, final String uc_id, final String village_id, final String family_head_name,
                                      final String family_address, final String water_source, final String toilet_facility,
                                      final String added_by, final String added_on) {

        try {


            JSONObject jobj = new JSONObject();
            jobj.put("manual_id", manual_id);
            jobj.put("uid", uid);
            jobj.put("province_id", province_id);
            jobj.put("district_id", district_id);
            jobj.put("subdistrict_id", subdistrict_id);
            jobj.put("uc_id", uc_id);
            jobj.put("village_id", village_id);
            jobj.put("family_head_name", family_head_name);
            jobj.put("family_address", family_address);
            jobj.put("water_source", water_source);
            jobj.put("toilet_facility", toilet_facility);
            jobj.put("added_by", added_by);
            jobj.put("added_on", added_on);

            Log.d("000100", "JSON DATA:  " + jobj.toString());

            String toencode_sms = getBase64String(String.valueOf(jobj));

            String uuid = UUID.randomUUID().toString().replace("-", "");
            final String sms_data = "HayatPK" + "|" + uuid + "|" + "KHANDAN" + "|" + toencode_sms;
            Log.d("000100", "SMS DATA:  " + sms_data);


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

                    Log.d("000100", "3");
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
                            Log.d("000100", "4");

                            PendingIntent piSent = PendingIntent.getBroadcast(SyncKhandanReg_Activity.this, 0, new Intent("SMS_SENT"), 0);
                            PendingIntent piDelivered = PendingIntent.getBroadcast(SyncKhandanReg_Activity.this, 0, new Intent("SMS_DELIVERED"), 0);
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

                                Log.d("000100", "SMS SENT SUCCESSFULLY !!!!!!!!!!");
                                //  Toast.makeText(this, "SMS has been sent.", Toast.LENGTH_SHORT).show();


                                Lister ls = new Lister(ctx);
                                ls.createAndOpenDB();

                                String is_synced="0,3";
                                String update_record = "UPDATE KHANDAN SET " +
                                        "is_synced='" + String.valueOf(is_synced) + "' " +
                                        "WHERE uid = '" + uid + "'";
                                ls.executeNonQuery(update_record);

                                Toast.makeText(ctx, "ڈیٹا سنک ہوگیا ہے.", Toast.LENGTH_SHORT).show();

                                arrayListKhandan.remove(0);
                                arrayList.remove(0);
                                adt.notifyDataSetChanged();

                                //reload();

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
                                    }, 1500);
                                }


                            } catch (Exception e) {
                                alertDialog.dismiss();
                                temp = "0";
                                Log.d("000100", "Exception Sending faild " + e);
                                e.printStackTrace();
                                Toast.makeText(SyncKhandanReg_Activity.this, "SMS sending failed...", Toast.LENGTH_SHORT).show();
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

            Log.d("000100", "5");


        } catch (Exception e) {
            alertDialog.dismiss();
            temp = "0";
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("000100", "Error:: " + e.getMessage());
        }
    }



    public static String getBase64String(String jsonString) {

        String base64 = "";
        try {
            byte[] data = (jsonString + "").getBytes("UTF-8");
            base64 = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e("000100", "Exception: " + e.getMessage());
        }
        // Log.d("000100", "getBase64String " + base64);

        return base64;

    }

    public void read_data() {

        arrayList.clear();

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

           // mData = ls.executeReader("Select *from KHANDAN where is_synced IN ('0','0,3','1,0')");
            mData = ls.executeReader("Select *from KHANDAN where is_synced IN ('0','0,3')");
            Log.d("000100", "Data: " + mData.length);

            HashMap<String, String> map;
            for (int i = 0; i < mData.length; i++) {

                valueforloop = mData.length;

                map = new HashMap<>();

               /* Log.d("000100", "ID: " + mData[i][0]);
                Log.d("000100", "manual_id: " + mData[i][1]);
                Log.d("000100", "uid: " + mData[i][2]);
                Log.d("000100", "province_id: " + mData[i][3]);
                Log.d("000100", "district_id: " + mData[i][4]);
                Log.d("000100", "subdistrict_id: " + mData[i][5]);
                Log.d("000100", "uc_id: " + mData[i][6]);
                Log.d("000100", "village_id: " + mData[i][7]);
                Log.d("000100", "family_head_name: " + mData[i][8]);
                Log.d("000100", "family_address: " + mData[i][9]);
                Log.d("000100", "water_source: " + mData[i][10]);
                Log.d("000100", "toilet_facility: " + mData[i][11]);
                Log.d("000100", "added_by: " + mData[i][12]);
                Log.d("000100", "is_synced: " + mData[i][13]);
                Log.d("000100", "added_on: " + mData[i][14]);*/

                map.put("Id", "" + mData[i][0]);
                map.put("manual_id", "" + mData[i][1]);
                map.put("uid", "" + mData[i][2]);
                map.put("province_id", "" + mData[i][3]);
                map.put("district_id", "" + mData[i][4]);
                map.put("subdistrict_id", "" + mData[i][5]);
                map.put("uc_id", "" + mData[i][6]);
                map.put("village_id", "" + mData[i][7]);
                map.put("name", "" + mData[i][8]);
                map.put("family_address", "" + mData[i][9]);
                map.put("water_source", "" + mData[i][10]);
                map.put("toilet_facility", "" + mData[i][11]);
                map.put("added_by", "" + mData[i][12]);
                map.put("is_synced", "" + mData[i][13]);
                map.put("added_on", "" + mData[i][14]);

                KhandanClass kc1;
                kc1 = new KhandanClass(Integer.parseInt(mData[i][0]), mData[i][1], mData[i][2], mData[i][3], mData[i][4], mData[i][5], mData[i][6], mData[i][7], mData[i][8], mData[i][9], mData[i][10], mData[i][11], mData[i][12], mData[i][13], mData[i][14]);
                arrayListKhandan.add(kc1);


                if (mData[i][14] == null) {
                    map.put("datetime", "" + "null");
                } else {
                    Date date = new Date(Long.parseLong(mData[i][14]));
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    String formatted = format.format(date);
                    Log.d("000100", "datetime: " + formatted);

                    map.put("datetime", "" + formatted);
                }
                arrayList.add(map);
            }

        } catch (Exception e) {
            Log.d("000100", "Error: " + e.getMessage());
            iv_sync.setVisibility(View.GONE);
            // tv_record.setVisibility(View.VISIBLE);
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
            }, 1000);
        }

    }


    class Task extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            Log.d("000100", "ON PREEEEE: ");
            ll_pbProgress.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Log.d("000100", "ON BACkGROUND: ");
                read_data();
                Thread.sleep(1500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("000100", "ON EXECUTE: ");
            ll_pbProgress.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            adt.notifyDataSetChanged();
            super.onPostExecute(result);
        }

    }


  /*  @Override
    protected void onResume() {
        super.onResume();

        arrayList.clear();
        arrayListKhandan.clear();

        try {
            Lister ls = new Lister(ctx);
           ls.createAndOpenDB();

            mData = ls.executeReader("Select *from KHANDAN where is_synced = '0'");
            Log.d("000100", "Data: " + mData.length);

            HashMap<String, String> map;
            for (int i = 0; i < mData.length; i++) {

                valueforloop = mData.length;

                map = new HashMap<>();
                kc = new KhandanClass(Integer.parseInt(mData[i][0]), mData[i][1], mData[i][2], mData[i][3], mData[i][4], mData[i][5], mData[i][6], mData[i][7], mData[i][8],
                        mData[i][9], mData[i][10], mData[i][11], mData[i][12], mData[i][13], mData[i][14]);

                arrayListKhandan.add(kc);

                Log.d("000100", "ID: " + mData[i][0]);
                Log.d("000100", "manual_id: " + mData[i][1]);
                Log.d("000100", "uid: " + mData[i][2]);
                Log.d("000100", "province_id: " + mData[i][3]);
                Log.d("000100", "district_id: " + mData[i][4]);
                Log.d("000100", "subdistrict_id: " + mData[i][5]);
                Log.d("000100", "uc_id: " + mData[i][6]);
                Log.d("000100", "village_id: " + mData[i][7]);
                Log.d("000100", "family_head_name: " + mData[i][8]);
                Log.d("000100", "family_address: " + mData[i][9]);
                Log.d("000100", "water_source: " + mData[i][10]);
                Log.d("000100", "toilet_facility: " + mData[i][11]);
                Log.d("000100", "added_by: " + mData[i][12]);
                Log.d("000100", "is_synced: " + mData[i][13]);
                Log.d("000100", "added_on: " + mData[i][14]);

                map.put("Id", "" + mData[i][0]);
                map.put("manual_id", "" + mData[i][1]);
                map.put("uid", "" + mData[i][2]);
                map.put("province_id", "" + mData[i][3]);
                map.put("district_id", "" + mData[i][4]);
                map.put("subdistrict_id", "" + mData[i][5]);
                map.put("uc_id", "" + mData[i][6]);
                map.put("village_id", "" + mData[i][7]);
                map.put("name", "" + mData[i][8]);
                map.put("family_address", "" + mData[i][9]);
                map.put("water_source", "" + mData[i][10]);
                map.put("toilet_facility", "" + mData[i][11]);
                map.put("added_by", "" + mData[i][12]);
                map.put("is_synced", "" + mData[i][13]);
                map.put("added_on", "" + mData[i][14]);

                if (mData[i][14] == null) {
                    map.put("datetime", "" + "null");
                } else {
                    Date date = new Date(Long.parseLong(mData[i][14]));
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    String formatted = format.format(date);
                    Log.d("000100", "datetime: " + formatted);

                    map.put("datetime", "" + formatted);
                }
                arrayList.add(map);
            }

            adt = new Adt_SyncItems(ctx, arrayList);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);


        } catch (Exception e) {
            Log.d("000100", "Error: " + e.getMessage());
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
