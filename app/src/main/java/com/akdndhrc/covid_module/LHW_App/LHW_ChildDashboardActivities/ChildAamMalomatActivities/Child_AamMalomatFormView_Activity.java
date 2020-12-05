package com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildAamMalomatActivities;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import static com.akdndhrc.covid_module.R.string.GPSonAlert;


public class Child_AamMalomatFormView_Activity extends AppCompatActivity {

    Context ctx = Child_AamMalomatFormView_Activity.this;

    EditText et_tareekh_indraj;
    CheckBox checkbox_haan_1, checkbox_nahi_1, checkbox_haan_2, checkbox_nahi_2, checkbox_haan_3, checkbox_nahi_3, checkbox_haan_4, checkbox_nahi_4,
            checkbox_2_kilo_gram_sy_kum, checkbox_2_kilo_gram_sy_zayada, checkbox_achi, checkbox_kharab, checkbox_100_sy_kum, checkbox_100_sy_zayada, checkbox_60_sy_kum,
            checkbox_60_sy_zayada, checkbox_haan_9, checkbox_nahi_9, checkbox_haan_10, checkbox_nahi_10, checkbox_haan_12, checkbox_nahi_12,
            checkbox_haan_13, checkbox_nahi_13, checkbox_normal_11, checkbox_neela_11, checkbox_peela_11;

    Button btn_jamaa_kre;
    ImageView iv_navigation_drawer, iv_home, iv_editform;
    double latitude;
    double longitude;
    // GPSTracker class
    GPSTracker gps;
    String child_uid, added_on, TAG = "000555";
    String TodayDate, record_date;

