package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_COVIDActivities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
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
import com.rey.material.widget.CheckBox;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNewPatientFormView_Activity extends AppCompatActivity {

    Context ctx = AddNewPatientFormView_Activity.this;

    EditText et_tareekh_indraj, et_refferal_hospital_others, et_patient_name, et_age;
    LinearLayout ll_referal_hospital;

    Spinner spRefHealthFacility, sp_gender;
    Button btn_jamaa_kre;
    RelativeLayout rl_navigation_drawer, rl_home;
    ImageView iv_navigation_drawer, iv_home, iv_editform;
    ProgressBar pbProgress;


    double latitude;
    double longitude;

    String patient_uid, TodayDate;
    String record_date, added_on;

    private int mYear, mMonth, mDay;


    String monthf2, dayf2, yearf2 = "null";
    ServiceLocation serviceLocation;
    String login_useruid;

    CheckBox cb_runynose_haan, cb_runynose_nahi, cb_throat_yes, cb_throat_nahi, cb_fever_yes, cb_fever_nahi, cb_cough_yes, cb_cough_nahi, cb_braething_nahi, cb_braething_yes,
            cb_another_country_haan, cb_another_country_nahi, cb_handshake_haan, cb_handshake_nahi;

    String runny_nose = "-1", throat = "-1", fever = "-1", cough = "-1", breathing = "-1", came_from_another_country = "-1", handshake = "-1", health_facility_uid = "-1";

    long mLastClickTime = 0;

    JSONObject jsonObject;
    Dialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_patient_layout);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, AddNewPatientFormView_Activity.class));

        patient_uid = getIntent().getExtras().getString("patient_uid");
        record_date = getIntent().getExtras().getString("record_date");
        added_on = getIntent().getExtras().getString("added_on");

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences("UserLogin", 0); // 0 - for private mode
            String shared_useruid = prefelse.getString("login_userid", null); // getting String
            login_useruid = shared_useruid;
            Log.d("000331", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000331", "Shared Err:" + e.getMessage());
        }


        //ImageView
        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_editform = findViewById(R.id.iv_editform);

        iv_editform.setVisibility(View.VISIBLE);
        iv_navigation_drawer.setVisibility(View.GONE);
        //  iv_home.setVisibility(View.GONE);

        //EditText
        et_tareekh_indraj = findViewById(R.id.et_tareekh_indraj);
        et_patient_name = findViewById(R.id.et_patient_name);
        et_age = findViewById(R.id.et_age);
        et_refferal_hospital_others = findViewById(R.id.et_refferal_hospital);

        //Spinner
        spRefHealthFacility = findViewById(R.id.spRefHealthFacility);
        sp_gender = findViewById(R.id.sp_gender);

        //LinearLayout
        ll_referal_hospital = findViewById(R.id.ll_referal_hospital);

        //ProgressBar
        pbProgress = findViewById(R.id.pbProgress);


        //CheckBox
        cb_runynose_haan = findViewById(R.id.cb_runynose_haan);
        cb_runynose_nahi = findViewById(R.id.cb_runynose_nahi);

        cb_throat_yes = findViewById(R.id.cb_throat_yes);
        cb_throat_nahi = findViewById(R.id.cb_throat_nahi);

        cb_fever_yes = findViewById(R.id.cb_fever_yes);
        cb_fever_nahi = findViewById(R.id.cb_fever_nahi);

        cb_cough_yes = findViewById(R.id.cb_cough_yes);
        cb_cough_nahi = findViewById(R.id.cb_cough_nahi);


        cb_braething_yes = findViewById(R.id.cb_braething_yes);
        cb_braething_nahi = findViewById(R.id.cb_braething_nahi);


        cb_another_country_haan = findViewById(R.id.cb_another_country_haan);
        cb_another_country_nahi = findViewById(R.id.cb_another_country_nahi);

        cb_handshake_haan = findViewById(R.id.cb_handshake_haan);
        cb_handshake_nahi = findViewById(R.id.cb_handshake_nahi);

        //Button
        btn_jamaa_kre = findViewById(R.id.submit);

        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                ShowTareekhIndraajDialog();
            }
        });


        cb_runynose_haan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (cb_runynose_haan.isChecked()) {
                    runny_nose = "1";
                    Log.d("000331", "RunnyNose IF: " + runny_nose);
                    cb_runynose_nahi.setChecked(false);
                } else {
                    runny_nose = "-1";
                    Log.d("000331", "RunnyNose ELSE: " + runny_nose);
                }
            }
        });


        cb_runynose_nahi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (cb_runynose_nahi.isChecked()) {
                    runny_nose = "0";
                    Log.d("000331", "RunnyNose IF: " + runny_nose);
                    cb_runynose_haan.setChecked(false);
                } else {
                    runny_nose = "-1";
                    Log.d("000331", "RunnyNose ELSE: " + runny_nose);
                }
            }
        });

        cb_throat_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (cb_throat_yes.isChecked()) {
                    throat = "1";
                    Log.d("000331", "Throat IF: " + throat);
                    cb_throat_nahi.setChecked(false);
                } else {
                    throat = "-1";
                    Log.d("000331", "Throat ELSE: " + throat);
                }
            }
        });


        cb_throat_nahi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (cb_throat_nahi.isChecked()) {
                    throat = "0";
                    Log.d("000331", "Throat IF: " + throat);
                    cb_throat_yes.setChecked(false);
                } else {
                    throat = "-1";
                    Log.d("000331", "Throat ELSE: " + throat);
                }
            }
        });


        cb_cough_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (cb_cough_yes.isChecked()) {
                    cough = "1";
                    Log.d("000331", "Cough IF: " + cough);
                    cb_cough_nahi.setChecked(false);
                } else {
                    cough = "-1";
                    Log.d("000331", "Cough ELSE: " + cough);
                }
            }
        });


        cb_cough_nahi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (cb_cough_nahi.isChecked()) {
                    cough = "0";
                    Log.d("000331", "Cough IF: " + cough);
                    cb_cough_yes.setChecked(false);
                } else {
                    cough = "-1";
                    Log.d("000331", "Cough ELSE: " + cough);
                }
            }
        });


        cb_fever_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (cb_fever_yes.isChecked()) {
                    fever = "1";
                    Log.d("000331", "Fever IF: " + fever);
                    cb_fever_nahi.setChecked(false);
                } else {
                    fever = "-1";
                    Log.d("000331", "fever ELSE: " + fever);
                }
            }
        });


        cb_fever_nahi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (cb_fever_nahi.isChecked()) {
                    fever = "0";
                    Log.d("000331", "fever IF: " + fever);
                    cb_fever_yes.setChecked(false);
                } else {
                    fever = "-1";
                    Log.d("000331", "fever ELSE: " + fever);
                }
            }
        });

        cb_braething_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (cb_braething_yes.isChecked()) {
                    breathing = "1";
                    Log.d("000331", "breathing IF: " + breathing);
                    cb_braething_nahi.setChecked(false);
                } else {
                    breathing = "-1";
                    Log.d("000331", "breathing ELSE: " + breathing);
                }
            }
        });


        cb_braething_nahi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (cb_braething_nahi.isChecked()) {
                    breathing = "0";
                    Log.d("000331", "breathing IF: " + breathing);
                    cb_braething_yes.setChecked(false);
                } else {
                    breathing = "-1";
                    Log.d("000331", "breathing ELSE: " + breathing);
                }
            }
        });

        cb_another_country_haan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (cb_another_country_haan.isChecked()) {
                    came_from_another_country = "1";
                    Log.d("000331", "Another COuntry IF: " + came_from_another_country);
                    cb_another_country_nahi.setChecked(false);
                } else {
                    came_from_another_country = "-1";
                    Log.d("000331", "Another Country ELSE: " + came_from_another_country);
                }
            }
        });


        cb_another_country_nahi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (cb_another_country_nahi.isChecked()) {
                    came_from_another_country = "0";
                    Log.d("000331", "Another Country IF: " + came_from_another_country);
                    cb_another_country_haan.setChecked(false);
                } else {
                    came_from_another_country = "-1";
                    Log.d("000331", "Another Country ELSE: " + came_from_another_country);
                }
            }
        });

        cb_handshake_haan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (cb_handshake_haan.isChecked()) {
                    handshake = "1";
                    Log.d("000331", "Handshake IF: " + handshake);
                    cb_handshake_nahi.setChecked(false);
                } else {
                    handshake = "-1";
                    Log.d("000331", "Handshake ELSE: " + handshake);
                }
            }
        });


        cb_handshake_nahi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (cb_handshake_nahi.isChecked()) {
                    handshake = "0";
                    Log.d("000331", "Handshake IF: " + handshake);
                    cb_handshake_haan.setChecked(false);
                } else {
                    handshake = "-1";
                    Log.d("000331", "Handshake ELSE: " + handshake);
                }
            }
        });


        spinner_data();


        iv_editform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pbProgress.setVisibility(View.VISIBLE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        et_tareekh_indraj.setEnabled(false);
                        et_patient_name.setEnabled(true);
                        et_age.setEnabled(true);
                        sp_gender.setEnabled(true);
                        cb_runynose_haan.setEnabled(true);
                        cb_runynose_nahi.setEnabled(true);
                        cb_throat_yes.setEnabled(true);
                        cb_throat_nahi.setEnabled(true);
                        cb_cough_yes.setEnabled(true);
                        cb_cough_nahi.setEnabled(true);
                        cb_fever_yes.setEnabled(true);
                        cb_fever_nahi.setEnabled(true);
                        cb_braething_yes.setEnabled(true);
                        cb_braething_nahi.setEnabled(true);
                        cb_another_country_haan.setEnabled(true);
                        cb_another_country_nahi.setEnabled(true);
                        cb_handshake_haan.setEnabled(true);
                        cb_handshake_nahi.setEnabled(true);

                        spRefHealthFacility.setEnabled(true);
                        et_refferal_hospital_others.setEnabled(true);


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

        if (et_patient_name.getText().toString().isEmpty()) {
            final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی مریض کا نام درج کریں.", Snackbar.LENGTH_SHORT);
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

        if (et_age.getText().toString().isEmpty()) {
            final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی مریض کی عمر درج کریں.", Snackbar.LENGTH_SHORT);
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

        if (sp_gender.getSelectedItemPosition() == 0) {
            final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی جنس منتخب کریں.", Snackbar.LENGTH_SHORT);
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

        if (!cb_runynose_haan.isChecked() && !cb_runynose_nahi.isChecked() && !cb_throat_yes.isChecked() && !cb_throat_nahi.isChecked()
                && !cb_cough_yes.isChecked() && !cb_cough_nahi.isChecked() && !cb_fever_yes.isChecked() && !cb_fever_nahi.isChecked() && !cb_braething_yes.isChecked() && !cb_braething_nahi.isChecked()) {
            final Snackbar snackbar = Snackbar.make(v, R.string.selectCheckboxPrompt, Snackbar.LENGTH_SHORT);
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


        if (spRefHealthFacility.getSelectedItemPosition() == 0) {
            final Snackbar snackbar = Snackbar.make(v, R.string.selectHealthCenterPrompt, Snackbar.LENGTH_SHORT);
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


        try {
            if (spRefHealthFacility.getSelectedItem().toString().equalsIgnoreCase("Others")) {
                if (et_refferal_hospital_others.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.selectReferalHealthCenterPrompt, Toast.LENGTH_LONG).show();
                    return;
                }
            } else {
                et_refferal_hospital_others.setText("none");
            }

        } catch (Exception e) {
            Log.d("0000999", "CATCH:" + e.getMessage());
        }


        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        Log.d("000332", " mLastClickTime: " + mLastClickTime);


        try {

            alertDialog = new Dialog(ctx);
            LayoutInflater layout = LayoutInflater.from(ctx);
            final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

            alertDialog.setContentView(dialogView);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.show();

            final String cur_added_on = String.valueOf(System.currentTimeMillis());


            Lister ls = new Lister(AddNewPatientFormView_Activity.this);
            ls.createAndOpenDB();


            jsonObject.put("patient_name", "" + et_patient_name.getText().toString());
            jsonObject.put("patient_age", "" + et_age.getText().toString());
            jsonObject.put("patient_gender", "" + sp_gender.getSelectedItem());
            jsonObject.put("patient_gender_pos", "" + String.valueOf(sp_gender.getSelectedItemPosition() - 1));
            jsonObject.put("cb_runny_nose", "" + runny_nose);
            jsonObject.put("cb_soar_throat", "" + throat);
            jsonObject.put("cb_cough", "" + cough);
            jsonObject.put("cb_fever", "" + fever);
            jsonObject.put("cb_breathing", "" + breathing);
            jsonObject.put("cb_another_country", "" + came_from_another_country);
            jsonObject.put("cb_handshake", "" + handshake);
            jsonObject.put("referal_facility", "" + spRefHealthFacility.getSelectedItem());
            jsonObject.put("referal_facility_pos", "" + String.valueOf(spRefHealthFacility.getSelectedItemPosition() - 1));
            jsonObject.put("referal_facility_others", "" + et_refferal_hospital_others.getText().toString());//spinner
            jsonObject.put("referal_facility_uid", "" + health_facility_uid);
            jsonObject.put("updated_current_record_date", "" + TodayDate);
            jsonObject.put("updated_added_on", "" + cur_added_on);


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();
                        String update_record = "UPDATE CVIRUS SET " +
                                "data='" + jsonObject.toString() + "'," +
                                "is_synced='" + 0 + "'" +
                                "WHERE member_uid = '" + patient_uid + "'  AND record_data = '" + record_date + "' AND added_on= '" + added_on + "' AND type IS '3'";

                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d("000332", "Data: " + update_record);
                        Log.d("000332", "Query: " + res.toString());


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
                            snackbar.setDuration(2500);
                            snackbar.show();


                            if (Utils.haveNetworkConnection(ctx) > 0) {
                                sendPostRequest(patient_uid, et_tareekh_indraj.getText().toString(), "3", String.valueOf(jsonObject), login_useruid, added_on);
                            } else {
                                //  Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();
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
                            snackbar.setDuration(2500);
                            snackbar.show();

                        }

                    } catch (Exception e) {
                        alertDialog.dismiss();
                        Log.d("000332", " Error: " + e.getMessage());

                    } finally {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog.dismiss();
                                finish();
                            }
                        }, 2000);
                    }

                }
            }, 2000);


        } catch (Exception e) {
            alertDialog.dismiss();
            Log.d("000332", "Err: " + e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void sendPostRequest(final String member_uid, final String record_data, final String type, final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/child/illness";

        Log.d("000999", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {
                    // Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000331", "Response:    " + response);

                        Lister ls = new Lister(AddNewPatientFormView_Activity.this);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE CVIRUS SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + patient_uid + "'AND record_data= '" + et_tareekh_indraj.getText().toString() + "'AND added_on= '" + added_on + "' AND type IS '3'";

                        ls.executeNonQuery(update_record);

                        Toast tt = Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();

                    } else {
                        Log.d("000331", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(AddNewPatientFormView_Activity.this, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000331", "catch: " + e.getMessage());
                    Toast tt = Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT);
                    tt.setGravity(Gravity.CENTER, 0, 0);
                    tt.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000331", "error:" + error.getMessage());
                // Toast.makeText(Child_BemaariRecordForm_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
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
                params.put("type", type);
                params.put("data", data);
                params.put("added_by", added_by);
                params.put("added_on", added_on);

                Log.d("000331", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000331", "map ");
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


        DatePickerDialog datePickerDialog = new DatePickerDialog(AddNewPatientFormView_Activity.this, R.style.DatePickerDialog,
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
                        Log.d("000331", "Tareekh_Indraj: " + et_tareekh_indraj.getText().toString());

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

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


                    if (spRefHealthFacility.getSelectedItemPosition() > 0) {

                        try {
                            Lister ls = new Lister(ctx);
                            ls.createAndOpenDB();

                            String[][] mData = ls.executeReader("select uid from FACILITY where name = '" + spRefHealthFacility.getSelectedItem() + "' order by name ");
                            health_facility_uid = mData[0][0];

                        } catch (Exception e) {
                            Log.d("0000999", "CATCH:" + e.getMessage());
                        }

                        try {
                            if (spRefHealthFacility.getSelectedItem().toString().equalsIgnoreCase("Others")) {
                                ll_referal_hospital.setVisibility(View.VISIBLE);
                                et_refferal_hospital_others.getText().clear();
                            } else {
                                ll_referal_hospital.setVisibility(View.GONE);
                                et_refferal_hospital_others.getText().clear();
                                et_refferal_hospital_others.setText("none");
                            }

                        } catch (Exception e) {
                            Log.d("0000999", "CATCH:" + e.getMessage());
                        }

                    } else {
                        Log.d("0000999", "ELSE:");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });


            // Sp gender
            final ArrayAdapter<CharSequence> adptr_gender = ArrayAdapter.createFromResource(this, R.array.array_gender, R.layout.temp);
            adptr_gender.setDropDownViewResource(R.layout.temp);

            sp_gender.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            adptr_gender,
                            R.layout.temp,
                            // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                            this));


            sp_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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


    @Override
    protected void onResume() {
        super.onResume();


        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String mData[][] = ls.executeReader("Select data from CVIRUS where member_uid = '" + patient_uid + "' AND record_data = '" + record_date + "'  AND added_on ='" + added_on + "' AND type IS '3'");


            Log.d("000332", "Data: " + mData[0][0]);


            String json = mData[0][0];

            jsonObject = new JSONObject(json);

            et_tareekh_indraj.setEnabled(false);
            et_patient_name.setEnabled(false);
            et_age.setEnabled(false);
            sp_gender.setEnabled(false);
            cb_runynose_haan.setEnabled(false);
            cb_runynose_nahi.setEnabled(false);
            cb_throat_yes.setEnabled(false);
            cb_throat_nahi.setEnabled(false);
            cb_cough_yes.setEnabled(false);
            cb_cough_nahi.setEnabled(false);
            cb_fever_yes.setEnabled(false);
            cb_fever_nahi.setEnabled(false);
            cb_braething_yes.setEnabled(false);
            cb_braething_nahi.setEnabled(false);
            cb_another_country_haan.setEnabled(false);
            cb_another_country_nahi.setEnabled(false);
            cb_handshake_haan.setEnabled(false);
            cb_handshake_nahi.setEnabled(false);

            spRefHealthFacility.setEnabled(false);
            et_refferal_hospital_others.setEnabled(false);


            btn_jamaa_kre.setVisibility(View.GONE);

            et_tareekh_indraj.setText(jsonObject.getString("record_enter_date"));
            et_patient_name.setText(jsonObject.getString("patient_name"));
            et_age.setText(jsonObject.getString("patient_age"));

            sp_gender.setSelection(Integer.parseInt(jsonObject.getString("patient_gender_pos")) + 1);


            try {
                if (jsonObject.getString("cb_runny_nose").equalsIgnoreCase("1")) {
                    runny_nose = jsonObject.getString("cb_runny_nose");
                    cb_runynose_haan.setChecked(true);
                    Log.d("000332", "IF RunnyNose: " + runny_nose);

                } else if (jsonObject.getString("cb_runny_nose").equalsIgnoreCase("0")) {
                    runny_nose = jsonObject.getString("cb_runny_nose");
                    cb_runynose_nahi.setChecked(true);
                    Log.d("000332", "ELSE IF RunnyNose: " + runny_nose);
                } else {
                    cb_runynose_haan.setChecked(false);
                    cb_runynose_nahi.setChecked(false);
                    runny_nose = jsonObject.getString("cb_runny_nose");
                    Log.d("000332", "ELSE RunnyNose: " + runny_nose);
                }

                if (jsonObject.getString("cb_soar_throat").equalsIgnoreCase("1")) {
                    throat = jsonObject.getString("cb_soar_throat");
                    cb_throat_yes.setChecked(true);
                    Log.d("000332", "IF Throat: " + throat);
                } else if (jsonObject.getString("cb_soar_throat").equalsIgnoreCase("0")) {
                    throat = jsonObject.getString("cb_soar_throat");
                    cb_throat_nahi.setChecked(true);
                    Log.d("000332", "ELSE IF Throat: " + throat);
                } else {
                    cb_throat_yes.setChecked(false);
                    cb_throat_nahi.setChecked(false);
                    throat = jsonObject.getString("cb_soar_throat");
                    Log.d("000332", "ELSE Throat: " + throat);
                }

                if (jsonObject.getString("cb_cough").equalsIgnoreCase("1")) {
                    cough = jsonObject.getString("cb_cough");
                    cb_cough_yes.setChecked(true);
                    Log.d("000332", "IF Cough: " + cough);
                } else if (jsonObject.getString("cb_cough").equalsIgnoreCase("0")) {
                    cough = jsonObject.getString("cb_cough");
                    cb_cough_nahi.setChecked(true);
                    Log.d("000332", "ELSE IF Cough: " + cough);
                } else {
                    cb_cough_yes.setChecked(false);
                    cb_cough_nahi.setChecked(false);
                    cough = jsonObject.getString("cb_cough");
                    Log.d("000332", "ELSE Cough: " + cough);
                }

                if (jsonObject.getString("cb_fever").equalsIgnoreCase("1")) {
                    fever = jsonObject.getString("cb_fever");
                    cb_fever_yes.setChecked(true);
                    Log.d("000332", "IF Fever: " + fever);
                } else if (jsonObject.getString("cb_fever").equalsIgnoreCase("0")) {
                    fever = jsonObject.getString("cb_fever");
                    cb_fever_nahi.setChecked(true);
                    Log.d("000332", "ELSE IF Fever: " + fever);
                } else {
                    cb_fever_yes.setChecked(false);
                    cb_fever_nahi.setChecked(false);
                    fever = jsonObject.getString("cb_fever");
                    Log.d("000332", "ELSE Fever: " + fever);
                }

                if (jsonObject.getString("cb_breathing").equalsIgnoreCase("1")) {
                    breathing = jsonObject.getString("cb_breathing");
                    cb_braething_yes.setChecked(true);
                    Log.d("000332", "IF Breathing: " + breathing);
                } else if (jsonObject.getString("cb_breathing").equalsIgnoreCase("0")) {
                    breathing = jsonObject.getString("cb_breathing");
                    cb_braething_nahi.setChecked(true);
                    Log.d("000332", "ELSE IF Breathing: " + breathing);
                } else {
                    cb_braething_yes.setChecked(false);
                    cb_braething_nahi.setChecked(false);
                    breathing = jsonObject.getString("cb_breathing");
                    Log.d("000332", "ELSE Breathing: " + breathing);
                }

                if (jsonObject.getString("cb_another_country").equalsIgnoreCase("1")) {
                    came_from_another_country = jsonObject.getString("cb_another_country");
                    cb_another_country_haan.setChecked(true);
                    Log.d("000332", "IF Another COuntry: " + came_from_another_country);
                } else if (jsonObject.getString("cb_another_country").equalsIgnoreCase("0")) {
                    came_from_another_country = jsonObject.getString("cb_another_country");
                    cb_another_country_nahi.setChecked(true);
                    Log.d("000332", "ELSE IF Another Country: " + came_from_another_country);
                } else {
                    cb_another_country_haan.setChecked(false);
                    cb_another_country_nahi.setChecked(false);
                    came_from_another_country = jsonObject.getString("cb_another_country");
                    Log.d("000332", "ELSE Another Country: " + came_from_another_country);
                }

                if (jsonObject.getString("cb_handshake").equalsIgnoreCase("1")) {
                    handshake = jsonObject.getString("cb_handshake");
                    cb_handshake_haan.setChecked(true);
                    Log.d("000332", "IF Handshake: " + handshake);
                } else if (jsonObject.getString("cb_handshake").equalsIgnoreCase("0")) {
                    handshake = jsonObject.getString("cb_handshake");
                    cb_handshake_nahi.setChecked(true);
                    Log.d("000332", "ELSE IF Handshake: " + handshake);
                } else {
                    cb_handshake_haan.setChecked(false);
                    cb_handshake_nahi.setChecked(false);
                    handshake = jsonObject.getString("cb_handshake");
                    Log.d("000332", "ELSE Handshake: " + handshake);
                }

            } catch (Exception e) {
                Log.d("000332", "CB Error: " + e.getMessage());
            }


            try {
                spRefHealthFacility.setSelection(Integer.parseInt(jsonObject.getString("referal_facility_pos")) + 1);

                if (jsonObject.getString("referal_facility").equalsIgnoreCase("Others")) {
                    ll_referal_hospital.setVisibility(View.VISIBLE);
                    et_refferal_hospital_others.setText(jsonObject.getString("referal_facility_others"));
                    Log.d("000332", "IF Referal FAcility");

                } else {
                    ll_referal_hospital.setVisibility(View.GONE);
                    Log.d("000332", "ELSE Referal FAcility");
                }


            } catch (Exception e1) {
                Log.d("000332", "sp Error: " + e1.getMessage());
            }

            ls.closeDB();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("000332", " Error: " + e.getMessage());
            Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
