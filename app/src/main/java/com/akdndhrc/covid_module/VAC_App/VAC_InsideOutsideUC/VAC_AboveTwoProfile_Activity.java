package com.akdndhrc.covid_module.VAC_App.VAC_InsideOutsideUC;

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

import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.NothingSelectedSpinnerAdapter;
import com.akdndhrc.covid_module.ServiceLocation;
import com.akdndhrc.covid_module.Utils;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;

import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.VAC_App.HomePageVacinator_Activity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class VAC_AboveTwoProfile_Activity extends AppCompatActivity {

    Context ctx = VAC_AboveTwoProfile_Activity.this;

    String mother_uid;
    ImageView iv_navigation_drawer, iv_home, iv_editprofile;
    Button btn_jamaa_kre, btn_jari_rhy;
    Spinner sp_jins, sp_azdawaji_hasiyat,sp_wajah_wafat;
    Switch sw_biometric_faal_kre, sw_qrcode_faal_kre,sw_mother_status;
    EditText et_bachey_ka_naam, et_tareekh_pedaish, et_walid_ka_naam, et_walid_ka_shanakti_card_number, et_walid_ka_mobile_number, et_vacination_card_number,
            et_address, et_walida_ka_naam, et_shohar_ka_naam, et_shohar_ka_cnic_number, et_shohar_ka_mobile_number,et_tabsarah_wajah_wafaat,et_tareekh_wafaat;
    double latitude;
    double longitude;
    String qr_code_text = "none";
    String TodayDate, union_council;

    Dialog alertDialog;
    ProgressBar pbProgress;
    ServiceLocation serviceLocation;
    String login_useruid, khandan_id, added_on, mother_age;
    JSONObject jsonObject;
    String mDatamother[][];
    public static String var_updateprofile_abovetwotemp = "null";
    LinearLayout ll_husband_fields, ll_vacination_card_number;
    private int mYear, mMonth, mDay;
    String monthf2, dayf2, yearf2 = "null";
   ImageView image_arrow_down_azdawaji,image_arrow_down_jins, image_arrow_down_wafat;
    LinearLayout ll_wafat,ll_wajah_wafat_likhey;
    String mother_status;
    String temp_tareekh_wafat = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vac_abovetwo_profile);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, VAC_AboveTwoProfile_Activity.class));

        mother_uid = getIntent().getExtras().getString("u_id");


        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());


        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences("UserLogin", 0); // 0 - for private mode
            String shared_useruid = prefelse.getString("login_userid", null); // getting String
            login_useruid = shared_useruid;
            Log.d("000123", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000123", "Shared Err:" + e.getMessage());
        }


        try {
            serviceLocation = new ServiceLocation(ctx);
            serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            serviceLocation.callAsynchronousTask();
        } catch (Exception e) {
            Log.d("000123", "GPS Service Err:  " + e.getMessage());
        }

        //  check_gps();


        //EditText
        et_bachey_ka_naam = findViewById(R.id.et_bachey_ka_naam);
        et_tareekh_pedaish = findViewById(R.id.et_tareekh_pedaish);
        et_walid_ka_naam = findViewById(R.id.et_walid_ka_naam);
        et_walid_ka_shanakti_card_number = findViewById(R.id.et_walid_ka_shanakti_card_number);
        et_walid_ka_mobile_number = findViewById(R.id.et_walid_ka_mobile_number);
        et_vacination_card_number = findViewById(R.id.et_vacination_card_number);
        et_address = findViewById(R.id.et_address);
        et_walida_ka_naam = findViewById(R.id.et_walida_ka_naam);
        et_shohar_ka_naam = findViewById(R.id.et_shohar_ka_naam);
        et_shohar_ka_cnic_number = findViewById(R.id.et_shohar_ka_cnic_number);
        et_shohar_ka_mobile_number = findViewById(R.id.et_shohar_ka_mobile_number);
        et_tareekh_wafaat = findViewById(R.id.et_tareekh_wafaat);
        et_tabsarah_wajah_wafaat = findViewById(R.id.et_tabsarah_wajah_wafaat);

        //Linear Layout
        ll_husband_fields = findViewById(R.id.ll_husband_fields);
        ll_vacination_card_number = findViewById(R.id.ll_vacination_card_number);


        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_editprofile = findViewById(R.id.iv_editprofile);
        image_arrow_down_jins = findViewById(R.id.image_arrow_down_jins);
        image_arrow_down_azdawaji = findViewById(R.id.image_arrow_down_azdawaji);
        image_arrow_down_wafat = findViewById(R.id.image_arrow_down_wafat);

        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);

