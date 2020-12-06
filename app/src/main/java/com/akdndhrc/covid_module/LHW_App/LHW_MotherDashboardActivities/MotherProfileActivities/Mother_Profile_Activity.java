package com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherProfileActivities;

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

import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.CustomClass.NothingSelectedSpinnerAdapter;
import com.akdndhrc.covid_module.CustomClass.UrlClass;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_RegisterActivities.Register_QRCode_Activity;
import com.akdndhrc.covid_module.ServiceLocation;
import com.akdndhrc.covid_module.Utils;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.Mother_Dashboard_Activity;
import com.akdndhrc.covid_module.R;
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


public class Mother_Profile_Activity extends AppCompatActivity {

    Context ctx = Mother_Profile_Activity.this;

    Spinner sp_azdawaji_hasiyat, sp_jins, sp_wajah_wafat;
    EditText et_naam_bimaah_waldiyat, et_walid_ka_naam, et_walida_ka_naam, et_tareekh_indraj, et_sarbarah_kay_sath_rishta, et_tareekh_pedaish, et_shanakhti_card_number,
            et_mobile_number, et_tareekh_nakal_makani, et_tareekh_wafaat, et_tabsarah_wajah_wafaat, et_khandan_number, et_infradi, et_tabsarah_wajah_trasnfer, et_umer;

    Switch sw_biometric_faal_kre, sw_qrcode_faal_kre, sw_mother_status;
    Button btn_jamaa_kre;
    ImageView iv_navigation_drawer, iv_home, iv_editprofile;
    String mother_uid, TodayDate, khandan_id, added_on;

    ProgressBar pbProgress;
    JSONObject jsonObject;
    Dialog alertDialog;

    private int mYear, mMonth, mDay;
    int date_for_condition = 0;
    int month_for_condition = 0;
    String monthf2, dayf2, yearf2 = "null";
    public String hold_age_date_condition = "fromage";
    double latitude;
    double longitude;

    ServiceLocation serviceLocation;
    String temp_tareekh_wafat = "0";
    String temp_tareekh_nakalmakani = "0";
    String login_useruid, mother_status;
    public static String var_mother_pro = "0";
    int age;
    ImageView image_arrow_down_azdawaji, image_arrow_down_jins, image_arrow_down_wafat;
    LinearLayout ll_wafat, ll_wajah_wafat_likhey;
    String khandan_number;

