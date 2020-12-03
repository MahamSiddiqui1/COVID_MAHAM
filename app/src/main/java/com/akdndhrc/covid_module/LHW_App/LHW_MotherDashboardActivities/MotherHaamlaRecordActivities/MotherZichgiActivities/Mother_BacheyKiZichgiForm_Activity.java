package com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHaamlaRecordActivities.MotherZichgiActivities;

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
import com.akdndhrc.covid_module.ServiceLocation;
import com.akdndhrc.covid_module.Utils;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHaamlaRecordActivities.Mother_HaamlaDashboard_Activity;
import com.akdndhrc.covid_module.R;
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


public class Mother_BacheyKiZichgiForm_Activity extends AppCompatActivity {

    Context ctx = Mother_BacheyKiZichgiForm_Activity.this;

    ImageView iv_navigation_drawer, iv_home;
    EditText et_tareekh_indraj, et_din, et_maah, et_saal;
    CheckBox checkbox_12_ghanty_sy_kum, checkbox_12_ghanty_sy_zyada, checkbox_ghar, checkbox_markaz_sehat, checkbox_hospital, checkbox_hunarmand_farad_ny,
            checkbox_tarbiyat_yafta_farad_ny, checkbox_ghair_tarbiyat_yafta_fard_ny, checkbox_normal_1, checkbox_normal_nahi_thi_1, checkbox_pedaish_normal, checkbox_tool, checkbox_operation;

    Button btn_jamaa_kre;
    String mother_uid, preg_id, TodayDate;
    double latitude;
    double longitude;
    // GPSTracker class
    GPSTracker gps;

