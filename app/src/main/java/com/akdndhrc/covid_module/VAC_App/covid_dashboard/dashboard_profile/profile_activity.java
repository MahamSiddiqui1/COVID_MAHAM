package com.akdndhrc.covid_module.VAC_App.covid_dashboard.dashboard_profile;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.CustomClass.NothingSelectedSpinnerAdapter;
import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.CustomClass.UrlClass;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.Child_Dashboard_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_RegisterActivities.Register_QRCode_Activity;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.ServiceLocation;
import com.akdndhrc.covid_module.Utils;
import com.akdndhrc.covid_module.VAC_App.HomePageVacinator_Activity;
import com.akdndhrc.covid_module.VAC_App.covid_dashboard.CovidModule_Dashboard_Activity;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.akdndhrc.covid_module.R.string.dataServiceNotSyncedEng;

public class profile_activity extends AppCompatActivity {

    Context ctx = profile_activity.this;

    String child_uid, TodayDate, khandan_id, TAG = "000555", added_on;

    ImageView iv_navigation_drawer, iv_home, iv_close,iv_editprofile;
    Button btn_jamaa_kre, btn_jari_rhy;
    Spinner sp_gaon, sp_zila, sp_union_council, sp_tahseel, sp_jins,sp_azdawaji_hasiyat,sp_peenay_kai_paani_ka_zarya, sp_latrine_system;
    Switch sw_biometric_faal_kre, sw_qrcode_faal_kre;
    EditText et_indid,et_Age, et_famid,et_bachey_ka_naam, et_tareekh_pedaish, et_walid_ka_naam, et_walida_ka_naam,et_walid_ka_shanakti_card_number, et_walid_ka_mobile_number, et_vacination_card_number, et_address,et_shohar_ka_naam,
            et_shohar_ka_cnic_number,et_shohar_ka_mobile_number,et_peenay_kai_paani_ka_zarya, et_latrine_system;

    String monthf2, dayf2, yearf2 = "null", union_council, khandan_uuid,peenay_kai_paani_ka_zarya, latrine_system, abc;
    LinearLayout ll_husband_fields,ll_vacination_card_number;
    LinearLayout ll_latrine_system, ll_peenay_kai_paani;
    ProgressBar pbProgress;
    JSONObject jsonObject;
    Dialog alertDialog;
    double latitude;
    double longitude;

    private int mYear, mMonth, mDay;
    int date_for_condition = 0;
    int month_for_condition = 0;
    public String hold_age_date_condition = "fromage";

    ServiceLocation serviceLocation;
    String temp_tareekh_wafat = "0";
    String temp_tareekh_nakalmakani = "0";
    String login_useruid;
    public static String var_child_pro = "0";
    int age;
    ImageView image_arrow_down_jins, image_arrow_down_wafat;
    LinearLayout ll_wafat, ll_wajah_wafat_likhey;
    String child_status, khandan_number;
    String qr_code_text = "none";
    long mLastClickTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.covid_dashboard_profile);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, profile_activity.class));

        child_uid = getIntent().getExtras().getString("u_id");

        var_child_pro = "0";

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
            String shared_useruid = prefelse.getString(String.valueOf(R.string.loginUserIDEng), null); // getting String
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
        et_bachey_ka_naam = findViewById(R.id.et_bachey_ka_naam);
        et_tareekh_pedaish = findViewById(R.id.et_tareekh_pedaish);
        et_walid_ka_naam = findViewById(R.id.et_walid_ka_naam);
        et_walida_ka_naam = findViewById(R.id.et_walida_ka_naam);
        et_walid_ka_shanakti_card_number = findViewById(R.id.et_walid_ka_shanakti_card_number);
        et_walid_ka_mobile_number = findViewById(R.id.et_mobile_number);
        et_vacination_card_number = findViewById(R.id.et_vacination_card_number);
        et_address = findViewById(R.id.et_address);
        et_famid=findViewById(R.id.et_fam_vac_id);
        //   et_shohar_ka_naam = findViewById(R.id.et_shohar_ka_naam);
        //  et_shohar_ka_cnic_number = findViewById(R.id.et_shohar_ka_cnic_number);
        // et_shohar_ka_mobile_number = findViewById(R.id.et_shohar_ka_mobile_number);

        //Linear Layout
        ll_husband_fields = findViewById(R.id.ll_husband_fields);
        ll_vacination_card_number = findViewById(R.id.ll_vacination_card_number);

        //ImageView
       // iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
