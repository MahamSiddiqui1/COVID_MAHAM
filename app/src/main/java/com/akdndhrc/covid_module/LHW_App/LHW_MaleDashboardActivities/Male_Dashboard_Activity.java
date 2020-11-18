package com.akdndhrc.covid_module.LHW_App.LHW_MaleDashboardActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherAamMalomatActivities.Mother_AamMalomatRecordList_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherBemaariRecordActivities.Mother_BemaariRecordList_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHaamlaRecordActivities.Mother_HaamlaRecordList_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHifazitiTeekeyRecordActivities.Mother_HifazitiTeekeyRecordList_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherKhandaniMansobabandiActivities.Mother_KhandaniMansobabandiList_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherProfileActivities.Mother_Profile_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherRefferalActivities.Mother_RefferalList_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SearchActivities.Search_FamilyMembersList;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SearchActivities.Search_MemberAndKhandanList_Activity;
import com.akdndhrc.covid_module.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Male_Dashboard_Activity extends AppCompatActivity implements View.OnClickListener {

    Context ctx = Male_Dashboard_Activity.this;

    TextView txt_mother_age, txt_mother_naam;
    RelativeLayout rl_haamla_ka_record, rl_khandani_mansobabandi, rl_beemari_ka_record, rl_hifazati_teekey_ka_record, rl_mawaad, rl_refferal, rl_profile, rl_aam_malomaat;
    ImageView iv_navigation_drawer, iv_home;
    String mother_uid, mother_name ,mother_age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_dashboard);

        mother_uid = getIntent().getExtras().getString("u_id");
     //   mother_name = getIntent().getExtras().getString("mother_name");

        //TextView
        txt_mother_age = findViewById(R.id.txt_mother_age);
        txt_mother_naam = findViewById(R.id.txt_mother_naam);



        calculateAge();

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);
        rl_haamla_ka_record = findViewById(R.id.rl_haamla_ka_record);
        rl_khandani_mansobabandi = findViewById(R.id.rl_khandani_mansobabandi);
        rl_beemari_ka_record = findViewById(R.id.rl_beemari_ka_record);
        rl_hifazati_teekey_ka_record = findViewById(R.id.rl_hifazati_teekey_ka_record);
        rl_mawaad = findViewById(R.id.rl_mawaad);
        rl_refferal = findViewById(R.id.rl_refferal);
        rl_profile = findViewById(R.id.rl_profile);
        rl_aam_malomaat = findViewById(R.id.rl_aam_malomaat);


        iv_navigation_drawer.setOnClickListener(this);
        iv_home.setOnClickListener(this);
        rl_haamla_ka_record.setOnClickListener(this);
        rl_khandani_mansobabandi.setOnClickListener(this);
        rl_beemari_ka_record.setOnClickListener(this);
        rl_hifazati_teekey_ka_record.setOnClickListener(this);
        rl_mawaad.setOnClickListener(this);
        rl_refferal.setOnClickListener(this);
        rl_profile.setOnClickListener(this);
        rl_aam_malomaat.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_navigation_drawer:
                Toast.makeText(ctx, "Click", Toast.LENGTH_SHORT).show();

                break;

            case R.id.iv_home:
                Intent newIntent = new Intent(ctx, HomePage_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
                break;


            case R.id.rl_haamla_ka_record:
                // startActivity(new Intent(ctx, Mother_HaamlaRecordList_Activity.class));
                Intent intent = new Intent(ctx, Mother_HaamlaRecordList_Activity.class);
                intent.putExtra("u_id", mother_uid);
                intent.putExtra("mother_name", mother_name);
                intent.putExtra("mother_age", mother_age);
                startActivity(intent);
                break;

            case R.id.rl_khandani_mansobabandi:
                //startActivity(new Intent(ctx, side_effects_List_Activity.class));
                Intent intent1 = new Intent(ctx, Mother_KhandaniMansobabandiList_Activity.class);
                intent1.putExtra("u_id", mother_uid);
                intent1.putExtra("mother_name", mother_name);
                intent1.putExtra("mother_age", mother_age);
                startActivity(intent1);
                break;

            case R.id.rl_beemari_ka_record:
                //startActivity(new Intent(ctx, Mother_BemaariRecordList_Activity.class));
                Intent intent2 = new Intent(ctx, Mother_BemaariRecordList_Activity.class);
                intent2.putExtra("u_id", mother_uid);
                intent2.putExtra("mother_name", mother_name);
                intent2.putExtra("mother_age", mother_age);
                startActivity(intent2);
                break;

            case R.id.rl_hifazati_teekey_ka_record:
                //startActivity(new Intent(ctx, Mother_HifazitiTeekeyRecordList_Activity2.class));
                Intent intent3 = new Intent(ctx, Mother_HifazitiTeekeyRecordList_Activity.class);
                intent3.putExtra("u_id", mother_uid);
                intent3.putExtra("mother_name", mother_name);
                intent3.putExtra("mother_age", mother_age);
                startActivity(intent3);
                break;

            case R.id.rl_mawaad:
                Toast.makeText(ctx, R.string.cd_mawaad, Toast.LENGTH_SHORT).show();
                break;

            case R.id.rl_refferal:
                // startActivity(new Intent(ctx, Mother_RefferalList_Activity.class));
                Intent intent4 = new Intent(ctx, Mother_RefferalList_Activity.class);
                intent4.putExtra("u_id", mother_uid);
                intent4.putExtra("mother_name", mother_name);
                intent4.putExtra("mother_age", mother_age);
                startActivity(intent4);
                break;

            case R.id.rl_profile:
                //  startActivity(new Intent(ctx, Mother_Profile_Activity.class));
                Intent intent5 = new Intent(ctx, Mother_Profile_Activity.class);
                intent5.putExtra("u_id", mother_uid);
                intent5.putExtra("mother_name", mother_name);
                intent5.putExtra("mother_age", mother_age);
                startActivity(intent5);
                break;

            case R.id.rl_aam_malomaat:
                Intent intent6 = new Intent(ctx, Mother_AamMalomatRecordList_Activity.class);
                intent6.putExtra("u_id", mother_uid);
                intent6.putExtra("mother_name", mother_name);
                intent6.putExtra("mother_age", mother_age);
                startActivity(intent6);
                break;
        }
    }

    private void calculateAge() {

        Lister ls = new Lister(ctx);
        ls.createAndOpenDB();

        String mData[][] = ls.executeReader("Select full_name,dob from MEMBER where uid = '" + mother_uid + "'");

        mother_name=mData[0][0];
        txt_mother_naam.setText(mData[0][0]);

        Log.d("000999","DOB: " +mData[0][1]);

        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = parse.parse(mData[0][1]);
            Date n = new Date();
            long dob = d.getTime();
            long now = n.getTime();
            long diff = (now - dob) / 1000;
            long days = diff / 86400;
            int dob_days = 0;
            int dob_weeks = 0;
            int dob_months = 0;
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

            Log.d("000999","Saal: " +String.valueOf(dob_years));
            Log.d("000999","Maah: " +String.valueOf(dob_months));

            mother_age = String.valueOf(dob_years) + " سال " + String.valueOf(dob_months) + " مہ ";
            txt_mother_age.setText(mother_age);
//            Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            Log.d("000999","DOB Error: " +e.getMessage());
        }

    }

    @Override
    protected void onResume() {
        mother_uid = getIntent().getExtras().getString("u_id");
        calculateAge();
        Log.d("000555", "OnResumee" );

        super.onResume();
    }

    @Override
    public void onBackPressed() {

        if (Search_MemberAndKhandanList_Activity.temp_variable.equalsIgnoreCase("mother_dashboard"))
        {
            Intent intent = new Intent(ctx, Search_MemberAndKhandanList_Activity.class);
            startActivity(intent);
            Search_MemberAndKhandanList_Activity.temp_variable="0";
        }
        else if (Search_FamilyMembersList.temp_var.equalsIgnoreCase("mother_dash"))
        {

            Intent intent = new Intent(ctx, Search_MemberAndKhandanList_Activity.class);
            startActivity(intent);
            Search_FamilyMembersList.temp_var="0";
        }
        else
        {
            finish();
            /*Intent intent = new Intent(ctx, HomePage_Activity.class);
            startActivity(intent);*/
        }

    }
}
