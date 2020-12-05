package com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities.VAC_SyncDataActivities;


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
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;


import com.akdndhrc.covid_module.R;


public class VAC_SyncData_Fragment extends Fragment {

    ImageView iv_navigation_drawer, iv_home;
    RelativeLayout rl_khandan_reg, rl_reg, rl_child_hifaziti_teekey_record, rl_feedback_record, rl_video_record, rl_mother_hifaziti_teekey_record, rl_merged_member, rl_vaccine_stock;

    TextView txt_khandan_reg, txt_reg, txt_child_hifaziti_teekey_record, txt_video_record, txt_feedback_record, txt_mother_hifaziti_teekey_record, txt_merged_member, txt_vaccine_stock;

    ProgressBar pb_2, pb_3, pb_4, pb_5, pb_11, pb_6, pb_7;
    Dialog alertDialog;

    String[][] data_khandan, data_Member, data_CVaccination, data_MVaccine, data_Video, data_Feedback, data_MergeredMember, data_VaccineStock;

    String login_useruid;
    Lister ls;

    public VAC_SyncData_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_sync_data_vac, container, false);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(getContext(), VAC_SyncData_Fragment.class));

        Log.d("000200", "ONCREATEEE");

        //ImageView
        iv_navigation_drawer = rootView.findViewById(R.id.iv_navigation_drawer);
        iv_home = rootView.findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);

        //Relative Layout
        rl_khandan_reg = rootView.findViewById(R.id.rl_khandan_reg);
        rl_reg = rootView.findViewById(R.id.rl_reg);
        rl_child_hifaziti_teekey_record = rootView.findViewById(R.id.rl_child_hifaziti_teekey_record);
        rl_video_record = rootView.findViewById(R.id.rl_video_record);
        rl_feedback_record = rootView.findViewById(R.id.rl_feedback_record);
        rl_mother_hifaziti_teekey_record = rootView.findViewById(R.id.rl_mother_hifaziti_teekey_record);
        rl_merged_member = rootView.findViewById(R.id.rl_merged_member);
        rl_vaccine_stock = rootView.findViewById(R.id.rl_vaccine_stock);

        //TextView
        txt_khandan_reg = rootView.findViewById(R.id.txt_khandan_reg);
        txt_reg = rootView.findViewById(R.id.txt_reg);
        txt_child_hifaziti_teekey_record = rootView.findViewById(R.id.txt_child_hifaziti_teekey_record);
        txt_video_record = rootView.findViewById(R.id.txt_video_record);
        txt_feedback_record = rootView.findViewById(R.id.txt_feedback_record);
        txt_mother_hifaziti_teekey_record = rootView.findViewById(R.id.txt_mother_hifaziti_teekey_record);
        txt_merged_member = rootView.findViewById(R.id.txt_merged_member);
        txt_vaccine_stock = rootView.findViewById(R.id.txt_vaccine_stock);

        //ProgressBar

        pb_2 = rootView.findViewById(R.id.pb_2);
        pb_3 = rootView.findViewById(R.id.pb_3);
        pb_4 = rootView.findViewById(R.id.pb_4);
        pb_5 = rootView.findViewById(R.id.pb_5);
        pb_11 = rootView.findViewById(R.id.pb_11);
        pb_6 = rootView.findViewById(R.id.pb_6);
        pb_7 = rootView.findViewById(R.id.pb_7);


        //Get shared USer name
        try {
            SharedPreferences prefelse = getContext().getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
            String shared_useruid = prefelse.getString((R.string.loginUserIDEng), null); // getting String
            login_useruid = shared_useruid;
            Log.d("000200", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000200", "Shared Err:" + e.getMessage());
        }




        rl_khandan_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(data_khandan[0][0]) > 0) {
                    startActivity(new Intent(getContext(), VAC_SyncKhandanReg_Activity.class));

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

                    startActivity(new Intent(getContext(), VAC_SyncMemberReg_Activity.class));
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
                    startActivity(new Intent(getContext(), VAC_SyncChildHifazitiTeekey_Activity.class));

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

                    startActivity(new Intent(getContext(), VAC_SyncVideo_Activity.class));
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
                    startActivity(new Intent(getContext(), VAC_SyncFeedback_Activity.class));

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
                    startActivity(new Intent(getContext(), VAC_SyncMotherHifazitiTeekey_Activity.class));

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

        rl_merged_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(data_MergeredMember[0][0]) > 0) {
                    startActivity(new Intent(getContext(), VAC_SyncMergedMember_Activity.class));

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

        rl_vaccine_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(data_VaccineStock[0][0]) > 0) {
                    Toast.makeText(getContext(), "Working Inprogress.", Toast.LENGTH_SHORT).show();
                    // startActivity(new Intent(getContext(), VAC_SyncVaccineStock_Activity.class));

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


        return rootView;

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
            txt_child_hifaziti_teekey_record.setVisibility(View.GONE);
            txt_mother_hifaziti_teekey_record.setVisibility(View.GONE);
            txt_video_record.setVisibility(View.GONE);
            txt_feedback_record.setVisibility(View.GONE);
            txt_merged_member.setVisibility(View.GONE);
            txt_vaccine_stock.setVisibility(View.GONE);

            pb_2.setVisibility(View.VISIBLE);
            pb_3.setVisibility(View.VISIBLE);
            pb_4.setVisibility(View.VISIBLE);
            pb_5.setVisibility(View.VISIBLE);
            pb_11.setVisibility(View.VISIBLE);
            pb_6.setVisibility(View.VISIBLE);
            pb_7.setVisibility(View.VISIBLE);

            Log.d("000200", "On Resume");

            Khanndan_registration();

        } catch (Exception e) {
            Log.d("000200", "Catach Err: " + e.getMessage());
        }
    }

    public void Khanndan_registration() {

        try {
          /*  Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
           // data_khandan = ls.executeReader("Select count(id) from KHANDAN where is_synced = '0'");
            data_khandan = ls.executeReader("Select count(id) from KHANDAN where is_synced IN ('0','0,3')");

            txt_khandan_reg.setVisibility(View.VISIBLE);
            txt_khandan_reg.setText(data_khandan[0][0]);
            Log.d("000200", "TRY: " + data_khandan[0][0]);

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
            Log.d("000200", "Khandan Err: " + e.getMessage());

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
          /*  Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            data_Member = ls.executeReader("Select count(id) from MEMBER where is_synced IN ('0','0,3')");


            pb_2.setVisibility(View.GONE);
            txt_reg.setVisibility(View.VISIBLE);
            txt_reg.setText(data_Member[0][0]);
            Log.d("000200", "TRY: " + data_Member[0][0]);

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
            Log.d("000200", "Reg Err: " + e.getMessage());

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
           /* Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            data_CVaccination = ls.executeReader("Select count(id) from CVACCINATION where is_synced IN ('0','0,3')");
            pb_3.setVisibility(View.GONE);
            txt_child_hifaziti_teekey_record.setVisibility(View.VISIBLE);
            txt_child_hifaziti_teekey_record.setText(data_CVaccination[0][0]);
            Log.d("000200", "TRY: " + data_CVaccination[0][0]);

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
            Log.d("000200", "CVACCINATION Err: " + e.getMessage());

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
           /* Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            data_MVaccine = ls.executeReader("Select count(id) from MVACINE where is_synced IN ('0','0,3')");
            pb_11.setVisibility(View.GONE);
            txt_mother_hifaziti_teekey_record.setVisibility(View.VISIBLE);
            txt_mother_hifaziti_teekey_record.setText(data_MVaccine[0][0]);
            Log.d("000200", "TRY: " + data_MVaccine[0][0]);

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
            Log.d("000200", "MVACINE Err: " + e.getMessage());

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
          /*  Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            data_Video = ls.executeReader("Select count(id) from VIDEOS where is_synced IN ('0','0,3')");
            pb_4.setVisibility(View.GONE);
            txt_video_record.setVisibility(View.VISIBLE);
            txt_video_record.setText(data_Video[0][0]);
            Log.d("000200", "TRY: " + data_Video[0][0]);

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
            Log.d("000200", "Video Err: " + e.getMessage());

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
            data_Feedback = ls.executeReader("Select count(*) from FEEDBACK where is_synced IN ('0','0,3')");
            pb_5.setVisibility(View.GONE);
            txt_feedback_record.setVisibility(View.VISIBLE);
            txt_feedback_record.setText(data_Feedback[0][0]);
            Log.d("000200", "TRY: " + data_Feedback[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    member_merged();

                }
            }, 200);


        } catch (Exception e) {
            pb_5.setVisibility(View.GONE);
            txt_feedback_record.setVisibility(View.VISIBLE);
            txt_feedback_record.setText("0");
            Log.d("000200", "Feedbac Err: " + e.getMessage());


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    member_merged();

                }
            }, 200);


        }
    }

    public void member_merged() {
        try {
            /*Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            data_MergeredMember = ls.executeReader("Select count(*) from MEMBER_MERGERD where is_synced IN ('0','0,3')");
            pb_6.setVisibility(View.GONE);
            txt_merged_member.setVisibility(View.VISIBLE);
            txt_merged_member.setText(data_MergeredMember[0][0]);
            Log.d("000200", "TRY: " + data_MergeredMember[0][0]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    vaccine_stock();

                }
            }, 200);


        } catch (Exception e) {
            pb_6.setVisibility(View.GONE);
            txt_merged_member.setVisibility(View.VISIBLE);
            txt_merged_member.setText("0");
            Log.d("000200", "Member Merged Err: " + e.getMessage());

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    vaccine_stock();

                }
            }, 200);

        }
    }

    public void vaccine_stock() {
        try {
          /*  Lister ls = new Lister(getContext());
            ls.createAndOpenDB();*/
            data_VaccineStock = ls.executeReader("Select count(id) from VACCINE_STOCK where is_synced IN ('0','0,3')");
            pb_7.setVisibility(View.GONE);
            txt_vaccine_stock.setVisibility(View.VISIBLE);
            txt_vaccine_stock.setText(data_VaccineStock[0][0]);
            Log.d("000200", "TRY: " + data_VaccineStock[0][0]);


        } catch (Exception e) {
            pb_7.setVisibility(View.GONE);
            txt_vaccine_stock.setVisibility(View.VISIBLE);
            txt_vaccine_stock.setText("0");
            Log.d("000200", "VaccineStock Err: " + e.getMessage());

        }
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

               /* final Dialog phonenumber_dialog = new Dialog(getContext());
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

}