package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_RegisterActivities;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.CustomClass.Customer;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.GPSTracker;
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

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class AboveTwo_Register_Activity extends AppCompatActivity {

    Context ctx = AboveTwo_Register_Activity.this;

    Spinner sp_azdawaji_hasiyat, sp_jins;
    EditText et_naam_bimaah_waldiyat, et_walid_ka_naam, et_walida_ka_naam, et_sarbarah_kay_sath_rishta, et_tareekh_pedaish, et_shanakhti_card_number,
            et_mobile_number, et_tareekh_nakal_makani, et_tareekh_wafaat, et_tabsarah_wajah_wafaat, et_khandan_number, et_infradi, et_tabsarah_wajah_nakal_makani, et_age;

    Switch sw_biometric_faal_kre, sw_qrcode_faal_kre;
    Button btn_jamaa_kre;
    RelativeLayout rl_home, rl_navigation_drawer, qr_code;


    double latitude;
    double longitude;
    // GPSTracker class
    GPSTracker gps;
    String khandan_manual_id, khandan_uuid;

    private int mYear, mMonth, mDay;
    int date_for_condition = 0;
    int month_for_condition = 0;

    public String hold_age_date_condition = "fromage";
    String monthf2, dayf2, yearf2 = "null", TodayDate, qr_code_text;

    int age;
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String temp_tareekh_wafat = "0";
    String temp_tareekh_nakalmakani = "0";
    String login_useruid;
    public static String var_reg_above="0";

    long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abovetwo_register_);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, AboveTwo_Register_Activity.class));


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

        // check_gps();

        //Spinner
        sp_azdawaji_hasiyat = findViewById(R.id.sp_azdawaji_hasiyat);
        sp_jins = findViewById(R.id.sp_jins);
        qr_code = findViewById(R.id.rl_qrcode);

        //EditText
        et_naam_bimaah_waldiyat = findViewById(R.id.et_naam_bimaah_waldiyat);
        et_walid_ka_naam = findViewById(R.id.et_walid_ka_naam);
        et_walida_ka_naam = findViewById(R.id.et_walida_ka_naam);
        //et_tareekh_indraj = findViewById(R.id.et_tareekh_pedaish);
        et_age = findViewById(R.id.et_age);
        et_sarbarah_kay_sath_rishta = findViewById(R.id.et_sarbarah_kay_sath_rishta);
        et_tareekh_pedaish = findViewById(R.id.et_tareekh_pedaish);
        et_shanakhti_card_number = findViewById(R.id.et_shanakhti_card_number);
        et_mobile_number = findViewById(R.id.et_mobile_number);
        et_tareekh_nakal_makani = findViewById(R.id.et_tareekh_nakal_makani);
        et_tareekh_wafaat = findViewById(R.id.et_tareekh_wafaat);

        et_tabsarah_wajah_nakal_makani = findViewById(R.id.et_tabsarah_wajah_trasnfer);
        et_tabsarah_wajah_wafaat = findViewById(R.id.et_tabsarah_wajah_wafaat);

        et_khandan_number = findViewById(R.id.et_khandan_number);
        et_infradi = findViewById(R.id.et_infradi);


        //Switch
        sw_biometric_faal_kre = findViewById(R.id.sw_biometric_faal_kre);
        sw_qrcode_faal_kre = findViewById(R.id.sw_qrcode_faal_kre);

        //Buttton
        btn_jamaa_kre = findViewById(R.id.submit);

        //RelativeLayout
        rl_navigation_drawer = findViewById(R.id.rl_navigation_drawer);
        rl_home = findViewById(R.id.rl_home);
        rl_navigation_drawer.setVisibility(View.GONE);
        rl_home.setVisibility(View.GONE);


        rl_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rl_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sw_qrcode_faal_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, Register_QRCode_Activity.class);
                intent.putExtra("class_id", "1");
                startActivity(intent);
            }
        });


        try {
            SharedPreferences settings = getSharedPreferences("Khandanuuid", MODE_PRIVATE);
            // Reading from SharedPreferences
            khandan_manual_id = settings.getString("k_uuid_m", "");
            khandan_uuid = settings.getString("k_uuid", "");

            et_khandan_number.setText(khandan_manual_id);

        } catch (Exception e) {

        }

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


        //Mobie Number
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