//        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);
        iv_editprofile = findViewById(R.id.iv_editprofile);

        //Spinner
        sp_jins = findViewById(R.id.sp_jins);
        sp_tahseel = findViewById(R.id.sp_tahseel);
        sp_union_council = findViewById(R.id.sp_union_council);
        sp_gaon = findViewById(R.id.sp_gaon);
        sp_zila = findViewById(R.id.sp_zila);
        sp_azdawaji_hasiyat = findViewById(R.id.sp_azdawaji_hasiyat);
        sp_union_council.setEnabled(true);
        sp_gaon.setEnabled(true);
        sp_zila.setEnabled(true);
        sp_tahseel.setEnabled(true);


        sp_latrine_system = findViewById(R.id.sp_latrine_system);
        sp_peenay_kai_paani_ka_zarya = findViewById(R.id.sp_peenay_kai_paani_ka_zarya);


        //Switch
        sw_biometric_faal_kre = findViewById(R.id.sw_biometric_faal_kre);
        sw_qrcode_faal_kre = findViewById(R.id.sw_qrcode_faal_kre);

        //Button
        btn_jamaa_kre = findViewById(R.id.submit);
        btn_jamaa_kre.setVisibility(View.GONE);


        //LinearLayout
        ll_latrine_system = findViewById(R.id.ll_latrine_system);
        ll_peenay_kai_paani = findViewById(R.id.ll_peenay_kai_paani);






        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(ctx, HomePageVacinator_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
                var_child_pro = "0";
            }
        });



        iv_editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   pbProgress.setVisibility(View.VISIBLE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        et_bachey_ka_naam.setEnabled(true);
                        et_tareekh_pedaish.setEnabled(true);
                        et_walid_ka_naam.setEnabled(true);
                        et_walid_ka_shanakti_card_number.setEnabled(true);
                        et_walid_ka_mobile_number.setEnabled(true);
                        et_vacination_card_number.setEnabled(true);
                        et_famid.setEnabled(true);
                        et_address.setEnabled(true);
                        sp_tahseel.setEnabled(true);
                        sp_union_council.setEnabled(true);
                        sp_jins.setEnabled(true);
                        sp_gaon.setEnabled(true);
                        sp_zila.setEnabled(true);
                        sp_azdawaji_hasiyat.setEnabled(true);
                        sp_latrine_system.setEnabled(true);
                        sp_peenay_kai_paani_ka_zarya.setEnabled(true);
                        btn_jamaa_kre.setVisibility(View.VISIBLE);
                        iv_editprofile.setVisibility(View.GONE);


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



        // Select sp_jins
        // Sp gender
        final ArrayAdapter<CharSequence> adptr_gender = ArrayAdapter.createFromResource(this, R.array.array_gender, R.layout.temp);
        adptr_gender.setDropDownViewResource(R.layout.temp);

        sp_jins.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_gender,
                        R.layout.temp,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));


        sp_jins.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        // Select sp_azdawaji_hasiyat
        final ArrayAdapter<CharSequence> adptr_azdawaji_hasiyat = ArrayAdapter.createFromResource(this, R.array.array_azdawaji_hasiyat, android.R.layout.simple_spinner_item);
        adptr_azdawaji_hasiyat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        sp_azdawaji_hasiyat.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_azdawaji_hasiyat,
                        R.layout.spinner_azdawaji_hasiyat_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));


        sp_azdawaji_hasiyat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