    private int mYear, mMonth, mDay;
    int date_for_condition = 0;
    int month_for_condition = 0;
    public String hold_age_date_condition = "fromage";
    String monthf2, dayf2, yearf2 = "null";
    String pain_duration, birth_place, delivered_by, baby_position, mode_deliver;
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_bachey_ki_zichgi_form);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Mother_BacheyKiZichgiForm_Activity.class));

        mother_uid = getIntent().getExtras().getString("u_id");
        preg_id = getIntent().getExtras().getString("preg_id");

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

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());


        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);
        //EditText
        et_tareekh_indraj = findViewById(R.id.et_tareekh_indraj);
        et_din = findViewById(R.id.et_din);
        et_maah = findViewById(R.id.et_maah);
        et_saal = findViewById(R.id.et_saal);

        //CheckBox
        checkbox_12_ghanty_sy_kum = findViewById(R.id.checkbox_12_ghanty_sy_kum);
        checkbox_12_ghanty_sy_zyada = findViewById(R.id.checkbox_12_ghanty_sy_zyada);
        checkbox_ghar = findViewById(R.id.checkbox_ghar);
        checkbox_markaz_sehat = findViewById(R.id.checkbox_markaz_sehat);
        checkbox_hospital = findViewById(R.id.checkbox_hospital);
        checkbox_hunarmand_farad_ny = findViewById(R.id.checkbox_hunarmand_farad_ny);
        checkbox_tarbiyat_yafta_farad_ny = findViewById(R.id.checkbox_tarbiyat_yafta_farad_ny);
        checkbox_ghair_tarbiyat_yafta_fard_ny = findViewById(R.id.checkbox_ghair_tarbiyat_yafta_fard_ny);
        checkbox_normal_1 = findViewById(R.id.checkbox_normal_1);
        checkbox_normal_nahi_thi_1 = findViewById(R.id.checkbox_normal_nahi_thi_1);
        checkbox_pedaish_normal = findViewById(R.id.checkbox_pedaish_normal);
        checkbox_tool = findViewById(R.id.checkbox_tool);
        checkbox_operation = findViewById(R.id.checkbox_operation);


        checkbox_12_ghanty_sy_kum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_12_ghanty_sy_kum.isChecked()) {
                    pain_duration = "1";
                }
                checkbox_12_ghanty_sy_zyada.setChecked(false);

            }
        });
        checkbox_12_ghanty_sy_zyada.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_12_ghanty_sy_zyada.isChecked()) {
                    pain_duration = "0";
                }
                checkbox_12_ghanty_sy_kum.setChecked(false);
            }
        });


        checkbox_markaz_sehat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_markaz_sehat.isChecked()) {
                    birth_place = "0";
                }
                checkbox_ghar.setChecked(false);
                checkbox_hospital.setChecked(false);
            }
        });
        checkbox_ghar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_ghar.isChecked()) {
                    birth_place = "1";
                }
                checkbox_markaz_sehat.setChecked(false);
                checkbox_hospital.setChecked(false);
            }
        });
        checkbox_hospital.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_hospital.isChecked()) {
                    birth_place = "2";
                }
                checkbox_ghar.setChecked(false);
                checkbox_markaz_sehat.setChecked(false);
            }
        });


        checkbox_hunarmand_farad_ny.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_hunarmand_farad_ny.isChecked()) {
                    delivered_by = "0";
                }
                checkbox_tarbiyat_yafta_farad_ny.setChecked(false);
                checkbox_ghair_tarbiyat_yafta_fard_ny.setChecked(false);
            }
        });
        checkbox_tarbiyat_yafta_farad_ny.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_tarbiyat_yafta_farad_ny.isChecked()) {
                    delivered_by = "1";
                }
                checkbox_hunarmand_farad_ny.setChecked(false);
                checkbox_ghair_tarbiyat_yafta_fard_ny.setChecked(false);
            }
        });
        checkbox_ghair_tarbiyat_yafta_fard_ny.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_ghair_tarbiyat_yafta_fard_ny.isChecked()) {
                    delivered_by = "2";
                }
                checkbox_hunarmand_farad_ny.setChecked(false);
                checkbox_tarbiyat_yafta_farad_ny.setChecked(false);
            }
        });


        checkbox_normal_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_normal_1.isChecked()) {
                    baby_position = "1";
                }
                checkbox_normal_nahi_thi_1.setChecked(false);
            }
        });
        checkbox_normal_nahi_thi_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_normal_nahi_thi_1.isChecked()) {
                    baby_position = "0";
                }
                checkbox_normal_1.setChecked(false);
            }
        });


        checkbox_pedaish_normal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_pedaish_normal.isChecked()) {
                    mode_deliver = "0";
                }
                checkbox_tool.setChecked(false);
                checkbox_operation.setChecked(false);
            }
        });
        checkbox_tool.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_tool.isChecked()) {
                    mode_deliver = "1";
                }
                checkbox_pedaish_normal.setChecked(false);
                checkbox_operation.setChecked(false);
            }
        });
        checkbox_operation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_operation.isChecked()) {
                    mode_deliver = "2";
                }
                checkbox_pedaish_normal.setChecked(false);
                checkbox_tool.setChecked(false);
            }
        });


        et_tareekh_indraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowDateDialoug();
            }
        });


        //Button
        btn_jamaa_kre = findViewById(R.id.submit);


        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(ctx, Mother_HaamlaDashboard_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
            }
        });

        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_tareekh_indraj.getText().toString().length() < 1) {
                    //btn_jamaa_kre.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), R.string.selectDateOfRecord, Toast.LENGTH_LONG).show();
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

                        String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from MDELIV");

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
                        Log.d("000258", "Read MDELIV Error: " + e.getMessage());
                    }
                }


                try {

                    Lister ls = new Lister(Mother_BacheyKiZichgiForm_Activity.this);
                    ls.createAndOpenDB();
                    // et_refferal_ki_waja = findViewById(R.id.et_refferal_ki_waja);
                    // et_refferal_hospital = findViewById(R.id.et_refferal_hospital);

                    //Edittext


                    JSONObject jobj = new JSONObject();

                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("baby_position", "" + baby_position);
                    jobj.put("dob_mon", "" + et_maah.getText().toString());
                    jobj.put("dob_year", "" + et_saal.getText().toString());
                    jobj.put("dob_date", "" + et_din.getText().toString());
                    jobj.put("pain_duration", "" + pain_duration);
                    jobj.put("birth_place", "" + birth_place);
                    jobj.put("delivered_by", "" + delivered_by);
                    jobj.put("mode_deliver", "" + mode_deliver);
                    jobj.put("record_enter_date", "" + et_tareekh_indraj.getText().toString());
                    jobj.put("added_on", "" + "null");


                    String cur_added_on = String.valueOf(System.currentTimeMillis());

                    String ans1 = "insert into MDELIV (member_uid, pregnancy_id,record_data,type, data,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + mother_uid + "'," +
                            "'" + preg_id + "'," +
                            "'" + et_tareekh_indraj.getText().toString() + "'," +
                            "'" + "1" + "'," +
                            "'" + jobj + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + cur_added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);

                    Log.d("000555", "Data: " + ans1);
                    Log.d("000555", "Query: " + res.toString());

                    if (Utils.haveNetworkConnection(ctx) > 0) {

                        sendPostRequest(mother_uid, preg_id, TodayDate, "1", String.valueOf(jobj), login_useruid, cur_added_on);
                    } else {
                        Toast.makeText(ctx, R.string.dataCollected, Toast.LENGTH_SHORT).show();
                    }

                    //   Toast.makeText(getApplicationContext(),String.valueOf(res)+String.valueOf(ans1),Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.d("000555", "Err: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {
                 /*   Intent intent = new Intent(ctx, Add_Family_Member_Activity.class);
                    startActivity(intent);*/
                    finish();
                }
            }
        });

    }

    private void sendPostRequest(final String member_uid, final String pregnancy_id, final String record_data, final String type,
                                 final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/mother/deliveries";

        Log.d("000555", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {


                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {
                        Log.d("000555", "response:    " + response);


                        Lister ls = new Lister(Mother_BacheyKiZichgiForm_Activity.this);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE MDELIV SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + mother_uid + "'AND pregnancy_id= '" + preg_id + "'AND added_on= '" + added_on + "'";
                        ls.executeNonQuery(update_record);
                        Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000555", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        // Toast.makeText(Mother_BacheyKiZichgiForm_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", "Err: " + e.getMessage());
                    // Toast.makeText(Mother_BacheyKiZichgiForm_Activity.this, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "onErrorResponse: " + error.getMessage());
                //Toast.makeText(Mother_BacheyKiZichgiForm_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("member_id", member_uid);
                params.put("pregnancy_id", pregnancy_id);
                params.put("record_data", record_data);
                params.put("type", type);
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


        DatePickerDialog datePickerDialog = new DatePickerDialog(Mother_BacheyKiZichgiForm_Activity.this, R.style.DatePickerDialog,
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
