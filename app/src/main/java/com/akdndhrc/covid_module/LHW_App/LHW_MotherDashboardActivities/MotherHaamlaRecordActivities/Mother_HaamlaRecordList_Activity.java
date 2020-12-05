package com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHaamlaRecordActivities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.Adt_MotherDashboard.Adt_MotherHaamlaRecordList;
import com.akdndhrc.covid_module.AppController;
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
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.akdndhrc.covid_module.R.string.wrongUpdatedANC;
import static com.akdndhrc.covid_module.R.string.wrongUpdatedDelivery;
import static com.akdndhrc.covid_module.R.string.wrongUpdatedPreg;


public class Mother_HaamlaRecordList_Activity extends AppCompatActivity {

    Context ctx = Mother_HaamlaRecordList_Activity.this;

    TextView txt_mother_age, txt_mother_name;
    //ListView lv;
    Button btn_naye_haaml_ka_form_shamil_kre, btn_jari_rhy;
    EditText et_mutawaqqa_zichgi_ki_tareekh, et_akhiri_haiz_ki_tareekh;

    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    Adt_MotherHaamlaRecordList adt;

    ImageView iv_navigation_drawer, iv_home, iv_close;
    double latitude;
    double longitude;
    // GPSTracker class
    GPSTracker gps;
    String[][] mData;
    private int mYear, mMonth, mDay;
    String monthf2, dayf2, yearf2 = "null";
    String mother_uid, TodayDate, mother_name, mother_age;
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid;
    public static String var_add_preg = "0";

    private SwipeMenuListView lv;

