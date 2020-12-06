package com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHifazitiTeekeyRecordActivities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.ServiceLocation;
import com.akdndhrc.covid_module.Utils;
import com.akdndhrc.covid_module.VAC_App.HomePageVacinator_Activity;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.akdndhrc.covid_module.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.akdndhrc.covid_module.R.string.selectRecDateEng;
import static com.akdndhrc.covid_module.R.string.vaccineDataNotSynced;


public class Mother_HifazitiTeekeyKahiAurSyLiHoe_Activity extends AppCompatActivity {

    Context ctx = Mother_HifazitiTeekeyKahiAurSyLiHoe_Activity.this;
    TextView txt_age, txt_naam, txt_refuse_vaccine;
    ListView lv;
    RelativeLayout rl_navigation_drawer, rl_home;

    ArrayList<String> arrayList_vaccine_name = new ArrayList<>();
    ArrayList<String> arrayList_vaccine_date = new ArrayList<>();
    ImageView iv_navigation_drawer, iv_home, iv_close, image_gender;

    Adt_MotherHifazatiTeekeyRecordList_KahiAurSy adt;
    String[][] mData;
    int index_val = 0;

    Button btn_jamaa_kre;

    String mother_uid, mother_age, mother_name, mother_gender;
    String to_make_active = String.valueOf(R.string.yes);
    int diffInDays;

    Spinner sp_inside_outside_council;
    double latitude;
    String TodayDate;
    double longitude;
    ServiceLocation serviceLocation;
    String login_useruid;

    EditText et_tareekh_mosool_hoe;
    Spinner sp_vaccine_kaha_farham_hoe;

    Button btn_InFacility, btn_Outreach, btn_MobileVaccine;
    String btn_name, btn_value,village_uid="none";

