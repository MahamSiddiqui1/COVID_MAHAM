package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SyncDataActivities;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;


public class SyncData_Fragment extends Fragment {

    ImageView iv_navigation_drawer, iv_home;
    RelativeLayout rl_khandan_reg, rl_reg, rl_child_nasho_numa_record, rl_child_beemari_record, rl_child_hifaziti_teekey_record, rl_child_aam_malomat_record, rl_mother_hifaziti_teekey_record, rl_mother_beemari_record,
            rl_mother_khandani_mansobabndi_record, rl_mother_aam_malomat_record, rl_mother_haaml_record, rl_mother_qabal_Az_pedaish_haaml_record,
            rl_mother_bad_az_pedaish_haaml_record, rl_mother_zachgi_haaml_record, rl_refferal, rl_video_record,
            rl_feedback_record, rl_social_contact_record, rl_stock_record, rl_delete_member;


    TextView txt_khandan_reg, txt_reg, txt_child_nasho_numa_recrd, txt_child_beemari_record, txt_child_hifaziti_teekey_record, txt_child_aam_malomat_record, txt_mother_hifaziti_teekey_record, txt_mother_beemari_record,
            txt_mother_khandani_mansobabndi_record, txt_mother_aam_malomat_record, txt_mother_haaml_record, txt_mother_qabal_Az_pedaish_haaml_record,
            txt_mother_bad_az_pedaish_haaml_record, txt_referral_record, txt_mother_zachgi_haaml_record, txt_video_record, txt_feedback_record,
            txt_social_contact_record, txt_stock_record, txt_delete_member;

    ProgressBar progressBar, pb_2, pb_3, pb_4, pb_5, pb_6, pb_7, pb_8, pb_9, pb_10, pb_11, pb_12, pb_13, pb_14, pb_15, pb_16, pb_17, pb_18, pb_19, pb_20;
    Dialog alertDialog;
    String[][] data_khandan, data_Member, data_CGrowth, data_CBemari, data_CVaccination, data_CMaloom, data_MPregnancy, data_MANC, data_MDelivery, data_MPNC, data_MVaccine,
            data_MBemari, data_MMaloom, data_MFPlan, data_Referal, data_Video, data_Feedback, data_SocialContact, data_MedicineStock, data_DeleteMember;

    String login_useruid;

    Lister ls;

