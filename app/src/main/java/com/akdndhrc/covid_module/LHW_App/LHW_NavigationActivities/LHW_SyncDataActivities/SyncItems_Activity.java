package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SyncDataActivities;

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
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;

public class SyncItems_Activity extends AppCompatActivity {

    Context ctx = SyncItems_Activity.this;
    ListView lv;
    ImageView iv_navigation_drawer, iv_home;
    RelativeLayout rl_khandan_reg, rl_reg, rl_child_nasho_numa_record, rl_child_beemari_record, rl_child_hifaziti_teekey_record, rl_child_aam_malomat_record, rl_mother_hifaziti_teekey_record, rl_mother_beemari_record,
            rl_mother_khandani_mansobabndi_record, rl_mother_aam_malomat_record, rl_mother_haaml_record, rl_mother_qabal_Az_pedaish_haaml_record,
            rl_mother_bad_az_pedaish_haaml_record, rl_mother_zachgi_haaml_record, rl_refferal, rl_video_record, rl_feedback_record;


    TextView txt_khandan_reg, txt_reg, txt_child_nasho_numa_recrd, txt_child_beemari_record, txt_child_hifaziti_teekey_record, txt_child_aam_malomat_record, txt_mother_hifaziti_teekey_record, txt_mother_beemari_record,
            txt_mother_khandani_mansobabndi_record, txt_mother_aam_malomat_record, txt_mother_haaml_record, txt_mother_qabal_Az_pedaish_haaml_record,
            txt_mother_bad_az_pedaish_haaml_record, txt_referral_record, txt_mother_zachgi_haaml_record, txt_video_record, txt_feedback_record;

