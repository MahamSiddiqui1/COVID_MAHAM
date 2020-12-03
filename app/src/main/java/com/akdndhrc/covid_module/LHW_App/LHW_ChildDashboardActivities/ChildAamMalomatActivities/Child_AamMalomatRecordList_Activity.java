package com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildAamMalomatActivities;

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

import com.akdndhrc.covid_module.Adapter.Adt_ChildDashboard.Adt_ChildAamMalomatRecordList;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class Child_AamMalomatRecordList_Activity extends AppCompatActivity {

    Context ctx = Child_AamMalomatRecordList_Activity.this;
    TextView txt_age, txt_naam;
    ListView lv;
    Button btn_naya_form_shamil_kre;
    RelativeLayout rl_navigation_drawer, rl_home;
    ImageView iv_navigation_drawer, iv_home, image_gender;

    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    Adt_ChildAamMalomatRecordList adt;
    String child_uid;
    String[][] mData;
    String child_age, child_name, child_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_aam_malomat_record_list);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Child_AamMalomatRecordList_Activity.class));

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
       // iv_home.setVisibility(View.GONE);

        //Image Gender
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

                // startActivity(new Intent(ctx , Child_AamMalomatForm_Activity.class));
                Intent intent = new Intent(ctx, Child_AamMalomatForm_Activity.class);
                intent.putExtra("u_id", child_uid);
                startActivity(intent);
            }
        });




        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newIntent = new Intent(ctx, Child_AamMalomatFormView_Activity.class);
                newIntent.putExtra("u_id", child_uid);
                newIntent.putExtra("record_date", mData[position][0]);
                newIntent.putExtra("added_on", mData[position][1]);

                Log.d("record_date", mData[position][0]);
                startActivity(newIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


        try {

            hashMapArrayList.clear();
            Lister ls = new Lister(Child_AamMalomatRecordList_Activity.this);
           ls.createAndOpenDB();

            try {
                //String[][] data = ls.executeReader("Select* from KHANDAN ");
                mData = ls.executeReader("Select record_data,added_on from CMALOOM where member_uid = '" + child_uid + "' ORDER BY added_on DESC");


                Log.d("child_data", String.valueOf(mData.length));
            } catch (Exception e) {
                Log.d("child_data", String.valueOf(e.getMessage()));
            }

            HashMap<String, String> map;
            for (int i = 0; i < mData.length; i++) {

                map = new HashMap<>();
                map.put("i", "" + ":" + String.valueOf(i + 1));
                map.put("aam_malomat", "" + mData[i][0]);
                map.put("aam_malomat_datetime", "" + "");
                //  map.put("mother_name", "" +"کرن اقبال");

                hashMapArrayList.add(map);
            }
            adt = new Adt_ChildAamMalomatRecordList(ctx, hashMapArrayList);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);


        } catch (Exception e) {
            Log.d("12345", "Error: " + e.getMessage());

            Toast tt  =Toast.makeText(ctx, R.string.aamMaloomatKaKoiRecordNahi, Toast.LENGTH_SHORT);
            tt.setGravity(Gravity.CENTER, 0, 0);
            tt.show();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        // startActivity(new Intent(ctx, Child_Dashboard_Activity.class));
    }
}
