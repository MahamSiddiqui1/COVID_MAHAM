package com.akdndhrc.covid_module.VAC_App.covid_dashboard.dashboard_comorbidity;

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


public class co_morbidity_Form_Activity extends AppCompatActivity {

    Context ctx = co_morbidity_Form_Activity.this;


    EditText et_tareekh_visit;
    Button btn_jamaa_kre;
    ImageView iv_navigation_drawer, iv_home;
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
    //Spinner sp_material,sp_fever,sp_cough,sp_breath,sp_rash,sp_taste,sp_smell,sp_dia;
    RelativeLayout rl_quantity, rl_add, rl_sub;
    TextView tv_count;
    int counter = 0;
    long mLastClickTime = 0;

    Switch cm_cancer,cm_chronic_kidney_disease,cm_copd,cm_heart_condition,cm_immune_compromised_organ,cm_obesity,cm_sickle_cell_disease,cm_pregnancy
            ,cm_asthma,cm_cerebrovascular_disease,cm_cystic_fibrosis,cm_hypertension,cm_immune_compromised_other,cm_neurologic
            ,cm_liver_disease,cm_overweight,cm_pulmonary_fibrosis,cm_thalassemia,cm_type1_diabetes,cm_ldl_cholestrol;

    EditText et_cm_cancer,et_cm_chronic_kidney_disease,et_cm_copd,et_cm_heart_condition,et_cm_immune_compromised_organ,et_cm_obesity,et_cm_sickle_cell_disease,et_cm_pregnancy
            ,et_cm_asthma,et_cm_cerebrovascular_disease,et_cm_cystic_fibrosis,et_cm_hypertension,et_cm_immune_compromised_other,et_cm_neurologic
            ,et_cm_liver_disease,et_cm_overweight,et_cm_pulmonary_fibrosis,et_cm_thalassemia,et_cm_type1_diabetes,et_cm_ldl_cholestrol;