//progress
        pbProgress = findViewById(R.id.pbProgress);

        //Spinner
        sp_jins = findViewById(R.id.sp_jins);
        sp_azdawaji_hasiyat = findViewById(R.id.sp_azdawaji_hasiyat);
        sp_wajah_wafat = findViewById(R.id.sp_wajah_wafat);

        //Linear layout
        ll_wafat = findViewById(R.id.ll_wafat);
        ll_wajah_wafat_likhey = findViewById(R.id.ll_wajah_wafat_likhey);


        //Switch
        sw_biometric_faal_kre = findViewById(R.id.sw_biometric_faal_kre);
        sw_qrcode_faal_kre = findViewById(R.id.sw_qrcode_faal_kre);
        sw_mother_status = findViewById(R.id.sw_mother_status);

        sw_qrcode_faal_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, VAC_Register_QRCode_Activity.class);
                intent.putExtra("class_id", "2");
                startActivity(intent);
            }
        });

        //Button
        btn_jamaa_kre = findViewById(R.id.submit);

        SpinnersData();

        read_profile();

        et_tareekh_pedaish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog();
            }
        });

        /////////////////// Walid ka  CNIC Number /////////////////////////////////////
        et_walid_ka_shanakti_card_number.addTextChangedListener(new TextWatcher() {
            int prev = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prev = et_walid_ka_shanakti_card_number.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if ((prev < length) && (length == 5 || length == 13)) {
                    String data = et_walid_ka_shanakti_card_number.getText().toString();
                    et_walid_ka_shanakti_card_number.setText(data + "-");
                    et_walid_ka_shanakti_card_number.setSelection(length + 1);
                }
            }
        });

        ////////////////// Walid Ka Mobile NUmber ////////////////////////////////////

        et_walid_ka_mobile_number.addTextChangedListener(new TextWatcher() {
            int previous = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previous = et_walid_ka_mobile_number.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if ((previous < length) && (length == 4)) {
                    String data = et_walid_ka_mobile_number.getText().toString();
                    et_walid_ka_mobile_number.setText(data + "-");
                    et_walid_ka_mobile_number.setSelection(length + 1);
                }
            }
        });

        /////////////////// Husband ka  CNIC Number //////////////////////////////////////
        et_shohar_ka_cnic_number.addTextChangedListener(new TextWatcher() {
            int prevs = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prevs = et_shohar_ka_cnic_number.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if ((prevs < length) && (length == 5 || length == 13)) {
                    String data = et_shohar_ka_cnic_number.getText().toString();
                    et_shohar_ka_cnic_number.setText(data + "-");
                    et_shohar_ka_cnic_number.setSelection(length + 1);
                }
            }
        });

        ////////////////// Shohar Ka Mobile NUmber ////////////////////////////////////

        et_shohar_ka_mobile_number.addTextChangedListener(new TextWatcher() {
            int previouss = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previouss = et_shohar_ka_mobile_number.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if ((previouss < length) && (length == 4)) {
                    String data = et_shohar_ka_mobile_number.getText().toString();
                    et_shohar_ka_mobile_number.setText(data + "-");
                    et_shohar_ka_mobile_number.setSelection(length + 1);
                }
            }
        });

        et_tareekh_wafaat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (temp_tareekh_wafat.equalsIgnoreCase("0")) {
                    ShowTareekhWafatDialog();
                } else {
                    et_tareekh_wafaat.getText().clear();
                    ShowTareekhWafatDialog();
                }

            }
        });

        sw_qrcode_faal_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, VAC_Register_QRCode_Activity.class);
                intent.putExtra("class_id", "2");
                startActivity(intent);
            }
        });

        sw_mother_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    ll_wafat.setVisibility(View.GONE);
                    mother_status="0";
                }
                else{
                    ll_wafat.setVisibility(View.VISIBLE);
                    mother_status="1";
                }
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

        iv_editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pbProgress.setVisibility(View.VISIBLE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        et_bachey_ka_naam.setEnabled(true);
                        et_tareekh_pedaish.setEnabled(true);
                        et_walid_ka_naam.setEnabled(true);
                        et_walid_ka_shanakti_card_number.setEnabled(true);
                        et_walid_ka_mobile_number.setEnabled(true);
                        et_address.setEnabled(true);
                        et_vacination_card_number.setEnabled(true);
                        et_walida_ka_naam.setEnabled(true);
                        et_shohar_ka_naam.setEnabled(true);
                        et_shohar_ka_cnic_number.setEnabled(true);
                        et_shohar_ka_mobile_number.setEnabled(true);
                        sp_azdawaji_hasiyat.setEnabled(true);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            image_arrow_down_jins.setImageTintList(ctx.getResources().getColorStateList(R.color.dark_blue_color));
                            image_arrow_down_azdawaji.setImageTintList(ctx.getResources().getColorStateList(R.color.dark_blue_color));
                        }


                        if (mDatamother[0][3].equalsIgnoreCase("-1")) {
                            sp_jins.setEnabled(true);
                        } else {
                            sp_jins.setEnabled(true);
                        }

                        if (mDatamother[0][9].equalsIgnoreCase("no_qr_code")) {
                            sw_qrcode_faal_kre.setEnabled(true);
                        } else if (mDatamother[0][9].equalsIgnoreCase("none")) {
                            sw_qrcode_faal_kre.setEnabled(true);
                        } else {
                            sw_qrcode_faal_kre.setEnabled(false);
                        }


                        if (mother_status.equalsIgnoreCase("1"))
                        {
                            sw_mother_status.setEnabled(false);
                            sp_wajah_wafat.setEnabled(false);
                            et_tareekh_wafaat.setEnabled(false);
                            et_tabsarah_wajah_wafaat.setEnabled(false);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                image_arrow_down_wafat.setImageTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                            }
                        }
                        else {
                            sw_mother_status.setEnabled(true);
                            sp_wajah_wafat.setEnabled(true);
                            et_tareekh_wafaat.setEnabled(true);
                            et_tabsarah_wajah_wafaat.setEnabled(true);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                image_arrow_down_wafat.setImageTintList(ctx.getResources().getColorStateList(R.color.dark_blue_color));
                            }
                        }


                        btn_jamaa_kre.setVisibility(View.VISIBLE);
                        pbProgress.setVisibility(View.GONE);
                        iv_editprofile.setVisibility(View.GONE);


                    }
                }, 2500);


            }
        });


        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_bachey_ka_naam.getText().toString().isEmpty()) {
                    final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی  نام درج کریں.", Snackbar.LENGTH_SHORT);
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

                if (et_walid_ka_naam.getText().toString().isEmpty()) {
                    //   Toast.makeText(getApplicationContext(), "برائے مہربانی تاریخ پیدائش منتخب کریں", Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی والد کا نام درج کریں.", Snackbar.LENGTH_SHORT);
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

                if (et_tareekh_pedaish.getText().toString().isEmpty()) {

                    //   Toast.makeText(getApplicationContext(), "برائے مہربانی تاریخ پیدائش منتخب کریں", Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی تاریخ پیدائش منتخب کریں.", Snackbar.LENGTH_SHORT);
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

                if (sp_jins.getSelectedItemPosition() == 0) {
                    final Snackbar snackbar = Snackbar.make(v, "جنس منتخب کریں.", Snackbar.LENGTH_SHORT);
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

                if (et_walid_ka_shanakti_card_number.getText().toString().length() > 0) {
                    if (et_walid_ka_shanakti_card_number.getText().toString().length() < 15) {
                        final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی صحیح شناختی کارڈ نمبر درج کریں.", Snackbar.LENGTH_SHORT);
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
                    //  Toast.makeText(getApplicationContext(), "برائے مہربانی  شناختی کارڈ نمبر درج کریں", Toast.LENGTH_SHORT).show();
                    //return;
                } else {
                    Log.d("000123", "ELSE NIC NUMBER");
                }
                Log.d("000123", "OKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");

                if (Integer.valueOf(mother_age) <= 2) {
                    //Toast.makeText(getApplicationContext(), "منتخب عمر کو دو سال سے زائد ہونا ضروری ہے", Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar.make(v, "منتخب عمر کو دو سال سے زائد ہونا ضروری ہے.", Snackbar.LENGTH_SHORT);
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

                if (mother_status.equalsIgnoreCase("1")){

                    if (et_tareekh_wafaat.getText().toString().length() < 1) {

                        //Toast.makeText(getApplicationContext(), "برائے مہربانی تاریخ پیدائش منتخب کریں", Toast.LENGTH_LONG).show();
                        final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی تاریخ وفات منتخب کریں.", Snackbar.LENGTH_SHORT);
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
                    if (sp_wajah_wafat.getSelectedItemPosition() == 0) {
                        final Snackbar snackbar = Snackbar.make(v, "وجہ وفات منتخب کریں.", Snackbar.LENGTH_SHORT);
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

                    if (sp_wajah_wafat.getSelectedItemPosition()==6)
                    {
                        if (et_tabsarah_wajah_wafaat.getText().toString().isEmpty()) {

                            //Toast.makeText(getApplicationContext(), "برائے مہربانی تاریخ پیدائش منتخب کریں", Toast.LENGTH_LONG).show();
                            final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی وجہ وفات درج کریں.", Snackbar.LENGTH_SHORT);
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
                    }
                    else{

                    }

                }
                else{
                    Log.d("000555", " Wajah Wafat ElSeeeeeeeeeeeee: ");
                }


                if (serviceLocation.showCurrentLocation() == true) {
                    latitude = serviceLocation.getLatitude();
                    longitude = serviceLocation.getLongitude();
                    Log.d("000123", " latitude: " + latitude);
                    Log.d("000123", " longitude: " + longitude);
                } else {

                    try {
                        serviceLocation.doAsynchronousTask.cancel();
                    }catch (Exception e){}
                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from MEMBER");

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

                        Log.d("000258", "Read Member Error Catch: " + e.getMessage());
                    }
                }

                update_data(v);
            }
        });

    }


    private void update_data(final View v) {


        alertDialog = new Dialog(ctx);
        LayoutInflater layout = LayoutInflater.from(ctx);
        final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

        alertDialog.setContentView(dialogView);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();


        try {
            String cur_added_on = String.valueOf(System.currentTimeMillis());

            if (VAC_Register_QRCode_Activity.switch_qr_code_values_vac.equalsIgnoreCase("1")) {

                SharedPreferences settings = getSharedPreferences("shared_QR_Value", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                // Reading from SharedPreferences
                qr_code_text = settings.getString("qr_code", "");
                editor.apply();
                Log.d("000123", "IF: " + qr_code_text);

            } else {
                qr_code_text = "none";
                Log.d("000123", "ELSE: " + qr_code_text);
            }


            if (jsonObject.has("vaination_card_number")) {
                Log.d("000123", "VAC REMOVE: ");
                jsonObject.remove("vaination_card_number");
            } else {
                Log.d("000123", "NO Vac Num : ");
            }

            if (jsonObject.has("lat")) {
                jsonObject.put("lat", "" + String.valueOf(latitude));
                jsonObject.put("lng", "" + String.valueOf(longitude));
                jsonObject.put("status_death", "" + mother_status);
                jsonObject.put("full_name", "" + et_bachey_ka_naam.getText().toString());
                jsonObject.put("father_name", "" + et_walid_ka_naam.getText().toString());
                jsonObject.put("mother_name", "" + et_walida_ka_naam.getText().toString());
                jsonObject.put("nic", "" + et_walid_ka_shanakti_card_number.getText().toString());
                jsonObject.put("dob", "" + et_tareekh_pedaish.getText().toString());
                jsonObject.put("age", "" + mother_age.toString());
                jsonObject.put("maritalstatus", "" + String.valueOf(sp_azdawaji_hasiyat.getSelectedItemPosition() - 1));//spinner
                jsonObject.put("husband_name", "" + et_shohar_ka_naam.getText().toString());
                jsonObject.put("husband_cnic_number", "" + et_shohar_ka_cnic_number.getText().toString());
                jsonObject.put("husband_mobile_number", "" + et_shohar_ka_mobile_number.getText().toString());
                jsonObject.put("address", "" + et_address.getText().toString());
                jsonObject.put("vaccination_card_number", et_vacination_card_number.getText().toString());
                jsonObject.put("date_of_death", "" + et_tareekh_wafaat.getText().toString());
                jsonObject.put("sp_reason_of_death", "" + String.valueOf(sp_wajah_wafat.getSelectedItem()));//spinner
                jsonObject.put("sp_reason_of_death_pos", "" + String.valueOf(sp_wajah_wafat.getSelectedItemPosition() - 1));//spinner
                jsonObject.put("reason_of_death", "" + et_tabsarah_wajah_wafaat.getText().toString());
                jsonObject.put("update_record_date", "" + TodayDate);
                jsonObject.put("added_on", "" + cur_added_on);

            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();
                        String update_record = "UPDATE MEMBER SET " +
                                "khandan_id='" + khandan_id + "'," +
                                "full_name='" + et_bachey_ka_naam.getText().toString() + "'," +
                                "nicnumber='" + et_walid_ka_shanakti_card_number.getText().toString() + "'," +
                                "phone_number='" + et_walid_ka_mobile_number.getText().toString() + "'," +
                                "dob='" + et_tareekh_pedaish.getText().toString() + "'," +
                                "age='" + mother_age.toString() + "'," +
                                "gender='" + String.valueOf(sp_jins.getSelectedItemPosition() - 1) + "'," +
                                "data='" + jsonObject.toString() + "'," +
                                "qr_code='" + qr_code_text + "'," +
                                "is_synced='" + 0 + "'" +
                                "WHERE uid = '" + mother_uid + "'";

                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d("000123", "Data: " + update_record);
                        Log.d("000123", "Query: " + res.toString());


                        if (res.toString().equalsIgnoreCase("true"))
                        {

                            final Snackbar snackbar = Snackbar.make(v, "پروفائل ڈیٹا اپڈیٹ ہوگیا ہے.", Snackbar.LENGTH_SHORT);
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

                                sendPostRequest(mother_uid, mother_uid, khandan_id, et_bachey_ka_naam.getText().toString(), et_walid_ka_shanakti_card_number.getText().toString(),
                                        et_walid_ka_mobile_number.getText().toString(), String.valueOf(sp_jins.getSelectedItemPosition() - 1), mother_age, et_tareekh_pedaish.getText().toString(),
                                        "bio_code", qr_code_text, login_useruid, String.valueOf(jsonObject), added_on);
                            } else {
                               // Toast.makeText(ctx, "ڈیٹا اپڈیٹ ہوگیا ہے", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            final Snackbar snackbar = Snackbar.make(v, "پروفائل ڈیٹا اپڈیٹ نہیں ہوا.", Snackbar.LENGTH_SHORT);
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
                        Log.d("000123", " Error" + e.getMessage());
                        // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    } finally {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog.dismiss();
                                finish();
                                VAC_Register_QRCode_Activity.switch_qr_code_values_vac = "0";
                                var_updateprofile_abovetwotemp = "1";
                            }
                        },2500);
                    }

                }
            }, 2000);

        } catch (Exception e) {
            alertDialog.dismiss();
            //  Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
            Log.d("000123", " Error" + e.getMessage());
        }


    }


    private void SpinnersData() {

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

             /*   if (sp_azdawaji_hasiyat.isEnabled() == false) {
                    ((TextView) parent.getChildAt(0)).setTextColor(ctx.getResources().getColor(R.color.grey_color));
                } else {
                    ((TextView) parent.getChildAt(0)).setTextColor(ctx.getResources().getColor(R.color.dark_blue_color));
                }*/
                if (position == 1) {
                    ll_husband_fields.setVisibility(View.VISIBLE);
                } else {
                    ll_husband_fields.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Select sp_motherStatus
        final ArrayAdapter<CharSequence> adptr_wajah_wafat = ArrayAdapter.createFromResource(this, R.array.array_mother_daeth_reason_urdu, android.R.layout.simple_spinner_item);
        adptr_wajah_wafat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        sp_wajah_wafat.setAdapter(
                new com.akdndhrc.covid_module.CustomClass.NothingSelectedSpinnerAdapter(
                        adptr_wajah_wafat,
                        R.layout.spinner_azdawaji_hasiyat_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));


        sp_wajah_wafat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 7) {

                    ll_wajah_wafat_likhey.setVisibility(View.VISIBLE);
                } else {
                    ll_wajah_wafat_likhey.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void sendPostRequest_khandan(final String manual_id, final String uid, final String province_id, final String district_id,
                                         final String subdistrict_id, final String uc_id, final String village_id, final String family_head_name,
                                         final String family_address, final String water_source, final String toilet_facility,
                                         final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/family/";


        Log.d("000123", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {


                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000123", "Khandan response    " + response);

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();
                        String update_record = "UPDATE KHANDAN SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE uid = '" + uid + "'";
                        ls.executeNonQuery(update_record);


                    } else {
                        alertDialog.dismiss();
                        Log.d("000123", "else ");
                        Toast.makeText(ctx, "ڈیٹا سروس پر سینک نہیں ہوا", Toast.LENGTH_SHORT).show();
                        // Toast.makeText(ctx, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {

                    Toast.makeText(ctx, "ڈیٹا سینک نہیں ہوا", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alertDialog.dismiss();
                Log.d("000123", "error    " + error.getMessage());
                // Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast.makeText(ctx, "ڈیٹا سینک نہیں ہوا", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("manual_id", manual_id);
                params.put("uid", uid);
                params.put("province_id", province_id);
                params.put("country_id", "0092");
                params.put("district_id", district_id);
                params.put("tehsil_id", subdistrict_id);
                params.put("uc_id", uc_id);
                params.put("village_id", village_id);
                params.put("family_head_name", family_head_name);
                params.put("family_address", family_address);
                params.put("water_source", water_source);
                params.put("toilet_facility", toilet_facility);
                params.put("added_by", added_by);
                params.put("added_on", added_on);

                Log.d("000123", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000123", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }


    private void sendPostRequest(final String manual_id, final String uid, final String khandan_id, final String full_name,
                                 final String nicnumber, final String phone_number, final String gender, final String age, final String dob,
                                 final String bio_code, final String qr_code, final String added_by,
                                 final String data, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/family/members";
//        String url = "https://development.api.teekoplus.akdndhrc.org/sync-adapter";

        Log.d("000123", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {
                    //Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000123", "response:    " + response);

                        // Toast.makeText(VAC_BelowInsideOutsideUC_Activity.this, "Data has been saved", Toast.LENGTH_SHORT).show();

                        Lister ls = new Lister(VAC_AboveTwoProfile_Activity.this);
                        ls.createAndOpenDB();
                        String update_record = "UPDATE MEMBER SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE uid = '" + mother_uid + "'";
                        ls.executeNonQuery(update_record);

                        Toast tt  =Toast.makeText(ctx, "ڈیٹا سنک ہوگیا ہے", Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();

                    } else {
                        Log.d("000123", "else ");
                        Toast.makeText(ctx, "ڈیٹا سروس پر سینک نہیں ہوا", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(VAC_BelowInsideOutsideUC_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000123", "Err: " + e.getMessage());
                    // Toast.makeText(VAC_BelowInsideOutsideUC_Activity.this, "Data has been sent incorrectly.", Toast.LENGTH_SHORT).show();
                    Toast tt  =Toast.makeText(ctx, "ڈیٹا سینک نہیں ہوا", Toast.LENGTH_SHORT);
                    tt.setGravity(Gravity.CENTER, 0, 0);
                    tt.show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000123", "error    " + error.getMessage());
                // Toast.makeText(VAC_BelowInsideOutsideUC_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast tt  =Toast.makeText(ctx, "ڈیٹا سینک نہیں ہوا", Toast.LENGTH_SHORT);
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

                Log.d("000123", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000123", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }

    public void ShowDialog() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(ctx, R.style.DatePickerDialog,
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
                        et_tareekh_pedaish.setText(yearf2 + "-" + monthf2 + "-" + dayf2);


                        Calendar dob = Calendar.getInstance();
                        Calendar today = Calendar.getInstance();

                        dob.set(year, monthOfYear, dayOfMonth);

                        mother_age = String.valueOf(today.get(Calendar.YEAR) - year);
                        Log.d("000123", "Age: " + mother_age);

                        Integer ageInt = new Integer(mother_age);
                        String ageS = ageInt.toString();
                        //Toast.makeText(getApplicationContext(),String.valueOf(year)+"major" + ageS +"age",Toast.LENGTH_SHORT).show();
//                        et_tareekh_pedaish.setText(ageS);
                        //Toast.makeText(getContext(),DateTwoOneval,Toast.LENGTH_LONG).show();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();


    }

    public void ShowTareekhWafatDialog() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(ctx, R.style.DatePickerDialog,
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
                        et_tareekh_wafaat.setText(yearf2 + "-" + monthf2 + "-" + dayf2);
                        temp_tareekh_wafat = "1";
                        Log.d("000555", "temp_tareekh_wafat: " + temp_tareekh_wafat);

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

    public void read_profile() {
        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();


            mDatamother = ls.executeReader("Select uid,full_name, dob,gender,nicnumber,phone_number,data,khandan_id,added_on,qr_code,age from MEMBER where uid = '" + mother_uid + "'");

            khandan_id = mDatamother[0][7];
            added_on = mDatamother[0][8];
            mother_age = mDatamother[0][10];

            Log.d("000123", "uid: " + mDatamother[0][0]);
            Log.d("000123", "FullName: " + mDatamother[0][1]);
            Log.d("000123", "DOB: " + mDatamother[0][2]);
            Log.d("000123", "Gender: " + mDatamother[0][3]);
            Log.d("000123", "NicNumber: " + mDatamother[0][4]);
            Log.d("000123", "PhoneNumber: " + mDatamother[0][5]);
            Log.d("000123", "JSON Data: " + mDatamother[0][6]);
            Log.d("000123", "khandan_id: " + mDatamother[0][7]);
            Log.d("000123", "added_on: " + mDatamother[0][8]);
            Log.d("000123", "qr_code: " + mDatamother[0][9]);
            Log.d("000123", "age: " + mDatamother[0][10]);


            et_bachey_ka_naam.setEnabled(false);
            et_tareekh_pedaish.setEnabled(false);
            sp_jins.setEnabled(false);
            et_walid_ka_naam.setEnabled(false);
            et_walida_ka_naam.setEnabled(false);
            et_walid_ka_shanakti_card_number.setEnabled(false);
            et_walid_ka_mobile_number.setEnabled(false);
            sp_azdawaji_hasiyat.setEnabled(false);
            et_shohar_ka_naam.setEnabled(false);
            et_shohar_ka_cnic_number.setEnabled(false);
            et_shohar_ka_mobile_number.setEnabled(false);
            et_address.setEnabled(false);
            et_vacination_card_number.setEnabled(false);
            sw_qrcode_faal_kre.setEnabled(false);

            sw_mother_status.setEnabled(false);
            sp_wajah_wafat.setEnabled(false);
            et_tareekh_wafaat.setEnabled(false);
            et_tabsarah_wajah_wafaat.setEnabled(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image_arrow_down_jins.setImageTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                image_arrow_down_azdawaji.setImageTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                image_arrow_down_wafat.setImageTintList(ctx.getResources().getColorStateList(R.color.grey_color));
            }

            String json = mDatamother[0][6];

            jsonObject = new JSONObject(json);

            et_bachey_ka_naam.setText(mDatamother[0][1]);
            et_tareekh_pedaish.setText(mDatamother[0][2]);
            sp_jins.setSelection(Integer.parseInt(mDatamother[0][3]) + 1);

            //  et_walid_ka_naam.setText(jsonObject.getString("father_name"));

            if (jsonObject.getString("father_name").equalsIgnoreCase("")) {
                et_walid_ka_naam.setText("");
            } else {
                et_walid_ka_naam.setText(jsonObject.getString("father_name"));
            }

            if (jsonObject.has("mother_name")) {
                if (jsonObject.getString("mother_name").equalsIgnoreCase("")) {
                    et_walida_ka_naam.setText("");
                } else {
                    et_walida_ka_naam.setText(jsonObject.getString("mother_name"));
                }
            } else {
                et_walida_ka_naam.setText("");
            }

            if (mDatamother[0][4].isEmpty()) {
                Log.d("000123", "NIC IFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
                et_walid_ka_shanakti_card_number.setText("");
            } else {
                Log.d("000123", "NIC ELSEEEEEEEEEEEEE");
                et_walid_ka_shanakti_card_number.setText(mDatamother[0][4]);
            }

            if (mDatamother[0][5].isEmpty()) {
                Log.d("000123", "MOBILE IFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
                et_walid_ka_mobile_number.setText("");
            } else {
                Log.d("000123", "M<OBILEEEEEEEEEEEEEEE ELSEEEEEEEEEEEEE");
                et_walid_ka_mobile_number.setText(mDatamother[0][5]);
            }

            try {
                sp_azdawaji_hasiyat.setSelection(Integer.parseInt(jsonObject.getString("maritalstatus")) + 1);
                if (jsonObject.getString("maritalstatus").equalsIgnoreCase("0")) {
                    ll_husband_fields.setVisibility(View.VISIBLE);

                    if (jsonObject.has("husband_name")) {
                        if (jsonObject.getString("husband_name").equalsIgnoreCase("")) {
                            et_shohar_ka_naam.setText("");
                        } else {
                            et_shohar_ka_naam.setText(jsonObject.getString("husband_name"));
                        }
                    } else {
                        et_shohar_ka_naam.setText("");
                    }

                    if (jsonObject.has("husband_cnic_number")) {
                        if (jsonObject.getString("husband_cnic_number").equalsIgnoreCase("")) {
                            et_shohar_ka_cnic_number.setText("");
                        } else {
                            et_shohar_ka_cnic_number.setText(jsonObject.getString("husband_cnic_number"));
                        }
                    } else {
                        et_shohar_ka_cnic_number.setText("");
                    }

                    if (jsonObject.has("husband_mobile_number")) {
                        if (jsonObject.getString("husband_mobile_number").equalsIgnoreCase("")) {
                            et_shohar_ka_mobile_number.setText("");
                        } else {
                            et_shohar_ka_mobile_number.setText(jsonObject.getString("husband_mobile_number"));
                        }
                    } else {
                        et_shohar_ka_mobile_number.setText("");
                    }
                } else {
                    ll_husband_fields.setVisibility(View.GONE);
                }

            } catch (Exception e1) {
                Log.d("000123", "sp Error: " + e1.getMessage());
            }


            if (jsonObject.has("vaination_card_number")) {
                et_vacination_card_number.setText(jsonObject.getString("vaination_card_number"));
            } else if (jsonObject.has("vaccination_card_number")) {
                et_vacination_card_number.setText(jsonObject.getString("vaccination_card_number"));
            } else {
                et_vacination_card_number.setText("");
            }

            if (jsonObject.has("address")) {
                et_address.setText(jsonObject.getString("address"));
            } else {
                et_address.setText("");
            }

            if (Integer.parseInt(mDatamother[0][10]) <= 14) {
                ll_vacination_card_number.setVisibility(View.VISIBLE);
                if (jsonObject.has("vaination_card_number")) {
                    et_vacination_card_number.setText(jsonObject.getString("vaination_card_number"));
                } else if (jsonObject.has("vaccination_card_number")) {
                    et_vacination_card_number.setText(jsonObject.getString("vaccination_card_number"));
                } else {
                    et_vacination_card_number.setText("");
                }
            } else {
                ll_vacination_card_number.setVisibility(View.GONE);
            }

            if (mDatamother[0][9].equalsIgnoreCase("no_qr_code")) {
                Log.d("000123", "QR IF");
                sw_qrcode_faal_kre.setChecked(false);
            } else if (mDatamother[0][9].equalsIgnoreCase("none")) {
                Log.d("000123", "QR ELSE IF");

                sw_qrcode_faal_kre.setChecked(false);
            } else {
                Log.d("000123", "QR ELSE");

                sw_qrcode_faal_kre.setChecked(true);
            }

            if (jsonObject.getString("date_of_death").equalsIgnoreCase("0") || jsonObject.getString("date_of_death").isEmpty()) {
                    sw_mother_status.setChecked(true);
                    ll_wafat.setVisibility(View.GONE);
                    mother_status="0";

                } else {
                    sw_mother_status.setChecked(false);
                    ll_wafat.setVisibility(View.VISIBLE);
                    et_tareekh_wafaat.setText(jsonObject.getString("date_of_death"));
                    sp_wajah_wafat.setSelection(Integer.parseInt(jsonObject.getString("sp_reason_of_death_pos")) + 1);
                    if (jsonObject.getString("reason_of_death").isEmpty())
                    {
                        et_tabsarah_wajah_wafaat.setText("-");
                    }
                    else{
                        et_tabsarah_wajah_wafaat.setText(jsonObject.getString("reason_of_death"));
                    }
                    mother_status="1";
            }

            ls.closeDB();

        } catch (Exception e) {
            e.printStackTrace();

            Log.d("000123", "Error: " + e.getMessage());
            Toast.makeText(ctx, "Something Wrong!!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (VAC_Register_QRCode_Activity.switch_qr_code_values_vac.equalsIgnoreCase("1")) {

            sw_qrcode_faal_kre.setChecked(true);
            sw_qrcode_faal_kre.setEnabled(false);

            Log.d("000123", "IF");
        } else {

            Log.d("000123", "ELSE: ");
            if (mDatamother[0][9].equalsIgnoreCase("no_qr_code")) {
                Log.d("000123", "QR IF");

                sw_qrcode_faal_kre.setChecked(false);
            } else if (mDatamother[0][9].equalsIgnoreCase("none")) {
                Log.d("000123", "QR ELSE IF");

                sw_qrcode_faal_kre.setChecked(false);
            } else {
                Log.d("000123", "QR ELSE");
                sw_qrcode_faal_kre.setChecked(true);
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        VAC_Register_QRCode_Activity.switch_qr_code_values_vac = "0";
        var_updateprofile_abovetwotemp = "0";
    }
}