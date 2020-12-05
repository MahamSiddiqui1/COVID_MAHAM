package com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildHifazitiTeekeyRecordActivities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
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


public class Child_HifazitiTeekeyPheleSyLiHoeVaccineForm_Activity extends AppCompatActivity {

    Context ctx = Child_HifazitiTeekeyPheleSyLiHoeVaccineForm_Activity.this;

    TextView txt_age, txt_naam;
    ImageView iv_navigation_drawer, iv_home, image_gender;
    EditText et_tareekh_mosool_hoe, et_vaccine_kaha_farham_ki_gae, et_tabsrah;
    Button btn_jamaa_kre;
    Switch switch_mosool_hoe;

    private int mYear, mMonth, mDay;
    int date_for_condition = 0;
    int month_for_condition = 0;

    public String hold_age_date_condition = "fromage";
    String monthf2, dayf2, yearf2 = "null";
    double latitude;
    String TodayDate;
    String child_uid, child_age, child_name, child_gender;
    double longitude;
    // GPSTracker cl
    GPSTracker gps;
    String vacine_uid, vacine_name, vacine_place, vac_duedate, vac_place_pos;
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_hifaziti_teekey_phele_sy_li_hoe_vaccine_form);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Child_HifazitiTeekeyPheleSyLiHoeVaccineForm_Activity.class));

        vacine_place = getIntent().getExtras().getString("vac_place");
        vacine_uid = getIntent().getExtras().getString("vac_id");
        vacine_name = getIntent().getExtras().getString("vac_name");
        child_uid = getIntent().getExtras().getString("u_id");
        child_name = getIntent().getExtras().getString("child_name");
        child_age = getIntent().getExtras().getString("child_age");
        child_gender = getIntent().getExtras().getString("child_gender");
        vac_place_pos = getIntent().getExtras().getString("vac_place_pos");
        vac_duedate = getIntent().getExtras().getString("vac_duedate");


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


        TextView vacine_title = findViewById(R.id.txt_polio_eng);
        vacine_title.setText(vacine_name);

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        // iv_home.setVisibility(View.GONE);
        image_gender = findViewById(R.id.image_gender);


        try {
            serviceLocation = new ServiceLocation(ctx);
            serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            serviceLocation.callAsynchronousTask();
        } catch (Exception e) {
            Log.d("000555", "GPS Service Err:  " + e.getMessage());
        }


        //check_gps();

        //EditText
        et_tareekh_mosool_hoe = findViewById(R.id.et_tareekh_mosool_hoe);
        et_vaccine_kaha_farham_ki_gae = findViewById(R.id.et_vaccine_kaha_farham_ki_gae);
        et_tabsrah = findViewById(R.id.et_tabsrah);

        //TextView
        txt_age = findViewById(R.id.txt_age);
        txt_naam = findViewById(R.id.txt_naam);

        txt_naam.setText(child_name);
        txt_age.setText(child_age);


     if (child_gender.equalsIgnoreCase("0")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_adult_female_50dp));
                image_gender.setImageTintList(ctx.getResources().getColorStateList(R.color.pink_color));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    image_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                    image_gender.setImageTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
                }
            }
        }

        //Switch
        switch_mosool_hoe = findViewById(R.id.switch_mosool_hoe);

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


        et_tareekh_mosool_hoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTareekhDialog();
            }
        });


        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_tareekh_mosool_hoe.getText().toString().length() < 1) {
                    //btn_jamaa_kre.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), R.string.selectDateReceived, Toast.LENGTH_LONG).show();
                    return;
                }

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

                        String[][] mData = ls.executeReader("SELECT max(added_on),metadata,count(*) from CVACCINATION");

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
                        Log.d("000258", "Read CVACCINE Error: " + e.getMessage());
                    }
                }

                try {

                    Lister ls = new Lister(Child_HifazitiTeekeyPheleSyLiHoeVaccineForm_Activity.this);
                  ls.createAndOpenDB();

                    // et_refferal_ki_waja = findViewById(R.id.et_refferal_ki_waja);
                    // et_refferal_hospital = findViewById(R.id.et_refferal_hospital);

                    //Edittext


                    SimpleDateFormat dates = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss aa");
                    Calendar c = Calendar.getInstance();
                    String current_timeStamp = dates.format(c.getTime());
                    Log.d("000555", "timestamp:" + current_timeStamp);

                    JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("type_name", "" + "phele_sy_li_hoe_vaccine");
                    jobj.put("vaccine_place", "" + vacine_place);
                    jobj.put("vaccine_place_pos", "" + vac_place_pos);
                    jobj.put("vaccine_kaha_farham_ki_gae", "" + et_vaccine_kaha_farham_ki_gae.getText().toString());
                    jobj.put("tabsrah", "" + et_tabsrah.getText().toString());
                    jobj.put("datetime", "" + current_timeStamp);

                    String added_on = String.valueOf(System.currentTimeMillis());

                    // jobjMain.put("data", jobj);
                    String ans1 = "insert into CVACCINATION (member_uid,vaccine_id, record_data, type, due_date,vaccinated_on,image_location,metadata,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + child_uid + "'," +
                            "'" + vacine_uid + "'," +
                            "'" + TodayDate + "'," +
                            "'" + "1" + "'," +
                            "'" + vac_duedate + "'," +
                            "'" + et_tareekh_mosool_hoe.getText().toString() + "'," +
                            "'" + TodayDate + "'," +
                            "'" + jobj + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000555", "Data: " + ans1);
                    Log.d("000555", "Query: " + res.toString());
                    //imageocation o be added
                    if (Utils.haveNetworkConnection(ctx) > 0) {

                        sendPostRequest(child_uid, vacine_uid, TodayDate, String.valueOf(jobj), login_useruid, added_on);
                    } else {
                        Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();


                    }

                    //  Toast.makeText(getApplicationContext(),String.valueOf(res)+String.valueOf(ans1),Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.d("000555", "Err: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {
                    finish();
                }
            }

        });

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


    public void ShowTareekhDialog() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(Child_HifazitiTeekeyPheleSyLiHoeVaccineForm_Activity.this, R.style.DatePickerDialog,
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
                        et_tareekh_mosool_hoe.setText(yearf2 + "-" + monthf2 + "-" + dayf2);

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

    private void sendPostRequest(final String member_uid, final String vacine_uid, final String record_data,
                                 final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/child/vaccinations";

        Log.d("000999", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000555", "Response    " + response);

                        //    Toast.makeText(Child_HifazitiTeekeyPheleSyLiHoeVaccineForm_Activity.this, "Data has been saved", Toast.LENGTH_SHORT).show();

                        Lister ls = new Lister(Child_HifazitiTeekeyPheleSyLiHoeVaccineForm_Activity.this);
                      ls.createAndOpenDB();

                        String update_record = "UPDATE CVACCINATION SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + child_uid + "'AND record_data= '" + TodayDate + "'AND vaccine_id= '" + vacine_uid + "'";

                        ls.executeNonQuery(update_record);

                        Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000555", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(Child_HifazitiTeekeyPheleSyLiHoeVaccineForm_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(Child_HifazitiTeekeyPheleSyLiHoeVaccineForm_Activity.this, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();
                    Log.d("000555", "Err: " + e.getMessage());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "onErrorResponse: " + error.getMessage());
                //Toast.makeText(Child_HifazitiTeekeyPheleSyLiHoeVaccineForm_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("member_id", member_uid);
                params.put("vaccine_id", vacine_uid);
                params.put("type", "1");
                params.put("record_data", record_data);
                params.put("vaccinated_on", TodayDate);
                params.put("image_location", added_by);
                params.put("metadata", data);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

        // startActivity(new Intent(ctx, Child_HifazitiTeekeyRecordListVac_Activity.class));
    }
}
