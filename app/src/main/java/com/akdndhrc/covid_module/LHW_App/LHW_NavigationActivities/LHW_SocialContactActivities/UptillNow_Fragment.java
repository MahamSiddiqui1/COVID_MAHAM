package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SocialContactActivities;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.akdndhrc.covid_module.Adapter.Adt_SocialContact.Adt_MeetingRecordList;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.R;
import com.github.clans.fab.FloatingActionMenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SocialContactActivities.HCM_RegisterForm_Activity.var_hcm_register;
import static com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SocialContactActivities.HCM_StartMeetingList_Activity.var_hcm_startMeeting;


public class UptillNow_Fragment extends Fragment {

    ListView lv;
    FloatingActionButton btn_FabAdd;

    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    TextView tv_record;
    LinearLayout ll_pbProgress;

    String[][] mData;

    Adt_MeetingRecordList adt;

    String meeting_uid, TodayDate, temp = "0", type;

    FloatingActionMenu btn_floating_action_menu;
    com.github.clans.fab.FloatingActionButton btn_FabMember,btn_FabMeeting;


    public UptillNow_Fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


            View rootView = inflater.inflate(R.layout.fragment_last_month, container, false);


        type = getActivity().getIntent().getExtras().getString("type");

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());


        //ListView
        lv = rootView.findViewById(R.id.lv);

        //Floating Action Button
        btn_FabAdd = rootView.findViewById(R.id.btn_FabAdd);
        btn_FabAdd.setVisibility(View.GONE);

        btn_floating_action_menu = rootView.findViewById(R.id.btn_floating_action_menu);
        btn_FabMember = rootView.findViewById(R.id.btn_FabMember);
        btn_FabMeeting = rootView.findViewById(R.id.btn_FabMeeting);
        btn_floating_action_menu.setVisibility(View.GONE);




        //TextView
        tv_record = rootView.findViewById(R.id.tv_record);

        //LinearLayout
        ll_pbProgress = rootView.findViewById(R.id.ll_pbProgress);


        adt = new Adt_MeetingRecordList(getContext(), hashMapArrayList);
        lv.setAdapter(adt);
        new Task().execute();



        return rootView;
    }


    class Task extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            Log.d("000357", "ON PREEEEE: ");
            ll_pbProgress.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Log.d("000357", "ON BACkGROUND: ");
                read_data();
                Thread.sleep(1500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("000357", "ON EXECUTE: ");
            ll_pbProgress.setVisibility(View.GONE);

            if (temp.equalsIgnoreCase("1")) {
                lv.setVisibility(View.VISIBLE);
                tv_record.setVisibility(View.GONE);
            } else {
                tv_record.setText(R.string.noRecordAtThisMoment);
                tv_record.setVisibility(View.VISIBLE);
            }

            adt.notifyDataSetChanged();
            super.onPostExecute(result);
        }

    }

    private void read_data() {

        hashMapArrayList.clear();
        try {


            Lister ls = new Lister(getContext());
            ls.createAndOpenDB();

      /*     mData = ls.executeReader("SELECT count(*),t0.meeting_uid,t1.meeting_status,t0.record_data from MEETING_MEMBER t0" +
                    " LEFT JOIN MEETING t1 on t0.meeting_uid = t1.uid" +
                    " where t0.type='" + type + "'" +
                    " GROUP BY t0.meeting_uid " +
                    " ORDER BY t0.meeting_uid DESC ");*/

         /*   mData = ls.executeReader("SELECT count(*),t1.uid,t1.meeting_status,t0.record_data from MEETING_MEMBER t0" +
                    " LEFT JOIN MEETING t1 on t0.type = t1.type" +
                    " where t0.type='" + type + "'" +
                     " GROUP BY t1.uid " +
                    " ORDER BY t0.added_on DESC ");*/

            mData = ls.executeReader("SELECT (CASE WHEN type IN (0, 1) THEN JSON_ARRAY_LENGTH(metadata) ELSE JSON_EXTRACT(metadata, \"$.total\") END) as length ,uid,meeting_status,added_on from MEETING" +
                    //" LEFT JOIN MEETING t1 on t0.type = t1.type" +
                    // " where t0.month= '" + TodayDate.split("-")[1] + "' AND t0.type='" + type + "'" +
                    " where type='" + type + "'" +
                    " GROUP BY uid ");
                   //" ORDER BY added_on DESC ");

//            if (Integer.parseInt(mData[0][0]) > 0) {
            if (mData != null) {
                temp = "1";

                HashMap<String, String> map;
                for (int i = 0; i < mData.length; i++) {

                    Log.d("000357", "Member Count: " + mData[i][0]);
                    Log.d("000357", "Meeting_status: " + mData[i][2]);

                    map = new HashMap<>();

                    map.put("member_count", "" + mData[i][0]);

                    if (type.equalsIgnoreCase("1")) {
                        //map.put("i", "" + "Women Support Group " + String.valueOf(i + 1));
                        map.put("i", "" + "Women Group Meeting " + String.valueOf(i + 1));
                    } else if (type.equalsIgnoreCase("2")) {
                        // map.put("i", "" + "Health Education in School " + String.valueOf(i + 1));
                        map.put("i", "" + "School Meeting " + String.valueOf(i + 1));
                    } else {
                        //map.put("i", "" + "Health Committee " + String.valueOf(i + 1));
                        map.put("i", "" + "Meeting " + String.valueOf(i + 1));
                    }

                    Date date = new Date(Long.parseLong(mData[i][3]));
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.ENGLISH);
                    String formatted = format.format(date);
                    Log.d("000357", "datetime: " + formatted);

                    map.put("record_data", "" + formatted);


                    if (mData[i][2] == null) {
                        map.put("meeting_status", "" + "null");
                    } else {
                        map.put("meeting_status", "" + mData[i][2]);
                    }

                    hashMapArrayList.add(map);
                }

            } else {
                Log.d("000357", "NULLLL: ");
            }

        } catch (Exception e) {
            Log.d("000357", "Error: " + e.getMessage());
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (var_hcm_register.equalsIgnoreCase("1")) {
            Log.d("000357", "RESTART IF: ");

            getActivity().finish();
            startActivity(getActivity().getIntent());

            var_hcm_register = "0";
        } else if (var_hcm_startMeeting.equalsIgnoreCase("1")) {
            Log.d("000357", "RESTART ELSE IF: ");
            getActivity().finish();
            startActivity(getActivity().getIntent());
            var_hcm_startMeeting = "0";
        } else {
            Log.d("000357", "RESTART ELSEEEE: ");
        }
    }

}