    Dialog alertDialog;
    Lister ls;
    JSONObject jsonObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_haamla_record_list);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Mother_HaamlaRecordList_Activity.class));

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

        mother_uid = getIntent().getExtras().getString("u_id");
        mother_name = getIntent().getExtras().getString("mother_name");
        mother_age = getIntent().getExtras().getString("mother_age");

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


        // check_gps();

        //ListView
        // lv = findViewById(R.id.lv);
        lv = (SwipeMenuListView) findViewById(R.id.lv);
        lv.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);


        //TextView
        txt_mother_name = findViewById(R.id.txt_mother_name);
        txt_mother_age = findViewById(R.id.txt_mother_age);

        txt_mother_name.setText(mother_name);
        txt_mother_age.setText(mother_age);


        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        //iv_home.setVisibility(View.GONE);

        //Button
        btn_naye_haaml_ka_form_shamil_kre = findViewById(R.id.btn_naye_haaml_ka_form_shamil_kre);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // startActivity(new Intent(ctx,Mother_HaamlaDashboard_Activity.class));
                Intent intent = new Intent(ctx, Mother_HaamlaDashboard_Activity.class);
                intent.putExtra("u_id", mother_uid);
                intent.putExtra("mother_age", mother_age);
                intent.putExtra("mother_name", mother_name);
                intent.putExtra("preg_id", mData[position][4]);
                //  Toast.makeText(getApplicationContext(),mData[position][0],Toast.LENGTH_SHORT).show();
                startActivity(intent);


            }
        });


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

        btn_naye_haaml_ka_form_shamil_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog_LMPDate_EditText();
            }
        });


        adt = new Adt_MotherHaamlaRecordList(ctx, hashMapArrayList);
        lv.setAdapter(adt);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {


                // create R.string.editEng item
                SwipeMenuItem editItem = new SwipeMenuItem(getApplicationContext());
                editItem.setBackground(new ColorDrawable(ctx.getResources().getColor(R.color.blue_text_color)));
                editItem.setWidth(dp2px(60));
                editItem.setIcon(R.drawable.ic_editprofile_icon);
                menu.addMenuItem(editItem);


// create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
// set item background
                // deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setBackground(new ColorDrawable(ctx.getResources().getColor(R.color.hp_listview_textview_redcolor)));
                //deleteItem.setBackground(ctx.getResources().getColor(R.color.hp_listview_textview_redcolor));

// set item width
                deleteItem.setWidth(dp2px(60));
// set a icon
                deleteItem.setIcon(R.drawable.ic_delete_black_24dp);

// add to menu
                menu.addMenuItem(deleteItem);

            }
        };


        // set creator

        lv.setMenuCreator(creator);
        lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                switch (index) {
                    case 0:
                        Dialog_Update_LMPDate(position);
                        break;

                    case 1:
                        Dialog_Delete_PregnancyRecord(position);
                        /*hashMapArrayList.remove(position);
                        adt.notifyDataSetChanged();*/
                        break;
                }
                return true;
            }
        });

        //mListView

        lv.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }

        });

        lv.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
            }

            @Override
            public void onSwipeEnd(int position) {
            }
        });

    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());

    }

    public void Dialog_LMPDate_EditText() {
        final Dialog dialog = new Dialog(ctx);
        LayoutInflater layout = LayoutInflater.from(ctx);

        View dialogView = layout.inflate(R.layout.dialog_mother_haamla_record_lmpdate_layout, null);

        et_mutawaqqa_zichgi_ki_tareekh = (EditText) dialogView.findViewById(R.id.et_mutawaqqa_zichgi_ki_tareekh);
        et_akhiri_haiz_ki_tareekh = (EditText) dialogView.findViewById(R.id.et_akhiri_haiz_ki_tareekh);

        et_akhiri_haiz_ki_tareekh.setEnabled(true);
        et_mutawaqqa_zichgi_ki_tareekh.setEnabled(false);
        et_mutawaqqa_zichgi_ki_tareekh.setFocusable(false);
        et_akhiri_haiz_ki_tareekh.setFocusable(false);
        et_akhiri_haiz_ki_tareekh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDateDialougeUpdated();
            }
        });
        iv_close = (ImageView) dialogView.findViewById(R.id.iv_close);

        btn_jari_rhy = (Button) dialogView.findViewById(R.id.btn_jari_rhy);

        dialog.setContentView(dialogView);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_jari_rhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_akhiri_haiz_ki_tareekh.getText().toString().length() < 1) {
                    // Toast.makeText(getApplicationContext(), "برائے مہربانی آخری ماہواری کی تاریخ منتخب کریں", Toast.LENGTH_LONG).show();
                    final Snackbar snackbar = Snackbar.make(v, R.string.selectLastPeriodDatePrompt, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(15);
                    snackbar.setDuration(3000);
                    snackbar.show();
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

                        String[][] mData = ls.executeReader("SELECT max(added_on),metadata,count(*) from MPREGNANCY");

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
                        Log.d("000258", "Read MPREGNANCY Error: " + e.getMessage());
                    }
                }


                try {


                    Lister ls = new Lister(Mother_HaamlaRecordList_Activity.this);
                    ls.createAndOpenDB();
                    // et_refferal_ki_waja = findViewById(R.id.et_refferal_ki_waja);
                    // et_refferal_hospital = findViewById(R.id.et_refferal_hospital);

                    String peg_uuid = UUID.randomUUID().toString().replace("-", "");
                    JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("mpregnancy_status", "" + "Active");
                    jobj.put("status", "" + "0");
                    jobj.put("fistula", "" + "Off");
                    jobj.put("fistula_status", "" + "0");


                    String cur_added_on = String.valueOf(System.currentTimeMillis());
                    // jobjMain.put("data", jobj);
                    String ans1 = "insert into MPREGNANCY (member_uid, pregnancy_id,record_data,lmp,edd,metadata, status,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + mother_uid + "'," +
                            "'" + peg_uuid + "'," +
                            "'" + TodayDate + "'," +
                            "'" + et_akhiri_haiz_ki_tareekh.getText().toString() + "'," +
                            "'" + et_mutawaqqa_zichgi_ki_tareekh.getText().toString() + "'," +
                            "'" + jobj + "'," +
                            "'" + "1" + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + cur_added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000555", "Data: " + ans1);
                    Log.d("000555", "Query: " + res.toString());
                    if (res.toString().equalsIgnoreCase("true")) {

                        final Snackbar snackbar = Snackbar.make(findViewById(R.id.pregnancy_layout), R.string.newPregnancyRecorded, Snackbar.LENGTH_SHORT);
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
                        snackbar.setDuration(3000);
                        snackbar.show();


                        if (Utils.haveNetworkConnection(ctx) > 0) {

                            sendPostRequest(mother_uid, peg_uuid, TodayDate, et_akhiri_haiz_ki_tareekh.getText().toString(), et_mutawaqqa_zichgi_ki_tareekh.getText().toString()
                                    , String.valueOf(jobj), login_useruid, cur_added_on);
                        } else {
                            //Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();
                        }

                        var_add_preg = "1";

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Intent intent = new Intent(ctx, Mother_HaamlaRecordList_Activity.class);
                                intent.putExtra("u_id", mother_uid);
                                intent.putExtra("mother_name", mother_name);
                                intent.putExtra("mother_age", mother_age);
                                startActivity(intent);
                            }
                        }, 2000);
                    } else {
                        final Snackbar snackbar = Snackbar.make(findViewById(R.id.pregnancy_layout), R.string.pregnancyNotEdited, Snackbar.LENGTH_SHORT);
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
                        snackbar.setDuration(3000);
                        snackbar.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 2000);

                    }

                } catch (Exception e) {
                    dialog.dismiss();
                    Log.d("000555", "Err: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    public void Dialog_Update_LMPDate(final int pos) {
        final Dialog dialog = new Dialog(ctx);
        LayoutInflater layout = LayoutInflater.from(ctx);
        View dialogView = layout.inflate(R.layout.dialog_mother_haamla_record_lmpdate_layout, null);

        et_mutawaqqa_zichgi_ki_tareekh = (EditText) dialogView.findViewById(R.id.et_mutawaqqa_zichgi_ki_tareekh);
        et_akhiri_haiz_ki_tareekh = (EditText) dialogView.findViewById(R.id.et_akhiri_haiz_ki_tareekh);

        et_akhiri_haiz_ki_tareekh.setEnabled(true);
        et_mutawaqqa_zichgi_ki_tareekh.setEnabled(false);
        et_mutawaqqa_zichgi_ki_tareekh.setFocusable(false);
        et_akhiri_haiz_ki_tareekh.setFocusable(false);
        et_akhiri_haiz_ki_tareekh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDateDialougeUpdated();
            }
        });
        iv_close = (ImageView) dialogView.findViewById(R.id.iv_close);

        btn_jari_rhy = (Button) dialogView.findViewById(R.id.btn_jari_rhy);

        dialog.setContentView(dialogView);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        et_akhiri_haiz_ki_tareekh.setText(hashMapArrayList.get(pos).get("haaml_txt").split(",")[0]);
        et_mutawaqqa_zichgi_ki_tareekh.setText(hashMapArrayList.get(pos).get("haaml_txt").split(",")[1]);


        btn_jari_rhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_akhiri_haiz_ki_tareekh.getText().toString().length() < 1) {
                    // Toast.makeText(getApplicationContext(), "برائے مہربانی آخری ماہواری کی تاریخ منتخب کریں", Toast.LENGTH_LONG).show();
                    final Snackbar snackbar = Snackbar.make(v, R.string.selectLastPeriodDatePrompt, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(15);
                    snackbar.setDuration(3000);
                    snackbar.show();
                    return;
                }


                try {


                    Lister ls = new Lister(Mother_HaamlaRecordList_Activity.this);
                    ls.createAndOpenDB();

                    final String updated_added_on = String.valueOf(System.currentTimeMillis());

                    SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar c = Calendar.getInstance();
                    String TodayDate = dates.format(c.getTime());


                    jsonObject = new JSONObject(hashMapArrayList.get(pos).get("metadata"));

                    if (jsonObject.has("lat")) {
                        jsonObject.put("updated_record_date", "" + TodayDate);
                        jsonObject.put("added_on", "" + updated_added_on);
                    }

                    String update_record = "UPDATE MPREGNANCY SET " +
                            "lmp='" + et_akhiri_haiz_ki_tareekh.getText().toString() + "'," +
                            "edd='" + et_mutawaqqa_zichgi_ki_tareekh.getText().toString() + "'," +
                            "metadata='" + jsonObject.toString() + "'," +
                            "is_synced='" + 0 + "'" +
                            "WHERE member_uid = '" + mother_uid + "' AND pregnancy_id='" + mData[pos][4] + "'AND added_on='" + mData[pos][3] + "'";
                    ls.executeNonQuery(update_record);

                    Boolean res = ls.executeNonQuery(update_record);
                    Log.d("000333", "Pregnancy Data Update: " + update_record);
                    Log.d("000333", "Query: " + res.toString());

                    if (res.toString().equalsIgnoreCase("true")) {

                        final Snackbar snackbar = Snackbar.make(findViewById(R.id.pregnancy_layout), R.string.pregnancyDataEdited, Snackbar.LENGTH_SHORT);
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
                        snackbar.setDuration(3500);
                        snackbar.show();

                        if (Utils.haveNetworkConnection(ctx) > 0) {

                            sendPostRequest(mother_uid, mData[pos][4], mData[pos][2], et_akhiri_haiz_ki_tareekh.getText().toString(), et_mutawaqqa_zichgi_ki_tareekh.getText().toString()
                                    , String.valueOf(jsonObject), login_useruid, mData[pos][3]);
                        } else {
                            //Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();

                                Intent intent = new Intent(ctx, Mother_HaamlaRecordList_Activity.class);
                                intent.putExtra("u_id", mother_uid);
                                intent.putExtra("mother_name", mother_name);
                                intent.putExtra("mother_age", mother_age);
                                startActivity(intent);
                            }
                        }, 2000);
                    }

                    else {
                        final Snackbar snackbar = Snackbar.make(findViewById(R.id.pregnancy_layout), R.string.pregnancyDataNotEdited, Snackbar.LENGTH_SHORT);
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
                        snackbar.setDuration(3000);
                        snackbar.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 2000);

                    }


                } catch (Exception e) {
                    dialog.dismiss();
                    Log.d("000555", "Err: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @SuppressLint("SetTextI18n")
    private void Dialog_Delete_PregnancyRecord(final int position) {

        final Dialog dialog = new Dialog(ctx);
        LayoutInflater layout = LayoutInflater.from(ctx);
        View dialogView = layout.inflate(R.layout.dialog_delete_yes_no_layout, null);

        dialog.setContentView(dialogView);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        Log.d("000102", "d" + "کیا آپ اس " + "06-02-2020" + " حمل کو ڈیلیٹ کرنا چاہتے ہے؟ ");

        TextView txtdelete = dialogView.findViewById(R.id.txtDelete);
        txtdelete.setText(getString(R.string.doYou) + mData[position][2] + getString(R.string.datePregnancyDel));

        TextView tvYes = dialogView.findViewById(R.id.tvYes);
        TextView tvNo = dialogView.findViewById(R.id.tvNo);

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                try {
                    alertDialog = new Dialog(ctx);
                    LayoutInflater layout = LayoutInflater.from(ctx);
                    final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

                    alertDialog.setContentView(dialogView);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    alertDialog.show();


                    ls = new Lister(ctx);
                    ls.createAndOpenDB();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Update_MANC(position);
                        }
                    }, 2000);

                } catch (Exception e) {
                    alertDialog.dismiss();
                    Log.d("000333", "Delete Pregnancy Update ERROR: " + e.getMessage());
                    Toast.makeText(ctx, getString(R.string.delErr) + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void Update_MANC(final int position) {

        try {
            String update_MANC = "UPDATE MANC SET " +
                    "is_synced='" + String.valueOf(-1) + "' " +
                    "WHERE member_uid = '" + mother_uid + "'AND pregnancy_id= '" + mData[position][4] + "'";

            ls.executeNonQuery(update_MANC);

            Boolean res = ls.executeNonQuery(update_MANC);
            Log.d("000333", "MANC Data Update: " + update_MANC);
            Log.d("000333", "Query: " + res.toString());

            if (res.toString().equalsIgnoreCase("true"))
            {
                Log.d("000333", "ALL MANC UPDATED !!!!!!!!!! ");
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("000333", "MDELIVERY START: ");
                        Update_MDELIV(position);
                    }
                }, 2000);
            }
            else {
                Toast.makeText(ctx, wrongUpdatedANC, Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                Log.d("000333", "MANC ELSE ********");
            }


        } catch (Exception e) {
            alertDialog.dismiss();
            Log.d("000333", "Delete MANC data ERROR: " + e.getMessage());
            Toast.makeText(ctx, getString(R.string.ancError)+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void Update_MDELIV(final int position) {

        try {
            String update_MDELIV = "UPDATE MDELIV SET " +
                    "is_synced='" + String.valueOf(-1) + "' " +
                    "WHERE member_uid = '" + mother_uid + "'AND pregnancy_id= '" + mData[position][4] + "'";

            ls.executeNonQuery(update_MDELIV);

            Boolean res = ls.executeNonQuery(update_MDELIV);
            Log.d("000333", "MDELIV Data Update: " + update_MDELIV);
            Log.d("000333", "Query: " + res.toString());

            if (res.toString().equalsIgnoreCase("true"))
            {
                Log.d("000333", "ALL MDELIVERY UPDATED !!!!!!!!!! ");
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("000333", "MPNC START: ");
                        Update_MPNC(position);
                    }
                }, 2000);
            }
            else {
                Toast.makeText(ctx, wrongUpdatedDelivery, Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                Log.d("000333", "Delivery ELSE ********");
            }


        } catch (Exception e) {
            alertDialog.dismiss();
            Log.d("000333", "Delete update_MDELIV data ERROR: " + e.getMessage());
            Toast.makeText(ctx, getString(R.string.delivErro)+e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    private void Update_MPNC(final int position) {

        try {
            String Update_MPNC = "UPDATE MPNC SET " +
                    "is_synced='" + String.valueOf(-1) + "' " +
                    "WHERE member_uid = '" + mother_uid + "'AND pregnancy_id= '" + mData[position][4] + "'";

            ls.executeNonQuery(Update_MPNC);

            Boolean res = ls.executeNonQuery(Update_MPNC);
            Log.d("000333", "MPNC Data Update: " + Update_MPNC);
            Log.d("000333", "Query: " + res.toString());

            if (res.toString().equalsIgnoreCase("true"))
            {
                Log.d("000333", "ALL MPNC UPDATED !!!!!!!!!! ");
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("000333", "MPREGNANCY START: ");
                        Update_MPREGNANCY(position);
                    }
                }, 2000);
            }
            else {
                Toast.makeText(ctx, R.string.wrongUpdatedPNC, Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                Log.d("000333", "MPNC ELSE ********");
            }

        } catch (Exception e) {
            alertDialog.dismiss();
            Log.d("000333", "Delete MPNC data ERROR: " + e.getMessage());
            Toast.makeText(ctx, getString(R.string.errorPNC)+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void Update_MPREGNANCY(final int position) {

        try {

            final String updated_added_on = String.valueOf(System.currentTimeMillis());

            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            String TodayDate = dates.format(c.getTime());


            jsonObject = new JSONObject(hashMapArrayList.get(position).get("metadata"));

            if (jsonObject.has("lat")) {
                jsonObject.put("status", "" + "-1");
                jsonObject.put("mpregnancy_status", "" + "Deleted");
                jsonObject.put("updated_record_date", "" + TodayDate);
                jsonObject.put("added_on", "" + updated_added_on);
            }


            String Update_MPREGNANCY = "UPDATE MPREGNANCY SET " +
                    "metadata='" + jsonObject.toString() + "'," +
                    "status='" + -1 + "'," +
                    "is_synced='" + String.valueOf(-1) + "' " +
                    "WHERE member_uid = '" + mother_uid + "'AND pregnancy_id= '" + mData[position][4] + "'AND added_on= '" + mData[position][3] + "'";

            ls.executeNonQuery(Update_MPREGNANCY);

            Boolean res = ls.executeNonQuery(Update_MPREGNANCY);
            Log.d("000333", "MPREGNANCY Data Update: " + Update_MPREGNANCY);
            Log.d("000333", "Query: " + res.toString());

            if (res.toString().equalsIgnoreCase("true")) {
                Log.d("000333", "PREGNANCY DELETED SUCCESSFULLY !!!!!!!!!!!!!!!!!!!!!!: ");

                final Snackbar snackbar = Snackbar.make(findViewById(R.id.pregnancy_layout), R.string.pregDel, Snackbar.LENGTH_SHORT);
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
                snackbar.setDuration(3500);
                snackbar.show();

                if (Utils.haveNetworkConnection(ctx) > 0) {
                    sendPostRequest_DeletePreg(mother_uid, mData[position][4], mData[position][2], hashMapArrayList.get(position).get("haaml_txt").split(",")[0], hashMapArrayList.get(position).get("haaml_txt").split(",")[1],
                             String.valueOf(jsonObject), login_useruid, mData[position][3]);
                } else {
                    //Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();
                }


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hashMapArrayList.remove(position);
                        adt.notifyDataSetChanged();
                        alertDialog.dismiss();
                    }
                }, 2000);


            }
            else {
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.pregnancy_layout), R.string.pregNotDel, Snackbar.LENGTH_SHORT);
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
                snackbar.setDuration(3000);
                snackbar.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                        Toast.makeText(ctx, wrongUpdatedPreg, Toast.LENGTH_SHORT).show();
                        Log.d("000333", "MPregnancy ELSE ********");
                    }
                }, 1000);
            }

        } catch (Exception e) {
            alertDialog.dismiss();
            Log.d("000333", "Delete MPREGNANCY data ERROR: " + e.getMessage());
            Toast.makeText(ctx, getString(R.string.pregErr)+e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        try {

            hashMapArrayList.clear();
            Lister ls = new Lister(Mother_HaamlaRecordList_Activity.this);
            ls.createAndOpenDB();

           /* SharedPreferences settings = getSharedPreferences("Khandanuuid", MODE_PRIVATE);
            // Reading from SharedPreferences
            String khandan_uuid_sp = settings.getString("k_uuid", "");
            //  Toast.makeText(getApplicationContext(),khandan_uuid_sp,Toast.LENGTH_SHORT).show();*/

            try {
                //String[][] data = ls.executeReader("Select* from KHANDAN ");
                mData = ls.executeReader("Select lmp,edd,record_data,added_on,pregnancy_id,metadata from MPREGNANCY where member_uid = '" + mother_uid + "' AND is_synced NOT IN ('-1') ORDER BY added_on DESC");

                Log.d("mother_data", String.valueOf(mData.length));
            } catch (Exception e) {
                Log.d("mother_data", String.valueOf(e.getMessage()));
            }

            HashMap<String, String> map;
            for (int i = 0; i < mData.length; i++) {

                map = new HashMap<>();
                map.put("haaml_txt", "" + mData[i][0] + "," + mData[i][1] + "," + mData[i][2]);
                 map.put("metadata", "" +mData[i][5]);
                //  map.put("mother_name", "" +"کرن اقبال");

                if (mData[i][3] == null) {
                    map.put("time", "" + "00:50");
                } else {
                    Date date = new Date(Long.parseLong(mData[i][3]));
                    SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
                    String formatted = format.format(date);
                    Log.d("000202", "datetime: " + formatted);

                    map.put("time", "" + formatted);
                }

                hashMapArrayList.add(map);
            }

            adt.notifyDataSetChanged();


        } catch (Exception e) {
            Log.d("12345", "Error: " + e.getMessage());
            //Toast.makeText(ctx, "کوئی ریکارڈ نہیں", Toast.LENGTH_SHORT).show();
            Toast tt = Toast.makeText(ctx, "حمل کا کوئی ریکارڈ نہیں", Toast.LENGTH_SHORT);
            tt.setGravity(Gravity.CENTER, 0, 0);
            tt.show();
        }

    }


    private void sendPostRequest(final String member_uid, final String pregnancy_id, final String record_data, final String lmp, final String edd,
                                 final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/mother/pregnancy";

        Log.d("000555", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {
                    // Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000555", "Response    " + response);


                        Lister ls = new Lister(Mother_HaamlaRecordList_Activity.this);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE MPREGNANCY SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + mother_uid + "'AND pregnancy_id= '" + pregnancy_id + "'AND added_on= '" + added_on + "'";
                        ls.executeNonQuery(update_record);
                        Toast tt = Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();

                        //  Toast.makeText(Mother_HaamlaRecordList_Activity.this, "Data has been saved", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000555", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        //  Toast.makeText(Mother_HaamlaRecordList_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", "Err    " + e.getMessage());
                    //   Toast.makeText(Mother_HaamlaRecordList_Activity.this, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();
                    Toast tt = Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT);
                    tt.setGravity(Gravity.CENTER, 0, 0);
                    tt.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "onErrorResponse: " + error.getMessage());
                //Toast.makeText(Mother_HaamlaRecordList_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast tt = Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT);
                tt.setGravity(Gravity.CENTER, 0, 0);
                tt.show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("member_id", member_uid);
                params.put("pregnancy_id", pregnancy_id);
                params.put("record_data", record_data);
                params.put("data", data);
                params.put("lmp", lmp);
                params.put("edd", edd);
                params.put("status", "1");
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


    private void sendPostRequest_DeletePreg(final String member_uid, final String pregnancy_id, final String record_data, final String lmp, final String edd,
                                 final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/mother/pregnancy";

        Log.d("000555", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {
                    // Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000555", "Deleted Response    " + response);


                        Lister ls = new Lister(Mother_HaamlaRecordList_Activity.this);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE MPREGNANCY SET " +
                                "is_synced='" + String.valueOf(-1) + "' " +
                                "WHERE member_uid = '" + mother_uid + "'AND pregnancy_id= '" + pregnancy_id + "'AND added_on= '" + added_on + "'";
                        ls.executeNonQuery(update_record);
                        Toast tt = Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();

                        //  Toast.makeText(Mother_HaamlaRecordList_Activity.this, "Data has been saved", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000555", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                        //  Toast.makeText(Mother_HaamlaRecordList_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", "Err    " + e.getMessage());
                    //   Toast.makeText(Mother_HaamlaRecordList_Activity.this, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();
                    Toast tt = Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT);
                    tt.setGravity(Gravity.CENTER, 0, 0);
                    tt.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "onErrorResponse: " + error.getMessage());
                //Toast.makeText(Mother_HaamlaRecordList_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast tt = Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT);
                tt.setGravity(Gravity.CENTER, 0, 0);
                tt.show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("member_id", member_uid);
                params.put("pregnancy_id", pregnancy_id);
                params.put("record_data", record_data);
                params.put("data", data);
                params.put("lmp", lmp);
                params.put("edd", edd);
                params.put("status", "1");
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

    public void ShowDateDialougeUpdated() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(Mother_HaamlaRecordList_Activity.this, R.style.DatePickerDialog,
                new DatePickerDialog.OnDateSetListener() {

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
                        et_akhiri_haiz_ki_tareekh.setText(yearf2 + "-" + monthf2 + "-" + dayf2);

				/*	if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
						age--;
					}
*/
                        try {
                            Calendar c = new GregorianCalendar();
                            java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            c.setTime(dateFormat.parse(et_akhiri_haiz_ki_tareekh.getText().toString()));
                            c.add(Calendar.DATE, 280);
                            Date d = c.getTime();

                            dateFormat.format(d.getTime());

                            et_mutawaqqa_zichgi_ki_tareekh.setText(dateFormat.format(d.getTime()));


                        } catch (Exception e) {

                        }
                        //Toast.makeText(getContext(),DateTwoOneval,Toast.LENGTH_LONG).show();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            if (var_add_preg.equalsIgnoreCase("1")) {
                Log.d("000987", " IF @@@@ ");
                var_add_preg = "1";
                finish();
            } else {
                Log.d("000987", " ELSE ****** ");
                finish();
                var_add_preg = "0";
            }
        } catch (Exception e) {

        }
    }
}
