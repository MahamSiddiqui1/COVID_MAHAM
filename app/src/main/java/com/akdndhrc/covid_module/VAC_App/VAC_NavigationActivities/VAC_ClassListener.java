package com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.Login_Activity;
import com.akdndhrc.covid_module.Utils;
import com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities.VAC_SearchActivities.VAC_Search_Activity;
import com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities.VAC_SyncDataActivities.VAC_SyncImages_Activity;
import com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities.VAC_SyncDataActivities.VAC_SyncItems_Activity;
import com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities.VAC_VideoActivities.VAC_VideoList_Activity;
import com.akdndhrc.covid_module.slider.SlideMenu;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.VAC_App.VAC_InsideOutsideUC.VAC_BelowTwoRegister_Activity;
import com.akdndhrc.covid_module.VAC_App.VAC_StatsActivity;
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




public class VAC_ClassListener implements OnClickListener {

    private Activity act;

    public static SlideMenu mSlideMenu;

    static boolean isClick = false;
    Intent global;

    RelativeLayout rl_search, rl_logout, rl_inside_union_council, rl_outside_union_council, rl_videos, rl_sync_data, rl_sync_images, rl_bluetooth_share,rl_adaad_O_shumaar;
    ImageView image_cancel;
    RelativeLayout rl_dou_sal_sy_kum_umer, rl_dou_sal_sy_zyada_umer;

    Dialog alertDialog;

    ImageView iv_Satisfied, iv_VerySatisfied, iv_Neutral, iv_UnSatisfied, iv_VeryUnSatisfied, iv_close, iv_feedback_emoji;
    LinearLayout ll_emoji, ll_feedback;
    TextView tv_give_feedback, tv_not_now;
    RatingBar ratingBar;
    String temp_var, temp_name, timeStamp;
    Button btn_jamaa_kre;
    ProgressBar progressBar;
    String login_useruid,login_username;


    public VAC_ClassListener(Activity act, SlideMenu slideMenu) {
        // TODO Auto-generated constructor stub
        this.act = act;
        mSlideMenu = slideMenu;

        init();

    }

