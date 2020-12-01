package com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildBemaariRecordActivities;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

public class Child_BemaariRecordForm_Activity extends AppCompatActivity {

    Context ctx = Child_BemaariRecordForm_Activity.this;
    //et_chout_zakham_jalna et_kharish et_jins_zanana_amraaz  et_mumkina_tea_be_ki_tashkhees_kai_lye_reffer, et_tashkhees_shuda_amraaz_tea_be,
    //            et_muawnat_doraan_elaaj_tea_be,
    EditText et_tareekh_indraj, et_ishaal, et_khansi_aur_saans_ki_bemaari, et_bukhar, et_khoon_ki_kami, et_ankhon_ki_bemaari, et_pait_kai_keeray, et_mumkina_malaria,
            et_elaaj, et_refferal_ki_waja, et_refferal_hospital;

    Spinner sp_jins;
    Button btn_jamaa_kre;
    RelativeLayout rl_navigation_drawer, rl_home;
    ImageView iv_navigation_drawer, iv_home;

    /*CustomerAdapter adapter = null;
    ArrayList<Customer> customers = null;*/
    double latitude;
    double longitude;
    // GPSTracker class
    GPSTracker gps;
    String child_uid, TodayDate;

    private int mYear, mMonth, mDay;
    int date_for_condition = 0;
    int month_for_condition = 0;

    public String hold_age_date_condition = "fromage";
    String monthf2, dayf2, yearf2 = "null", cur_added_on;
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid;

    CheckBox checkbox_ishaal, checkbox_khansi_aur_saans,checkbox_bukhar,checkbox_khoon_ki_kami,checkbox_ankhon_ki_bemaari,checkbox_mumkina_malaria,
            checkbox_pait_ke_keray,checkbox_elaaj;
    LinearLayout ll_ishal,ll_khansi_aur_saans_ki_bemaari,ll_bukhar,ll_khoon_ki_kami,ll_ankhon_ki_bemaari,ll_mumkina_malaria, ll_pait_ke_keray,ll_elaaj;

    Spinner sp_ishal_medicine,sp_khansi_aur_saans_ki_bemaari,sp_bukhar,sp_khoon_ki_kami,sp_ankhon_ki_bemaari,sp_mumkina_malaria, sp_pait_ke_keray,sp_elaaj;


    String ishaal ="0",khansi_aur_saans="0",bukhar="0",khoon_ki_kami="0",ankhon_ki_bemaari="0",mumkina_malaria="0",pait_ke_keray="0",elaaj="0";

    String[] x = new String[8];

    String medicine_type_1 = "-1",medicine_type_2 = "-1",medicine_type_3 = "-1",medicine_type_4 = "-1",medicine_type_5 = "-1",medicine_type_6 = "-1",medicine_type_7 = "-1",
            medicine_type_8 = "-1";

    EditText et_tablet_quantity_1,et_tablet_quantity_2,et_tablet_quantity_3,et_tablet_quantity_4,et_tablet_quantity_5,et_tablet_quantity_6,et_tablet_quantity_7,et_tablet_quantity_8;

