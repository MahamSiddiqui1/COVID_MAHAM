package com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherAamMalomatActivities;

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
import com.akdndhrc.covid_module.ServiceLocation;
import com.akdndhrc.covid_module.Utils;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.akdndhrc.covid_module.R;
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


public class Mother_AamMalomatForm_Activity extends AppCompatActivity {


    Context ctx = Mother_AamMalomatForm_Activity.this;

    ImageView iv_navigation_drawer, iv_home;
    Button btn_jamaa_kre;
    EditText et_tareekh_indraj, et_tafseel_1, et_tafseel_2;
    CheckBox checkbox_haan_1, checkbox_nahi_1, checkbox_haan_2, checkbox_nahi_2, checkbox_haan_3, checkbox_nahi_3, checkbox_haan_4, checkbox_nahi_4, checkbox_haan_5, checkbox_nahi_5,
            checkbox_haan_6, checkbox_nahi_6, checkbox_haan_7, checkbox_nahi_7, checkbox_haan_8, checkbox_nahi_8;
    String mother_uid;
    GPSTracker gps;
    double latitude;
    double longitude;
    String TodayDate;
    String disease_history, mom_on_med, abortion_hist, still_birth, old_preg, highbp_preg, csec, extra_bleed;

    private int mYear, mMonth, mDay;
    int date_for_condition = 0;
    int month_for_condition = 0;
    public String hold_age_date_condition = "fromage";
    String monthf2, dayf2, yearf2 = "null";
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid;

