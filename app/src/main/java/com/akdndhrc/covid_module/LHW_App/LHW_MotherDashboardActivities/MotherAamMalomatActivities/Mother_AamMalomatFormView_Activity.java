package com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherAamMalomatActivities;

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
import com.rey.material.widget.CheckBox;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Mother_AamMalomatFormView_Activity extends AppCompatActivity {


    Context ctx = Mother_AamMalomatFormView_Activity.this;

    ImageView iv_navigation_drawer, iv_home;
    Button btn_jamaa_kre;
    EditText et_tareekh_indraj, et_tafseel_1, et_tafseel_2;
    CheckBox checkbox_haan_1, checkbox_nahi_1, checkbox_haan_2, checkbox_nahi_2, checkbox_haan_3, checkbox_nahi_3, checkbox_haan_4, checkbox_nahi_4, checkbox_haan_5, checkbox_nahi_5,
            checkbox_haan_6, checkbox_nahi_6, checkbox_haan_7, checkbox_nahi_7, checkbox_haan_8, checkbox_nahi_8;
    String mother_uid;
    GPSTracker gps;
    double latitude;
    double longitude;
    String TodayDate, record_date, added_on;
    ProgressBar pbProgress;
    JSONObject jsonObject;
    Dialog alertDialog;
    ImageView iv_editform;

    String disease_history, mom_on_med, abortion_hist, still_birth, old_preg, highbp_preg, csec, extra_bleed;

    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_aam_malomat_form);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Mother_AamMalomatFormView_Activity.class));

        mother_uid = getIntent().getExtras().getString("u_id");
        added_on = getIntent().getExtras().getString("added_on");
        record_date = getIntent().getExtras().getString("record_date");


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
        //GPS\
        // check_gps();

        try {
            serviceLocation = new ServiceLocation(ctx);
            serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            serviceLocation.callAsynchronousTask();
        } catch (Exception e) {
            Log.d("000555", "GPS Service Err:  " + e.getMessage());
        }

        //EditText
        et_tareekh_indraj = findViewById(R.id.et_tareekh_indraj);
        et_tafseel_1 = findViewById(R.id.et_tafseel_1);
        et_tafseel_2 = findViewById(R.id.et_tafseel_2);

        //CheckBox
        checkbox_haan_1 = findViewById(R.id.checkbox_haan_1);
        checkbox_nahi_1 = findViewById(R.id.checkbox_nahi_1);
        checkbox_haan_2 = findViewById(R.id.checkbox_haan_2);
        checkbox_nahi_2 = findViewById(R.id.checkbox_nahi_2);
        checkbox_haan_3 = findViewById(R.id.checkbox_haan_3);
        checkbox_nahi_3 = findViewById(R.id.checkbox_nahi_3);
        checkbox_haan_4 = findViewById(R.id.checkbox_haan_4);
        checkbox_nahi_4 = findViewById(R.id.checkbox_nahi_4);
        checkbox_haan_5 = findViewById(R.id.checkbox_haan_5);
        checkbox_nahi_5 = findViewById(R.id.checkbox_nahi_5);
        checkbox_haan_6 = findViewById(R.id.checkbox_haan_6);
        checkbox_nahi_6 = findViewById(R.id.checkbox_nahi_6);
        checkbox_haan_7 = findViewById(R.id.checkbox_haan_7);
        checkbox_nahi_7 = findViewById(R.id.checkbox_nahi_7);
        checkbox_haan_8 = findViewById(R.id.checkbox_haan_8);
        checkbox_nahi_8 = findViewById(R.id.checkbox_nahi_8);


        checkbox_haan_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_1.isChecked()) {
                    disease_history = "1";
                }
                checkbox_nahi_1.setChecked(false);

            }
        });
        checkbox_nahi_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_1.isChecked()) {
                    disease_history = "0";
                }
                checkbox_haan_1.setChecked(false);
            }
        });

        checkbox_haan_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_2.isChecked()) {
                    mom_on_med = "1";
                }
                checkbox_nahi_2.setChecked(false);

            }
        });
        checkbox_nahi_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_2.isChecked()) {
                    mom_on_med = "0";
                }
                checkbox_haan_2.setChecked(false);
            }
        });
        checkbox_haan_3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_3.isChecked()) {
                    abortion_hist = "1";
                }
                checkbox_nahi_3.setChecked(false);
            }
        });
        checkbox_nahi_3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_3.isChecked()) {
                    abortion_hist = "0";
                }
                checkbox_haan_3.setChecked(false);
            }
        });
        checkbox_haan_4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_4.isChecked()) {
                    still_birth = "1";
                }
                checkbox_nahi_4.setChecked(false);
            }
        });
        checkbox_nahi_4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_4.isChecked()) {
                    still_birth = "0";
                }
                checkbox_haan_4.setChecked(false);
            }
        });
        checkbox_haan_5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_5.isChecked()) {
                    old_preg = "1";
                }
                checkbox_nahi_5.setChecked(false);
            }
        });
        checkbox_nahi_5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_5.isChecked()) {
                    old_preg = "0";
                }
                checkbox_haan_5.setChecked(false);
            }
        });
        checkbox_haan_6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_6.isChecked()) {
                    highbp_preg = "1";
                }
                checkbox_nahi_6.setChecked(false);
            }
        });
        checkbox_nahi_6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_6.isChecked()) {
                    highbp_preg = "0";
                }
                checkbox_haan_6.setChecked(false);
            }
        });
        checkbox_haan_7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_7.isChecked()) {
                    csec = "1";
                }
                checkbox_nahi_7.setChecked(false);
            }
        });
        checkbox_nahi_7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_7.isChecked()) {
                    csec = "0";
                }
                checkbox_haan_7.setChecked(false);
            }
        });

        checkbox_haan_8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_8.isChecked()) {
                    extra_bleed = "1";
                }
                checkbox_nahi_8.setChecked(false);
            }
        });
        checkbox_nahi_8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_8.isChecked()) {
                    extra_bleed = "0";
                }
                checkbox_haan_8.setChecked(false);
            }
        });

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_editform = findViewById(R.id.iv_editform);

        iv_editform.setVisibility(View.VISIBLE);
        iv_navigation_drawer.setVisibility(View.GONE);
        //  iv_home.setVisibility(View.GONE);

        //Progress
        pbProgress = findViewById(R.id.pbProgress);

        //Button
        btn_jamaa_kre = findViewById(R.id.submit);
        btn_jamaa_kre.setVisibility(View.GONE);

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

        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_data();

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


                        et_tafseel_1.setEnabled(true);
                        et_tafseel_2.setEnabled(true);
                        checkbox_haan_1.setEnabled(true);
                        checkbox_nahi_1.setEnabled(true);
                        checkbox_haan_2.setEnabled(true);
                        checkbox_nahi_2.setEnabled(true);
                        checkbox_haan_3.setEnabled(true);
                        checkbox_nahi_3.setEnabled(true);
                        checkbox_haan_4.setEnabled(true);
                        checkbox_nahi_4.setEnabled(true);
                        checkbox_haan_5.setEnabled(true);
                        checkbox_nahi_5.setEnabled(true);
                        checkbox_haan_6.setEnabled(true);
                        checkbox_nahi_6.setEnabled(true);
                        checkbox_haan_7.setEnabled(true);
                        checkbox_nahi_7.setEnabled(true);
                        checkbox_haan_8.setEnabled(true);
                        checkbox_nahi_8.setEnabled(true);

                        btn_jamaa_kre.setVisibility(View.VISIBLE);
                        pbProgress.setVisibility(View.GONE);
                        iv_editform.setVisibility(View.GONE);


                    }
                }, 2500);


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

                String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from MMALOOM");

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
                Log.d("000258", "Read MMALOOM Error: " + e.getMessage());
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
                jsonObject.put("tafseel_1", "" + et_tafseel_1.getText().toString());
                jsonObject.put("tafseel_2", "" + et_tafseel_2.getText().toString());
                jsonObject.put("disease_history", "" + disease_history);//spinner
                jsonObject.put("mom_on_med", "" + mom_on_med);
                jsonObject.put("abortion_hist", "" + abortion_hist);
                jsonObject.put("still_birth", "" + still_birth);
                jsonObject.put("old_preg", "" + old_preg);
                jsonObject.put("highbp_preg", "" + highbp_preg);
                jsonObject.put("csec", "" + csec);
                jsonObject.put("extra_bleed", "" + extra_bleed);
                jsonObject.put("added_on", "" + cur_added_on);

            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {

                        Lister ls = new Lister(ctx);
                      ls.createAndOpenDB();
                        String update_record = "UPDATE MMALOOM SET " +
                                "data='" + jsonObject.toString() + "'," +
                                "is_synced='" + 0 + "'" +
                                "WHERE member_uid = '" + mother_uid + "' AND added_on='" + added_on + "' ";

                        ls.executeNonQuery(update_record);

                        boolean a = ls.executeNonQuery(update_record);
                        Log.d("000555", " Qury" + a);
                        if (Utils.haveNetworkConnection(ctx) > 0) {

                            sendPostRequest(mother_uid, TodayDate, String.valueOf(jsonObject), login_useruid, added_on);
                        } else {
                            Toast.makeText(ctx, R.string.dataEdited, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
        }


    }

    private void sendPostRequest(final String member_uid, final String record_data,
                                 final String data, final String added_by, final String added_on) {

        // String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/mother/info";
        String url = UrlClass.update_mother_info_url;

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

                        Log.d("000555", "Response    " + response);

                        Lister ls = new Lister(Mother_AamMalomatFormView_Activity.this);
                     ls.createAndOpenDB();

                        String update_record = "UPDATE MMALOOM SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + mother_uid + "'AND added_on= '" + added_on + "'";

                        ls.executeNonQuery(update_record);
                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d("000555", "Data: " + update_record);
                        Log.d("000555", "Query: " + res.toString());

                        Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();
                        // Toast.makeText(ctx, "Data Updated Successfully.", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000555", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        //   Toast.makeText(Mother_AamMalomatFormView_Activity.this, "Data has not been updated to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", "Error: " + e.getMessage());
                    // Toast.makeText(Mother_AamMalomatFormView_Activity.this, "Data has been updated incorrectly.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "onErrorResponse: " + error.getMessage());
                // Toast.makeText(Mother_AamMalomatFormView_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();

        try {
            Lister ls = new Lister(ctx);
          ls.createAndOpenDB();
            String mData[][] = ls.executeReader("Select data from MMALOOM where member_uid = '" + mother_uid + "' AND added_on = '" + added_on + "'AND record_data = '" + record_date + "'");


            Log.d("000888", "Data: " + mData[0][0]);


            String json = mData[0][0];
            jsonObject = new JSONObject(json);

            et_tareekh_indraj.setText((jsonObject.getString("record_enter_date")));

            if (jsonObject.getString("disease_history").equalsIgnoreCase("1")) {
                checkbox_haan_1.setChecked(true);
                disease_history = jsonObject.getString("disease_history");
            } else if (jsonObject.getString("disease_history").equalsIgnoreCase("0")) {
                checkbox_nahi_1.setChecked(true);
                disease_history = jsonObject.getString("disease_history");
            }
            et_tafseel_1.setText((jsonObject.getString("tafseel_1")));


            if (jsonObject.getString("mom_on_med").equalsIgnoreCase("1")) {
                checkbox_haan_2.setChecked(true);
                mom_on_med = jsonObject.getString("mom_on_med");
            } else if (jsonObject.getString("mom_on_med").equalsIgnoreCase("0")) {
                checkbox_nahi_2.setChecked(true);
                mom_on_med = jsonObject.getString("mom_on_med");
            }
            et_tafseel_2.setText(jsonObject.getString("tafseel_2"));


            if (jsonObject.getString("abortion_hist").equalsIgnoreCase("1")) {
                checkbox_haan_3.setChecked(true);
                abortion_hist = jsonObject.getString("abortion_hist");
            } else if (jsonObject.getString("abortion_hist").equalsIgnoreCase("0")) {
                checkbox_nahi_3.setChecked(true);
                abortion_hist = jsonObject.getString("abortion_hist");
            }

            if (jsonObject.getString("still_birth").equalsIgnoreCase("1")) {
                checkbox_haan_4.setChecked(true);
                still_birth = jsonObject.getString("still_birth");
            } else if (jsonObject.getString("still_birth").equalsIgnoreCase("0")) {
                checkbox_nahi_4.setChecked(true);
                still_birth = jsonObject.getString("still_birth");
            }

            if (jsonObject.getString("old_preg").equalsIgnoreCase("1")) {
                checkbox_haan_5.setChecked(true);
                old_preg = jsonObject.getString("old_preg");
            } else if (jsonObject.getString("old_preg").equalsIgnoreCase("0")) {
                checkbox_nahi_5.setChecked(true);
                old_preg = jsonObject.getString("old_preg");
            }

            if (jsonObject.getString("highbp_preg").equalsIgnoreCase("1")) {
                checkbox_haan_6.setChecked(true);
                highbp_preg = jsonObject.getString("highbp_preg");
            } else if (jsonObject.getString("highbp_preg").equalsIgnoreCase("0")) {
                checkbox_nahi_6.setChecked(true);
                highbp_preg = jsonObject.getString("highbp_preg");
            }

            if (jsonObject.getString("csec").equalsIgnoreCase("1")) {
                checkbox_haan_7.setChecked(true);
                csec = jsonObject.getString("csec");
            } else if (jsonObject.getString("csec").equalsIgnoreCase("0")) {
                checkbox_nahi_7.setChecked(true);
                csec = jsonObject.getString("csec");
            }

            if (jsonObject.getString("extra_bleed").equalsIgnoreCase("1")) {
                checkbox_haan_8.setChecked(true);
                extra_bleed = jsonObject.getString("extra_bleed");
            } else if (jsonObject.getString("extra_bleed").equalsIgnoreCase("0")) {
                checkbox_nahi_8.setChecked(true);
                extra_bleed = jsonObject.getString("extra_bleed");
            }

            et_tareekh_indraj.setEnabled(false);
            et_tafseel_1.setEnabled(false);
            et_tafseel_2.setEnabled(false);
            checkbox_haan_1.setEnabled(false);
            checkbox_nahi_1.setEnabled(false);
            checkbox_haan_2.setEnabled(false);
            checkbox_nahi_2.setEnabled(false);
            checkbox_haan_3.setEnabled(false);
            checkbox_nahi_3.setEnabled(false);
            checkbox_haan_4.setEnabled(false);
            checkbox_nahi_4.setEnabled(false);
            checkbox_haan_5.setEnabled(false);
            checkbox_nahi_5.setEnabled(false);
            checkbox_haan_6.setEnabled(false);
            checkbox_nahi_6.setEnabled(false);
            checkbox_haan_7.setEnabled(false);
            checkbox_nahi_7.setEnabled(false);
            checkbox_haan_8.setEnabled(false);
            checkbox_nahi_8.setEnabled(false);


            ls.closeDB();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("000888", "Err: " + e.getMessage());
            Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
