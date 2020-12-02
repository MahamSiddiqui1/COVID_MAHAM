package com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildHifazitiTeekeyRecordActivities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.Adt_ChildDashboard.Adt_ChildHifazatiTeekeyRecordList_KahiAurSy;
import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
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

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class Child_HifazitiTeekeyKahiAurSyLiHoe_Activity extends AppCompatActivity {

    Context ctx = Child_HifazitiTeekeyKahiAurSyLiHoe_Activity.this;
    TextView txt_age, txt_naam, txt_refuse_vaccine;
    ListView lv;
    RelativeLayout rl_navigation_drawer, rl_home;

    ArrayList<String> arrayListVaccine = new ArrayList<>();
    ArrayList<String> arrayListDate = new ArrayList<>();
    ImageView iv_navigation_drawer, iv_home, iv_close, image_gender;

    Adt_ChildHifazatiTeekeyRecordList_KahiAurSy adt;
    String[][] mData;
    int index_val = 0;

    Button btn_jamaa_kre;

    String child_uid, child_age, child_name, child_gender, vaccine_duedate, vaccine_name;
    String to_make_active = "yes";
    int diffInDays;

    Spinner sp_inside_outside_council;
    double latitude;
    String TodayDate;
    double longitude;
    ServiceLocation serviceLocation;
    String login_useruid;

    EditText et_tareekh_mosool_hoe;
    Spinner sp_vaccine_kaha_farham_hoe;

    Button btn_InFacility, btn_Outreach, btn_MobileVaccine;
    String btn_name, btn_value,village_uid="none";

    String monthf2, dayf2, yearf2 = "null";
    private int mYear, mMonth, mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_hifaziti_teekey_record_list);


        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Child_HifazitiTeekeyKahiAurSyLiHoe_Activity.class));
        Log.d("000266", "RESTARRRRRRRRRRRRR");

        child_uid = getIntent().getExtras().getString("u_id");
        child_name = getIntent().getExtras().getString("child_name");
        //  child_age = getIntent().getExtras().getString("child_age");
        child_gender = getIntent().getExtras().getString("child_gender");
        vaccine_duedate = getIntent().getExtras().getString("vaccine_duedate");
        diffInDays = getIntent().getExtras().getInt("diffInDays");


        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences("UserLogin", 0); // 0 - for private mode
            String shared_useruid = prefelse.getString("login_userid", null); // getting String
            login_useruid = shared_useruid;
            Log.d("000266", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000266", "Shared Err:" + e.getMessage());
        }

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

        try {
            serviceLocation = new ServiceLocation(ctx);
            serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            serviceLocation.callAsynchronousTask();
        } catch (Exception e) {
            Log.d("000266", "GPS Service Err:  " + e.getMessage());
        }


        //ListView
        lv = findViewById(R.id.lv);

        //TextView
        txt_age = findViewById(R.id.txt_age);
        txt_naam = findViewById(R.id.txt_naam);


        // txt_naam.setText(child_name);
        // txt_age.setText(child_age);

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        //  iv_home.setVisibility(View.GONE);
        image_gender = findViewById(R.id.image_gender);

        calculateAge();

        if (child_gender.equalsIgnoreCase("0")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_adult_female_50dp));
                image_gender.setImageTintList(ctx.getResources().getColorStateList(R.color.pink_color));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    image_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                    image_gender.setImageTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
                }
            }
        }


        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("12345", "Errr");

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


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index_val = position;
                Log.d("000522", " POS: " + arrayListVaccine.get(index_val));


                //  if (diffInDays < Integer.parseInt(mData[position][2]) && diffInDays > (Integer.parseInt(mData[position][2]) - 15)) {
                if (diffInDays == Integer.parseInt(mData[position][2])) {
                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String[][] mDataa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "'");
                        if (mDataa != null) {

                            String[][] mData_ref_vac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "' AND type = '3' ");

                            if (mData_ref_vac != null) {
                                Log.d("000522", "Refuse");
                                // Toast.makeText(getApplicationContext(), R.string.refused_vaccine, Toast.LENGTH_SHORT).show();
                                final Snackbar snackbar = Snackbar.make(view, R.string.vaccineDenied, Snackbar.LENGTH_SHORT);
                                View mySbView = snackbar.getView();
                                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                textView.setTextSize(16);
                                textView.setGravity(Gravity.CENTER_VERTICAL);
                                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                }
                                snackbar.setDuration(2500);
                                snackbar.show();
                            } else {
                                Log.d("000522", "Not Ref");
                                //Toast.makeText(getApplicationContext(), R.string.Child_already_vaccinated, Toast.LENGTH_SHORT).show();
                                final Snackbar snackbar = Snackbar.make(view, R.string.thisVaccineApplied, Snackbar.LENGTH_SHORT);
                                View mySbView = snackbar.getView();
                                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                textView.setGravity(Gravity.CENTER_VERTICAL);
                                textView.setTextSize(16);
                                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                }
                                snackbar.setDuration(2500);
                                snackbar.show();
                            }

                        } else {
                            //   Dialog_VaccineKoAnjaamDy();

                            Log.d("000213", "DueDateAAA : " + mData[position][2]);
                            Dialog_VaccineKoAnjaamDy();
                        }

                    } catch (Exception e) {
                        Log.d("000522", "Err:" + e.getMessage());
                        // Toast.makeText(getApplicationContext(), R.string.Child_already_vaccinated, Toast.LENGTH_SHORT).show();
                    }

                } else if (diffInDays < Integer.parseInt(mData[position][2])) {

                    // Toast.makeText(getApplicationContext(), R.string.vaccine_not_yet_active, Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar.make(view, R.string.vaccineNotAct, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(16);
                    textView.setGravity(Gravity.CENTER_VERTICAL);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                    }
                    snackbar.setDuration(2500);
                    snackbar.show();
                }

              /*  else if (diffInDays > Integer.parseInt(mData[position][2])) {

                    Toast.makeText(getApplicationContext(), R.string.vaccine_not_yet_active, Toast.LENGTH_SHORT).show();
                }*/

                else {
                    Log.d("000522", "DAYS DIFF: " + diffInDays);

                    if (mData[position][1].equalsIgnoreCase("BCG"))
                    {

                        try {
                            Lister ls = new Lister(ctx);
                            ls.createAndOpenDB();

                            String[][] mDataa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "'");
                            if (mDataa != null) {

                                String[][] mData_ref_vac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "' AND type = '3' ");

                                if (mData_ref_vac != null) {
                                    Log.d("000522", "Refuse");
                                    // Toast.makeText(getApplicationContext(), R.string.refused_vaccine, Toast.LENGTH_SHORT).show();
                                    final Snackbar snackbar = Snackbar.make(view, R.string.vaccineDenied, Snackbar.LENGTH_SHORT);
                                    View mySbView = snackbar.getView();
                                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.WHITE);
                                    textView.setTextSize(16);
                                    textView.setGravity(Gravity.CENTER_VERTICAL);
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                    }
                                    snackbar.setDuration(2500);
                                    snackbar.show();
                                } else {
                                    Log.d("000522", "Not Ref");
                                    //Toast.makeText(getApplicationContext(), R.string.Child_already_vaccinated, Toast.LENGTH_SHORT).show();
                                    final Snackbar snackbar = Snackbar.make(view, R.string.thisVaccineApplied, Snackbar.LENGTH_SHORT);
                                    View mySbView = snackbar.getView();
                                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.WHITE);
                                    textView.setGravity(Gravity.CENTER_VERTICAL);
                                    textView.setTextSize(16);
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                    }
                                    snackbar.setDuration(2500);
                                    snackbar.show();
                                }

                            } else {
                                Log.d("000522", "PERFORM VACCINE");
                                Dialog_VaccineKoAnjaamDy();
                            }

                        } catch (Exception e) {
                            Log.d("000522", "Err:" + e.getMessage());
                        }

                    }

                    else if (mData[position][1].equalsIgnoreCase("HEP-B"))
                    {
                        try {
                            Lister ls = new Lister(ctx);
                            ls.createAndOpenDB();

                            String[][] mDataa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "'");
                            if (mDataa != null) {

                                String[][] mData_ref_vac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "' AND type = '3' ");

                                if (mData_ref_vac != null) {
                                    Log.d("000522", "Refuse");
                                    // Toast.makeText(getApplicationContext(), R.string.refused_vaccine, Toast.LENGTH_SHORT).show();
                                    final Snackbar snackbar = Snackbar.make(view, R.string.vaccineDenied, Snackbar.LENGTH_SHORT);
                                    View mySbView = snackbar.getView();
                                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.WHITE);
                                    textView.setTextSize(16);
                                    textView.setGravity(Gravity.CENTER_VERTICAL);
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                    }
                                    snackbar.setDuration(2500);
                                    snackbar.show();
                                } else {
                                    Log.d("000522", "Not Ref");
                                    //Toast.makeText(getApplicationContext(), R.string.Child_already_vaccinated, Toast.LENGTH_SHORT).show();
                                    final Snackbar snackbar = Snackbar.make(view, R.string.thisVaccineApplied, Snackbar.LENGTH_SHORT);
                                    View mySbView = snackbar.getView();
                                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.WHITE);
                                    textView.setGravity(Gravity.CENTER_VERTICAL);
                                    textView.setTextSize(16);
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                    }
                                    snackbar.setDuration(2500);
                                    snackbar.show();
                                }

                            } else {
                                Log.d("000522", "PERFORM VACCINE");
                                Dialog_VaccineKoAnjaamDy();
                            }

                        } catch (Exception e) {
                            Log.d("000522", "Err:" + e.getMessage());
                        }

                    }


                    else if (mData[position][1].equalsIgnoreCase("OPV-0"))
                    {

                        try {
                            Lister ls = new Lister(ctx);
                            ls.createAndOpenDB();

                            String[][] mDataa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "'");
                            if (mDataa != null) {

                                String[][] mData_ref_vac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "' AND type = '3' ");

                                if (mData_ref_vac != null) {
                                    Log.d("000522", "Refuse");
                                    // Toast.makeText(getApplicationContext(), R.string.refused_vaccine, Toast.LENGTH_SHORT).show();
                                    final Snackbar snackbar = Snackbar.make(view, R.string.vaccineDenied, Snackbar.LENGTH_SHORT);
                                    View mySbView = snackbar.getView();
                                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.WHITE);
                                    textView.setTextSize(16);
                                    textView.setGravity(Gravity.CENTER_VERTICAL);
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                    }
                                    snackbar.setDuration(2500);
                                    snackbar.show();
                                } else {
                                    Log.d("000522", "Not Ref");
                                    //Toast.makeText(getApplicationContext(), R.string.Child_already_vaccinated, Toast.LENGTH_SHORT).show();
                                    final Snackbar snackbar = Snackbar.make(view, R.string.thisVaccineApplied, Snackbar.LENGTH_SHORT);
                                    View mySbView = snackbar.getView();
                                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.WHITE);
                                    textView.setGravity(Gravity.CENTER_VERTICAL);
                                    textView.setTextSize(16);
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                    }
                                    snackbar.setDuration(2500);
                                    snackbar.show();
                                }

                            } else {
                                Log.d("000522", "PERFORM VACCINE");
                                Dialog_VaccineKoAnjaamDy();
                            }

                        } catch (Exception e) {
                            Log.d("000522", "Err:" + e.getMessage());
                        }


                    } else if (mData[position][1].equalsIgnoreCase("OPV-2")) {
                        try {
                            Lister ls = new Lister(ctx);
                            ls.createAndOpenDB();

                            String[][] mDataaa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = 'c6a25e916636f934'");
                            if (mDataaa != null) {

                                try {
                                    String[][] mDataa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "'");
                                    if (mDataa != null) {

                                        String[][] mData_ref_vac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "' AND type = '3' ");

                                        if (mData_ref_vac != null) {
                                            Log.d("000522", "Refuse");
                                            // Toast.makeText(getApplicationContext(), R.string.refused_vaccine, Toast.LENGTH_SHORT).show();
                                            final Snackbar snackbar = Snackbar.make(view, R.string.vaccineDenied, Snackbar.LENGTH_SHORT);
                                            View mySbView = snackbar.getView();
                                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                            textView.setTextColor(Color.WHITE);
                                            textView.setTextSize(16);
                                            textView.setGravity(Gravity.CENTER_VERTICAL);
                                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                            }
                                            snackbar.setDuration(2500);
                                            snackbar.show();
                                        } else {
                                            Log.d("000522", "Not Ref");
                                            //Toast.makeText(getApplicationContext(), R.string.Child_already_vaccinated, Toast.LENGTH_SHORT).show();
                                            final Snackbar snackbar = Snackbar.make(view, R.string.thisVaccineApplied, Snackbar.LENGTH_SHORT);
                                            View mySbView = snackbar.getView();
                                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                            textView.setTextColor(Color.WHITE);
                                            textView.setGravity(Gravity.CENTER_VERTICAL);
                                            textView.setTextSize(16);
                                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                            }
                                            snackbar.setDuration(2500);
                                            snackbar.show();
                                        }

                                    } else {
                                        Log.d("000522", "OPV-2 VACCINE ENABLE YET");

                                        Dialog_VaccineKoAnjaamDy();
                                    }

                                } catch (Exception e) {
                                    Log.d("000522", "Err:" + e.getMessage());
                                }


                            } else {

                                Log.d("000522", "DueDateBBB : " + mData[position][2]);
                                Log.d("000522", "PLEASE PERFORM OPV-1 first");

                                final Snackbar snackbar = Snackbar.make(view, R.string.selectOPV1first, Snackbar.LENGTH_SHORT);
                                View mySbView = snackbar.getView();
                                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                textView.setGravity(Gravity.CENTER_VERTICAL);
                                textView.setTextSize(16);
                                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                }
                                snackbar.setDuration(5000);
                                snackbar.show();
                            }

                        } catch (Exception e) {
                            Log.d("000522", "Err 2:" + e.getMessage());
                        }
                    } else if (mData[position][1].equalsIgnoreCase("Rota-2")) {
                        try {
                            Lister ls = new Lister(ctx);
                            ls.createAndOpenDB();

                            String[][] mDataaa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '63f3ed284f597bc8'");
                            if (mDataaa != null) {

                                try {

                                    String[][] mDataa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "'");
                                    if (mDataa != null) {

                                        String[][] mData_ref_vac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "' AND type = '3' ");

                                        if (mData_ref_vac != null) {
                                            Log.d("000522", "Refuse");
                                            // Toast.makeText(getApplicationContext(), R.string.refused_vaccine, Toast.LENGTH_SHORT).show();
                                            final Snackbar snackbar = Snackbar.make(view, R.string.vaccineDenied, Snackbar.LENGTH_SHORT);
                                            View mySbView = snackbar.getView();
                                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                            textView.setTextColor(Color.WHITE);
                                            textView.setTextSize(16);
                                            textView.setGravity(Gravity.CENTER_VERTICAL);
                                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                            }
                                            snackbar.setDuration(2500);
                                            snackbar.show();
                                        } else {
                                            Log.d("000522", "Not Ref");
                                            //Toast.makeText(getApplicationContext(), R.string.Child_already_vaccinated, Toast.LENGTH_SHORT).show();
                                            final Snackbar snackbar = Snackbar.make(view, R.string.thisVaccineApplied, Snackbar.LENGTH_SHORT);
                                            View mySbView = snackbar.getView();
                                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                            textView.setTextColor(Color.WHITE);
                                            textView.setGravity(Gravity.CENTER_VERTICAL);
                                            textView.setTextSize(16);
                                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                            }
                                            snackbar.setDuration(2500);
                                            snackbar.show();
                                        }

                                    } else {
                                        Log.d("000522", "Rota-2 VACCINE ENABLE YET");

                                        Dialog_VaccineKoAnjaamDy();
                                    }

                                } catch (Exception e) {
                                    Log.d("000522", "Err:" + e.getMessage());
                                }
                            } else {
                                Log.d("000522", "DueDateBBB : " + mData[position][2]);
                                Log.d("000522", "PLEASE PERFORM Rota-1 first");

                                final Snackbar snackbar = Snackbar.make(view, R.string.selectRota1first, Snackbar.LENGTH_SHORT);
                                View mySbView = snackbar.getView();
                                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                textView.setGravity(Gravity.CENTER_VERTICAL);
                                textView.setTextSize(16);
                                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                }
                                snackbar.setDuration(5000);
                                snackbar.show();
                            }

                        } catch (Exception e) {
                            Log.d("000522", "Err 2:" + e.getMessage());
                        }
                    } else if (mData[position][1].equalsIgnoreCase("PCVIO-2")) {
                        try {
                            Lister ls = new Lister(ctx);
                            ls.createAndOpenDB();

                            String[][] mDataaa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = 'e68d18affc6f9e5a'");
                            if (mDataaa != null) {

                                try {

                                    String[][] mDataa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "'");
                                    if (mDataa != null) {

                                        String[][] mData_ref_vac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "' AND type = '3' ");

                                        if (mData_ref_vac != null) {
                                            Log.d("000522", "Refuse");
                                            // Toast.makeText(getApplicationContext(), R.string.refused_vaccine, Toast.LENGTH_SHORT).show();
                                            final Snackbar snackbar = Snackbar.make(view, R.string.vaccineDenied, Snackbar.LENGTH_SHORT);
                                            View mySbView = snackbar.getView();
                                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                            textView.setTextColor(Color.WHITE);
                                            textView.setTextSize(16);
                                            textView.setGravity(Gravity.CENTER_VERTICAL);
                                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                            }
                                            snackbar.setDuration(2500);
                                            snackbar.show();
                                        } else {
                                            Log.d("000522", "Not Ref");
                                            //Toast.makeText(getApplicationContext(), R.string.Child_already_vaccinated, Toast.LENGTH_SHORT).show();
                                            final Snackbar snackbar = Snackbar.make(view, R.string.thisVaccineApplied, Snackbar.LENGTH_SHORT);
                                            View mySbView = snackbar.getView();
                                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                            textView.setTextColor(Color.WHITE);
                                            textView.setGravity(Gravity.CENTER_VERTICAL);
                                            textView.setTextSize(16);
                                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                            }
                                            snackbar.setDuration(2500);
                                            snackbar.show();
                                        }

                                    } else {

                                        Log.d("000522", "PCVIO-2 VACCINE ENABLE YET");
                                        Dialog_VaccineKoAnjaamDy();
                                    }

                                } catch (Exception e) {
                                    Log.d("000522", "Err:" + e.getMessage());
                                }

                            } else {

                                Log.d("000522", "DueDateBBB : " + mData[position][2]);
                                Log.d("000522", "PLEASE PERFORM PCVIO-1 first");

                                final Snackbar snackbar = Snackbar.make(view, R.string.selectPCVIO1first, Snackbar.LENGTH_SHORT);
                                View mySbView = snackbar.getView();
                                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                textView.setGravity(Gravity.CENTER_VERTICAL);
                                textView.setTextSize(16);
                                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                }
                                snackbar.setDuration(5000);
                                snackbar.show();
                            }

                        } catch (Exception e) {
                            Log.d("000522", "Err 2:" + e.getMessage());
                        }
                    } else if (mData[position][1].equalsIgnoreCase("Penta-2")) {
                        try {
                            Lister ls = new Lister(ctx);
                            ls.createAndOpenDB();

                            String[][] mDataaa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = 'ff1e5aebe8d92ebe'");
                            if (mDataaa != null) {

                                try {

                                    String[][] mDataa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "'");
                                    if (mDataa != null) {

                                        String[][] mData_ref_vac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "' AND type = '3' ");

                                        if (mData_ref_vac != null) {
                                            Log.d("000522", "Refuse");
                                            // Toast.makeText(getApplicationContext(), R.string.refused_vaccine, Toast.LENGTH_SHORT).show();
                                            final Snackbar snackbar = Snackbar.make(view, R.string.vaccineDenied, Snackbar.LENGTH_SHORT);
                                            View mySbView = snackbar.getView();
                                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                            textView.setTextColor(Color.WHITE);
                                            textView.setTextSize(16);
                                            textView.setGravity(Gravity.CENTER_VERTICAL);
                                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                            }
                                            snackbar.setDuration(2500);
                                            snackbar.show();
                                        } else {
                                            Log.d("000522", "Not Ref");
                                            //Toast.makeText(getApplicationContext(), R.string.Child_already_vaccinated, Toast.LENGTH_SHORT).show();
                                            final Snackbar snackbar = Snackbar.make(view, R.string.thisVaccineApplied, Snackbar.LENGTH_SHORT);
                                            View mySbView = snackbar.getView();
                                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                            textView.setTextColor(Color.WHITE);
                                            textView.setGravity(Gravity.CENTER_VERTICAL);
                                            textView.setTextSize(16);
                                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                            }
                                            snackbar.setDuration(2500);
                                            snackbar.show();
                                        }

                                    } else {
                                        Log.d("000522", "PERFORM VACCINE");
                                        Log.d("000522", "Penta-2 VACCINE ENABLE YET");
                                        Dialog_VaccineKoAnjaamDy();
                                    }

                                } catch (Exception e) {
                                    Log.d("000522", "Err:" + e.getMessage());
                                }


                            } else {

                                Log.d("000522", "DueDateBBB : " + mData[position][2]);
                                Log.d("000522", "PLEASE PERFORM Penta-1 first");

                                final Snackbar snackbar = Snackbar.make(view, R.string.selectPenta1First, Snackbar.LENGTH_SHORT);
                                View mySbView = snackbar.getView();
                                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                textView.setGravity(Gravity.CENTER_VERTICAL);
                                textView.setTextSize(16);
                                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                }
                                snackbar.setDuration(5000);
                                snackbar.show();
                            }

                        } catch (Exception e) {
                            Log.d("000522", "Err 2:" + e.getMessage());
                        }
                    } else if (mData[position][1].equalsIgnoreCase("OPV-3")) {
                        try {
                            Lister ls = new Lister(ctx);
                            ls.createAndOpenDB();

                            String[][] mDataaa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = 'ed9955a9bac861ad'");
                            if (mDataaa != null) {

                                try {

                                    String[][] mDataa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "'");
                                    if (mDataa != null) {

                                        String[][] mData_ref_vac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "' AND type = '3' ");

                                        if (mData_ref_vac != null) {
                                            Log.d("000522", "Refuse");
                                            // Toast.makeText(getApplicationContext(), R.string.refused_vaccine, Toast.LENGTH_SHORT).show();
                                            final Snackbar snackbar = Snackbar.make(view, R.string.vaccineDenied, Snackbar.LENGTH_SHORT);
                                            View mySbView = snackbar.getView();
                                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                            textView.setTextColor(Color.WHITE);
                                            textView.setTextSize(16);
                                            textView.setGravity(Gravity.CENTER_VERTICAL);
                                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                            }
                                            snackbar.setDuration(2500);
                                            snackbar.show();
                                        } else {
                                            Log.d("000522", "Not Ref");
                                            //Toast.makeText(getApplicationContext(), R.string.Child_already_vaccinated, Toast.LENGTH_SHORT).show();
                                            final Snackbar snackbar = Snackbar.make(view, R.string.thisVaccineApplied, Snackbar.LENGTH_SHORT);
                                            View mySbView = snackbar.getView();
                                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                            textView.setTextColor(Color.WHITE);
                                            textView.setGravity(Gravity.CENTER_VERTICAL);
                                            textView.setTextSize(16);
                                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                            }
                                            snackbar.setDuration(2500);
                                            snackbar.show();
                                        }

                                    } else {
                                        Log.d("000522", "PERFORM VACCINE");
                                        Log.d("000522", "OPV-3 VACCINE ENABLE YET");
                                        Dialog_VaccineKoAnjaamDy();
                                    }

                                } catch (Exception e) {
                                    Log.d("000522", "Err:" + e.getMessage());
                                }


                            } else {

                                Log.d("000522", "DueDateBBB : " + mData[position][2]);
                                Log.d("000522", "PLEASE PERFORM OPV-2 first");

                                final Snackbar snackbar = Snackbar.make(view, R.string.selectOPV2first, Snackbar.LENGTH_SHORT);
                                View mySbView = snackbar.getView();
                                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                textView.setGravity(Gravity.CENTER_VERTICAL);
                                textView.setTextSize(16);
                                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                }
                                snackbar.setDuration(5000);
                                snackbar.show();
                            }

                        } catch (Exception e) {
                            Log.d("000522", "Err 2:" + e.getMessage());
                        }
                    } else if (mData[position][1].equalsIgnoreCase("PCVIO-3")) {
                        try {
                            Lister ls = new Lister(ctx);
                            ls.createAndOpenDB();

                            String[][] mDataaa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '9b08c2e606b51d40'");
                            if (mDataaa != null) {
                                try {

                                    String[][] mDataa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "'");
                                    if (mDataa != null) {

                                        String[][] mData_ref_vac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "' AND type = '3' ");

                                        if (mData_ref_vac != null) {
                                            Log.d("000522", "Refuse");
                                            // Toast.makeText(getApplicationContext(), R.string.refused_vaccine, Toast.LENGTH_SHORT).show();
                                            final Snackbar snackbar = Snackbar.make(view, R.string.vaccineDenied, Snackbar.LENGTH_SHORT);
                                            View mySbView = snackbar.getView();
                                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                            textView.setTextColor(Color.WHITE);
                                            textView.setTextSize(16);
                                            textView.setGravity(Gravity.CENTER_VERTICAL);
                                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                            }
                                            snackbar.setDuration(2500);
                                            snackbar.show();
                                        } else {
                                            Log.d("000522", "Not Ref");
                                            //Toast.makeText(getApplicationContext(), R.string.Child_already_vaccinated, Toast.LENGTH_SHORT).show();
                                            final Snackbar snackbar = Snackbar.make(view, R.string.thisVaccineApplied, Snackbar.LENGTH_SHORT);
                                            View mySbView = snackbar.getView();
                                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                            textView.setTextColor(Color.WHITE);
                                            textView.setGravity(Gravity.CENTER_VERTICAL);
                                            textView.setTextSize(16);
                                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                            }
                                            snackbar.setDuration(2500);
                                            snackbar.show();
                                        }

                                    } else {
                                        Log.d("000522", "PERFORM VACCINE");
                                        Log.d("000522", "PCVIO-3 VACCINE ENABLE YET");
                                        Dialog_VaccineKoAnjaamDy();
                                    }

                                } catch (Exception e) {
                                    Log.d("000522", "Err:" + e.getMessage());
                                }

                            } else {

                                Log.d("000522", "DueDateBBB : " + mData[position][2]);
                                Log.d("000522", "PLEASE PERFORM PCVIO-2 first");

                                final Snackbar snackbar = Snackbar.make(view, R.string.selectPCVIO2first, Snackbar.LENGTH_SHORT);
                                View mySbView = snackbar.getView();
                                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                textView.setGravity(Gravity.CENTER_VERTICAL);
                                textView.setTextSize(16);
                                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                }
                                snackbar.setDuration(5000);
                                snackbar.show();
                            }

                        } catch (Exception e) {
                            Log.d("000522", "Err 2:" + e.getMessage());
                        }
                    } else if (mData[position][1].equalsIgnoreCase("Penta-3")) {
                        try {
                            Lister ls = new Lister(ctx);
                            ls.createAndOpenDB();

                            String[][] mDataaa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '7c0af8c6db48f56f'");
                            if (mDataaa != null) {

                                try {

                                    String[][] mDataa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "'");
                                    if (mDataa != null) {

                                        String[][] mData_ref_vac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "' AND type = '3' ");

                                        if (mData_ref_vac != null) {
                                            Log.d("000522", "Refuse");
                                            // Toast.makeText(getApplicationContext(), R.string.refused_vaccine, Toast.LENGTH_SHORT).show();
                                            final Snackbar snackbar = Snackbar.make(view, R.string.vaccineDenied, Snackbar.LENGTH_SHORT);
                                            View mySbView = snackbar.getView();
                                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                            textView.setTextColor(Color.WHITE);
                                            textView.setTextSize(16);
                                            textView.setGravity(Gravity.CENTER_VERTICAL);
                                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                            }
                                            snackbar.setDuration(2500);
                                            snackbar.show();
                                        } else {
                                            Log.d("000522", "Not Ref");
                                            //Toast.makeText(getApplicationContext(), R.string.Child_already_vaccinated, Toast.LENGTH_SHORT).show();
                                            final Snackbar snackbar = Snackbar.make(view, R.string.thisVaccineApplied, Snackbar.LENGTH_SHORT);
                                            View mySbView = snackbar.getView();
                                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                            textView.setTextColor(Color.WHITE);
                                            textView.setGravity(Gravity.CENTER_VERTICAL);
                                            textView.setTextSize(16);
                                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                            }
                                            snackbar.setDuration(2500);
                                            snackbar.show();
                                        }

                                    } else {
                                        Log.d("000522", "PERFORM VACCINE");
                                        Log.d("000522", "Penta-3 VACCINE ENABLE YET");
                                        Dialog_VaccineKoAnjaamDy();
                                    }

                                } catch (Exception e) {
                                    Log.d("000522", "Err:" + e.getMessage());
                                }

                            } else {

                                Log.d("000522", "DueDateBBB : " + mData[position][2]);
                                Log.d("000522", "PLEASE PERFORM Penta-2 first");

                                final Snackbar snackbar = Snackbar.make(view, R.string.selectPenta2first, Snackbar.LENGTH_SHORT);
                                View mySbView = snackbar.getView();
                                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                textView.setGravity(Gravity.CENTER_VERTICAL);
                                textView.setTextSize(16);
                                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                }
                                snackbar.setDuration(5000);
                                snackbar.show();
                            }

                        } catch (Exception e) {
                            Log.d("000522", "Err 2:" + e.getMessage());
                        }
                    } else if (mData[position][1].equalsIgnoreCase("Measles-2")) {
                        try {
                            Lister ls = new Lister(ctx);
                            ls.createAndOpenDB();

                            String[][] mDataaa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '4263ac05ab335e9c'");
                            if (mDataaa != null) {

                                try {

                                    String[][] mDataa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "'");
                                    if (mDataa != null) {

                                        String[][] mData_ref_vac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "' AND type = '3' ");

                                        if (mData_ref_vac != null) {
                                            Log.d("000522", "Refuse");
                                            // Toast.makeText(getApplicationContext(), R.string.refused_vaccine, Toast.LENGTH_SHORT).show();
                                            final Snackbar snackbar = Snackbar.make(view, R.string.vaccineDenied, Snackbar.LENGTH_SHORT);
                                            View mySbView = snackbar.getView();
                                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                            textView.setTextColor(Color.WHITE);
                                            textView.setTextSize(16);
                                            textView.setGravity(Gravity.CENTER_VERTICAL);
                                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                            }
                                            snackbar.setDuration(2500);
                                            snackbar.show();
                                        } else {
                                            Log.d("000522", "Not Ref");
                                            //Toast.makeText(getApplicationContext(), R.string.Child_already_vaccinated, Toast.LENGTH_SHORT).show();
                                            final Snackbar snackbar = Snackbar.make(view, R.string.thisVaccineApplied, Snackbar.LENGTH_SHORT);
                                            View mySbView = snackbar.getView();
                                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                            textView.setTextColor(Color.WHITE);
                                            textView.setGravity(Gravity.CENTER_VERTICAL);
                                            textView.setTextSize(16);
                                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                            }
                                            snackbar.setDuration(2500);
                                            snackbar.show();
                                        }

                                    } else {
                                        Log.d("000522", "PERFORM VACCINE");
                                        Log.d("000522", "Measles-3 VACCINE ENABLE YET");
                                        Dialog_VaccineKoAnjaamDy();
                                    }

                                } catch (Exception e) {
                                    Log.d("000522", "Err:" + e.getMessage());
                                }
                            } else {

                                Log.d("000522", "DueDateBBB : " + mData[position][2]);
                                Log.d("000522", "PLEASE PERFORM Measles-1 first");

                                final Snackbar snackbar = Snackbar.make(view, R.string.selectMeasles1, Snackbar.LENGTH_SHORT);
                                View mySbView = snackbar.getView();
                                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                textView.setGravity(Gravity.CENTER_VERTICAL);
                                textView.setTextSize(16);
                                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                }
                                snackbar.setDuration(5000);
                                snackbar.show();
                            }

                        } catch (Exception e) {
                            Log.d("000522", "Err 2:" + e.getMessage());
                        }
                    } else {

                        try {
                            Lister ls = new Lister(ctx);
                            ls.createAndOpenDB();

                            String[][] mDataa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "'");
                            if (mDataa != null) {

                                String[][] mData_ref_vac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "' AND type = '3' ");

                                if (mData_ref_vac != null) {
                                    Log.d("000522", "Refuse");
                                    // Toast.makeText(getApplicationContext(), R.string.refused_vaccine, Toast.LENGTH_SHORT).show();
                                    final Snackbar snackbar = Snackbar.make(view, R.string.vaccineDenied, Snackbar.LENGTH_SHORT);
                                    View mySbView = snackbar.getView();
                                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.WHITE);
                                    textView.setTextSize(16);
                                    textView.setGravity(Gravity.CENTER_VERTICAL);
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                                    }
                                    snackbar.setDuration(2500);
                                    snackbar.show();
                                } else {
                                    Log.d("000522", "Not Ref");
                                    //Toast.makeText(getApplicationContext(), R.string.Child_already_vaccinated, Toast.LENGTH_SHORT).show();
                                    final Snackbar snackbar = Snackbar.make(view, R.string.thisVaccineApplied, Snackbar.LENGTH_SHORT);
                                    View mySbView = snackbar.getView();
                                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.WHITE);
                                    textView.setGravity(Gravity.CENTER_VERTICAL);
                                    textView.setTextSize(16);
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                    }
                                    snackbar.setDuration(2500);
                                    snackbar.show();
                                }

                            } else {
                                Log.d("000522", "PERFORM VACCINE");
                                Dialog_VaccineKoAnjaamDy();
                            }

                        } catch (Exception e) {
                            Log.d("000522", "Err:" + e.getMessage());
                        }
                    }
                }
            }
        });

    }


    public void Dialog_VaccineKoAnjaamDy() {
        final Dialog dialog = new Dialog(ctx);
        LayoutInflater layout = LayoutInflater.from(ctx);
        final View dialogView = layout.inflate(R.layout.dialog_phele_sy_li_hoe_vaccines_layout, null);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);


        iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        btn_jamaa_kre = (Button) dialog.findViewById(R.id.submit);
        et_tareekh_mosool_hoe = (EditText) dialog.findViewById(R.id.et_tareekh_mosool_hoe);
        sp_vaccine_kaha_farham_hoe = (Spinner) dialog.findViewById(R.id.sp_vaccine_kaha_farham_hoe);

        btn_InFacility = dialog.findViewById(R.id.btn_InFacility);
        btn_Outreach = dialog.findViewById(R.id.btn_Outreach);
        btn_MobileVaccine = dialog.findViewById(R.id.btn_MobileVaccine);


        et_tareekh_mosool_hoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTareekhDialog();
            }
        });


        spineer_data();
        dialog.show();

        btn_InFacility.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_left_border_only));
        btn_Outreach.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_border_only));
        btn_MobileVaccine.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_right_border_only));

        btn_InFacility.setTextColor(ContextCompat.getColor(ctx, R.color.color_white));
        btn_Outreach.setTextColor(ContextCompat.getColor(ctx, R.color.dark_blue_color));
        btn_MobileVaccine.setTextColor(ContextCompat.getColor(ctx, R.color.dark_blue_color));

        btn_name = "In Facility";
        btn_value = "1";

        btn_InFacility.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                btn_name = "In Facility";
                btn_value = "1";

                btn_InFacility.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_left_border_only));
                btn_Outreach.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_border_only));
                btn_MobileVaccine.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_right_border_only));

                btn_InFacility.setTextColor(ContextCompat.getColor(ctx, R.color.color_white));
                btn_Outreach.setTextColor(ContextCompat.getColor(ctx, R.color.dark_blue_color));
                btn_MobileVaccine.setTextColor(ContextCompat.getColor(ctx, R.color.dark_blue_color));

            }
        });

        btn_Outreach.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                btn_name = "Outreach";
                btn_value = "2";


                btn_InFacility.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_left_border_only2));
                btn_Outreach.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_border_only2));
                btn_MobileVaccine.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_right_border_only));

                btn_InFacility.setTextColor(ContextCompat.getColor(ctx, R.color.dark_blue_color));
                btn_Outreach.setTextColor(ContextCompat.getColor(ctx, R.color.color_white));
                btn_MobileVaccine.setTextColor(ContextCompat.getColor(ctx, R.color.dark_blue_color));


            }
        });

        btn_MobileVaccine.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                btn_name = getString(R.string.mobileVaccination);
                btn_value = "3";


                btn_InFacility.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_left_border_only2));
                btn_Outreach.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_border_only));
                btn_MobileVaccine.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_right_border_only2));

                btn_InFacility.setTextColor(ContextCompat.getColor(ctx, R.color.dark_blue_color));
                btn_Outreach.setTextColor(ContextCompat.getColor(ctx, R.color.dark_blue_color));
                btn_MobileVaccine.setTextColor(ContextCompat.getColor(ctx, R.color.color_white));

            }
        });


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_tareekh_mosool_hoe.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(), R.string.enterDateReceived, Toast.LENGTH_LONG).show();
                    return;
                }

                if (sp_vaccine_kaha_farham_hoe.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.selectVaccineAvailPlace, Toast.LENGTH_LONG).show();
                    return;
                }


                if (serviceLocation.showCurrentLocation() == true) {
                    latitude = serviceLocation.getLatitude();
                    longitude = serviceLocation.getLongitude();
                    Log.d("000266", " latitude: " + latitude);
                    Log.d("000266", " longitude: " + longitude);
                } else {
                    try {
                        serviceLocation.doAsynchronousTask.cancel();
                    } catch (Exception e) {
                    }
                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String[][] mData = ls.executeReader("SELECT max(added_on),metadata,count(*) from CVACCINATION");

                        if (Integer.parseInt(mData[0][2]) > 0) {
                            JSONObject jsonObject = new JSONObject(mData[0][1]);
                            Log.d("000266", "  Last Latitude: " + jsonObject.getString("lat"));
                            Log.d("000266", "  Last Longitude: " + jsonObject.getString("lng"));

                            latitude = Double.parseDouble(jsonObject.getString("lat"));
                            longitude = Double.parseDouble(jsonObject.getString("lng"));

                            Toast.makeText(ctx, R.string.R.string.dataGPS, Toast.LENGTH_SHORT).show();
                        } else {
                            latitude = Double.parseDouble("0.0");
                            longitude = Double.parseDouble("0.0");
                            Toast.makeText(ctx, R.string.notDataGPS, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.d("000266", "Read CVACCINE Error: " + e.getMessage());
                    }
                }

                try {


                    Lister ls = new Lister(Child_HifazitiTeekeyKahiAurSyLiHoe_Activity.this);
                    ls.createAndOpenDB();


                    SimpleDateFormat dates = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss aa");
                    Calendar c = Calendar.getInstance();
                    String current_timeStamp = dates.format(c.getTime());
                    Log.d("000266", "timestamp:" + current_timeStamp);

                    JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("type_name", "" + "phele_sy_li_hoe_vaccine");
                    jobj.put("vaccine_place", "" + btn_name);
                    jobj.put("vaccine_place_pos", "" + btn_value);
                    jobj.put("vaccine_kaha_farham_ki_gae", "" + sp_vaccine_kaha_farham_hoe.getSelectedItem().toString());
                    jobj.put("village_uid", "" + village_uid);
                    jobj.put("datetime", "" + current_timeStamp);

                    String added_on = String.valueOf(System.currentTimeMillis());

                    // jobjMain.put("data", jobj);
                    String ans1 = "insert into CVACCINATION (member_uid,vaccine_id, record_data, type, due_date,vaccinated_on,image_location,metadata,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + child_uid + "'," +
                            "'" + mData[index_val][0] + "'," +
                            "'" + TodayDate + "'," +
                            "'" + "1" + "'," +
                            "'" + arrayListDate.get(index_val) + "'," +
                            "'" + et_tareekh_mosool_hoe.getText().toString() + "'," +
                            "'" + TodayDate + "'," +
                            "'" + jobj + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000266", "Data: " + ans1);
                    Log.d("000266", "Query: " + res);

                    //imageocation o be added

                    if (res.toString().equalsIgnoreCase("true")) {
                        final Snackbar snackbar = Snackbar.make(v, R.string.vaccDataSubmitted, Snackbar.LENGTH_SHORT);
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

                            sendPostRequest(child_uid, mData[index_val][0], et_tareekh_mosool_hoe.getText().toString(), String.valueOf(jobj), login_useruid, added_on);
                        } else {
                            // Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        final Snackbar snackbar = Snackbar.make(v, "ویکسین ڈیٹا جمع نہیں ہوا.", Snackbar.LENGTH_SHORT);
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

                } catch (Exception e) {
                    Log.d("000266", " Error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Intent intent = new Intent(ctx, Child_HifazitiTeekeyKahiAurSyLiHoe_Activity.class);
                            intent.putExtra("u_id", child_uid);
                            intent.putExtra("child_name", child_name);
                            intent.putExtra("child_gender", child_gender);
                            startActivity(intent);
                        }
                    }, 2500);

                }

            }
        });


    }

    private void sendPostRequest(final String member_uid, final String vacine_uid, final String record_data,
                                 final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/child/vaccinations";

        Log.d("000266", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {
                    //   Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000266", "response1    " + response);
                        //Toast.makeText(ctx, "Data has been saved", Toast.LENGTH_SHORT).show();

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE CVACCINATION SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + child_uid + "'AND added_on= '" + added_on + "'AND vaccine_id= '" + vacine_uid + "'";
                        ls.executeNonQuery(update_record);

                        Toast tt = Toast.makeText(ctx, R.string.vaccineDataSynced, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();
                    } else {
                        Log.d("000266", "else ");
                        Toast tt = Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();
                        //Toast.makeText(ctx, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000266", " Error: " + e.getMessage());
                    //Toast.makeText(ctx, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();
                    Toast.makeText(ctx, R.string.vaccineDataSynced, Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("000266", "error    " + error.getMessage());
                //    Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast tt = Toast.makeText(ctx, "ویکسین ڈیٹا سنک نہیں ہوا", Toast.LENGTH_SHORT);
                tt.setGravity(Gravity.CENTER, 0, 0);
                tt.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("member_id", member_uid);
                params.put("vaccine_id", vacine_uid);
                params.put("type", "2");
                params.put("record_data", record_data);
                params.put("vaccinated_on", et_tareekh_mosool_hoe.getText().toString());
                params.put("image_location", added_by);
                params.put("metadata", data);
                params.put("added_by", added_by);
                params.put("added_on", added_on);


                Log.d("000266", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000266", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }


    public void ShowTareekhDialog() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(Child_HifazitiTeekeyKahiAurSyLiHoe_Activity.this, R.style.DatePickerDialog,
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
                        et_tareekh_mosool_hoe.setText(yearf2 + "-" + monthf2 + "-" + dayf2);


                        Calendar dob = Calendar.getInstance();
                        Calendar today = Calendar.getInstance();

                        dob.set(year, monthOfYear, dayOfMonth);

                        int age = today.get(Calendar.YEAR) - year;

                        Integer ageInt = new Integer(age);
                        String ageS = ageInt.toString();


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();


    }


    private void spineer_data() {

        Utils.setSpinnervillage(getApplicationContext(), sp_vaccine_kaha_farham_hoe);
        sp_vaccine_kaha_farham_hoe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (sp_vaccine_kaha_farham_hoe.getSelectedItemPosition() > 0) {
                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String[][] mData = ls.executeReader("select uid from VILLAGES where name = '"+sp_vaccine_kaha_farham_hoe.getSelectedItem()+"' order by name ");
                        village_uid = mData[0][0];

                    } catch (Exception e) {
                        Log.d("0000999", "CATCH:" + e.getMessage());
                    }
                }
                else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

    }


    private void calculateAge() {

        Lister ls = new Lister(ctx);
        ls.createAndOpenDB();

        String mData[][] = ls.executeReader("Select full_name,dob from MEMBER where uid = '" + child_uid + "'");

        child_name = mData[0][0];
        txt_naam.setText(mData[0][0]);

        Log.d("000266", "DOB: " + mData[0][1]);

        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = parse.parse(mData[0][1]);
            Date n = new Date();
            long dob = d.getTime();
            long now = n.getTime();
            long diff = (now - dob) / 1000;
            long days = diff / 86400;
            int dob_days = 0;
            int dob_weeks = 0;
            int dob_months = 0;
            int dob_years = 0;
            while (days > 7) {
                if ((days - 365) > 0) {
                    days -= 365;
                    dob_years++;
                    continue;
                }
                if ((days - 30) > 0) {
                    days -= 30;
                    dob_months++;

                    continue;
                }
                if ((days - 7) > 0) {
                    days -= 7;
                    dob_weeks++;
                }
            }


            dob_days = Integer.valueOf((int) days);

            Log.d("000266", "Saal: " + String.valueOf(dob_years));
            Log.d("000266", "Maah: " + String.valueOf(dob_months));

            child_age = String.valueOf(dob_years) + " سال " + String.valueOf(dob_months) + " مہ ";
            txt_age.setText(child_age);
//            Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            Log.d("000266", "DOB Error: " + e.getMessage());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        try {


            arrayListVaccine.clear();
            arrayListDate.clear();

            arrayListVaccine = new ArrayList<String>();
            arrayListDate = new ArrayList<String>();


            try {

                Lister ls = new Lister(ctx);
                ls.createAndOpenDB();


                mData = ls.executeReader("Select uid,name,due_date from VACCINES where due_date='" + vaccine_duedate + "'");
                Log.d("000266", "Data" + String.valueOf(mData.length));


                String[][] mDataa = ls.executeReader("Select dob from MEMBER where uid = '" + child_uid + "'");
                Log.d("000266", "DOB:" + mDataa[0][0]);
                SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                String TodayDate = dates.format(c.getTime());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


                //Convert to Date
                Date startDate = dates.parse(TodayDate);
                Calendar c1 = Calendar.getInstance();
                //Change to Calendar Date
                c1.setTime(startDate);

                //Convert to Date
                Date endDate = dates.parse(mDataa[0][0]);
                Calendar c2 = Calendar.getInstance();
                //Change to Calendar Date
                c2.setTime(endDate);

                //get Time in milli seconds
                long ms1 = c1.getTimeInMillis();
                long ms2 = c2.getTimeInMillis();
                //get difference in milli seconds
                long diff = ms1 - ms2;
                diffInDays = (int) (diff / (24 * 60 * 60 * 1000));
                Log.d("000266cal", String.valueOf(diffInDays));
//0 is due 1 is active 2 is defaulter 3 is grey 4 is else

                //String[] vacine_name ="testname,0".split("[,]");
                //  Log.d("000266",vacine_name[0]+vacine_name[1]);
                if (mData != null) {
                    HashMap<String, String> map;
                    for (int i = 0; i < mData.length; i++) {
                        Log.d("000266", "Vaccines Name: " + mData[i][1]);

                        if (diffInDays == Integer.parseInt(mData[i][2])) {

                            Calendar cvac = new GregorianCalendar();
                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                            cvac.setTime(dateFormatcvac.parse(mDataa[0][0]));
                            cvac.add(Calendar.DATE, Integer.parseInt(mData[i][2]));
                            Date dvac = cvac.getTime();
                            dateFormat.format(dvac.getTime());

                            String[][] mDatavac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                            // Log.d("000266", "Type: " +mDatavac.length);
                            if (mDatavac != null) {
                                String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                if (mData_refuse != null) {
                                    arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_refuse[0][0]);
                                    Log.d("000266", "1000: ");
                                } else {
                                    arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mDatavac[0][0]);
                                    Log.d("000266", "1: ");
                                }

                            } else {
                                arrayListVaccine.add(i, mData[i][1] + ",1");
                                Log.d("000266", "2: ");
                            }
                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                        } else if (diffInDays < Integer.parseInt(mData[i][2]) && diffInDays > (Integer.parseInt(mData[i][2]) - 15)) {
                            //arrayListVaccine.add(i, mData[i][1] + ",1");
                            Calendar cvac = new GregorianCalendar();
                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                            cvac.setTime(dateFormatcvac.parse(mDataa[0][0]));
                            cvac.add(Calendar.DATE, Integer.parseInt(mData[i][2]));
                            Date dvac = cvac.getTime();
                            dateFormat.format(dvac.getTime());
                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                            String[][] mDatavac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                            if (mDatavac != null) {

                                String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                if (mData_refuse != null) {
                                    arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_refuse[0][0]);
                                    Log.d("000266", "101: ");
                                } else {
                                    arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mDatavac[0][0]);
                                    Log.d("000266", "3: ");
                                }
                            } else {
                                arrayListVaccine.add(i, mData[i][1] + ",1");
                                Log.d("000266", "4: ");
                            }
                        } else if (diffInDays > Integer.parseInt(mData[i][2])) {
                            // arrayListVaccine.add(i, mData[i][1] + ",2");
                            Calendar cvac = new GregorianCalendar();
                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                            cvac.setTime(dateFormatcvac.parse(mDataa[0][0]));
                            cvac.add(Calendar.DATE, Integer.parseInt(mData[i][2]));
                            Date dvac = cvac.getTime();
                            dateFormat.format(dvac.getTime());
                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                            String[][] mDatavac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                            if (mDatavac != null) {
//                                arrayListVaccine.add(i, mData[i][1] + ",0");
//                                Log.d("000266", "5: " );
                                String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                if (mData_refuse != null) {
                                    arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_refuse[0][0]);
                                    Log.d("000266", "102: ");
                                } else {
                                    arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mDatavac[0][0]);
                                    Log.d("000266", "5: ");
                                }

                            } else {
                                arrayListVaccine.add(i, mData[i][1] + ",2");
                                Log.d("000266", "6: ");
                            }

                        } else if (diffInDays < Integer.parseInt(mData[i][2])) {
                            // arrayListVaccine.add(i, mData[i][1] + ",3");
                            Calendar cvac = new GregorianCalendar();
                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                            cvac.setTime(dateFormatcvac.parse(mDataa[0][0]));
                            cvac.add(Calendar.DATE, Integer.parseInt(mData[i][2]));
                            Date dvac = cvac.getTime();
                            dateFormat.format(dvac.getTime());
                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                            String[][] mDatavac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                            if (mDatavac != null) {
                               /* arrayListVaccine.add(i, mData[i][1] + ",0");
                                Log.d("000266", "7: " );*/
                                String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                if (mData_refuse != null) {
                                    arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_refuse[0][0]);
                                    Log.d("000266", "103: ");
                                } else {
                                    arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mDatavac[0][0]);
                                    Log.d("000266", "7: ");
                                }

                            } else {
                                arrayListVaccine.add(i, mData[i][1] + ",3");
                                Log.d("000266", "8 ");
                            }
                        } else {
                            //arrayListVaccine.add(i, mData[i][1] + ",4");
                            Calendar cvac = new GregorianCalendar();
                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                            cvac.setTime(dateFormatcvac.parse(mDataa[0][0]));
                            cvac.add(Calendar.DATE, Integer.parseInt(mData[i][2]));
                            Date dvac = cvac.getTime();
                            dateFormat.format(dvac.getTime());
                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                            String[][] mDatavac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                            if (mDatavac != null) {
                               /* arrayListVaccine.add(i, mData[i][1] + ",0");
                                Log.d("000266", "9: " );*/
                                String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                if (mData_refuse != null) {
                                    arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_refuse[0][0]);
                                    Log.d("000266", "104: ");
                                } else {
                                    arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mDatavac[0][0]);
                                    Log.d("000266", "9: ");
                                }

                            } else {

                                arrayListVaccine.add(i, mData[i][1] + ",4");
                                Log.d("000266", "10 ");
                            }
                        }
                    }
                } else {
                    Toast.makeText(ctx, "No Data", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.d("000266", "Vacine Err: " + String.valueOf(e.getMessage()));
            }
            adt = new Adt_ChildHifazatiTeekeyRecordList_KahiAurSy(ctx, arrayListVaccine, arrayListDate);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);

        } catch (Exception e) {
            Log.d("12345", "Error: " + e.getMessage());
            Toast.makeText(ctx, R.string.noRecord, Toast.LENGTH_SHORT).show();

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
        //  startActivity(new Intent(ctx, Child_Dashboard_Activity.class));
    }
}
