package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SocialContactActivities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.R;

import java.util.ArrayList;
import java.util.List;

public class HealthCommittee_Activity extends AppCompatActivity {

    Context ctx = HealthCommittee_Activity.this;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    TextView tvNaam;

    String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_health_committee);

        type = getIntent().getExtras().getString("type");

        tvNaam = findViewById(R.id.tvNaam);

        if (type.equalsIgnoreCase("1")) {
            tvNaam.setText(R.string.womenSupportGroup);
        } else if (type.equalsIgnoreCase("2")) {
            tvNaam.setText(R.string.healthEducationInSchool);
        } else {
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        ImageView iv_home = findViewById(R.id.iv_home);
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(ctx, HomePage_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);

            }
        });

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LastMonth_Fragment(), "(Curr Month) موجودہ مہینہ");
        adapter.addFragment(new UptillNow_Fragment(), "اب تک (Uptill Now)");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}