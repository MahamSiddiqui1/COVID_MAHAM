package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_ReferActivities;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.Adt_Refer.Adt_FollowUp;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class FollowUp_Fragment extends Fragment {


    ListView lv;
    EditText et_is_tareekh_tk, et_is_tareekh_sy;

    TableLayout tableLayout;
    ProgressBar pbProgress;
    TextView tv_record;

    String TodayDate;

    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    Adt_FollowUp adt_followUp;

    private int mYear, mMonth, mDay, mYear2, mMonth2, mDay2;
    String monthf, dayf, yearf = "null";
    String monthf2, dayf2, yearf2 = "null";

    public FollowUp_Fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(getContext(), FollowUp_Fragment.class));


        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());



        View rootView = inflater.inflate(R.layout.fragment_followup, container, false);


        lv =  rootView.findViewById(R.id.lv);

        //ProgressBar
        pbProgress =  rootView.findViewById(R.id.pbProgress);

        //TableLayout
        tableLayout = (TableLayout) rootView.findViewById(R.id.table_layout);


        tv_record =  rootView.findViewById(R.id.tv_record);


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



        adt_followUp = new Adt_FollowUp(getContext(), hashMapArrayList);
        lv.setAdapter(adt_followUp);
        //new Task().execute();

        Read_Data();

        return rootView;
    }



    /*class Task extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            Log.d("000800", "ON PREEEEE: ");
            pbProgress.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Log.d("000800", "ON BACkGROUND: ");

                Read_Data();
                Thread.sleep(1500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("000800", "ON EXECUTE: ");
            pbProgress.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);

         adt_followUp.notifyDataSetChanged();
            super.onPostExecute(result);
        }

    }*/

    public void Read_Data() {

        hashMapArrayList.clear();

        try {

            Lister ls = new Lister(getContext());
            ls.createAndOpenDB();

            String[][] mData = ls.executeReader("Select t1.full_name,t0.added_on,t0.record_data,JSON_EXTRACT(t0.data, '$.referal_reason'),JSON_EXTRACT(t0.data, '$.referal_type') from REFERAL t0" +
                    " INNER JOIN MEMBER t1 ON t0.member_uid = t1.uid" +
                   // " where JSON_EXTRACT(t0.data, '$.referal_reason') IS NOT \"\" OR JSON_EXTRACT(t0.data, '$.referal_facility') IS NOT \"\""+
                    " where date(t0.record_data) BETWEEN '" + et_is_tareekh_sy.getText().toString() + "' AND '" + et_is_tareekh_tk.getText().toString() + "' ORDER BY t0.added_on DESC");

            if (mData != null) {

               lv.setVisibility(View.VISIBLE);
                tv_record.setVisibility(View.GONE);

                HashMap<String, String> map;
                for (int i = 0; i < mData.length; i++) {

                   Log.d("000800", "FullName and AddedOn !!!!!!!!!!: " + mData[i][0] + "-" + mData[i][1]+ "-" + mData[i][2]+ "-" + mData[i][3]+"-"+mData[i][4]);
                    map = new HashMap<>();

                    map.put("full_name", "" + mData[i][0]);

                    if (mData[i][1] == null) {
                        map.put("time", "" + "01:06");
                    } else {
                        Date date = new Date(Long.parseLong(mData[i][1]));
                        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                        String formatted = format.format(date);
                        Log.d("000800", "Converted Time: " + formatted);

                        map.put("time", "" + formatted);
                    }

                    map.put("date", "" + mData[i][2]);
                    map.put("referal_reason", "" + mData[i][3]);
                    map.put("type", "" + mData[i][4]);

                    hashMapArrayList.add(map);
                }
                adt_followUp.notifyDataSetChanged();
            }
            else {
                Log.d("000800", "NULL: " );
                tv_record.setVisibility(View.VISIBLE);
                lv.setVisibility(View.GONE);
            }

            //adt_followUp.notifyDataSetChanged();

        } catch (Exception e) {
            tv_record.setVisibility(View.VISIBLE);
            Log.d("000800", "Error: " + e.getMessage());
            Toast.makeText(getContext(), R.string.somethingWrong, Toast.LENGTH_SHORT).show();
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

                        et_is_tareekh_sy.setText(yearf + "-" + monthf + "-" + dayf);

                        Log.d("000800", "IS Tareekh SY: " + et_is_tareekh_sy.getText().toString());

                        tv_record.setVisibility(View.GONE);
                        lv.setVisibility(View.GONE);
                        pbProgress.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pbProgress.setVisibility(View.GONE);
                                Read_Data();

                            }
                        }, 2000);



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

                        et_is_tareekh_tk.setText(yearf2 + "-" + monthf2 + "-" + dayf2);
                        Log.d("000800", "IS Tareekh TK: " + et_is_tareekh_tk.getText().toString());

                        lv.setVisibility(View.GONE);
                        tv_record.setVisibility(View.GONE);
                        pbProgress.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pbProgress.setVisibility(View.GONE);
                                Read_Data();

                            }
                        }, 2000);


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
            Log.d("000800", "DF" + e.getMessage());
            e.printStackTrace();

        }

        datePickerDialog.show();


    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        boolean isVisible = isVisibleToUser;
        // Make sure that fragment is currently visible
        if (!isVisible && isResumed()) {
            Log.d("000321","2");
            // Call code when Fragment not visible
        } else if (isVisible && isResumed()) {
            // Call code when Fragment becomes visible.
            Log.d("000321","3");
            Read_Data();
        }

    }


}