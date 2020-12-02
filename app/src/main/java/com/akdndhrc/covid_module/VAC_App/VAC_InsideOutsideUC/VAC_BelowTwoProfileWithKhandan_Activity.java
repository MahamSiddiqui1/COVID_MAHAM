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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.UUID;

public class VAC_BelowTwoProfileWithKhandan_Activity extends AppCompatActivity {

    Context ctx = VAC_BelowTwoProfileWithKhandan_Activity.this;

    String child_uid;
    ImageView iv_navigation_drawer, iv_home, iv_editprofile;
    Button btn_jamaa_kre, btn_jari_rhy;
    Spinner sp_gaon, sp_zila, sp_union_council, sp_tahseel, sp_jins;
    Switch sw_biometric_faal_kre, sw_qrcode_faal_kre;
    EditText et_bachey_ka_naam, et_tareekh_pedaish, et_walid_ka_naam, et_walida_ka_naam, et_walid_ka_shanakti_card_number, et_mobile_number, et_vacination_card_number, et_address;
    double latitude;
    double longitude;
    String qr_code_text = "none";
    String TodayDate, union_council;

    Dialog alertDialog;
    ProgressBar pbProgress;
    ServiceLocation serviceLocation;
    String login_useruid, added_on, child_age;
    JSONObject jsonObject;
    String mDatachild[][];
    public static String var_updateprofilekhandan_belowtwotemp = "null";
    String khandan_uuid;
    private int mYear, mMonth, mDay;
    String monthf2, dayf2, yearf2 = "null";
    ImageView image_arrow_drop_jins;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vac_belowtwo_khandan_profile);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, VAC_BelowTwoProfileWithKhandan_Activity.class));

        child_uid = getIntent().getExtras().getString("u_id");


        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());


        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences("UserLogin", 0); // 0 - for private mode
            String shared_useruid = prefelse.getString("login_userid", null); // getting String
            login_useruid = shared_useruid;
            Log.d("000258", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000258", "Shared Err:" + e.getMessage());
        }


        try {
            serviceLocation = new ServiceLocation(ctx);
            serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            serviceLocation.callAsynchronousTask();
        } catch (Exception e) {
            Log.d("000258", "GPS Service Err:  " + e.getMessage());
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


        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_editprofile = findViewById(R.id.iv_editprofile);
        image_arrow_drop_jins = findViewById(R.id.image_arrow_drop_jins);
        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);

