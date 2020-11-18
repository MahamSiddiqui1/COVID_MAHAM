package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_RegisterActivities;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;

import java.util.StringTokenizer;

public class LHW_RegisterHouseView_Activity extends AppCompatActivity {

    Context ctx = LHW_RegisterHouseView_Activity.this;

    EditText et_khandan_ka_number, et_khandan_kai_sarbarah_ka_naam, et_mukamal_pata,et_khandan_kai_sarbarah_ka_nicnumber , et_khandan_kai_sarbarah_ka_mobile_number ;
    Button btn_jamaa_kre;
    RelativeLayout rl_home_image, rl_navigation_drawer;
    ImageView iv_editform;

    String login_useruid;
    ProgressBar pbProgress;
    String mData[][];
    String  khandan_uuid;
    Dialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lhw__register_house_view);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, LHW_RegisterHouseView_Activity.class));

        khandan_uuid = getIntent().getExtras().getString("khandan_uuid");

        try {

           /* SharedPreferences settings = getSharedPreferences("Khandanuuid", MODE_PRIVATE);
            khandan_uuid = settings.getString("k_uuid", "");*/

            Log.d("000147", "khandan_uuid: " + khandan_uuid);

            //Get shared USer name
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences("UserLogin", 0); // 0 - for private mode
            String shared_useruid = prefelse.getString("login_userid", null); // getting String
            login_useruid = shared_useruid;
            Log.d("000147", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000147", "Shared Err:" + e.getMessage());
        }


        //check_gps();

        //EditText
        et_khandan_ka_number = findViewById(R.id.et_khandan_ka_number);
        et_khandan_kai_sarbarah_ka_naam = findViewById(R.id.et_khandan_kai_sarbarah_ka_naam);
        et_khandan_kai_sarbarah_ka_nicnumber = findViewById(R.id.et_khandan_kai_sarbarah_ka_nicnumber);
        et_khandan_kai_sarbarah_ka_mobile_number = findViewById(R.id.et_khandan_kai_sarbarah_ka_mobile_number);
        et_mukamal_pata = findViewById(R.id.et_mukamal_pata);


        //Progressbar
        pbProgress = findViewById(R.id.pbProgress);

        //Button
        btn_jamaa_kre = findViewById(R.id.submit);
        btn_jamaa_kre.setVisibility(View.GONE);

        //ImageView
        iv_editform = findViewById(R.id.iv_editform);

        //RelativeLayout
        rl_navigation_drawer = findViewById(R.id.rl_navigation_drawer);
        rl_home_image = findViewById(R.id.rl_home_image);
        rl_navigation_drawer.setVisibility(View.GONE);
        rl_home_image.setVisibility(View.GONE);



        rl_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Navigation", Toast.LENGTH_SHORT).show();
            }
        });

        rl_home_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Home", Toast.LENGTH_SHORT).show();
            }
        });


        //Edit Person CNIC
        et_khandan_kai_sarbarah_ka_nicnumber.addTextChangedListener(new TextWatcher() {
            int previous = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previous = et_khandan_kai_sarbarah_ka_nicnumber.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if ((previous < length) && (length == 5 || length == 13)) {
                    String data = et_khandan_kai_sarbarah_ka_nicnumber.getText().toString();
                    et_khandan_kai_sarbarah_ka_nicnumber.setText(data + "-");
                    et_khandan_kai_sarbarah_ka_nicnumber.setSelection(length + 1);
                }
            }
        });


        /////Mobile Number
        et_khandan_kai_sarbarah_ka_mobile_number.addTextChangedListener(new TextWatcher() {
            int prev = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prev = et_khandan_kai_sarbarah_ka_mobile_number.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if ((prev < length) && (length == 4)) {
                    String data = et_khandan_kai_sarbarah_ka_mobile_number.getText().toString();
                    et_khandan_kai_sarbarah_ka_mobile_number.setText(data + "-");
                    et_khandan_kai_sarbarah_ka_mobile_number.setSelection(length + 1);
                }
            }
        });



        iv_editform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pbProgress.setVisibility(View.VISIBLE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        et_khandan_ka_number.setEnabled(false);
                        et_khandan_kai_sarbarah_ka_naam.setEnabled(true);
                        et_khandan_kai_sarbarah_ka_nicnumber.setEnabled(true);
                        et_khandan_kai_sarbarah_ka_mobile_number.setEnabled(true);
                        et_mukamal_pata.setEnabled(true);


                        btn_jamaa_kre.setVisibility(View.VISIBLE);
                        pbProgress.setVisibility(View.GONE);
                        iv_editform.setVisibility(View.GONE);


                    }
                }, 2500);


            }
        });



        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (et_khandan_ka_number.getText().toString().isEmpty()) {
                    final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی خاندان کا نمبر درج کریں.", Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(15);
                    snackbar.setDuration(3000);
                    snackbar.show();
                    return;
                }


                if (et_khandan_kai_sarbarah_ka_naam.getText().toString().isEmpty()) {
                    final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی خاندان کے سربراہ کا نام  درج کریں.", Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(15);
                    snackbar.setDuration(3000);
                    snackbar.show();
                    return;
                }
                
                
                update_data();
                
            }
        });


    }

    private void update_data() {

        alertDialog = new Dialog(ctx);
        LayoutInflater layout = LayoutInflater.from(ctx);
        final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

        alertDialog.setContentView(dialogView);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();


        try {
            
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {

                        SharedPreferences settings = getSharedPreferences("Khandanuuid", MODE_PRIVATE);
                        // Writing data to SharedPreferences
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("k_uuid", khandan_uuid);
                        editor.putString("k_uuid_m", et_khandan_ka_number.getText().toString());
                        editor.commit();


                        String et_address;
                        if (et_mukamal_pata.getText().toString().isEmpty()) {

                            et_address = "-";
                        } else {
                            et_address = et_mukamal_pata.getText().toString();
                        }

                        String et_k_cnic_number;
                        if (et_khandan_kai_sarbarah_ka_nicnumber.getText().toString().isEmpty()) {

                            et_k_cnic_number = "-";
                        } else {
                            et_k_cnic_number = et_khandan_kai_sarbarah_ka_nicnumber.getText().toString();
                        }

                        String et_k_mobile_number;
                        if (et_khandan_kai_sarbarah_ka_mobile_number.getText().toString().isEmpty()) {
                            et_k_mobile_number = "-";
                        } else {
                            et_k_mobile_number = et_khandan_kai_sarbarah_ka_mobile_number.getText().toString();
                        }

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE KHANDAN SET " +
                                "manual_id='" + et_khandan_ka_number.getText().toString()+ "'," +
                                "family_head_name='" + et_khandan_kai_sarbarah_ka_naam.getText().toString()+ "'," +
                                "family_address='" + et_address+"|"+et_k_cnic_number+"|"+et_k_mobile_number+ "'," +
                                "is_synced='" + 0 + "'" +
                                "WHERE uid = '" + khandan_uuid + "'";
                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d("000147", "Data: " + update_record);
                        Log.d("000147", "Query: " + res.toString());

                        Toast.makeText(ctx, "ڈیٹا اپڈیٹ ہوگیا ہے", Toast.LENGTH_SHORT).show();
                      
                        
                    } catch (Exception e) {
                        alertDialog.dismiss();
                        Log.d("000147", " Error" + e.getMessage());
                        // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    } finally {
                        finish();
                    }

                }
            }, 2000);

        } catch (Exception e) {
            Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
            Log.d("000147", " Error" + e.getMessage());
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            mData = ls.executeReader("Select manual_id , uid , family_head_name, family_address from KHANDAN where uid = '" + khandan_uuid + "'");

            khandan_uuid = mData[0][1];

            et_khandan_ka_number.setEnabled(false);
            et_khandan_kai_sarbarah_ka_naam.setEnabled(false);
            et_khandan_kai_sarbarah_ka_nicnumber.setEnabled(false);
            et_khandan_kai_sarbarah_ka_mobile_number.setEnabled(false);
            et_mukamal_pata.setEnabled(false);


            Log.d("000147","Manual ID: " +mData[0][0]);
            Log.d("000147","K_ID: " +mData[0][1]);
            Log.d("000147","family_head_name: " +mData[0][2]);
            Log.d("000147","family_address: " +mData[0][3]);

            et_khandan_ka_number.setText(mData[0][0]);
            et_khandan_kai_sarbarah_ka_naam.setText(mData[0][2]);


            StringTokenizer s = new StringTokenizer(mData[0][3],"|");
            String address =  s.nextToken();
            String k_cnic_number =  s.nextToken();
            String k_mobile_number =  s.nextToken();
            et_khandan_kai_sarbarah_ka_nicnumber.setText(k_cnic_number);
            et_khandan_kai_sarbarah_ka_mobile_number.setText(k_mobile_number);
            et_mukamal_pata.setText(address);

            Log.d("000147","address: " +address);
            Log.d("000147","k_cnic_number: " +k_cnic_number);
            Log.d("000147","k_mobile_number: " +k_mobile_number);



            ls.closeDB();

        } catch (Exception e) {
            e.printStackTrace();

            Log.d("000147", "Error: " + e.getMessage());
            Toast.makeText(ctx, "Something Wrong!!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {

        finish();

    }
}
