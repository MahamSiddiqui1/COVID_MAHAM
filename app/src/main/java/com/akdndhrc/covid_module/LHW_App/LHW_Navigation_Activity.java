package com.akdndhrc.covid_module.LHW_App;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_COVIDActivities.LHW_CovidTabActivity;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_MonthlyReportActivities.LHW_MonthlyReport_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_ReferActivities.LHW_ReferTabActivity;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_RegisterActivities.Register_House;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SearchActivities.Search_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SocialContactActivities.HealthCommittee_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_StatsActivity;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_StockActivities.LHW_MedicinesStock_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SyncDataActivities.SyncAndDownloadData_TabActivity;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SyncDataActivities.SyncVaccineImages_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_VideoActivities.VideoList_Activity;
import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.DatabaseFiles.Helper;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.Login_Activity;
import com.akdndhrc.covid_module.NothingSelectedSpinnerAdapter;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.akdndhrc.covid_module.R.*;
import static com.akdndhrc.covid_module.R.string.*;
import static com.akdndhrc.covid_module.R.string.enterPhoneNoPrompt;
import static com.akdndhrc.covid_module.R.string.loginUserIDWrong;
import static com.akdndhrc.covid_module.R.string.monthlyReportAlreadyDownloaded;
import static com.akdndhrc.covid_module.R.string.reportDownloadSuccessEng;
import static com.akdndhrc.covid_module.R.string.selectYearEng;
import static com.akdndhrc.covid_module.R.string.startUpdateData;
import static com.akdndhrc.covid_module.R.string.startUpdatePregData;

public class LHW_Navigation_Activity extends AppCompatActivity implements View.OnClickListener {


    Context act = LHW_Navigation_Activity.this;
    Activity activity;

    RelativeLayout rl_bluetooth_share, rl_register, rl_search, rl_vaccinate_inlisted, rl_sync, rl_adaad_O_shumaar, rl_aamdani, rl_refferal, rl_stock, rl_application_settings,
            rl_logout, rl_sync_images, rl_socialContact, rl_refer, rl_monthly_report,rl_COVID;
    ImageView image_cancel, iv_Satisfied, iv_VerySatisfied, iv_Neutral, iv_UnSatisfied, iv_VeryUnSatisfied, iv_close, iv_feedback_emoji, image_phonenumber;
    LinearLayout ll_emoji, ll_feedback;
    TextView tv_give_feedback, tv_not_now, tv_username, tv_LatLng;
    RatingBar ratingBar;
    String temp_var, temp_name, timeStamp;
    Button btn_jamaa_kre;
    ProgressBar progressBar;
    public static String login_useruid, login_username;

    double gps_latitude, gps_longitude;
    Dialog alertDialog;
    TextView txt_message;
    ProgressBar pbProgress;


    Spinner sp_year;
    Spinner sp_month;

    String month, year;
    ProgressDialog progressDialog;

    Button btn_submit, btn_view_report;

