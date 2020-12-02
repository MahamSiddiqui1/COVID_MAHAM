package com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHaamlaRecordActivities.MotherQabalAzPedaishActivities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.Adt_MotherDashboard.Adt_HaamlaDashboard.Adt_MotherQabalAzPedaishRecordList;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHaamlaRecordActivities.Mother_HaamlaDashboard_Activity;
import com.akdndhrc.covid_module.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Mother_QabalAzPedaishList_Activity extends AppCompatActivity {


    Context ctx = Mother_QabalAzPedaishList_Activity.this;
    TextView txt_mother_age, txt_mother_name;
    ListView lv;
    Button btn_naya_form_shamil_kre;
    RelativeLayout rl_qabalAzpedaish_doorah, rl_qabalAzpedaish_health_centre_ka_doorah;


    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    Adt_MotherQabalAzPedaishRecordList adt;

    ImageView iv_navigation_drawer, iv_home;
    String mother_uid;
    String[][] mData;
    String preg_id, TodayDate, mother_name, mother_age;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_qabal_az_pedaish_list);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Mother_QabalAzPedaishList_Activity.class));

        mother_uid = getIntent().getExtras().getString("u_id");
        preg_id = getIntent().getExtras().getString("preg_id");
        mother_name = getIntent().getExtras().getString("mother_name");
        mother_age = getIntent().getExtras().getString("mother_age");


        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());


        //ListView
        lv = findViewById(R.id.lv);

        //TextView
        txt_mother_name = findViewById(R.id.txt_mother_name);
        txt_mother_age = findViewById(R.id.txt_mother_age);

        txt_mother_name.setText(mother_name);
        txt_mother_age.setText(mother_age);


        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);


        //Button
        btn_naya_form_shamil_kre = findViewById(R.id.btn_naya_form_shamil_kre);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mData[position][2].equalsIgnoreCase("0")) {
                    Intent newIntent = new Intent(ctx, Mother_QabalAzPedaishFormView_Activity.class);
                    newIntent.putExtra("u_id", mother_uid);
                    newIntent.putExtra("record_date", mData[position][0]);
                    newIntent.putExtra("added_on", mData[position][1]);
                    newIntent.putExtra("preg_id", preg_id);
                    startActivity(newIntent);
                } else if (mData[position][2].equalsIgnoreCase("1")) {
                    Intent newIntent = new Intent(ctx, Mother_QabalAzPedaishDorahFormView_Activity.class);
                    newIntent.putExtra("u_id", mother_uid);
                    newIntent.putExtra("record_date", mData[position][0]);
                    newIntent.putExtra("added_on", mData[position][1]);
                    newIntent.putExtra("preg_id", preg_id);
                    startActivity(newIntent);
                } else if (mData[position][2].equalsIgnoreCase("2")) {
                    Intent newIntent = new Intent(ctx, Mother_QabalAzPedaishHealthCenterDorahFormView_Activity.class);
                    newIntent.putExtra("u_id", mother_uid);
                    newIntent.putExtra("record_date", mData[position][0]);
                    newIntent.putExtra("added_on", mData[position][1]);
                    newIntent.putExtra("preg_id", preg_id);
                    startActivity(newIntent);
                }
            }
        });
        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, R.string.navigation, Toast.LENGTH_SHORT).show();
            }
        });

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(ctx, Mother_HaamlaDashboard_Activity.class);
                newIntent.putExtra("u_id", mother_uid);
                newIntent.putExtra("preg_id", preg_id);
                newIntent.putExtra("mother_age", mother_age);
                newIntent.putExtra("mother_name", mother_name);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
            }
        });

        btn_naya_form_shamil_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Lister ls = new Lister(Mother_QabalAzPedaishList_Activity.this);
                    ls.createAndOpenDB();


                    String[][] mData = ls.executeReader("Select record_data,count(*), max(added_on) from MANC where member_uid = '" + mother_uid + "'");

                    if (Integer.parseInt(mData[0][1]) > 0) {
                        Log.d("000985", "Record Date: " + mData[0][0]);

                        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar c = Calendar.getInstance();
                        String TodayDate = dates.format(c.getTime());
                        String DateFrom = mData[0][0];
                        Date date1;
                        Date date2;
                        date1 = dates.parse(TodayDate);
                        date2 = dates.parse(DateFrom);
                        long difference = Math.abs(date1.getTime() - date2.getTime());
                        long differenceDates = (difference / (24 * 60 * 60 * 1000));
                        String dayDifference = Long.toString(differenceDates);

                        Log.e("000985", "TodayDate: " + TodayDate);
                        Log.e("000985", "Register Date: " + DateFrom);
                        Log.e("000985", "Days_Differernce: " + dayDifference);

                        if (Integer.parseInt(dayDifference) >= 1 || Integer.parseInt(dayDifference) > 1) {
                            Log.d("000985", "IFFFFFFFFF !!!!!!!!!!!!!!!!!!!!!");
                            Dialog_QabalAzPedaish_DoorahAndHealthCenterButton();

                        } else {
                            Log.d("000985", "ELSE !!!!!!!!!!!!!!!");

                            final Snackbar snackbar = Snackbar.make(v, R.string.noRecordTodayWaitTomorrow, Snackbar.LENGTH_SHORT);
                            View mySbView = snackbar.getView();
                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                            TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(15);
                            snackbar.setDuration(5000);
                            snackbar.show();

                        }
                    } else {
                        Dialog_QabalAzPedaish_DoorahAndHealthCenterButton();
                    }


                } catch (Exception e) {
                    Log.d("000985", "Error: " + e.getMessage());
                }

            }
        });


        adt = new Adt_MotherQabalAzPedaishRecordList(ctx, hashMapArrayList);
        lv.setAdapter(adt);

    }


    public void Dialog_QabalAzPedaish_DoorahAndHealthCenterButton() {
        final Dialog dialog = new Dialog(ctx);
        LayoutInflater layout = LayoutInflater.from(ctx);
        final View dialogView = layout.inflate(R.layout.dialog_mother_qabal_az_pedaish_form_button_layout, null);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);


        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        rl_qabalAzpedaish_doorah = (RelativeLayout) dialog.findViewById(R.id.rl_qabalAzpedaish_doorah);
        rl_qabalAzpedaish_health_centre_ka_doorah = (RelativeLayout) dialog.findViewById(R.id.rl_qabalAzpedaish_health_centre_ka_doorah);


        dialog.show();


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        rl_qabalAzpedaish_doorah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // startActivity(new Intent(ctx, Mother_QabalAzPedaishDorahForm_Activity.class));
                Intent intent1 = new Intent(ctx, Mother_QabalAzPedaishDorahForm_Activity.class);
                intent1.putExtra("u_id", mother_uid);
                intent1.putExtra("preg_id", preg_id);
                startActivity(intent1);
                dialog.dismiss();

            }
        });

        rl_qabalAzpedaish_health_centre_ka_doorah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  startActivity(new Intent(ctx, Mother_QabalAzPedaishHealthCenterDorahForm_Activity.class));
                Intent intent1 = new Intent(ctx, Mother_QabalAzPedaishHealthCenterDorahForm_Activity.class);
                intent1.putExtra("u_id", mother_uid);
                intent1.putExtra("preg_id", preg_id);
                startActivity(intent1);
                dialog.dismiss();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();


        try {

            hashMapArrayList.clear();
            Lister ls = new Lister(Mother_QabalAzPedaishList_Activity.this);
           ls.createAndOpenDB();
           /* SharedPreferences settings = getSharedPreferences("Khandanuuid", MODE_PRIVATE);
            // Reading from SharedPreferences
            String khandan_uuid_sp = settings.getString("k_uuid", "");
            //  Toast.makeText(getApplicationContext(),khandan_uuid_sp,Toast.LENGTH_SHORT).show();*/

            try {


                //String[][] data = ls.executeReader("Select* from KHANDAN "); pregnancy_id,
                mData = ls.executeReader("Select record_data,added_on,type from MANC where member_uid = '" + mother_uid + "' AND pregnancy_id= '" + preg_id + "' AND is_synced NOT IN ('-1') ORDER BY added_on DESC");

                Log.d("mother_data", String.valueOf(mData.length));
            } catch (Exception e) {
                Log.d("mother_data", String.valueOf(e.getMessage()));
            }

            HashMap<String, String> map;
            for (int i = 0; i < mData.length; i++) {

                map = new HashMap<>();
                map.put("i", "" + ":" + String.valueOf(i + 1));
                map.put("qabalAzpedaish_from_txt", "" + mData[i][0]);
                //  map.put("bemaari_record_date", "" +mData[i][1]);
                //  map.put("mother_name", "" +"کرن اقبال");

                hashMapArrayList.add(map);
            }
            adt = new Adt_MotherQabalAzPedaishRecordList(ctx, hashMapArrayList);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);


        } catch (Exception e) {
            Log.d("12345", "Error: " + e.getMessage());
            Toast.makeText(ctx, R.string.noRecord, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
