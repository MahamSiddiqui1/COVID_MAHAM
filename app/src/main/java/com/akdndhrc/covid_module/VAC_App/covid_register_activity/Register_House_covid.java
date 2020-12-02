package com.akdndhrc.covid_module.VAC_App.covid_register_activity;

import android.app.ProgressDialog;
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
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.GPSTracker;
import com.akdndhrc.covid_module.VAC_App.HomePageVacinator_Activity;
import com.akdndhrc.covid_module.VAC_App.VAC_InsideOutsideUC.VAC_Register_QRCode_Activity;
import com.akdndhrc.covid_module.VAC_App.covid_register_activity.Add_Family_Member_covid;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Register_House_covid extends AppCompatActivity {

    Context ctx = Register_House_covid.this;

    EditText et_khandan_ka_number, et_khandan_kai_sarbarah_ka_naam, et_mukamal_pata, et_peenay_kai_paani_ka_zarya, et_latrine_system, et_khandan_kai_sarbarah_ka_nicnumber, et_khandan_kai_sarbarah_ka_mobile_number;
    Button btn_jamaa_kre;
    RelativeLayout rl_home_image, rl_navigation_drawer;
    Spinner sp_gaon, sp_zila, sp_union_council, sp_tahseel, sp_peenay_kai_paani_ka_zarya, sp_latrine_system;
    GPSTracker gps;
    double latitude;
    double longitude;
    String uuid, peenay_kai_paani_ka_zarya, latrine_system, abc;

    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid;
    LinearLayout ll_latrine_system, ll_peenay_kai_paani;

    ProgressDialog progressDialog;
    long mLastClickTime = 0;

    Lister ls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Register_House_covid.class));



        ls = new Lister(ctx);
        ls.createAndOpenDB();


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

        //check_gps();

        //EditText
        et_khandan_ka_number = findViewById(R.id.et_khandan_ka_number);
        et_khandan_kai_sarbarah_ka_naam = findViewById(R.id.et_khandan_kai_sarbarah_ka_naam);
        et_khandan_kai_sarbarah_ka_nicnumber = findViewById(R.id.et_khandan_kai_sarbarah_ka_nicnumber);
        et_khandan_kai_sarbarah_ka_mobile_number = findViewById(R.id.et_khandan_kai_sarbarah_ka_mobile_number);
        et_mukamal_pata = findViewById(R.id.et_mukamal_pata);
       // et_peenay_kai_paani_ka_zarya = findViewById(R.id.et_peenay_kai_paani_ka_zarya);
        //et_latrine_system = findViewById(R.id.et_latrine_system);

        //Spinner
        sp_tahseel = findViewById(R.id.sp_tahseel);
        sp_union_council = findViewById(R.id.sp_union_council);
        sp_gaon = findViewById(R.id.sp_gaon);
        sp_zila = findViewById(R.id.sp_zila);
        sp_latrine_system = findViewById(R.id.sp_latrine_system);
        sp_peenay_kai_paani_ka_zarya = findViewById(R.id.sp_peenay_kai_paani_ka_zarya);

        sp_tahseel.setEnabled(false);
        sp_union_council.setEnabled(false);
        sp_gaon.setEnabled(false);
        sp_zila.setEnabled(true);

        //Button
        btn_jamaa_kre = findViewById(R.id.submit);

        //RelativeLayout
        rl_navigation_drawer = findViewById(R.id.rl_navigation_drawer);
        rl_home_image = findViewById(R.id.rl_home_image);
        rl_navigation_drawer.setVisibility(View.GONE);
        rl_home_image.setVisibility(View.GONE);


        //LinearLayout
        ll_latrine_system = findViewById(R.id.ll_latrine_system);
        ll_peenay_kai_paani = findViewById(R.id.ll_peenay_kai_paani);


        rl_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, R.string.navigation, Toast.LENGTH_SHORT).show();
            }
        });

        rl_home_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Home", Toast.LENGTH_SHORT).show();
            }
        });


        //Edit Person CNIC
        et_khandan_kai_sarbarah_ka_nicnumber.addTextChangedListener(new TextWatcher() {
            int previous = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previous = et_khandan_kai_sarbarah_ka_nicnumber.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if ((previous < length) && (length == 5 || length == 13)) {
                    String data = et_khandan_kai_sarbarah_ka_nicnumber.getText().toString();
                    et_khandan_kai_sarbarah_ka_nicnumber.setText(data + "-");
                    et_khandan_kai_sarbarah_ka_nicnumber.setSelection(length + 1);
                }
            }
        });


        /////Mobile Number
        et_khandan_kai_sarbarah_ka_mobile_number.addTextChangedListener(new TextWatcher() {
            int prev = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prev = et_khandan_kai_sarbarah_ka_mobile_number.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if ((prev < length) && (length == 4)) {
                    String data = et_khandan_kai_sarbarah_ka_mobile_number.getText().toString();
                    et_khandan_kai_sarbarah_ka_mobile_number.setText(data + "-");
                    et_khandan_kai_sarbarah_ka_mobile_number.setSelection(length + 1);
                }
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
                    Log.d("RegisterHouse", selDistrict);
                    String[][] tehsils = ls.executeReader("SELECT name FROM UNIONCOUNCIL WHERE tehsil_id = ( SELECT uid FROM TEHSIL WHERE name = '" + selDistrict + "')");
                    if (tehsils == null) {
                        // Toast.makeText(parent.getContext(), "There was an error fetching details.", Toast.LENGTH_LONG).show();
                    } else {
                        Utils.setSpinnerTehsel(Register_House_covid.this, sp_union_council, tehsils);
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
                    // SQLiteDatabase.loadLibs(ctx);
                    // ls.createAndOpenDB("test123");
                    ls.createAndOpenDB();

                    String selDistrict = String.valueOf(sp_union_council.getSelectedItem());
                    Log.d("RegisterHouse", selDistrict);
                    String[][] tehsils = ls.executeReader("SELECT name FROM VILLAGES WHERE uc_id = ( SELECT uid FROM UNIONCOUNCIL WHERE name = '" + selDistrict + "')");
                    if (tehsils == null) {
                        //  Toast.makeText(parent.getContext(), "There was an error fetching details.", Toast.LENGTH_LONG).show();
                    } else {
                        Utils.setSpinnerTehsel(Register_House_covid.this, sp_gaon, tehsils);
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
                    String[][] tehsils = ls.executeReader("SELECT name FROM TEHSIL WHERE district_id = ( SELECT uid FROM DISTRICT WHERE name = '" + selDistrict + "')");
                    if (tehsils == null) {
                        // Toast.makeText(parent.getContext(), "There was an error fetching details.", Toast.LENGTH_LONG).show();
                    } else {
                        Utils.setSpinnerTehsel(Register_House_covid.this, sp_tahseel, tehsils);
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

        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (et_khandan_ka_number.getText().toString().isEmpty()) {
                    final Snackbar snackbar = Snackbar.make(v, "Please enter a family number.", Snackbar.LENGTH_SHORT);
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


                if (et_khandan_kai_sarbarah_ka_naam.getText().toString().isEmpty()) {
                    final Snackbar snackbar = Snackbar.make(v, "Please enter the name of the head of the family.", Snackbar.LENGTH_SHORT);
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


                if (sp_zila.getSelectedItemPosition() == 0) {
                    //Toast.makeText(getApplicationContext(), "ضلع منتخب کریں", Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar.make(v, "Select district.", Snackbar.LENGTH_SHORT);
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
                    final Snackbar snackbar = Snackbar.make(v, "Select Tehsil.", Snackbar.LENGTH_SHORT);
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
                    final Snackbar snackbar = Snackbar.make(v, "Select the Union Council.", Snackbar.LENGTH_SHORT);
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

                    final Snackbar snackbar = Snackbar.make(v, "Select the village.", Snackbar.LENGTH_SHORT);
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

                try
                {
                    /*Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/

                    String[][] check_khandan_number = ls.executeReader("Select count(*) from KHANDAN where manual_id = '"+et_khandan_ka_number.getText().toString()+"'");
                    Log.d("000555", " Khandan Number Count: " + check_khandan_number[0][0]);
                    if (Integer.parseInt(check_khandan_number[0][0]) > 0)
                    {
                        Log.d("000555", " Khandan Number EXISTS ---------");

                        final Snackbar snackbar = Snackbar.make(v, "Family number already exists.", Snackbar.LENGTH_SHORT);
                        View mySbView = snackbar.getView();
                        mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                        TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(15);
                        snackbar.setDuration(4000);
                        snackbar.show();
                        return;
                    }
                    else {
                        Log.d("000555", " Khandan Number NOT EXISTS ******");
                    }

                }catch (Exception e)
                {
                    Log.d("000555", " Check Khandan Number Error: " + e.getMessage());
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
                }


                try {



                   /* Lister ls = new Lister(Register_House.this);
                    ls.createAndOpenDB();*/

                    uuid = UUID.randomUUID().toString().replace("-", "");
                    // khandan_uuid = uuid;


                    SharedPreferences settings = getSharedPreferences("Khandanuuid", MODE_PRIVATE);
                    // Writing data to SharedPreferences
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("k_uuid", uuid);
                    editor.putString("k_uuid_m", et_khandan_ka_number.getText().toString());
                    editor.commit();

                    JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("family_head_nicnumber", "" + et_khandan_kai_sarbarah_ka_nicnumber.getText().toString());
                    jobj.put("family_head_mobilenumber", "" + et_khandan_kai_sarbarah_ka_mobile_number.getText().toString());

                    if (sp_peenay_kai_paani_ka_zarya.getSelectedItemPosition() == 5) {
                        peenay_kai_paani_ka_zarya = String.valueOf(sp_peenay_kai_paani_ka_zarya.getSelectedItemPosition() - 1);
                        peenay_kai_paani_ka_zarya = peenay_kai_paani_ka_zarya + "|" + et_peenay_kai_paani_ka_zarya.getText().toString();
                        Log.d("000555", "pene value IFFFFFFFFFF: " + peenay_kai_paani_ka_zarya);
                    } else {
                        peenay_kai_paani_ka_zarya = String.valueOf(sp_peenay_kai_paani_ka_zarya.getSelectedItemPosition() - 1);
                        peenay_kai_paani_ka_zarya = peenay_kai_paani_ka_zarya + "|" + "none";
                        Log.d("000555", "pene value ELSEEEEEEEEEEE: " + peenay_kai_paani_ka_zarya);
                    }

                    if (sp_latrine_system.getSelectedItemPosition() == 3) {
                        latrine_system = String.valueOf(sp_latrine_system.getSelectedItemPosition() - 1);
                        latrine_system = latrine_system + "|" + et_latrine_system.getText().toString();
                        Log.d("000555", "latrine_system IFFFFFFFFFF: " + latrine_system);
                    } else {
                        latrine_system = String.valueOf(sp_latrine_system.getSelectedItemPosition() - 1);
                        latrine_system = latrine_system + "|" + "none";
                        Log.d("000555", "latrine_system value ELSEEEEEEEEEEE: " + latrine_system);
                    }

                    String address;
                    if (et_mukamal_pata.getText().toString().isEmpty()) {

                        address = "-";
                    } else {
                        address = et_mukamal_pata.getText().toString();
                    }

                    String k_cnic_number;
                    if (et_khandan_kai_sarbarah_ka_nicnumber.getText().toString().isEmpty()) {

                        k_cnic_number = "-";
                    } else {
                        k_cnic_number = et_khandan_kai_sarbarah_ka_nicnumber.getText().toString();
                    }

                    String k_mobile_number;
                    if (et_khandan_kai_sarbarah_ka_mobile_number.getText().toString().isEmpty()) {
                        k_mobile_number = "-";
                    } else {
                        k_mobile_number = et_khandan_kai_sarbarah_ka_mobile_number.getText().toString();
                    }



                    String added_on = String.valueOf(System.currentTimeMillis());

                    String ans1 = "insert into KHANDAN (manual_id, uid, province_id, district_id, subdistrict_id, uc_id, village_id, family_head_name, family_address, water_source, toilet_facility, added_by,is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + et_khandan_ka_number.getText().toString() + "'," +
                            "'" + uuid + "'," +
                            "(SELECT province_id FROM DISTRICT WHERE name = '" + String.valueOf(sp_zila.getSelectedItem()) + "' LIMIT 1)," +
                            "(SELECT uid FROM DISTRICT WHERE name = '" + String.valueOf(sp_zila.getSelectedItem()) + "' LIMIT 1)," +
                            "(SELECT uid FROM TEHSIL WHERE name = '" + String.valueOf(sp_tahseel.getSelectedItem()) + "' LIMIT 1)," +
                            "(SELECT uid FROM UNIONCOUNCIL WHERE name = '" + String.valueOf(sp_union_council.getSelectedItem()) + "' LIMIT 1)," +
                            "(SELECT uid FROM VILLAGES WHERE name = '" + String.valueOf(sp_gaon.getSelectedItem()) + "' LIMIT 1)," +
                            "'" + et_khandan_kai_sarbarah_ka_naam.getText().toString() + "'," +
                            "'" + address+ "|" + k_cnic_number + "|" + k_mobile_number + "'," +
                            "'" + peenay_kai_paani_ka_zarya + "'," +
                            "'" + latrine_system + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000555", ans1);
                    Log.d("00055", res.toString());


                   /* if (res.toString().equalsIgnoreCase("true"))
                    {
                        Toast tt = Toast.makeText(ctx,"خاندان رجسٹر ہوگیا ہے", Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();

                        String[][] mData = ls.executeReader("SELECT province_id, district_id, subdistrict_id, uc_id, village_id FROM KHANDAN WHERE uid = '" + uuid + "'");
                        if (Utils.haveNetworkConnection(ctx) > 0) {
                            sendPostRequest(et_khandan_ka_number.getText().toString(), uuid, mData[0][0], mData[0][1], mData[0][2], mData[0][3], mData[0][4],
                                    et_khandan_kai_sarbarah_ka_naam.getText().toString(), et_mukamal_pata.getText().toString(), peenay_kai_paani_ka_zarya,
                                    latrine_system, login_useruid, added_on);
                        } else {
                           /* sendSMSMethod(et_khandan_ka_number.getText().toString(), uuid, mData[0][0], mData[0][1], mData[0][2], mData[0][3], mData[0][4],
                                    et_khandan_kai_sarbarah_ka_naam.getText().toString(), et_mukamal_pata.getText().toString(), peenay_kai_paani_ka_zarya,
                                    latrine_system, login_useruid, added_on);*/
                            //
               /*         }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(Register_House.this, Add_Family_Member_Activity.class));
                                Log.d("000951", "ABC");
                            }
                        },1000);
                    }
                    else{
                        Toast tt = Toast.makeText(ctx,"خاندان رجسٹر نہیں ہوا", Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();
                    }*/



                    if (res.toString().equalsIgnoreCase("true"))
                    {

                        final Snackbar snackbar = Snackbar.make(v, "The family is registered.", Snackbar.LENGTH_SHORT);
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


                        String[][] mData = ls.executeReader("SELECT province_id, district_id, subdistrict_id, uc_id, village_id FROM KHANDAN WHERE uid = '" + uuid + "'");
                        if (Utils.haveNetworkConnection(ctx) > 0) {
                            sendPostRequest(et_khandan_ka_number.getText().toString(), uuid, mData[0][0], mData[0][1], mData[0][2], mData[0][3], mData[0][4],
                                    et_khandan_kai_sarbarah_ka_naam.getText().toString(), et_mukamal_pata.getText().toString(), peenay_kai_paani_ka_zarya,
                                    latrine_system, login_useruid, added_on);
                        } else {
                           /* sendSMSMethod(et_khandan_ka_number.getText().toString(), uuid, mData[0][0], mData[0][1], mData[0][2], mData[0][3], mData[0][4],
                                    et_khandan_kai_sarbarah_ka_naam.getText().toString(), et_mukamal_pata.getText().toString(), peenay_kai_paani_ka_zarya,
                                    latrine_system, login_useruid, added_on);*/
                            //
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                startActivity(new Intent(Register_House_covid.this, Add_Family_Member_covid.class));
                            }
                        },2000);
                    }
                    else
                    {
                        final Snackbar snackbar = Snackbar.make(v, "The family was not registered.", Snackbar.LENGTH_SHORT);
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
                    Log.d("000555", "Err: " + e.getMessage());
                }
            }
        });
    }

    private void sendPostRequest(final String manual_id, final String uid, final String province_id, final String district_id,
                                 final String subdistrict_id, final String uc_id, final String village_id, final String family_head_name,
                                 final String family_address, final String water_source, final String toilet_facility,
                                 final String added_by, final String added_on) {


        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/family/";

        Log.d("000951", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {
                    // Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000951", "Response:    " + response);

                     /*   Lister ls = new Lister(Register_House.this);
                        ls.createAndOpenDB();*/

                        String update_record = "UPDATE KHANDAN SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE uid = '" + uuid + "'";
                        ls.executeNonQuery(update_record);

                        // Toast.makeText(Register_House.this, "Data has been saved", Toast.LENGTH_SHORT).show();
                        Toast tt  =Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();

                    } else {
                        Log.d("000951", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        //  Toast.makeText(Register_House.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, "Data service not synced", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", "Err: " + e.getMessage());
                    //Toast.makeText(Register_House.this, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();
                    Toast.makeText(ctx, "Data not synced", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000951", "onErrorResponse: " + error.getMessage());
                //Toast.makeText(Register_House.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast tt  = Toast.makeText(ctx, "Data not synced", Toast.LENGTH_SHORT);
                tt.setGravity(Gravity.CENTER, 0, 0);
                tt.show();

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

                Log.d("000951", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000951", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }


   /* public void sendSMSMethod(final String manual_id, final String uid, final String province_id, final String district_id,
                              final String subdistrict_id, final String uc_id, final String village_id, final String family_head_name,
                              final String family_address, final String water_source, final String toilet_facility,
                              final String added_by, final String added_on)  {

        try {


            JSONObject jobj = new JSONObject();
            jobj.put("manual_id", manual_id);
            jobj.put("uid", uid);
            jobj.put("province_id", province_id);
            jobj.put("district_id", district_id);
            jobj.put("subdistrict_id", subdistrict_id);
            jobj.put("uc_id", uc_id);
            jobj.put("village_id", village_id);
            jobj.put("family_head_name", family_head_name);
            jobj.put("family_address", family_address);
            jobj.put("water_source", water_source);
            jobj.put("toilet_facility", toilet_facility);
            jobj.put("added_by", added_by);
            jobj.put("added_on", added_on);

            String toencode_sms = getBase64String(String.valueOf(jobj));

            String uuid = UUID.randomUUID().toString().replace("-","");
            String sms_data =   "HayatPK" +"|"+uuid + "|" + "KHANDAN_TABLE" + "|" + toencode_sms;
            Log.d("000951", "SMS DATA:  " + sms_data);


            TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            int simState = telMgr.getSimState();
            switch (simState) {
                case TelephonyManager.SIM_STATE_ABSENT:
                    // do something
                    //Toast.makeText(getApplicationContext(), "Data is not synced, there is no sim in your phone.", Toast.LENGTH_LONG).show();

                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.sync_khandan_layout), "آپ کے فون میں کوئی سم موجود نہیں.", Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(16);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                    snackbar.setDuration(3000);
                    snackbar.show();


                    break;
                case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                    // do something
                        Toast.makeText(getApplicationContext(), "SIM_STATE_NETWORK_LOCKED", Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                    // do something
                    Toast.makeText(getApplicationContext(), "SIM_STATE_PIN_REQUIRED", Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                    // do something
                    Toast.makeText(getApplicationContext(), "SIM_STATE_PUK_REQUIRED", Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.USSD_RETURN_FAILURE:
                    Toast.makeText(getApplicationContext(), "USSD_RETURN_FAILURE", Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.USSD_ERROR_SERVICE_UNAVAIL:
                    Toast.makeText(getApplicationContext(), "USSD_ERROR_SERVICE_UNAVAIL", Toast.LENGTH_LONG).show();

                    break;

                case TelephonyManager.SIM_STATE_READY:
                    // do something

                    Log.d("000951", "3");
                    progressDialog = new ProgressDialog(Register_House.this,
                            android.R.style.Theme_DeviceDefault_Light_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();

                    ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
                    ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
                    Log.d("000951", "4");

                    PendingIntent piSent = PendingIntent.getBroadcast(Register_House.this, 0, new Intent("SMS_SENT"), 0);
                    PendingIntent piDelivered = PendingIntent.getBroadcast(Register_House.this, 0, new Intent("SMS_DELIVERED"), 0);
                    // sms.sendTextMessage(phone, null, message, piSent, piDelivered);
                    try {
                        SmsManager sms = SmsManager.getDefault();
                        ArrayList<String> mSMSMessage = sms.divideMessage(sms_data);

                        for (int in = 0; in < mSMSMessage.size(); in++) {
                            sentPendingIntents.add(in, piSent);
                            deliveredPendingIntents.add(in, piDelivered);
                        }

                       int numberofsmsDel = mSMSMessage.size();
                        sms.sendMultipartTextMessage("03432350528", null, mSMSMessage, sentPendingIntents, deliveredPendingIntents);

                        Log.d("000951", "SMS SENT");
                        Toast.makeText(this, "SMS has been sent.", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Log.d("000951", "Exception Sending faild " + e);
                        e.printStackTrace();
                        Toast.makeText(Register_House.this, "SMS sending failed...", Toast.LENGTH_SHORT).show();
                    }

                    break;

                case TelephonyManager.SIM_STATE_UNKNOWN:
                    // do something
                    Toast.makeText(getApplicationContext(), "SIM_STATE_UNKNOWN", Toast.LENGTH_SHORT).show();
                    break;
            }

            Log.d("000951", "5");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                     progressDialog.dismiss();
                }
            },3000);

        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("000951", "Error:: " + e.getMessage());
        }
    }*/


    public static String getBase64String(String jsonString) {

        String base64 = "";
        try {
            byte[] data = (jsonString + "").getBytes("UTF-8");
            base64 = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e("000951", "Exception: " + e.getMessage());
        }
        Log.d("000951", "getBase64String " + base64);

        return base64;

    }

   /* @Override
    protected void onResume() {
        super.onResume();

        ls = new Lister(ctx);
        ls.createAndOpenDB();
    }*/

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();

       /* Log.d("00077", "ONBAck: ");
        Intent newIntent = new Intent(ctx, HomePageVacinator_Activity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
*/
        Intent intent = new Intent(ctx, HomePageVacinator_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        VAC_ClassListener.mSlideMenu.close(false);
        VAC_Register_QRCode_Activity.switch_qr_code_values_vac = "0";
        //var_regtemp_abovetwo = "0";



    }
}
