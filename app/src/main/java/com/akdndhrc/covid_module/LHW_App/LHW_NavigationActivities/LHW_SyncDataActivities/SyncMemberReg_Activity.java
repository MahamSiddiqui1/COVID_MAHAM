package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SyncDataActivities;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.akdndhrc.covid_module.CustomClass.UrlClass;
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

import static com.akdndhrc.covid_module.R.string.SMSnotDelivered;
import static com.akdndhrc.covid_module.R.string.SMSsent;
import static com.akdndhrc.covid_module.R.string.radioOff;

public class SyncMemberReg_Activity extends AppCompatActivity {

    Context ctx = SyncMemberReg_Activity.this;
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
    Dialog progressDialog;
    String smsNumber;
    long mLastClickTime = 0;

    BroadcastReceiver smsSentReceiverDel, smsDeliveredReceiverDel;

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
        tv.setText(R.string.syncReg);

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_sync = findViewById(R.id.iv_sync);
        iv_navigation_drawer.setVisibility(View.GONE);

        ll_pbProgress = findViewById(R.id.ll_pbProgress);
        ll_pbProgress.setVisibility(View.VISIBLE);


        //Get shared SMS Number
        try {
            SharedPreferences prefelse = ctx.getApplicationContext().getSharedPreferences((getString(R.string.duplicateEntry)), 0); // 0 - for private mode
            String shared_sms_number = prefelse.getString((getString(R.string.duplicateEntry)), null); // getting String
            smsNumber = shared_sms_number;
            Log.d("000102", "USER UID: " + shared_sms_number);

        } catch (Exception e) {
            Log.d("000102", "Shared Err:" + e.getMessage());
        }


       /* adt = new Adt_SyncItems(ctx, arrayList);
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

                    Log.d("000102", "IFFFF");
                } else {
                    Log.d("000102", "ELssssss");
                }


                if (isConnectingToInternet()) {

                    if (temp.equalsIgnoreCase("0")) {
                        Toast.makeText(ctx, R.string.dataSendingInternet, Toast.LENGTH_SHORT).show();
                        Log.d("000102", "IFFFF 2");
                    } else {
                        Log.d("000102", "ELssssss 2");
                    }

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                if (arrayList.size() > 0) {

                                    sendPostRequest_overall(arrayList.get(0).get("manual_id"), arrayList.get(0).get("uid"), arrayList.get(0).get("khandan_id"), arrayList.get(0).get("name"),
                                            arrayList.get(0).get("nicnumber"), arrayList.get(0).get("phone_number"), arrayList.get(0).get("gender"), arrayList.get(0).get("dob"),
                                            arrayList.get(0).get("bio_code"), arrayList.get(0).get("qr_code"), arrayList.get(0).get("added_by"), arrayList.get(0).get("data"), arrayList.get(0).get("added_on"));

                                } else {
                                    alertDialog.dismiss();
                                    onBackPressed();
                                    //  Toast.makeText(ctx, "No data found", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                alertDialog.dismiss();
                                Log.d("000102", "Error:" + e.getMessage());
                            }
                        }
                    }, 1000);


                } else {
                    if (temp.equalsIgnoreCase("0")) {
                        Toast.makeText(ctx, R.string.dataSendingSMS, Toast.LENGTH_SHORT).show();

                        Log.d("000102", "IFFFF OFFLINE");
                    } else {
                        Log.d("000102", "ELssssss OFFLINE");
                    }
                   /* if (temp.equalsIgnoreCase("1")) {
                        alertDialog.dismiss();
                        Log.d("000102", "IFFFF 1111");
                    } else {
                        Log.d("000102", "ELSEE 1111");
                    }*/