    public void init() {

        //Get shared USer name
        try {
            SharedPreferences prefelse = act.getApplicationContext().getSharedPreferences(String.valueOf(R.string.userLogin), 0); // 0 - for private mode
            String usernaame = prefelse.getString(act.getString(R.string.username), null); // getting String
            String shared_useruid = prefelse.getString((String.valueOf(R.string.loginUserIDEng)), null); // getting String
            login_useruid = shared_useruid;
            login_username = usernaame;
            Log.d("000555", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000555", "Shared Err:" + e.getMessage());
        }


        //ImageView
        image_cancel = act.findViewById(R.id.image_cancel);
        image_cancel.setOnClickListener(this);

        //Relative Layout
        rl_inside_union_council = act.findViewById(R.id.rl_inside_union_council);
        rl_outside_union_council = act.findViewById(R.id.rl_outside_union_council);
        rl_adaad_O_shumaar = act.findViewById(R.id.rl_adaad_O_shumaar);
        rl_sync_data = act.findViewById(R.id.rl_sync_data);
        rl_sync_images = act.findViewById(R.id.rl_sync_images);
        rl_bluetooth_share = act.findViewById(R.id.rl_bluetooth_share);
        rl_videos = act.findViewById(R.id.rl_videos);
        rl_search = act.findViewById(R.id.rl_search);
        rl_logout = act.findViewById(R.id.rl_logout);


        rl_inside_union_council.setOnClickListener(this);
        rl_outside_union_council.setOnClickListener(this);
        rl_videos.setOnClickListener(this);
        rl_sync_data.setOnClickListener(this);
        rl_sync_images.setOnClickListener(this);
        rl_search.setOnClickListener(this);
        rl_bluetooth_share.setOnClickListener(this);
        rl_logout.setOnClickListener(this);
        rl_adaad_O_shumaar.setOnClickListener(this);


        mSlideMenu.setEdgeSlideEnable(true);


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub


        switch (v.getId()) {

            case R.id.image_cancel:

                mSlideMenu.close(true);
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
                act.startActivity(new Intent(act, VAC_SyncItems_Activity.class));
                // act.startActivity(new Intent(act, SyncItems_Activity.class));
                break;

            case R.id.rl_sync_images:
                Log.d("000555", "NAAAAA: ");
                act.startActivity(new Intent(act, VAC_SyncImages_Activity.class));
                break;

            case R.id.rl_adaad_O_shumaar:

                act.startActivity(new Intent(act, VAC_StatsActivity.class));
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
                    copyDatabase(act, "HayatPKDB");
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
    public void copyDatabase(Activity c, String DATABASE_NAME) {
        String databasePath = c.getDatabasePath(DATABASE_NAME).getPath();
        File f = new File(databasePath);
        OutputStream myOutput = null;
        InputStream myInput = null;
        Log.d("000222", " testing db path " + databasePath);
        Log.d("000222", " testing db exist " + f.exists());


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


    /*public void openClose() {

        if (isClick == false) {
            mSlideMenu.open(false, true);
            Toast.makeText(act, "1", Toast.LENGTH_SHORT).show();

            isClick = true;
        } else {
            mSlideMenu.close(true);
            Toast.makeText(act, "2", Toast.LENGTH_SHORT).show();
            isClick = false;
        }
    }*/


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


        iv_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        iv_VerySatisfied.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_emoji.setVisibility(View.GONE);
                iv_feedback_emoji.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.VISIBLE);
                btn_jamaa_kre.setVisibility(View.VISIBLE);
                tv_not_now.setVisibility(View.VISIBLE);
                iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.v_satisfied));
                tv_give_feedback.setText(R.string.feedbackAboutHayatApp);
                ratingBar.setRating(5);
                temp_var = "5";
                temp_name = "Very Satisfied";
            }
        });

        iv_Satisfied.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_emoji.setVisibility(View.GONE);
                iv_feedback_emoji.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.VISIBLE);
                btn_jamaa_kre.setVisibility(View.VISIBLE);
                tv_not_now.setVisibility(View.VISIBLE);
                iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.satisfied));
                tv_give_feedback.setText(R.string.feedbackAboutHayatApp);
                ratingBar.setRating(4);
                temp_var = "4";
                temp_name = "Satisfied";
            }
        });

        iv_Neutral.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_emoji.setVisibility(View.GONE);
                iv_feedback_emoji.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.VISIBLE);
                btn_jamaa_kre.setVisibility(View.VISIBLE);
                tv_not_now.setVisibility(View.VISIBLE);
                iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.neutral));
                tv_give_feedback.setText(R.string.feedbackAboutHayatApp);
                ratingBar.setRating(3);
                temp_var = "3";
                temp_name = "Neutral";
            }
        });

        iv_UnSatisfied.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_emoji.setVisibility(View.GONE);
                iv_feedback_emoji.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.VISIBLE);
                btn_jamaa_kre.setVisibility(View.VISIBLE);
                tv_not_now.setVisibility(View.VISIBLE);
                iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.unsatisfied));
                tv_give_feedback.setText(R.string.feedbackAboutHayatApp);
                ratingBar.setRating(2);
                temp_var = "2";
                temp_name = "UnSatisfied";
            }
        });

        iv_VeryUnSatisfied.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_emoji.setVisibility(View.GONE);
                iv_feedback_emoji.setVisibility(View.VISIBLE);
                ratingBar.setVisibility(View.VISIBLE);
                btn_jamaa_kre.setVisibility(View.VISIBLE);
                tv_not_now.setVisibility(View.VISIBLE);
                iv_feedback_emoji.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.v_unsatisfied));
                tv_give_feedback.setText(R.string.feedbackAboutHayatApp);
                ratingBar.setRating(1);
                temp_var = "1";
                temp_name = "Very UnSatisfied";
            }
        });
        tv_not_now.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent1 = new Intent(act, Login_Activity.class);
                act.startActivity(intent1);
            }
        });

        btn_jamaa_kre.setOnClickListener(new OnClickListener() {
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

                            if (Utils.haveNetworkConnection(act)>0) {

                                sendPostRequest(Integer.valueOf(temp_var), String.valueOf(jobj), login_useruid, added_on);
                            }
                            else{
                            }


                        } catch (Exception e) {
                            alertDialog.dismiss();
                            Log.d("000555", "Err: " + e.getMessage());
                        } finally {
                            alertDialog.dismiss();
                            Intent intent1 = new Intent(act, Login_Activity.class);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            act.startActivity(intent1);
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

                        Toast.makeText(act, R.string.dataSynced, Toast.LENGTH_SHORT).show();
                     //   Toast.makeText(act, "Your response submitted successfully.", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000555", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                     //   Toast.makeText(act, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", "catch    " + e.getMessage());
                    //Toast.makeText(act, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "error    " + error.getMessage());
               // Toast.makeText(act, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast.makeText(act, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();

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


    public void startNewAct() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                act.startActivity(global);
            }
        }, 1000);
    }


}