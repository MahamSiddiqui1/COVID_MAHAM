package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_COVIDActivities;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.Adt_COVID.Adt_PatientList;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class PatientList_Fragment extends Fragment {


    ListView lv;
    EditText et_tareekh_indraj;
    FloatingActionButton btn_FabAdd;


    ProgressBar pbProgress;
    TextView tv_record;

    String TodayDate;

    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    Adt_PatientList adt_newPatientList;

    private int mYear, mMonth, mDay;
    String monthf, dayf, yearf = "null";

    String[][] mData;



    public PatientList_Fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(getContext(), PatientList_Fragment.class));


        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());


        View rootView = inflater.inflate(R.layout.fragment_patient_list, container, false);


        //ListView
        lv = rootView.findViewById(R.id.lv);

        //FloatingActionButton
        btn_FabAdd = rootView.findViewById(R.id.btn_FabAdd);

        //ProgressBar
        pbProgress = rootView.findViewById(R.id.pbProgress);


        tv_record = rootView.findViewById(R.id.tv_record);


        //Edittext
        et_tareekh_indraj = rootView.findViewById(R.id.et_tareekh_indraj);
        et_tareekh_indraj.setText(TodayDate);


        et_tareekh_indraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TareekIndeerajDialog();

            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getContext(), AddNewPatientFormView_Activity.class);
                intent.putExtra("record_date",mData[position][1]);
                intent.putExtra("added_on",mData[position][2]);
                intent.putExtra("patient_uid",mData[position][3]);
                startActivity(intent);
            }
        });


        btn_FabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddNewPatientForm_Activity.class);
                startActivity(intent);

            }
        });



        adt_newPatientList = new Adt_PatientList(getContext(), hashMapArrayList);
        lv.setAdapter(adt_newPatientList);

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                switch (scrollState) {
                    case 2: // SCROLL_STATE_FLING
                        //hide button here
                        btn_FabAdd.setVisibility(View.GONE);
                        break;

                    case 1: // SCROLL_STATE_TOUCH_SCROLL
                        //hide button here
                        btn_FabAdd.setVisibility(View.GONE);

                        break;

                    case 0: // SCROLL_STATE_IDLE
                        //show button here
                        btn_FabAdd.setVisibility(View.VISIBLE);

                        break;

                    default:
                        //show button here
                        btn_FabAdd.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


       // Read_Data();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("000331","ONRESUME");
        
        Read_Data();
    }

    public void Read_Data() {

        hashMapArrayList.clear();

        try {

            Lister ls = new Lister(getContext());
            ls.createAndOpenDB();

             mData = ls.executeReader("Select JSON_EXTRACT(data, '$.patient_name'),record_data,added_on,member_uid from CVIRUS where date(record_data)= '" + et_tareekh_indraj.getText().toString() + "' AND type IS '3' ORDER BY added_on DESC");

            if (mData != null) {

                lv.setVisibility(View.VISIBLE);
                tv_record.setVisibility(View.GONE);

                HashMap<String, String> map;
                for (int i = 0; i < mData.length; i++) {
                    Log.d("000331", "patient_name: " + mData[i][0]);
                    Log.d("000331", "Record Date: " + mData[i][1]);
                    Log.d("000331", "Added: " + mData[i][2]);

                    map = new HashMap<>();

                    map.put("patient_name", "" + mData[i][0]);
                    map.put("date", "" + mData[i][1]);


                    if (mData[i][2] == null) {
                        map.put("time", "" + "01:06");
                    } else {
                        Date date = new Date(Long.parseLong(mData[i][2]));
                        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
                        String formatted = format.format(date);
                        Log.d("000331", "Converted Time: " + formatted);

                        map.put("time", "" + formatted);
                    }

                    hashMapArrayList.add(map);
                }

                adt_newPatientList.notifyDataSetChanged();

            } else {
                Log.d("000331", "NULL: ");
                tv_record.setVisibility(View.VISIBLE);
                lv.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            tv_record.setVisibility(View.VISIBLE);
            Log.d("000331", "Error: " + e.getMessage());
            Toast.makeText(getContext(), "Something wrong!!", Toast.LENGTH_SHORT).show();
        }

    }

    public void TareekIndeerajDialog() {

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

                        et_tareekh_indraj.setText(yearf + "-" + monthf + "-" + dayf);

                        Log.d("000331", "IS Tareekh SY: " + et_tareekh_indraj.getText().toString());

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
           // Read_Data();
        }

    }


}