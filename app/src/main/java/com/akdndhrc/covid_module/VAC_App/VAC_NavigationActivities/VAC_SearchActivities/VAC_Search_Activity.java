package com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities.VAC_SearchActivities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.VAC_App.HomePageVacinator_Activity;

import org.json.JSONObject;

public class VAC_Search_Activity extends AppCompatActivity {

    Context ctx = VAC_Search_Activity.this;
    ImageView iv_idcard, iv_smartphone, iv_close,iv_qrcode;
    EditText et_idcard_number, et_phone_number, et_vaccine_card_number;
    Button btn_jaari_rhy, btn_finger_print_search;
    ImageView iv_navigation_drawer, iv_home, iv_khandannumber;
    String[][] mData, mDatanew;
    TextView tv_kahndan_number_search;
    String vacine_card_num;
    Dialog alertDialog;
String temp ="0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //ImageView
        iv_idcard = findViewById(R.id.iv_idcard);
        iv_smartphone = findViewById(R.id.iv_smartphone);
        iv_khandannumber = findViewById(R.id.iv_khandannumber);
        tv_kahndan_number_search = findViewById(R.id.tv_kahndan_number_search);
        tv_kahndan_number_search.setText("Search by Vaccination Card");
        // iv_khandannumber.setBackgroundResource(R.drawable.injection);
        iv_khandannumber.setImageDrawable(ContextCompat.getDrawable(VAC_Search_Activity.this, R.drawable.icons8_syringe_96));


        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_qrcode = findViewById(R.id.iv_qrcode);
        iv_home.setVisibility(View.GONE);
        iv_navigation_drawer.setVisibility(View.GONE);
        btn_finger_print_search = findViewById(R.id.btn_finger_print_search);

