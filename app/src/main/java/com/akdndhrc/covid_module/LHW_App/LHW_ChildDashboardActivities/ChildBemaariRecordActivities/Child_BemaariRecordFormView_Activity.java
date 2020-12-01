package com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildBemaariRecordActivities;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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


public class Child_BemaariRecordFormView_Activity extends AppCompatActivity {

    Context ctx = Child_BemaariRecordFormView_Activity.this;
    //et_chout_zakham_jalna et_kharish et_jins_zanana_amraaz  et_mumkina_tea_be_ki_tashkhees_kai_lye_reffer, et_tashkhees_shuda_amraaz_tea_be,
    //            et_muawnat_doraan_elaaj_tea_be,
    EditText et_tareekh_indraj, et_ishaal, et_khansi_aur_saans_ki_bemaari, et_bukhar, et_khoon_ki_kami, et_ankhon_ki_bemaari, et_pait_kai_keeray, et_mumkina_malaria,
            et_elaaj, et_refferal_ki_waja, et_refferal_hospital;

    Spinner sp_jins;
    Button btn_jamaa_kre;
    RelativeLayout rl_navigation_drawer, rl_home;
    ImageView iv_navigation_drawer, iv_home, iv_editform;
    // CustomerAdapter adapter = null;
    // ArrayList<Customer> customers = null;
    double latitude;
    double longitude;
    // GPSTracker class
    GPSTracker gps;
    String child_uid, TodayDate;

    private int mYear, mMonth, mDay;
    int date_for_condition = 0;
    int month_for_condition = 0;

    public String hold_age_date_condition = "fromage";
    String monthf2, dayf2, yearf2 = "null", record_date, added_on;

    ProgressBar pbProgress;
    JSONObject jsonObject;
    Dialog alertDialog;
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid;

    CheckBox checkbox_ishaal, checkbox_khansi_aur_saans, checkbox_bukhar, checkbox_khoon_ki_kami, checkbox_ankhon_ki_bemaari, checkbox_mumkina_malaria,
            checkbox_pait_ke_keray, checkbox_elaaj;
    LinearLayout ll_ishal, ll_khansi_aur_saans_ki_bemaari, ll_bukhar, ll_khoon_ki_kami, ll_ankhon_ki_bemaari, ll_mumkina_malaria,
            ll_pait_ke_keray, ll_elaaj;
    Spinner sp_ishal_medicine, sp_khansi_aur_saans_ki_bemaari, sp_bukhar, sp_khoon_ki_kami, sp_ankhon_ki_bemaari, sp_mumkina_malaria,
            sp_pait_ke_keray, sp_elaaj;


    String ishaal = "0", khansi_aur_saans = "0", bukhar, khoon_ki_kami = "0", ankhon_ki_bemaari = "0", mumkina_malaria = "0", pait_ke_keray = "0", elaaj = "0";

    String medicine_type_1 = "-1", medicine_type_2 = "-1", medicine_type_3 = "-1", medicine_type_4 = "-1", medicine_type_5 = "-1", medicine_type_6 = "-1", medicine_type_7 = "-1",
            medicine_type_8 = "-1";

    EditText et_tablet_quantity_1, et_tablet_quantity_2, et_tablet_quantity_3, et_tablet_quantity_4, et_tablet_quantity_5, et_tablet_quantity_6, et_tablet_quantity_7, et_tablet_quantity_8;

    String[] x = new String[8];
    long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_bemaari_record_form);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Child_BemaariRecordFormView_Activity.class));

        child_uid = getIntent().getExtras().getString("u_id");
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
            Log.d("000987", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000987", "Shared Err:" + e.getMessage());
        }


        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_editform = findViewById(R.id.iv_editform);
        iv_navigation_drawer.setVisibility(View.GONE);
        iv_editform.setVisibility(View.VISIBLE);
        // iv_home.setVisibility(View.GONE);


        try {
            serviceLocation = new ServiceLocation(ctx);
            serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            serviceLocation.callAsynchronousTask();
        } catch (Exception e) {
            Log.d("000987", "GPS Service Err:  " + e.getMessage());
        }

//        check_gps();

        //EditText
        et_tareekh_indraj = findViewById(R.id.et_tareekh_indraj);
        et_ishaal = findViewById(R.id.et_ishaal);
        et_khansi_aur_saans_ki_bemaari = findViewById(R.id.et_khansi_aur_saans_ki_bemaari);
        et_bukhar = findViewById(R.id.et_bukhar);
        //  et_chout_zakham_jalna = findViewById(R.id.et_chout_zakham_jalna);
        et_khoon_ki_kami = findViewById(R.id.et_khoon_ki_kami);
        //  et_kharish = findViewById(R.id.et_kharish);
        et_ankhon_ki_bemaari = findViewById(R.id.et_ankhon_ki_bemaari);
        // et_jins_zanana_amraaz = findViewById(R.id.et_jins_zanana_amraaz);
        et_pait_kai_keeray = findViewById(R.id.et_pait_kai_keeray);
        et_mumkina_malaria = findViewById(R.id.et_mumkina_malaria);
        //et_mumkina_tea_be_ki_tashkhees_kai_lye_reffer = findViewById(R.id.et_mumkina_tea_be_ki_tashkhees_kai_lye_reffer);
        //et_tashkhees_shuda_amraaz_tea_be = findViewById(R.id.et_tashkhees_shuda_amraaz_tea_be);
        //et_muawnat_doraan_elaaj_tea_be = findViewById(R.id.et_muawnat_doraan_elaaj_tea_be);
        et_elaaj = findViewById(R.id.et_elaaj);
        et_refferal_ki_waja = findViewById(R.id.et_refferal_ki_waja);
        et_refferal_hospital = findViewById(R.id.et_refferal_hospital);


        et_tablet_quantity_1 = findViewById(R.id.et_tablet_quantity_1);
        et_tablet_quantity_2 = findViewById(R.id.et_tablet_quantity_2);
        et_tablet_quantity_3 = findViewById(R.id.et_tablet_quantity_3);
        et_tablet_quantity_4 = findViewById(R.id.et_tablet_quantity_4);
        et_tablet_quantity_5 = findViewById(R.id.et_tablet_quantity_5);
        et_tablet_quantity_6 = findViewById(R.id.et_tablet_quantity_6);
        et_tablet_quantity_7 = findViewById(R.id.et_tablet_quantity_7);
        et_tablet_quantity_8 = findViewById(R.id.et_tablet_quantity_8);



        /*//Spinner
        sp_jins = findViewById(R.id.sp_jins);*/

        //Button
        btn_jamaa_kre = findViewById(R.id.submit);
        btn_jamaa_kre.setVisibility(View.GONE);


