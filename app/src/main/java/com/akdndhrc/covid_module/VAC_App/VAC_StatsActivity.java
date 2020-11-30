package com.akdndhrc.covid_module.VAC_App;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.Adt_VAC_StatsExpandableList;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.akdndhrc.covid_module.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class VAC_StatsActivity extends AppCompatActivity {

    Context ctx = VAC_StatsActivity.this;
    ExpandableListView expListView;

    Adt_VAC_StatsExpandableList adt_statsExpandableList;
    ArrayList<String> mArrayListChild = new ArrayList<>();
    HashMap<String, ArrayList<String>> mArrayList_child = new HashMap<String, ArrayList<String>>();
    List<String> mArrayList_parent = new ArrayList<String>();

    EditText et_is_tareekh_tk, et_is_tareekh_sy;
    private int mYear, mMonth, mDay, mYear2, mMonth2, mDay2;
    int date_for_condition = 0;
    int date_for_condition_2 = 0;
    int month_for_condition = 0;
    int month_for_condition_2 = 0;
    public String hold_age_date_condition = "fromage";
    public String hold_age_date_condition_2 = "fromage";
    String monthf, dayf, yearf = "null";
    String monthf2, dayf2, yearf2 = "null";
    boolean isOkayClicked = false;
    String temp = "0", TodayDate;
    ProgressBar pbProgress;
    ImageView iv_home, iv_navigation_drawer;
    ListView lv;
    SimpleAdapter simpleAdapter;
    ArrayList<HashMap<String, String>> hashmap_simplelist = new ArrayList<HashMap<String, String>>();
    View lv_view;
    ScrollView activity_scroll_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vac_stats);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, VAC_StatsActivity.class));


        //ListView
        lv = (ListView) findViewById(R.id.lv);
        expListView = (ExpandableListView) findViewById(R.id.exp_list);


        //LineView
        lv_view = findViewById(R.id.lv_view);

        activity_scroll_view = findViewById(R.id.activity_scroll_view);





        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_home.setVisibility(View.GONE);

        pbProgress = findViewById(R.id.pbProgress);
        pbProgress.setVisibility(View.VISIBLE);

        //Edittext
        et_is_tareekh_sy = findViewById(R.id.et_is_tareekh_sy);
        et_is_tareekh_tk = findViewById(R.id.et_is_tareekh_tk);
        et_is_tareekh_sy.setText(TodayDate);
        et_is_tareekh_tk.setText(TodayDate);

        et_is_tareekh_sy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDateDialougeIsTareekhSy();

                /*if (temp.equalsIgnoreCase("0")) {
                    ShowDateDialougeIsTareekhSy();
                } else {
                    et_is_tareekh_sy.getText().clear();

                }*/
            }
        });
        et_is_tareekh_tk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowDateDialougeIsTareekhTk();

            }
        });
        // preparing list data

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pbProgress.setVisibility(View.GONE);
                lv_view.setVisibility(View.GONE);
               // prepareListData();

            }
        }, 1500);