    long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_aam_malomat_form);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Mother_AamMalomatForm_Activity.class));

        mother_uid = getIntent().getExtras().getString("u_id");

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
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

        //EditText
        et_tareekh_indraj = findViewById(R.id.et_tareekh_indraj);
        et_tafseel_1 = findViewById(R.id.et_tafseel_1);
        et_tafseel_2 = findViewById(R.id.et_tafseel_2);

        //CheckBox
        checkbox_haan_1 = findViewById(R.id.checkbox_haan_1);
        checkbox_nahi_1 = findViewById(R.id.checkbox_nahi_1);
        checkbox_haan_2 = findViewById(R.id.checkbox_haan_2);
        checkbox_nahi_2 = findViewById(R.id.checkbox_nahi_2);
        checkbox_haan_3 = findViewById(R.id.checkbox_haan_3);
        checkbox_nahi_3 = findViewById(R.id.checkbox_nahi_3);
        checkbox_haan_4 = findViewById(R.id.checkbox_haan_4);
        checkbox_nahi_4 = findViewById(R.id.checkbox_nahi_4);
        checkbox_haan_5 = findViewById(R.id.checkbox_haan_5);
        checkbox_nahi_5 = findViewById(R.id.checkbox_nahi_5);
        checkbox_haan_6 = findViewById(R.id.checkbox_haan_6);
        checkbox_nahi_6 = findViewById(R.id.checkbox_nahi_6);
        checkbox_haan_7 = findViewById(R.id.checkbox_haan_7);
        checkbox_nahi_7 = findViewById(R.id.checkbox_nahi_7);
        checkbox_haan_8 = findViewById(R.id.checkbox_haan_8);
        checkbox_nahi_8 = findViewById(R.id.checkbox_nahi_8);

        checkbox_haan_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_1.isChecked()) {
                    disease_history = "1";
                }
                checkbox_nahi_1.setChecked(false);

            }
        });
        checkbox_nahi_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_1.isChecked()) {
                    disease_history = "0";
                }
                checkbox_haan_1.setChecked(false);
            }
        });

        checkbox_haan_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_2.isChecked()) {
                    mom_on_med = "1";
                }
                checkbox_nahi_2.setChecked(false);

            }
        });
        checkbox_nahi_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_2.isChecked()) {
                    mom_on_med = "0";
                }
                checkbox_haan_2.setChecked(false);
            }
        });
        checkbox_haan_3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_3.isChecked()) {
                    abortion_hist = "1";
                }
                checkbox_nahi_3.setChecked(false);
            }
        });
        checkbox_nahi_3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_3.isChecked()) {
                    abortion_hist = "0";
                }
                checkbox_haan_3.setChecked(false);
            }
        });
        checkbox_haan_4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_4.isChecked()) {
                    still_birth = "1";
                }
                checkbox_nahi_4.setChecked(false);
            }
        });
        checkbox_nahi_4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_4.isChecked()) {
                    still_birth = "0";
                }
                checkbox_haan_4.setChecked(false);
            }
        });
        checkbox_haan_5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_5.isChecked()) {
                    old_preg = "1";
                }
                checkbox_nahi_5.setChecked(false);
            }
        });
        checkbox_nahi_5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_5.isChecked()) {
                    old_preg = "0";
                }
                checkbox_haan_5.setChecked(false);
            }
        });
        checkbox_haan_6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_6.isChecked()) {
                    highbp_preg = "1";
                }
                checkbox_nahi_6.setChecked(false);
            }
        });
        checkbox_nahi_6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_6.isChecked()) {
                    highbp_preg = "0";
                }
                checkbox_haan_6.setChecked(false);
            }
        });
        checkbox_haan_7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_7.isChecked()) {
                    csec = "1";
                }
                checkbox_nahi_7.setChecked(false);
            }
        });
        checkbox_nahi_7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_7.isChecked()) {
                    csec = "0";
                }
                checkbox_haan_7.setChecked(false);
            }
        });

        checkbox_haan_8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_8.isChecked()) {
                    extra_bleed = "1";
                }
                checkbox_nahi_8.setChecked(false);
            }
        });
        checkbox_nahi_8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_8.isChecked()) {
                    extra_bleed = "0";
                }
                checkbox_haan_8.setChecked(false);
            }
        });

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        //  iv_home.setVisibility(View.GONE);

        //Button
        btn_jamaa_kre = findViewById(R.id.submit);
        et_tareekh_indraj.setFocusable(false);
        et_tareekh_indraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowDateDialoug();
            }
        });


        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, R.string.navigation, Toast.LENGTH_SHORT).show();
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

                        String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from MMALOOM");

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
                        Log.d("000258", "Read MMALOOM Error: " + e.getMessage());
                    }
                }

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Log.d("000555", " mLastClickTime: " + mLastClickTime);


                try {


                    Lister ls = new Lister(Mother_AamMalomatForm_Activity.this);
                  ls.createAndOpenDB();



                    // et_refferal_ki_waja = findViewById(R.id.et_refferal_ki_waja);
                    // et_refferal_hospital = findViewById(R.id.et_refferal_hospital);

                    //Edittext


                    JSONObject jobj = new JSONObject();

                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("tafseel_1", "" + et_tafseel_1.getText().toString());
                    jobj.put("tafseel_2", "" + et_tafseel_2.getText().toString());
                    jobj.put("disease_history", "" + disease_history);//spinner
                    jobj.put("mom_on_med", "" + mom_on_med);
                    //          jobj.put("paitkekiray", ""+et_pait_kai_keeray.getText().toString());
                    jobj.put("abortion_hist", "" + abortion_hist);
                    jobj.put("still_birth", "" + still_birth);
                    //        jobj.put("kharish", ""+et_kharish.getText().toString());
                    jobj.put("old_preg", "" + old_preg);
                    //      jobj.put("chout_zakhm", ""+et_chout_zakham_jalna.getText().toString());
                    jobj.put("highbp_preg", "" + highbp_preg);
                    jobj.put("csec", "" + csec);
                    jobj.put("extra_bleed", "" + extra_bleed);
                    jobj.put("record_enter_date", "" + et_tareekh_indraj.getText().toString());
                    jobj.put("added_on", "null");


                    String cur_added_on = String.valueOf(System.currentTimeMillis());

                    // jobjMain.put("data", jobj);
                    String ans1 = "insert into MMALOOM (member_uid, record_data, data,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + mother_uid + "'," +
                            "'" + et_tareekh_indraj.getText().toString() + "'," +
                            "'" + jobj + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + cur_added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000555", ans1);
                    Log.d("000555", res.toString());

                    if (res.toString().equalsIgnoreCase("true"))
                    {

                        final Snackbar snackbar = Snackbar.make(v, R.string.dataSubmissionMessage, Snackbar.LENGTH_SHORT);
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

                            sendPostRequest(mother_uid, TodayDate, String.valueOf(jobj), login_useruid, cur_added_on);
                        } else {
                          //  Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        final Snackbar snackbar = Snackbar.make(v, R.string.dataSubmissionFailed, Snackbar.LENGTH_SHORT);
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




                    //  Toast.makeText(getApplicationContext(),String.valueOf(res)+String.valueOf(ans1),Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.d("000555", "Err: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {
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

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/mother/info";

        Log.d("000555", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000555", "Response    " + response);


                        Lister ls = new Lister(Mother_AamMalomatForm_Activity.this);
                      ls.createAndOpenDB();

                        String update_record = "UPDATE MMALOOM SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + mother_uid + "'AND added_on= '" + added_on + "'";
                        ls.executeNonQuery(update_record);

                        Toast tt  =Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();

                    } else {
                        Log.d("000555", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        //  Toast.makeText(Mother_AamMalomatForm_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", "Error: " + e.getMessage());
                    // Toast.makeText(Mother_AamMalomatForm_Activity.this, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();
                    Toast tt  =Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT);
                    tt.setGravity(Gravity.CENTER, 0, 0);
                    tt.show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "onErrorResponse: " + error.getMessage());
                // Toast.makeText(Mother_AamMalomatForm_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
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


    public void ShowDateDialoug() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(Mother_AamMalomatForm_Activity.this, R.style.DatePickerDialog,
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
                        // et_umer.setText(ageS);
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
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());

    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
