package com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildHifazitiTeekeyRecordActivities;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.akdndhrc.covid_module.Adapter.Adt_ChildDashboard.Adt_ChildHifazitiTeekeyRecordList_DueDate;
import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.NothingSelectedSpinnerAdapter;
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


public class Child_HifazitiTeekeyDueDateList_Activity extends AppCompatActivity {

    Context ctx = Child_HifazitiTeekeyDueDateList_Activity.this;
    TextView txt_age, txt_naam, txt_refuse_vaccine;
    ListView lv;
    RelativeLayout rl_navigation_drawer, rl_home;

    ArrayList<String> arrayListVaccine = new ArrayList<>();
    ArrayList<String> arrayListDate = new ArrayList<>();
    ImageView iv_navigation_drawer, iv_home, iv_close, image_gender;

    Adt_ChildHifazitiTeekeyRecordList_DueDate adt;
    String[][] mData;
    int index_val = 0;
    Button btn_phle_sy_li_hoe_vaccine, btn_phle_sy_li_hoe_vaccine_uc, btn_vaccine_ko_anjaam_dy, btn_refuse_vaccine, btn_jamaa_kre, btn_kahi_aur_sy_karae_ho, btn_new_vial, btn_opened_vial;

    String child_uid, child_age, child_name, child_gender, vaccine_duedate, vaccine_name;
    String to_make_active = String.valueOf(R.string.yes);
    int diffInDays;

    Spinner sp_inside_outside_council;
    double latitude;
    String TodayDate;
    double longitude;
    ServiceLocation serviceLocation;
    String login_useruid;

    Button btn_InFacility, btn_Outreach, btn_MobileVaccine;
    String btn_name, btn_value;
    LinearLayout ll_btns;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_hifaziti_teekey_record_list);


        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Child_HifazitiTeekeyDueDateList_Activity.class));
        Log.d("000159", "RESTARRRRRRRRRRRRR");

        child_uid = getIntent().getExtras().getString("u_id");
        child_name = getIntent().getExtras().getString("child_name");
        child_gender = getIntent().getExtras().getString("child_gender");
        vaccine_duedate = getIntent().getExtras().getString("vaccine_duedate");


        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
            String shared_useruid = prefelse.getString(("login_userid"), null); // getting String
            login_useruid = shared_useruid;
            Log.d("000159", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000159", "Shared Err:" + e.getMessage());
        }

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

        try {
            serviceLocation = new ServiceLocation(ctx);
            serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            serviceLocation.callAsynchronousTask();
        } catch (Exception e) {
            Log.d("000159", "GPS Service Err:  " + e.getMessage());
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

        btn_kahi_aur_sy_karae_ho = findViewById(R.id.btn_kahi_aur_sy_karae_ho);
        btn_kahi_aur_sy_karae_ho.setVisibility(View.VISIBLE);
        btn_kahi_aur_sy_karae_ho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ctx, Child_HifazitiTeekeyKahiAurSyLiHoe_Activity.class);
                intent2.putExtra("u_id", child_uid);
                intent2.putExtra("child_name", child_name);
                intent2.putExtra("child_age", child_age);
                intent2.putExtra("child_gender", child_gender);
                intent2.putExtra("vaccine_duedate", vaccine_duedate);
                intent2.putExtra("diffInDays", diffInDays);

                startActivity(intent2);
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


                       /* if (diffInDays > 365)
                        {
                            Log.d("000522", "DISABLE BCG VACCINE");
                            final Snackbar snackbar = Snackbar.make(view, "یہ ویکسین اب ہمیشہ کے لئے غیر فعال ہے.", Snackbar.LENGTH_SHORT);
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
                            snackbar.setDuration(4000);
                            snackbar.show();
                        }
                        else
                        {
                            Log.d("000522", "BCG VACCINE ENABLE YET");

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
                                    Intent intent2 = new Intent(ctx, Child_HifazitiTeekeyDueDateList_Activity.class);
                                    intent2.putExtra("u_id", child_uid);
                                    intent2.putExtra("child_name", child_name);
                                    intent2.putExtra("child_age", child_age);
                                    intent2.putExtra("child_gender", child_gender);
                                    intent2.putExtra("vaccine_duedate", mData[position][2]);
                                    startActivity(intent2);
                                }

                            } catch (Exception e) {
                                Log.d("000522", "Err:" + e.getMessage());
                            }
                        }*/
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

                      /*  if (diffInDays > 365)
                        {
                            Log.d("000522", "DISABLE HEP-B VACCINE");
                            final Snackbar snackbar = Snackbar.make(view, "یہ ویکسین اب ہمیشہ کے لئے غیر فعال ہے.", Snackbar.LENGTH_SHORT);
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
                            snackbar.setDuration(4000);
                            snackbar.show();
                        }
                        else
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
                                    Log.d("000522", "HEP-B VACCINE ENABLE YET");
                                    Intent intent2 = new Intent(ctx, Child_HifazitiTeekeyDueDateList_Activity.class);
                                    intent2.putExtra("u_id", child_uid);
                                    intent2.putExtra("child_name", child_name);
                                    intent2.putExtra("child_age", child_age);
                                    intent2.putExtra("child_gender", child_gender);
                                    intent2.putExtra("vaccine_duedate", mData[position][2]);
                                    startActivity(intent2);
                                }

                            } catch (Exception e) {
                                Log.d("000522", "Err HEP:" + e.getMessage());
                            }*/
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
                       /* if (diffInDays > 45)
                        {

                            Log.d("000522", "DISABLE OPV-0 VACCINE");
                            final Snackbar snackbar = Snackbar.make(view, "یہ ویکسین اب ہمیشہ کے لئے غیر فعال ہے.", Snackbar.LENGTH_SHORT);
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
                            snackbar.setDuration(4000);
                            snackbar.show();
                        }
                        else
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
                                    Log.d("000522", "OPV-0 VACCINE ENABLE YET");
                                    Intent intent2 = new Intent(ctx, Child_HifazitiTeekeyDueDateList_Activity.class);
                                    intent2.putExtra("u_id", child_uid);
                                    intent2.putExtra("child_name", child_name);
                                    intent2.putExtra("child_age", child_age);
                                    intent2.putExtra("child_gender", child_gender);
                                    intent2.putExtra("vaccine_duedate", mData[position][2]);
                                    startActivity(intent2);
                                }

                            } catch (Exception e) {
                                Log.d("000522", "Err OPV-O:" + e.getMessage());
                            }
                        }*/

                    }

                    else if (mData[position][1].equalsIgnoreCase("OPV-2"))
                    {
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
                    }

                    else if (mData[position][1].equalsIgnoreCase("Rota-2"))
                    {
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
                            }
                            else {
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
                    }

                    else if (mData[position][1].equalsIgnoreCase("PCVIO-2"))
                    {
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
                    }
                    else if (mData[position][1].equalsIgnoreCase("Penta-2"))
                    {
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
                    }
                    else if (mData[position][1].equalsIgnoreCase("OPV-3"))
                    {
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
                    }
                    else if (mData[position][1].equalsIgnoreCase("PCVIO-3"))
                    {
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
                    }

                    else if (mData[position][1].equalsIgnoreCase("Penta-3"))
                    {
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
                    }
                    else if (mData[position][1].equalsIgnoreCase("Measles-2"))
                    {
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
                    }

                    else {

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
        final View dialogView = layout.inflate(R.layout.dialog_vaccine_ko_anjaam_dy_layout_vac, null);
        dialog.setContentView(dialogView);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);


        iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        //Button
        btn_vaccine_ko_anjaam_dy = (Button) dialog.findViewById(R.id.btn_vaccine_ko_anjaam_dy);
        btn_phle_sy_li_hoe_vaccine = (Button) dialog.findViewById(R.id.btn_phle_sy_li_hoe_vaccine);
        btn_phle_sy_li_hoe_vaccine_uc = (Button) dialog.findViewById(R.id.btn_phle_sy_li_hoe_vaccine_uc);
        btn_refuse_vaccine = (Button) dialog.findViewById(R.id.btn_refuse_vaccine);
        btn_jamaa_kre = (Button) dialog.findViewById(R.id.submit);
        btn_opened_vial = (Button) dialog.findViewById(R.id.btn_opened_vial);
        btn_new_vial = (Button) dialog.findViewById(R.id.btn_new_vial);


        //Spinner
        sp_inside_outside_council = (Spinner) dialog.findViewById(R.id.sp_inside_outside_council);

        //TextView
        txt_refuse_vaccine = (TextView) dialog.findViewById(R.id.txt_refuse_vaccine);

        //Button
        ll_btns = dialog.findViewById(R.id.ll_btns);
        btn_InFacility = dialog.findViewById(R.id.btn_InFacility);
        btn_Outreach = dialog.findViewById(R.id.btn_Outreach);
        btn_MobileVaccine = dialog.findViewById(R.id.btn_MobileVaccine);


        //spineer_data();
        dialog.show();


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


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

                btn_name = String.valueOf(R.string.outreach);
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


        btn_vaccine_ko_anjaam_dy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("000699", "Vac Name: " + mData[index_val][1]);
                Log.d("000699", "Facility: " + btn_name);


                if (Build.VERSION.SDK_INT >= 24) {
                    //Toast.makeText(ctx, "Supported", Toast.LENGTH_SHORT).show();
                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.cameraFeatureNotAvailable, Snackbar.LENGTH_SHORT);
                    snackbar.setDuration(4000);
                    snackbar.show();
                    return;
                }


          /*      if (mData[index_val][1].equalsIgnoreCase("BCG") || mData[index_val][1].equalsIgnoreCase("OPV-0") || mData[index_val][1].equalsIgnoreCase("OPV-1")
                        || mData[index_val][1].equalsIgnoreCase("OPV-2") || mData[index_val][1].equalsIgnoreCase("IPV") || mData[index_val][1].equalsIgnoreCase("PCVIO-1")
                        || mData[index_val][1].equalsIgnoreCase("PCVIO-2") || mData[index_val][1].equalsIgnoreCase("PCVIO-3") || mData[index_val][1].equalsIgnoreCase("Measles-1")
                        || mData[index_val][1].equalsIgnoreCase("Measles-2")) {*/
                if (mData[index_val][1].equalsIgnoreCase("BCG") ||mData[index_val][1].equalsIgnoreCase("Measles-1") || mData[index_val][1].equalsIgnoreCase("Measles-2")) {

                    ll_btns.setVisibility(View.GONE);
                    btn_vaccine_ko_anjaam_dy.setVisibility(View.GONE);
                    btn_phle_sy_li_hoe_vaccine_uc.setVisibility(View.GONE);
                    btn_refuse_vaccine.setVisibility(View.GONE);
                    btn_opened_vial.setVisibility(View.VISIBLE);
                    btn_new_vial.setVisibility(View.VISIBLE);

                    ////////////////////// btn Opned Vial //////////////////

                    btn_opened_vial.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(View v) {
                            btn_opened_vial.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.green_color));

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(ctx, Child_HifazitiTeekeyVaccineKoAnjamDy_3_Activity.class);
                                    Bundle bundle = new Bundle();

                                    intent.putExtra("u_id", child_uid);
                                    intent.putExtra("vac_id", mData[index_val][0]);
                                    intent.putExtra("vac_name", mData[index_val][1]);
                                    intent.putExtra("vac_duedate", arrayListDate.get(index_val));
                                    intent.putExtra("vac_place", btn_name);
                                    intent.putExtra("vac_place_pos", btn_value);
                                    intent.putExtra("child_name", child_name);
                                    intent.putExtra("child_age", child_age);
                                    intent.putExtra("child_gender", child_gender);
                                    intent.putExtra("vial_type", "0");
                                    intent.putExtra("vac_groupId", vaccine_duedate);
                                    intent.putExtra("diffInDays", diffInDays);
                                    bundle.putSerializable("mData", mData);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            },1000);
                        }
                    });

                    ////////////////////// btn New Vial //////////////////

                    btn_new_vial.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                btn_new_vial.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.green_color));
                            }
                            btn_new_vial.setTextColor(ctx.getResources().getColor(R.color.color_white));

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(ctx, Child_HifazitiTeekeyVaccineKoAnjamDy_3_Activity.class);
                                    Bundle bundle = new Bundle();

                                    intent.putExtra("u_id", child_uid);
                                    intent.putExtra("vac_id", mData[index_val][0]);
                                    intent.putExtra("vac_name", mData[index_val][1]);
                                    intent.putExtra("vac_duedate", arrayListDate.get(index_val));
                                    intent.putExtra("vac_place", btn_name);
                                    intent.putExtra("vac_place_pos", btn_value);
                                    intent.putExtra("child_name", child_name);
                                    intent.putExtra("child_age", child_age);
                                    intent.putExtra("child_gender", child_gender);
                                    intent.putExtra("vial_type", "1");
                                    intent.putExtra("vac_groupId", vaccine_duedate);
                                    intent.putExtra("diffInDays", diffInDays);
                                    bundle.putSerializable("mData", mData);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            },1000);


                        }
                    });


                } else {

                    Intent intent = new Intent(ctx, Child_HifazitiTeekeyVaccineKoAnjamDy_3_Activity.class);
                    Bundle bundle = new Bundle();

                    intent.putExtra("u_id", child_uid);
                    intent.putExtra("vac_id", mData[index_val][0]);
                    intent.putExtra("vac_name", mData[index_val][1]);
                    intent.putExtra("vac_duedate", arrayListDate.get(index_val));
                    intent.putExtra("vac_place", btn_name);
                    intent.putExtra("vac_place_pos", btn_value);
                    intent.putExtra("child_name", child_name);
                    intent.putExtra("child_age", child_age);
                    intent.putExtra("child_gender", child_gender);
                    intent.putExtra("vial_type", "-1");
                    intent.putExtra("vac_groupId", vaccine_duedate);
                    intent.putExtra("diffInDays", diffInDays);
                    bundle.putSerializable("mData", mData);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    dialog.dismiss();

                }


