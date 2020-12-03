package com.akdndhrc.covid_module.VAC_App.covid_dashboard.dashboard_comorbidity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.CustomClass.NothingSelectedSpinnerAdapter;
import com.akdndhrc.covid_module.CustomClass.UrlClass;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class co_morbidity_FormView_Activity extends AppCompatActivity {

    Context ctx = co_morbidity_FormView_Activity.this;

    //  TextView txt_mother_age, txt_mother_name;
  //  EditText et_notes_diabetes,et_notes_blood,et_notes_cholestrol,et_notes_cancer,et_notes_heart;
  //  Spinner sp_diabetes,sp_blood_pressure,sp_cancer,sp_cholestrol,sp_heart;
    EditText et_tareekh_visit;
    //, et_refferal_ki_waja, et_refferal_hospital,et_value;
    CheckBox checkbox_haan_1, checkbox_nahi_1, checkbox_haan_2, checkbox_nahi_2, checkbox_awareness, checkbox_service_provided;
    Button btn_jamaa_kre;
    ImageView iv_navigation_drawer, iv_home;
    Spinner sp_naya_sabiqa, sp_planning_type;
    double latitude;
    double longitude;
    // GPSTracker class
    GPSTracker gps;
    String mother_uid, TodayDate, added_on;
   // Spinner sp_material,sp_fever,sp_cough,sp_breath,sp_rash,sp_taste,sp_smell,sp_dia;


    private int mYear, mMonth, mDay;
    int date_for_condition = 0;
    int month_for_condition = 0;
    public String hold_age_date_condition = "fromage", record_date;
    String monthf2, dayf2, yearf2 = "null";

    JSONObject jsonObject;
    ImageView iv_editform;
    Dialog alertDialog;
    ProgressBar pbProgress;

    String khud_muhaiya = "-1";
    String refer = "-1";
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid, services_and_awareness = "-1";
    LinearLayout ll_services_provided;

    RelativeLayout rl_quantity, rl_add, rl_sub;
    TextView tv_count;
    int counter = 0;
    String med_uid = "";
    String mData_medicineLog[][];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.co_morbidity_form);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, co_morbidity_FormView_Activity.class));

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
            Log.d("000987", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000987", "Shared Err:" + e.getMessage());
        }

        try {
            serviceLocation = new ServiceLocation(ctx);
            serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            serviceLocation.callAsynchronousTask();
        } catch (Exception e) {
            Log.d("000987", "GPS Service Err:  " + e.getMessage());
        }

        //   check_gps();

        //TextView
        //  txt_mother_name = findViewById(R.id.txt_mother_name);
        // txt_mother_age = findViewById(R.id.txt_mother_age);

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_editform = findViewById(R.id.iv_editform);

        iv_editform.setVisibility(View.VISIBLE);
        iv_navigation_drawer.setVisibility(View.GONE);
        //  iv_home.setVisibility(View.GONE);


        //Prgress Bar
        pbProgress = findViewById(R.id.pbProgress);

        //EDitTExt
        et_tareekh_visit = findViewById(R.id.et_tareekh_visit);



        //Linear Layout
        ll_services_provided = findViewById(R.id.ll_services_provided);

        //RelativeLayout
        rl_quantity = findViewById(R.id.rl_quantity);
        rl_add = findViewById(R.id.rl_add);
        rl_sub = findViewById(R.id.rl_sub);

        //TextView
        tv_count = findViewById(R.id.tv_count);
        //   tv_count.setText("" + counter);


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
        });
        checkbox_awareness.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (checkbox_awareness.isChecked()) {
                    services_and_awareness = "0";
                    ll_services_provided.setVisibility(View.GONE);
                    rl_quantity.setVisibility(View.GONE);
                    sp_material.setSelection(0);
                    counter = 0;
                    tv_count.setText("0");
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
        });*/

        //spinner_data();


       /* rl_add.setOnClickListener(new View.OnClickListener() {
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
*/

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

                Intent newIntent = new Intent(ctx, HomePageVacinator_Activity.class);
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
                       /* et_notes_blood.setEnabled(true);
                        et_notes_cancer.setEnabled(true);
                        et_notes_heart.setEnabled(true);
                        et_notes_cholestrol.setEnabled(true);
                        et_notes_diabetes.setEnabled(true);

                        sp_cancer.setEnabled(true);
                        sp_heart.setEnabled(true);
                        sp_blood_pressure.setEnabled(true);
                        sp_diabetes.setEnabled(true);
                        sp_cholestrol.setEnabled(true);*/
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
                update_data(v);

            }
        });


    }



    private void update_data(final View v) {

        if (serviceLocation.showCurrentLocation() == true) {

            latitude = serviceLocation.getLatitude();
            longitude = serviceLocation.getLongitude();

            Log.d("000987", " latitude: " + latitude);
            Log.d("000987", " longitude: " + longitude);
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
                Log.d("000258", "Read MFPLAN Error: " + e.getMessage());
            }
        }

        try {
            alertDialog = new Dialog(ctx);
            LayoutInflater layout = LayoutInflater.from(ctx);
            final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

            alertDialog.setContentView(dialogView);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            final String cur_added_on = String.valueOf(System.currentTimeMillis());

            if (jsonObject.has("lat")) {
                jsonObject.put("lat", "" + String.valueOf(latitude));
                jsonObject.put("lng", "" + String.valueOf(longitude));
              /*  jsonObject.put("plan", "" + String.valueOf(sp_naya_sabiqa.getSelectedItemPosition() - 1));
              //  jsonObject.put("tariqa", "" + String.valueOf(sp_planning_type.getSelectedItemPosition() - 1));//spinner
                jsonObject.put("provided", "" + khud_muhaiya);
                jsonObject.put("refer", "" + refer);//spinner
                jsonObject.put("services_and_awareness", "" + services_and_awareness);//spinner
                jsonObject.put("material", "" + sp_material.getSelectedItem());//spinner
                jsonObject.put("material_pos", "" + String.valueOf(sp_material.getSelectedItemPosition() - 1));//spinner
                jsonObject.put("material_quantity", "" + counter);//spinner
                jsonObject.put("reason_refer", "" + et_refferal_ki_waja.getText().toString());
                jsonObject.put("facility_refer", "" + et_refferal_hospital.getText().toString());
               */ jsonObject.put("added_on", "" + cur_added_on);
                jsonObject.put("tareekh_visit", "" + et_tareekh_visit.getText().toString());
              /*  jsonObject.put("notes_diabetes", "" + et_notes_diabetes.getText().toString());
                jsonObject.put("notes_cancer", "" + et_notes_cancer.getText().toString());
                jsonObject.put("notes_heart", "" + et_notes_heart.getText().toString());
                jsonObject.put("notes_blood", "" + et_notes_blood.getText().toString());
                jsonObject.put("notes_cholestrol", "" + et_notes_cholestrol.getText().toString());
                // jobj.put("plan", "" + String.valueOf(sp_naya_sabiqa.getSelectedItemPosition() - 1));
                jsonObject.put("diabetes", "" + String.valueOf(sp_diabetes.getSelectedItemPosition() - 1));
                jsonObject.put("blood_pressure", "" + String.valueOf(sp_blood_pressure.getSelectedItemPosition() - 1));
                jsonObject.put("cholestrol", "" + String.valueOf(sp_cholestrol.getSelectedItemPosition() - 1));
                jsonObject.put("heart", "" + String.valueOf(sp_heart.getSelectedItemPosition() - 1));
                jsonObject.put("cancer", "" + String.valueOf(sp_cancer.getSelectedItemPosition() - 1));*/



            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();
                        String update_record = "UPDATE MBEMARI SET " +
                                "data='" + jsonObject.toString() + "'," +
                                "is_synced='" + 0 + "'" +
                                "WHERE member_uid = '" + mother_uid + "' AND added_on='" + added_on + "' AND  record_data='"+record_date+"'";
                        ls.executeNonQuery(update_record);


                        final Snackbar snackbar = Snackbar.make(v, "The data has been updated.", Snackbar.LENGTH_SHORT);
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

                            sendPostRequest(mother_uid, et_tareekh_visit.getText().toString(), String.valueOf(jsonObject), login_useruid, added_on);
                        } else {
                            //Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();
                        }

                        if (services_and_awareness.equalsIgnoreCase("1")) {
                            //update_medicineLog();
                        } else {
                        }


                    } catch (Exception e) {
                        alertDialog.dismiss();
                        Log.d("000987", " Error" + e.getMessage());
                        // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    } finally {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog.dismiss();
                                finish();
                            }
                        }, 2500);
                    }
                }
            }, 2000);

        } catch (Exception e) {
            alertDialog.dismiss();
            //  Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
            Log.d("000987", " Error" + e.getMessage());
            Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
        }


    }

    private void sendPostRequest(final String member_uid, final String record_data, final String data, final String added_by, final String added_on) {

        String abc = "pak";
        // String url = " https://"+abc+".api.teekoplus.akdndhrc.org/sync/save/mother/family-plan";
        String url = UrlClass.update_mother_fplanning_url;

        Log.d("000987", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {
                        Log.d("000987", "Response:  " + response);


                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE MBEMARI SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + mother_uid + "'AND added_on= '" + added_on + "'";

                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d("000987", "Updated Data: " + update_record);
                        Log.d("000987", "Updated Query: " + res.toString());

                        Toast tt  =Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();

                        //Toast.makeText(ctx, "Data updated successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000987", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, R.string.noDataSyncServiceEng, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000987", "Err: " + e.getMessage());
                    //Toast.makeText(ctx, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();
                    Toast tt  =Toast.makeText(ctx, "Data not synced", Toast.LENGTH_SHORT);
                    tt.setGravity(Gravity.CENTER, 0, 0);
                    tt.show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000987", "error    " + error.getMessage());
                //Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
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


                Log.d("000987", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000987", "map ");
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
            Log.d("000987", "latitude value: " + latitude);
            Log.d("000987", "longitude value: " + longitude);
        } else {
            gps.showSettingsAlert();
            Toast.makeText(ctx, "Data not turned on please turn on GPS position.", Toast.LENGTH_LONG).show();
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

                Log.d("000987", "IF lat lng: ");
                Log.d("000987", "latitude: " + latitude);
                Log.d("000987", "longitude: " + longitude);


                return;
            } else {
                snackbar.dismiss();
                Log.d("000987", "ELSE lat lng: ");
                Log.d("000987", "latitude: " + latitude);
                Log.d("000987", "longitude: " + longitude);

                Toast.makeText(ctx, "GPS position is now on", Toast.LENGTH_SHORT).show();
            }

        } else {
            gps.showSettingsAlert();
            Toast.makeText(ctx, "Please turn on GPS position", Toast.LENGTH_LONG).show();
            return;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            String mData[][] = ls.executeReader("Select data from MBEMARI where member_uid = '" + mother_uid + "' AND record_data = '" + record_date + "' AND added_on = '" + added_on + "'");

            Log.d("000987", "Data: " + mData[0][0]);

            iv_editform.setVisibility(View.VISIBLE);

            String json = mData[0][0];
            jsonObject = new JSONObject(json);


            et_tareekh_visit.setEnabled(false);
           /* et_refferal_ki_waja.setEnabled(false);
            et_refferal_hospital.setEnabled(false);

            sp_planning_type.setEnabled(false);
            sp_naya_sabiqa.setEnabled(false);
            checkbox_haan_1.setEnabled(false);
            checkbox_nahi_1.setEnabled(false);
            checkbox_haan_2.setEnabled(false);
            checkbox_nahi_2.setEnabled(false);

            checkbox_service_provided.setEnabled(false);
            checkbox_awareness.setEnabled(false);
            sp_material.setEnabled(false);
            ll_services_provided.setEnabled(false);
            rl_quantity.setEnabled(false);
            rl_add.setEnabled(false);
            rl_sub.setEnabled(false);
            tv_count.setEnabled(false);
*/

          /*  sp_diabetes.setEnabled(false);
            sp_blood_pressure.setEnabled(false);
            sp_cancer.setEnabled(false);
            sp_cholestrol.setEnabled(false);
            sp_heart.setEnabled(false);
            et_notes_diabetes.setEnabled(false);
            et_notes_heart.setEnabled(false);
            et_notes_blood.setEnabled(false);
            et_notes_cholestrol.setEnabled(false);
            et_notes_cancer.setEnabled(false);
*/

            Log.d("000987","1");
            et_tareekh_visit.setText((jsonObject.getString("tareekh_visit")));
           /* sp_diabetes.setSelection(Integer.parseInt(jsonObject.getString("diabetes")) + 1);
            sp_blood_pressure.setSelection(Integer.parseInt(jsonObject.getString("blood_pressure")) + 1);
            sp_cancer.setSelection(Integer.parseInt(jsonObject.getString("cancer")) + 1);
            sp_heart.setSelection(Integer.parseInt(jsonObject.getString("heart")) + 1);
            sp_cholestrol.setSelection(Integer.parseInt(jsonObject.getString("cholestrol")) + 1);
            et_notes_diabetes.setText(jsonObject.getString("notes_diabetes"));
            et_notes_blood.setText(jsonObject.getString("notes_blood"));
            et_notes_cancer.setText(jsonObject.getString("notes_cancer"));
            et_notes_heart.setText(jsonObject.getString("notes_heart"));
            et_notes_cholestrol.setText(jsonObject.getString("notes_cholestrol"));*/



            //et_tariqa.setText(jsonObject.getString("tariqa"));
          /*  et_refferal_ki_waja.setText(jsonObject.getString("reason_refer"));
            et_refferal_hospital.setText(jsonObject.getString("facility_refer"));
*/
            Log.d("000987","2");

           /* if (jsonObject.getString("provided").equalsIgnoreCase("1")) {
                Log.d("000987","3");

                checkbox_haan_1.setChecked(true);

                khud_muhaiya = jsonObject.getString("provided");
            } else if (jsonObject.getString("provided").equalsIgnoreCase("0")) {
                Log.d("000987","4");

                checkbox_nahi_1.setChecked(true);
                khud_muhaiya = jsonObject.getString("provided");
            }

            if (jsonObject.getString("refer").equalsIgnoreCase("1")) {
                checkbox_haan_2.setChecked(true);

                refer = jsonObject.getString("refer");
                Log.d("000987","5");


            } else if (jsonObject.getString("refer").equalsIgnoreCase("0")) {
                checkbox_nahi_2.setChecked(true);
                refer = jsonObject.getString("refer");
                Log.d("000987","6");

            }
*/

            try {
                if (jsonObject.has("services_and_awareness")) {
                    Log.d("000987", "7");

                    if (jsonObject.getString("services_and_awareness").equalsIgnoreCase("1")) {
                        Log.d("000987", "8");

                        checkbox_service_provided.setChecked(true);
                        ll_services_provided.setVisibility(View.VISIBLE);
                       // sp_material.setSelection(Integer.parseInt(jsonObject.getString("material_pos")) + 1);
                        tv_count.setText(jsonObject.getString("material_quantity"));
                        counter = Integer.parseInt(jsonObject.getString("material_quantity"));
                        services_and_awareness = jsonObject.getString("services_and_awareness");

                    } else if (jsonObject.getString("services_and_awareness").equalsIgnoreCase("0")) {
                        Log.d("000987", "9");

                        checkbox_awareness.setChecked(true);
                        Log.d("000987", "91");
                        ll_services_provided.setVisibility(View.GONE);
                        Log.d("000987", "92");
                        // sp_material.setSelection(1);

                        tv_count.setText("0");
                        Log.d("000987", "93");
                        counter = (0);
                        Log.d("000987", "94");
                        services_and_awareness = jsonObject.getString("services_and_awareness");
                        Log.d("000987", "95");
                    } else {
                        Log.d("000987", "10");

                        checkbox_service_provided.setChecked(false);
                        checkbox_awareness.setChecked(false);
                        ll_services_provided.setVisibility(View.GONE);
                    }
                    Log.d("000987", "11");

                }
                Log.d("000987", "12");
            }catch (Exception e)
            {
                Log.d("000987","Error ServiceAwarness: " +e.getMessage());
            }



            try {
                 mData_medicineLog= ls.executeReader("Select medicine_id,metadata from MEDICINE_LOG where member_uid = '" + mother_uid + "' AND added_on = '" + added_on + "'");

                Log.d("000852", "Data LEN: " + mData_medicineLog.length);

                med_uid=mData_medicineLog[0][0];
                Log.d("000852", "Medicine UID: " + med_uid);
                Log.d("000852", "MetaData: " + mData_medicineLog[0][1]);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("000852", "Read MedicineLog Error:" + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("000987", " Error: " + e.getMessage());
            Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
        }

      /*  try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            String mDataa[][] = ls.executeReader("Select medicine_id from MEDICINE_LOG where member_uid = '" + mother_uid + "' AND added_on = '" + added_on + "'");

            Log.d("000987", "Data: " + mDataa[0][0]);
            med_uid = mDataa[0][0];

            ls.closeDB();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("000987", " Error" + e.getMessage());
        }*/
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
