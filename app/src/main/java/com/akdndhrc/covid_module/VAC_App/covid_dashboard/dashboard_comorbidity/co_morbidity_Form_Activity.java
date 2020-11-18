package com.akdndhrc.covid_module.VAC_App.covid_dashboard.dashboard_comorbidity;

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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.akdndhrc.covid_module.CustomClass.NothingSelectedSpinnerAdapter;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.GPSTracker;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.ServiceLocation;
import com.akdndhrc.covid_module.Utils;
import com.akdndhrc.covid_module.VAC_App.HomePageVacinator_Activity;
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


public class co_morbidity_Form_Activity extends AppCompatActivity {

    Context ctx = co_morbidity_Form_Activity.this;

    //  TextView txt_mother_age, txt_mother_name;
    EditText et_notes_diabetes,et_notes_blood,et_notes_cholestrol,et_notes_cancer,et_notes_heart;
    Spinner sp_diabetes,sp_blood_pressure,sp_cancer,sp_cholestrol,sp_heart;
    EditText et_tareekh_visit, et_refferal_ki_waja, et_refferal_hospital,et_value;
    CheckBox checkbox_haan_1, checkbox_nahi_1, checkbox_haan_2, checkbox_nahi_2, checkbox_awareness, checkbox_service_provided;
    Button btn_jamaa_kre;
    ImageView iv_navigation_drawer, iv_home;
    Spinner sp_naya_sabiqa, sp_planning_type;
    double latitude;
    double longitude;
    // GPSTracker class
    GPSTracker gps;
    String mother_uid, TodayDate;

