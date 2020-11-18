package com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildBemaariRecordActivities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.Adt_ChildDashboard.Adt_ChildBemaariRecordList;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Child_BemaariRecordList_Activity extends AppCompatActivity {

    Context ctx = Child_BemaariRecordList_Activity.this;
    TextView txt_age, txt_naam,tv_record;
    ListView lv;
    EditText et_is_tareekh_tk, et_is_tareekh_sy;
    Button btn_naya_form_shamil_kre;
    RelativeLayout rl_navigation_drawer, rl_home;
    ImageView iv_navigation_drawer, iv_home, image_gender;

    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    Adt_ChildBemaariRecordList adt;

    String child_uid, child_age, child_name, child_gender;
    String[][] mData;

    private int mYear, mMonth, mDay;
    int date_for_condition = 0;
    int month_for_condition = 0;
    public String hold_age_date_condition = "fromage";
    String monthf2, dayf2, yearf2 = "null",TodayDate;
    ProgressBar pbProgress;
    String temp = "0";


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_bemaari_record_list);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Child_BemaariRecordList_Activity.class));

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());


        child_uid = getIntent().getExtras().getString("u_id");
        child_name = getIntent().getExtras().getString("child_name");
        child_age = getIntent().getExtras().getString("child_age");
        child_gender = getIntent().getExtras().getString("child_gender");

        pbProgress = findViewById(R.id.pbProgress);
        pbProgress.setVisibility(View.VISIBLE);

        //ListView
        lv = findViewById(R.id.lv);

        //TextView
        txt_age = findViewById(R.id.txt_age);
        txt_naam = findViewById(R.id.txt_naam);
        tv_record = findViewById(R.id.tv_record);

        txt_naam.setText(child_name);
        txt_age.setText(child_age);

        //Edittext
        et_is_tareekh_sy = findViewById(R.id.et_is_tareekh_sy);
        et_is_tareekh_tk = findViewById(R.id.et_is_tareekh_tk);
        et_is_tareekh_sy.setText(TodayDate);
        et_is_tareekh_tk.setText(TodayDate);

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        //iv_home.setVisibility(View.GONE);
        image_gender = findViewById(R.id.image_gender);

        if (child_gender.equalsIgnoreCase("0")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image_gender.setImageTintList(ctx.getResources().getColorStateList(R.color.pink_color));
            }
        }

        //Button
        btn_naya_form_shamil_kre = findViewById(R.id.btn_naya_form_shamil_kre);

        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Navigation", Toast.LENGTH_SHORT).show();
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

        et_is_tareekh_sy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (temp.equalsIgnoreCase("0")) {
                    ShowDateDialougeIsTareekhSy();
                } else {
                    et_is_tareekh_sy.getText().clear();
                    ShowDateDialougeIsTareekhSy();
                }
            }
        });
        et_is_tareekh_tk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowDateDialougeIsTareekhTk();

            }
        });


        btn_naya_form_shamil_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Lister ls = new Lister(Child_BemaariRecordList_Activity.this);
                    ls.createAndOpenDB();


                    String[][] mData = ls.executeReader("Select record_data,count(*), max(added_on) from CBEMARI where member_uid = '" + child_uid + "' AND JSON_EXTRACT(data, '$.type') IS NOT 'CVirus'");

                    if (Integer.parseInt(mData[0][1]) > 0) {
                        Log.d("000985", "Record Date: " + mData[0][0]);

                        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar c = Calendar.getInstance();
                        String TodayDate = dates.format(c.getTime());
                        String DateFrom = mData[0][0];
                        Date date1;
                        Date date2;
                        date1 = dates.parse(TodayDate);
                        date2 = dates.parse(DateFrom);
                        long difference = Math.abs(date1.getTime() - date2.getTime());
                        long differenceDates = (difference / (24 * 60 * 60 * 1000));
                        String dayDifference = Long.toString(differenceDates);

                        Log.e("000985", "TodayDate: " + TodayDate);
                        Log.e("000985", "Register Date: " + DateFrom);
                        Log.e("000985", "Days_Differernce: " + dayDifference);

                        if (Integer.parseInt(dayDifference) >= 1 || Integer.parseInt(dayDifference) > 1) {
                            Log.d("000985", "IF:");
                            Intent intent = new Intent(ctx, Child_BemaariRecordForm_Activity.class);
                            intent.putExtra("u_id", child_uid);
                            startActivity(intent);

                        } else {
                            Log.d("000985", "IELSEEEEF:");

                            final Snackbar snackbar = Snackbar.make(v, "آج کی تاریخ کا ریکارڈ موجود ہے. برائے مہربانی کل کی تاریخ کا انتظار کریں.", Snackbar.LENGTH_SHORT);
                            View mySbView = snackbar.getView();
                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                            TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(15);
                            snackbar.setDuration(5000);
                            snackbar.show();

                        }
                    } else {
                        Intent intent = new Intent(ctx, Child_BemaariRecordForm_Activity.class);
                        intent.putExtra("u_id", child_uid);
                        startActivity(intent);
                    }


                } catch (Exception e) {
                    Log.d("000985", "Error: " + e.getMessage());

                }

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newIntent = new Intent(ctx, Child_BemaariRecordFormView_Activity.class);
                newIntent.putExtra("u_id", child_uid);
                newIntent.putExtra("record_date", mData[position][0]);
                newIntent.putExtra("added_on", mData[position][1]);
                startActivity(newIntent);
            }
        });
    }

    private void prepareListData() {
        hashMapArrayList.clear();

        try {

            Lister ls = new Lister(Child_BemaariRecordList_Activity.this);
          ls.createAndOpenDB();
            SharedPreferences settings = getSharedPreferences("Khandanuuid", MODE_PRIVATE);

            // Reading from SharedPreferences
            String khandan_uuid_sp = settings.getString("k_uuid", "");

//            Toast.makeText(getApplicationContext(),khandan_uuid_sp,Toast.LENGTH_SHORT).show();

            try {
                //String[][] data = ls.executeReader("Select* from KHANDAN ");
                mData = ls.executeReader("Select record_data,added_on from CBEMARI where member_uid = '" + child_uid + "'  AND date(record_data) BETWEEN '" + et_is_tareekh_sy.getText().toString() + "' AND'" + et_is_tareekh_tk.getText().toString() + "' AND JSON_EXTRACT(data, '$.type') IS NOT 'CVirus' ORDER BY added_on DESC");
                Log.d("child_data", String.valueOf(mData.length));
            } catch (Exception e) {
                Log.d("child_data", String.valueOf(e.getMessage()));
            }


            HashMap<String, String> map;
            for (int i = 0; i < mData.length; i++) {

                map = new HashMap<>();
                map.put("bemaari_record", "" + mData[i][0]);
                map.put("bemaari_record_date", "" + "");
                //  map.put("mother_name", "" +"کرن اقبال");

                hashMapArrayList.add(map);
            }
            adt = new Adt_ChildBemaariRecordList(ctx, hashMapArrayList);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);


        } catch (Exception e) {
            tv_record.setVisibility(View.VISIBLE);
            Log.d("000555", "Error: " + e.getMessage());
        //    Toast.makeText(ctx, "اس تاریخ کا کوئی ریکارڈ نہیں", Toast.LENGTH_SHORT).show();
        }

    }

    public void ShowDateDialougeIsTareekhSy() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(Child_BemaariRecordList_Activity.this, R.style.DatePickerDialog,
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
                        et_is_tareekh_sy.setText(yearf2 + "-" + monthf2 + "-" + dayf2);

                        temp = "1";
                        Log.d("000555", "onDateSet: " + temp);
                        Log.d("000555", "IS Tareekh SY: " + et_is_tareekh_sy.getText().toString());

                        tv_record.setVisibility(View.GONE);
                        pbProgress.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pbProgress.setVisibility(View.GONE);
                                prepareListData();

                            }
                        }, 2000);


                        date_for_condition = Integer.parseInt(dayf2);
                        month_for_condition = Integer.parseInt(monthf2);

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
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(Child_BemaariRecordList_Activity.this, R.style.DatePickerDialog,
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

                        tv_record.setVisibility(View.GONE);
                        pbProgress.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pbProgress.setVisibility(View.GONE);
                                prepareListData();

                            }
                        }, 2000);


                        date_for_condition = Integer.parseInt(dayf2);
                        month_for_condition = Integer.parseInt(monthf2);

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



    @Override
    protected void onResume() {

        Log.d("000555","ONRESUME CBEMARI");

        tv_record.setVisibility(View.GONE);
        pbProgress.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pbProgress.setVisibility(View.GONE);
                prepareListData();

            }
        }, 1500);

        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //  startActivity(new Intent(ctx, Child_Dashboard_Activity.class));
    }
}
