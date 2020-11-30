package com.akdndhrc.covid_module.VAC_App.covid_dashboard.dashboard_side_effects;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.CustomClass.NothingSelectedSpinnerAdapter;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.GPSTracker;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.ServiceLocation;
import com.akdndhrc.covid_module.Utils;
import com.akdndhrc.covid_module.VAC_App.HomePageVacinator_Activity;
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


public class side_effects_Form_Activity extends AppCompatActivity {

    Context ctx = side_effects_Form_Activity.this;

    //  TextView txt_mother_age, txt_mother_name;
    EditText et_tareekh_visit;
            //, et_refferal_ki_waja, et_refferal_hospital,et_value;
    CheckBox checkbox_haan_1, checkbox_nahi_1, checkbox_haan_2, checkbox_nahi_2, checkbox_awareness, checkbox_service_provided;
    Button btn_jamaa_kre;
    ImageView iv_navigation_drawer, iv_home;
    Spinner sp_naya_sabiqa, sp_planning_type;
    double latitude;
    double longitude;
    // GPSTracker class
    GPSTracker gps;
    String mother_uid, TodayDate;

    private int mYear, mMonth, mDay;
    int date_for_condition = 0;
    int month_for_condition = 0;
    public String hold_age_date_condition = "fromage";
    String monthf2, dayf2, yearf2 = "null";
    String khud_muhaiya = "-1";
    String refer = "-1";
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid, services_and_awareness = "-1";
    LinearLayout ll_services_provided;
   // Spinner sp_material,sp_fever,sp_cough,sp_breath,sp_rash,sp_taste,sp_smell,sp_dia;
    RelativeLayout rl_quantity, rl_add, rl_sub;
    TextView tv_count;
    int counter = 0;
    long mLastClickTime = 0;
    Switch se_hyptension,se_syncope_near_syncope,se_anaphylaxis,se_local_redness_pain,se_fatigue,se_headache,se_myalgias,se_diarrhea
            ,se_chills,se_nausea,se_urticaria,se_joint_pain,se_fever,se_shortnessBreath,se_lossTaste,se_lossSmell;
    EditText et_se_fever;
    // ,"se_feverValue">Vue of Fever</string>

