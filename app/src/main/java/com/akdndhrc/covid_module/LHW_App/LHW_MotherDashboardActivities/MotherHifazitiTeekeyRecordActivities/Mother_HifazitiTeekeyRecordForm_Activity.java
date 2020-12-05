package com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHifazitiTeekeyRecordActivities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.GPSTracker;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.ServiceLocation;
import com.akdndhrc.covid_module.Utils;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
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


public class Mother_HifazitiTeekeyRecordForm_Activity extends AppCompatActivity {

    Context ctx = Mother_HifazitiTeekeyRecordForm_Activity.this;

    EditText et_tareekh_indraj, et_dusra, et_phela, et_mahina, et_saal, et_first, et_second, et_third, et_fourth, et_zichgi_kis_ny_ki, et_haaml_ka_natija, et_tabsrah_khaas_ikdaam;
    ImageView iv_navigation_drawer, iv_home;
    Button btn_jamaa_kre;
    String mother_uid;
    double latitude;
    double longitude;
    // GPSTracker class
    GPSTracker gps;
    String TodayDate;

    private int mYear, mMonth, mDay;
    int date_for_condition = 0;
    int month_for_condition = 0;
    public String hold_age_date_condition = "fromage";
    String monthf2, dayf2, yearf2 = "null";

    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_hifaziti_teekey_record_form);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Mother_HifazitiTeekeyRecordForm_Activity.class));

        mother_uid = getIntent().getExtras().getString("u_id");


        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
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
        et_tareekh_indraj = findViewById(R.id.et_tareekh_indraj);
        et_dusra = findViewById(R.id.et_dusra);
        et_phela = findViewById(R.id.et_phela);
        et_mahina = findViewById(R.id.et_mahina);
        et_saal = findViewById(R.id.et_saal);
        et_first = findViewById(R.id.et_first);
        et_second = findViewById(R.id.et_second);
        et_third = findViewById(R.id.et_third);
        et_fourth = findViewById(R.id.et_fourth);
        et_zichgi_kis_ny_ki = findViewById(R.id.et_zichgi_kis_ny_ki);
        et_haaml_ka_natija = findViewById(R.id.et_haaml_ka_natija);
        et_tabsrah_khaas_ikdaam = findViewById(R.id.et_tabsrah_khaas_ikdaam);

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        // iv_home.setVisibility(View.GONE);
        //Button
        btn_jamaa_kre = findViewById(R.id.submit);


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

        et_tareekh_indraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowDateDialoug();
            }
        });

        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_tareekh_indraj.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(), R.string.selectDateOfRecord, Toast.LENGTH_LONG).show();
                    return;
                }


                if (serviceLocation.showCurrentLocation() == true) {
                    latitude = serviceLocation.getLatitude();
                    longitude = serviceLocation.getLongitude();
                    Log.d("000555", " latitude: " + latitude);
                    Log.d("000555", " longitude: " + longitude);
                } else {
                }


                try {


                    Lister ls = new Lister(Mother_HifazitiTeekeyRecordForm_Activity.this);
                    ls.createAndOpenDB();

                    // et_refferal_ki_waja = findViewById(R.id.et_refferal_ki_waja);
                    // et_refferal_hospital = findViewById(R.id.et_refferal_hospital);


                    JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("pehla", "" + et_phela.getText().toString());//spinner
                    jobj.put("et_dusra", "" + et_dusra.getText().toString());
                    jobj.put("mahina", "" + et_mahina.getText().toString());
                    jobj.put("saal", "" + et_saal.getText().toString());
                    jobj.put("first", "" + et_first.getText().toString());
                    jobj.put("second", "" + et_second.getText().toString());
                    jobj.put("third", "" + et_third.getText().toString());
                    jobj.put("forth", "" + et_fourth.getText().toString());
                    jobj.put("deliveredby", "" + et_zichgi_kis_ny_ki.getText().toString());
                    jobj.put("deliveryoutcome", "" + et_haaml_ka_natija.getText().toString());
                    jobj.put("details", "" + et_tabsrah_khaas_ikdaam.getText().toString());
                    jobj.put("record_data", "" + et_tareekh_indraj.getText().toString());
                    jobj.put("added_on", "null");

                    String cur_added_on = String.valueOf(System.currentTimeMillis());

                    // jobjMain.put("data", jobj);
                    String ans1 = "insert into MVACINE (member_uid, record_data, data,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + mother_uid + "'," +
                            "'" + et_tareekh_indraj.getText().toString() + "'," +
                            "'" + jobj + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + cur_added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000555", "Data: " + ans1);
                    Log.d("000555", "Query: " + res.toString());

                    if (Utils.haveNetworkConnection(ctx) > 0) {

                        sendPostRequest(mother_uid, et_tareekh_indraj.getText().toString(), String.valueOf(jobj), login_useruid, cur_added_on);
                    } else {

                        Toast.makeText(ctx, R.string.dataCollected, Toast.LENGTH_SHORT).show();
                    }

                    // Toast.makeText(getApplicationContext(),String.valueOf(res)+String.valueOf(ans1),Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.d("000555", "Err: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {
                    finish();
                    /*Intent intent = new Intent(ctx, Add_Family_Member_Activity.class);
                    startActivity(intent);*/
                }

            }
        });

    }

    private void sendPostRequest(final String member_uid, final String record_data,
                                 final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/mother/vaccinations";

        Log.d("000555", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {
                    // Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {
                        Log.d("000555", "Response:    " + response);
                        Lister ls = new Lister(Mother_HifazitiTeekeyRecordForm_Activity.this);
                       ls.createAndOpenDB();

                        String update_record = "UPDATE MVACINE SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + mother_uid + "'AND record_data= '" + et_tareekh_indraj.getText().toString() + "'AND added_on= '" + added_on + "' ";
                        ls.executeNonQuery(update_record);
                        Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(Mother_HifazitiTeekeyRecordForm_Activity.this, "Data has been saved", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000555", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(Mother_HifazitiTeekeyRecordForm_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", "Catch:  " + e.getMessage());
                    //Toast.makeText(Mother_HifazitiTeekeyRecordForm_Activity.this, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "onErrorResponse: " + error.getMessage());
                //Toast.makeText(Mother_HifazitiTeekeyRecordForm_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();

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

    public void ShowDateDialoug() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(Mother_HifazitiTeekeyRecordForm_Activity.this, R.style.DatePickerDialog,
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
            Log.d("000555", "latitude value: " + latitude);
            Log.d("000555", "longitude value: " + longitude);
        } else {
            gps.showSettingsAlert();
            Toast.makeText(ctx, R.string.GPSonAlert, Toast.LENGTH_LONG).show();
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

                Toast.makeText(ctx, R.string.GPSonMessage, Toast.LENGTH_SHORT).show();
            }

        } else {
            gps.showSettingsAlert();
            Toast.makeText(ctx, R.string.GPSonAlert, Toast.LENGTH_LONG).show();
            return;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