//progress bar
        pbProgress = findViewById(R.id.pbProgress);


        //Spinner
        sp_ishal_medicine = findViewById(R.id.sp_ishal_medicine);
        sp_khansi_aur_saans_ki_bemaari = findViewById(R.id.sp_khansi_aur_saans_ki_bemaari);
        sp_bukhar = findViewById(R.id.sp_bukhar);
        sp_khoon_ki_kami = findViewById(R.id.sp_khoon_ki_kami);
        sp_ankhon_ki_bemaari = findViewById(R.id.sp_ankhon_ki_bemaari);
        sp_mumkina_malaria = findViewById(R.id.sp_mumkina_malaria);
        sp_pait_ke_keray = findViewById(R.id.sp_pait_kai_keeray);
        sp_elaaj = findViewById(R.id.sp_elaaj);

        //CheckBox
        checkbox_ishaal = findViewById(R.id.checkbox_ishaal);
        checkbox_khansi_aur_saans = findViewById(R.id.checkbox_khansi_aur_saans);
        checkbox_bukhar = findViewById(R.id.checkbox_bukhar);
        checkbox_khoon_ki_kami = findViewById(R.id.checkbox_khoon_ki_kami);
        checkbox_ankhon_ki_bemaari = findViewById(R.id.checkbox_ankhon_ki_bemaari);
        checkbox_pait_ke_keray = findViewById(R.id.checkbox_pait_kai_keeray);
        checkbox_mumkina_malaria = findViewById(R.id.checkbox_mumkina_malaria);
        checkbox_elaaj = findViewById(R.id.checkbox_elaaj);

        //LinearLayout
        ll_ishal = findViewById(R.id.ll_ishal);
        ll_khansi_aur_saans_ki_bemaari = findViewById(R.id.ll_khansi_aur_saans_ki_bemaari);
        ll_bukhar = findViewById(R.id.ll_bukhar);
        ll_khoon_ki_kami = findViewById(R.id.ll_khoon_ki_kami);
        ll_ankhon_ki_bemaari = findViewById(R.id.ll_ankhon_ki_bemaari);
        ll_pait_ke_keray = findViewById(R.id.ll_pait_kai_keeray);
        ll_mumkina_malaria = findViewById(R.id.ll_mumkina_malaria);
        ll_elaaj = findViewById(R.id.ll_elaaj);


        spineer_data();

        checkbox_ishaal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_ishaal.isChecked()) {
                    ishaal = "1";
                    ll_ishal.setVisibility(View.VISIBLE);
                    Log.d("000987", "Ishaal IF: " + ishaal);
                } else {
                    ll_ishal.setVisibility(View.GONE);
                    ishaal = "0";
                    sp_ishal_medicine.setSelection(0);
                    et_tablet_quantity_1.getText().clear();
                    Log.d("000987", "Ishaal ELSE: " + ishaal);
                }
            }
        });

        checkbox_khansi_aur_saans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_khansi_aur_saans.isChecked()) {
                    khansi_aur_saans = "1";
                    ll_khansi_aur_saans_ki_bemaari.setVisibility(View.VISIBLE);
                    Log.d("000987", "khansi_aur_saans IF: " + khansi_aur_saans);
                } else {
                    ll_khansi_aur_saans_ki_bemaari.setVisibility(View.GONE);
                    khansi_aur_saans = "0";
                    sp_khansi_aur_saans_ki_bemaari.setSelection(0);
                    et_tablet_quantity_2.getText().clear();
                    Log.d("000987", "khansi_aur_saans ELSE: " + khansi_aur_saans);
                }
            }
        });

        checkbox_bukhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_bukhar.isChecked()) {
                    bukhar = "1";
                    ll_bukhar.setVisibility(View.VISIBLE);
                    Log.d("000987", "bukhar: " + bukhar);
                } else {
                    ll_bukhar.setVisibility(View.GONE);
                    bukhar = "0";
                    sp_bukhar.setSelection(0);
                    et_tablet_quantity_3.getText().clear();
                    Log.d("000987", "bukhar ELSE: " + bukhar);
                }
            }
        });

        checkbox_khoon_ki_kami.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_khoon_ki_kami.isChecked()) {
                    khoon_ki_kami = "1";
                    ll_khoon_ki_kami.setVisibility(View.VISIBLE);
                    Log.d("000987", "khoon_ki_kami.: " + khoon_ki_kami);
                } else {
                    ll_khoon_ki_kami.setVisibility(View.GONE);
                    khoon_ki_kami = "0";
                    sp_khoon_ki_kami.setSelection(0);
                    et_tablet_quantity_4.getText().clear();
                    Log.d("000987", "khoon_ki_kami ELSE: " + khoon_ki_kami);
                }
            }
        });


        checkbox_ankhon_ki_bemaari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_ankhon_ki_bemaari.isChecked()) {
                    ankhon_ki_bemaari = "1";
                    ll_ankhon_ki_bemaari.setVisibility(View.VISIBLE);
                    Log.d("000987", "ankhon_ki_bemaari.: " + ankhon_ki_bemaari);
                } else {
                    ll_ankhon_ki_bemaari.setVisibility(View.GONE);
                    ankhon_ki_bemaari = "0";
                    sp_ankhon_ki_bemaari.setSelection(0);
                    et_tablet_quantity_5.getText().clear();
                    Log.d("000987", "ankhon_ki_bemaari ELSE: " + ankhon_ki_bemaari);
                }
            }
        });


        checkbox_pait_ke_keray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_pait_ke_keray.isChecked()) {
                    pait_ke_keray = "1";
                    ll_pait_ke_keray.setVisibility(View.VISIBLE);
                    Log.d("000987", "pait kai keray.: " + pait_ke_keray);
                } else {
                    ll_pait_ke_keray.setVisibility(View.GONE);
                    pait_ke_keray = "0";
                    sp_pait_ke_keray.setSelection(0);
                    et_tablet_quantity_6.getText().clear();
                    Log.d("000987", "pait_ke_keray ELSE: " + pait_ke_keray);
                }
            }
        });

        checkbox_mumkina_malaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_mumkina_malaria.isChecked()) {
                    mumkina_malaria = "1";
                    ll_mumkina_malaria.setVisibility(View.VISIBLE);
                    Log.d("000987", "mumkina_malaria.: " + mumkina_malaria);
                } else {
                    ll_mumkina_malaria.setVisibility(View.GONE);
                    mumkina_malaria = "0";
                    sp_mumkina_malaria.setSelection(0);
                    et_tablet_quantity_7.getText().clear();
                    Log.d("000987", "mumkina_malaria ELSE: " + mumkina_malaria);
                }
            }
        });


        checkbox_elaaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_elaaj.isChecked()) {
                    elaaj = "1";
                    ll_elaaj.setVisibility(View.VISIBLE);
                    Log.d("000987", "elaaj: " + elaaj);
                } else {
                    ll_elaaj.setVisibility(View.GONE);
                    elaaj = "0";
                    sp_elaaj.setSelection(0);
                    et_tablet_quantity_8.getText().clear();
                    Log.d("000987", "elaaj ELSE: " + elaaj);
                }
            }
        });




        read_data();

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

                update_data(v);

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


                        et_ishaal.setEnabled(true);
                        et_khansi_aur_saans_ki_bemaari.setEnabled(true);
                        et_bukhar.setEnabled(true);
                        et_khoon_ki_kami.setEnabled(true);
                        et_ankhon_ki_bemaari.setEnabled(true);
                        et_pait_kai_keeray.setEnabled(true);
                        et_mumkina_malaria.setEnabled(true);
                        et_elaaj.setEnabled(true);
                        //   et_refferal_ki_waja.setEnabled(true);
                        //et_refferal_hospital.setEnabled(true);


                        et_tablet_quantity_1.setEnabled(true);
                        et_tablet_quantity_2.setEnabled(true);
                        et_tablet_quantity_3.setEnabled(true);
                        et_tablet_quantity_4.setEnabled(true);
                        et_tablet_quantity_5.setEnabled(true);
                        et_tablet_quantity_6.setEnabled(true);
                        et_tablet_quantity_7.setEnabled(true);
                        et_tablet_quantity_8.setEnabled(true);


                        checkbox_ishaal.setEnabled(true);
                        checkbox_khansi_aur_saans.setEnabled(true);
                        checkbox_bukhar.setEnabled(true);
                        checkbox_khoon_ki_kami.setEnabled(true);
                        checkbox_ankhon_ki_bemaari.setEnabled(true);
                        checkbox_pait_ke_keray.setEnabled(true);
                        checkbox_mumkina_malaria.setEnabled(true);
                        checkbox_elaaj.setEnabled(true);

                        sp_ishal_medicine.setEnabled(true);
                        sp_khansi_aur_saans_ki_bemaari.setEnabled(true);
                        sp_bukhar.setEnabled(true);
                        sp_khoon_ki_kami.setEnabled(true);
                        sp_ankhon_ki_bemaari.setEnabled(true);
                        sp_pait_ke_keray.setEnabled(true);
                        sp_mumkina_malaria.setEnabled(true);
                        sp_elaaj.setEnabled(true);


                        btn_jamaa_kre.setVisibility(View.VISIBLE);
                        pbProgress.setVisibility(View.GONE);
                        iv_editform.setVisibility(View.GONE);


                    }
                }, 2500);


            }
        });

    }

    private void update_data(final View v) {


        if (serviceLocation.showCurrentLocation() == true) {

            latitude = serviceLocation.getLatitude();
            longitude = serviceLocation.getLongitude();

            Log.d("000987", " latitude: " + latitude);
            Log.d("000987", " longitude: " + longitude);
        } else {
            try {
                serviceLocation.doAsynchronousTask.cancel();
            } catch (Exception e) {
            }
            try {
                Lister ls = new Lister(ctx);
                ls.createAndOpenDB();

                String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from CBEMARI");

                if (Integer.parseInt(mData[0][2]) > 0) {
                    JSONObject jsonObject = new JSONObject(mData[0][1]);
                    Log.d("000987", "  Last Latitude: " + jsonObject.getString("lat"));
                    Log.d("000987", "  Last Longitude: " + jsonObject.getString("lng"));

                    latitude = Double.parseDouble(jsonObject.getString("lat"));
                    longitude = Double.parseDouble(jsonObject.getString("lng"));

                    Toast.makeText(ctx, R.string.dataGPS, Toast.LENGTH_SHORT).show();
                } else {
                    latitude = Double.parseDouble("0.0");
                    longitude = Double.parseDouble("0.0");
                    Toast.makeText(ctx, R.string.notDataGPS, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.d("000987", "Read CBEMARI Error: " + e.getMessage());
            }
        }

        btn_jamaa_kre.setVisibility(View.GONE);
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        Log.d("000987", " mLastClickTime: " + mLastClickTime);

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

            if (jsonObject.has("lat")) {
                jsonObject.put("lat", "" + String.valueOf(latitude));
                jsonObject.put("lng", "" + String.valueOf(longitude));

                jsonObject.put("cb_ishaal", "" + ishaal);
                jsonObject.put("ishaal", "" + et_ishaal.getText().toString());
                if (String.valueOf(sp_ishal_medicine.getSelectedItem()).equalsIgnoreCase("null")) {
                    jsonObject.put("sp_ishaal", "" + "-1");
                    jsonObject.put("ishaal_medicine_quantity", "");
                } else {
                    jsonObject.put("sp_ishaal", "" + String.valueOf(sp_ishal_medicine.getSelectedItem()));
                    jsonObject.put("ishaal_medicine_quantity", "" + et_tablet_quantity_1.getText().toString());
                }
                jsonObject.put("sp_ishaal_pos", "" + String.valueOf(sp_ishal_medicine.getSelectedItemPosition() - 1));


                jsonObject.put("cb_khansi_sans", "" + khansi_aur_saans);
                jsonObject.put("khansi_sans", "" + et_khansi_aur_saans_ki_bemaari.getText().toString());
                if (String.valueOf(sp_khansi_aur_saans_ki_bemaari.getSelectedItem()).equalsIgnoreCase("null")) {
                    jsonObject.put("sp_khansi_sans", "" + "-1");
                    jsonObject.put("khansi_medicine_quantity", "");
                } else {
                    jsonObject.put("sp_khansi_sans", "" + String.valueOf(sp_khansi_aur_saans_ki_bemaari.getSelectedItem()));
                    jsonObject.put("khansi_medicine_quantity", "" + et_tablet_quantity_2.getText().toString());
                }
                jsonObject.put("sp_khansi_sans_pos", "" + String.valueOf(sp_khansi_aur_saans_ki_bemaari.getSelectedItemPosition() - 1));


                jsonObject.put("cb_bukhar", "" + bukhar);
                jsonObject.put("bukhar", "" + et_bukhar.getText().toString());
                if (String.valueOf(sp_bukhar.getSelectedItem()).equalsIgnoreCase("null")) {
                    jsonObject.put("sp_bukhar", "" + "-1");
                    jsonObject.put("bukhar_medicine_quantity", "");
                } else {
                    jsonObject.put("sp_bukhar", "" + String.valueOf(sp_bukhar.getSelectedItem()));
                    jsonObject.put("bukhar_medicine_quantity", "" + et_tablet_quantity_3.getText().toString());
                }
                jsonObject.put("sp_bukhar_pos", "" + String.valueOf(sp_bukhar.getSelectedItemPosition() - 1));


                jsonObject.put("cb_khoon_ki_kami", "" + khoon_ki_kami);
                jsonObject.put("khoon_ki_kami", "" + et_khoon_ki_kami.getText().toString());
                if (String.valueOf(sp_khoon_ki_kami.getSelectedItem()).equalsIgnoreCase("null")) {
                    jsonObject.put("sp_khoon_ki_kami", "" + "-1");
                    jsonObject.put("khoon_ki_kami_medicine_quantity", "");
                } else {
                    jsonObject.put("sp_khoon_ki_kami", "" + String.valueOf(sp_khoon_ki_kami.getSelectedItem()));
                    jsonObject.put("khoon_ki_kami_medicine_quantity", "" + et_tablet_quantity_4.getText().toString());
                }
                jsonObject.put("sp_khoon_ki_kami_pos", "" + String.valueOf(sp_khoon_ki_kami.getSelectedItemPosition() - 1));


                jsonObject.put("cb_ankhon_ki_bemaari", "" + ankhon_ki_bemaari);
                jsonObject.put("ankhun_ki_bemari", "" + et_ankhon_ki_bemaari.getText().toString());
                if (String.valueOf(sp_ankhon_ki_bemaari.getSelectedItem()).equalsIgnoreCase("null")) {
                    jsonObject.put("sp_ankhon_ki_bemaari", "" + "-1");
                    jsonObject.put("ankhon_ki_bemaari_medicine_quantity", "");
                } else {
                    jsonObject.put("sp_ankhon_ki_bemaari", "" + String.valueOf(sp_ankhon_ki_bemaari.getSelectedItem()));
                    jsonObject.put("ankhon_ki_bemaari_medicine_quantity", "" + et_tablet_quantity_5.getText().toString());
                }
                jsonObject.put("sp_ankhon_ki_bemaari_pos", "" + String.valueOf(sp_ankhon_ki_bemaari.getSelectedItemPosition() - 1));


                jsonObject.put("cb_pait_ke_keray", "" + pait_ke_keray);
                jsonObject.put("paitkekiray", "" + et_pait_kai_keeray.getText().toString());
                if (String.valueOf(sp_pait_ke_keray.getSelectedItem()).equalsIgnoreCase("null")) {
                    jsonObject.put("sp_pait_ke_keray", "" + "-1");
                    jsonObject.put("paitkekiray_medicine_quantity", "");
                } else {
                    jsonObject.put("sp_pait_ke_keray", "" + String.valueOf(sp_pait_ke_keray.getSelectedItem()));
                    jsonObject.put("paitkekiray_medicine_quantity", "" + et_tablet_quantity_6.getText().toString());
                }
                jsonObject.put("sp_pait_ke_keray_pos", "" + String.valueOf(sp_pait_ke_keray.getSelectedItemPosition() - 1));

                jsonObject.put("cb_malaria", "" + mumkina_malaria);
                jsonObject.put("malaria", "" + et_mumkina_malaria.getText().toString());
                if (String.valueOf(sp_mumkina_malaria.getSelectedItem()).equalsIgnoreCase("null")) {
                    jsonObject.put("sp_malaria", "" + "-1");
                    jsonObject.put("malaria_medicine_quantity", "");
                } else {
                    jsonObject.put("sp_malaria", "" + String.valueOf(sp_mumkina_malaria.getSelectedItem()));
                    jsonObject.put("malaria_medicine_quantity", "" + et_tablet_quantity_7.getText().toString());
                }
                jsonObject.put("sp_malaria_pos", "" + String.valueOf(sp_mumkina_malaria.getSelectedItemPosition() - 1));


                jsonObject.put("cb_elaaj", "" + elaaj);
                jsonObject.put("ellaj", "" + et_elaaj.getText().toString());
                if (String.valueOf(sp_elaaj.getSelectedItem()).equalsIgnoreCase("null")) {
                    jsonObject.put("sp_elaaj", "" + "-1");
                    jsonObject.put("elaaj_medicine_quantity", "");
                } else {
                    jsonObject.put("sp_elaaj", "" + String.valueOf(sp_elaaj.getSelectedItem()));
                    jsonObject.put("elaaj_medicine_quantity", "" + et_tablet_quantity_8.getText().toString());
                }
                jsonObject.put("sp_elaaj_pos", "" + String.valueOf(sp_elaaj.getSelectedItemPosition() - 1));

                //   jsonObject.put("referal_reason", "" + et_refferal_ki_waja.getText().toString());
                //jsonObject.put("referal_facility", "" + et_refferal_hospital.getText().toString());//spinner
                jsonObject.put("record_updated_date", "" + TodayDate);
                jsonObject.put("added_on", "" + cur_added_on);


            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();
                        String update_record = "UPDATE CBEMARI SET " +
                                "data='" + jsonObject.toString() + "'," +
                                "is_synced='" + 0 + "'" +
                                "WHERE member_uid = '" + child_uid + "'  AND record_data = '" + record_date + "'  AND added_on= '" + added_on + "'";

                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d("000987", "Data: " + update_record);
                        Log.d("000987", "Query: " + res.toString());

                        /*if (Utils.haveNetworkConnection(ctx) > 0) {

                            sendPostRequest(child_uid, et_tareekh_indraj.getText().toString(), String.valueOf(jsonObject), login_useruid, added_on);
                        } else {
                            Toast.makeText(ctx, "ڈیٹااپڈیٹ ہوگیا ہے", Toast.LENGTH_SHORT).show();
                        }*/
                        if (res.toString().equalsIgnoreCase("true")) {

                            final Snackbar snackbar = Snackbar.make(v, R.string.dataUpdated, Snackbar.LENGTH_SHORT);
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
                                //  Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();
                            }

                            update_medicineLog();


                        } else {
                            final Snackbar snackbar = Snackbar.make(v, R.string.noDataUpdated, Snackbar.LENGTH_SHORT);
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
                        alertDialog.dismiss();
                        Log.d("000987", " Error" + e.getMessage());
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
            alertDialog.dismiss();
            Toast.makeText(ctx, R.string.error, Toast.LENGTH_SHORT).show();
            Log.d("000987", " Error" + e.getMessage());
        }
    }

    private void update_medicineLog() {

        try {

            for (int i = 0; i < x.length; i++) {

                Log.d("000852", "LENGTH: " + x.length);

                Log.d("000852", "Array: " + i + "@" + x[i].split("@")[0]);
                Log.d("000852", "Array: " + i + "@" + x[i].split("@")[1]);
                Log.d("000852", "Array: " + i + "@" + x[i].split("@")[2]);
                Log.d("000852", "Array: " + i + "@" + x[i].split("@")[3]);

                JSONObject jobj_medicine_stock = new JSONObject();
                jobj_medicine_stock.put(getString(R.string.medNameEng), "" + x[i].split("@")[4]);
                jobj_medicine_stock.put(getString(R.string.medQuantityEng), "" + x[i].split("@")[2]);
                jobj_medicine_stock.put(getString(R.string.medTypeEng), "" + x[i].split("@")[3]);


                Lister ls = new Lister(ctx);
                ls.createAndOpenDB();
                String update_record = "UPDATE MEDICINE_LOG SET " +
                        "medicine_id='" + x[i].split("@")[0] + "'," +
                        "metadata='" + jobj_medicine_stock + "'" +
                        "WHERE member_uid = '" + child_uid + "'  AND disease = '" + x[i].split("@")[1] + "' AND record_data = '" + record_date + "'";


                ls.executeNonQuery(update_record);

                Boolean res = ls.executeNonQuery(update_record);
                Log.d("000852", "updated Med Data: " + update_record);
                Log.d("000852", "Query: " + res.toString());
            }

        } catch (Exception e) {
            Log.d("000852", " Error: " + e.getMessage());

            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            for (int i = 0; i < x.length; i++) {

                if (x[i] != null) {
                    Log.d("000852", "Array: " + i + "@" + x[i].split("@")[0]);
                    Log.d("000852", "Array: " + i + "@" + x[i].split("@")[1]);
                    try {

                        //String cur_added_on = String.valueOf(System.currentTimeMillis());

                        JSONObject jobj_medicine_stock = new JSONObject();
                        jobj_medicine_stock.put(String.valueOf(R.string.medNameEng), "" + x[i].split("@")[4]);
                        jobj_medicine_stock.put(String.valueOf(R.string.medQuantityEng), "" + x[i].split("@")[2]);
                        jobj_medicine_stock.put(String.valueOf(R.string.medTypeEng), "" + x[i].split("@")[3]);


                        String ans1 = "insert or ignore into MEDICINE_LOG (member_uid, medicine_id, record_data,type,disease,metadata,added_by,added_on)" +
                                "values" +
                                "(" +
                                "'" + child_uid + "'," +
                                "'" + x[i].split("@")[0] + "'," +
                                "'" + TodayDate + "'," +
                                "'CBemari'," +
                                "'" + x[i].split("@")[1] + "'," +
                                "'" + jobj_medicine_stock + "'," +
                                "'" + login_useruid + "'," +
                                "'" + added_on + "'" +
                                ")";

                        Boolean res = ls.executeNonQuery(ans1);
                        Log.d("000852", "Insert Data Medicine: " + ans1);
                        Log.d("000852", "Query Medicine: " + res.toString());

                        //    Toast.makeText(ctx, "Addedd", Toast.LENGTH_SHORT).show();

                    } catch (Exception e1) {
                        Log.d("000852", "Err: " + e1.getMessage());
                    }

                } else {
                    Log.d("000852", "NULLLL: ");
                }
            }
        }
    }


    private void sendPostRequest(final String member_uid, final String record_data,
                                 final String data, final String added_by, final String added_on) {

        // String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/child/illness";
        String url = UrlClass.update_illness_url;

        Log.d("000987", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jobj = new JSONObject(response);
                    if (jobj.getBoolean("success")) {

                        Log.d("000987", "Response:    " + response);

                        Lister ls = new Lister(Child_BemaariRecordFormView_Activity.this);
                        ls.createAndOpenDB();
                        String update_record = "UPDATE CBEMARI SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + child_uid + "'  AND record_data = '" + record_date + "'  AND added_on= '" + added_on + "'";

                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d("000987", "Updated Data: " + update_record);
                        Log.d("000987", "Updated Query: " + res.toString());


                        Toast tt = Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();
                        //  Toast.makeText(ctx, "Data updated successfully.", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000987", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        // Toast.makeText(Child_BemaariRecordFormView_Activity.this, "Data has not been updated to the service.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000987", "catch: " + e.getMessage());
                    // Toast.makeText(Child_BemaariRecordFormView_Activity.this, "Data has been updated incorrectly.", Toast.LENGTH_SHORT).show();
                    Toast tt = Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT);
                    tt.setGravity(Gravity.CENTER, 0, 0);
                    tt.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000987", "error:" + error.getMessage());
                //Toast.makeText(Child_BemaariRecordFormView_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                //Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
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

                Log.d("000987", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000987", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }


    private void spineer_data() {
        // Select sp_adptr_illness_medicine
       /* final ArrayAdapter<CharSequence> adptr_illness_medicine = ArrayAdapter.createFromResource(this, R.array.array_child_illness_medicine, android.R.layout.simple_spinner_item);
        adptr_illness_medicine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_ishal_medicine.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_illness_medicine,
                        R.layout.spinner_illness_medicine_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));*/

        Utils.setSpinnerMedicines(ctx, sp_ishal_medicine);
        sp_ishal_medicine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (sp_ishal_medicine.getSelectedItemPosition() > 0) {

                    if (sp_ishal_medicine.getSelectedItem().toString().contains("Tab")) {
                        medicine_type_1 = "1";
                        et_tablet_quantity_1.setHint(R.string.tabQuantity);

                    } else if (sp_ishal_medicine.getSelectedItem().toString().contains("Syp")) {
                        medicine_type_1 = "2";
                        et_tablet_quantity_1.setHint(R.string.sypQuantity);
                    } else if (sp_ishal_medicine.getSelectedItem().toString().contains("Lotion")) {
                        medicine_type_1 = "3";
                        et_tablet_quantity_1.setHint(R.string.lotionQuantity);
                    } else if (sp_ishal_medicine.getSelectedItem().toString().equalsIgnoreCase("ORS")) {
                        medicine_type_1 = "4";
                        et_tablet_quantity_1.setHint(R.string.sachetQuantity);
                    } else if (sp_ishal_medicine.getSelectedItem().toString().startsWith("Eye")) {
                        medicine_type_1 = "5";
                        et_tablet_quantity_1.setHint(R.string.bottleQuantity);
                    } else {
                        medicine_type_1 = "0";
                        et_tablet_quantity_1.setHint(R.string.quantity);
                    }
                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        final String[][] mData = ls.executeReader("SELECT uid from MEDICINE where name ='" + sp_ishal_medicine.getSelectedItem() + "'");
                        Log.d("000357", "UID: " + mData[0][0]);

                        et_tablet_quantity_1.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                try {
                                    if (et_tablet_quantity_1.getText().toString().isEmpty()) {
                                        x[0] = mData[0][0] + "@" + "sp_ishal_medicine" + "@" + "0"+"@"+medicine_type_1+"@"+sp_ishal_medicine.getSelectedItem().toString();
                                    } else {
                                        x[0] = mData[0][0] + "@" + "sp_ishal_medicine" + "@" + et_tablet_quantity_1.getText().toString()+"@"+medicine_type_1+"@"+sp_ishal_medicine.getSelectedItem().toString();
                                    }
                                } catch (Exception e) {
                                    Log.d("000987", "TRY Array Err: " + e.getMessage());
                                }
                                Log.d("000987", "Array: " + x[0]);
                            }
                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        if (et_tablet_quantity_1.getText().toString().isEmpty()) {
                            x[0] = mData[0][0] + "@" + "sp_ishal_medicine" + "@" + "0"+"@"+medicine_type_1+"@"+sp_ishal_medicine.getSelectedItem().toString();
                        } else {
                            x[0] = mData[0][0] + "@" + "sp_ishal_medicine" + "@" + et_tablet_quantity_1.getText().toString()+"@"+medicine_type_1+"@"+sp_ishal_medicine.getSelectedItem().toString();
                        }

                        Log.d("000987", "Arrays: " + x[0]);
                       /* if (arrayList_spinner.get(0).length() > 0)
                        {
                            Log.d("000987","IF Array: " +arrayList_spinner);
                            arrayList_spinner.remove(0);
                            Log.d("000987","If Array: " +arrayList_spinner);
                            arrayList_spinner.add(0,mData[0][0]+"@"+"0");
                            Log.d("000987","IF Array: " +arrayList_spinner);
                        }
                        else{

                            arrayList_spinner.add(0,mData[0][0]);
                            Log.d("000987","Arrays: " +arrayList_spinner);
                        }*/
                    } catch (Exception e) {
                        Log.d("000357", "Err: " + e.getMessage());
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

/////////////////sp_khansi_aur_saans_ki_bemaari
       /* sp_khansi_aur_saans_ki_bemaari.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_illness_medicine,
                        R.layout.spinner_illness_medicine_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));*/

        Utils.setSpinnerMedicines(ctx, sp_khansi_aur_saans_ki_bemaari);
        sp_khansi_aur_saans_ki_bemaari.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (sp_khansi_aur_saans_ki_bemaari.getSelectedItemPosition() > 0) {

                    if (sp_khansi_aur_saans_ki_bemaari.getSelectedItem().toString().contains("Tab")) {
                        medicine_type_2 = "1";
                        et_tablet_quantity_2.setHint(R.string.tabQuantity);
                    } else if (sp_khansi_aur_saans_ki_bemaari.getSelectedItem().toString().contains("Syp")) {
                        medicine_type_2 = "2";
                        et_tablet_quantity_2.setHint(R.string.sypQuantity);
                    } else if (sp_khansi_aur_saans_ki_bemaari.getSelectedItem().toString().contains("Lotion")) {
                        medicine_type_2 = "3";
                        et_tablet_quantity_2.setHint(R.string.lotionQuantity);
                    } else if (sp_khansi_aur_saans_ki_bemaari.getSelectedItem().toString().equalsIgnoreCase("ORS")) {
                        medicine_type_2 = "4";
                        et_tablet_quantity_2.setHint(R.string.sachetQuantity);
                    } else if (sp_khansi_aur_saans_ki_bemaari.getSelectedItem().toString().startsWith("Eye")) {
                        medicine_type_2 = "5";
                        et_tablet_quantity_2.setHint(R.string.bottleQuantity);
                    } else {
                        medicine_type_2 = "0";
                        et_tablet_quantity_2.setHint(R.string.quantity);
                    }

                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        final String[][] mData = ls.executeReader("SELECT uid from MEDICINE where name ='" + sp_khansi_aur_saans_ki_bemaari.getSelectedItem() + "'");
                        Log.d("000357", "UID: " + mData[0][0]);
                        //x[1] = mData[0][0] + "@" + "sp_khansi_aur_saans_ki_bemaari" + "@" + et_tablet_quantity_2.getText().toString();

                        et_tablet_quantity_2.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                try {
                                    if (et_tablet_quantity_2.getText().toString().isEmpty()) {
                                        x[1] = mData[0][0] + "@" + "sp_khansi_aur_saans_ki_bemaari" + "@" + "0"+"@"+medicine_type_2+"@"+sp_khansi_aur_saans_ki_bemaari.getSelectedItem().toString();
                                    } else {
                                        x[1] = mData[0][0] + "@" + "sp_khansi_aur_saans_ki_bemaari" + "@" + et_tablet_quantity_2.getText().toString()+"@"+medicine_type_2+"@"+sp_khansi_aur_saans_ki_bemaari.getSelectedItem().toString();
                                    }
                                } catch (Exception e) {
                                    Log.d("000987", "TRY Array Err: " + e.getMessage());
                                }
                                Log.d("000987", "Array: " + x[1]);
                            }
                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        if (et_tablet_quantity_2.getText().toString().isEmpty()) {
                            x[1] = mData[0][0] + "@" + "sp_khansi_aur_saans_ki_bemaari" + "@" + "0"+"@"+medicine_type_2+"@"+sp_khansi_aur_saans_ki_bemaari.getSelectedItem().toString();
                        } else {
                            x[1] = mData[0][0] + "@" + "sp_khansi_aur_saans_ki_bemaari" + "@" + et_tablet_quantity_2.getText().toString()+"@"+medicine_type_2+"@"+sp_khansi_aur_saans_ki_bemaari.getSelectedItem().toString();
                        }

                        // arrayList_spinner.add(position,mData[0][0]);
                        Log.d("000987", "Array: " + x[1]);
                    } catch (Exception e) {
                        Log.d("000357", "Err: " + e.getMessage());
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        /////////////////sp_bukhar
     /*   sp_bukhar.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_illness_medicine,
                        R.layout.spinner_illness_medicine_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));*/

        Utils.setSpinnerMedicines(ctx, sp_bukhar);

        sp_bukhar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (sp_bukhar.getSelectedItemPosition() > 0) {

                    if (sp_bukhar.getSelectedItem().toString().contains("Tab")) {
                        medicine_type_3 = "1";
                        et_tablet_quantity_3.setHint(R.string.tabQuantity);
                    } else if (sp_bukhar.getSelectedItem().toString().contains("Syp")) {
                        medicine_type_3 = "2";
                        et_tablet_quantity_3.setHint(R.string.sypQuantity);
                    } else if (sp_bukhar.getSelectedItem().toString().contains("Lotion")) {
                        medicine_type_3 = "3";
                        et_tablet_quantity_3.setHint(R.string.lotionQuantity);
                    } else if (sp_bukhar.getSelectedItem().toString().equalsIgnoreCase("ORS")) {
                        medicine_type_3 = "4";
                        et_tablet_quantity_3.setHint(R.string.sachetQuantity);
                    } else if (sp_bukhar.getSelectedItem().toString().startsWith("Eye")) {
                        medicine_type_3 = "5";
                        et_tablet_quantity_3.setHint(R.string.bottleQuantity);
                    } else {
                        medicine_type_3 = "0";
                        et_tablet_quantity_3.setHint(R.string.quantity);
                    }


                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        final String[][] mData = ls.executeReader("SELECT uid from MEDICINE where name ='" + sp_bukhar.getSelectedItem() + "'");
                        Log.d("000357", "UID: " + mData[0][0]);

                        //  x[2] = mData[0][0] + "@" + "sp_bukhar" + "@" + et_tablet_quantity_3.getText().toString();

                        et_tablet_quantity_3.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                try {
                                    if (et_tablet_quantity_3.getText().toString().isEmpty()) {
                                        x[2] = mData[0][0] + "@" + "sp_bukhar" + "@" + "0"+"@"+medicine_type_3+"@"+sp_bukhar.getSelectedItem().toString();
                                    } else {
                                        x[2] = mData[0][0] + "@" + "sp_bukhar" + "@" + et_tablet_quantity_3.getText().toString()+"@"+medicine_type_3+"@"+sp_bukhar.getSelectedItem().toString();
                                    }
                                } catch (Exception e) {
                                    Log.d("000987", "TRY Array Err: " + e.getMessage());
                                }
                                Log.d("000987", "Array: " + x[2]);
                            }
                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        if (et_tablet_quantity_3.getText().toString().isEmpty()) {
                            x[2] = mData[0][0] + "@" + "sp_bukhar" + "@" +"0"+"@"+medicine_type_3+"@"+sp_bukhar.getSelectedItem().toString();
                        } else {
                            x[2] = mData[0][0] + "@" + "sp_bukhar" + "@" + et_tablet_quantity_3.getText().toString()+"@"+medicine_type_3+"@"+sp_bukhar.getSelectedItem().toString();
                        }


                        Log.d("000987", "Array: " + x[2]);

                        //    arrayList_spinner.add(position,mData[0][0]);
                        //  Log.d("000987","Array: " +arrayList_spinner);
                    } catch (Exception e) {
                        Log.d("000357", "Err: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        /////////////////sp_khoon_ki_kami
    /*    sp_khoon_ki_kami.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_illness_medicine,
                        R.layout.spinner_illness_medicine_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));*/

        Utils.setSpinnerMedicines(ctx, sp_khoon_ki_kami);
        sp_khoon_ki_kami.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_khoon_ki_kami.getSelectedItemPosition() > 0) {

                    if (sp_khoon_ki_kami.getSelectedItem().toString().contains("Tab")) {
                        medicine_type_4 = "1";
                        et_tablet_quantity_4.setHint(R.string.tabQuantity);
                    } else if (sp_khoon_ki_kami.getSelectedItem().toString().contains("Syp")) {
                        medicine_type_4 = "2";
                        et_tablet_quantity_4.setHint(R.string.sypQuantity);
                    } else if (sp_khoon_ki_kami.getSelectedItem().toString().contains("Lotion")) {
                        medicine_type_4 = "3";
                        et_tablet_quantity_4.setHint(R.string.lotionQuantity);
                    } else if (sp_khoon_ki_kami.getSelectedItem().toString().equalsIgnoreCase("ORS")) {
                        medicine_type_4 = "4";
                        et_tablet_quantity_4.setHint(R.string.sachetQuantity);
                    } else if (sp_khoon_ki_kami.getSelectedItem().toString().startsWith("Eye")) {
                        medicine_type_4 = "5";
                        et_tablet_quantity_4.setHint(R.string.bottleQuantity);
                    } else {
                        medicine_type_4 = "0";
                        et_tablet_quantity_4.setHint(R.string.quantity);
                    }


                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        final String[][] mData = ls.executeReader("SELECT uid from MEDICINE where name ='" + sp_khoon_ki_kami.getSelectedItem() + "'");
                        Log.d("000357", "UID: " + mData[0][0]);
                        //   x[3] = mData[0][0] + "@" + "sp_khoon_ki_kami" + "@" + et_tablet_quantity_4.getText().toString();

                        et_tablet_quantity_4.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                try {
                                    if (et_tablet_quantity_4.getText().toString().isEmpty()) {
                                        x[3] = mData[0][0] + "@" + "sp_khoon_ki_kami" + "@" +  "0"+"@"+medicine_type_4+"@"+sp_khoon_ki_kami.getSelectedItem().toString();
                                    } else {
                                        x[3] = mData[0][0] + "@" + "sp_khoon_ki_kami" + "@" + et_tablet_quantity_4.getText().toString()+"@"+medicine_type_4+"@"+sp_khoon_ki_kami.getSelectedItem().toString();
                                    }
                                } catch (Exception e) {
                                    Log.d("000987", "TRY Array Err: " + e.getMessage());
                                }
                                Log.d("000987", "Array: " + x[3]);
                            }
                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        if (et_tablet_quantity_4.getText().toString().isEmpty()) {
                            x[3] = mData[0][0] + "@" + "sp_khoon_ki_kami" + "@" + "0"+"@"+medicine_type_4+"@"+sp_khoon_ki_kami.getSelectedItem().toString();
                        } else {
                            x[3] = mData[0][0] + "@" + "sp_khoon_ki_kami" + "@" + et_tablet_quantity_4.getText().toString()+"@"+medicine_type_4+"@"+sp_khoon_ki_kami.getSelectedItem().toString();
                        }


                        Log.d("000987", "Array: " + x[3]);


                        //  arrayList_spinner.add(3,mData[0][0]);
                        // Log.d("000987","Array: " +arrayList_spinner);
                    } catch (Exception e) {
                        Log.d("000357", "Err: " + e.getMessage());
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /////////////////sp_ankhon_ki_bemaari
    /*    sp_ankhon_ki_bemaari.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_illness_medicine,
                        R.layout.spinner_illness_medicine_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
*/

        Utils.setSpinnerMedicines(ctx, sp_ankhon_ki_bemaari);
        sp_ankhon_ki_bemaari.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (sp_ankhon_ki_bemaari.getSelectedItemPosition() > 0) {

                    if (sp_ankhon_ki_bemaari.getSelectedItem().toString().contains("Tab")) {
                        medicine_type_5 = "1";
                        et_tablet_quantity_5.setHint(R.string.tabQuantity);
                    } else if (sp_ankhon_ki_bemaari.getSelectedItem().toString().contains("Syp")) {
                        medicine_type_5 = "2";
                        et_tablet_quantity_5.setHint(R.string.sypQuantity);
                    } else if (sp_ankhon_ki_bemaari.getSelectedItem().toString().contains("Lotion")) {
                        medicine_type_5 = "3";
                        et_tablet_quantity_5.setHint(R.string.lotionQuantity);
                    } else if (sp_ankhon_ki_bemaari.getSelectedItem().toString().equalsIgnoreCase("ORS")) {
                        medicine_type_5 = "4";
                        et_tablet_quantity_5.setHint(R.string.sachetQuantity);
                    } else if (sp_ankhon_ki_bemaari.getSelectedItem().toString().startsWith("Eye")) {
                        medicine_type_5 = "5";
                        et_tablet_quantity_5.setHint(R.string.bottleQuantity);
                    } else {
                        medicine_type_5 = "0";
                        et_tablet_quantity_5.setHint(R.string.quantity);
                    }

                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        final String[][] mData = ls.executeReader("SELECT uid from MEDICINE where name ='" + sp_ankhon_ki_bemaari.getSelectedItem() + "'");

                        //x[4] = mData[0][0] + "@" + "sp_ankhon_ki_bemaari" + "@" + et_tablet_quantity_5.getText().toString();

                        et_tablet_quantity_5.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                try {
                                    if (et_tablet_quantity_5.getText().toString().isEmpty()) {
                                        x[4] = mData[0][0] + "@" + "sp_ankhon_ki_bemaari" + "@" +  "0"+"@"+medicine_type_5+"@"+sp_ankhon_ki_bemaari.getSelectedItem().toString();
                                    } else {
                                        x[4] = mData[0][0] + "@" + "sp_ankhon_ki_bemaari" + "@" + et_tablet_quantity_5.getText().toString()+"@"+medicine_type_5+"@"+sp_ankhon_ki_bemaari.getSelectedItem().toString();

                                    }
                                } catch (Exception e) {
                                    Log.d("000987", "TRY Array Err: " + e.getMessage());
                                }
                                Log.d("000987", "Array: " + x[4]);
                            }
                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        if (et_tablet_quantity_5.getText().toString().isEmpty()) {
                            x[4] = mData[0][0] + "@" + "sp_ankhon_ki_bemaari" + "@" + "0"+"@"+medicine_type_5+"@"+sp_ankhon_ki_bemaari.getSelectedItem().toString();
                        } else {
                            x[4] = mData[0][0] + "@" + "sp_ankhon_ki_bemaari" + "@" + et_tablet_quantity_5.getText().toString()+"@"+medicine_type_5+"@"+sp_ankhon_ki_bemaari.getSelectedItem().toString();
                        }

                        Log.d("000987", "Array: " + x[4]);

                        //Log.d("000357", "UID: " + mData[0][0]);
                        // arrayList_spinner.add(4,mData[0][0]);
                        //   Log.d("000987", "Array: " + arrayList_spinner);
                    } catch (Exception e) {
                        Log.d("000357", "Err: " + e.getMessage());
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        /////////////////sp_jins_zanana_amraaz
    /*    sp_pait_ke_keray.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_illness_medicine,
                        R.layout.spinner_illness_medicine_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
*/

        Utils.setSpinnerMedicines(ctx, sp_pait_ke_keray);
        sp_pait_ke_keray.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (sp_pait_ke_keray.getSelectedItemPosition() > 0) {

                    if (sp_pait_ke_keray.getSelectedItem().toString().contains("Tab")) {
                        medicine_type_6 = "1";
                        et_tablet_quantity_6.setHint(R.string.tabQuantity);
                    } else if (sp_pait_ke_keray.getSelectedItem().toString().contains("Syp")) {
                        medicine_type_6 = "2";
                        et_tablet_quantity_6.setHint(R.string.sypQuantity);
                    } else if (sp_pait_ke_keray.getSelectedItem().toString().contains("Lotion")) {
                        medicine_type_6 = "3";
                        et_tablet_quantity_6.setHint(R.string.lotionQuantity);
                    } else if (sp_pait_ke_keray.getSelectedItem().toString().equalsIgnoreCase("ORS")) {
                        medicine_type_6 = "4";
                        et_tablet_quantity_6.setHint(R.string.sachetQuantity);
                    } else if (sp_pait_ke_keray.getSelectedItem().toString().startsWith("Eye")) {
                        medicine_type_6 = "5";
                        et_tablet_quantity_6.setHint(R.string.bottleQuantity);
                    } else {
                        medicine_type_6 = "0";
                        et_tablet_quantity_6.setHint(R.string.quantity);
                    }

                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        final String[][] mData = ls.executeReader("SELECT uid from MEDICINE where name ='" + sp_pait_ke_keray.getSelectedItem() + "'");

                        //x[4] = mData[0][0] + "@" + "sp_pait_ke_keray" + "@" + et_tablet_quantity_5.getText().toString();

                        et_tablet_quantity_6.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                try {
                                    if (et_tablet_quantity_6.getText().toString().isEmpty()) {
                                        x[5] = mData[0][0] + "@" + "sp_pait_ke_keray" + "@" +  "0"+"@"+medicine_type_6+"@"+sp_pait_ke_keray.getSelectedItem().toString();
                                    } else {
                                        x[5] = mData[0][0] + "@" + "sp_pait_ke_keray" + "@" + et_tablet_quantity_6.getText().toString()+"@"+medicine_type_6+"@"+sp_pait_ke_keray.getSelectedItem().toString();

                                    }
                                } catch (Exception e) {
                                    Log.d("000987", "TRY Array Err: " + e.getMessage());
                                }
                                Log.d("000987", "Array: " + x[5]);
                            }
                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        if (et_tablet_quantity_6.getText().toString().isEmpty()) {
                            x[5] = mData[0][0] + "@" + "sp_pait_ke_keray" + "@" + "0"+"@"+medicine_type_6+"@"+sp_pait_ke_keray.getSelectedItem().toString();
                        } else {
                            x[5] = mData[0][0] + "@" + "sp_pait_ke_keray" + "@" + et_tablet_quantity_6.getText().toString()+"@"+medicine_type_6+"@"+sp_pait_ke_keray.getSelectedItem().toString();
                        }

                        Log.d("000987", "Array: " + x[5]);

                        //Log.d("000357", "UID: " + mData[0][0]);
                        // arrayList_spinner.add(4,mData[0][0]);
                        //   Log.d("000987", "Array: " + arrayList_spinner);
                    } catch (Exception e) {
                        Log.d("000357", "Err: " + e.getMessage());
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /////////////////sp_mumkina_malaria
 /*       sp_mumkina_malaria.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_illness_medicine,
                        R.layout.spinner_illness_medicine_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));*/

        Utils.setSpinnerMedicines(ctx, sp_mumkina_malaria);

        sp_mumkina_malaria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (sp_mumkina_malaria.getSelectedItemPosition() > 0) {

                    if (sp_mumkina_malaria.getSelectedItem().toString().contains("Tab")) {
                        medicine_type_7 = "1";
                        et_tablet_quantity_7.setHint(R.string.tabQuantity);
                    } else if (sp_mumkina_malaria.getSelectedItem().toString().contains("Syp")) {
                        medicine_type_7 = "2";
                        et_tablet_quantity_7.setHint(R.string.sypQuantity);
                    } else if (sp_mumkina_malaria.getSelectedItem().toString().contains("Lotion")) {
                        medicine_type_7 = "3";
                        et_tablet_quantity_7.setHint(R.string.lotionQuantity);
                    } else if (sp_mumkina_malaria.getSelectedItem().toString().equalsIgnoreCase("ORS")) {
                        medicine_type_7 = "4";
                        et_tablet_quantity_7.setHint(R.string.sachetQuantity);
                    } else if (sp_mumkina_malaria.getSelectedItem().toString().startsWith("Eye")) {
                        medicine_type_7 = "5";
                        et_tablet_quantity_7.setHint(R.string.bottleQuantity);
                    } else {
                        medicine_type_7 = "0";
                        et_tablet_quantity_7.setHint(R.string.quantity);
                    }


                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        final String[][] mData = ls.executeReader("SELECT uid from MEDICINE where name ='" + sp_mumkina_malaria.getSelectedItem() + "'");
                        Log.d("000357", "UID: " + mData[0][0]);

                        //  x[6] = mData[0][0] + "@" + "sp_mumkina_malaria" + "@" + et_tablet_quantity_7.getText().toString();

                        et_tablet_quantity_7.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                try {
                                    if (et_tablet_quantity_7.getText().toString().isEmpty()) {
                                        x[6] = mData[0][0] + "@" + "sp_mumkina_malaria"+ "@" +  "0"+"@"+medicine_type_7+"@"+sp_mumkina_malaria.getSelectedItem().toString();
                                    } else {
                                        x[6] = mData[0][0] + "@" + "sp_mumkina_malaria" + "@" + et_tablet_quantity_7.getText().toString()+"@"+medicine_type_7+"@"+sp_mumkina_malaria.getSelectedItem().toString();
                                    }
                                } catch (Exception e) {
                                    Log.d("000987", "TRY Array Err: " + e.getMessage());
                                }
                                Log.d("000987", "Array: " + x[6]);
                            }
                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        if (et_tablet_quantity_7.getText().toString().isEmpty()) {
                            x[6] = mData[0][0] + "@" + "sp_mumkina_malaria" + "@" + "0"+"@"+medicine_type_7+"@"+sp_mumkina_malaria.getSelectedItem().toString();
                        } else {
                            x[6] = mData[0][0] + "@" + "sp_mumkina_malaria" + "@" + et_tablet_quantity_7.getText().toString()+"@"+medicine_type_7+"@"+sp_mumkina_malaria.getSelectedItem().toString();
                        }
                        Log.d("000987", "Array: " + x[6]);

                        // arrayList_spinner.add(6,mData[0][0]);
                        //Log.d("000987","Array: " +arrayList_spinner);
                    } catch (Exception e) {
                        Log.d("000357", "Err: " + e.getMessage());
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /////////////////sp_elaaj
   /*     sp_elaaj.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_illness_medicine,
                        R.layout.spinner_illness_medicine_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
*/

        Utils.setSpinnerMedicines(ctx, sp_elaaj);
        sp_elaaj.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (sp_elaaj.getSelectedItemPosition() > 0) {

                    if (sp_elaaj.getSelectedItem().toString().contains("Tab")) {
                        medicine_type_8 = "1";
                        et_tablet_quantity_8.setHint(R.string.tabQuantity);
                    } else if (sp_elaaj.getSelectedItem().toString().contains("Syp")) {
                        medicine_type_8 = "2";
                        et_tablet_quantity_8.setHint(R.string.sypQuantity);
                    } else if (sp_elaaj.getSelectedItem().toString().contains("Lotion")) {
                        medicine_type_8 = "3";
                        et_tablet_quantity_8.setHint(R.string.lotionQuantity);
                    } else if (sp_elaaj.getSelectedItem().toString().equalsIgnoreCase("ORS")) {
                        medicine_type_8 = "4";
                        et_tablet_quantity_8.setHint(R.string.sachetQuantity);
                    } else if (sp_elaaj.getSelectedItem().toString().startsWith("Eye")) {
                        et_tablet_quantity_8.setHint(R.string.bottleQuantity);
                        medicine_type_8 = "5";
                    } else {
                        medicine_type_8 = "0";
                        et_tablet_quantity_8.setHint(R.string.quantity);
                    }

                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        final String[][] mData = ls.executeReader("SELECT uid from MEDICINE where name ='" + sp_elaaj.getSelectedItem() + "'");
                        Log.d("000357", "UID: " + mData[0][0]);

                        //  x[10] = mData[0][0] + "@" + "sp_elaaj" + "@" + et_tablet_quantity_8.getText().toString();

                        et_tablet_quantity_8.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                try {
                                    if (et_tablet_quantity_8.getText().toString().isEmpty()) {
                                        x[7] = mData[0][0] + "@" + "sp_elaaj" + "@" +  "0"+"@"+medicine_type_8+"@"+sp_elaaj.getSelectedItem().toString();
                                    } else {
                                        x[7] = mData[0][0] + "@" + "sp_elaaj" + "@" + et_tablet_quantity_8.getText().toString()+"@"+medicine_type_8+"@"+sp_elaaj.getSelectedItem().toString();
                                    }
                                } catch (Exception e) {
                                    Log.d("000987", "TRY Array Err: " + e.getMessage());
                                }
                                Log.d("000987", "Array: " + x[7]);
                            }
                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        if (et_tablet_quantity_8.getText().toString().isEmpty()) {
                            x[7] = mData[0][0] + "@" + "sp_elaaj" + "@" + "0"+"@"+medicine_type_8+"@"+sp_elaaj.getSelectedItem().toString();
                        } else {
                            x[7] = mData[0][0] + "@" + "sp_elaaj" + "@" + et_tablet_quantity_8.getText().toString()+"@"+medicine_type_8+"@"+sp_elaaj.getSelectedItem().toString();
                        }

                        Log.d("000987", "Array: " + x[7]);

                        // arrayList_spinner.add(10,mData[0][0]);
                        //Log.d("000987","Array: " +arrayList_spinner);
                    } catch (Exception e) {
                        Log.d("000357", "Err: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }



    public  void read_data(){

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String mData[][] = ls.executeReader("Select data from CBEMARI where member_uid = '" + child_uid + "' AND record_data = '" + record_date + "' AND added_on ='" + added_on + "'");


            Log.d("000987", "1: " + mData[0][0]);


            String json = mData[0][0];
            jsonObject = new JSONObject(json);

            et_tareekh_indraj.setEnabled(false);
            et_ishaal.setEnabled(false);
            et_khansi_aur_saans_ki_bemaari.setEnabled(false);
            et_bukhar.setEnabled(false);
            et_khoon_ki_kami.setEnabled(false);
            et_ankhon_ki_bemaari.setEnabled(false);
            et_pait_kai_keeray.setEnabled(false);
            et_mumkina_malaria.setEnabled(false);
            et_elaaj.setEnabled(false);
            et_tablet_quantity_1.setEnabled(false);
            et_tablet_quantity_2.setEnabled(false);
            et_tablet_quantity_3.setEnabled(false);
            et_tablet_quantity_4.setEnabled(false);
            et_tablet_quantity_5.setEnabled(false);
            et_tablet_quantity_6.setEnabled(false);
            et_tablet_quantity_7.setEnabled(false);
            et_tablet_quantity_8.setEnabled(false);

            //   et_refferal_ki_waja.setEnabled(false);
            //   et_refferal_hospital.setEnabled(false);


            checkbox_ishaal.setEnabled(false);
            checkbox_khansi_aur_saans.setEnabled(false);
            checkbox_bukhar.setEnabled(false);
            checkbox_khoon_ki_kami.setEnabled(false);
            checkbox_ankhon_ki_bemaari.setEnabled(false);
            checkbox_pait_ke_keray.setEnabled(false);
            checkbox_mumkina_malaria.setEnabled(false);
            checkbox_elaaj.setEnabled(false);

            sp_ishal_medicine.setEnabled(false);
            sp_khansi_aur_saans_ki_bemaari.setEnabled(false);
            sp_bukhar.setEnabled(false);
            sp_khoon_ki_kami.setEnabled(false);
            sp_ankhon_ki_bemaari.setEnabled(false);
            sp_pait_ke_keray.setEnabled(false);
            sp_mumkina_malaria.setEnabled(false);
            sp_elaaj.setEnabled(false);

            btn_jamaa_kre.setVisibility(View.GONE);

            et_elaaj.setText((jsonObject.getString("ellaj")));
            et_pait_kai_keeray.setText(jsonObject.getString("paitkekiray"));
            et_ankhon_ki_bemaari.setText(jsonObject.getString("ankhun_ki_bemari"));
            et_mumkina_malaria.setText(jsonObject.getString("malaria"));
            et_ankhon_ki_bemaari.setText(jsonObject.getString("ankhun_ki_bemari"));
            et_khoon_ki_kami.setText(jsonObject.getString("khoon_ki_kami"));
            et_bukhar.setText(jsonObject.getString("bukhar"));
            et_khansi_aur_saans_ki_bemaari.setText(jsonObject.getString("khansi_sans"));
            et_ishaal.setText(jsonObject.getString("ishaal"));
            // et_refferal_ki_waja.setText(jsonObject.getString("referal_reason"));
            //  et_refferal_hospital.setText(jsonObject.getString("referal_facility"));
            et_tareekh_indraj.setText(jsonObject.getString("record_enter_date"));

            try {
                if (jsonObject.getString("cb_ishaal").equalsIgnoreCase("1")) {
                    ishaal = jsonObject.getString("cb_ishaal");
                    checkbox_ishaal.setChecked(true);
                    ll_ishal.setVisibility(View.VISIBLE);
                } else {
                    ishaal = jsonObject.getString("cb_ishaal");
                    checkbox_ishaal.setChecked(false);
                    ll_ishal.setVisibility(View.GONE);
                }

                if (jsonObject.getString("cb_khansi_sans").equalsIgnoreCase("1")) {
                    khansi_aur_saans = jsonObject.getString("cb_khansi_sans");
                    checkbox_khansi_aur_saans.setChecked(true);
                    ll_khansi_aur_saans_ki_bemaari.setVisibility(View.VISIBLE);
                } else {
                    khansi_aur_saans = jsonObject.getString("cb_khansi_sans");
                    checkbox_khansi_aur_saans.setChecked(false);
                    ll_khansi_aur_saans_ki_bemaari.setVisibility(View.GONE);
                }

                if (jsonObject.getString("cb_bukhar").equalsIgnoreCase("1")) {
                    bukhar = jsonObject.getString("cb_bukhar");
                    checkbox_bukhar.setChecked(true);
                    ll_bukhar.setVisibility(View.VISIBLE);
                } else {
                    bukhar = jsonObject.getString("cb_bukhar");
                    checkbox_bukhar.setChecked(false);
                    ll_bukhar.setVisibility(View.GONE);
                }

                if (jsonObject.getString("cb_khoon_ki_kami").equalsIgnoreCase("1")) {
                    khoon_ki_kami = jsonObject.getString("cb_khoon_ki_kami");
                    checkbox_khoon_ki_kami.setChecked(true);
                    ll_khoon_ki_kami.setVisibility(View.VISIBLE);
                } else {
                    khoon_ki_kami = jsonObject.getString("cb_khoon_ki_kami");
                    checkbox_khoon_ki_kami.setChecked(false);
                    ll_khoon_ki_kami.setVisibility(View.GONE);
                }

                if (jsonObject.getString("cb_ankhon_ki_bemaari").equalsIgnoreCase("1")) {
                    ankhon_ki_bemaari = jsonObject.getString("cb_ankhon_ki_bemaari");
                    checkbox_ankhon_ki_bemaari.setChecked(true);
                    ll_ankhon_ki_bemaari.setVisibility(View.VISIBLE);
                } else {
                    ankhon_ki_bemaari = jsonObject.getString("cb_ankhon_ki_bemaari");
                    checkbox_ankhon_ki_bemaari.setChecked(false);
                    ll_ankhon_ki_bemaari.setVisibility(View.GONE);
                }

                if (jsonObject.getString("cb_pait_ke_keray").equalsIgnoreCase("1")) {
                    pait_ke_keray = jsonObject.getString("cb_pait_ke_keray");
                    checkbox_pait_ke_keray.setChecked(true);
                    ll_pait_ke_keray.setVisibility(View.VISIBLE);
                } else {
                    pait_ke_keray = jsonObject.getString("cb_pait_ke_keray");
                    checkbox_pait_ke_keray.setChecked(false);
                    ll_pait_ke_keray.setVisibility(View.GONE);
                }

                if (jsonObject.getString("cb_malaria").equalsIgnoreCase("1")) {
                    mumkina_malaria = jsonObject.getString("cb_malaria");
                    checkbox_mumkina_malaria.setChecked(true);
                    ll_mumkina_malaria.setVisibility(View.VISIBLE);
                } else {
                    mumkina_malaria = jsonObject.getString("cb_malaria");
                    checkbox_mumkina_malaria.setChecked(false);
                    ll_mumkina_malaria.setVisibility(View.GONE);
                }

                if (jsonObject.getString("cb_elaaj").equalsIgnoreCase("1")) {
                    elaaj = jsonObject.getString("cb_elaaj");
                    checkbox_elaaj.setChecked(true);
                    ll_elaaj.setVisibility(View.VISIBLE);
                } else {
                    elaaj = jsonObject.getString("cb_elaaj");
                    checkbox_elaaj.setChecked(false);
                    ll_elaaj.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                Log.d("000987", "cb Error: " + e.getMessage());
            }


            try {
                sp_ishal_medicine.setSelection(Integer.parseInt(jsonObject.getString("sp_ishaal_pos")) + 1);
                sp_khansi_aur_saans_ki_bemaari.setSelection(Integer.parseInt(jsonObject.getString("sp_khansi_sans_pos")) + 1);
                sp_bukhar.setSelection(Integer.parseInt(jsonObject.getString("sp_bukhar_pos")) + 1);
                sp_khoon_ki_kami.setSelection(Integer.parseInt(jsonObject.getString("sp_khoon_ki_kami_pos")) + 1);
                sp_ankhon_ki_bemaari.setSelection(Integer.parseInt(jsonObject.getString("sp_ankhon_ki_bemaari_pos")) + 1);
                sp_pait_ke_keray.setSelection(Integer.parseInt(jsonObject.getString("sp_pait_ke_keray_pos")) + 1);
                sp_mumkina_malaria.setSelection(Integer.parseInt(jsonObject.getString("sp_malaria_pos")) + 1);
                sp_elaaj.setSelection(Integer.parseInt(jsonObject.getString("sp_elaaj_pos")) + 1);

            } catch (Exception e1) {
                Log.d("000987", "sp Error: " + e1.getMessage());
            }

            try {
                if (jsonObject.has("ishaal_medicine_quantity")) {

                    et_tablet_quantity_1.setText(jsonObject.getString("ishaal_medicine_quantity"));
                    medicine_type_1 = jsonObject.getString("ishaal_medicine_quantity");


                    et_tablet_quantity_2.setText(jsonObject.getString("khansi_medicine_quantity"));
                    medicine_type_2 = jsonObject.getString("khansi_medicine_quantity");


                    et_tablet_quantity_3.setText(jsonObject.getString("bukhar_medicine_quantity"));
                    medicine_type_3 = jsonObject.getString("bukhar_medicine_quantity");


                    et_tablet_quantity_4.setText(jsonObject.getString("khoon_ki_kami_medicine_quantity"));
                    medicine_type_4 = jsonObject.getString("khoon_ki_kami_medicine_quantity");


                    et_tablet_quantity_5.setText(jsonObject.getString("ankhon_ki_bemaari_medicine_quantity"));
                    medicine_type_5 = jsonObject.getString("ankhon_ki_bemaari_medicine_quantity");


                    et_tablet_quantity_6.setText(jsonObject.getString("paitkekiray_medicine_quantity"));
                    medicine_type_6 = jsonObject.getString("paitkekiray_medicine_quantity");


                    et_tablet_quantity_7.setText(jsonObject.getString("malaria_medicine_quantity"));
                    medicine_type_7 = jsonObject.getString("malaria_medicine_quantity");


                    et_tablet_quantity_8.setText(jsonObject.getString("elaaj_medicine_quantity"));
                    medicine_type_8 = jsonObject.getString("elaaj_medicine_quantity");

                } else {
                    et_tablet_quantity_1.setText("-");
                    et_tablet_quantity_2.setText("-");
                    et_tablet_quantity_3.setText("-");
                    et_tablet_quantity_4.setText("-");
                    et_tablet_quantity_5.setText("-");
                    et_tablet_quantity_5.setText("-");
                    et_tablet_quantity_6.setText("-");
                    et_tablet_quantity_7.setText("-");
                    et_tablet_quantity_8.setText("-");
                }
            } catch (Exception e1) {
                Log.d("000987", "sp Error: " + e1.getMessage());
            }


            ls.closeDB();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("000987", " Error" + e.getMessage());
            Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
        }
    }
    /*@Override
    protected void onResume() {
        super.onResume();

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String mData[][] = ls.executeReader("Select data from CBEMARI where member_uid = '" + child_uid + "' AND record_data = '" + record_date + "' AND added_on ='" + added_on + "'");


            Log.d("000987", "1: " + mData[0][0]);


            String json = mData[0][0];
            jsonObject = new JSONObject(json);

            et_tareekh_indraj.setEnabled(false);
            et_ishaal.setEnabled(false);
            et_khansi_aur_saans_ki_bemaari.setEnabled(false);
            et_bukhar.setEnabled(false);
            et_khoon_ki_kami.setEnabled(false);
            et_ankhon_ki_bemaari.setEnabled(false);
            et_pait_kai_keeray.setEnabled(false);
            et_mumkina_malaria.setEnabled(false);
            et_elaaj.setEnabled(false);
            et_tablet_quantity_1.setEnabled(false);
            et_tablet_quantity_2.setEnabled(false);
            et_tablet_quantity_3.setEnabled(false);
            et_tablet_quantity_4.setEnabled(false);
            et_tablet_quantity_5.setEnabled(false);
            et_tablet_quantity_6.setEnabled(false);
            et_tablet_quantity_7.setEnabled(false);
            et_tablet_quantity_8.setEnabled(false);

            //   et_refferal_ki_waja.setEnabled(false);
            //   et_refferal_hospital.setEnabled(false);


            checkbox_ishaal.setEnabled(false);
            checkbox_khansi_aur_saans.setEnabled(false);
            checkbox_bukhar.setEnabled(false);
            checkbox_khoon_ki_kami.setEnabled(false);
            checkbox_ankhon_ki_bemaari.setEnabled(false);
            checkbox_pait_ke_keray.setEnabled(false);
            checkbox_mumkina_malaria.setEnabled(false);
            checkbox_elaaj.setEnabled(false);

            sp_ishal_medicine.setEnabled(false);
            sp_khansi_aur_saans_ki_bemaari.setEnabled(false);
            sp_bukhar.setEnabled(false);
            sp_khoon_ki_kami.setEnabled(false);
            sp_ankhon_ki_bemaari.setEnabled(false);
            sp_pait_ke_keray.setEnabled(false);
            sp_mumkina_malaria.setEnabled(false);
            sp_elaaj.setEnabled(false);

            btn_jamaa_kre.setVisibility(View.GONE);

            et_elaaj.setText((jsonObject.getString("ellaj")));
            et_pait_kai_keeray.setText(jsonObject.getString("paitkekiray"));
            et_ankhon_ki_bemaari.setText(jsonObject.getString("ankhun_ki_bemari"));
            et_mumkina_malaria.setText(jsonObject.getString("malaria"));
            et_ankhon_ki_bemaari.setText(jsonObject.getString("ankhun_ki_bemari"));
            et_khoon_ki_kami.setText(jsonObject.getString("khoon_ki_kami"));
            et_bukhar.setText(jsonObject.getString("bukhar"));
            et_khansi_aur_saans_ki_bemaari.setText(jsonObject.getString("khansi_sans"));
            et_ishaal.setText(jsonObject.getString("ishaal"));
            // et_refferal_ki_waja.setText(jsonObject.getString("referal_reason"));
            //  et_refferal_hospital.setText(jsonObject.getString("referal_facility"));
            et_tareekh_indraj.setText(jsonObject.getString("record_enter_date"));

            try {
                if (jsonObject.getString("cb_ishaal").equalsIgnoreCase("1")) {
                    ishaal = jsonObject.getString("cb_ishaal");
                    checkbox_ishaal.setChecked(true);
                    ll_ishal.setVisibility(View.VISIBLE);
                } else {
                    ishaal = jsonObject.getString("cb_ishaal");
                    checkbox_ishaal.setChecked(false);
                    ll_ishal.setVisibility(View.GONE);
                }

                if (jsonObject.getString("cb_khansi_sans").equalsIgnoreCase("1")) {
                    khansi_aur_saans = jsonObject.getString("cb_khansi_sans");
                    checkbox_khansi_aur_saans.setChecked(true);
                    ll_khansi_aur_saans_ki_bemaari.setVisibility(View.VISIBLE);
                } else {
                    khansi_aur_saans = jsonObject.getString("cb_khansi_sans");
                    checkbox_khansi_aur_saans.setChecked(false);
                    ll_khansi_aur_saans_ki_bemaari.setVisibility(View.GONE);
                }

                if (jsonObject.getString("cb_bukhar").equalsIgnoreCase("1")) {
                    bukhar = jsonObject.getString("cb_bukhar");
                    checkbox_bukhar.setChecked(true);
                    ll_bukhar.setVisibility(View.VISIBLE);
                } else {
                    bukhar = jsonObject.getString("cb_bukhar");
                    checkbox_bukhar.setChecked(false);
                    ll_bukhar.setVisibility(View.GONE);
                }

                if (jsonObject.getString("cb_khoon_ki_kami").equalsIgnoreCase("1")) {
                    khoon_ki_kami = jsonObject.getString("cb_khoon_ki_kami");
                    checkbox_khoon_ki_kami.setChecked(true);
                    ll_khoon_ki_kami.setVisibility(View.VISIBLE);
                } else {
                    khoon_ki_kami = jsonObject.getString("cb_khoon_ki_kami");
                    checkbox_khoon_ki_kami.setChecked(false);
                    ll_khoon_ki_kami.setVisibility(View.GONE);
                }

                if (jsonObject.getString("cb_ankhon_ki_bemaari").equalsIgnoreCase("1")) {
                    ankhon_ki_bemaari = jsonObject.getString("cb_ankhon_ki_bemaari");
                    checkbox_ankhon_ki_bemaari.setChecked(true);
                    ll_ankhon_ki_bemaari.setVisibility(View.VISIBLE);
                } else {
                    ankhon_ki_bemaari = jsonObject.getString("cb_ankhon_ki_bemaari");
                    checkbox_ankhon_ki_bemaari.setChecked(false);
                    ll_ankhon_ki_bemaari.setVisibility(View.GONE);
                }

                if (jsonObject.getString("cb_pait_ke_keray").equalsIgnoreCase("1")) {
                    pait_ke_keray = jsonObject.getString("cb_pait_ke_keray");
                    checkbox_pait_ke_keray.setChecked(true);
                    ll_pait_ke_keray.setVisibility(View.VISIBLE);
                } else {
                    pait_ke_keray = jsonObject.getString("cb_pait_ke_keray");
                    checkbox_pait_ke_keray.setChecked(false);
                    ll_pait_ke_keray.setVisibility(View.GONE);
                }

                if (jsonObject.getString("cb_malaria").equalsIgnoreCase("1")) {
                    mumkina_malaria = jsonObject.getString("cb_malaria");
                    checkbox_mumkina_malaria.setChecked(true);
                    ll_mumkina_malaria.setVisibility(View.VISIBLE);
                } else {
                    mumkina_malaria = jsonObject.getString("cb_malaria");
                    checkbox_mumkina_malaria.setChecked(false);
                    ll_mumkina_malaria.setVisibility(View.GONE);
                }

                if (jsonObject.getString("cb_elaaj").equalsIgnoreCase("1")) {
                    elaaj = jsonObject.getString("cb_elaaj");
                    checkbox_elaaj.setChecked(true);
                    ll_elaaj.setVisibility(View.VISIBLE);
                } else {
                    elaaj = jsonObject.getString("cb_elaaj");
                    checkbox_elaaj.setChecked(false);
                    ll_elaaj.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                Log.d("000987", "cb Error: " + e.getMessage());
            }


            try {
                sp_ishal_medicine.setSelection(Integer.parseInt(jsonObject.getString("sp_ishaal_pos")) + 1);
                sp_khansi_aur_saans_ki_bemaari.setSelection(Integer.parseInt(jsonObject.getString("sp_khansi_sans_pos")) + 1);
                sp_bukhar.setSelection(Integer.parseInt(jsonObject.getString("sp_bukhar_pos")) + 1);
                sp_khoon_ki_kami.setSelection(Integer.parseInt(jsonObject.getString("sp_khoon_ki_kami_pos")) + 1);
                sp_ankhon_ki_bemaari.setSelection(Integer.parseInt(jsonObject.getString("sp_ankhon_ki_bemaari_pos")) + 1);
                sp_pait_ke_keray.setSelection(Integer.parseInt(jsonObject.getString("sp_pait_ke_keray_pos")) + 1);
                sp_mumkina_malaria.setSelection(Integer.parseInt(jsonObject.getString("sp_malaria_pos")) + 1);
                sp_elaaj.setSelection(Integer.parseInt(jsonObject.getString("sp_elaaj_pos")) + 1);

            } catch (Exception e1) {
                Log.d("000987", "sp Error: " + e1.getMessage());
            }

            try {
                if (jsonObject.has("ishaal_medicine_quantity")) {

                    et_tablet_quantity_1.setText(jsonObject.getString("ishaal_medicine_quantity"));
                    medicine_type_1 = jsonObject.getString("ishaal_medicine_quantity");


                    et_tablet_quantity_2.setText(jsonObject.getString("khansi_medicine_quantity"));
                    medicine_type_2 = jsonObject.getString("khansi_medicine_quantity");


                    et_tablet_quantity_3.setText(jsonObject.getString("bukhar_medicine_quantity"));
                    medicine_type_3 = jsonObject.getString("bukhar_medicine_quantity");


                    et_tablet_quantity_4.setText(jsonObject.getString("khoon_ki_kami_medicine_quantity"));
                    medicine_type_4 = jsonObject.getString("khoon_ki_kami_medicine_quantity");


                    et_tablet_quantity_5.setText(jsonObject.getString("ankhon_ki_bemaari_medicine_quantity"));
                    medicine_type_5 = jsonObject.getString("ankhon_ki_bemaari_medicine_quantity");


                    et_tablet_quantity_6.setText(jsonObject.getString("paitkekiray_medicine_quantity"));
                    medicine_type_6 = jsonObject.getString("paitkekiray_medicine_quantity");


                    et_tablet_quantity_7.setText(jsonObject.getString("malaria_medicine_quantity"));
                    medicine_type_7 = jsonObject.getString("malaria_medicine_quantity");


                    et_tablet_quantity_8.setText(jsonObject.getString("elaaj_medicine_quantity"));
                    medicine_type_8 = jsonObject.getString("elaaj_medicine_quantity");

                } else {
                    et_tablet_quantity_1.setText("-");
                    et_tablet_quantity_2.setText("-");
                    et_tablet_quantity_3.setText("-");
                    et_tablet_quantity_4.setText("-");
                    et_tablet_quantity_5.setText("-");
                    et_tablet_quantity_5.setText("-");
                    et_tablet_quantity_6.setText("-");
                    et_tablet_quantity_7.setText("-");
                    et_tablet_quantity_8.setText("-");
                }
            } catch (Exception e1) {
                Log.d("000987", "sp Error: " + e1.getMessage());
            }


            ls.closeDB();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("000987", " Error" + e.getMessage());
            Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
        }

    }*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