    long mLastClickTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_bemaari_record_form);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Child_BemaariRecordForm_Activity.class));

        child_uid = getIntent().getExtras().getString("u_id");

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

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

        //  check_gps();

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);

        iv_navigation_drawer.setVisibility(View.GONE);
        //  iv_home.setVisibility(View.GONE);

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


        checkbox_ishaal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_ishaal.isChecked()) {
                    ishaal="1";
                    ll_ishal.setVisibility(View.VISIBLE);
                    Log.d("000987","Ishaal IF: " +ishaal);
                } else {
                    ll_ishal.setVisibility(View.GONE);
                    ishaal="0";
                    sp_ishal_medicine.setSelection(0);
                    et_tablet_quantity_1.getText().clear();
                    Log.d("000987","Ishaal ELSE: " +ishaal);
                }
            }
        });

        checkbox_khansi_aur_saans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_khansi_aur_saans.isChecked()) {
                    khansi_aur_saans="1";
                    ll_khansi_aur_saans_ki_bemaari.setVisibility(View.VISIBLE);
                    Log.d("000987","khansi_aur_saans IF: " +khansi_aur_saans);
                } else {
                    ll_khansi_aur_saans_ki_bemaari.setVisibility(View.GONE);
                    khansi_aur_saans="0";
                    sp_khansi_aur_saans_ki_bemaari.setSelection(0);
                    et_tablet_quantity_2.getText().clear();
                    Log.d("000987","khansi_aur_saans ELSE: " +khansi_aur_saans);
                }
            }
        });

        checkbox_bukhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_bukhar.isChecked()) {
                    bukhar="1";
                    ll_bukhar.setVisibility(View.VISIBLE);
                    Log.d("000987","bukhar: " +bukhar);
                } else {
                    ll_bukhar.setVisibility(View.GONE);
                    bukhar="0";
                    sp_bukhar.setSelection(0);
                    et_tablet_quantity_3.getText().clear();
                    Log.d("000987","bukhar ELSE: " +bukhar);
                }
            }
        });

        checkbox_khoon_ki_kami.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_khoon_ki_kami.isChecked()) {
                    khoon_ki_kami="1";
                    ll_khoon_ki_kami.setVisibility(View.VISIBLE);
                    Log.d("000987","khoon_ki_kami.: " +khoon_ki_kami);
                } else {
                    ll_khoon_ki_kami.setVisibility(View.GONE);
                    khoon_ki_kami="0";
                    sp_khoon_ki_kami.setSelection(0);
                    et_tablet_quantity_4.getText().clear();
                    Log.d("000987","khoon_ki_kami ELSE: " +khoon_ki_kami);
                }
            }
        });


        checkbox_ankhon_ki_bemaari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_ankhon_ki_bemaari.isChecked()) {
                    ankhon_ki_bemaari="1";
                    ll_ankhon_ki_bemaari.setVisibility(View.VISIBLE);
                    Log.d("000987","ankhon_ki_bemaari.: " +ankhon_ki_bemaari);
                } else {
                    ll_ankhon_ki_bemaari.setVisibility(View.GONE);
                    ankhon_ki_bemaari="0";
                    sp_ankhon_ki_bemaari.setSelection(0);
                    et_tablet_quantity_5.getText().clear();
                    Log.d("000987","ankhon_ki_bemaari ELSE: " +ankhon_ki_bemaari);
                }
            }
        });


        checkbox_pait_ke_keray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_pait_ke_keray.isChecked()) {
                    pait_ke_keray="1";
                    ll_pait_ke_keray.setVisibility(View.VISIBLE);
                    Log.d("000987","pait kai keray.: " +pait_ke_keray);
                } else {
                    ll_pait_ke_keray.setVisibility(View.GONE);
                    pait_ke_keray="0";
                    sp_pait_ke_keray.setSelection(0);
                    et_tablet_quantity_6.getText().clear();
                    Log.d("000987","pait_ke_keray ELSE: " +pait_ke_keray);
                }
            }
        });

        checkbox_mumkina_malaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_mumkina_malaria.isChecked()) {
                    mumkina_malaria="1";
                    ll_mumkina_malaria.setVisibility(View.VISIBLE);
                    Log.d("000987","mumkina_malaria.: " +mumkina_malaria);
                } else {
                    ll_mumkina_malaria.setVisibility(View.GONE);
                    mumkina_malaria="0";
                    sp_mumkina_malaria.setSelection(0);
                    et_tablet_quantity_7.getText().clear();
                    Log.d("000987","mumkina_malaria ELSE: " +mumkina_malaria);
                }
            }
        });


        checkbox_elaaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_elaaj.isChecked()) {
                    elaaj="1";
                    ll_elaaj.setVisibility(View.VISIBLE);
                    Log.d("000987","elaaj: " +elaaj);
                } else {
                    ll_elaaj.setVisibility(View.GONE);
                    elaaj="0";
                    sp_elaaj.setSelection(0);
                    et_tablet_quantity_8.getText().clear();
                    Log.d("000987","elaaj ELSE: " +elaaj);
                }
            }
        });


        spineer_data();




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

                if (!checkbox_ishaal.isChecked() && !checkbox_khansi_aur_saans.isChecked() && !checkbox_bukhar.isChecked() && !checkbox_khoon_ki_kami.isChecked()
                        && !checkbox_ankhon_ki_bemaari.isChecked() && !checkbox_pait_ke_keray.isChecked() && !checkbox_mumkina_malaria.isChecked() && !checkbox_elaaj.isChecked())
                {
                    final Snackbar snackbar = Snackbar.make(v, R.string.selectDiseaseCheckboxPrompt, Snackbar.LENGTH_SHORT);
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

                        String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from CBEMARI");

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
                        Log.d("000258", "Read CBEMARI Error: " + e.getMessage());
                    }
                }

                btn_jamaa_kre.setVisibility(View.GONE);
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Log.d("000555", " mLastClickTime: " + mLastClickTime);



                try {


                    Lister ls = new Lister(Child_BemaariRecordForm_Activity.this);
                   ls.createAndOpenDB();
                    
                    JSONObject jobj = new JSONObject();

                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));

                    jobj.put("record_enter_date", "" + et_tareekh_indraj.getText().toString());

                    jobj.put("cb_ishaal", "" + ishaal);
                    jobj.put("ishaal", "" + et_ishaal.getText().toString());
                    if (String.valueOf(sp_ishal_medicine.getSelectedItem()).equalsIgnoreCase("null")) {
                        jobj.put("sp_ishaal", "" + "-1");
                        jobj.put("ishaal_medicine_quantity", "");
                    } else {
                        jobj.put("sp_ishaal", "" + String.valueOf(sp_ishal_medicine.getSelectedItem()));
                        jobj.put("ishaal_medicine_quantity", "" + et_tablet_quantity_1.getText().toString());
                    }
                    jobj.put("sp_ishaal_pos", "" + String.valueOf(sp_ishal_medicine.getSelectedItemPosition() - 1));


                    jobj.put("cb_khansi_sans", "" + khansi_aur_saans);
                    jobj.put("khansi_sans", "" + et_khansi_aur_saans_ki_bemaari.getText().toString());
                    if (String.valueOf(sp_khansi_aur_saans_ki_bemaari.getSelectedItem()).equalsIgnoreCase("null")) {
                        jobj.put("sp_khansi_sans", "" + "-1");
                        jobj.put("khansi_medicine_quantity", "");
                    } else {
                        jobj.put("sp_khansi_sans", "" + String.valueOf(sp_khansi_aur_saans_ki_bemaari.getSelectedItem()));
                        jobj.put("khansi_medicine_quantity", "" + et_tablet_quantity_2.getText().toString());
                    }
                    jobj.put("sp_khansi_sans_pos", "" + String.valueOf(sp_khansi_aur_saans_ki_bemaari.getSelectedItemPosition() - 1));




                    jobj.put("cb_bukhar", "" + bukhar);
                    jobj.put("bukhar", "" + et_bukhar.getText().toString());
                    if (String.valueOf(sp_bukhar.getSelectedItem()).equalsIgnoreCase("null")) {
                        jobj.put("sp_bukhar", "" + "-1");
                        jobj.put("bukhar_medicine_quantity", "");
                    } else {
                        jobj.put("sp_bukhar", "" + String.valueOf(sp_bukhar.getSelectedItem()));
                        jobj.put("bukhar_medicine_quantity", "" + et_tablet_quantity_3.getText().toString());
                    }
                    jobj.put("sp_bukhar_pos", "" + String.valueOf(sp_bukhar.getSelectedItemPosition() - 1));


                    jobj.put("cb_khoon_ki_kami", "" + khoon_ki_kami);
                    jobj.put("khoon_ki_kami", "" + et_khoon_ki_kami.getText().toString());
                    if (String.valueOf(sp_khoon_ki_kami.getSelectedItem()).equalsIgnoreCase("null")) {
                        jobj.put("sp_khoon_ki_kami", "" + "-1");
                        jobj.put("khoon_ki_kami_medicine_quantity", "");
                    } else {
                        jobj.put("sp_khoon_ki_kami", "" + String.valueOf(sp_khoon_ki_kami.getSelectedItem()));
                        jobj.put("khoon_ki_kami_medicine_quantity", "" + et_tablet_quantity_4.getText().toString());
                    }
                    jobj.put("sp_khoon_ki_kami_pos", "" + String.valueOf(sp_khoon_ki_kami.getSelectedItemPosition() - 1));


                    jobj.put("cb_ankhon_ki_bemaari", "" + ankhon_ki_bemaari);
                    jobj.put("ankhun_ki_bemari", "" + et_ankhon_ki_bemaari.getText().toString());
                    if (String.valueOf(sp_ankhon_ki_bemaari.getSelectedItem()).equalsIgnoreCase("null")) {
                        jobj.put("sp_ankhon_ki_bemaari", "" + "-1");
                        jobj.put("ankhon_ki_bemaari_medicine_quantity", "");
                    } else {
                        jobj.put("sp_ankhon_ki_bemaari", "" + String.valueOf(sp_ankhon_ki_bemaari.getSelectedItem()));
                        jobj.put("ankhon_ki_bemaari_medicine_quantity", "" + et_tablet_quantity_5.getText().toString());
                    }
                    jobj.put("sp_ankhon_ki_bemaari_pos", "" + String.valueOf(sp_ankhon_ki_bemaari.getSelectedItemPosition() - 1));

                    jobj.put("cb_pait_ke_keray", "" + pait_ke_keray);
                    jobj.put("paitkekiray", "" + et_pait_kai_keeray.getText().toString());
                    if (String.valueOf(sp_pait_ke_keray.getSelectedItem()).equalsIgnoreCase("null"))
                    {
                        jobj.put("sp_pait_ke_keray", "" +"-1");
                        jobj.put("paitkekiray_medicine_quantity", "");
                    }
                    else{
                        jobj.put("sp_pait_ke_keray", "" + String.valueOf(sp_pait_ke_keray.getSelectedItem()));
                        jobj.put("paitkekiray_medicine_quantity", "" +et_tablet_quantity_6.getText().toString());
                    }
                    jobj.put("sp_pait_ke_keray_pos", "" + String.valueOf(sp_pait_ke_keray.getSelectedItemPosition() -1));

                    jobj.put("cb_malaria", "" + mumkina_malaria);
                    jobj.put("malaria", "" + et_mumkina_malaria.getText().toString());
                    if (String.valueOf(sp_mumkina_malaria.getSelectedItem()).equalsIgnoreCase("null")) {
                        jobj.put("sp_malaria", "" + "-1");
                        jobj.put("malaria_medicine_quantity", "");
                    } else {
                        jobj.put("sp_malaria", "" + String.valueOf(sp_mumkina_malaria.getSelectedItem()));
                        jobj.put("malaria_medicine_quantity", "" + et_tablet_quantity_7.getText().toString());
                    }
                    jobj.put("sp_malaria_pos", "" + String.valueOf(sp_mumkina_malaria.getSelectedItemPosition() - 1));


                    jobj.put("cb_elaaj", "" + elaaj);
                    jobj.put("ellaj", "" + et_elaaj.getText().toString());
                    if (String.valueOf(sp_elaaj.getSelectedItem()).equalsIgnoreCase("null")) {
                        jobj.put("sp_elaaj", "" + "-1");
                        jobj.put("elaaj_medicine_quantity", "");
                    } else {
                        jobj.put("sp_elaaj", "" + String.valueOf(sp_elaaj.getSelectedItem()));
                        jobj.put("elaaj_medicine_quantity", "" + et_tablet_quantity_8.getText().toString());
                    }
                    jobj.put("sp_elaaj_pos", "" + String.valueOf(sp_elaaj.getSelectedItemPosition() - 1));

                  // jobj.put("referal_reason", "" + et_refferal_ki_waja.getText().toString());
                    //jobj.put("referal_facility", "" + et_refferal_hospital.getText().toString());//spinner
                    jobj.put("current_record_date", "" + TodayDate);
                    jobj.put("added_on", "null");


                    cur_added_on = String.valueOf(System.currentTimeMillis());

                    String ans1 = "insert into CBEMARI (member_uid, record_data, data,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + child_uid + "'," +
                            "'" + et_tareekh_indraj.getText().toString() + "'," +
                            "'" + jobj + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + cur_added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000555", "Data: " + ans1);
                    Log.d("000555", "Query: " + res.toString());


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
                            sendPostRequest(child_uid, et_tareekh_indraj.getText().toString(), String.valueOf(jobj), login_useruid, cur_added_on);
                        } else {
                          //  Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();
                        }

                        insert_into_medicineLog(cur_added_on);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                finish();
                            }
                        },2000);
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

                  /*  if (et_refferal_hospital.getText().toString().length() > 0 && et_refferal_ki_waja.getText().toString().length() > 0) {
                        ReferalFormSubmit();
                    }else{}*/


                } catch (Exception e) {
                    Log.d("000555", "Err: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } /*finally {
                    finish();
                }*/
            }
        });
    }

    private void insert_into_medicineLog(String cur_added_on) {

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            Log.d("000987", "LENGTH: "+ x.length);

            for (int i = 0; i < x.length; i++) {

                if (x[i] != null) {
                    Log.d("000987", "Array: " + i + "@" + x[i].split("@")[0]);
                    Log.d("000987", "Array: " + i + "@" + x[i].split("@")[1]);
                    try {

                        //String cur_added_on = String.valueOf(System.currentTimeMillis());

                        JSONObject jobj_medicine_stock = new JSONObject();
                        jobj_medicine_stock.put("medicine_name", "" + x[i].split("@")[4]);
                        jobj_medicine_stock.put("medicine_quantity", "" + x[i].split("@")[2]);
                        jobj_medicine_stock.put("medicine_type", "" + x[i].split("@")[3]);



                        String ans1 = "insert into MEDICINE_LOG (member_uid, medicine_id, record_data,type,disease,metadata,added_by,added_on)" +
                                "values" +
                                "(" +
                                "'" + child_uid + "'," +
                                "'" + x[i].split("@")[0] + "'," +
                                "'" + TodayDate + "'," +
                                "'CBemari'," +
                                "'" + x[i].split("@")[1] + "'," +
                                "'" + jobj_medicine_stock + "'," +
                                "'" + login_useruid + "'," +
                                "'" + cur_added_on + "'" +
                                ")";

                        Boolean res = ls.executeNonQuery(ans1);
                        Log.d("000555", "Data: " + ans1);
                        Log.d("000555", "Query: " + res.toString());

                        //    Toast.makeText(ctx, "Addedd", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Log.d("000555", "Err: " + e.getMessage());
                     //   Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Log.d("000555", "NULLLL: ");
                }
            }

        } catch (Exception e) {
            Log.d("000555", "Err: " + e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void sendPostRequest(final String member_uid, final String record_data, final String data, final String added_by, final String added_on) {

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

                        Log.d("000555", "Response:    " + response);

                        Lister ls = new Lister(Child_BemaariRecordForm_Activity.this);
                      ls.createAndOpenDB();

                        String update_record = "UPDATE CBEMARI SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + child_uid + "'AND record_data= '" + et_tareekh_indraj.getText().toString() + "'AND added_on= '" + added_on + "'";

                        ls.executeNonQuery(update_record);

                        Toast tt  =Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT);

                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();

                    } else {
                        Log.d("000555", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(Child_BemaariRecordForm_Activity.this, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    Log.d("000555", "catch: " + e.getMessage());
                    Toast tt  =Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT);

                    tt.setGravity(Gravity.CENTER, 0, 0);
                    tt.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "error:" + error.getMessage());
                // Toast.makeText(Child_BemaariRecordForm_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
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


    public void ReferalFormSubmit() {
        try {

            Lister ls = new Lister(Child_BemaariRecordForm_Activity.this);
          ls.createAndOpenDB();

            JSONObject jobj = new JSONObject();

            jobj.put("lat", "" + String.valueOf(latitude));
            jobj.put("lng", "" + String.valueOf(longitude));
            jobj.put("referal_type", "" + "CBemari");
            jobj.put("referal_reason", "" + et_refferal_ki_waja.getText().toString());
            jobj.put("referal_facility", "" + et_refferal_hospital.getText().toString());//spinner
            jobj.put("added_on", "" + "null");//spinner
            //spinner


            String cur_ref_added_on = String.valueOf(System.currentTimeMillis());


            // jobjMain.put("data", jobj);
            String ans1 = "insert into REFERAL (member_uid, record_data, data,added_by, is_synced,added_on)" +
                    "values" +
                    "(" +
                    "'" + child_uid + "'," +
                    "'" + et_tareekh_indraj.getText().toString() + "'," +
                    "'" + jobj + "'," +
                    "'" + login_useruid + "'," +
                    "'0'," +
                    "'" + cur_ref_added_on + "'" +
                    ")";

            Boolean res = ls.executeNonQuery(ans1);
            Log.d("000555", " Ref Data: " + ans1);
            Log.d("000555", "Ref Data: " + res.toString());

            sendPostRequestReferal(child_uid, et_tareekh_indraj.getText().toString(), String.valueOf(jobj), login_useruid, cur_ref_added_on);

            //  Toast.makeText(getApplicationContext(),String.valueOf(res)+String.valueOf(ans1),Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.d("000555", "Ref Err: " + e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
                    /*Intent intent = new Intent(ctx, Add_Family_Member_Activity.class);
                    startActivity(intent);*/
            finish();
        }


    }

    private void sendPostRequestReferal(final String member_uid, final String record_data, final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/family/referrals";

        Log.d("000555", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000555", "Ref Response:    " + response);

                        Lister ls = new Lister(Child_BemaariRecordForm_Activity.this);
                      ls.createAndOpenDB();

                        String update_record = "UPDATE REFERAL SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + child_uid + "'AND record_data= '" + et_tareekh_indraj.getText().toString() + "'AND added_on= '" + added_on + "'";

                        ls.executeNonQuery(update_record);

                        //   Toast.makeText(Child_BemaariRecordForm_Activity.this, "Data has been saved", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000555", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        //       Toast.makeText(Child_BemaariRecordForm_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {

                    //Toast.makeText(Child_BemaariRecordForm_Activity.this, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();
                    Log.d("000555", "ref catch " + e.getMessage());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "Ref onErrorResponse: " + error.getMessage());
                //  Toast.makeText(Child_BemaariRecordForm_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();


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


        DatePickerDialog datePickerDialog = new DatePickerDialog(Child_BemaariRecordForm_Activity.this, R.style.DatePickerDialog,
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
                        medicine_type_1 = "0";
                        et_tablet_quantity_1.setHint(R.string.sypQuantity);
                    } else if (sp_ishal_medicine.getSelectedItem().toString().contains("Lotion")) {
                        medicine_type_1 = "0";
                        et_tablet_quantity_1.setHint(R.string.lotionQuantity);
                    } else if (sp_ishal_medicine.getSelectedItem().toString().equalsIgnoreCase("ORS")) {
                        medicine_type_1 = "0";
                        et_tablet_quantity_1.setHint(R.string.sachetQuantity);
                    } else if (sp_ishal_medicine.getSelectedItem().toString().startsWith("Eye")) {
                        medicine_type_1 = "0";
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
                        //medicine_type_2 = "0";
                        et_tablet_quantity_2.setHint(R.string.sypQuantity);
                    } else if (sp_khansi_aur_saans_ki_bemaari.getSelectedItem().toString().contains("Lotion")) {
                        medicine_type_2 = "3";
                        //medicine_type_2 = "0";
                        et_tablet_quantity_2.setHint(R.string.lotionQuantity);
                    } else if (sp_khansi_aur_saans_ki_bemaari.getSelectedItem().toString().equalsIgnoreCase("ORS")) {
                        medicine_type_2 = "4";
                        //medicine_type_2 = "0";
                        et_tablet_quantity_2.setHint(R.string.sachetQuantity);
                    } else if (sp_khansi_aur_saans_ki_bemaari.getSelectedItem().toString().startsWith("Eye")) {
                        medicine_type_2 = "5";
                        //medicine_type_2 = "0";
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