    String monthf2, dayf2, yearf2 = "null";
    private int mYear, mMonth, mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_hifaziti_teekey_record_list);


        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Mother_HifazitiTeekeyKahiAurSyLiHoe_Activity.class));
        Log.d("000266", "RESTARRRRRRRRRRRRR");

        mother_uid = getIntent().getExtras().getString("u_id");


        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
            String shared_useruid = prefelse.getString((String.valueOf(R.string.loginUserIDEng)), null); // getting String
            login_useruid = shared_useruid;
            Log.d("000266", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000266", "Shared Err:" + e.getMessage());
        }

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

        try {
            serviceLocation = new ServiceLocation(ctx);
            serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            serviceLocation.callAsynchronousTask();
        } catch (Exception e) {
            Log.d("000266", "GPS Service Err:  " + e.getMessage());
        }


        //ListView
        lv = findViewById(R.id.lv);

        //TextView
        txt_naam = findViewById(R.id.txt_mother_name);
        txt_age = findViewById(R.id.txt_mother_age);


        // txt_naam.setText(child_name);
        // txt_age.setText(child_age);

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        //  iv_home.setVisibility(View.GONE);
        image_gender = findViewById(R.id.image_gender);

        calculateAge();



        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("12345", "Errr");

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


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index_val = position;
               // Log.d("000522", " POS: " + arrayListVaccine.get(index_val));

                try {
                    Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();

                    String[][] mDataa = ls.executeReader("Select vaccinated_on from MVACINE where member_uid = '" + mother_uid + "' AND vaccine_id = '" + position + "'");
                    if (mDataa != null) {

                        String[][] mData_ref_vac = ls.executeReader("Select vaccinated_on from MVACINE where member_uid = '" + mother_uid + "' AND vaccine_id = '" + position + "' AND type = '3' ");

                        if (mData_ref_vac != null) {
                            Log.d("000555", "Refuse");
//                            Toast.makeText(getApplicationContext(), R.string.refused_vaccine, Toast.LENGTH_SHORT).show();
                            final Snackbar snackbar = Snackbar.make(view, R.string.vaccineRefused, Snackbar.LENGTH_SHORT);
                            View mySbView = snackbar.getView();
                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(16);
                            textView.setGravity(Gravity.CENTER_VERTICAL);
                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refused_vaccine, 0, 0, 0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                            }
                            snackbar.setDuration(2500);
                            snackbar.show();
                        } else {
                            Log.d("000555", "Not Ref");
                            final Snackbar snackbar = Snackbar.make(view, R.string.vaccineGiven, Snackbar.LENGTH_SHORT);
                            View mySbView = snackbar.getView();
                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            textView.setGravity(Gravity.CENTER_VERTICAL);
                            textView.setTextSize(16);
                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                            }
                            snackbar.setDuration(2500);
                            snackbar.show();
                        }

                    } else {
                        Dialog_VaccineKoAnjaamDy(position);
                    }

                } catch (Exception e) {
                    Log.d("000555", "Err:" + e.getMessage());
                    // Toast.makeText(getApplicationContext(), "Mother already vaccinated", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void Dialog_VaccineKoAnjaamDy(final int pos) {
        final Dialog dialog = new Dialog(ctx);
        LayoutInflater layout = LayoutInflater.from(ctx);
        final View dialogView = layout.inflate(R.layout.dialog_phele_sy_li_hoe_vaccines_layout, null);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);


        iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        btn_jamaa_kre = (Button) dialog.findViewById(R.id.submit);
        et_tareekh_mosool_hoe = (EditText) dialog.findViewById(R.id.et_tareekh_mosool_hoe);
        sp_vaccine_kaha_farham_hoe = (Spinner) dialog.findViewById(R.id.sp_vaccine_kaha_farham_hoe);

        btn_InFacility = dialog.findViewById(R.id.btn_InFacility);
        btn_Outreach = dialog.findViewById(R.id.btn_Outreach);
        btn_MobileVaccine = dialog.findViewById(R.id.btn_MobileVaccine);
        
        
        et_tareekh_mosool_hoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTareekhDialog();
            }
        });


        spineer_data();
        dialog.show();

        btn_InFacility.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_left_border_only));
        btn_Outreach.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_border_only));
        btn_MobileVaccine.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_right_border_only));

        btn_InFacility.setTextColor(ContextCompat.getColor(ctx, R.color.color_white));
        btn_Outreach.setTextColor(ContextCompat.getColor(ctx, R.color.dark_blue_color));
        btn_MobileVaccine.setTextColor(ContextCompat.getColor(ctx, R.color.dark_blue_color));

        btn_name = "In Facility";
        btn_value = "1";

        btn_InFacility.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                btn_name = "In Facility";
                btn_value = "1";

                btn_InFacility.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_left_border_only));
                btn_Outreach.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_border_only));
                btn_MobileVaccine.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_right_border_only));

                btn_InFacility.setTextColor(ContextCompat.getColor(ctx, R.color.color_white));
                btn_Outreach.setTextColor(ContextCompat.getColor(ctx, R.color.dark_blue_color));
                btn_MobileVaccine.setTextColor(ContextCompat.getColor(ctx, R.color.dark_blue_color));

            }
        });

        btn_Outreach.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                btn_name = String.valueOf(R.string.outreach);
                btn_value = "2";


                btn_InFacility.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_left_border_only2));
                btn_Outreach.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_border_only2));
                btn_MobileVaccine.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_right_border_only));

                btn_InFacility.setTextColor(ContextCompat.getColor(ctx, R.color.dark_blue_color));
                btn_Outreach.setTextColor(ContextCompat.getColor(ctx, R.color.color_white));
                btn_MobileVaccine.setTextColor(ContextCompat.getColor(ctx, R.color.dark_blue_color));


            }
        });

        btn_MobileVaccine.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                btn_name = getString(R.string.mobileVaccination);
                btn_value = "3";


                btn_InFacility.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_left_border_only2));
                btn_Outreach.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_border_only));
                btn_MobileVaccine.setBackground(ContextCompat.getDrawable(ctx, R.drawable.button_with_right_border_only2));

                btn_InFacility.setTextColor(ContextCompat.getColor(ctx, R.color.dark_blue_color));
                btn_Outreach.setTextColor(ContextCompat.getColor(ctx, R.color.dark_blue_color));
                btn_MobileVaccine.setTextColor(ContextCompat.getColor(ctx, R.color.color_white));

            }
        });


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_tareekh_mosool_hoe.getText().toString().length() < 1) {
                    Toast.makeText(getApplicationContext(), selectRecDateEng, Toast.LENGTH_LONG).show();
                    return;
                }

                if (sp_vaccine_kaha_farham_hoe.getSelectedItemPosition()==0)
                {
                    Toast.makeText(getApplicationContext(), R.string.vaccProvideLocation, Toast.LENGTH_LONG).show();
                    return;
                }


                if (serviceLocation.showCurrentLocation() == true) {
                    latitude = serviceLocation.getLatitude();
                    longitude = serviceLocation.getLongitude();
                    Log.d("000266", " latitude: " + latitude);
                    Log.d("000266", " longitude: " + longitude);
                } else {
                    try {
                        serviceLocation.doAsynchronousTask.cancel();
                    } catch (Exception e) {
                    }
                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String[][] mData = ls.executeReader("SELECT max(added_on),metadata,count(*) from CVACCINATION");

                        if (Integer.parseInt(mData[0][2]) > 0) {
                            JSONObject jsonObject = new JSONObject(mData[0][1]);
                            Log.d("000266", "  Last Latitude: " + jsonObject.getString("lat"));
                            Log.d("000266", "  Last Longitude: " + jsonObject.getString("lng"));

                            latitude = Double.parseDouble(jsonObject.getString("lat"));
                            longitude = Double.parseDouble(jsonObject.getString("lng"));

                            Toast.makeText(ctx, R.string.dataGPS, Toast.LENGTH_SHORT).show();
                        } else {
                            latitude = Double.parseDouble("0.0");
                            longitude = Double.parseDouble("0.0");
                            Toast.makeText(ctx, R.string.notDataGPS, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.d("000266", "Read CVACCINE Error: " + e.getMessage());
                    }
                }

                try {


                    Lister ls = new Lister(Mother_HifazitiTeekeyKahiAurSyLiHoe_Activity.this);
                    ls.createAndOpenDB();


                    SimpleDateFormat dates = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss aa");
                    Calendar c = Calendar.getInstance();
                    String current_timeStamp = dates.format(c.getTime());
                    Log.d("000266", "timestamp:" + current_timeStamp);

                    JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("type_name", "" + "phele_sy_li_hoe_vaccine");
                    jobj.put("vaccine_place", "" + btn_name);
                    jobj.put("vaccine_place_pos", "" + btn_value);
                    jobj.put("vaccine_kaha_farham_ki_gae", "" + sp_vaccine_kaha_farham_hoe.getSelectedItem().toString());
                    jobj.put("village_uid", "" + village_uid);
                    jobj.put("datetime", "" + current_timeStamp);

                    String added_on = String.valueOf(System.currentTimeMillis());

                    // jobjMain.put("data", jobj);
                    String ans1 = "insert into MVACINE (member_uid,vaccine_id, record_data, type,vaccinated_on,image_location,data,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + mother_uid + "'," +
                            "'" + pos + "'," +
                            "'" + TodayDate + "'," +
                            "'" + "1" + "'," +
                            "'" + et_tareekh_mosool_hoe.getText().toString() + "'," +
                            "'" + TodayDate + "'," +
                            "'" + jobj + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + added_on + "'" +
                            ")";


                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000266", "Data: " + ans1);
                    Log.d("000266", "Query: " + res);

                    //imageocation o be added

                    //Toast.makeText(getApplicationContext(), "Data saved.", Toast.LENGTH_SHORT).show();

                    if (res.toString().equalsIgnoreCase("true"))
                    {
                        final Snackbar snackbar = Snackbar.make(v, R.string.vaccDataCollectedEng, Snackbar.LENGTH_SHORT);
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

                            sendPostRequest(mother_uid, String.valueOf(pos), et_tareekh_mosool_hoe.getText().toString(), String.valueOf(jobj), login_useruid, added_on);
                        } else {
                            // Toast.makeText(ctx, R.string.dataCollected, Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        final Snackbar snackbar = Snackbar.make(v, R.string.vaccDataNotCollected, Snackbar.LENGTH_SHORT);
                        View mySbView = snackbar.getView();
                        mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                        TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(16);
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                        }
                        snackbar.setDuration(2000);
                        snackbar.show();
                    }


                } catch (Exception e) {
                    Log.d("000266", " Error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Intent intent = new Intent(ctx, Mother_HifazitiTeekeyKahiAurSyLiHoe_Activity.class);
                            intent.putExtra("u_id", mother_uid);
                            startActivity(intent);
                        }
                    },2500);

                }

            }
        });



    }

    private void sendPostRequest(final String member_uid, final String vacine_uid, final String record_data,
                                 final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/mother/vaccinations/new";

        Log.d("000266", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {
                    //   Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000266", "response1    " + response);
                        //Toast.makeText(ctx, "Data has been saved", Toast.LENGTH_SHORT).show();

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE CVACCINATION SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + mother_uid + "'AND added_on= '" + added_on + "'AND vaccine_id= '" + vacine_uid + "'";
                        ls.executeNonQuery(update_record);

                        Toast tt  =Toast.makeText(ctx, R.string.vaccDataSyncedEng, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();
                    } else {
                        Log.d("000266", "else ");
                        Toast tt  =Toast.makeText(ctx, R.string.noDataSyncEng, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();
                        //Toast.makeText(ctx, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000266", " Error: " + e.getMessage());
                    //Toast.makeText(ctx, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();
                    Toast.makeText(ctx, vaccineDataNotSynced, Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("000266", "error    " + error.getMessage());
                //    Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast tt  =Toast.makeText(ctx, R.string.noDataSyncEng, Toast.LENGTH_SHORT);
                tt.setGravity(Gravity.CENTER, 0, 0);
                tt.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("member_id", member_uid);
                params.put("vaccine_id", vacine_uid);
                params.put("type", "2");
                params.put("record_data", record_data);
                params.put("vaccinated_on", et_tareekh_mosool_hoe.getText().toString());
                params.put("image_location", added_by);
                params.put("metadata", data);
                params.put("added_by", added_by);
                params.put("added_on", added_on);


                Log.d("000266", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000266", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }



    public void ShowTareekhDialog() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(Mother_HifazitiTeekeyKahiAurSyLiHoe_Activity.this, R.style.DatePickerDialog,
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
                        et_tareekh_mosool_hoe.setText(yearf2 + "-" + monthf2 + "-" + dayf2);



                        Calendar dob = Calendar.getInstance();
                        Calendar today = Calendar.getInstance();

                        dob.set(year, monthOfYear, dayOfMonth);

                        int age = today.get(Calendar.YEAR) - year;

                        Integer ageInt = new Integer(age);
                        String ageS = ageInt.toString();


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();


    }


    private void spineer_data() {

        Utils.setSpinnervillage(getApplicationContext(), sp_vaccine_kaha_farham_hoe);
        sp_vaccine_kaha_farham_hoe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (sp_vaccine_kaha_farham_hoe.getSelectedItemPosition() > 0) {
                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String[][] mData = ls.executeReader("select uid from VILLAGES where name = '"+sp_vaccine_kaha_farham_hoe.getSelectedItem()+"' order by name ");
                        village_uid = mData[0][0];

                    } catch (Exception e) {
                        Log.d("0000999", "CATCH:" + e.getMessage());
                    }
                }
                else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

    }


    private void calculateAge() {

        Lister ls = new Lister(ctx);
        ls.createAndOpenDB();

        String mData[][] = ls.executeReader("Select full_name,dob from MEMBER where uid = '" + mother_uid + "'");

        mother_name = mData[0][0];
        txt_naam.setText(mData[0][0]);

        Log.d("000266", "DOB: " + mData[0][1]);

        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = parse.parse(mData[0][1]);
            Date n = new Date();
            long dob = d.getTime();
            long now = n.getTime();
            long diff = (now - dob) / 1000;
            long days = diff / 86400;
            int dob_days = 0;
            int dob_weeks = 0;
            int dob_months = 0;
            int dob_years = 0;
            while (days > 7) {
                if ((days - 365) > 0) {
                    days -= 365;
                    dob_years++;
                    continue;
                }
                if ((days - 30) > 0) {
                    days -= 30;
                    dob_months++;

                    continue;
                }
                if ((days - 7) > 0) {
                    days -= 7;
                    dob_weeks++;
                }
            }


            dob_days = Integer.valueOf((int) days);

            Log.d("000266", "Saal: " + String.valueOf(dob_years));
            Log.d("000266", "Maah: " + String.valueOf(dob_months));

            mother_age = String.valueOf(dob_years) + " years " + String.valueOf(dob_months) + " months ";
            txt_age.setText(mother_age);
//            Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            Log.d("000266", "DOB Error: " + e.getMessage());
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        arrayList_vaccine_name.clear();
        arrayList_vaccine_date.clear();

        try {

            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            arrayList_vaccine_name.add("Vaccine A - Shot 1");
            arrayList_vaccine_name.add("Vaccine A - Shot 2");
            arrayList_vaccine_name.add("Vaccine B - Shot 1");
            arrayList_vaccine_name.add("Vaccine B - Shot 2");

            for (int i = 0; i < arrayList_vaccine_name.size(); i++) {
                index_val = i;
                Log.d("000555", "Size: " + arrayList_vaccine_name.size());
                Log.d("000555", "Loop: " + index_val);

                String[][] mDatavac = ls.executeReader("Select vaccinated_on from MVACINE where member_uid = '" + mother_uid + "' AND vaccine_id = '" + i + "'");
                if (mDatavac != null) {
                    String[][] mData_refuse = ls.executeReader("Select vaccinated_on from MVACINE where member_uid = '" + mother_uid + "' AND vaccine_id = '" + i + "' AND type ='3' ");
                    if (mData_refuse != null) {
                        arrayList_vaccine_date.add(i + "@" + mData_refuse[0][0] + "@" + "0");
                        Log.d("000555", "Vaccine Refuse: ");
                    } else {
                        arrayList_vaccine_date.add(i + "@" + mDatavac[0][0] + "@" + "1");
                        Log.d("000555", "Vaccine Not Refuse: ");
                    }
                    Log.d("000555", "Vacine active : ");
                } else {
                    arrayList_vaccine_date.add(i + "@" + "vaccine due" + "@" + "2");
                    Log.d("000555", "Vacine not active: ");
                }

            }
            adt = new Adt_MotherHifazatiTeekeyRecordList_KahiAurSy(ctx, arrayList_vaccine_name, arrayList_vaccine_date);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);

        } catch (Exception e) {
            Log.d("000555", "Error: " + e.getMessage());
            Toast.makeText(ctx, R.string.noRecordEng, Toast.LENGTH_SHORT).show();
        }


    }

    public static class Adt_MotherHifazatiTeekeyRecordList_KahiAurSy extends BaseAdapter {
        private Context ctx;
        //    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
        ArrayList<String> arrayList_vaccine_name;
        ArrayList<String> arrayList_vaccine_date;

        private LayoutInflater inflater = null;

        // Constructor
        public Adt_MotherHifazatiTeekeyRecordList_KahiAurSy(Context ctx, ArrayList<String> arrayList_vaccine_name, ArrayList<String> arrayList_vaccine_date) {
            this.ctx = ctx;
            this.arrayList_vaccine_name = arrayList_vaccine_name;
            this.arrayList_vaccine_date = arrayList_vaccine_date;
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        public int getCount() {
            return arrayList_vaccine_date.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int pos, View row, ViewGroup viewGroup) {

            final ViewHolder holder;
            if (row == null) {
                holder = new ViewHolder();

                row = inflater.inflate(R.layout.custom_lv_child_hifazati_record_list_kahi_aur_sy_layout, null);
                holder.chtr_lv_tvTareekh = row.findViewById(R.id.chtr_lv_tvTareekh);

                holder.chtr_lv_tvVaccineName = row.findViewById(R.id.chtr_lv_tvVaccineName);
                holder.chtr_lv_rl_background = row.findViewById(R.id.chtr_lv_rl_background);
                holder.chtr_lv_rl_checkbox = row.findViewById(R.id.checkbox);

                row.setTag(holder);

            } else {
                holder = (ViewHolder) row.getTag();
            }


            String[] vacine_name = arrayList_vaccine_date.get(pos).split("@");
            Log.d("000987","pos: " + vacine_name[0]);
            Log.d("000987","vaccinated_on: " + vacine_name[1]);
            Log.d("000987","type: " + vacine_name[2]);

            if (pos % 2 == 0) {

                if (vacine_name[2].equalsIgnoreCase("0")) {

                    holder.chtr_lv_tvVaccineName.setText(arrayList_vaccine_name.get(pos));
                    holder.chtr_lv_tvTareekh.setText(vacine_name[1]);
                    holder.chtr_lv_rl_checkbox.setChecked(true);
                    holder.chtr_lv_rl_checkbox.setClickable(false);
                    holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.purple_color));

                    holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.purple_color));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.chtr_lv_rl_checkbox.setButtonTintList(ctx.getResources().getColorStateList(R.color.purple_color));
                    }

                } else if (vacine_name[2].equalsIgnoreCase("1"))

                {
                    holder.chtr_lv_tvVaccineName.setText(arrayList_vaccine_name.get(pos));
                    holder.chtr_lv_tvTareekh.setText(vacine_name[1]);
                    holder.chtr_lv_rl_checkbox.setChecked(true);
                    holder.chtr_lv_rl_checkbox.setClickable(false);
                    holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.green_color));

                    holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.green_color));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.chtr_lv_rl_checkbox.setButtonTintList(ctx.getResources().getColorStateList(R.color.green_color));
                    }


                } else if (vacine_name[2].equalsIgnoreCase("2")) {
                    holder.chtr_lv_tvVaccineName.setText(arrayList_vaccine_name.get(pos));
                    holder.chtr_lv_tvTareekh.setText(vacine_name[1]);

                    holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.hp_listview_textview_redcolor));

                    holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.hp_listview_textview_redcolor));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.chtr_lv_rl_checkbox.setButtonTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                    }


                }

                else {
                    holder.chtr_lv_tvVaccineName.setText(arrayList_vaccine_name.get(pos));
                    holder.chtr_lv_tvTareekh.setText(vacine_name[1]);
                    holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.grey_color));

                    holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.grey_color));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.chtr_lv_rl_checkbox.setButtonTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                    }
                }

                //  holder.chtr_lv_tvVaccineName.setText(arrayListVaccine.get(pos));
                //  holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));

            } else {


                if (vacine_name[2].equalsIgnoreCase("0")) {

                    holder.chtr_lv_tvVaccineName.setText(arrayList_vaccine_name.get(pos));
                    holder.chtr_lv_tvTareekh.setText(vacine_name[1]);
                    holder.chtr_lv_rl_checkbox.setChecked(true);
                    holder.chtr_lv_rl_checkbox.setClickable(false);
                    holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.purple_color));

                    holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.purple_color));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.chtr_lv_rl_checkbox.setButtonTintList(ctx.getResources().getColorStateList(R.color.purple_color));
                    }

                } else if (vacine_name[2].equalsIgnoreCase("1"))

                {
                    holder.chtr_lv_tvVaccineName.setText(arrayList_vaccine_name.get(pos));
                    holder.chtr_lv_tvTareekh.setText(vacine_name[1]);
                    holder.chtr_lv_rl_checkbox.setChecked(true);
                    holder.chtr_lv_rl_checkbox.setClickable(false);
                    holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.green_color));

                    holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.green_color));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.chtr_lv_rl_checkbox.setButtonTintList(ctx.getResources().getColorStateList(R.color.green_color));
                    }


                } else if (vacine_name[2].equalsIgnoreCase("2")) {
                    holder.chtr_lv_tvVaccineName.setText(arrayList_vaccine_name.get(pos));
                    holder.chtr_lv_tvTareekh.setText(vacine_name[1]);

                    holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.hp_listview_textview_redcolor));

                    holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.hp_listview_textview_redcolor));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.chtr_lv_rl_checkbox.setButtonTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                    }
                }


                else {
                    holder.chtr_lv_tvVaccineName.setText(arrayList_vaccine_name.get(pos));
                    holder.chtr_lv_tvTareekh.setText(vacine_name[1]);
                    holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.grey_color));

                    holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.grey_color));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.chtr_lv_rl_checkbox.setButtonTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                    }

                }


                holder.chtr_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.color_white));

            }

            return row;
        }


        static class ViewHolder {
            TextView chtr_lv_tvTareekh, chtr_lv_tvDate, chtr_lv_tvVaccineName;
            RelativeLayout chtr_lv_rl_background;
            AppCompatCheckBox chtr_lv_rl_checkbox;

            int[][] states = new int[][]{
                    new int[]{-android.R.attr.state_empty}, // red
                    new int[]{-android.R.attr.state_active}, // orange
                    new int[]{-android.R.attr.state_checked}, //green
                    new int[]{}  // grey
            };

            int[] colors = new int[]{
                    0xCC4747,
                    0xffff8800,
                    0x469519,
                    0x929292
            };

            ColorStateList colorStateList = new ColorStateList(states, colors);

        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //  startActivity(new Intent(ctx, Child_Dashboard_Activity.class));
    }
}
