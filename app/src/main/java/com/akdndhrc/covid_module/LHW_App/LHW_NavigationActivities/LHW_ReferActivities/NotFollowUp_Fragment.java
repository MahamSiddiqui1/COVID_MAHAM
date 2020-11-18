package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_ReferActivities;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.akdndhrc.covid_module.Adapter.Adt_Refer.Adt_NotFollowUpExpandableList;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class NotFollowUp_Fragment extends Fragment {

    NotFollowUp_Fragment ctx = NotFollowUp_Fragment.this;

    ExpandableListView exp_list;

    String[][] mData_CBemari, mData_MBemari, mData_MFPlanning, mData_Referral;
    public static Adt_NotFollowUpExpandableList adt_lhw_referExpandableList;


    EditText et_is_tareekh_tk, et_is_tareekh_sy;

    ImageView iv_navigation_drawer, iv_home;
    TableLayout tableLayout;
    ProgressBar pbProgress;

    public static String TodayDate;

    ArrayList<String> mArrayList_HeaderList = new ArrayList<String>();
    ArrayList<String> mArrayList_CBemari = new ArrayList<String>();
    ArrayList<String> mArrayList_MBemari = new ArrayList<String>();
    ArrayList<String> mArrayList_MFplaning = new ArrayList<String>();
    ArrayList<String> mArrayList_Referral = new ArrayList<String>();


    HashMap<String, ArrayList<String>> mArrayList_Main = new HashMap<String, ArrayList<String>>();


    private int mYear, mMonth, mDay, mYear2, mMonth2, mDay2;
    String monthf, dayf, yearf = "null";
    String monthf2, dayf2, yearf2 = "null";

    public  static  View rootView;


    public NotFollowUp_Fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(getContext(), NotFollowUp_Fragment.class));


        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());


         rootView = inflater.inflate(R.layout.fragment_not_followup, container, false);

        //ProgressBar
        pbProgress = rootView.findViewById(R.id.pbProgress);

        //Expandable ListView
        exp_list = (ExpandableListView) rootView.findViewById(R.id.exp_list);

        //TableLayout
        tableLayout = (TableLayout) rootView.findViewById(R.id.table_layout);


        //Edittext
        et_is_tareekh_sy = rootView.findViewById(R.id.et_is_tareekh_sy);
        et_is_tareekh_tk = rootView.findViewById(R.id.et_is_tareekh_tk);
        et_is_tareekh_sy.setText(TodayDate);
        et_is_tareekh_tk.setText(TodayDate);


        et_is_tareekh_sy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowDateDialougeIsTareekhSy();

            }
        });
        et_is_tareekh_tk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowDateDialougeIsTareekhTk();

            }
        });


        adt_lhw_referExpandableList = new Adt_NotFollowUpExpandableList(getContext(), mArrayList_HeaderList, mArrayList_Main,ctx);
        exp_list.setAdapter(adt_lhw_referExpandableList);
        new Task().execute();


        return rootView;
    }

    public  void prepareListData() {


        mArrayList_HeaderList.clear();
        mArrayList_CBemari.clear();
        mArrayList_MBemari.clear();
        mArrayList_MFplaning.clear();
        mArrayList_Referral.clear();

        if (et_is_tareekh_sy.getText().toString().isEmpty() || et_is_tareekh_tk.getText().toString().isEmpty()) {
            //Toast.makeText(getContext(), "برائے مہربانی تاریخ منتخب کریں", Toast.LENGTH_SHORT).show();
            final Snackbar snackbar = Snackbar.make(getView(), "برائے مہربانی تاریخ منتخب کریں", Snackbar.LENGTH_SHORT);
            View mySbView = snackbar.getView();
            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
            TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(14);
            snackbar.setDuration(3500);
            snackbar.show();
            return;
        }


        try {

            Lister ls = new Lister(getContext());
            ls.createAndOpenDB();


            /////////////////////////////// CBEMARRI Record /////////////////////////////////////////////////
            Log.d("000700", "*************************  Child BEMARI DATA*******************: ");

            try {
                mData_CBemari = ls.executeReader("Select count(*) from CBEMARI where JSON_EXTRACT(data, '$.referal_reason') IS \"\" AND JSON_EXTRACT(data, '$.referal_facility') IS \"\" AND date(record_data) BETWEEN '" + et_is_tareekh_sy.getText().toString() + "' AND '" + et_is_tareekh_tk.getText().toString() + "'");
                if (mData_CBemari != null) {
                    Log.d("000700", "Total CBEMARI Count: " + mData_CBemari[0][0]);
                    mArrayList_HeaderList.add(mData_CBemari[0][0] + "@" + "بچوں کی بیماری کا حوالہ");

                    Log.d("000700", "CBEMARI HEADER LIST Size: " + mArrayList_HeaderList.size());

                    //  for (int c = 0; c < mArrayList_HeaderList.size(); c++) {
                    mArrayList_CBemari = new ArrayList<>();
                    String[][] mData = ls.executeReader("Select t1.full_name,t0.added_on,t0.record_data,JSON_EXTRACT(t0.data, '$.referal_reason'),t0.data,t0.member_uid,JSON_EXTRACT(t0.data, '$.referal_facility') from CBEMARI t0" +
                            " INNER JOIN MEMBER t1 ON t0.member_uid = t1.uid" +
                            " where JSON_EXTRACT(t0.data, '$.referal_reason') IS \"\" AND JSON_EXTRACT(t0.data, '$.referal_facility') IS \"\"" +
                            " AND date(t0.record_data) BETWEEN '" + et_is_tareekh_sy.getText().toString() + "' AND '" + et_is_tareekh_tk.getText().toString() + "'");
                    if (mData != null) {
                        String formatted = null;
                        String referal_facility = null;
                        String referal_reason = null;
                        for (int i = 0; i < mData.length; i++) {
                            if (mData[i][1] == null) {
                                formatted = "01:05";
                            } else {
                                Date date = new Date(Long.parseLong(mData[i][1]));
                                SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                                formatted = format.format(date);
                                Log.d("000700", "Converted Time: " + formatted);
                            }

                            if (mData[i][3].isEmpty()) {
                                Log.d("000700", "-++++++++++++++++ YESSSSSS EMPTY ++++++++++++++++ ");
                                referal_reason = "none";
                            } else {
                                Log.d("000700", "------------------  NO EMPTY----------------- ");
                                referal_reason = mData[i][3];
                            }

                            if (mData[i][6].isEmpty()) {
                                Log.d("000700", "-++++++++++++++++ YESSSSSS EMPTY ++++++++++++++++ ");
                                referal_facility = "none";
                            } else {
                                Log.d("000700", "------------------  NO EMPTY----------------- ");
                                referal_reason = mData[i][6];
                            }

                            Log.d("000700", "FullName and AddedOn: " + mData[i][0] + "-" + formatted + "-" + mData[i][2] + "-" + referal_reason + "-" + "Child Illness" + "-" + mData[i][5] + "-" + referal_facility+"-"+mData[i][1]+"-"+"0"+"-"+mData[i][4]);
                            mArrayList_CBemari.add(mData[i][0] + "@" + formatted + "@" + mData[i][2] + "@" + referal_reason + "@" + "Child Illness" + "@" + mData[i][5] + "@" + referal_facility+"@"+mData[i][1]+"@"+"0"+"@"+mData[i][4]);


                         //   Log.d("000700", "CBEMARI ARRAYLIST : " + i + mArrayList_CBemari);
                        }
                    } else {
                        Log.d("000700", "----------  CBEMARI child data Null -----------------");
                    }
                    //  }
                } else {
                    Log.d("000700", "ELSE 2");
                }

                mArrayList_Main.put(mArrayList_HeaderList.get(0), mArrayList_CBemari);
                Log.d("000700", "CBEMARI ChildList Size: " + mArrayList_CBemari.size());

            } catch (Exception e) {
                Log.d("000700", "Error CBEMARI Catch:  " + e.getMessage());
            }


            ///////////////////////////////////// MBEMARIII Record /////////////////////////////////
            Log.d("000700", "*************************  Mother BEMARI DATA*******************: ");
            try {
                mData_MBemari = ls.executeReader("Select count(*) from MBEMARI where JSON_EXTRACT(data, '$.referal_reason') IS \"\" AND JSON_EXTRACT(data, '$.referal_facility') IS \"\" AND date(record_data) BETWEEN '" + et_is_tareekh_sy.getText().toString() + "' AND '" + et_is_tareekh_tk.getText().toString() + "'");
                if (mData_MBemari != null) {
                    Log.d("000700", "Total MBEMARI Count: " + mData_MBemari[0][0]);
                    mArrayList_HeaderList.add(mData_MBemari[0][0] + "@" + "ماں  کی بیماری کا حوالہ");

                    Log.d("000700", "MBEMARI HEADER LIST Size: " + mArrayList_HeaderList.size());

                    //   for (int c = 0; c < mArrayList_HeaderList.size(); c++) {
                    mArrayList_MBemari = new ArrayList<>();
                    String[][] mData = ls.executeReader("Select t1.full_name,t0.added_on,t0.record_data,JSON_EXTRACT(t0.data, '$.referal_reason'),t0.data,t0.member_uid,JSON_EXTRACT(t0.data, '$.referal_facility') from MBEMARI t0" +
                            " INNER JOIN MEMBER t1 ON t0.member_uid = t1.uid" +
                            " where JSON_EXTRACT(t0.data, '$.referal_reason') IS \"\" AND JSON_EXTRACT(t0.data, '$.referal_facility') IS \"\"" +
                            " AND date(t0.record_data) BETWEEN '" + et_is_tareekh_sy.getText().toString() + "' AND '" + et_is_tareekh_tk.getText().toString() + "'");
                    if (mData != null) {
                        String formatted = null;
                        String referal_facility = null;
                        String referal_reason = null;
                        for (int i = 0; i < mData.length; i++) {
                            if (mData[i][1] == null) {
                                formatted = "01:05";
                            } else {
                                Date date = new Date(Long.parseLong(mData[i][1]));
                                SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                                formatted = format.format(date);
                                Log.d("000700", "Converted Time: " + formatted);
                            }

                            if (mData[i][3].isEmpty()) {
                                Log.d("000700", "-++++++++++++++++ YESSSSSS EMPTY ++++++++++++++++ ");
                                referal_reason = "none";
                            } else {
                                Log.d("000700", "------------------  NO EMPTY----------------- ");
                                referal_reason = mData[i][3];
                                //mArrayList_Referral.add(mData[i][2]+ "@" + mData[i][3]);
                            }

                            if (mData[i][6].isEmpty()) {
                                Log.d("000700", "-++++++++++++++++ YESSSSSS EMPTY ++++++++++++++++ ");
                                referal_facility = "none";
                            } else {
                                Log.d("000700", "------------------  NO EMPTY----------------- ");
                                referal_reason = mData[i][6];
                            }

                            Log.d("000700", "FullName and AddedOn: " + mData[i][0] + "-" + formatted + "-" + mData[i][2] + "-" + referal_reason + "-" + "Mother Illness"+ "-" + mData[i][5] + "-" + referal_facility+"-"+mData[i][1]+"-"+"1"+"-"+mData[i][4]);
                            mArrayList_MBemari.add(mData[i][0] + "@" + formatted + "@" + mData[i][2] + "@" + referal_reason + "@" + "Mother Illness"+ "@" + mData[i][5] + "@" + referal_facility+"@"+mData[i][1]+"@"+"1"+"@"+mData[i][4]);


                          //  Log.d("000700", "MBEMARI ARRAYLIST : " + i + mArrayList_MBemari);
                        }
                    } else {
                        Log.d("000700", "-----------------  MBEMARI child data Null -------------------- ");
                    }
                    // }
                } else {
                    Log.d("000700", "ELSE 2");
                }

                mArrayList_Main.put(mArrayList_HeaderList.get(1), mArrayList_MBemari);
                Log.d("000700", "MBEMARI ChildList Size: " + mArrayList_MBemari.size());

            } catch (Exception e) {
                Log.d("000700", "Error MBEMARI Catch:  " + e.getMessage());
            }


            ///////////////////////////////////// MFPlanning Record /////////////////////////////////
            Log.d("000700", "*************************  Mother FPlanning DATA*******************: ");
            try {
                mData_MFPlanning = ls.executeReader("Select count(*) from MFPLAN where JSON_EXTRACT(data, '$.reason_refer') IS \"\" AND JSON_EXTRACT(data, '$.facility_refer') IS \"\" AND date(record_data) BETWEEN '" + et_is_tareekh_sy.getText().toString() + "' AND '" + et_is_tareekh_tk.getText().toString() + "'");
                if (mData_MFPlanning != null) {
                    Log.d("000700", "Total MFPLAN Count: " + mData_MFPlanning[0][0]);
                    mArrayList_HeaderList.add(mData_MFPlanning[0][0] + "@" + "خاندانی منصوبہ بندی کا حوالہ");

                    Log.d("000700", "MFPLAN HEADER LIST Size: " + mArrayList_HeaderList.size());

                    //  for (int c = 0; c < mArrayList_HeaderList.size(); c++) {
                    mArrayList_MFplaning = new ArrayList<>();
                    String[][] mData = ls.executeReader("Select t1.full_name,t0.added_on,t0.record_data,JSON_EXTRACT(t0.data, '$.reason_refer'),t0.data,t0.member_uid,JSON_EXTRACT(t0.data, '$.facility_refer')  from MFPLAN t0" +
                            " INNER JOIN MEMBER t1 ON t0.member_uid = t1.uid" +
                            " where JSON_EXTRACT(t0.data, '$.reason_refer') IS \"\" AND JSON_EXTRACT(t0.data, '$.facility_refer') IS \"\"" +
                            " AND date(t0.record_data) BETWEEN '" + et_is_tareekh_sy.getText().toString() + "' AND '" + et_is_tareekh_tk.getText().toString() + "'");
                    if (mData != null) {
                        String formatted = null;
                        String referal_facility = null;
                        String referal_reason = null;
                        for (int i = 0; i < mData.length; i++) {
                            if (mData[i][1] == null) {
                                formatted = "01:05";
                            } else {
                                Date date = new Date(Long.parseLong(mData[i][1]));
                                SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                                formatted = format.format(date);
                                Log.d("000700", "Converted Time: " + formatted);
                            }

                            if (mData[i][3].isEmpty()) {
                                Log.d("000700", "-++++++++++++++++ YESSSSSS EMPTY ++++++++++++++++ ");
                                referal_reason = "none";
                            } else {
                                Log.d("000700", "------------------  NO EMPTY----------------- ");
                                referal_reason = mData[i][3];
                                //mArrayList_Referral.add(mData[i][2]+ "@" + mData[i][3]);
                            }

                            if (mData[i][6].isEmpty()) {
                                Log.d("000700", "-++++++++++++++++ YESSSSSS EMPTY ++++++++++++++++ ");
                                referal_facility = "none";
                            } else {
                                Log.d("000700", "------------------  NO EMPTY----------------- ");
                                referal_reason = mData[i][6];
                            }

                            Log.d("000700", "FullName and AddedOn: " + mData[i][0] + "-" + formatted + "-" + mData[i][2] + "-" + referal_reason + "-" + "Family Planning"+ "-" + mData[i][5] + "-" + referal_facility+"-"+mData[i][1]+"-"+"2"+"-"+mData[i][4]);
                            mArrayList_MFplaning.add(mData[i][0] + "@" + formatted + "@" + mData[i][2] + "@" + referal_reason + "@"+ "Family Planning"+ "@" + mData[i][5] + "@" + referal_facility+"@"+mData[i][1]+"@"+"2"+"@"+mData[i][4]);


                           // Log.d("000700", "MFPLAN ARRAYLIST : " + i + mArrayList_MFplaning);
                        }
                    } else {
                        Log.d("000700", "-----------------------  MFPLAN child data Null --------------------");
                    }
                    //}
                } else {
                    Log.d("000700", "ELSE 2");
                }

                mArrayList_Main.put(mArrayList_HeaderList.get(2), mArrayList_MFplaning);
                Log.d("000700", "MFPLAN ChildList Size: " + mArrayList_MFplaning.size());

            } catch (Exception e) {
                Log.d("000700", "Error MFPLAN Catch:  " + e.getMessage());
            }


            ///////////////////////////////////// Referral Record /////////////////////////////////
            Log.d("000700", "*************************  REFERRAL DATA*******************: ");
            try {
                mData_Referral = ls.executeReader("Select count(*) from REFERAL where JSON_EXTRACT(data, '$.referal_reason') IS \"\" AND JSON_EXTRACT(data, '$.referal_facility') IS \"\" AND date(record_data) BETWEEN '" + et_is_tareekh_sy.getText().toString() + "' AND '" + et_is_tareekh_tk.getText().toString() + "'");
                if (mData_Referral != null) {
                    Log.d("000700", "Total REFERRAL Count: " + mData_Referral[0][0]);
                    mArrayList_HeaderList.add(mData_Referral[0][0] + "@" + "ریفرل");

                    Log.d("000700", "REFERRAL HEADER LIST Size: " + mArrayList_HeaderList.size());

                    // for (int c = 0; c < mArrayList_HeaderList.size(); c++) {
                    mArrayList_Referral = new ArrayList<>();
                    String[][] mData = ls.executeReader("Select t1.full_name,t0.added_on,t0.record_data,JSON_EXTRACT(t0.data, '$.referal_reason'),t0.data,t0.member_uid,JSON_EXTRACT(t0.data, '$.referal_facility')  from REFERAL t0" +
                            " INNER JOIN MEMBER t1 ON t0.member_uid = t1.uid" +
                            " where JSON_EXTRACT(t0.data, '$.referal_reason') IS \"\" AND JSON_EXTRACT(t0.data, '$.referal_facility') IS \"\"" +
                            " AND date(t0.record_data) BETWEEN '" + et_is_tareekh_sy.getText().toString() + "' AND '" + et_is_tareekh_tk.getText().toString() + "'");
                    if (mData != null) {
                        String formatted = null;
                        String referal_facility = null;
                        String referal_reason = null;
                        for (int i = 0; i < mData.length; i++) {
                            if (mData[i][1] == null) {
                                formatted = "01:05";
                                // mArrayList_Referral.add(mData[i][0] + "@" + "01:05");
                                //     mArrayList_Referral.add(mData[i][0] + "@" + "01:05"+ "@" + mData[i][2]+ "@" + mData[i][3]+"@"+mData[i][4]+"@"+mData[i][5]);
                            } else {
                                Date date = new Date(Long.parseLong(mData[i][1]));
                                SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                                formatted = format.format(date);
                                Log.d("000700", "Converted Time: " + formatted);

                                //   mArrayList_Referral.add(mData[i][0] + "@" + formatted);
                                // mArrayList_Referral.add(mData[i][0] + "@" + formatted+ "@" + mData[i][2]+ "@" + mData[i][3]+"@"+mData[i][4]+"@"+mData[i][5]);
                                // Log.d("000700", "FullName and AddedOn !!!!!!!!!!: " + mData[i][0] + "-" + formatted+ "-" + mData[i][2]+ "-" + mData[i][3]+"-"+mData[i][4]+"-"+mData[i][5]+"-"+mData[i][6]);
                            }

                            if (mData[i][3].isEmpty()) {
                                Log.d("000700", "-++++++++++++++++ YESSSSSS EMPTY ++++++++++++++++ ");
                                referal_reason = "none";
                                // mArrayList_Referral.add(mData[i][2]+ "@" + "none");
                            } else {
                                Log.d("000700", "------------------  NO EMPTY----------------- ");
                                referal_reason = mData[i][3];
                                //mArrayList_Referral.add(mData[i][2]+ "@" + mData[i][3]);
                            }

                            if (mData[i][6].isEmpty()) {
                                Log.d("000700", "-++++++++++++++++ YESSSSSS EMPTY ++++++++++++++++ ");
                                referal_facility = "none";
                                // mArrayList_Referral.add(mData[i][4]+"@"+mData[i][5]+"@"+"none");
                            } else {
                                Log.d("000700", "------------------  NO EMPTY----------------- ");
                                referal_reason = mData[i][6];
                                //mArrayList_Referral.add(mData[i][4]+"@"+mData[i][5]+"@"+mData[i][6]);
                            }

                            Log.d("000700", "FullName and AddedOn: " + mData[i][0] + "-" + formatted + "-" + mData[i][2] + "-" + referal_reason + "-" + "Referral" + "-" + mData[i][5] + "-" + referal_facility+"-"+mData[i][1]+"-"+"3"+"-"+mData[i][4]);
                            mArrayList_Referral.add(mData[i][0] + "@" + formatted + "@" + mData[i][2] + "@" + referal_reason + "@" + "Referral" + "@" + mData[i][5] + "@" + referal_facility+"@"+mData[i][1]+"@"+"3"+"@"+mData[i][4]);


                         //   Log.d("000700", "REFERRAL ARRAYLIST : " + i + mArrayList_Referral);
                        }
                    } else {
                        Log.d("000700", "------------------  REFERRAL child data Null ----------------- ");
                    }
                    //  }
                } else {
                    Log.d("000700", "ELSE 2");
                }

                mArrayList_Main.put(mArrayList_HeaderList.get(3), mArrayList_Referral);
                Log.d("000700", "REFERRAL ARRAYLIST: " + mArrayList_Referral);
                Log.d("000700", "REFERRAL ChildList Size: " + mArrayList_Referral.size());

            } catch (Exception e) {
                Log.d("000700", "Error REFERRAL Catch:  " + e.getMessage());
            }

            adt_lhw_referExpandableList.notifyDataSetChanged();
            exp_list.setClickable(true);

        } catch (Exception e) {
            Log.d("000700", "OVERALL Error Catch:  " + e.getMessage());
        }

    }

    class Task extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            Log.d("000700", "ON PREEEEE: ");
            pbProgress.setVisibility(View.VISIBLE);
            exp_list.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Log.d("000700", "ON BACkGROUND: ");
                prepareListData();
                Thread.sleep(1500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("000700", "ON EXECUTE: ");
            pbProgress.setVisibility(View.GONE);
            exp_list.setVisibility(View.VISIBLE);

            adt_lhw_referExpandableList.notifyDataSetChanged();
            super.onPostExecute(result);
        }

    }


    public void ShowDateDialougeIsTareekhSy() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DatePickerDialog,
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

                        Log.d("000555", "IS Tareekh SY: " + et_is_tareekh_sy.getText().toString());

                        pbProgress.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pbProgress.setVisibility(View.GONE);
                                prepareListData();

                            }
                        }, 2000);


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


    public void ShowDateDialougeIsTareekhTk() {


        final Calendar c = Calendar.getInstance();
        mYear2 = c.get(Calendar.YEAR);
        mMonth2 = c.get(Calendar.MONTH);
        mDay2 = c.get(Calendar.DAY_OF_MONTH);


        final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DatePickerDialog,
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
                                prepareListData();

                            }
                        }, 2000);


                        Calendar dob = Calendar.getInstance();
                        Calendar today = Calendar.getInstance();

                        dob.set(year, monthOfYear, dayOfMonth);

                        int age = today.get(Calendar.YEAR) - year;

                        Integer ageInt = new Integer(age);
                        String ageS = ageInt.toString();

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