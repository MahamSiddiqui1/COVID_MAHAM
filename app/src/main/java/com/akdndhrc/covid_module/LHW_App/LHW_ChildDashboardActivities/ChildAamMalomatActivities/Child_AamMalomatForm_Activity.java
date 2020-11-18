package com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildAamMalomatActivities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.AppController;
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


public class Child_AamMalomatForm_Activity extends AppCompatActivity {

    Context ctx = Child_AamMalomatForm_Activity.this;

    EditText et_tareekh_indraj;
    CheckBox checkbox_haan_1, checkbox_nahi_1, checkbox_haan_2, checkbox_nahi_2, checkbox_haan_3, checkbox_nahi_3, checkbox_haan_4, checkbox_nahi_4,
            checkbox_2_kilo_gram_sy_kum, checkbox_2_kilo_gram_sy_zayada, checkbox_achi, checkbox_kharab, checkbox_100_sy_kum, checkbox_100_sy_zayada, checkbox_60_sy_kum,
            checkbox_60_sy_zayada, checkbox_haan_9, checkbox_nahi_9, checkbox_haan_10, checkbox_nahi_10, checkbox_haan_12, checkbox_nahi_12,
            checkbox_haan_13, checkbox_nahi_13, checkbox_normal_11, checkbox_neela_11, checkbox_peela_11;

    Button btn_jamaa_kre;
    ImageView iv_navigation_drawer, iv_home;
    double latitude;
    double longitude;
    // GPSTracker class
    GPSTracker gps;
    String child_uid;
    String TodayDate;
    String sib_lt_2, sbl_die_lt5, mother_die, father_atic, brith_weight, birth_cond, temp, heart_beat, naro_obs, naro_tratment, skin_color, reproductive_waste, birth_record;


    private int mYear, mMonth, mDay;
    int date_for_condition = 0;
    int month_for_condition = 0;

