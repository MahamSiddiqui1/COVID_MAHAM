package com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHaamlaRecordActivities.MotherQabalAzPedaishActivities;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class Mother_QabalAzPedaishDorahFormView_Activity extends AppCompatActivity {

    Context ctx = Mother_QabalAzPedaishDorahFormView_Activity.this;

    ImageView iv_navigation_drawer, iv_home;
    EditText et_tareekh_indraj, et_haaml_ka_dorania, et_tareekh_muaina, et_takleef_shikayat; //et_folaad_ki_gooliyou_kai_mutaliq_hidayat;
    CheckBox checkbox_mojood_hai_1, checkbox_nahi_hai_1, checkbox_mojood_hai_2, checkbox_nahi_hai_2, checkbox_mehsoos_hoti_hai, checkbox_nahi_mehsoos_hoti,
            checkbox_mojood_hai_4, checkbox_nahi_hai_4, checkbox_mojood_hai_5, checkbox_nahi_hai_5, checkbox_mojood_hai_6, checkbox_nahi_hai_6;

    Button btn_jamaa_kre;
    String mother_uid, preg_id;
    double latitude;
    double longitude;
    // GPSTracker class
    GPSTracker gps;
    String TodayDate, record_date, added_on;

    private int mYear, mMonth, mDay;
    int date_for_condition = 0;
    int month_for_condition = 0;
    public String hold_age_date_condition = "fromage";
    String monthf2, dayf2, yearf2 = "null";

    ProgressBar pbProgress;
    JSONObject jsonObject;
    Dialog alertDialog;
    ImageView iv_editform;
    String khoon_kami, sojan, bachay_ki_harqat, bleeding, bad_odour, breast_problem;
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid;

    RelativeLayout rl_sub,rl_add,rl_IronSup;
    TextView tv_count;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_qabal_az_pedaish_dorah_form);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Mother_QabalAzPedaishDorahFormView_Activity.class));

        mother_uid = getIntent().getExtras().getString("u_id");
        preg_id = getIntent().getExtras().getString("preg_id");
        record_date = getIntent().getExtras().getString("record_date");
        added_on = getIntent().getExtras().getString("added_on");


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

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_editform = findViewById(R.id.iv_editform);

        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);
        iv_editform.setVisibility(View.VISIBLE);

        //Prgress Bar
        pbProgress = findViewById(R.id.pbProgress);

        //EditText
        et_tareekh_indraj = findViewById(R.id.et_tareekh_indraj);
        et_haaml_ka_dorania = findViewById(R.id.et_haaml_ka_dorania);
        et_tareekh_muaina = findViewById(R.id.et_tareekh_muaina);
        et_takleef_shikayat = findViewById(R.id.et_takleef_shikayat);