//progress
        pbProgress = findViewById(R.id.pbProgress);

        //Spinner
        sp_jins = findViewById(R.id.sp_jins);
        sp_tahseel = findViewById(R.id.sp_tahseel);
        sp_union_council = findViewById(R.id.sp_union_council);
        sp_gaon = findViewById(R.id.sp_gaon);
        sp_zila = findViewById(R.id.sp_zila);


        //Switch
        sw_biometric_faal_kre = findViewById(R.id.sw_biometric_faal_kre);
        sw_qrcode_faal_kre = findViewById(R.id.sw_qrcode_faal_kre);

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
                        et_walida_ka_naam.setEnabled(true);
                        et_walid_ka_shanakti_card_number.setEnabled(true);
                        et_mobile_number.setEnabled(true);
                        et_address.setEnabled(true);
                        et_vacination_card_number.setEnabled(true);
                        sp_gaon.setEnabled(true);
                        sp_zila.setEnabled(true);
                        sp_tahseel.setEnabled(true);
                        sp_union_council.setEnabled(true);

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

            /*    if (et_walid_ka_shanakti_card_number.getText().toString().length() > 0) {
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
                    Log.d("000258", "ELSE NIC NUMBER");
                }*/

                Log.d("000258", "OKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
                if (sp_zila.getSelectedItemPosition() == 0) {

                    //Toast.makeText(getApplicationContext(), "ضلع منتخب کریں", Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar.make(v, "ضلع منتخب کریں.", Snackbar.LENGTH_SHORT);
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

                if (sp_tahseel.getSelectedItemPosition() == 0) {

                    //Toast.makeText(getApplicationContext(), "تحصیل منتخب کریں", Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar.make(v, "تحصیل منتخب کریں.", Snackbar.LENGTH_SHORT);
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


                if (sp_union_council.getSelectedItemPosition() == 0) {
                    // Toast.makeText(getApplicationContext(), "یونین کونسل منتخب کریں", Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar.make(v, "یونین کونسل منتخب کریں.", Snackbar.LENGTH_SHORT);
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


                if (sp_gaon.getSelectedItemPosition() == 0) {
                    //btn_jamaa_kre.setVisibility(View.GONE);
                    //Toast.makeText(getApplicationContext(), "گاؤں منتخب کریں", Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar.make(v, "گاؤں منتخب کریں.", Snackbar.LENGTH_SHORT);
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

                if (Integer.valueOf(child_age) >=4) {
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

                if (serviceLocation.showCurrentLocation() == true) {
                    latitude = serviceLocation.getLatitude();
                    longitude = serviceLocation.getLongitude();
                    Log.d("000258", " latitude: " + latitude);
                    Log.d("000258", " longitude: " + longitude);
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

                alertDialog = new Dialog(ctx);
                LayoutInflater layout = LayoutInflater.from(ctx);
                final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

                alertDialog.setContentView(dialogView);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();


                if (et_walid_ka_shanakti_card_number.getText().toString().isEmpty()) {

                    Khandan_Register();
                } else {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                Check_Khandan();

                                //  Child_Register();

                            } catch (Exception e) {
                                alertDialog.dismiss();
                                Log.d("000258", "Khandan Error" + e.getMessage());
                                // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 2000);

                }


                // update_data(); 
            }
        });

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


             //   ((TextView) parent.getChildAt(0)).setTextColor(ctx.getResources().getColor(R.color.grey_color));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Select sp_zila
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
                    Log.d("000258", "Tehsil: " + selDistrict);

                    String[][] tehsils = ls.executeReader("SELECT name FROM TEHSIL WHERE district_id = ( SELECT uid FROM DISTRICT WHERE name = '" + selDistrict + "')");
                    if (tehsils == null) {
                        // Toast.makeText(parent.getContext(), "There was an error fetching details.", Toast.LENGTH_LONG).show();
                    } else {
                        Utils.setSpinnerTehsel(VAC_BelowTwoProfileWithKhandan_Activity.this, sp_tahseel, tehsils);
                        sp_tahseel.setEnabled(true);
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
                    Log.d("000258", "UNIONCOUNCIL: " + selDistrict);
                    String[][] tehsils = ls.executeReader("SELECT name FROM UNIONCOUNCIL WHERE tehsil_id = ( SELECT uid FROM TEHSIL WHERE name = '" + selDistrict + "')");
                    if (tehsils == null) {
                        // Toast.makeText(parent.getContext(), "There was an error fetching details.", Toast.LENGTH_LONG).show();
                    } else {
                        Utils.setSpinnerTehsel(VAC_BelowTwoProfileWithKhandan_Activity.this, sp_union_council, tehsils);
                        sp_union_council.setEnabled(true);
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
        Utils.setSpinnerUnionCouncil(getApplicationContext(), sp_union_council);

        sp_union_council.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Lister ls = new Lister(getApplicationContext());
                    ls.createAndOpenDB();

                    String selDistrict = String.valueOf(sp_union_council.getSelectedItem());
                    Log.d("000258", "Village: " + selDistrict);
                    String[][] tehsils = ls.executeReader("SELECT name FROM VILLAGES WHERE uc_id = ( SELECT uid FROM UNIONCOUNCIL WHERE name = '" + selDistrict + "')");
                    if (tehsils == null) {
                        //  Toast.makeText(parent.getContext(), "There was an error fetching details.", Toast.LENGTH_LONG).show();
                    } else {
                        Utils.setSpinnerTehsel(VAC_BelowTwoProfileWithKhandan_Activity.this, sp_gaon, tehsils);
                        sp_gaon.setEnabled(true);
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


        ///////////////// Sp_gaon ////////////////////////
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

    public void Check_Khandan() {
        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            // String[][] mData_khandan = ls.executeReader("Select khandan_id from MEMBER where nicnumber = '" + et_walid_ka_shanakti_card_number.getText().toString() + "'");
            String q = "    SELECT t2.uid from MEMBER t1" +
                    "INNER JOIN KHANDAN t2 ON t1.khandan_id=t2.uid" +
                    "where t1.nicnumber = '" + et_walid_ka_shanakti_card_number.getText().toString() + "'";

            String[][] mData_khandan = ls.executeReader(q);

            Log.d("000258", "Check Query : " + q);
            Log.d("000258", "Khandan ID: " + mData_khandan[0][0]);

            khandan_uuid = mData_khandan[0][0].toString();
            // Log.d("000258", "khandan_uuid: " + khandan_uuid);

            if (mData_khandan != null) {

                update_data();
            } else {
            }

        } catch (Exception e) {
            Log.d("000258", "Check Khandan Err: " + e.getMessage());

            Khandan_Register();

        }
    }

    public void Khandan_Register() {


        try {

            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            TodayDate = dates.format(c.getTime());

            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            //Generate Random UUID
            //uuid = UUID.randomUUID().toString().replace("-", "");

            //Generate Khandan UID
            String khandauuid = UUID.randomUUID().toString().replace("-", "");
            khandan_uuid = khandauuid;

            JSONObject jobj = new JSONObject();

            jobj.put("lat", "" + String.valueOf(latitude));
            jobj.put("lng", "" + String.valueOf(longitude));

            String added_on = String.valueOf(System.currentTimeMillis());
            String ans1 = "insert into KHANDAN (manual_id, uid, province_id, district_id, subdistrict_id, uc_id, village_id, family_head_name, family_address, water_source, toilet_facility, added_by,is_synced,added_on)" +
                    "values" +
                    "(" +
                    "(SELECT 'VAC_' || substr('0000' || (SELECT count(*)+1 FROM KHANDAN), -4, 4))," +
                    "'" + khandan_uuid + "'," +
                    "(SELECT province_id FROM DISTRICT WHERE name = '" + String.valueOf(sp_zila.getSelectedItem()) + "' LIMIT 1)," +
                    "(SELECT uid FROM DISTRICT WHERE name = '" + String.valueOf(sp_zila.getSelectedItem()) + "' LIMIT 1)," +
                    "(SELECT uid FROM TEHSIL WHERE name = '" + String.valueOf(sp_tahseel.getSelectedItem()) + "' LIMIT 1)," +
                    "(SELECT uid FROM UNIONCOUNCIL WHERE name = '" + String.valueOf(sp_union_council.getSelectedItem()) + "' LIMIT 1)," +
                    "(SELECT uid FROM VILLAGES WHERE name = '" + String.valueOf(sp_gaon.getSelectedItem()) + "' LIMIT 1)," +
                    "'" + et_walid_ka_naam.getText().toString() + "'," +
                    "'" + "0" + "'," +
                    "'" + "-1" + "'," +
                    "'" + "-1" + "'," +
                    "'" + login_useruid + "'," +
                    "'0'," +
                    "'" + added_on + "'" +
                    ")";

            Boolean res = ls.executeNonQuery(ans1);
            Log.d("000258", "Insert Data: " + ans1);
            Log.d("000258", "Query " + res.toString());

            String[][] mData = ls.executeReader("SELECT province_id, district_id, subdistrict_id, uc_id, village_id,manual_id FROM KHANDAN WHERE uid = '" + khandan_uuid + "'");

            if (Utils.haveNetworkConnection(ctx) > 0) {

                sendPostRequest_khandan(mData[0][5], khandan_uuid, mData[0][0], mData[0][1], mData[0][2], mData[0][3], mData[0][4],
                        et_walid_ka_naam.getText().toString(), et_address.getText().toString(), "0", "0", login_useruid, added_on);
            } else {

            }

        } catch (Exception e) {
            alertDialog.dismiss();
            Log.d("000258", "Error Khandan Reg: " + e.getMessage());
        } finally {
            update_data();
        }

        /*} else {
            alertDialog.dismiss();
            Toast.makeText(ctx, "Kindly check internet connection.", Toast.LENGTH_SHORT).show();
        }*/
    }


    private void update_data() {

        try {
            String cur_added_on = String.valueOf(System.currentTimeMillis());

            if (VAC_Register_QRCode_Activity.switch_qr_code_values_vac.equalsIgnoreCase("1")) {

                SharedPreferences settings = getSharedPreferences("shared_QR_Value", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                // Reading from SharedPreferences
                qr_code_text = settings.getString("qr_code", "");
                editor.apply();
                Log.d("000258", "IF: " + qr_code_text);

            } else {
                qr_code_text = "none";
                Log.d("000258", "ELSE: " + qr_code_text);
            }


            if (jsonObject.has("vaination_card_number")) {
                Log.d("000258", "VAC REMOVE: ");
                jsonObject.remove("vaination_card_number");
            } else {
                Log.d("000258", "NO Vac Num : ");
            }

            if (jsonObject.has("lat")) {
                jsonObject.put("lat", "" + String.valueOf(latitude));
                jsonObject.put("lng", "" + String.valueOf(longitude));
                jsonObject.put("full_name", "" + et_bachey_ka_naam.getText().toString());
                jsonObject.put("mother_name", "" + et_walida_ka_naam.getText().toString());
                jsonObject.put("address", "" + et_address.getText().toString());
                jsonObject.put("dob", "" + et_tareekh_pedaish.getText().toString());
                jsonObject.put("age", "" + child_age.toString());
                jsonObject.put("nic", "" + et_walid_ka_shanakti_card_number.getText().toString());
                jsonObject.put("vaccination_card_number", et_vacination_card_number.getText().toString());
                jsonObject.put("update_record_date", "" + TodayDate);
                jsonObject.put("sp_zila", "" + String.valueOf(sp_zila.getSelectedItem()));
                jsonObject.put("sp_zila_pos", "" + String.valueOf(sp_zila.getSelectedItemPosition() - 1));
                jsonObject.put("sp_tahseel", "" + String.valueOf(sp_tahseel.getSelectedItem()));
                jsonObject.put("sp_tahseel_pos", "" + String.valueOf(sp_tahseel.getSelectedItemPosition() - 1));
                jsonObject.put("sp_unioncouncil", "" + String.valueOf(sp_union_council.getSelectedItem()));
                jsonObject.put("sp_unioncouncil_pos", "" + String.valueOf(sp_union_council.getSelectedItemPosition() - 1));
                jsonObject.put("sp_village", "" + String.valueOf(sp_gaon.getSelectedItem()));
                jsonObject.put("sp_village_pos", "" + String.valueOf(sp_gaon.getSelectedItemPosition() - 1));
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
                                "khandan_id='" + khandan_uuid + "'," +
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
                        Log.d("000258", "Data: " + update_record);
                        Log.d("000258", "Query: " + res.toString());

                        if (Utils.haveNetworkConnection(ctx) > 0) {

                            sendPostRequest(child_uid, child_uid, khandan_uuid, et_bachey_ka_naam.getText().toString(), et_walid_ka_shanakti_card_number.getText().toString(),
                                    et_mobile_number.getText().toString(), String.valueOf(sp_jins.getSelectedItemPosition() - 1), child_age, et_tareekh_pedaish.getText().toString(),
                                    "bio_code", qr_code_text, login_useruid, String.valueOf(jsonObject), added_on);
                        } else {
                            Toast.makeText(ctx, R.string.dataEdited, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        alertDialog.dismiss();
                        Log.d("000258", " Error" + e.getMessage());
                        // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    } finally {
                        finish();
                        VAC_Register_QRCode_Activity.switch_qr_code_values_vac = "0";
                        var_updateprofilekhandan_belowtwotemp = "1";
                    }

                }
            }, 2000);

        } catch (Exception e) {
            alertDialog.dismiss();
            //  Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
            Log.d("000258", " Error" + e.getMessage());
        }


    }

    private void sendPostRequest_khandan(final String manual_id, final String uid, final String province_id, final String district_id,
                                         final String subdistrict_id, final String uc_id, final String village_id, final String family_head_name,
                                         final String family_address, final String water_source, final String toilet_facility,
                                         final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/family/";


        Log.d("000258", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {


                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000258", "Khandan response    " + response);

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();
                        String update_record = "UPDATE KHANDAN SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE uid = '" + uid + "'";
                        ls.executeNonQuery(update_record);


                    } else {
                        alertDialog.dismiss();
                        Log.d("000258", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        // Toast.makeText(ctx, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {

                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alertDialog.dismiss();
                Log.d("000258", "onErrorResponse: " + error.getMessage());
                // Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();

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

                Log.d("000258", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000258", "map ");
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

        Log.d("000258", "mURL " + url);
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

                        Log.d("000258", "response:    " + response);

                        // Toast.makeText(VAC_BelowInsideOutsideUC_Activity.this, "Data has been saved", Toast.LENGTH_SHORT).show();

                        Lister ls = new Lister(VAC_BelowTwoProfileWithKhandan_Activity.this);
                        ls.createAndOpenDB();
                        String update_record = "UPDATE MEMBER SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE uid = '" + child_uid + "'";
                        ls.executeNonQuery(update_record);

                        Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(ctx,HomePageVacinator_Activity.class));

                    } else {
                        Log.d("000258", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(VAC_BelowInsideOutsideUC_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000258", "Err: " + e.getMessage());
                    // Toast.makeText(VAC_BelowInsideOutsideUC_Activity.this, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000258", "onErrorResponse: " + error.getMessage());
                // Toast.makeText(VAC_BelowInsideOutsideUC_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();

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

                Log.d("000258", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000258", "map ");
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


    public void read_profile() {
        try {
            final Lister ls = new Lister(ctx);
            ls.createAndOpenDB();


            mDatachild = ls.executeReader("Select uid,full_name, dob,gender,nicnumber,phone_number,data,khandan_id,added_on,qr_code,age from MEMBER where uid = '" + child_uid + "'");


            added_on = mDatachild[0][8];
            child_age = mDatachild[0][10];

            Log.d("000258", "uid: " + mDatachild[0][0]);
            Log.d("000258", "FullName: " + mDatachild[0][1]);
            Log.d("000258", "DOB: " + mDatachild[0][2]);
            Log.d("000258", "Gender: " + mDatachild[0][3]);
            Log.d("000258", "NicNumber: " + mDatachild[0][4]);
            Log.d("000258", "PhoneNumber: " + mDatachild[0][5]);
            Log.d("000258", "JSON Data: " + mDatachild[0][6]);
            Log.d("000258", "khandan_id: " + mDatachild[0][7]);
            Log.d("000258", "added_on: " + mDatachild[0][8]);
            Log.d("000258", "qr_code: " + mDatachild[0][9]);
            Log.d("000258", R.string.ageColon + mDatachild[0][10]);


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
            sp_tahseel.setEnabled(false);
            sp_union_council.setEnabled(false);
            sp_gaon.setEnabled(false);
            sp_zila.setEnabled(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image_arrow_drop_jins.setImageTintList(ctx.getResources().getColorStateList(R.color.grey_color));
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
                Log.d("000258", "NIC IFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
                et_walid_ka_shanakti_card_number.setText("");
            } else {
                Log.d("000258", "NIC ELSEEEEEEEEEEEEE");
                et_walid_ka_shanakti_card_number.setText(mDatachild[0][4]);
            }

            if (mDatachild[0][5].isEmpty()) {
                Log.d("000258", "MOBILE IFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
                et_mobile_number.setText("");
            } else {
                Log.d("000258", "M<OBILEEEEEEEEEEEEEEE ELSEEEEEEEEEEEEE");
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
                Log.d("000258", "QR IF");
                sw_qrcode_faal_kre.setChecked(false);
            } else if (mDatachild[0][9].equalsIgnoreCase("none")) {
                Log.d("000258", "QR ELSE IF");

                sw_qrcode_faal_kre.setChecked(false);
            } else {
                Log.d("000258", "QR ELSE");

                sw_qrcode_faal_kre.setChecked(true);
            }

            ls.closeDB();

        } catch (Exception e) {
            e.printStackTrace();

            Log.d("000258", "Error: " + e.getMessage());
            Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (VAC_Register_QRCode_Activity.switch_qr_code_values_vac.equalsIgnoreCase("1")) {

            sw_qrcode_faal_kre.setChecked(true);
            sw_qrcode_faal_kre.setEnabled(false);

            Log.d("000258", "IF");
        } else {

            Log.d("000258", "ELSE: ");
            if (mDatachild[0][9].equalsIgnoreCase("no_qr_code")) {
                Log.d("000258", "QR IF");

                sw_qrcode_faal_kre.setChecked(false);
            } else if (mDatachild[0][9].equalsIgnoreCase("none")) {
                Log.d("000258", "QR ELSE IF");

                sw_qrcode_faal_kre.setChecked(false);
            } else {
                Log.d("000258", "QR ELSE");
                sw_qrcode_faal_kre.setChecked(true);
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        VAC_Register_QRCode_Activity.switch_qr_code_values_vac = "0";
        var_updateprofilekhandan_belowtwotemp = "0";
    }
}







 /*     try{
                 final String mData_Khandan[][]=ls.executeReader("SELECT t1.full_name,t2.province_id,t2.district_id,t2.subdistrict_id,t2.uc_id,t2.village_id from MEMBER t1" +
                                       " LEFT JOIN KHANDAN t2 On t1.khandan_id=t2.uid" +
                                       " where t1.uid='"+child_uid+"'");

                       if (mData_Khandan[0][1]!=null)
                       {
                           Log.d("000258", "Province ID:" +mData_Khandan[0][1]);

                       }
                       else {
                           Log.d("000258", "Province NULL");
                       }

                if (mData_Khandan[0][2]!=null)
                {
                    Log.d("000258", "District UID:" +mData_Khandan[0][2]);
                    try {
                        // Select sp_zila
                        final String [][] district_name= ls.executeReader("Select name,id from DISTRICT where uid='" + mData_Khandan[0][2] + "' AND province_id='"+mData_Khandan[0][1]+"'");
                        Log.d("000258", "District Name: " + district_name[0][0]);
                        Log.d("000258", "District ID: " + district_name[0][1]);

                        sp_zila.setSelection(Integer.parseInt(district_name[0][1]));

                    }catch (Exception e0)
                    {
                        Log.d("000258", "Distr NAme Err:" +e0.getMessage());
                    }
                }
                else {
                    Log.d("000258", "District NULL");
                }

             /*   if (mData_Khandan[0][3]!=null)
                {
                    Log.d("000258", "Tehseel UID:" +mData_Khandan[0][3]);
                    try {
                        String subdistrict_name[][] = ls.executeReader("Select name,id from TEHSIL where uid='" + mData_Khandan[0][3] + "' AND province_id='"+mData_Khandan[0][1]+"'AND district_id='"+mData_Khandan[0][2]+"'");
                        Log.d("000258", "Tehseel Name: " + subdistrict_name[0][0]);
                        Log.d("000258", "Tehseel ID: " + subdistrict_name[0][1]);

                    }catch (Exception e0)
                    {
                        Log.d("000258", "Tehseel NAme Err:" +e0.getMessage());
                    }
                }
                else {
                    Log.d("000258", "Tehseel NULL");
                }
                if (mData_Khandan[0][4]!=null)
                {
                    Log.d("000258", "UC UID:" +mData_Khandan[0][4]);

                    try {
                        String uc_name[][] = ls.executeReader("Select name,id from UNIONCOUNCIL where uid='" + mData_Khandan[0][4] + "'AND province_id='"+mData_Khandan[0][1]+"'AND district_id='"+mData_Khandan[0][2]+"'AND tehsil_id='"+mData_Khandan[0][3]+"'");
                        Log.d("000258", "UC Name: " + uc_name[0][0]);
                        Log.d("000258", "UC ID: " + uc_name[0][1]);

                        sp_union_council.setSelection(Integer.parseInt(uc_name[0][1]));

                    }catch (Exception e0)
                    {
                        Log.d("000258", "UC NAme Err:" +e0.getMessage());
                    }
                }
                else {
                    Log.d("000258", "UC NULL");
                }

                if (mData_Khandan[0][5]!=null)
                {
                    Log.d("000258", "Village UID:" +mData_Khandan[0][5]);
                    try {
                        String village_name[][] = ls.executeReader("Select name,id from VILLAGES where uid='" + mData_Khandan[0][5] + "'AND province_id='"+mData_Khandan[0][1]+"'AND district_id='"+mData_Khandan[0][2]+"'AND tehsil_id='"+mData_Khandan[0][3]+"'AND uc_id='"+mData_Khandan[0][4]+"'");
                        Log.d("000258", "Village Name: " + village_name[0][0]);
                        Log.d("000258", "Village ID: " + village_name[0][1]);

                        sp_gaon.setSelection(Integer.parseInt(village_name[0][1]));

                    }catch (Exception e0)
                    {
                        Log.d("000258", "Village NAme Err:" +e0.getMessage());

                    }
                }
                else {
                    Log.d("000258", "Village  NULL");
                }


            }catch (Exception e){
                Log.d("000258", "Sp Error: " + e.getMessage());
            }*/