package com.akdndhrc.covid_module.LHW_App.LHW_MaleDashboardActivities.MaleReferralActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildRefferalActivities.Child_RefferalFormView_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildRefferalActivities.Child_RefferalForm_Activity;
import com.akdndhrc.covid_module.Adapter.Adt_ChildDashboard.Adt_ChildRefferalRecordList;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Male_RefferalRecordList_Activity extends AppCompatActivity {

    Context ctx = Male_RefferalRecordList_Activity.this;

    TextView txt_age, txt_naam;
    ListView lv;
    Button btn_naya_form_shamil_kre;
    RelativeLayout rl_navigation_drawer, rl_home;
    ImageView iv_navigation_drawer, iv_home, image_gender;

    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    Adt_ChildRefferalRecordList adt;
    String[][] mData;
    String child_uid, child_name, child_age, child_gender,age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_refferal_record_list);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Male_RefferalRecordList_Activity.class));

        child_uid = getIntent().getExtras().getString("u_id");
        age = getIntent().getExtras().getString("child_age");
        child_name = getIntent().getExtras().getString("child_name");
        child_gender = getIntent().getExtras().getString("child_gender");


        //ListView
        lv = findViewById(R.id.lv);

        //TextView
        txt_naam = findViewById(R.id.txt_naam);
        txt_age = findViewById(R.id.txt_age);


       /* txt_naam.setText(child_name);
        txt_age.setText(child_age);*/

        calculateAge();


        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
      //  iv_home.setVisibility(View.GONE);


        image_gender = findViewById(R.id.image_gender);


        if (Integer.parseInt(age) >=5 && Integer.parseInt(age) <=14){
            if (child_gender.equalsIgnoreCase("0")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    image_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_adult_female_50dp));
                    image_gender.setImageTintList(ctx.getResources().getColorStateList(R.color.pink_color));
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        image_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                        image_gender.setImageTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
                    }
                }
            }
        }
        else {
            if (child_gender.equalsIgnoreCase("1")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    image_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                    image_gender.setImageTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        image_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.woman_icon));

                    }
                }
            }
        }




        //Button
        btn_naya_form_shamil_kre = findViewById(R.id.btn_naya_form_shamil_kre);


        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, R.string.navigation, Toast.LENGTH_SHORT).show();
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

        btn_naya_form_shamil_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(ctx , Child_RefferalForm_Activity.class));
                Intent intent3 = new Intent(ctx, Child_RefferalForm_Activity.class);
                intent3.putExtra("u_id", child_uid);
                startActivity(intent3);
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newIntent = new Intent(ctx, Child_RefferalFormView_Activity.class);
                newIntent.putExtra("u_id", child_uid);
                newIntent.putExtra("record_date", mData[position][0]);
                newIntent.putExtra("added_on", mData[position][1]);
                startActivity(newIntent);


               Log.d("000333","1: " +mData[position][0]);
               Log.d("000333","2: " +mData[position][1]);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        hashMapArrayList.clear();
        try {


            Lister ls = new Lister(Male_RefferalRecordList_Activity.this);
          ls.createAndOpenDB();

           /* SharedPreferences settings = getSharedPreferences("Khandanuuid", MODE_PRIVATE);
            // Reading from SharedPreferences
            String khandan_uuid_sp = settings.getString("k_uuid", "");
//            Toast.makeText(getApplicationContext(),khandan_uuid_sp,Toast.LENGTH_SHORT).show();*/

            mData = ls.executeReader("Select record_data,added_on from REFERAL where member_uid = '" + child_uid + "' ORDER BY added_on DESC");

            HashMap<String, String> map;
            for (int i = 0; i < mData.length; i++) {

                map = new HashMap<>();
                map.put("i", "" + ":" + String.valueOf(i + 1));
                map.put("refferal_text", "" + mData[i][0]);
                map.put("refferal_datetime", "" + "");
                //  map.put("mother_name", "" +"کرن اقبال");

                hashMapArrayList.add(map);
            }
            adt = new Adt_ChildRefferalRecordList(ctx, hashMapArrayList);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);


        } catch (Exception e) {
            Log.d("12345", "Error: " + e.getMessage());
            Toast tt  =Toast.makeText(ctx, R.string.noRefferalRecord, Toast.LENGTH_SHORT);
            tt.setGravity(Gravity.CENTER, 0, 0);
            tt.show();
        }


    }

    private void calculateAge() {

        Lister ls = new Lister(ctx);
        ls.createAndOpenDB();

        String mData[][] = ls.executeReader("Select full_name,dob from MEMBER where uid = '" + child_uid + "'");

        child_name = mData[0][0];
        txt_naam.setText(mData[0][0]);
        Log.d("000999", "DOB: " + mData[0][1]);

        try {
            SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
            Date d = null;
            d = parse.parse(mData[0][1]);
            Date n = new Date();
            long dob = d.getTime();
            long now = n.getTime();
            long diff = (now - dob) / 1000;
            long days = diff / 86400;
            // long days_chart = diff / 86400;
            int days_chart = 0;

            int dob_days = 0;
            int dob_weeks = 0;
            int dob_months = 0;
            int dob_months_charts = 0;
            int dob_years = 0;
            while (days > 7) {
                if ((days - 365) > 0) {
                    days -= 365;
                    dob_years++;
                    continue;
                }
                if ((days - 30) > 0) {
                    days -= 30;
                    dob_months++;

                    continue;
                }
                if ((days - 7) > 0) {
                    days -= 7;
                    dob_weeks++;
                }
            }

            dob_days = Integer.valueOf((int) days);
            Log.d("000999", "Saal: " + String.valueOf(dob_years));
            Log.d("000999", "Maah: " + String.valueOf(dob_months));
            Log.d("000999", "Maah_chart: " + String.valueOf(dob_months));

            child_age = String.valueOf(dob_years) + " سال " + String.valueOf(dob_months) + " مہ ";
            age = String.valueOf(dob_years);
            txt_age.setText(child_age);
        }
        catch(Exception e) {
            Log.d("000999", "DOB Error: " + e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
