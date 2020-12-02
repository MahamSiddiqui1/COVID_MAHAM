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

public class VAC_BelowTwoProfile_Activity extends AppCompatActivity {

    Context ctx = VAC_BelowTwoProfile_Activity.this;

    String child_uid;
    ImageView iv_navigation_drawer, iv_home, iv_editprofile;
    Button btn_jamaa_kre, btn_jari_rhy;
    Spinner sp_jins, sp_wajah_wafat;
    Switch sw_biometric_faal_kre, sw_qrcode_faal_kre, sw_child_status;
    EditText et_bachey_ka_naam, et_tareekh_pedaish, et_walid_ka_naam, et_walida_ka_naam, et_walid_ka_shanakti_card_number, et_mobile_number, et_vacination_card_number, et_address, et_tabsarah_wajah_wafaat, et_tareekh_wafaat;
    double latitude;
    double longitude;
    String qr_code_text = "none";
    String TodayDate, union_council;

    Dialog alertDialog;
    ProgressBar pbProgress;
    ServiceLocation serviceLocation;
    String login_useruid, khandan_id, added_on, child_age;
    JSONObject jsonObject;
    String mDatachild[][];
    public static String var_updateprofile_belowtwotemp = "null";
    private int mYear, mMonth, mDay;
    String monthf2, dayf2, yearf2 = "null";
    ImageView image_arrow_drop_jins, image_arrow_down_wafat;
    LinearLayout ll_wafat, ll_wajah_wafat_likhey;
    String child_status;
    String temp_tareekh_wafat = "0";
    long mLastClickTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vac_belowtwo_profile);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, VAC_BelowTwoProfile_Activity.class));

        child_uid = getIntent().getExtras().getString("u_id");


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
        et_walida_ka_naam = findViewById(R.id.et_walida_ka_naam);
        et_walid_ka_shanakti_card_number = findViewById(R.id.et_walid_ka_shanakti_card_number);
        et_mobile_number = findViewById(R.id.et_mobile_number);
        et_vacination_card_number = findViewById(R.id.et_vacination_card_number);
        et_address = findViewById(R.id.et_address);
        et_tareekh_wafaat = findViewById(R.id.et_tareekh_wafaat);
        et_tabsarah_wajah_wafaat = findViewById(R.id.et_tabsarah_wajah_wafaat);


        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_editprofile = findViewById(R.id.iv_editprofile);
        image_arrow_drop_jins = findViewById(R.id.image_arrow_drop_jins);
        image_arrow_down_wafat = findViewById(R.id.image_arrow_down_wafat);
        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);

