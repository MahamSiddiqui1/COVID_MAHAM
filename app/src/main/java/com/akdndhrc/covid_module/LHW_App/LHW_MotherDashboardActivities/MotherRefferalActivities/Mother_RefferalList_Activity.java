package com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherRefferalActivities;

import android.content.Context;
import android.content.Intent;
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

import com.akdndhrc.covid_module.Adapter.Adt_MotherDashboard.Adt_MotherRefferalRecordList;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.akdndhrc.covid_module.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Mother_RefferalList_Activity extends AppCompatActivity {

    Context ctx = Mother_RefferalList_Activity.this;

    TextView txt_mother_age, txt_mother_name;
    ListView lv;
    Button btn_naya_form_shamil_kre;

    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    Adt_MotherRefferalRecordList adt;
    String[][] mData;
    ImageView iv_navigation_drawer, iv_home;

    String mother_uid, mother_name, mother_age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_refferal_list);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Mother_RefferalList_Activity.class));

        mother_uid = getIntent().getExtras().getString("u_id");
        mother_age = getIntent().getExtras().getString("mother_age");
        mother_name = getIntent().getExtras().getString("mother_name");

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
        //iv_home.setVisibility(View.GONE);
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


                Intent intent = new Intent(ctx, Mother_RefferalForm_Activity.class);
                intent.putExtra("u_id", mother_uid);
                startActivity(intent);
                btn_naya_form_shamil_kre.setVisibility(View.GONE);
            }
        });



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newIntent = new Intent(ctx, Mother_RefferalFormView_Activity.class);
                newIntent.putExtra("u_id", mother_uid);
                newIntent.putExtra("record_date", mData[position][0]);
                newIntent.putExtra("added_on", mData[position][1]);
                startActivity(newIntent);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();


        try {

            hashMapArrayList.clear();
            Lister ls = new Lister(Mother_RefferalList_Activity.this);
           ls.createAndOpenDB();

           /* SharedPreferences settings = getSharedPreferences("Khandanuuid", MODE_PRIVATE);
            // Reading from SharedPreferences
            String khandan_uuid_sp = settings.getString("k_uuid", "");
            //  Toast.makeText(getApplicationContext(),khandan_uuid_sp,Toast.LENGTH_SHORT).show();*/

            try {

                //String[][] data = ls.executeReader("Select* from KHANDAN ");
                mData = ls.executeReader("Select record_data,added_on from REFERAL where member_uid = '" + mother_uid + "' ORDER BY added_on DESC");

                Log.d("mother_data", String.valueOf(mData.length));
            } catch (Exception e) {
                Log.d("mother_data", String.valueOf(e.getMessage()));
            }

            HashMap<String, String> map;
            for (int i = 0; i < mData.length; i++) {

                map = new HashMap<>();
                map.put("i", "" + ":" + String.valueOf(i + 1));
                map.put("refferal_txt", "" + mData[i][0]);
                //  map.put("bemaari_record_date", "" +mData[i][1]);
                //  map.put("mother_name", "" +"کرن اقبال");

                hashMapArrayList.add(map);
            }
            adt = new Adt_MotherRefferalRecordList(ctx, hashMapArrayList);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);


        } catch (Exception e) {
            Log.d("12345", "Error: " + e.getMessage());
            Toast tt  =Toast.makeText(ctx, R.string.noRefferalRecord, Toast.LENGTH_SHORT);
            tt.setGravity(Gravity.CENTER, 0, 0);
            tt.show();
        }


    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
        finish();
        // startActivity(new Intent(ctx, Mother_Dashboard_Activity.class));
    }
}