//                ((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                //Toast.makeText(parent.getContext(), "Selected: " + position, Toast.LENGTH_LONG).show();

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
                    final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی نام درج کریں.", Snackbar.LENGTH_SHORT);
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



                if (et_tareekh_pedaish.getText().toString().length() < 1) {

                    //Toast.makeText(getApplicationContext(), "برائے مہربانی تاریخ پیدائش منتخب کریں", Toast.LENGTH_LONG).show();
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

                    // Toast.makeText(getApplicationContext(), "جنس منتخب کریں", Toast.LENGTH_LONG).show();
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

                if (age <= 2) {
                 //   Toast.makeText(getApplicationContext(), "منتخب عمر کو دو سال سے زائد ہونا ضروری ہے", Toast.LENGTH_SHORT).show();
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

                btn_jamaa_kre.setVisibility(View.GONE);
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Log.d("000555", " mLastClickTime: " + mLastClickTime);


                if (serviceLocation.showCurrentLocation() == true) {
                    latitude = serviceLocation.getLatitude();
                    longitude = serviceLocation.getLongitude();
                    Log.d("000555", " latitude: " + latitude);
                    Log.d("000555", " longitude: " + longitude);
                } else {

                    serviceLocation.doAsynchronousTask.cancel();
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

                try {

                    SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar c = Calendar.getInstance();
                    TodayDate = dates.format(c.getTime());

                    if (Register_QRCode_Activity.switch_qr_code_values.equalsIgnoreCase("1")) {

                        SharedPreferences settings = getSharedPreferences("shared_QR_Value", MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        // Reading from SharedPreferences
                        qr_code_text = settings.getString("qr_code", "");
                        editor.apply();
                        Log.d("000555", "IF VALUE: " + qr_code_text);

                    } else {
                        qr_code_text = "none";
                        Log.d("000555", "ELSE Value: " + qr_code_text);
                    }


                    Lister ls = new Lister(AboveTwo_Register_Activity.this);
                  ls.createAndOpenDB();

                    JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("status_death", "" + "0");
                    jobj.put("deliv_year", "" + "0");
                    jobj.put("deliv_month", "" + "0");
                    jobj.put("member_type", "" + "abovetwo");
                    jobj.put("full_name", "" + et_naam_bimaah_waldiyat.getText().toString());
                    jobj.put("father_name", "" + et_walid_ka_naam.getText().toString());
                    jobj.put("mother_name", "" + et_walida_ka_naam.getText().toString());
                    jobj.put("relation_with_hof", "" + et_sarbarah_kay_sath_rishta.getText().toString());
                    jobj.put("age", "" + et_age.getText().toString());
                    jobj.put("dob", "" + et_tareekh_pedaish.getText().toString());
                    jobj.put("nic", "" + et_shanakhti_card_number.getText().toString());
                    jobj.put("gender", "" + String.valueOf(sp_jins.getSelectedItemPosition() - 1));
                    jobj.put("maritalstatus", "" + String.valueOf(sp_azdawaji_hasiyat.getSelectedItemPosition() - 1));//spinner
                    jobj.put("mobile", "" + et_mobile_number.getText().toString());
                    jobj.put("date_of_death", "" + et_tareekh_wafaat.getText().toString());
                    jobj.put("date_of_transfer", "" + et_tareekh_nakal_makani.getText().toString());
                    jobj.put("reason_of_transfer", "" + et_tabsarah_wajah_nakal_makani.getText().toString());
                    jobj.put("reason_of_death", "" + et_tabsarah_wajah_wafaat.getText().toString());
                    jobj.put("manual_id", "" + et_infradi.getText().toString());
                    jobj.put("khandan_number_manual", "" + et_khandan_number.getText().toString());
                    jobj.put("record_date", "" + TodayDate);


                    // jobjMain.put("data", jobj);
                    String uuid = UUID.randomUUID().toString().replace("-", "");

                    /*SharedPreferences settings = getSharedPreferences("Khandanuuid", MODE_PRIVATE);
                    // Reading from SharedPreferences
                    khandan_uuid = settings.getString("k_uuid", "");*/

                  /*  SharedPreferences settings1 = getSharedPreferences("Khandanuuid", MODE_PRIVATE);
                    // Reading from SharedPreferences
                    String qr_code_text = settings1.getString("qr_code", "");
                    if (qr_code_text.length() < 1) {
                        qr_code_text = "no_qr_code";
                    }*/

                    String added_on = String.valueOf(System.currentTimeMillis());
                    String ans1 = "insert into MEMBER (manual_id, uid, khandan_id, full_name, nicnumber, phone_number, gender, age,dob, bio_code, qr_code, added_by, data,is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + et_infradi.getText().toString() + "'," +
                            "'" + uuid + "'," +
                            "'" + khandan_uuid + "'," +
                            "'" + et_naam_bimaah_waldiyat.getText().toString() + "'," +
                            "'" + et_shanakhti_card_number.getText().toString() + "'," +
                            "'" + et_mobile_number.getText().toString() + "'," +
                            "'" + String.valueOf(sp_jins.getSelectedItemPosition() - 1) + "'," +
                            "'" + et_age.getText().toString() + "'," +
                            "'" + et_tareekh_pedaish.getText().toString() + "'," +
                            "'" + "bio_code" + "'," +
                            "'" + qr_code_text + "'," +
                            "'" + login_useruid + "'," +
                            "'" + jobj + "'," +
                            "'0'," +
                            "'" + added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000555", "Data: " + ans1);
                    Log.d("00055", "Query:" + res.toString());

                    if (res.toString().equalsIgnoreCase("true"))
                    {
                        final Snackbar snackbar = Snackbar.make(v, "خاندان کا رکن رجسٹر ہوگیا ہے.", Snackbar.LENGTH_SHORT);
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
                            sendPostRequest(et_infradi.getText().toString(), uuid, khandan_uuid, et_naam_bimaah_waldiyat.getText().toString(), et_shanakhti_card_number.getText().toString(),
                                    et_mobile_number.getText().toString(), String.valueOf(sp_jins.getSelectedItemPosition() - 1), et_age.getText().toString(), et_tareekh_pedaish.getText().toString(),
                                    "bio_code", qr_code_text, login_useruid,
                                    String.valueOf(jobj), added_on);
                        } else {

                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(ctx, Add_Family_Member_Activity.class);
                                startActivity(intent);
                                Register_QRCode_Activity.switch_qr_code_values = "0";
                                var_reg_above="1";
                            }
                        },2000);
                    }
                    else
                    {
                        final Snackbar snackbar = Snackbar.make(v, "خاندان کا رکن رجسٹر نہیں ہوا.", Snackbar.LENGTH_SHORT);
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


                   /* if (res.toString().equalsIgnoreCase("true"))
                    {
                        Toast tt = Toast.makeText(ctx,"خاندان کا رکن رجسٹر ہوگیا ہے", Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();

                        if (Utils.haveNetworkConnection(ctx) > 0) {
                            sendPostRequest(et_infradi.getText().toString(), uuid, khandan_uuid, et_naam_bimaah_waldiyat.getText().toString(), et_shanakhti_card_number.getText().toString(),
                                    et_mobile_number.getText().toString(), String.valueOf(sp_jins.getSelectedItemPosition() - 1), et_age.getText().toString(), et_tareekh_pedaish.getText().toString(),
                                    "bio_code", qr_code_text, login_useruid,
                                    String.valueOf(jobj), added_on);
                        } else {
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(ctx, Add_Family_Member_Activity.class);
                                startActivity(intent);
                                Register_QRCode_Activity.switch_qr_code_values = "0";
                                var_reg_above="1";
                            }
                        },1500);
                    }
                    else{
                        Toast tt = Toast.makeText(ctx,"خاندان کا رکن رجسٹر نہیں ہوا", Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();
                    }*/





                   /* if (Utils.haveNetworkConnection(ctx) > 0) {

                        sendPostRequest(et_infradi.getText().toString(), uuid, khandan_uuid, et_naam_bimaah_waldiyat.getText().toString(), et_shanakhti_card_number.getText().toString(),
                                et_mobile_number.getText().toString(), String.valueOf(sp_jins.getSelectedItemPosition() - 1), et_age.getText().toString(), et_tareekh_pedaish.getText().toString(),
                                "bio_code", qr_code_text, login_useruid,
                                String.valueOf(jobj), added_on);
                    } else {
                    }
*/
                } catch (Exception e) {
                    Log.d("000555", "Err: " + e.getMessage());
                } finally {

                  /*  Intent intent = new Intent(ctx, Add_Family_Member_Activity.class);
                    startActivity(intent);
                    Register_QRCode_Activity.switch_qr_code_values = "0";
                    var_reg_above="1";*/
                }
            }
        });


    }

    private void sendPostRequest(final String manual_id, final String uid, final String khandan_id, final String full_name,
                                 final String nicnumber, final String phone_number, final String gender, final String age, final String dob,
                                 final String bio_code, final String qr_code, final String added_by,
                                 final String data, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/family/members";

        Log.d("000999", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {
                    //  Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000999", "response    " + response);
                        Lister ls = new Lister(AboveTwo_Register_Activity.this);
                      ls.createAndOpenDB();

                        String update_record = "UPDATE MEMBER SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE uid = '" + uid + "'";
                        ls.executeNonQuery(update_record);

                        Toast tt  =Toast.makeText(ctx, "ڈیٹا سنک ہوگیا ہے", Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();

                    } else {
                        Log.d("000999", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        // Toast.makeText(AboveTwo_Register_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, "ڈیٹا سروس پر سینک نہیں ہوا", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", "Err: " + e.getMessage());
                    Toast.makeText(ctx, "ڈیٹا سروس پر سینک نہیں ہوا", Toast.LENGTH_SHORT).show();
                    /// /Toast.makeText(AboveTwo_Register_Activity.this, "Data has been sent incorrectly.", Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000999", "onErrorResponse: " + error.getMessage());
                //Toast.makeText(AboveTwo_Register_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
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

                Log.d("000999", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000999", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }

    private ArrayList<Customer> populateCustomerData(ArrayList<Customer> customers) {
        customers.add(new Customer("ایک منتخب کریں", 1, android.R.color.transparent));
        customers.add(new Customer("عورت", 2, R.drawable.ic_gender_women_icon));
        customers.add(new Customer("مرد", 3, R.drawable.ic_gender_male_icon));


        return customers;
    }


    public void ShowTareekhPedaishDialog() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(AboveTwo_Register_Activity.this, R.style.DatePickerDialog,
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
                                    new NothingSelectedSpinnerAdapter(
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
                                    new NothingSelectedSpinnerAdapter(
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
                        //Toast.makeText(getApplicationContext(),String.valueOf(year)+"major" + ageS +"age",Toast.LENGTH_SHORT).show();
                        //et_tareekh_pedaish.setText(ageS);
                        et_age.setText(ageS);
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


        DatePickerDialog datePickerDialog = new DatePickerDialog(AboveTwo_Register_Activity.this, R.style.DatePickerDialog,
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


        DatePickerDialog datePickerDialog = new DatePickerDialog(AboveTwo_Register_Activity.this, R.style.DatePickerDialog,
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
                        // et_age.setText(ageS);
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
            Log.d("000555", "latitude value: " + latitude);
            Log.d("000555", "longitude value: " + longitude);
        } else {
            gps.showSettingsAlert();
            Toast.makeText(ctx, "برائے مہربانی جی پی ایس پوزیشن کو آن کریں", Toast.LENGTH_LONG).show();
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

                Log.d("000555", "IF lat lng: ");
                Log.d("000555", "latitude: " + latitude);
                Log.d("000555", "longitude: " + longitude);


                return;
            } else {
                snackbar.dismiss();
                Log.d("000555", "ELSE lat lng: ");
                Log.d("000555", "latitude: " + latitude);
                Log.d("000555", "longitude: " + longitude);

                Toast.makeText(ctx, "جی پی ایس پوزیشن اب آن ہے", Toast.LENGTH_SHORT).show();
            }

        } else {
            gps.showSettingsAlert();
            Toast.makeText(ctx, "برائے مہربانی جی پی ایس پوزیشن کو آن کریں", Toast.LENGTH_LONG).show();
            return;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (Register_QRCode_Activity.switch_qr_code_values.equalsIgnoreCase("1")) {

            sw_qrcode_faal_kre.setChecked(true);
            sw_qrcode_faal_kre.setEnabled(false);

            Log.d("000111", "IF");
        } else {

            Log.d("000111", "ELSE: ");
            sw_qrcode_faal_kre.setChecked(false);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // ClassListener.mSlideMenu.close(false);

//        Intent intent = new Intent(ctx, HomePage_Activity.class);
//        startActivity(intent);
        finish();
        Register_QRCode_Activity.switch_qr_code_values = "0";
        var_reg_above="0";
    }
}

