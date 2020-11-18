package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SocialContactActivities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.Adt_SocialContact.Adt_HCMMembeList;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.R;

import java.util.ArrayList;
import java.util.HashMap;

public class HCM_MemberList_Activity extends AppCompatActivity {


    Context ctx = HCM_MemberList_Activity.this;

    ImageView iv_startMeeting;
    ListView lv;
    FloatingActionButton btn_FabRegister;

    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    Adt_HCMMembeList adt;

    String meeting_uid,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hcm_member_list);


        type = getIntent().getExtras().getString("type");

        //Get shared meeting UID
      /*  try {
            SharedPreferences pre = getApplicationContext().getSharedPreferences("meetingID", 0); // 0 - for private mode
            String shared_meetingId = pre.getString("meeting_uid", null); // getting String
            meeting_uid = shared_meetingId;

            Log.d("000357", "Meeting UID: " + meeting_uid);

        } catch (Exception e) {
            Log.d("000357", "Shared Err:" + e.getMessage());
        }*/

        //ListView
        lv = findViewById(R.id.lv);


        //ImageView
        iv_startMeeting = findViewById(R.id.iv_startMeeting);
        iv_startMeeting.setVisibility(View.GONE);

        //Floating Button
        btn_FabRegister = findViewById(R.id.btn_FabRegister);

        btn_FabRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(ctx, HCM_RegisterForm_Activity.class);
                newIntent.putExtra("type",type);
                startActivity(newIntent);
            }
        });

        iv_startMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(ctx, HCM_StartMeetingList_Activity.class);
               // newIntent.putExtra("meeting_uid",meeting_uid);
                newIntent.putExtra("type",type);
                startActivity(newIntent);
            }
        });

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                switch (scrollState) {
                    case 2: // SCROLL_STATE_FLING
                        //hide button here
                        btn_FabRegister.setVisibility(View.GONE);
                        break;

                    case 1: // SCROLL_STATE_TOUCH_SCROLL
                        //hide button here
                        btn_FabRegister.setVisibility(View.GONE);

                        break;

                    case 0: // SCROLL_STATE_IDLE
                        //show button here
                        btn_FabRegister.setVisibility(View.VISIBLE);

                        break;

                    default:
                        //show button here
                        btn_FabRegister.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        hashMapArrayList.clear();
        try {


            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            //String[][] mData = ls.executeReader("Select JSON_EXTRACT(metadata, '$.full_name'),JSON_EXTRACT(metadata, '$.gender'),JSON_EXTRACT(metadata, '$.age') from MEETING_MEMBER where meeting_uid = '" + meeting_uid + "' AND type='"+type+"'");
            String[][] mData = ls.executeReader("Select JSON_EXTRACT(metadata, '$.full_name'),JSON_EXTRACT(metadata, '$.gender'),JSON_EXTRACT(metadata, '$.age') from MEETING_MEMBER where type='"+type+"'");

            if (mData != null) {
                iv_startMeeting.setVisibility(View.VISIBLE);

                HashMap<String, String> map;
                for (int i = 0; i < mData.length; i++) {

                    Log.d("000357", "MEMBER NAME: " +  mData[i][0]);
                    Log.d("000357", "MEMBER GENDER " +  mData[i][1]);
                    Log.d("000357", "MEMBER AGE " +  mData[i][2]);


                    map = new HashMap<>();

                    map.put("full_name", "" + mData[i][0]);
                    map.put("gender", "" + mData[i][1]);
                    map.put("age", "" + mData[i][2]);

                 /*   if (mData[i][1].equalsIgnoreCase("0")) {
                        map.put("gender", "" + "عورت");
                    } else if (mData_search[i][4].equalsIgnoreCase("1")) {
                        map.put("gender", "" + "مرد");
                    } else {
                        map.put("gender", "" + "unknown");
                    }*/

                    hashMapArrayList.add(map);
                }
            }
            else {
                Log.d("000357", "DATA NULLLLLLLL ");
                Toast.makeText(ctx, "کوئی رکن رجسٹر نہیں", Toast.LENGTH_SHORT).show();
            }
            adt = new Adt_HCMMembeList(ctx, hashMapArrayList);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);


        } catch (Exception e) {
            Log.d("12345", "Error: " + e.getMessage());
            Toast.makeText(ctx, "کوئی رکن رجسٹر نہیں", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ctx, HealthCommittee_Activity.class);
        intent.putExtra("type",type);
        startActivity(intent);
    }
}