//        activity_scroll_view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.v("000987","ABC");
//                v.getParent().requestDisallowInterceptTouchEvent(false);
//                return false;
//            }
//        });

    }



 /*   @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        if(ev.getAction()==MotionEvent.ACTION_MOVE)
            return true;
        return super.dispatchTouchEvent(ev);
    }
*/
    private void prepareListData() {


        if (et_is_tareekh_sy.getText().toString().isEmpty() || et_is_tareekh_tk.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please select date range.",Toast.LENGTH_LONG).show();
            return;
        }
        else{

        }

        mArrayList_parent.clear();
        hashmap_simplelist.clear();

        //////////////// Total Member Registration////////////////////////////////////
        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String[][] total_member = ls.executeReader("Select count(*) from MEMBER where date(added_on/1000, 'unixepoch', 'localtime') BETWEEN '" + et_is_tareekh_sy.getText().toString() + "' AND'" + et_is_tareekh_tk.getText().toString() + "'");
            if (total_member != null) {
                Log.d("000111", "Total MEMBER REGSITER: " + total_member[0][0]);

                HashMap<String, String> map1 = new HashMap<>();
                map1.put("total", "" + total_member[0][0]);
                map1.put("name", "" + "Total Registrations");
                hashmap_simplelist.add(map1);
            } else {
                Log.d("000111", "ELSE TOTAL MEMBER");
            }

        } catch (Exception e) {
            Log.d("000555", "Error Total Member Catch: " + e.getMessage());
        }

        //////////////// Total Vaccination////////////////////////////////////
        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String[][] total_member = ls.executeReader("Select count(*) from CVACCINATION where date(added_on/1000, 'unixepoch', 'localtime') BETWEEN '" + et_is_tareekh_sy.getText().toString() + "' AND'" + et_is_tareekh_tk.getText().toString() + "'");
            if (total_member != null) {
                Log.d("000111", "Total CVACCINATION: " + total_member[0][0]);

                HashMap<String, String> map2 = new HashMap<>();
                map2.put("total", "" + total_member[0][0]);
                map2.put("name", "" + "Total Registrations");
                hashmap_simplelist.add(map2);
            } else {
                Log.d("000111", "ELSE TOTAL CVACCINATION");
            }

        } catch (Exception e) {
            Log.d("000555", "Error Total CVACCINATION Catch: " + e.getMessage());
        }

        simpleAdapter = new SimpleAdapter(this, hashmap_simplelist,
                R.layout.custom_stats_parent_list,
                new String[]{"total", "name"},
                new int[]{R.id.tv_count, R.id.tvName});

        lv.setAdapter(simpleAdapter);
        simpleAdapter.notifyDataSetChanged();

    /*    /////////////////////////// Total Child < 2 ///////////////////////////////////////////////////

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String[][] total_childlessthanTwo = ls.executeReader("Select count(*) from MEMBER where age < 2 AND datetime(added_on/1000, 'unixepoch', 'localtime') BETWEEN '" + et_is_tareekh_sy.getText().toString() + "' AND'" + et_is_tareekh_tk.getText().toString() + "'");
            if (total_childlessthanTwo != null) {
                Log.d("000111", "Total CHILD LESS THAN TWO REGSITER: " + total_childlessthanTwo[0][0]);

                HashMap<String, String> map2 = new HashMap<>();
                map2.put("total", "" + total_childlessthanTwo[0][0]);
                map2.put("name", "" + "2 سال سے کم عمر کے بچے");
                hashmap_simplelist.add(map2);

            } else {
                Log.d("000111", "ELSE TOTAL CHILD LESS THAN TWO");
            }

        } catch (Exception e) {
            Log.d("000555", "Error CHILD LESS THAN TWO Catch: " + e.getMessage());
        }

        /////////////////////////// Total Child >2  AND <15 ///////////////////////////////////////////////////

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String[][] total_child2to15 = ls.executeReader("Select count(*) from MEMBER where age > 2 AND age < 15 AND datetime(added_on/1000, 'unixepoch', 'localtime') BETWEEN '" + et_is_tareekh_sy.getText().toString() + "' AND'" + et_is_tareekh_tk.getText().toString() + "'");
            if (total_child2to15 != null) {
                Log.d("000111", "Total CHILD 2 to 15 REGSITER: " + total_child2to15[0][0]);
                HashMap<String, String> map3 = new HashMap<>();
                map3.put("total", "" + total_child2to15[0][0]);
                map3.put("name", "" + "2 سال سے زیادہ اور 15 سال سے کم عمر کے بچے");
                hashmap_simplelist.add(map3);
            } else {
                Log.d("000111", "ELSE Total CHILD 2 to 15 REGSITER");
            }
        } catch (Exception e) {
            Log.d("000555", "Error Total CHILD 2 to 15 REGSITER Catch :" + e.getMessage());
        }


        /////////////////////////// Total MEMBER >15  AND <49 ///////////////////////////////////////////////////

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String[][] total_member15to49 = ls.executeReader("Select count(*) from MEMBER where age > 15 AND age < 49 AND datetime(added_on/1000, 'unixepoch', 'localtime') BETWEEN '" + et_is_tareekh_sy.getText().toString() + "' AND'" + et_is_tareekh_tk.getText().toString() + "'");
            if (total_member15to49 != null) {
                Log.d("000111", "Total MEMBER 15 to 49 REGSITER: " + total_member15to49[0][0]);
                HashMap<String, String> map4 = new HashMap<>();
                map4.put("total", "" + total_member15to49[0][0]);
                map4.put("name", "" + "15 سال سے زیادہ اور 49 سال سے کم عمر کے افراد");
                hashmap_simplelist.add(map4);
            } else {
                Log.d("000111", "ELSE Total MEMBER 15 to 49 REGSITER");
            }
        } catch (Exception e) {
            Log.d("000555", "Error Total MEMBER 15 to 49 REGSITER Catch :" + e.getMessage());
        }

        /////////////////////////// Total MEMBER >49 ///////////////////////////////////////////////////
        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String[][] total_member49 = ls.executeReader("Select count(*) from MEMBER where age > 49 AND datetime(added_on/1000, 'unixepoch', 'localtime') BETWEEN '" + et_is_tareekh_sy.getText().toString() + "' AND'" + et_is_tareekh_tk.getText().toString() + "'");
            if (total_member49 != null) {
                Log.d("000111", "Total MEMBER 49 REGSITER: " + total_member49[0][0]);
                HashMap<String, String> map5 = new HashMap<>();
                map5.put("total", "" + total_member49[0][0]);
                map5.put("name", "" + "49 سال سے زیادہ عمر کے افراد");
                hashmap_simplelist.add(map5);
            } else {
                Log.d("000111", "ELSE Total MEMBER 49 REGSITER");
            }
        } catch (Exception e) {
            Log.d("000555", "Error Total MEMBER 49 REGSITER Catch :" + e.getMessage());
        }


*/
        try {
            // Adding child data

            Lister ls = new Lister(VAC_StatsActivity.this);
            ls.createAndOpenDB();


            //Log.d("QTIME", "START0 " + System.currentTimeMillis());
            String[][] mData_Vaccines = ls.executeReader("Select uid,name from VACCINES");
         //   Log.d("QTIME", "END0 " + System.currentTimeMillis());

            Log.d("000258", "mArrayListChild SIZE: " + mArrayList_child.size());

            for (int i = 0; i < mData_Vaccines.length; i++) {
                Log.d("000111", "" + mData_Vaccines[i][0] + " - " + mData_Vaccines[i][1]);

                String[][] mData_query = ls.executeReader("SELECT" +
                        " coalesce( SUM(CASE WHEN inside_uc = 1 THEN 1 ELSE 0 END), 0) inside_uc," +
                        " coalesce( SUM(CASE WHEN inside_uc = 0 THEN 1 ELSE 0 END), 0) outside_uc" +
                        " FROM" +
                        " (" +
                        " Select " +
                        " (CASE WHEN t2.uc_id = t3.uc_id THEN 1 ELSE 0 END) inside_uc" +
                        " from CVACCINATION t0" +
                        " INNER JOIN MEMBER t1 ON t0.member_uid = t1.uid" +
                        " INNER JOIN KHANDAN t2 ON t1.khandan_id = t2.uid" +
                        " INNER JOIN USERS t3 ON t0.added_by = t3.uid" +
                        " where t0.vaccine_id = '" + mData_Vaccines[i][0] + "' AND date(t0.record_data) BETWEEN '" + et_is_tareekh_sy.getText().toString() + "' AND'" + et_is_tareekh_tk.getText().toString() + "'" +
                        " ) x");
                // "where t0.added_by='"+ Login_Activity.user_uuid+"' AND vaccine_id = '"+mData_Vaccines[i][0]+"' AND date(t0.record_data) BETWEEN \"2019-08-23\" AND \"2019-08-25\"" +
                mArrayList_parent.add(mData_Vaccines[i][0] + "@" + mData_Vaccines[i][1] + "@" + mData_query[0][0] + "@" + mData_query[0][1]);
                Log.d("000111", "" + mData_query[0][0] + " - " + mData_query[0][1]);
                Log.d("000258", "mArrayList_parent SIZE: " + mArrayList_parent.size());
                for (int c = 0; c < mArrayList_parent.size(); c++) {
                    mArrayListChild = new ArrayList<>();

                    Log.d("000111", "START1 " + System.currentTimeMillis());
                    /*String[][] mData_child_name = ls.executeReader("Select t2.full_name from CVACCINATION t1" +
                            " INNER JOIN MEMBER t2 ON t1.member_uid = t2.uid" +
                            " where  t1.vaccine_id ='" + mData_Vaccines[i][0] + "'AND date(t1.record_data) BETWEEN '" + et_is_tareekh_sy.getText().toString() + "'AND'" + et_is_tareekh_tk.getText().toString() + "'");*/

                    String[][] mData_child_name = ls.executeReader("SELECT" +
                            " (CASE WHEN t2.uc_id = t3.uc_id THEN 1 ELSE 0 END) inside_outside,t1.full_name" +
                            " from CVACCINATION t0" +
                            " INNER JOIN MEMBER t1 ON t0.member_uid = t1.uid" +
                            " INNER JOIN KHANDAN t2 ON t1.khandan_id = t2.uid" +
                            " INNER JOIN USERS t3 ON t0.added_by = t3.uid" +
                            " where t0.vaccine_id = '" + mData_Vaccines[i][0] + "' AND date(t0.record_data) BETWEEN '" + et_is_tareekh_sy.getText().toString() + "' AND'" + et_is_tareekh_tk.getText().toString() + "'");

                    Log.d("QTIME", "END1 " + System.currentTimeMillis());

                    if (mData_child_name != null) {
                        for (int a = 0; a < mData_child_name.length; a++) {
                            Log.d("000111", " full_name: " + mData_child_name[a][0] + " - " + mData_child_name[a][1]);
                            mArrayListChild.add(mData_child_name[a][0] + "@" + mData_child_name[a][1]);
                        }
                    } else {
                        //mArrayListChild.add("No data");
                    }
                    mArrayList_child.put(mArrayList_parent.get(i), mArrayListChild);
                    // childList.add(mArrayListChild);
                    Log.d("000111", "mArrayListChild SIZE: " + mArrayListChild.size());
                }
            }


            ls.closeDB();
            adt_statsExpandableList = new Adt_VAC_StatsExpandableList(ctx, mArrayList_parent, mArrayList_child);
            // setting list adapter
            adt_statsExpandableList.notifyDataSetChanged();
            expListView.setAdapter(adt_statsExpandableList);

            expListView.setClickable(true);


        } catch (Exception e) {

            Log.d("000111", "Error: " + e.getMessage());

        }
    }


    public void ShowDateDialougeIsTareekhSy() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(VAC_StatsActivity.this, R.style.DatePickerDialog,
                new DatePickerDialog.OnDateSetListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        if (monthOfYear <= 8) {

                            monthf = "0" + String.valueOf(monthOfYear + 1);

                        } else {
                            monthf = String.valueOf(monthOfYear + 1);
                        }
                        if (dayOfMonth <= 9) {

                            dayf = "0" + String.valueOf(dayOfMonth);
                        } else {
                            dayf = String.valueOf(dayOfMonth);
                        }
                        yearf = String.valueOf(year);
                        //datetwo.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        // DateTwoOne= "newvaladded";
                        //et_dob.setText(dayf2 + "-" + monthf2 + "-" + yearf2);
                        et_is_tareekh_sy.setText(yearf + "-" + monthf + "-" + dayf);

                        temp = "1";
                        Log.d("000555", "onDateSet: " + temp);
                        Log.d("000555", "IS Tareekh SY: " + et_is_tareekh_sy.getText().toString());

                        pbProgress.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pbProgress.setVisibility(View.GONE);
                              //  prepareListData();

                            }
                        }, 2000);

                        date_for_condition = Integer.parseInt(dayf);
                        month_for_condition = Integer.parseInt(monthf);

                        hold_age_date_condition = "fromdate";


                        Calendar dob = Calendar.getInstance();
                        Calendar today = Calendar.getInstance();

                        dob.set(year, monthOfYear, dayOfMonth);

                        int age = today.get(Calendar.YEAR) - year;

                        Integer ageInt = new Integer(age);
                        String ageS = ageInt.toString();
                        //Toast.makeText(getApplicationContext(),String.valueOf(year)+"major" + ageS +"age",Toast.LENGTH_SHORT).show();
                        // et_umer.setText(ageS);
                        //Toast.makeText(getContext(),DateTwoOneval,Toast.LENGTH_LONG).show();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();


    }

    public void ShowDateDialougeIsTareekhTk() {


        final Calendar c = Calendar.getInstance();
        mYear2 = c.get(Calendar.YEAR);
        mMonth2 = c.get(Calendar.MONTH);
        mDay2 = c.get(Calendar.DAY_OF_MONTH);


        final DatePickerDialog datePickerDialog = new DatePickerDialog(VAC_StatsActivity.this, R.style.DatePickerDialog,
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
                        //datetwo.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        // DateTwoOne= "newvaladded";
                        //et_dob.setText(dayf2 + "-" + monthf2 + "-" + yearf2);

                        et_is_tareekh_tk.setText(yearf2 + "-" + monthf2 + "-" + dayf2);
                        Log.d("000555", "IS Tareekh TK: " + et_is_tareekh_tk.getText().toString());

                        pbProgress.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pbProgress.setVisibility(View.GONE);
                               // prepareListData();

                            }
                        }, 2000);

                        date_for_condition_2 = Integer.parseInt(dayf2);
                        month_for_condition_2 = Integer.parseInt(monthf2);

                        hold_age_date_condition_2 = "fromdate";


                        Calendar dob = Calendar.getInstance();
                        Calendar today = Calendar.getInstance();

                        dob.set(year, monthOfYear, dayOfMonth);

                        int age = today.get(Calendar.YEAR) - year;

                        Integer ageInt = new Integer(age);
                        String ageS = ageInt.toString();
                        //Toast.makeText(getApplicationContext(),String.valueOf(year)+"major" + ageS +"age",Toast.LENGTH_SHORT).show();
                        // et_umer.setText(ageS);
                        //Toast.makeText(getContext(),DateTwoOneval,Toast.LENGTH_LONG).show();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date mDate = sdf.parse(et_is_tareekh_sy.getText().toString() + "");
            long timeInMilliseconds = mDate.getTime();
            datePickerDialog.getDatePicker().setMinDate(timeInMilliseconds);

        } catch (ParseException e) {
            // Toast.makeText(ctx, "Failed", Toast.LENGTH_SHORT).show();
            Log.d("000555", "DF" + e.getMessage());
            e.printStackTrace();

        }

        datePickerDialog.show();


    }


}