                    //Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendSMSMethod_overall(arrayList.get(0).get("manual_id"), arrayList.get(0).get("uid"), arrayList.get(0).get("khandan_id"), arrayList.get(0).get("name"),
                                arrayList.get(0).get("nicnumber"), arrayList.get(0).get("phone_number"), arrayList.get(0).get("gender"), arrayList.get(0).get("dob"),
                                arrayList.get(0).get("bio_code"), arrayList.get(0).get("qr_code"), arrayList.get(0).get("added_by"), arrayList.get(0).get("data"), arrayList.get(0).get("added_on"));

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
                Log.d("000102", " mLastClickTime: " + mLastClickTime);

                if (isConnectingToInternet()) {

                    Toast.makeText(ctx, R.string.dataSendingInternet, Toast.LENGTH_SHORT).show();
                    /*if (arrayList.get(position).get("is_synced").equalsIgnoreCase("1,0")){
                        Toast.makeText(ctx, "Data already synced", Toast.LENGTH_SHORT).show();
                    }
                    else   if (arrayList.get(position).get("is_synced").equalsIgnoreCase("1,3")){
                        Toast.makeText(ctx, "Data already synced !!", Toast.LENGTH_SHORT).show();
                    }
                    else {*/
                        //continue

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
                                    Log.d("000102", "Name: " + mData[position][4]);

                                    pos_value = String.valueOf(position);

                              /*  sendPostRequest(mData[position][1], mData[position][2], mData[position][3], mData[position][4], mData[position][5], mData[position][6], mData[position][8],
                                        mData[position][10], mData[position][11], mData[position][12], mData[position][13], mData[position][7], mData[position][15]);*/

                                    sendPostRequest(arrayList.get(position).get("manual_id"), arrayList.get(position).get("uid"), arrayList.get(position).get("khandan_id"), arrayList.get(position).get("name"),
                                            arrayList.get(position).get("nicnumber"), arrayList.get(position).get("phone_number"), arrayList.get(position).get("gender"), arrayList.get(position).get("dob"),
                                            arrayList.get(position).get("bio_code"), arrayList.get(position).get("qr_code"), arrayList.get(position).get("added_by"), arrayList.get(position).get("data"), arrayList.get(position).get("added_on"));


                                }
                            }, 1000);

                        } catch (Exception e) {
                            Log.d("000102", "Catach Err: " + e.getMessage());
                        }
                    //}

                } else {
                    Toast.makeText(ctx, R.string.dataSendingSMS, Toast.LENGTH_SHORT).show();
                    
                    /*if (arrayList.get(position).get("is_synced").equalsIgnoreCase("0,3")){
                        Toast.makeText(ctx, "Data already synced", Toast.LENGTH_SHORT).show();
                    }
                    else   if (arrayList.get(position).get("is_synced").equalsIgnoreCase("1,3")){
                        Toast.makeText(ctx, "Data already synced !!", Toast.LENGTH_SHORT).show();
                    }
                    else {*/
                 //   Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                    pos_value = String.valueOf(position);
                   /* sendSMSMethod(mData[position][1], mData[position][2], mData[position][3], mData[position][4], mData[position][5], mData[position][6], mData[position][8],
                            mData[position][10], mData[position][11], mData[position][12], mData[position][13], mData[position][7], mData[position][15]);*/
                    sendSMSMethod(arrayList.get(position).get("manual_id"), arrayList.get(position).get("uid"), arrayList.get(position).get("khandan_id"), arrayList.get(position).get("name"),
                            arrayList.get(position).get("nicnumber"), arrayList.get(position).get("phone_number"), arrayList.get(position).get("gender"), arrayList.get(position).get("dob"),
                            arrayList.get(position).get("bio_code"), arrayList.get(position).get("qr_code"), arrayList.get(position).get("added_by"), arrayList.get(position).get("data"), arrayList.get(position).get("added_on"));
               // }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("000102", "onResume !!!!!!!!!!!! ");

        try{
            adt = new Adt_SyncItems(ctx, arrayList);
            lv.setAdapter(adt);
            new Task().execute();

        }catch (Exception e)
        {
            Log.d("000102", "onResume Error: " +e.getMessage());

        }
    }

    private void sendPostRequest(final String manual_id, final String uid, final String khandan_id, final String full_name,
                                 final String nicnumber, final String phone_number, final String gender, final String dob,
                                 final String bio_code, final String qr_code, final String added_by,
                                 final String data,final String added_on) {

       // String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/family/members";

        String url = UrlClass.update_member_url;
        Log.d("000102", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {
                    //   Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {
                        Log.d("000102", "Response:    " + response);

                       /* String is_synced = "0";
                        if (arrayList.get(Integer.parseInt(pos_value)).get("is_synced").equalsIgnoreCase("0")) {
                            Log.d("000102", "IF isSynced 0 ");
                            is_synced = "1,0";
                        } else if (arrayList.get(Integer.parseInt(pos_value)).get("is_synced").equalsIgnoreCase("0,3")) {
                            Log.d("000102", "ELSE IF isSynced 0,3 ");
                            is_synced = "1,3";
                        }
                        else {
                            Log.d("000102", "ELSE isSynced");
                            is_synced = "0";
                        }*/

                        Lister ls = new Lister(ctx);
                       ls.createAndOpenDB();
                        String update_record = "UPDATE MEMBER SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE uid = '" + uid + "'";
                                ls.executeNonQuery(update_record);
                       /* "is_synced='" + String.valueOf(1) + "' " ;*/

                        final Snackbar snackbar = Snackbar.make(findViewById(R.id.sync_khandan_layout), R.string.dataSynced, Snackbar.LENGTH_SHORT);
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
                            },1500); }
                    } else {
                        alertDialog.dismiss();
                        Log.d("000102", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    alertDialog.dismiss();
                    Log.d("000102", "Err: " + e.getMessage());
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alertDialog.dismiss();
                Log.d("000102", "onErrorResponse: " + error.getMessage());
                String string = new String(error.networkResponse.data);
                Log.d("000102", "onErrorResponse String: " + string);
                Log.d("000102", "onErrorResponse Status: " + error.networkResponse.statusCode);
                if (string.contains((getString(R.string.duplicateEntry))))
                {
                    Log.d("000102", "Duplicate Entry YES !!!!!!!!: ");
                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.sync_khandan_layout), R.string.dataAlreadySyncedServer, Snackbar.LENGTH_SHORT);
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
                                },1500); }
                            
                        }
                    },2500);

                }
                else{
                    alertDialog.dismiss();
                    Log.d("000102", "Duplicate Entry NOT: ");
                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.sync_khandan_layout), R.string.noDataSyncAlert, Snackbar.LENGTH_SHORT);
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
                params.put("family_id", khandan_id);
                params.put("qr_code", qr_code);
                params.put("full_name", full_name);
                params.put("nic_number", nicnumber);
                params.put("phone_number", phone_number);
                params.put("data", data);
                params.put("gender", gender);
                params.put("dob", dob);
                params.put("bio_code", bio_code);
                params.put("added_by", added_by);
                params.put("added_on", added_on);

                Log.d("000102", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000102", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }


    private void sendPostRequest_overall(final String manual_id, final String uid, final String khandan_id, final String full_name,
                                         final String nicnumber, final String phone_number, final String gender, final String dob,
                                         final String bio_code, final String qr_code, final String added_by,
                                         final String data,  final String added_on) {

        String url = UrlClass.update_member_url;
        //String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/family/members";

        Log.d("000102", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {
                    //   Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {
                        Log.d("000102", "Response:    " + response);

                      /*  String is_synced="0";
                        if (arrayList.get(0).get("is_synced").equalsIgnoreCase("0"))
                        {
                            Log.d("000102", "IF isSynced 0 ");
                            is_synced="1,0";
                        }
                        else   if (arrayList.get(0).get("is_synced").equalsIgnoreCase("0,3"))
                        {
                            Log.d("000102", "ELSE IF isSynced 0,3 ");
                            is_synced="1,3";
                        }
                        else {
                            Log.d("000102", "ELSE isSynced");
                            is_synced="0";
                        }*/


                        Lister ls = new Lister(ctx);
                       ls.createAndOpenDB();

                        String update_record = "UPDATE MEMBER SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE uid = '" + uid + "'";
                        ls.executeNonQuery(update_record);

                        Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();

                        arrayList.remove(0);
                        adt.notifyDataSetChanged();

                        reload();

                    } else {
                        alertDialog.dismiss();
                        Log.d("000102", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    alertDialog.dismiss();
                    Log.d("000102", "Err: " + e.getMessage());
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("000102", "onErrorResponse: " + error.getMessage());

                String string = new String(error.networkResponse.data);
                Log.d("000102", "onErrorResponse String: " + string);
                Log.d("000102", "onErrorResponse Status: " + error.networkResponse.statusCode);

                if (string.contains((getString(R.string.duplicateEntry))))
                {
                    Log.d("000102", "Duplicate Entry YES !!!!!!!!: ");

                    Toast.makeText(ctx, R.string.dataAlreadySyncedServer, Toast.LENGTH_SHORT).show();

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
                    Log.d("000102", "Duplicate Entry NOT: ");
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
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
                params.put("family_id", khandan_id);
                params.put("qr_code", qr_code);
                params.put("full_name", full_name);
                params.put("nic_number", nicnumber);
                params.put("phone_number", phone_number);
                params.put("data", data);
                params.put("gender", gender);
                params.put("dob", dob);
                params.put("bio_code", bio_code);
                params.put("added_by", added_by);
                params.put("added_on", added_on);


                Log.d("000102", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000102", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }



    public void sendSMSMethod(final String manual_id, final String uid, final String khandan_id, final String full_name,
                              final String nicnumber, final String phone_number, final String gender, final String dob,
                              final String bio_code, final String qr_code, final String added_by,
                              final String data,final String added_on){

        try {

            JSONObject jobj = new JSONObject();
            jobj.put("manual_id", manual_id);
            jobj.put("uid", uid);
            jobj.put("family_id", khandan_id);
            jobj.put("qr_code", qr_code);
            jobj.put("full_name", full_name);
            jobj.put("nic_number", nicnumber);
            jobj.put("phone_number", phone_number);
            jobj.put("data", data);
            jobj.put("gender", gender);
            jobj.put("dob", dob);
            jobj.put("bio_code", bio_code);
            jobj.put("added_by", added_by);
            jobj.put("added_on", added_on);

            Log.d("000102", "JSON DATA:  " + jobj.toString());

            String toencode_sms = getBase64String(String.valueOf(jobj));

            String uuid = UUID.randomUUID().toString().replace("-", "");
            final String sms_data = "HAYATPK" + "|" + uuid + "|" + "MEMBER" + "|" + toencode_sms;
            Log.d("000102", "SMS DATA:  " + sms_data);


            TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

          /*  Log.d("000102", "SMS:  " + telMgr.getNetworkOperator());
            Log.d("000102", "SMS:  " + telMgr.getNetworkOperatorName());
            Log.d("000102", "SMS:  " + telMgr.getSimOperator());
            Log.d("000102", "SMS:  " + telMgr.getSimOperatorName());
            Log.d("000102", "SMS:  " + telMgr.getNetworkType());*/



            int simState = telMgr.getSimState();
            switch (simState) {
                case TelephonyManager.SIM_STATE_ABSENT:
                    // do something
                    //Toast.makeText(getApplicationContext(), "Data is not synced, there is no sim in your phone.", Toast.LENGTH_LONG).show();

                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.sync_khandan_layout), R.string.noPhoneSIM, Snackbar.LENGTH_SHORT);
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
                    Toast.makeText(getApplicationContext(), R.string.simStateNetworkLock, Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                    // do something
                    Toast.makeText(getApplicationContext(), R.string.simStatePINrequired, Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                    // do something
                    Toast.makeText(getApplicationContext(), R.string.simStatePUKrequired, Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.USSD_RETURN_FAILURE:
                    Toast.makeText(getApplicationContext(), R.string.ussdReturnFailure, Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.USSD_ERROR_SERVICE_UNAVAIL:
                    Toast.makeText(getApplicationContext(), R.string.ussdErrorServiceUnavail, Toast.LENGTH_LONG).show();

                    break;


                case TelephonyManager.SIM_STATE_READY:

                    // do something

                    Log.d("000102", "3");
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
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.setCancelable(true);
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    progressDialog.show();


                    //check_smsManager(sms_data);

                   new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
                            ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
                            Log.d("000102", "4");

                            PendingIntent piSent = PendingIntent.getBroadcast(SyncMemberReg_Activity.this, 0, new Intent("SMS_SENT"), 0);
                            PendingIntent piDelivered = PendingIntent.getBroadcast(SyncMemberReg_Activity.this, 0, new Intent("SMS_DELIVERED"), 0);
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

                                Log.d("000102", "SMS SENT SUCCESSFULLY !!!!!!!!!!");
                                //  Toast.makeText(this, "SMS has been sent.", Toast.LENGTH_SHORT).show();

                                /*       String is_synced="0";
                       if (arrayList.get(Integer.parseInt(pos_value)).get("is_synced").equalsIgnoreCase("0"))
                       {
                           Log.d("000102", "IF isSynced 0 ");
                           is_synced="0,3";
                       }
                       else   if (arrayList.get(Integer.parseInt(pos_value)).get("is_synced").equalsIgnoreCase("1,0"))
                       {
                           Log.d("000102", "ELSE IF isSynced 1,0 ");
                           is_synced="1,3";
                       }
                       else {
                           Log.d("000102", "ELSE isSynced");
                           is_synced="0";
                       }*/

                                Lister ls = new Lister(ctx);
                                ls.createAndOpenDB();
                                
                                String is_synced="0,3";
                                String update_record = "UPDATE MEMBER SET " +
                                        "is_synced='" + String.valueOf(is_synced) + "' " +
                                        "WHERE uid = '" + uid + "'";
                                ls.executeNonQuery(update_record);

                                Boolean res = ls.executeNonQuery(update_record);
                                Log.d("000102", "Data Update:  " + update_record);
                                Log.d("000102", "Query Update:  " + res);

                                final Snackbar snackbar = Snackbar.make(findViewById(R.id.sync_khandan_layout), R.string.dataSynced, Snackbar.LENGTH_SHORT);
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

                               // Log.d("000102", "POS REMOVED:  " + pos_value);

                                arrayList.remove(Integer.parseInt(pos_value));
                                arrayList.remove(true);

                                if (arrayList.size() > 0) {
                                    progressDialog.dismiss();
                                    adt.notifyDataSetChanged();

                                  /*  finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(getIntent());
                                    overridePendingTransition(0, 0);*/

                                   Log.d("000102", "IF: ");

                                } else {
                                    adt.notifyDataSetChanged();
                                    progressDialog.dismiss();
                                    tv_record.setVisibility(View.VISIBLE);

                                    Log.d("000102", "ELSE: ");

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
                                Log.d("000102", "Exception Sending faild " + e);
                                e.printStackTrace();
                                // Toast.makeText(SyncMemberReg_Activity.this, R.string.SMSsendingFail, Toast.LENGTH_SHORT).show();
                                final Snackbar snackbar = Snackbar.make(findViewById(R.id.sync_khandan_layout), R.string.SMSsendPermFail, Snackbar.LENGTH_SHORT);
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
                    Toast.makeText(getApplicationContext(), R.string.simStateUnknown, Toast.LENGTH_SHORT).show();
                    break;
            }

            Log.d("000102", "5");

        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("000102", "Error:: " + e.getMessage());
        }
    }


    public void sendSMSMethod_overall(final String manual_id, final String uid, final String khandan_id, final String full_name,
                                      final String nicnumber, final String phone_number, final String gender, final String dob,
                                      final String bio_code, final String qr_code, final String added_by,
                                      final String data,  final String added_on) {

        try {

            JSONObject jobj = new JSONObject();
            jobj.put("manual_id", manual_id);
            jobj.put("uid", uid);
            jobj.put("family_id", khandan_id);
            jobj.put("qr_code", qr_code);
            jobj.put("full_name", full_name);
            jobj.put("nic_number", nicnumber);
            jobj.put("phone_number", phone_number);
            jobj.put("data", data);
            jobj.put("gender", gender);
            jobj.put("dob", dob);
            jobj.put("bio_code", bio_code);
            jobj.put("added_by", added_by);
            jobj.put("added_on", added_on);


            Log.d("000102", "JSON DATA:  " + jobj.toString());

            String toencode_sms = getBase64String(String.valueOf(jobj));

            String uuid = UUID.randomUUID().toString().replace("-", "");
            final String sms_data = "HAYATPK" + "|" + uuid + "|" + "MEMBER" + "|" + toencode_sms;
            Log.d("000102", "SMS DATA:  " + sms_data);


            TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            int simState = telMgr.getSimState();
            switch (simState) {
                case TelephonyManager.SIM_STATE_ABSENT:
                    // do something
                    //Toast.makeText(getApplicationContext(), "Data is not synced, there is no sim in your phone.", Toast.LENGTH_LONG).show();

                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.sync_khandan_layout), R.string.noPhoneSIM, Snackbar.LENGTH_SHORT);
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
                    alertDialog.dismiss();
                    break;

                case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                    // do something
                    Toast.makeText(getApplicationContext(), R.string.simStateNetworkLock, Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                    temp = "0";
                    break;

                case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                    // do something
                    Toast.makeText(getApplicationContext(), R.string.simStatePINrequired, Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                    temp = "0";
                    break;

                case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                    // do something
                    Toast.makeText(getApplicationContext(), R.string.simStatePUKrequired, Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                    temp = "0";
                    break;

                case TelephonyManager.USSD_RETURN_FAILURE:
                    Toast.makeText(getApplicationContext(), R.string.ussdReturnFailure, Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                    temp = "0";
                    break;

                case TelephonyManager.USSD_ERROR_SERVICE_UNAVAIL:
                    Toast.makeText(getApplicationContext(), R.string.ussdErrorServiceUnavail, Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                    temp = "0";
                    break;

                case TelephonyManager.SIM_STATE_READY:
                    // do something


                    Log.d("000102", "3");
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
                            Log.d("000102", "4");

                            PendingIntent piSent = PendingIntent.getBroadcast(SyncMemberReg_Activity.this, 0, new Intent("SMS_SENT"), 0);
                            PendingIntent piDelivered = PendingIntent.getBroadcast(SyncMemberReg_Activity.this, 0, new Intent("SMS_DELIVERED"), 0);
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

                                Log.d("000102", "SMS SENT SUCCESSFULLY !!!!!!!!!!");
                                //  Toast.makeText(this, "SMS has been sent.", Toast.LENGTH_SHORT).show();

                                /*String is_synced="0";
                                if (arrayList.get(0).get("is_synced").equalsIgnoreCase("0"))
                                {
                                    Log.d("000102", "IF isSynced 0 ");
                                    is_synced="0,3";
                                }
                                else   if (arrayList.get(0).get("is_synced").equalsIgnoreCase("1,0"))
                                {
                                    Log.d("000102", "ELSE IF isSynced 1,0 ");
                                    is_synced="1,3";
                                }
                                else {
                                    Log.d("000102", "ELSE isSynced");
                                    is_synced="0";
                                }*/


                                Lister ls = new Lister(ctx);
                                ls.createAndOpenDB();

                                String is_synced="0,3";
                                String update_record = "UPDATE MEMBER SET " +
                                        "is_synced='" + String.valueOf(is_synced) + "' " +
                                        "WHERE uid = '" + uid + "'";
                                ls.executeNonQuery(update_record);

                                Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();


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
                                Log.d("000102", "Exception Sending faild " + e);
                                e.printStackTrace();
                                Toast.makeText(SyncMemberReg_Activity.this, R.string.SMSsendingFail, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 1500);


                    break;

                case TelephonyManager.SIM_STATE_UNKNOWN:
                    // do something
                    Toast.makeText(getApplicationContext(), R.string.simStateUnknown, Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    temp = "0";
                    break;
            }

            Log.d("000102", "5");


        } catch (Exception e) {
            alertDialog.dismiss();
            temp = "0";
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("000102", "Error:: " + e.getMessage());
        }
    }

    
    private void check_smsManager(final String message) {
        try {
            Log.d("000102", "333");

            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            final PendingIntent piSent = PendingIntent.getBroadcast(this, 0,
                    new Intent(SENT), 0);

            final PendingIntent piDelivered = PendingIntent.getBroadcast(this, 0,
                    new Intent(DELIVERED), 0);



            //---when the SMS has been sent---
            registerReceiver(new BroadcastReceiver(){
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode())
                    {
                        case Activity.RESULT_OK:
                            Log.d("000102", "30");
                            Toast.makeText(getBaseContext(), SMSsent,
                                    Toast.LENGTH_SHORT).show();

                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Log.d("000102", "31");
                            Toast.makeText(getBaseContext(), R.string.genericFailure,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(getBaseContext(), R.string.noService,
                                    Toast.LENGTH_SHORT).show();
                            Log.d("000102", "32");
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(getBaseContext(), R.string.nullPDU,
                                    Toast.LENGTH_SHORT).show();
                            Log.d("000102", "33");
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(getBaseContext(), radioOff,
                                    Toast.LENGTH_SHORT).show();
                            Log.d("000102", "34");
                            break;
                    }
                }
            }, new IntentFilter(SENT));

            //---when the SMS has been delivered---
            registerReceiver(new BroadcastReceiver(){
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    switch (getResultCode())
                    {
                        case Activity.RESULT_OK:
                            Toast.makeText(getBaseContext(), R.string.SMSdelivered,
                                    Toast.LENGTH_SHORT).show();
                            Log.d("000102", "35");
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(getBaseContext(), SMSnotDelivered,
                                    Toast.LENGTH_SHORT).show();
                            Log.d("000102", "36");
                            break;
                    }
                }
            }, new IntentFilter(DELIVERED));

            final ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
            final ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
            Log.d("000102", "37");


            final SmsManager sms = SmsManager.getDefault();
            Log.d("000102", "SMSSSSSSSSS:  " +sms.toString());

            final ArrayList<String> mSMSMessage = sms.divideMessage(message);
            Log.d("000102", "SMS DIVIDE:  " +sms.divideMessage(message).size());
            Log.d("000102", "SMS MESSAGE:  " +mSMSMessage.toString());

            for (int in = 0; in < mSMSMessage.size(); in++) {
                sentPendingIntents.add(in, piSent);
                deliveredPendingIntents.add(in, piDelivered);
            }
            Log.d("000102", "SMS sentPendingIntents:  " +sentPendingIntents.toString());
            Log.d("000102", "SMS deliveredPendingIntents:  " +deliveredPendingIntents.toString());

            Log.d("000102", "38");
            sms.sendMultipartTextMessage(smsNumber, null, mSMSMessage, sentPendingIntents, deliveredPendingIntents);
            Log.d("000102", "39");




           /* Log.d("000102", "333");

            BroadcastReceiver smsSentReceiverDel = new BroadcastReceiver() {

                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    // TODO Auto-generated method stub
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Log.d("000102", "6");
                            Toast.makeText(getApplicationContext(), "RESULT OKY", Toast.LENGTH_SHORT).show();

                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            //credit problem Generic Failure
                            Log.d("000102", "7");
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Data has not been synced. You have insufficient balance in your SIM.", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Log.d("000102", "8");
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), "No Service ", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            //problem with sms binding
                            Log.d("000102", "9");
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Null PDU ", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            //air plane mode
                            Log.d("000102", "10");
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Data is not synced, your phone is on airplane mode.", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Log.d("000102", "11");
                            progressDialog.dismiss();
                            break;
                    }
                    Log.d("000102", "12");
                }
            };

            registerReceiver(smsSentReceiverDel, new IntentFilter("SMS_SENT"));*/
        }catch (Exception e)
        {
            Log.d("000102", "Error11:" + e.getMessage());
        }
        }
        
        
    public static String getBase64String(String jsonString) {

        String base64 = "";
        try {
            byte[] data = (jsonString + "").getBytes("UTF-8");
            base64 = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e("000102", "Exception: " + e.getMessage());
        }
        //Log.d("000102", "getBase64String " + base64);

        return base64;

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

    private void read_data() {

        arrayList.clear();

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            //mData = ls.executeReader("Select *from MEMBER where is_synced IN ('0','0,3','1,0')");
            mData = ls.executeReader("Select *from MEMBER where is_synced IN ('0','0,3')");
            Log.d("000102", "Data: " + mData.length);

            HashMap<String, String> map;
            for (int i = 0; i < mData.length; i++) {

                map = new HashMap<>();

             /*   Log.d("000102", "ID: " + mData[i][0]);
                Log.d("000102", "manual_id: " + mData[i][1]);*/
                Log.d("000102", "uid: " + mData[i][2]);
                Log.d("000102", "khandan_id: " + mData[i][3]);
                Log.d("000102", "full_name: " + mData[i][4]);
              /*  Log.d("000102", "nicnumber: " + mData[i][5]);
                Log.d("000102", "phone_number: " + mData[i][6]);
                Log.d("000102", "data: " + mData[i][7]);
                Log.d("000102", "gender: " + mData[i][8]);
                Log.d("000102", "age: " + mData[i][9]);
                Log.d("000102", "dob: " + mData[i][10]);
                Log.d("000102", "bio_code: " + mData[i][11]);
                Log.d("000102", "qr_code: " + mData[i][12]);
                Log.d("000102", "added_by: " + mData[i][13]);
                Log.d("000102", "is_synced: " + mData[i][14]);
                Log.d("000102", "added_on: " + mData[i][15]);*/

                map.put("Id", "" + mData[i][0]);
                map.put("manual_id", "" + mData[i][1]);
                map.put("uid", "" + mData[i][2]);
                map.put("khandan_id", "" + mData[i][3]);
                map.put("name", "" + mData[i][4]);
                map.put("nicnumber", "" + mData[i][5]);
                map.put("phone_number", "" + mData[i][6]);
                map.put("data", "" + mData[i][7]);
                map.put("gender", "" + mData[i][8]);
                map.put("age", "" + mData[i][9]);
                map.put("dob", "" + mData[i][10]);
                map.put("bio_code", "" + mData[i][11]);
                map.put("qr_code", "" + mData[i][12]);
                map.put("added_by", "" + mData[i][13]);
                map.put("is_synced", "" + mData[i][14]);
                map.put("added_on", "" + mData[i][15]);


                if (mData[i][15] == null) {
                    map.put("datetime", "" + "null");
                } else {
                    Date date = new Date(Long.parseLong(mData[i][15]));
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    String formatted = format.format(date);
                    Log.d("000102", "datetime: " + formatted);

                    map.put("datetime", "" + formatted);
                }

                arrayList.add(map);
            }

           /*adt = new Adt_SyncItems(ctx, arrayList);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);*/


        } catch (Exception e) {
            Log.d("000102", "Error: " + e.getMessage());
            iv_sync.setVisibility(View.GONE);
            //   tv_record.setVisibility(View.VISIBLE);
        }
    }



    class Task extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            Log.d("000102", "ON PREEEEE: ");
            ll_pbProgress.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Log.d("000102", "ON BACkGROUND: ");
                read_data();
                Thread.sleep(1500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("000102", "ON EXECUTE: ");
            ll_pbProgress.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            adt.notifyDataSetChanged();
            super.onPostExecute(result);
        }

    }

  /*  @Override
    protected void onResume() {
        super.onResume();

        Log.d("000102","ONRESUME");
        smsSentReceiverDel = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                // TODO Auto-generated method stub
                Lister ls = new Lister(SyncMemberReg_Activity.this);
           ls.createAndOpenDB();

                switch (getResultCode()) {
                    case Activity.RESULT_OK:

                        Log.d("000102","RESULT OK");

                        Toast.makeText(getBaseContext(), "SMS has been sent successfully.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        onBackPressed();

                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Log.d("000102","RESULT ERROR");
                        Toast.makeText(getBaseContext(), "Data has not been synced. You have insufficient balance in your SIM.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:

                        Log.d("000102","NO SERVICE");
                        progressDialog.dismiss();
                        Log.d("000102","NULL PDU");
                        Toast.makeText(getBaseContext(), "No Service ", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        //problem with sms binding
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Null PDU ", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:

                        Log.d("000102","AP MODE");
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Data is not synced, your phone is on airplane mode.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        progressDialog.dismiss();
                        break;
                }
            }
        };
        smsDeliveredReceiverDel = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                // TODO Auto-generated method stub
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Log.d("000102","OKAY");

                        Toast.makeText(getBaseContext(), "SMS has been delivered.", Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.d("000102","CANCELLED");

                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(), "SMS not delivered ", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        registerReceiver(smsSentReceiverDel, new IntentFilter("SMS_SENT"));
        registerReceiver(smsDeliveredReceiverDel, new IntentFilter("SMS_DELIVERED"));
        Log.d("000102","ENDDDDDDDDDD");
    }*/

   /* @Override
    protected void onResume() {
        super.onResume();


        arrayList.clear();

        try {
            Lister ls = new Lister(ctx);
           ls.createAndOpenDB();

            mData = ls.executeReader("Select *from MEMBER where is_synced = '0'");
            Log.d("000102", "Data: " + mData.length);

            HashMap<String, String> map;
            for (int i = 0; i < mData.length; i++) {

                map = new HashMap<>();

                Log.d("000102", "ID: " + mData[i][0]);
                Log.d("000102", "manual_id: " + mData[i][1]);
                Log.d("000102", "uid: " + mData[i][2]);
                Log.d("000102", "khandan_id: " + mData[i][3]);
                Log.d("000102", "full_name: " + mData[i][4]);
                Log.d("000102", "nicnumber: " + mData[i][5]);
                Log.d("000102", "phone_number: " + mData[i][6]);
                Log.d("000102", "data: " + mData[i][7]);
                Log.d("000102", "gender: " + mData[i][8]);
                Log.d("000102", "age: " + mData[i][9]);
                Log.d("000102", "dob: " + mData[i][10]);
                Log.d("000102", "bio_code: " + mData[i][11]);
                Log.d("000102", "qr_code: " + mData[i][12]);
                Log.d("000102", "added_by: " + mData[i][13]);
                Log.d("000102", "is_synced: " + mData[i][14]);
                Log.d("000102", "added_on: " + mData[i][15]);

                map.put("Id", "" + mData[i][0]);
                map.put("manual_id", "" + mData[i][1]);
                map.put("uid", "" + mData[i][2]);
                map.put("khandan_id", "" + mData[i][3]);
                map.put("name", "" + mData[i][4]);
                map.put("nicnumber", "" + mData[i][5]);
                map.put("phone_number", "" + mData[i][6]);
                map.put("data", "" + mData[i][7]);
                map.put("gender", "" + mData[i][8]);
                map.put("age", "" + mData[i][9]);
                map.put("dob", "" + mData[i][10]);
                map.put("bio_code", "" + mData[i][11]);
                map.put("qr_code", "" + mData[i][12]);
                map.put("added_by", "" + mData[i][13]);
                map.put("is_synced", "" + mData[i][14]);
                map.put("added_on", "" + mData[i][15]);


                if (mData[i][15] == null) {
                    map.put("datetime", "" + "null");
                } else {
                    Date date = new Date(Long.parseLong(mData[i][15]));
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    String formatted = format.format(date);
                    Log.d("000102", "datetime: " + formatted);

                    map.put("datetime", "" + formatted);
                }

                arrayList.add(map);
            }

            adt = new Adt_SyncItems(ctx, arrayList);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);


        } catch (Exception e) {
            Log.d("000102", "Error: " + e.getMessage());
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