    public SyncData_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_sync_data, container, false);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(getContext(), SyncItems_Activity.class));

        Log.d("000555", "ONCREATEEE");

        //ImageView``
        iv_navigation_drawer = rootView.findViewById(R.id.iv_navigation_drawer);
        iv_home = rootView.findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);


        //Relative Layout
        rl_khandan_reg = rootView.findViewById(R.id.rl_khandan_reg);
        rl_reg = rootView.findViewById(R.id.rl_reg);
        rl_child_nasho_numa_record = rootView.findViewById(R.id.rl_child_nasho_numa_record);
        rl_child_beemari_record = rootView.findViewById(R.id.rl_child_beemari_record);
        rl_child_hifaziti_teekey_record = rootView.findViewById(R.id.rl_child_hifaziti_teekey_record);
        rl_child_aam_malomat_record = rootView.findViewById(R.id.rl_child_aam_malomat_record);
        rl_mother_hifaziti_teekey_record = rootView.findViewById(R.id.rl_mother_hifaziti_teekey_record);
        rl_mother_beemari_record = rootView.findViewById(R.id.rl_mother_beemari_record);
        rl_mother_khandani_mansobabndi_record = rootView.findViewById(R.id.rl_mother_khandani_mansobabndi_record);
        rl_mother_aam_malomat_record = rootView.findViewById(R.id.rl_mother_aam_malomat_record);
        rl_mother_haaml_record = rootView.findViewById(R.id.rl_mother_haaml_record);
        rl_mother_qabal_Az_pedaish_haaml_record = rootView.findViewById(R.id.rl_mother_qabal_Az_pedaish_haaml_record);
        rl_mother_qabal_Az_pedaish_haaml_record = rootView.findViewById(R.id.rl_mother_qabal_Az_pedaish_haaml_record);
        rl_mother_zachgi_haaml_record = rootView.findViewById(R.id.rl_mother_zachgi_haaml_record);
        rl_mother_bad_az_pedaish_haaml_record = rootView.findViewById(R.id.rl_mother_bad_az_pedaish_haaml_record);
        rl_refferal = rootView.findViewById(R.id.rl_referral_record);
        rl_video_record = rootView.findViewById(R.id.rl_video_record);
        rl_feedback_record = rootView.findViewById(R.id.rl_feedback_record);
        rl_social_contact_record = rootView.findViewById(R.id.rl_social_contact_record);
        rl_stock_record = rootView.findViewById(R.id.rl_stock_record);
        rl_delete_member = rootView.findViewById(R.id.rl_delete_member);

        //TextView
        txt_khandan_reg = rootView.findViewById(R.id.txt_khandan_reg);
        txt_reg = rootView.findViewById(R.id.txt_reg);
        txt_child_nasho_numa_recrd = rootView.findViewById(R.id.txt_child_nasho_numa_recrd);
        txt_child_beemari_record = rootView.findViewById(R.id.txt_child_beemari_record);
        txt_child_hifaziti_teekey_record = rootView.findViewById(R.id.txt_child_hifaziti_teekey_record);
        txt_child_aam_malomat_record = rootView.findViewById(R.id.txt_child_aam_malomat_record);
        txt_mother_hifaziti_teekey_record = rootView.findViewById(R.id.txt_mother_hifaziti_teekey_record);
        txt_mother_beemari_record = rootView.findViewById(R.id.txt_mother_beemari_record);
        txt_mother_khandani_mansobabndi_record = rootView.findViewById(R.id.txt_mother_khandani_mansobabndi_record);
        txt_mother_aam_malomat_record = rootView.findViewById(R.id.txt_mother_aam_malomat_record);
        txt_mother_haaml_record = rootView.findViewById(R.id.txt_mother_haaml_record);
        txt_mother_qabal_Az_pedaish_haaml_record = rootView.findViewById(R.id.txt_mother_qabal_Az_pedaish_haaml_record);
        txt_mother_zachgi_haaml_record = rootView.findViewById(R.id.txt_mother_zachgi_haaml_record);
        txt_mother_bad_az_pedaish_haaml_record = rootView.findViewById(R.id.txt_mother_bad_az_pedaish_haaml_record);
        txt_referral_record = rootView.findViewById(R.id.txt_referral_record);
        txt_video_record = rootView.findViewById(R.id.txt_video_record);
        txt_feedback_record = rootView.findViewById(R.id.txt_feedback_record);
        txt_social_contact_record = rootView.findViewById(R.id.txt_social_contatact_record);
        txt_stock_record = rootView.findViewById(R.id.txt_stock_record);
        txt_delete_member = rootView.findViewById(R.id.txt_delete_member);

        //ProgressBar
        progressBar = rootView.findViewById(R.id.pbProgress);

        pb_2 = rootView.findViewById(R.id.pb_2);
        pb_3 = rootView.findViewById(R.id.pb_3);
        pb_4 = rootView.findViewById(R.id.pb_4);
        pb_5 = rootView.findViewById(R.id.pb_5);
        pb_6 = rootView.findViewById(R.id.pb_6);
        pb_7 = rootView.findViewById(R.id.pb_7);
        pb_8 = rootView.findViewById(R.id.pb_8);
        pb_9 = rootView.findViewById(R.id.pb_9);
        pb_10 = rootView.findViewById(R.id.pb_10);
        pb_11 = rootView.findViewById(R.id.pb_11);
        pb_12 = rootView.findViewById(R.id.pb_12);
        pb_13 = rootView.findViewById(R.id.pb_13);
        pb_14 = rootView.findViewById(R.id.pb_14);
        pb_15 = rootView.findViewById(R.id.pb_15);
        pb_16 = rootView.findViewById(R.id.pb_16);
        pb_17 = rootView.findViewById(R.id.pb_17);
        pb_18 = rootView.findViewById(R.id.pb_18);
        pb_19 = rootView.findViewById(R.id.pb_19);
        pb_20 = rootView.findViewById(R.id.pb_20);


        //Get shared USer name
        try {
            SharedPreferences prefelse = getContext().getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
            String shared_useruid = prefelse.getString(("login_userid"), null); // getting String
            login_useruid = shared_useruid;
            Log.d("000555", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000555", "Shared Err:" + e.getMessage());
        }


        //Read_PhoneNumber();


        rl_khandan_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(data_khandan[0][0]) > 0) {
                    startActivity(new Intent(getContext(), SyncKhandanReg_Activity.class));
                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
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
                if (Integer.valueOf(data_Member[0][0]) > 0) {

                    startActivity(new Intent(getContext(), SyncMemberReg_Activity.class));
                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
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

                if (Integer.valueOf(data_CGrowth[0][0]) > 0) {
                    startActivity(new Intent(getContext(), SyncChildNashoNuma_Activity.class));

                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
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
                if (Integer.valueOf(data_CBemari[0][0]) > 0) {
                    startActivity(new Intent(getContext(), SyncChildBemari_Activity.class));

                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
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
                if (Integer.valueOf(data_CVaccination[0][0]) > 0) {
                    startActivity(new Intent(getContext(), SyncChildHifazitiTeekey_Activity.class));

                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
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
                if (Integer.valueOf(data_CMaloom[0][0]) > 0) {
                    startActivity(new Intent(getContext(), SyncChildAamMalomat_Activity.class));

                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
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
                if (Integer.valueOf(data_Referal[0][0]) > 0) {
                    startActivity(new Intent(getContext(), SyncReferral_Activity.class));

                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
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
                if (Integer.valueOf(data_MMaloom[0][0]) > 0) {
                    startActivity(new Intent(getContext(), SyncMotherAamMalomat_Activity.class));

                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
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
                if (Integer.valueOf(data_MBemari[0][0]) > 0) {
                    startActivity(new Intent(getContext(), SyncMotherBemari_Activity.class));

                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
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
                if (Integer.valueOf(data_MFPlan[0][0]) > 0) {
                    startActivity(new Intent(getContext(), SyncMotherFamilyPlanning_Activity.class));

                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
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
                if (Integer.valueOf(data_MVaccine[0][0]) > 0) {
                    startActivity(new Intent(getContext(), SyncMotherHifazitiTeekey_Activity.class));

                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
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
                if (Integer.valueOf(data_MPregnancy[0][0]) > 0) {
                    startActivity(new Intent(getContext(), SyncMotherPregnancy_Activity.class));

                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
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
                if (Integer.valueOf(data_MANC[0][0]) > 0) {
                    startActivity(new Intent(getContext(), SyncMotherANC_Activity.class));

                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
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
                if (Integer.valueOf(data_MDelivery[0][0]) > 0) {
                    startActivity(new Intent(getContext(), SyncMotherDelivery_Activity.class));
                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
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
                if (Integer.valueOf(data_MPNC[0][0]) > 0) {
                    startActivity(new Intent(getContext(), SyncMotherPNC_Activity.class));

                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
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
                if (Integer.valueOf(data_Video[0][0]) > 0) {
                    startActivity(new Intent(getContext(), SyncVideo_Activity.class));

                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
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
                if (Integer.valueOf(data_Feedback[0][0]) > 0) {
                    startActivity(new Intent(getContext(), SyncFeedback_Activity.class));

                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
                    TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(15);
                    snackbar.setDuration(2000);
                    snackbar.show();
                }
            }
        });

        rl_social_contact_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(data_SocialContact[0][0]) > 0) {
                    startActivity(new Intent(getContext(), SyncSocialContact_Activity.class));

                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
                    TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(15);
                    snackbar.setDuration(2000);
                    snackbar.show();
                }
            }
        });

        rl_stock_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(data_MedicineStock[0][0]) > 0) {
                    startActivity(new Intent(getContext(), SyncMedicineStock_Activity.class));

                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
                    TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(15);
                    snackbar.setDuration(2000);
                    snackbar.show();
                }
            }
        });


        rl_delete_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(data_DeleteMember[0][0]) > 0) {
                    //startActivity(new Intent(getContext(), SyncDeleteMember_Activity.class));
                    Toast.makeText(getContext(), R.string.workingUnderProgress, Toast.LENGTH_SHORT).show();
                } else {
                    final Snackbar snackbar = Snackbar.make(v, R.string.noData4sync, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
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
                Intent newIntent = new Intent(getContext(), HomePage_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
            }
        });

        return rootView;
    }

    private void Read_PhoneNumber() {

        try {

            /*Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/

            String[][] mData = ls.executeReader("Select phone_number from SMS_NUMBER where user_uid = '" + login_useruid + "'");

            if (mData != null) {
                Log.d("000369", "PHONE NUMBER: " + mData[0][0]);

                ////////////Save user name in shared pref
                SharedPreferences prefw = getContext().getSharedPreferences((getString(R.string.duplicateEntry)), 0); // 0 - for private mode
                SharedPreferences.Editor editorw = prefw.edit();
                editorw.putString((getString(R.string.duplicateEntry)), mData[0][0]);
                editorw.apply();

            } else {
                Log.d("000369", "NO PHONE NUMBER");
                /*Toast tt = Toast.makeText(getContext(), "برائے مہربانی پہلے فون نمبر منتخب کریں.", Toast.LENGTH_LONG);
                tt.setGravity(Gravity.CENTER, 0, 0);
                tt.show();*/

              /*  final Dialog phonenumber_dialog = new Dialog(getContext());
                LayoutInflater layout = LayoutInflater.from(getContext());
                final View dialogView = layout.inflate(R.layout.dialog_set_phonenumber_error, null);

                phonenumber_dialog.setContentView(dialogView);
                phonenumber_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
                phonenumber_dialog.setCanceledOnTouchOutside(true);
                phonenumber_dialog.setCancelable(true);
                phonenumber_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                phonenumber_dialog.show();


               ImageView iv_close_dilaog = dialogView.findViewById(R.id.iv_close_dilaog);

                iv_close_dilaog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        phonenumber_dialog.dismiss();
                    }
                });
*/
            }

        } catch (Exception e) {
            Log.d("000666", "Err: " + e.getMessage());
            Toast.makeText(getContext(), R.string.somethingWrong, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();


         ls = new Lister(getContext());
        ls.createAndOpenDB();

        Read_PhoneNumber();

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
            txt_social_contact_record.setVisibility(View.GONE);
            txt_stock_record.setVisibility(View.GONE);
            txt_delete_member.setVisibility(View.GONE);

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
            pb_18.setVisibility(View.VISIBLE);
            pb_19.setVisibility(View.VISIBLE);
            pb_20.setVisibility(View.VISIBLE);
            Log.d("000555", "On Resume");

            Khanndan_registration();

        } catch (Exception e) {
            Log.d("000555", "Catach Err: " + e.getMessage());
        }
    }

    public void Khanndan_registration() {

        try {
            /*Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            //data_khandan = ls.executeReader("Select count(id) from KHANDAN where is_synced IN ('0','0,3','1,0')");
            data_khandan = ls.executeReader("Select count(id) from KHANDAN where is_synced IN ('0','0,3')");

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
           /* Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            //data_Member = ls.executeReader("Select count(id) from MEMBER where is_synced IN ('0','0,3','1,0')");
            data_Member = ls.executeReader("Select count(id) from MEMBER where is_synced IN ('0','0,3')");

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
           /* Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
           // data_CGrowth = ls.executeReader("Select count(id) from CGROWTH where is_synced IN ('0','0,3','1,0')");
            data_CGrowth = ls.executeReader("Select count(id) from CGROWTH where is_synced IN ('0','0,3')");
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
           /* Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            //data_CBemari = ls.executeReader("Select count(id) from CBEMARI where is_synced IN ('0','0,3','1,0') ");
            data_CBemari = ls.executeReader("Select count(id) from CBEMARI where is_synced IN ('0','0,3')");
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
            /*Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            //data_CVaccination = ls.executeReader("Select count(id) from CVACCINATION where is_synced IN ('0','0,3','1,0') ");
            data_CVaccination = ls.executeReader("Select count(id) from CVACCINATION where is_synced IN ('0','0,3')");
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
            /*Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            //data_CMaloom = ls.executeReader("Select count(id) from CMALOOM where is_synced IN ('0','0,3','1,0') ");
            data_CMaloom = ls.executeReader("Select count(id) from CMALOOM where is_synced IN ('0','0,3')");
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
            /*Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            //data_MPregnancy = ls.executeReader("Select count(id) from MPREGNANCY where is_synced IN ('0','0,3','1,0') ");
            data_MPregnancy = ls.executeReader("Select count(id) from MPREGNANCY where is_synced IN ('0','0,3')");

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
            /*Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            //data_MANC = ls.executeReader("Select count(id) from MANC where is_synced IN ('0','0,3','1,0') ");
            data_MANC = ls.executeReader("Select count(id) from MANC where is_synced IN ('0','0,3')");
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
           /* Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
           //data_MDelivery = ls.executeReader("Select count(id) from MDELIV where is_synced IN ('0','0,3','1,0') ");
            data_MDelivery = ls.executeReader("Select count(id) from MDELIV where is_synced IN ('0','0,3')");
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
           /* Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            //data_MPNC = ls.executeReader("Select count(id) from MPNC where is_synced IN ('0','0,3','1,0') ");
            data_MPNC = ls.executeReader("Select count(id) from MPNC where is_synced IN ('0','0,3')");
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
            /*Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/

            //data_MVaccine = ls.executeReader("Select count(id) from MVACINE where is_synced IN ('0','0,3','1,0') ");
            data_MVaccine = ls.executeReader("Select count(id) from MVACINE where is_synced IN ('0','0,3')");
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
            /*Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            //data_MBemari = ls.executeReader("Select count(id) from MBEMARI where is_synced IN ('0','0,3','1,0') ");
            data_MBemari = ls.executeReader("Select count(id) from MBEMARI where is_synced IN ('0','0,3')");
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
           /* Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            //data_MMaloom = ls.executeReader("Select count(id) from MMALOOM where is_synced IN ('0','0,3','1,0') ");
            data_MMaloom = ls.executeReader("Select count(id) from MMALOOM where is_synced IN ('0','0,3')");
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
            /*Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            //data_MFPlan = ls.executeReader("Select count(id) from MFPLAN where is_synced IN ('0','0,3','1,0') ");
            data_MFPlan = ls.executeReader("Select count(id) from MFPLAN where is_synced IN ('0','0,3')");
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
          /*  Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            //data_Referal = ls.executeReader("Select count(id) from REFERAL where is_synced IN ('0','0,3','1,0') ");
            data_Referal = ls.executeReader("Select count(id) from REFERAL where is_synced IN ('0','0,3')");
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
            /*Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            //data_Video = ls.executeReader("Select count(id) from VIDEOS where is_synced IN ('0','0,3','1,0') ");
            data_Video = ls.executeReader("Select count(id) from VIDEOS where is_synced IN ('0','0,3')");
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
           /* Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            //data_Feedback = ls.executeReader("Select count(*) from FEEDBACK where is_synced IN ('0','0,3','1,0') ");
            data_Feedback = ls.executeReader("Select count(*) from FEEDBACK where is_synced IN ('0','0,3')");
            pb_17.setVisibility(View.GONE);
            txt_feedback_record.setVisibility(View.VISIBLE);
            txt_feedback_record.setText(data_Feedback[0][0]);
            Log.d("000555", "FEEDBACK Count: " + data_Feedback[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SocialContact_record();
                }
            }, 200);


        } catch (Exception e) {
            pb_17.setVisibility(View.GONE);
            txt_feedback_record.setVisibility(View.VISIBLE);
            txt_feedback_record.setText("0");
            Log.d("000555", "Feedback Err: " + e.getMessage());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SocialContact_record();
                }
            }, 200);

        }
    }

    public void SocialContact_record() {
        try {
            /*Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            //data_SocialContact = ls.executeReader("Select count(*) from MEETING where is_synced IN ('0','0,3','1,0') ");
            data_SocialContact = ls.executeReader("Select count(*) from MEETING where is_synced IN ('0','0,3')");
            pb_18.setVisibility(View.GONE);
            txt_social_contact_record.setVisibility(View.VISIBLE);
            txt_social_contact_record.setText(data_SocialContact[0][0]);
            Log.d("000555", "SocialContact Count: " + data_SocialContact[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MedicineStock_record();
                }
            }, 200);

        } catch (Exception e) {
            pb_18.setVisibility(View.GONE);
            txt_social_contact_record.setVisibility(View.VISIBLE);
            txt_social_contact_record.setText("0");
            Log.d("000555", "Feedback Err: " + e.getMessage());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MedicineStock_record();
                }
            }, 200);

        }
    }

    public void MedicineStock_record() {
        try {
            /*Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            //data_MedicineStock = ls.executeReader("Select count(*) from MEDICINE_STOCK where is_synced IN ('0','0,3','1,0') ");
            data_MedicineStock = ls.executeReader("Select count(*) from MEDICINE_STOCK where is_synced IN ('0','0,3')");
            pb_19.setVisibility(View.GONE);
            txt_stock_record.setVisibility(View.VISIBLE);
            txt_stock_record.setText(data_MedicineStock[0][0]);
            Log.d("000555", "MedicineStock Count: " + data_MedicineStock[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DeleteMember_record();
                }
            }, 200);


        } catch (Exception e) {
            pb_19.setVisibility(View.GONE);
            txt_stock_record.setVisibility(View.VISIBLE);
            txt_stock_record.setText("0");
            Log.d("000555", "MedicineStock Err: " + e.getMessage());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DeleteMember_record();
                }
            }, 200);
        }
    }

    public void DeleteMember_record() {
        try {
            /*Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
           // data_DeleteMember = ls.executeReader("Select count(*) from DELETE_MEMBER where is_synced IN ('0','0,3','1,0')");
            data_DeleteMember = ls.executeReader("Select count(*) from DELETE_MEMBER where is_synced IN ('0','0,3')");
            pb_20.setVisibility(View.GONE);
            txt_delete_member.setVisibility(View.VISIBLE);
            txt_delete_member.setText(data_DeleteMember[0][0]);
            Log.d("000555", "DeleteMember Count: " + data_DeleteMember[0][0]);


        } catch (Exception e) {
            pb_20.setVisibility(View.GONE);
            txt_delete_member.setVisibility(View.VISIBLE);
            txt_delete_member.setText("0");
            Log.d("000555", "DeleteMember Count: " + e.getMessage());


        }
    }

}