package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SearchActivities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.R;

import static com.akdndhrc.covid_module.R.string.correctCNICprompt;
import static com.akdndhrc.covid_module.R.string.correctFamilyNumPrompt;
import static com.akdndhrc.covid_module.R.string.enterMobilePrompt;
import static com.akdndhrc.covid_module.R.string.navigation;

public class  Search_Activity extends AppCompatActivity {

    Context ctx = Search_Activity.this;
    ImageView iv_idcard, iv_smartphone, iv_close, iv_khandannumber,iv_qrcode;
    EditText et_idcard_number, et_phone_number, et_khandan_number;
    Button btn_jaari_rhy, btn_finger_print_search;
    ImageView iv_navigation_drawer, iv_home;
    String[][] mData, mDatanew;
    String Khandan_number;
    public Dialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //ImageView
        iv_idcard = findViewById(R.id.iv_idcard);
        iv_smartphone = findViewById(R.id.iv_smartphone);
        iv_khandannumber = findViewById(R.id.iv_khandannumber);

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_qrcode = findViewById(R.id.iv_qrcode);
     //  iv_home.setVisibility(View.GONE);
        iv_navigation_drawer.setVisibility(View.GONE);

        btn_finger_print_search = findViewById(R.id.btn_finger_print_search);

        iv_idcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_SearchWithIDCard();
            }
        });
        iv_khandannumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_SearchWithKhandanNumber();
                // Toast.makeText(ctx, "Data field unavailable.", Toast.LENGTH_SHORT).show();

            }
        });
        iv_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ctx,Search_QRCode_Activity.class));
            }
        });
        /*btn_finger_print_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Search_WithFingerprintAndQrCode_Activity.class));
            }
        });*/

        iv_smartphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_SearchWithPhoneNumber();
            }
        });


        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, navigation, Toast.LENGTH_SHORT).show();
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

    public void Dialog_SearchWithIDCard() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.dialog_search_with_idcard_layout, null);

        final AlertDialog alertD = new AlertDialog.Builder(this).create();

        et_idcard_number = (EditText) promptView.findViewById(R.id.et_idcard_number);
        iv_close = (ImageView) promptView.findViewById(R.id.iv_close);
        btn_jaari_rhy = (Button) promptView.findViewById(R.id.btn_jaari_rhy);

        et_idcard_number.addTextChangedListener(new TextWatcher() {
            int prev = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prev = et_idcard_number.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if ((prev < length) && (length == 5 || length == 13)) {
                    String data = et_idcard_number.getText().toString();
                    et_idcard_number.setText(data + "-");
                    et_idcard_number.setSelection(length + 1);
                }
            }
        });

        alertD.setView(promptView);
        alertD.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        alertD.setCanceledOnTouchOutside(false);
        alertD.setCancelable(false);
        alertD.show();



        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
            }
        });

        btn_jaari_rhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_idcard_number.getText().toString().isEmpty()) {
                    Toast.makeText(ctx, R.string.enterCNICprompt, Toast.LENGTH_SHORT).show();
                    return;
                }


                alertD.dismiss();

                alertDialog = new Dialog(ctx);
                LayoutInflater layout = LayoutInflater.from(ctx);
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

                        Log.d("Search_code", "work");
                        try {
                            Lister ls = new Lister(Search_Activity.this);
                           ls.createAndOpenDB();
                            //mData = ls.executeReader("Select data from MEMBER where khandan_id = '" + et_khandan_number.getText().toString().trim() + "' LIMIT 1");

                            mData = ls.executeReader("Select count(*) from MEMBER where nicnumber LIKE '%" + et_idcard_number.getText().toString().trim() + "%'");

                            Log.d("000777", "NIC NUMBER COUNT: " + mData[0][0]);
                       /*     for (int i = 0; i < mData.length; i++) {
                                Log.d("000777", "FULL NAME: " + mData[i][0]);
                            }*/

                            if (Integer.parseInt(mData[0][0]) > 0) {
                            SharedPreferences settings = getSharedPreferences("shared_preference", MODE_PRIVATE);
                            // Writing data to SharedPreferences
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("nic_number", et_idcard_number.getText().toString());
                            editor.putString("type", "srch_nicnumber");
                            editor.commit();

                            Log.d("000777", "work");

                            Intent intent = new Intent(ctx, Search_MemberAndKhandanList_Activity.class);
                            startActivity(intent);
                            alertDialog.dismiss();

                        } else {
                            alertDialog.dismiss();
                            alertD.show();
                            Toast.makeText(ctx, correctCNICprompt, Toast.LENGTH_SHORT).show();
                        }

                        } catch (Exception e) {
                            alertDialog.dismiss();
                            alertD.show();
                            Toast.makeText(ctx, correctCNICprompt, Toast.LENGTH_SHORT).show();
                            Log.d("000777", "ERROR: " + e.getMessage());

                        }
                    }
                }, 1500);

            }
        });


    }


    public void Dialog_SearchWithPhoneNumber() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.dialog_search_with_phonenumber_layout, null);

        final AlertDialog alertD = new AlertDialog.Builder(this).create();

        et_phone_number = (EditText) promptView.findViewById(R.id.et_phone_number);
        iv_close = (ImageView) promptView.findViewById(R.id.iv_close);
        btn_jaari_rhy = (Button) promptView.findViewById(R.id.btn_jaari_rhy);

        et_phone_number.addTextChangedListener(new TextWatcher() {
            int previous = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previous = et_phone_number.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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


        alertD.setView(promptView);
        alertD.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        alertD.setCanceledOnTouchOutside(false);
        alertD.setCancelable(false);
        alertD.show();


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
                    Toast.makeText(ctx, enterMobilePrompt, Toast.LENGTH_SHORT).show();
                    return;
                }

                alertD.dismiss();

                alertDialog = new Dialog(ctx);
                LayoutInflater layout = LayoutInflater.from(ctx);
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

                        Log.d("Search_code", "work");
                        try {
                            Lister ls = new Lister(Search_Activity.this);
                            ls.createAndOpenDB();
                            //mData = ls.executeReader("Select data from MEMBER where khandan_id = '" + et_khandan_number.getText().toString().trim() + "' LIMIT 1");

                            mData = ls.executeReader("Select count(*) from MEMBER where phone_number LIKE '%" + et_phone_number.getText().toString().trim() + "%'");

                            Log.d("000777", "PHONE NUMBER COUNT: " + mData[0][0]);

                            /*for (int i = 0; i < mData.length; i++) {
                                Log.d("000777", "FULL NAME: " + mData[i][0]);
                            }*/

                            if (Integer.parseInt(mData[0][0]) > 0) {
                            SharedPreferences settings = getSharedPreferences("shared_preference", MODE_PRIVATE);
                            // Writing data to SharedPreferences
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("phone_number", et_phone_number.getText().toString());
                            editor.putString("type", "srch_phonenumber");
                            editor.commit();

                            Log.d("000777", "work");

                            Intent intent = new Intent(ctx, Search_MemberAndKhandanList_Activity.class);
                            startActivity(intent);
                            alertDialog.dismiss();
                            } else {
                                alertDialog.dismiss();
                                alertD.show();
                                Toast.makeText(ctx, R.string.correctMobilePrompt, Toast.LENGTH_SHORT).show();

                            }

                        } catch (Exception e) {
                            alertDialog.dismiss();
                            alertD.show();
                            Toast.makeText(ctx, R.string.correctMobilePrompt, Toast.LENGTH_SHORT).show();
                            Log.d("000777", "ERROR: " + e.getMessage());

                        }
                    }
                }, 1500);

            }
        });


    }

    public void Dialog_SearchWithKhandanNumber() {


        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.dialog_search_with_khandannumber_layout, null);

        final AlertDialog alertD = new AlertDialog.Builder(this).create();

        et_khandan_number = (EditText) promptView.findViewById(R.id.et_khandan_number_search);
        iv_close = (ImageView) promptView.findViewById(R.id.iv_close);
        btn_jaari_rhy = (Button) promptView.findViewById(R.id.btn_jaari_rhy);


        alertD.setView(promptView);

        //alertD.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertD.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        alertD.setCanceledOnTouchOutside(false);
        alertD.setCancelable(false);
        alertD.show();


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
            }
        });

        btn_jaari_rhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (et_khandan_number.getText().toString().isEmpty()) {
                    Toast.makeText(ctx, R.string.enterFamilyNumPrompt, Toast.LENGTH_SHORT).show();
                    return;
                }

                alertD.dismiss();

                alertDialog = new Dialog(ctx);
                LayoutInflater layout = LayoutInflater.from(ctx);
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

                        Log.d("Search_code", "work");
                        try {
                            Lister ls = new Lister(Search_Activity.this);
                            ls.createAndOpenDB();
                            //mData = ls.executeReader("Select data from MEMBER where khandan_id = '" + et_khandan_number.getText().toString().trim() + "' LIMIT 1");

                          /*  mData = ls.executeReader("Select uid from KHANDAN where manual_id LIKE '%" + et_khandan_number.getText().toString().trim() + "%'");*/
                            mData = ls.executeReader("Select count(*) from KHANDAN where manual_id LIKE '%" + et_khandan_number.getText().toString().trim() + "%'");

                            Log.d("000777", "KHANDAN NUMBER COUNT: " + mData[0][0]);

                           /* for (int i = 0; i < mData.length; i++) {
                                Log.d("000777", "UID: " + mData[i][0]);
                            }*/

                            if (Integer.parseInt(mData[0][0]) > 0) {
                            SharedPreferences settings = getSharedPreferences("shared_preference", MODE_PRIVATE);
                            // Writing data to SharedPreferences
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("khandan_number", et_khandan_number.getText().toString());
                            editor.putString("type", "srch_khandan_number");
                            editor.commit();

                            Log.d("000777", "work");


//                            alertD.dismiss();

                            //ClassListener.mSlideMenu.close(false);
                            Intent intent = new Intent(ctx, Search_MemberAndKhandanList_Activity.class);
                            startActivity(intent);
                            alertDialog.dismiss();

                            } else {
                                //Log.d("000777", "ELSE Count NicNO: " + mData[0][0]);
                                alertDialog.dismiss();
                                alertD.show();
                                Toast.makeText(ctx, correctFamilyNumPrompt, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            alertDialog.dismiss();
                            alertD.show();
                            Toast.makeText(ctx, correctFamilyNumPrompt, Toast.LENGTH_SHORT).show();
                            Log.d("000777", "ERROR: " + e.getMessage());

                        }
                    }
                }, 1500);

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        ClassListener.mSlideMenu.close(false);
        startActivity(new Intent(ctx, HomePage_Activity.class));
    }


}
