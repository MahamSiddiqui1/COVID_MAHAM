package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SocialContactActivities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.Adt_SocialContact.Adt_HCMStartMeetingList;
import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.CustomClass.CheckListClass;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.NothingSelectedSpinnerAdapter;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HCM_StartMeetingList_Activity extends AppCompatActivity {

    Context ctx = HCM_StartMeetingList_Activity.this;

    ListView lv;
    Button btn_jaama_kre;

    Spinner sp_topic;

    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> hashMapMemberStatus = new ArrayList<HashMap<String, String>>();

    ArrayList<CheckListClass> mArrayList_status = new ArrayList<>();

    Adt_HCMStartMeetingList adt;

    String meeting_uid,login_useruid,TodayDate,type,added_on;
    String[][] mData;
    public  static  String var_hcm_startMeeting ="0";

    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hcm_start_meeting_list);

      //  meeting_uid = getIntent().getExtras().getString("meeting_uid");
        type = getIntent().getExtras().getString("type");

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
            String shared_useruid = prefelse.getString(("login_userid"), null); // getting String
            login_useruid = shared_useruid;


            Log.d("000357", "USER UID: " + login_useruid);
            //Log.d("000357", "Meeting UID: " + meeting_uid);

        } catch (Exception e) {
            Log.d("000357", "Shared Err:" + e.getMessage());
        }

        //ListView
        lv = findViewById(R.id.lv);

        //Floating Button
        btn_jaama_kre = findViewById(R.id.btn_jaama_kre);

        //Spinner
        sp_topic = findViewById(R.id.sp_topic);


        spinner_data();

        btn_jaama_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("000457", "SIZEEEEEE: " + Adt_HCMStartMeetingList.hashMap_1.size());

                if (sp_topic.getSelectedItemPosition()==0)
                {
                    final Snackbar snackbar = Snackbar.make(v, R.string.selectTopicPrompt, Snackbar.LENGTH_SHORT);
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

                if (Adt_HCMStartMeetingList.hashMap_1.size() > 0){

                    dialog = new Dialog(ctx);
                    LayoutInflater layout = LayoutInflater.from(ctx);
                    final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

                    dialog.setContentView(dialogView);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();


                Lister ls = new Lister(ctx);
                ls.createAndOpenDB();

                try {



                    for (String key_subIndicator : Adt_HCMStartMeetingList.hashMap_1.keySet()) {
                        System.out.println("------------------------------------------------");
                        System.out.println("Iterating or looping map using java5 foreach loop");
                        System.out.println("key: " + key_subIndicator + " value: " + Adt_HCMStartMeetingList.hashMap_1.get(key_subIndicator));
                        Log.d("000999", "HASH MAP 1: " + "key_subIndicator: " + key_subIndicator + " Answers: " + Adt_HCMStartMeetingList.hashMap_1.get(key_subIndicator));
                        Log.d("000999", "SIZEEEEEEE: " + Adt_HCMStartMeetingList.hashMap_1.keySet().size());

                        try {

                            String[][] memberStatus_data = ls.executeReader("Select JSON_EXTRACT(metadata, '$.full_name'),JSON_EXTRACT(metadata, '$.gender'),JSON_EXTRACT(metadata, '$.age'),member_status,member_uid," +
                                    " JSON_EXTRACT(metadata, '$.mobile_number'),JSON_EXTRACT(metadata, '$.address'),JSON_EXTRACT(metadata, '$.expertise') " +
                                    " from MEETING_MEMBER where member_uid = '" + Adt_HCMStartMeetingList.hashMap_1.get(key_subIndicator) + "' AND type='" + type + "'");

                            if (memberStatus_data != null) {
                                HashMap<String, String> map;
                                for (int i = 0; i < memberStatus_data.length; i++) {

                                    map = new HashMap<>();

                                    map.put("member_uid", "" + memberStatus_data[i][4]);
                                    map.put("full_name", "" + memberStatus_data[i][0]);
                                    map.put("gender", "" + memberStatus_data[i][1]);
                                    map.put("age", "" + memberStatus_data[i][2]);
                                    map.put("mobile_number", "" + memberStatus_data[i][5]);
                                    map.put("address", "" + memberStatus_data[i][6]);
                                    map.put("expertise", "" + memberStatus_data[i][7]);
                                    map.put("member_status", "" + "1");
                                    map.put("topic", "" + sp_topic.getSelectedItem());
                                    map.put("total", "" + Adt_HCMStartMeetingList.hashMap_1.keySet().size());


                                    hashMapMemberStatus.add(map);

                                }
                                Log.d("000357", "hashMapMemberStatus: " + hashMapMemberStatus);
                            } else {
                                Log.d("000357", "DATA NULL ");
                            }

                        } catch (Exception e) {
                            dialog.dismiss();
                            Log.d("000999", "Failed:" + e.getMessage());
                        }
                    }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("000357", "20000 SEC ");
                                insert_into_db();

                            }
                        }, 2000);

                            /*String mData_update ="UPDATE MEETING_MEMBER SET " +
                                    "member_status='" + Adt_HCMStartMeetingList.hashMap_1.get(key_subIndicator) + "'" +
                                    " where " +
                                    " meeting_uid = '" + meeting_uid + "' " +
                                    " and " +
                                    " member_uid = '" +mData[Integer.parseInt(key_subIndicator)][4] + "' "+
                                    " and " +
                                    " type = '"+type+"' ";

                            Boolean res = ls.executeNonQuery(mData_update);
                            Log.d("000999", "MEETING MEMBER Data: " + mData_update);
                            Log.d("000999", " Query: " + res.toString());
*/

                            //((Activity) ctx).finish();

                         //   ls.closeDB();




                } catch (Exception e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Log.d("000999", "ERR " +e.getMessage());
                }
                }
                else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.checkMemListPrompt, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(15);
                    snackbar.setDuration(4000);
                    snackbar.show();
                }
            }
        });

    }

    private void insert_into_db() {


        try {
            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            TodayDate = dates.format(c.getTime());

            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            meeting_uid = UUID.randomUUID().toString().replace("-", "");
            added_on = String.valueOf(System.currentTimeMillis());

            JSONArray jsonObject = new JSONArray(hashMapMemberStatus);

            String ans1 = "insert into MEETING(uid, type, metadata, meeting_topic,record_data,meeting_status,added_by,is_synced,added_on)" +
                    "values" +
                    "(" +
                    "'" + meeting_uid + "'," +
                    "'" + type + "'," +
                    "'" + jsonObject.toString() + "'," +
                    "'" + String.valueOf(sp_topic.getSelectedItemPosition() - 1) + "'," +
                    "'" + TodayDate + "'," +
                    "'1'," +
                    "'" + login_useruid + "'," +
                    "'0'," +
                    "'" + added_on + "'" +
                    ")";

            Boolean res = ls.executeNonQuery(ans1);
            Log.d("000357", "Data: " + ans1);
            Log.d("000357", "Query:" + res.toString());

            if (Utils.haveNetworkConnection(ctx) > 0) {

                sendPostRequest(meeting_uid, type,TodayDate, String.valueOf(hashMapMemberStatus), login_useruid,added_on);
            } else {
                Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            dialog.dismiss();
            Log.d("000357", "Err: " + e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            var_hcm_startMeeting ="1";
            Adt_HCMStartMeetingList.hashMap_1.clear();
            dialog.dismiss();
            Intent intent = new Intent(ctx, HealthCommittee_Activity.class);
            intent.putExtra("type",type);
            startActivity(intent);


        }

    }

    private void sendPostRequest(final String uid, final String type, final String record_data, final String metadata,final String added_by, final String added_on) {

        String url = "https://development.api.teekoplus.akdndhrc.org/sync/save/meetings";

        Log.d("000357", "mURL " + url);


        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {
                        Log.d("000357", "response:    " + response);

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE MEETING SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE uid = '" + uid + "'  AND type ='"+type+"'";

                      //  ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d("000357", "UpdateData: " + update_record);
                        Log.d("000357", "Query:" + res.toString());

                        //  Toast.makeText(BelowTwo_Register_Activity.this, "Data has been saved", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000357", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.d("000357", "catch: " + e.getMessage());
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000357", "onErrorResponse: " + error.getMessage());
                // Toast.makeText(BelowTwo_Register_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", uid);
                params.put("type", type);
                params.put("record_data", record_data);
                params.put("metadata", metadata);
                params.put("added_by", added_by);
                params.put("added_on", added_on);

                Log.d("000357", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000357", "map ");
                Map<String, String> params = new HashMap<String, String>();
                // params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }


    private void spinner_data() {

        // Sp gender
        final ArrayAdapter<CharSequence> adptr_topic = ArrayAdapter.createFromResource(this, R.array.array_health_committee_topic, R.layout.sp_title_topic_layout);
        adptr_topic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_topic.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_topic,
                        R.layout.sp_title_topic_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));


        sp_topic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        hashMapArrayList.clear();
        mArrayList_status.clear();

        try {


            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

          //mData = ls.executeReader("Select JSON_EXTRACT(metadata, '$.full_name'),JSON_EXTRACT(metadata, '$.gender'),JSON_EXTRACT(metadata, '$.age'),member_status,member_uid from MEETING_MEMBER where meeting_uid = '" + meeting_uid + "' AND type='"+type+"'");
          mData = ls.executeReader("Select JSON_EXTRACT(metadata, '$.full_name'),JSON_EXTRACT(metadata, '$.gender'),JSON_EXTRACT(metadata, '$.age'),member_status,member_uid from MEETING_MEMBER where type='"+type+"'");

            if (mData != null) {
                HashMap<String, String> map;
                for (int i = 0; i < mData.length; i++) {

                    Log.d("000357", "MEMBER NAME: " +  mData[i][0]);
                    Log.d("000357", "MEMBER GENDER " +  mData[i][1]);
                    Log.d("000357", "MEMBER AGE " +  mData[i][2]);

                    CheckListClass checkListClass = new CheckListClass(mData[i][1],mData[i][3]);

                    map = new HashMap<>();

                    map.put("full_name", "" + mData[i][0]);
                    map.put("gender", "" + mData[i][1]);
                    map.put("age", "" + mData[i][2]);
                    map.put("member_status", "" + mData[i][3]);
                    map.put("member_uid", "" + mData[i][4]);

                    hashMapArrayList.add(map);

                    mArrayList_status.add(checkListClass);

                }
               // Log.d("000357", "hashMapArrayList: " + hashMapArrayList);
            }
            else {
                Log.d("000357", "DATA NULLLLLLLL ");
                Toast.makeText(ctx, R.string.noMemRegistered, Toast.LENGTH_SHORT).show();
                btn_jaama_kre.setVisibility(View.GONE);
            }

            adt = new Adt_HCMStartMeetingList(ctx, hashMapArrayList,mArrayList_status);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);


        } catch (Exception e) {
            Log.d("12345", "Error: " + e.getMessage());
            Toast.makeText(ctx, R.string.noMemRegistered, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

        Log.d("12345", "Error 1: " + Adt_HCMStartMeetingList.hashMap_1.size());
       // showAlert();

        Adt_HCMStartMeetingList.hashMap_1.clear();
        Intent intent = new Intent(ctx, HealthCommittee_Activity.class);
        intent.putExtra("type",type);
        startActivity(intent);
        var_hcm_startMeeting = "0";
    }

    public void showAlert() {

        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(ctx);

        dialogBuilder.setMessage(R.string.wantoExitMeetingScreen);
        dialogBuilder.setPositiveButton(R.string._jee_haan, null);
        dialogBuilder.setNegativeButton(R.string._gee_nahi, null);


        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        TextView messageView = (TextView)alertDialog.findViewById(android.R.id.message);
        messageView.setTextSize(17);
        messageView.setTypeface(Typeface.DEFAULT_BOLD);
        messageView.setTextColor(ctx.getResources().getColor(R.color.dark_blue_color));

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        // override the text color of positive button
        positiveButton.setTextColor(ctx.getResources().getColor(R.color.pink_color));
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                Adt_HCMStartMeetingList.hashMap_1.clear();
                Intent intent = new Intent(ctx, HealthCommittee_Activity.class);
                intent.putExtra("type",type);
                startActivity(intent);
                var_hcm_startMeeting = "0";

            }
        });

        negativeButton.setTextColor(ctx.getResources().getColor(R.color.pink_color));
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
    }

}
