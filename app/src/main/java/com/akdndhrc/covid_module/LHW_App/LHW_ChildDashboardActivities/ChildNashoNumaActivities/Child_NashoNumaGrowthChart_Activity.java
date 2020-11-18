package com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildNashoNumaActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.R;

import org.json.JSONObject;

import java.text.DecimalFormat;

public class Child_NashoNumaGrowthChart_Activity extends AppCompatActivity {

    Context ctx = Child_NashoNumaGrowthChart_Activity.this;

    RelativeLayout rl_navigation_drawer, rl_home;
    Button btn_wapis_jae;
    ImageView iv_bmi_graph;
    TextView txt_age, txt_naam, txt_bmi_value;
    ImageView iv_navigation_drawer, iv_home, image_gender, iv_editform;
    String child_uid, child_age, child_name, child_gender, record_date, added_on;
    float heightValue;
    float weightValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_nasho_numa_growth_chart);

        child_uid = getIntent().getExtras().getString("u_id");
        child_name = getIntent().getExtras().getString("child_name");
        child_age = getIntent().getExtras().getString("child_age");
        child_gender = getIntent().getExtras().getString("child_gender");

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_editform = findViewById(R.id.iv_editform);

        iv_navigation_drawer.setVisibility(View.GONE);
//        iv_home.setVisibility(View.GONE);
        //ImageView
        iv_bmi_graph = findViewById(R.id.iv_bmi_graph);
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

        //TextView
        txt_naam = findViewById(R.id.txt_naam);
        txt_age = findViewById(R.id.txt_age);
        txt_bmi_value = findViewById(R.id.txt_bmi_value);
        // txt_bmi_value.setText("0");
        txt_naam.setText(child_name);
        txt_age.setText(child_age);


       // calculate_BMI();

        //Button
        btn_wapis_jae = findViewById(R.id.btn_wapis_jae);

        btn_wapis_jae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        iv_editform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(ctx, Child_NashoNumaFormLive_Activity.class);
                newIntent.putExtra("u_id", child_uid);
                newIntent.putExtra("record_date", record_date);
                newIntent.putExtra("added_on", added_on);
                newIntent.putExtra("temp_value", "1");
                startActivity(newIntent);
            }
        });

        iv_bmi_graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Lister ls = new Lister(Child_NashoNumaGrowthChart_Activity.this);
                ls.createAndOpenDB();

                Log.d("test_covered", child_uid);
                String[][] mData = ls.executeReader("Select gender from MEMBER where uid = '" + child_uid + "'");
                if (mData != null) {
                    Log.d("test_covered", mData[0][0] + String.valueOf(mData.length));

                    if (Integer.parseInt(mData[0][0]) == 1) {


                        Intent intent = new Intent(ctx, Child_NashoNumaGrowthLineGraph_Activity_Boy.class);
                        intent.putExtra("u_id", child_uid);
                        intent.putExtra("child_name", child_name);
                        intent.putExtra("child_age", child_age);
                        intent.putExtra("child_gender", child_gender);
                        startActivity(intent);
                    } else if (Integer.parseInt(mData[0][0]) == 0) {


                        Intent intent = new Intent(ctx, Child_NashoNumaGrowthLineGraph_Activity_Girl.class);
                        intent.putExtra("u_id", child_uid);
                        intent.putExtra("child_name", child_name);
                        intent.putExtra("child_age", child_age);
                        intent.putExtra("child_gender", child_gender);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid gender", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("test_covered", "HERE");
                }
            }
        });

    }

    @Override
    protected void onResume() {
        calculate_BMI();
        super.onResume();
    }

    public void calculate_BMI() {

        try {
            Lister ls = new Lister(Child_NashoNumaGrowthChart_Activity.this);
            ls.createAndOpenDB();

            String[][] mData_bmi = ls.executeReader("Select data,record_data,added_on,max(added_on) from CGROWTH where member_uid = '" + child_uid + "'");
            if (mData_bmi[0][0] != null) {
                Log.d("000369", "Data: " + String.valueOf(mData_bmi[0][0]));

                record_date = mData_bmi[0][1];
                added_on = mData_bmi[0][2];

                JSONObject jsonObject = new JSONObject(mData_bmi[0][0]);

                if (jsonObject.has("qad") || jsonObject.has("wazan"))
                    if (jsonObject.getString("qad").isEmpty() || jsonObject.getString("wazan").isEmpty() || jsonObject.getString("inch").isEmpty()) {
                        txt_bmi_value.setText("وزن یا قد نہ ہونے کی وجہ سے بی ام آئی پتہ نہیں کیا جا سکتا.");
                        iv_editform.setVisibility(View.VISIBLE);
                    } else {
                        iv_editform.setVisibility(View.GONE);
                      /*  heightValue = Float.parseFloat(jsonObject.getString("qad").toString()) / 100;
                        Log.d("000369", " Height Value: " + heightValue);

                        weightValue = Float.parseFloat(jsonObject.get("wazan").toString());
                        Log.d("000369", " Weight Value: " + weightValue);

                        float bmi = weightValue / (heightValue * heightValue);*/

                            float inch = Float.parseFloat(jsonObject.getString("inch"));
                            float feet = Float.parseFloat(jsonObject.getString("qad"));
                            float Kg = Float.parseFloat(jsonObject.getString("wazan"));
                            double lb = Kg * (2.2);
                            double inch_final = (feet * 12) + inch;
                          double  BMI_value = (lb / (Math.pow(inch_final, 2))) * 703;

                        txt_bmi_value.setText(new DecimalFormat("##.##").format(BMI_value));
                      // txt_bmi_value.setText(String.format("%.2f", BMI_value));
                        String bmi_value = String.valueOf(BMI_value);
                        Log.d("000369", "BMI VALUE: " + bmi_value);
                    }

            } else {
                Log.d("000369", "Else Null");
            }
        } catch (Exception e) {
            Log.d("000369", "BMI Err: " + e.getMessage());
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