    private int mYear, mMonth, mDay;
    int date_for_condition = 0;
    int month_for_condition = 0;
    public String hold_age_date_condition = "fromage";
    String monthf2, dayf2, yearf2 = "null";
    String khud_muhaiya = "-1";
    String refer = "-1";
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid, services_and_awareness = "-1";
    LinearLayout ll_services_provided;
    Spinner sp_material,sp_fever,sp_cough,sp_breath,sp_rash,sp_taste,sp_smell,sp_dia;
    RelativeLayout rl_quantity, rl_add, rl_sub;
    TextView tv_count;
    int counter = 0;
    long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.co_morbidity_form);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, co_morbidity_Form_Activity.class));

        mother_uid = getIntent().getExtras().getString("u_id");

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences("UserLogin", 0); // 0 - for private mode
            String shared_useruid = prefelse.getString("login_userid", null); // getting String
            login_useruid = shared_useruid;
            Log.d("000362", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000362", "Shared Err:" + e.getMessage());
        }

        try {
            serviceLocation = new ServiceLocation(ctx);
            serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            serviceLocation.callAsynchronousTask();
        } catch (Exception e) {
            Log.d("000362", "GPS Service Err:  " + e.getMessage());
        }

        //   check_gps();


        //TextView
        //  txt_mother_name = findViewById(R.id.txt_mother_name);
        // txt_mother_age = findViewById(R.id.txt_mother_age);

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        // iv_home.setVisibility(View.GONE);

        //EDitTExt
      et_tareekh_visit=findViewById(R.id.et_tareekh_visit);
        et_tareekh_visit.setText(TodayDate);


        et_notes_diabetes = findViewById(R.id.et_dia);
        et_notes_blood = findViewById(R.id.et_blood_pressure_notes);
        et_notes_cholestrol = findViewById(R.id.et_cholestrol);
        et_notes_cancer = findViewById(R.id.et_cancer);
        et_notes_heart = findViewById(R.id.et_heart_disease_notes);


        //Spinner
      //  sp_naya_sabiqa = findViewById(R.id.sp_naya_sabiqa);
        sp_diabetes = findViewById(R.id.sp_dia);
        sp_blood_pressure = findViewById(R.id.sp_blood);
        sp_cholestrol=findViewById(R.id.sp_cholestrol);
        sp_cancer=findViewById(R.id.sp_cancer);
        sp_heart=findViewById(R.id.sp_heart);




        //Linear Layout
        ll_services_provided = findViewById(R.id.ll_services_provided);

        //RelativeLayout
        //RelativeLayout
        rl_quantity = findViewById(R.id.rl_quantity);
        rl_add = findViewById(R.id.rl_add);
        rl_sub = findViewById(R.id.rl_sub);

        //TextView
       // tv_count = findViewById(R.id.tv_count);
        //tv_count.setText("" + counter);


        spinner_data();

        //CheckBox
       /* checkbox_haan_1 = findViewById(R.id.checkbox_haan_1);
        checkbox_nahi_1 = findViewById(R.id.checkbox_nahi_1);
        checkbox_haan_2 = findViewById(R.id.checkbox_haan_2);
        checkbox_nahi_2 = findViewById(R.id.checkbox_nahi_2);

        checkbox_service_provided = findViewById(R.id.checkbox_service_provided);
        checkbox_awareness = findViewById(R.id.checkbox_awareness);
*/
        /*checkbox_service_provided.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_service_provided.isChecked()) {
                    services_and_awareness = "1";

                    ll_services_provided.setVisibility(View.VISIBLE);
                    Log.d("00147", "IF");
                    checkbox_awareness.setChecked(false);
                } else {
                    Log.d("00147", "ELSE");
                    checkbox_awareness.setChecked(false);
                    ll_services_provided.setVisibility(View.GONE);
                }

            }
        });*/
        /*checkbox_awareness.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_awareness.isChecked()) {
                    services_and_awareness = "0";
                    ll_services_provided.setVisibility(View.GONE);
                    rl_quantity.setVisibility(View.GONE);
                    sp_material.setSelection(0);
                    counter = 0;
                   // tv_count.setText("0");
                }

                checkbox_service_provided.setChecked(false);

            }
        });


        checkbox_haan_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_1.isChecked()) {
                    khud_muhaiya = "1";
                }

                checkbox_nahi_1.setChecked(false);
            }
        });
        checkbox_nahi_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_1.isChecked()) {
                    khud_muhaiya = "0";
                }
                checkbox_haan_1.setChecked(false);
            }
        });

        checkbox_haan_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_2.isChecked()) {
                    refer = "1";
                }
                checkbox_nahi_2.setChecked(false);
            }
        });

        checkbox_nahi_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_2.isChecked()) {
                    refer = "0";
                }
                checkbox_haan_2.setChecked(false);
            }
        });
*/

      /*  rl_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter < 100) {
                    counter += 1;
                  //  tv_count.setText("" + counter);

                    Log.d("000362", ": " + counter);
                } else {
                  //  tv_count.setText("" + counter);
                    Log.d("000362", ":: " + counter);
                }
            }
        });
        rl_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter > 0) {
                    counter -= 1;
                   // tv_count.setText("" + counter);
                    Log.d("000362", ": " + counter);
                } else {
                  //  tv_count.setText("" + counter);
                    Log.d("000362", ": " + counter);
                }
            }


        });
*/

        //Button
        btn_jamaa_kre = findViewById(R.id.submit);

        et_tareekh_visit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ShowDateDialouge();
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


        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_tareekh_visit.getText().toString().length() < 1) {
                    final Snackbar snackbar = Snackbar.make(v, "Please select a visit date.", Snackbar.LENGTH_SHORT);
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
              /*  if (et_tareekh_visit.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(), "برائے مہربانی تاریخ وزٹ منتخب کریں", Toast.LENGTH_LONG).show();
                    return;
                }*/
                if (serviceLocation.showCurrentLocation() == true) {
                    latitude = serviceLocation.getLatitude();
                    longitude = serviceLocation.getLongitude();
                    Log.d("000362", " latitude: " + latitude);
                    Log.d("000362", " longitude: " + longitude);
                } else {
                    try {
                        serviceLocation.doAsynchronousTask.cancel();
                    } catch (Exception e) {
                    }
                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from MBEMARI");

                        if (Integer.parseInt(mData[0][2]) > 0) {
                            JSONObject jsonObject = new JSONObject(mData[0][1]);
                            Log.d("000362", "  Last Latitude: " + jsonObject.getString("lat"));
                            Log.d("000362", "  Last Longitude: " + jsonObject.getString("lng"));

                            latitude = Double.parseDouble(jsonObject.getString("lat"));
                            longitude = Double.parseDouble(jsonObject.getString("lng"));

                            Toast.makeText(ctx, "Data GPS", Toast.LENGTH_SHORT).show();
                        } else {
                            latitude = Double.parseDouble("0.0");
                            longitude = Double.parseDouble("0.0");
                            Toast.makeText(ctx, "Not Data GPS", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.d("000362", "Read CBEMARI Error: " + e.getMessage());
                    }
                }

                btn_jamaa_kre.setVisibility(View.GONE);
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Log.d("000362", " mLastClickTime: " + mLastClickTime);


                try {

                    Lister ls = new Lister(co_morbidity_Form_Activity.this);
                    ls.createAndOpenDB();

                    // et_refferal_ki_waja = findViewById(R.id.et_refferal_ki_waja);
                    // et_refferal_hospital = findViewById(R.id.et_refferal_hospital);


                    JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("tareekh_visit", "" + et_tareekh_visit.getText().toString());
                    jobj.put("notes_diabetes", "" + et_notes_diabetes.getText().toString());
                    jobj.put("notes_cancer", "" + et_notes_cancer.getText().toString());
                    jobj.put("notes_heart", "" + et_notes_heart.getText().toString());
                    jobj.put("notes_blood", "" + et_notes_blood.getText().toString());
                    jobj.put("notes_cholestrol", "" + et_notes_cholestrol.getText().toString());
                   // jobj.put("plan", "" + String.valueOf(sp_naya_sabiqa.getSelectedItemPosition() - 1));
                     jobj.put("diabetes", "" + String.valueOf(sp_diabetes.getSelectedItemPosition() - 1));
                    jobj.put("blood_pressure", "" + String.valueOf(sp_blood_pressure.getSelectedItemPosition() - 1));
                    jobj.put("cholestrol", "" + String.valueOf(sp_cholestrol.getSelectedItemPosition() - 1));
                    jobj.put("heart", "" + String.valueOf(sp_heart.getSelectedItemPosition() - 1));
                    jobj.put("cancer", "" + String.valueOf(sp_cancer.getSelectedItemPosition() - 1));

                    jobj.put("added_on", "null");


                    String cur_added_on = String.valueOf(System.currentTimeMillis());

                    // jobjMain.put("data", jobj);
                    String ans1 = "insert into MBEMARI (member_uid, record_data, data,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + mother_uid + "'," +
                            "'" + et_tareekh_visit.getText().toString() + "'," +
                            "'" + jobj + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + cur_added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000362", "Data: " + ans1);
                    Log.d("000362", "Query: " + res.toString());


                    if (res.toString().equalsIgnoreCase("true")) {

                        final Snackbar snackbar = Snackbar.make(v, "Data has been collected.", Snackbar.LENGTH_SHORT);
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

                            sendPostRequest(mother_uid, et_tareekh_visit.getText().toString(), String.valueOf(jobj), login_useruid, cur_added_on);
                        } else {
                            //Toast.makeText(ctx, "Data has been collected", Toast.LENGTH_SHORT).show();
                        }



                        if (services_and_awareness.equalsIgnoreCase("1")) {
                            insert_into_medicineLog(cur_added_on);
                        } else {
                        }
                    } else {
                        final Snackbar snackbar = Snackbar.make(v, "Data not collected.", Snackbar.LENGTH_SHORT);
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


                    // Toast.makeText(getApplicationContext(),String.valueOf(res)+String.valueOf(ans1),Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.d("000362", "Err: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            finish();
                        }
                    }, 2500);
                }

            }
        });
    }

    private void insert_into_medicineLog(String cur_added_on) {

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();


            String med_uid = "";
            JSONObject jobj_medicine_stock = new JSONObject();
            jobj_medicine_stock.put("medicine_quantity", "" + counter);
            jobj_medicine_stock.put("material", "" + sp_material.getSelectedItem());//spinner
            jobj_medicine_stock.put("material_pos", "" +String.valueOf(sp_material.getSelectedItemPosition() - 1));//spinner

            if (sp_material.getSelectedItemPosition() == 1) {
                jobj_medicine_stock.put("medicine_type", "1");
                med_uid = "fafacbfe1f639599c407c19373438280";

            }
           else if (sp_material.getSelectedItemPosition() == 2) {
                jobj_medicine_stock.put("medicine_type", "0");
                med_uid = "3ac832b9524e6c0496cf449eb30cc8b3";

            }
            else {
                jobj_medicine_stock.put("medicine_type", "0");
                med_uid = String.valueOf(sp_material.getSelectedItemPosition() - 1);//spinner
            }


            String ans1 = "insert into MEDICINE_LOG (member_uid, medicine_id, record_data,type,disease,metadata,added_by,added_on)" +
                    "values" +
                    "(" +
                    "'" + mother_uid + "'," +
                    "'" +med_uid+ "'," +
                    "'" + TodayDate + "'," +
                    "'MBEMARI'," +
                    "'none'," +
                    "'" + jobj_medicine_stock + "'," +
                    "'" + login_useruid + "'," +
                    "'" + cur_added_on + "'" +
                    ")";

            Boolean res = ls.executeNonQuery(ans1);
            Log.d("000362", "Medicine Data: " + ans1);
            Log.d("000362", "Query: " + res.toString());

            //    Toast.makeText(ctx, "Addedd", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.d("000362", "Err: " + e.getMessage());
          //  Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void spinner_data() {

        try {
          /*  // Select sp_azdawaji_hasiyat
            final ArrayAdapter<CharSequence> adptr_naya_sabiqa = ArrayAdapter.createFromResource(this, R.array.array_naya_sabiqa, android.R.layout.simple_spinner_item);
            adptr_naya_sabiqa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


            sp_naya_sabiqa.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            adptr_naya_sabiqa,
                            R.layout.spinner_azdawaji_hasiyat_layout,
                            // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                            this));


            sp_naya_sabiqa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                ((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                    //Toast.makeText(parent.getContext(), "Selected: " + position, Toast.LENGTH_LONG).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



*/


            final ArrayAdapter<CharSequence> adptr_diabetes= ArrayAdapter.createFromResource(this, R.array.yes_no, android.R.layout.simple_spinner_item);
            adptr_diabetes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


            sp_diabetes.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            adptr_diabetes,
                            R.layout.spinner_azdawaji_hasiyat_layout,
                            // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                            this));


            sp_diabetes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                ((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                    //Toast.makeText(parent.getContext(), "Selected: " + position, Toast.LENGTH_LONG).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            final ArrayAdapter<CharSequence> adptr_cancer = ArrayAdapter.createFromResource(this, R.array.yes_no, android.R.layout.simple_spinner_item);
            adptr_cancer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


            sp_cancer.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            adptr_cancer,
                            R.layout.spinner_azdawaji_hasiyat_layout,
                            // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                            this));


            sp_cancer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                ((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                    //Toast.makeText(parent.getContext(), "Selected: " + position, Toast.LENGTH_LONG).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            final ArrayAdapter<CharSequence> adptr_heart = ArrayAdapter.createFromResource(this, R.array.yes_no, android.R.layout.simple_spinner_item);
            adptr_heart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


            sp_heart.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            adptr_heart,
                            R.layout.spinner_azdawaji_hasiyat_layout,
                            // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                            this));


            sp_heart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                ((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                    //Toast.makeText(parent.getContext(), "Selected: " + position, Toast.LENGTH_LONG).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            final ArrayAdapter<CharSequence> adptr_blood = ArrayAdapter.createFromResource(this, R.array.yes_no, android.R.layout.simple_spinner_item);
            adptr_blood.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


            sp_blood_pressure.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            adptr_blood,
                            R.layout.spinner_azdawaji_hasiyat_layout,
                            // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                            this));


            sp_blood_pressure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                ((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                    //Toast.makeText(parent.getContext(), "Selected: " + position, Toast.LENGTH_LONG).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            final ArrayAdapter<CharSequence> adptr_cholestrol = ArrayAdapter.createFromResource(this, R.array.yes_no, android.R.layout.simple_spinner_item);
            adptr_cholestrol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


            sp_cholestrol.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            adptr_cholestrol,
                            R.layout.spinner_azdawaji_hasiyat_layout,
                            // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                            this));


            sp_cholestrol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                ((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                    //Toast.makeText(parent.getContext(), "Selected: " + position, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



            ///////////sp_planning_type//////////

            final ArrayAdapter<CharSequence> adptr_planning = ArrayAdapter.createFromResource(this, R.array.array_sp_planning_type, android.R.layout.simple_spinner_item);
            adptr_planning.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            
            sp_planning_type.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            adptr_planning,
                            R.layout.spinner_azdawaji_hasiyat_layout,
                            // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                            this));


            sp_planning_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                ((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                    //Toast.makeText(parent.getContext(), "Selected: " + position, Toast.LENGTH_LONG).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            ////////////////////////// Sp_Month ////////////////////////////////////////
            final ArrayAdapter<CharSequence> adptr_material = ArrayAdapter.createFromResource(this, R.array.array_sp_service_provided, R.layout.sp_title_topic_layout);
            adptr_material.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


            sp_material.setAdapter(
                    new com.akdndhrc.covid_module.NothingSelectedSpinnerAdapter(
                            adptr_material,
                            R.layout.sp_title_topic_layout,
                            // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                            this));

            sp_material.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (sp_material.getSelectedItemPosition() > 0) {
                        rl_quantity.setVisibility(View.VISIBLE);
                    } else {
                        rl_quantity.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (Exception e) {

        }
    }


    private void sendPostRequest(final String member_uid, final String record_data,
                                 final String data, final String added_by, final String added_on) {

        String url = " https://pak.api.teekoplus.akdndhrc.org/sync/save/mother/illness";

        Log.d("000362", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {
                        Log.d("000362", "Response:  " + response);


                        Lister ls = new Lister(co_morbidity_Form_Activity.this);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE MBEMARI SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + mother_uid + "'AND record_data= '" + et_tareekh_visit.getText().toString() + "'AND added_on= '" + added_on + "'";
                        ls.executeNonQuery(update_record);
                        Log.d("000362", "Update Record:  " + update_record);

                        Toast tt  =Toast.makeText(ctx, "Data synced", Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();

                    } else {
                        Log.d("000362", "else ");
                        Toast.makeText(ctx, "Data service not synced", Toast.LENGTH_SHORT).show();
                        //  Toast.makeText(side_effects_Form_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000362", "Err: " + e.getMessage());
                    //Toast.makeText(side_effects_Form_Activity.this, "Data has been sent incorrectly.", Toast.LENGTH_SHORT).show();
                    Toast tt  =Toast.makeText(ctx, "Data not synced", Toast.LENGTH_SHORT);
                    tt.setGravity(Gravity.CENTER, 0, 0);
                    tt.show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000362", "error    " + error.getMessage());
                //Toast.makeText(side_effects_Form_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast tt  =Toast.makeText(ctx, "Data not synced", Toast.LENGTH_SHORT);
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


                Log.d("000362", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000362", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }

    public void ReferalFormSubmit() {
        try {

            Lister ls = new Lister(co_morbidity_Form_Activity.this);
            ls.createAndOpenDB();

            JSONObject jobj = new JSONObject();
            jobj.put("lat", "" + String.valueOf(latitude));
            jobj.put("lng", "" + String.valueOf(longitude));
           /* jobj.put("referal_reason", "" + et_refferal_ki_waja.getText().toString());
            jobj.put("referal_facility", "" + et_refferal_hospital.getText().toString());//spinner
            jobj.put("referal_type", "" + "MFPLAN");//spinner
           */ jobj.put("added_on", "null");

            String cur_added_oon = String.valueOf(System.currentTimeMillis());

            String ans1 = "insert into REFERAL (member_uid, record_data, data,added_by, is_synced,added_on)" +
                    "values" +
                    "(" +
                    "'" + mother_uid + "'," +
                    "'" + TodayDate + "'," +
                    "'" + jobj + "'," +
                    "'" + login_useruid + "'," +
                    "'0'," +
                    "'" + cur_added_oon + "'" +
                    ")";

            Boolean res = ls.executeNonQuery(ans1);
            Log.d("000362", "Data: " + ans1);
            Log.d("000362", "Query: " + res.toString());

            sendPostRequestReferal(mother_uid, TodayDate, String.valueOf(jobj), login_useruid, cur_added_oon);
            ;

            //  Toast.makeText(getApplicationContext(),String.valueOf(res)+String.valueOf(ans1),Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.d("000362", "Err: " + e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
                    /*Intent intent = new Intent(ctx, Add_Family_Member_Activity.class);
                    startActivity(intent);*/
            finish();
        }


    }

    private void sendPostRequestReferal(final String member_uid, final String record_data, final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/family/referrals";

        Log.d("000362", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000362", "Response    " + response);

                        //   Toast.makeText(side_effects_Form_Activity.this, "Data has been saved", Toast.LENGTH_SHORT).show();

                        Lister ls = new Lister(co_morbidity_Form_Activity.this);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE REFERAL SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + mother_uid + "'AND record_data= '" + record_data + "'AND added_on= '" + added_on + "'";

                        ls.executeNonQuery(update_record);


                    } else {
                        Log.d("000362", "else ");

                        //Toast.makeText(side_effects_Form_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000362", "Err: " + e.getMessage());
                    //Toast.makeText(side_effects_Form_Activity.this, "Data has been sent incorrectly.", Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000362", "error    " + error.getMessage());
//                Toast.makeText(side_effects_Form_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();


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


                Log.d("000362", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000362", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }


    public void ShowDateDialouge() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(co_morbidity_Form_Activity.this, R.style.DatePickerDialog,
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
                        et_tareekh_visit.setText(yearf2 + "-" + monthf2 + "-" + dayf2);

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
                        // et_umer.setText(ageS);
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
            Log.d("000362", "latitude value: " + latitude);
            Log.d("000362", "longitude value: " + longitude);
        } else {
            gps.showSettingsAlert();
            Toast.makeText(ctx, "Please turn on GPS position", Toast.LENGTH_LONG).show();
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

                Log.d("000362", "IF lat lng: ");
                Log.d("000362", "latitude: " + latitude);
                Log.d("000362", "longitude: " + longitude);


                return;
            } else {
                snackbar.dismiss();
                Log.d("000362", "ELSE lat lng: ");
                Log.d("000362", "latitude: " + latitude);
                Log.d("000362", "longitude: " + longitude);

                Toast.makeText(ctx, "GPS position is now on", Toast.LENGTH_SHORT).show();
            }

        } else {
            gps.showSettingsAlert();
            Toast.makeText(ctx, "Please turn on GPS position", Toast.LENGTH_LONG).show();
            return;
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