    ProgressBar pbProgress;
    JSONObject jsonObject;
    Dialog alertDialog;
    String sib_lt_2, sbl_die_lt5, mother_die, father_atic, brith_weight, birth_cond, temp, heart_beat, naro_obs, naro_tratment, skin_color, reproductive_waste, birth_record;
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_aam_malomat_form);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Child_AamMalomatFormView_Activity.class));

        child_uid = getIntent().getExtras().getString("u_id");
        record_date = getIntent().getExtras().getString("record_date");
        added_on = getIntent().getExtras().getString("added_on");

        Log.d("record_date", record_date);


        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences("UserLogin", 0); // 0 - for private mode
            String shared_useruid = prefelse.getString((R.string.loginUserIDEng), null); // getting String
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


        // check_gps();

        //EditTExt
        et_tareekh_indraj = findViewById(R.id.et_tareekh_indraj);

        //Button
        btn_jamaa_kre = findViewById(R.id.submit);

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_editform = findViewById(R.id.iv_editform);

        iv_navigation_drawer.setVisibility(View.GONE);
        //iv_home.setVisibility(View.GONE);
        iv_editform.setVisibility(View.VISIBLE);

        //Progress Bar
        pbProgress = findViewById(R.id.pbProgress);

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

        //Checkbox
        checkbox_haan_1 = findViewById(R.id.checkbox_haan_1);
        checkbox_nahi_1 = findViewById(R.id.checkbox_nahi_1);
        checkbox_haan_2 = findViewById(R.id.checkbox_haan_2);
        checkbox_nahi_2 = findViewById(R.id.checkbox_nahi_2);
        checkbox_haan_3 = findViewById(R.id.checkbox_haan_3);
        checkbox_nahi_3 = findViewById(R.id.checkbox_nahi_3);
        checkbox_haan_4 = findViewById(R.id.checkbox_haan_4);
        checkbox_nahi_4 = findViewById(R.id.checkbox_nahi_4);
        checkbox_2_kilo_gram_sy_kum = findViewById(R.id.checkbox_2_kilo_gram_sy_kum);
        checkbox_2_kilo_gram_sy_zayada = findViewById(R.id.checkbox_2_kilo_gram_sy_zayada);
        checkbox_achi = findViewById(R.id.checkbox_achi);
        checkbox_kharab = findViewById(R.id.checkbox_kharab);
        checkbox_100_sy_kum = findViewById(R.id.checkbox_100_sy_kum);
        checkbox_100_sy_zayada = findViewById(R.id.checkbox_100_sy_zayada);
        checkbox_60_sy_kum = findViewById(R.id.checkbox_60_sy_kum);
        checkbox_60_sy_zayada = findViewById(R.id.checkbox_60_sy_zayada);
        checkbox_haan_9 = findViewById(R.id.checkbox_haan_9);
        checkbox_nahi_9 = findViewById(R.id.checkbox_nahi_9);
        checkbox_haan_10 = findViewById(R.id.checkbox_haan_10);
        checkbox_nahi_10 = findViewById(R.id.checkbox_nahi_10);
        checkbox_normal_11 = findViewById(R.id.checkbox_normal_11);
        checkbox_neela_11 = findViewById(R.id.checkbox_neela_11);
        checkbox_peela_11 = findViewById(R.id.checkbox_peela_11);

        checkbox_haan_12 = findViewById(R.id.checkbox_haan_12);
        checkbox_nahi_12 = findViewById(R.id.checkbox_nahi_12);
        checkbox_haan_13 = findViewById(R.id.checkbox_haan_13);
        checkbox_nahi_13 = findViewById(R.id.checkbox_nahi_13);

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(ctx, HomePage_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
            }
        });

        checkbox_haan_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_1.isChecked()) {
                    sib_lt_2 = "1";

                    Log.d("000333", "sib_lt_2: " + sib_lt_2);
                }

                checkbox_nahi_1.setChecked(false);
            }
        });
        checkbox_nahi_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_1.isChecked()) {
                    sib_lt_2 = "0";

                    Log.d("000333", "sib_lt_2: " + sib_lt_2);
                }
                checkbox_haan_1.setChecked(false);
            }
        });

        checkbox_haan_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_2.isChecked()) {
                    sbl_die_lt5 = "1";

                    Log.d("000333", "sib_lt_5: " + sbl_die_lt5);
                }
                checkbox_nahi_2.setChecked(false);

            }
        });
        checkbox_nahi_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_2.isChecked()) {
                    sbl_die_lt5 = "0";
                    Log.d("000333", "sib_lt5: " + sbl_die_lt5);
                }
                checkbox_haan_2.setChecked(false);
            }
        });

        checkbox_haan_3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_3.isChecked()) {
                    mother_die = "1";
                    Log.d("000333", "mother_die: " + mother_die);
                }
                checkbox_nahi_3.setChecked(false);

            }
        });
        checkbox_nahi_3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_3.isChecked()) {
                    mother_die = "0";
                    Log.d("000333", "mother_die: " + mother_die);
                }
                checkbox_haan_3.setChecked(false);
            }
        });

        checkbox_haan_4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_4.isChecked()) {
                    father_atic = "1";
                    Log.d("000333", "father_atic: " + father_atic);
                }
                checkbox_nahi_4.setChecked(false);
            }
        });
        checkbox_nahi_4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_4.isChecked()) {
                    father_atic = "0";
                    Log.d("000333", "father_atic: " + father_atic);
                }
                checkbox_haan_4.setChecked(false);
            }
        });

        checkbox_2_kilo_gram_sy_kum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_2_kilo_gram_sy_kum.isChecked()) {
                    brith_weight = "1";

                    Log.d("000333", "brith_weight: " + brith_weight);
                }
                checkbox_2_kilo_gram_sy_zayada.setChecked(false);
            }
        });
        checkbox_2_kilo_gram_sy_zayada.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_2_kilo_gram_sy_zayada.isChecked()) {
                    brith_weight = "0";
                    Log.d("000333", "brith_weight: " + brith_weight);
                }
                checkbox_2_kilo_gram_sy_kum.setChecked(false);
            }
        });

        checkbox_achi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_achi.isChecked()) {
                    birth_cond = "1";
                    Log.d("000333", "birth_cond: " + birth_cond);
                }
                checkbox_kharab.setChecked(false);
            }
        });
        checkbox_kharab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_kharab.isChecked()) {
                    birth_cond = "0";
                    Log.d("000333", "birth_cond: " + birth_cond);
                }
                checkbox_achi.setChecked(false);
            }
        });

        checkbox_100_sy_kum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_100_sy_kum.isChecked()) {
                    temp = "1";
                    Log.d("000333", "temp: " + temp);
                }
                checkbox_100_sy_zayada.setChecked(false);
            }
        });
        checkbox_100_sy_zayada.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_100_sy_zayada.isChecked()) {
                    temp = "0";
                    Log.d("000333", "temp: " + temp);
                }
                checkbox_100_sy_kum.setChecked(false);
            }
        });

        checkbox_60_sy_kum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_60_sy_kum.isChecked()) {
                    heart_beat = "1";
                    Log.d("000333", "heart_beat: " + heart_beat);
                }
                checkbox_60_sy_zayada.setChecked(false);
            }
        });
        checkbox_60_sy_zayada.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_60_sy_zayada.isChecked()) {
                    heart_beat = "0";
                    Log.d("000333", "heart_beat: " + heart_beat);
                }
                checkbox_60_sy_kum.setChecked(false);
            }
        });

        checkbox_haan_9.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_9.isChecked()) {
                    naro_obs = "1";
                    Log.d("000333", "naro_obs: " + naro_obs);
                }
                checkbox_nahi_9.setChecked(false);
            }
        });
        checkbox_nahi_9.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_9.isChecked()) {
                    naro_obs = "0";
                    Log.d("000333", "naro_obs: " + naro_obs);
                }
                checkbox_haan_9.setChecked(false);
            }
        });

        checkbox_haan_10.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_10.isChecked()) {
                    naro_tratment = "1";
                    Log.d("000333", "naro_tratment: " + naro_tratment);
                }
                checkbox_nahi_10.setChecked(false);
            }
        });
        checkbox_nahi_10.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_10.isChecked()) {
                    naro_tratment = "0";
                    Log.d("000333", "naro_tratment: " + naro_tratment);
                }
                checkbox_haan_10.setChecked(false);
            }
        });

        checkbox_normal_11.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_normal_11.isChecked()) {
                    skin_color = "0";
                    Log.d("000333", "skin_color: " + skin_color);
                }
                checkbox_neela_11.setChecked(false);
                checkbox_peela_11.setChecked(false);
            }
        });
        checkbox_neela_11.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_neela_11.isChecked()) {
                    skin_color = "1";
                    Log.d("000333", "skin_color: " + skin_color);
                }
                checkbox_peela_11.setChecked(false);
                checkbox_normal_11.setChecked(false);
            }
        });
        checkbox_peela_11.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_peela_11.isChecked()) {
                    skin_color = "2";
                    Log.d("000333", "skin_color: " + skin_color);
                }
                checkbox_neela_11.setChecked(false);
                checkbox_normal_11.setChecked(false);
            }
        });

        checkbox_haan_12.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_12.isChecked()) {
                    reproductive_waste = "1";
                    Log.d("000333", "reproductive_waste: " + reproductive_waste);
                }
                checkbox_nahi_12.setChecked(false);
            }
        });
        checkbox_nahi_12.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_12.isChecked()) {
                    reproductive_waste = "0";
                    Log.d("000333", "reproductive_waste: " + reproductive_waste);
                }
                checkbox_haan_12.setChecked(false);
            }
        });

        checkbox_haan_13.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_13.isChecked()) {
                    birth_record = "1";
                    Log.d("000333", "birth_record: " + birth_record);
                }
                checkbox_nahi_13.setChecked(false);
            }
        });
        checkbox_nahi_13.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_13.isChecked()) {
                    birth_record = "0";
                    Log.d("000333", "birth_record: " + birth_record);
                }
                checkbox_haan_13.setChecked(false);
            }
        });


        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, R.string.nav, Toast.LENGTH_SHORT).show();
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


                        checkbox_haan_1.setEnabled(true);
                        checkbox_nahi_1.setEnabled(true);
                        checkbox_haan_2.setEnabled(true);
                        checkbox_nahi_2.setEnabled(true);
                        checkbox_haan_3.setEnabled(true);
                        checkbox_nahi_3.setEnabled(true);
                        checkbox_haan_4.setEnabled(true);
                        checkbox_nahi_4.setEnabled(true);

                        checkbox_2_kilo_gram_sy_kum.setEnabled(true);
                        checkbox_2_kilo_gram_sy_zayada.setEnabled(true);
                        checkbox_achi.setEnabled(true);
                        checkbox_kharab.setEnabled(true);
                        checkbox_100_sy_kum.setEnabled(true);
                        checkbox_100_sy_zayada.setEnabled(true);
                        checkbox_60_sy_kum.setEnabled(true);
                        checkbox_60_sy_zayada.setEnabled(true);

                        checkbox_haan_9.setEnabled(true);
                        checkbox_nahi_9.setEnabled(true);
                        checkbox_haan_10.setEnabled(true);
                        checkbox_nahi_10.setEnabled(true);
                        checkbox_normal_11.setEnabled(true);
                        checkbox_neela_11.setEnabled(true);
                        checkbox_peela_11.setEnabled(true);
                        checkbox_haan_12.setEnabled(true);
                        checkbox_nahi_12.setEnabled(true);
                        checkbox_haan_13.setEnabled(true);
                        checkbox_nahi_13.setEnabled(true);


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

                String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from CMALOOM");

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
                Log.d("000258", "Read CMALOOM Error: " + e.getMessage());
            }
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


            String cur_added_on = String.valueOf(System.currentTimeMillis());

            if (jsonObject.has("lat")) {
                jsonObject.put("lat", "" + String.valueOf(latitude));
                jsonObject.put("lng", "" + String.valueOf(longitude));
                jsonObject.put("sib_lt_2", "" + sib_lt_2);
                jsonObject.put("sbl_die_lt5", "" + sbl_die_lt5);//spinner
                jsonObject.put("mother_die", "" + mother_die);
                jsonObject.put("father_atic", "" + father_atic);
                jsonObject.put("brith_weight", "" + brith_weight);
                jsonObject.put("birth_cond", "" + birth_cond);
                jsonObject.put("temp", "" + temp);//spinner
                jsonObject.put("heart_beat", "" + heart_beat);
                jsonObject.put("naro_obs", "" + naro_obs);
                jsonObject.put("skin_color", "" + skin_color);
                jsonObject.put("naro_tratment", "" + naro_tratment);
                jsonObject.put("reproductive_waste", "" + reproductive_waste);
                jsonObject.put("birth_record", "" + birth_record);
                jsonObject.put("record_date", "" + et_tareekh_indraj.getText().toString());
                jsonObject.put("added_on", "" + cur_added_on);
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE CMALOOM SET " +
                                "data='" + jsonObject.toString() + "'," +
                                "is_synced='" + 0 + "'" +
                                "WHERE member_uid = '" + child_uid + "' AND added_on='" + added_on + "' ";
                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d(TAG, "Data: " + update_record);
                        Log.d(TAG, "Query: " + res.toString());

                       /* Toast.makeText(ctx, R.string.dataEdited, Toast.LENGTH_SHORT).show();
                        if (Utils.haveNetworkConnection(ctx) > 0) {
                            sendPostRequest(child_uid, et_tareekh_indraj.getText().toString(), String.valueOf(jsonObject), login_useruid, added_on);
                        } else {
                        }*/

                        if (res.toString().equalsIgnoreCase("true")) {
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

                        } else {
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

                            btn_jamaa_kre.setVisibility(View.VISIBLE);
                        }


                    } catch (Exception e) {
                        alertDialog.dismiss();
                        Log.d("000555", " Error" + e.getMessage());
                        // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    } finally {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog.dismiss();
                                finish();
                            }
                        }, 2500);
                    }

                }
            }, 2000);

        } catch (Exception e) {
            Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
            Log.d("000555", " Error" + e.getMessage());
        }

    }

    private void sendPostRequest(final String member_uid, final String record_data,
                                 final String data, final String added_by, final String added_on) {

        //  String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/child/info";
        String url = UrlClass.update_info_url;


        Log.d("000555", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000555", "Response:    " + response);

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE CMALOOM SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + child_uid + "'AND record_data= '" + record_data + "'AND added_on= '" + added_on + "'";

                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d(TAG, "Updated Data: " + update_record);
                        Log.d(TAG, "Updated Query: " + res.toString());

                        Toast tt = Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();

                        //  Toast.makeText(ctx, "Data updated successfuly.", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000555", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        // Toast.makeText(ctx, "Data has not been updated to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d(TAG, "Error: " + e.getMessage());
                    // Toast.makeText(ctx, "Data has been updated incorrectly.", Toast.LENGTH_SHORT).show();
                    Toast tt = Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT);
                    tt.setGravity(Gravity.CENTER, 0, 0);
                    tt.show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "onErrorResponse: " + error.getMessage());
                //Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast tt = Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT);
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
            Toast.makeText(ctx, GPSonAlert, Toast.LENGTH_LONG).show();
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
            Toast.makeText(ctx, GPSonAlert, Toast.LENGTH_LONG).show();
            return;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String mData[][] = ls.executeReader("Select data from CMALOOM where member_uid = '" + child_uid + "' AND record_data = '" + record_date + "' AND added_on = '" + added_on + "'");


            Log.d("000999", "1: " + mData[0][0]);


            String json = mData[0][0];
            jsonObject = new JSONObject(json);


            et_tareekh_indraj.setText((jsonObject.getString("record_date")));


            if (jsonObject.getString("sib_lt_2").equalsIgnoreCase("1")) {
                sib_lt_2 = jsonObject.getString("sib_lt_2");
                checkbox_haan_1.setChecked(true);
            } else if (jsonObject.getString("sib_lt_2").equalsIgnoreCase("0")) {
                checkbox_nahi_1.setChecked(true);
                sib_lt_2 = jsonObject.getString("sib_lt_2");
            } else {

            }

            if (jsonObject.getString("sbl_die_lt5").equalsIgnoreCase("1")) {
                sbl_die_lt5 = jsonObject.getString("sbl_die_lt5");
                checkbox_haan_2.setChecked(true);
            } else if (jsonObject.getString("sbl_die_lt5").equalsIgnoreCase("0")) {
                checkbox_nahi_2.setChecked(true);
                sbl_die_lt5 = jsonObject.getString("sbl_die_lt5");
            } else {

            }

            if (jsonObject.getString("mother_die").equalsIgnoreCase("1")) {
                mother_die = jsonObject.getString("mother_die");
                checkbox_haan_3.setChecked(true);
            } else if (jsonObject.getString("mother_die").equalsIgnoreCase("0")) {
                mother_die = jsonObject.getString("mother_die");
                checkbox_nahi_3.setChecked(true);
            } else {

            }

            if (jsonObject.getString("father_atic").equalsIgnoreCase("1")) {
                father_atic = jsonObject.getString("father_atic");
                checkbox_haan_4.setChecked(true);
            } else if (jsonObject.getString("father_atic").equalsIgnoreCase("0")) {
                father_atic = jsonObject.getString("father_atic");
                checkbox_nahi_4.setChecked(true);
            } else {

            }

            if (jsonObject.getString("brith_weight").equalsIgnoreCase("1")) {
                brith_weight = jsonObject.getString("brith_weight");
                checkbox_2_kilo_gram_sy_kum.setChecked(true);
            } else if (jsonObject.getString("brith_weight").equalsIgnoreCase("0")) {
                brith_weight = jsonObject.getString("brith_weight");
                checkbox_2_kilo_gram_sy_zayada.setChecked(true);
            } else {

            }

            if (jsonObject.getString("birth_cond").equalsIgnoreCase("1")) {
                birth_cond = jsonObject.getString("birth_cond");
                checkbox_achi.setChecked(true);
            } else if (jsonObject.getString("birth_cond").equalsIgnoreCase("0")) {
                birth_cond = jsonObject.getString("birth_cond");
                checkbox_kharab.setChecked(true);
            } else {

            }

            if (jsonObject.getString("temp").equalsIgnoreCase("1")) {
                temp = jsonObject.getString("temp");
                checkbox_100_sy_kum.setChecked(true);
            } else if (jsonObject.getString("temp").equalsIgnoreCase("0")) {
                temp = jsonObject.getString("temp");
                checkbox_100_sy_zayada.setChecked(true);
            } else {

            }

            if (jsonObject.getString("heart_beat").equalsIgnoreCase("1")) {
                heart_beat = jsonObject.getString("heart_beat");
                checkbox_60_sy_kum.setChecked(true);
            } else if (jsonObject.getString("heart_beat").equalsIgnoreCase("0")) {
                heart_beat = jsonObject.getString("heart_beat");
                checkbox_60_sy_zayada.setChecked(true);
            } else {

            }

            if (jsonObject.getString("naro_obs").equalsIgnoreCase("1")) {
                naro_obs = jsonObject.getString("naro_obs");
                checkbox_haan_9.setChecked(true);
            } else if (jsonObject.getString("naro_obs").equalsIgnoreCase("0")) {
                naro_obs = jsonObject.getString("naro_obs");
                checkbox_nahi_9.setChecked(true);
            } else {

            }

            if (jsonObject.getString("naro_tratment").equalsIgnoreCase("1")) {
                naro_tratment = jsonObject.getString("naro_tratment");
                checkbox_haan_10.setChecked(true);
            } else if (jsonObject.getString("naro_tratment").equalsIgnoreCase("0")) {
                naro_tratment = jsonObject.getString("naro_tratment");
                checkbox_nahi_10.setChecked(true);
            } else {

            }

            if (jsonObject.getString("skin_color").equalsIgnoreCase("0")) {
                skin_color = jsonObject.getString("skin_color");
                checkbox_normal_11.setChecked(true);
            } else if (jsonObject.getString("skin_color").equalsIgnoreCase("1")) {
                skin_color = jsonObject.getString("skin_color");
                checkbox_neela_11.setChecked(true);
            } else if (jsonObject.getString("skin_color").equalsIgnoreCase("2")) {
                skin_color = jsonObject.getString("skin_color");
                checkbox_peela_11.setChecked(true);
            } else {

            }

            if (jsonObject.getString("reproductive_waste").equalsIgnoreCase("1")) {
                reproductive_waste = jsonObject.getString("reproductive_waste");
                checkbox_haan_12.setChecked(true);
            } else if (jsonObject.getString("reproductive_waste").equalsIgnoreCase("0")) {
                reproductive_waste = jsonObject.getString("reproductive_waste");
                checkbox_nahi_12.setChecked(true);
            } else {

            }

            if (jsonObject.getString("birth_record").equalsIgnoreCase("1")) {
                checkbox_haan_13.setChecked(true);
                birth_record = jsonObject.getString("birth_record");
            } else if (jsonObject.getString("birth_record").equalsIgnoreCase("0")) {
                checkbox_nahi_13.setChecked(true);
                birth_record = jsonObject.getString("birth_record");
            } else {

            }

            ls.closeDB();


            et_tareekh_indraj.setEnabled(false);

            checkbox_haan_1.setEnabled(false);
            checkbox_nahi_1.setEnabled(false);
            checkbox_haan_2.setEnabled(false);
            checkbox_nahi_2.setEnabled(false);
            checkbox_haan_3.setEnabled(false);
            checkbox_nahi_3.setEnabled(false);
            checkbox_haan_4.setEnabled(false);
            checkbox_nahi_4.setEnabled(false);

            checkbox_2_kilo_gram_sy_kum.setEnabled(false);
            checkbox_2_kilo_gram_sy_zayada.setEnabled(false);
            checkbox_achi.setEnabled(false);
            checkbox_kharab.setEnabled(false);
            checkbox_100_sy_kum.setEnabled(false);
            checkbox_100_sy_zayada.setEnabled(false);
            checkbox_60_sy_kum.setEnabled(false);
            checkbox_60_sy_zayada.setEnabled(false);

            checkbox_haan_9.setEnabled(false);
            checkbox_nahi_9.setEnabled(false);
            checkbox_haan_10.setEnabled(false);
            checkbox_nahi_10.setEnabled(false);
            checkbox_normal_11.setEnabled(false);
            checkbox_neela_11.setEnabled(false);
            checkbox_peela_11.setEnabled(false);
            checkbox_haan_12.setEnabled(false);
            checkbox_nahi_12.setEnabled(false);
            checkbox_haan_13.setEnabled(false);
            checkbox_nahi_13.setEnabled(false);

            btn_jamaa_kre.setVisibility(View.GONE);


        } catch (Exception e) {
            Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
            Log.d("000555", " Error" + e.getMessage());
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