    RelativeLayout rl_cm_cancer,rl_cm_chronic_kidney_disease,rl_cm_copd,rl_cm_heart_condition,rl_cm_immune_compromised_organ,rl_cm_obesity,rl_cm_sickle_cell_disease,rl_cm_pregnancy
            ,rl_cm_asthma,rl_cm_cerebrovascular_disease,rl_cm_cystic_fibrosis,rl_cm_hypertension,rl_cm_immune_compromised_other,rl_cm_neurologic
            ,rl_cm_liver_disease,rl_cm_overweight,rl_cm_pulmonary_fibrosis,rl_cm_thalassemia,rl_cm_type1_diabetes,rl_cm_ldl_cholestrol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.co_morbidity_form);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, co_morbidity_Form_Activity.class));

        mother_uid = getIntent().getExtras().getString("u_id");

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
            String shared_useruid = prefelse.getString((R.string.loginUserIDEng), null); // getting String
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
      et_tareekh_visit=findViewById(R.id.et_tareekh_visit);
        et_tareekh_visit.setText(TodayDate);






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


       //Switch
        cm_cancer= findViewById(R.id.cm_cancer);
        cm_chronic_kidney_disease= findViewById(R.id.cm_chronic_kidney_disease);
        cm_copd= findViewById(R.id.cm_copd);
        cm_heart_condition= findViewById(R.id.cm_heart_condition);
        cm_immune_compromised_organ= findViewById(R.id.cm_immune_compromised_organ);
        cm_obesity= findViewById(R.id.cm_obesity);
        cm_sickle_cell_disease= findViewById(R.id.cm_sickle_cell_disease);
        cm_pregnancy = findViewById(R.id.cm_pregnancy);
        cm_asthma= findViewById(R.id.cm_asthma);
        cm_cerebrovascular_disease= findViewById(R.id.cm_cerebrovascular_disease);
        cm_cystic_fibrosis= findViewById(R.id.cm_cystic_fibrosis);
        cm_hypertension= findViewById(R.id.cm_hypertension);
        cm_immune_compromised_other= findViewById(R.id.cm_immune_compromised_other);
        cm_neurologic = findViewById(R.id.cm_neurologic);
        cm_liver_disease= findViewById(R.id.cm_liver_disease);
        cm_overweight= findViewById(R.id.cm_overweight);
        cm_pulmonary_fibrosis= findViewById(R.id.cm_pulmonary_fibrosis);
        cm_thalassemia= findViewById(R.id.cm_thalassemia);
        cm_type1_diabetes= findViewById(R.id.cm_type1_diabetes);
        cm_ldl_cholestrol= findViewById(R.id.cm_ldl_cholestrol);;

        //EditText
        //Switch
        et_cm_cancer= findViewById(R.id.et_cm_cancer);
        et_cm_chronic_kidney_disease= findViewById(R.id.et_cm_chronic_kidney_disease);
        et_cm_copd= findViewById(R.id.et_cm_copd);
        et_cm_heart_condition= findViewById(R.id.et_cm_heart_condition);
        et_cm_immune_compromised_organ= findViewById(R.id.et_cm_immune_compromised_organ);
        et_cm_obesity= findViewById(R.id.et_cm_obesity);
        et_cm_sickle_cell_disease= findViewById(R.id.et_cm_sickle_cell_disease);
        et_cm_pregnancy = findViewById(R.id.et_cm_pregnancy);
        et_cm_asthma= findViewById(R.id.et_cm_asthma);
        et_cm_cerebrovascular_disease= findViewById(R.id.et_cm_cerebrovascular_disease);
        et_cm_cystic_fibrosis= findViewById(R.id.et_cm_cystic_fibrosis);
        et_cm_hypertension= findViewById(R.id.et_cm_hypertension);
        et_cm_immune_compromised_other= findViewById(R.id.et_cm_immune_compromised_other);
        et_cm_neurologic = findViewById(R.id.et_cm_neurologic);
        et_cm_liver_disease= findViewById(R.id.et_cm_liver_disease);
        et_cm_overweight= findViewById(R.id.et_cm_overweight);
        et_cm_pulmonary_fibrosis= findViewById(R.id.et_cm_pulmonary_fibrosis);
        et_cm_thalassemia= findViewById(R.id.et_cm_thalassemia);
        et_cm_type1_diabetes= findViewById(R.id.et_cm_type1_diabetes);
        et_cm_ldl_cholestrol= findViewById(R.id.et_cm_ldl_cholestrol);

        //RelativeLayout
        //Switch
        rl_cm_cancer= findViewById(R.id.rl_cm_cancer);
        rl_cm_chronic_kidney_disease= findViewById(R.id.rl_cm_chronic_kidney_disease);
        rl_cm_copd= findViewById(R.id.rl_cm_copd);
        rl_cm_heart_condition= findViewById(R.id.rl_cm_heart_condition);
        rl_cm_immune_compromised_organ= findViewById(R.id.rl_cm_immune_compromised_organ);
        rl_cm_obesity= findViewById(R.id.rl_cm_obesity);
        rl_cm_sickle_cell_disease= findViewById(R.id.rl_cm_sickle_cell_disease);
        rl_cm_pregnancy = findViewById(R.id.rl_cm_pregnancy);
        rl_cm_asthma= findViewById(R.id.rl_cm_asthma);
        rl_cm_cerebrovascular_disease= findViewById(R.id.rl_cm_cerebrovascular_disease);
        rl_cm_cystic_fibrosis= findViewById(R.id.rl_cm_cystic_fibrosis);
        rl_cm_hypertension= findViewById(R.id.rl_cm_hypertension);
        rl_cm_immune_compromised_other= findViewById(R.id.rl_cm_immune_compromised_other);
        rl_cm_neurologic = findViewById(R.id.rl_cm_neurologic);
        rl_cm_liver_disease= findViewById(R.id.rl_cm_liver_disease);
        rl_cm_overweight= findViewById(R.id.rl_cm_overweight);
        rl_cm_pulmonary_fibrosis= findViewById(R.id.rl_cm_pulmonary_fibrosis);
        rl_cm_thalassemia= findViewById(R.id.rl_cm_thalassemia);
        rl_cm_type1_diabetes= findViewById(R.id.rl_cm_type1_diabetes);
        rl_cm_ldl_cholestrol= findViewById(R.id.rl_cm_ldl_cholestrol);;

        cm_cancer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cm_cancer.setText("Yes");
                    et_cm_cancer.setFocusable(true);
                    et_cm_cancer.setCursorVisible(true);
                    et_cm_cancer.setFocusableInTouchMode(true);
                    et_cm_cancer.setClickable(true);

                    et_cm_cancer.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_cm_cancer.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));


                } else {
                    // The toggle is disabled
                    cm_cancer.setText("No");

                    et_cm_cancer.setFocusable(false);
                    et_cm_cancer.setCursorVisible(false);
                    et_cm_cancer.setFocusableInTouchMode(false);
                    et_cm_cancer.setClickable(false);
                    et_cm_cancer.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_cm_cancer.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });

        cm_chronic_kidney_disease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cm_chronic_kidney_disease.setText("Yes");
                    et_cm_chronic_kidney_disease.setFocusable(true);
                    et_cm_chronic_kidney_disease.setCursorVisible(true);
                    et_cm_chronic_kidney_disease.setFocusableInTouchMode(true);
                    et_cm_chronic_kidney_disease.setClickable(true);

                    et_cm_chronic_kidney_disease.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_cm_chronic_kidney_disease.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));


                } else {
                    // The toggle is disabled
                    cm_chronic_kidney_disease.setText("No");
                    et_cm_chronic_kidney_disease.setFocusable(false);
                    et_cm_chronic_kidney_disease.setCursorVisible(false);
                    et_cm_chronic_kidney_disease.setFocusableInTouchMode(false);
                    et_cm_chronic_kidney_disease.setClickable(false);
                    et_cm_chronic_kidney_disease.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_cm_chronic_kidney_disease.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });

        cm_copd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cm_copd.setText("Yes");
                    et_cm_copd.setFocusable(true);
                    et_cm_copd.setCursorVisible(true);
                    et_cm_copd.setFocusableInTouchMode(true);
                    et_cm_copd.setClickable(true);

                    et_cm_copd.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_cm_copd.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));



                } else {
                    // The toggle is disabled
                    cm_copd.setText("No");
                    et_cm_copd.setFocusable(false);
                    et_cm_copd.setCursorVisible(false);
                    et_cm_copd.setFocusableInTouchMode(false);
                    et_cm_copd.setClickable(false);
                    et_cm_copd.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_cm_copd.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });

        cm_heart_condition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cm_heart_condition.setText("Yes");
                    et_cm_heart_condition.setFocusable(true);
                    et_cm_heart_condition.setCursorVisible(true);
                    et_cm_heart_condition.setFocusableInTouchMode(true);
                    et_cm_heart_condition.setClickable(true);

                    et_cm_heart_condition.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_cm_heart_condition.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));


                } else {
                    // The toggle is disabled
                    cm_heart_condition.setText("No");
                    et_cm_heart_condition.setFocusable(false);
                    et_cm_heart_condition.setCursorVisible(false);
                    et_cm_heart_condition.setFocusableInTouchMode(false);
                    et_cm_heart_condition.setClickable(false);
                    et_cm_heart_condition.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_cm_heart_condition.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });

        cm_immune_compromised_organ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cm_immune_compromised_organ.setText("Yes");
                    et_cm_immune_compromised_organ.setFocusable(true);
                    et_cm_immune_compromised_organ.setCursorVisible(true);
                    et_cm_immune_compromised_organ.setFocusableInTouchMode(true);
                    et_cm_immune_compromised_organ.setClickable(true);

                    et_cm_immune_compromised_organ.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_cm_immune_compromised_organ.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));


                } else {
                    // The toggle is disabled
                    cm_immune_compromised_organ.setText("No");
                    et_cm_immune_compromised_organ.setFocusable(false);
                    et_cm_immune_compromised_organ.setCursorVisible(false);
                    et_cm_immune_compromised_organ.setFocusableInTouchMode(false);
                    et_cm_immune_compromised_organ.setClickable(false);
                    et_cm_immune_compromised_organ.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_cm_immune_compromised_organ.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });

        cm_obesity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cm_obesity.setText("Yes");
                    et_cm_obesity.setFocusable(true);
                    et_cm_obesity.setCursorVisible(true);
                    et_cm_obesity.setFocusableInTouchMode(true);
                    et_cm_obesity.setClickable(true);

                    et_cm_obesity.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_cm_obesity.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));


                } else {
                    // The toggle is disabled
                    cm_obesity.setText("No");
                    et_cm_obesity.setFocusable(false);
                    et_cm_obesity.setCursorVisible(false);
                    et_cm_obesity.setFocusableInTouchMode(false);
                    et_cm_obesity.setClickable(false);
                    et_cm_obesity.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_cm_obesity.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });

        cm_sickle_cell_disease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cm_sickle_cell_disease.setText("Yes");
                    et_cm_sickle_cell_disease.setFocusable(true);
                    et_cm_sickle_cell_disease.setCursorVisible(true);
                    et_cm_sickle_cell_disease.setFocusableInTouchMode(true);
                    et_cm_sickle_cell_disease.setClickable(true);

                    et_cm_sickle_cell_disease.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_cm_sickle_cell_disease.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));


                } else {
                    // The toggle is disabled
                    cm_sickle_cell_disease.setText("No");
                    et_cm_sickle_cell_disease.setFocusable(false);
                    et_cm_sickle_cell_disease.setCursorVisible(false);
                    et_cm_sickle_cell_disease.setFocusableInTouchMode(false);
                    et_cm_sickle_cell_disease.setClickable(false);
                    et_cm_sickle_cell_disease.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_cm_sickle_cell_disease.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });

        cm_pregnancy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cm_pregnancy.setText("Yes");
                    et_cm_pregnancy.setFocusable(true);
                    et_cm_pregnancy.setCursorVisible(true);
                    et_cm_pregnancy.setFocusableInTouchMode(true);
                    et_cm_pregnancy.setClickable(true);

                    et_cm_pregnancy.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_cm_pregnancy.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));


                } else {
                    // The toggle is disabled
                    cm_pregnancy.setText("No");
                    et_cm_pregnancy.setFocusable(false);
                    et_cm_pregnancy.setCursorVisible(false);
                    et_cm_pregnancy.setFocusableInTouchMode(false);
                    et_cm_pregnancy.setClickable(false);
                    et_cm_pregnancy.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_cm_pregnancy.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });

        cm_asthma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cm_asthma.setText("Yes");
                    et_cm_asthma.setFocusable(true);
                    et_cm_asthma.setCursorVisible(true);
                    et_cm_asthma.setFocusableInTouchMode(true);
                    et_cm_asthma.setClickable(true);

                    et_cm_asthma.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_cm_asthma.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));


                } else {
                    // The toggle is disabled
                    cm_asthma.setText("No");
                    et_cm_asthma.setFocusable(false);
                    et_cm_asthma.setCursorVisible(false);
                    et_cm_asthma.setFocusableInTouchMode(false);
                    et_cm_asthma.setClickable(false);
                    et_cm_asthma.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_cm_asthma.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });

        cm_cerebrovascular_disease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cm_cerebrovascular_disease.setText("Yes");
                    et_cm_cerebrovascular_disease.setFocusable(true);
                    et_cm_cerebrovascular_disease.setCursorVisible(true);
                    et_cm_cerebrovascular_disease.setFocusableInTouchMode(true);
                    et_cm_cerebrovascular_disease.setClickable(true);

                    et_cm_cerebrovascular_disease.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_cm_cerebrovascular_disease.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));


                } else {
                    // The toggle is disabled
                    cm_cerebrovascular_disease.setText("No");
                    et_cm_cerebrovascular_disease.setFocusable(false);
                    et_cm_cerebrovascular_disease.setCursorVisible(false);
                    et_cm_cerebrovascular_disease.setFocusableInTouchMode(false);
                    et_cm_cerebrovascular_disease.setClickable(false);
                    et_cm_cerebrovascular_disease.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_cm_cerebrovascular_disease.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });

        cm_cystic_fibrosis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cm_cystic_fibrosis.setText("Yes");
                    et_cm_cystic_fibrosis.setFocusable(true);
                    et_cm_cystic_fibrosis.setCursorVisible(true);
                    et_cm_cystic_fibrosis.setFocusableInTouchMode(true);
                    et_cm_cystic_fibrosis.setClickable(true);

                    et_cm_cystic_fibrosis.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_cm_cystic_fibrosis.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));


                } else {
                    // The toggle is disabled
                    cm_cystic_fibrosis.setText("No");
                    et_cm_cystic_fibrosis.setFocusable(false);
                    et_cm_cystic_fibrosis.setCursorVisible(false);
                    et_cm_cystic_fibrosis.setFocusableInTouchMode(false);
                    et_cm_cystic_fibrosis.setClickable(false);
                    et_cm_cystic_fibrosis.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_cm_cystic_fibrosis.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });

        cm_hypertension.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cm_hypertension.setText("Yes");
                    et_cm_hypertension.setFocusable(true);
                    et_cm_hypertension.setCursorVisible(true);
                    et_cm_hypertension.setFocusableInTouchMode(true);
                    et_cm_hypertension.setClickable(true);

                    et_cm_hypertension.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_cm_hypertension.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));


                } else {
                    // The toggle is disabled
                    cm_hypertension.setText("No");
                    et_cm_hypertension.setFocusable(false);
                    et_cm_hypertension.setCursorVisible(false);
                    et_cm_hypertension.setFocusableInTouchMode(false);
                    et_cm_hypertension.setClickable(false);
                    et_cm_hypertension.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_cm_hypertension.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });

        cm_immune_compromised_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cm_immune_compromised_other.setText("Yes");
                    et_cm_immune_compromised_other.setFocusable(true);
                    et_cm_immune_compromised_other.setCursorVisible(true);
                    et_cm_immune_compromised_other.setFocusableInTouchMode(true);
                    et_cm_immune_compromised_other.setClickable(true);

                    et_cm_immune_compromised_other.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_cm_immune_compromised_other.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));


                } else {
                    // The toggle is disabled
                    cm_immune_compromised_other.setText("No");
                    et_cm_immune_compromised_other.setFocusable(false);
                    et_cm_immune_compromised_other.setCursorVisible(false);
                    et_cm_immune_compromised_other.setFocusableInTouchMode(false);
                    et_cm_immune_compromised_other.setClickable(false);
                    et_cm_immune_compromised_other.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_cm_immune_compromised_other.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });

        cm_neurologic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cm_neurologic.setText("Yes");
                    et_cm_neurologic.setFocusable(true);
                    et_cm_neurologic.setCursorVisible(true);
                    et_cm_neurologic.setFocusableInTouchMode(true);
                    et_cm_neurologic.setClickable(true);

                    et_cm_neurologic.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_cm_neurologic.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));


                } else {
                    // The toggle is disabled
                    cm_neurologic.setText("No");
                    et_cm_neurologic.setFocusable(false);
                    et_cm_neurologic.setCursorVisible(false);
                    et_cm_neurologic.setFocusableInTouchMode(false);
                    et_cm_neurologic.setClickable(false);
                    et_cm_neurologic.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_cm_neurologic.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });

        cm_liver_disease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cm_liver_disease.setText("Yes");
                    et_cm_liver_disease.setFocusable(true);
                    et_cm_liver_disease.setCursorVisible(true);
                    et_cm_liver_disease.setFocusableInTouchMode(true);
                    et_cm_liver_disease.setClickable(true);

                    et_cm_liver_disease.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_cm_liver_disease.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));


                } else {
                    // The toggle is disabled
                    cm_liver_disease.setText("No");
                    et_cm_liver_disease.setFocusable(false);
                    et_cm_liver_disease.setCursorVisible(false);
                    et_cm_liver_disease.setFocusableInTouchMode(false);
                    et_cm_liver_disease.setClickable(false);
                    et_cm_liver_disease.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_cm_liver_disease.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });

        cm_overweight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cm_overweight.setText("Yes");
                    et_cm_overweight.setFocusable(true);
                    et_cm_overweight.setCursorVisible(true);
                    et_cm_overweight.setFocusableInTouchMode(true);
                    et_cm_overweight.setClickable(true);

                    et_cm_overweight.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_cm_overweight.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));


                } else {
                    // The toggle is disabled
                    cm_overweight.setText("No");
                    et_cm_overweight.setFocusable(false);
                    et_cm_overweight.setCursorVisible(false);
                    et_cm_overweight.setFocusableInTouchMode(false);
                    et_cm_overweight.setClickable(false);
                    et_cm_overweight.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_cm_overweight.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });

        cm_pulmonary_fibrosis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cm_pulmonary_fibrosis.setText("Yes");
                    et_cm_pulmonary_fibrosis.setFocusable(true);
                    et_cm_pulmonary_fibrosis.setCursorVisible(true);
                    et_cm_pulmonary_fibrosis.setFocusableInTouchMode(true);
                    et_cm_pulmonary_fibrosis.setClickable(true);

                    et_cm_pulmonary_fibrosis.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_cm_pulmonary_fibrosis.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));


                } else {
                    // The toggle is disabled
                    cm_pulmonary_fibrosis.setText("No");
                    et_cm_pulmonary_fibrosis.setFocusable(false);
                    et_cm_pulmonary_fibrosis.setCursorVisible(false);
                    et_cm_pulmonary_fibrosis.setFocusableInTouchMode(false);
                    et_cm_pulmonary_fibrosis.setClickable(false);
                    et_cm_pulmonary_fibrosis.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_cm_pulmonary_fibrosis.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });

        cm_thalassemia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cm_thalassemia.setText("Yes");
                    et_cm_thalassemia.setFocusable(true);
                    et_cm_thalassemia.setCursorVisible(true);
                    et_cm_thalassemia.setFocusableInTouchMode(true);
                    et_cm_thalassemia.setClickable(true);

                    et_cm_thalassemia.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_cm_thalassemia.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));


                } else {
                    // The toggle is disabled
                    cm_thalassemia.setText("No");
                    et_cm_thalassemia.setFocusable(false);
                    et_cm_thalassemia.setCursorVisible(false);
                    et_cm_thalassemia.setFocusableInTouchMode(false);
                    et_cm_thalassemia.setClickable(false);

                    et_cm_thalassemia.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_cm_thalassemia.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });

        cm_type1_diabetes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cm_type1_diabetes.setText("Yes");
                    et_cm_type1_diabetes.setFocusable(true);
                    et_cm_type1_diabetes.setCursorVisible(true);
                    et_cm_type1_diabetes.setFocusableInTouchMode(true);
                    et_cm_type1_diabetes.setClickable(true);

                    et_cm_type1_diabetes.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_cm_type1_diabetes.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));


                } else {
                    // The toggle is disabled
                    cm_type1_diabetes.setText("No");
                    et_cm_type1_diabetes.setFocusable(false);
                    et_cm_type1_diabetes.setCursorVisible(false);
                    et_cm_type1_diabetes.setFocusableInTouchMode(false);
                    et_cm_type1_diabetes.setClickable(false);
                    et_cm_type1_diabetes.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_cm_type1_diabetes.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });

        cm_ldl_cholestrol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    cm_ldl_cholestrol.setText("Yes");
                    et_cm_ldl_cholestrol.setFocusable(true);
                    et_cm_ldl_cholestrol.setCursorVisible(true);
                    et_cm_ldl_cholestrol.setFocusableInTouchMode(true);
                    et_cm_ldl_cholestrol.setClickable(true);

                    et_cm_ldl_cholestrol.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));
                    rl_cm_ldl_cholestrol.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout));


                } else {
                    // The toggle is disabled
                    cm_ldl_cholestrol.setText("No");
                    et_cm_ldl_cholestrol.setFocusable(false);
                    et_cm_ldl_cholestrol.setCursorVisible(false);
                    et_cm_ldl_cholestrol.setFocusableInTouchMode(false);
                    et_cm_ldl_cholestrol.setClickable(false);
                    et_cm_ldl_cholestrol.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                    rl_cm_ldl_cholestrol.setBackground(getResources().getDrawable(R.drawable.edittext_background_layout_outline));
                }
            }
        });


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
                Toast.makeText(ctx, R.string.navigation, Toast.LENGTH_SHORT).show();
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

                        String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from COVID_CO_MORBIDITY");

                        if (Integer.parseInt(mData[0][2]) > 0) {
                            JSONObject jsonObject = new JSONObject(mData[0][1]);
                            Log.d("000362", "  Last Latitude: " + jsonObject.getString("lat"));
                            Log.d("000362", "  Last Longitude: " + jsonObject.getString("lng"));

                            latitude = Double.parseDouble(jsonObject.getString("lat"));
                            longitude = Double.parseDouble(jsonObject.getString("lng"));

                            Toast.makeText(ctx, R.string.dataGPS, Toast.LENGTH_SHORT).show();
                        } else {
                            latitude = Double.parseDouble("0.0");
                            longitude = Double.parseDouble("0.0");
                            Toast.makeText(ctx, R.string.notDataGPS, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.d("000362", "Read CBEMARI Error: " + e.getMessage());
                    }
                }

                btn_jamaa_kre.setVisibility(View.GONE);
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Log.d("000362", " mLastClickTime: " + mLastClickTime);


                try {

                    Lister ls = new Lister(co_morbidity_Form_Activity.this);
                    ls.createAndOpenDB();

                    // et_refferal_ki_waja = findViewById(R.id.et_refferal_ki_waja);
                    // et_refferal_hospital = findViewById(R.id.et_refferal_hospital);


                    JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("tareekh_visit", "" + et_tareekh_visit.getText().toString());
           /*         jobj.put("notes_diabetes", "" + et_notes_diabetes.getText().toString());
                    jobj.put("notes_cancer", "" + et_notes_cancer.getText().toString());
                    jobj.put("notes_heart", "" + et_notes_heart.getText().toString());
                    jobj.put("notes_blood", "" + et_notes_blood.getText().toString());
                    jobj.put("notes_cholestrol", "" + et_notes_cholestrol.getText().toString());
                   // jobj.put("plan", "" + String.valueOf(sp_naya_sabiqa.getSelectedItemPosition() - 1));
                     jobj.put("diabetes", "" + String.valueOf(sp_diabetes.getSelectedItemPosition() - 1));
                    jobj.put("blood_pressure", "" + String.valueOf(sp_blood_pressure.getSelectedItemPosition() - 1));
                    jobj.put("cholestrol", "" + String.valueOf(sp_cholestrol.getSelectedItemPosition() - 1));
                    jobj.put("heart", "" + String.valueOf(sp_heart.getSelectedItemPosition() - 1));
                    jobj.put("cancer", "" + String.valueOf(sp_cancer.getSelectedItemPosition() - 1));*/

                    jobj.put("added_on", "null");


                    String cur_added_on = String.valueOf(System.currentTimeMillis());

                    // jobjMain.put("data", jobj);
                    String ans1 = "insert into COVID_CO_MORBIDITY (member_uid, record_data, data,added_by, is_synced,added_on)" +
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
                            //Toast.makeText(ctx, R.string.dataCollected, Toast.LENGTH_SHORT).show();
                        }



                        if (services_and_awareness.equalsIgnoreCase("1")) {
                          //  insert_into_medicineLog(cur_added_on);
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

        String url = " https://pak.api.teekoplus.akdndhrc.org/sync/save/mother/illness";

        Log.d("000362", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {
                        Log.d("000362", "Response:  " + response);


                        Lister ls = new Lister(co_morbidity_Form_Activity.this);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE COVID_CO_MORBIDITY SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + mother_uid + "'AND record_data= '" + et_tareekh_visit.getText().toString() + "'AND added_on= '" + added_on + "'";
                        ls.executeNonQuery(update_record);
                        Log.d("000362", "Update Record:  " + update_record);

                        Toast tt  =Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();

                    } else {
                        Log.d("000362", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServiceEng, Toast.LENGTH_SHORT).show();
                        //  Toast.makeText(side_effects_Form_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000362", "Err: " + e.getMessage());
                    //Toast.makeText(side_effects_Form_Activity.this, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();
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

    public void ReferalFormSubmit() {
        try {

            Lister ls = new Lister(co_morbidity_Form_Activity.this);
            ls.createAndOpenDB();

            JSONObject jobj = new JSONObject();
            jobj.put("lat", "" + String.valueOf(latitude));
            jobj.put("lng", "" + String.valueOf(longitude));
           /* jobj.put("referal_reason", "" + et_refferal_ki_waja.getText().toString());
            jobj.put("referal_facility", "" + et_refferal_hospital.getText().toString());//spinner
            jobj.put("referal_type", "" + "MFPLAN");//spinner
           */ jobj.put("added_on", "null");

            String cur_added_oon = String.valueOf(System.currentTimeMillis());

            String ans1 = "insert into REFERAL (member_uid, record_data, data,added_by, is_synced,added_on)" +
                    "values" +
                    "(" +
                    "'" + mother_uid + "'," +
                    "'" + TodayDate + "'," +
                    "'" + jobj + "'," +
                    "'" + login_useruid + "'," +
                    "'0'," +
                    "'" + cur_added_oon + "'" +
                    ")";

            Boolean res = ls.executeNonQuery(ans1);
            Log.d("000362", "Data: " + ans1);
            Log.d("000362", "Query: " + res.toString());

            sendPostRequestReferal(mother_uid, TodayDate, String.valueOf(jobj), login_useruid, cur_added_oon);
            ;

            //  Toast.makeText(getApplicationContext(),String.valueOf(res)+String.valueOf(ans1),Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.d("000362", "Err: " + e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
                    /*Intent intent = new Intent(ctx, Add_Family_Member_Activity.class);
                    startActivity(intent);*/
            finish();
        }


    }

    private void sendPostRequestReferal(final String member_uid, final String record_data, final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/family/referrals";

        Log.d("000362", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000362", "Response    " + response);

                        //   Toast.makeText(side_effects_Form_Activity.this, "Data has been saved", Toast.LENGTH_SHORT).show();

                        Lister ls = new Lister(co_morbidity_Form_Activity.this);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE REFERAL SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + mother_uid + "'AND record_data= '" + record_data + "'AND added_on= '" + added_on + "'";

                        ls.executeNonQuery(update_record);


                    } else {
                        Log.d("000362", "else ");

                        //Toast.makeText(side_effects_Form_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000362", "Err: " + e.getMessage());
                    //Toast.makeText(side_effects_Form_Activity.this, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000362", "error    " + error.getMessage());
//                Toast.makeText(side_effects_Form_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();


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


        DatePickerDialog datePickerDialog = new DatePickerDialog(co_morbidity_Form_Activity.this, R.style.DatePickerDialog,
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
