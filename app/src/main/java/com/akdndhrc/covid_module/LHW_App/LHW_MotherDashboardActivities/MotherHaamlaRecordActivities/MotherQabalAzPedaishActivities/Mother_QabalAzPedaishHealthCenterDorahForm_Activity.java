package com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHaamlaRecordActivities.MotherQabalAzPedaishActivities;

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


public class Mother_QabalAzPedaishHealthCenterDorahForm_Activity extends AppCompatActivity {

    Context ctx = Mother_QabalAzPedaishHealthCenterDorahForm_Activity.this;

    ImageView iv_navigation_drawer, iv_home;
    EditText et_tareekh_indraj, et_wazan, et_haftay, et_centimeter;
    CheckBox checkbox_140_90_sy_kum, checkbox_140_90_sy_zyada, checkbox_mojood_hai_2, checkbox_nahi_hai_2, checkbox_sir_kay_baal_seedha, checkbox_aara,
            checkbox_thercha, checkbox_ulta, checkbox_normal, checkbox_sugar_ki_mojoodgi, checkbox_protein_ki_mojoodgi, checkbox_100gm_dl_sy_kum, checkbox_100gm_dl_sy_zyada;

    Button btn_jamaa_kre;
    String mother_uid, preg_id;
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
    String blood_pressure, child_hb, child_direction, urine_obser, hemo_globin;
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_qabal_az_pedaish_health_center_dorah_form);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Mother_QabalAzPedaishHealthCenterDorahForm_Activity.class));

        mother_uid = getIntent().getExtras().getString("u_id");
        preg_id = getIntent().getExtras().getString("preg_id");


        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
            String shared_useruid = prefelse.getString(("login_userid"), null); // getting String
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

        //   check_gps();

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);
        //EditText
        et_tareekh_indraj = findViewById(R.id.et_tareekh_indraj);
        et_wazan = findViewById(R.id.et_wazan);
        et_haftay = findViewById(R.id.et_haftay);
        et_centimeter = findViewById(R.id.et_centimeter);


        //CheckBox
        checkbox_140_90_sy_kum = findViewById(R.id.checkbox_140_90_sy_kum);
        checkbox_140_90_sy_zyada = findViewById(R.id.checkbox_140_90_sy_zyada);
        checkbox_mojood_hai_2 = findViewById(R.id.checkbox_mojood_hai_2);
        checkbox_nahi_hai_2 = findViewById(R.id.checkbox_nahi_hai_2);
        checkbox_sir_kay_baal_seedha = findViewById(R.id.checkbox_sir_kay_baal_seedha);
        checkbox_aara = findViewById(R.id.checkbox_aara);
        checkbox_thercha = findViewById(R.id.checkbox_thercha);
        checkbox_ulta = findViewById(R.id.checkbox_ulta);
        checkbox_normal = findViewById(R.id.checkbox_normal);
        checkbox_sugar_ki_mojoodgi = findViewById(R.id.checkbox_sugar_ki_mojoodgi);
        checkbox_protein_ki_mojoodgi = findViewById(R.id.checkbox_protein_ki_mojoodgi);
        checkbox_100gm_dl_sy_kum = findViewById(R.id.checkbox_100gm_dl_sy_kum);
        checkbox_100gm_dl_sy_zyada = findViewById(R.id.checkbox_100gm_dl_sy_zyada);

        checkbox_140_90_sy_kum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_140_90_sy_kum.isChecked()) {
                    blood_pressure = "1";
                }
                checkbox_140_90_sy_zyada.setChecked(false);
            }
        });
        checkbox_140_90_sy_zyada.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_140_90_sy_zyada.isChecked()) {
                    blood_pressure = "0";
                }
                checkbox_140_90_sy_kum.setChecked(false);
            }
        });
        checkbox_mojood_hai_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_mojood_hai_2.isChecked()) {
                    child_hb = "1";
                }
                checkbox_nahi_hai_2.setChecked(false);
            }
        });
        checkbox_nahi_hai_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_hai_2.isChecked()) {
                    child_hb = "0";
                }
                checkbox_mojood_hai_2.setChecked(false);
            }
        });


        checkbox_sir_kay_baal_seedha.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_sir_kay_baal_seedha.isChecked()) {
                    child_direction = "0";
                }
                checkbox_aara.setChecked(false);
                checkbox_thercha.setChecked(false);
                checkbox_ulta.setChecked(false);

            }
        });
        checkbox_aara.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_aara.isChecked()) {
                    child_direction = "1";
                }
                checkbox_sir_kay_baal_seedha.setChecked(false);
                checkbox_thercha.setChecked(false);
                checkbox_ulta.setChecked(false);
            }
        });
        checkbox_thercha.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_thercha.isChecked()) {
                    child_direction = "2";
                }
                checkbox_sir_kay_baal_seedha.setChecked(false);
                checkbox_aara.setChecked(false);
                checkbox_ulta.setChecked(false);
            }
        });
        checkbox_ulta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_ulta.isChecked()) {
                    child_direction = "3";
                }
                checkbox_sir_kay_baal_seedha.setChecked(false);
                checkbox_aara.setChecked(false);
                checkbox_thercha.setChecked(false);
            }
        });


        checkbox_normal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_normal.isChecked()) {
                    urine_obser = "0";
                }
                checkbox_sugar_ki_mojoodgi.setChecked(false);
                checkbox_protein_ki_mojoodgi.setChecked(false);
            }
        });
        checkbox_sugar_ki_mojoodgi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_sugar_ki_mojoodgi.isChecked()) {
                    urine_obser = "1";
                }
                checkbox_normal.setChecked(false);
                checkbox_protein_ki_mojoodgi.setChecked(false);
            }
        });
        checkbox_protein_ki_mojoodgi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_protein_ki_mojoodgi.isChecked()) {
                    urine_obser = "2";
                }
                checkbox_sugar_ki_mojoodgi.setChecked(false);
                checkbox_normal.setChecked(false);
            }
        });
        checkbox_100gm_dl_sy_kum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_100gm_dl_sy_kum.isChecked()) {
                    hemo_globin = "0";
                }
                checkbox_100gm_dl_sy_zyada.setChecked(false);
            }
        });
        checkbox_100gm_dl_sy_zyada.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_100gm_dl_sy_zyada.isChecked()) {
                    hemo_globin = "1";
                }
                checkbox_100gm_dl_sy_kum.setChecked(false);
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

                        String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from MANC");

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
                        Log.d("000258", "Read MANC Error: " + e.getMessage());
                    }
                }


                try {

                    Lister ls = new Lister(Mother_QabalAzPedaishHealthCenterDorahForm_Activity.this);
                    ls.createAndOpenDB();
                    // et_refferal_ki_waja = findViewById(R.id.et_refferal_ki_waja);
                    // et_refferal_hospital = findViewById(R.id.et_refferal_hospital);

                    //Edittext


                    JSONObject jobj = new JSONObject();

                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));

                    jobj.put("blood_pressure", "" + blood_pressure);
                    jobj.put("child_hb", "" + child_hb);

                    jobj.put("child_direction", "" + child_direction);
                    jobj.put("urine_obser", "" + urine_obser);
                    jobj.put("hemo_globin", "" + hemo_globin);
                    jobj.put("centimenter", "" + et_centimeter.getText().toString());
                    jobj.put("haftay", "" + et_haftay.getText().toString());
                    jobj.put("wazan", "" + et_wazan.getText().toString());
                    jobj.put("record_enter_date", "" + et_tareekh_indraj.getText().toString());
                    jobj.put("added_on", "" + "null");

                    String cur_added_on = String.valueOf(System.currentTimeMillis());

                    String ans1 = "insert into MANC (member_uid, pregnancy_id,record_data,type, data,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + mother_uid + "'," +
                            "'" + preg_id + "'," +
                            "'" + et_tareekh_indraj.getText().toString() + "'," +
                            "'" + "2" + "'," +
                            "'" + jobj + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + cur_added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000555", "Data: " + ans1);
                    Log.d("000555", "Query: " + res.toString());


                    if (Utils.haveNetworkConnection(ctx) > 0) {

                        sendPostRequest(mother_uid, preg_id, et_tareekh_indraj.getText().toString(), "2", String.valueOf(jobj), login_useruid, cur_added_on);
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

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/mother/anc";

        Log.d("000555", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {
                    // Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {
                        Log.d("000555", "response   " + response);

                        Lister ls = new Lister(Mother_QabalAzPedaishHealthCenterDorahForm_Activity.this);
                        ls.createAndOpenDB();


                        String update_record = "UPDATE MANC SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + mother_uid + "'AND pregnancy_id= '" + preg_id + "'AND added_on= '" + added_on + "'";

                        ls.executeNonQuery(update_record);
                        Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000555", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", "Err: " + e.getMessage());
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "onErrorResponse: " + error.getMessage());
                //  Toast.makeText(Mother_QabalAzPedaishHealthCenterDorahForm_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
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


        DatePickerDialog datePickerDialog = new DatePickerDialog(Mother_QabalAzPedaishHealthCenterDorahForm_Activity.this, R.style.DatePickerDialog,
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