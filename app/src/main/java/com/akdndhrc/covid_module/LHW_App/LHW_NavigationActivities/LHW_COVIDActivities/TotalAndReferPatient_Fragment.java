package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_COVIDActivities;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.Adt_COVID.Adt_TotalAndReferPatients;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.ServiceLocation;
import com.akdndhrc.covid_module.Utils;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.akdndhrc.covid_module.R.string.enterCountPatient;
import static com.akdndhrc.covid_module.R.string.enterRefPatientCount;


public class TotalAndReferPatient_Fragment extends Fragment {


    ListView lv;
    EditText et_tareekh_indraj;
    FloatingActionButton btn_FabAdd;

    TableLayout tableLayout;
    ProgressBar pbProgress;
    TextView tv_record;

    String TodayDate;

    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    Adt_TotalAndReferPatients adt_totalAndReferPatients;

    private int mYear, mMonth, mDay;
    String monthf, dayf, yearf = "null";

    EditText et_tareekh_indraj_dialog, et_total_number_patients, et_refer_number_patients;
    Button btn_jama_kre;
    long mLastClickTime = 0;

    ServiceLocation serviceLocation;
    String login_useruid;
    double latitude;
    double longitude;
    Lister ls;

    public TotalAndReferPatient_Fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(getContext(), TotalAndReferPatient_Fragment.class));


        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());


        View rootView = inflater.inflate(R.layout.fragment_total_and_refer_patient, container, false);


        //Get shared USer name
        try {
            SharedPreferences prefelse = getContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
            String shared_useruid = prefelse.getString((R.string.loginUserIDEng), null); // getting String
            login_useruid = shared_useruid;
            Log.d("000369", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000369", "Shared Err:" + e.getMessage());
        }

        try {
            serviceLocation = new ServiceLocation(getContext());
            serviceLocation.locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            serviceLocation.callAsynchronousTask();
        } catch (Exception e) {
            Log.d("000369", "GPS Service Err:  " + e.getMessage());
        }


         ls = new Lister(getContext());

        //ListView
        lv = rootView.findViewById(R.id.lv);

        //FloatingActionButton
        btn_FabAdd = rootView.findViewById(R.id.btn_FabAdd);

        //ProgressBar
        pbProgress = rootView.findViewById(R.id.pbProgress);

        //TableLayout
        tableLayout = (TableLayout) rootView.findViewById(R.id.table_layout);


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


        btn_FabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddTotalPatientDialog();
            }
        });


        adt_totalAndReferPatients = new Adt_TotalAndReferPatients(getContext(), hashMapArrayList);
        lv.setAdapter(adt_totalAndReferPatients);
        //new Task().execute();

        Read_Data();

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

        return rootView;
    }

    private void AddTotalPatientDialog() {

        //android.R.style.Theme_Black_NoTitleBar_Fullscreen

        final Dialog alertDialog = new Dialog(getContext());
        LayoutInflater layout = LayoutInflater.from(getContext());
        final View dialogView = layout.inflate(R.layout.dialog_total_and_refer_patient_layout, null);

        alertDialog.setContentView(dialogView);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        alertDialog.show();


        //ImageView
        ImageView iv_close = dialogView.findViewById(R.id.iv_close);

        //EditText
        et_tareekh_indraj_dialog = dialogView.findViewById(R.id.et_tareekh_indraj);
        et_total_number_patients = dialogView.findViewById(R.id.et_total_number_patients);
        et_refer_number_patients = dialogView.findViewById(R.id.et_refer_number_patients);

        //Button
        btn_jama_kre = dialogView.findViewById(R.id.btn_jama_kre);


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        et_tareekh_indraj_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogTareekhIndraj();
            }
        });

        btn_jama_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_tareekh_indraj_dialog.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), R.string.dateOfEntrancePrompt, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (et_total_number_patients.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), enterCountPatient, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (et_refer_number_patients.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), enterRefPatientCount, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (serviceLocation.showCurrentLocation() == true) {
                    latitude = serviceLocation.getLatitude();
                    longitude = serviceLocation.getLongitude();
                    Log.d("000369", " latitude: " + latitude);
                    Log.d("000369", " longitude: " + longitude);
                } else {

                    try {
                        serviceLocation.doAsynchronousTask.cancel();
                    } catch (Exception e) {
                    }
                    try {
                        ls.createAndOpenDB();

                        String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from CBEMARI");

                        if (Integer.parseInt(mData[0][2]) > 0) {
                            JSONObject jsonObject = new JSONObject(mData[0][1]);
                            Log.d("000369", "  Last Latitude: " + jsonObject.getString("lat"));
                            Log.d("000369", "  Last Longitude: " + jsonObject.getString("lng"));

                            latitude = Double.parseDouble(jsonObject.getString("lat"));
                            longitude = Double.parseDouble(jsonObject.getString("lng"));

                        } else {
                            latitude = Double.parseDouble("0.0");
                            longitude = Double.parseDouble("0.0");
                        }

                    } catch (Exception e) {
                        Log.d("000369", "Read CGROWTH Error: " + e.getMessage());
                    }
                }

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Log.d("000369", " mLastClickTime: " + mLastClickTime);


                try {

                    ls.createAndOpenDB();

                    JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("record_enter_date", "" + et_tareekh_indraj_dialog.getText().toString());
                    jobj.put("total_number_of_patients", "" + et_total_number_patients.getText().toString());
                    jobj.put("refer_number_of_patients", "" + et_refer_number_patients.getText().toString());
                    jobj.put("current_record_date", "" + TodayDate);


                    String cur_added_on = String.valueOf(System.currentTimeMillis());

                    String ans1 = "insert into CVIRUS (member_uid, record_data,type, data,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'NOT_ASSIGNED'," +
                            "'" + et_tareekh_indraj_dialog.getText().toString() + "'," +
                            "'2'," +
                            "'" + jobj + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + cur_added_on + "'" +
                            ")";


                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000369", "Data: " + ans1);
                    Log.d("000369", "Query: " + res.toString());


                    if (res.toString().equalsIgnoreCase("true")) {

                        Log.d("000369", "********** TRUE *************");
                        final Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.fragment_total_and_refer_patient), R.string.dataSubmissionMessage, Snackbar.LENGTH_SHORT);
                        View mySbView = snackbar.getView();
                        mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
                        TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(16);
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            textView.setCompoundDrawableTintList(getContext().getResources().getColorStateList(R.color.green_color));
                        }
                        snackbar.setDuration(2500);
                        snackbar.show();


                        if (Utils.haveNetworkConnection(getContext()) > 0) {

                           // sendPostRequest("NOT_ASSIGNED", et_tareekh_indraj.getText().toString(), "2", String.valueOf(jobj), login_useruid, cur_added_on);
                        } else {
                            // Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                alertDialog.dismiss();
                                Read_Data();

                            }
                        }, 1500);
                    } else {
                        Log.d("000369", "********** FALSE *************");
                        final Snackbar snackbar = Snackbar.make(v, R.string.dataSubmissionFailed, Snackbar.LENGTH_SHORT);
                        View mySbView = snackbar.getView();
                        mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
                        TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(16);
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            textView.setCompoundDrawableTintList(getContext().getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                        }
                        snackbar.setDuration(2000);
                        snackbar.show();

                    }


                } catch (Exception e) {
                    Log.d("000369", "Err: " + e.getMessage());
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    public void Read_Data() {

        hashMapArrayList.clear();

        try {

            Lister ls = new Lister(getContext());
            ls.createAndOpenDB();

            String[][] mData = ls.executeReader("Select JSON_EXTRACT(data, '$.total_number_of_patients'),JSON_EXTRACT(data, '$.refer_number_of_patients'),record_data,added_on from CVIRUS where date(record_data)= '" + et_tareekh_indraj.getText().toString() + "' AND type IS '2' ORDER BY added_on DESC");

            if (mData != null) {

                lv.setVisibility(View.VISIBLE);
                tv_record.setVisibility(View.GONE);

                HashMap<String, String> map;
                for (int i = 0; i < mData.length; i++) {
                    Log.d("000369", "total_number_of_patients: " + mData[i][0]);
                    Log.d("000369", "refer_number_of_patients: " + mData[i][1]);
                    Log.d("000369", "Added: " + mData[i][2]);

                    map = new HashMap<>();

                    map.put("total_patients", "" + mData[i][0]);
                    map.put("refer_patients", "" + mData[i][1]);
                    map.put("date", "" + mData[i][2]);
              

                    if (mData[i][3] == null) {
                        map.put("time", "" + "01:06");
                    } else {
                        Date date = new Date(Long.parseLong(mData[i][3]));
                        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
                        String formatted = format.format(date);
                        Log.d("000369", "Converted Time: " + formatted);

                        map.put("time", "" + formatted);
                    }
                    
                    hashMapArrayList.add(map);
                }
                
                adt_totalAndReferPatients.notifyDataSetChanged();
                
            } else {
                Log.d("000369", "NULL: ");
                tv_record.setVisibility(View.VISIBLE);
                lv.setVisibility(View.GONE);
            }
            

        } catch (Exception e) {
            tv_record.setVisibility(View.VISIBLE);
            Log.d("000369", "Error: " + e.getMessage());
            Toast.makeText(getContext(), R.string.somethingWrong, Toast.LENGTH_SHORT).show();
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

                        Log.d("000369", "IS Tareekh SY: " + et_tareekh_indraj.getText().toString());

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

    public void DialogTareekhIndraj() {

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

                        et_tareekh_indraj_dialog.setText(yearf + "-" + monthf + "-" + dayf);

                        Log.d("000369", "IS Tareekh SY: " + et_tareekh_indraj_dialog.getText().toString());


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
            Log.d("000321", "2");
            // Call code when Fragment not visible
        } else if (isVisible && isResumed()) {
            // Call code when Fragment becomes visible.
            Log.d("000321", "3");
            //Read_Data();
        }

    }


}