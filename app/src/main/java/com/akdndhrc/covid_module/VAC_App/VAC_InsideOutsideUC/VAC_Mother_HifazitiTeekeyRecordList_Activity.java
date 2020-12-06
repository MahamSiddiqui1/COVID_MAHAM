package com.akdndhrc.covid_module.VAC_App.VAC_InsideOutsideUC;

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
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.Adt_MotherDashboard.Adt_MotherVaccineList;
import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.NothingSelectedSpinnerAdapter;
import com.akdndhrc.covid_module.ServiceLocation;
import com.akdndhrc.covid_module.Utils;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHifazitiTeekeyRecordActivities.Mother_HifazitiTeekeyPheleSyLiHoeVaccineForm_Activity;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.VAC_App.HomePageVacinator_Activity;
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

import static com.akdndhrc.covid_module.R.string.notDataGPS;
import static com.akdndhrc.covid_module.VAC_App.covid_register_activity.register_individual_covid.var_regtemp_abovetwo;

public class VAC_Mother_HifazitiTeekeyRecordList_Activity extends AppCompatActivity {

    Context ctx = VAC_Mother_HifazitiTeekeyRecordList_Activity.this;

    TextView txt_mother_age, txt_mother_name;
    ListView lv;
    Button btn_naya_form_shamil_kre;

    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    Adt_MotherVaccineList adt;

    ImageView iv_navigation_drawer, iv_home, image_edit;

    String mother_uid, mother_name, mother_age, mother_gender;


    TextView txt_refuse_vaccine;
    Spinner sp_inside_outside_council;
    ImageView iv_close;
    Button btn_vaccine_ko_anjaam_dy, btn_phle_sy_li_hoe_vaccine, btn_phle_sy_li_hoe_vaccine_uc, btn_refuse_vaccine, btn_jamaa_kre;

    ArrayList<String> arrayList_vaccine_name = new ArrayList<String>();
    ArrayList<String> arrayList_vaccine_date = new ArrayList<String>();

    double latitude;
    String TodayDate;
    double longitude;
    ServiceLocation serviceLocation;
    String login_useruid;
    int index_val = 0;
    RelativeLayout rl_circle_profile;