//                    ll_husband_fields.setVisibility(View.GONE);
                //  et_shohar_ka_naam.getText().clear();
                // et_shohar_ka_mobile_number.getText().clear();
                //et_shohar_ka_cnic_number.getText().clear();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Select sp_peenay_kai_paani_ka_zarya
        final ArrayAdapter<CharSequence> adptr_peenay_kai_paani = ArrayAdapter.createFromResource(this, R.array.array_peenay_kai_paani_ka_zarya, android.R.layout.simple_spinner_item);
        adptr_peenay_kai_paani.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_peenay_kai_paani_ka_zarya.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_peenay_kai_paani,
                        R.layout.spinner_tehseel_layout,
                        this));
        sp_peenay_kai_paani_ka_zarya.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 5) {
                    ll_peenay_kai_paani.setVisibility(View.VISIBLE);
                 /*   peenay_kai_paani_ka_zarya = String.valueOf(sp_peenay_kai_paani_ka_zarya.getSelectedItemPosition() - 1);
                    peenay_kai_paani_ka_zarya = peenay_kai_paani_ka_zarya + "|" + et_peenay_kai_paani_ka_zarya.getText().toString();*/

                    abc = et_peenay_kai_paani_ka_zarya.getText().toString();

                } else {
                    ll_peenay_kai_paani.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Select sp_latrine_system
        final ArrayAdapter<CharSequence> adptr_latrine_system = ArrayAdapter.createFromResource(this, R.array.array_latrine_system, android.R.layout.simple_spinner_item);
        adptr_latrine_system.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_latrine_system.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_latrine_system,
                        R.layout.spinner_tehseel_layout,
                        this));
        sp_latrine_system.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 3) {
                    ll_latrine_system.setVisibility(View.VISIBLE);
                    latrine_system = String.valueOf(sp_latrine_system.getSelectedItemPosition() - 1) + et_latrine_system.getText().toString();
                } else {
                    ll_latrine_system.setVisibility(View.GONE);
                    latrine_system = String.valueOf(sp_latrine_system.getSelectedItemPosition() - 1);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        // Select sp_tahseel
        final ArrayAdapter<CharSequence> adptr_tahseel = ArrayAdapter.createFromResource(this, R.array.array_tahseel, android.R.layout.simple_spinner_item);
        adptr_tahseel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        sp_tahseel.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_tahseel,
                        R.layout.spinner_tehseel_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));

        // Utils.setSpinnerTehsel(getApplicationContext(), sp_tahseel);

        sp_tahseel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Lister ls = new Lister(getApplicationContext());
                    ls.createAndOpenDB();
                    String selDistrict = String.valueOf(sp_tahseel.getSelectedItem());
                    Log.d("000555", "UNIONCOUNCIL: " + selDistrict);
                    String[][] tehsils = ls.executeReader("SELECT name FROM UNIONCOUNCIL WHERE tehsil_id = ( SELECT uid FROM TEHSIL WHERE name = '" + selDistrict + "')");
                    if (tehsils == null) {
                        // Toast.makeText(parent.getContext(), "There was an error fetching details.", Toast.LENGTH_LONG).show();
                    } else {
                        Utils.setSpinnerTehsel(profile_activity.this, sp_union_council, tehsils);

                    }
                    //((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                    //Toast.makeText(parent.getContext(), "Selected: " + position, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    //  Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        // Select sp_union_council
      final ArrayAdapter<CharSequence> adptr_union = ArrayAdapter.createFromResource(this, R.array.array_union_council, android.R.layout.simple_spinner_item);
        adptr_union.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        sp_union_council.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_union,
                        R.layout.spinner_union_council_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));

        Utils.setSpinnerUnionCouncil(getApplicationContext(), sp_union_council);

        sp_union_council.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Lister ls = new Lister(getApplicationContext());
                    ls.createAndOpenDB();

                    String selDistrict = String.valueOf(sp_union_council.getSelectedItem());
                    Log.d("000555", "Village: " + selDistrict);
                    String[][] tehsils = ls.executeReader("SELECT name FROM VILLAGES WHERE uc_id = ( SELECT uid FROM UNIONCOUNCIL WHERE name = '" + selDistrict + "')");
                    if (tehsils == null) {
                        //  Toast.makeText(parent.getContext(), "There was an error fetching details.", Toast.LENGTH_LONG).show();
                    } else {
                        Utils.setSpinnerTehsel(profile_activity.this, sp_gaon, tehsils);

                    }
                    //((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                    //Toast.makeText(parent.getContext(), "Selected: " + position, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }

