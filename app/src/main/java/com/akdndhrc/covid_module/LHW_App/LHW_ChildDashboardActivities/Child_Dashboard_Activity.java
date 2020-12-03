package com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildAamMalomatActivities.Child_AamMalomatRecordList_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildBemaariRecordActivities.Child_BemaariRecordList_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildNashoNumaActivities.Child_NashoNumaRecordList_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildProfileActivities.Child_Profile_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildRefferalActivities.Child_RefferalRecordList_Activity;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildCVirusScreeingActivities.Child_CVirusRecordList_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildHifazitiTeekeyRecordActivities.Child_HifazitiTeekeyRecordList2_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SearchActivities.Search_FamilyMembersList;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SearchActivities.Search_MemberAndKhandanList_Activity;
import com.akdndhrc.covid_module.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Child_Dashboard_Activity extends AppCompatActivity implements View.OnClickListener {

    Context ctx = Child_Dashboard_Activity.this;
    TextView txt_naam, txt_age;
    RelativeLayout rl_nashonuma_ka_record, rl_beemari_ka_record, rl_hifazati_teekey_ka_record, rl_mawaad, rl_refferal, rl_profile, rl_aam_malomaat, rl_navigation_drawer, rl_home,rl_virus_screeing;
    ImageView iv_navigation_drawer, iv_home, image_gender;
    String child_uid, child_name, child_age, child_gender;
    String child_age_months;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_dashboard);

        child_uid = getIntent().getExtras().getString("u_id");
        child_gender = getIntent().getExtras().getString("child_gender");

        //TextView
        txt_naam = findViewById(R.id.txt_naam);
        txt_age = findViewById(R.id.txt_age);

        calculateAge();

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);
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

        rl_nashonuma_ka_record = findViewById(R.id.rl_nashonuma_ka_record);
        rl_beemari_ka_record = findViewById(R.id.rl_beemari_ka_record);
        rl_hifazati_teekey_ka_record = findViewById(R.id.rl_hifazati_teekey_ka_record);
        rl_mawaad = findViewById(R.id.rl_mawaad);
        rl_refferal = findViewById(R.id.rl_refferal);
        rl_profile = findViewById(R.id.rl_profile);
        rl_aam_malomaat = findViewById(R.id.rl_aam_malomaat);
        rl_virus_screeing = findViewById(R.id.rl_virus_screeing);


        iv_navigation_drawer.setOnClickListener(this);
        iv_home.setOnClickListener(this);
        rl_nashonuma_ka_record.setOnClickListener(this);
        rl_beemari_ka_record.setOnClickListener(this);
        rl_hifazati_teekey_ka_record.setOnClickListener(this);
        rl_mawaad.setOnClickListener(this);
        rl_refferal.setOnClickListener(this);
        rl_profile.setOnClickListener(this);
        rl_aam_malomaat.setOnClickListener(this);
        rl_virus_screeing.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_navigation_drawer:
                Toast.makeText(ctx, R.string.click, Toast.LENGTH_SHORT).show();

                break;

            case R.id.iv_home:
                Intent newIntent = new Intent(ctx, HomePage_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
                break;


            case R.id.rl_nashonuma_ka_record:
                Intent intent = new Intent(ctx, Child_NashoNumaRecordList_Activity.class);
                intent.putExtra("u_id", child_uid);
                intent.putExtra("child_name", child_name);
                intent.putExtra("child_age", child_age);
                intent.putExtra("child_gender", child_gender);
                intent.putExtra("child_age_month", child_age_months);

                startActivity(intent);
                // startActivity(new Intent(ctx, Child_NashoNumaRecordList_Activity.class));

                break;

            case R.id.rl_beemari_ka_record:
                // startActivity(new Intent(ctx, Child_BemaariRecordList_Activity.class));

                Intent intent1 = new Intent(ctx, Child_BemaariRecordList_Activity.class);
                intent1.putExtra("u_id", child_uid);
                intent1.putExtra("child_name", child_name);
                intent1.putExtra("child_age", child_age);
                intent1.putExtra("child_gender", child_gender);
                startActivity(intent1);
                break;

            case R.id.rl_hifazati_teekey_ka_record:
                // startActivity(new Intent(ctx, Child_HifazitiTeekeyRecordListVac_Activity.class));
               Intent intent2 = new Intent(ctx, Child_HifazitiTeekeyRecordList2_Activity.class);
               // Intent intent2 = new Intent(ctx, Child_HifazitiTeekeyGroupList_Activity.class);
                intent2.putExtra("u_id", child_uid);
                intent2.putExtra("child_name", child_name);
                intent2.putExtra("child_age", child_age);
                intent2.putExtra("child_gender", child_gender);
                startActivity(intent2);
                break;

            case R.id.rl_mawaad:
                Toast.makeText(ctx, R.string.cd_mawaad, Toast.LENGTH_SHORT).show();
                break;

            case R.id.rl_refferal:
                // startActivity(new Intent(ctx, Child_RefferalRecordList_Activity.class));
                Intent intent3 = new Intent(ctx, Child_RefferalRecordList_Activity.class);
                intent3.putExtra("u_id", child_uid);
                intent3.putExtra("child_age", child_age);
                intent3.putExtra("child_name", child_name);
                intent3.putExtra("child_gender", child_gender);
                startActivity(intent3);
                break;

            case R.id.rl_profile:
                // startActivity(new Intent(ctx, Child_Profile_Activity.class));
                Intent intent4 = new Intent(ctx, Child_Profile_Activity.class);
                intent4.putExtra("u_id", child_uid);
                intent4.putExtra("child_name", child_name);
                intent4.putExtra("child_age", child_age);
                intent4.putExtra("child_gender", child_gender);
                startActivity(intent4);
                break;

            case R.id.rl_aam_malomaat:
                //startActivity(new Intent(ctx, Child_AamMalomatRecordList_Activity.class));
                Intent intent5 = new Intent(ctx, Child_AamMalomatRecordList_Activity.class);
                intent5.putExtra("u_id", child_uid);
                intent5.putExtra("child_name", child_name);
                intent5.putExtra("child_age", child_age);
                intent5.putExtra("child_gender", child_gender);
                startActivity(intent5);
                break;


                case R.id.rl_virus_screeing:
                Intent intent6 = new Intent(ctx, Child_CVirusRecordList_Activity.class);
                    intent6.putExtra("u_id", child_uid);
                    intent6.putExtra("child_name", child_name);
                    intent6.putExtra("child_age", child_age);
                    intent6.putExtra("child_gender", child_gender);
                    startActivity(intent6);
                break;
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
            txt_age.setText(child_age);
            if (dob_years > 0) {
                days_chart = dob_years * 12;
            }
            dob_months_charts = dob_months + (days_chart);
            child_age_months = String.valueOf(dob_months_charts);
            Log.d("000999", ": " + dob_months_charts);
    }
    catch(Exception e) {
        Log.d("000999", "DOB Error: " + e.getMessage());
    }
       /* try {
            SimpleDateFormat parse = new SimpleDateFormat("yyyy-M-d");
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
            txt_age.setText(child_age);
            if (dob_years > 0) {
                days_chart = dob_years * 12;
            }
            dob_months_charts = dob_months + (days_chart);
            child_age_months = String.valueOf(dob_months_charts);
            Log.d("000999", ": " + dob_months_charts);
        }
        catch(Exception e1)
        {
            Log.d("000999", "CATCH DOB Error: " + e1.getMessage());
        }
    }*/


}

    @Override
    protected void onResume() {

        child_uid = getIntent().getExtras().getString("u_id");
        child_gender = getIntent().getExtras().getString("child_gender");

        calculateAge();
        Log.d("000555", "OnResume: ");
        super.onResume();
    }

    @Override
    public void onBackPressed() {

        if (Search_MemberAndKhandanList_Activity.temp_variable.equalsIgnoreCase("child_dashboard")) {
            Intent intent = new Intent(ctx, Search_MemberAndKhandanList_Activity.class);
            startActivity(intent);
            Search_MemberAndKhandanList_Activity.temp_variable = "0";
        } else if (Search_FamilyMembersList.temp_var.equalsIgnoreCase("child_dash")) {

            Intent intent = new Intent(ctx, Search_MemberAndKhandanList_Activity.class);
            startActivity(intent);
            Search_FamilyMembersList.temp_var = "0";
        } else {
            finish();
          /*  Intent intent = new Intent(ctx, HomePage_Activity.class);
            startActivity(intent);*/
        }

    }
}