    Button btn_InFacility, btn_Outreach, btn_MobileVaccine;
    String btn_name, btn_value;
    LinearLayout ll_btns;
    Button btn_kahi_aur_sy_karae_ho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vac_mother_hifaziti_teekey_record_list);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, VAC_Mother_HifazitiTeekeyRecordList_Activity.class));

        mother_uid = getIntent().getExtras().getString("u_id");


        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
            String shared_useruid = prefelse.getString((String.valueOf(R.string.loginUserIDEng)), null); // getting String
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


        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());


        //ListView
        lv = findViewById(R.id.lv);

        //TextView
        txt_mother_name = findViewById(R.id.txt_mother_name);
        txt_mother_age = findViewById(R.id.txt_mother_age);


        //Button
        btn_kahi_aur_sy_karae_ho = findViewById(R.id.btn_kahi_aur_sy_karae_ho);
        btn_kahi_aur_sy_karae_ho.setVisibility(View.VISIBLE);
        btn_kahi_aur_sy_karae_ho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ctx, VAC_Mother_HifazitiTeekeyKahiAurSyLiHoe_Activity.class);
                intent2.putExtra("u_id", mother_uid);
                startActivity(intent2);
            }
        });


        //RelativeLayout
        rl_circle_profile = findViewById(R.id.rl_circle_profile);


        calculateAge();
        /*txt_mother_name.setText(mother_name);
        txt_mother_age.setText(mother_age);*/


        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        image_edit = findViewById(R.id.image_edit);
        iv_navigation_drawer.setVisibility(View.GONE);
        //iv_home.setVisibility(View.GONE);

        //Button
        btn_naya_form_shamil_kre = findViewById(R.id.btn_naya_form_shamil_kre);


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

        rl_circle_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog alertDialog = new Dialog(ctx);
                LayoutInflater layout = LayoutInflater.from(ctx);
                final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

                alertDialog.setContentView(dialogView);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        alertDialog.dismiss();
                        Intent intent = new Intent(ctx, VAC_AboveTwoProfile_Activity.class);
                        intent.putExtra("u_id", mother_uid);
                        startActivity(intent);
                    }
                }, 1000);


            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index_val = position;
                //Log.d("000555", " POS: " + arrayListDate.get(index_val));
                try {
                    Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();

                    String[][] mDataa = ls.executeReader("Select vaccinated_on from MVACINE where member_uid = '" + mother_uid + "' AND vaccine_id = '" + position + "'");
                    if (mDataa != null) {

                        String[][] mData_ref_vac = ls.executeReader("Select vaccinated_on from MVACINE where member_uid = '" + mother_uid + "' AND vaccine_id = '" + position + "' AND type = '3' ");

                        if (mData_ref_vac != null) {
                            Log.d("000555", "Refuse");
//                            Toast.makeText(getApplicationContext(), R.string.refused_vaccine, Toast.LENGTH_SHORT).show();
                            final Snackbar snackbar = Snackbar.make(view, R.string.vaccineDenied, Snackbar.LENGTH_SHORT);
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
                            final Snackbar snackbar = Snackbar.make(view, R.string.thisVaccineApplied, Snackbar.LENGTH_SHORT);
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


     /*   btn_naya_form_shamil_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent3 = new Intent(ctx, Mother_HifazitiTeekeyRecordForm_Activity.class);
                intent3.putExtra("u_id", mother_uid);
                startActivity(intent3);
            }
        });*/


    }


    public void Dialog_VaccineKoAnjaamDy(final int pos) {
        final Dialog dialog = new Dialog(ctx);
        LayoutInflater layout = LayoutInflater.from(ctx);
        final View dialogView = layout.inflate(R.layout.dialog_vaccine_ko_anjaam_dy_layout_lhw, null);
        dialog.setContentView(dialogView);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);


        iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        //Button
        btn_vaccine_ko_anjaam_dy = (Button) dialog.findViewById(R.id.btn_vaccine_ko_anjaam_dy);
        btn_phle_sy_li_hoe_vaccine = (Button) dialog.findViewById(R.id.btn_phle_sy_li_hoe_vaccine);
        btn_phle_sy_li_hoe_vaccine_uc = (Button) dialog.findViewById(R.id.btn_phle_sy_li_hoe_vaccine_uc);
        btn_refuse_vaccine = (Button) dialog.findViewById(R.id.btn_refuse_vaccine);
        btn_jamaa_kre = (Button) dialog.findViewById(R.id.submit);
       /* btn_opened_vial = (Button) dialog.findViewById(R.id.btn_opened_vial);
        btn_new_vial = (Button) dialog.findViewById(R.id.btn_new_vial);*/


        //Spinner
        sp_inside_outside_council = (Spinner) dialog.findViewById(R.id.sp_inside_outside_council);

        //TextView
        txt_refuse_vaccine = (TextView) dialog.findViewById(R.id.txt_refuse_vaccine);

        //Button
        ll_btns = dialog.findViewById(R.id.ll_btns);
        btn_InFacility = dialog.findViewById(R.id.btn_InFacility);
        btn_Outreach = dialog.findViewById(R.id.btn_Outreach);
        btn_MobileVaccine = dialog.findViewById(R.id.btn_MobileVaccine);


        //spineer_data();
        dialog.show();


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


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


        btn_vaccine_ko_anjaam_dy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 24) {
                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.featureNotAvailable, Snackbar.LENGTH_SHORT);
                    snackbar.setDuration(4000);
                    snackbar.show();
                    return;
                }

               /* if (sp_inside_outside_council.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.pleaseSelectOne, Toast.LENGTH_LONG).show();
                    return;
                } else {*/
                Intent intent = new Intent(ctx, VAC_Mother_HifazitiTeekeyVaccineKoAnjamDy_Activity.class);
                intent.putExtra("u_id", mother_uid);
                intent.putExtra("vac_id", String.valueOf(index_val));
                intent.putExtra("vac_name", arrayList_vaccine_name.get(pos));
                intent.putExtra("vac_place", btn_name);
                intent.putExtra("vac_place_pos", btn_value);
                intent.putExtra("mother_name", mother_name);
                intent.putExtra("mother_age", mother_age);

                startActivity(intent);
                dialog.dismiss();
                //}


            }
        });

        btn_phle_sy_li_hoe_vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sp_inside_outside_council.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.pleaseSelectOne, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Intent intent = new Intent(ctx, Mother_HifazitiTeekeyPheleSyLiHoeVaccineForm_Activity.class);
                    intent.putExtra("u_id", mother_uid);
                    intent.putExtra("vac_id", String.valueOf(index_val));
                    intent.putExtra("vac_name", arrayList_vaccine_name.get(pos));
                    intent.putExtra("vac_place", sp_inside_outside_council.getSelectedItem().toString());
                    intent.putExtra("vac_place_pos", String.valueOf(sp_inside_outside_council.getSelectedItemPosition() - 1));
                    intent.putExtra("mother_name", mother_name);
                    intent.putExtra("mother_age", mother_age);
                    startActivity(intent);
                    dialog.dismiss();
                }

            }
        });

        btn_phle_sy_li_hoe_vaccine_uc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (serviceLocation.showCurrentLocation() == true) {
                    latitude = serviceLocation.getLatitude();
                    longitude = serviceLocation.getLongitude();
                    Log.d("000555", " latitude: " + latitude);
                    Log.d("000555", " longitude: " + longitude);
                } else {
                    try {
                        serviceLocation.doAsynchronousTask.cancel();
                    } catch (Exception e) {
                    }
                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from MVACINE");

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
                            Toast.makeText(ctx, notDataGPS, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.d("000258", "Read MVACINE Error: " + e.getMessage());
                    }
                }

                try {


                    Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();


                    SimpleDateFormat dates = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss aa");
                    Calendar c = Calendar.getInstance();
                    String current_timeStamp = dates.format(c.getTime());
                    Log.d("000555", "timestamp:" + current_timeStamp);


                    final JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("vacine_place", "" + "inside UC");
                    jobj.put("vacine_name", "" + arrayList_vaccine_name.get(pos));
                    jobj.put("datetime", "" + current_timeStamp);


                    final String added_on = String.valueOf(System.currentTimeMillis());

                    String ans1 = "insert into MVACINE (member_uid,vaccine_id, record_data, type,vaccinated_on,image_location,data,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + mother_uid + "'," +
                            "'" + pos + "'," +
                            "'" + TodayDate + "'," +
                            "'" + "2" + "'," +
                            "'" + TodayDate + "'," +
                            "'" + TodayDate + "'," +
                            "'" + jobj + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000555", "Data: " + ans1);
                    Log.d("000555", "Query: " + res);

                    final Snackbar snackbar = Snackbar.make(v, R.string.vaccDataSubmitted, Snackbar.LENGTH_SHORT);
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

                        sendPostRequest(mother_uid, String.valueOf(pos), TodayDate, String.valueOf(jobj), login_useruid, added_on);
                    } else {
                        // Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", " Error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            dialog.dismiss();
                            Intent intent = new Intent(ctx, VAC_Mother_HifazitiTeekeyRecordList_Activity.class);
                            intent.putExtra("u_id", mother_uid);
                            intent.putExtra("mother_name", mother_name);
                            intent.putExtra("mother_gender", mother_age);
                            startActivity(intent);
                        }
                    }, 2000);
                }

            }
        });

        btn_refuse_vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ll_btns.setVisibility(View.GONE);
                btn_vaccine_ko_anjaam_dy.setVisibility(View.GONE);
                btn_phle_sy_li_hoe_vaccine.setVisibility(View.GONE);
                btn_phle_sy_li_hoe_vaccine_uc.setVisibility(View.GONE);
                btn_refuse_vaccine.setVisibility(View.GONE);
                sp_inside_outside_council.setVisibility(View.VISIBLE);
                txt_refuse_vaccine.setVisibility(View.VISIBLE);
                btn_jamaa_kre.setVisibility(View.VISIBLE);

                spineer_refuse_vacine();


            }
        });


        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sp_inside_outside_council.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.selectVaccRefusalPrompt, Toast.LENGTH_LONG).show();
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
                    } catch (Exception e) {
                    }
                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from MVACINE");

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
                            Toast.makeText(ctx, notDataGPS, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.d("000258", "Read MVACINE Error: " + e.getMessage());
                    }
                }

                try {

                    Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();

                    SimpleDateFormat dates = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss aa");
                    Calendar c = Calendar.getInstance();
                    String current_timeStamp = dates.format(c.getTime());
                    Log.d("000555", "timestamp:" + current_timeStamp);

                    final JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("vacine_place", "" + "refuse_vaccine");
                    jobj.put("vacine_name", "" + arrayList_vaccine_name.get(pos));
                    jobj.put("refuse_reason", "" + String.valueOf(sp_inside_outside_council.getSelectedItem().toString()));
                    jobj.put("refuse_reason_pos", "" + String.valueOf(sp_inside_outside_council.getSelectedItemPosition() - 1));
                    jobj.put("datetime", "" + current_timeStamp);

                    final String added_on = String.valueOf(System.currentTimeMillis());

                    String ans1 = "insert into MVACINE (member_uid,vaccine_id, record_data, type,vaccinated_on,image_location,data,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + mother_uid + "'," +
                            "'" + pos + "'," +
                            "'" + TodayDate + "'," +
                            "'" + "3" + "'," +
                            "'" + TodayDate + "'," +
                            "'" + TodayDate + "'," +
                            "'" + jobj + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000555", "Data: " + ans1);
                    Log.d("000555", "Query: " + res);

                    final Snackbar snackbar = Snackbar.make(v, R.string.vaccDataSubmitted, Snackbar.LENGTH_SHORT);
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

                        sendPostRequest_RefuseVaccine(mother_uid, String.valueOf(pos), TodayDate, String.valueOf(jobj), login_useruid, added_on);
                    } else {
                        // Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    Log.d("000555", " Error: " + e.getMessage());
                    //  Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            dialog.dismiss();
                            Intent intent = new Intent(ctx, VAC_Mother_HifazitiTeekeyRecordList_Activity.class);
                            intent.putExtra("u_id", mother_uid);
                            intent.putExtra("mother_name", mother_name);
                            intent.putExtra("mother_gender", mother_age);
                   /* intent.putExtra("mother_name", mother_name);
                    intent.putExtra("mother_gender", mother_age);*/
                            startActivity(intent);
                        }
                    }, 2000);

                }
            }
        });

    }


    public void Dialog_VaccineKoAnjaamDy_OLD(final int pos) {
        final Dialog dialog = new Dialog(ctx);
        LayoutInflater layout = LayoutInflater.from(ctx);
        final View dialogView = layout.inflate(R.layout.dialog_vaccine_ko_anjaam_dy_layout_vac, null);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);


        iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        btn_vaccine_ko_anjaam_dy = (Button) dialog.findViewById(R.id.btn_vaccine_ko_anjaam_dy);
        btn_phle_sy_li_hoe_vaccine = (Button) dialog.findViewById(R.id.btn_phle_sy_li_hoe_vaccine);
        btn_phle_sy_li_hoe_vaccine_uc = (Button) dialog.findViewById(R.id.btn_phle_sy_li_hoe_vaccine_uc);
        btn_refuse_vaccine = (Button) dialog.findViewById(R.id.btn_refuse_vaccine);
        btn_jamaa_kre = (Button) dialog.findViewById(R.id.submit);
        sp_inside_outside_council = (Spinner) dialog.findViewById(R.id.sp_inside_outside_council);
        txt_refuse_vaccine = (TextView) dialog.findViewById(R.id.txt_refuse_vaccine);


        spineer_data();
        dialog.show();


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        btn_vaccine_ko_anjaam_dy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 24) {
                }
                else{
                    final Snackbar snackbar = Snackbar.make(v, R.string.featureNotAvailable, Snackbar.LENGTH_SHORT);
                    snackbar.setDuration(4000);
                    snackbar.show();
                    return;
                }
                if (sp_inside_outside_council.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.pleaseSelectOne, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Intent intent = new Intent(ctx, VAC_Mother_HifazitiTeekeyVaccineKoAnjamDy_Activity.class);
                    intent.putExtra("u_id", mother_uid);
                    intent.putExtra("vac_id", String.valueOf(index_val));
                    intent.putExtra("vac_name", arrayList_vaccine_name.get(pos));
                    intent.putExtra("vac_place", sp_inside_outside_council.getSelectedItem().toString());
                    intent.putExtra("vac_place_pos", String.valueOf(sp_inside_outside_council.getSelectedItemPosition() - 1));
                    intent.putExtra("mother_name", mother_name);
                    intent.putExtra("mother_age", mother_age);

                    startActivity(intent);
                    dialog.dismiss();
                }


            }
        });

        btn_phle_sy_li_hoe_vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sp_inside_outside_council.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.pleaseSelectOne, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Intent intent = new Intent(ctx, VAC_Mother_HifazitiTeekeyPheleSyLiHoeVaccineForm_Activity.class);
                    intent.putExtra("u_id", mother_uid);
                    intent.putExtra("vac_id", String.valueOf(index_val));
                    intent.putExtra("vac_name", arrayList_vaccine_name.get(pos));
                    intent.putExtra("vac_place", sp_inside_outside_council.getSelectedItem().toString());
                    intent.putExtra("vac_place_pos", String.valueOf(sp_inside_outside_council.getSelectedItemPosition() - 1));
                    intent.putExtra("mother_name", mother_name);
                    intent.putExtra("mother_age", mother_age);
                    startActivity(intent);
                    dialog.dismiss();
                }

            }
        });

        btn_phle_sy_li_hoe_vaccine_uc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                        String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from MVACINE");

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
                            Toast.makeText(ctx, notDataGPS, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.d("000258", "Read MVACINE Error: " + e.getMessage());
                    }
                }

                try {


                    Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();


                    SimpleDateFormat dates = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss aa");
                    Calendar c = Calendar.getInstance();
                    String current_timeStamp = dates.format(c.getTime());
                    Log.d("000555", "timestamp:" + current_timeStamp);


                    final JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("vacine_place", "" + "inside UC");
                    jobj.put("vacine_name", "" + arrayList_vaccine_name.get(pos));
                    jobj.put("datetime", "" + current_timeStamp);


                    final String added_on = String.valueOf(System.currentTimeMillis());

                    String ans1 = "insert into MVACINE (member_uid,vaccine_id, record_data, type,vaccinated_on,image_location,data,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + mother_uid + "'," +
                            "'" + pos + "'," +
                            "'" + TodayDate + "'," +
                            "'" + "2" + "'," +
                            "'" + TodayDate + "'," +
                            "'" + TodayDate + "'," +
                            "'" + jobj + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000555", "Data: " + ans1);
                    Log.d("000555", "Query: " + res);

                    if (Utils.haveNetworkConnection(ctx) > 0) {

                        sendPostRequest(mother_uid, String.valueOf(pos), TodayDate, String.valueOf(jobj), login_useruid, added_on);
                    } else {
                        Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    Log.d("000555", " Error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {
                    dialog.dismiss();
                    Intent intent = new Intent(ctx, VAC_Mother_HifazitiTeekeyRecordList_Activity.class);
                    intent.putExtra("u_id", mother_uid);
                    intent.putExtra("mother_name", mother_name);
                    intent.putExtra("mother_gender", mother_age);
                    startActivity(intent);
                }

            }
        });

        btn_refuse_vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spineer_refuse_vacine();
                btn_vaccine_ko_anjaam_dy.setVisibility(View.GONE);
                btn_phle_sy_li_hoe_vaccine.setVisibility(View.GONE);
                btn_phle_sy_li_hoe_vaccine_uc.setVisibility(View.GONE);
                btn_refuse_vaccine.setVisibility(View.GONE);
                txt_refuse_vaccine.setVisibility(View.VISIBLE);
                btn_jamaa_kre.setVisibility(View.VISIBLE);


            }
        });


        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sp_inside_outside_council.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.selectVaccRefusalPrompt, Toast.LENGTH_LONG).show();
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

                        String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from MVACINE");

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
                            Toast.makeText(ctx, notDataGPS, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.d("000258", "Read MVACINE Error: " + e.getMessage());
                    }
                }

                try {

                    Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();

                    SimpleDateFormat dates = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss aa");
                    Calendar c = Calendar.getInstance();
                    String current_timeStamp = dates.format(c.getTime());
                    Log.d("000555", "timestamp:" + current_timeStamp);

                    final JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("vacine_place", "" + "refuse_vaccine");
                    jobj.put("vacine_name", "" + arrayList_vaccine_name.get(pos));
                    jobj.put("refuse_reason", "" + String.valueOf(sp_inside_outside_council.getSelectedItem().toString()));
                    jobj.put("refuse_reason_pos", "" + String.valueOf(sp_inside_outside_council.getSelectedItemPosition() - 1));
                    jobj.put("datetime", "" + current_timeStamp);

                    final String added_on = String.valueOf(System.currentTimeMillis());

                    String ans1 = "insert into MVACINE (member_uid,vaccine_id, record_data, type,vaccinated_on,image_location,data,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + mother_uid + "'," +
                            "'" + pos + "'," +
                            "'" + TodayDate + "'," +
                            "'" + "3" + "'," +
                            "'" + TodayDate + "'," +
                            "'" + TodayDate + "'," +
                            "'" + jobj + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000555", "Data: " + ans1);
                    Log.d("000555", "Query: " + res);

                    if (Utils.haveNetworkConnection(ctx) > 0) {

                        sendPostRequest_RefuseVaccine(mother_uid, String.valueOf(pos), TodayDate, String.valueOf(jobj), login_useruid, added_on);
                    } else {
                        Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    Log.d("000555", " Error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {

                    dialog.dismiss();
                    Intent intent = new Intent(ctx, VAC_Mother_HifazitiTeekeyRecordList_Activity.class);
                    intent.putExtra("u_id", mother_uid);
                    intent.putExtra("mother_name", mother_name);
                    intent.putExtra("mother_gender", mother_age);
                    startActivity(intent);
                }
            }
        });

    }


    private void spineer_data() {

        // Select sp_azdawaji_hasiyat
        final ArrayAdapter<CharSequence> adptr_council = ArrayAdapter.createFromResource(this, R.array.array_inside_outside_council, android.R.layout.simple_spinner_item);
        adptr_council.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        sp_inside_outside_council.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_council,
                        R.layout.spinner_azdawaji_hasiyat_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));


        sp_inside_outside_council.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void spineer_refuse_vacine() {

        // Select sp_azdawaji_hasiyat
        final ArrayAdapter<CharSequence> adptr_refuse = ArrayAdapter.createFromResource(this, R.array.array_refuse_vaccine, android.R.layout.simple_spinner_item);
        adptr_refuse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        sp_inside_outside_council.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_refuse,
                        R.layout.spinner_azdawaji_hasiyat_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));


        sp_inside_outside_council.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void sendPostRequest(final String member_uid, final String vacine_uid, final String record_data,
                                 final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/mother/vaccinations/new";

        Log.d("000555", "mURL " + url);
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

                        Log.d("000555", "response:  " + response);
                        //Toast.makeText(ctx, "Data has been saved", Toast.LENGTH_SHORT).show();

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();
                        String update_record = "UPDATE MVACINE SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + mother_uid + "'AND added_on= '" + added_on + "'AND vaccine_id= '" + vacine_uid + "'";
                        ls.executeNonQuery(update_record);
                        Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("000555", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(ctx, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", " Error: " + e.getMessage());
                    //Toast.makeText(ctx, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("000555", "onErrorResponse: " + error.getMessage());
                //    Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("member_id", member_uid);
                params.put("vaccine_id", vacine_uid);
                params.put("type", "2");
                params.put("record_data", record_data);
                params.put("vaccinated_on", TodayDate);
                params.put("image_location", TodayDate);
                params.put("metadata", data);
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


    private void sendPostRequest_RefuseVaccine(final String member_uid, final String vacine_uid, final String record_data,
                                               final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/mother/vaccinations/new";

        Log.d("000555", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000555", "response:   " + response);
                        //  Toast.makeText(ctx, "Data has been saved", Toast.LENGTH_SHORT).show();

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();
                        String update_record = "UPDATE MVACINE SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + member_uid + "'AND added_on= '" + added_on + "'AND vaccine_id= '" + vacine_uid + "'";
                        ls.executeNonQuery(update_record);

                        Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000555", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        //   Toast.makeText(ctx, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", " Error: " + e.getMessage());
                    //    Toast.makeText(ctx, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();
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
                params.put("vaccine_id", vacine_uid);
                params.put("type", "3");
                params.put("record_data", record_data);
                params.put("vaccinated_on", TodayDate);
                params.put("image_location", TodayDate);
                params.put("metadata", data);
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

        arrayList_vaccine_name.clear();
        arrayList_vaccine_date.clear();

        try {

            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            arrayList_vaccine_name.add("TT Injection 1");
            arrayList_vaccine_name.add("TT Injection 2");
            arrayList_vaccine_name.add("TT Injection 3");
            arrayList_vaccine_name.add("TT Injection 4");
            arrayList_vaccine_name.add("TT Injection 5");

            for (int i = 0; i < arrayList_vaccine_name.size(); i++) {
                index_val = i;
                Log.d("000987", "Size: " + arrayList_vaccine_name.size());
                Log.d("000987", "Loop: " + index_val);

                String[][] mDatavac = ls.executeReader("Select vaccinated_on from MVACINE where member_uid = '" + mother_uid + "' AND vaccine_id = '" + i + "'");
                if (mDatavac != null) {
                    String[][] mData_refuse = ls.executeReader("Select vaccinated_on from MVACINE where member_uid = '" + mother_uid + "' AND vaccine_id = '" + i + "' AND type ='3' ");
                    if (mData_refuse != null) {
                        arrayList_vaccine_date.add(i + "@" + mData_refuse[0][0] + "@" + "0");
                        Log.d("000987", "Vaccine Refuse: ");
                    } else {
                        arrayList_vaccine_date.add(i + "@" + mDatavac[0][0] + "@" + "1");
                        Log.d("000987", "Vaccine Not Refuse: ");
                    }
                    Log.d("000987", "Vacine active : ");
                } else {
                    arrayList_vaccine_date.add(i + "@" + "vaccine due" + "@" + "2");
                    Log.d("000987", "Vacine not active: ");
                }

            }
            adt = new Adt_MotherVaccineList(ctx, arrayList_vaccine_name, arrayList_vaccine_date);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);


        } catch (Exception e) {
            Log.d("000987", "Error: " + e.getMessage());
            Toast.makeText(ctx, R.string.noRecord, Toast.LENGTH_SHORT).show();
        }


    }

    private void calculateAge() {

        Lister ls = new Lister(ctx);
        ls.createAndOpenDB();

        String mData[][] = ls.executeReader("Select full_name,dob,gender from MEMBER where uid = '" + mother_uid + "'");

        if (mData[0][0].isEmpty())
        {
            mother_name=String.valueOf(R.string.unknown);
            txt_mother_name.setText(mother_name);
        }
        else{
            mother_name = mData[0][0];
            txt_mother_name.setText(mother_name);
        }

        mother_gender = mData[0][2];

        Log.d("000555", "DOB: " + mData[0][1]);

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

            Log.d("000555", "Saal: " + String.valueOf(dob_years));
            Log.d("000555", "Maah: " + String.valueOf(dob_months));

            String mother_age_with_saal = String.valueOf(dob_years) + " سال " + String.valueOf(dob_months) + " مہ ";
            txt_mother_age.setText(mother_age_with_saal);
            mother_age = mother_age_with_saal;

        } catch (ParseException e) {
            Log.d("000555", "DOB Error: " + e.getMessage());
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
          if ( var_regtemp_abovetwo.equalsIgnoreCase("1"))
        {
            Intent intent = new Intent(ctx, HomePageVacinator_Activity.class);
            startActivity(intent);
            Log.d("000555", "IFFF");
        }
        else {
            finish();
            Log.d("000555", "ELSEEEEEEEEEE");
        }


        Intent intent = new Intent(ctx, HomePageVacinator_Activity.class);
        startActivity(intent);
    }
}