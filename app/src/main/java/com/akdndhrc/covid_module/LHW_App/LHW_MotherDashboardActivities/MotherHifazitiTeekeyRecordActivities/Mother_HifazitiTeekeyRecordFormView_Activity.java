package com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHifazitiTeekeyRecordActivities;

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

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.akdndhrc.covid_module.R.layout.activity_mother_hifaziti_teekey_record_form;


public class Mother_HifazitiTeekeyRecordFormView_Activity extends AppCompatActivity {

    Context ctx = Mother_HifazitiTeekeyRecordFormView_Activity.this;

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
    String record_date, added_on;


    ProgressBar pbProgress;
    JSONObject jsonObject;
    Dialog alertDialog;
    ImageView iv_editform;
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_mother_hifaziti_teekey_record_form);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Mother_HifazitiTeekeyRecordFormView_Activity.class));

        mother_uid = getIntent().getExtras().getString("u_id");
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


        //Progress
        pbProgress = findViewById(R.id.pbProgress);


        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_editform = findViewById(R.id.iv_editform);

        iv_editform.setVisibility(View.VISIBLE);
        iv_navigation_drawer.setVisibility(View.GONE);
        // iv_home.setVisibility(View.GONE);


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

                        et_phela.setEnabled(true);
                        et_dusra.setEnabled(true);
                        et_saal.setEnabled(true);
                        et_mahina.setEnabled(true);
                        et_first.setEnabled(true);
                        et_second.setEnabled(true);
                        et_third.setEnabled(true);
                        et_fourth.setEnabled(true);
                        et_zichgi_kis_ny_ki.setEnabled(true);
                        et_haaml_ka_natija.setEnabled(true);
                        et_tabsrah_khaas_ikdaam.setEnabled(true);

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
                jsonObject.put("pehla", "" + et_phela.getText().toString());//spinner
                jsonObject.put("et_dusra", "" + et_dusra.getText().toString());
                jsonObject.put("mahina", "" + et_mahina.getText().toString());
                jsonObject.put("saal", "" + et_saal.getText().toString());
                jsonObject.put("first", "" + et_first.getText().toString());
                jsonObject.put("second", "" + et_second.getText().toString());
                jsonObject.put("third", "" + et_third.getText().toString());
                jsonObject.put("forth", "" + et_fourth.getText().toString());
                jsonObject.put("deliveredby", "" + et_zichgi_kis_ny_ki.getText().toString());
                jsonObject.put("deliveryoutcome", "" + et_haaml_ka_natija.getText().toString());
                jsonObject.put("details", "" + et_tabsrah_khaas_ikdaam.getText().toString());
                jsonObject.put("added_on", "" + cur_added_on);

            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE MVACINE SET " +
                                "data='" + jsonObject.toString() + "'," +
                                "is_synced='" + 0 + "'" +
                                "WHERE member_uid = '" + mother_uid + "' AND added_on='" + added_on + "' ";

                        ls.executeNonQuery(update_record);

                        boolean a = ls.executeNonQuery(update_record);
                        Log.d("000555", " Qury" + a);

                        if (Utils.haveNetworkConnection(ctx) > 0) {

                            sendPostRequest(mother_uid, et_tareekh_indraj.getText().toString(), String.valueOf(jsonObject), login_useruid, added_on);
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

    private void sendPostRequest(final String member_uid, final String record_data, final String data, final String added_by, final String added_on) {

        //String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/mother/vaccinations";
        String url = UrlClass.update_mother_vaccinations_url;

        Log.d("000555", "mURL " + url);
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
                        Log.d("000555", "Response:    " + response);

                        Lister ls = new Lister(ctx);
                       ls.createAndOpenDB();

                        String update_record = "UPDATE MVACINE SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + mother_uid + "' AND added_on= '" + added_on + "' ";
                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d("000555", "Updated Data: " + update_record);
                        Log.d("000555", "Updated Query: " + res.toString());

                        Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(ctx, "Data Updated Successfully.", Toast.LENGTH_SHORT).show();


                    } else {
                        Log.d("000555", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        //  Toast.makeText(ctx, "Data has not been updated to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", "Catch:  " + e.getMessage());
                    // Toast.makeText(ctx, "Data has been updated incorrectly.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "onErrorResponse: " + error.getMessage());
                //  Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            String mData[][] = ls.executeReader("Select data from MVACINE where member_uid = '" + mother_uid + "' AND added_on = '" + added_on + "'AND record_data = '" + record_date + "'");


            Log.d("000555", "Data: " + mData[0][0]);


            String json = mData[0][0];
            jsonObject = new JSONObject(json);


            //jobj.put("plan", "" + String.valueOf(sp_naya_sabiqa.getSelectedItemPosition()-1));
            //jobj.put("provided", "" + khud_muhaiya);
            //jobj.put("refer", "" + refer);//spinner


            et_tareekh_indraj.setText((jsonObject.getString("record_data")));
            et_phela.setText(jsonObject.getString("pehla"));
            et_dusra.setText(jsonObject.getString("et_dusra"));
            et_saal.setText(jsonObject.getString("saal"));
            et_mahina.setText(jsonObject.getString("mahina"));
            et_first.setText(jsonObject.getString("first"));
            et_second.setText(jsonObject.getString("second"));
            et_third.setText(jsonObject.getString("third"));
            et_fourth.setText(jsonObject.getString("forth"));
            et_zichgi_kis_ny_ki.setText(jsonObject.getString("deliveredby"));
            et_haaml_ka_natija.setText(jsonObject.getString("deliveryoutcome"));
            et_tabsrah_khaas_ikdaam.setText((jsonObject.getString("details")));


            et_tareekh_indraj.setEnabled(false);
            et_phela.setEnabled(false);
            et_dusra.setEnabled(false);
            et_saal.setEnabled(false);
            et_mahina.setEnabled(false);
            et_first.setEnabled(false);
            et_second.setEnabled(false);
            et_third.setEnabled(false);
            et_fourth.setEnabled(false);
            et_zichgi_kis_ny_ki.setEnabled(false);
            et_haaml_ka_natija.setEnabled(false);
            et_tabsrah_khaas_ikdaam.setEnabled(false);


            ls.closeDB();
        } catch (Exception e) {
            e.printStackTrace();

            Log.d("000555", "Err: " + e.getMessage());
            Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
