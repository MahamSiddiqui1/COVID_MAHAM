package com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildRefferalActivities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.CustomClass.UrlClass;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.GPSTracker;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.NothingSelectedSpinnerAdapter;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.ServiceLocation;
import com.akdndhrc.covid_module.Utils;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Child_RefferalFormView_Activity extends AppCompatActivity {

    Context ctx = Child_RefferalFormView_Activity.this;

    EditText et_refferal_ki_waja, et_refferal_hospital, et_tareekh_indraj;
    Button btn_jamaa_kre;
    RelativeLayout rl_navigation_drawer, rl_home;
    ImageView iv_navigation_drawer, iv_home, iv_editform;
    String child_uid, TodayDate, record_date, added_on, TAG = "000555";
    double latitude, longitude;

    // GPSTracker class
    GPSTracker gps;

    ProgressBar pbProgress;
    JSONObject jsonObject;
    Dialog alertDialog;
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid,health_facility_uid;

    Spinner spRefHealthFacility, spReferralReason;
    LinearLayout ll_referal_reason, ll_referal_hospital;

    private int mYear, mMonth, mDay;
    String monthf2, dayf2, yearf2 = "null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_refferal_form);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Child_RefferalFormView_Activity.class));

        record_date = getIntent().getExtras().getString("record_date");
        child_uid = getIntent().getExtras().getString("u_id");
        added_on = getIntent().getExtras().getString("added_on");

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_editform = findViewById(R.id.iv_editform);

        iv_editform.setVisibility(View.VISIBLE);
        iv_navigation_drawer.setVisibility(View.GONE);
        //iv_home.setVisibility(View.GONE);

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences("UserLogin", 0); // 0 - for private mode
            String shared_useruid = prefelse.getString("login_userid", null); // getting String
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


        //  check_gps();

        //EditText
        et_tareekh_indraj = findViewById(R.id.et_tareekh_indraj);
        et_refferal_ki_waja = findViewById(R.id.et_refferal_ki_waja);
        et_refferal_hospital = findViewById(R.id.et_refferal_hospital);

        //Button
        btn_jamaa_kre = findViewById(R.id.submit);
        btn_jamaa_kre.setVisibility(View.GONE);


        //Spinner
        spRefHealthFacility = findViewById(R.id.spRefHealthFacility);
        spReferralReason = findViewById(R.id.spReferralReason);

        //Prgress Bar
        pbProgress = findViewById(R.id.pbProgress);

        //LinearLayout
        ll_referal_hospital = findViewById(R.id.ll_referal_hospital);
        ll_referal_reason = findViewById(R.id.ll_referal_reason);


        spinner_data();


        et_tareekh_indraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTareekhIndraajDialog();
            }
        });



        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Navigation", Toast.LENGTH_SHORT).show();
            }
        });

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(ctx, HomePage_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
            }
        });

        iv_editform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pbProgress.setVisibility(View.VISIBLE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        et_tareekh_indraj.setEnabled(true);
                        et_refferal_ki_waja.setEnabled(true);
                        et_refferal_hospital.setEnabled(true);
                        spRefHealthFacility.setEnabled(true);
                        spReferralReason.setEnabled(true);

                        btn_jamaa_kre.setVisibility(View.VISIBLE);
                        pbProgress.setVisibility(View.GONE);
                        iv_editform.setVisibility(View.GONE);


                    }
                }, 2500);


            }
        });

        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_data(v);
            }
        });

    }


    private void update_data(final View v) {


        if (spRefHealthFacility.getSelectedItemPosition() == 0) {
            final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی صحت مرکز منتخب کریں.", Snackbar.LENGTH_SHORT);
            View mySbView = snackbar.getView();
            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
            TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(15);
            snackbar.setDuration(3000);
            snackbar.show();
            return;
        }


        if (spReferralReason.getSelectedItemPosition() == 0) {
            //  Toast.makeText(getApplicationContext(), "برائے مہربانی ریفرل درج کریں", Toast.LENGTH_LONG).show();
            final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی ریفرل وجہ منتخب کریں.", Snackbar.LENGTH_SHORT);
            View mySbView = snackbar.getView();
            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
            TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(15);
            snackbar.setDuration(3000);
            snackbar.show();

            return;
        }

      /*  if (et_refferal_hospital.getText().toString().isEmpty()) {
            final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی ریفرل اسپتال درج کریں.", Snackbar.LENGTH_SHORT);
            View mySbView = snackbar.getView();
            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
            TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(15);
            snackbar.setDuration(3000);
            snackbar.show();

            return;
        }*/

        if (serviceLocation.showCurrentLocation() == true) {

            latitude = serviceLocation.getLatitude();
            longitude = serviceLocation.getLongitude();

            Log.d("000555", " latitude: " + latitude);
            Log.d("000555", " longitude: " + longitude);
        } else {
            try {
                serviceLocation.doAsynchronousTask.cancel();
            } catch (Exception e) {
            }
            try {
                Lister ls = new Lister(ctx);
                ls.createAndOpenDB();

                String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from REFERAL");

                if (Integer.parseInt(mData[0][2]) > 0) {
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
                Log.d("000258", "Read Referral Error: " + e.getMessage());
            }
        }

        if (spRefHealthFacility.getSelectedItemPosition() == 20) {
            if (et_refferal_hospital.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "برائے مہربانی ریفرل صحت مرکز درج کریں.", Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            et_refferal_hospital.setText("none");
        }

        if (spReferralReason.getSelectedItemPosition() == 18) {
            if (et_refferal_ki_waja.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "برائے مہربانی ریفرل کی وجہ درج کریں.", Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            et_refferal_ki_waja.setText("none");
        }


        try {

            alertDialog = new Dialog(ctx);
            LayoutInflater layout = LayoutInflater.from(ctx);
            final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

            alertDialog.setContentView(dialogView);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.show();

            String update_added_on = String.valueOf(System.currentTimeMillis());

            if (jsonObject.has("lat")) {
                jsonObject.put("lat", "" + String.valueOf(latitude));
                jsonObject.put("lng", "" + String.valueOf(longitude));
                jsonObject.put("referal_record_date", "" + et_tareekh_indraj.getText().toString());
                jsonObject.put("referal_facility", "" + spRefHealthFacility.getSelectedItem());
                jsonObject.put("referal_facility_pos", "" + String.valueOf(spRefHealthFacility.getSelectedItemPosition() - 1));
                jsonObject.put("referal_facility_others", "" + et_refferal_hospital.getText().toString());//spinner
                jsonObject.put("referal_facility_uid", "" +health_facility_uid);
                jsonObject.put("referal_reason", "" + spReferralReason.getSelectedItem());
                jsonObject.put("referal_reason_pos", "" + String.valueOf(spReferralReason.getSelectedItemPosition() - 1));
                jsonObject.put("referal_reason_others", "" + et_refferal_ki_waja.getText().toString());
                jsonObject.put("updated_record_date", "" + TodayDate);//spinner
                jsonObject.put("added_on", "" + update_added_on);


            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();
                        String update_record = "UPDATE REFERAL SET " +
                                "record_data='" + et_tareekh_indraj.getText().toString() + "'," +
                                "data='" + jsonObject.toString() + "'," +
                                "is_synced='" + 0 + "'" +
                                "WHERE member_uid = '" + child_uid + "' AND added_on='" + added_on + "' ";

                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d(TAG, "Data: " + update_record);
                        Log.d(TAG, "Query: " + res.toString());


                        if (res.toString().equalsIgnoreCase("true"))
                        {

                            final Snackbar snackbar = Snackbar.make(v, "ڈیٹا اپڈیٹ ہوگیا ہے.", Snackbar.LENGTH_SHORT);
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
                            snackbar.setDuration(2000);
                            snackbar.show();


                            if (Utils.haveNetworkConnection(ctx) > 0) {

                                sendPostRequest(child_uid, et_tareekh_indraj.getText().toString(), String.valueOf(jsonObject), login_useruid, added_on);
                            } else {
                                // Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            final Snackbar snackbar = Snackbar.make(v, "ڈیٹا اپڈیٹ نہیں ہوا.", Snackbar.LENGTH_SHORT);
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
                            snackbar.setDuration(2000);
                            snackbar.show();

                        }

                       /* if (Utils.haveNetworkConnection(ctx) > 0) {

                            sendPostRequest(child_uid, et_tareekh_indraj.getText().toString(), String.valueOf(jsonObject), login_useruid, added_on);
                        } else {
                            Toast.makeText(ctx, "ڈیٹا اپڈیٹ ہوگیا ہے", Toast.LENGTH_SHORT).show();
                        }*/


                    } catch (Exception e) {
                        alertDialog.dismiss();
                        Log.d("000888", " Error" + e.getMessage());
                        // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    } finally {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog.dismiss();
                                finish();
                            }
                        },2000);

                    }
                }
            }, 2000);

        } catch (Exception e) {
            alertDialog.dismiss();
            //  Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
            Log.d("000888", " Error" + e.getMessage());
        }


    }

    private void sendPostRequest(final String member_uid, final String record_data,
                                 final String data, final String added_by, final String added_on) {

        //String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/family/referrals";
        String url = UrlClass.update_referrals_url;

        Log.d(TAG, "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d(TAG, "Response:    " + response);


                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();
                        String update_record = "UPDATE REFERAL SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + child_uid + "'AND record_data= '" + TodayDate + "'AND added_on= '" + added_on + "' ";

                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d(TAG, "Updated Data: " + update_record);
                        Log.d(TAG, "Updated Query: " + res.toString());

                        Log.d(TAG, "Referal Upated Data: " + update_record);


                        Toast tt  =Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();
                        //    Toast.makeText(ctx, "Data has been updated.", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d(TAG, "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(ctx, "Data has not been updated to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d(TAG, "Catch:  " + e.getMessage());
                    // Toast.makeText(ctx, "Data has been updated incorrectly.", Toast.LENGTH_SHORT).show();
                    Toast tt  =Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT);
                    tt.setGravity(Gravity.CENTER, 0, 0);
                    tt.show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                //    Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast tt  =Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT);
                tt.setGravity(Gravity.CENTER, 0, 0);
                tt.show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("member_id", member_uid);
                params.put("record_data", record_data);
                params.put("data", data);
                params.put("added_by", added_by);
                params.put("added_on", added_on);


                Log.d(TAG, "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d(TAG, "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }


    private void spinner_data() {


        try {

            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            String[][] mData = ls.executeReader("select name,uid from FACILITY order by name ");

            List a = new ArrayList();

            for (int i = 0; i < mData.length; i++) {
                a.add(mData[i][0]);
            }
            a.add("Others");

            Log.d("0000999", "spHealthFacility name " + a);

            String[] facilities = (String[]) a.toArray(new String[0]);

          /*  Log.d("0000999", "spHealthFacility FACILITES " + facilities[0].split("@")[0]);
            Log.d("0000999", "spHealthFacility FACILITES " + facilities[0].split("@")[1]);*/


            final ArrayAdapter<String> adptr_facilities = new ArrayAdapter<String>(this, R.layout.sp_health_facility_layout, facilities);
            adptr_facilities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spRefHealthFacility.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            adptr_facilities,
                            R.layout.sp_health_facility_layout,
                            this));

            spRefHealthFacility.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    if (spRefHealthFacility.getSelectedItemPosition() > 0)
                    {

                        try {
                            Lister ls = new Lister(ctx);
                            ls.createAndOpenDB();

                            String[][] mData = ls.executeReader("select uid from FACILITY where name = '"+spRefHealthFacility.getSelectedItem()+"' order by name ");
                            health_facility_uid = mData[0][0];

                        } catch (Exception e) {
                            Log.d("0000999", "CATCH:" + e.getMessage());
                        }

                        try {
                            if (spRefHealthFacility.getSelectedItem().toString().equalsIgnoreCase("Others")) {
                                ll_referal_hospital.setVisibility(View.VISIBLE);
                            } else {
                                ll_referal_hospital.setVisibility(View.GONE);
                                et_refferal_hospital.getText().clear();
                            }

                        } catch (Exception e) {
                            Log.d("0000999", "CATCH:" + e.getMessage());
                        }

                    }
                    else {
                        Log.d("0000999", "ELSE:" );
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });


            //////////////////////////////SpReferralReason/////////////////////////////

            final ArrayAdapter<CharSequence> adptr_reason = ArrayAdapter.createFromResource(this, R.array.referral_reasons, R.layout.sp_title_referral_reason_layout);
            // adptr_reason.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adptr_reason.setDropDownViewResource(R.layout.sp_title_referral_reason_layout);

            spReferralReason.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            adptr_reason,
                            R.layout.sp_title_referral_reason_layout,
                            this));

            spReferralReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spReferralReason.getSelectedItemPosition() > 0) {
                        try {
                            if (spReferralReason.getSelectedItemPosition() == 18) {
                                ll_referal_reason.setVisibility(View.VISIBLE);
                            } else {
                                ll_referal_reason.setVisibility(View.GONE);
                                et_refferal_ki_waja.getText().clear();
                            }
                        } catch (Exception e) {
                            Log.d("0000999", "CATCH:" + e.getMessage());
                        }
                    }else {
                        Log.d("0000999", "ELSE:" );
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });


        } catch (Exception ex) {
            Log.d("0000999", "Exception spHealthFacility " + ex);
            ex.printStackTrace();
        }

    }

    public void ShowTareekhIndraajDialog() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(Child_RefferalFormView_Activity.this, R.style.DatePickerDialog,
                new DatePickerDialog.OnDateSetListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        if (monthOfYear <= 8) {

                            monthf2 = "0" + String.valueOf(monthOfYear + 1);
                        } else {
                            monthf2 = String.valueOf(monthOfYear + 1);
                        }

                        if (dayOfMonth <= 9) {
                            dayf2 = "0" + String.valueOf(dayOfMonth);
                        } else {
                            dayf2 = String.valueOf(dayOfMonth);
                        }
                        yearf2 = String.valueOf(year);

                        et_tareekh_indraj.setText(yearf2 + "-" + monthf2 + "-" + dayf2);


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();


    }


    @Override
    protected void onResume() {
        super.onResume();

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            String mData[][] = ls.executeReader("Select data from REFERAL where member_uid = '" + child_uid + "' AND record_data = '" + record_date + "' AND added_on = '" + added_on + "'");

            Log.d("000999", "Data: " + mData[0][0]);


            et_tareekh_indraj.setEnabled(false);
            et_refferal_ki_waja.setEnabled(false);
            et_refferal_hospital.setEnabled(false);
            spRefHealthFacility.setEnabled(false);
            spReferralReason.setEnabled(false);


            String json = mData[0][0];
            jsonObject = new JSONObject(json);

            if (jsonObject.has("referal_record_date")) {
                et_tareekh_indraj.setText(jsonObject.getString("referal_record_date"));
                spRefHealthFacility.setSelection(Integer.parseInt(jsonObject.getString("referal_facility_pos")) + 1);
                spReferralReason.setSelection(Integer.parseInt(jsonObject.getString("referal_reason_pos")) + 1);
                health_facility_uid = jsonObject.getString("referal_facility_uid");

                try{
                if (spReferralReason.getSelectedItemPosition() == 18) {
                    ll_referal_reason.setVisibility(View.VISIBLE);
                    et_refferal_ki_waja.setText((jsonObject.getString("referal_reason_others")));
                    Log.d("000999", "IF 1");
                } else {
                    Log.d("000999", "ELS IF 2 ");
                    ll_referal_reason.setVisibility(View.GONE);
                }
                }catch (Exception e) {
                    Log.d("000999", "Err: " +e.getMessage());
                }

                try{
                if (jsonObject.getString("referal_facility").equalsIgnoreCase("Others")) {
                    ll_referal_hospital.setVisibility(View.VISIBLE);
                    et_refferal_hospital.setText((jsonObject.getString("referal_facility_others")));
                    Log.d("000999", "IF 3");

                } else {
                    ll_referal_hospital.setVisibility(View.GONE);
                    Log.d("000999", "ELSE 3");
                }
            }catch (Exception e) {
                    Log.d("000999", "Error 01: " +e.getMessage());
                }
            } else {
                et_tareekh_indraj.setText("-");
                ll_referal_hospital.setVisibility(View.VISIBLE);
                ll_referal_reason.setVisibility(View.VISIBLE);
                et_refferal_hospital.setText((jsonObject.getString("referal_facility")));
                et_refferal_ki_waja.setText((jsonObject.getString("referal_reason")));
            }

            ls.closeDB();

        } catch (Exception e) {
            Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
            Log.d("000999", " Error: " + e.getMessage());
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