    public String hold_age_date_condition = "fromage";
    String monthf2, dayf2, yearf2 = "null", TAG = "000555";
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid;
    long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_aam_malomat_form);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Child_AamMalomatForm_Activity.class));

        child_uid = getIntent().getExtras().getString("u_id");

        //  check_gps();


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


        //EditTExt
        et_tareekh_indraj = findViewById(R.id.et_tareekh_indraj);
        et_tareekh_indraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTareekhIndraajDialog();
            }
        });
        //Button
        btn_jamaa_kre = findViewById(R.id.submit);

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);

        iv_navigation_drawer.setVisibility(View.GONE);
        // iv_home.setVisibility(View.GONE);


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

        checkbox_haan_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_1.isChecked()) {
                    sib_lt_2 = "1";
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
                }
                checkbox_haan_13.setChecked(false);
            }
        });

        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Nav", Toast.LENGTH_SHORT).show();
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


        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (et_tareekh_indraj.getText().toString().length() < 1) {

                    final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی تاریخ اندراج منتخب کریں.", Snackbar.LENGTH_SHORT);
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

                        String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from CMALOOM");

                        if (Integer.parseInt(mData[0][2]) >  0 ) {
                            JSONObject jsonObject = new JSONObject(mData[0][1]);
                            Log.d("000258", "  Last Latitude: " + jsonObject.getString("lat"));
                            Log.d("000258", "  Last Longitude: " + jsonObject.getString("lng"));

                            latitude = Double.parseDouble(jsonObject.getString("lat"));
                            longitude = Double.parseDouble(jsonObject.getString("lng"));

                            Toast.makeText(ctx, "Data GPS", Toast.LENGTH_SHORT).show();
                        } else {
                            latitude = Double.parseDouble("0.0");
                            longitude = Double.parseDouble("0.0");
                            Toast.makeText(ctx, "Not Data GPS", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.d("000258", "Read CMALOOM Error: " + e.getMessage());
                    }
                }


                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Log.d("000555", " mLastClickTime: " + mLastClickTime);

                try {

                    Lister ls = new Lister(Child_AamMalomatForm_Activity.this);
                  ls.createAndOpenDB();

                    // et_refferal_ki_waja = findViewById(R.id.et_refferal_ki_waja);
                    // et_refferal_hospital = findViewById(R.id.et_refferal_hospital);

                    JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("sib_lt_2", "" + sib_lt_2);
                    jobj.put("sbl_die_lt5", "" + sbl_die_lt5);//spinner
                    jobj.put("mother_die", "" + mother_die);
                    jobj.put("father_atic", "" + father_atic);
                    jobj.put("brith_weight", "" + brith_weight);
                    jobj.put("birth_cond", "" + birth_cond);
                    jobj.put("temp", "" + temp);//spinner
                    jobj.put("heart_beat", "" + heart_beat);
                    jobj.put("naro_obs", "" + naro_obs);
                    jobj.put("skin_color", "" + skin_color);
                    jobj.put("naro_tratment", "" + naro_tratment);
                    jobj.put("reproductive_waste", "" + reproductive_waste);
                    jobj.put("birth_record", "" + birth_record);
                    jobj.put("record_date", "" + et_tareekh_indraj.getText().toString());
                    jobj.put("added_on", "" + "null");

                    // jobjMain.put("data", jobj);

                    String added_on = String.valueOf(System.currentTimeMillis());

                    String ans1 = "insert into CMALOOM (member_uid, record_data, data,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + child_uid + "'," +
                            "'" + et_tareekh_indraj.getText().toString() + "'," +
                            "'" + jobj + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d(TAG, "Data: " + ans1);
                    Log.d(TAG, "Query: " + res.toString());


                    if (res.toString().equalsIgnoreCase("true"))
                    {

                        final Snackbar snackbar = Snackbar.make(v, "ڈیٹا جمع ہوگیا ہے.", Snackbar.LENGTH_SHORT);
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

                            sendPostRequest(child_uid, et_tareekh_indraj.getText().toString(), String.valueOf(jobj), login_useruid, added_on);
                        } else {
                            // Toast.makeText(ctx, "ڈیٹا جمع ہوگیا ہے", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        final Snackbar snackbar = Snackbar.make(v, "ڈیٹا جمع نہیں ہوا.", Snackbar.LENGTH_SHORT);
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


                   /* Toast.makeText(ctx, "ڈیٹا جمع ہوگیا ہے", Toast.LENGTH_SHORT).show();
                    if (Utils.haveNetworkConnection(ctx) > 0) {
                        sendPostRequest(child_uid, et_tareekh_indraj.getText().toString(), String.valueOf(jobj), login_useruid, added_on);
                    } else {
                      /*  View toastView = getLayoutInflater().inflate(R.layout.custom_toast_view_layout, null);
                        // Initiate the Toast instance.
                        Toast toast = new Toast(getApplicationContext());
                        // Set custom view in toast.
                        toast.setView(toastView);

                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 50,50);
                        toast.show();*/
                   /* }*/
                    //  Toast.makeText(getApplicationContext(),String.valueOf(res)+String.valueOf(ans1),Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.d(TAG, "catch: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                finally {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            finish();
                        }
                    },2500);
                }

            }
        });


    }

    private void sendPostRequest(final String member_uid, final String record_data,
                                 final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/child/info";

        Log.d("000555", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000555", "Response:    " + response);

                        Lister ls = new Lister(Child_AamMalomatForm_Activity.this);
                      ls.createAndOpenDB();

                        String update_record = "UPDATE CMALOOM SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + child_uid + "'AND record_data= '" + et_tareekh_indraj.getText().toString() + "'AND added_on= '" + added_on + "'";

                        ls.executeNonQuery(update_record);
                        //  Toast.makeText(Child_AamMalomatForm_Activity.this, "Data has been saved", Toast.LENGTH_SHORT).show();

                       // Toast.makeText(ctx, "ڈیٹا سنک ہوگیا ہے", Toast.LENGTH_SHORT).show();
                        Toast tt  =Toast.makeText(ctx, "ڈیٹا سنک ہوگیا ہے", Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();

                    } else {
                        Log.d("000555", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        //  Toast.makeText(Child_AamMalomatForm_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, "ڈیٹا سروس پر سینک نہیں ہوا", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d(TAG, "Error: " + e.getMessage());
//                    Toast.makeText(Child_AamMalomatForm_Activity.this, "Data has been sent incorrectly.", Toast.LENGTH_SHORT).show();
                    Toast tt  =Toast.makeText(ctx, "ڈیٹا سینک نہیں ہوا", Toast.LENGTH_SHORT);
                    tt.setGravity(Gravity.CENTER, 0, 0);
                    tt.show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "onErrorResponse: " + error.getMessage());
                //Toast.makeText(Child_AamMalomatForm_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast tt  =Toast.makeText(ctx, "ڈیٹا سینک نہیں ہوا", Toast.LENGTH_SHORT);
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

    public void ShowTareekhIndraajDialog() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(Child_AamMalomatForm_Activity.this, R.style.DatePickerDialog,
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
            Toast.makeText(ctx, "برائے مہربانی جی پی ایس پوزیشن کو آن کریں", Toast.LENGTH_LONG).show();
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

            } else {
                snackbar.dismiss();
                Log.d("000555", "ELSE lat lng: ");
                Log.d("000555", "latitude: " + latitude);
                Log.d("000555", "longitude: " + longitude);

                Toast.makeText(ctx, "جی پی ایس پوزیشن اب آن ہے", Toast.LENGTH_SHORT).show();
            }

        } else {
            gps.showSettingsAlert();
            Toast.makeText(ctx, "برائے مہربانی جی پی ایس پوزیشن کو آن کریں", Toast.LENGTH_LONG).show();
            return;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
