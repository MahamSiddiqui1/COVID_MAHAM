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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.CustomerAdapter;
import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.CustomClass.Customer;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.GPSTracker;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class VAC_BelowTwoRegister_Activity extends AppCompatActivity {

    Context ctx = VAC_BelowTwoRegister_Activity.this;
    String uuid,abc,peenay_kai_paani_ka_zarya, latrine_system;
    ImageView iv_navigation_drawer, iv_home, iv_close;
    Button btn_jamaa_kre, btn_jari_rhy;
    Spinner sp_gaon, sp_zila, sp_union_council, sp_tahseel, sp_jins,sp_peenay_kai_paani_ka_zarya, sp_latrine_system;;
    Switch sw_biometric_faal_kre, sw_qrcode_faal_kre;
    EditText et_peenay_kai_paani_ka_zarya, et_latrine_system,et_bachey_ka_naam, et_tareekh_pedaish, et_walid_ka_naam, et_walida_ka_naam,et_walid_ka_shanakti_card_number, et_mobile_number, et_vacination_card_number, et_address;
    double latitude;
    LinearLayout ll_latrine_system, ll_peenay_kai_paani;
    double longitude;
    // GPSTracker class
    GPSTracker gps;
    CustomerAdapter adapter = null;
    ArrayList<Customer> customers = null;

    private int mYear, mMonth, mDay;
    int date_for_condition = 0;
    int month_for_condition = 0;
    String qr_code_text = "noscan";
    public String hold_age_date_condition = "fromage";
    String monthf2, dayf2, yearf2 = "null", TodayDate, union_council, khandan_uuid;
    int age;

    Dialog alertDialog;
    Snackbar snackbar;

    ServiceLocation serviceLocation;
    String login_useruid;
    public static String var_regtemp_belowtwo="null";

    long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vac_below_two);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, VAC_BelowTwoRegister_Activity.class));

        // union_council = getIntent().getExtras().getString("union_council");

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences("UserLogin", 0); // 0 - for private mode
            String shared_useruid = prefelse.getString((R.string.loginUserIDEng), null); // getting String
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
        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);

        //Spinner
        sp_jins = findViewById(R.id.sp_jins);
        sp_tahseel = findViewById(R.id.sp_tahseel);
        sp_union_council = findViewById(R.id.sp_union_council);
        sp_gaon = findViewById(R.id.sp_gaon);
        sp_zila = findViewById(R.id.sp_zila);
        sp_tahseel.setEnabled(false);
        sp_union_council.setEnabled(false);
        sp_gaon.setEnabled(false);
        sp_zila.setEnabled(true);
        sp_latrine_system = findViewById(R.id.sp_latrine_system);
        sp_peenay_kai_paani_ka_zarya = findViewById(R.id.sp_peenay_kai_paani_ka_zarya);


        //Switch
        sw_biometric_faal_kre = findViewById(R.id.sw_biometric_faal_kre);
        sw_qrcode_faal_kre = findViewById(R.id.sw_qrcode_faal_kre);

        //Button
        btn_jamaa_kre = findViewById(R.id.submit);


        et_tareekh_pedaish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog();
            }
        });


        SpinnersData();


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
                Intent newIntent = new Intent(ctx, HomePage_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
            }
        });

        sw_biometric_faal_kre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Dialog_BiometricFaAlKre();
            }
        });

        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
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

                    //Toast.makeText(getApplicationContext(), "برائے مہربانی  نام درج کریں", Toast.LENGTH_SHORT).show();
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
                    //Toast.makeText(getApplicationContext(), "جنس منتخب کریں", Toast.LENGTH_SHORT).show();
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
                    if (et_walid_ka_shanakti_card_number.getText().toString().length() < 15){
                        //Toast.makeText(getApplicationContext(), "برائے مہربانی صحیح شناختی کارڈ نمبر درج کریں", Toast.LENGTH_SHORT).show();
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
                }
                else{
                    Log.d("000555","ELSE NIC NUMBER");
                }
                Log.d("000555","OKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");

                if (sp_zila.getSelectedItemPosition() == 0) {

                    //Toast.makeText(getApplicationContext(), "ضلع منتخب کریں", Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar.make(v, R.string.selectDistrict, Snackbar.LENGTH_SHORT);
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
                    final Snackbar snackbar = Snackbar.make(v, R.string.selectTehseel, Snackbar.LENGTH_SHORT);
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
                    final Snackbar snackbar = Snackbar.make(v, R.string.selectUC, Snackbar.LENGTH_SHORT);
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
                    final Snackbar snackbar = Snackbar.make(v, R.string.selectVillage, Snackbar.LENGTH_SHORT);
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

               if (age >= 4) {
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


                alertDialog = new Dialog(ctx);
                LayoutInflater layout = LayoutInflater.from(ctx);
                final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

                alertDialog.setContentView(dialogView);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();

                if (et_walid_ka_shanakti_card_number.getText().toString().isEmpty()) {

                    Khandan_Register(v);
                }
                else {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                Check_Khandan(v);

                                //  Child_Register();

                            } catch (Exception e) {
                                alertDialog.dismiss();
                                Log.d("000555", "Khandan Error" + e.getMessage());
                                // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 2000);

                }
            }
        });


    }

    public void Check_Khandan(View v) {
        try {
            Lister ls = new Lister(ctx);
           ls.createAndOpenDB();
            // String[][] mData_khandan = ls.executeReader("Select khandan_id from MEMBER where nicnumber = '" + et_walid_ka_shanakti_card_number.getText().toString() + "'");
            String q = "Select khandan_id from MEMBER where nicnumber = '" + et_walid_ka_shanakti_card_number.getText().toString() + "'";
            Log.d("000555", "Check Query : " + q);
            String[][] mData_khandan = ls.executeReader(q);
            Log.d("000555", "Khandan ID: " + mData_khandan[0][0]);

            khandan_uuid = mData_khandan[0][0].toString();
            // Log.d("000555", "khandan_uuid: " + khandan_uuid);

            if (mData_khandan != null) {

                Child_Register(v);
            } else {
            }

        } catch (Exception e) {
            Log.d("000555", "Check Khandan Err: " + e.getMessage());

            Khandan_Register(v);

        }
    }

    public void Khandan_Register(View v) {


        // if (isConnectingToInternet()) {

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
            Log.d("000555", "Insert Data: " + ans1);
            Log.d("000555", "Query " + res.toString());

            String[][] mData = ls.executeReader("SELECT province_id, district_id, subdistrict_id, uc_id, village_id,manual_id FROM KHANDAN WHERE uid = '" + khandan_uuid + "'");

            if (Utils.haveNetworkConnection(ctx) > 0) {

                sendPostRequest_khandan(mData[0][5], khandan_uuid, mData[0][0], mData[0][1], mData[0][2], mData[0][3], mData[0][4],
                        et_walid_ka_naam.getText().toString(), et_address.getText().toString(), "0", "0", login_useruid, added_on);
            } else {

            }


        } catch (Exception e) {
            alertDialog.dismiss();
            Log.d("000555", "Error Khandan Reg: " + e.getMessage());
        } finally {
            Child_Register(v);
        }

        /*} else {
            alertDialog.dismiss();
            Toast.makeText(ctx, "Kindly check internet connection.", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void sendPostRequest_khandan(final String manual_id, final String uid, final String province_id, final String district_id,
                                         final String subdistrict_id, final String uc_id, final String village_id, final String family_head_name,
                                         final String family_address, final String water_source, final String toilet_facility,
                                         final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/family/";


        Log.d("000555", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {


                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000555", "Khandan response    " + response);

                        Lister ls = new Lister(ctx);
                       ls.createAndOpenDB();
                        String update_record = "UPDATE KHANDAN SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE uid = '" + uid + "'";
                        ls.executeNonQuery(update_record);


                    } else {
                        alertDialog.dismiss();
                        Log.d("000555", "else ");
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
                Log.d("000555", "onErrorResponse: " + error.getMessage());
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

    public void Child_Register(View v) {


        try {

            Lister ls = new Lister(VAC_BelowTwoRegister_Activity.this);
           ls.createAndOpenDB();

            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            TodayDate = dates.format(c.getTime());


            if (VAC_Register_QRCode_Activity.switch_qr_code_values_vac.equalsIgnoreCase("1")) {

                SharedPreferences settings = getSharedPreferences(getString(R.string.shared_QR_Value), MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                // Reading from SharedPreferences
                qr_code_text = settings.getString("qr_code", "");
                editor.apply();
                Log.d("000555", "IF: " + qr_code_text);

            } else {
                qr_code_text = "none";
                Log.d("000555", "ELSE: " + qr_code_text);
            }

            //    Toast.makeText(getApplicationContext(),qr_code_text,Toast.LENGTH_SHORT).show();
            JSONObject jobj = new JSONObject();
            jobj.put("lat", "" + String.valueOf(latitude));
            jobj.put("lng", "" + String.valueOf(longitude));
            jobj.put("status_death", "" + "0");
            jobj.put("deliv_year", "" + "0");
            jobj.put("deliv_month", "" + "0");
            jobj.put("member_type", "" + "unlisted");
            jobj.put("type", "" + "vac_belowtwo");
            jobj.put("maritalstatus", "" + "-1");//spinner
            jobj.put("reason_of_transfer", "" + "0");
            jobj.put("date_of_transfer", "" + "0");
            jobj.put("reason_of_death", "" + "0");
            jobj.put("date_of_death", "" + "0");
            jobj.put("dob", "" + et_tareekh_pedaish.getText().toString());
            jobj.put("relation_with_hof", "" + "0");
            jobj.put("full_name", "" + et_bachey_ka_naam.getText().toString());
            jobj.put("mother_name", "" + et_walida_ka_naam.getText().toString());
            jobj.put("father_name", "" + et_walid_ka_naam.getText().toString());
            jobj.put("address", "" + et_address.getText().toString());
            jobj.put("nic", "" + et_walid_ka_shanakti_card_number.getText().toString());
            jobj.put("khandan_number_manual", "" + "0");
            jobj.put("age", "" + age);
            jobj.put("manual_id", "" + "0");
            jobj.put("union_council", "" + union_council);
            jobj.put("vaccination_card_number", et_vacination_card_number.getText().toString());
            jobj.put("record_date", "" + TodayDate);
            jobj.put("sp_zila", "" + String.valueOf(sp_zila.getSelectedItem()));
            jobj.put("sp_zila_pos", "" + String.valueOf(sp_zila.getSelectedItemPosition() - 1));
            jobj.put("sp_tahseel", "" + String.valueOf(sp_tahseel.getSelectedItem()));
            jobj.put("sp_tahseel_pos", "" + String.valueOf(sp_tahseel.getSelectedItemPosition() - 1));
            jobj.put("sp_unioncouncil", "" + String.valueOf(sp_union_council.getSelectedItem()));
            jobj.put("sp_unioncouncil_pos", "" + String.valueOf(sp_union_council.getSelectedItemPosition() - 1));
            jobj.put("sp_village", "" + String.valueOf(sp_gaon.getSelectedItem()));
            jobj.put("sp_village_pos", "" + String.valueOf(sp_gaon.getSelectedItemPosition() - 1));

            // jobjMain.put("data", jobj);
            uuid = UUID.randomUUID().toString().replace("-", "");
            //String uuid = UUID.randomUUID().toString().replace("-","");


            String added_on = String.valueOf(System.currentTimeMillis());

            String ans1 = "insert into MEMBER (manual_id, uid, khandan_id, full_name, nicnumber, phone_number, gender, age, dob, bio_code, qr_code, added_by, data,is_synced,added_on)" +
                    "values" +
                    "(" +
                    "'" + uuid + "'," +
                    "'" + uuid + "'," +
                    "'" + khandan_uuid + "'," +
                    "'" + et_bachey_ka_naam.getText().toString() + "'," +
                    "'" + et_walid_ka_shanakti_card_number.getText().toString() + "'," +
                    "'" + et_mobile_number.getText().toString() + "'," +
                    "'" + String.valueOf(sp_jins.getSelectedItemPosition() - 1) + "'," +
                    "'" + age + "'," +
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
                final Snackbar snackbar = Snackbar.make(v, "رکن رجسٹر ہوگیا ہے.", Snackbar.LENGTH_SHORT);
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

                    sendPostRequest(uuid, uuid, khandan_uuid, et_bachey_ka_naam.getText().toString(), et_walid_ka_shanakti_card_number.getText().toString(),
                            et_mobile_number.getText().toString(), String.valueOf(sp_jins.getSelectedItemPosition() - 1), String.valueOf(age), et_tareekh_pedaish.getText().toString(),
                            "bio_code", qr_code_text, login_useruid, String.valueOf(jobj), added_on);
                } else {
                    //Toast.makeText(ctx, "ڈیٹا رجسٹر ہوگیا ہے", Toast.LENGTH_SHORT).show();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                        Intent intent = new Intent(ctx, VAC_Child_HifazitiTeekeyRecordList2_Activity.class);
                        intent.putExtra("u_id",uuid);
                        intent.putExtra("child_name", et_bachey_ka_naam.getText().toString());
                        intent.putExtra("child_gender", String.valueOf(sp_jins.getSelectedItemPosition() - 1));
                        startActivity(intent);
                        VAC_Register_QRCode_Activity.switch_qr_code_values_vac = "0";
                        var_regtemp_belowtwo = "1";
                    }
                },2000);
            }
            else
            {
                final Snackbar snackbar = Snackbar.make(v, R.string.noFamMemReg, Snackbar.LENGTH_SHORT);
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
            Log.d("000555", "Err: " + e.getMessage());
        } finally {

           /*Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                    Intent intent = new Intent(ctx, VAC_Child_HifazitiTeekeyRecordList2_Activity.class);
                    intent.putExtra("u_id",uuid);
                    intent.putExtra("child_name", et_bachey_ka_naam.getText().toString());
                    intent.putExtra("child_gender", String.valueOf(sp_jins.getSelectedItemPosition() - 1));
                    startActivity(intent);
                    VAC_Register_QRCode_Activity.switch_qr_code_values_vac = "0";
                    var_regtemp_belowtwo = "1";
                }
            },1500);*/

                   /* Intent intent = new Intent(ctx, VAC_Child_HifazitiTeekeyRecordList_Activity.class);
                    intent.putExtra("u_id", uuid);
                    intent.putExtra("child_name", et_bachey_ka_naam.getText().toString());
                    intent.putExtra("child_gender", String.valueOf(sp_jins.getSelectedItemPosition() - 1));
                    //intent.putExtra("child_age", mDatachild[childPosition][9]);
                    startActivity(intent);*/
        }
    }


    private void sendPostRequest(final String manual_id, final String uid, final String khandan_id, final String full_name,
                                 final String nicnumber, final String phone_number, final String gender, final String age, final String dob,
                                 final String bio_code, final String qr_code, final String added_by,
                                 final String data, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/family/members";
//        String url = "https://development.api.teekoplus.akdndhrc.org/sync-adapter";

        Log.d("000555", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {
                    //Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000555", "response:    " + response);

                        // Toast.makeText(VAC_BelowTwoRegister_Activity.this, "Data has been saved", Toast.LENGTH_SHORT).show();

                        Lister ls = new Lister(VAC_BelowTwoRegister_Activity.this);
                       ls.createAndOpenDB();
                        String update_record = "UPDATE MEMBER SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE uid = '" + uuid + "'";
                        ls.executeNonQuery(update_record);
                        Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(ctx,HomePageVacinator_Activity.class));

                    } else {
                        alertDialog.dismiss();
                        Log.d("000555", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(VAC_BelowTwoRegister_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    alertDialog.dismiss();
                    Log.d("000555", "Err: " + e.getMessage());
                    // Toast.makeText(VAC_BelowTwoRegister_Activity.this, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alertDialog.dismiss();
                Log.d("000555", "onErrorResponse: " + error.getMessage());
                // Toast.makeText(VAC_BelowTwoRegister_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
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


    public void ShowDialog() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(VAC_BelowTwoRegister_Activity.this, R.style.DatePickerDialog,
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

                        date_for_condition = Integer.parseInt(dayf2);
                        month_for_condition = Integer.parseInt(monthf2);

                        hold_age_date_condition = "fromdate";


                        Calendar dob = Calendar.getInstance();
                        Calendar today = Calendar.getInstance();

                        dob.set(year, monthOfYear, dayOfMonth);

                        age = today.get(Calendar.YEAR) - year;
                        Log.d("000555", R.string.ageColon + age);

                        Integer ageInt = new Integer(age);
                        String ageS = ageInt.toString();
                        //Toast.makeText(getApplicationContext(),String.valueOf(year)+"major" + ageS +"age",Toast.LENGTH_SHORT).show();
//                        et_tareekh_pedaish.setText(ageS);
                        //Toast.makeText(getContext(),DateTwoOneval,Toast.LENGTH_LONG).show();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();


    }

    public void Dialog_BiometricFaAlKre() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.dialog_biometric_faal_kre_layout, null);

        final AlertDialog alertD = new AlertDialog.Builder(this).create();

        iv_close = (ImageView) promptView.findViewById(R.id.iv_close);
        btn_jari_rhy = (Button) promptView.findViewById(R.id.btn_jari_rhy);


        alertD.setView(promptView);

        alertD.setCanceledOnTouchOutside(false);
        alertD.setCancelable(false);
        alertD.show();


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
            }
        });

        btn_jari_rhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertD.dismiss();


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
                        Utils.setSpinnerTehsel(VAC_BelowTwoRegister_Activity.this, sp_union_council, tehsils);
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
   /*     final ArrayAdapter<CharSequence> adptr_union = ArrayAdapter.createFromResource(this, R.array.array_union_council, android.R.layout.simple_spinner_item);
        adptr_union.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        sp_union_council.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_union,
                        R.layout.spinner_union_council_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
*/
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
                        Utils.setSpinnerTehsel(VAC_BelowTwoRegister_Activity.this, sp_gaon, tehsils);
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


        // Select sp_zila
   /*     final ArrayAdapter<CharSequence> adptr_zila = ArrayAdapter.createFromResource(this, R.array.array_zila, android.R.layout.simple_spinner_item);
        adptr_zila.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        sp_zila.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_zila,
                        R.layout.spinner_zila_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
*/

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
                        Utils.setSpinnerTehsel(VAC_BelowTwoRegister_Activity.this, sp_tahseel, tehsils);
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


        // Select sp_gaon
  /*      final ArrayAdapter<CharSequence> adptr_gaon = ArrayAdapter.createFromResource(this, R.array.array_gaon, android.R.layout.simple_spinner_item);
        adptr_gaon.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        sp_gaon.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_gaon,
                        R.layout.spinner_gaon_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));
*/

        Utils.setSpinnervillage(getApplicationContext(), sp_gaon);
        sp_gaon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


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


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (VAC_Register_QRCode_Activity.switch_qr_code_values_vac.equalsIgnoreCase("1")) {

            sw_qrcode_faal_kre.setChecked(true);
            sw_qrcode_faal_kre.setEnabled(false);

            Log.d("000555", "IF");
        } else {

            Log.d("000555", "ELSE: ");
            sw_qrcode_faal_kre.setChecked(false);

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ctx, HomePageVacinator_Activity.class);
        startActivity(intent);
//        VAC_ClassListener.mSlideMenu.close(false);
        VAC_Register_QRCode_Activity.switch_qr_code_values_vac = "0";
        var_regtemp_belowtwo = "0";

    }
}