//        et_folaad_ki_gooliyou_kai_mutaliq_hidayat = findViewById(R.id.et_folaad_ki_gooliyou_kai_mutaliq_hidayat);

        //CheckBox
        checkbox_mojood_hai_1 = findViewById(R.id.checkbox_mojood_hai_1);
        checkbox_nahi_hai_1 = findViewById(R.id.checkbox_nahi_hai_1);
        checkbox_mojood_hai_2 = findViewById(R.id.checkbox_mojood_hai_2);
        checkbox_nahi_hai_2 = findViewById(R.id.checkbox_nahi_hai_2);
        checkbox_mehsoos_hoti_hai = findViewById(R.id.checkbox_mehsoos_hoti_hai);
        checkbox_nahi_mehsoos_hoti = findViewById(R.id.checkbox_nahi_mehsoos_hoti);
        checkbox_mojood_hai_4 = findViewById(R.id.checkbox_mojood_hai_4);
        checkbox_nahi_hai_4 = findViewById(R.id.checkbox_nahi_hai_4);
        checkbox_mojood_hai_5 = findViewById(R.id.checkbox_mojood_hai_5);
        checkbox_nahi_hai_5 = findViewById(R.id.checkbox_nahi_hai_5);
        checkbox_mojood_hai_6 = findViewById(R.id.checkbox_mojood_hai_6);
        checkbox_nahi_hai_6 = findViewById(R.id.checkbox_nahi_hai_6);

        //Button
        btn_jamaa_kre = findViewById(R.id.submit);
        btn_jamaa_kre.setVisibility(View.GONE);


        checkbox_mojood_hai_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_mojood_hai_1.isChecked()) {
                    khoon_kami = "1";
                }
                checkbox_nahi_hai_1.setChecked(false);
            }
        });
        checkbox_nahi_hai_1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_hai_1.isChecked()) {
                    khoon_kami = "0";
                }
                checkbox_mojood_hai_1.setChecked(false);
            }
        });
        checkbox_mojood_hai_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_mojood_hai_2.isChecked()) {
                    sojan = "1";
                }
                checkbox_nahi_hai_2.setChecked(false);
            }
        });
        checkbox_nahi_hai_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_hai_2.isChecked()) {
                    sojan = "0";
                }
                checkbox_mojood_hai_2.setChecked(false);
            }
        });
        checkbox_mehsoos_hoti_hai.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_mehsoos_hoti_hai.isChecked()) {
                    bachay_ki_harqat = "1";
                }
                checkbox_nahi_mehsoos_hoti.setChecked(false);
            }
        });
        checkbox_nahi_mehsoos_hoti.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_mehsoos_hoti.isChecked()) {
                    bachay_ki_harqat = "0";
                }
                checkbox_mehsoos_hoti_hai.setChecked(false);
            }
        });
        checkbox_mojood_hai_4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_mojood_hai_4.isChecked()) {
                    bleeding = "1";
                }
                checkbox_nahi_hai_4.setChecked(false);
            }
        });
        checkbox_nahi_hai_4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_hai_4.isChecked()) {
                    bleeding = "0";
                }
                checkbox_mojood_hai_4.setChecked(false);
            }
        });
        checkbox_mojood_hai_5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_mojood_hai_5.isChecked()) {
                    bad_odour = "1";
                }
                checkbox_nahi_hai_5.setChecked(false);
            }
        });
        checkbox_nahi_hai_5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_hai_5.isChecked()) {
                    bad_odour = "0";
                }
                checkbox_mojood_hai_5.setChecked(false);
            }
        });
        checkbox_mojood_hai_6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_mojood_hai_6.isChecked()) {
                    breast_problem = "1";
                }
                checkbox_nahi_hai_6.setChecked(false);
            }
        });
        checkbox_nahi_hai_6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_nahi_hai_6.isChecked()) {
                    breast_problem = "0";
                }
                checkbox_mojood_hai_6.setChecked(false);
            }
        });

        //RelativeLayout
        rl_add = findViewById(R.id.rl_add);
        rl_sub = findViewById(R.id.rl_sub);
        rl_IronSup = findViewById(R.id.rl_IronSup);

        //TextView
        tv_count = findViewById(R.id.tv_count);


        rl_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter < 100) {
                    counter += 1;
                    tv_count.setText("" + counter);

                    Log.d("000457", ": " + counter);
                } else {
                    tv_count.setText("" + counter);
                    Log.d("000457", ":: " + counter);
                }
            }
        });
        rl_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter > 0) {
                    counter -= 1;
                    tv_count.setText("" + counter);
                    Log.d("000457", ": " + counter);
                } else {
                    tv_count.setText("" + counter);
                    Log.d("000457", ": " + counter);
                }
            }


        });

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


                        et_haaml_ka_dorania.setEnabled(true);
                        et_tareekh_muaina.setEnabled(true);
                        et_takleef_shikayat.setEnabled(true);
                        rl_IronSup.setEnabled(true);
                       // et_folaad_ki_gooliyou_kai_mutaliq_hidayat.setEnabled(true);


                        checkbox_mojood_hai_1.setEnabled(true);
                        checkbox_nahi_hai_1.setEnabled(true);
                        checkbox_mojood_hai_2.setEnabled(true);
                        checkbox_nahi_hai_2.setEnabled(true);
                        checkbox_mehsoos_hoti_hai.setEnabled(true);
                        checkbox_nahi_mehsoos_hoti.setEnabled(true);
                        checkbox_mojood_hai_4.setEnabled(true);
                        checkbox_nahi_hai_4.setEnabled(true);
                        checkbox_mojood_hai_5.setEnabled(true);
                        checkbox_nahi_hai_5.setEnabled(true);
                        checkbox_mojood_hai_6.setEnabled(true);
                        checkbox_nahi_hai_6.setEnabled(true);


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
                jsonObject.put("preg_duration", "" + et_haaml_ka_dorania.getText().toString());
                jsonObject.put("assesment_date", "" + et_tareekh_muaina.getText().toString());
                jsonObject.put("khoon_kami", "" + khoon_kami);//spinner
                jsonObject.put("sojan", "" + sojan);
                jsonObject.put("bachay_ki_harqat", "" + bachay_ki_harqat);
                jsonObject.put("bleeding", "" + bleeding);
                jsonObject.put("bad_odour", "" + bad_odour);
                jsonObject.put("breast_problem", "" + breast_problem);
                jsonObject.put("complains", "" + et_takleef_shikayat.getText().toString());
                jsonObject.put("iron_sup", "" + counter);
              //  jsonObject.put("iron_sup", "" + et_folaad_ki_gooliyou_kai_mutaliq_hidayat.getText().toString());
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

                        String update_record = "UPDATE MANC SET " +
                                "data='" + jsonObject.toString() + "'," +
                                "is_synced='" + 0 + "'" +
                                "WHERE member_uid = '" + mother_uid + "' AND pregnancy_id='" + preg_id + "' AND added_on='" + added_on + "' ";
                        ls.executeNonQuery(update_record);

                        boolean a = ls.executeNonQuery(update_record);
                        Log.d("000555", " Qury" + a);


                        if (Utils.haveNetworkConnection(ctx) > 0) {

                            sendPostRequest(mother_uid, preg_id, et_tareekh_indraj.getText().toString(), "1", String.valueOf(jsonObject), login_useruid, added_on);
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

        // String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/mother/anc";
        String url = UrlClass.update_mother_anc_url;


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

                        String update_record = "UPDATE MANC SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + mother_uid + "'AND pregnancy_id= '" + preg_id + "'AND added_on= '" + added_on + "'";

                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d("000555", "Updated Data: " + update_record);
                        Log.d("000555", "Updated Query: " + res.toString());
                        Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();
                        // Toast.makeText(ctx, "Data updated successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000555", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        // Toast.makeText(ctx, "Data has not been updated to the service.", Toast.LENGTH_SHORT).show();
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
                //  Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
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

            String mData[][] = ls.executeReader("Select data from MANC where member_uid = '" + mother_uid + "' AND pregnancy_id = '" + preg_id + "' AND added_on = '" + added_on + "' AND type = 1");


            Log.d("000555", "DATA: " + mData[0][0]);

            et_tareekh_indraj.setEnabled(false);
            et_haaml_ka_dorania.setEnabled(false);
            et_tareekh_muaina.setEnabled(false);
            et_takleef_shikayat.setEnabled(false);
            //et_folaad_ki_gooliyou_kai_mutaliq_hidayat.setEnabled(false);
            rl_IronSup.setEnabled(false);


            checkbox_mojood_hai_1.setEnabled(false);
            checkbox_nahi_hai_1.setEnabled(false);
            checkbox_mojood_hai_2.setEnabled(false);
            checkbox_nahi_hai_2.setEnabled(false);
            checkbox_mehsoos_hoti_hai.setEnabled(false);
            checkbox_nahi_mehsoos_hoti.setEnabled(false);
            checkbox_mojood_hai_4.setEnabled(false);
            checkbox_nahi_hai_4.setEnabled(false);
            checkbox_mojood_hai_5.setEnabled(false);
            checkbox_nahi_hai_5.setEnabled(false);
            checkbox_mojood_hai_6.setEnabled(false);
            checkbox_nahi_hai_6.setEnabled(false);

            String json = mData[0][0];
            jsonObject = new JSONObject(json);


            et_takleef_shikayat.setText((jsonObject.getString("complains")));
            //et_folaad_ki_gooliyou_kai_mutaliq_hidayat.setText((jsonObject.getString("iron_sup")));
            et_tareekh_indraj.setText(jsonObject.getString("record_enter_date"));
            et_haaml_ka_dorania.setText(jsonObject.getString("preg_duration"));
            et_tareekh_muaina.setText(jsonObject.getString("assesment_date"));
            tv_count.setText((jsonObject.getString("iron_sup")));
          counter = Integer.parseInt(jsonObject.getString("iron_sup"));


            if (jsonObject.getString("khoon_kami").equalsIgnoreCase("1")) {
                checkbox_mojood_hai_1.setChecked(true);
                khoon_kami = jsonObject.getString("khoon_kami");
            } else if (jsonObject.getString("khoon_kami").equalsIgnoreCase("0")) {
                checkbox_nahi_hai_1.setChecked(true);
                khoon_kami = jsonObject.getString("khoon_kami");
            }

            if (jsonObject.getString("sojan").equalsIgnoreCase("1")) {
                checkbox_mojood_hai_2.setChecked(true);
                sojan = jsonObject.getString("sojan");
            } else if (jsonObject.getString("sojan").equalsIgnoreCase("0")) {
                checkbox_nahi_hai_2.setChecked(true);
                sojan = jsonObject.getString("sojan");
            }

            if (jsonObject.getString("bachay_ki_harqat").equalsIgnoreCase("1")) {
                checkbox_mehsoos_hoti_hai.setChecked(true);
                bachay_ki_harqat = jsonObject.getString("bachay_ki_harqat");
            } else if (jsonObject.getString("bachay_ki_harqat").equalsIgnoreCase("0")) {
                checkbox_nahi_mehsoos_hoti.setChecked(true);
                bachay_ki_harqat = jsonObject.getString("bachay_ki_harqat");
            }

            if (jsonObject.getString("bleeding").equalsIgnoreCase("1")) {
                checkbox_mojood_hai_4.setChecked(true);
                bleeding = jsonObject.getString("bleeding");
            } else if (jsonObject.getString("bleeding").equalsIgnoreCase("0")) {
                checkbox_nahi_hai_4.setChecked(true);
                bleeding = jsonObject.getString("bleeding");
            }

            if (jsonObject.getString("bad_odour").equalsIgnoreCase("1")) {
                checkbox_mojood_hai_5.setChecked(true);
                bad_odour = jsonObject.getString("bad_odour");
            } else if (jsonObject.getString("bad_odour").equalsIgnoreCase("0")) {
                checkbox_nahi_hai_5.setChecked(true);
                bad_odour = jsonObject.getString("bad_odour");
            }

            if (jsonObject.getString("breast_problem").equalsIgnoreCase("1")) {
                checkbox_mojood_hai_6.setChecked(true);
                breast_problem = jsonObject.getString("breast_problem");
            } else if (jsonObject.getString("breast_problem").equalsIgnoreCase("0")) {
                checkbox_nahi_hai_6.setChecked(true);
                breast_problem = jsonObject.getString("breast_problem");
            }

            ls.closeDB();




        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
            Log.d("000555", " Error" + e.getMessage());
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}