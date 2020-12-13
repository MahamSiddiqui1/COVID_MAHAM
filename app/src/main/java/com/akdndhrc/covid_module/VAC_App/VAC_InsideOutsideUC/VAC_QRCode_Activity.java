package com.akdndhrc.covid_module.VAC_App.VAC_InsideOutsideUC;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.ServiceLocation;
import com.akdndhrc.covid_module.Utils;
import com.akdndhrc.covid_module.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.Result;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class VAC_QRCode_Activity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    Context ctx = VAC_QRCode_Activity.this;

    private ZXingScannerView mScannerView;
    ImageView iv_navigation_drawer, iv_home;
    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    ADT adt;
    Dialog dialog;

    String[][] mData;
    String child_uid,login_useruid;

    double latitude;
    double longitude;
    ServiceLocation serviceLocation;

    public  static  String vac_qr_merged="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_qrcode);

        child_uid = getIntent().getExtras().getString("u_id");

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
            String shared_useruid = prefelse.getString(("login_userid"), null); // getting String
            login_useruid = shared_useruid;
            Log.d("000555", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000555", "Shared Err:" + e.getMessage());
        }



        try {
            serviceLocation = new ServiceLocation(ctx);
            serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            serviceLocation.callAsynchronousTask();
        } catch (Exception e) {
            Log.d("000555", "GPS Service Err:  " + e.getMessage());
        }



        //ScannerView
        mScannerView = findViewById(R.id.scanner);

        //ImageView
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home.setVisibility(View.GONE);
        iv_navigation_drawer.setVisibility(View.GONE);
    }

    @Override
    public void handleResult(final Result rawResult) {
        // Do something with the result here
        Log.v("TAG", rawResult.getText()); // Prints scan results
        // Prints the scan format (qrcode, pdf417 etc.)
        Log.v("TAG", rawResult.getBarcodeFormat().toString());
        mScannerView.stopCamera();

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_lv_merged_qrcode_layout, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setView(view);

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();


        hashMapArrayList.clear();

        final ListView lv = (ListView) view.findViewById(R.id.lv);

        TextView tvNoDataFound = (TextView) view.findViewById(R.id.tvNoDataFound);

        TextView tvDone = (TextView) view.findViewById(R.id.tvDone);
        TextView tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        final TextView tvScanAgain = (TextView) view.findViewById(R.id.tvScanAgain);
        final TextView tvMerged = (TextView) view.findViewById(R.id.tvMerged);

        final LinearLayout ll_threeButtons = (LinearLayout) view.findViewById(R.id.ll_threeButtons);
        final LinearLayout ll_twoButtons = (LinearLayout) view.findViewById(R.id.ll_twoButtons);
        final TextView tvYes = (TextView) view.findViewById(R.id.tvYes);
        final TextView tvNo = (TextView) view.findViewById(R.id.tvNo);


        try{
        Lister ls = new Lister(ctx);
        ls.createAndOpenDB();

         mData = ls.executeReader("Select t0.full_name,t0.dob,t0.age,t0.gender,JSON_EXTRACT(t0.data, '$.father_name'),JSON_EXTRACT(t0.data, '$.mother_name'),t2.name,t0.data,t0.uid from MEMBER t0 " +
                " INNER JOIN KHANDAN t1 ON t0.khandan_id = t1.uid " +
                " INNER JOIN VILLAGES t2 ON t1.village_id = t2.uid" +
                " where t0.qr_code = '" + rawResult.getText() + "' AND t0.uid <> '"+child_uid+"'");


            HashMap<String, String> map;
            for (int i = 0; i < mData.length; i++) {

                Log.d("000255", "child_uid: " +  child_uid);
                Log.d("000255", "full_name: " +  mData[i][0]);
                Log.d("000255", "dob: " +  mData[i][1]);
                Log.d("000255", R.string.ageColon +  mData[i][2]);
                Log.d("000255", "gender: " +  mData[i][3]);
                Log.d("000255", "father_name: " +  mData[i][4]);
                Log.d("000255", "mother_name: " +  mData[i][5]);
                Log.d("000255", "village_name: " +  mData[i][6]);
                Log.d("000255", "metadata: " +  mData[i][7]);
                Log.d("000255", "uid: " +  mData[i][8]);

                map = new HashMap<>();

                  map.put("full_name", "" + mData[i][0]);
                    map.put("dob", "" + mData[i][1]);
                    map.put("age", "" + mData[i][2]);
                    map.put("village_name", "" + mData[i][6]);

                    if (mData[i][4].isEmpty())
                    {
                        map.put("father_name", "" + "No Father Name");
                    }
                    else{
                        map.put("father_name", "" + mData[i][4]);
                    }

                if (mData[i][5].isEmpty())
                {
                    map.put("mother_name", "" + "No Mother Name");
                }
                else{
                    map.put("mother_name", "" + mData[i][5]);
                }

                if (mData[i][3].equalsIgnoreCase("0")) {
                    map.put("gender", "" + R.string.aurat);
                } else if (mData[i][3].equalsIgnoreCase("1")) {
                    map.put("gender", "" + getString(R.string.murd));
                } else {
                    map.put("gender", "" + R.string.unknown);
                }

                 hashMapArrayList.add(map);

                }

                adt = new ADT(ctx,hashMapArrayList);
                lv.setAdapter(adt);

            // If you would like to resume scanning, call this method below:
            mScannerView.resumeCameraPreview(this);

        }
        catch (Exception e){
            tvNoDataFound.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
            tvDone.setVisibility(View.GONE);
           Toast.makeText(ctx, R.string.noQRcode, Toast.LENGTH_LONG).show();

            Log.d("000255", "Err: " +  e.getMessage());
        }

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lv.setVisibility(View.GONE);
                ll_threeButtons.setVisibility(View.GONE);
                tvMerged.setVisibility(View.VISIBLE);
                ll_twoButtons.setVisibility(View.VISIBLE);

               // alertDialog.dismiss();
            }
        });


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                onBackPressed();
            }
        });


        tvScanAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                mScannerView.startCamera();
            }
        });

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                alertDialog.dismiss();

                dialog = new Dialog(ctx);
                LayoutInflater layout = LayoutInflater.from(ctx);
                final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

                dialog.setContentView(dialogView);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("000457", "20000 SEC ");
                        insert_into_db(v);
                    }
                },2000);
            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                onBackPressed();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        // Start camera on resume
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }

    private void insert_into_db(View v) {


        try {
            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            String TodayDate = dates.format(c.getTime());

            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();


            if (serviceLocation.showCurrentLocation() == true) {
                latitude = serviceLocation.getLatitude();
                longitude = serviceLocation.getLongitude();
                Log.d("000555", " latitude: " + latitude);
                Log.d("000555", " longitude: " + longitude);
            } else {
                try {
                    serviceLocation.doAsynchronousTask.cancel();
                }catch (Exception e){}
                try {

                    String[][] mData = ls.executeReader("SELECT max(added_on),metadata,count(*) from CVACCINATION");

                    if (Integer.parseInt(mData[0][2]) >  0 ) {
                        JSONObject jsonObject = new JSONObject(mData[0][1]);
                        Log.d("000258", "  Last Latitude: " + jsonObject.getString("lat"));
                        Log.d("000258", "  Last Longitude: " + jsonObject.getString("lng"));

                        latitude = Double.parseDouble(jsonObject.getString("lat"));
                        longitude = Double.parseDouble(jsonObject.getString("lng"));

                        Toast.makeText(ctx, R.string.dataGPS, Toast.LENGTH_SHORT).show();
                    } else {
                        latitude = Double.parseDouble("0.0");
                        longitude = Double.parseDouble("0.0");
                        Toast.makeText(ctx, R.string.notDataGPS, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000258", "Read CVACCINE Error: " + e.getMessage());
                }
            }


            String added_on = String.valueOf(System.currentTimeMillis());

            final JSONObject jobj = new JSONObject();
            jobj.put("lat", "" + String.valueOf(latitude));
            jobj.put("lng", "" + String.valueOf(longitude));

            String ans1 = "insert or ignore into MEMBER_MERGERD(member_uid_1, member_uid_2, metadata, record_data,added_by,is_synced,added_on)" +
                    "values" +
                    "(" +
                    "'" + child_uid + "'," +
                    "'" + mData[0][8] + "'," +
                    "'" + jobj + "'," +
                    "'" + TodayDate + "'," +
                    "'" + login_useruid + "'," +
                    "'0'," +
                    "'" + added_on + "'" +
                    ")";

            Boolean res = ls.executeNonQuery(ans1);
            Log.d("000457", "Data: " + ans1);
            Log.d("000457", "Query:" + res.toString());
            if (res.toString().equalsIgnoreCase("true"))
            {

                final Snackbar snackbar = Snackbar.make(findViewById(R.id.search_qrcode_layout), R.string.dupDataMerged, Snackbar.LENGTH_SHORT);
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
                snackbar.setDuration(4000);
                snackbar.show();


                if (Utils.haveNetworkConnection(ctx) > 0) {

                    sendPostRequest(child_uid, mData[0][8],String.valueOf(jobj),TodayDate,login_useruid,added_on);

                } else {
                    //Toast.makeText(ctx, R.string.dataEdited, Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                dialog.dismiss();

                final Snackbar snackbar = Snackbar.make(findViewById(R.id.search_qrcode_layout), R.string.dupDataNotMerged, Snackbar.LENGTH_SHORT);
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

            }


           /* if (Utils.haveNetworkConnection(ctx) > 0) {

            } else {
               // Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();
                Toast.makeText(ctx, "ڈپلیکیٹ ڈیٹا ملا دیا گیا ہے.", Toast.LENGTH_SHORT).show();
            }*/

        } catch (Exception e) {
            dialog.dismiss();
            Log.d("000457", "Err: " + e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {

            dialog.dismiss();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    vac_qr_merged="1";
                    finish();
                }
            },3000);


        }

    }


    private void sendPostRequest(final String member_id_1, final String member_id_2, final String metadata , final String record_data ,final String added_by,  final String added_on) {


        String url = "https://development.api.teekoplus.akdndhrc.org/sync/save/family/members/merge";

        Log.d("000333", "mURL " + url);
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
                        Log.d("000333", "Response:    " + response);

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE MEMBER_MERGERD SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid_1 = '" + member_id_1 + "' AND member_uid_2= '" + member_id_2 + "' AND added_by= '" + added_by + "'";

                        ls.executeNonQuery(update_record);

                        Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();

                    } else {

                        Log.d("000333", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {

                    Log.d("000333", "Err: " + e.getMessage());
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000333", "onErrorResponse:    " + error.getMessage());
                //    Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                Log.d("000333", "mParam " + params);

                params.put("member_id_1", member_id_1);
                params.put("member_id_2", member_id_2);
                params.put("metadata", metadata);
                params.put("record_data", record_data);
                params.put("added_by", added_by);
                params.put("added_on", added_on);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000333", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }




    public class ADT extends BaseAdapter {

        Context ctx;


        ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
        LayoutInflater inflater;


        public ADT(Context ctx, ArrayList<HashMap<String, String>> hashMapArrayList) {
            this.ctx = ctx;

            this.hashMapArrayList = hashMapArrayList;
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return hashMapArrayList.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(final int position, View view, ViewGroup parent) {

            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();

                view = inflater.inflate(R.layout.custom_lv_qrcode_member_data, null);
                holder.tvMemberName = (TextView) view.findViewById(R.id.tvMemberName);
                holder.tvFatherName = (TextView) view.findViewById(R.id.tvFatherName);
                holder.tvDOB = (TextView) view.findViewById(R.id.tvDOB);
                holder.tvVillageName = (TextView) view.findViewById(R.id.tvVillageName);
                holder.tvAge = (TextView) view.findViewById(R.id.tvAge);
                holder.tvGender = (TextView) view.findViewById(R.id.tvGender);


                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();

            }


            holder.tvMemberName.setText(hashMapArrayList.get(position).get("full_name"));
            holder.tvFatherName.setText(hashMapArrayList.get(position).get("father_name"));
            holder.tvDOB.setText(hashMapArrayList.get(position).get("dob"));
            holder.tvAge.setText(hashMapArrayList.get(position).get("age"));
            holder.tvGender.setText(hashMapArrayList.get(position).get("gender"));
            holder.tvVillageName.setText(hashMapArrayList.get(position).get("village_name"));


            return view;
        }

    }

    public static class ViewHolder {

        protected TextView tvMemberName,tvFatherName,tvDOB,tvVillageName,tvAge,tvGender;
    }

    @Override
    public void onBackPressed() {
        finish();
        vac_qr_merged="0";
        super.onBackPressed();

    }
}