package com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities.VAC_SyncDataActivities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.akdndhrc.covid_module.R;

public class VAC_SyncItems_Activity extends AppCompatActivity {

    Context ctx = VAC_SyncItems_Activity.this;
    ListView lv;
    ImageView iv_navigation_drawer, iv_home;
    RelativeLayout rl_khandan_reg, rl_reg,  rl_child_hifaziti_teekey_record , rl_feedback_record,rl_video_record,rl_mother_hifaziti_teekey_record;

    TextView txt_khandan_reg, txt_reg,txt_child_hifaziti_teekey_record,txt_video_record,txt_feedback_record,txt_mother_hifaziti_teekey_record;

    ProgressBar pb_2, pb_3,pb_4,pb_5,pb_11;
    Dialog alertDialog;
    /*ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    Adt_SyncItems adt;*/
    String[][] data_khandan,data_Member,data_CVaccination,data_MVaccine,data_Video,data_Feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_items_vac);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this,
                VAC_SyncItems_Activity.class));

        Log.d("000555", "ONCREATEEE");

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);

        //Relative Layout
        rl_khandan_reg = findViewById(R.id.rl_khandan_reg);
        rl_reg = findViewById(R.id.rl_reg);
        rl_child_hifaziti_teekey_record = findViewById(R.id.rl_child_hifaziti_teekey_record);
        rl_video_record = findViewById(R.id.rl_video_record);
        rl_feedback_record = findViewById(R.id.rl_feedback_record);
        rl_mother_hifaziti_teekey_record = findViewById(R.id.rl_mother_hifaziti_teekey_record);

        //TextView
        txt_khandan_reg = findViewById(R.id.txt_khandan_reg);
        txt_reg = findViewById(R.id.txt_reg);
        txt_child_hifaziti_teekey_record = findViewById(R.id.txt_child_hifaziti_teekey_record);
        txt_video_record = findViewById(R.id.txt_video_record);
        txt_feedback_record = findViewById(R.id.txt_feedback_record);
        txt_mother_hifaziti_teekey_record = findViewById(R.id.txt_mother_hifaziti_teekey_record);

        //ProgressBar

        pb_2 = findViewById(R.id.pb_2);
        pb_3 = findViewById(R.id.pb_3);
        pb_4 = findViewById(R.id.pb_4);
        pb_5 = findViewById(R.id.pb_5);
        pb_11 = findViewById(R.id.pb_11);



        rl_khandan_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(data_khandan[0][0]) > 0){
                    startActivity(new Intent(ctx, VAC_SyncKhandanReg_Activity.class));

                }else{
                    final Snackbar snackbar = Snackbar.make(v, "سینک کرنے کے لئے کوئی ڈیٹا مجود نہیں.", Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(15);
                    snackbar.setDuration(2000);
                    snackbar.show();
                }
            }
        });

        rl_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.valueOf(data_Member[0][0]) > 0){

                    startActivity(new Intent(ctx, VAC_SyncMemberReg_Activity.class));
                }else{
                    final Snackbar snackbar = Snackbar.make(v, "سینک کرنے کے لئے کوئی ڈیٹا مجود نہیں.", Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(15);
                    snackbar.setDuration(2000);
                    snackbar.show();
                }
            }
        });


        rl_child_hifaziti_teekey_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.valueOf(data_CVaccination[0][0]) > 0){
                    startActivity(new Intent(ctx, VAC_SyncChildHifazitiTeekey_Activity.class));

                }else{
                    final Snackbar snackbar = Snackbar.make(v, "سینک کرنے کے لئے کوئی ڈیٹا مجود نہیں.", Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(15);
                    snackbar.setDuration(2000);
                    snackbar.show();
                }
            }
        });
        rl_video_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.valueOf(data_Video[0][0]) > 0){

                    startActivity(new Intent(ctx, VAC_SyncVideo_Activity.class));
                }else{
                    final Snackbar snackbar = Snackbar.make(v, "سینک کرنے کے لئے کوئی ڈیٹا مجود نہیں.", Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(15);
                    snackbar.setDuration(2000);
                    snackbar.show();
                }
            }
        });

        rl_feedback_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.valueOf(data_Feedback[0][0]) > 0){
                    startActivity(new Intent(ctx, VAC_SyncFeedback_Activity.class));

                }else{
                    final Snackbar snackbar = Snackbar.make(v, "سینک کرنے کے لئے کوئی ڈیٹا مجود نہیں.", Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(15);
                    snackbar.setDuration(2000);
                    snackbar.show();
                }
            }
        });

        rl_mother_hifaziti_teekey_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(data_MVaccine[0][0]) > 0){
                    startActivity(new Intent(ctx, VAC_SyncMotherHifazitiTeekey_Activity.class));

                }else{
                    final Snackbar snackbar = Snackbar.make(v, "سینک کرنے کے لئے کوئی ڈیٹا مجود نہیں.", Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(15);
                    snackbar.setDuration(2000);
                    snackbar.show();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            txt_khandan_reg.setVisibility(View.GONE);
            txt_reg.setVisibility(View.GONE);

            txt_child_hifaziti_teekey_record.setVisibility(View.GONE);
            txt_video_record.setVisibility(View.GONE);
            txt_feedback_record.setVisibility(View.GONE);
            txt_mother_hifaziti_teekey_record.setVisibility(View.GONE);

            pb_2.setVisibility(View.VISIBLE);
            pb_3.setVisibility(View.VISIBLE);
            pb_4.setVisibility(View.VISIBLE);
            pb_5.setVisibility(View.VISIBLE);
            pb_11.setVisibility(View.VISIBLE);

            Log.d("000555", "On Resume");

            Khanndan_registration();

        } catch (Exception e) {
            Log.d("000555", "Catach Err: " + e.getMessage());
        }
    }

    public void Khanndan_registration() {

        try {
            Lister ls = new Lister(ctx);
           ls.createAndOpenDB();
            data_khandan = ls.executeReader("Select count(id) from KHANDAN where is_synced = '0'");

            txt_khandan_reg.setVisibility(View.VISIBLE);
            txt_khandan_reg.setText(data_khandan[0][0]);
            Log.d("000555", "TRY: " + data_khandan[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    member_registration();

                }
            },200);



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
            },200);


        }

    }

    public void member_registration() {

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            data_Member = ls.executeReader("Select count(id) from MEMBER where is_synced = '0'");


            pb_2.setVisibility(View.GONE);
            txt_reg.setVisibility(View.VISIBLE);
            txt_reg.setText(data_Member[0][0]);
            Log.d("000555", "TRY: " + data_Member[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    child_vaccination();

                }
            },200);


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
            },200);


        }
    }

    public void child_vaccination() {
        try {
            Lister ls = new Lister(ctx);
           ls.createAndOpenDB();
            data_CVaccination = ls.executeReader("Select count(id) from CVACCINATION where is_synced= '0' ");
            pb_3.setVisibility(View.GONE);
            txt_child_hifaziti_teekey_record.setVisibility(View.VISIBLE);
            txt_child_hifaziti_teekey_record.setText(data_CVaccination[0][0]);
            Log.d("000555", "TRY: " + data_CVaccination[0][0]);

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
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            data_MVaccine = ls.executeReader("Select count(id) from MVACINE where is_synced= '0' ");
            pb_11.setVisibility(View.GONE);
            txt_mother_hifaziti_teekey_record.setVisibility(View.VISIBLE);
            txt_mother_hifaziti_teekey_record.setText(data_MVaccine[0][0]);
            Log.d("000555", "TRY: " + data_MVaccine[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    video_record();

                }
            }, 200);

        } catch (Exception e) {
            pb_11.setVisibility(View.GONE);
            txt_mother_hifaziti_teekey_record.setVisibility(View.VISIBLE);

            txt_mother_hifaziti_teekey_record.setText("0");
            Log.d("000555", "MVACINE Err: " + e.getMessage());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    video_record();

                }
            }, 200);

        }
    }

    public void video_record() {
        try {
            Lister ls = new Lister(ctx);
           ls.createAndOpenDB();
            data_Video = ls.executeReader("Select count(id) from VIDEOS where is_synced= '0' ");
            pb_4.setVisibility(View.GONE);
            txt_video_record.setVisibility(View.VISIBLE);
            txt_video_record.setText(data_Video[0][0]);
            Log.d("000555", "TRY: " + data_Video[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    feedback_record();

                }
            }, 200);


        } catch (Exception e) {
            pb_4.setVisibility(View.GONE);
            txt_video_record.setVisibility(View.VISIBLE);
            txt_video_record.setText("0");
            Log.d("000555", "Video Err: " + e.getMessage());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    feedback_record();

                }
            }, 200);


        }
    }

    public void feedback_record() {
        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            data_Feedback = ls.executeReader("Select count(*) from FEEDBACK where is_synced= '0' ");
            pb_5.setVisibility(View.GONE);
            txt_feedback_record.setVisibility(View.VISIBLE);
            txt_feedback_record.setText(data_Feedback[0][0]);
            Log.d("000555", "TRY: " + data_Feedback[0][0]);

        } catch (Exception e) {
            pb_5.setVisibility(View.GONE);
            txt_feedback_record.setVisibility(View.VISIBLE);
            txt_feedback_record.setText("0");
            Log.d("000555", "Feedbac Err: " + e.getMessage());

        }
    }



}
