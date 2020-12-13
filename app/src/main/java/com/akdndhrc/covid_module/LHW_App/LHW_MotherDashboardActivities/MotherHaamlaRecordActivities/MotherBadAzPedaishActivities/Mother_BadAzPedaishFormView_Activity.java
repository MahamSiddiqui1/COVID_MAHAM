package com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHaamlaRecordActivities.MotherBadAzPedaishActivities;

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


public class Mother_BadAzPedaishFormView_Activity extends AppCompatActivity {

    Context ctx = Mother_BadAzPedaishFormView_Activity.this;

    EditText et_tareekh_indraj;
    CheckBox checkbox_haan_1, checkbox_nahi_1, checkbox_haan_2, checkbox_nahi_2, checkbox_haan_3, checkbox_nahi_3, checkbox_100_sy_kum_4, checkbox_100_sy_zayada_4,
            checkbox_haan_5, checkbox_nahi_5, checkbox_100_sy_kum_6, checkbox_100_sy_zayada_6, checkbox_haan_7, checkbox_nahi_7, checkbox_haan_8, checkbox_nahi_8, checkbox_haan_9,
            checkbox_nahi_9, checkbox_haan_10, checkbox_nahi_10, checkbox_haan_11, checkbox_nahi_11, checkbox_haan_12, checkbox_nahi_12;

    Button btn_jamaa_kre;
    ImageView iv_navigation_drawer, iv_home;

    String mother_uid, preg_id, TodayDate, added_on;
    double latitude;
    double longitude;
    // GPSTracker class
    GPSTracker gps;

    private int mYear, mMonth, mDay;
    int date_for_condition = 0;
    int month_for_condition = 0;
    public String hold_age_date_condition = "fromage";
    String monthf2, dayf2, yearf2 = "null", record_date;

    ProgressBar pbProgress;
    JSONObject jsonObject;
    Dialog alertDialog;
    ImageView iv_editform;