//                ((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                //Toast.makeText(parent.getContext(), "Selected: " + position, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        // Select sp_zila
        final ArrayAdapter<CharSequence> adptr_zila = ArrayAdapter.createFromResource(this, R.array.array_zila, android.R.layout.simple_spinner_item);
        adptr_zila.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        sp_zila.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_zila,
                        R.layout.spinner_zila_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));


        Utils.setSpinnerdistrict(getApplicationContext(), sp_zila);
        sp_zila.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                ((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                //Toast.makeText(parent.getContext(), "Selected: " + position, Toast.LENGTH_LONG).show();
                try {
                    Lister ls = new Lister(getApplicationContext());
                    ls.createAndOpenDB();

                    String selDistrict = String.valueOf(sp_zila.getSelectedItem());
                    Log.d("000555", "Tehsil: " + selDistrict);

                    String[][] tehsils = ls.executeReader("SELECT name FROM TEHSIL WHERE district_id = ( SELECT uid FROM DISTRICT WHERE name = '" + selDistrict + "')");
                    if (tehsils == null) {
                        // Toast.makeText(parent.getContext(), "There was an error fetching details.", Toast.LENGTH_LONG).show();
                    } else {
                        Utils.setSpinnerTehsel(profile_activity.this, sp_tahseel, tehsils);

                    }
                    //((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                    //Toast.makeText(parent.getContext(), "Selected: " + position, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    //  Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        // Select sp_gaon
       final ArrayAdapter<CharSequence> adptr_gaon = ArrayAdapter.createFromResource(this, R.array.array_gaon, android.R.layout.simple_spinner_item);
        adptr_gaon.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        sp_gaon.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_gaon,
                        R.layout.spinner_gaon_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));


        Utils.setSpinnervillage(getApplicationContext(), sp_gaon);
        sp_gaon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


    }







    private void update_data(final View v) {


        if (serviceLocation.showCurrentLocation() == true) {

            latitude = serviceLocation.getLatitude();
            longitude = serviceLocation.getLongitude();

            Log.d("000987", " latitude: " + latitude);
            Log.d("000987", " longitude: " + longitude);
        }

        try {
            alertDialog = new Dialog(ctx);
            LayoutInflater layout = LayoutInflater.from(ctx);
            final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

            alertDialog.setContentView(dialogView);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();
            final String updated_added_on = String.valueOf(System.currentTimeMillis());


            if (jsonObject.has("lat")) {
                jsonObject.put("lat", "" + String.valueOf(latitude));
                jsonObject.put("lng", "" + String.valueOf(longitude));
                 jsonObject.put("vaccination_card_number", "" + et_vacination_card_number.getText().toString());
                jsonObject.put("full_name", "" + et_bachey_ka_naam.getText().toString());
                jsonObject.put("father_name", "" + et_walid_ka_naam.getText().toString());
                jsonObject.put("address", "" + et_address.getText().toString());
                jsonObject.put("nic", "" + et_walid_ka_shanakti_card_number.getText().toString());
                jsonObject.put("mobile", "" + et_walid_ka_mobile_number.getText().toString());
                jsonObject.put("dob", "" + et_tareekh_pedaish.getText().toString());
                //  jsonObject.put("age", "" + et_age.getText().toString());
                jsonObject.put("gender", "" + String.valueOf(sp_jins.getSelectedItemPosition() - 1));
                jsonObject.put("sp_tahseel_pos", "" + String.valueOf(sp_tahseel.getSelectedItemPosition() - 1));
                jsonObject.put("sp_zila_pos", "" + String.valueOf(sp_zila.getSelectedItemPosition() - 1));//spinner
                jsonObject.put("sp_village_pos", "" + String.valueOf(sp_gaon.getSelectedItemPosition() - 1));//spinner
                jsonObject.put("sp_unioncouncil_pos", "" + String.valueOf(sp_union_council.getSelectedItemPosition() - 1));//spinner
                jsonObject.put("sp_laterine", "" + String.valueOf(sp_latrine_system.getSelectedItemPosition() - 1));//spinner
                jsonObject.put("sp_water", "" + String.valueOf(sp_peenay_kai_paani_ka_zarya.getSelectedItemPosition() - 1));//spinner
                jsonObject.put("sp_azdawaji", "" + String.valueOf(sp_azdawaji_hasiyat.getSelectedItemPosition() - 1));//spinner
                jsonObject.put("khandan_number_manual", "" + et_famid.getText().toString());
                jsonObject.put("record_date", "" + TodayDate);
                jsonObject.put("added_on", "" + updated_added_on);
                jsonObject.put("maritalstatus", "" + String.valueOf(sp_azdawaji_hasiyat.getSelectedItemPosition() - 1));//spinner

            }


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE MEMBER SET " +
                                "full_name='" + et_bachey_ka_naam.getText().toString() + "'," +
                                "nicnumber='" + et_walid_ka_shanakti_card_number.getText().toString() + "'," +
                                "phone_number='" + et_walid_ka_mobile_number.getText().toString() + "'," +
                                "gender='" + String.valueOf(sp_jins.getSelectedItemPosition() - 1) + "'," +
                                "dob='" + et_tareekh_pedaish.getText().toString() + "'," +
                                "data='" + jsonObject.toString() + "'," +
                                "is_synced='" + 0 + "'" +
                                "WHERE uid = '" + child_uid + "'";
                        ls.executeNonQuery(update_record);

                        final Snackbar snackbar = Snackbar.make(v, R.string.dataUpdatedEng, Snackbar.LENGTH_SHORT);
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

                    sendPostRequest(et_famid.getText().toString(), child_uid, et_famid.getText().toString(), et_bachey_ka_naam.getText().toString(), et_walid_ka_shanakti_card_number.getText().toString(),
                            et_walid_ka_mobile_number.getText().toString(), String.valueOf(sp_jins.getSelectedItemPosition() - 1), "0", et_tareekh_pedaish.getText().toString(),
                            "bio_code", "0", login_useruid, String.valueOf(jsonObject), added_on);
                             } else {
                                         }
            }
            catch (Exception e) {
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
                //  Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
                Log.d("000987", " Error" + e.getMessage());
                //Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
            }


        }




    private void sendPostRequest(final String manual_id, final String uid, final String khandan_id, final String full_name,
                                 final String nicnumber, final String phone_number, final String gender, final String age, final String dob,
                                 final String bio_code, final String qr_code, final String added_by,
                                 final String data, final String added_on) {

        // String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/family/members";
        String url = UrlClass.update_member_url;

        Log.d("000999", "mURL " + url);

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

                        String update_record = "UPDATE MEMBER SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE uid = '" + child_uid + "'";

                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d(TAG, "Updated Data: " + update_record);
                        Log.d(TAG, "Updated Query: " + res.toString());

                        Toast tt  =Toast.makeText(ctx, R.string.dataSyncedEnglish, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();
                        //  Toast.makeText(ctx, "Data updated successfuly.", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000555", "else ");
                        // Toast.makeText(ctx, "Data has not been updated to the service.", Toast.LENGTH_SHORT).show();

                        Toast tt  =Toast.makeText(ctx, R.string.dataNotSyncedEnglish, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();
                    }

                } catch (Exception e) {
                    Log.d("000555", "catch:   " + e.getMessage());
                    //   Toast.makeText(ctx, "Data has been sent updated.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ctx, dataServiceNotSyncedEng, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "error " + error.getMessage());
                // Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast tt  =Toast.makeText(ctx, R.string.dataNotSyncedEnglish, Toast.LENGTH_SHORT);
                tt.setGravity(Gravity.CENTER, 0, 0);
                tt.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("manual_id", manual_id);
                params.put("uid", uid);
                params.put("family_id", khandan_id);
                params.put("qr_code", qr_code);
                params.put("full_name", full_name);
                params.put("nic_number", nicnumber);
                params.put("phone_number", phone_number);
                params.put("data", data);
                params.put("gender", gender);
                params.put("dob", dob);
                params.put("bio_code", bio_code);
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


    @Override
    protected void onResume() {
        super.onResume();

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            String mData[][] = ls.executeReader("Select data,khandan_id,added_on,gender,age,qr_code from MEMBER where uid = '" + child_uid + "'");
            Log.d("000555", "Data: : " + mData[0][0]);

            String json = mData[0][0];
            khandan_id = mData[0][1];
            added_on = mData[0][2];
            age = Integer.parseInt(mData[0][4]);
            qr_code_text =mData[0][5];

            jsonObject = new JSONObject(json);

            et_bachey_ka_naam.setEnabled(false);
            et_walid_ka_naam.setEnabled(false);
            et_walid_ka_shanakti_card_number.setEnabled(false);
            et_tareekh_pedaish.setEnabled(false);
            et_walid_ka_mobile_number.setEnabled(false);
            et_address.setEnabled(false);
            et_famid.setEnabled(false);
            et_vacination_card_number.setEnabled(false);
            sp_jins.setEnabled(false);
            sp_tahseel.setEnabled(false);
            sp_zila.setEnabled(false);
            sp_gaon.setEnabled(false);
            sp_union_council.setEnabled(false);
            sp_azdawaji_hasiyat.setEnabled(false);
            sp_latrine_system.setEnabled(false);
            sp_peenay_kai_paani_ka_zarya.setEnabled(false);


            et_bachey_ka_naam.setText(jsonObject.getString("full_name"));
            et_walid_ka_naam.setText(jsonObject.getString("father_name"));
            et_walid_ka_shanakti_card_number.setText(jsonObject.getString("nic"));
            et_address.setText(jsonObject.getString("address"));
            et_walid_ka_mobile_number.setText(jsonObject.getString("mobile"));
            sp_jins.setSelection(Integer.parseInt(jsonObject.getString("gender")) + 1);
            et_tareekh_pedaish.setText(jsonObject.getString("dob"));
            et_famid.setText(jsonObject.getString("fam_id"));
            et_vacination_card_number.setText(jsonObject.getString("vaccination_card_number"));
            sp_latrine_system.setSelection(Integer.parseInt(jsonObject.getString("sp_laterine")) + 1);
            sp_peenay_kai_paani_ka_zarya.setSelection(Integer.parseInt(jsonObject.getString("sp_laterine")) + 1);
            sp_tahseel.setSelection(Integer.parseInt(jsonObject.getString("sp_tahseel_pos")) + 1);
            sp_zila.setSelection(Integer.parseInt(jsonObject.getString("sp_zila_pos")) + 1);
            sp_gaon.setSelection(Integer.parseInt(jsonObject.getString("sp_village_pos")) + 1);
            sp_union_council.setSelection(Integer.parseInt(jsonObject.getString("sp_unioncouncil_pos")) + 1);
            sp_azdawaji_hasiyat.setSelection(Integer.parseInt(jsonObject.getString("maritalstatus")) + 1);
            khandan_number = jsonObject.getString("khandan_number_manual");



            ls.closeDB();

        } catch (Exception e) {
            e.printStackTrace();

            Log.d("000555", "Error: " + e.getMessage());
           // Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();

    }
}