//progress
        pbProgress = findViewById(R.id.pbProgress);

        //Spinner
        sp_jins = findViewById(R.id.sp_jins);
        sp_wajah_wafat = findViewById(R.id.sp_wajah_wafat);

        //Linear layout
        ll_wafat = findViewById(R.id.ll_wafat);
        ll_wajah_wafat_likhey = findViewById(R.id.ll_wajah_wafat_likhey);

        //Switch
        sw_biometric_faal_kre = findViewById(R.id.sw_biometric_faal_kre);
        sw_qrcode_faal_kre = findViewById(R.id.sw_qrcode_faal_kre);
        sw_child_status = findViewById(R.id.sw_child_status);

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


        //Edit Person CNIC
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

        //Mobile NUmber

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

        sw_qrcode_faal_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, VAC_Register_QRCode_Activity.class);
                intent.putExtra("class_id", "2");
                startActivity(intent);
            }
        });


        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ctx, R.string.navigation, Toast.LENGTH_SHORT).show();
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
        sw_child_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll_wafat.setVisibility(View.GONE);
                    child_status = "0";
                } else {
                    ll_wafat.setVisibility(View.VISIBLE);
                    child_status = "1";
                }
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

        et_tareekh_pedaish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog();
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


                        et_tareekh_pedaish.setEnabled(true);
                        et_bachey_ka_naam.setEnabled(true);
                        et_walid_ka_naam.setEnabled(true);
                        et_walida_ka_naam.setEnabled(true);
                        et_walid_ka_shanakti_card_number.setEnabled(true);
                        et_mobile_number.setEnabled(true);
                        et_address.setEnabled(true);
                        et_vacination_card_number.setEnabled(true);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            image_arrow_drop_jins.setImageTintList(ctx.getResources().getColorStateList(R.color.dark_blue_color));
                        }

                        if (mDatachild[0][3].equalsIgnoreCase("-1")) {
                            sp_jins.setEnabled(true);
                        } else {
                            sp_jins.setEnabled(true);
                        }

                        if (mDatachild[0][9].equalsIgnoreCase("no_qr_code")) {
                            sw_qrcode_faal_kre.setEnabled(true);
                        } else if (mDatachild[0][9].equalsIgnoreCase("none")) {
                            sw_qrcode_faal_kre.setEnabled(true);
                        } else {
                            sw_qrcode_faal_kre.setEnabled(false);
                        }

                        if (child_status.equalsIgnoreCase("1")) {
                            sw_child_status.setEnabled(false);
                            sp_wajah_wafat.setEnabled(false);
                            et_tareekh_wafaat.setEnabled(false);
                            et_tabsarah_wajah_wafaat.setEnabled(false);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                image_arrow_down_wafat.setImageTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                            }
                        } else {
                            sw_child_status.setEnabled(true);
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
                    final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی بچے کا نام درج کریں.", Snackbar.LENGTH_SHORT);
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

                if (et_tareekh_pedaish.getText().toString().isEmpty()) {
                    //   Toast.makeText(getApplicationContext(), "برائے مہربانی تاریخ پیدائش منتخب کریں", Toast.LENGTH_SHORT).show();
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

                if (Integer.valueOf(child_age) >= 4) {
                    //Toast.makeText(getApplicationContext(), "منتخب عمر کو دو سال سے کم ہونا ضروری ہے", Toast.LENGTH_SHORT).show();
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


                if (child_status.equalsIgnoreCase("1")) {

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

                    if (sp_wajah_wafat.getSelectedItemPosition()==1 || sp_wajah_wafat.getSelectedItemPosition()==2 ||  sp_wajah_wafat.getSelectedItemPosition()==3 || sp_wajah_wafat.getSelectedItemPosition()==4)
                    {
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

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Log.d("000555", " mLastClickTime: " + mLastClickTime);


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
                jsonObject.put("status_death", "" + child_status);
                jsonObject.put("full_name", "" + et_bachey_ka_naam.getText().toString());
                jsonObject.put("mother_name", "" + et_walida_ka_naam.getText().toString());
                jsonObject.put("address", "" + et_address.getText().toString());
                jsonObject.put("dob", "" + et_tareekh_pedaish.getText().toString());
                jsonObject.put("age", "" + child_age.toString());
                jsonObject.put("nic", "" + et_walid_ka_shanakti_card_number.getText().toString());
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
                                "phone_number='" + et_mobile_number.getText().toString() + "'," +
                                "dob='" + et_tareekh_pedaish.getText().toString() + "'," +
                                "age='" + child_age.toString() + "'," +
                                "gender='" + String.valueOf(sp_jins.getSelectedItemPosition() - 1) + "'," +
                                "data='" + jsonObject.toString() + "'," +
                                "qr_code='" + qr_code_text + "'," +
                                "is_synced='" + 0 + "'" +
                                "WHERE uid = '" + child_uid + "'";

                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d("000123", "Data: " + update_record);
                        Log.d("000123", "Query: " + res.toString());


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

                                sendPostRequest(child_uid, child_uid, khandan_id, et_bachey_ka_naam.getText().toString(), et_walid_ka_shanakti_card_number.getText().toString(),
                                        et_mobile_number.getText().toString(), String.valueOf(sp_jins.getSelectedItemPosition() - 1), child_age, et_tareekh_pedaish.getText().toString(),
                                        "bio_code", qr_code_text, login_useruid, String.valueOf(jsonObject), added_on);
                            } else {
                                //Toast.makeText(ctx, R.string.dataEdited, Toast.LENGTH_SHORT).show();
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
                                var_updateprofile_belowtwotemp = "1";

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
        final ArrayAdapter<CharSequence> adptr_gender = ArrayAdapter.createFromResource(this, R.array.array_child_gender, R.layout.temp);
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


        // Select sp_childStatus
        final ArrayAdapter<CharSequence> adptr_wajah_wafat = ArrayAdapter.createFromResource(this, R.array.array_child_daeth_reason_urdu, android.R.layout.simple_spinner_item);
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

                if (position == 1 || position == 2 || position == 3 || position == 5) {

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

                        Lister ls = new Lister(VAC_BelowTwoProfile_Activity.this);
                        ls.createAndOpenDB();
                        String update_record = "UPDATE MEMBER SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE uid = '" + child_uid + "'";
                        ls.executeNonQuery(update_record);

                        Toast tt  =Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();

                    } else {
                        Log.d("000123", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(VAC_BelowInsideOutsideUC_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000123", "Err: " + e.getMessage());
                    // Toast.makeText(VAC_BelowInsideOutsideUC_Activity.this, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();
                    Toast tt  =Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT);
                    tt.setGravity(Gravity.CENTER, 0, 0);
                    tt.show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000123", "onErrorResponse: " + error.getMessage());
                // Toast.makeText(VAC_BelowInsideOutsideUC_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
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


                        child_age = String.valueOf(today.get(Calendar.YEAR) - year);
                        Log.d("000123", R.string.ageColon + child_age);

                        Integer ageInt = new Integer(child_age);
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
            final Lister ls = new Lister(ctx);
            ls.createAndOpenDB();


            mDatachild = ls.executeReader("Select uid,full_name, dob,gender,nicnumber,phone_number,data,khandan_id,added_on,qr_code,age from MEMBER where uid = '" + child_uid + "'");

            khandan_id = mDatachild[0][7];
            added_on = mDatachild[0][8];
            child_age = mDatachild[0][10];

            Log.d("000123", "uid: " + mDatachild[0][0]);
            Log.d("000123", "FullName: " + mDatachild[0][1]);
            Log.d("000123", "DOB: " + mDatachild[0][2]);
            Log.d("000123", "Gender: " + mDatachild[0][3]);
            Log.d("000123", "NicNumber: " + mDatachild[0][4]);
            Log.d("000123", "PhoneNumber: " + mDatachild[0][5]);
            Log.d("000123", "JSON Data: " + mDatachild[0][6]);
            Log.d("000123", "khandan_id: " + mDatachild[0][7]);
            Log.d("000123", "added_on: " + mDatachild[0][8]);
            Log.d("000123", "qr_code: " + mDatachild[0][9]);
            Log.d("000123", R.string.ageColon + mDatachild[0][10]);


            et_bachey_ka_naam.setEnabled(false);
            et_tareekh_pedaish.setEnabled(false);
            sp_jins.setEnabled(false);
            et_walid_ka_naam.setEnabled(false);
            et_walida_ka_naam.setEnabled(false);
            et_walid_ka_shanakti_card_number.setEnabled(false);
            et_mobile_number.setEnabled(false);
            et_address.setEnabled(false);
            et_vacination_card_number.setEnabled(false);
            sw_qrcode_faal_kre.setEnabled(false);

            sw_child_status.setEnabled(false);
            sp_wajah_wafat.setEnabled(false);
            et_tareekh_wafaat.setEnabled(false);
            et_tabsarah_wajah_wafaat.setEnabled(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image_arrow_drop_jins.setImageTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                image_arrow_down_wafat.setImageTintList(ctx.getResources().getColorStateList(R.color.grey_color));
            }

            String json = mDatachild[0][6];

            jsonObject = new JSONObject(json);

            et_bachey_ka_naam.setText(mDatachild[0][1]);
            et_tareekh_pedaish.setText(mDatachild[0][2]);
            sp_jins.setSelection(Integer.parseInt(mDatachild[0][3]) + 1);
            et_walid_ka_naam.setText(jsonObject.getString("father_name"));


            if (jsonObject.has("mother_name")) {
                if (jsonObject.getString("mother_name").equalsIgnoreCase("")) {
                    et_walida_ka_naam.setText("");
                } else {
                    et_walida_ka_naam.setText(jsonObject.getString("mother_name"));
                }
            } else {
                et_walida_ka_naam.setText("");
            }

            if (mDatachild[0][4].isEmpty()) {
                Log.d("000123", "NIC IFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
                et_walid_ka_shanakti_card_number.setText("");
            } else {
                Log.d("000123", "NIC ELSEEEEEEEEEEEEE");
                et_walid_ka_shanakti_card_number.setText(mDatachild[0][4]);
            }

            if (mDatachild[0][5].isEmpty()) {
                Log.d("000123", "MOBILE IFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
                et_mobile_number.setText("");
            } else {
                Log.d("000123", "M<OBILEEEEEEEEEEEEEEE ELSEEEEEEEEEEEEE");
                et_mobile_number.setText(mDatachild[0][5]);
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

            if (mDatachild[0][9].equalsIgnoreCase("no_qr_code")) {
                Log.d("000123", "QR IF");
                sw_qrcode_faal_kre.setChecked(false);
            } else if (mDatachild[0][9].equalsIgnoreCase("none")) {
                Log.d("000123", "QR ELSE IF");

                sw_qrcode_faal_kre.setChecked(false);
            } else {
                Log.d("000123", "QR ELSE");

                sw_qrcode_faal_kre.setChecked(true);
            }


            if (jsonObject.getString("date_of_death").equalsIgnoreCase("0") || jsonObject.getString("date_of_death").isEmpty()) {
                sw_child_status.setChecked(true);
                ll_wafat.setVisibility(View.GONE);
                child_status = "0";

            } else {
                sw_child_status.setChecked(false);
                ll_wafat.setVisibility(View.VISIBLE);
                et_tareekh_wafaat.setText(jsonObject.getString("date_of_death"));
                sp_wajah_wafat.setSelection(Integer.parseInt(jsonObject.getString("sp_reason_of_death_pos")) + 1);
                child_status = "1";

                if (jsonObject.getString("reason_of_death").isEmpty())
                {
                    et_tabsarah_wajah_wafaat.setText("-");
                }
                else{
                    et_tabsarah_wajah_wafaat.setText(jsonObject.getString("reason_of_death"));
                }
            }


            ls.closeDB();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("000123", "Error: " + e.getMessage());
            Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
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
            if (mDatachild[0][9].equalsIgnoreCase("no_qr_code")) {
                Log.d("000123", "QR IF");

                sw_qrcode_faal_kre.setChecked(false);
            } else if (mDatachild[0][9].equalsIgnoreCase("none")) {
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
        var_updateprofile_belowtwotemp = "0";
    }
}