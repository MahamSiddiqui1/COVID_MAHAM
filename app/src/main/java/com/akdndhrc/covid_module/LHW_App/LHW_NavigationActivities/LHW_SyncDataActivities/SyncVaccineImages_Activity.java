package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SyncDataActivities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.akdndhrc.covid_module.R;

import java.util.ArrayList;
import java.util.List;

public class SyncVaccineImages_Activity extends AppCompatActivity {

    Context ctx = SyncVaccineImages_Activity.this;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String mother_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sync_vaccine_images);



//        mother_uid = getIntent().getExtras().getString("u_id");


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }


    private void setupViewPager(ViewPager viewPager) {
        SyncVaccineImages_Activity.ViewPagerAdapter adapter = new SyncVaccineImages_Activity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SyncChildVaccineImg_Fragment(), "بچے کی ویکسین");
        adapter.addFragment(new SyncMotherVaccineImg_Fragment(), "ماں کی ویکسین");
        viewPager.setAdapter(adapter);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
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