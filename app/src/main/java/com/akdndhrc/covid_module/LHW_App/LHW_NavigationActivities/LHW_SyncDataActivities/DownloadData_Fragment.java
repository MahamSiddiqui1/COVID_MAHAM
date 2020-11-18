package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SyncDataActivities;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import static com.akdndhrc.covid_module.Utils.haveNetworkConnection;


public class DownloadData_Fragment extends Fragment {

    ImageView iv_navigation_drawer, iv_home;
    RelativeLayout rl_khandan_reg, rl_reg, rl_child_hifaziti_teekey_record, rl_mother_hifaziti_teekey_record;

    TextView txt_khandan_reg, txt_reg, txt_child_hifaziti_teekey_record, txt_mother_hifaziti_teekey_record;
    ProgressBar pb_2, pb_3, pb_4;

    Dialog alertDialog;


    String login_useruid;

    ProgressBar pbProgress;
    ImageView image_tick;
    TextView message, tvCount,tvDataCount;
    LinearLayout ll_progress, ll_done;

    Lister ls;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    int i = 0;
    JSONArray jobj;

    public DownloadData_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_download_data, container, false);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(getContext(), DownloadData_Fragment.class));

        Log.d("000564", "ONCREATEEE");

        ls = new Lister(getContext());
        ls.createAndOpenDB();

        //Get shared USer name
        try {
            SharedPreferences prefelse = getActivity().getApplicationContext().getSharedPreferences("UserLogin", 0); // 0 - for private mode

            String shared_useruid = prefelse.getString("login_userid", null); // getting String
            login_useruid = shared_useruid;

            Log.d("000564", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000564", "Shared Err:" + e.getMessage());
        }


        //ImageView``
        iv_navigation_drawer = rootView.findViewById(R.id.iv_navigation_drawer);
        iv_home = rootView.findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);


        //Relative Layout
        rl_khandan_reg = rootView.findViewById(R.id.rl_khandan_reg);
        rl_reg = rootView.findViewById(R.id.rl_reg);
        rl_child_hifaziti_teekey_record = rootView.findViewById(R.id.rl_child_hifaziti_teekey_record);
        rl_mother_hifaziti_teekey_record = rootView.findViewById(R.id.rl_mother_hifaziti_teekey_record);

        //TextView
        txt_khandan_reg = rootView.findViewById(R.id.txt_khandan_reg);
        txt_reg = rootView.findViewById(R.id.txt_reg);
        txt_child_hifaziti_teekey_record = rootView.findViewById(R.id.txt_child_hifaziti_teekey_record);
        txt_mother_hifaziti_teekey_record = rootView.findViewById(R.id.txt_mother_hifaziti_teekey_record);

        //ProgressBar
        pb_2 = rootView.findViewById(R.id.pb_2);
        pb_3 = rootView.findViewById(R.id.pb_3);
        pb_4 = rootView.findViewById(R.id.pb_4);


        rl_khandan_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Download_KhandanData(v);
            }
        });

        rl_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Download_MemberData(v);
            }
        });


        rl_child_hifaziti_teekey_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Download_CVaccinationData(v);
            }
        });

        rl_mother_hifaziti_teekey_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Download_MVaccinationData(v);
            }
        });


        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getContext(), HomePage_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
            }
        });

        return rootView;
    }

    private void Download_KhandanData(View v) {

        if (haveNetworkConnection(getContext()) > 0) {

            i=0;
            new Khandan_Task().execute();

/*
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    sendPostRequest_Khandan();

                }
            }, 2500);
*/

        } else {

            final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں.", Snackbar.LENGTH_SHORT);
            View mySbView = snackbar.getView();
            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
            TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(15);
            snackbar.setDuration(3000);
            snackbar.show();

          /*  alertDialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog);
            LayoutInflater layout = LayoutInflater.from(getContext());
            final View dialogView = layout.inflate(R.layout.lay_dialog_loading6, null);

            alertDialog.setContentView(dialogView);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.show();

            pbProgress = dialogView.findViewById(R.id.pbProcessing);
            image_tick = dialogView.findViewById(R.id.image_tick);
            message = dialogView.findViewById(R.id.message);
            tvCount = dialogView.findViewById(R.id.tvCount);

            //LinearLayout
            ll_progress = dialogView.findViewById(R.id.ll_progress_layout);
            ll_done = dialogView.findViewById(R.id.ll_done_layout);


            // Set the progress status zero on each button click
            progressStatus = 0;


                    A Thread is a concurrent unit of execution. It has its own call stack for
                    methods being invoked, their arguments and local variables. Each application
                    has at least one thread running when it is started, the main thread,
                    in the main ThreadGroup. The runtime keeps its own threads
                    in the system thread group.

            // Start the lengthy operation in a background thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (progressStatus < 10) {
                        // Update the progress status
                        progressStatus += 1;

                        Log.d("000654", "AC:" + progressStatus);
                        // Try to sleep the thread for 20 milliseconds
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Update the progress bar
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //      pbProgress.setProgress(progressStatus);
                                // Show the progress on TextView
                                tvCount.setText(progressStatus + "/" + "300");
                            }
                        });
                    }
                    ll_progress.setVisibility(View.GONE);
                    // ll_done.setVisibility(View.VISIBLE);
                }
            }).start(); // Start the operation
*/
        }
    }
    private void sendPostRequest_Khandan() {

        String url = "https://development.api.teekoplus.akdndhrc.org/sync/fetch/families";

        Log.d("000564", "mURL " + url);

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("000564", "Khandan API Response !!:    " + response);

                try {
                    final Lister ls = new Lister(getContext());
                    ls.createAndOpenDB();

                    jobj = new JSONArray(response);
                    // Log.d("000564", "onResponse!!!!!!!!!!!!: " + jobj.toString());
                    //boolean m1 = ls.executeNonQuery(Helper.CREATE_TABLE_KHANDAN);
                    Log.d("000564", "Khandan JSONARRAY Leng:   " + jobj.length());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                final String[][] read_Khandan_data = ls.executeReader("Select count(*) from KHANDAN where is_synced = '2'");
                                if (Integer.parseInt(read_Khandan_data[0][0]) < jobj.length()) {
                                    Log.d("000564", "START Data DOWNLOADED:  ");

                                    for (i = 0; i < jobj.length(); i++) {

                                   /*   final String[][] read_Khandan_data;
                                  read_Khandan_data = ls.executeReader("Select uid from KHANDAN where uid = '" + jobj.getJSONObject(i).getString("uid") + "'");
                                    if (read_Khandan_data != null) {
                                        // Log.d("000564", "Data UID:   " + read_member_data[0][0]);
                                        Log.d("000564", "Data ALREADY DOWNLOADED:   ");
                                    } else {}*/

//                                        Log.d("000564", " UID not in table:");

                                        String query_form_get_data = null;
                                        query_form_get_data = "insert or ignore into KHANDAN (uid, manual_id, province_id, district_id, subdistrict_id, uc_id, village_id,family_head_name,family_address, water_source,toilet_facility, added_by,is_synced,added_on)" +
                                                "values" +
                                                "(" +
                                                "'" + jobj.getJSONObject(i).getString("uid") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("manual_id") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("province_id") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("district_id") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("tehsil_id") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("uc_id") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("village_id") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("family_head_name") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("family_address") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("water_source") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("toilet_facility") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("added_by") + "'," +
                                                "'2'," +
                                                "'" + jobj.getJSONObject(i).getString("added_on") + "'" +
                                                ")";

                                        Boolean res = ls.executeNonQuery(query_form_get_data);
                                        Log.d("000564", "Khandan Data Query: " + query_form_get_data);
                                        Log.d("000564", "Query: " + res.toString());
                                        Log.d("000564", "Data Received !!!!! ");

                                        try {
                                            Thread.sleep(500);
                                            Log.d("000564", "************* ");
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        // Update the progress bar
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                tvCount.setText(i + "/" + jobj.length());
                                            }
                                        });
                                        //}

                                        if (i == jobj.length() - 1) {
                                            Log.d("000564", "IF !!!!!!!!");
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ll_progress.setVisibility(View.GONE);
                                                    ll_done.setVisibility(View.VISIBLE);
                                                    tvDataCount.setText(i + "/" + jobj.length());

                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Log.d("000564", "+++++++++++++++++++ ");
                                                            alertDialog.dismiss();
                                                            Khandan_registration();
                                                        }
                                                    }, 4000);
                                                }
                                            });

                                  /*  ll_progress.setVisibility(View.GONE);
                                    ll_done.setVisibility(View.VISIBLE);*/

                                   /* new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            alertDialog.dismiss();
                                            //member_registration();
                                            onResume();
                                        }
                                    }, 4000);*/

                                        } else {
                                            Log.d("000564", "ELSEEEEE !!!!!!!");
                                        }

                                    }
                                }
                                else{
                                    Log.d("000564", "Data ALREADY DOWNLOADED: ");

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ll_progress.setVisibility(View.GONE);
                                            ll_done.setVisibility(View.VISIBLE);
                                            message.setText("ڈیٹا پہلے سے ہی ڈاؤن لوڈ ہے.");
                                            tvDataCount.setText(read_Khandan_data[0][0] + "/" + jobj.length());

                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d("000564", "+++++++++++++++++++ ");
                                                    alertDialog.dismiss();
                                                }
                                            }, 4000);
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                alertDialog.dismiss();
                                Log.d("000564", "Err:    " + e.getMessage());
                                Toast.makeText(getContext(), "Something wrong !!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).start(); // Start the operation

                } catch (Exception e) {
                    alertDialog.dismiss();
                    Log.d("000564", "Thread Catch:    " + e.getMessage());
                    Toast.makeText(getContext(), "Something wrong !!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alertDialog.dismiss();
                Log.d("000564", "error:    " + error.getMessage());
                Toast.makeText(getContext(), "برائے مہربانی انٹرنیٹ کنکشن چیک کریں.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", login_useruid);

                Log.d("000564", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000564", "map ");
                Map<String, String> params = new HashMap<String, String>();
                // params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq1, REQUEST_TAG);
    }


    private void Download_MemberData(View v) {

        if (haveNetworkConnection(getContext()) > 0) {

            i=0;
            new Member_Task().execute();

        } else {

            final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں.", Snackbar.LENGTH_SHORT);
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
    private void sendPostRequest_Member() {

        String url = "https://development.api.teekoplus.akdndhrc.org/sync/fetch/families/members";

        Log.d("000564", "mURL " + url);

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("000565", "Member API Response !!:    " + response);

                try {
                    final Lister ls = new Lister(getContext());
                    ls.createAndOpenDB();

                    jobj = new JSONArray(response);
                    // Log.d("000564", "onResponse!!!!!!!!!!!!: " + jobj.toString());
                    //boolean m1 = ls.executeNonQuery(Helper.CREATE_TABLE_KMEMBER);
                    Log.d("000565", "Member JSONARRAY Leng:   " + jobj.length());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                final String[][] read_member_data = ls.executeReader("Select count(*) from MEMBER where is_synced = '2'");
                                if (Integer.parseInt(read_member_data[0][0]) < jobj.length()) {
                                    Log.d("000565", "START Data DOWNLOADED:  ");

                                    for (i = 0; i < jobj.length(); i++) {

                                        String query_member_data = null;
                                        query_member_data = "insert or ignore into MEMBER (uid, manual_id, khandan_id, full_name, nicnumber, phone_number, data,gender,age, dob,bio_code,qr_code, added_by,is_synced,added_on)" +
                                                "values" +
                                                "(" +
                                                "'" + jobj.getJSONObject(i).getString("uid") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("manual_id") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("family_id") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("full_name") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("nic_number") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("phone_number") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("data") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("gender") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("age") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("dob") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("bio_code") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("qr_code") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("added_by") + "'," +
                                                "'2'," +
                                                "'" + jobj.getJSONObject(i).getString("added_on") + "'" +
                                                ")";

                                        Boolean res = ls.executeNonQuery(query_member_data);
                                        Log.d("000565", "MEMBER Data Query: " + query_member_data);
                                        Log.d("000565", "Query: " + res.toString());
                                        Log.d("000565", "Data Received !!!!! ");

                                        try {
                                            Thread.sleep(500);
                                            Log.d("000565", "************* ");
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        // Update the progress bar
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                tvCount.setText(i + "/" + jobj.length());
                                            }
                                        });
                                        //}

                                        if (i == jobj.length() - 1) {
                                            Log.d("000565", "IF !!!!!!!!");
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ll_progress.setVisibility(View.GONE);
                                                    ll_done.setVisibility(View.VISIBLE);
                                                    tvDataCount.setText(i + "/" + jobj.length());

                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Log.d("000565", "+++++++++++++++++++ ");
                                                            alertDialog.dismiss();
                                                            member_registration();
                                                        }
                                                    }, 4000);
                                                }
                                            });

                                        } else {
                                            Log.d("000565", "ELSEEEEE !!!!!!!");
                                        }

                                    }
                                }
                                else{
                                    Log.d("000565", "Data ALREADY DOWNLOADED: ");

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ll_progress.setVisibility(View.GONE);
                                            ll_done.setVisibility(View.VISIBLE);
                                            message.setText("ڈیٹا پہلے سے ہی ڈاؤن لوڈ ہے.");
                                            tvDataCount.setText(read_member_data[0][0] + "/" + jobj.length());

                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d("000565", "+++++++++++++++++++ ");
                                                    alertDialog.dismiss();
                                                }
                                            }, 4000);
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                alertDialog.dismiss();
                                Log.d("000565", "Err:    " + e.getMessage());
                                Toast.makeText(getContext(), "Something wrong !!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).start(); // Start the operation

                } catch (Exception e) {
                    alertDialog.dismiss();
                    Log.d("000565", "Thread Catch:    " + e.getMessage());
                    Toast.makeText(getContext(), "Something wrong !!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alertDialog.dismiss();
                Log.d("000565", "error:    " + error.getMessage());
                Toast.makeText(getContext(), "برائے مہربانی انٹرنیٹ کنکشن چیک کریں.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", login_useruid);

                Log.d("000565", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000565", "map ");
                Map<String, String> params = new HashMap<String, String>();
                 //params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq1, REQUEST_TAG);
    }


    private void Download_CVaccinationData(View v) {

        if (haveNetworkConnection(getContext()) > 0) {

            i=0;
            new CVaccination_Task().execute();

        } else {


            final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں.", Snackbar.LENGTH_SHORT);
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
    private void sendPostRequest_CVaccination() {

        String url = "https://development.api.teekoplus.akdndhrc.org/sync/fetch/child/vaccinations";

        Log.d("000566", "mURL " + url);

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("000566", "CVACCINATION API Response !!:    " + response);


                try {
                    final Lister ls = new Lister(getContext());
                    ls.createAndOpenDB();

                    jobj = new JSONArray(response);
                   // boolean m1 = ls.executeNonQuery(Helper.CREATE_TABLE_CHILD_VACINATION);
                    Log.d("000566", "CVACCINATION JSONARRAY Leng:   " + jobj.length());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                final String[][] read_cVaccination_data = ls.executeReader("Select count(*) from CVACCINATION where is_synced = '3'");
                                if (Integer.parseInt(read_cVaccination_data[0][0]) < jobj.length()) {
                                    Log.d("000566", "START Data DOWNLOADED:  ");

                                    for (i = 0; i < jobj.length(); i++) {

                                        String query_CVaccination_data = null;
                                        query_CVaccination_data = "insert or ignore into CVACCINATION (member_uid, vaccine_id, record_data, type, due_date,vaccinated_on, image_location, metadata, added_by,is_synced,added_on)" +
                                                "values" +
                                                "(" +
                                                "'" + jobj.getJSONObject(i).getString("member_id") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("vaccine_id") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("record_data") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("type") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("due_date") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("vaccinated_on") + "'," +
                                                "'none'," +
                                                "'" + jobj.getJSONObject(i).getString("metadata") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("added_by") + "'," +
                                                "'3'," +
                                                "'" + jobj.getJSONObject(i).getString("added_on") + "'" +
                                                ")";

                                        Boolean res = ls.executeNonQuery(query_CVaccination_data);
                                        Log.d("000566", "CVACCINATION Data Query: " + query_CVaccination_data);
                                        Log.d("000566", "Query: " + res.toString());
                                        Log.d("000566", "Data Received !!!!! ");

                                        try {
                                            Thread.sleep(500);
                                            Log.d("000566", "************* ");
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        // Update the progress bar
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                tvCount.setText(i + "/" + jobj.length());
                                            }
                                        });
                                        //}

                                        if (i == jobj.length() - 1) {
                                            Log.d("000566", "IF !!!!!!!!");
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ll_progress.setVisibility(View.GONE);
                                                    ll_done.setVisibility(View.VISIBLE);
                                                    tvDataCount.setText(i + "/" + jobj.length());

                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Log.d("000566", "+++++++++++++++++++ ");
                                                            alertDialog.dismiss();
                                                            child_vaccination();
                                                        }
                                                    }, 4000);
                                                }
                                            });

                                        } else {
                                            Log.d("000566", "ELSEEEEE !!!!!!!");
                                        }

                                    }
                                }
                                else{
                                    Log.d("000566", "Data ALREADY DOWNLOADED: ");

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ll_progress.setVisibility(View.GONE);
                                            ll_done.setVisibility(View.VISIBLE);
                                            message.setText("ڈیٹا پہلے سے ہی ڈاؤن لوڈ ہے.");
                                            tvDataCount.setText(read_cVaccination_data[0][0] + "/" + jobj.length());

                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d("000566", "+++++++++++++++++++ ");
                                                    alertDialog.dismiss();
                                                }
                                            }, 4000);
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                alertDialog.dismiss();
                                Log.d("000566", "Err:    " + e.getMessage());
                                Toast.makeText(getContext(), "Something wrong !!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).start(); // Start the operation

                } catch (Exception e) {
                    alertDialog.dismiss();
                    Log.d("000566", "Thread Catch:    " + e.getMessage());
                    Toast.makeText(getContext(), "Something wrong !!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alertDialog.dismiss();
                Log.d("000566", "error:    " + error.getMessage());
                Toast.makeText(getContext(), "برائے مہربانی انٹرنیٹ کنکشن چیک کریں.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", login_useruid);

                Log.d("000566", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000566", "map ");
                Map<String, String> params = new HashMap<String, String>();
                // params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq1, REQUEST_TAG);
    }


    private void Download_MVaccinationData(View v) {

        if (haveNetworkConnection(getContext()) > 0) {

            new MVaccination_Task().execute();

        } else {


            final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں.", Snackbar.LENGTH_SHORT);
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
    private void sendPostRequest_MVaccination() {

        String url = "https://development.api.teekoplus.akdndhrc.org/sync/fetch/mother/vaccinations";

        Log.d("000567", "mURL " + url);

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("000567", "MVACINE API Response !!:    " + response);


                try {
                    final Lister ls = new Lister(getContext());
                    ls.createAndOpenDB();

                    jobj = new JSONArray(response);
                    //boolean m1 = ls.executeNonQuery(Helper.CREATE_TABLE_MOTHER_VACINATION);
                    Log.d("000567", "MVACINE JSONARRAY Leng:   " + jobj.length());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                final String[][] read_MVaccination_data = ls.executeReader("Select count(*) from MVACINE where is_synced = '3'");
                                if (Integer.parseInt(read_MVaccination_data[0][0]) < jobj.length()) {
                                    Log.d("000567", "START Data DOWNLOADED:  ");

                                    for (i = 0; i < jobj.length(); i++) {

                                        String query_MVaccination_data = null;
                                        query_MVaccination_data = "insert or ignore into MVACINE (member_uid, record_data,data,vaccine_id,type,vaccinated_on,image_location,added_by,is_synced,added_on)" +
                                                "values" +
                                                "(" +
                                                "'" + jobj.getJSONObject(i).getString("member_id") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("record_data") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("metadata") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("vaccine_id") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("type") + "'," +
                                                "'" + jobj.getJSONObject(i).getString("vaccinated_on") + "'," +
                                                "'none'," +
                                                "'" + jobj.getJSONObject(i).getString("added_by") + "'," +
                                                "'3'," +
                                                "'" + jobj.getJSONObject(i).getString("added_on") + "'" +
                                                ")";
                                        Boolean res = ls.executeNonQuery(query_MVaccination_data);
                                        Log.d("000567", "MVACINE Data Query: " + query_MVaccination_data);
                                        Log.d("000567", "Query: " + res.toString());
                                        Log.d("000567", "Data Received !!!!! ");

                                        try {
                                            Thread.sleep(500);
                                            Log.d("000567", "************* ");
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        // Update the progress bar
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                tvCount.setText(i + "/" + jobj.length());
                                            }
                                        });
                                        //}

                                        if (i == jobj.length() - 1) {
                                            Log.d("000567", "IF !!!!!!!!");
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ll_progress.setVisibility(View.GONE);
                                                    ll_done.setVisibility(View.VISIBLE);
                                                    tvDataCount.setText(i + "/" + jobj.length());

                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Log.d("000567", "+++++++++++++++++++ ");
                                                            alertDialog.dismiss();
                                                            mother_vaccination();
                                                        }
                                                    }, 4000);
                                                }
                                            });

                                        } else {
                                            Log.d("000567", "ELSEEEEE !!!!!!!");
                                        }

                                    }
                                }
                                else{
                                    Log.d("000567", "Data ALREADY DOWNLOADED: ");

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ll_progress.setVisibility(View.GONE);
                                            ll_done.setVisibility(View.VISIBLE);
                                            message.setText("ڈیٹا پہلے سے ہی ڈاؤن لوڈ ہے.");
                                            tvDataCount.setText(read_MVaccination_data[0][0] + "/" + jobj.length());

                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d("000567", "+++++++++++++++++++ ");
                                                    alertDialog.dismiss();
                                                }
                                            }, 4000);
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                alertDialog.dismiss();
                                Log.d("000567", "Err:    " + e.getMessage());
                                Toast.makeText(getContext(), "Something wrong !!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).start(); // Start the operation

                } catch (Exception e) {
                    alertDialog.dismiss();
                    Log.d("000567", "Thread Catch:    " + e.getMessage());
                    Toast.makeText(getContext(), "Something wrong !!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alertDialog.dismiss();
                Log.d("000567", "error:    " + error.getMessage());
                Toast.makeText(getContext(), "برائے مہربانی انٹرنیٹ کنکشن چیک کریں.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", login_useruid);

                Log.d("000567", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000567", "map ");
                Map<String, String> params = new HashMap<String, String>();
              //  params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq1, REQUEST_TAG);
    }


    class Khandan_Task extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            Log.d("000333", "ON PREEEEE: ");

            alertDialog = new Dialog(getContext());
            LayoutInflater layout = LayoutInflater.from(getContext());
            final View dialogView = layout.inflate(R.layout.lay_dialog_loading6, null);

            alertDialog.setContentView(dialogView);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.show();

           /* pbProgress = dialogView.findViewById(R.id.pbProcessing);
            image_tick = dialogView.findViewById(R.id.image_tick);*/
            tvDataCount = dialogView.findViewById(R.id.tvDataCount);
            message = dialogView.findViewById(R.id.tvmessage_download_complete);
            tvCount = dialogView.findViewById(R.id.tvCountData);

            //LinearLayout
            ll_progress = dialogView.findViewById(R.id.ll_progress_layout);
            ll_done = dialogView.findViewById(R.id.ll_done_layout);

            ll_progress.setVisibility(View.VISIBLE);
            ll_done.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                sendPostRequest_Khandan();
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("000564", "ER !!!!! " + e.getMessage());

            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("000564", "ON EXECUTE: ");
           /* ll_progress.setVisibility(View.GONE);
            ll_done.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                    Khandan_registration();
                }
            }, 4000);*/
            super.onPostExecute(result);
        }

    }

    class Member_Task extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            Log.d("000333", "ON PREEEEE: ");

            alertDialog = new Dialog(getContext());
            LayoutInflater layout = LayoutInflater.from(getContext());
            final View dialogView = layout.inflate(R.layout.lay_dialog_loading6, null);

            alertDialog.setContentView(dialogView);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.show();


            tvDataCount = dialogView.findViewById(R.id.tvDataCount);
            message = dialogView.findViewById(R.id.tvmessage_download_complete);
            tvCount = dialogView.findViewById(R.id.tvCountData);

            //LinearLayout
            ll_progress = dialogView.findViewById(R.id.ll_progress_layout);
            ll_done = dialogView.findViewById(R.id.ll_done_layout);

            ll_progress.setVisibility(View.VISIBLE);
            ll_done.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                sendPostRequest_Member();
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("000564", "ER !!!!! " + e.getMessage());

            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("000564", "ON EXECUTE: ");
            super.onPostExecute(result);
        }

    }

    class CVaccination_Task extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            Log.d("000333", "ON PREEEEE: ");

            alertDialog = new Dialog(getContext());
            LayoutInflater layout = LayoutInflater.from(getContext());
            final View dialogView = layout.inflate(R.layout.lay_dialog_loading6, null);

            alertDialog.setContentView(dialogView);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.show();

           /* pbProgress = dialogView.findViewById(R.id.pbProcessing);
            image_tick = dialogView.findViewById(R.id.image_tick);*/
            tvDataCount = dialogView.findViewById(R.id.tvDataCount);
            message = dialogView.findViewById(R.id.tvmessage_download_complete);
            tvCount = dialogView.findViewById(R.id.tvCountData);

            //LinearLayout
            ll_progress = dialogView.findViewById(R.id.ll_progress_layout);
            ll_done = dialogView.findViewById(R.id.ll_done_layout);

            ll_progress.setVisibility(View.VISIBLE);
            ll_done.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                sendPostRequest_CVaccination();
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("000564", "ER !!!!! " + e.getMessage());

            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("000564", "ON EXECUTE: ");

            super.onPostExecute(result);
        }

    }

    class MVaccination_Task extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            Log.d("000333", "ON PREEEEE: ");

            alertDialog = new Dialog(getContext());
            LayoutInflater layout = LayoutInflater.from(getContext());
            final View dialogView = layout.inflate(R.layout.lay_dialog_loading6, null);

            alertDialog.setContentView(dialogView);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.show();

           /* pbProgress = dialogView.findViewById(R.id.pbProcessing);
            image_tick = dialogView.findViewById(R.id.image_tick);*/
            tvDataCount = dialogView.findViewById(R.id.tvDataCount);
            message = dialogView.findViewById(R.id.tvmessage_download_complete);
            tvCount = dialogView.findViewById(R.id.tvCountData);

            //LinearLayout
            ll_progress = dialogView.findViewById(R.id.ll_progress_layout);
            ll_done = dialogView.findViewById(R.id.ll_done_layout);

            ll_progress.setVisibility(View.VISIBLE);
            ll_done.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                sendPostRequest_MVaccination();
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("000564", "ER !!!!! " + e.getMessage());

            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("000564", "ON EXECUTE: ");
            super.onPostExecute(result);
        }

    }


    @Override
    public void onResume() {
        super.onResume();

        try {
            txt_khandan_reg.setVisibility(View.GONE);
            txt_reg.setVisibility(View.GONE);
            txt_child_hifaziti_teekey_record.setVisibility(View.GONE);
            txt_mother_hifaziti_teekey_record.setVisibility(View.GONE);

            pb_2.setVisibility(View.VISIBLE);
            pb_3.setVisibility(View.VISIBLE);
            pb_4.setVisibility(View.VISIBLE);

            Log.d("000555", "On Resume");

            Khandan_registration();

        } catch (Exception e) {
            Log.d("000555", "Catach Err: " + e.getMessage());
        }


    }

    public void Khandan_registration() {

        try {

            String[][] data_khandan = ls.executeReader("Select count(id) from KHANDAN where is_synced = '2'");

            txt_khandan_reg.setVisibility(View.VISIBLE);
            txt_khandan_reg.setText(data_khandan[0][0]);
            Log.d("000555", "Khandan Count: " + data_khandan[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    member_registration();

                }
            }, 200);


        } catch (Exception e) {

            txt_khandan_reg.setVisibility(View.VISIBLE);
            txt_khandan_reg.setText("0");
            Log.d("000555", "Khandan Err: " + e.getMessage());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    member_registration();

                }
            }, 200);
        }

    }

    public void member_registration() {

        try {
            Lister ls = new Lister(getContext());
            ls.createAndOpenDB();
            String[][] data_Member = ls.executeReader("Select count(id) from MEMBER where is_synced = '2'");

            pb_2.setVisibility(View.GONE);
            txt_reg.setVisibility(View.VISIBLE);
            txt_reg.setText(data_Member[0][0]);
            Log.d("000555", "Member Count: " + data_Member[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    child_vaccination();

                }
            }, 200);


        } catch (Exception e) {
            pb_2.setVisibility(View.GONE);
            txt_reg.setVisibility(View.VISIBLE);
            txt_reg.setText("0");
            Log.d("000555", "Reg Err: " + e.getMessage());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    child_vaccination();

                }
            }, 200);


        }
    }

    public void child_vaccination() {
        try {
            Lister ls = new Lister(getContext());
            ls.createAndOpenDB();
            String[][] data_CVaccination = ls.executeReader("Select count(id) from CVACCINATION where is_synced= '2' ");
            pb_3.setVisibility(View.GONE);
            txt_child_hifaziti_teekey_record.setVisibility(View.VISIBLE);
            txt_child_hifaziti_teekey_record.setText(data_CVaccination[0][0]);
            Log.d("000555", "CVACCINATION Count: " + data_CVaccination[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mother_vaccination();

                }
            }, 200);


        } catch (Exception e) {
            pb_3.setVisibility(View.GONE);
            txt_child_hifaziti_teekey_record.setVisibility(View.VISIBLE);
            txt_child_hifaziti_teekey_record.setText("0");
            Log.d("000555", "CVACCINATION Err: " + e.getMessage());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mother_vaccination();

                }
            }, 200);

        }
    }

    public void mother_vaccination() {
        try {
            Lister ls = new Lister(getContext());
            ls.createAndOpenDB();

            String[][] data_MVaccine = ls.executeReader("Select count(id) from MVACINE where is_synced= '0' ");
            pb_4.setVisibility(View.GONE);
            txt_mother_hifaziti_teekey_record.setVisibility(View.VISIBLE);
            txt_mother_hifaziti_teekey_record.setText(data_MVaccine[0][0]);
            Log.d("000555", "MVACINE Count: " + data_MVaccine[0][0]);


        } catch (Exception e) {
            pb_4.setVisibility(View.GONE);
            txt_mother_hifaziti_teekey_record.setVisibility(View.VISIBLE);

            txt_mother_hifaziti_teekey_record.setText("0");
            Log.d("000555", "MVACINE Err: " + e.getMessage());

        }
    }



    class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(getContext());
            this.progressDialog.setTitle("Downloading, please wait...");
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setIndeterminate(false);
            this.progressDialog.setCancelable(false);
            this.progressDialog.setProgress(0);
            this.progressDialog.setMax(100);
            int progressbarstatus = 0;

            // Change the background color of progress dialog
            this.progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

            this.progressDialog.setCanceledOnTouchOutside(false);

            this.progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < 20; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                progressDialog.incrementProgressBy(1);
            }
            return "completed";
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();
            // Display File path after downloading

        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            //  progressDialog.setProgress(Integer.parseInt(progress[0]));
            progressDialog.setMessage("Downloading... (" + progress[0] + "%)");
        }


    }




}