    RelativeLayout rl_se_fever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.covid_side_effect_form_ver2);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, side_effects_Form_Activity.class));

        mother_uid = getIntent().getExtras().getString("u_id");

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences("UserLogin", 0); // 0 - for private mode
            String shared_useruid = prefelse.getString("login_userid", null); // getting String
            login_useruid = shared_useruid;
            Log.d("000362", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000362", "Shared Err:" + e.getMessage());
        }

        try {
            serviceLocation = new ServiceLocation(ctx);
            serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            serviceLocation.callAsynchronousTask();
        } catch (Exception e) {
            Log.d("000362", "GPS Service Err:  " + e.getMessage());
        }

        //   check_gps();


        //TextView
        //  txt_mother_name = findViewById(R.id.txt_mother_name);
        // txt_mother_age = findViewById(R.id.txt_mother_age);

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        // iv_home.setVisibility(View.GONE);

        //EDitTExt
        et_tareekh_visit = findViewById(R.id.et_tareekh_visit);
       et_tareekh_visit.setFocusable(false);
        et_tareekh_visit.setText(TodayDate);



        //Spinner
      //  sp_naya_sabiqa = findViewById(R.id.sp_naya_sabiqa);
        sp_planning_type = findViewById(R.id.sp_planning_type);


        //swithc
       se_hyptension= findViewById(R.id.se_hyptension);
       se_syncope_near_syncope= findViewById(R.id.se_syncope_near_syncope);
       se_anaphylaxis= findViewById(R.id.se_anaphylaxis);
       se_local_redness_pain= findViewById(R.id.se_local_redness_pain);
       se_fatigue= findViewById(R.id.se_fatigue);
       se_headache= findViewById(R.id.se_headache);
       se_myalgias= findViewById(R.id.se_myalgias);
       se_diarrhea = findViewById(R.id.se_diarrhea);
       se_chills= findViewById(R.id.se_chills);
       se_nausea= findViewById(R.id.se_nausea);
       se_urticaria= findViewById(R.id.se_urticaria);
       se_joint_pain= findViewById(R.id.se_joint_pain);
       se_fever= findViewById(R.id.se_fever);
       se_shortnessBreath= findViewById(R.id.se_shortnessBreath);
       se_lossTaste= findViewById(R.id.se_lossTaste);
       se_lossSmell= findViewById(R.id.se_lossSmell);

        et_se_fever = findViewById(R.id.et_se_fever);
        rl_se_fever= findViewById(R.id.rl_se_fever);


        se_hyptension.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    se_hyptension.setText("Yes");
                } else {
                    // The toggle is disabled
                    se_hyptension.setText("No");
                }
            }
        });

        se_syncope_near_syncope.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    se_syncope_near_syncope.setText("Yes");
                } else {
                    // The toggle is disabled
                    se_syncope_near_syncope.setText("No");
                }
            }
        });

        se_anaphylaxis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    se_anaphylaxis.setText("Yes");
                } else {
                    // The toggle is disabled
                    se_anaphylaxis.setText("No");
                }
            }
        });

        se_local_redness_pain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    se_local_redness_pain.setText("Yes");
                } else {
                    // The toggle is disabled
                    se_local_redness_pain.setText("No");
                }
            }
        });

        se_fatigue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    se_fatigue.setText("Yes");
                } else {
                    // The toggle is disabled
                    se_fatigue.setText("No");
                }
            }
        });

        se_headache.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    se_headache.setText("Yes");
                } else {
                    // The toggle is disabled
                    se_headache.setText("No");
                }
            }
        });

        se_myalgias.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    se_myalgias.setText("Yes");
                } else {
                    // The toggle is disabled
                    se_myalgias.setText("No");
                }
            }
        });

        se_diarrhea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    se_diarrhea.setText("Yes");
                } else {
                    // The toggle is disabled
                    se_diarrhea.setText("No");
                }
            }
        });

        se_chills.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    se_chills.setText("Yes");
                } else {
                    // The toggle is disabled
                    se_chills.setText("No");
                }
            }
        });

        se_nausea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    se_nausea.setText("Yes");
                } else {
                    // The toggle is disabled
                    se_nausea.setText("No");
                }
            }
        });

        se_urticaria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    se_urticaria.setText("Yes");
                } else {
                    // The toggle is disabled
                    se_urticaria.setText("No");
                }
            }
        });


        se_joint_pain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    se_joint_pain.setText("Yes");
                } else {
                    // The toggle is disabled
                    se_joint_pain.setText("No");
                }
            }
        });

        se_fever.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    se_fever.setText("Yes");
                    et_se_fever.setFocusable(true);
                    et_se_fever.setCursorVisible(true);
                    et_se_fever.setFocusableInTouchMode(true);
                    et_se_fever.setClickable(true);

                    et_se_fever.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_se_fever.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));


                } else {
                    // The toggle is disabled
                    se_fever.setText("No");
                    et_se_fever.setFocusable(false);
                    et_se_fever.setCursorVisible(false);
                    et_se_fever.setFocusableInTouchMode(false);
                    et_se_fever.setClickable(false);
                    et_se_fever.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_se_fever.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });

        se_shortnessBreath.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    se_shortnessBreath.setText("Yes");
                } else {
                    // The toggle is disabled
                    se_shortnessBreath.setText("No");
                }
            }
        });

        se_lossTaste.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    se_lossTaste.setText("Yes");
                } else {
                    // The toggle is disabled
                    se_lossTaste.setText("No");
                }
            }
        });

        se_lossSmell.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    se_lossSmell.setText("Yes");
                } else {
                    // The toggle is disabled
                    se_lossSmell.setText("No");
                }
            }
        });
        //Linear Layout
        ll_services_provided = findViewById(R.id.ll_services_provided);

        //RelativeLayout
        //RelativeLayout
        rl_quantity = findViewById(R.id.rl_quantity);
        rl_add = findViewById(R.id.rl_add);
        rl_sub = findViewById(R.id.rl_sub);

        //TextView
       // tv_count = findViewById(R.id.tv_count);
        //tv_count.setText("" + counter);




        //CheckBox
       /* checkbox_haan_1 = findViewById(R.id.checkbox_haan_1);
        checkbox_nahi_1 = findViewById(R.id.checkbox_nahi_1);
        checkbox_haan_2 = findViewById(R.id.checkbox_haan_2);
        checkbox_nahi_2 = findViewById(R.id.checkbox_nahi_2);

        checkbox_service_provided = findViewById(R.id.checkbox_service_provided);
        checkbox_awareness = findViewById(R.id.checkbox_awareness);
*/
        /*checkbox_service_provided.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_service_provided.isChecked()) {
                    services_and_awareness = "1";

                    ll_services_provided.setVisibility(View.VISIBLE);
                    Log.d("00147", "IF");
                    checkbox_awareness.setChecked(false);
                } else {
                    Log.d("00147", "ELSE");
                    checkbox_awareness.setChecked(false);
                    ll_services_provided.setVisibility(View.GONE);
                }

            }
        });*/
        /*checkbox_awareness.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_awareness.isChecked()) {
                    services_and_awareness = "0";
                    ll_services_provided.setVisibility(View.GONE);
                    rl_quantity.setVisibility(View.GONE);
                    sp_material.setSelection(0);
                    counter = 0;
                   // tv_count.setText("0");
                }

                checkbox_service_provided.setChecked(false);

            }
        });


        checkbox_haan_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_1.isChecked()) {
                    khud_muhaiya = "1";
                }

                checkbox_nahi_1.setChecked(false);
            }
        });
        checkbox_nahi_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_1.isChecked()) {
                    khud_muhaiya = "0";
                }
                checkbox_haan_1.setChecked(false);
            }
        });

        checkbox_haan_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_2.isChecked()) {
                    refer = "1";
                }
                checkbox_nahi_2.setChecked(false);
            }
        });

        checkbox_nahi_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_2.isChecked()) {
                    refer = "0";
                }
                checkbox_haan_2.setChecked(false);
            }
        });
*/

      /*  rl_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter < 100) {
                    counter += 1;
                  //  tv_count.setText("" + counter);

                    Log.d("000362", ": " + counter);
                } else {
                  //  tv_count.setText("" + counter);
                    Log.d("000362", ":: " + counter);
                }
            }
        });
        rl_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter > 0) {
                    counter -= 1;
                   // tv_count.setText("" + counter);
                    Log.d("000362", ": " + counter);
                } else {
                  //  tv_count.setText("" + counter);
                    Log.d("000362", ": " + counter);
                }
            }


        });
*/

        //Button
        btn_jamaa_kre = findViewById(R.id.submit);

        et_tareekh_visit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ShowDateDialouge();
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

                Intent newIntent = new Intent(ctx, HomePageVacinator_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
            }
        });


        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_tareekh_visit.getText().toString().length() < 1) {
                    final Snackbar snackbar = Snackbar.make(v, "Please select a visit date.", Snackbar.LENGTH_SHORT);
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
              /*  if (et_tareekh_visit.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(), "برائے مہربانی تاریخ وزٹ منتخب کریں", Toast.LENGTH_LONG).show();
                    return;
                }*/
                if (serviceLocation.showCurrentLocation() == true) {
                    latitude = serviceLocation.getLatitude();
                    longitude = serviceLocation.getLongitude();
                    Log.d("000362", " latitude: " + latitude);
                    Log.d("000362", " longitude: " + longitude);
                } else {
                    try {
                        serviceLocation.doAsynchronousTask.cancel();
                    } catch (Exception e) {
                    }
                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from COVID_SIDE_EFFECTS");

                        if (Integer.parseInt(mData[0][2]) > 0) {
                            JSONObject jsonObject = new JSONObject(mData[0][1]);
                            Log.d("000362", "  Last Latitude: " + jsonObject.getString("lat"));
                            Log.d("000362", "  Last Longitude: " + jsonObject.getString("lng"));

                            latitude = Double.parseDouble(jsonObject.getString("lat"));
                            longitude = Double.parseDouble(jsonObject.getString("lng"));

                            Toast.makeText(ctx, "Data GPS", Toast.LENGTH_SHORT).show();
                        } else {
                            latitude = Double.parseDouble("0.0");
                            longitude = Double.parseDouble("0.0");
                            Toast.makeText(ctx, "Not Data GPS", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.d("000362", "Read COVID_SIDE_EFFECTS Error: " + e.getMessage());
                    }
                }

                btn_jamaa_kre.setVisibility(View.GONE);
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Log.d("000362", " mLastClickTime: " + mLastClickTime);


                try {

                    Lister ls = new Lister(side_effects_Form_Activity.this);
                    ls.createAndOpenDB();

                    // et_refferal_ki_waja = findViewById(R.id.et_refferal_ki_waja);
                    // et_refferal_hospital = findViewById(R.id.et_refferal_hospital);


                    JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("tareekh_visit", "" + et_tareekh_visit.getText().toString());

                    jobj.put("added_on", "null");


                    String cur_added_on = String.valueOf(System.currentTimeMillis());

                    // jobjMain.put("data", jobj);
                    String ans1 = "insert into COVID_SIDE_EFFECTS (member_uid, record_data, data,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + mother_uid + "'," +
                            "'" + et_tareekh_visit.getText().toString() + "'," +
                            "'" + jobj + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + cur_added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000362", "Data: " + ans1);
                    Log.d("000362", "Query: " + res.toString());


                    if (res.toString().equalsIgnoreCase("true")) {

                        final Snackbar snackbar = Snackbar.make(v, "Data has been collected.", Snackbar.LENGTH_SHORT);
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

                            sendPostRequest(mother_uid, et_tareekh_visit.getText().toString(), String.valueOf(jobj), login_useruid, cur_added_on);
                        } else {
                            //Toast.makeText(ctx, "ڈیٹا جمع ہوگیا ہے", Toast.LENGTH_SHORT).show();
                        }



                        if (services_and_awareness.equalsIgnoreCase("1")) {
                           // insert_into_medicineLog(cur_added_on);
                        } else {
                        }
                    } else {
                        final Snackbar snackbar = Snackbar.make(v, "Data not collected.", Snackbar.LENGTH_SHORT);
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


                    // Toast.makeText(getApplicationContext(),String.valueOf(res)+String.valueOf(ans1),Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.d("000362", "Err: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            finish();
                        }
                    }, 2500);
                }

            }
        });
    }




    private void sendPostRequest(final String member_uid, final String record_data,
                                 final String data, final String added_by, final String added_on) {

        String url = " https://pak.api.teekoplus.akdndhrc.org/sync/save/mother/family-plan";

        Log.d("000362", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {
                        Log.d("000362", "Response:  " + response);


                        Lister ls = new Lister(side_effects_Form_Activity.this);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE COVID_SIDE_EFFECTS SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + mother_uid + "'AND record_data= '" + et_tareekh_visit.getText().toString() + "'AND added_on= '" + added_on + "'";
                        ls.executeNonQuery(update_record);
                        Log.d("000362", "Update Record:  " + update_record);

                        Toast tt  =Toast.makeText(ctx, "Data synced", Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();

                    } else {
                        Log.d("000362", "else ");
                        Toast.makeText(ctx, "Data service not synced", Toast.LENGTH_SHORT).show();
                        //  Toast.makeText(side_effects_Form_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000362", "Err: " + e.getMessage());
                    //Toast.makeText(side_effects_Form_Activity.this, "Data has been sent incorrectly.", Toast.LENGTH_SHORT).show();
                    Toast tt  =Toast.makeText(ctx, "Data not synced", Toast.LENGTH_SHORT);
                    tt.setGravity(Gravity.CENTER, 0, 0);
                    tt.show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000362", "error    " + error.getMessage());
                //Toast.makeText(side_effects_Form_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast tt  =Toast.makeText(ctx, "Data not synced", Toast.LENGTH_SHORT);
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


                Log.d("000362", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000362", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }




    public void ShowDateDialouge() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(side_effects_Form_Activity.this, R.style.DatePickerDialog,
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
                        et_tareekh_visit.setText(yearf2 + "-" + monthf2 + "-" + dayf2);

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
            Log.d("000362", "latitude value: " + latitude);
            Log.d("000362", "longitude value: " + longitude);
        } else {
            gps.showSettingsAlert();
            Toast.makeText(ctx, "Please turn on GPS position", Toast.LENGTH_LONG).show();
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

                Log.d("000362", "IF lat lng: ");
                Log.d("000362", "latitude: " + latitude);
                Log.d("000362", "longitude: " + longitude);


                return;
            } else {
                snackbar.dismiss();
                Log.d("000362", "ELSE lat lng: ");
                Log.d("000362", "latitude: " + latitude);
                Log.d("000362", "longitude: " + longitude);

                Toast.makeText(ctx, "GPS position is now on", Toast.LENGTH_SHORT).show();
            }

        } else {
            gps.showSettingsAlert();
            Toast.makeText(ctx, "Please turn on GPS position", Toast.LENGTH_LONG).show();
            return;
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