    String mother_alive, baby_born, elemnt_left_inside, hb, bleeding_vag, temp, odour_vag, bleeding_breast, animia, breastfeed, mother_niut, familyplan;
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_bad_az_pedaish_form);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Mother_BadAzPedaishFormView_Activity.class));

        mother_uid = getIntent().getExtras().getString("u_id");
        record_date = getIntent().getExtras().getString("record_date");
        added_on = getIntent().getExtras().getString("added_on");
        preg_id = getIntent().getExtras().getString("preg_id");

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

        //check_gps();

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());


        //EditTExt
        et_tareekh_indraj = findViewById(R.id.et_tareekh_indraj);

        //Button
        btn_jamaa_kre = findViewById(R.id.submit);
        btn_jamaa_kre.setVisibility(View.GONE);

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_editform = findViewById(R.id.iv_editform);

        iv_editform.setVisibility(View.VISIBLE);
        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);


        //Prgress Bar
        pbProgress = findViewById(R.id.pbProgress);

        //Checkbox
        checkbox_haan_1 = findViewById(R.id.checkbox_haan_1);
        checkbox_nahi_1 = findViewById(R.id.checkbox_nahi_1);
        checkbox_haan_2 = findViewById(R.id.checkbox_haan_2);
        checkbox_nahi_2 = findViewById(R.id.checkbox_nahi_2);
        checkbox_haan_3 = findViewById(R.id.checkbox_haan_3);
        checkbox_nahi_3 = findViewById(R.id.checkbox_nahi_3);
        checkbox_100_sy_kum_4 = findViewById(R.id.checkbox_100_sy_kum_4);
        checkbox_100_sy_zayada_4 = findViewById(R.id.checkbox_100_sy_zayada_4);
        checkbox_haan_5 = findViewById(R.id.checkbox_haan_5);
        checkbox_nahi_5 = findViewById(R.id.checkbox_nahi_5);
        checkbox_100_sy_kum_6 = findViewById(R.id.checkbox_100_sy_kum_6);
        checkbox_100_sy_zayada_6 = findViewById(R.id.checkbox_100_sy_zayada_6);
        checkbox_haan_7 = findViewById(R.id.checkbox_haan_7);
        checkbox_nahi_7 = findViewById(R.id.checkbox_nahi_7);
        checkbox_haan_8 = findViewById(R.id.checkbox_haan_8);
        checkbox_nahi_8 = findViewById(R.id.checkbox_nahi_8);
        checkbox_haan_9 = findViewById(R.id.checkbox_haan_9);
        checkbox_nahi_9 = findViewById(R.id.checkbox_nahi_9);
        checkbox_haan_10 = findViewById(R.id.checkbox_haan_10);
        checkbox_nahi_10 = findViewById(R.id.checkbox_nahi_10);
        checkbox_haan_11 = findViewById(R.id.checkbox_haan_11);
        checkbox_nahi_11 = findViewById(R.id.checkbox_nahi_11);
        checkbox_haan_12 = findViewById(R.id.checkbox_haan_12);
        checkbox_nahi_12 = findViewById(R.id.checkbox_nahi_12);

        checkbox_haan_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_1.isChecked()) {
                    mother_alive = "1";
                }
                checkbox_nahi_1.setChecked(false);

            }
        });
        checkbox_nahi_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_1.isChecked()) {
                    mother_alive = "0";
                }
                checkbox_haan_1.setChecked(false);
            }
        });


        checkbox_haan_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_2.isChecked()) {
                    baby_born = "1";
                }
                checkbox_nahi_2.setChecked(false);
            }
        });
        checkbox_nahi_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_2.isChecked()) {
                    baby_born = "0";
                }
                checkbox_haan_2.setChecked(false);
            }
        });


        checkbox_haan_3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_3.isChecked()) {
                    elemnt_left_inside = "1";
                }
                checkbox_nahi_3.setChecked(false);
            }
        });
        checkbox_nahi_3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_3.isChecked()) {
                    elemnt_left_inside = "0";
                }
                checkbox_haan_3.setChecked(false);
            }
        });

        checkbox_100_sy_kum_4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_100_sy_kum_4.isChecked()) {
                    hb = "1";
                }
                checkbox_100_sy_zayada_4.setChecked(false);
            }
        });
        checkbox_100_sy_zayada_4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_100_sy_zayada_4.isChecked()) {
                    hb = "0";
                }
                checkbox_100_sy_kum_4.setChecked(false);
            }
        });


        checkbox_haan_5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_5.isChecked()) {
                    bleeding_vag = "1";
                }
                checkbox_nahi_5.setChecked(false);
            }
        });
        checkbox_nahi_5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_5.isChecked()) {
                    bleeding_vag = "0";
                }
                checkbox_haan_5.setChecked(false);
            }
        });


        checkbox_100_sy_kum_6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_100_sy_kum_6.isChecked()) {
                    temp = "1";
                }
                checkbox_100_sy_zayada_6.setChecked(false);
            }
        });
        checkbox_100_sy_zayada_6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_100_sy_zayada_6.isChecked()) {
                    temp = "0";
                }
                checkbox_100_sy_kum_6.setChecked(false);
            }
        });
        checkbox_haan_7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_7.isChecked()) {
                    odour_vag = "1";
                }
                checkbox_nahi_7.setChecked(false);
            }
        });
        checkbox_nahi_7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_7.isChecked()) {
                    odour_vag = "0";
                }
                checkbox_haan_7.setChecked(false);
            }
        });
        checkbox_haan_8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_8.isChecked()) {
                    bleeding_breast = "1";
                }
                checkbox_nahi_8.setChecked(false);
            }
        });
        checkbox_nahi_8.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_8.isChecked()) {
                    bleeding_breast = "0";
                }
                checkbox_haan_8.setChecked(false);
            }
        });
        checkbox_haan_9.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_9.isChecked()) {
                    animia = "1";
                }
                checkbox_nahi_9.setChecked(false);
            }
        });
        checkbox_nahi_9.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_9.isChecked()) {
                    animia = "0";
                }
                checkbox_haan_9.setChecked(false);
            }
        });

        checkbox_haan_10.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_10.isChecked()) {
                    breastfeed = "1";
                }
                checkbox_nahi_10.setChecked(false);
            }
        });
        checkbox_nahi_10.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_10.isChecked()) {
                    breastfeed = "0";
                }
                checkbox_haan_10.setChecked(false);
            }
        });
        checkbox_haan_11.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_11.isChecked()) {
                    mother_niut = "1";
                }
                checkbox_nahi_11.setChecked(false);
            }
        });
        checkbox_nahi_11.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_11.isChecked()) {
                    mother_niut = "0";
                }
                checkbox_haan_11.setChecked(false);
            }
        });
        checkbox_haan_12.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_haan_12.isChecked()) {
                    familyplan = "1";
                }
                checkbox_nahi_12.setChecked(false);
            }
        });
        checkbox_nahi_12.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_12.isChecked()) {
                    familyplan = "0";
                }
                checkbox_haan_12.setChecked(false);
            }
        });

        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, R.string.nav, Toast.LENGTH_SHORT).show();
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

                        checkbox_haan_1.setEnabled(true);
                        checkbox_nahi_1.setEnabled(true);
                        checkbox_haan_2.setEnabled(true);
                        checkbox_nahi_2.setEnabled(true);
                        checkbox_haan_3.setEnabled(true);
                        checkbox_nahi_3.setEnabled(true);
                        checkbox_100_sy_kum_4.setEnabled(true);
                        checkbox_100_sy_zayada_4.setEnabled(true);
                        checkbox_haan_5.setEnabled(true);
                        checkbox_nahi_5.setEnabled(true);
                        checkbox_100_sy_kum_6.setEnabled(true);
                        checkbox_100_sy_zayada_6.setEnabled(true);
                        checkbox_haan_7.setEnabled(true);
                        checkbox_nahi_7.setEnabled(true);
                        checkbox_haan_8.setEnabled(true);
                        checkbox_nahi_8.setEnabled(true);
                        checkbox_haan_9.setEnabled(true);
                        checkbox_nahi_9.setEnabled(true);
                        checkbox_haan_10.setEnabled(true);
                        checkbox_nahi_10.setEnabled(true);
                        checkbox_haan_11.setEnabled(true);
                        checkbox_nahi_11.setEnabled(true);
                        checkbox_haan_12.setEnabled(true);
                        checkbox_nahi_12.setEnabled(true);

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

                String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from MPNC");

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
                Log.d("000258", "Read MPNC Error: " + e.getMessage());
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
                jsonObject.put("mother_alive", "" + mother_alive);
                jsonObject.put("baby_born", "" + baby_born);
                jsonObject.put("elemnt_left_inside", "" + elemnt_left_inside);
                jsonObject.put("hb", "" + hb);
                jsonObject.put("bleeding_vag", "" + bleeding_vag);
                jsonObject.put("temp", "" + temp);
                jsonObject.put("odour_vag", "" + odour_vag);
                jsonObject.put("bleeding_breast", "" + bleeding_breast);
                jsonObject.put("animia", "" + animia);
                jsonObject.put("breastfeed", "" + breastfeed);
                jsonObject.put("mother_niut", "" + mother_niut);
                jsonObject.put("familyplan", "" + familyplan);
                jsonObject.put("added_on", "" + cur_added_on);
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {

                        Lister ls = new Lister(ctx);
                      ls.createAndOpenDB();

                        String update_record = "UPDATE MPNC SET " +
                                "data='" + jsonObject.toString() + "'," +
                                "is_synced='" + 0 + "'" +
                                "WHERE member_uid = '" + mother_uid + "' AND pregnancy_id='" + preg_id + "' AND added_on='" + added_on + "' ";
                        ls.executeNonQuery(update_record);

                        boolean a = ls.executeNonQuery(update_record);
                        Log.d("000555", " Qury" + a);

                        if (Utils.haveNetworkConnection(ctx) > 0) {

                            sendPostRequest(mother_uid, preg_id, et_tareekh_indraj.getText().toString(), "0", String.valueOf(jsonObject), login_useruid, added_on);
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

    private void sendPostRequest(final String member_uid, final String pregnancy_id, final String record_data, final String type,
                                 final String data, final String added_by, final String added_on) {

        //String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/mother/pnc";
        String url = UrlClass.update_mother_pnc_url;

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

                        Log.d("000555", "Response:    " + response);

                        Lister ls = new Lister(ctx);
                      ls.createAndOpenDB();

                        String update_record = "UPDATE MPNC SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + mother_uid + "' AND pregnancy_id='" + preg_id + "' AND added_on='" + added_on + "' ";

                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d("000555", "Updated Data: " + update_record);
                        Log.d("000555", "Updated Query: " + res.toString());

                        Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();

                        /*/Toast.makeText(ctx, "Data updated successfully", Toast.LENGTH_SHORT).show();


                       /* Intent intent = new Intent(ctx, Add_Family_Member_Activity.class);
                        startActivity(intent);*/

                    } else {
                        Log.d("000555", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        //  Toast.makeText(ctx, "Data has not been updated to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", "Err: " + e.getMessage());
                    //Toast.makeText(ctx, "Data has been updated incorrectly.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "onErrorResponse: " + error.getMessage());
                //Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onResume() {
        super.onResume();

        try {
            Lister ls = new Lister(ctx);
          ls.createAndOpenDB();

            String mData[][] = ls.executeReader("Select data from MPNC where member_uid = '" + mother_uid + "' AND pregnancy_id = '" + preg_id + "'AND added_on = '" + added_on + "' AND type = 0");


            Log.d("000555", "Data: " + mData[0][0]);


            String json = mData[0][0];
            jsonObject = new JSONObject(json);


            et_tareekh_indraj.setText(jsonObject.getString("record_enter_date"));


            if (jsonObject.getString("mother_alive").equalsIgnoreCase("1")) {
                checkbox_haan_1.setChecked(true);
                mother_alive = jsonObject.getString("mother_alive");
            } else if (jsonObject.getString("mother_alive").equalsIgnoreCase("0")) {
                checkbox_nahi_1.setChecked(true);
                mother_alive = jsonObject.getString("mother_alive");
            }


            if (jsonObject.getString("baby_born").equalsIgnoreCase("1")) {
                checkbox_haan_2.setChecked(true);
                baby_born = jsonObject.getString("baby_born");
            } else if (jsonObject.getString("baby_born").equalsIgnoreCase("0")) {
                checkbox_nahi_2.setChecked(true);
                baby_born = jsonObject.getString("baby_born");
            }


            if (jsonObject.getString("elemnt_left_inside").equalsIgnoreCase("1")) {
                checkbox_haan_3.setChecked(true);
                elemnt_left_inside = jsonObject.getString("elemnt_left_inside");
            } else if (jsonObject.getString("elemnt_left_inside").equalsIgnoreCase("0")) {
                checkbox_nahi_3.setChecked(true);
                elemnt_left_inside = jsonObject.getString("elemnt_left_inside");
            }


            if (jsonObject.getString("hb").equalsIgnoreCase("1")) {
                checkbox_100_sy_kum_4.setChecked(true);
                hb = jsonObject.getString("hb");
            } else if (jsonObject.getString("hb").equalsIgnoreCase("0")) {
                checkbox_100_sy_zayada_4.setChecked(true);
                hb = jsonObject.getString("hb");
            }


            if (jsonObject.getString("bleeding_vag").equalsIgnoreCase("1")) {
                checkbox_haan_5.setChecked(true);
                bleeding_vag = jsonObject.getString("bleeding_vag");
            } else if (jsonObject.getString("bleeding_vag").equalsIgnoreCase("0")) {
                checkbox_nahi_5.setChecked(true);
                bleeding_vag = jsonObject.getString("bleeding_vag");
            }


            if (jsonObject.getString("temp").equalsIgnoreCase("1")) {
                checkbox_100_sy_kum_6.setChecked(true);
                temp = jsonObject.getString("temp");
            } else if (jsonObject.getString("temp").equalsIgnoreCase("0")) {
                checkbox_100_sy_zayada_6.setChecked(true);
                temp = jsonObject.getString("temp");
            }


            if (jsonObject.getString("odour_vag").equalsIgnoreCase("1")) {
                checkbox_haan_7.setChecked(true);
                odour_vag = jsonObject.getString("odour_vag");
            } else if (jsonObject.getString("odour_vag").equalsIgnoreCase("0")) {
                checkbox_nahi_7.setChecked(true);
                odour_vag = jsonObject.getString("odour_vag");
            }


            if (jsonObject.getString("bleeding_breast").equalsIgnoreCase("1")) {
                checkbox_haan_8.setChecked(true);
                bleeding_breast = jsonObject.getString("bleeding_breast");
            } else if (jsonObject.getString("bleeding_breast").equalsIgnoreCase("0")) {
                checkbox_nahi_8.setChecked(true);
                bleeding_breast = jsonObject.getString("bleeding_breast");
            }


            if (jsonObject.getString("animia").equalsIgnoreCase("1")) {
                checkbox_haan_9.setChecked(true);
                animia = jsonObject.getString("animia");
            } else if (jsonObject.getString("animia").equalsIgnoreCase("0")) {
                checkbox_nahi_9.setChecked(true);
                animia = jsonObject.getString("animia");
            }


            if (jsonObject.getString("breastfeed").equalsIgnoreCase("1")) {
                checkbox_haan_10.setChecked(true);
                breastfeed = jsonObject.getString("breastfeed");
            } else if (jsonObject.getString("breastfeed").equalsIgnoreCase("0")) {
                checkbox_nahi_10.setChecked(true);
                breastfeed = jsonObject.getString("breastfeed");
            }


            if (jsonObject.getString("mother_niut").equalsIgnoreCase("1")) {
                checkbox_haan_11.setChecked(true);
                mother_niut = jsonObject.getString("mother_niut");
            } else if (jsonObject.getString("mother_niut").equalsIgnoreCase("0")) {
                checkbox_nahi_11.setChecked(true);
                mother_niut = jsonObject.getString("mother_niut");
            }


            if (jsonObject.getString("familyplan").equalsIgnoreCase("1")) {
                checkbox_haan_12.setChecked(true);
                familyplan = jsonObject.getString("familyplan");
            } else if (jsonObject.getString("familyplan").equalsIgnoreCase("0")) {
                checkbox_nahi_12.setChecked(true);
                familyplan = jsonObject.getString("familyplan");
            }


            et_tareekh_indraj.setEnabled(false);
            checkbox_haan_1.setEnabled(false);
            checkbox_nahi_1.setEnabled(false);
            checkbox_haan_2.setEnabled(false);
            checkbox_nahi_2.setEnabled(false);
            checkbox_haan_3.setEnabled(false);
            checkbox_nahi_3.setEnabled(false);
            checkbox_100_sy_kum_4.setEnabled(false);
            checkbox_100_sy_zayada_4.setEnabled(false);
            checkbox_haan_5.setEnabled(false);
            checkbox_nahi_5.setEnabled(false);
            checkbox_100_sy_kum_6.setEnabled(false);
            checkbox_100_sy_zayada_6.setEnabled(false);
            checkbox_haan_7.setEnabled(false);
            checkbox_nahi_7.setEnabled(false);
            checkbox_haan_8.setEnabled(false);
            checkbox_nahi_8.setEnabled(false);
            checkbox_haan_9.setEnabled(false);
            checkbox_nahi_9.setEnabled(false);
            checkbox_haan_10.setEnabled(false);
            checkbox_nahi_10.setEnabled(false);
            checkbox_haan_11.setEnabled(false);
            checkbox_nahi_11.setEnabled(false);
            checkbox_haan_12.setEnabled(false);
            checkbox_nahi_12.setEnabled(false);

            ls.closeDB();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("000555", " Error" + e.getMessage());
            Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
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
