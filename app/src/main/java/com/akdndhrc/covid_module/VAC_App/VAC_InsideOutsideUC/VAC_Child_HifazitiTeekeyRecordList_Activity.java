package com.akdndhrc.covid_module.VAC_App.VAC_InsideOutsideUC;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.Adt_ChildDashboard.Adt_ChildHifazitiTeekeyRecordList;
import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.GPSTracker;
import com.akdndhrc.covid_module.NothingSelectedSpinnerAdapter;
import com.akdndhrc.covid_module.ServiceLocation;
import com.akdndhrc.covid_module.Utils;
import com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities.VAC_SearchActivities.VAC_Search_MemberList_Activity;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class VAC_Child_HifazitiTeekeyRecordList_Activity extends AppCompatActivity {

    Context ctx = VAC_Child_HifazitiTeekeyRecordList_Activity.this;
    TextView txt_age, txt_naam, txt_refuse_vaccine;
    ListView lv;
    RelativeLayout rl_navigation_drawer, rl_home, rl_circle_profile;

    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> arrayListVaccine = new ArrayList<>();
    ArrayList<String> arrayListDate = new ArrayList<>();
    ImageView iv_navigation_drawer, iv_home, iv_close, image_gender;

    Adt_ChildHifazitiTeekeyRecordList adt;
    String[][] mData;
    int index_val = 0;
    Button btn_phle_sy_li_hoe_vaccine, btn_vaccine_ko_anjaam_dy, btn_phle_sy_li_hoe_vaccine_uc, btn_refuse_vaccine, btn_jamaa_kre;

    String child_uid, child_age, child_name, child_gender;
    String to_make_active = "yes";
    int diffInDays;

    Spinner sp_inside_outside_council, sp_refuse_vaccine;
    double latitude;
    String TodayDate;
    double longitude;
    // GPSTracker cl
    GPSTracker gps;
    ServiceLocation serviceLocation;
    String login_useruid;

    Dialog alertDialog;
    String age;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vac_child_hifaziti_teekey_record_list);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, VAC_Child_HifazitiTeekeyRecordList_Activity.class));
        Log.d("000555", "RESTARRTTT");

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

        child_uid = getIntent().getExtras().getString("u_id");
        child_name = getIntent().getExtras().getString("child_name");
        // child_age = getIntent().getExtras().getString("child_age");
        child_gender = getIntent().getExtras().getString("child_gender");


        Log.d("000123", child_uid);
        Log.d("000123", child_name);
        Log.d("000123", child_gender);

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


        //ListView
        lv = findViewById(R.id.lv);

        //TextView
        txt_age = findViewById(R.id.txt_age);
        txt_naam = findViewById(R.id.txt_naam);

       /* txt_naam.setText(child_name);
        //txt_age.setText(child_age);*/

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);
        image_gender = findViewById(R.id.image_gender);

        //RelativeLayout
        rl_circle_profile = findViewById(R.id.rl_circle_profile);

        calculateAge();

     if (child_gender.equalsIgnoreCase("0")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_adult_female_50dp));
                image_gender.setImageTintList(ctx.getResources().getColorStateList(R.color.pink_color));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    image_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                    image_gender.setImageTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
                }
            }
        }


        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("12345", "Errr");

                Toast.makeText(ctx, "Navigation", Toast.LENGTH_SHORT).show();
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

                if (diffInDays < Integer.parseInt(mData[position][2]) && diffInDays > (Integer.parseInt(mData[position][2]) - 15)) {
                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String[][] mDataa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "'");
                        if (mDataa != null) {

                            String[][] mData_ref_vac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "' AND type = '3' ");

                            if (mData_ref_vac != null) {
                                Log.d("000555", "Refuse");
                                Toast.makeText(getApplicationContext(), R.string.refused_vaccine, Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("000555", "Not Ref");
                                Toast.makeText(getApplicationContext(), R.string.Child_already_vaccinated, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Dialog_VaccineKoAnjaamDy();
                        }

                    } catch (Exception e) {
                        Log.d("000555", "Err:" + e.getMessage());
                        Toast.makeText(getApplicationContext(), R.string.Child_already_vaccinated, Toast.LENGTH_SHORT).show();
                    }

                } else if (diffInDays < Integer.parseInt(mData[position][2])) {

                    Toast.makeText(getApplicationContext(), R.string.vaccine_not_yet_active, Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String[][] mDataa = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "'");
                        if (mDataa != null) {

                            String[][] mData_ref_vac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[position][0]) + "' AND type = '3' ");

                            if (mData_ref_vac != null) {
                                Log.d("000555", "Refuse 12");
                                Toast.makeText(getApplicationContext(), R.string.refused_vaccine, Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("000555", "Not Ref  12");
                                Toast.makeText(getApplicationContext(), R.string.Child_already_vaccinated, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Dialog_VaccineKoAnjaamDy();
                        }

                    } catch (Exception e) {
                        Log.d("000555", "Err 2:" + e.getMessage());
                    }
                }
            }
        });

        rl_circle_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialog = new Dialog(ctx);
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

                        try {
                            Lister ls = new Lister(ctx);
                            ls.createAndOpenDB();

                            final String mData_Khandan[][] = ls.executeReader("SELECT t1.full_name,t2.province_id,t2.district_id,t2.subdistrict_id,t2.uc_id,t2.village_id from MEMBER t1" +
                                    " LEFT JOIN KHANDAN t2 On t1.khandan_id=t2.uid" +
                                    " where t1.uid='" + child_uid + "'");

                            if (mData_Khandan[0][1] != null && mData_Khandan[0][2] != null && mData_Khandan[0][3] != null && mData_Khandan[0][4] != null && mData_Khandan[0][4] != null) {
                                Log.d("000123", "KHANDAN Already Register:" );

                                Intent intent = new Intent(ctx, VAC_BelowTwoProfile_Activity.class);
                                intent.putExtra("u_id", child_uid);
                                startActivity(intent);
                                alertDialog.dismiss();
                            } else {
                                Log.d("000123", "KHANDAN Not Register");
                                Intent intent = new Intent(ctx, VAC_BelowTwoProfileWithKhandan_Activity.class);
                                intent.putExtra("u_id", child_uid);
                                startActivity(intent);
                                alertDialog.dismiss();
                            }

                        } catch (Exception e) {
                            Log.d("000123", "Sp Error: " + e.getMessage());
                            alertDialog.dismiss();
                        }


                    }
                }, 1500);


            }
        });

    }

    public void Dialog_VaccineKoAnjaamDy() {
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
                } else {
                    final Snackbar snackbar = Snackbar.make(v, "This feature is not available due to lowest sdk version.", Snackbar.LENGTH_SHORT);
                    snackbar.setDuration(4000);
                    snackbar.show();
                    return;
                }

                if (sp_inside_outside_council.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.pleaseSelectOne, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    //Intent intent = new Intent(ctx, VAC_Child_HifazitiTeekeyVaccineKoAnjamDy_Activity.class);
                    Intent intent = new Intent(ctx, VAC_Child_HifazitiTeekeyVaccineKoAnjamDy_2_Activity.class);

                    intent.putExtra("u_id", child_uid);
                    intent.putExtra("vac_id", mData[index_val][0]);
                    intent.putExtra("vac_name", mData[index_val][1]);
                    intent.putExtra("vac_duedate", arrayListDate.get(index_val));
                    intent.putExtra("vac_place", sp_inside_outside_council.getSelectedItem().toString());
                    intent.putExtra("vac_place_pos", String.valueOf(sp_inside_outside_council.getSelectedItemPosition() - 1));
                    intent.putExtra("child_name", child_name);
                    // intent.putExtra("child_age", child_age);
                    intent.putExtra("child_gender", child_gender);

                    startActivity(intent);
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
                    Intent intent = new Intent(ctx, VAC_Child_HifazitiTeekeyPheleSyLiHoeVaccineForm_Activity.class);
                    intent.putExtra("u_id", child_uid);
                    intent.putExtra("vac_id", mData[index_val][0]);
                    intent.putExtra("vac_name", mData[index_val][1]);
                    intent.putExtra("vac_duedate", arrayListDate.get(index_val));
                    intent.putExtra("vac_place", sp_inside_outside_council.getSelectedItem().toString());
                    intent.putExtra("vac_place_pos", String.valueOf(sp_inside_outside_council.getSelectedItemPosition() - 1));
                    intent.putExtra("child_name", child_name);
                    intent.putExtra("child_age", child_age);
                    intent.putExtra("child_gender", child_gender);

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

                        String[][] mData = ls.executeReader("SELECT max(added_on),metadata,count(*) from CVACCINATION");

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
                        Log.d("000258", "Read CVACCINE Error: " + e.getMessage());
                    }
                }

                try {


                    Lister ls = new Lister(VAC_Child_HifazitiTeekeyRecordList_Activity.this);
                    ls.createAndOpenDB();


                    // et_refferal_ki_waja = findViewById(R.id.et_refferal_ki_waja);
                    // et_refferal_hospital = findViewById(R.id.et_refferal_hospital);

                    //Edittext


                    final JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("vacine_place", "" + "inside UC");
                    // jobjMain.put("data", jobj);

                    final String added_on = String.valueOf(System.currentTimeMillis());

                    String ans1 = "insert into CVACCINATION (member_uid,vaccine_id, record_data, type, due_date,vaccinated_on,image_location,metadata,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + child_uid + "'," +
                            "'" + mData[index_val][0] + "'," +
                            "'" + TodayDate + "'," +
                            "'" + "2" + "'," +
                            "'" + arrayListDate.get(index_val) + "'," +
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

                    //imageocation o be added

                    // Toast.makeText(getApplicationContext(), "Data saved.", Toast.LENGTH_SHORT).show();

                    if (Utils.haveNetworkConnection(ctx) > 0) {

                        sendPostRequest(child_uid, mData[index_val][0], TodayDate, String.valueOf(jobj), login_useruid, added_on);
                    } else {
                        Toast.makeText(ctx, "Data has been collected", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", " Error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {

                    dialog.dismiss();
                    Intent intent = new Intent(ctx, VAC_Child_HifazitiTeekeyRecordList_Activity.class);
                    intent.putExtra("u_id", child_uid);
                    intent.putExtra("child_name", child_name);
                    intent.putExtra("child_gender", child_gender);
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

               /* dialog.dismiss();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Dialog_Refuse_Vaccine();
                    }
                }, 1500);*/


            }
        });


        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
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

                        String[][] mData = ls.executeReader("SELECT max(added_on),metadata,count(*) from CVACCINATION");

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
                        Log.d("000258", "Read CVACCINE Error: " + e.getMessage());
                    }
                }


                if (sp_inside_outside_council.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), "R.string.reasonToDenyVaccine منتخب کریں", Toast.LENGTH_LONG).show();
                    return;
                }
                try {

                    Lister ls = new Lister(VAC_Child_HifazitiTeekeyRecordList_Activity.this);
                    ls.createAndOpenDB();

                    final JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("vacine_place", "" + "refuse_vaccine");
                    jobj.put("refuse_reason", "" + String.valueOf(sp_inside_outside_council.getSelectedItem().toString()));
                    jobj.put("refuse_reason_pos", "" + String.valueOf(sp_inside_outside_council.getSelectedItemPosition() - 1));


                    final String added_on = String.valueOf(System.currentTimeMillis());

                    String ans1 = "insert into CVACCINATION (member_uid,vaccine_id, record_data, type, due_date,vaccinated_on,image_location,metadata,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + child_uid + "'," +
                            "'" + mData[index_val][0] + "'," +
                            "'" + TodayDate + "'," +
                            "'" + "3" + "'," +
                            "'" + arrayListDate.get(index_val) + "'," +
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

                    //imageocation o be added

                    if (Utils.haveNetworkConnection(ctx) > 0) {
                        sendPostRequest_RefuseVaccine(child_uid, mData[index_val][0], TodayDate, String.valueOf(jobj), login_useruid, added_on);

                    } else {
                        Toast.makeText(ctx, "Data has been collected", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    Log.d("000555", " Error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {

                    dialog.dismiss();
                    Intent intent = new Intent(ctx, VAC_Child_HifazitiTeekeyRecordList_Activity.class);
                    intent.putExtra("u_id", child_uid);
                    intent.putExtra("child_name", child_name);
                    intent.putExtra("child_gender", child_gender);
                    startActivity(intent);
                }


            }
        });

    }

    private void sendPostRequest(final String member_uid, final String vacine_uid, final String record_data,
                                 final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/child/vaccinations";

        Log.d("000555", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {
                    //   Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000555", "response1    " + response);
                        //Toast.makeText(VAC_Child_HifazitiTeekeyRecordList_Activity.this, "Data has been saved", Toast.LENGTH_SHORT).show();

                        Lister ls = new Lister(VAC_Child_HifazitiTeekeyRecordList_Activity.this);
                        ls.createAndOpenDB();
                        String update_record = "UPDATE CVACCINATION SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + child_uid + "'AND added_on= '" + added_on + "'AND vaccine_id= '" + vacine_uid + "'";
                        ls.executeNonQuery(update_record);

                        Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000555", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(VAC_Child_HifazitiTeekeyRecordList_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", " Error: " + e.getMessage());
                    //Toast.makeText(VAC_Child_HifazitiTeekeyRecordList_Activity.this, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("000555", "error    " + error.getMessage());
                //Toast.makeText(VAC_Child_HifazitiTeekeyRecordList_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
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

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/child/vaccinations";

        Log.d("000555", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {
                    //   Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000555", "response1    " + response);
                        // Toast.makeText(VAC_Child_HifazitiTeekeyRecordList_Activity.this, "Data has been saved", Toast.LENGTH_SHORT).show();

                        Lister ls = new Lister(VAC_Child_HifazitiTeekeyRecordList_Activity.this);
                        ls.createAndOpenDB();
                        String update_record = "UPDATE CVACCINATION SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + child_uid + "'AND added_on= '" + added_on + "'AND vaccine_id= '" + vacine_uid + "'";
                        ls.executeNonQuery(update_record);

                        Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000555", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(VAC_Child_HifazitiTeekeyRecordList_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", " Error: " + e.getMessage());
                    //    Toast.makeText(VAC_Child_HifazitiTeekeyRecordList_Activity.this, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("000555", "error    " + error.getMessage());
                //Toast.makeText(VAC_Child_HifazitiTeekeyRecordList_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
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

    private void calculateAge() {

        Lister ls = new Lister(ctx);
        ls.createAndOpenDB();

        String mData[][] = ls.executeReader("Select full_name,dob from MEMBER where uid = '" + child_uid + "'");

        if (mData[0][0].isEmpty()) {
            child_name = "Unknown";
            txt_naam.setText(child_name);
        } else {
            child_name = mData[0][0];
            txt_naam.setText(child_name);
        }

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

            child_age = String.valueOf(dob_years) + " سال " + String.valueOf(dob_months) + " مہ ";
            txt_age.setText(child_age);

            age = String.valueOf(dob_years);

//            Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            Log.d("000555", "DOB Error: " + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {


            arrayListVaccine.clear();
            arrayListDate.clear();

            arrayListVaccine = new ArrayList<String>();
            arrayListDate = new ArrayList<String>();


            try {

                Lister ls = new Lister(ctx);
                ls.createAndOpenDB();

                mData = ls.executeReader("Select uid,name,due_date from VACCINES ");
                Log.d("000555", "Data" + String.valueOf(mData.length));


                String[][] mDataa = ls.executeReader("Select dob from MEMBER where uid = '" + child_uid + "'");
                Log.d("000555", "DOB:" + mDataa[0][0]);
                SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                String TodayDate = dates.format(c.getTime());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


                //Convert to Date
                Date startDate = dates.parse(TodayDate);
                Calendar c1 = Calendar.getInstance();
                //Change to Calendar Date
                c1.setTime(startDate);

                //Convert to Date
                Date endDate = dates.parse(mDataa[0][0]);
                Calendar c2 = Calendar.getInstance();
                //Change to Calendar Date
                c2.setTime(endDate);

                //get Time in milli seconds
                long ms1 = c1.getTimeInMillis();
                long ms2 = c2.getTimeInMillis();
                //get difference in milli seconds
                long diff = ms1 - ms2;
                diffInDays = (int) (diff / (24 * 60 * 60 * 1000));
                Log.d("000555cal", String.valueOf(diffInDays));
//0 is due 1 is active 2 is defaulter 3 is grey 4 is else

                //String[] vacine_name ="testname,0".split("[,]");
                //  Log.d("000555",vacine_name[0]+vacine_name[1]);
                if (mData != null) {
                    HashMap<String, String> map;
                    for (int i = 0; i < mData.length; i++) {
                        Log.d("000555", "Vaccines Name: " + mData[i][1]);

                        if (diffInDays == Integer.parseInt(mData[i][2])) {

                            Calendar cvac = new GregorianCalendar();
                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                            cvac.setTime(dateFormatcvac.parse(mDataa[0][0]));
                            cvac.add(Calendar.DATE, Integer.parseInt(mData[i][2]));
                            Date dvac = cvac.getTime();
                            dateFormat.format(dvac.getTime());
                            String[][] mDatavac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                            // Log.d("000555", "Type: " +mDatavac.length);
                            if (mDatavac != null) {
                                String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                if (mData_refuse != null) {
                                    arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                    Log.d("000555", "1000: ");
                                } else {
                                    arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mDatavac[0][0]);
                                    Log.d("000555", "1: ");
                                }

                            } else {
                                arrayListVaccine.add(i, mData[i][1] + ",1");
                                Log.d("000555", "2: ");
                            }
                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                        } else if (diffInDays < Integer.parseInt(mData[i][2]) && diffInDays > (Integer.parseInt(mData[i][2]) - 15)) {
                            //arrayListVaccine.add(i, mData[i][1] + ",1");
                            Calendar cvac = new GregorianCalendar();
                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                            cvac.setTime(dateFormatcvac.parse(mDataa[0][0]));
                            cvac.add(Calendar.DATE, Integer.parseInt(mData[i][2]));
                            Date dvac = cvac.getTime();
                            dateFormat.format(dvac.getTime());
                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                            String[][] mDatavac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                            if (mDatavac != null) {

                                String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                if (mData_refuse != null) {
                                    arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                    Log.d("000555", "101: ");
                                } else {
                                    arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mDatavac[0][0]);
                                    Log.d("000555", "3: ");
                                }
                            } else {
                                arrayListVaccine.add(i, mData[i][1] + ",1");
                                Log.d("000555", "4: ");
                            }
                        } else if (diffInDays > Integer.parseInt(mData[i][2])) {
                            // arrayListVaccine.add(i, mData[i][1] + ",2");
                            Calendar cvac = new GregorianCalendar();
                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                            cvac.setTime(dateFormatcvac.parse(mDataa[0][0]));
                            cvac.add(Calendar.DATE, Integer.parseInt(mData[i][2]));
                            Date dvac = cvac.getTime();
                            dateFormat.format(dvac.getTime());
                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                            String[][] mDatavac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                            if (mDatavac != null) {
//                                arrayListVaccine.add(i, mData[i][1] + ",0");
//                                Log.d("000555", "5: " );
                                String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                if (mData_refuse != null) {
                                    arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                    Log.d("000555", "102: ");
                                } else {
                                    arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mDatavac[0][0]);
                                    Log.d("000555", "5: ");
                                }

                            } else {
                                arrayListVaccine.add(i, mData[i][1] + ",2");
                                Log.d("000555", "6: ");
                            }
                        } else if (diffInDays < Integer.parseInt(mData[i][2])) {
                            // arrayListVaccine.add(i, mData[i][1] + ",3");
                            Calendar cvac = new GregorianCalendar();
                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                            cvac.setTime(dateFormatcvac.parse(mDataa[0][0]));
                            cvac.add(Calendar.DATE, Integer.parseInt(mData[i][2]));
                            Date dvac = cvac.getTime();
                            dateFormat.format(dvac.getTime());
                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                            String[][] mDatavac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                            if (mDatavac != null) {
                               /* arrayListVaccine.add(i, mData[i][1] + ",0");
                                Log.d("000555", "7: " );*/
                                String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                if (mData_refuse != null) {
                                    arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                    Log.d("000555", "103: ");
                                } else {
                                    arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mDatavac[0][0]);
                                    Log.d("000555", "7: ");
                                }

                            } else {

                                arrayListVaccine.add(i, mData[i][1] + ",3");
                                Log.d("000555", "8 ");
                            }
                        } else {
                            //arrayListVaccine.add(i, mData[i][1] + ",4");
                            Calendar cvac = new GregorianCalendar();
                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                            cvac.setTime(dateFormatcvac.parse(mDataa[0][0]));
                            cvac.add(Calendar.DATE, Integer.parseInt(mData[i][2]));
                            Date dvac = cvac.getTime();
                            dateFormat.format(dvac.getTime());
                            arrayListDate.add(i, dateFormat.format(dvac.getTime()));
                            String[][] mDatavac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                            if (mDatavac != null) {
                               /* arrayListVaccine.add(i, mData[i][1] + ",0");
                                Log.d("000555", "9: " );*/
                                String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                if (mData_refuse != null) {
                                    arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                    Log.d("000555", "104: ");
                                } else {
                                    arrayListVaccine.add(i, mData[i][1] + ",0" + "," + mDatavac[0][0]);
                                    Log.d("000555", "9: ");
                                }

                            } else {

                                arrayListVaccine.add(i, mData[i][1] + ",4");
                                Log.d("000555", "10 ");
                            }
                        }
                    }
                } else {
                    Toast.makeText(ctx, "No Data", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.d("000555", "Vacine Err: " + String.valueOf(e.getMessage()));
            }
            adt = new Adt_ChildHifazitiTeekeyRecordList(ctx, arrayListVaccine, arrayListDate);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);

        } catch (Exception e) {
            Log.d("12345", "Error: " + e.getMessage());
            Toast.makeText(ctx, R.string.noRecord, Toast.LENGTH_SHORT).show();
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

        if (VAC_Search_MemberList_Activity.temp_var.equalsIgnoreCase("child_record_list")) {
            Intent intent = new Intent(ctx, VAC_Search_MemberList_Activity.class);
            startActivity(intent);
            VAC_Search_MemberList_Activity.temp_var = "0";
            Log.d("000555", "IFFF");
        }
        else if ( VAC_BelowTwoRegister_Activity.var_regtemp_belowtwo.equalsIgnoreCase("1"))
        {
            Intent intent = new Intent(ctx, HomePageVacinator_Activity.class);
           startActivity(intent);
            Log.d("000555", "ELSEE IFFF");
        }
        else {
            finish();
            Log.d("000555", "ELSEEEEEEEEEE");
        }


    }
}