    String qr_code_text = "none";
    long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_profile);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Mother_Profile_Activity.class));


        var_mother_pro = "0";

        mother_uid = getIntent().getExtras().getString("u_id");


        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
            String shared_useruid = prefelse.getString((String.valueOf(R.string.loginUserIDEng)), null); // getting String
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


        //Spinner
       // sp_azdawaji_hasiyat = findViewById(R.id.sp_azdawaji_hasiyat);
        sp_jins = findViewById(R.id.sp_jins);
        sp_wajah_wafat = findViewById(R.id.sp_wajah_wafat);

        //EditText
        et_naam_bimaah_waldiyat = findViewById(R.id.et_naam_bimaah_waldiyat);
        et_walid_ka_naam = findViewById(R.id.et_walid_ka_naam);
        et_walida_ka_naam = findViewById(R.id.et_walida_ka_naam);
        et_tareekh_indraj = findViewById(R.id.et_tareekh_indraj);
        et_tareekh_pedaish = findViewById(R.id.et_tareekh_pedaish);
        et_sarbarah_kay_sath_rishta = findViewById(R.id.et_sarbarah_kay_sath_rishta);
        et_shanakhti_card_number = findViewById(R.id.et_shanakhti_card_number);
        et_umer = findViewById(R.id.et_age);
        et_mobile_number = findViewById(R.id.et_mobile_number);
        et_tareekh_nakal_makani = findViewById(R.id.et_tareekh_nakal_makani);
        et_tareekh_wafaat = findViewById(R.id.et_tareekh_wafaat);
        et_tabsarah_wajah_wafaat = findViewById(R.id.et_tabsarah_wajah_wafaat);
        et_tabsarah_wajah_trasnfer = findViewById(R.id.et_tabsarah_wajah_trasnfer);
        et_khandan_number = findViewById(R.id.et_khandan_number);
        et_infradi = findViewById(R.id.et_infradi);

        //Switch
        sw_biometric_faal_kre = findViewById(R.id.sw_biometric_faal_kre);
        sw_qrcode_faal_kre = findViewById(R.id.sw_qrcode_faal_kre);
        sw_mother_status = findViewById(R.id.sw_mother_status);


        sw_qrcode_faal_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, Register_QRCode_Activity.class);
                intent.putExtra("class_id", "0");
                startActivity(intent);
            }
        });


        //Buttton
        btn_jamaa_kre = findViewById(R.id.submit);
        btn_jamaa_kre.setVisibility(View.GONE);

        //Linear layout
        ll_wafat = findViewById(R.id.ll_wafat);
        ll_wajah_wafat_likhey = findViewById(R.id.ll_wajah_wafat_likhey);


        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_navigation_drawer.setVisibility(View.GONE);

        iv_home = findViewById(R.id.iv_home);
        iv_editprofile = findViewById(R.id.iv_editprofile);
        image_arrow_down_jins = findViewById(R.id.image_arrow_down_jins);
        image_arrow_down_azdawaji = findViewById(R.id.image_arrow_down_azdawaji);
        image_arrow_down_wafat = findViewById(R.id.image_arrow_down_wafat);
        //iv_home.setVisibility(View.GONE);

        //Progress
        pbProgress = findViewById(R.id.pbProgress);


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
                var_mother_pro = "0";
            }
        });


        et_tareekh_pedaish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowTareekhPedaishDialog();
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
        et_tareekh_nakal_makani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (temp_tareekh_nakalmakani.equalsIgnoreCase("0")) {
                    ShowTareekhNakalMakaniDialog();
                } else {
                    et_tareekh_nakal_makani.getText().clear();
                    ShowTareekhNakalMakaniDialog();
                }
            }
        });


        sw_mother_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll_wafat.setVisibility(View.GONE);
                    mother_status = "0";
                } else {
                    ll_wafat.setVisibility(View.VISIBLE);
                    mother_status = "1";
                }
            }
        });

        //Edit Person CNIC
        et_shanakhti_card_number.addTextChangedListener(new TextWatcher() {
            int prev = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prev = et_shanakhti_card_number.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if ((prev < length) && (length == 5 || length == 13)) {
                    String data = et_shanakhti_card_number.getText().toString();
                    et_shanakhti_card_number.setText(data + "-");
                    et_shanakhti_card_number.setSelection(length + 1);
                }
            }
        });

        ///Mobile Number
        et_mobile_number.addTextChangedListener(new TextWatcher() {
            int previous = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previous = et_mobile_number.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if ((previous < length) && (length == 4)) {
                    String data = et_mobile_number.getText().toString();
                    et_mobile_number.setText(data + "-");
                    et_mobile_number.setSelection(length + 1);
                }
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


                        et_naam_bimaah_waldiyat.setEnabled(true);
                        et_tareekh_pedaish.setEnabled(true);
                        et_walid_ka_naam.setEnabled(true);
                        et_walida_ka_naam.setEnabled(true);
                        et_sarbarah_kay_sath_rishta.setEnabled(true);

                        et_shanakhti_card_number.setEnabled(true);
                     //   sp_azdawaji_hasiyat.setEnabled(true);
                        sp_jins.setEnabled(true);
                        et_khandan_number.setEnabled(true);

                        et_mobile_number.setEnabled(true);
                        et_tareekh_nakal_makani.setEnabled(true);

                        et_tabsarah_wajah_trasnfer.setEnabled(true);

                        et_infradi.setEnabled(true);

                        if (qr_code_text.equalsIgnoreCase("no_qr_code")) {
                            sw_qrcode_faal_kre.setEnabled(true);
                        } else if (qr_code_text.equalsIgnoreCase("none")) {
                            sw_qrcode_faal_kre.setEnabled(true);
                        } else {
                            sw_qrcode_faal_kre.setEnabled(false);
                        }



                        if (mother_status.equalsIgnoreCase("1")) {
                            sw_mother_status.setEnabled(false);
                            sp_wajah_wafat.setEnabled(false);
                            et_tareekh_wafaat.setEnabled(false);
                            et_tabsarah_wajah_wafaat.setEnabled(false);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                image_arrow_down_wafat.setImageTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                            }
                        } else {
                            sw_mother_status.setEnabled(true);
                            sp_wajah_wafat.setEnabled(true);
                            et_tareekh_wafaat.setEnabled(true);
                            et_tabsarah_wajah_wafaat.setEnabled(true);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                image_arrow_down_wafat.setImageTintList(ctx.getResources().getColorStateList(R.color.dark_blue_color));
                            }
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            image_arrow_down_azdawaji.setImageTintList(ctx.getResources().getColorStateList(R.color.dark_blue_color));
                            image_arrow_down_jins.setImageTintList(ctx.getResources().getColorStateList(R.color.dark_blue_color));

                        }
                        btn_jamaa_kre.setVisibility(View.VISIBLE);
                        pbProgress.setVisibility(View.GONE);
                        iv_editprofile.setVisibility(View.GONE);


                    }
                }, 2500);


            }
        });

        // Select sp_jins
        // Select sp_gender
        final ArrayAdapter<CharSequence> adptr_jins = ArrayAdapter.createFromResource(this, R.array.array_gender, android.R.layout.simple_spinner_item);
        adptr_jins.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        sp_jins.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_jins,
                        R.layout.spinner_azdawaji_hasiyat_layout,
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

      /*  // Select sp_azdawaji_hasiyat
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
        // Select sp_motherStatus
        final ArrayAdapter<CharSequence> adptr_wajah_wafat = ArrayAdapter.createFromResource(this, R.array.array_mother_daeth_reason_urdu, android.R.layout.simple_spinner_item);
        adptr_wajah_wafat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        sp_wajah_wafat.setAdapter(
                new NothingSelectedSpinnerAdapter(
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


        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_naam_bimaah_waldiyat.getText().toString().isEmpty()) {

                    //Toast.makeText(getApplicationContext(), "برائے مہربانی نام بمعہ ولدیت درج کریں", Toast.LENGTH_LONG).show();
                    final Snackbar snackbar = Snackbar.make(v, R.string.writeNamePrompt, Snackbar.LENGTH_SHORT);
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
                    final Snackbar snackbar = Snackbar.make(v, R.string.writeFatherNamePrompt, Snackbar.LENGTH_SHORT);
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
                if (et_tareekh_pedaish.getText().toString().length() < 1) {

                    //Toast.makeText(getApplicationContext(), "برائے مہربانی تاریخ پیدائش منتخب کریں", Toast.LENGTH_LONG).show();
                    final Snackbar snackbar = Snackbar.make(v, R.string.selectDOBprompt, Snackbar.LENGTH_SHORT);
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
                    final Snackbar snackbar = Snackbar.make(v, R.string.selectGenderPrompt, Snackbar.LENGTH_SHORT);
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


                if (age <= 2) {
                    //   Toast.makeText(getApplicationContext(), "منتخب عمر کو دو سال سے زائد ہونا ضروری ہے", Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar.make(v, R.string.selectedAgeGreater2Prompt, Snackbar.LENGTH_SHORT);
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

                if (et_khandan_number.getText().toString().isEmpty()) {
                    final Snackbar snackbar = Snackbar.make(v, R.string.writeFamilyNumPrompt, Snackbar.LENGTH_SHORT);
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


                if (mother_status.equalsIgnoreCase("1")) {

                    if (et_tareekh_wafaat.getText().toString().length() < 1) {

                        //Toast.makeText(getApplicationContext(), "برائے مہربانی تاریخ پیدائش منتخب کریں", Toast.LENGTH_LONG).show();
                        final Snackbar snackbar = Snackbar.make(v, R.string.selectDeathDatePrompt, Snackbar.LENGTH_SHORT);
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
                        final Snackbar snackbar = Snackbar.make(v, R.string.selectDeathReasonPrompt, Snackbar.LENGTH_SHORT);
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

                    if (sp_wajah_wafat.getSelectedItemPosition() == 6) {
                        if (et_tabsarah_wajah_wafaat.getText().toString().isEmpty()) {

                            //Toast.makeText(getApplicationContext(), "برائے مہربانی تاریخ پیدائش منتخب کریں", Toast.LENGTH_LONG).show();
                            final Snackbar snackbar = Snackbar.make(v, R.string.writeDeathReasonPrompt, Snackbar.LENGTH_SHORT);
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
                    } else {
                    }
                } else {
                    Log.d("000555", " Wajah Wafat ElSeeeeeeeeeeeee: ");
                }


                if (serviceLocation.showCurrentLocation() == true) {

                    latitude = serviceLocation.getLatitude();
                    longitude = serviceLocation.getLongitude();

                    Log.d("000555", " latitude: " + latitude);
                    Log.d("000555", " longitude: " + longitude);
                } else {
                    try {
                        serviceLocation.doAsynchronousTask.cancel();
                    } catch (Exception e) {
                    }
                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from MEMBER");

                        if (Integer.parseInt(mData[0][2]) > 0) {
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

                        Log.d("000258", "Read Member Error Catch: " + e.getMessage());
                    }
                }

                btn_jamaa_kre.setVisibility(View.GONE);
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Log.d("000555", " mLastClickTime: " + mLastClickTime);



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

        if (Register_QRCode_Activity.switch_qr_code_values.equalsIgnoreCase("1")) {

            SharedPreferences settings = getSharedPreferences(getString(R.string.shared_QR_Value), MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            // Reading from SharedPreferences
            qr_code_text = settings.getString("qr_code", "");
            editor.apply();
            Log.d("000123", "IF: " + qr_code_text);

        } else {
            qr_code_text = "none";
            Log.d("000123", "ELSE: " + qr_code_text);
        }



        //Khandan Update
        if (et_khandan_number.getText().toString().equals(khandan_number)) {
            Log.d("000555", "KHANDAN NUMBER SAMEE !!!!!!!!!");
        } else {
            Log.d("000555", "KHANDAN NUMBER DIFFERENT !!!!!!!!!");
            try {
                Lister ls = new Lister(ctx);
                ls.createAndOpenDB();

                String update_record = "UPDATE KHANDAN SET " +
                        "manual_id='" + et_khandan_number.getText().toString() + "'," +
                        "is_synced='" + 0 + "'" +
                        "WHERE uid = '" + khandan_id + "'";
                ls.executeNonQuery(update_record);

                final Snackbar snackbar = Snackbar.make(v, R.string.familyNumChanged, Snackbar.LENGTH_SHORT);
                View mySbView = snackbar.getView();
                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(15);
                snackbar.setDuration(2000);
                snackbar.show();

                Boolean res = ls.executeNonQuery(update_record);
                Log.d("000555", "Khand Data: " + update_record);
                Log.d("000555", "Khandan Query: " + res.toString());


            } catch (Exception e) {
                Log.d("000555", " Error" + e.getMessage());
            }
        }

        try {
            String cur_added_on = String.valueOf(System.currentTimeMillis());

            if (jsonObject.has("lat")) {
                jsonObject.put("lat", "" + String.valueOf(latitude));
                jsonObject.put("lng", "" + String.valueOf(longitude));
                jsonObject.put("status_death", "" + mother_status);
                jsonObject.put("member_type", "" + "abovetwo");
                jsonObject.put("full_name", "" + et_naam_bimaah_waldiyat.getText().toString());
                jsonObject.put("father_name", "" + et_walid_ka_naam.getText().toString());
                jsonObject.put("mother_name", "" + et_walida_ka_naam.getText().toString());
                jsonObject.put("relation_with_hof", "" + et_sarbarah_kay_sath_rishta.getText().toString());
                jsonObject.put("nic", "" + et_shanakhti_card_number.getText().toString());
                jsonObject.put("dob", "" + et_tareekh_pedaish.getText().toString());
                jsonObject.put("age", "" + et_umer.getText().toString());
              //  jsonObject.put("maritalstatus", "" + String.valueOf(sp_azdawaji_hasiyat.getSelectedItemPosition() - 1));//spinner
                jsonObject.put("mobile", "" + et_mobile_number.getText().toString());
                jsonObject.put("gender", "" + String.valueOf(sp_jins.getSelectedItemPosition() - 1));
                jsonObject.put("date_of_transfer", "" + et_tareekh_nakal_makani.getText().toString());
                jsonObject.put("reason_of_transfer", "" + et_tabsarah_wajah_trasnfer.getText().toString());
                jsonObject.put("date_of_death", "" + et_tareekh_wafaat.getText().toString());
                jsonObject.put("sp_reason_of_death", "" + String.valueOf(sp_wajah_wafat.getSelectedItem()));//spinner
                jsonObject.put("sp_reason_of_death_pos", "" + String.valueOf(sp_wajah_wafat.getSelectedItemPosition() - 1));//spinner
                jsonObject.put("reason_of_death", "" + et_tabsarah_wajah_wafaat.getText().toString());
                jsonObject.put("khandan_number_manual", "" + et_khandan_number.getText().toString());
                jsonObject.put("manual_id", "" + et_infradi.getText().toString());
                jsonObject.put("record_date", "" + TodayDate);
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
                                "full_name='" + et_naam_bimaah_waldiyat.getText().toString() + "'," +
                                "nicnumber='" + et_shanakhti_card_number.getText().toString() + "'," +
                                "phone_number='" + et_mobile_number.getText().toString() + "'," +
                                "dob='" + et_tareekh_pedaish.getText().toString() + "'," +
                                "gender='" + String.valueOf(sp_jins.getSelectedItemPosition() - 1) + "'," +
                                "age='" + et_umer.getText().toString() + "'," +
                                "data='" + jsonObject.toString() + "'," +
                                "qr_code='" + qr_code_text + "'," +
                                "is_synced='" + 0 + "'" +
                                "WHERE uid = '" + mother_uid + "'";

                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d("000555", "Data: " + update_record);
                        Log.d("000555", "Query: " + res.toString());

                        if (res.toString().equalsIgnoreCase("true"))
                        {

                            final Snackbar snackbar = Snackbar.make(v, R.string.profileDataUpdated, Snackbar.LENGTH_SHORT);
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

                                sendPostRequest(et_infradi.getText().toString(), mother_uid, khandan_id, et_naam_bimaah_waldiyat.getText().toString(), et_shanakhti_card_number.getText().toString(),
                                        et_mobile_number.getText().toString(), String.valueOf(sp_jins.getSelectedItemPosition() - 1), et_umer.getText().toString(), et_tareekh_pedaish.getText().toString(),
                                        "bio_code", qr_code_text, login_useruid, String.valueOf(jsonObject), added_on);
                            } else {
                              //  Toast.makeText(ctx, R.string.dataEdited, Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            final Snackbar snackbar = Snackbar.make(v, R.string.profileDataNotUpdated, Snackbar.LENGTH_SHORT);
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




                    } catch (Exception e) {
                        alertDialog.dismiss();
                        Log.d("000555", " Error" + e.getMessage());
                        // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    } finally {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog.dismiss();
                                    Intent intent = new Intent(ctx, Mother_Dashboard_Activity.class);
                                    intent.putExtra("u_id", mother_uid);
                                    startActivity(intent);
                                    var_mother_pro = "1";
                            }
                        },2500);


                    }

                }
            }, 3000);

        } catch (Exception e) {
            alertDialog.dismiss();
            //  Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
            Log.d("000555", " Error" + e.getMessage());
        }


    }

    private void sendPostRequest(final String manual_id, final String uid, final String khandan_id, final String full_name, final String nicnumber, final String phone_number, final String gender, final String age,
                                 final String dob, final String bio_code, final String qr_code, final String added_by, final String data, final String added_on) {

        // String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/family/members";
        String url = UrlClass.update_member_url;

        Log.d("000555", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

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
                                "WHERE uid = '" + mother_uid + "'";

                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d("000555", "Updated Data: " + update_record);
                        Log.d("000555", "Updated Query: " + res.toString());

                        Toast tt  =Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();

                    } else {
                        Log.d("000555", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        // Toast.makeText(ctx, "Data has not been updated to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", "catch:   " + e.getMessage());
                    // Toast.makeText(ctx, "Data has been sent updated.", Toast.LENGTH_SHORT).show();
                    Toast tt  =Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT);
                    tt.setGravity(Gravity.CENTER, 0, 0);
                    tt.show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "onErrorResponse: " + error.getMessage());
                // Toast.makeText(Mother_Profile_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast tt  =Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT);
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

    public void ShowTareekhPedaishDialog() {

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
                            Log.d("000321", "IFF Org Month: " + monthOfYear);
                            Log.d("000321", "IFF Selected Month: " + monthf2);

                        } else {
                            monthf2 = String.valueOf(monthOfYear + 1);
                            Log.d("000321", "ESLS Org Month: " + monthOfYear);
                            Log.d("000321", "ESLS Selected Month: " + monthf2);
                        }
                        if (dayOfMonth <= 9) {

                            dayf2 = "0" + String.valueOf(dayOfMonth);
                            Log.d("000321", "IFF Org Day: " + dayf2);

                        } else {
                            dayf2 = String.valueOf(dayOfMonth);
                            Log.d("000321", "ELSEE Org Day: " + dayf2);
                        }

                        yearf2 = String.valueOf(year);
                        Log.d("000321", "Year Org : " + yearf2);
                        //datetwo.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        // DateTwoOne= "newvaladded";
                        //et_dob.setText(dayf2 + "-" + monthf2 + "-" + yearf2);
                        et_tareekh_pedaish.setText(yearf2 + "-" + monthf2 + "-" + dayf2);


                        Calendar dob = Calendar.getInstance();
                        Calendar today = Calendar.getInstance();

                        dob.set(year, monthOfYear, dayOfMonth);

                        age = today.get(Calendar.YEAR) - year;

                        if (age >= 3 && age <= 14) {

                            Log.d("000321", "IFF : " );
// Sp gender
                            final ArrayAdapter<CharSequence> adptr_gender = ArrayAdapter.createFromResource(ctx, R.array.array_child_gender, R.layout.temp);
                            adptr_gender.setDropDownViewResource(R.layout.temp);

                            sp_jins.setAdapter(
                                    new com.akdndhrc.covid_module.NothingSelectedSpinnerAdapter(
                                            adptr_gender,
                                            R.layout.temp,
                                            ctx));


                            sp_jins.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                        else {
                            Log.d("000321", "ELSE : " );
                            final ArrayAdapter<CharSequence> adptr_gender = ArrayAdapter.createFromResource(ctx, R.array.array_gender, R.layout.temp);
                            adptr_gender.setDropDownViewResource(R.layout.temp);

                            sp_jins.setAdapter(
                                    new com.akdndhrc.covid_module.NothingSelectedSpinnerAdapter(
                                            adptr_gender,
                                            R.layout.temp,
                                            ctx));


                            sp_jins.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }

                        Integer ageInt = new Integer(age);
                        String ageS = ageInt.toString();
                        et_umer.setText(ageS);

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

    public void ShowTareekhNakalMakaniDialog() {

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
                        et_tareekh_nakal_makani.setText(yearf2 + "-" + monthf2 + "-" + dayf2);
                        temp_tareekh_nakalmakani = "1";
                        Log.d("000555", "temp_tareekh_nakalmakani: " + temp_tareekh_nakalmakani);

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

    @Override
    protected void onResume() {
        super.onResume();

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String mData[][] = ls.executeReader("Select data ,khandan_id,added_on,gender,age,phone_number,qr_code from MEMBER where uid = '" + mother_uid + "'");

            Log.d("000555", "Data:  " + mData[0][0]);

            String json = mData[0][0];
            jsonObject = new JSONObject(json);

            khandan_id = mData[0][1];
            added_on = mData[0][2];
            age = Integer.parseInt(mData[0][4]);
            qr_code_text = mData[0][6];

            //EditText
            et_naam_bimaah_waldiyat.setEnabled(false);
            et_walid_ka_naam.setEnabled(false);
            et_walida_ka_naam.setEnabled(false);
            et_sarbarah_kay_sath_rishta.setEnabled(false);

            et_tareekh_pedaish.setEnabled(false);
            et_umer.setEnabled(false);
            et_shanakhti_card_number.setEnabled(false);
            sp_azdawaji_hasiyat.setEnabled(false);
            sp_jins.setEnabled(false);

            et_mobile_number.setEnabled(false);
            et_tareekh_nakal_makani.setEnabled(false);
            et_tareekh_wafaat.setEnabled(false);
            et_tabsarah_wajah_wafaat.setEnabled(false);
            et_tabsarah_wajah_trasnfer.setEnabled(false);
            et_khandan_number.setEnabled(false);
            et_infradi.setEnabled(false);

            sw_biometric_faal_kre.setEnabled(false);
            sw_qrcode_faal_kre.setEnabled(false);
            sw_mother_status.setEnabled(false);
            sp_wajah_wafat.setEnabled(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image_arrow_down_azdawaji.setImageTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                image_arrow_down_jins.setImageTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                image_arrow_down_wafat.setImageTintList(ctx.getResources().getColorStateList(R.color.grey_color));
            }

            et_naam_bimaah_waldiyat.setText(jsonObject.getString("full_name"));
            et_walid_ka_naam.setText(jsonObject.getString("father_name"));
            et_walida_ka_naam.setText(jsonObject.getString("mother_name"));
            et_sarbarah_kay_sath_rishta.setText(jsonObject.getString("relation_with_hof"));
            et_tareekh_pedaish.setText(jsonObject.getString("dob"));
            et_umer.setText(jsonObject.getString("age"));
            et_shanakhti_card_number.setText(jsonObject.getString("nic"));
            sp_azdawaji_hasiyat.setSelection(Integer.parseInt(jsonObject.getString("maritalstatus")) + 1);
            et_mobile_number.setText(mData[0][5]);
            et_tareekh_nakal_makani.setText(jsonObject.getString("date_of_transfer"));
            et_tabsarah_wajah_trasnfer.setText(jsonObject.getString("reason_of_transfer"));
            et_khandan_number.setText(jsonObject.getString("khandan_number_manual"));
            et_infradi.setText(jsonObject.getString("manual_id"));

            khandan_number = jsonObject.getString("khandan_number_manual");


            if (jsonObject.getString("date_of_death").isEmpty()) {
                sw_mother_status.setChecked(true);
                ll_wafat.setVisibility(View.GONE);
                mother_status = "0";

            } else {
                sw_mother_status.setChecked(false);
                ll_wafat.setVisibility(View.VISIBLE);
                et_tareekh_wafaat.setText(jsonObject.getString("date_of_death"));
                sp_wajah_wafat.setSelection(Integer.parseInt(jsonObject.getString("sp_reason_of_death_pos")) + 1);
                if (jsonObject.getString("reason_of_death").isEmpty()) {
                    et_tabsarah_wajah_wafaat.setText("-");
                } else {
                    et_tabsarah_wajah_wafaat.setText(jsonObject.getString("reason_of_death"));
                }
                mother_status = "1";
            }

            if (qr_code_text.equalsIgnoreCase("no_qr_code")) {
                Log.d("000123", "QR IF");
                sw_qrcode_faal_kre.setChecked(false);
            } else if (qr_code_text.equalsIgnoreCase("none")) {
                Log.d("000123", "QR ELSE IF");

                sw_qrcode_faal_kre.setChecked(false);
            } else {
                Log.d("000123", "QR ELSE");

                sw_qrcode_faal_kre.setChecked(true);
            }

            if (age >= 3 && age <= 14) {

                Log.d("000321", "IFF : " );
// Sp gender
                final ArrayAdapter<CharSequence> adptr_gender = ArrayAdapter.createFromResource(ctx, R.array.array_child_gender, R.layout.temp);
                adptr_gender.setDropDownViewResource(R.layout.temp);

                sp_jins.setAdapter(
                        new com.akdndhrc.covid_module.NothingSelectedSpinnerAdapter(
                                adptr_gender,
                                R.layout.temp,
                                ctx));


                sp_jins.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            else {
                Log.d("000321", "ELSE : " );
                final ArrayAdapter<CharSequence> adptr_gender = ArrayAdapter.createFromResource(ctx, R.array.array_gender, R.layout.temp);
                adptr_gender.setDropDownViewResource(R.layout.temp);

                sp_jins.setAdapter(
                        new com.akdndhrc.covid_module.NothingSelectedSpinnerAdapter(
                                adptr_gender,
                                R.layout.temp,
                                ctx));


                sp_jins.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            sp_jins.setSelection(Integer.parseInt(mData[0][3]) + 1);


            ls.closeDB();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("000555", "Error: " + e.getMessage());
            Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
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
        var_mother_pro = "0";

    }
}
