package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SocialContactActivities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
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

import static com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SocialContactActivities.HES_StartSchoolMeetingList_Activity.var_hes_startMeeting;


public class LastMonth_Fragment extends Fragment {


    ListView lv;
    FloatingActionButton btn_FabAdd;

    FloatingActionMenu btn_floating_action_menu;
    com.github.clans.fab.FloatingActionButton btn_FabMember,btn_FabMeeting;

    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    TextView tv_record;
    LinearLayout ll_pbProgress;

    String[][] mData;

    Adt_MeetingRecordList adt;

    String meeting_uid, TodayDate, temp = "0", type;

    public LastMonth_Fragment() {

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


        if (type.equalsIgnoreCase("2"))
        {
            btn_FabMember.setLabelText("School Name (اسکول کا نام)");
        }
        //TextView
        tv_record = rootView.findViewById(R.id.tv_record);

        //LinearLayout
        ll_pbProgress = rootView.findViewById(R.id.ll_pbProgress);

        btn_FabMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            /*    meeting_uid = UUID.randomUUID().toString().replace("-", "");

                SharedPreferences settings = getContext().getSharedPreferences("meetingID", MODE_PRIVATE);
                // Writing data to SharedPreferences
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("meeting_uid", meeting_uid);
                editor.commit();*/

                if (type.equalsIgnoreCase("2")) {

                    Intent newIntent = new Intent(getContext(), HES_SchoolList_Activity.class);
                    newIntent.putExtra("type", type);
                    startActivity(newIntent);
                } else {

                    Intent newIntent = new Intent(getContext(), HCM_MemberList_Activity.class);
                    newIntent.putExtra("type", type);
                    startActivity(newIntent);
                }

            }
        });


        btn_FabMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type.equalsIgnoreCase("2")) {
                    Intent newIntent = new Intent(getContext(), HES_StartSchoolMeetingList_Activity.class);
                    newIntent.putExtra("type", type);
                    startActivity(newIntent);
                } else {

                    Intent newIntent = new Intent(getContext(), HCM_StartMeetingList_Activity.class);
                    newIntent.putExtra("type", type);
                    startActivity(newIntent);
                }

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (type.equalsIgnoreCase("2")) {
                    if (mData[position][2] == null) {
                        Intent newIntent = new Intent(getContext(), HES_StartSchoolMeetingList_Activity.class);
                        newIntent.putExtra("meeting_uid", mData[position][1]);
                        newIntent.putExtra("type", type);
                        startActivity(newIntent);
                    } else {
                        final Snackbar snackbar = Snackbar.make(view, "میٹنگ مکمل ہوچکی ہے.", Snackbar.LENGTH_SHORT);
                        View mySbView = snackbar.getView();
                        mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
                        TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(15);
                        snackbar.setDuration(3000);
                        snackbar.show();
                    }
                } else {
                    if (mData[position][2] == null) {
                        Intent newIntent = new Intent(getContext(), HCM_StartMeetingList_Activity.class);
                        newIntent.putExtra("meeting_uid", mData[position][1]);
                        newIntent.putExtra("type", type);
                        startActivity(newIntent);
                    } else {
                        final Snackbar snackbar = Snackbar.make(view, "میٹنگ مکمل ہوچکی ہے.", Snackbar.LENGTH_SHORT);
                        View mySbView = snackbar.getView();
                        mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
                        TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(15);
                        snackbar.setDuration(3000);
                        snackbar.show();
                    }
                }
            }
        });

        adt = new Adt_MeetingRecordList(getContext(), hashMapArrayList);
        lv.setAdapter(adt);
        new Task().execute();


        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                switch (scrollState) {
                    case 2: // SCROLL_STATE_FLING
                        //hide button here
                        btn_floating_action_menu.setVisibility(View.GONE);
                        break;

                    case 1: // SCROLL_STATE_TOUCH_SCROLL
                        //hide button here
                        btn_floating_action_menu.setVisibility(View.GONE);

                        break;

                    case 0: // SCROLL_STATE_IDLE
                        //show button here
                        btn_floating_action_menu.setVisibility(View.VISIBLE);

                        break;

                    default:
                        //show button here
                        btn_floating_action_menu.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


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
                tv_record.setVisibility(View.VISIBLE);
            }

            adt.notifyDataSetChanged();
            super.onPostExecute(result);
        }

    }

    private void read_data() {

        hashMapArrayList.clear();
        try {

            /*Toast tt = Toast.makeText(getContext(),"Your text displayed here", Toast.LENGTH_SHORT);
            tt.setGravity(Gravity.CENTER, 0, 0);
            tt.show();*/

            // get your custom_toast.xml ayout
       /*     LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast_view_layout, null);
            // set a message
            TextView text = (TextView) layout.findViewById(R.id.customToastText);
            text.setText("ڈیٹا جمع ہوگیا ہے");
            // Toast...
            Toast toast = new Toast(getContext());
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();*/


            Lister ls = new Lister(getContext());
            ls.createAndOpenDB();

          /*  mData = ls.executeReader("SELECT count(*),t0.meeting_uid,t1.meeting_status,t0.record_data from MEETING_MEMBER t0" +
                    " LEFT JOIN MEETING t1 on t0.meeting_uid = t1.uid" +
                    " where t0.month= '" + TodayDate.split("-")[1] + "' AND t0.type='" + type + "'" +
                  //  " GROUP BY t1.meeting_uid " +H
                    " ORDER BY t0.meeting_uid DESC "); */
         /*   mData = ls.executeReader("SELECT count(*),t1.uid,t1.meeting_status,t0.record_data from MEETING_MEMBER t0" +
                    " LEFT JOIN MEETING t1 on t0.type = t1.type" +
                    " where t0.month= '" + TodayDate.split("-")[1] + "' AND t0.type='" + type + "'" +
                    " GROUP BY t1.uid " +
                    " ORDER BY t0.added_on DESC "); */

            mData = ls.executeReader("SELECT (CASE WHEN type IN (0, 1) THEN JSON_ARRAY_LENGTH(metadata) ELSE JSON_EXTRACT(metadata, '$.total') END) as length ,uid,meeting_status,added_on from MEETING" +
                    //" LEFT JOIN MEETING t1 on t0.type = t1.type" +
                   // " where t0.month= '" + TodayDate.split("-")[1] + "' AND t0.type='" + type + "'" +
                   " where type='" + type + "'" +
                    " AND strftime('%m', record_data) ='"+TodayDate.split("-")[1]+"'" +
                    " GROUP BY uid ");
                   //" ORDER BY added_on DESC ");

          //  if (Integer.parseInt(mData[0][0]) > 0) {
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
          /*  adt = new Adt_MeetingRecordList(getContext(), hashMapArrayList);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);*/

        } catch (Exception e) {
            Log.d("000357", "Error: " + e.getMessage());
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (HCM_RegisterForm_Activity.var_hcm_register.equalsIgnoreCase("1")) {
            Log.d("000357", "RESTART IF: ");
            getActivity().finish();
            startActivity(getActivity().getIntent());
            HCM_RegisterForm_Activity.var_hcm_register = "0";
        } else if (HCM_StartMeetingList_Activity.var_hcm_startMeeting.equalsIgnoreCase("1")) {
            Log.d("000357", "RESTART ELSE IF 1: ");
            getActivity().finish();
            startActivity(getActivity().getIntent());
            HCM_StartMeetingList_Activity.var_hcm_startMeeting = "0";
        }

        else if (HES_RegisterSchoolForm_Activity.var_hes_register.equalsIgnoreCase("1")) {
            Log.d("000357", "RESTART ELSE IF 2: ");
            getActivity().finish();
            startActivity(getActivity().getIntent());
            HES_RegisterSchoolForm_Activity.var_hes_register = "0";
        }

        else if (var_hes_startMeeting.equalsIgnoreCase("1")) {
            Log.d("000357", "RESTART ELSE IF 3: ");
            getActivity().finish();
            startActivity(getActivity().getIntent());
            var_hes_startMeeting = "0";
        }

        else {
            Log.d("000357", "RESTART ELSEEEE: ");
        }
    }

}