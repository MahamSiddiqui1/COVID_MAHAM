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
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.akdndhrc.covid_module.Adapter.Adt_SocialContact.Adt_HESStartMeetingList;
import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.NothingSelectedSpinnerAdapter;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HES_StartSchoolMeetingList_Activity extends AppCompatActivity {

    Context ctx = HES_StartSchoolMeetingList_Activity.this;

    ListView lv;
    Button btn_jaama_kre;

    Spinner sp_topic,sp_school_list;
    TextView tv_count_male,tv_count_female;
    RelativeLayout rl_add_male,rl_sub_male,rl_add_female,rl_sub_female;
    int counter_male=0, counter_female=0;

   /* ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> hashMapMemberStatus = new ArrayList<HashMap<String, String>>();

    ArrayList<SchoolMeetingClass> mArrayList_status = new ArrayList<>();*/

    //Adt_HESStartMeetingList adt;

    String meeting_uid,login_useruid,TodayDate,type;
    String[][] mData;
    public  static  String var_hes_startMeeting ="0";

    Dialog dialog;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hes_start_meeting_list);

        //meeting_uid = getIntent().getExtras().getString("meeting_uid");
        type = getIntent().getExtras().getString("type");

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences("UserLogin", 0); // 0 - for private mode
            String shared_useruid = prefelse.getString("login_userid", null); // getting String
            login_useruid = shared_useruid;


            Log.d("000457", "USER UID: " + login_useruid);
            Log.d("000457", "Meeting UID: " + meeting_uid);

        } catch (Exception e) {
            Log.d("000457", "Shared Err:" + e.getMessage());
        }

        //ListView
        lv = findViewById(R.id.lv);

        //Floating Button
        btn_jaama_kre = findViewById(R.id.btn_jaama_kre);

        //Spinner
        sp_topic = findViewById(R.id.sp_topic);
        sp_school_list = findViewById(R.id.sp_school_list);


        //TextView
        tv_count_male = findViewById(R.id.tv_count_male);
        tv_count_female = findViewById(R.id.tv_count_female);

        //RelativeLayout
        rl_add_male = findViewById(R.id.rl_add_male);
        rl_sub_male = findViewById(R.id.rl_sub_male);
        rl_add_female = findViewById(R.id.rl_add_female);
        rl_sub_female = findViewById(R.id.rl_sub_female);


        spinner_data();

        counter_male = Integer.parseInt("0");
        counter_female = Integer.parseInt("0");

        tv_count_male.setText("" + counter_male);
        tv_count_female.setText("" + counter_female);


        //Counter Male 15 t0 19 Age
        rl_add_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter_male < 999) {
                    counter_male += 1;
                    tv_count_male.setText("" + counter_male);

                    Log.d("000457", ": " + counter_male);
                } else {
                    tv_count_male.setText("" + counter_male);
                    Log.d("000457", ":: " + counter_male);
                }
            }
        });
        rl_sub_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter_male > 0) {
                    counter_male -= 1;
                    tv_count_male.setText("" + counter_male);
                    Log.d("000457", ": " + counter_male);
                } else {
                    tv_count_male.setText("" + counter_male);
                    Log.d("000457", ": " + counter_male);
                }
            }


        });

        //Counter Female 15 t0 19 Age
        rl_add_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter_female < 999) {
                    counter_female += 1;
                    tv_count_female.setText("" + counter_female);
                    Log.d("000457", "* " + counter_female);
                } else {
                    tv_count_female.setText("" + counter_female);
                    Log.d("000457", "* " + counter_female);
                }
            }
        });
        rl_sub_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter_female > 0) {
                    counter_female -= 1;
                    tv_count_female.setText("" + counter_female);
                    Log.d("000457", "* " + counter_female);
                } else {
                    tv_count_female.setText("" + counter_female);
                    Log.d("000457", "* " + counter_female);
                }
            }
        });



        btn_jaama_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("000457", "SIZEEEEEE: " + Adt_HESStartMeetingList.hashMap_1.size());

                if (sp_topic.getSelectedItemPosition()==0)
                {
                    final Snackbar snackbar = Snackbar.make(v, "برائے  مہربانی موضوع منتخب کریں.", Snackbar.LENGTH_SHORT);
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

                if (sp_school_list.getSelectedItemPosition()==0)
                {
                    final Snackbar snackbar = Snackbar.make(v, "برائے  مہربانی اسکول منتخب کریں.", Snackbar.LENGTH_SHORT);
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

                if (tv_count_male.getText().equals("0") && tv_count_female.getText().equals("0")) {

                    final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی مرد اور عورت کی تعداد درج کریں.", Snackbar.LENGTH_SHORT);
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

                    dialog = new Dialog(ctx);
                    LayoutInflater layout = LayoutInflater.from(ctx);
                    final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

                    dialog.setContentView(dialogView);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("000457", "20000 SEC ");
                        insert_into_db();
                    }
                },2000);

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

            jsonObject.put("topic", "" + sp_topic.getSelectedItem());
            jsonObject.put("count_male", "" + counter_male);
            jsonObject.put("count_female", "" + counter_female);
            jsonObject.put("school_status", "" + "1");

            int total = counter_male + counter_female;
            jsonObject.put("total", "" +total);

            meeting_uid = UUID.randomUUID().toString().replace("-", "");

            String added_on = String.valueOf(System.currentTimeMillis());

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
            Log.d("000457", "Data: " + ans1);
            Log.d("000457", "Query:" + res.toString());

            if (Utils.haveNetworkConnection(ctx) > 0) {

                sendPostRequest(meeting_uid, type,TodayDate, String.valueOf(jsonObject), login_useruid,added_on);
            } else {
                Toast.makeText(ctx, "ڈیٹا جمع ہوگیا ہے", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            dialog.dismiss();
            Log.d("000457", "Err: " + e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            var_hes_startMeeting ="1";
           // Adt_HESStartMeetingList.hashMap_1.clear();
            dialog.dismiss();
            Intent intent = new Intent(ctx, HealthCommittee_Activity.class);
            intent.putExtra("type",type);
            startActivity(intent);


        }

    }

    private void sendPostRequest(final String uid, final String type, final String record_data, final String metadata,final String added_by, final String added_on)
    {

        String url = "https://development.api.teekoplus.akdndhrc.org/sync/save/meetings";

        Log.d("000457", "mURL " + url);


        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {
                        Log.d("000457", "response:    " + response);

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE MEETING SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE uid = '" + uid + "'  AND type ='"+type+"'";

                      //  ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d("000457", "UpdateData: " + update_record);
                        Log.d("000457", "Query:" + res.toString());

                        //  Toast.makeText(BelowTwo_Register_Activity.this, "Data has been saved", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, "ڈیٹا سنک ہوگیا ہے", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000457", "else ");
                        Toast.makeText(ctx, "ڈیٹا سروس پر سینک نہیں ہوا", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000457", "catch: " + e.getMessage());
                    Toast.makeText(ctx, "ڈیٹا سینک نہیں ہوا", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000457", "onErrorResponse: " + error.getMessage());
                // Toast.makeText(BelowTwo_Register_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast.makeText(ctx, "ڈیٹا سینک نہیں ہوا", Toast.LENGTH_SHORT).show();

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

                Log.d("000457", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000457", "map ");
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

        /////////////// Sp_school_list///////////////////


        try {

            final Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            final String[][] mData = ls.executeReader("Select JSON_EXTRACT(metadata, '$.school_name') from MEETING_MEMBER where  type='"+type+"'");

            List a = new ArrayList();

            if (mData != null)
            {
                for(int i=0; i<mData.length; i++){
                    a.add(mData[i][0]);
                }

                Log.d("000457", "Sp_school_list name "+a);
            }
            else{
                a.add("No School Register");
                Toast.makeText(ctx, "کوئی اسکول رجسٹر نہیں", Toast.LENGTH_SHORT).show();
                btn_jaama_kre.setVisibility(View.GONE);
            }


            String[] school_name = (String[]) a.toArray(new String[0]);

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ctx, R.layout.sp_title_topic_layout, school_name);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_school_list.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            spinnerArrayAdapter,
                            R.layout.sp_title_topic_layout,
                            ctx));

            sp_school_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (sp_school_list.getSelectedItemPosition() == 0)
                    {
                        Log.d("000457", "EQUAL To 0");
                    } else {

                            String[][] mData_metadata = ls.executeReader("Select metadata from MEETING_MEMBER where JSON_EXTRACT(metadata, '$.school_name') = '" + sp_school_list.getSelectedItem() + "' AND type='"+type+"'");
                            if (mData_metadata != null)
                            {
                                Log.d("000457", "JSON DATA "+mData_metadata[0][0]);
                                jsonObject = new JSONObject(mData_metadata[0][0]);
                            }
                            else{
                                Log.d("000457", "JSON DATA NULL ");
                            }
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("000457", "JSON Exception "+e.getMessage());
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (Exception ex) {
            Log.d("000457", "Exception school_name "+ex.getMessage());
            ex.printStackTrace();
        }


    }

 /*   @Override
    protected void onResume() {
        super.onResume();

        hashMapArrayList.clear();
        mArrayList_status.clear();

        try {


            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

          mData = ls.executeReader("Select JSON_EXTRACT(metadata, '$.school_name'),JSON_EXTRACT(metadata, '$.school_headname'),member_status,member_uid from MEETING_MEMBER where meeting_uid = '" + meeting_uid + "' AND type='"+type+"'");

            if (mData != null) {
                HashMap<String, String> map;
                for (int i = 0; i < mData.length; i++) {

                    Log.d("000457", "school_name: " +  mData[i][0]);
                    Log.d("000457", "school_headname " +  mData[i][1]);


                    SchoolMeetingClass schoolMeetingClass = new SchoolMeetingClass("0","0");

                    map = new HashMap<>();

                    map.put("school_name", "" + mData[i][0]);
                    map.put("school_headname", "" + mData[i][1]);
//                    map.put("age", "" + mData[i][2]);
//                    map.put("member_status", "" + mData[i][3]);
//                    map.put("member_uid", "" + mData[i][4]);

                    hashMapArrayList.add(map);

                    mArrayList_status.add(schoolMeetingClass);

                }
               // Log.d("000457", "hashMapArrayList: " + hashMapArrayList);
            }
            else {
                Log.d("000457", "DATA NULLLLLLLL ");
            }

            adt = new Adt_HESStartMeetingList(ctx, hashMapArrayList,mArrayList_status);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);


        } catch (Exception e) {
            Log.d("12345", "Error: " + e.getMessage());
            Toast.makeText(ctx, "Something wrong !!", Toast.LENGTH_SHORT).show();
        }


    }*/

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

        Log.d("12345", "Error 1: " + Adt_HESStartMeetingList.hashMap_1.size());
     //   showAlert();

        Intent intent = new Intent(ctx, HealthCommittee_Activity.class);
        intent.putExtra("type",type);
        startActivity(intent);
        var_hes_startMeeting = "0";

    }

    public void showAlert() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);

        dialogBuilder.setMessage("کیا آپ  ڈیٹا بغیر محفوظ کیے میٹنگ اسکرین سے باہر جانا چاہتے ہیں؟");
        dialogBuilder.setPositiveButton("جی ہاں", null);
        dialogBuilder.setNegativeButton("جی نہیں", null);


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
            //    Adt_HESStartMeetingList.hashMap_1.clear();
                Intent intent = new Intent(ctx, HealthCommittee_Activity.class);
                intent.putExtra("type",type);
                startActivity(intent);
                var_hes_startMeeting = "0";

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
