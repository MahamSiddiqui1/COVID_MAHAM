package com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildNashoNumaActivities;

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

import com.akdndhrc.covid_module.Adapter.Adt_ChildDashboard.Adt_ChildNashoNumaRecordList;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class Child_NashoNumaRecordList_Activity extends AppCompatActivity {

    Context ctx = Child_NashoNumaRecordList_Activity.this;
    TextView txt_age, txt_naam;
    ListView lv;
    Button btn_naya_form_shamil_kre, btn_growth_chart;
    RelativeLayout rl_navigation_drawer, rl_home;

    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    Adt_ChildNashoNumaRecordList adt;

    ImageView iv_navigation_drawer, iv_home, image_gender;
    String child_uid, child_name, child_age, child_gender, child_age_months;
    String[][] mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_nasho_numa_record_list);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Child_NashoNumaRecordList_Activity.class));

        child_uid = getIntent().getExtras().getString("u_id");
        child_name = getIntent().getExtras().getString("child_name");
        child_age = getIntent().getExtras().getString("child_age");
        child_gender = getIntent().getExtras().getString("child_gender");
        child_age_months = getIntent().getExtras().getString("child_age_month");


        //ListView
        lv = findViewById(R.id.lv);

        //TextView
        txt_age = findViewById(R.id.txt_age);
        txt_naam = findViewById(R.id.txt_naam);

        txt_naam.setText(child_name);
        txt_age.setText(child_age);


        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);

        image_gender = findViewById(R.id.image_gender);

     /*   if (child_gender.equalsIgnoreCase("0")) {
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
*/
        //Button
        btn_naya_form_shamil_kre = findViewById(R.id.btn_naya_form_shamil_kre);
        btn_growth_chart = findViewById(R.id.btn_growth_chart);


        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Navigation", Toast.LENGTH_SHORT).show();
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

                // startActivity(new Intent(ctx , Child_NashoNumaForm_Activity.class));
                Intent intent = new Intent(ctx, Child_NashoNumaForm_Activity.class);
                intent.putExtra("u_id", child_uid);
                intent.putExtra("child_age_month", child_age_months);
                startActivity(intent);
            }
        });

        btn_growth_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // startActivity(new Intent(ctx , Child_NashoNumaGrowthChart_Activity.class));
                Intent intent = new Intent(ctx, Child_NashoNumaGrowthChart_Activity.class);
                intent.putExtra("u_id", child_uid);
                intent.putExtra("child_name", child_name);
                intent.putExtra("child_age", child_age);
                intent.putExtra("child_gender", child_gender);
                startActivity(intent);
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newIntent = new Intent(ctx, Child_NashoNumaFormLive_Activity.class);
                newIntent.putExtra("u_id", child_uid);
                newIntent.putExtra("record_date", mData[position][0]);
                newIntent.putExtra("added_on", mData[position][1]);
                newIntent.putExtra("temp_value", "0");
                startActivity(newIntent);

                Log.d("000777", "record_date" + mData[position][0]);
                Log.d("000777", "added_on" + mData[position][1]);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        hashMapArrayList.clear();

        try {

            Lister ls = new Lister(Child_NashoNumaRecordList_Activity.this);
            ls.createAndOpenDB();

            /*SharedPreferences settings = getSharedPreferences("Khandanuuid", MODE_PRIVATE);
            // Reading from SharedPreferences
            String khandan_uuid_sp = settings.getString("k_uuid", "");
            // Toast.makeText(getApplicationContext(),khandan_uuid_sp,Toast.LENGTH_SHORT).show();*/


            //String[][] data = ls.executeReader("Select* from KHANDAN ");

            mData = ls.executeReader("Select record_data,added_on from CGROWTH where member_uid = '" + child_uid + "' ORDER BY added_on DESC");
            Log.d("child_data", String.valueOf(mData.length));

            HashMap<String, String> map;
            for (int i = 0; i < mData.length; i++) {
                map = new HashMap<>();
                map.put("i", "" + ":" + String.valueOf(i + 1));
                map.put("nasho_numa", "" + mData[i][0]);

                //  map.put("nasho_numa_datetime", "" +"");
                //  map.put("mother_name", "" +"کرن اقبال");
                btn_growth_chart.setVisibility(View.VISIBLE);


                hashMapArrayList.add(map);
            }

            adt = new Adt_ChildNashoNumaRecordList(ctx, hashMapArrayList);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);

        } catch (Exception e) {
            Log.d("12345", "Error: " + e.getMessage());
            Toast tt = Toast.makeText(ctx, "نشو نما کا کوئی ریکارڈ نہیں", Toast.LENGTH_SHORT);
            tt.setGravity(Gravity.CENTER, 0, 0);
            tt.show();
            btn_growth_chart.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //startActivity(new Intent(ctx, Child_Dashboard_Activity.class));
    }
}