    ProgressBar progressBar, pb_2, pb_3, pb_4, pb_5, pb_6, pb_7, pb_8, pb_9, pb_10, pb_11, pb_12, pb_13, pb_14, pb_15, pb_16, pb_17;
    Dialog alertDialog;
    /*ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    Adt_SyncItems adt;*/
    String[][] data_khandan,data_Member,data_CGrowth,data_CBemari,data_CVaccination,data_CMaloom,data_MPregnancy,data_MANC,data_MDelivery,data_MPNC,data_MVaccine,
    data_MBemari,data_MMaloom,data_MFPlan,data_Referal,data_Video,data_Feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_items);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, SyncItems_Activity.class));

        Log.d("000555", "ONCREATEEE");

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);


        //Relative Layout
        rl_khandan_reg = findViewById(R.id.rl_khandan_reg);
        rl_reg = findViewById(R.id.rl_reg);
        rl_child_nasho_numa_record = findViewById(R.id.rl_child_nasho_numa_record);
        rl_child_beemari_record = findViewById(R.id.rl_child_beemari_record);
        rl_child_hifaziti_teekey_record = findViewById(R.id.rl_child_hifaziti_teekey_record);
        rl_child_aam_malomat_record = findViewById(R.id.rl_child_aam_malomat_record);
        rl_mother_hifaziti_teekey_record = findViewById(R.id.rl_mother_hifaziti_teekey_record);
        rl_mother_beemari_record = findViewById(R.id.rl_mother_beemari_record);
        rl_mother_khandani_mansobabndi_record = findViewById(R.id.rl_mother_khandani_mansobabndi_record);
        rl_mother_aam_malomat_record = findViewById(R.id.rl_mother_aam_malomat_record);
        rl_mother_haaml_record = findViewById(R.id.rl_mother_haaml_record);
        rl_mother_qabal_Az_pedaish_haaml_record = findViewById(R.id.rl_mother_qabal_Az_pedaish_haaml_record);
        rl_mother_qabal_Az_pedaish_haaml_record = findViewById(R.id.rl_mother_qabal_Az_pedaish_haaml_record);
        rl_mother_zachgi_haaml_record = findViewById(R.id.rl_mother_zachgi_haaml_record);
        rl_mother_bad_az_pedaish_haaml_record = findViewById(R.id.rl_mother_bad_az_pedaish_haaml_record);
        rl_refferal = findViewById(R.id.rl_referral_record);
        rl_video_record = findViewById(R.id.rl_video_record);
        rl_feedback_record = findViewById(R.id.rl_feedback_record);

        //TextView
        txt_khandan_reg = findViewById(R.id.txt_khandan_reg);
        txt_reg = findViewById(R.id.txt_reg);
        txt_child_nasho_numa_recrd = findViewById(R.id.txt_child_nasho_numa_recrd);
        txt_child_beemari_record = findViewById(R.id.txt_child_beemari_record);
        txt_child_hifaziti_teekey_record = findViewById(R.id.txt_child_hifaziti_teekey_record);
        txt_child_aam_malomat_record = findViewById(R.id.txt_child_aam_malomat_record);
        txt_mother_hifaziti_teekey_record = findViewById(R.id.txt_mother_hifaziti_teekey_record);
        txt_mother_beemari_record = findViewById(R.id.txt_mother_beemari_record);
        txt_mother_khandani_mansobabndi_record = findViewById(R.id.txt_mother_khandani_mansobabndi_record);
        txt_mother_aam_malomat_record = findViewById(R.id.txt_mother_aam_malomat_record);
        txt_mother_haaml_record = findViewById(R.id.txt_mother_haaml_record);
        txt_mother_qabal_Az_pedaish_haaml_record = findViewById(R.id.txt_mother_qabal_Az_pedaish_haaml_record);
        txt_mother_zachgi_haaml_record = findViewById(R.id.txt_mother_zachgi_haaml_record);
        txt_mother_bad_az_pedaish_haaml_record = findViewById(R.id.txt_mother_bad_az_pedaish_haaml_record);
        txt_referral_record = findViewById(R.id.txt_referral_record);
        txt_video_record = findViewById(R.id.txt_video_record);
        txt_feedback_record = findViewById(R.id.txt_feedback_record);

        //ProgressBar
        progressBar = findViewById(R.id.pbProgress);

        pb_2 = findViewById(R.id.pb_2);
        pb_3 = findViewById(R.id.pb_3);
        pb_4 = findViewById(R.id.pb_4);
        pb_5 = findViewById(R.id.pb_5);
        pb_6 = findViewById(R.id.pb_6);
        pb_7 = findViewById(R.id.pb_7);
        pb_8 = findViewById(R.id.pb_8);
        pb_9 = findViewById(R.id.pb_9);
        pb_10 = findViewById(R.id.pb_10);
        pb_11 = findViewById(R.id.pb_11);
        pb_12 = findViewById(R.id.pb_12);
        pb_13 = findViewById(R.id.pb_13);
        pb_14 = findViewById(R.id.pb_14);
        pb_15 = findViewById(R.id.pb_15);
        pb_16 = findViewById(R.id.pb_16);
        pb_17 = findViewById(R.id.pb_17);


        rl_khandan_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(data_khandan[0][0]) > 0){
                    startActivity(new Intent(ctx, SyncKhandanReg_Activity.class));
                }else{
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
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

                    startActivity(new Intent(ctx, SyncMemberReg_Activity.class));
                }else{
                        final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
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

        rl_child_nasho_numa_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.valueOf(data_CGrowth[0][0]) > 0){
                    startActivity(new Intent(ctx, SyncChildNashoNuma_Activity.class));

                }else{
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
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

        rl_child_beemari_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(data_CBemari[0][0]) > 0){
                    startActivity(new Intent(ctx, SyncChildBemari_Activity.class));

                }else{
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
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
                    startActivity(new Intent(ctx, SyncChildHifazitiTeekey_Activity.class));

                }else{
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
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
        rl_child_aam_malomat_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(data_CMaloom[0][0]) > 0){
                    startActivity(new Intent(ctx, SyncChildAamMalomat_Activity.class));

                }else{
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
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

        rl_refferal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(data_Referal[0][0]) > 0){
                    startActivity(new Intent(ctx, SyncReferral_Activity.class));

                }else{
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
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

        rl_mother_aam_malomat_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(data_MMaloom[0][0]) > 0){
                    startActivity(new Intent(ctx, SyncMotherAamMalomat_Activity.class));

                }else{
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
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

        rl_mother_beemari_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(data_MBemari[0][0]) > 0){
                    startActivity(new Intent(ctx, SyncMotherBemari_Activity.class));

                }else{
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
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

        rl_mother_khandani_mansobabndi_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(data_MFPlan[0][0]) > 0){
                    startActivity(new Intent(ctx, SyncMotherFamilyPlanning_Activity.class));

                }else{
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
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
                    startActivity(new Intent(ctx, SyncMotherHifazitiTeekey_Activity.class));

                }else{
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
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

        rl_mother_haaml_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(data_MPregnancy[0][0]) > 0){
                    startActivity(new Intent(ctx, SyncMotherPregnancy_Activity.class));

                }else{
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
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

        rl_mother_qabal_Az_pedaish_haaml_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(data_MANC[0][0]) > 0){
                    startActivity(new Intent(ctx, SyncMotherANC_Activity.class));

                }else{
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
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
        rl_mother_zachgi_haaml_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(data_MDelivery[0][0]) > 0){
                    startActivity(new Intent(ctx, SyncMotherDelivery_Activity.class));
                }else{
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
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
        rl_mother_bad_az_pedaish_haaml_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(data_MPNC[0][0]) > 0){
                    startActivity(new Intent(ctx, SyncMotherPNC_Activity.class));

                }else{
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
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
                    startActivity(new Intent(ctx, SyncVideo_Activity.class));

                }else{
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
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
                    startActivity(new Intent(ctx, SyncFeedback_Activity.class));

                }else{
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
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

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(ctx, HomePage_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            txt_khandan_reg.setVisibility(View.GONE);
            txt_reg.setVisibility(View.GONE);
            txt_child_nasho_numa_recrd.setVisibility(View.GONE);
            txt_child_beemari_record.setVisibility(View.GONE);
            txt_child_hifaziti_teekey_record.setVisibility(View.GONE);
            txt_child_aam_malomat_record.setVisibility(View.GONE);
            txt_mother_haaml_record.setVisibility(View.GONE);
            txt_mother_qabal_Az_pedaish_haaml_record.setVisibility(View.GONE);
            txt_mother_zachgi_haaml_record.setVisibility(View.GONE);
            txt_mother_bad_az_pedaish_haaml_record.setVisibility(View.GONE);
            txt_mother_beemari_record.setVisibility(View.GONE);
            txt_mother_khandani_mansobabndi_record.setVisibility(View.GONE);
            txt_mother_hifaziti_teekey_record.setVisibility(View.GONE);
            txt_mother_aam_malomat_record.setVisibility(View.GONE);
            txt_referral_record.setVisibility(View.GONE);
            txt_video_record.setVisibility(View.GONE);
            txt_feedback_record.setVisibility(View.GONE);

            pb_2.setVisibility(View.VISIBLE);
            pb_3.setVisibility(View.VISIBLE);
            pb_4.setVisibility(View.VISIBLE);
            pb_5.setVisibility(View.VISIBLE);
            pb_6.setVisibility(View.VISIBLE);
            pb_7.setVisibility(View.VISIBLE);
            pb_8.setVisibility(View.VISIBLE);
            pb_9.setVisibility(View.VISIBLE);
            pb_10.setVisibility(View.VISIBLE);
            pb_11.setVisibility(View.VISIBLE);
            pb_12.setVisibility(View.VISIBLE);
            pb_13.setVisibility(View.VISIBLE);
            pb_14.setVisibility(View.VISIBLE);
            pb_15.setVisibility(View.VISIBLE);
            pb_16.setVisibility(View.VISIBLE);
            pb_17.setVisibility(View.VISIBLE);
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
            Lister ls = new Lister(ctx);
           ls.createAndOpenDB();
            data_Member = ls.executeReader("Select count(id) from MEMBER where is_synced = '0'");

            pb_2.setVisibility(View.GONE);
            txt_reg.setVisibility(View.VISIBLE);
            txt_reg.setText(data_Member[0][0]);
            Log.d("000555", "Member Count: " + data_Member[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    child_growth();

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
                    child_growth();

                }
            }, 200);


        }
    }

    public void child_growth() {
        try {
            Lister ls = new Lister(ctx);
           ls.createAndOpenDB();
            data_CGrowth = ls.executeReader("Select count(id) from CGROWTH where is_synced = '0'");
            pb_3.setVisibility(View.GONE);

            txt_child_nasho_numa_recrd.setVisibility(View.VISIBLE);
            txt_child_nasho_numa_recrd.setText(data_CGrowth[0][0]);
            Log.d("000555", "CGROWTH Count: " + data_CGrowth[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    child_bemari();

                }
            }, 200);

        } catch (Exception e) {
            pb_3.setVisibility(View.GONE);
            txt_child_nasho_numa_recrd.setVisibility(View.VISIBLE);
            txt_child_nasho_numa_recrd.setText("0");
            Log.d("000555", "CGrowth Err: " + e.getMessage());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    child_bemari();

                }
            }, 200);


        }
    }

    public void child_bemari() {
        try {
            Lister ls = new Lister(ctx);
           ls.createAndOpenDB();
            data_CBemari = ls.executeReader("Select count(id) from CBEMARI where is_synced= '0' ");
            pb_4.setVisibility(View.GONE);
            txt_child_beemari_record.setVisibility(View.VISIBLE);
            txt_child_beemari_record.setText(data_CBemari[0][0]);
            Log.d("000555", "CBEMARI Count: " + data_CBemari[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    child_vaccination();

                }
            }, 200);

        } catch (Exception e) {
            pb_4.setVisibility(View.GONE);
            txt_child_beemari_record.setVisibility(View.VISIBLE);
            txt_child_beemari_record.setText("0");
            Log.d("000555", "CBemari Err: " + e.getMessage());

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
            Lister ls = new Lister(ctx);
           ls.createAndOpenDB();
            data_CVaccination = ls.executeReader("Select count(id) from CVACCINATION where is_synced= '0' ");
            pb_5.setVisibility(View.GONE);
            txt_child_hifaziti_teekey_record.setVisibility(View.VISIBLE);
            txt_child_hifaziti_teekey_record.setText(data_CVaccination[0][0]);
            Log.d("000555", "CVACCINATION Count: " + data_CVaccination[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    child_maloom();

                }
            }, 200);


        } catch (Exception e) {
            pb_5.setVisibility(View.GONE);
            txt_child_hifaziti_teekey_record.setVisibility(View.VISIBLE);
            txt_child_hifaziti_teekey_record.setText("0");
            Log.d("000555", "CVACCINATION Err: " + e.getMessage());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    child_maloom();

                }
            }, 200);

        }
    }

    public void child_maloom() {
        try {
            Lister ls = new Lister(ctx);
           ls.createAndOpenDB();
            data_CMaloom = ls.executeReader("Select count(id) from CMALOOM where is_synced= '0' ");
            pb_6.setVisibility(View.GONE);
            txt_child_aam_malomat_record.setVisibility(View.VISIBLE);
            txt_child_aam_malomat_record.setText(data_CMaloom[0][0]);
            Log.d("000555", "CMALOOM Count: " + data_CMaloom[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mother_pregnancy();

                }
            }, 200);

        } catch (Exception e) {
            pb_6.setVisibility(View.GONE);
            txt_child_aam_malomat_record.setVisibility(View.VISIBLE);
            txt_child_aam_malomat_record.setText("0");
            Log.d("000555", "CMALOOM Err: " + e.getMessage());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mother_pregnancy();

                }
            }, 200);

        }
    }

    public void mother_pregnancy() {
        try {
            Lister ls = new Lister(ctx);
           ls.createAndOpenDB();
            data_MPregnancy= ls.executeReader("Select count(id) from MPREGNANCY where is_synced= '0' ");

            pb_7.setVisibility(View.GONE);
            txt_mother_haaml_record.setVisibility(View.VISIBLE);
            txt_mother_haaml_record.setText(data_MPregnancy[0][0]);
            Log.d("000555", "MPREGNANCY Count: " + data_MPregnancy[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mother_ANC();

                }
            }, 200);


        } catch (Exception e) {
            pb_7.setVisibility(View.GONE);
            txt_mother_haaml_record.setVisibility(View.VISIBLE);
            txt_mother_haaml_record.setText("0");
            Log.d("000555", "MPREGNANCY Err: " + e.getMessage());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mother_ANC();

                }
            }, 200);

        }
    }

    public void mother_ANC() {
        try {
            Lister ls = new Lister(ctx);
           ls.createAndOpenDB();
            data_MANC = ls.executeReader("Select count(id) from MANC where is_synced= '0' ");
            pb_8.setVisibility(View.GONE);
            txt_mother_qabal_Az_pedaish_haaml_record.setVisibility(View.VISIBLE);
            txt_mother_qabal_Az_pedaish_haaml_record.setText(data_MANC[0][0]);
            Log.d("000555", "MANC Count: " + data_MANC[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mother_delivery();

                }
            }, 200);

        } catch (Exception e) {
            pb_8.setVisibility(View.GONE);
            txt_mother_qabal_Az_pedaish_haaml_record.setVisibility(View.VISIBLE);
            txt_mother_qabal_Az_pedaish_haaml_record.setText("0");
            Log.d("000555", "MANC Err: " + e.getMessage());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mother_delivery();

                }
            }, 200);

        }
    }

    public void mother_delivery() {
        try {
            Lister ls = new Lister(ctx);
           ls.createAndOpenDB();
            data_MDelivery= ls.executeReader("Select count(id) from MDELIV where is_synced= '0' ");
            pb_9.setVisibility(View.GONE);
            txt_mother_zachgi_haaml_record.setVisibility(View.VISIBLE);
            txt_mother_zachgi_haaml_record.setText(data_MDelivery[0][0]);
            Log.d("000555", "MDELIV Count: " + data_MDelivery[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mother_PNC();

                }
            }, 200);

        } catch (Exception e) {
            pb_9.setVisibility(View.GONE);
            txt_mother_zachgi_haaml_record.setVisibility(View.VISIBLE);
            txt_mother_zachgi_haaml_record.setText("0");
            Log.d("000555", "MDELIV Err: " + e.getMessage());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mother_PNC();

                }
            }, 200);

        }
    }

    public void mother_PNC() {
        try {
            Lister ls = new Lister(ctx);
           ls.createAndOpenDB();
            data_MPNC = ls.executeReader("Select count(id) from MPNC where is_synced= '0' ");
            pb_10.setVisibility(View.GONE);
            txt_mother_bad_az_pedaish_haaml_record.setVisibility(View.VISIBLE);
            txt_mother_bad_az_pedaish_haaml_record.setText(data_MPNC[0][0]);
            Log.d("000555", "MPNC Count: " + data_MPNC[0][0]);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mother_vaccination();

                }
            }, 200);

        } catch (Exception e) {
            pb_10.setVisibility(View.GONE);
            txt_mother_bad_az_pedaish_haaml_record.setVisibility(View.VISIBLE);
            txt_mother_bad_az_pedaish_haaml_record.setText("0");
            Log.d("000555", "MPNC Err: " + e.getMessage());

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
            Log.d("000555", "MVACINE Count: " + data_MVaccine[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mother_bemari();

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
                    mother_bemari();

                }
            }, 200);

        }
    }

    public void mother_bemari() {
        try {
            Lister ls = new Lister(ctx);
           ls.createAndOpenDB();
            data_MBemari = ls.executeReader("Select count(id) from MBEMARI where is_synced= '0' ");
            pb_12.setVisibility(View.GONE);
            txt_mother_beemari_record.setVisibility(View.VISIBLE);
            txt_mother_beemari_record.setText(data_MBemari[0][0]);
            Log.d("000555", "MBEMARI Count: " + data_MBemari[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mother_maloom();

                }
            }, 200);

        } catch (Exception e) {
            pb_12.setVisibility(View.GONE);
            txt_mother_beemari_record.setVisibility(View.VISIBLE);
            txt_mother_beemari_record.setText("0");
            Log.d("000555", "MBEMARI Err: " + e.getMessage());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mother_maloom();

                }
            }, 200);


        }
    }

    public void mother_maloom() {
        try {
            Lister ls = new Lister(ctx);
           ls.createAndOpenDB();
            data_MMaloom = ls.executeReader("Select count(id) from MMALOOM where is_synced= '0' ");
            pb_13.setVisibility(View.GONE);
            txt_mother_aam_malomat_record.setVisibility(View.VISIBLE);
            txt_mother_aam_malomat_record.setText(data_MMaloom[0][0]);
            Log.d("000555", "MMALOOM Count: " + data_MMaloom[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mother_khandani_mansobabandi();

                }
            }, 200);


        } catch (Exception e) {
            pb_13.setVisibility(View.GONE);
            txt_mother_aam_malomat_record.setVisibility(View.VISIBLE);
            txt_mother_aam_malomat_record.setText("0");
            Log.d("000555", "MMALOOM Err: " + e.getMessage());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mother_khandani_mansobabandi();

                }
            }, 200);

        }
    }

    public void mother_khandani_mansobabandi() {
        try {
            Lister ls = new Lister(ctx);
           ls.createAndOpenDB();
            data_MFPlan = ls.executeReader("Select count(id) from MFPLAN where is_synced= '0' ");
            pb_14.setVisibility(View.GONE);
            txt_mother_khandani_mansobabndi_record.setVisibility(View.VISIBLE);
            txt_mother_khandani_mansobabndi_record.setText(data_MFPlan[0][0]);
            Log.d("000555", "MFPLAN Count: " + data_MFPlan[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    child_and_mother_referral();

                }
            }, 200);

        } catch (Exception e) {
            pb_14.setVisibility(View.GONE);
            txt_mother_khandani_mansobabndi_record.setVisibility(View.VISIBLE);
            txt_mother_khandani_mansobabndi_record.setText("0");
            Log.d("000555", "MFPLAN Err: " + e.getMessage());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    child_and_mother_referral();

                }
            }, 200);

        }
    }

    public void child_and_mother_referral() {
        try {
            Lister ls = new Lister(ctx);
           ls.createAndOpenDB();
            data_Referal = ls.executeReader("Select count(id) from REFERAL where is_synced= '0' ");
            pb_15.setVisibility(View.GONE);
            txt_referral_record.setVisibility(View.VISIBLE);
            txt_referral_record.setText(data_Referal[0][0]);
            Log.d("000555", "REFERAL Count: " + data_Referal[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    video_record();

                }
            }, 200);


        } catch (Exception e) {
            pb_15.setVisibility(View.GONE);
            txt_referral_record.setVisibility(View.VISIBLE);
            txt_referral_record.setText("0");
            Log.d("000555", "REFERAL Err: " + e.getMessage());

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
            pb_16.setVisibility(View.GONE);
            txt_video_record.setVisibility(View.VISIBLE);
            txt_video_record.setText(data_Video[0][0]);
            Log.d("000555", "VIDEOS Count: " + data_Video[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    feedback_record();

                }
            }, 200);


        } catch (Exception e) {
            pb_16.setVisibility(View.GONE);
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
            pb_17.setVisibility(View.GONE);
            txt_feedback_record.setVisibility(View.VISIBLE);
            txt_feedback_record.setText(data_Feedback[0][0]);
            Log.d("000555", "FEEDBACK Count: " + data_Feedback[0][0]);

        } catch (Exception e) {
            pb_17.setVisibility(View.GONE);
            txt_feedback_record.setVisibility(View.VISIBLE);
            txt_feedback_record.setText("0");
            Log.d("000555", "Feedback Err: " + e.getMessage());

        }
    }

    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();
    }
}
   /* @Override
    protected void onResume() {
        super.onResume();

        try {

            Log.d("000555", "ONCEEE");

            alertDialog = new Dialog(ctx);
            LayoutInflater layout = LayoutInflater.from(ctx);
            final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

            alertDialog.setContentView(dialogView);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.show();

            Khanndan_registration();

           Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        pb_2.setVisibility(View.VISIBLE);
                        member_registration();
                    } catch (Exception e) {
                        Log.d("000555", "Err: " + e.getMessage());
                    }
                }
            }, 1000);


            Handler handlerl = new Handler();
            handlerl.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {

                        child_growth();
                    } catch (Exception e) {
                        Log.d("000555", "Err: " + e.getMessage());
                    }
                }
            }, 1000);


            Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        child_bemari();
                    } catch (Exception e) {
                        Log.d("000555", "Err: " + e.getMessage());
                    }
                }
            }, 500);


            Handler handler3 = new Handler();
            handler3.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {

                        child_vaccination();
                    } catch (Exception e) {
                        Log.d("000555", "Err: " + e.getMessage());
                    }
                }
            }, 500);


            Handler handler4 = new Handler();
            handler4.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        child_maloom();
                    } catch (Exception e) {
                        Log.d("000555", "Err: " + e.getMessage());
                    }
                }
            }, 500);


            Handler handler5 = new Handler();
            handler5.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        mother_pregnancy();
                    } catch (Exception e) {
                        Log.d("000555", "Err: " + e.getMessage());
                    }
                }
            }, 500);


            Handler handler6 = new Handler();
            handler6.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {

                        mother_ANC();
                    } catch (Exception e) {
                        Log.d("000555", "Err: " + e.getMessage());
                    }
                }
            }, 500);


            Handler handler7 = new Handler();
            handler7.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        mother_delivery();
                    } catch (Exception e) {
                        Log.d("000555", "Err: " + e.getMessage());
                    }
                }
            }, 500);


            Handler handler8 = new Handler();
            handler8.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        mother_PNC();
                    } catch (Exception e) {
                        Log.d("000555", "Err: " + e.getMessage());
                    }
                }
            }, 500);


            Handler handler9 = new Handler();
            handler9.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        mother_khandani_mansobabandi();
                    } catch (Exception e) {
                        Log.d("000555", "Err: " + e.getMessage());
                    }
                }
            }, 500);

            Handler handlerl0 = new Handler();
            handlerl0.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        mother_bemari();
                    } catch (Exception e) {
                        Log.d("000555", "Err: " + e.getMessage());
                    }
                }
            }, 500);


            Handler handlerl1 = new Handler();
            handlerl1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        mother_vaccination();
                    } catch (Exception e) {
                        Log.d("000555", "Err: " + e.getMessage());
                    }
                }
            }, 500);

            Handler handlerl2 = new Handler();
            handlerl2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        mother_maloom();
                    } catch (Exception e) {
                        Log.d("000555", "Err: " + e.getMessage());
                    }
                }
            }, 500);

            Handler handlerl3 = new Handler();
            handlerl3.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {

                        child_and_mother_referral();
                        alertDialog.dismiss();


                    } catch (Exception e) {
                        Log.d("000555", "Err: " + e.getMessage());
                    }
                }
            }, 500);

        } catch (Exception e) {
            Log.d("000555", "Catach Err: " + e.getMessage());
        }
    }
    public void member_registration() {

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String[][] data = ls.executeReader("Select count(id) from MEMBER where is_synced = '0'");

            pb_2.setVisibility(View.GONE);
            txt_reg.setVisibility(View.VISIBLE);
            txt_reg.setText(data[0][0]);
            Log.d("000555", "TRY: " + data[0][0]);
            pb_3.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            pb_3.setVisibility(View.VISIBLE);
            pb_2.setVisibility(View.GONE);
            txt_reg.setVisibility(View.VISIBLE);
            txt_reg.setText("0");
            Log.d("000555", "Reg Err: " + e.getMessage());

        }
    }

    public void child_growth() {
        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String[][] data = ls.executeReader("Select count(id) from CGROWTH where is_synced = '0'");
            pb_3.setVisibility(View.GONE);
            txt_child_nasho_numa_recrd.setVisibility(View.VISIBLE);
            txt_child_nasho_numa_recrd.setText(data[0][0]);
            Log.d("000555", "TRY: " + data[0][0]);
        } catch (Exception e) {
            pb_3.setVisibility(View.GONE);
            txt_child_nasho_numa_recrd.setVisibility(View.VISIBLE);
            txt_child_nasho_numa_recrd.setText("0");
            Log.d("000555", "CGrowth Err: " + e.getMessage());
        }
    }

    public void child_bemari() {
        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String[][] data = ls.executeReader("Select count(id) from CBEMARI where is_synced= '0' ");
            pb_4.setVisibility(View.GONE);
            txt_child_beemari_record.setVisibility(View.VISIBLE);
            txt_child_beemari_record.setText(data[0][0]);
            Log.d("000555", "TRY: " + data[0][0]);
        } catch (Exception e) {
            pb_4.setVisibility(View.GONE);
            txt_child_beemari_record.setVisibility(View.VISIBLE);
            txt_child_beemari_record.setText("0");
            Log.d("000555", "CBemari Err: " + e.getMessage());
        }
    }

    public void child_vaccination() {
        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String[][] data = ls.executeReader("Select count(id) from CVACCINATION where is_synced= '0' ");
            pb_5.setVisibility(View.GONE);
            txt_child_hifaziti_teekey_record.setVisibility(View.VISIBLE);
            txt_child_hifaziti_teekey_record.setText(data[0][0]);
            Log.d("000555", "TRY: " + data[0][0]);
        } catch (Exception e) {
            pb_5.setVisibility(View.GONE);
            txt_child_hifaziti_teekey_record.setVisibility(View.VISIBLE);
            txt_child_hifaziti_teekey_record.setText("0");
            Log.d("000555", "CVACCINATION Err: " + e.getMessage());
        }
    }

    public void child_maloom() {
        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String[][] data = ls.executeReader("Select count(id) from CMALOOM where is_synced= '0' ");
            pb_6.setVisibility(View.GONE);
            txt_child_aam_malomat_record.setVisibility(View.VISIBLE);
            txt_child_aam_malomat_record.setText(data[0][0]);
            Log.d("000555", "TRY: " + data[0][0]);
        } catch (Exception e) {
            pb_6.setVisibility(View.GONE);
            txt_child_aam_malomat_record.setVisibility(View.VISIBLE);
            txt_child_aam_malomat_record.setText("0");
            Log.d("000555", "CMALOOM Err: " + e.getMessage());
        }
    }

    public void mother_pregnancy() {
        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String[][] data = ls.executeReader("Select count(id) from MPREGNANCY where is_synced= '0' ");

            pb_7.setVisibility(View.GONE);
            txt_mother_haaml_record.setVisibility(View.VISIBLE);
            txt_mother_haaml_record.setText(data[0][0]);
            Log.d("000555", "TRY: " + data[0][0]);
        } catch (Exception e) {
            pb_7.setVisibility(View.GONE);
            txt_mother_haaml_record.setVisibility(View.VISIBLE);
            txt_mother_haaml_record.setText("0");
            Log.d("000555", "MPREGNANCY Err: " + e.getMessage());
        }
    }

    public void mother_ANC() {
        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String[][] data = ls.executeReader("Select count(id) from MANC where is_synced= '0' ");
            pb_8.setVisibility(View.GONE);
            txt_mother_qabal_Az_pedaish_haaml_record.setVisibility(View.VISIBLE);
            txt_mother_qabal_Az_pedaish_haaml_record.setText(data[0][0]);
            Log.d("000555", "TRY: " + data[0][0]);
        } catch (Exception e) {
            pb_8.setVisibility(View.GONE);
            txt_mother_qabal_Az_pedaish_haaml_record.setVisibility(View.VISIBLE);
            txt_mother_qabal_Az_pedaish_haaml_record.setText("0");
            Log.d("000555", "MANC Err: " + e.getMessage());
        }
    }

    public void mother_delivery() {
        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String[][] data = ls.executeReader("Select count(id) from MDELIV where is_synced= '0' ");
            pb_9.setVisibility(View.GONE);
            txt_mother_zachgi_haaml_record.setVisibility(View.VISIBLE);
            txt_mother_zachgi_haaml_record.setText(data[0][0]);
            Log.d("000555", "TRY: " + data[0][0]);
        } catch (Exception e) {
            pb_9.setVisibility(View.GONE);
            txt_mother_zachgi_haaml_record.setVisibility(View.VISIBLE);
            txt_mother_zachgi_haaml_record.setText("0");
            Log.d("000555", "MDELIV Err: " + e.getMessage());
        }
    }

    public void mother_PNC() {
        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String[][] data = ls.executeReader("Select count(id) from MPNC where is_synced= '0' ");
            pb_10.setVisibility(View.GONE);
            txt_mother_bad_az_pedaish_haaml_record.setVisibility(View.VISIBLE);
            txt_mother_bad_az_pedaish_haaml_record.setText(data[0][0]);
        } catch (Exception e) {
            pb_10.setVisibility(View.GONE);
            txt_mother_bad_az_pedaish_haaml_record.setVisibility(View.VISIBLE);
            txt_mother_bad_az_pedaish_haaml_record.setText("0");
            Log.d("000555", "MPNC Err: " + e.getMessage());
        }
    }


    public void mother_vaccination() {
        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String[][] data = ls.executeReader("Select count(id) from MVACINE where is_synced= '0' ");
            pb_11.setVisibility(View.GONE);
            txt_mother_hifaziti_teekey_record.setVisibility(View.VISIBLE);
            txt_mother_hifaziti_teekey_record.setText(data[0][0]);
            Log.d("000555", "TRY: " + data[0][0]);
        } catch (Exception e) {
            pb_11.setVisibility(View.GONE);
            txt_mother_hifaziti_teekey_record.setVisibility(View.VISIBLE);

            txt_mother_hifaziti_teekey_record.setText("0");
            Log.d("000555", "MVACINE Err: " + e.getMessage());
        }
    }

    public void mother_bemari() {
        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String[][] data = ls.executeReader("Select count(id) from MBEMARI where is_synced= '0' ");
            pb_12.setVisibility(View.GONE);
            txt_mother_beemari_record.setVisibility(View.VISIBLE);
            txt_mother_beemari_record.setText(data[0][0]);
            Log.d("000555", "TRY: " + data[0][0]);
        } catch (Exception e) {
            pb_12.setVisibility(View.GONE);
            txt_mother_beemari_record.setVisibility(View.VISIBLE);
            txt_mother_beemari_record.setText("0");
            Log.d("000555", "MBEMARI Err: " + e.getMessage());
        }
    }

    public void mother_maloom() {
        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String[][] data = ls.executeReader("Select count(id) from MMALOOM where is_synced= '0' ");
            pb_13.setVisibility(View.GONE);
            txt_mother_aam_malomat_record.setVisibility(View.VISIBLE);
            txt_mother_aam_malomat_record.setText(data[0][0]);
            Log.d("000555", "TRY: " + data[0][0]);
        } catch (Exception e) {
            pb_13.setVisibility(View.GONE);
            txt_mother_aam_malomat_record.setVisibility(View.VISIBLE);
            txt_mother_aam_malomat_record.setText("0");
            Log.d("000555", "MMALOOM Err: " + e.getMessage());
        }
    }

    public void mother_khandani_mansobabandi() {
        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String[][] data = ls.executeReader("Select count(id) from MFPLAN where is_synced= '0' ");
            pb_14.setVisibility(View.GONE);
            txt_mother_khandani_mansobabndi_record.setVisibility(View.VISIBLE);
            txt_mother_khandani_mansobabndi_record.setText(data[0][0]);
            Log.d("000555", "TRY: " + data[0][0]);
        } catch (Exception e) {
            pb_14.setVisibility(View.GONE);
            txt_mother_khandani_mansobabndi_record.setVisibility(View.VISIBLE);
            txt_mother_khandani_mansobabndi_record.setText("0");
            Log.d("000555", "MFPLAN Err: " + e.getMessage());
        }
    }

    public void child_and_mother_referral() {
        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            String[][] data = ls.executeReader("Select count(id) from REFERAL where is_synced= '0' ");
            pb_15.setVisibility(View.GONE);
            txt_referral_record.setVisibility(View.VISIBLE);
            txt_referral_record.setText(data[0][0]);
            Log.d("000555", "TRY: " + data[0][0]);
        } catch (Exception e) {
            pb_15.setVisibility(View.GONE);
            txt_referral_record.setVisibility(View.VISIBLE);
            txt_referral_record.setText("0");
            Log.d("000555", "REFERAL Err: " + e.getMessage());
        }
    }*/


