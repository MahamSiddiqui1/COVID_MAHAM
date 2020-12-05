package com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildNashoNumaActivities;

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
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.CustomClass.UrlClass;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.GPSTracker;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.ServiceLocation;
import com.akdndhrc.covid_module.Utils;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rey.material.widget.CheckBox;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Child_NashoNumaFormLive_Activity extends AppCompatActivity {

    Context ctx = Child_NashoNumaFormLive_Activity.this;
    RelativeLayout rl_navigation_drawer, rl_home;
    EditText et_tareekh_indraj, et_qad, et_inch, et_wazan, et_tafseel;
    CheckBox checkbox_6maah_tk_sirf_maa_ka_doodh, checkbox_bottle_ka_doodh, checkbox_izafi_khoraak_thoos_ghiza;
    Button btn_jamaa_kre;
    ImageView iv_navigation_drawer, iv_home, iv_editform;
    String ghiza_value,Sixmaah_tk_sirf_maa_ka_doodh,bottle_ka_doodh;

    double latitude;
    String child_uid, TodayDate;
    double longitude;
    // GPSTracker class
    GPSTracker gps;

    private int mYear, mMonth, mDay;
    int date_for_condition = 0;
    int month_for_condition = 0;

    public String hold_age_date_condition = "fromage";
    String monthf2, dayf2, yearf2 = "null", record_date, added_on, datetime, cur_added_on, TAG = "000555";
    ProgressBar pbProgress;
    JSONObject jsonObject;
    double BMI;
    Dialog alertDialog;
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid,form_tempValue;
    long mLastClickTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_nasho_numa_form);

        record_date = getIntent().getExtras().getString("record_date");
        child_uid = getIntent().getExtras().getString("u_id");
        added_on = getIntent().getExtras().getString("added_on");
        form_tempValue = getIntent().getExtras().getString("temp_value");

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Child_NashoNumaFormLive_Activity.class));

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
            String shared_useruid = prefelse.getString((R.string.loginUserIDEng), null); // getting String
            login_useruid = shared_useruid;
            Log.d("000555", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000555", "Shared Err:" + e.getMessage());
        }


        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_editform = findViewById(R.id.iv_editform);

        iv_editform.setVisibility(View.VISIBLE);
        //iv_home.setVisibility(View.GONE);
        iv_navigation_drawer.setVisibility(View.GONE);


        try {
            serviceLocation = new ServiceLocation(ctx);
            serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            serviceLocation.callAsynchronousTask();
        } catch (Exception e) {
            Log.d("000555", "GPS Service Err:  " + e.getMessage());
        }


        //check_gps();

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
        Calendar c = Calendar.getInstance();
        datetime = dates.format(c.getTime());


        //Prgress Bar
        pbProgress = findViewById(R.id.pbProgress);

        //Edittext
        et_tareekh_indraj = findViewById(R.id.et_tareekh_indraj);
        et_qad = findViewById(R.id.et_qad);
        et_inch = findViewById(R.id.et_inch);
        et_wazan = findViewById(R.id.et_wazan);
        et_tafseel = findViewById(R.id.et_tafseel);

        btn_jamaa_kre = findViewById(R.id.submit);
        btn_jamaa_kre.setVisibility(View.GONE);

        //Checkbox
        checkbox_6maah_tk_sirf_maa_ka_doodh = findViewById(R.id.checkbox_6maah_tk_sirf_maa_ka_doodh);
        checkbox_bottle_ka_doodh = findViewById(R.id.checkbox_bottle_ka_doodh);
        checkbox_izafi_khoraak_thoos_ghiza = findViewById(R.id.checkbox_izafi_khoraak_thoos_ghiza);

        checkbox_6maah_tk_sirf_maa_ka_doodh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_6maah_tk_sirf_maa_ka_doodh.isChecked()) {
                    ghiza_value = "0";
                    checkbox_bottle_ka_doodh.setChecked(false);
                    checkbox_izafi_khoraak_thoos_ghiza.setChecked(false);
                }

            }
        });

        checkbox_bottle_ka_doodh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_bottle_ka_doodh.isChecked()) {
                    ghiza_value = "1";
                    checkbox_6maah_tk_sirf_maa_ka_doodh.setChecked(false);
                    checkbox_izafi_khoraak_thoos_ghiza.setChecked(false);
                }
            }
        });

        checkbox_izafi_khoraak_thoos_ghiza.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_izafi_khoraak_thoos_ghiza.isChecked()) {
                    ghiza_value = "2";
                    checkbox_6maah_tk_sirf_maa_ka_doodh.setChecked(false);
                    checkbox_bottle_ka_doodh.setChecked(false);
                }

            }
        });

        //Button


        iv_editform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pbProgress.setVisibility(View.VISIBLE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        et_qad.setEnabled(true);
                        et_inch.setEnabled(true);
                        et_wazan.setEnabled(true);
                        et_tafseel.setEnabled(true);
                        checkbox_bottle_ka_doodh.setEnabled(true);
                        checkbox_6maah_tk_sirf_maa_ka_doodh.setEnabled(true);
                        checkbox_izafi_khoraak_thoos_ghiza.setEnabled(true);

                        btn_jamaa_kre.setVisibility(View.VISIBLE);
                        pbProgress.setVisibility(View.GONE);
                        iv_editform.setVisibility(View.GONE);


                    }
                }, 2500);


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

        et_tareekh_indraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTareekhNakalMakaniDialog();
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

        if (et_tareekh_indraj.getText().toString().length() < 1) {
            //btn_jamaa_kre.setVisibility(View.GONE);
            final Snackbar snackbar = Snackbar.make(v, R.string.dateOfEntrancePrompt, Snackbar.LENGTH_SHORT);
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

        if (et_wazan.getText().toString().isEmpty()) {
            //btn_jamaa_kre.setVisibility(View.GONE);
            final Snackbar snackbar = Snackbar.make(v, R.string.enterWeightPrompt, Snackbar.LENGTH_SHORT);
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

        if (!checkbox_bottle_ka_doodh.isChecked() && !checkbox_6maah_tk_sirf_maa_ka_doodh.isChecked() && !checkbox_izafi_khoraak_thoos_ghiza.isChecked()) {
            final Snackbar snackbar = Snackbar.make(v, R.string.selectFoodCheckboxPrompt, Snackbar.LENGTH_SHORT);
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
                Lister ls = new Lister(ctx);
                ls.createAndOpenDB();

                String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from CGROWTH");

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
                Log.d("000258", "Read CGROWTH Error: " + e.getMessage());
            }
        }

        btn_jamaa_kre.setVisibility(View.GONE);
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        Log.d("000555", " mLastClickTime: " + mLastClickTime);


        try {

            alertDialog = new Dialog(ctx);
            LayoutInflater layout = LayoutInflater.from(ctx);
            final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

            alertDialog.setContentView(dialogView);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.show();

            try {
                float inch = Float.parseFloat(et_inch.getText().toString());
                float feet = Float.parseFloat(et_qad.getText().toString());
                float Kg = Float.parseFloat(et_wazan.getText().toString());
                double lb = Kg * (2.2);
                double inch_final = (feet * 12) + inch;
                BMI = (lb / (Math.pow(inch_final, 2))) * 703;
                Log.d("000555","Try BMIIIIIIIIIIIIIIIIIII: ");
            } catch (Exception e) {
                Log.d("000555","Catch BMI: " +e.getMessage());
                BMI = 0;
            }

            cur_added_on = String.valueOf(System.currentTimeMillis());

            if (jsonObject.has("lat")) {
                jsonObject.put("lat", "" + String.valueOf(latitude));
                jsonObject.put("lng", "" + String.valueOf(longitude));
                jsonObject.put("qad", "" + et_qad.getText().toString());
                jsonObject.put("inch", "" + et_inch.getText().toString());//spinner
                jsonObject.put("wazan", "" + et_wazan.getText().toString());
                jsonObject.put("ghiza", "" + ghiza_value);
                jsonObject.put("tafseel", "" + et_tafseel.getText().toString());
                jsonObject.put("entrydate", "" + et_tareekh_indraj.getText().toString());
                jsonObject.put("bmi", "" + String.valueOf(BMI));
                jsonObject.put("added_on", "" + cur_added_on);

            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {

                        Lister ls = new Lister(ctx);
                      ls.createAndOpenDB();
                        String update_record = "UPDATE CGROWTH SET " +
                                "data='" + jsonObject.toString() + "'," +
                                "added_by='" + login_useruid + "'," +
                                "is_synced='" + 0 + "'" +
                                "WHERE member_uid = '" + child_uid + "' AND added_on='" + added_on + "' ";

                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d("000555", "Update: " + update_record);
                        Log.d("000555", "Query: " + res.toString());


                        if (res.toString().equalsIgnoreCase("true"))
                        {

                            final Snackbar snackbar = Snackbar.make(v, R.string.dataEdited, Snackbar.LENGTH_SHORT);

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

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    alertDialog.dismiss();
                                    finish();
                                }
                            },2000);
                        }
                        else
                        {

                            final Snackbar snackbar = Snackbar.make(v, R.string.dataNotEdited, Snackbar.LENGTH_SHORT);
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

                            alertDialog.dismiss();
                            btn_jamaa_kre.setVisibility(View.VISIBLE);
                        }

                       /* if (Utils.haveNetworkConnection(ctx) > 0) {

                            sendPostRequest(child_uid, et_tareekh_indraj.getText().toString(), String.valueOf(jsonObject), login_useruid, added_on);
                        } else {
                            Toast.makeText(ctx, R.string.dataEdited, Toast.LENGTH_SHORT).show();
                        }*/


                    } catch (Exception e) {
                        alertDialog.dismiss();
                        Log.d("000555", " Error" + e.getMessage());
                        // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    /*finally {
                        alertDialog.dismiss();
//                        if (form_tempValue.equalsIgnoreCase("0"))
//                        {
//
//                        }
//                        else if (form_tempValue.equalsIgnoreCase("1"))
//                        {
//
//                        }
//                        else{
                            finish();
                       // }
                    }*/
                }
            }, 2000);

        } catch (Exception e) {
            alertDialog.dismiss();
            //  Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
            Log.d("000888", " Error: " + e.getMessage());
        }


    }

    private void sendPostRequest(final String member_uid, final String record_data,
                                 final String data, final String added_by, final String added_on) {


        // String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/child/growth";
        String url = UrlClass.update_growth_url;

        Log.d("000555", "mURL " + url);

        Log.d(TAG, "cur_added_on: " + added_on);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {
                        Log.d("000555", "Response:    " + response);

                        Lister ls = new Lister(Child_NashoNumaFormLive_Activity.this);
                      ls.createAndOpenDB();

                        String update_record = "UPDATE CGROWTH SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + child_uid + "'AND added_on= '" + added_on + "'";
                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d(TAG, "Updated Data: " + update_record);
                        Log.d(TAG, "Updated Query: " + res.toString());

                        Toast tt  =Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();


                    } else {
                        Log.d("000555", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        // Toast.makeText(Child_NashoNumaFormLive_Activity.this, "Data has not been updated to the service.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", "catch:  " + e.getMessage());
                    //Toast.makeText(Child_NashoNumaFormLive_Activity.this, "Data has been updated incorrectly.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "onErrorResponse: " + error.getMessage());
                //Toast.makeText(Child_NashoNumaFormLive_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
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


                Log.d("000555", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000555", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }


    public void ShowTareekhNakalMakaniDialog() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(Child_NashoNumaFormLive_Activity.this, R.style.DatePickerDialog,
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
                        //datetwo.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        // DateTwoOne= "newvaladded";
                        //et_dob.setText(dayf2 + "-" + monthf2 + "-" + yearf2);
                        et_tareekh_indraj.setText(yearf2 + "-" + monthf2 + "-" + dayf2);

                        date_for_condition = Integer.parseInt(dayf2);
                        month_for_condition = Integer.parseInt(monthf2);

                        hold_age_date_condition = "fromdate";


                        Calendar dob = Calendar.getInstance();
                        Calendar today = Calendar.getInstance();

                        dob.set(year, monthOfYear, dayOfMonth);

                        int age = today.get(Calendar.YEAR) - year;

                        Integer ageInt = new Integer(age);
                        String ageS = ageInt.toString();
                        //Toast.makeText(getApplicationContext(),String.valueOf(year)+"major" + ageS +"age",Toast.LENGTH_SHORT).show();
                        //et_age.setText(ageS);
                        //Toast.makeText(getContext(),DateTwoOneval,Toast.LENGTH_LONG).show();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();


    }

    private void check_gps() {

        //GPS\
        gps = new GPSTracker(ctx);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.d("000555", "latitude value: " + latitude);
            Log.d("000555", "longitude value: " + longitude);
        } else {
            gps.showSettingsAlert();
            Toast.makeText(ctx, R.string.GPSonAlert, Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void check_gps2() {

        //GPS\
        gps = new GPSTracker(ctx);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            if (latitude <= 0 && longitude <= 0) {
               /* Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        check_gps2();
                    }
                }, 1500);*/

                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        check_gps2();
                    }
                };

                runnable.run();

                Log.d("000555", "IF lat lng: ");
                Log.d("000555", "latitude: " + latitude);
                Log.d("000555", "longitude: " + longitude);


                return;
            } else {
                snackbar.dismiss();
                Log.d("000555", "ELSE lat lng: ");
                Log.d("000555", "latitude: " + latitude);
                Log.d("000555", "longitude: " + longitude);

                Toast.makeText(ctx, R.string.GPSonMessage, Toast.LENGTH_SHORT).show();
            }

        } else {
            gps.showSettingsAlert();
            Toast.makeText(ctx, R.string.GPSonAlert, Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {

            Lister ls = new Lister(ctx);
          ls.createAndOpenDB();

            String mData[][] = ls.executeReader("Select data from CGROWTH where member_uid = '" + child_uid + "' AND record_data = '" + record_date + "' AND added_on = '" + added_on + "'");

            Log.d("000999", "1: " + mData[0][0]);

            String json = mData[0][0];
            jsonObject = new JSONObject(json);

            // et_tareekh_indraj.setText((jsonObject.getString("entrydate")));
            et_qad.setText((jsonObject.getString("qad")));
            et_inch.setText((jsonObject.getString("inch")));
            et_wazan.setText((jsonObject.getString("wazan")));
            et_tafseel.setText((jsonObject.getString("tafseel")));
            //et_tareekh_indraj.setText((jsonObject.getString("record_date")));
            et_tareekh_indraj.setText((jsonObject.getString("entrydate")));

            if (jsonObject.getString("ghiza").equalsIgnoreCase("0")) {
                checkbox_6maah_tk_sirf_maa_ka_doodh.setChecked(true);
                ghiza_value = "0";
            } else if (jsonObject.getString("ghiza").equalsIgnoreCase("1")) {
                checkbox_bottle_ka_doodh.setChecked(true);
                ghiza_value = "1";
            } else if (jsonObject.getString("ghiza").equalsIgnoreCase("2")) {
                checkbox_izafi_khoraak_thoos_ghiza.setChecked(true);
                ghiza_value = "2";
            }

            ls.closeDB();

            et_qad.setEnabled(false);
            et_inch.setEnabled(false);
            et_wazan.setEnabled(false);
            et_tafseel.setEnabled(false);
            et_tareekh_indraj.setEnabled(false);
            et_tareekh_indraj.setTextColor(ctx.getResources().getColor(R.color.grey_color));
            checkbox_bottle_ka_doodh.setEnabled(false);
            checkbox_6maah_tk_sirf_maa_ka_doodh.setEnabled(false);
            checkbox_izafi_khoraak_thoos_ghiza.setEnabled(false);

        } catch (Exception e) {
            // Toast.makeText(getApplicationContext(), "Error Found", Toast.LENGTH_SHORT).show();
            Log.d("000888", " Error" + e.getMessage());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
