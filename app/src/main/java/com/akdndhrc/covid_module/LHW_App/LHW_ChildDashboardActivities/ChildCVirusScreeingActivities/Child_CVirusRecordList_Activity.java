package com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildCVirusScreeingActivities;

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
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.Adt_ChildDashboard.Adt_ChildBemaariRecordList;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.akdndhrc.covid_module.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Child_CVirusRecordList_Activity extends AppCompatActivity {

    Context ctx = Child_CVirusRecordList_Activity.this;
    TextView txt_age, txt_naam;
    ListView lv;
    Button btn_naya_form_shamil_kre;
    

    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    Adt_ChildBemaariRecordList adt;

    ImageView iv_navigation_drawer, iv_home, image_gender;
    String child_uid, child_name, child_age, child_gender, child_age_months;
    String[][] mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_virus_record_list);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Child_CVirusRecordList_Activity.class));

        child_uid = getIntent().getExtras().getString("u_id");
        child_name = getIntent().getExtras().getString("child_name");
        child_age = getIntent().getExtras().getString("child_age");
        child_gender = getIntent().getExtras().getString("child_gender");
       
        
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

        //Button
        btn_naya_form_shamil_kre = findViewById(R.id.btn_naya_form_shamil_kre);


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

                Intent intent = new Intent(ctx, Child_CVirusForm_Activity.class);
                intent.putExtra("u_id", child_uid);
                startActivity(intent);
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newIntent = new Intent(ctx, Child_CVirusFormView_Activity.class);
                newIntent.putExtra("u_id", child_uid);
                newIntent.putExtra("record_date", mData[position][0]);
                newIntent.putExtra("added_on", mData[position][1]);
                startActivity(newIntent);

                Log.d("000987", "record_date: " + mData[position][0]);
                Log.d("000987", "added_on: " + mData[position][1]);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        hashMapArrayList.clear();

        try {

            Lister ls = new Lister(Child_CVirusRecordList_Activity.this);
            ls.createAndOpenDB();

            mData = ls.executeReader("Select record_data,added_on from CVIRUS where member_uid = '" + child_uid + "' AND type IS '0' ORDER BY added_on DESC");
            Log.d("000987", String.valueOf(mData.length));

            HashMap<String, String> map;
     
            for (int i = 0; i < mData.length; i++) {

                map = new HashMap<>();
                map.put("bemaari_record", "" + mData[i][0]);
                map.put("bemaari_record_date", "" + "");
         

                hashMapArrayList.add(map);
            }
            adt = new Adt_ChildBemaariRecordList(ctx, hashMapArrayList);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);

        } catch (Exception e) {
            Log.d("000987", "Error: " + e.getMessage());
            Toast tt = Toast.makeText(ctx, "کوئی ریکارڈ نہیں", Toast.LENGTH_SHORT);
            tt.setGravity(Gravity.CENTER, 0, 0);
            tt.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //startActivity(new Intent(ctx, Child_Dashboard_Activity.class));
    }
}