        iv_idcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_SearchWithIDCard();
            }
        });

       /* btn_finger_print_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), VAC_Search_WithFingerprintAndQrCode_Activity.class));
            }
        });*/
        iv_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ctx,VAC_Search_QRCode_Activity.class));
            }
        });

        iv_smartphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_SearchWithPhoneNumber();
                // Toast.makeText(ctx, "Data field unavailable.", Toast.LENGTH_SHORT).show();

            }
        });

        iv_khandannumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_SearchWithVaccineCardNumber();
                // Toast.makeText(ctx, "Data field unavailable.", Toast.LENGTH_SHORT).show();

            }
        });
        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Navigation", Toast.LENGTH_SHORT).show();
            }
        });

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(ctx, HomePageVacinator_Activity.class);
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
                    Toast.makeText(ctx, "Please enter an ID card number", Toast.LENGTH_SHORT).show();
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
                            Lister ls = new Lister(VAC_Search_Activity.this);
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

                                Intent intent = new Intent(ctx, VAC_Search_MemberList_Activity.class);
                                startActivity(intent);
                                alertDialog.dismiss();

                            } else {
                                alertDialog.dismiss();
                                alertD.show();
                                Toast.makeText(ctx, "Please enter the correct ID card number", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            alertDialog.dismiss();
                            alertD.show();
                            Toast.makeText(ctx, "Please enter the correct ID card number", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ctx, "Please enter a mobile number", Toast.LENGTH_SHORT).show();
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
                            Lister ls = new Lister(VAC_Search_Activity.this);
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

                                Intent intent = new Intent(ctx, VAC_Search_MemberList_Activity.class);
                                startActivity(intent);
                                alertDialog.dismiss();
                            } else {
                                alertDialog.dismiss();
                                alertD.show();
                                Toast.makeText(ctx, "Please enter the correct mobile number", Toast.LENGTH_SHORT).show();

                            }

                        } catch (Exception e) {
                            alertDialog.dismiss();
                            alertD.show();
                            Toast.makeText(ctx, "Please enter the correct mobile number", Toast.LENGTH_SHORT).show();
                            Log.d("000777", "ERROR: " + e.getMessage());

                        }
                    }
                }, 1500);

            }
        });
    }

    public void Dialog_SearchWithVaccineCardNumber() {


        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View promptView = layoutInflater.inflate(R.layout.dialog_search_with_vacination_layout, null);

        final AlertDialog alertD = new AlertDialog.Builder(ctx).create();

        et_vaccine_card_number = (EditText) promptView.findViewById(R.id.et_vacine_number_search);
        iv_close = (ImageView) promptView.findViewById(R.id.iv_close);
        btn_jaari_rhy = (Button) promptView.findViewById(R.id.btn_jaari_rhy);

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
                if (et_vaccine_card_number.getText().toString().isEmpty()) {
                    Toast.makeText(ctx, "Please enter the vaccination card number", Toast.LENGTH_SHORT).show();
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

                        try {
                            Lister ls = new Lister(ctx);
                            ls.createAndOpenDB();

                            String q = "Select data from MEMBER where data LIKE '%" + et_vaccine_card_number.getText().toString().trim() + "%' and age <=14";
                            Log.d("000888", "Check Query : " + q);
                            String[][] mData = ls.executeReader(q);

                            if (mData != null) {

                                for (int i = 0; i < mData.length; i++) {

                                    Log.d("000888","Looop:" + i);

                                    JSONObject jsonObject = new JSONObject(mData[i][0]);
                                    try {
                                        if (jsonObject.has("vaccination_card_number")) {
                                            if (jsonObject.getString("vaccination_card_number").contains(et_vaccine_card_number.getText().toString())) {
                                                Log.d("000888","DATA:" + mData[i][0]);
                                                Log.d("000888", "IF vaccination_card_number : " + jsonObject.getString("vaccination_card_number"));
                                                temp="1";
                                                continue;
                                            }
                                            else {
                                                Log.d("000888", "ELSE 1: ");
                                                continue;
                                            }
                                        }
                                       /*else if (jsonObject.has("vaination_card_number")) {
                                            if (jsonObject.getString("vaination_card_number").contains(et_vaccine_card_number.getText().toString()))
                                            {
                                                Log.d("000888","DATA:" + mData[i][0]);
                                                Log.d("000888", "IF vaination_card_number : " + jsonObject.getString("vaination_card_number"));
                                                temp="1";
                                                continue;
                                            }
                                            else {
                                                Log.d("000888", "ELSE 2: ");
                                                continue;
                                            }
                                        }*/
                                        else {
                                            Log.d("000888", "ELSE 11: ");
                                            continue;
                                        }
                                    } catch (Exception e) {
                                        Log.d("000888", "CATCH: " + e.getMessage());
                                        continue;
                                    }
                                }


                                if (temp.equalsIgnoreCase("1"))
                                {
                                    SharedPreferences settings = getSharedPreferences("shared_preference", MODE_PRIVATE);
                                    // Writing data to SharedPreferences
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("vaccination_card_no", et_vaccine_card_number.getText().toString());
                                    editor.putString("type", "vaccination_cardno");
                                    editor.commit();

                                    temp="0";
                                    Intent intent = new Intent(ctx, VAC_Search_MemberList_Activity.class);
                                    startActivity(intent);
                                    alertDialog.dismiss();
                                }
                                else{
                                    Toast.makeText(ctx, "Please enter the correct vaccination card number", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                    alertD.show();
                                }

                            } else {
                                Toast.makeText(ctx, "Please enter the correct vaccination card number", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                                alertD.show();

                            }


                        } catch (Exception e) {
                            alertDialog.dismiss();
                            alertD.show();
                            Toast.makeText(ctx, "Please enter the correct vaccination card number", Toast.LENGTH_SHORT).show();
                            Log.d("000888", "Search Vac Err: " + e.getMessage());

                        }

                    }
                }, 1500);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent newIntent = new Intent(ctx, HomePageVacinator_Activity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
    }
}