//                if (sp_inside_outside_council.getSelectedItemPosition() == 0) {
//                    Toast.makeText(getApplicationContext(), R.string.pleaseSelectOne, Toast.LENGTH_LONG).show();
//                    return;
//                }


            }
        });


        ////////////////////////// phele sy li hoe vaccine /////////////////////////////
        btn_phle_sy_li_hoe_vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                if (sp_inside_outside_council.getSelectedItemPosition() == 0) {
//                    Toast.makeText(getApplicationContext(), R.string.pleaseSelectOne, Toast.LENGTH_LONG).show();
//                    return;
//                }

                Intent intent = new Intent(ctx, Child_HifazitiTeekeyPheleSyLiHoeVaccineForm_Activity.class);
                intent.putExtra("u_id", child_uid);
                intent.putExtra("vac_id", mData[index_val][0]);
                intent.putExtra("vac_name", mData[index_val][1]);
                intent.putExtra("vac_duedate", arrayListDate.get(index_val));
                intent.putExtra("vac_place", btn_name);
                intent.putExtra("vac_place_pos", btn_value);
//                    intent.putExtra("vac_place", sp_inside_outside_council.getSelectedItem().toString());
//                    intent.putExtra("vac_place_pos", String.valueOf(sp_inside_outside_council.getSelectedItemPosition() - 1));
                intent.putExtra("child_name", child_name);
                intent.putExtra("child_age", child_age);
                intent.putExtra("child_gender", child_gender);
                intent.putExtra("diffInDays", diffInDays);

                startActivity(intent);
                dialog.dismiss();


            }
        });


        ///////////////////////////////// Phele sy li hoe vaccine in UC ///////////////////////////////

        btn_phle_sy_li_hoe_vaccine_uc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (serviceLocation.showCurrentLocation() == true) {
                    latitude = serviceLocation.getLatitude();
                    longitude = serviceLocation.getLongitude();
                    Log.d("000159", " latitude: " + latitude);
                    Log.d("000159", " longitude: " + longitude);
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
                            Log.d("000159", "  Last Latitude: " + jsonObject.getString("lat"));
                            Log.d("000159", "  Last Longitude: " + jsonObject.getString("lng"));

                            latitude = Double.parseDouble(jsonObject.getString("lat"));
                            longitude = Double.parseDouble(jsonObject.getString("lng"));

                            Toast.makeText(ctx, R.string.dataGPS, Toast.LENGTH_SHORT).show();
                        } else {
                            latitude = Double.parseDouble("0.0");
                            longitude = Double.parseDouble("0.0");
                            Toast.makeText(ctx, R.string.notDataGPS, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.d("000159", "Read CVACCINE Error: " + e.getMessage());
                    }
                }

                try {


                    Lister ls = new Lister(Child_HifazitiTeekeyDueDateList_Activity.this);
                    ls.createAndOpenDB();


                    final JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("vaccine_place", "" + "inside UC");
                    // jobjMain.put("data", jobj);

                    final String added_on = String.valueOf(System.currentTimeMillis());

                    String ans1 = "insert into CVACCINATION (member_uid,vaccine_id, record_data, type, due_date,vaccinated_on,image_location,metadata,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + child_uid + "'," +
                            "'" + mData[index_val][0] + "'," +
                            "'" + TodayDate + "'," +
                            "'" + "2" + "'," +
                            "'" + arrayListDate.get(index_val) + "'," +
                            "'" + TodayDate + "'," +
                            "'" + TodayDate + "'," +
                            "'" + jobj + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000159", "Data: " + ans1);
                    Log.d("000159", "Query: " + res);

                    //imageocation o be added

                    //Toast.makeText(getApplicationContext(), "Data saved.", Toast.LENGTH_SHORT).show();

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

                        sendPostRequest(child_uid, mData[index_val][0], TodayDate, String.valueOf(jobj), login_useruid, added_on);
                    } else {
                       // Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000159", " Error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            dialog.dismiss();
                            Intent intent = new Intent(ctx, Child_HifazitiTeekeyDueDateList_Activity.class);
                            intent.putExtra("u_id", child_uid);
                            intent.putExtra("child_name", child_name);
                            intent.putExtra("child_gender", child_gender);
                            startActivity(intent);
                        }
                    },2000);

                }

            }
        });


        ////////////////////////// Refuse Vaccine /////////////////////////

        btn_refuse_vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ll_btns.setVisibility(View.GONE);
                btn_vaccine_ko_anjaam_dy.setVisibility(View.GONE);
                btn_phle_sy_li_hoe_vaccine.setVisibility(View.GONE);
                btn_phle_sy_li_hoe_vaccine_uc.setVisibility(View.GONE);
                btn_refuse_vaccine.setVisibility(View.GONE);
                sp_inside_outside_council.setVisibility(View.VISIBLE);
                txt_refuse_vaccine.setVisibility(View.VISIBLE);
                btn_jamaa_kre.setVisibility(View.VISIBLE);

                spineer_refuse_vacine();

               /* dialog.dismiss();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Dialog_Refuse_Vaccine();
                    }
                }, 1500);*/


            }
        });


        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sp_inside_outside_council.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.selectVaccRefusalPrompt, Toast.LENGTH_LONG).show();
                    return;
                }


                if (serviceLocation.showCurrentLocation() == true) {
                    latitude = serviceLocation.getLatitude();
                    longitude = serviceLocation.getLongitude();
                    Log.d("000159", " latitude: " + latitude);
                    Log.d("000159", " longitude: " + longitude);
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
                            Log.d("000159", "  Last Latitude: " + jsonObject.getString("lat"));
                            Log.d("000159", "  Last Longitude: " + jsonObject.getString("lng"));

                            latitude = Double.parseDouble(jsonObject.getString("lat"));
                            longitude = Double.parseDouble(jsonObject.getString("lng"));

                            Toast.makeText(ctx, R.string.dataGPS, Toast.LENGTH_SHORT).show();
                        } else {
                            latitude = Double.parseDouble("0.0");
                            longitude = Double.parseDouble("0.0");
                            Toast.makeText(ctx, R.string.notDataGPS, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.d("000159", "Read CVACCINE Error: " + e.getMessage());
                    }
                }


                try {

                    Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();

                    final JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("vaccine_place", "" + "refuse_vaccine");
                    jobj.put("refuse_reason", "" + String.valueOf(sp_inside_outside_council.getSelectedItem().toString()));
                    jobj.put("refuse_reason_pos", "" + String.valueOf(sp_inside_outside_council.getSelectedItemPosition() - 1));


                    final String added_on = String.valueOf(System.currentTimeMillis());

                    String ans1 = "insert into CVACCINATION (member_uid,vaccine_id, record_data, type, due_date,vaccinated_on,image_location,metadata,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + child_uid + "'," +
                            "'" + mData[index_val][0] + "'," +
                            "'" + TodayDate + "'," +
                            "'" + "3" + "'," +
                            "'" + arrayListDate.get(index_val) + "'," +
                            "'" + TodayDate + "'," +
                            "'" + TodayDate + "'," +
                            "'" + jobj + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000159", "Data: " + ans1);
                    Log.d("000159", "Query: " + res);

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

                        sendPostRequest_RefuseVaccine(child_uid, mData[index_val][0], TodayDate, String.valueOf(jobj), login_useruid, added_on);
                    } else {
                    }
                    //imageocation o be added


                } catch (Exception e) {
                    Log.d("000159", " Error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            dialog.dismiss();
                            Intent intent = new Intent(ctx, Child_HifazitiTeekeyDueDateList_Activity.class);
                            intent.putExtra("u_id", child_uid);
                            intent.putExtra("child_name", child_name);
                            intent.putExtra("child_gender", child_gender);
                            startActivity(intent);
                        }
                    },2000);
                }


            }
        });

    }

    private void sendPostRequest(final String member_uid, final String vacine_uid, final String record_data,
                                 final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/child/vaccinations";

        Log.d("000159", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {
                    //   Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000159", "response1    " + response);
                        //Toast.makeText(ctx, "Data has been saved", Toast.LENGTH_SHORT).show();

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE CVACCINATION SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + child_uid + "'AND added_on= '" + added_on + "'AND vaccine_id= '" + vacine_uid + "'";
                        ls.executeNonQuery(update_record);
                        Toast tt  =Toast.makeText(ctx, R.string.vaccineDataSynced, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();
                    } else {
                        Log.d("000159", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(ctx, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000159", " Error: " + e.getMessage());
                    Toast tt  =Toast.makeText(ctx, R.string.vaccineDataSynced, Toast.LENGTH_SHORT);
                    tt.setGravity(Gravity.CENTER, 0, 0);
                    tt.show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("000159", "onErrorResponse: " + error.getMessage());
                Toast tt  =Toast.makeText(ctx, R.string.vaccineDataSynced, Toast.LENGTH_SHORT);
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
                params.put("vaccinated_on", TodayDate);
                params.put("image_location", added_by);
                params.put("metadata", data);
                params.put("added_by", added_by);
                params.put("added_on", added_on);


                Log.d("000159", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000159", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }

    private void sendPostRequest_RefuseVaccine(final String member_uid, final String vacine_uid, final String record_data,
                                               final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/child/vaccinations";

        Log.d("000159", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {
                    //   Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000159", "response1    " + response);
                        //  Toast.makeText(ctx, "Data has been saved", Toast.LENGTH_SHORT).show();

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE CVACCINATION SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + child_uid + "'AND added_on= '" + added_on + "'AND vaccine_id= '" + vacine_uid + "'";
                        ls.executeNonQuery(update_record);

                        Toast tt  =Toast.makeText(ctx, R.string.vaccineDataSynced, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();

                    } else {
                        Log.d("000159", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        //   Toast.makeText(ctx, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000159", " Error: " + e.getMessage());
                    Toast tt  =Toast.makeText(ctx, R.string.vaccineDataSynced, Toast.LENGTH_SHORT);
                    tt.setGravity(Gravity.CENTER, 0, 0);
                    tt.show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("000159", "onErrorResponse: " + error.getMessage());
                //Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast tt  =Toast.makeText(ctx, R.string.vaccineDataSynced, Toast.LENGTH_SHORT);
                tt.setGravity(Gravity.CENTER, 0, 0);
                tt.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("member_id", member_uid);
                params.put("vaccine_id", vacine_uid);
                params.put("type", "3");
                params.put("record_data", record_data);
                params.put("vaccinated_on", TodayDate);
                params.put("image_location", added_by);
                params.put("metadata", data);
                params.put("added_by", added_by);
                params.put("added_on", added_on);


                Log.d("000159", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000159", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }


    private void spineer_data() {

        // Select sp_azdawaji_hasiyat
        final ArrayAdapter<CharSequence> adptr_council = ArrayAdapter.createFromResource(this, R.array.array_inside_outside_council, android.R.layout.simple_spinner_item);
        adptr_council.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        sp_inside_outside_council.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_council,
                        R.layout.spinner_azdawaji_hasiyat_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));


        sp_inside_outside_council.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void spineer_refuse_vacine() {

        // Select sp_azdawaji_hasiyat
        final ArrayAdapter<CharSequence> adptr_refuse = ArrayAdapter.createFromResource(this, R.array.array_refuse_vaccine, android.R.layout.simple_spinner_item);
        adptr_refuse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        sp_inside_outside_council.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_refuse,
                        R.layout.spinner_azdawaji_hasiyat_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));


        sp_inside_outside_council.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


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

        Log.d("000159", "DOB: " + mData[0][1]);

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

            Log.d("000159", "Saal: " + String.valueOf(dob_years));
            Log.d("000159", "Maah: " + String.valueOf(dob_months));

            child_age = String.valueOf(dob_years) + " سال " + String.valueOf(dob_months) + " مہ ";
            txt_age.setText(child_age);
//            Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            Log.d("000159", "DOB Error: " + e.getMessage());
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

              /* String[][] mData_VacDueDate = ls.executeReader("Select due_date from VACCINES where name ='"+vaccine_name+"'");
                Log.d("000159", "mData_VacDueDate" + mData_VacDueDate[0][0]);
                vaccine_duedate=mData_VacDueDate[0][0];*/

                mData = ls.executeReader("Select uid,name,due_date from VACCINES where due_date='" + vaccine_duedate + "'");
                Log.d("000159", "Data" + String.valueOf(mData.length));


                String[][] mDataa = ls.executeReader("Select dob from MEMBER where uid = '" + child_uid + "'");
                Log.d("000159", "DOB:" + mDataa[0][0]);
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
                Log.d("000159", "DaysDiff: " + String.valueOf(diffInDays));

                if (mData != null) {
                    HashMap<String, String> map;
                    for (int i = 0; i < mData.length; i++) {


                        if (diffInDays == Integer.parseInt(mData[i][2])) {

                            Calendar cvac = new GregorianCalendar();
                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                            cvac.setTime(dateFormatcvac.parse(mDataa[0][0]));
                            cvac.add(Calendar.DATE, Integer.parseInt(mData[i][2]));
                            Date dvac = cvac.getTime();
                            dateFormat.format(dvac.getTime());

                            Log.d("000159", "DayDiff IF 1: " + dateFormat.format(dvac.getTime()));

                            String[][] mDatavac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                            // Log.d("000159", "Type: " +mDatavac.length);
                            if (mDatavac != null) {
                                String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                if (mData_refuse != null) {
                                    arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_refuse[0][0]);
                                    Log.d("000159", "1000: ");
                                } else {
                                    arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mDatavac[0][0]);
                                    Log.d("000159", "1: ");
                                }

                            } else {
                                arrayListVaccine.add(i, mData[i][1] + ",1");
                                Log.d("000159", "2: ");
                            }
                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                            Log.d("000159", "Vaccines Name 1: " + mData[i][1]);


                        }

                        /*else if (diffInDays < Integer.parseInt(mData[i][2]) && diffInDays > (Integer.parseInt(mData[i][2]) - 15)) {
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
                                    arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                    Log.d("000159", "101: ");
                                } else {
                                    arrayListVaccine.add(i, mData[i][1] + ",0"+ "," + mDatavac[0][0]);
                                    Log.d("000159", "3: ");
                                }
                            } else {
                                arrayListVaccine.add(i, mData[i][1] + ",1");
                                Log.d("000159", "4: ");
                            }
                        }*/

                        else if (diffInDays < Integer.parseInt(mData[i][2])) {

                            Log.d("000159", "DAYS LESS THAN DUE DATEEE: " + diffInDays + " < " + mData[i][2]);
                            Log.d("000159", "Vaccines Name 2: " + mData[i][1]);

                            switch (mData[i][1]) {
                                case "OPV-2":
                                    String[][] mData_opv1 = ls.executeReader("Select t1.name ,t0.vaccinated_on from CVACCINATION t0" +
                                            " INNER JOIN VACCINES t1 ON t0.vaccine_id=t1.uid" +
                                            " where t0.member_uid = '" + child_uid + "' AND t0.vaccine_id = 'c6a25e916636f934'");
                                    if (mData_opv1 != null) {
                                        Log.d("000159", "OPV-1 PERFORMED : ");
                                        Log.d("000159", "Vaccinated VACCINE : " + mData_opv1[0][0]);
                                        Log.d("000159", "Vaccinated Date : " + mData_opv1[0][1]);

                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = 'c6a25e916636f934' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_refuse[0][0]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "OPV 1 DayDiff REFUSE : " + dateFormat.format(dvac.getTime()));

                                            // arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                            Log.d("000159", "OPV 1 REFUSE PERFORMED: ");
                                        } else {
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_opv1[0][1]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "OPV 1 DayDiff : " + dateFormat.format(dvac.getTime()));

                                            // arrayListVaccine.add(i, mData[i][1] + ",0"+ "," + mData_opv2[0][1]);
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                                            Log.d("000159", "OPV 1 REFUSE NOT  PERFORMED: ");
                                        }

                                        String[][] mData_opv2 = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                        if (mData_opv2 != null) {
                                            String[][] mData_opv2_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                            if (mData_opv2_refuse != null) {
                                                arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_opv2_refuse[0][0]);
                                                Log.d("000159", "OPV-2 PERFORMED REFUSE VAC : ");
                                            } else {
                                                arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mData_opv2[0][0]);
                                                Log.d("000159", "OPV-2 PERFORMED : ");
                                            }
                                        } else {
                                            arrayListVaccine.add(i, mData[i][1] + ",3");
                                            Log.d("000159", "OPV-2 NOT PERFORMED: ");
                                        }

                                    } else {
                                        Log.d("000159", "ELSEEEEEE OPV-1 NOT PERFORMED: ");
                                        arrayListVaccine.add(i, mData[i][1] + ",3");
                                        arrayListDate.add(i, "OPV-1 Pending");
                                    }
                                    break;

                                case "Rota-2":
                                    String[][] mData_rota1 = ls.executeReader("Select t1.name ,t0.vaccinated_on from CVACCINATION t0" +
                                            " INNER JOIN VACCINES t1 ON t0.vaccine_id=t1.uid" +
                                            " where t0.member_uid = '" + child_uid + "' AND t0.vaccine_id = '63f3ed284f597bc8'");
                                    if (mData_rota1 != null) {
                                        Log.d("000159", "ROTA-1 PERFORMED : ");
                                        Log.d("000159", "Vaccinated VACCINE : " + mData_rota1[0][0]);
                                        Log.d("000159", "Vaccinated Date : " + mData_rota1[0][1]);

                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '63f3ed284f597bc8' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            //  arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_refuse[0][0]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "ROTA 1 DayDiff REFUSE : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                            Log.d("000159", "ROTA 1 REFUSE PERFORMED: ");
                                        } else {
                                            // arrayListVaccine.add(i, mData[i][1] + ",0"+ "," + mData_rota1[0][1]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_rota1[0][1]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "ROTA 1 DayDiff : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                                            Log.d("000159", "ROTA 1 REFUSE NOT  PERFORMED: ");
                                        }

                                        String[][] mData_rota2 = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                        if (mData_rota2 != null) {
                                            String[][] mData_rota_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                            if (mData_rota_refuse != null) {
                                                arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_rota_refuse[0][0]);
                                                Log.d("000159", "ROTA-2 PERFORMED REFUSE VAC : ");
                                            } else {
                                                arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mData_rota2[0][0]);
                                                Log.d("000159", "ROTA-2 PERFORMED : ");
                                            }

                                        } else {
                                            arrayListVaccine.add(i, mData[i][1] + ",3");
                                            Log.d("000159", "ROTA-2 NOT PERFORMED: ");
                                        }

                                    } else {

                                        Log.d("000159", "ELSEEEEEE ROTA-1 NOT PERFORMED: ");
                                        arrayListVaccine.add(i, mData[i][1] + ",3");
                                        arrayListDate.add(i, "Rota-1 Pending");
                                    }
                                    break;

                                case "PCVIO-2":
                                    String[][] mData_pcvio1 = ls.executeReader("Select t1.name ,t0.vaccinated_on from CVACCINATION t0" +
                                            " INNER JOIN VACCINES t1 ON t0.vaccine_id=t1.uid" +
                                            " where t0.member_uid = '" + child_uid + "' AND t0.vaccine_id = 'e68d18affc6f9e5a'");
                                    if (mData_pcvio1 != null) {
                                        Log.d("000159", "PCVIO-1 PERFORMED : ");
                                        Log.d("000159", "Vaccinated VACCINE : " + mData_pcvio1[0][0]);
                                        Log.d("000159", "Vaccinated Date : " + mData_pcvio1[0][1]);
                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = 'e68d18affc6f9e5a' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            // arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_refuse[0][0]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "PCVIO 1 DayDiff REFUSE : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                            Log.d("000159", "PCVIO 1 REFUSE PERFORMED: ");
                                        } else {
                                            //   arrayListVaccine.add(i, mData[i][1] + ",0"+ "," + mData_pcvio1[0][1]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_pcvio1[0][1]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "PCVIO 1 DayDiff : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                                            Log.d("000159", "PCVIO 1 REFUSE NOT  PERFORMED: ");
                                        }

                                        String[][] mData_pcvio2 = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                        if (mData_pcvio2 != null) {
                                            String[][] mData_PCVIO2_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                            if (mData_PCVIO2_refuse != null) {
                                                arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_PCVIO2_refuse[0][0]);
                                                Log.d("000159", "PCVIO-2 PERFORMED REFUSE VAC : ");
                                            } else {
                                                arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mData_pcvio2[0][0]);
                                                Log.d("000159", "PCVIO-2 PERFORMED : ");
                                            }

                                        } else {
                                            arrayListVaccine.add(i, mData[i][1] + ",3");
                                            Log.d("000159", "PCVIO-2 NOT PERFORMED: ");
                                        }

                                    } else {

                                        Log.d("000159", "ELSEEEEEE PCVIO-1 NOT PERFORMED: ");
                                        arrayListVaccine.add(i, mData[i][1] + ",3");
                                        arrayListDate.add(i, "PCVIO-1 Pending");
                                    }
                                    break;

                                case "Penta-2":
                                    String[][] mData_penta1 = ls.executeReader("Select t1.name ,t0.vaccinated_on from CVACCINATION t0" +
                                            " INNER JOIN VACCINES t1 ON t0.vaccine_id=t1.uid" +
                                            " where t0.member_uid = '" + child_uid + "' AND t0.vaccine_id = 'ff1e5aebe8d92ebe'");

                                    if (mData_penta1 != null) {
                                        Log.d("000159", "PENTA-1 PERFORMED : ");
                                        Log.d("000159", "Vaccinated VACCINE : " + mData_penta1[0][0]);
                                        Log.d("000159", "Vaccinated Date : " + mData_penta1[0][1]);

                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = 'ff1e5aebe8d92ebe' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            //  arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_refuse[0][0]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_refuse[0][0]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "PENTA 1 DayDiff REFUSE : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                            Log.d("000159", "PENTA 1 REFUSE PERFORMED: ");
                                        } else {
                                            //  arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mData_penta1[0][1]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_penta1[0][1]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "PENTA 1 DayDiff : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                                            Log.d("000159", "PENTA 1 REFUSE NOT  PERFORMED: ");
                                        }

                                        String[][] mData_Penta2 = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                        if (mData_Penta2 != null) {
                                            String[][] mData_opv2_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                            if (mData_opv2_refuse != null) {
                                                arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_opv2_refuse[0][0]);
                                                Log.d("000159", "Penta-2 PERFORMED REFUSE VAC : ");
                                            } else {
                                                arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mData_Penta2[0][0]);
                                                Log.d("000159", "PENTA-2 PERFORMED : ");
                                            }

                                        } else {
                                            arrayListVaccine.add(i, mData[i][1] + ",3");
                                            Log.d("000159", "OPV-2 NOT PERFORMED: ");
                                        }

                                    } else {
                                        Log.d("000159", "ELSEEEEEE PENTA-1 NOT PERFORMED: ");
                                        arrayListVaccine.add(i, mData[i][1] + ",3");
                                        arrayListDate.add(i, "Penta-1 Pending");
                                    }
                                    break;

                                case "OPV-3":
                                    String[][] mData_opv2 = ls.executeReader("Select t1.name ,t0.vaccinated_on from CVACCINATION t0" +
                                            " INNER JOIN VACCINES t1 ON t0.vaccine_id=t1.uid" +
                                            " where t0.member_uid = '" + child_uid + "' AND t0.vaccine_id = 'ed9955a9bac861ad'");
                                    if (mData_opv2 != null) {
                                        Log.d("000159", "OPV-2 PERFORMED : ");
                                        Log.d("000159", "Vaccinated VACCINE : " + mData_opv2[0][0]);
                                        Log.d("000159", "Vaccinated Date : " + mData_opv2[0][1]);

                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = 'ed9955a9bac861ad' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            //      arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_refuse[0][0]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "OPV-2 DayDiff REFUSE : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                            Log.d("000159", "OPV-2 REFUSE PERFORMED: ");
                                        } else {
                                            //   arrayListVaccine.add(i, mData[i][1] + ",0"+ "," + mData_opv2[0][1]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_opv2[0][1]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "OPV-2 DayDiff : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                                            Log.d("000159", "OPV-2 REFUSE NOT  PERFORMED: ");
                                        }


                                        String[][] mData_opv3 = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                        if (mData_opv3 != null) {
                                            String[][] mData_opv3_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                            if (mData_opv3_refuse != null) {
                                                arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_opv3_refuse[0][0]);
                                                Log.d("000159", "OPV-3 PERFORMED REFUSE VAC : ");
                                            } else {
                                                arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mData_opv2[0][0]);
                                                Log.d("000159", "OPV-3 PERFORMED : ");
                                            }

                                        } else {
                                            arrayListVaccine.add(i, mData[i][1] + ",3");
                                            Log.d("000159", "OPV-2 NOT PERFORMED: ");
                                        }

                                    } else {

                                        Log.d("000159", "ELSEEEEEE OPV-2 NOT PERFORMED: ");
                                        arrayListVaccine.add(i, mData[i][1] + ",3");
                                        arrayListDate.add(i, "OPV-2 Pending");
                                    }
                                    break;

                                case "Rota-3":
                                    String[][] mData_rota2 = ls.executeReader("Select t1.name ,t0.vaccinated_on from CVACCINATION t0" +
                                            " INNER JOIN VACCINES t1 ON t0.vaccine_id=t1.uid" +
                                            " where t0.member_uid = '" + child_uid + "' AND t0.vaccine_id = '29cf14688dc4b5d3'");
                                    if (mData_rota2 != null) {
                                        Log.d("000159", "Rota-2 PERFORMED : ");
                                        Log.d("000159", "Vaccinated VACCINE : " + mData_rota2[0][0]);
                                        Log.d("000159", "Vaccinated Date : " + mData_rota2[0][1]);

                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '29cf14688dc4b5d3' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            //     arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_refuse[0][0]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "ROTA 2 DayDiff REFUSE : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                            Log.d("000159", "ROTA 2 REFUSE PERFORMED: ");
                                        } else {
                                            //    arrayListVaccine.add(i, mData[i][1] + ",0"+ "," + mData_rota2[0][1]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_rota2[0][1]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "ROTA 2 DayDiff : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                                            Log.d("000159", "ROTA 2 REFUSE NOT  PERFORMED: ");
                                        }

                                        String[][] mData_Rota3 = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                        if (mData_Rota3 != null) {
                                            String[][] mData_Rota3_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                            if (mData_Rota3_refuse != null) {
                                                arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_Rota3_refuse[0][0]);
                                                Log.d("000159", "ROTA-3 PERFORMED REFUSE VAC : ");
                                            } else {
                                                arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mData_Rota3[0][0]);
                                                Log.d("000159", "ROTA-3 PERFORMED : ");
                                            }

                                        } else {
                                            arrayListVaccine.add(i, mData[i][1] + ",3");
                                            Log.d("000159", "ROTA-3 NOT PERFORMED: ");
                                        }

                                    } else {

                                        Log.d("000159", "ELSEEEEEE Rota-2 NOT PERFORMED: ");
                                        arrayListVaccine.add(i, mData[i][1] + ",3");
                                        arrayListDate.add(i, "Rota-2 Pending");
                                    }
                                    break;


                                case "PCVIO-3":
                                    String[][] mData_PCVIO2 = ls.executeReader("Select t1.name ,t0.vaccinated_on from CVACCINATION t0" +
                                            " INNER JOIN VACCINES t1 ON t0.vaccine_id=t1.uid" +
                                            " where t0.member_uid = '" + child_uid + "' AND t0.vaccine_id = '9b08c2e606b51d40'");
                                    if (mData_PCVIO2 != null) {
                                        Log.d("000159", "PCVIO-2 PERFORMED : ");
                                        Log.d("000159", "Vaccinated VACCINE : " + mData_PCVIO2[0][0]);
                                        Log.d("000159", "Vaccinated Date : " + mData_PCVIO2[0][1]);

                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '9b08c2e606b51d40' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            //         arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_refuse[0][0]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "PCVIO-2 DayDiff REFUSE : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                            Log.d("000159", "PCVIO-2 REFUSE PERFORMED: ");
                                        } else {
                                            //     arrayListVaccine.add(i, mData[i][1] + ",0"+ "," + mData_PCVIO2[0][1]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_PCVIO2[0][1]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "PCVIO-2 DayDiff : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                                            Log.d("000159", "PCVIO-2 REFUSE NOT  PERFORMED: ");
                                        }


                                        String[][] mData_PCVIO3 = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                        if (mData_PCVIO3 != null) {
                                            String[][] mData_PCVIO2_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                            if (mData_PCVIO2_refuse != null) {
                                                arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_PCVIO2_refuse[0][0]);
                                                Log.d("000159", "PCVIO-3 PERFORMED REFUSE VAC : ");
                                            } else {
                                                arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mData_PCVIO3[0][0]);
                                                Log.d("000159", "PCVIO-3 PERFORMED : ");
                                            }

                                        } else {
                                            arrayListVaccine.add(i, mData[i][1] + ",3");
                                            Log.d("000159", "PCVIO-3 NOT PERFORMED: ");
                                        }

                                    } else {

                                        Log.d("000159", "ELSEEEEEE PCVIO-2 NOT PERFORMED: ");
                                        arrayListVaccine.add(i, mData[i][1] + ",3");
                                        arrayListDate.add(i, "PCVIO-2 Pending");
                                    }
                                    break;

                                case "Penta-3":
                                    String[][] mData_Penta2 = ls.executeReader("Select t1.name ,t0.vaccinated_on from CVACCINATION t0" +
                                            " INNER JOIN VACCINES t1 ON t0.vaccine_id=t1.uid" +
                                            " where t0.member_uid = '" + child_uid + "' AND t0.vaccine_id = '7c0af8c6db48f56f'");
                                    if (mData_Penta2 != null) {
                                        Log.d("000159", "PENTA-2 PERFORMED : ");
                                        Log.d("000159", "Vaccinated VACCINE : " + mData_Penta2[0][0]);
                                        Log.d("000159", "Vaccinated Date : " + mData_Penta2[0][1]);

                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '7c0af8c6db48f56f' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            //     arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_refuse[0][0]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "PENTA-2 DayDiff REFUSE : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                            Log.d("000159", "PENTA-2 REFUSE PERFORMED: ");
                                        } else {
                                            //    arrayListVaccine.add(i, mData[i][1] + ",0"+ "," + mData_Penta2[0][1]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_Penta2[0][1]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "PENTA-2 DayDiff : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                                            Log.d("000159", "PENTA-2 REFUSE NOT  PERFORMED: ");
                                        }

                                        String[][] mData_Penta3 = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                        if (mData_Penta3 != null) {
                                            String[][] mData_Penta3_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                            if (mData_Penta3_refuse != null) {
                                                arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_Penta3_refuse[0][0]);
                                                Log.d("000159", "PENTA-3 PERFORMED REFUSE VAC : ");
                                            } else {
                                                arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mData_Penta3[0][0]);
                                                Log.d("000159", "Penta-3 PERFORMED : ");
                                            }

                                        } else {
                                            arrayListVaccine.add(i, mData[i][1] + ",3");
                                            Log.d("000159", "PENTA-3 NOT PERFORMED: ");
                                        }

                                    } else {
                                        Log.d("000159", "ELSEEEEEE PENTA-2 NOT PERFORMED: ");
                                        arrayListVaccine.add(i, mData[i][1] + ",3");
                                        arrayListDate.add(i, "Penta-2 Pending");
                                    }
                                    break;


                                case "Measles-2":

                                    String[][] mData_Measles1 = ls.executeReader("Select t1.name ,t0.vaccinated_on from CVACCINATION t0" +
                                            " INNER JOIN VACCINES t1 ON t0.vaccine_id=t1.uid" +
                                            " where t0.member_uid = '" + child_uid + "' AND t0.vaccine_id = '4263ac05ab335e9c'");
                                    if (mData_Measles1 != null) {
                                        Log.d("000159", "Measles1 PERFORMED : ");
                                        Log.d("000159", "Vaccinated VACCINE : " + mData_Measles1[0][0]);
                                        Log.d("000159", "Vaccinated Date : " + mData_Measles1[0][1]);

                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '4263ac05ab335e9c' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            //     arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);

                                            if (diffInDays < 420) {
                                                Log.d("000159", "DAY LESS THAN 420 !!!: ");

                                                Calendar cvac = new GregorianCalendar();
                                                java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                                cvac.setTime(dateFormatcvac.parse(mDataa[0][0]));
                                                cvac.add(Calendar.DATE, Integer.parseInt(mData[i][2]));
                                                Date dvac = cvac.getTime();
                                                dateFormat.format(dvac.getTime());
                                                Log.d("000159", "Measles-1 DayDiff LESS than 420 : " + dateFormat.format(dvac.getTime()));

                                                arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                            } else {
                                                Log.d("000159", "DAY GREATER  THAN 420 !!!: ");
                                                Calendar cvac = new GregorianCalendar();
                                                java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                                cvac.setTime(dateFormatcvac.parse(mData_refuse[0][0]));
                                                cvac.add(Calendar.DATE, 30);
                                                Date dvac = cvac.getTime();
                                                dateFormat.format(dvac.getTime());
                                                Log.d("000159", "Measles-1 DayDiff GREATER than 420 : " + dateFormat.format(dvac.getTime()));

                                                arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                                            }

                                            Log.d("000159", "Measles-1 REFUSE PERFORMED: ");

                                        } else {

                                            if (diffInDays < 420) {
                                                Log.d("000159", "DAYS LESS THAN 420 !!!: ");

                                                Calendar cvac = new GregorianCalendar();
                                                java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                                cvac.setTime(dateFormatcvac.parse(mDataa[0][0]));
                                                cvac.add(Calendar.DATE, Integer.parseInt(mData[i][2]));
                                                Date dvac = cvac.getTime();
                                                dateFormat.format(dvac.getTime());
                                                Log.d("000159", "Measles-1 DayDiff LESS than 420 : " + dateFormat.format(dvac.getTime()));

                                                arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                            } else {
                                                Log.d("000159", "DAYS GREATER  THAN 420 !!!: ");
                                                Calendar cvac = new GregorianCalendar();
                                                java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                                cvac.setTime(dateFormatcvac.parse(mData_Measles1[0][1]));
                                                cvac.add(Calendar.DATE, 30);
                                                Date dvac = cvac.getTime();
                                                dateFormat.format(dvac.getTime());
                                                Log.d("000159", "Measles-1 DayDiff GREATER than 420 : " + dateFormat.format(dvac.getTime()));

                                                arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                                            }
                                            Log.d("000159", "Measles-1 REFUSE NOT  PERFORMED: ");
                                        }

                                        String[][] mData_Measles2 = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                        if (mData_Measles2 != null) {
                                            String[][] mData_Measles2_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                            if (mData_Measles2_refuse != null) {
                                                arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_Measles2_refuse[0][0]);
                                                Log.d("000159", "Measles-2 PERFORMED REFUSE VAC : ");
                                            } else {
                                                arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mData_Measles2[0][0]);
                                                Log.d("000159", "Measles-2 PERFORMED : ");
                                            }

                                        } else {
                                            arrayListVaccine.add(i, mData[i][1] + ",3");
                                            Log.d("000159", "Measles-2 NOT PERFORMED: ");
                                        }

                                    } else {
                                        Log.d("000159", "ELSEEEEEE Measles-1 NOT PERFORMED: ");
                                        arrayListVaccine.add(i, mData[i][1] + ",3");
                                        arrayListDate.add(i, "Measles-1 Pending");
                                    }

                                    break;

                                default:
                                    Calendar cvac = new GregorianCalendar();
                                    java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                    cvac.setTime(dateFormatcvac.parse(mDataa[0][0]));
                                    cvac.add(Calendar.DATE, Integer.parseInt(mData[i][2]));
                                    Date dvac = cvac.getTime();
                                    dateFormat.format(dvac.getTime());
                                    Log.d("000159", "DayDiff LESS THAN ELSE  " + dateFormat.format(dvac.getTime()));
                                    arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                    String[][] mDatavac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                    if (mDatavac != null) {

                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_refuse[0][0]);
                                            Log.d("000159", mData[i][1] + " REFUSED PERFORMED");
                                        } else {
                                            arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mDatavac[0][0]);
                                            Log.d("000159", mData[i][1] + " VACCINE PERFORMED");
                                        }

                                    } else {
                                        arrayListVaccine.add(i, mData[i][1] + ",3");
                                        Log.d("000159", mData[i][1] + " VACCINE NOT PERFORMED");
                                    }

                                    Log.d("000159", "DEFAULTTT : ");
                                    break;
                            }

                        } else if (diffInDays > Integer.parseInt(mData[i][2])) {

                            Log.d("000159", "DAYS GREATER THAN DUE DATEEE: " + diffInDays + " > " + mData[i][2]);
                            Log.d("000159", "Vaccines Name 3: " + mData[i][1]);

                            switch (mData[i][1]) {
                                case "OPV-2":
                                    String[][] mData_opv1 = ls.executeReader("Select t1.name ,t0.vaccinated_on from CVACCINATION t0" +
                                            " INNER JOIN VACCINES t1 ON t0.vaccine_id=t1.uid" +
                                            " where t0.member_uid = '" + child_uid + "' AND t0.vaccine_id = 'c6a25e916636f934'");
                                    if (mData_opv1 != null) {
                                        Log.d("000159", "OPV-1 PERFORMED : ");
                                        Log.d("000159", "Vaccinated VACCINE : " + mData_opv1[0][0]);
                                        Log.d("000159", "Vaccinated Date : " + mData_opv1[0][1]);

                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = 'c6a25e916636f934' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_refuse[0][0]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "OPV 1 DayDiff REFUSE : " + dateFormat.format(dvac.getTime()));

                                            // arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                            Log.d("000159", "OPV 1 REFUSE PERFORMED: ");
                                        } else {
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_opv1[0][1]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "OPV 1 DayDiff : " + dateFormat.format(dvac.getTime()));

                                            // arrayListVaccine.add(i, mData[i][1] + ",0"+ "," + mData_opv2[0][1]);
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                                            Log.d("000159", "OPV 1 REFUSE NOT  PERFORMED: ");
                                        }

                                        String[][] mData_opv2 = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                        if (mData_opv2 != null) {
                                            String[][] mData_opv2_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                            if (mData_opv2_refuse != null) {
                                                arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_opv2_refuse[0][0]);
                                                Log.d("000159", "OPV-2 PERFORMED REFUSE VAC : ");
                                            } else {
                                                arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mData_opv2[0][0]);
                                                Log.d("000159", "OPV-2 PERFORMED : ");
                                            }
                                        } else {
                                            arrayListVaccine.add(i, mData[i][1] + ",2");
                                            Log.d("000159", "OPV-2 NOT PERFORMED: ");
                                        }

                                    } else {
                                        Log.d("000159", "ELSEEEEEE OPV-1 NOT PERFORMED: ");
                                        arrayListVaccine.add(i, mData[i][1] + ",3");
                                        arrayListDate.add(i, "OPV-1 Pending");
                                    }
                                    break;

                                case "Rota-2":
                                    String[][] mData_rota1 = ls.executeReader("Select t1.name ,t0.vaccinated_on from CVACCINATION t0" +
                                            " INNER JOIN VACCINES t1 ON t0.vaccine_id=t1.uid" +
                                            " where t0.member_uid = '" + child_uid + "' AND t0.vaccine_id = '63f3ed284f597bc8'");
                                    if (mData_rota1 != null) {
                                        Log.d("000159", "ROTA-1 PERFORMED : ");
                                        Log.d("000159", "Vaccinated VACCINE : " + mData_rota1[0][0]);
                                        Log.d("000159", "Vaccinated Date : " + mData_rota1[0][1]);

                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '63f3ed284f597bc8' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            //  arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_refuse[0][0]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "ROTA 1 DayDiff REFUSE : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                            Log.d("000159", "ROTA 1 REFUSE PERFORMED: ");
                                        } else {
                                            // arrayListVaccine.add(i, mData[i][1] + ",0"+ "," + mData_rota1[0][1]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_rota1[0][1]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "ROTA 1 DayDiff : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                                            Log.d("000159", "ROTA 1 REFUSE NOT  PERFORMED: ");
                                        }

                                        String[][] mData_rota2 = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                        if (mData_rota2 != null) {
                                            String[][] mData_rota_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                            if (mData_rota_refuse != null) {
                                                arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_rota_refuse[0][0]);
                                                Log.d("000159", "ROTA-2 PERFORMED REFUSE VAC : ");
                                            } else {
                                                arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mData_rota2[0][0]);
                                                Log.d("000159", "ROTA-2 PERFORMED : ");
                                            }

                                        } else {
                                            arrayListVaccine.add(i, mData[i][1] + ",2");
                                            Log.d("000159", "ROTA-2 NOT PERFORMED: ");
                                        }

                                    } else {

                                        Log.d("000159", "ELSEEEEEE ROTA-1 NOT PERFORMED: ");
                                        arrayListVaccine.add(i, mData[i][1] + ",3");
                                        arrayListDate.add(i, "Rota-1 Pending");
                                    }
                                    break;

                                case "PCVIO-2":
                                    String[][] mData_pcvio1 = ls.executeReader("Select t1.name ,t0.vaccinated_on from CVACCINATION t0" +
                                            " INNER JOIN VACCINES t1 ON t0.vaccine_id=t1.uid" +
                                            " where t0.member_uid = '" + child_uid + "' AND t0.vaccine_id = 'e68d18affc6f9e5a'");
                                    if (mData_pcvio1 != null) {
                                        Log.d("000159", "PCVIO-1 PERFORMED : ");
                                        Log.d("000159", "Vaccinated VACCINE : " + mData_pcvio1[0][0]);
                                        Log.d("000159", "Vaccinated Date : " + mData_pcvio1[0][1]);
                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = 'e68d18affc6f9e5a' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            // arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_refuse[0][0]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "PCVIO 1 DayDiff REFUSE : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                            Log.d("000159", "PCVIO 1 REFUSE PERFORMED: ");
                                        } else {
                                            //   arrayListVaccine.add(i, mData[i][1] + ",0"+ "," + mData_pcvio1[0][1]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_pcvio1[0][1]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "PCVIO 1 DayDiff : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                                            Log.d("000159", "PCVIO 1 REFUSE NOT  PERFORMED: ");
                                        }

                                        String[][] mData_pcvio2 = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                        if (mData_pcvio2 != null) {
                                            String[][] mData_PCVIO2_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                            if (mData_PCVIO2_refuse != null) {
                                                arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_PCVIO2_refuse[0][0]);
                                                Log.d("000159", "PCVIO-2 PERFORMED REFUSE VAC : ");
                                            } else {
                                                arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mData_pcvio2[0][0]);
                                                Log.d("000159", "PCVIO-2 PERFORMED : ");
                                            }

                                        } else {
                                            arrayListVaccine.add(i, mData[i][1] + ",2");
                                            Log.d("000159", "PCVIO-2 NOT PERFORMED: ");
                                        }

                                    } else {

                                        Log.d("000159", "ELSEEEEEE PCVIO-1 NOT PERFORMED: ");
                                        arrayListVaccine.add(i, mData[i][1] + ",3");
                                        arrayListDate.add(i, "PCVIO-1 Pending");
                                    }
                                    break;

                                case "Penta-2":
                                    String[][] mData_penta1 = ls.executeReader("Select t1.name ,t0.vaccinated_on from CVACCINATION t0" +
                                            " INNER JOIN VACCINES t1 ON t0.vaccine_id=t1.uid" +
                                            " where t0.member_uid = '" + child_uid + "' AND t0.vaccine_id = 'ff1e5aebe8d92ebe'");

                                    if (mData_penta1 != null) {
                                        Log.d("000159", "PENTA-1 PERFORMED : ");
                                        Log.d("000159", "Vaccinated VACCINE : " + mData_penta1[0][0]);
                                        Log.d("000159", "Vaccinated Date : " + mData_penta1[0][1]);

                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = 'ff1e5aebe8d92ebe' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            //  arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_refuse[0][0]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_refuse[0][0]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "PENTA 1 DayDiff REFUSE : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                            Log.d("000159", "PENTA 1 REFUSE PERFORMED: ");
                                        } else {
                                            //  arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mData_penta1[0][1]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_penta1[0][1]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "PENTA 1 DayDiff : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                                            Log.d("000159", "PENTA 1 REFUSE NOT  PERFORMED: ");
                                        }

                                        String[][] mData_Penta2 = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                        if (mData_Penta2 != null) {
                                            String[][] mData_opv2_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                            if (mData_opv2_refuse != null) {
                                                arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_opv2_refuse[0][0]);
                                                Log.d("000159", "Penta-2 PERFORMED REFUSE VAC : ");
                                            } else {
                                                arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mData_Penta2[0][0]);
                                                Log.d("000159", "PENTA-2 PERFORMED : ");
                                            }

                                        } else {
                                            arrayListVaccine.add(i, mData[i][1] + ",2");
                                            Log.d("000159", "OPV-2 NOT PERFORMED: ");
                                        }

                                    } else {
                                        Log.d("000159", "ELSEEEEEE PENTA-1 NOT PERFORMED: ");
                                        arrayListVaccine.add(i, mData[i][1] + ",3");
                                        arrayListDate.add(i, "Penta-1 Pending");
                                    }
                                    break;

                                case "OPV-3":
                                    String[][] mData_opv2 = ls.executeReader("Select t1.name ,t0.vaccinated_on from CVACCINATION t0" +
                                            " INNER JOIN VACCINES t1 ON t0.vaccine_id=t1.uid" +
                                            " where t0.member_uid = '" + child_uid + "' AND t0.vaccine_id = 'ed9955a9bac861ad'");
                                    if (mData_opv2 != null) {
                                        Log.d("000159", "OPV-2 PERFORMED : ");
                                        Log.d("000159", "Vaccinated VACCINE : " + mData_opv2[0][0]);
                                        Log.d("000159", "Vaccinated Date : " + mData_opv2[0][1]);

                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = 'ed9955a9bac861ad' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            //      arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_refuse[0][0]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "OPV-2 DayDiff REFUSE : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                            Log.d("000159", "OPV-2 REFUSE PERFORMED: ");
                                        } else {
                                            //   arrayListVaccine.add(i, mData[i][1] + ",0"+ "," + mData_opv2[0][1]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_opv2[0][1]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "OPV-2 DayDiff : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                                            Log.d("000159", "OPV-2 REFUSE NOT  PERFORMED: ");
                                        }


                                        String[][] mData_opv3 = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                        if (mData_opv3 != null) {
                                            String[][] mData_opv3_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                            if (mData_opv3_refuse != null) {
                                                arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_opv3_refuse[0][0]);
                                                Log.d("000159", "OPV-3 PERFORMED REFUSE VAC : ");
                                            } else {
                                                arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mData_opv2[0][0]);
                                                Log.d("000159", "OPV-3 PERFORMED : ");
                                            }

                                        } else {
                                            arrayListVaccine.add(i, mData[i][1] + ",2");
                                            Log.d("000159", "OPV-2 NOT PERFORMED: ");
                                        }

                                    } else {

                                        Log.d("000159", "ELSEEEEEE OPV-2 NOT PERFORMED: ");
                                        arrayListVaccine.add(i, mData[i][1] + ",3");
                                        arrayListDate.add(i, "OPV-2 Pending");
                                    }
                                    break;

                                case "Rota-3":
                                    String[][] mData_rota2 = ls.executeReader("Select t1.name ,t0.vaccinated_on from CVACCINATION t0" +
                                            " INNER JOIN VACCINES t1 ON t0.vaccine_id=t1.uid" +
                                            " where t0.member_uid = '" + child_uid + "' AND t0.vaccine_id = '29cf14688dc4b5d3'");
                                    if (mData_rota2 != null) {
                                        Log.d("000159", "Rota-2 PERFORMED : ");
                                        Log.d("000159", "Vaccinated VACCINE : " + mData_rota2[0][0]);
                                        Log.d("000159", "Vaccinated Date : " + mData_rota2[0][1]);

                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '29cf14688dc4b5d3' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            //     arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_refuse[0][0]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "ROTA 2 DayDiff REFUSE : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                            Log.d("000159", "ROTA 2 REFUSE PERFORMED: ");
                                        } else {
                                            //    arrayListVaccine.add(i, mData[i][1] + ",0"+ "," + mData_rota2[0][1]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_rota2[0][1]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "ROTA 2 DayDiff : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                                            Log.d("000159", "ROTA 2 REFUSE NOT  PERFORMED: ");
                                        }

                                        String[][] mData_Rota3 = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                        if (mData_Rota3 != null) {
                                            String[][] mData_Rota3_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                            if (mData_Rota3_refuse != null) {
                                                arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_Rota3_refuse[0][0]);
                                                Log.d("000159", "ROTA-3 PERFORMED REFUSE VAC : ");
                                            } else {
                                                arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mData_Rota3[0][0]);
                                                Log.d("000159", "ROTA-3 PERFORMED : ");
                                            }

                                        } else {
                                            arrayListVaccine.add(i, mData[i][1] + ",2");
                                            Log.d("000159", "ROTA-3 NOT PERFORMED: ");
                                        }

                                    } else {

                                        Log.d("000159", "ELSEEEEEE Rota-2 NOT PERFORMED: ");
                                        arrayListVaccine.add(i, mData[i][1] + ",3");
                                        arrayListDate.add(i, "Rota-2 Pending");
                                    }
                                    break;


                                case "PCVIO-3":
                                    String[][] mData_PCVIO2 = ls.executeReader("Select t1.name ,t0.vaccinated_on from CVACCINATION t0" +
                                            " INNER JOIN VACCINES t1 ON t0.vaccine_id=t1.uid" +
                                            " where t0.member_uid = '" + child_uid + "' AND t0.vaccine_id = '9b08c2e606b51d40'");
                                    if (mData_PCVIO2 != null) {
                                        Log.d("000159", "PCVIO-2 PERFORMED : ");
                                        Log.d("000159", "Vaccinated VACCINE : " + mData_PCVIO2[0][0]);
                                        Log.d("000159", "Vaccinated Date : " + mData_PCVIO2[0][1]);

                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '9b08c2e606b51d40' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            //         arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_refuse[0][0]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "PCVIO-2 DayDiff REFUSE : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                            Log.d("000159", "PCVIO-2 REFUSE PERFORMED: ");
                                        } else {
                                            //     arrayListVaccine.add(i, mData[i][1] + ",0"+ "," + mData_PCVIO2[0][1]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_PCVIO2[0][1]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "PCVIO-2 DayDiff : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                                            Log.d("000159", "PCVIO-2 REFUSE NOT  PERFORMED: ");
                                        }


                                        String[][] mData_PCVIO3 = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                        if (mData_PCVIO3 != null) {
                                            String[][] mData_PCVIO2_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                            if (mData_PCVIO2_refuse != null) {
                                                arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_PCVIO2_refuse[0][0]);
                                                Log.d("000159", "PCVIO-3 PERFORMED REFUSE VAC : ");
                                            } else {
                                                arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mData_PCVIO3[0][0]);
                                                Log.d("000159", "PCVIO-3 PERFORMED : ");
                                            }

                                        } else {
                                            arrayListVaccine.add(i, mData[i][1] + ",2");
                                            Log.d("000159", "PCVIO-3 NOT PERFORMED: ");
                                        }

                                    } else {

                                        Log.d("000159", "ELSEEEEEE PCVIO-2 NOT PERFORMED: ");
                                        arrayListVaccine.add(i, mData[i][1] + ",3");
                                        arrayListDate.add(i, "PCVIO-2 Pending");
                                    }
                                    break;

                                case "Penta-3":
                                    String[][] mData_Penta2 = ls.executeReader("Select t1.name ,t0.vaccinated_on from CVACCINATION t0" +
                                            " INNER JOIN VACCINES t1 ON t0.vaccine_id=t1.uid" +
                                            " where t0.member_uid = '" + child_uid + "' AND t0.vaccine_id = '7c0af8c6db48f56f'");
                                    if (mData_Penta2 != null) {
                                        Log.d("000159", "PENTA-2 PERFORMED : ");
                                        Log.d("000159", "Vaccinated VACCINE : " + mData_Penta2[0][0]);
                                        Log.d("000159", "Vaccinated Date : " + mData_Penta2[0][1]);

                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '7c0af8c6db48f56f' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            //     arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_refuse[0][0]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "PENTA-2 DayDiff REFUSE : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                            Log.d("000159", "PENTA-2 REFUSE PERFORMED: ");
                                        } else {
                                            //    arrayListVaccine.add(i, mData[i][1] + ",0"+ "," + mData_Penta2[0][1]);
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_Penta2[0][1]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000159", "PENTA-2 DayDiff : " + dateFormat.format(dvac.getTime()));
                                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                                            Log.d("000159", "PENTA-2 REFUSE NOT  PERFORMED: ");
                                        }

                                        String[][] mData_Penta3 = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                        if (mData_Penta3 != null) {
                                            String[][] mData_Penta3_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                            if (mData_Penta3_refuse != null) {
                                                arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_Penta3_refuse[0][0]);
                                                Log.d("000159", "PENTA-3 PERFORMED REFUSE VAC : ");
                                            } else {
                                                arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mData_Penta3[0][0]);
                                                Log.d("000159", "Penta-3 PERFORMED : ");
                                            }

                                        } else {
                                            arrayListVaccine.add(i, mData[i][1] + ",2");
                                            Log.d("000159", "PENTA-3 NOT PERFORMED: ");
                                        }

                                    } else {
                                        Log.d("000159", "ELSEEEEEE PENTA-2 NOT PERFORMED: ");
                                        arrayListVaccine.add(i, mData[i][1] + ",3");
                                        arrayListDate.add(i, "Penta-2 Pending");
                                    }
                                    break;


                                case "Measles-2":

                                    String[][] mData_Measles1 = ls.executeReader("Select t1.name ,t0.vaccinated_on from CVACCINATION t0" +
                                            " INNER JOIN VACCINES t1 ON t0.vaccine_id=t1.uid" +
                                            " where t0.member_uid = '" + child_uid + "' AND t0.vaccine_id = '4263ac05ab335e9c'");
                                    if (mData_Measles1 != null) {
                                        Log.d("000159", "Measles1 PERFORMED : ");
                                        Log.d("000159", "Vaccinated VACCINE : " + mData_Measles1[0][0]);
                                        Log.d("000159", "Vaccinated Date : " + mData_Measles1[0][1]);

                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '4263ac05ab335e9c' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            //     arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);

                                            if (diffInDays < 420) {
                                                Log.d("000159", "DAY LESS THAN 420 !!!: ");

                                                Calendar cvac = new GregorianCalendar();
                                                java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                                cvac.setTime(dateFormatcvac.parse(mDataa[0][0]));
                                                cvac.add(Calendar.DATE, Integer.parseInt(mData[i][2]));
                                                Date dvac = cvac.getTime();
                                                dateFormat.format(dvac.getTime());
                                                Log.d("000159", "Measles-1 DayDiff LESS than 420 : " + dateFormat.format(dvac.getTime()));

                                                arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                            } else {
                                                Log.d("000159", "DAY GREATER  THAN 420 !!!: ");
                                                Calendar cvac = new GregorianCalendar();
                                                java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                                cvac.setTime(dateFormatcvac.parse(mData_refuse[0][0]));
                                                cvac.add(Calendar.DATE, 30);
                                                Date dvac = cvac.getTime();
                                                dateFormat.format(dvac.getTime());
                                                Log.d("000159", "Measles-1 DayDiff GREATER than 420 : " + dateFormat.format(dvac.getTime()));

                                                arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                                            }

                                            Log.d("000159", "Measles-1 REFUSE PERFORMED: ");

                                        } else {

                                            if (diffInDays < 420) {
                                                Log.d("000159", "DAYS LESS THAN 420 !!!: ");

                                                Calendar cvac = new GregorianCalendar();
                                                java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                                cvac.setTime(dateFormatcvac.parse(mDataa[0][0]));
                                                cvac.add(Calendar.DATE, Integer.parseInt(mData[i][2]));
                                                Date dvac = cvac.getTime();
                                                dateFormat.format(dvac.getTime());
                                                Log.d("000159", "Measles-1 DayDiff LESS than 420 : " + dateFormat.format(dvac.getTime()));

                                                arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                            } else {
                                                Log.d("000159", "DAYS GREATER  THAN 420 !!!: ");
                                                Calendar cvac = new GregorianCalendar();
                                                java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                                cvac.setTime(dateFormatcvac.parse(mData_Measles1[0][1]));
                                                cvac.add(Calendar.DATE, 30);
                                                Date dvac = cvac.getTime();
                                                dateFormat.format(dvac.getTime());
                                                Log.d("000159", "Measles-1 DayDiff GREATER than 420 : " + dateFormat.format(dvac.getTime()));

                                                arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                                            }
                                            Log.d("000159", "Measles-1 REFUSE NOT  PERFORMED: ");
                                        }

                                        String[][] mData_Measles2 = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                        if (mData_Measles2 != null) {
                                            String[][] mData_Measles2_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                            if (mData_Measles2_refuse != null) {
                                                arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_Measles2_refuse[0][0]);
                                                Log.d("000159", "Measles-2 PERFORMED REFUSE VAC : ");
                                            } else {
                                                arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mData_Measles2[0][0]);
                                                Log.d("000159", "Measles-2 PERFORMED : ");
                                            }

                                        } else {
                                            arrayListVaccine.add(i, mData[i][1] + ",2");
                                            Log.d("000159", "Measles-2 NOT PERFORMED: ");
                                        }

                                    } else {
                                        Log.d("000159", "ELSEEEEEE Measles-1 NOT PERFORMED: ");
                                        arrayListVaccine.add(i, mData[i][1] + ",3");
                                        arrayListDate.add(i, "Measles-1 Pending");
                                    }

                                    break;

                                default:
                                    Calendar cvac = new GregorianCalendar();
                                    java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                    cvac.setTime(dateFormatcvac.parse(mDataa[0][0]));
                                    cvac.add(Calendar.DATE, Integer.parseInt(mData[i][2]));
                                    Date dvac = cvac.getTime();
                                    dateFormat.format(dvac.getTime());
                                    Log.d("000159", "DayDiff IF 3: " + dateFormat.format(dvac.getTime()));
                                    arrayListDate.add(i, dateFormat.format(dvac.getTime()));

                                    String[][] mDatavac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                    if (mDatavac != null) {

                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            arrayListVaccine.add(i, mData[i][1] + ",5" + "," + mData_refuse[0][0]);
                                            Log.d("000159", mData[i][1] + " REFUSED PERFORMED");
                                        } else {
                                            arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mDatavac[0][0]);
                                            Log.d("000159", mData[i][1] + " VACCINE PERFORMED");
                                        }

                                    } else {
                                        arrayListVaccine.add(i, mData[i][1] + ",2");
                                        Log.d("000159", mData[i][1] + " VACCINE NOT PERFORMED");
                                    }

                                    Log.d("000159", "DEFAULTTT : ");
                                    break;
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
                            Log.d("000159", "DayDiff IF 4: " + dateFormat.format(dvac.getTime()));

                          /*  String[][] mDatavac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                            if (mDatavac != null) {

                                String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                if (mData_refuse != null) {
                                    arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                    Log.d("000159", "104: ");
                                } else {
                                    arrayListVaccine.add(i, mData[i][1] + ",0"+ "," + mDatavac[0][0]);
                                    Log.d("000159", "9: ");
                                }

                            } else {

                                arrayListVaccine.add(i, mData[i][1] + ",4");
                                Log.d("000159", "10 ");
                            }*/

                            Log.d("000159", "Vaccines Name 4: " + mData[i][1]);
                        }
                    }
                } else {
                    Toast.makeText(ctx, "No Data", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.d("000159", "Vacine Err: " + String.valueOf(e.getMessage()));
            }
            adt = new Adt_ChildHifazitiTeekeyRecordList_DueDate(ctx, arrayListVaccine, arrayListDate);
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