    boolean phonenumber = false;
    String number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_lhw_navigation);


        //Get shared USer name
        try {
            SharedPreferences prefelse = act.getApplicationContext().getSharedPreferences(getString(userLogin), 0); // 0 - for private mode
            String usernaame = prefelse.getString(getString(username), null); // getting String
            String shared_useruid = prefelse.getString("login_userid", null); // getting String
            login_useruid = shared_useruid;
            login_username = usernaame;
            Log.d("000555", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000555", "Shared Err:" + e.getMessage());
        }

        try {
            ////////////Get GPS latlng in shared pref
            SharedPreferences prefw = getApplicationContext().getSharedPreferences(getString(loginGPSeng), 0); // 0 - for private mode
            String latitude = prefw.getString("gps_latitude", null); // getting String
            String longitude = prefw.getString("gps_longitude", null); // getting String
            gps_latitude = Double.parseDouble(latitude);
            gps_longitude = Double.parseDouble(longitude);

            Log.d("000555", "Shared Latitude: " + gps_latitude);
            Log.d("000555", "Shared Longitude: " + gps_longitude);

        } catch (Exception e) {
            Log.d("000555", "SharedPred GPS Catch: " + e.getMessage());
        }


        tv_username = findViewById(id.tv_username);
        tv_username.setText(login_username);

        tv_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(act, startUpdateData, Toast.LENGTH_SHORT).show();

                Update_Khandan();

            }
        });

        tv_username.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(act, startUpdatePregData, Toast.LENGTH_SHORT).show();
                UpdateQueries();
                return true;
            }
        });

        //TextView GPS
        tv_LatLng = findViewById(id.tv_LatLng);
        float latitude = Float.parseFloat(String.format("%.5f", gps_latitude));
        float longitude = Float.parseFloat(String.format("%.5f", gps_longitude));

        Log.d("000555", " Latitude: " + latitude);
        Log.d("000555", " Longitude: " + longitude);

        tv_LatLng.setText(String.valueOf(latitude) + " | " + String.valueOf(longitude));

        //ImageView
        image_cancel = findViewById(id.image_cancel);
        image_phonenumber = findViewById(id.image_phonenumber);
        image_cancel.setOnClickListener(this);
        image_phonenumber.setOnClickListener(this);

        //Relative Layout
        rl_register = findViewById(id.rl_register);
        rl_search = findViewById(id.rl_search);
        rl_vaccinate_inlisted = findViewById(id.rl_vaccinate_inlisted);
        rl_sync = findViewById(id.rl_sync);
        rl_adaad_O_shumaar = findViewById(id.rl_adaad_O_shumaar);
        rl_aamdani = findViewById(id.rl_aamdani);
        rl_refferal = findViewById(id.rl_refferal);
        rl_stock = findViewById(id.rl_stock);
        rl_application_settings = findViewById(id.rl_application_settings);
        rl_logout = findViewById(id.rl_logout);
        rl_sync_images = findViewById(id.rl_sync_images);
        rl_bluetooth_share = findViewById(id.rl_bluetooth_share);
        rl_socialContact = findViewById(id.rl_socialContact);
        rl_refer = findViewById(id.rl_refer);
        rl_monthly_report = findViewById(id.rl_monthly_report);
        rl_COVID = findViewById(id.rl_COVID);


        rl_register.setOnClickListener(this);
        rl_search.setOnClickListener(this);
        rl_vaccinate_inlisted.setOnClickListener(this);
        rl_sync.setOnClickListener(this);
        rl_adaad_O_shumaar.setOnClickListener(this);
        rl_aamdani.setOnClickListener(this);
        rl_refferal.setOnClickListener(this);
        rl_stock.setOnClickListener(this);
        rl_application_settings.setOnClickListener(this);
        rl_logout.setOnClickListener(this);
        rl_sync_images.setOnClickListener(this);
        rl_bluetooth_share.setOnClickListener(this);
        rl_socialContact.setOnClickListener(this);
        rl_refer.setOnClickListener(this);
        rl_monthly_report.setOnClickListener(this);
        rl_COVID.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub


        switch (v.getId()) {

            case id.image_cancel:

                onBackPressed();

                break;

            case id.image_phonenumber:

                Dialog_SpinnerUserPhoneNumber();

                break;


            case id.rl_register:

                final Dialog alertDialog = new Dialog(act);
                LayoutInflater layout = LayoutInflater.from(act);
                final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);
                alertDialog.setContentView(dialogView);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        alertDialog.dismiss();
                        Intent newIntent = new Intent(act, Register_House.class);
                        act.startActivity(newIntent);
                    }
                }, 1000);

                break;

            case id.rl_search:


                act.startActivity(new Intent(act, Search_Activity.class));


                break;

            case id.rl_vaccinate_inlisted:


                // act.startActivity(new Intent(act, VAC_InsideOutsideUC_Activity.class));
                break;

            case id.rl_sync:
                act.startActivity(new Intent(act, SyncAndDownloadData_TabActivity.class));
                // act.startActivity(new Intent(act, SyncItems_Activity.class));
                break;

            case id.rl_adaad_O_shumaar:

                act.startActivity(new Intent(act, LHW_StatsActivity.class));
                break;

            case id.rl_aamdani:
                //  Toast.makeText(act, R.string.nav_aamdani_text, Toast.LENGTH_SHORT).show();
                break;

            case id.rl_refferal:
                //  Toast.makeText(act, R.string.nav_refferal_text, Toast.LENGTH_SHORT).show();
                break;

            case id.rl_taleeem_O_wasaeel:
                act.startActivity(new Intent(act, VideoList_Activity.class));
                break;


            case id.rl_application_settings:
                //  Toast.makeText(act, R.string.nav_application_settings_text, Toast.LENGTH_SHORT).show();
                break;

            case id.rl_sync_images:
                act.startActivity(new Intent(act, SyncVaccineImages_Activity.class));
                break;


            case id.rl_bluetooth_share:

                try {
                    if (Build.VERSION.SDK_INT >= 24) {
                        try {
                            Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                            m.invoke(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    SimpleDateFormat dates = new SimpleDateFormat("dd-MM-yy_hh:mm:ss");
                    Calendar c = Calendar.getInstance();
                    timeStamp = dates.format(c.getTime());
                    Log.d("000222", "timestamp:" + timeStamp);


                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
//                date_for_file = String.valueOf(System.currentTimeMillis());
                    copyDatabase("HayatPKDB");
                    File fileWithinMyDir = new File(Environment.getExternalStorageDirectory()
                            + File.separator + "HayatPK"
                            + File.separator + "HayatPKDB" + "-" + login_username + "-" + timeStamp + ".db");
                    Log.d("000222", "NAAAAA: " + fileWithinMyDir);

                    Log.d("000222", String.valueOf(fileWithinMyDir));
                    Log.d("000222", String.valueOf(fileWithinMyDir.exists()));

                    intentShareFile.setType("application/pdf");
                    intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + Environment.getExternalStorageDirectory()
                            + File.separator + "HayatPK"
                            + File.separator + "HayatPKDB" + "-" + login_username + "-" + timeStamp + ".db"));

                    Log.d("000222", "NA: " + Uri.parse("file://" + Environment.getExternalStorageDirectory()
                            + File.separator + "HayatPK"
                            + File.separator + "HayatPKDB" + "-" + login_username + "-" + timeStamp + ".db"));


                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                            "Sharing File...");
                    intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                    act.startActivity(Intent.createChooser(intentShareFile, "Share with.."));

                } catch (Exception e) {
                    Log.d("000222", " ERRR:  " + e.getMessage());
                }

                break;


            case id.rl_logout:

                Feedback_Dialog();

              /*Intent intent1 = new Intent(act, Login_Activity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                act.startActivity(intent1);*/
                break;


            case id.rl_socialContact:

                SocialContact_Dialog();

                break;


            case id.rl_stock:

                MedicinesStock_Dialog();

                break;

            case id.rl_refer:
                act.startActivity(new Intent(act, LHW_ReferTabActivity.class));
               /* String intro="[INTRO MESSAGE]\n";
                String phoneNumber="03432350528";
                Uri sms_uri = Uri.parse("smsto:" +phoneNumber);
                Intent sms_intent = new Intent(Intent.ACTION_VIEW, sms_uri);
                sms_intent.setData(sms_uri);
                sms_intent.putExtra("sms_body", intro);
                startActivity(sms_intent);*/
                break;

            case id.rl_monthly_report:
                Intent intent = new Intent(act, LHW_MonthlyReport_Activity.class);
                act.startActivity(intent);

                //MonthlyReport_Dialog();
                break;

                case id.rl_COVID:
                Intent intent1 = new Intent(act, LHW_CovidTabActivity.class);
                act.startActivity(intent1);
                break;

        }
    }

    // Copy to sdcard for debug use
    public void copyDatabase(String DATABASE_NAME) {
      /*  String databasePath = c.getDatabasePath(DATABASE_NAME).getPath();
        File f = new File(databasePath);

        Log.d("000222", " testing db path " + databasePath);
        Log.d("000222", " testing db exist " + f.exists());*/
        OutputStream myOutput = null;
        InputStream myInput = null;

        try {

            File directory = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "HayatPK"
                    + File.separator);
            if (!directory.exists()) {
                directory.mkdir();
            }
            Log.d("000222", " testing db path " + directory);

              /*  myOutput = new FileOutputStream(directory.getAbsolutePath()
                        + "/" + DATABASE_NAME+"-"+user_name+"-"+String.valueOf(System.currentTimeMillis()));*/

            myOutput = new FileOutputStream(Environment.getExternalStorageDirectory()
                    + File.separator + "HayatPK"
                    + File.separator + DATABASE_NAME + "-" + login_username + "-" + timeStamp + ".db");

            Log.d("000222", "PUTPUT: " + myOutput);

            myInput = new FileInputStream(Environment.getExternalStorageDirectory()
                    + File.separator + "HayatPK"
                    + File.separator + DATABASE_NAME);

            Log.d("000222", "PUTPUT: " + myInput);

            Log.d("000222", " testing db path 1" + String.valueOf(myInput));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
        } catch (Exception e) {
            Log.d("000222", " testing db path 2 " + e.getMessage());
        } finally {
            try {
                Log.d("000222", " testing 1");
                if (myOutput != null) {
                    myOutput.close();
                    myOutput = null;
                }
                if (myInput != null) {
                    myInput.close();
                    myInput = null;
                }
            } catch (Exception e) {
                Log.d("000222", " testing 2");
            }
        }
    }


    private void Feedback_Dialog() {

        final Dialog alertDialog = new Dialog(act);
        LayoutInflater layout = LayoutInflater.from(act);
        final View dialogView = layout.inflate(R.layout.dialog_feedback_emoji_layout, null);

        alertDialog.setContentView(dialogView);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

        //LinearLayout
        ll_emoji = dialogView.findViewById(id.ll_emoji);
        ll_feedback = dialogView.findViewById(id.ll_feedback);


        //ProgressBar
        progressBar = dialogView.findViewById(id.pbProgress);

        //Button
        btn_jamaa_kre = dialogView.findViewById(id.submit);

        //Rating
        ratingBar = dialogView.findViewById(id.rating_star);
        ratingBar.setEnabled(false);

        //ImageView
        iv_close = dialogView.findViewById(id.iv_close);
        iv_Neutral = dialogView.findViewById(id.iv_Neutral);
        iv_Satisfied = dialogView.findViewById(id.iv_Satisfied);
        iv_VerySatisfied = dialogView.findViewById(id.iv_VerySatisfied);
        iv_UnSatisfied = dialogView.findViewById(id.iv_UnSatisfied);
        iv_VeryUnSatisfied = dialogView.findViewById(id.iv_VeryUnSatisfied);
        iv_feedback_emoji = dialogView.findViewById(id.iv_feedback_emoji);

        //TextView
        tv_give_feedback = dialogView.findViewById(id.tv_give_feedback);
        tv_not_now = dialogView.findViewById(id.tv_not_now);


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        iv_VerySatisfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_emoji.setVisibility(View.GONE);
                iv_feedback_emoji.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.VISIBLE);
                btn_jamaa_kre.setVisibility(View.VISIBLE);
                tv_not_now.setVisibility(View.GONE);
                iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, drawable.v_satisfied));
                tv_give_feedback.setText(feedbackAboutHayatApp);
                ratingBar.setRating(5);
                temp_var = "5";
                temp_name = "Very Satisfied";
            }
        });

        iv_Satisfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_emoji.setVisibility(View.GONE);
                iv_feedback_emoji.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.VISIBLE);
                btn_jamaa_kre.setVisibility(View.VISIBLE);
                tv_not_now.setVisibility(View.GONE);
                iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, drawable.satisfied));
                tv_give_feedback.setText(feedbackAboutHayatApp);
                ratingBar.setRating(4);
                temp_var = "4";
                temp_name = "Satisfied";
            }
        });

        iv_Neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_emoji.setVisibility(View.GONE);
                iv_feedback_emoji.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.VISIBLE);
                btn_jamaa_kre.setVisibility(View.VISIBLE);
                tv_not_now.setVisibility(View.GONE);
                iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, drawable.neutral));
                tv_give_feedback.setText(feedbackAboutHayatApp);
                ratingBar.setRating(3);
                temp_var = "3";
                temp_name = "Neutral";
            }
        });

        iv_UnSatisfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_emoji.setVisibility(View.GONE);
                iv_feedback_emoji.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.VISIBLE);
                btn_jamaa_kre.setVisibility(View.VISIBLE);
                tv_not_now.setVisibility(View.GONE);
                iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, drawable.unsatisfied));
                tv_give_feedback.setText(feedbackAboutHayatApp);
                ratingBar.setRating(2);
                temp_var = "2";
                temp_name = "UnSatisfied";
            }
        });

        iv_VeryUnSatisfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_emoji.setVisibility(View.GONE);
                iv_feedback_emoji.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.VISIBLE);
                btn_jamaa_kre.setVisibility(View.VISIBLE);
                tv_not_now.setVisibility(View.GONE);
                iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, drawable.v_unsatisfied));
                tv_give_feedback.setText(feedbackAboutHayatApp);
                ratingBar.setRating(1);
                temp_var = "1";
                temp_name = "Very UnSatisfied";
            }
        });
        tv_not_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent1 = new Intent(act, Login_Activity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                act.startActivity(intent1);
            }
        });

        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                iv_close.setVisibility(View.GONE);
                tv_give_feedback.setVisibility(View.GONE);
                iv_feedback_emoji.setVisibility(View.GONE);
                ratingBar.setVisibility(View.GONE);
                btn_jamaa_kre.setVisibility(View.GONE);
                tv_not_now.setVisibility(View.GONE);

                progressBar.setVisibility(View.VISIBLE);

                SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar c = Calendar.getInstance();
                final String datetime = dates.format(c.getTime());

                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cc = Calendar.getInstance();
                final String TodayDate = date.format(cc.getTime());

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        try {


                            Lister ls = new Lister(act);
                            ls.createAndOpenDB();

                            JSONObject jobj = new JSONObject();

                            jobj.put("response", "" + temp_name);
                            jobj.put("response_rating", "" + temp_var);
                            jobj.put("datetime", "" + datetime);


                            String added_on = String.valueOf(System.currentTimeMillis());

                            String ans1 = "insert into FEEDBACK (rating, record_data, data,added_by, is_synced,added_on)" +
                                    "values" +
                                    "(" +
                                    "'" + temp_var + "'," +
                                    "'" + TodayDate + "'," +
                                    "'" + jobj + "'," +
                                    "'" + login_useruid + "'," +
                                    "'0'," +
                                    "'" + added_on + "'" +
                                    ")";

                            Boolean res = ls.executeNonQuery(ans1);
                            Log.d("000555", "Query: " + ans1);
                            Log.d("000555", "Json Data:" + res.toString());


                            if (Utils.haveNetworkConnection(act) > 0) {

                                sendPostRequest(Integer.valueOf(temp_var), String.valueOf(jobj), login_useruid, added_on);
                            } else {
                            }


                        } catch (Exception e) {
                            alertDialog.dismiss();
                            Log.d("000555", "Err: " + e.getMessage());
                        } finally {
                            alertDialog.dismiss();

                            Intent newIntent = new Intent(act, Login_Activity.class);
                            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            newIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(newIntent);
                        }
                    }
                }, 2000);


            }
        });

       /* ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                if (rating==1)
                {
                    iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.v_unsatisfied));
                }
               else if (rating==2)
                {
                    iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.unsatisfied));
                }
                else if (rating==3)
                {
                    iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.neutral));
                }
               else if (rating==4)
                {
                    iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.satisfied));
                }
                else if (rating==5)
                {
                    iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.v_satisfied));
                }
                else{
                    iv_feedback_emoji.setImageDrawable(null);
                }
            }
        });*/
    }

    private void Feedback_Dialog2() {

        final Dialog alertDialog = new Dialog(act);
        LayoutInflater layout = LayoutInflater.from(act);
        final View dialogView = layout.inflate(R.layout.dialog_feedback_emoji_layout, null);

        alertDialog.setContentView(dialogView);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

        //LinearLayout
        ll_emoji = dialogView.findViewById(id.ll_emoji);
        ll_emoji.setVisibility(View.GONE);

        ll_feedback = dialogView.findViewById(id.ll_feedback);

        //Button
        btn_jamaa_kre = dialogView.findViewById(id.submit);
        btn_jamaa_kre.setVisibility(View.VISIBLE);

        //Rating
        ratingBar = dialogView.findViewById(id.rating_star);
        ratingBar.setVisibility(View.VISIBLE);

        //ImageView
        iv_close = dialogView.findViewById(id.iv_close);
        iv_Neutral = dialogView.findViewById(id.iv_Neutral);
        iv_Satisfied = dialogView.findViewById(id.iv_Satisfied);
        iv_VerySatisfied = dialogView.findViewById(id.iv_VerySatisfied);
        iv_UnSatisfied = dialogView.findViewById(id.iv_UnSatisfied);
        iv_VeryUnSatisfied = dialogView.findViewById(id.iv_VeryUnSatisfied);

        iv_feedback_emoji = dialogView.findViewById(id.iv_feedback_emoji);

        //TextView
        tv_give_feedback = dialogView.findViewById(id.tv_give_feedback);


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    sendPostRequest(Integer.valueOf(temp_var), "0", login_useruid, String.valueOf(System.currentTimeMillis()));
                } catch (Exception e) {
                    Log.d("000555", "Err: " + e.getMessage());
                } finally {
                    alertDialog.dismiss();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent intent1 = new Intent(act, Login_Activity.class);
                            act.startActivity(intent1);
                        }
                    }, 1000);


                }
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                if (rating == 1) {
                    iv_feedback_emoji.setVisibility(View.VISIBLE);
                    iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, drawable.v_unsatisfied));
                } else if (rating == 2) {
                    iv_feedback_emoji.setVisibility(View.VISIBLE);
                    iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, drawable.unsatisfied));
                } else if (rating == 3) {
                    iv_feedback_emoji.setVisibility(View.VISIBLE);
                    iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, drawable.neutral));
                } else if (rating == 4) {
                    iv_feedback_emoji.setVisibility(View.VISIBLE);
                    iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, drawable.satisfied));
                } else if (rating == 5) {
                    iv_feedback_emoji.setVisibility(View.VISIBLE);
                    iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, drawable.v_satisfied));
                } else {
                    iv_feedback_emoji.setVisibility(View.GONE);
                    iv_feedback_emoji.setImageDrawable(null);
                }
            }
        });
    }

    private void sendPostRequest(final Integer feedback, final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/generic/feedback";

        Log.d("000555", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000555", "Response:    " + response);

                        Lister ls = new Lister(act);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE FEEDBACK SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE added_by = '" + added_by + "'AND added_on= '" + added_on + "'";

                        ls.executeNonQuery(update_record);

                        Toast.makeText(act, dataSynced, Toast.LENGTH_SHORT).show();
                        // Toast.makeText(act, "Your response submitted successfully.", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000555", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        //  Toast.makeText(act, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", "catch    " + e.getMessage());
                    //Toast.makeText(act, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "onErrorResponse: " + error.getMessage());
                //Toast.makeText(act, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast.makeText(act, noDataSyncAlert, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("feedback", String.valueOf(feedback));
                params.put("metadata", data);
                params.put("added_by", added_by);
                params.put("added_on", added_on);


                Log.d("000555", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000555", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }


    private void SocialContact_Dialog() {


        final Dialog alertDialog = new Dialog(act);

        LayoutInflater layout = LayoutInflater.from(act);
        final View dialogView = layout.inflate(R.layout.dialog_social_contact_layout, null);
        alertDialog.setContentView(dialogView);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();


        //ImageView
        iv_close = dialogView.findViewById(id.iv_close);
        RelativeLayout rl_HealthCommittee = dialogView.findViewById(id.rl_HealthCommittee);
        RelativeLayout rl_WomenSupport = dialogView.findViewById(id.rl_WomenSupport);
        RelativeLayout rl_HealthEducation = dialogView.findViewById(id.rl_HealthEducation);


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        rl_HealthCommittee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent newIntent = new Intent(act, HealthCommittee_Activity.class);
                newIntent.putExtra("type", "0");
                act.startActivity(newIntent);
            }
        });

        rl_WomenSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent newIntent = new Intent(act, HealthCommittee_Activity.class);
                newIntent.putExtra("type", "1");
                act.startActivity(newIntent);
            }
        });

        rl_HealthEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent newIntent = new Intent(act, HealthCommittee_Activity.class);
                newIntent.putExtra("type", "2");
                act.startActivity(newIntent);
            }
        });

    }

    private void MedicinesStock_Dialog() {

        final Dialog alertDialog = new Dialog(act);
        LayoutInflater layout = LayoutInflater.from(act);
        final View dialogView = layout.inflate(R.layout.dialog_medicines_stock_layout, null);

        alertDialog.setContentView(dialogView);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();


        //ImageView
        iv_close = dialogView.findViewById(id.iv_close);
        RelativeLayout rl_StockMaster = dialogView.findViewById(id.rl_StockMaster);
        RelativeLayout rl_Miscellaneous = dialogView.findViewById(id.rl_Miscellaneous);


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        rl_StockMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent newIntent = new Intent(act, LHW_MedicinesStock_Activity.class);
                newIntent.putExtra("type", "0");
                act.startActivity(newIntent);
            }
        });

        rl_Miscellaneous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent newIntent = new Intent(act, LHW_MedicinesStock_Activity.class);
                newIntent.putExtra("type", "1");
                act.startActivity(newIntent);
            }
        });

    }

    private void MonthlyReport_Dialog() {

        final Dialog alertDialog = new Dialog(act);
        LayoutInflater layout = LayoutInflater.from(act);
        final View dialogView = layout.inflate(R.layout.dialog_month_and_year_layout, null);

        alertDialog.setContentView(dialogView);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();


        //ImageView
        iv_close = dialogView.findViewById(id.iv_close);
        sp_year = dialogView.findViewById(id.sp_year);
        sp_month = dialogView.findViewById(id.sp_month);
        btn_submit = dialogView.findViewById(id.submit);
        btn_view_report = dialogView.findViewById(id.btn_view_report);

        sp_year.setEnabled(false);

        spinner_data();

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_view_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(act, LHW_MonthlyReport_Activity.class);
                intent.putExtra("month", month);
                intent.putExtra("year", year);
                act.startActivity(intent);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sp_month.getSelectedItemPosition() == 0) {
                    Toast.makeText(act, selectMonthEng, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sp_year.getSelectedItemPosition() == 0) {
                    Toast.makeText(act, selectYearEng, Toast.LENGTH_SHORT).show();
                    return;
                }


                Log.d("000147", "ELSEEEE");

                if (Utils.haveNetworkConnection(act) > 0) {

                    alertDialog.dismiss();

                    progressDialog = new ProgressDialog(act);
                    progressDialog.setMessage("Downloading");
                    progressDialog.show();


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            sendPostRequestCount(month, year);
                        }
                    }, 2000);
                } else {
                    Toast.makeText(act, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void spinner_data() {


        try {

            ////////////////////////// Sp_Month ////////////////////////////////////////
            final ArrayAdapter<CharSequence> adptr_council = ArrayAdapter.createFromResource(this, array.month, layout.sp_title_topic_layout);
            adptr_council.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


            sp_month.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            adptr_council,
                            layout.sp_title_topic_layout,
                            // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                            this));


            sp_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (sp_month.getSelectedItemPosition() == 1) {
                        month = "01";
                        sp_year.setEnabled(true);
                        check_report();
                    } else if (sp_month.getSelectedItemPosition() == 2) {
                        month = "02";
                        sp_year.setEnabled(true);
                        check_report();
                    } else if (sp_month.getSelectedItemPosition() == 3) {
                        month = "03";
                        sp_year.setEnabled(true);
                        check_report();
                    } else if (sp_month.getSelectedItemPosition() == 4) {
                        month = "04";
                        sp_year.setEnabled(true);
                        check_report();
                    } else if (sp_month.getSelectedItemPosition() == 5) {
                        month = "05";
                        sp_year.setEnabled(true);
                        check_report();
                    } else if (sp_month.getSelectedItemPosition() == 6) {
                        month = "06";
                        sp_year.setEnabled(true);
                        check_report();

                    } else if (sp_month.getSelectedItemPosition() == 7) {
                        month = "07";
                        sp_year.setEnabled(true);
                        check_report();
                    } else if (sp_month.getSelectedItemPosition() == 8) {
                        month = "08";
                        sp_year.setEnabled(true);

                        check_report();
                    } else if (sp_month.getSelectedItemPosition() == 9) {
                        month = "09";
                        sp_year.setEnabled(true);
                        check_report();
                    } else if (sp_month.getSelectedItemPosition() == 10) {
                        month = "10";
                        sp_year.setEnabled(true);
                        check_report();
                    } else if (sp_month.getSelectedItemPosition() == 11) {
                        month = "11";
                        sp_year.setEnabled(true);
                        check_report();
                    } else if (sp_month.getSelectedItemPosition() == 12) {
                        month = "12";
                        sp_year.setEnabled(true);
                        check_report();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            ////////////////////////// Sp_Year ////////////////////////////////////////
            ArrayList<String> years = new ArrayList<String>();
            int thisYear = Calendar.getInstance().get(Calendar.YEAR);
            for (int i = 2019; i <= thisYear; i++) {
                years.add(Integer.toString(i));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, layout.sp_title_topic_layout, years);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_year.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            adapter,
                            layout.sp_title_topic_layout,
                            // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                            this));
            sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (sp_year.getSelectedItemPosition() > 0) {
                        year = String.valueOf(sp_year.getSelectedItem());
                        check_report();

                    } else {

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        } catch (Exception e) {

        }
    }

    public void check_report() {
        try {
            Lister ls = new Lister(act);
            ls.createAndOpenDB();

            String[][] mData = ls.executeReader("Select year, month from MONTHLY_REPORT");

            if (mData != null) {
                for (int i = 0; i < mData.length; i++) {

                    if (mData[i][0].equalsIgnoreCase(year) && mData[i][1].equalsIgnoreCase(month)) {
                        Toast.makeText(act, monthlyReportAlreadyDownloaded, Toast.LENGTH_SHORT).show();
                        btn_view_report.setVisibility(View.VISIBLE);
                        btn_submit.setVisibility(View.GONE);
                    } else {
                        btn_submit.setVisibility(View.VISIBLE);
                        btn_view_report.setVisibility(View.GONE);
                    }
                }

            } else {

                btn_submit.setVisibility(View.VISIBLE);
                btn_view_report.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.d("000147", "Err:" + e.getMessage());
        }
    }

    public void UpdateQueries() {

        alertDialog = new Dialog(LHW_Navigation_Activity.this);
        LayoutInflater layout = LayoutInflater.from(LHW_Navigation_Activity.this);
        final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

        alertDialog.setContentView(dialogView);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

        txt_message = dialogView.findViewById(id.message);
        pbProgress = dialogView.findViewById(id.pbProgress);

        try {
            ////////////////////////////////////////////// MANC ///////////////////////////////////////////

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Update_MANC();
                }
            }, 2000);

        } catch (Exception e) {
            alertDialog.dismiss();
            Log.d("000111", "Error: " + e.getMessage());
        }
    }

    public void Update_MANC() {

        try {
            Lister ls = new Lister(act);
            ls.createAndOpenDB();

            String[][] mData_preg = ls.executeReader("Select t0.member_uid, t1.pregnancy_id as new_pregId, t0.pregnancy_id,t0.added_on from MANC t0 " +
                    "INNER JOIN MPREGNANCY t1 ON t1.lmp = t0.pregnancy_id ");

            Log.d("000111", "MANC Data LEN: " + mData_preg.length);


            for (int i = 0; i < mData_preg.length; i++) {

                Log.d("000111", "Loop: " + i);
                Log.d("000111", "MEMBER UID: " + mData_preg[i][0]);
                Log.d("000111", "PREG ID: " + mData_preg[i][1]);
                Log.d("000111", "MANC PREG ID: " + mData_preg[i][2]);
                Log.d("000111", " Added_On: " + mData_preg[i][3]);


                String update_record = "UPDATE MANC SET " +
                        "pregnancy_id='" + mData_preg[i][1] + "' " +
                        "WHERE member_uid = '" + mData_preg[i][0] + "' and added_on = '" + mData_preg[i][3] + "'";
                ls.executeNonQuery(update_record);

                Log.d("000111", "Updated MANC !!!!!!!!!! " + i);
            }


        } catch (Exception e) {
            Log.d("000111", "Update MANCQuery Err: " + e.getMessage());
            final Snackbar snackbar = Snackbar.make(findViewById(id.navigation_screen), dataAlreadyEdited, Snackbar.LENGTH_SHORT);
            View mySbView = snackbar.getView();
            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            mySbView.setBackgroundColor(act.getResources().getColor(android.R.color.black));
            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(16);
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable.ic_error_outline_black_24dp, 0, 0, 0);
            snackbar.setDuration(2000);
            snackbar.show();
        }

        //////////////////////////////////////////////// MDELIVER //////////////////////////////////////////////////////

        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Update_MDELIV();
            }
        }, 2000);

    }

    public void Update_MDELIV() {

        try {
            Lister ls = new Lister(act);
            ls.createAndOpenDB();

            String[][] mData_preg = ls.executeReader("Select t0.member_uid, t1.pregnancy_id as new_pregId, t0.pregnancy_id,t0.added_on from MDELIV t0 " +
                    "INNER JOIN MPREGNANCY t1 ON t1.lmp = t0.pregnancy_id ");

            Log.d("000111", "MDELIV Data LEN: " + mData_preg.length);


            for (int i = 0; i < mData_preg.length; i++) {

                Log.d("000111", "Loop: " + i);
                Log.d("000111", "MEMBER UID: " + mData_preg[i][0]);
                Log.d("000111", "PREG ID: " + mData_preg[i][1]);
                Log.d("000111", "MDELIV PREG ID: " + mData_preg[i][2]);
                Log.d("000111", " Added_On: " + mData_preg[i][3]);


                String update_record = "UPDATE MDELIV SET " +
                        "pregnancy_id='" + mData_preg[i][1] + "' " +
                        "WHERE member_uid = '" + mData_preg[i][0] + "' and added_on = '" + mData_preg[i][3] + "'";
                ls.executeNonQuery(update_record);

                Log.d("000111", "Updated MDELIV !!!!!!!!!! " + i);
            }


        } catch (Exception e) {
            Log.d("000111", "Update MDELIVQuery Err: " + e.getMessage());

            final Snackbar snackbar = Snackbar.make(findViewById(id.navigation_screen), dataAlreadyEdited, Snackbar.LENGTH_SHORT);
            View mySbView = snackbar.getView();
            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            mySbView.setBackgroundColor(act.getResources().getColor(android.R.color.black));
            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(16);
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable.ic_error_outline_black_24dp, 0, 0, 0);
            snackbar.setDuration(2000);
            snackbar.show();

        }

        ////////////////////////////////////// MPNC //////////////////////////////////

        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                Update_MPNC();
            }
        }, 2000);

    }

    public void Update_MPNC() {

        try {
            Lister ls = new Lister(act);
            ls.createAndOpenDB();

            String[][] mData_preg = ls.executeReader("Select t0.member_uid, t1.pregnancy_id as new_pregId, t0.pregnancy_id,t0.added_on from MPNC t0 " +
                    "INNER JOIN MPREGNANCY t1 ON t1.lmp = t0.pregnancy_id ");

            Log.d("000111", "MPNC Data LEN: " + mData_preg.length);


            for (int i = 0; i < mData_preg.length; i++) {

                Log.d("000111", "Loop: " + i);
                Log.d("000111", "MEMBER UID: " + mData_preg[i][0]);
                Log.d("000111", "PREG ID: " + mData_preg[i][1]);
                Log.d("000111", "MPNC PREG ID: " + mData_preg[i][2]);
                Log.d("000111", " Added_On: " + mData_preg[i][3]);


                String update_record = "UPDATE MPNC SET " +
                        "pregnancy_id='" + mData_preg[i][1] + "' " +
                        "WHERE member_uid = '" + mData_preg[i][0] + "' and added_on = '" + mData_preg[i][3] + "'";
                ls.executeNonQuery(update_record);

                Log.d("000111", "Updated MPNC !!!!!!!!!! " + i);
            }

            Handler handler22 = new Handler();
            handler22.postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                    final Snackbar snackbar = Snackbar.make(findViewById(id.navigation_screen), allDataEdited, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(act.getResources().getColor(android.R.color.black));
                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(16);
                    textView.setCompoundDrawablesWithIntrinsicBounds(drawable.ic_check_black_24dp, 0, 0, 0);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        textView.setCompoundDrawableTintList(act.getResources().getColorStateList(color.green_color));
                    }
                    snackbar.setDuration(4000);
                    snackbar.show();
                }
            }, 2000);


        } catch (Exception e) {
            Log.d("000111", "Update MPNC Err: " + e.getMessage());

            Handler handler22 = new Handler();
            handler22.postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                    final Snackbar snackbar = Snackbar.make(findViewById(id.navigation_screen), allDataAlreadyEdited, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(act.getResources().getColor(android.R.color.black));
                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(16);
                    textView.setCompoundDrawablesWithIntrinsicBounds(drawable.ic_error_outline_black_24dp, 0, 0, 0);
                    snackbar.setDuration(4000);
                    snackbar.show();
                }
            }, 2000);
        }

    }


    public void Update_Khandan() {

        alertDialog = new Dialog(LHW_Navigation_Activity.this);
        LayoutInflater layout = LayoutInflater.from(LHW_Navigation_Activity.this);
        final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

        alertDialog.setContentView(dialogView);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

        txt_message = dialogView.findViewById(id.message);
        pbProgress = dialogView.findViewById(id.pbProgress);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {

                    Lister ls = new Lister(act);
                    ls.createAndOpenDB();

                    String[][] mData_count = ls.executeReader("Select count(*) from MEMBER where khandan_id NOT IN (SELECT uid from KHANDAN) order by added_on ASC");

                    Log.d("000105", "KHANDAN COUNT: " + mData_count[0][0]);


                    if (Integer.parseInt(mData_count[0][0]) > 0) {
                        Log.d("000105", "COUNT GREATER THAN ZERO --------");

                        String[][] mData_khandan = ls.executeReader("SELECT t0.province_id,t0.district_id,t0.subdistrict_id,t0.uc_id,t0.village_id,t0.water_source,t0.toilet_facility,t1.name,Max(t0.added_on) from KHANDAN t0" +
                                " INNER JOIN VILLAGES t1 ON t0.village_id=t1.uid" +
                                " where t0.added_by = '"+login_useruid+"'");

                        if (mData_khandan != null)
                        {

                        try {
                            String[][] mData_Member = ls.executeReader("SELECT id,khandan_id, full_name, JSON_EXTRACT(data ,\"$.khandan_number_manual\") as manual_id, added_by,added_on FROM MEMBER WHERE id IN (" +
                                    " SELECT MIN(id) as id FROM MEMBER" +
                                    " WHERE khandan_id not IN (SELECT uid from KHANDAN)" +
                                    " GROUP BY khandan_id" +
                                    " )" +
                                    " AND added_by='"+login_useruid+"'");

                           /* String[][] mData_Member = ls.executeReader("Select id,khandan_id, full_name, JSON_EXTRACT(data ,'$.khandan_number_manual') as manual_id, added_on, added_by from MEMBER" +
                                    " WHERE khandan_id NOT IN (SELECT uid from KHANDAN)" +
                                    " GROUP BY khandan_id order by id ASC");*/


                            Log.d("000105", "MEMBER DATA LEN : " + mData_Member.length);

                        /* String[][] mData_khandan = ls.executeReader("SELECT t0.province_id,t0.district_id,t0.subdistrict_id,t0.uc_id,t0.village_id,t0.water_source,t0.toilet_facility,t1.name,Max(t0.added_on) from KHANDAN t0" +
                                    " INNER JOIN VILLAGES t1 ON t0.village_id=t1.uid" +
                                    " where t0.added_by = '" + mData_Member[0][4] + "'");*/

                            Log.d("000105", " KHANDAN PROVINCE ID: " + mData_khandan[0][0]);
                            Log.d("000105", " KHANDAN DISTRICT ID: " + mData_khandan[0][1]);
                            Log.d("000105", " KHANDAN SUBDISTRICT ID: " + mData_khandan[0][2]);
                            Log.d("000105", " KHANDAN UC ID: " + mData_khandan[0][3]);
                            Log.d("000105", " KHANDAN VILLAGE ID: " + mData_khandan[0][4]);



                            for (int i = 0; i < mData_Member.length; i++) {

                                Log.d("000105", "*****************************************************") ;
                                Log.d("000105", "Loop: " + i);
                                Log.d("000105", "MEMBER  ID: " + mData_Member[i][0]);
                                Log.d("000105", "MEMBER KHANDAN ID: " + mData_Member[i][1]);
                                Log.d("000105", "MEMBER NAME: " + mData_Member[i][2]);
                                Log.d("000105", "MEMBER MANUAL ID: " + mData_Member[i][3]);
                                Log.d("000105", "MEMBER ABBED BY ID: " + mData_Member[i][4]);


                               String ans1 = "insert or ignore into KHANDAN (manual_id, uid, province_id, district_id, subdistrict_id, uc_id, village_id, family_head_name, family_address, water_source, toilet_facility, added_by,is_synced,added_on)" +
                                        "values" +
                                        "(" +
                                        "'" + mData_Member[i][3] + "'," +
                                        "'" + mData_Member[i][1] + "'," +
                                        "'" + mData_khandan[0][0] + "'," +
                                        "'" + mData_khandan[0][1] + "'," +
                                        "'" + mData_khandan[0][2] + "'," +
                                        "'" + mData_khandan[0][3] + "'," +
                                        "'" + mData_khandan[0][4] + "'," +
                                        "'" + mData_Member[i][2] + "'," +
                                        "'" + mData_khandan[0][7] + "'," +
                                        "'" + mData_khandan[0][5] + "'," +
                                        "'" + mData_khandan[0][6] + "'," +
                                        "'" + mData_Member[i][4] + "'," +
                                        "'0'," +
                                        "'" + mData_Member[i][5] + "'" +
                                        ")";

                                Boolean res = ls.executeNonQuery(ans1);
                                Log.d("000105", "INSERT INTO KHANDAN: " + ans1);
                                Log.d("000105", "QUERY: " + res.toString());


                                if (res.toString().equalsIgnoreCase("true")) {
                                    Log.d("000105", "******* QUERY TRUE *********");
                                    if (i == mData_Member.length - 1) {
                                        Log.d("000105", "IF LENGTH ");

                                        Handler handler22 = new Handler();
                                        handler22.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                alertDialog.dismiss();
                                                final Snackbar snackbar = Snackbar.make(findViewById(id.navigation_screen), allDataEdited, Snackbar.LENGTH_SHORT);
                                                View mySbView = snackbar.getView();
                                                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                                mySbView.setBackgroundColor(act.getResources().getColor(android.R.color.black));
                                                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                                textView.setTextColor(Color.WHITE);
                                                textView.setTextSize(16);
                                                textView.setCompoundDrawablesWithIntrinsicBounds(drawable.ic_check_black_24dp, 0, 0, 0);
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    textView.setCompoundDrawableTintList(act.getResources().getColorStateList(color.green_color));
                                                }
                                                snackbar.setDuration(4000);
                                                snackbar.show();
                                            }
                                        }, 2000);
                                    } else {

                                        Log.d("000105", "ELSE LENGTH ");
                                    }

                                } else {
                                    Log.d("000105", "////// QUERY NOT TRUE /////////");
                                }
                            }

                        } catch (Exception e) {
                            Log.d("000105", "Error:  --------" + e.getMessage());
                            if (e.getMessage().equalsIgnoreCase(getString(getLenOfNullArray)))
                            {
                                Toast.makeText(act, loginUserIDWrong, Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(act, somethingWrong, Toast.LENGTH_SHORT).show();
                            }
                        }
                        }
                        else {
                            Log.d("000105", "DATA NULL --------");
                            Toast.makeText(act, somethingWrong, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        final Snackbar snackbar = Snackbar.make(findViewById(id.navigation_screen), allDataAlreadyEdited, Snackbar.LENGTH_SHORT);
                        View mySbView = snackbar.getView();
                        mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        mySbView.setBackgroundColor(act.getResources().getColor(android.R.color.black));
                        TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(16);
                        textView.setCompoundDrawablesWithIntrinsicBounds(drawable.ic_error_outline_black_24dp, 0, 0, 0);
                        snackbar.setDuration(4000);
                        snackbar.show();
                    }

                } catch (Exception e) {
                    Toast.makeText(act, somethingWrong, Toast.LENGTH_SHORT).show();
                    Log.d("000111", "Update KHANDAN TABLE Err: " + e.getMessage());

                } finally {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog.dismiss();
                        }
                    },2000);

                }
            }
        }, 1500);


    }


    private void sendPostRequestCount(final String month, final String year) {

        String url = "https://development.api.teekoplus.akdndhrc.org/lhs/report";

        Log.d("000222", "mURL " + url);

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("000222", "Response:    " + response);

                try {
                    Lister ls = new Lister(act);
                    // ls.closeDB();
                    ls.createAndOpenDB();

                    try {

                        //  JSONArray jobj = new JSONArray(response);
                        Log.d("000999", response);
                        JSONObject obj = new JSONObject(String.valueOf(response));
                        Log.d("000999", obj.toString());

                        //basicinfo
                        JSONObject basic_info = obj.getJSONObject("basic_info");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + basic_info.toString());
                        JSONObject watersource = basic_info.getJSONObject("water_source");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + watersource.toString());

                        JSONObject child_health = obj.getJSONObject("child_health");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + child_health.toString());
                        JSONObject age1223 = child_health.getJSONObject("age_1223");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + age1223.toString());
                        JSONObject age_lt3 = child_health.getJSONObject("age_lt3");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + age_lt3.toString());

                        //maternalhealth
                        JSONObject maternal_health = obj.getJSONObject("maternal_health");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + maternal_health.toString());

                        //familyplan
                        JSONObject family_planning = obj.getJSONObject("family_plan");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + family_planning.toString());

                        //diseases
                        JSONObject diseases = obj.getJSONObject("diseases");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + diseases.toString());

                        JSONObject anamia = diseases.getJSONObject("anaemia");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + anamia.toString());

                        JSONObject ari = diseases.getJSONObject("ari");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + ari.toString());

                        JSONObject diarrhea = diseases.getJSONObject("diarrhea");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + diarrhea.toString());

                        JSONObject eye_infections = diseases.getJSONObject("eye_infections");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + eye_infections.toString());

                        JSONObject fever = diseases.getJSONObject("fever");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + fever.toString());

                        JSONObject malaria = diseases.getJSONObject("malaria");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + malaria.toString());

                        JSONObject referral = diseases.getJSONObject("referral");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + referral.toString());

                        JSONObject resp = diseases.getJSONObject("resp");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + resp.toString());

                        JSONObject rtis = diseases.getJSONObject("rtis");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + rtis.toString());

                        JSONObject scabies = diseases.getJSONObject("scabies");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + scabies.toString());

                        JSONObject tb_diagnosed = diseases.getJSONObject("tb_diagnosed");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + tb_diagnosed.toString());

                        JSONObject tb_followed = diseases.getJSONObject("tb_followed");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + tb_followed.toString());

                        JSONObject tb_suspect = diseases.getJSONObject("tb_suspect");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + tb_suspect.toString());

                        JSONObject worm = diseases.getJSONObject("worm");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + worm.toString());

                        //birth_deaths
                        JSONObject births_deaths = obj.getJSONObject("births_deaths");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + births_deaths.toString());

                        //medicines
                        JSONObject medicines = obj.getJSONObject("medicines");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + medicines.toString());

                        //mis
                        JSONObject misc = obj.getJSONObject("misc");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + misc.toString());

                        //supervisor
                        JSONObject supervision = obj.getJSONObject("supervision");
                        Log.d("000999", "onResponse!!!!!!!!!!!!: " + supervision.toString());

                        boolean m2 = ls.executeNonQuery(Helper.CREATE_TABLE_MONTHLY_REPORT);


                        String query_form_get_data = "insert into MONTHLY_REPORT (month, year, health_commettees,women_suport_group,household_registered_lhw, " +
                                " tap, spring, handpump, well, other,flush_system," +
                                " age_1223_count, age_1223_fully_imunized, age_lt3_count, age_lt3_gm, age_lt3_malnurished, new_borns_1week," +
                                " low_birth_weight, breast_fed, immunized,new_preg,total_preg,total_vistis,iron_sup,abortions,delivey_4p,delivery_pnc," +
                                "delivery_immunized,eligible,provided, followup,modern,condom_users,pill_users,injectible_users,iucd_users,surgical_users," +
                                "other_users,traditional_users,referred,supplied_condoms,supplied_pills,supplied_injectibles,diarrhea_a5,diarrhea_u5," +
                                "ari_a5,ari_u5,fever_a5,fever_u5,resp_a5,resp_u5,anaemia_a5,anaemia_u5,scabies_a5,scabies_u5,eye_infections_a5,eye_infections_u5," +
                                "rtis_a5,rtis_u5,worm_a5,worm_u5,malaria_a5,malaria_u5,referral_a5,referral_u5,tb_suspect_a5,tb_suspect_u5,tb_diagnosed_a5," +
                                "tb_diagnosed_u5,tb_followed_a5,tb_followed_u5,live,still,deaths_all,noenatal,infant,children,maternal,tab_paracetamol," +
                                "syp_paracetamol,tab_choloroquin,syp_choloroquin,tab_mebendazole,syp_pipearzine,ors,eye_ontiment,syp_contrimexazole,iron_tab," +
                                "antiseptic_lotion,benzyle_benzoate_lotion,sticking_plaster,b_complex_syp,cotton_bandages,cotton_wool,condoms,oral_pills," +
                                "contraceptive_inj,med_others,lhw_kit_bag,weighing_machine,thermometer,torch_with_cell,scissors,syringe_cutter,mis_others," +
                                "lhs,dco,adc,fpo,ppiu)" +
                                " values " +
                                "(" +
                                "'" + month + "'," +
                                "'" + year + "'," +
                                "'" + basic_info.getString("health_committees") + "'," +
                                "'" + basic_info.getString("support_groups") + "'," +
                                "'" + basic_info.getString("registerations") + "'," +
                                "'" + watersource.getString("tap") + "'," +
                                "'" + watersource.getString("spring") + "'," +
                                "'" + watersource.getString("hand_pump") + "'," +
                                "'" + watersource.getString("well") + "'," +
                                "'" + watersource.getString("other") + "'," +
                                "'" + basic_info.getString("flush_system") + "'," +
                                "'" + age1223.getString("count") + "'," +
                                "'" + age1223.getString("fully_imunized") + "'," +
                                "'" + age_lt3.getString("count") + "'," +
                                "'" + age_lt3.getString("gm") + "'," +
                                "'" + age_lt3.getString("malnurished") + "'," +
                                "'" + child_health.getString("new_borns_1week") + "'," +
                                "'" + child_health.getString("low_birth_weight") + "'," +
                                "'" + child_health.getString("breast_fed") + "'," +
                                "'" + child_health.getString("immunized") + "'," +
                                "'" + maternal_health.getString("new_preg") + "'," +
                                "'" + maternal_health.getString("total_preg") + "'," +
                                "'" + maternal_health.getString("total_vistis") + "'," +
                                "'" + maternal_health.getString("iron_sup") + "'," +
                                "'" + maternal_health.getString("abortions") + "'," +
                                "'" + maternal_health.getString("delivey_4p") + "'," +
                                "'" + maternal_health.getString("delivery_pnc") + "'," +
                                "'" + maternal_health.getString("delivery_immunized") + "'," +
                                "'" + family_planning.getString("eligible") + "'," +
                                "'" + family_planning.getString("provided") + "'," +
                                "'" + family_planning.getString("followup") + "'," +
                                "'" + family_planning.getString("modern") + "'," +
                                "'" + family_planning.getString("condom_users") + "'," +
                                "'" + family_planning.getString("pill_users") + "'," +
                                "'" + family_planning.getString("injectible_users") + "'," +
                                "'" + family_planning.getString("iucd_users") + "'," +
                                "'" + family_planning.getString("surgical_users") + "'," +
                                "'" + family_planning.getString("other_users") + "'," +
                                "'" + family_planning.getString("traditional_users") + "'," +
                                "'" + family_planning.getString("referred") + "'," +
                                "'" + family_planning.getString("supplied_condoms") + "'," +
                                "'" + family_planning.getString("supplied_pills") + "'," +
                                "'" + family_planning.getString("supplied_injectibles") + "'," +
                                "'" + diarrhea.getString("a5") + "'," +
                                "'" + diarrhea.getString("u5") + "'," +
                                "'" + ari.getString("a5") + "'," +
                                "'" + ari.getString("u5") + "'," +
                                "'" + fever.getString("a5") + "'," +
                                "'" + fever.getString("u5") + "'," +
                                "'" + resp.getString("a5") + "'," +
                                "'" + resp.getString("u5") + "'," +
                                "'" + anamia.getString("a5") + "'," +
                                "'" + anamia.getString("u5") + "'," +
                                "'" + scabies.getString("a5") + "'," +
                                "'" + scabies.getString("u5") + "'," +
                                "'" + eye_infections.getString("a5") + "'," +
                                "'" + eye_infections.getString("u5") + "'," +
                                "'" + rtis.getString("a5") + "'," +
                                "'" + rtis.getString("u5") + "'," +
                                "'" + worm.getString("a5") + "'," +
                                "'" + worm.getString("u5") + "'," +
                                "'" + malaria.getString("a5") + "'," +
                                "'" + malaria.getString("u5") + "'," +
                                "'" + referral.getString("a5") + "'," +
                                "'" + referral.getString("u5") + "'," +
                                "'" + tb_suspect.getString("a5") + "'," +
                                "'" + tb_suspect.getString("u5") + "'," +
                                "'" + tb_diagnosed.getString("a5") + "'," +
                                "'" + tb_diagnosed.getString("u5") + "'," +
                                "'" + tb_followed.getString("a5") + "'," +
                                "'" + tb_followed.getString("u5") + "'," +
                                "'" + births_deaths.getString("live") + "'," +
                                "'" + births_deaths.getString("still") + "'," +
                                "'" + births_deaths.getString("deaths_all") + "'," +
                                "'" + births_deaths.getString("noenatal") + "'," +
                                "'" + births_deaths.getString("infant") + "'," +
                                "'" + births_deaths.getString("children") + "'," +
                                "'" + births_deaths.getString("maternal") + "'," +
                                "'" + medicines.getString("tab_paracetamol") + "'," +
                                "'" + medicines.getString("syp_paracetamol") + "'," +
                                "'" + medicines.getString("tab_choloroquin") + "'," +
                                "'" + medicines.getString("syp_choloroquin") + "'," +
                                "'" + medicines.getString("tab_mebendazole") + "'," +
                                "'" + medicines.getString("syp_pipearzine") + "'," +
                                "'" + medicines.getString("ors") + "'," +
                                "'" + medicines.getString("eye_ontiment") + "'," +
                                "'" + medicines.getString("syp_contrimexazole") + "'," +
                                "'" + medicines.getString("iron_tab.") + "'," +
                                "'" + medicines.getString("antiseptic_lotion") + "'," +
                                "'" + medicines.getString("benzyle_benzoate_lotion") + "'," +
                                "'" + medicines.getString("sticking_plaster") + "'," +
                                "'" + medicines.getString("b_complex_syp") + "'," +
                                "'" + medicines.getString("cotton_bandages") + "'," +
                                "'" + medicines.getString("cotton_wool") + "'," +
                                "'" + medicines.getString("condoms") + "'," +
                                "'" + medicines.getString("oral_pills") + "'," +
                                "'" + medicines.getString("contraceptive_inj") + "'," +
                                "'" + medicines.getString("others") + "'," +
                                "'" + misc.getString("lhw_kit_bag") + "'," +
                                "'" + misc.getString("weighing_machine") + "'," +
                                "'" + misc.getString("thermometer") + "'," +
                                "'" + misc.getString("torch_with_cell") + "'," +
                                "'" + misc.getString("scissors") + "'," +
                                "'" + misc.getString("syringe_cutter") + "'," +
                                "'" + misc.getString("others") + "'," +
                                "'" + supervision.getString("lhs") + "'," +
                                "'" + supervision.getString("dco") + "'," +
                                "'" + supervision.getString("adc") + "'," +
                                "'" + supervision.getString("fpo") + "'," +
                                "'" + supervision.getString("ppiu") + "'" +
                                ")";

                        Log.d("000555", query_form_get_data);
                        boolean query = ls.executeNonQuery(query_form_get_data);
                        Log.d("000555", "Receiving Data count: " + query);

                        progressDialog.dismiss();
                        Toast.makeText(act, reportDownloadSuccessEng, Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Log.d("000222", "Catch: " + e.getMessage());
                        Toast.makeText(act, getString(noDataForEng) + "users " + String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.d("000222", "Err:    " + e.getMessage());
                    Toast.makeText(act, somethingWrong, Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("000222", "onErrorResponse: " + error.getMessage());
                Toast.makeText(act, somethingWrong, Toast.LENGTH_SHORT).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("month", month);
                params.put("year", year);
                params.put("uid", "51846d06295c11eab3eb42010a800003");
                params.put("lhw", login_useruid);


                Log.d("000222", "mParam " + params);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq1, REQUEST_TAG);
    }

    public void Dialog_UserPhoneNumber() {

      /*  LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.dialog_search_with_phonenumber_layout, null);

        final AlertDialog alertD = new AlertDialog.Builder(this).create();*/

        final Dialog alertD = new Dialog(LHW_Navigation_Activity.this);
        LayoutInflater layout = LayoutInflater.from(LHW_Navigation_Activity.this);
        final View promptView = layout.inflate(R.layout.dialog_search_with_phonenumber_layout, null);

        alertD.setContentView(promptView);
        alertD.setCanceledOnTouchOutside(false);
        alertD.setCancelable(false);
        alertD.getWindow().getAttributes().windowAnimations = style.DialogAnimation; //style id
        alertD.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertD.show();


        iv_close = (ImageView) promptView.findViewById(id.iv_close);
        final EditText et_phone_number = (EditText) promptView.findViewById(id.et_phone_number);
        final Button btn_jaari_rhy = (Button) promptView.findViewById(id.btn_jaari_rhy);

        et_phone_number.addTextChangedListener(new TextWatcher() {
            int previous = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previous = et_phone_number.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                btn_jaari_rhy.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if ((previous < length) && (length == 4)) {
                    String data = et_phone_number.getText().toString();
                    et_phone_number.setText(data + "-");
                    et_phone_number.setSelection(length + 1);
                }
            }
        });


        /*alertD.setView(promptView);
        alertD.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        alertD.setCanceledOnTouchOutside(false);
        alertD.setCancelable(false);
        alertD.show();*/


        try {
            Lister ls = new Lister(LHW_Navigation_Activity.this);
            ls.createAndOpenDB();

            String[][] mData = ls.executeReader("Select phone_number from SMS_NUMBER where user_uid = '" + login_useruid + "'");

            if (mData != null) {
                Log.d("000369", "PHONE NUMBER: " + mData[0][0]);
                et_phone_number.setText(mData[0][0]);
                number = mData[0][0];
                phonenumber = true;
                btn_jaari_rhy.setVisibility(View.GONE);
                btn_jaari_rhy.setText(numEdit);
            } else {
                phonenumber = false;
                Log.d("000369", "NO PHONE NUMBER");
                btn_jaari_rhy.setText("جمع کریں");
            }

        } catch (Exception e) {
            phonenumber = false;
            Log.d("000369", "Read PHONE NUMBER Err: " + e.getMessage());
        }


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
            }
        });

        btn_jaari_rhy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (et_phone_number.getText().toString().isEmpty()) {
                    Toast.makeText(act, enterPhoneNoPrompt, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_phone_number.getText().length() < 12) {
                    Toast.makeText(act, correctPhoneNoPrompt, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (et_phone_number.getText().toString().equalsIgnoreCase(number)) {
                    alertD.dismiss();
                    return;
                }

                Lister ls = new Lister(LHW_Navigation_Activity.this);
                ls.createAndOpenDB();
                try {
                    if (phonenumber == true) {

                        String update_number = "update SMS_NUMBER set phone_number = '" + et_phone_number.getText().toString() + "'   where user_uid = '" + login_useruid + "'";
                        Boolean res = ls.executeNonQuery(update_number);
                        Log.d("000369", "Query: " + res);
                        Log.d("000369", "update Data: " + res.toString());

                        if (res.toString().equalsIgnoreCase("true")) {


                            final Snackbar snackbar = Snackbar.make(findViewById(id.navigation_screen), string.mobileNoEdited, Snackbar.LENGTH_SHORT);
                            View mySbView = snackbar.getView();
                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            mySbView.setBackgroundColor(act.getResources().getColor(android.R.color.black));
                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(16);
                            textView.setCompoundDrawablesWithIntrinsicBounds(drawable.ic_check_black_24dp, 0, 0, 0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                textView.setCompoundDrawableTintList(act.getResources().getColorStateList(color.green_color));
                            }
                            snackbar.setDuration(3000);
                            snackbar.show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    alertD.dismiss();
                                }
                            }, 2000);

                        } else {
                            final Snackbar snackbar = Snackbar.make(findViewById(id.navigation_screen), string.mobileNoNotEdited, Snackbar.LENGTH_SHORT);
                            View mySbView = snackbar.getView();
                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            mySbView.setBackgroundColor(act.getResources().getColor(android.R.color.black));
                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(16);
                            textView.setCompoundDrawablesWithIntrinsicBounds(drawable.ic_check_black_24dp, 0, 0, 0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                textView.setCompoundDrawableTintList(act.getResources().getColorStateList(color.green_color));
                            }
                            snackbar.setDuration(4000);
                            snackbar.show();

                        }
                    } else {
                        String ans1 = "insert or ignore into SMS_Number (user_uid,phone_number)" +
                                "values" +
                                "(" +
                                "'" + login_useruid + "'," +
                                "'" + et_phone_number.getText().toString() + "'" +
                                ")";

                        Boolean res = ls.executeNonQuery(ans1);
                        Log.d("000369", "Insert PHONE NUMBER: " + ans1);
                        Log.d("000369", "Query SMSNumber: " + res.toString());

                        if (res.toString().equalsIgnoreCase("true")) {

                            final Snackbar snackbar = Snackbar.make(findViewById(id.navigation_screen), string.mobileNoAdded, Snackbar.LENGTH_SHORT);
                            View mySbView = snackbar.getView();
                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            mySbView.setBackgroundColor(act.getResources().getColor(android.R.color.black));
                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(16);
                            textView.setCompoundDrawablesWithIntrinsicBounds(drawable.ic_check_black_24dp, 0, 0, 0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                textView.setCompoundDrawableTintList(act.getResources().getColorStateList(color.green_color));
                            }
                            snackbar.setDuration(3000);
                            snackbar.show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    alertD.dismiss();
                                }
                            }, 2000);

                        } else {
                            final Snackbar snackbar = Snackbar.make(findViewById(id.navigation_screen), string.mobileNoNotAdded, Snackbar.LENGTH_SHORT);
                            View mySbView = snackbar.getView();
                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            mySbView.setBackgroundColor(act.getResources().getColor(android.R.color.black));
                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(16);
                            textView.setCompoundDrawablesWithIntrinsicBounds(drawable.ic_check_black_24dp, 0, 0, 0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                textView.setCompoundDrawableTintList(act.getResources().getColorStateList(color.green_color));
                            }
                            snackbar.setDuration(4000);
                            snackbar.show();

                        }

                    }

                } catch (Exception e) {
                    Toast.makeText(act, somethingWrong, Toast.LENGTH_SHORT).show();
                    Log.d("000777", "ERROR: " + e.getMessage());
                }

            }
        });


    }

    public void Dialog_SpinnerUserPhoneNumber() {


        final Dialog alertD = new Dialog(LHW_Navigation_Activity.this);
        LayoutInflater layout = LayoutInflater.from(LHW_Navigation_Activity.this);
        final View promptView = layout.inflate(R.layout.dialog_sms_phonenumber, null);

        alertD.setContentView(promptView);
        alertD.setCanceledOnTouchOutside(false);
        alertD.setCancelable(false);
        alertD.getWindow().getAttributes().windowAnimations = style.DialogAnimation; //style id
        alertD.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertD.show();


        iv_close = (ImageView) promptView.findViewById(id.iv_close);
        final Spinner spPhoneNumber = (Spinner) promptView.findViewById(id.spPhoneNumber);
        final Button btn_jaari_rhy = (Button) promptView.findViewById(id.btn_jaari_rhy);

        spinner_number(spPhoneNumber);


        try {
            Lister ls = new Lister(LHW_Navigation_Activity.this);
            ls.createAndOpenDB();

            String[][] mData = ls.executeReader("Select phone_number from SMS_NUMBER where user_uid = '" + login_useruid + "'");

            if (mData != null) {
                Log.d("000369", "PHONE NUMBER: " + mData[0][0]);
                String[] data = act.getResources().getStringArray(array.array_sms_numbers);
                Log.d("000369", "PHONE NUMBER length: " + data.length);
                Log.d("000369", "PHONE NUMBER length: " + data[0]);
                Log.d("000369", "PHONE NUMBER length: " + data[1]);

                number = mData[0][0];
                phonenumber = true;


                btn_jaari_rhy.setText(numEdit);

                try {
                    int mPos = 0;
                    for (int i = 0; i < data.length; i++) {
                        Log.d("000369", "LLOOOOOOOOOOOP: " + i);
                        Log.d("000369", "ArrayPerformBy: " + data[i]);
                        if (data[i].trim().equals(mData[0][0].trim())) {
                            Log.d("000369", data[i] + "   ====   " + mData[0][0]);
                            Log.d("000369", "loop value: " + i);

                            mPos = i;
                            spPhoneNumber.setSelection(mPos + 1);
                        } else {
                            Log.d("000369", "PerformedBy NOT Match !!!: ");
                        }
                    }

                } catch (Exception e) {
                    Log.d("000369", "PerformedBy Err: " + e.getMessage());
                }

               /* try {

                    switch (mData[0][0]){
                        case "03455315392":
                            spPhoneNumber.setSelection(1);
                            break;
                        case "03455235762":
                            spPhoneNumber.setSelection(2);
                            break;
                        case "03457981783":
                            spPhoneNumber.setSelection(3);
                            break;
                        case "03452074907":
                            spPhoneNumber.setSelection(4);
                            break;

                        default:
                            break;
                    }
                } catch (Exception e) {
                    Log.d("000100", "PerformedBy Err: " + e.getMessage());
                }*/

            } else {
                phonenumber = false;
                Log.d("000369", "NO PHONE NUMBER");
                btn_jaari_rhy.setText("جمع کریں");
            }

        } catch (Exception e) {
            phonenumber = false;
            Log.d("000369", "Read PHONE NUMBER Err: " + e.getMessage());
        }


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
            }
        });

        btn_jaari_rhy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (spPhoneNumber.getSelectedItemPosition() == 0) {
                    Toast.makeText(act, string.selectPhoneNoPrompt, Toast.LENGTH_SHORT).show();
                    return;
                }


                if (spPhoneNumber.getSelectedItem().toString().equalsIgnoreCase(number)) {
                    alertD.dismiss();
                    return;
                }

                Lister ls = new Lister(LHW_Navigation_Activity.this);
                ls.createAndOpenDB();
                try {
                    if (phonenumber == true) {

                        String update_number = "update SMS_NUMBER set phone_number = '" + spPhoneNumber.getSelectedItem().toString() + "'   where user_uid = '" + login_useruid + "'";
                        Boolean res = ls.executeNonQuery(update_number);
                        Log.d("000369", "Query: " + res);
                        Log.d("000369", "update Data: " + res.toString());

                        if (res.toString().equalsIgnoreCase("true")) {


                            final Snackbar snackbar = Snackbar.make(findViewById(id.navigation_screen), string.mobileNoEdited, Snackbar.LENGTH_SHORT);
                            View mySbView = snackbar.getView();
                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            mySbView.setBackgroundColor(act.getResources().getColor(android.R.color.black));
                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(16);
                            textView.setCompoundDrawablesWithIntrinsicBounds(drawable.ic_check_black_24dp, 0, 0, 0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                textView.setCompoundDrawableTintList(act.getResources().getColorStateList(color.green_color));
                            }
                            snackbar.setDuration(3000);
                            snackbar.show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    alertD.dismiss();
                                }
                            }, 2000);

                        } else {
                            final Snackbar snackbar = Snackbar.make(findViewById(id.navigation_screen), string.mobileNoNotEdited, Snackbar.LENGTH_SHORT);
                            View mySbView = snackbar.getView();
                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            mySbView.setBackgroundColor(act.getResources().getColor(android.R.color.black));
                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(16);
                            textView.setCompoundDrawablesWithIntrinsicBounds(drawable.ic_check_black_24dp, 0, 0, 0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                textView.setCompoundDrawableTintList(act.getResources().getColorStateList(color.green_color));
                            }
                            snackbar.setDuration(4000);
                            snackbar.show();

                        }
                    } else {
                        String ans1 = "insert or ignore into SMS_Number (user_uid,phone_number)" +
                                "values" +
                                "(" +
                                "'" + login_useruid + "'," +
                                "'" + spPhoneNumber.getSelectedItem().toString() + "'" +
                                ")";

                        Boolean res = ls.executeNonQuery(ans1);
                        Log.d("000369", "Insert PHONE NUMBER: " + ans1);
                        Log.d("000369", "Query SMSNumber: " + res.toString());

                        if (res.toString().equalsIgnoreCase("true")) {

                            final Snackbar snackbar = Snackbar.make(findViewById(id.navigation_screen), string.mobileNoAdded, Snackbar.LENGTH_SHORT);
                            View mySbView = snackbar.getView();
                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            mySbView.setBackgroundColor(act.getResources().getColor(android.R.color.black));
                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(16);
                            textView.setCompoundDrawablesWithIntrinsicBounds(drawable.ic_check_black_24dp, 0, 0, 0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                textView.setCompoundDrawableTintList(act.getResources().getColorStateList(color.green_color));
                            }
                            snackbar.setDuration(3000);
                            snackbar.show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    alertD.dismiss();
                                }
                            }, 2000);

                        } else {
                            final Snackbar snackbar = Snackbar.make(findViewById(id.navigation_screen), string.mobileNoNotAdded, Snackbar.LENGTH_SHORT);
                            View mySbView = snackbar.getView();
                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            mySbView.setBackgroundColor(act.getResources().getColor(android.R.color.black));
                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(16);
                            textView.setCompoundDrawablesWithIntrinsicBounds(drawable.ic_check_black_24dp, 0, 0, 0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                textView.setCompoundDrawableTintList(act.getResources().getColorStateList(color.green_color));
                            }
                            snackbar.setDuration(4000);
                            snackbar.show();

                        }

                    }

                } catch (Exception e) {
                    Toast.makeText(act, somethingWrong, Toast.LENGTH_SHORT).show();
                    Log.d("000777", "ERROR: " + e.getMessage());
                }

            }
        });


    }

    private void spinner_number(Spinner spPhoneNumber) {


        final ArrayAdapter<CharSequence> adptr_topic = ArrayAdapter.createFromResource(this, array.array_sms_numbers, layout.sp_title_topic_layout);
        adptr_topic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spPhoneNumber.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_topic,
                        layout.sp_title_topic_layout,
                        this));


        spPhoneNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(LHW_Navigation_Activity.this, HomePage_Activity.class);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition(anim.right_in, anim.left_out);

        super.onBackPressed();
    }
}

