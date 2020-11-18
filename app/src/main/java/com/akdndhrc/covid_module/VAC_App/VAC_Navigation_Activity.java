package com.akdndhrc.covid_module.VAC_App;

import android.app.Dialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.Login_Activity;
import com.akdndhrc.covid_module.NothingSelectedSpinnerAdapter;
import com.akdndhrc.covid_module.Utils;
import com.akdndhrc.covid_module.VAC_App.VAC_InsideOutsideUC.VAC_BelowTwoRegister_Activity;
import com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities.VAC_SearchActivities.VAC_Search_Activity;
import com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities.VAC_StockActivity;
import com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities.VAC_SyncDataActivities.VAC_SyncAndDownloadData_TabActivity;
import com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities.VAC_SyncDataActivities.VAC_SyncVaccineImages_Activity;
import com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities.VAC_VideoActivities.VAC_VideoList_Activity;
import com.akdndhrc.covid_module.R;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class VAC_Navigation_Activity extends AppCompatActivity implements View.OnClickListener {


    Context act = VAC_Navigation_Activity.this;


    RelativeLayout rl_search, rl_logout, rl_inside_union_council, rl_outside_union_council, rl_videos, rl_sync_data, rl_sync_images, rl_bluetooth_share, rl_adaad_O_shumaar,rl_stock;
    ImageView image_cancel;
    RelativeLayout rl_dou_sal_sy_kum_umer, rl_dou_sal_sy_zyada_umer;

    Dialog alertDialog;

    ImageView iv_Satisfied, iv_VerySatisfied, iv_Neutral, iv_UnSatisfied, iv_VeryUnSatisfied, iv_close, iv_feedback_emoji;
    LinearLayout ll_emoji, ll_feedback;
    TextView tv_give_feedback, tv_not_now,tv_username,tv_LatLng;
    RatingBar ratingBar;
    String temp_var, temp_name, timeStamp;
    Button btn_jamaa_kre;
    ProgressBar progressBar;
    String login_useruid,login_username;

    double gps_latitude,gps_longitude;

    ImageView image_phonenumber;
    boolean phonenumber = false;
    String number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vac_navigation);

        //Get shared USer name
        try {
            SharedPreferences prefelse = act.getApplicationContext().getSharedPreferences("UserLogin", 0); // 0 - for private mode

            String usernaame = prefelse.getString("username", null); // getting String
            String shared_useruid = prefelse.getString("login_userid", null); // getting String
            login_useruid = shared_useruid;
            login_username = usernaame;
            Log.d("000555", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000555", "Shared Err:" + e.getMessage());
        }

        try {
            ////////////Get GPS latlng in shared pref
            SharedPreferences prefw = getApplicationContext().getSharedPreferences("LoginGPS", 0); // 0 - for private mode
            String latitude = prefw.getString("gps_latitude", null); // getting String
            String longitude = prefw.getString("gps_longitude", null); // getting String
            gps_latitude = Double.parseDouble(latitude);
            gps_longitude = Double.parseDouble(longitude);

            Log.d("000555", "Shared Latitude: " + gps_latitude);
            Log.d("000555", "Shared Longitude: " + gps_longitude);

        } catch (Exception e) {
            Log.d("000555", "SharedPred GPS Catch: " + e.getMessage());
        }


        tv_username = findViewById(R.id.tv_username);
        tv_username.setText(login_username);

        //TextView GPS
        tv_LatLng = findViewById(R.id.tv_LatLng);
        float latitude = Float.parseFloat(String.format("%.5f", gps_latitude));
        float longitude = Float.parseFloat(String.format("%.5f", gps_longitude));

        Log.d("000555", " Latitude: " + latitude);
        Log.d("000555", " Longitude: " + longitude);

        tv_LatLng.setText(String.valueOf(latitude) +" | " + String.valueOf(longitude));

        //ImageView
        image_phonenumber = findViewById(R.id.image_phonenumber);
        image_cancel = findViewById(R.id.image_close);
        image_cancel.setOnClickListener(this);
        image_phonenumber.setOnClickListener(this);

        //Relative Layout
        rl_inside_union_council = findViewById(R.id.rl_inside_union_council);
        rl_outside_union_council = findViewById(R.id.rl_outside_union_council);
        rl_adaad_O_shumaar = findViewById(R.id.rl_adaad_O_shumaar);
        rl_sync_data = findViewById(R.id.rl_sync_data);
        rl_sync_images = findViewById(R.id.rl_sync_images);
        rl_bluetooth_share = findViewById(R.id.rl_bluetooth_share);
        rl_videos = findViewById(R.id.rl_videos);
        rl_search = findViewById(R.id.rl_search);
        rl_logout = findViewById(R.id.rl_logout);
        rl_stock = findViewById(R.id.rl_stock);


        rl_inside_union_council.setOnClickListener(this);
        rl_outside_union_council.setOnClickListener(this);
        rl_videos.setOnClickListener(this);
        rl_sync_data.setOnClickListener(this);
        rl_sync_images.setOnClickListener(this);
        rl_search.setOnClickListener(this);
        rl_bluetooth_share.setOnClickListener(this);
        rl_logout.setOnClickListener(this);
        rl_adaad_O_shumaar.setOnClickListener(this);
        rl_stock.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub


        switch (v.getId()) {

            case R.id.image_close:

                Log.d("000555", "Query:" );
                onBackPressed();

                break;

            case R.id.image_phonenumber:

                Dialog_SpinnerUserPhoneNumber();

                break;


            case R.id.rl_inside_union_council:


                alertDialog = new Dialog(act);
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
                        Intent newIntent = new Intent(act, VAC_BelowTwoRegister_Activity.class);
                        newIntent.putExtra("union_council", "1");
                        act.startActivity(newIntent);
                    }
                }, 1000);


                break;

            case R.id.rl_search:
                Intent newIntent = new Intent(act, VAC_Search_Activity.class);
                act.startActivity(newIntent);

                break;

            case R.id.rl_outside_union_council:

                alertDialog = new Dialog(act);
                LayoutInflater layoutInflater = LayoutInflater.from(act);
                final View dialogViewView = layoutInflater.inflate(R.layout.lay_dialog_loading3, null);

                alertDialog.setContentView(dialogViewView);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();


                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                        Intent intent = new Intent(act, VAC_BelowTwoRegister_Activity.class);
                        intent.putExtra("union_council", "0");
                        act.startActivity(intent);
                    }
                }, 1000);

                break;

            case R.id.rl_videos:

                act.startActivity(new Intent(act, VAC_VideoList_Activity.class));
                break;

            case R.id.rl_sync_data:
                Log.d("000555", "NA: ");
                act.startActivity(new Intent(act, VAC_SyncAndDownloadData_TabActivity.class));
                // act.startActivity(new Intent(act, VAC_SyncItems_Activity.class));
                break;

            case R.id.rl_sync_images:
                Log.d("000555", "NAAAAA: ");
             //   act.startActivity(new Intent(act, VAC_SyncImages_Activity.class));
                act.startActivity(new Intent(act, VAC_SyncVaccineImages_Activity.class));
                break;

            case R.id.rl_adaad_O_shumaar:
                act.startActivity(new Intent(act, VAC_StatsActivity.class));
                break;

            case R.id.rl_stock:
                act.startActivity(new Intent(act, VAC_StockActivity.class));
                break;

            case R.id.rl_bluetooth_share:

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
                            + File.separator + "HayatPKDB" + "-" + login_username+ "-" + timeStamp + ".db"));


                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                            "Sharing File...");
                    intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                    act.startActivity(Intent.createChooser(intentShareFile, "Share with.."));

                } catch (Exception e)

                {
                    Log.d("000222", " ERRR:  " + e.getMessage());
                }

                break;

            case R.id.rl_logout:

                Feedback_Dialog();

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
                    + File.separator + DATABASE_NAME + "-" +login_username+ "-" + timeStamp + ".db");

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
        ll_emoji = dialogView.findViewById(R.id.ll_emoji);
        ll_feedback = dialogView.findViewById(R.id.ll_feedback);


        //ProgressBar
        progressBar = dialogView.findViewById(R.id.pbProgress);

        //Button
        btn_jamaa_kre = dialogView.findViewById(R.id.submit);

        //Rating
        ratingBar = dialogView.findViewById(R.id.rating_star);
        ratingBar.setEnabled(false);

        //ImageView
        iv_close = dialogView.findViewById(R.id.iv_close);
        iv_Neutral = dialogView.findViewById(R.id.iv_Neutral);
        iv_Satisfied = dialogView.findViewById(R.id.iv_Satisfied);
        iv_VerySatisfied = dialogView.findViewById(R.id.iv_VerySatisfied);
        iv_UnSatisfied = dialogView.findViewById(R.id.iv_UnSatisfied);
        iv_VeryUnSatisfied = dialogView.findViewById(R.id.iv_VeryUnSatisfied);
        iv_feedback_emoji = dialogView.findViewById(R.id.iv_feedback_emoji);

        //TextView
        tv_give_feedback = dialogView.findViewById(R.id.tv_give_feedback);
        tv_not_now = dialogView.findViewById(R.id.tv_not_now);


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
                iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.v_satisfied));
                tv_give_feedback.setText("حیات ایپلیکیشن کے استعمال سے متعلق آپ کی رائے کا شکریہ");
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
                iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.satisfied));
                tv_give_feedback.setText("حیات ایپلیکیشن کے استعمال سے متعلق آپ کی رائے کا شکریہ");
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
                iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.neutral));
                tv_give_feedback.setText("حیات ایپلیکیشن کے استعمال سے متعلق آپ کی رائے کا شکریہ");
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
                iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.unsatisfied));
                tv_give_feedback.setText("حیات ایپلیکیشن کے استعمال سے متعلق آپ کی رائے کا شکریہ");
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
                iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.v_unsatisfied));
                tv_give_feedback.setText("حیات ایپلیکیشن کے استعمال سے متعلق آپ کی رائے کا شکریہ");
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


    private void sendPostRequest(final Integer feedback, final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/generic/feedback";

        Log.d("000555", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

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

                        Toast.makeText(act, "Data has been synced", Toast.LENGTH_SHORT).show();
                        //   Toast.makeText(act, "Your response submitted successfully.", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000555", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        //   Toast.makeText(act, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", "catch    " + e.getMessage());
                    //Toast.makeText(act, "Data has been sent incorrectly.", Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "onErrorResponse: " + error.getMessage());
                // Toast.makeText(act, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast.makeText(act, "Data not synced", Toast.LENGTH_SHORT).show();

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
        ll_emoji = dialogView.findViewById(R.id.ll_emoji);
        ll_emoji.setVisibility(View.GONE);

        ll_feedback = dialogView.findViewById(R.id.ll_feedback);

        //Button
        btn_jamaa_kre = dialogView.findViewById(R.id.submit);
        btn_jamaa_kre.setVisibility(View.VISIBLE);

        //Rating
        ratingBar = dialogView.findViewById(R.id.rating_star);
        ratingBar.setVisibility(View.VISIBLE);

        //ImageView
        iv_close = dialogView.findViewById(R.id.iv_close);
        iv_Neutral = dialogView.findViewById(R.id.iv_Neutral);
        iv_Satisfied = dialogView.findViewById(R.id.iv_Satisfied);
        iv_VerySatisfied = dialogView.findViewById(R.id.iv_VerySatisfied);
        iv_UnSatisfied = dialogView.findViewById(R.id.iv_UnSatisfied);
        iv_VeryUnSatisfied = dialogView.findViewById(R.id.iv_VeryUnSatisfied);

        iv_feedback_emoji = dialogView.findViewById(R.id.iv_feedback_emoji);

        //TextView
        tv_give_feedback = dialogView.findViewById(R.id.tv_give_feedback);


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
                    iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.v_unsatisfied));
                } else if (rating == 2) {
                    iv_feedback_emoji.setVisibility(View.VISIBLE);
                    iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.unsatisfied));
                } else if (rating == 3) {
                    iv_feedback_emoji.setVisibility(View.VISIBLE);
                    iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.neutral));
                } else if (rating == 4) {
                    iv_feedback_emoji.setVisibility(View.VISIBLE);
                    iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.satisfied));
                } else if (rating == 5) {
                    iv_feedback_emoji.setVisibility(View.VISIBLE);
                    iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.v_satisfied));
                } else {
                    iv_feedback_emoji.setVisibility(View.GONE);
                    iv_feedback_emoji.setImageDrawable(null);
                }
            }
        });
    }


    public void Dialog_SpinnerUserPhoneNumber() {



        final Dialog alertD = new Dialog(VAC_Navigation_Activity.this);
        LayoutInflater layout = LayoutInflater.from(VAC_Navigation_Activity.this);
        final View promptView = layout.inflate(R.layout.dialog_sms_phonenumber, null);

        alertD.setContentView(promptView);
        alertD.setCanceledOnTouchOutside(false);
        alertD.setCancelable(false);
        alertD.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        alertD.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertD.show();


        iv_close = (ImageView) promptView.findViewById(R.id.iv_close);
        final Spinner spPhoneNumber = (Spinner) promptView.findViewById(R.id.spPhoneNumber);
        final Button btn_jaari_rhy = (Button) promptView.findViewById(R.id.btn_jaari_rhy);

        spinner_number(spPhoneNumber);


        try {
            Lister ls = new Lister(VAC_Navigation_Activity.this);
            ls.createAndOpenDB();

            String[][] mData = ls.executeReader("Select phone_number from SMS_NUMBER where user_uid = '" + login_useruid + "'");

            if (mData != null) {
                Log.d("000369", "PHONE NUMBER: " + mData[0][0]);
                String[] data = act.getResources().getStringArray(R.array.array_sms_numbers);
                Log.d("000369", "PHONE NUMBER length: " + data.length);
                Log.d("000369", "PHONE NUMBER length: " + data[0]);
                Log.d("000369", "PHONE NUMBER length: " + data[1]);

                number= mData[0][0];
                phonenumber = true;


                btn_jaari_rhy.setText("Update number");

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


            } else {
                phonenumber = false;
                Log.d("000369", "NO PHONE NUMBER");
                btn_jaari_rhy.setText("Submit");
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
                    Toast.makeText(act, "Please select a phone number.", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (spPhoneNumber.getSelectedItem().toString().equalsIgnoreCase(number))
                {
                    alertD.dismiss();
                    return;
                }

                Lister ls = new Lister(VAC_Navigation_Activity.this);
                ls.createAndOpenDB();
                try {
                    if (phonenumber == true) {

                        String update_number = "update SMS_NUMBER set phone_number = '" + spPhoneNumber.getSelectedItem().toString() + "'   where user_uid = '" + login_useruid + "'";
                        Boolean res = ls.executeNonQuery(update_number);
                        Log.d("000369", "Query: " + res);
                        Log.d("000369", "update Data: " + res.toString());

                        if (res.toString().equalsIgnoreCase("true")) {


                            final Snackbar snackbar = Snackbar.make(findViewById(R.id.navigation_screen), "Mobile number has been updated.", Snackbar.LENGTH_SHORT);
                            View mySbView = snackbar.getView();
                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            mySbView.setBackgroundColor(act.getResources().getColor(android.R.color.black));
                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(16);
                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                textView.setCompoundDrawableTintList(act.getResources().getColorStateList(R.color.green_color));
                            }
                            snackbar.setDuration(3000);
                            snackbar.show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    alertD.dismiss();
                                }
                            },2000);

                        } else {
                            final Snackbar snackbar = Snackbar.make(findViewById(R.id.navigation_screen), "Mobile number not updated.", Snackbar.LENGTH_SHORT);
                            View mySbView = snackbar.getView();
                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            mySbView.setBackgroundColor(act.getResources().getColor(android.R.color.black));
                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(16);
                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                textView.setCompoundDrawableTintList(act.getResources().getColorStateList(R.color.green_color));
                            }
                            snackbar.setDuration(4000);
                            snackbar.show();

                        }
                    }
                    else {
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

                            final Snackbar snackbar = Snackbar.make(findViewById(R.id.navigation_screen), "Mobile number has been collected.", Snackbar.LENGTH_SHORT);
                            View mySbView = snackbar.getView();
                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            mySbView.setBackgroundColor(act.getResources().getColor(android.R.color.black));
                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(16);
                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                textView.setCompoundDrawableTintList(act.getResources().getColorStateList(R.color.green_color));
                            }
                            snackbar.setDuration(3000);
                            snackbar.show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    alertD.dismiss();
                                }
                            },2000);

                        } else {
                            final Snackbar snackbar = Snackbar.make(findViewById(R.id.navigation_screen), "Mobile number not collected.", Snackbar.LENGTH_SHORT);
                            View mySbView = snackbar.getView();
                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            mySbView.setBackgroundColor(act.getResources().getColor(android.R.color.black));
                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(16);
                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                textView.setCompoundDrawableTintList(act.getResources().getColorStateList(R.color.green_color));
                            }
                            snackbar.setDuration(4000);
                            snackbar.show();

                        }

                    }

                } catch (Exception e) {
                    Toast.makeText(act, "Somthing wrong.", Toast.LENGTH_SHORT).show();
                    Log.d("000777", "ERROR: " + e.getMessage());
                }

            }
        });


    }

    private void   spinner_number(Spinner spPhoneNumber) {


        final ArrayAdapter<CharSequence> adptr_topic = ArrayAdapter.createFromResource(this, R.array.array_sms_numbers, R.layout.sp_title_topic_layout);
        adptr_topic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spPhoneNumber.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_topic,
                        R.layout.sp_title_topic_layout,
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

        Intent intent = new Intent(VAC_Navigation_Activity.this, HomePageVacinator_Activity.class);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);

        super.onBackPressed();
    }
}

