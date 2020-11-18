package com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHaamlaRecordActivities.MotherZichgiActivities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.CustomClass.UrlClass;
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


public class Mother_ZichgiKiMansobabandiFormView_Activity extends AppCompatActivity {

    Context ctx = Mother_ZichgiKiMansobabandiFormView_Activity.this;

    ImageView iv_navigation_drawer, iv_home;
    EditText et_tareekh_indraj;
    CheckBox checkbox_ghar, checkbox_markaz_sehat, checkbox_hospital, checkbox_haan, checkbox_nahi;

    Button btn_jamaa_kre;
    String mother_uid, preg_id;
    double latitude;
    double longitude;
    // GPSTracker class
    GPSTracker gps;
    String TodayDate, added_on;

    private int mYear, mMonth, mDay;
    int date_for_condition = 0;
    int month_for_condition = 0;
    public String hold_age_date_condition = "fromage";
    String monthf2, dayf2, yearf2 = "null", record_date;

    ProgressBar pbProgress;
    JSONObject jsonObject;
    Dialog alertDialog;
    ImageView iv_editform;
    String delivery_place, emergency_vehical;
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_zichgi_ki_mansobabandi_form);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Mother_ZichgiKiMansobabandiFormView_Activity.class));

        mother_uid = getIntent().getExtras().getString("u_id");
        preg_id = getIntent().getExtras().getString("preg_id");
        record_date = getIntent().getExtras().getString("record_date");
        added_on = getIntent().getExtras().getString("added_on");

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

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

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_editform = findViewById(R.id.iv_editform);

        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);
        iv_editform.setVisibility(View.VISIBLE);

        //Progress
        pbProgress = findViewById(R.id.pbProgress);


        //EditText
        et_tareekh_indraj = findViewById(R.id.et_tareekh_indraj);

        //CheckBox
        checkbox_ghar = findViewById(R.id.checkbox_ghar);
        checkbox_markaz_sehat = findViewById(R.id.checkbox_markaz_sehat);
        checkbox_hospital = findViewById(R.id.checkbox_hospital);
        checkbox_haan = findViewById(R.id.checkbox_haan);
        checkbox_nahi = findViewById(R.id.checkbox_nahi);


        checkbox_ghar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_ghar.isChecked()) {
                    delivery_place = "0";
                }
                checkbox_markaz_sehat.setChecked(false);
                checkbox_hospital.setChecked(false);

            }
        });
        checkbox_markaz_sehat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_markaz_sehat.isChecked()) {
                    delivery_place = "1";
                }
                checkbox_ghar.setChecked(false);
                checkbox_hospital.setChecked(false);
            }
        });

        checkbox_hospital.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_hospital.isChecked()) {
                    delivery_place = "2";
                }
                checkbox_ghar.setChecked(false);
                checkbox_markaz_sehat.setChecked(false);
            }
        });
        checkbox_haan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan.isChecked()) {
                    emergency_vehical = "0";
                }
                checkbox_nahi.setChecked(false);
            }
        });
        checkbox_nahi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi.isChecked()) {
                    emergency_vehical = "1";
                }
                checkbox_haan.setChecked(false);
            }
        });
        //Button
        btn_jamaa_kre = findViewById(R.id.submit);
        btn_jamaa_kre.setVisibility(View.GONE);


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


        iv_editform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pbProgress.setVisibility(View.VISIBLE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        checkbox_nahi.setEnabled(true);
                        checkbox_haan.setEnabled(true);
                        checkbox_ghar.setEnabled(true);
                        checkbox_markaz_sehat.setEnabled(true);
                        checkbox_hospital.setEnabled(true);

                        btn_jamaa_kre.setVisibility(View.VISIBLE);
                        pbProgress.setVisibility(View.GONE);
                        iv_editform.setVisibility(View.GONE);


                    }
                }, 2500);


            }
        });


        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update_data();
            }
        });

    }

    private void update_data() {

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

                    Toast.makeText(ctx, "Data GPS", Toast.LENGTH_SHORT).show();
                } else {
                    latitude = Double.parseDouble("0.0");
                    longitude = Double.parseDouble("0.0");
                    Toast.makeText(ctx, "Not Data GPS", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.d("000258", "Read MDELIV Error: " + e.getMessage());
            }
        }

        try {

            alertDialog = new Dialog(ctx);
            LayoutInflater layout = LayoutInflater.from(ctx);
            final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

            alertDialog.setContentView(dialogView);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.show();

            String cur_added_on = String.valueOf(System.currentTimeMillis());

            if (jsonObject.has("lat")) {
                jsonObject.put("lat", "" + String.valueOf(latitude));
                jsonObject.put("lng", "" + String.valueOf(longitude));
                ;
                jsonObject.put("delivery_place", "" + delivery_place);
                jsonObject.put("emergency_vehical", "" + emergency_vehical);
                jsonObject.put("record_enter_date", "" + et_tareekh_indraj.getText().toString());
                jsonObject.put("added_on", "" + cur_added_on);

            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();
                        String update_record = "UPDATE MDELIV SET " +
                                "data='" + jsonObject.toString() + "'," +
                                "is_synced='" + 0 + "'" +
                                "WHERE member_uid = '" + mother_uid + "' AND pregnancy_id='" + preg_id + "' AND added_on='" + added_on + "' ";
                        ls.executeNonQuery(update_record);

                        boolean a = ls.executeNonQuery(update_record);
                        Log.d("000555", " Qury" + a);

                        if (Utils.haveNetworkConnection(ctx) > 0) {

                            sendPostRequest(mother_uid, preg_id, et_tareekh_indraj.getText().toString(), "0", String.valueOf(jsonObject), login_useruid, added_on);
                        } else {
                            Toast.makeText(ctx, "ڈیٹا اپڈیٹ ہوگیا ہے", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        alertDialog.dismiss();
                        Log.d("000555", " Error" + e.getMessage());
                        // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    } finally {
                        finish();
                    }
                }
            }, 2000);

        } catch (Exception e) {
            alertDialog.dismiss();
            //  Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
            Log.d("000555", " Error" + e.getMessage());
            Toast.makeText(ctx, "Something Wrong!!", Toast.LENGTH_SHORT).show();
        }


    }


    private void sendPostRequest(final String member_uid, final String pregnancy_id, final String record_data, final String type,
                                 final String data, final String added_by, final String added_on) {

        //String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/mother/deliveries";
        String url = UrlClass.update_mother_deliveries_url;

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

                        Log.d("000555", "Response   " + response);


                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE MDELIV SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + mother_uid + "'AND pregnancy_id= '" + preg_id + "'AND added_on= '" + added_on + "'";

                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d("000555", "Updated Data: " + update_record);
                        Log.d("000555", "Updated Query: " + res.toString());

                        Toast.makeText(ctx, "Data synced", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(ctx, "Data updated successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000555", "else ");
                        Toast.makeText(ctx, "ڈیٹا سروس پر سینک نہیں ہوا", Toast.LENGTH_SHORT).show();
                        // Toast.makeText(ctx, "Data has not been updated to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", "Err: " + e.getMessage());
                    //Toast.makeText(ctx, "Data has been updated incorrectly.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ctx, "ڈیٹا سینک نہیں ہوا", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "onErrorResponse: " + error.getMessage());
                //Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast.makeText(ctx, "ڈیٹا سینک نہیں ہوا", Toast.LENGTH_SHORT).show();

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


    @Override
    protected void onResume() {
        super.onResume();

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            String mData[][] = ls.executeReader("Select data from MDELIV where member_uid = '" + mother_uid + "' AND pregnancy_id = '" + preg_id + "' AND added_on = '" + added_on + "'AND type = 0");


            Log.d("000555", "DAta: " + mData[0][0]);


            String json = mData[0][0];
            jsonObject = new JSONObject(json);


            et_tareekh_indraj.setText(jsonObject.getString("record_enter_date"));


            if (jsonObject.getString("emergency_vehical").equalsIgnoreCase("1")) {
                checkbox_nahi.setChecked(true);
                emergency_vehical = jsonObject.getString("emergency_vehical");
            } else if (jsonObject.getString("emergency_vehical").equalsIgnoreCase("0")) {
                checkbox_haan.setChecked(true);
                emergency_vehical = jsonObject.getString("emergency_vehical");
            }

            if (jsonObject.getString("delivery_place").equalsIgnoreCase("0")) {
                checkbox_ghar.setChecked(true);
                delivery_place = jsonObject.getString("delivery_place");
            } else if (jsonObject.getString("delivery_place").equalsIgnoreCase("1")) {
                checkbox_markaz_sehat.setChecked(true);
                delivery_place = jsonObject.getString("delivery_place");
            } else if (jsonObject.getString("delivery_place").equalsIgnoreCase("2")) {
                checkbox_hospital.setChecked(true);
                delivery_place = jsonObject.getString("delivery_place");
            }

            et_tareekh_indraj.setEnabled(false);
            checkbox_nahi.setEnabled(false);
            checkbox_haan.setEnabled(false);
            checkbox_ghar.setEnabled(false);
            checkbox_markaz_sehat.setEnabled(false);
            checkbox_hospital.setEnabled(false);

            ls.closeDB();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("000555", " Error" + e.getMessage());
            Toast.makeText(ctx, "Something Wrong!!", Toast.LENGTH_SHORT).show();
        }

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
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }
}
