package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_MonthlyReportActivities;

import android.app.DatePickerDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.akdndhrc.covid_module.Adapter.Adt_ExpMonthlyReport;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.MonthYearPickerDialog;
import com.akdndhrc.covid_module.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class LHW_MonthlyReport_Activity extends AppCompatActivity {

    Adt_ExpMonthlyReport listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();

    EditText et_month_year;
    String selected_year, selected_month, monthYearStr, CurrentYearMonth, TodayDate;

    SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");

    ProgressBar pbProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lhw_monthly_report);


        SimpleDateFormat dates = new SimpleDateFormat("MMMM yyyy");
        Calendar c = Calendar.getInstance();
        CurrentYearMonth = dates.format(c.getTime());
        Log.d("000789", "CurrentYearMonth: " + CurrentYearMonth);

        SimpleDateFormat datess = new SimpleDateFormat("yyyy-MM");
        Calendar cc = Calendar.getInstance();
        TodayDate = datess.format(cc.getTime());
        Log.d("000789", "TodayDate: " + TodayDate);

        selected_year = TodayDate.split("-")[0];
        selected_month = TodayDate.split("-")[1];

        //Exp
        expListView = (ExpandableListView) findViewById(R.id.exp_list);

        //ProgressBar
        pbProgress = (ProgressBar) findViewById(R.id.pbProgress);

        //EditText
        et_month_year = findViewById(R.id.et_month_year);
        et_month_year.setText(CurrentYearMonth);

        et_month_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthYearPickerDialog pickerDialog = new MonthYearPickerDialog();
                pickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
                        monthYearStr = year + "-" + (month + 2) + "-" + i2;
                        et_month_year.setText(formatMonthYear(monthYearStr));

                        if (month <= 8) {
                            selected_month = "0" + String.valueOf(month + 1);
                            Log.d("000789", "IF MONTH: " + String.valueOf(month + 1));
                            Log.d("000789", "IF MONTH: " + selected_month);

                        } else {
                            selected_month = String.valueOf(month + 1);
                            Log.d("000789", "ELSE MONTH: " + String.valueOf(month + 1));
                            Log.d("000789", "ELSE MONTH: " + selected_month);
                        }

                        selected_year = String.valueOf(year);

                        pbProgress.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pbProgress.setVisibility(View.GONE);
                                prepareListData();

                            }
                        }, 2000);

                        Log.d("000789", "YEAR: " + year);
                        Log.d("000789", "MONTH: " + (month + 2));

                    }
                });
                pickerDialog.show(getSupportFragmentManager(), "MonthYearPickerDialog");
            }
        });

        //prepareListData();


//        listAdapter = new Adt_ExpMonthlyReport(this, listDataHeader, listDataChild);
//        expListView.setAdapter(listAdapter);

    }


    private void prepareListData() {

        listDataHeader.clear();
        listDataChild.clear();

        try {
            final Lister ls = new Lister(LHW_MonthlyReport_Activity.this);
            ls.createAndOpenDB();

            // Adding header data
            listDataHeader.add("بنیادی معلومات");
            listDataHeader.add("سماجی رابطے");
            listDataHeader.add("بچے کی صحت");
            listDataHeader.add("ماں کی صحت");
            listDataHeader.add("خاندانی منصوبہ بندی");
            listDataHeader.add("علاج معالجہ");
            listDataHeader.add("پیدائش واموات");
            listDataHeader.add("لیڈی ہیلتھ ورکر کی کٹ");


            /////////////////////////////////// BASIC INFORMATION //////////////////////////////////
            try {

                /////////////////////////// No of Women Registered By LHW   /////////////////////////////
                String data_household_register = "SELECT count(*) from KHANDAN t0" +
                        " INNER JOIN USERS t1 ON t0.added_by= t1.uid" +
                        " where t1.privilege= '1' AND strftime('%Y'-'%m',datetime(t0.added_on/1000, 'unixepoch', 'localtime'))= '" + selected_year + "-" + selected_month + "'";

                String[][] mData_household_registered = ls.executeReader(data_household_register);
                Boolean res = ls.executeNonQuery(data_household_register);
                Log.d("000789", "Query 1: " + data_household_register);
                Log.d("000789", "Boolean 1: " + res.toString());

                Log.d("000789", "HOUSEHOLD REGISTERED BY LHW: " + mData_household_registered[0][0]);

                /////////////////////////// No of Drinking Water Source Facility   /////////////////////////////
                String data_household_with_source_of_drinking_water = "SELECT  COALESCE(SUM(CASE WHEN t0.water_source = \"Not Selected\" OR water_source like '-1%' THEN 1 ELSE 0 END), 0) as not_selected," +
                        " COALESCE(SUM(CASE WHEN t0.water_source = \"Tap (Public Health Engineering)\" OR t0.water_source like '0%' THEN 1 ELSE 0 END), 0) as tap," +
                        " COALESCE(SUM(CASE WHEN t0.water_source = \"Hand Pump / Motor Pump\" OR t0.water_source like '1%' THEN 1 ELSE 0 END), 0) as hand_pump," +
                        " COALESCE(SUM(CASE WHEN t0.water_source = \"Spring / Canal\" OR t0.water_source like '2%' THEN 1 ELSE 0 END), 0) as spring," +
                        " COALESCE(SUM(CASE WHEN t0.water_source = \"Well\" OR t0.water_source like '3%' THEN 1 ELSE 0 END), 0) as well," +
                        " COALESCE(SUM(CASE WHEN t0.water_source = \"Other\" OR t0.water_source = '4%' THEN 1 ELSE 0 END), 0) as other" +
                        " FROM KHANDAN t0" +
                        " INNER JOIN USERS t1 ON t0.added_by= t1.uid" +
                        " where t1.privilege='1' AND strftime(\"%Y-%m\",datetime(t0.added_on/1000, 'unixepoch', 'localtime'))= '" + selected_year + "-" + selected_month + "'";

                String[][] mData_household_with_source_of_drinking_water = ls.executeReader(data_household_with_source_of_drinking_water);
                Boolean ress = ls.executeNonQuery(data_household_register);
                Log.d("000789", "Query 2: " + data_household_register);
                Log.d("000789", "Boorlean 2: " + ress.toString());
                Log.d("000789", "NOT Selected: " + mData_household_with_source_of_drinking_water[0][0]);
                Log.d("000789", "Tap (Public Health Engineering): " + mData_household_with_source_of_drinking_water[0][1]);
                Log.d("000789", "Hand Pump / Motor Pump: " + mData_household_with_source_of_drinking_water[0][2]);
                Log.d("000789", "Spring/Canal: " + mData_household_with_source_of_drinking_water[0][3]);
                Log.d("000789", "Well: " + mData_household_with_source_of_drinking_water[0][4]);
                Log.d("000789", "Other: " + mData_household_with_source_of_drinking_water[0][5]);


                /////////////////////////// No of Toilet Facility   /////////////////////////////
                String data_household_with_toilet_facility = "SELECT" +
                        " COALESCE(SUM(CASE WHEN t0.toilet_facility = \"Flush System with Septic Tank\" OR \"Flush System with Sewerage\" OR t0.toilet_facility like '0%' OR t0.toilet_facility like '1%' THEN 1 ELSE 0 END), 0) as septic_sewer" +
                        " FROM KHANDAN t0" +
                        " INNER JOIN USERS t1 ON t0.added_by= t1.uid" +
                        " where t1.privilege='1' AND strftime(\"%Y-%m\",datetime(t0.added_on/1000, 'unixepoch', 'localtime'))= '" + selected_year + "-" + selected_month + "'";

                String[][] mData_household_with_toilet_facility = ls.executeReader(data_household_with_toilet_facility);
                Boolean resss = ls.executeNonQuery(data_household_with_toilet_facility);
                Log.d("000789", "Query 3: " + data_household_with_toilet_facility);
                Log.d("000789", "Boolean 3: " + resss.toString());

                Log.d("000789", "TOILET FACILITY SWERAGE/TANK: " + mData_household_with_toilet_facility[0][0]);

                int total_drinking_water_source = Integer.parseInt(mData_household_with_source_of_drinking_water[0][0]) + Integer.parseInt(mData_household_with_source_of_drinking_water[0][1]) + Integer.parseInt(mData_household_with_source_of_drinking_water[0][2]) + Integer.parseInt(mData_household_with_source_of_drinking_water[0][3]) + Integer.parseInt(mData_household_with_source_of_drinking_water[0][4]) + Integer.parseInt(mData_household_with_source_of_drinking_water[0][5]);


                // Adding child data
                List<String> basicInformation = new ArrayList<String>();
                basicInformation.add("تشکیل دی گئی صحت کمیٹی کی تعداد" + "@" + "0");
                basicInformation.add("تشکیل دی گئی عورتوں کی کمیٹی کی تعداد" + "@" + "1");
                basicInformation.add("لیڈی ہیلتھ ورکرز کے رجسٹر خاندانوں کی تعداد" + "@" + mData_household_registered[0][0]);
                basicInformation.add("پینے کے پانی کے زیرِ استعمال ذرائع" + "@" + "0");
                basicInformation.add("سرکاری نل" + "@" + mData_household_with_source_of_drinking_water[0][1]);
                basicInformation.add("ہاتھ پمپ/موٹر پمپ" + "@" + mData_household_with_source_of_drinking_water[0][2]);
                basicInformation.add("چشمہ/ نہر" + "@" + mData_household_with_source_of_drinking_water[0][3]);
                basicInformation.add("کنویں" + "@" + mData_household_with_source_of_drinking_water[0][4]);
                basicInformation.add("اس کے علاوہ" + "@" + mData_household_with_source_of_drinking_water[0][5]);
                basicInformation.add("کل" + "@" + total_drinking_water_source);
                //basicInformation.add("Not Selected" + "@" + mData_household_with_source_of_drinking_water[0][0]);
                basicInformation.add("تعداد خاندان جہاں فلش سسٹم بمع سیوریج سینک ٹینک موجود ہے." + "@" + mData_household_with_toilet_facility[0][0]);

                // Header, Child data
                listDataChild.put(listDataHeader.get(0), basicInformation);


            } catch (Exception e) {
                Log.d("000788", "Error 1: " + e.getMessage());
            }


            /////////////////////////////////// SOCIAL CONTACT //////////////////////////////////

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {

                        ////////////////// No Health Committee Meeting  //////////////////////////

                        String mdata_NoHealthCommitteMeeting = "SELECT count(*) from MEETING t0" +
                                " INNER JOIN USERS t1 ON t0.added_by= t1.uid" +
                                " where t0.type= '0' AND  t1.privilege= '1' AND strftime(\"%Y-%m\", t0.record_data)= '" + selected_year + "-" + selected_month + "'";

                        String[][] mData_NoHealthCommitteeMeeting = ls.executeReader(mdata_NoHealthCommitteMeeting);
                        Boolean res = ls.executeNonQuery(mdata_NoHealthCommitteMeeting);
                        Log.d("000789", "Query 4: " + mdata_NoHealthCommitteMeeting);
                        Log.d("000789", "Boolean 4: " + res.toString());

                        Log.d("000789", "No Health Committee Meeting: " + mData_NoHealthCommitteeMeeting[0][0]);


                        ////////////////// No Women Support Group  //////////////////////////
                        String mdata_NoWomenSupportGroup = "SELECT count(*) from MEETING t0" +
                                " INNER JOIN USERS t1 ON t0.added_by= t1.uid" +
                                " where t0.type= '1' AND  t1.privilege= '1' AND strftime(\"%Y-%m\", t0.record_data)= '" + selected_year + "-" + selected_month + "'";

                        String[][] mData_NoWomenSupportGroup = ls.executeReader(mdata_NoWomenSupportGroup);
                        Boolean ress = ls.executeNonQuery(mdata_NoWomenSupportGroup);
                        Log.d("000789", "Query 5: " + mdata_NoWomenSupportGroup);
                        Log.d("000789", "Boolean5 : " + ress.toString());

                        Log.d("000789", "No Women Support Group : " + mData_NoWomenSupportGroup[0][0]);


                        ////////////////// No of Health Education in School  //////////////////////////
                        String mdata_NoHealthEducationSchool = "SELECT count(*) from MEETING t0" +
                                " INNER JOIN USERS t1 ON t0.added_by= t1.uid" +
                                " where t0.type= '2' AND  t1.privilege= '1' AND strftime(\"%Y-%m\", t0.record_data)= '" + selected_year + "-" + selected_month + "'";

                        String[][] mData_NoHealthEducationSchool = ls.executeReader(mdata_NoHealthEducationSchool);
                        Boolean resss = ls.executeNonQuery(mdata_NoHealthEducationSchool);
                        Log.d("000789", "Query 6: " + mdata_NoHealthEducationSchool);
                        Log.d("000789", "Boolean 6: " + resss.toString());

                        Log.d("000789", "No Health Education In School: " + mData_NoHealthEducationSchool[0][0]);

                        List<String> socialContacts = new ArrayList<String>();
                        socialContacts.add(getString(R.string.healthCommitteCount) + "@" + mData_NoHealthCommitteeMeeting[0][0]);
                        socialContacts.add(getString(R.string.womenCommitteCount) + "@" + mData_NoWomenSupportGroup[0][0]);
                        socialContacts.add(getString(R.string.schoolHealthCenterCount) + "@" + mData_NoHealthEducationSchool[0][0]);


                        // Header, Child data
                        listDataChild.put(listDataHeader.get(1), socialContacts);


                    } catch (Exception e) {
                        Log.d("000788", "Error 2: " + e.getMessage());
                    }
                }
            }, 2000);


            /////////////////////////////////// CHILD  HEALTH  //////////////////////////////////

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    try {

                        ////////////////// No New Born Weighted (With in one week)  //////////////////////////

                        String mdata_NoNewBornWeighted = "SELECT count(*) FROM (" +
                                " SELECT" +
                                " c01.member_uid,CAST(julianday(c02.added_on_f) - julianday(t0.dob)as INT) as diff," +
                                " JSON_EXTRACT(c01.data, \"$.wazan\") as wazan" +
                                " FROM CGROWTH as c01" +
                                " INNER JOIN (" +
                                " SELECT member_uid, date(min(record_data)) as added_on_f,added_on" +
                                " FROM CGROWTH" +
                                " GROUP BY member_uid" +
                                " ) as c02 ON c01.member_uid = c02.member_uid AND c01.added_on = c02.added_on" +
                                " INNER JOIN MEMBER as t0 ON c02.member_uid = t0.uid" +
                                " INNER JOIN USERS as t1 ON c01.added_by = t1.uid" +
                                " where t1.privilege= '1' AND strftime(\"%Y-%m\", record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " ) as x WHERE diff BETWEEN 0 AND 7";

                        String[][] mData_NoNewBornWeighted = ls.executeReader(mdata_NoNewBornWeighted);
                        Boolean res = ls.executeNonQuery(mdata_NoNewBornWeighted);
                        Log.d("000789", "Query 7: " + mdata_NoNewBornWeighted);
                        Log.d("000789", "Boolean 7: " + res.toString());

                        Log.d("000789", "No New Born Weighted: " + mData_NoNewBornWeighted[0][0]);


                        ////////////////// No Low Birth Weighted Babies  //////////////////////////
                        String mdata_NoLowBirthWeightedBabies = "SELECT count(*) FROM (" +
                                " SELECT" +
                                " c01.member_uid,CAST(julianday(c02.added_on_f) - julianday(t0.dob)as INT) as diff," +
                                " JSON_EXTRACT(c01.data, \"$.wazan\") as wazan" +
                                " FROM CGROWTH as c01" +
                                " INNER JOIN (" +
                                " SELECT member_uid, date(min(record_data)) as added_on_f," +
                                " added_on" +
                                " FROM CGROWTH" +
                                " GROUP BY member_uid" +
                                " ) as c02 ON c01.member_uid = c02.member_uid AND c01.added_on = c02.added_on" +
                                " INNER JOIN MEMBER as t0 ON c02.member_uid = t0.uid" +
                                " INNER JOIN USERS as t1 ON c01.added_by = t1.uid" +
                                " where t1.privilege= '1' AND strftime(\"%Y-%m\", record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " ) as x WHERE wazan < '2.5' ";

                        String[][] mData_NoLowBirthWeightedBabies = ls.executeReader(mdata_NoLowBirthWeightedBabies);
                        Boolean res1 = ls.executeNonQuery(mdata_NoLowBirthWeightedBabies);
                        Log.d("000789", "Query 8: " + mdata_NoLowBirthWeightedBabies);
                        Log.d("000789", "Boolean 8: " + res1.toString());

                        Log.d("000789", "No Low Birth Weighted: " + mData_NoLowBirthWeightedBabies[0][0]);


                        ////////////////// No New Born Babies Started Breast Feeding Within 24 Hours  //////////////////////////

                        String mdata_NoNewBabiesStartedFeeding = "SELECT count(*) FROM (" +
                                " SELECT" +
                                " c01.member_uid,CAST(julianday(c02.added_on_f) - julianday(t0.dob)as INT) as diff," +
                                " JSON_EXTRACT(c01.data, \"$.ghiza\") as feed" +
                                " FROM CGROWTH as c01" +
                                " INNER JOIN (" +
                                " SELECT member_uid, date(min(record_data)) as added_on_f, added_on," +
                                " added_by" +
                                " FROM CGROWTH" +
                                " GROUP BY member_uid" +
                                " ) as c02 ON c01.member_uid = c02.member_uid AND c01.added_on = c02.added_on" +
                                " INNER JOIN MEMBER as t0 ON c02.member_uid = t0.uid" +
                                " INNER JOIN USERS as t1 ON c01.added_by = t1.uid" +
                                " where t1.privilege= '1' AND strftime(\"%Y-%m\", record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " ) as x WHERE feed = '0' ";

                        String[][] mData_NoNewBabiesStartedFeeding = ls.executeReader(mdata_NoNewBabiesStartedFeeding);
                        Boolean res2 = ls.executeNonQuery(mdata_NoNewBabiesStartedFeeding);
                        Log.d("000789", "Query 9: " + mdata_NoNewBabiesStartedFeeding);
                        Log.d("000789", "Boolean 9: " + res2.toString());

                        Log.d("000789", "No New Babies Started Feeding: " + mData_NoNewBabiesStartedFeeding[0][0]);


                        ////////////////// No of Infants whose Immunization Started This Month  //////////////////////////

                        String mdata_NoInfantsImmunizationStarted = "SELECT count(*) FROM (" +
                                " SELECT" +
                                " member_uid," +
                                " MIN(vaccinated_on) as vaccinated_on," +
                                " added_by" +
                                " FROM CVACCINATION " +
                                " where vaccinated_on IS NOT NULL" +
                                " GROUP BY member_uid" +
                                " ) as t0" +
                                " INNER JOIN USERS as t1 ON t0.added_by = t1.uid" +
                                " WHERE " +
                                " t1.privilege= '1' " +
                                " AND" +
                                " strftime(\"%Y-%m\", vaccinated_on)= '" + selected_year + "-" + selected_month + "'";

                        String[][] mData_NoInfantsImmunizationStarted = ls.executeReader(mdata_NoInfantsImmunizationStarted);
                        Boolean res3 = ls.executeNonQuery(mdata_NoInfantsImmunizationStarted);
                        Log.d("000789", "Query 10: " + mdata_NoInfantsImmunizationStarted);
                        Log.d("000789", "Boolean 10: " + res3.toString());

                        Log.d("000789", "No Infants Immunization Started: " + mData_NoInfantsImmunizationStarted[0][0]);


                        ////////////////// No of 12-23 Months Old Children  //////////////////////////

                        String mdata_No12to23MonthsOldChild = "SELECT count(*) FROM MEMBER t0" +
                                " INNER JOIN USERS as t1 ON t0.added_by = t1.uid" +
                                " where t0.age BETWEEN 1 AND 2" +
                                " AND t1.privilege= '1'" +
                                "AND strftime(\"%Y-%m\",datetime(t0.added_on/1000, 'unixepoch', 'localtime'))= '" + selected_year + "-" + selected_month + "'";


                        String[][] mData_No12to23MonthsOldChild = ls.executeReader(mdata_No12to23MonthsOldChild);
                        Boolean res4 = ls.executeNonQuery(mdata_No12to23MonthsOldChild);
                        Log.d("000789", "Query 11: " + mdata_No12to23MonthsOldChild);
                        Log.d("000789", "Boolean 11: " + res4.toString());

                        Log.d("000789", "No 12to23 Months Old Children: " + mData_No12to23MonthsOldChild[0][0]);

                        ////////////////// No of 12to23 MOnths Old CHild Fully Immunized  //////////////////////////

                        String mdata_No12to23MonthsChildFullyImmunized = "SELECT count(*) FROM (" +
                                " SELECT" +
                                " f02.uid as member_uid," +
                                " count(*) as vaccines" +
                                " FROM CVACCINATION as c02" +
                                " INNER JOIN MEMBER as f02 ON c02.member_uid = f02.uid" +
                                " INNER JOIN USERS as t1 ON c02.added_by = t1.uid" +
                                " where" +
                                " (" +
                                " t1.privilege= '1' " +
                                " AND c02.vaccine_id NOT IN (\"9b843410517a57ea\" , \"d82904394a3c56f3\", \"9419f38b7fad4c75\")) AND c02.vaccinated_on IS NOT NULL" +
                                " AND c02.vaccinated_on <= date()" +
                                " GROUP BY member_uid" +
                                " HAVING vaccines >= 14" +
                                " )" +
                                " as x";

                        String[][] mData_No12to23MonthsChildFullyImmunized = ls.executeReader(mdata_No12to23MonthsChildFullyImmunized);
                        Boolean res5 = ls.executeNonQuery(mdata_No12to23MonthsChildFullyImmunized);
                        Log.d("000789", "Query 12: " + mdata_No12to23MonthsChildFullyImmunized);
                        Log.d("000789", "Boolean 12: " + res5.toString());

                        Log.d("000789", "No12 to 23 Months Child Fully Immunized: " + mData_No12to23MonthsChildFullyImmunized[0][0]);


                        ////////////////// No of Less than 3 Years Children  //////////////////////////

                        String mdata_NoLessThan3YearsChild = "SELECT count(*) FROM MEMBER t0" +
                                " INNER JOIN USERS as t1 ON t0.added_by = t1.uid" +
                                " where t0.age < 3" +
                                " AND t1.privilege= '1'" +
                                "AND strftime(\"%Y-%m\",datetime(t0.added_on/1000, 'unixepoch', 'localtime'))= '" + selected_year + "-" + selected_month + "'";


                        String[][] mData_NoLessThan3YearsChild = ls.executeReader(mdata_NoLessThan3YearsChild);
                        Boolean res6 = ls.executeNonQuery(mdata_NoLessThan3YearsChild);
                        Log.d("000789", "Query 13: " + mdata_NoLessThan3YearsChild);
                        Log.d("000789", "Boolean 13: " + res6.toString());

                        Log.d("000789", "No of Less than 3 Years Childrens: " + mData_NoLessThan3YearsChild[0][0]);


                        ////////////////// No of Less than 3 Years Children WHose GM Done  //////////////////////////

                        String mdata_NoLessThan3YearsChildWithGM = "SELECT  count(*) FROM CGROWTH t0" +
                                " INNER JOIN MEMBER t1 ON t0.member_uid = t1.uid" +
                                " INNER JOIN USERS t2 ON t0.added_by = t2.uid" +
                                " where t1.age < 3" +
                                " AND t2.privilege= '1'" +
                                "AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'";


                        String[][] mData_NoLessThan3YearsChildWithGM = ls.executeReader(mdata_NoLessThan3YearsChildWithGM);
                        Boolean res7 = ls.executeNonQuery(mdata_NoLessThan3YearsChildWithGM);
                        Log.d("000789", "Query 14: " + mdata_NoLessThan3YearsChildWithGM);
                        Log.d("000789", "Boolean 14: " + res7.toString());

                        Log.d("000789", "No of Less than 3 Years Children With GM Done: " + mData_NoLessThan3YearsChildWithGM[0][0]);


                        List<String> childHealth = new ArrayList<String>();
                        childHealth.add(getString(R.string.NoNewBornWeighted) + "@" + mData_NoNewBornWeighted[0][0]);
                        childHealth.add(getString(R.string.NoLowBirthWeightedBabies) + "@" + mData_NoLowBirthWeightedBabies[0][0]);
                        childHealth.add(getString(R.string.NoNewBabiesStartedFeeding) + "@" + mData_NoNewBabiesStartedFeeding[0][0]);
                        childHealth.add(getString(R.string.NoInfantsImmunizationStarted) + "@" + mData_NoInfantsImmunizationStarted[0][0]);
                        childHealth.add(getString(R.string.No12to23MonthsOldChild) + "@" + mData_No12to23MonthsOldChild[0][0]);
                            childHealth.add(getString(R.string.No12to23MonthsChildFullyImmunized) + "@" + mData_No12to23MonthsChildFullyImmunized[0][0]);
                        childHealth.add(getString(R.string.NoLessThan3YearsChild) + "@" + mData_NoLessThan3YearsChild[0][0]);
                        childHealth.add(getString(R.string.NoLessThan3YearsChildWithGM) + "@" + mData_NoLessThan3YearsChildWithGM[0][0]);
                        childHealth.add(getString(R.string.underweightChildUnder3) + "@" + "0");

                        // Header, Child data
                        listDataChild.put(listDataHeader.get(2), childHealth);


                    } catch (Exception e) {
                        Log.d("000788", "Error 3: " + e.getMessage());
                    }

                }
            }, 2000);


            /////////////////////////////////// MOTHER HEALTH //////////////////////////////////

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {

                        ////////////////// No New Newly Registered Pregnant Women //////////////////////////

                        String mdata_NoNewlyRegisteredPregnantWomen = "SELECT count(*) FROM" +
                                " (" +
                                " SELECT datetime(m02.added_on/1000, 'unixepoch', 'localtime') as added_on FROM MPREGNANCY as m02" +
                                " INNER JOIN USERS t0 ON m02.added_by = t0.uid" +
                                " WHERE t0.privilege = '1'" +
                                " ) as x" +
                                " where" +
                                " strftime(\"%Y-%m\",added_on)= '" + selected_year + "-" + selected_month + "'";


                        String[][] mData_NoNewlyRegisteredPregnantWomen = ls.executeReader(mdata_NoNewlyRegisteredPregnantWomen);
                        Boolean res8 = ls.executeNonQuery(mdata_NoNewlyRegisteredPregnantWomen);
                        Log.d("000789", "Query 15: " + mdata_NoNewlyRegisteredPregnantWomen);
                        Log.d("000789", "Boolean 15: " + res8.toString());

                        Log.d("000789", "No Newly Registered Pregnant Women: " + mData_NoNewlyRegisteredPregnantWomen[0][0]);


                        ////////////////// Total Pregnant Women //////////////////////////

                        String mdata_TotalPregnantWomen = "SELECT count(*) FROM" +
                                " (" +
                                " SELECT datetime(m02.added_on/1000, 'unixepoch', 'localtime') as added_on FROM MPREGNANCY as m02" +
                                " INNER JOIN USERS t0 ON m02.added_by = t0.uid" +
                                " WHERE t0.privilege = '1'" +
                                " ) as x" +
                                " where" +
                                " added_on <= date()" +
                                "AND strftime(\"%Y-%m\",added_on)= '" + selected_year + "-" + selected_month + "'";

                        String[][] mData_TotalPregnantWomen = ls.executeReader(mdata_TotalPregnantWomen);
                        Boolean res9 = ls.executeNonQuery(mdata_TotalPregnantWomen);
                        Log.d("000789", "Query 16: " + mdata_TotalPregnantWomen);
                        Log.d("000789", "Boolean 16: " + res9.toString());

                        Log.d("000789", "No Newly Registered Pregnant Women: " + mData_TotalPregnantWomen[0][0]);


                        ////////////////// Total Pregnant Women Visited //////////////////////////

                        String mdata_TotalPregnantWomenVisited = "SELECT count(*) FROM" +
                                " (" +
                                " SELECT member_uid,record_data FROM MANC as m03" +
                                " INNER JOIN USERS t0 ON m03.added_by = t0.uid" +
                                " WHERE t0.privilege = '1'" +
                                " AND date(m03.record_data) <= date()" +
                                " GROUP BY member_uid" +
                                " ) as x" +
                                " where" +
                                " strftime(\"%Y-%m\",record_data)= '" + selected_year + "-" + selected_month + "'";


                        String[][] mData_TotalPregnantWomenVisited = ls.executeReader(mdata_TotalPregnantWomenVisited);
                        Boolean res10 = ls.executeNonQuery(mdata_TotalPregnantWomenVisited);
                        Log.d("000789", "Query 17: " + mdata_TotalPregnantWomenVisited);
                        Log.d("000789", "Boolean 17: " + res10.toString());

                        Log.d("000789", "No Newly Registered Pregnant Women Visited: " + mData_TotalPregnantWomenVisited[0][0]);


                        ////////////////// No of Pregnant Women Supplied Iron Tablet //////////////////////////

                        String mdata_PregnantWomenSupliedIronTablet = "SELECT count(*) FROM" +
                                " (" +
                                " SELECT member_uid,record_data FROM MANC as m03" +
                                " INNER JOIN USERS t0 ON m03.added_by = t0.uid" +
                                " WHERE date(m03.record_data) <= date()" +
                                " AND" +
                                " JSON_EXTRACT(m03.data, \"$.iron_sup\") IS NOT \"\"" +
                                " AND" +
                                " m03.type = '1' AND t0.privilege = '1'" +
                                " GROUP BY member_uid" +
                                " ) as x" +
                                " where" +
                                " strftime(\"%Y-%m\",record_data)= '" + selected_year + "-" + selected_month + "'";


                        String[][] mData_PregnantWomenSupliedIronTablet = ls.executeReader(mdata_PregnantWomenSupliedIronTablet);
                        Boolean res11 = ls.executeNonQuery(mdata_PregnantWomenSupliedIronTablet);
                        Log.d("000789", "Query 18: " + mdata_PregnantWomenSupliedIronTablet);
                        Log.d("000789", "Boolean 18: " + res11.toString());

                        Log.d("000789", "No of Women Suplied Iron Tablet: " + mData_PregnantWomenSupliedIronTablet[0][0]);

                        List<String> motherHealth = new ArrayList<String>();
                        motherHealth.add(getString(R.string.NoNewlyRegisteredPregnantWomen) + "@" + mData_NoNewlyRegisteredPregnantWomen[0][0]);
                        motherHealth.add(getString(R.string.TotalPregnantWomen) + "@" + mData_TotalPregnantWomen[0][0]);
                        motherHealth.add(getString(R.string.TotalPregnantWomenVisited) + "@" + mData_TotalPregnantWomenVisited[0][0]);
                        motherHealth.add(getString(R.string.PregnantWomenSupliedIronTablet) + "@" + mData_PregnantWomenSupliedIronTablet[0][0]);
                        motherHealth.add(getString(R.string.PregBelow7monthWasted) + "@" + "0");
                        motherHealth.add(getString(R.string.pregVisited4orMore) +
                                getString(R.string.healthProf) + "@" + "0");
                        motherHealth.add(getString(R.string.pregVaccForTetanus) + "@" + "0");
                        motherHealth.add(getString(R.string.deliveryByHealthProf) + "@" + "0");

                        // Header, Child data
                        listDataChild.put(listDataHeader.get(3), motherHealth);

                    } catch (Exception e) {
                        Log.d("000788", "Error 4: " + e.getMessage());
                    }
                }
            }, 2000);


            /////////////////////////////////// FAMILY PLANNING //////////////////////////////////

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {

                        ////////////////// No of Eligible Couples (15-49 Years)//////////////////////////

                        String mdata_NoEligibleCouples15to49 = "SELECT count(*) FROM MEMBER as t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE JSON_EXTRACT(t0.data, \"$.maritalstatus\") = 0 AND JSON_EXTRACT(t0.data, \"$.member_type\") = \"abovetwo\"" +
                                " AND" +
                                " t1.privilege = '1'" +
                                " AND" +
                                " strftime(\"%Y-%m\",datetime(t0.added_on/1000, 'unixepoch', 'localtime'))= '" + selected_year + "-" + selected_month + "'" +
                                " ORDER BY t0.added_on DESC";


                        String[][] mData_NoEligibleCouples15to49 = ls.executeReader(mdata_NoEligibleCouples15to49);
                        Boolean res12 = ls.executeNonQuery(mdata_NoEligibleCouples15to49);
                        Log.d("000789", "Query 19: " + mdata_NoEligibleCouples15to49);
                        Log.d("000789", "Boolean 19: " + res12.toString());

                        Log.d("000789", "No Eligible Couples 15to49: " + mData_NoEligibleCouples15to49[0][0]);


                        ////////////////// No of New Client of FamilyPlanning (Modern + Traditional)//////////////////////////
                        String mdata_NoofNewClientFPlanning = "SELECT count(*) FROM" +
                                " (" +
                                " SELECT member_uid,JSON_EXTRACT(m02.data, \"$.plan\") as fp,record_data,added_by FROM MFPLAN as m02" +
                                " INNER JOIN USERS t1 ON m02.added_by = t1.uid" +
                                " GROUP BY member_uid" +
                                " ) as x" +
                                " INNER JOIN USERS t1 ON added_by = t1.uid" +
                                " WHERE" +
                                " t1.privilege = '1'" +
                                " AND" +
                                " strftime(\"%Y-%m\",record_data)= '" + selected_year + "-" + selected_month + "'";


                        String[][] mData_NoofNewClientFPlanning = ls.executeReader(mdata_NoofNewClientFPlanning);
                        Boolean res13 = ls.executeNonQuery(mdata_NoofNewClientFPlanning);
                        Log.d("000789", "Query 20: " + mdata_NoofNewClientFPlanning);
                        Log.d("000789", "Boolean 20: " + res13.toString());

                        Log.d("000789", "No of New Client Family Planning: " + mData_NoofNewClientFPlanning[0][0]);


                        ////////////////// No of FollowUp Cases Family Planning (Modern + Traditional)//////////////////////////

                        String mdata_NoFollowUpCasesForFPlan = "SELECT count(*) FROM" +
                                " (" +
                                " SELECT member_uid,count(*) as fp,record_data, added_by FROM MFPLAN as m02" +
                                " WHERE date(m02.record_data) <= date()" +
                                " GROUP BY member_uid" +
                                " HAVING fp > 1" +
                                " ) as x" +
                                " INNER JOIN USERS t1 ON added_by = t1.uid" +
                                " WHERE" +
                                " t1.privilege = '1'" +
                                " AND" +
                                " strftime(\"%Y-%m\",record_data)= '" + selected_year + "-" + selected_month + "'";


                        String[][] mData_NoFollowUpCasesForFPlan = ls.executeReader(mdata_NoFollowUpCasesForFPlan);
                        Boolean res14 = ls.executeNonQuery(mdata_NoFollowUpCasesForFPlan);
                        Log.d("000789", "Query 21: " + mdata_NoFollowUpCasesForFPlan);
                        Log.d("000789", "Boolean 21: " + res14.toString());

                        Log.d("000789", "No of FollowUp Cases For Family Planning: " + mData_NoFollowUpCasesForFPlan[0][0]);


                        ////////////////// Total No of Modern Contraceptive Method Users//////////////////////////
                        String mdata_NoModernContraceptiveMethodUser = "SELECT count(*) FROM" +
                                " (" +
                                " SELECT member_uid,JSON_EXTRACT(m02.data, \"$.plan\") as fp,record_data,added_by FROM MFPLAN as m02" +
                                " WHERE  date(m02.record_data) <= date()" +
                                " GROUP BY member_uid" +
                                " ) as x" +
                                " INNER JOIN USERS t1 ON added_by = t1.uid" +
                                " WHERE" +
                                " t1.privilege = '1'" +
                                " AND" +
                                " strftime(\"%Y-%m\",record_data)= '" + selected_year + "-" + selected_month + "'";


                        String[][] mData_NoModernContraceptiveMethodUser = ls.executeReader(mdata_NoModernContraceptiveMethodUser);
                        Boolean res15 = ls.executeNonQuery(mdata_NoModernContraceptiveMethodUser);
                        Log.d("000789", "Query 22: " + mdata_NoModernContraceptiveMethodUser);
                        Log.d("000789", "Boolean 22: " + res15.toString());

                        Log.d("000789", "No Modern Contraceptive Method User: " + mData_NoModernContraceptiveMethodUser[0][0]);

                        ////////////////// No of Condom Users//////////////////////////
                        String mdata_NoCondomUsers = "SELECT count(*) FROM" +
                                " (" +
                                " SELECT member_uid,JSON_EXTRACT(m02.data, \"$.plan\") as fp FROM MFPLAN as m02" +
                                " INNER JOIN USERS t1 ON m02.added_by = t1.uid" +
                                " WHERE  strftime(\"%Y-%m\",record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND" +
                                " JSON_EXTRACT(m02.data, \"$.tariqa\") = '1'" +
                                " AND t1.privilege = '1'" +
                                " GROUP BY member_uid" +
                                " ) as x";

                        String[][] mData_NoCondomUsers = ls.executeReader(mdata_NoCondomUsers);
                        Boolean res16 = ls.executeNonQuery(mdata_NoCondomUsers);
                        Log.d("000789", "Query 23: " + mdata_NoCondomUsers);
                        Log.d("000789", "Boolean 23: " + res16.toString());

                        Log.d("000789", "No of Condom Users: " + mData_NoCondomUsers[0][0]);


                        ////////////////// No of Oral Pills Users//////////////////////////
                        String mdata_NoOralPillsUsers = "SELECT count(*) FROM" +
                                " (" +
                                " SELECT member_uid,JSON_EXTRACT(m02.data, \"$.plan\") as fp FROM MFPLAN as m02" +
                                " INNER JOIN USERS t1 ON m02.added_by = t1.uid" +
                                " WHERE  strftime(\"%Y-%m\",record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND" +
                                " JSON_EXTRACT(m02.data, \"$.tariqa\") = '0'" +
                                " AND t1.privilege = '1'" +
                                " GROUP BY member_uid" +
                                " ) as x";

                        String[][] mData_NoOralPillsUsers = ls.executeReader(mdata_NoOralPillsUsers);
                        Boolean res17 = ls.executeNonQuery(mdata_NoOralPillsUsers);
                        Log.d("000789", "Query 24: " + mdata_NoOralPillsUsers);
                        Log.d("000789", "Boolean 24: " + res17.toString());

                        Log.d("000789", "No of Oral Pills Users: " + mData_NoOralPillsUsers[0][0]);


                        ////////////////// No of Injectable Contraceptive Users//////////////////////////
                        String mdata_NoInjectableContraceptiveUsers = "SELECT count(*) FROM" +
                                " (" +
                                " SELECT member_uid,JSON_EXTRACT(m02.data, \"$.plan\") as fp FROM MFPLAN as m02" +
                                " INNER JOIN USERS t1 ON m02.added_by = t1.uid" +
                                " WHERE  strftime(\"%Y-%m\",record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND" +
                                " JSON_EXTRACT(m02.data, \"$.tariqa\") = '4'" +
                                " AND t1.privilege = '1'" +
                                " GROUP BY member_uid" +
                                " ) as x";

                        String[][] mData_NoInjectableContraceptiveUsers = ls.executeReader(mdata_NoInjectableContraceptiveUsers);
                        Boolean res18 = ls.executeNonQuery(mdata_NoInjectableContraceptiveUsers);
                        Log.d("000789", "Query 25: " + mdata_NoInjectableContraceptiveUsers);
                        Log.d("000789", "Boolean 25: " + res18.toString());

                        Log.d("000789", "No of Injectable Contraceptive Users: " + mData_NoInjectableContraceptiveUsers[0][0]);


                        ////////////////// No of IUCD Clients //////////////////////////
                        String mdata_NoIUCDClient = "SELECT count(*) FROM" +
                                " (" +
                                " SELECT member_uid,JSON_EXTRACT(m02.data, \"$.plan\") as fp FROM MFPLAN as m02" +
                                " INNER JOIN USERS t1 ON m02.added_by = t1.uid" +
                                " WHERE  strftime(\"%Y-%m\",record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND" +
                                " JSON_EXTRACT(m02.data, \"$.tariqa\") = '2'" +
                                " AND t1.privilege = '1'" +
                                " GROUP BY member_uid" +
                                " ) as x";

                        String[][] mData_NoIUCDClient = ls.executeReader(mdata_NoIUCDClient);
                        Boolean res19 = ls.executeNonQuery(mdata_NoIUCDClient);
                        Log.d("000789", "Query 26: " + mdata_NoIUCDClient);
                        Log.d("000789", "Boolean 26: " + res19.toString());

                        Log.d("000789", "No IUCD Client: " + mData_NoIUCDClient[0][0]);


                        ////////////////// No of Surgical Clients //////////////////////////
                        String mdata_NoSurgicalClient = "SELECT count(*) FROM" +
                                " (" +
                                " SELECT member_uid,JSON_EXTRACT(m02.data, \"$.plan\") as fp FROM MFPLAN as m02" +
                                " INNER JOIN USERS t1 ON m02.added_by = t1.uid" +
                                " WHERE  strftime(\"%Y-%m\",record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND" +
                                " JSON_EXTRACT(m02.data, \"$.tariqa\") = '3'" +
                                " AND t1.privilege = '1'" +
                                " GROUP BY member_uid" +
                                " ) as x";

                        String[][] mData_NoSurgicalClient = ls.executeReader(mdata_NoSurgicalClient);
                        Boolean res20 = ls.executeNonQuery(mdata_NoSurgicalClient);
                        Log.d("000789", "Query 27: " + mdata_NoSurgicalClient);
                        Log.d("000789", "Boolean 27: " + res20.toString());

                        Log.d("000789", "No Surgical Client: " + mData_NoSurgicalClient[0][0]);

                        ////////////////// No of Other Modern Contraceptive Method User //////////////////////////
                        String mdata_NoOtherModernContMethodUser = "SELECT count(*) FROM" +
                                " (" +
                                " SELECT member_uid,JSON_EXTRACT(m02.data, \"$.plan\") as fp FROM MFPLAN as m02" +
                                " INNER JOIN USERS t1 ON m02.added_by = t1.uid" +
                                " WHERE  strftime(\"%Y-%m\",record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND" +
                                " JSON_EXTRACT(m02.data, \"$.tariqa\") NOT IN ('0', '1', '2', '3', '4')" +
                                " AND t1.privilege = '1'" +
                                " GROUP BY member_uid" +
                                " ) as x";

                        String[][] mData_NoOtherModernContMethodUser = ls.executeReader(mdata_NoOtherModernContMethodUser);
                        Boolean res21 = ls.executeNonQuery(mdata_NoOtherModernContMethodUser);
                        Log.d("000789", "Query 28: " + mdata_NoOtherModernContMethodUser);
                        Log.d("000789", "Boolean 28: " + res21.toString());

                        Log.d("000789", "No Other Modern Cont Method User " + mData_NoOtherModernContMethodUser[0][0]);


                        ////////////////// No of Family Planning Clients Referredr //////////////////////////
                        String mdata_NoFamilyPlanningClientsReferred = "SELECT count(*) FROM" +
                                " (" +
                                " SELECT member_uid,JSON_EXTRACT(m02.data, \"$.plan\") as fp FROM MFPLAN as m02" +
                                " INNER JOIN USERS t1 ON m02.added_by = t1.uid" +
                                " WHERE  strftime(\"%Y-%m\",record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND" +
                                " JSON_EXTRACT(m02.data, \"$.facility_refer\") <> '' OR JSON_EXTRACT(m02.data, \"$.reason_refer\") <> ''" +
                                " AND t1.privilege = '1'" +
                                " GROUP BY member_uid" +
                                " ) as x";

                        String[][] mData_NoFamilyPlanningClientsReferred = ls.executeReader(mdata_NoFamilyPlanningClientsReferred);
                        Boolean res22 = ls.executeNonQuery(mdata_NoFamilyPlanningClientsReferred);
                        Log.d("000789", "Query 29: " + mdata_NoFamilyPlanningClientsReferred);
                        Log.d("000789", "Boolean 29: " + res22.toString());

                        Log.d("000789", "No Family Planning Clients Referred: " + mData_NoFamilyPlanningClientsReferred[0][0]);

                        ////////////////// No of Clients Supplied Condoms //////////////////////////
                        String mdata_NoClientsSuppliedCondom = "SELECT count(*) FROM" +
                                " (" +
                                " SELECT member_uid,JSON_EXTRACT(m02.data, \"$.plan\") as fp FROM MFPLAN as m02" +
                                " INNER JOIN USERS t1 ON m02.added_by = t1.uid" +
                                " WHERE  strftime(\"%Y-%m\",record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND" +
                                " JSON_EXTRACT(m02.data, \"$.tariqa\") = '0' AND JSON_EXTRACT(m02.data, \"$.provided\") = '1'" +
                                " AND t1.privilege = '1'" +
                                " GROUP BY member_uid" +
                                " ) as x";

                        String[][] mData_NoClientsSuppliedCondom = ls.executeReader(mdata_NoClientsSuppliedCondom);
                        Boolean res23 = ls.executeNonQuery(mdata_NoClientsSuppliedCondom);
                        Log.d("000789", "Query 30: " + mdata_NoClientsSuppliedCondom);
                        Log.d("000789", "Boolean 30: " + res23.toString());

                        Log.d("000789", "No of Clients Supplied Condom: " + mData_NoClientsSuppliedCondom[0][0]);


                        ////////////////// No of Clients Supplied Oral Pills //////////////////////////
                        String mdata_NoClientsSuppliedPills = "SELECT count(*) FROM" +
                                " (" +
                                " SELECT member_uid,JSON_EXTRACT(m02.data, \"$.plan\") as fp FROM MFPLAN as m02" +
                                " INNER JOIN USERS t1 ON m02.added_by = t1.uid" +
                                " WHERE  strftime(\"%Y-%m\",record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND" +
                                " JSON_EXTRACT(m02.data, \"$.tariqa\") = '1' AND JSON_EXTRACT(m02.data, \"$.provided\") = '1'" +
                                " AND t1.privilege = '1'" +
                                " GROUP BY member_uid" +
                                " ) as x";

                        String[][] mData_NoClientsSuppliedPills = ls.executeReader(mdata_NoClientsSuppliedPills);
                        Boolean res24 = ls.executeNonQuery(mdata_NoClientsSuppliedPills);
                        Log.d("000789", "Query 31: " + mdata_NoClientsSuppliedPills);
                        Log.d("000789", "Boolean 31: " + res24.toString());

                        Log.d("000789", "No of Clients Supplied Oral Pills: " + mData_NoClientsSuppliedPills[0][0]);


                        ////////////////// No of Clients Administered Injectable Contaceptive //////////////////////////
                        String mdata_NoClientsAdministeredInj = "SELECT count(*) FROM" +
                                " (" +
                                " SELECT member_uid,JSON_EXTRACT(m02.data, \"$.plan\") as fp FROM MFPLAN as m02" +
                                " INNER JOIN USERS t1 ON m02.added_by = t1.uid" +
                                " WHERE  strftime(\"%Y-%m\",record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND" +
                                " JSON_EXTRACT(m02.data, \"$.tariqa\") = '4' AND JSON_EXTRACT(m02.data, \"$.provided\") = '1'" +
                                " AND t1.privilege = '1'" +
                                " GROUP BY member_uid" +
                                " ) as x";

                        String[][] mData_NoClientsAdministeredInj = ls.executeReader(mdata_NoClientsAdministeredInj);
                        Boolean res25 = ls.executeNonQuery(mdata_NoClientsAdministeredInj);
                        Log.d("000789", "Query 32: " + mdata_NoClientsAdministeredInj);
                        Log.d("000789", "Boolean 32: " + res25.toString());

                        Log.d("000789", "No of Clients Administered Injectable: " + mData_NoClientsAdministeredInj[0][0]);

                        List<String> familyPlanningHealth = new ArrayList<String>();
                        familyPlanningHealth.add(getString(R.string.NoEligibleCouples15to49) + "@" + mData_NoEligibleCouples15to49[0][0]);
                        familyPlanningHealth.add(getString(R.string.NoofNewClientFPlanning) + "@" + mData_NoofNewClientFPlanning[0][0]);
                        familyPlanningHealth.add(getString(R.string.NoFollowUpCasesForFPlan) + "@" + mData_NoFollowUpCasesForFPlan[0][0]);
                        familyPlanningHealth.add(getString(R.string.NoModernContraceptiveMethodUser) + "@" + mData_NoModernContraceptiveMethodUser[0][0]);
                        familyPlanningHealth.add(getString(R.string.NoCondomUsers) + "@" + mData_NoCondomUsers[0][0]);
                        familyPlanningHealth.add(getString(R.string.NoOralPillsUsers) + "@" + mData_NoOralPillsUsers[0][0]);
                        familyPlanningHealth.add(getString(R.string.NoInjectableContraceptiveUsers) + "@" + mData_NoInjectableContraceptiveUsers[0][0]);
                        familyPlanningHealth.add(getString(R.string.NoIUCDClient) + "@" + mData_NoIUCDClient[0][0]);
                        familyPlanningHealth.add(getString(R.string.NoSurgicalClient) + "@" + mData_NoSurgicalClient[0][0]);
                        familyPlanningHealth.add(getString(R.string.NoOtherModernContMethodUser) + "@" + mData_NoOtherModernContMethodUser[0][0]);
                        familyPlanningHealth.add(getString(R.string.NoFamilyPlanningClientsReferred) + "@" + mData_NoFamilyPlanningClientsReferred[0][0]);
                        familyPlanningHealth.add(getString(R.string.NoClientsSuppliedCondom) + "@" + mData_NoClientsSuppliedCondom[0][0]);
                        familyPlanningHealth.add(getString(R.string.NoClientsSuppliedPills) + "@" + mData_NoClientsSuppliedPills[0][0]);
                        familyPlanningHealth.add(getString(R.string.NoClientsAdministeredInj) + "@" + mData_NoClientsAdministeredInj[0][0]);


                        // Header, Child data
                        listDataChild.put(listDataHeader.get(4), familyPlanningHealth);


                    } catch (Exception e) {
                        Log.d("000788", "Error 5: " + e.getMessage());
                    }

                }
            }, 2000);


            /////////////////////////////////// COMMON AILMENTS DISEASES //////////////////////////////////

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {

                        ////////////////// No of Case of Diarrhoea //////////////////////////
                        String mdata_NoCaseDiarrhoea = "SELECT" +
                                " (" +
                                " SELECT COUNT(*) FROM CBEMARI t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  JSON_EXTRACT(t0.data,'$.cb_ishaal') IS \"1\" AND t1.privilege= '1' AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " ) as child_ishaal," +
                                " (" +
                                " SELECT COUNT(*) FROM MBEMARI t2" +
                                " INNER JOIN USERS t3 ON t2.added_by = t3.uid" +
                                " WHERE  JSON_EXTRACT(t2.data,'$.cb_ishaal') IS \"1\" AND t3.privilege= '1' AND strftime(\"%Y-%m\",t2.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " ) As mother_ishaal";

                        String[][] mData_NoCaseDiarrhoea = ls.executeReader(mdata_NoCaseDiarrhoea);
                        Boolean res26 = ls.executeNonQuery(mdata_NoCaseDiarrhoea);
                        Log.d("000789", "Query 33: " + mdata_NoCaseDiarrhoea);
                        Log.d("000789", "Boolean 33: " + res26.toString());

                        Log.d("000789", "No of Case of Diarrhoea: " + mData_NoCaseDiarrhoea[0][0]);


                        ////////////////// No of Case of ARI //////////////////////////
                        String mdata_NoCaseARI = "SELECT" +
                                " (" +
                                " SELECT COUNT(*) FROM CBEMARI t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  JSON_EXTRACT(t0.data,'$.cb_khansi_sans') IS \"1\" AND t1.privilege= '1' AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " ) as child," +
                                " (" +
                                " SELECT COUNT(*) FROM MBEMARI t2" +
                                " INNER JOIN USERS t3 ON t2.added_by = t3.uid" +
                                " WHERE  JSON_EXTRACT(t2.data,'$.cb_khansi_sans') IS \"1\" AND t3.privilege= '1' AND strftime(\"%Y-%m\",t2.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " ) As mother";

                        String[][] mData_NoCaseARI = ls.executeReader(mdata_NoCaseARI);
                        Boolean res27 = ls.executeNonQuery(mdata_NoCaseARI);
                        Log.d("000789", "Query 34: " + mdata_NoCaseARI);
                        Log.d("000789", "Boolean 34: " + res27.toString());

                        Log.d("000789", "No of Case of ARI: " + mData_NoCaseARI[0][0]);


                        ////////////////// No of Case of FEVER //////////////////////////
                        String mdata_NoCaseFever = "SELECT" +
                                " (" +
                                " SELECT COUNT(*) FROM CBEMARI t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  JSON_EXTRACT(t0.data,'$.cb_bukhar') IS \"1\" AND t1.privilege= '1' AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " ) as child," +
                                " (" +
                                " SELECT COUNT(*) FROM MBEMARI t2" +
                                " INNER JOIN USERS t3 ON t2.added_by = t3.uid" +
                                " WHERE  JSON_EXTRACT(t2.data,'$.cb_bukhar') IS \"1\" AND t3.privilege= '1' AND strftime(\"%Y-%m\",t2.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " ) As mother";

                        String[][] mData_NoCaseFever = ls.executeReader(mdata_NoCaseFever);
                        Boolean res28 = ls.executeNonQuery(mdata_NoCaseFever);
                        Log.d("000789", "Query 35: " + mdata_NoCaseFever);
                        Log.d("000789", "Boolean 35: " + res28.toString());

                        Log.d("000789", "No of Case of Fever: " + mData_NoCaseFever[0][0]);

                        ////////////////// No of Case of Anemia //////////////////////////
                        String mdata_NoCaseAnemia = "SELECT" +
                                " (" +
                                " SELECT COUNT(*) FROM CBEMARI t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  JSON_EXTRACT(t0.data,'$.cb_khoon_ki_kami') IS \"1\" AND t1.privilege= '1' AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " ) as child," +
                                " (" +
                                " SELECT COUNT(*) FROM MBEMARI t2" +
                                " INNER JOIN USERS t3 ON t2.added_by = t3.uid" +
                                " WHERE  JSON_EXTRACT(t2.data,'$.cb_khoon_ki_kami') IS \"1\" AND t3.privilege= '1' AND strftime(\"%Y-%m\",t2.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " ) As mother";

                        String[][] mData_NoCaseAnemia = ls.executeReader(mdata_NoCaseAnemia);
                        Boolean res29 = ls.executeNonQuery(mdata_NoCaseAnemia);
                        Log.d("000789", "Query 36: " + mdata_NoCaseAnemia);
                        Log.d("000789", "Boolean 36: " + res29.toString());

                        Log.d("000789", "No of Case of Anemia: " + mData_NoCaseAnemia[0][0]);


                        ////////////////// No of Case of Eye Infection //////////////////////////
                        String mdata_NoCaseEyeInfection = "SELECT" +
                                " (" +
                                " SELECT COUNT(*) FROM CBEMARI t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  JSON_EXTRACT(t0.data,'$.cb_ankhon_ki_bemaari') IS \"1\" AND t1.privilege= '1' AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " ) as child," +
                                " (" +
                                " SELECT COUNT(*) FROM MBEMARI t2" +
                                " INNER JOIN USERS t3 ON t2.added_by = t3.uid" +
                                " WHERE  JSON_EXTRACT(t2.data,'$.cb_ankhon_ki_bemaari') IS \"1\" AND t3.privilege= '1' AND strftime(\"%Y-%m\",t2.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " ) As mother";

                        String[][] mData_NoCaseEyeInfection = ls.executeReader(mdata_NoCaseEyeInfection);
                        Boolean res30 = ls.executeNonQuery(mdata_NoCaseEyeInfection);
                        Log.d("000789", "Query 37: " + mdata_NoCaseEyeInfection);
                        Log.d("000789", "Boolean 37: " + res30.toString());

                        Log.d("000789", "No of Case of Eye Infection: " + mData_NoCaseEyeInfection[0][0]);


                        ////////////////// No of Case of Jins Zanana Amraz //////////////////////////
                        String mdata_NoCaseJinsZanana = "SELECT COUNT(*) FROM MBEMARI t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  JSON_EXTRACT(t0.data,'$.cb_jins_zanana_amraz') IS \"1\" AND t1.privilege= '1' AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'";


                        String[][] mData_NoCaseJinsZanana = ls.executeReader(mdata_NoCaseJinsZanana);
                        Boolean res31 = ls.executeNonQuery(mdata_NoCaseJinsZanana);
                        Log.d("000789", "Query 38: " + mdata_NoCaseJinsZanana);
                        Log.d("000789", "Boolean 38: " + res31.toString());

                        Log.d("000789", "No of Case of Jins Zanana Amraz: " + mData_NoCaseJinsZanana[0][0]);


                        ////////////////// No of Case of Worn Infestation //////////////////////////
                        String mdata_NoCaseWormInfestation = "SELECT COUNT(*) FROM CBEMARI t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  JSON_EXTRACT(t0.data,'$.cb_pait_ke_keray') IS \"1\" AND t1.privilege= '1' AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'";

                        String[][] mData_NoCaseWormInfestation = ls.executeReader(mdata_NoCaseWormInfestation);
                        Boolean res32 = ls.executeNonQuery(mdata_NoCaseWormInfestation);
                        Log.d("000789", "Query 39: " + mdata_NoCaseWormInfestation);
                        Log.d("000789", "Boolean 39: " + res32.toString());

                        Log.d("000789", "No of Case of Worm Infestation: " + mData_NoCaseWormInfestation[0][0]);


                        ////////////////// No of Case of Malaira //////////////////////////
                        String mdata_NoCaseMalaria = "SELECT" +
                                " (" +
                                " SELECT COUNT(*) FROM CBEMARI t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  JSON_EXTRACT(t0.data,'$.cb_malaria') IS \"1\" AND t1.privilege= '1' AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " ) as child," +
                                " (" +
                                " SELECT COUNT(*) FROM MBEMARI t2" +
                                " INNER JOIN USERS t3 ON t2.added_by = t3.uid" +
                                " WHERE  JSON_EXTRACT(t2.data,'$.cb_malaria') IS \"1\" AND t3.privilege= '1' AND strftime(\"%Y-%m\",t2.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " ) As mother";

                        String[][] mData_NoCaseMalaria = ls.executeReader(mdata_NoCaseMalaria);
                        Boolean res33 = ls.executeNonQuery(mdata_NoCaseMalaria);
                        Log.d("000789", "Query 40: " + mdata_NoCaseMalaria);
                        Log.d("000789", "Boolean 40: " + res33.toString());

                        Log.d("000789", "No of Case of Malaria: " + mData_NoCaseMalaria[0][0]);


                        ////////////////// No of Case of Suspected TB Referred //////////////////////////
                        String mdata_NoCaseSuspectedTBReferred = "SELECT COUNT(*) FROM MBEMARI t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  JSON_EXTRACT(t0.data,'$.cb_mumkinatb') IS \"1\" AND t1.privilege= '1' AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'";

                        String[][] mData_NoCaseSuspectedTBReferred = ls.executeReader(mdata_NoCaseSuspectedTBReferred);
                        Boolean res34 = ls.executeNonQuery(mdata_NoCaseSuspectedTBReferred);
                        Log.d("000789", "Query 41: " + mdata_NoCaseSuspectedTBReferred);
                        Log.d("000789", "Boolean 41: " + res34.toString());

                        Log.d("000789", "No of Case of Malaria: " + mData_NoCaseSuspectedTBReferred[0][0]);


                        ////////////////// No of Case of Diagnosed TB //////////////////////////
                        String mdata_NoCaseDiagnosedTB = "SELECT COUNT(*) FROM MBEMARI t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  JSON_EXTRACT(t0.data,'$.cb_tashkees') IS \"1\" AND t1.privilege= '1' AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'";

                        String[][] mData_NoCaseDiagnosedTB = ls.executeReader(mdata_NoCaseDiagnosedTB);
                        Boolean res35 = ls.executeNonQuery(mdata_NoCaseDiagnosedTB);
                        Log.d("000789", "Query 42: " + mdata_NoCaseDiagnosedTB);
                        Log.d("000789", "Boolean 42: " + res35.toString());

                        Log.d("000789", "No of Case of Diagnosed TB: " + mData_NoCaseDiagnosedTB[0][0]);


                        ////////////////// No of Case TB Patients Followed by LHW //////////////////////////
                        String mdata_NoCaseTBPatientFollowed = "SELECT COUNT(*) FROM MBEMARI t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  JSON_EXTRACT(t0.data,'$.cb_muawnat') IS \"1\" AND t1.privilege= '1' AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'";

                        String[][] mData_NoCaseTBPatientFollowed = ls.executeReader(mdata_NoCaseTBPatientFollowed);
                        Boolean res36 = ls.executeNonQuery(mdata_NoCaseTBPatientFollowed);
                        Log.d("000789", "Query 43: " + mdata_NoCaseTBPatientFollowed);
                        Log.d("000789", "Boolean 43: " + res36.toString());

                        Log.d("000789", "No of Case of TB Patient Followed: " + mData_NoCaseTBPatientFollowed[0][0]);


                        List<String> commonAilmentsDis = new ArrayList<String>();
                        commonAilmentsDis.add(getString(R.string.NoCaseDiarrhoea) + "@" + mData_NoCaseDiarrhoea[0][0]);
                        commonAilmentsDis.add(getString(R.string.NoCaseARI) + "@" + mData_NoCaseARI[0][0]);
                        commonAilmentsDis.add(getString(R.string.NoCaseFever) + "@" + mData_NoCaseFever[0][0]);
                        commonAilmentsDis.add(getString(R.string.NoCaseAnemia) + "@" + mData_NoCaseAnemia[0][0]);
                        commonAilmentsDis.add(getString(R.string.NoCaseEyeInfection) + "@" + mData_NoCaseEyeInfection[0][0]);
                        commonAilmentsDis.add(getString(R.string.NoCaseJinsZanana) + "@" + mData_NoCaseJinsZanana[0][0]);
                        commonAilmentsDis.add(getString(R.string.NoCaseWormInfestation) + "@" + mData_NoCaseWormInfestation[0][0]);
                        commonAilmentsDis.add(getString(R.string.NoCaseMalaria) + "@" + mData_NoCaseMalaria[0][0]);
                        commonAilmentsDis.add(getString(R.string.NoCaseSuspectedTBReferred) + "@" + mData_NoCaseSuspectedTBReferred[0][0]);
                        commonAilmentsDis.add(getString(R.string.NoCaseDiagnosedTB) + "@" + mData_NoCaseDiagnosedTB[0][0]);
                        commonAilmentsDis.add(getString(R.string.NoCaseTBPatientFollowed) + "@" + mData_NoCaseTBPatientFollowed[0][0]);

                        // Header, Child data
                        listDataChild.put(listDataHeader.get(5), commonAilmentsDis);


                    } catch (Exception e) {
                        Log.d("000788", "Error 6: " + e.getMessage());
                    }
                }
            }, 2000);


            /////////////////////////////////// BIRTHS/DEATHS//////////////////////////////////
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        ////////////////// No of LIve Birth//////////////////////////
                        String mdata_NoCaseofLiveBirth = "SELECT COUNT(*) FROM MPREGNANCY t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE JSON_EXTRACT(t0.metadata,'$.preg_inactive_reason') IS \"Live birth\" AND t1.privilege= '1' AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'";

                        String[][] mData_NoCaseofLiveBirth = ls.executeReader(mdata_NoCaseofLiveBirth);
                        Boolean res37 = ls.executeNonQuery(mdata_NoCaseofLiveBirth);
                        Log.d("000789", "Query 44: " + mdata_NoCaseofLiveBirth);
                        Log.d("000789", "Boolean 44: " + res37.toString());

                        Log.d("000789", "No of Case of Live Birth: " + mData_NoCaseofLiveBirth[0][0]);

                        ////////////////// No of Stilll Births //////////////////////////
                        String mdata_NoCaseofStillBirth = "SELECT count(*) from" +
                                " (" +
                                " SELECT Cast((JulianDay(JSON_EXTRACT(t0.metadata,'$.\"updated_record_date\"')) - JulianDay(t0.lmp)) As Integer) as diff_preg_days FROM MPREGNANCY as t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE JSON_EXTRACT(t0.metadata,'$.preg_inactive_reason') IS \"Still birth\" AND t1.privilege= '1' AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " ) as x where diff_preg_days > 196";

                        String[][] mData_NoCaseofStillBirth = ls.executeReader(mdata_NoCaseofStillBirth);
                        Boolean res43 = ls.executeNonQuery(mdata_NoCaseofStillBirth);
                        Log.d("000789", "Query 45: " + mdata_NoCaseofStillBirth);
                        Log.d("000789", "Boolean 45: " + res43.toString());

                        Log.d("000789", "No of Case of Still Births: " + mData_NoCaseofStillBirth[0][0]);


                        ////////////////// No of All Death//////////////////////////
                        String mdata_NoCaseofAllDeath = "SELECT COUNT(*) FROM MEMBER t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE JSON_EXTRACT(t0.data,'$.status_death') IS \"1\" AND t1.privilege= '1' AND strftime(\"%Y-%m\", JSON_EXTRACT(t0.data,'$.\"record_date\"'))= '" + selected_year + "-" + selected_month + "'";

                        String[][] mData_NoCaseofAllDeath = ls.executeReader(mdata_NoCaseofAllDeath);
                        Boolean res38 = ls.executeNonQuery(mdata_NoCaseofAllDeath);
                        Log.d("000789", "Query 46:" + mdata_NoCaseofAllDeath);
                        Log.d("000789", "Boolean 46: " + res38.toString());

                        Log.d("000789", "No of Case of All Death: " + mData_NoCaseofAllDeath[0][0]);

                        ////////////////// No of Neo-Natal Death //////////////////////////
                        String mdata_NoCaseofNeoNatalDeath = "SELECT COUNT(*) FROM MPREGNANCY t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE JSON_EXTRACT(t0.metadata,'$.preg_inactive_reason') IS \"Neo-Natal death(< 1 week of Birth)\" AND t1.privilege= '1' AND strftime(\"%Y-%m\", t0.record_data)= '" + selected_year + "-" + selected_month + "'";

                        String[][] mData_NoCaseofNeoNatalDeath = ls.executeReader(mdata_NoCaseofNeoNatalDeath);
                        Boolean res39 = ls.executeNonQuery(mdata_NoCaseofNeoNatalDeath);
                        Log.d("000789", "Query 47: " + mdata_NoCaseofNeoNatalDeath);
                        Log.d("000789", "Boolean 47: " + res39.toString());

                        Log.d("000789", "No of Case of Neo-Natal Death: " + mData_NoCaseofNeoNatalDeath[0][0]);


                        ////////////////// No of Infant Death //////////////////////////
                        String mdata_NoCaseofInfantDeath = "SELECT COUNT(*) FROM MPREGNANCY t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE JSON_EXTRACT(t0.metadata,'$.preg_inactive_reason') IS \"Infant death (1+ week but <1 year)\" AND t1.privilege= '1' AND strftime(\"%Y-%m\", t0.record_data)= '" + selected_year + "-" + selected_month + "'";

                        String[][] mData_NoCaseofInfantDeath = ls.executeReader(mdata_NoCaseofInfantDeath);
                        Boolean res40 = ls.executeNonQuery(mdata_NoCaseofInfantDeath);
                        Log.d("000789", "Query 48: " + mdata_NoCaseofInfantDeath);
                        Log.d("000789", "Boolean 48: " + res40.toString());

                        Log.d("000789", "No of Case of Infant Death: " + mData_NoCaseofInfantDeath[0][0]);


                        ////////////////// No of Children Death//////////////////////////
                        String mdata_NoCaseofChildrenDeath = "SELECT COUNT(*) FROM MEMBER t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE JSON_EXTRACT(t0.data,'$.status_death') IS \"1\" AND t0.age > 1 AND t0.age < 5 AND t1.privilege= '1' AND strftime(\"%Y-%m\", JSON_EXTRACT(t0.data,'$.\"record_date\"'))= '" + selected_year + "-" + selected_month + "'";

                        String[][] mData_NoCaseofChildrenDeath = ls.executeReader(mdata_NoCaseofChildrenDeath);
                        Boolean res41 = ls.executeNonQuery(mdata_NoCaseofChildrenDeath);
                        Log.d("000789", "Query 49: " + mdata_NoCaseofChildrenDeath);
                        Log.d("000789", "Boolean 49: " + res41.toString());

                        Log.d("000789", "No of Case of Children Death: " + mData_NoCaseofChildrenDeath[0][0]);


                        ////////////////// No of Maternal Death//////////////////////////
                        String mdata_NoCaseofMaternalDeath = "SELECT COUNT(*) FROM MEMBER t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE JSON_EXTRACT(t0.data,'$.status_death') IS \"1\" AND t0.age > 15 AND t0.gender < 0 AND t1.privilege= '1' AND strftime(\"%Y-%m\", JSON_EXTRACT(t0.data,'$.\"record_date\"'))= '" + selected_year + "-" + selected_month + "'";

                        String[][] mData_NoCaseofMaternalDeath = ls.executeReader(mdata_NoCaseofMaternalDeath);
                        Boolean res42 = ls.executeNonQuery(mdata_NoCaseofMaternalDeath);
                        Log.d("000789", "Query 50: " + mdata_NoCaseofMaternalDeath);
                        Log.d("000789", "Boolean 50: " + res42.toString());

                        Log.d("000789", "No of Case of Children Death: " + mData_NoCaseofMaternalDeath[0][0]);


                        List<String> birthsDeaths = new ArrayList<String>();
                        birthsDeaths.add(getString(R.string.NoCaseofLiveBirth) + "@" + mData_NoCaseofLiveBirth[0][0]);
                        birthsDeaths.add(getString(R.string.NoCaseofStillBirth) + "@" + mData_NoCaseofStillBirth[0][0]);
                        birthsDeaths.add("کل اموات" + "@" + mData_NoCaseofAllDeath[0][0]);
                        birthsDeaths.add(getString(R.string.NoCaseofNeoNatalDeath) + "@" + mData_NoCaseofNeoNatalDeath[0][0]);
                        birthsDeaths.add(getString(R.string.NoCaseofInfantDeath) + "@" + mData_NoCaseofInfantDeath[0][0]);
                        birthsDeaths.add(getString(R.string.NoCaseofChildrenDeath) + "@" + mData_NoCaseofChildrenDeath[0][0]);
                        birthsDeaths.add(getString(R.string.NoPregDeath) + "@" + "0");


                        // Header, Child data
                        listDataChild.put(listDataHeader.get(6), birthsDeaths);


                    } catch (Exception e) {
                        Log.d("000788", "Error 7: " + e.getMessage());
                    }

                }
            }, 2000);


            /////////////////////////////////// LOGISTIC (LHW KIT) //////////////////////////////////
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {

                        List<String> logistic = new ArrayList<String>();

                        ////////////////// Tab. Paracetamol //////////////////////////

                        String mdata_TabParacetamol = "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"56fa8e57b7da0dea273c97d429072a72\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_TabParacetamol = ls.executeReader(mdata_TabParacetamol);
                        Boolean res25 = ls.executeNonQuery(mdata_TabParacetamol);
                        Log.d("000790", "Query TabParacetamol: " + mdata_TabParacetamol);
                        Log.d("000790", "Boolean TabParacetamol: " + res25.toString());

                        Log.d("000790", "TabParacetamol COUNT: " + mData_TabParacetamol[0][4]);
                        Log.d("000790", "TabParacetamol TotalAmount: " + mData_TabParacetamol[0][0]);
                        Log.d("000790", "TabParacetamol BALNCE: " + mData_TabParacetamol[0][1]);
                        Log.d("000790", "TabParacetamol USED_Medicines: " + mData_TabParacetamol[0][2]);
                        Log.d("000790", "TabParacetamol WASTAGE: " + mData_TabParacetamol[0][3]);


                        if (Integer.parseInt(mData_TabParacetamol[0][4]) > 0) {
                            logistic.add("Tab. Paracetamol" + "@" + mData_TabParacetamol[0][1] + "/" + mData_TabParacetamol[0][0] + "@" + mData_TabParacetamol[0][2] + "@" + mData_TabParacetamol[0][3]);
                        }else {
                            logistic.add("Tab. Paracetamol" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }

                        ////////////////// Syp. Paracetamol //////////////////////////

                        String mdata_SypParacetamol= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"4a19b6bbfe7379df6c6257c988d20ab2\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_SypParacetamol = ls.executeReader(mdata_SypParacetamol);
                        Boolean res26 = ls.executeNonQuery(mdata_SypParacetamol);
                        Log.d("000790", "Query SypParacetamol: " + mdata_SypParacetamol);
                        Log.d("000790", "Boolean SypParacetamol: " + res26.toString());

                        Log.d("000790", "SypParacetamol COUNT: " + mData_SypParacetamol[0][4]);
                        Log.d("000790", "SypParacetamol TotalAmount: " + mData_SypParacetamol[0][0]);
                        Log.d("000790", "SypParacetamol BALNCE: " + mData_SypParacetamol[0][1]);
                        Log.d("000790", "SypParacetamol USED_Medicines: " + mData_SypParacetamol[0][2]);
                        Log.d("000790", "SypParacetamol WASTAGE: " + mData_SypParacetamol[0][3]);

                        if (Integer.parseInt(mData_SypParacetamol[0][4]) > 0) {
                            logistic.add("Syp. Paracetamol" + "@" + mData_SypParacetamol[0][1] + "/" + mData_SypParacetamol[0][0] + "@" + mData_SypParacetamol[0][2] + "@" + mData_SypParacetamol[0][3]);
                        }else {
                            logistic.add("Syp. Paracetamol" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }

                        ////////////////// Tab. Choloroquin //////////////////////////

                        String mdata_TabCholoroquin= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"600a2f1f84c075ba5d26ba5556a8cc01\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_TabCholoroquin = ls.executeReader(mdata_TabCholoroquin);
                        Boolean res27 = ls.executeNonQuery(mdata_TabCholoroquin);
                        Log.d("000790", "Query TabCholoroquin: " + mdata_TabCholoroquin);
                        Log.d("000790", "Boolean TabCholoroquin: " + res27.toString());

                        Log.d("000790", "TabCholoroquin COUNT: " + mData_TabCholoroquin[0][4]);
                        Log.d("000790", "TabCholoroquin TotalAmount: " + mData_TabCholoroquin[0][0]);
                        Log.d("000790", "TabCholoroquin BALNCE: " + mData_TabCholoroquin[0][1]);
                        Log.d("000790", "TabCholoroquin USED_Medicines: " + mData_TabCholoroquin[0][2]);
                        Log.d("000790", "TabCholoroquin WASTAGE: " + mData_TabCholoroquin[0][3]);

                        if (Integer.parseInt(mData_TabCholoroquin[0][4]) > 0) {
                            logistic.add("Tab. Choloroquin" + "@" + mData_TabCholoroquin[0][1] + "/" + mData_TabCholoroquin[0][0] + "@" + mData_TabCholoroquin[0][2] + "@" + mData_TabCholoroquin[0][3]);
                        }else {
                            logistic.add("Tab. Choloroquin" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }

                        ////////////////// Syp. Choloroquin //////////////////////////

                        String mdata_SypCholoroquin= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"5cfb0f83509177a0070967319d14867e\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_SypCholoroquin = ls.executeReader(mdata_SypCholoroquin);
                        Boolean res28 = ls.executeNonQuery(mdata_SypCholoroquin);
                        Log.d("000790", "Query SypCholoroquin: " + mdata_SypCholoroquin);
                        Log.d("000790", "Boolean SypCholoroquin: " + res28.toString());

                        Log.d("000790", "SypCholoroquin COUNT: " + mData_SypCholoroquin[0][4]);
                        Log.d("000790", "SypCholoroquin TotalAmount: " + mData_SypCholoroquin[0][0]);
                        Log.d("000790", "SypCholoroquin BALNCE: " + mData_SypCholoroquin[0][1]);
                        Log.d("000790", "SypCholoroquin USED_Medicines: " + mData_SypCholoroquin[0][2]);
                        Log.d("000790", "SypCholoroquin WASTAGE: " + mData_SypCholoroquin[0][3]);


                        if (Integer.parseInt(mData_SypCholoroquin[0][4]) > 0) {
                            logistic.add("Syp. Choloroquin" + "@" + mData_SypCholoroquin[0][1] + "/" + mData_SypCholoroquin[0][0] + "@" + mData_SypCholoroquin[0][2] + "@" + mData_SypCholoroquin[0][3]);
                        }else {
                            logistic.add("Syp. Choloroquin" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }

                        ////////////////// Tab. Mebendazole //////////////////////////

                        String mdata_TabMebendazole= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"bb12dcbf85930098dc9ab6deff801ef5\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_TabMebendazole = ls.executeReader(mdata_TabMebendazole);
                        Boolean res29 = ls.executeNonQuery(mdata_TabMebendazole);
                        Log.d("000790", "Query TabMebendazole: " + mdata_TabMebendazole);
                        Log.d("000790", "Boolean TabMebendazole: " + res29.toString());

                        Log.d("000790", "TabMebendazole COUNT: " + mData_TabMebendazole[0][4]);
                        Log.d("000790", "TabMebendazole TotalAmount: " + mData_TabMebendazole[0][0]);
                        Log.d("000790", "TabMebendazole BALNCE: " + mData_TabMebendazole[0][1]);
                        Log.d("000790", "TabMebendazole USED_Medicines: " + mData_TabMebendazole[0][2]);
                        Log.d("000790", "TabMebendazole WASTAGE: " + mData_TabMebendazole[0][3]);


                        if (Integer.parseInt(mData_TabMebendazole[0][4]) > 0) {
                            logistic.add("Tab. Mebendazole" + "@" + mData_TabMebendazole[0][1] + "/" + mData_TabMebendazole[0][0] + "@" + mData_TabMebendazole[0][2] + "@" + mData_TabMebendazole[0][3]);
                        }else {
                            logistic.add("Tab. Mebendazole" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }

                        ////////////////// Syp. Pipearzine //////////////////////////

                        String mdata_SypPipearzine= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"33581d3117a152845e1c685c651f4847\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_SypPipearzine = ls.executeReader(mdata_SypPipearzine);
                        Boolean res30 = ls.executeNonQuery(mdata_SypPipearzine);
                        Log.d("000790", "Query SypPipearzine: " + mdata_SypPipearzine);
                        Log.d("000790", "Boolean SypPipearzine: " + res30.toString());

                        Log.d("000790", "SypPipearzine COUNT: " + mData_SypPipearzine[0][4]);
                        Log.d("000790", "SypPipearzine TotalAmount: " + mData_SypPipearzine[0][0]);
                        Log.d("000790", "SypPipearzine BALNCE: " + mData_SypPipearzine[0][1]);
                        Log.d("000790", "SypPipearzine USED_Medicines: " + mData_SypPipearzine[0][2]);
                        Log.d("000790", "SypPipearzine WASTAGE: " + mData_SypPipearzine[0][3]);


                        if (Integer.parseInt(mData_SypPipearzine[0][4]) > 0) {
                            logistic.add("Syp. Pipearzine" + "@" + mData_SypPipearzine[0][1] + "/" + mData_SypPipearzine[0][0] + "@" + mData_SypPipearzine[0][2] + "@" + mData_SypPipearzine[0][3]);
                        }else {
                            logistic.add("Syp. Pipearzine" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }


                        ////////////////// ORS //////////////////////////

                        String mdata_ORS= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"db55f21efd3c297287e3712e62cf5317\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_ORS = ls.executeReader(mdata_ORS);
                        Boolean res31 = ls.executeNonQuery(mdata_ORS);
                        Log.d("000790", "Query ORS: " + mdata_ORS);
                        Log.d("000790", "Boolean ORS: " + res31.toString());

                        Log.d("000790", "ORS COUNT: " + mData_ORS[0][4]);
                        Log.d("000790", "ORS TotalAmount: " + mData_ORS[0][0]);
                        Log.d("000790", "ORS BALNCE: " + mData_ORS[0][1]);
                        Log.d("000790", "ORS USED_Medicines: " + mData_ORS[0][2]);
                        Log.d("000790", "ORS WASTAGE: " + mData_ORS[0][3]);


                        if (Integer.parseInt(mData_ORS[0][4]) > 0) {
                            logistic.add("ORS" + "@" + mData_ORS[0][1] + "/" + mData_ORS[0][0] + "@" + mData_ORS[0][2] + "@" + mData_ORS[0][3]);
                        }else {
                            logistic.add("ORS" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }

                        ////////////////// Eye Ontiment //////////////////////////

                        String mdata_EyeOntiment= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"7b6848519b2a4b71e9b7357fdcd3ebc4\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_EyeOntiment = ls.executeReader(mdata_EyeOntiment);
                        Boolean res32 = ls.executeNonQuery(mdata_EyeOntiment);
                        Log.d("000790", "Query EyeOntiment: " + mdata_EyeOntiment);
                        Log.d("000790", "Boolean EyeOntiment: " + res32.toString());

                        Log.d("000790", "EyeOntiment COUNT: " + mData_EyeOntiment[0][4]);
                        Log.d("000790", "EyeOntiment TotalAmount: " + mData_EyeOntiment[0][0]);
                        Log.d("000790", "EyeOntiment BALNCE: " + mData_EyeOntiment[0][1]);
                        Log.d("000790", "EyeOntiment USED_Medicines: " + mData_EyeOntiment[0][2]);
                        Log.d("000790", "EyeOntiment WASTAGE: " + mData_EyeOntiment[0][3]);


                        if (Integer.parseInt(mData_EyeOntiment[0][4]) > 0) {
                            logistic.add("Eye Ontiment" + "@" + mData_EyeOntiment[0][1] + "/" + mData_EyeOntiment[0][0] + "@" + mData_EyeOntiment[0][2] + "@" + mData_EyeOntiment[0][3]);
                        }else {
                            logistic.add("Eye Ontiment" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }


                        ////////////////// Syp. Contrimexazole //////////////////////////

                        String mdata_SypContrimexazole= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"8d51829fb1b7ed937d799fb04ce79116\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_SypContrimexazole = ls.executeReader(mdata_SypContrimexazole);
                        Boolean res33 = ls.executeNonQuery(mdata_SypContrimexazole);
                        Log.d("000790", "Query SypContrimexazole: " + mdata_SypContrimexazole);
                        Log.d("000790", "Boolean SypContrimexazole: " + res33.toString());

                        Log.d("000790", "SypContrimexazole COUNT: " + mData_SypContrimexazole[0][4]);
                        Log.d("000790", "SypContrimexazole TotalAmount: " + mData_SypContrimexazole[0][0]);
                        Log.d("000790", "SypContrimexazole BALNCE: " + mData_SypContrimexazole[0][1]);
                        Log.d("000790", "SypContrimexazole USED_Medicines: " + mData_SypContrimexazole[0][2]);
                        Log.d("000790", "SypContrimexazole WASTAGE: " + mData_SypContrimexazole[0][3]);


                        if (Integer.parseInt(mData_SypContrimexazole[0][4]) > 0) {
                            logistic.add("Syp. Contrimexazole" + "@" + mData_SypContrimexazole[0][1] + "/" + mData_SypContrimexazole[0][0] + "@" + mData_SypContrimexazole[0][2] + "@" + mData_SypContrimexazole[0][3]);
                        }else {
                            logistic.add("Syp. Contrimexazole" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }

                        ////////////////// Iron Tab. //////////////////////////

                        String mdata_IronTab= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"d56941c4af3235305379cfe673da3314\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_IronTab = ls.executeReader(mdata_IronTab);
                        Boolean res34 = ls.executeNonQuery(mdata_IronTab);
                        Log.d("000790", "Query IronTab: " + mdata_IronTab);
                        Log.d("000790", "Boolean IronTab: " + res34.toString());

                        Log.d("000790", "IronTab COUNT: " + mData_IronTab[0][4]);
                        Log.d("000790", "IronTab TotalAmount: " + mData_IronTab[0][0]);
                        Log.d("000790", "IronTab BALNCE: " + mData_IronTab[0][1]);
                        Log.d("000790", "IronTab USED_Medicines: " + mData_IronTab[0][2]);
                        Log.d("000790", "IronTab WASTAGE: " + mData_IronTab[0][3]);


                        if (Integer.parseInt(mData_IronTab[0][4]) > 0) {
                            logistic.add("Iron Tab" + "@" + mData_IronTab[0][1] + "/" + mData_IronTab[0][0] + "@" + mData_IronTab[0][2] + "@" + mData_IronTab[0][3]);
                        }else {
                            logistic.add("Iron Tab" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }


                        ////////////////// Antiseptic Lotion //////////////////////////

                        String mdata_AntisepticLotion= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"44fcb360dda72072b8f3e8da1c7b75f4\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_AntisepticLotion = ls.executeReader(mdata_AntisepticLotion);
                        Boolean res35 = ls.executeNonQuery(mdata_AntisepticLotion);
                        Log.d("000790", "Query AntisepticLotion: " + mdata_AntisepticLotion);
                        Log.d("000790", "Boolean AntisepticLotion: " + res35.toString());

                        Log.d("000790", "AntisepticLotion COUNT: " + mData_AntisepticLotion[0][4]);
                        Log.d("000790", "AntisepticLotion TotalAmount: " + mData_AntisepticLotion[0][0]);
                        Log.d("000790", "AntisepticLotion BALNCE: " + mData_AntisepticLotion[0][1]);
                        Log.d("000790", "AntisepticLotion USED_Medicines: " + mData_AntisepticLotion[0][2]);
                        Log.d("000790", "AntisepticLotion WASTAGE: " + mData_AntisepticLotion[0][3]);


                        if (Integer.parseInt(mData_AntisepticLotion[0][4]) > 0) {
                            logistic.add("Antiseptic Lotion" + "@" + mData_AntisepticLotion[0][1] + "/" + mData_AntisepticLotion[0][0] + "@" + mData_AntisepticLotion[0][2] + "@" + mData_AntisepticLotion[0][3]);
                        }else {
                            logistic.add("Antiseptic Lotion" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }


                        ////////////////// Benzyle Benzoate Lotion //////////////////////////

                        String mdata_BenzyleBenzoateLotion= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"c9c96bba05e51e645cca7cf1990f2353\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_BenzyleBenzoateLotion = ls.executeReader(mdata_BenzyleBenzoateLotion);
                        Boolean res36 = ls.executeNonQuery(mdata_BenzyleBenzoateLotion);
                        Log.d("000790", "Query BenzyleBenzoateLotion: " + mdata_BenzyleBenzoateLotion);
                        Log.d("000790", "Boolean BenzyleBenzoateLotion: " + res36.toString());

                        Log.d("000790", "BenzyleBenzoateLotion COUNT: " + mData_BenzyleBenzoateLotion[0][4]);
                        Log.d("000790", "BenzyleBenzoateLotion TotalAmount: " + mData_BenzyleBenzoateLotion[0][0]);
                        Log.d("000790", "BenzyleBenzoateLotion BALNCE: " + mData_BenzyleBenzoateLotion[0][1]);
                        Log.d("000790", "BenzyleBenzoateLotion USED_Medicines: " + mData_BenzyleBenzoateLotion[0][2]);
                        Log.d("000790", "BenzyleBenzoateLotion WASTAGE: " + mData_BenzyleBenzoateLotion[0][3]);


                        if (Integer.parseInt(mData_BenzyleBenzoateLotion[0][4]) > 0) {
                            logistic.add("Benzyle Benzoate Lotion" + "@" + mData_BenzyleBenzoateLotion[0][1] + "/" + mData_BenzyleBenzoateLotion[0][0] + "@" + mData_BenzyleBenzoateLotion[0][2] + "@" + mData_BenzyleBenzoateLotion[0][3]);
                        }else {
                            logistic.add("Benzyle Benzoate Lotion" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }


                        ////////////////// Sticking Plaster //////////////////////////

                        String mdata_StickingPlaster= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"b9c87099c2e8c6a6a57f0928724d25af\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_StickingPlaster = ls.executeReader(mdata_StickingPlaster);
                        Boolean res37 = ls.executeNonQuery(mdata_StickingPlaster);
                        Log.d("000790", "Query StickingPlaster: " + mdata_StickingPlaster);
                        Log.d("000790", "Boolean StickingPlaster: " + res37.toString());

                        Log.d("000790", "StickingPlaster COUNT: " + mData_StickingPlaster[0][4]);
                        Log.d("000790", "StickingPlaster TotalAmount: " + mData_StickingPlaster[0][0]);
                        Log.d("000790", "StickingPlaster BALNCE: " + mData_StickingPlaster[0][1]);
                        Log.d("000790", "StickingPlaster USED_Medicines: " + mData_StickingPlaster[0][2]);
                        Log.d("000790", "StickingPlaster WASTAGE: " + mData_StickingPlaster[0][3]);


                        if (Integer.parseInt(mData_StickingPlaster[0][4]) > 0) {
                            logistic.add("Sticking Plaster" + "@" + mData_StickingPlaster[0][1] + "/" + mData_StickingPlaster[0][0] + "@" + mData_StickingPlaster[0][2] + "@" + mData_StickingPlaster[0][3]);
                        }else {
                            logistic.add("Sticking Plaster" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }

                            ////////////////// B. Complex Syp //////////////////////////

                        String mdata_BComplexSyp= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"ff515920c5a8ddb0bd79ed82e9203ddd\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_BComplexSyp = ls.executeReader(mdata_BComplexSyp);
                        Boolean res38 = ls.executeNonQuery(mdata_BComplexSyp);
                        Log.d("000790", "Query BComplexSyp: " + mdata_BComplexSyp);
                        Log.d("000790", "Boolean BComplexSyp: " + res38.toString());

                        Log.d("000790", "BComplexSyp COUNT: " + mData_BComplexSyp[0][4]);
                        Log.d("000790", "BComplexSyp TotalAmount: " + mData_BComplexSyp[0][0]);
                        Log.d("000790", "BComplexSyp BALNCE: " + mData_BComplexSyp[0][1]);
                        Log.d("000790", "BComplexSyp USED_Medicines: " + mData_BComplexSyp[0][2]);
                        Log.d("000790", "BComplexSyp WASTAGE: " + mData_BComplexSyp[0][3]);


                        if (Integer.parseInt(mData_BComplexSyp[0][4]) > 0) {
                            logistic.add("B. Complex Syp" + "@" + mData_BComplexSyp[0][1] + "/" + mData_BComplexSyp[0][0] + "@" + mData_BComplexSyp[0][2] + "@" + mData_BComplexSyp[0][3]);
                        }else {
                            logistic.add("B. Complex Syp" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }


                        ////////////////// Cotton Bandages //////////////////////////

                        String mdata_CottonBandages= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"59131c181f9ce7681c82c4c6bcf8b922\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_CottonBandages = ls.executeReader(mdata_CottonBandages);
                        Boolean res39 = ls.executeNonQuery(mdata_CottonBandages);
                        Log.d("000790", "Query CottonBandages: " + mdata_CottonBandages);
                        Log.d("000790", "Boolean CottonBandages: " + res39.toString());

                        Log.d("000790", "CottonBandages COUNT: " + mData_CottonBandages[0][4]);
                        Log.d("000790", "CottonBandages TotalAmount: " + mData_CottonBandages[0][0]);
                        Log.d("000790", "CottonBandages BALNCE: " + mData_CottonBandages[0][1]);
                        Log.d("000790", "CottonBandages USED_Medicines: " + mData_CottonBandages[0][2]);
                        Log.d("000790", "CottonBandages WASTAGE: " + mData_CottonBandages[0][3]);


                        if (Integer.parseInt(mData_CottonBandages[0][4]) > 0) {
                            logistic.add("Cotton Bandages" + "@" + mData_CottonBandages[0][1] + "/" + mData_CottonBandages[0][0] + "@" + mData_CottonBandages[0][2] + "@" + mData_CottonBandages[0][3]);
                        }else {
                            logistic.add("Cotton Bandages" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }

                        ////////////////// Cotton Wool //////////////////////////

                        String mdata_CottonWool= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"8877aa8a77cdc4bc86cf2bfb11836d81\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_CottonWool = ls.executeReader(mdata_CottonWool);
                        Boolean res40 = ls.executeNonQuery(mdata_CottonWool);
                        Log.d("000790", "Query CottonWool: " + mdata_CottonWool);
                        Log.d("000790", "Boolean CottonWool: " + res40.toString());

                        Log.d("000790", "CottonWool COUNT: " + mData_CottonWool[0][4]);
                        Log.d("000790", "CottonWool TotalAmount: " + mData_CottonWool[0][0]);
                        Log.d("000790", "CottonWool BALNCE: " + mData_CottonWool[0][1]);
                        Log.d("000790", "CottonWool USED_Medicines: " + mData_CottonWool[0][2]);
                        Log.d("000790", "CottonWool WASTAGE: " + mData_CottonWool[0][3]);


                        if (Integer.parseInt(mData_CottonWool[0][4]) > 0) {
                            logistic.add("Cotton Wool" + "@" + mData_CottonWool[0][1] + "/" + mData_CottonWool[0][0] + "@" + mData_CottonWool[0][2] + "@" + mData_CottonWool[0][3]);
                        }else {
                            logistic.add("Cotton Wool" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }

                        ////////////////// Condoms //////////////////////////

                        String mdata_Condoms= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"3ac832b9524e6c0496cf449eb30cc8b3\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_Condoms = ls.executeReader(mdata_Condoms);
                        Boolean res41 = ls.executeNonQuery(mdata_Condoms);
                        Log.d("000790", "Query Condoms: " + mdata_Condoms);
                        Log.d("000790", "Boolean Condoms: " + res41.toString());

                        Log.d("000790", "Condoms COUNT: " + mData_Condoms[0][4]);
                        Log.d("000790", "Condoms TotalAmount: " + mData_Condoms[0][0]);
                        Log.d("000790", "Condoms BALNCE: " + mData_Condoms[0][1]);
                        Log.d("000790", "Condoms USED_Medicines: " + mData_Condoms[0][2]);
                        Log.d("000790", "Condoms WASTAGE: " + mData_Condoms[0][3]);


                        if (Integer.parseInt(mData_Condoms[0][4]) > 0) {
                            logistic.add("Condoms" + "@" + mData_Condoms[0][1] + "/" + mData_Condoms[0][0] + "@" + mData_Condoms[0][2] + "@" + mData_Condoms[0][3]);
                        }else {
                            logistic.add("Condoms" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }


                        ////////////////// Oral Pills //////////////////////////

                        String mdata_OralPills= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"fafacbfe1f639599c407c19373438280\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_OralPills = ls.executeReader(mdata_OralPills);
                        Boolean res42 = ls.executeNonQuery(mdata_OralPills);
                        Log.d("000790", "Query OralPills: " + mdata_OralPills);
                        Log.d("000790", "Boolean OralPills: " + res42.toString());

                        Log.d("000790", "OralPills COUNT: " + mData_OralPills[0][4]);
                        Log.d("000790", "OralPills TotalAmount: " + mData_OralPills[0][0]);
                        Log.d("000790", "OralPills BALNCE: " + mData_OralPills[0][1]);
                        Log.d("000790", "OralPills USED_Medicines: " + mData_OralPills[0][2]);
                        Log.d("000790", "OralPills WASTAGE: " + mData_OralPills[0][3]);


                        if (Integer.parseInt(mData_OralPills[0][4]) > 0) {
                            logistic.add("Oral Pills" + "@" + mData_OralPills[0][1] + "/" + mData_OralPills[0][0] + "@" + mData_OralPills[0][2] + "@" + mData_OralPills[0][3]);
                        }else {
                            logistic.add("Oral Pills" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }

                        ////////////////// Contraceptive Inj. //////////////////////////

                        String mdata_ContraceptiveInj= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"cd92b9c2ac55ac1167fdf50285a55d40\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_ContraceptiveInj = ls.executeReader(mdata_ContraceptiveInj);
                        Boolean res43 = ls.executeNonQuery(mdata_ContraceptiveInj);
                        Log.d("000790", "Query ContraceptiveInj: " + mdata_ContraceptiveInj);
                        Log.d("000790", "Boolean ContraceptiveInj: " + res43.toString());

                        Log.d("000790", "ContraceptiveInj COUNT: " + mData_ContraceptiveInj[0][4]);
                        Log.d("000790", "ContraceptiveInj TotalAmount: " + mData_ContraceptiveInj[0][0]);
                        Log.d("000790", "ContraceptiveInj BALNCE: " + mData_ContraceptiveInj[0][1]);
                        Log.d("000790", "ContraceptiveInj USED_Medicines: " + mData_ContraceptiveInj[0][2]);
                        Log.d("000790", "ContraceptiveInj WASTAGE: " + mData_ContraceptiveInj[0][3]);


                        if (Integer.parseInt(mData_ContraceptiveInj[0][4]) > 0) {
                            logistic.add("Contraceptive Inj." + "@" + mData_ContraceptiveInj[0][1] + "/" + mData_ContraceptiveInj[0][0] + "@" + mData_ContraceptiveInj[0][2] + "@" + mData_ContraceptiveInj[0][3]);
                        }else {
                            logistic.add("Contraceptive Inj." + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }


                        ////////////////// Others //////////////////////////

                        String mdata_Others= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"e0ea5765a09f8b481bd081c5936cbb78\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_Others = ls.executeReader(mdata_Others);
                        Boolean res44 = ls.executeNonQuery(mdata_Others);
                        Log.d("000790", "Query Others: " + mdata_Others);
                        Log.d("000790", "Boolean Others: " + res44.toString());

                        Log.d("000790", "Others COUNT: " + mData_Others[0][4]);
                        Log.d("000790", "Others TotalAmount: " + mData_Others[0][0]);
                        Log.d("000790", "Others BALNCE: " + mData_Others[0][1]);
                        Log.d("000790", "Others USED_Medicines: " + mData_Others[0][2]);
                        Log.d("000790", "Others WASTAGE: " + mData_Others[0][3]);


                        if (Integer.parseInt(mData_Others[0][4]) > 0) {
                            logistic.add("Others" + "@" + mData_Others[0][1] + "/" + mData_Others[0][0] + "@" + mData_Others[0][2] + "@" + mData_Others[0][3]);
                        }else {
                            logistic.add("Others" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }


                        ////////////////// LHW Kit Bag //////////////////////////

                        String mdata_LHWKitBag= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"0f60f9cb45f9399f49b00ab99a033217\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_LHWKitBag = ls.executeReader(mdata_LHWKitBag);
                        Boolean res45 = ls.executeNonQuery(mdata_LHWKitBag);
                        Log.d("000790", "Query LHWKitBag: " + mdata_LHWKitBag);
                        Log.d("000790", "Boolean LHWKitBag: " + res45.toString());

                        Log.d("000790", "LHWKitBag COUNT: " + mData_LHWKitBag[0][4]);
                        Log.d("000790", "LHWKitBag TotalAmount: " + mData_LHWKitBag[0][0]);
                        Log.d("000790", "LHWKitBag BALNCE: " + mData_LHWKitBag[0][1]);
                        Log.d("000790", "LHWKitBag USED_Medicines: " + mData_LHWKitBag[0][2]);
                        Log.d("000790", "LHWKitBag WASTAGE: " + mData_LHWKitBag[0][3]);


                        if (Integer.parseInt(mData_LHWKitBag[0][4]) > 0) {
                            logistic.add("LHW Kit Bag" + "@" + mData_LHWKitBag[0][1] + "/" + mData_LHWKitBag[0][0] + "@" + mData_LHWKitBag[0][2] + "@" + mData_LHWKitBag[0][3]);
                        }else {
                            logistic.add("LHW Kit Bag" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }

                        ////////////////// Weighing Machine //////////////////////////

                        String mdata_WeighingMachine= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"57f69f0a5a518cac95e5087577b753e3\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_WeighingMachine = ls.executeReader(mdata_WeighingMachine);
                        Boolean res46 = ls.executeNonQuery(mdata_WeighingMachine);
                        Log.d("000790", "Query WeighingMachine: " + mdata_WeighingMachine);
                        Log.d("000790", "Boolean WeighingMachine: " + res46.toString());

                        Log.d("000790", "WeighingMachine COUNT: " + mData_WeighingMachine[0][4]);
                        Log.d("000790", "WeighingMachine TotalAmount: " + mData_WeighingMachine[0][0]);
                        Log.d("000790", "WeighingMachine BALNCE: " + mData_WeighingMachine[0][1]);
                        Log.d("000790", "WeighingMachine USED_Medicines: " + mData_WeighingMachine[0][2]);
                        Log.d("000790", "WeighingMachine WASTAGE: " + mData_WeighingMachine[0][3]);


                        if (Integer.parseInt(mData_WeighingMachine[0][4]) > 0) {
                            logistic.add("Weighing Machine" + "@" + mData_WeighingMachine[0][1] + "/" + mData_WeighingMachine[0][0] + "@" + mData_WeighingMachine[0][2] + "@" + mData_WeighingMachine[0][3]);
                        }else {
                            logistic.add("Weighing Machine" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }


                        ////////////////// Thermometer //////////////////////////

                        String mdata_Thermometer= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"28a5d4ec5a978bbc19199c84fcd218af\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_Thermometer = ls.executeReader(mdata_Thermometer);
                        Boolean res47 = ls.executeNonQuery(mdata_Thermometer);
                        Log.d("000790", "Query Thermometer: " + mdata_Thermometer);
                        Log.d("000790", "Boolean Thermometer: " + res47.toString());

                        Log.d("000790", "Thermometer COUNT: " + mData_Thermometer[0][4]);
                        Log.d("000790", "Thermometer TotalAmount: " + mData_Thermometer[0][0]);
                        Log.d("000790", "Thermometer BALNCE: " + mData_Thermometer[0][1]);
                        Log.d("000790", "Thermometer USED_Medicines: " + mData_Thermometer[0][2]);
                        Log.d("000790", "Thermometer WASTAGE: " + mData_Thermometer[0][3]);


                        if (Integer.parseInt(mData_Thermometer[0][4]) > 0) {
                            logistic.add("Thermometer" + "@" + mData_Thermometer[0][1] + "/" + mData_Thermometer[0][0] + "@" + mData_Thermometer[0][2] + "@" + mData_Thermometer[0][3]);
                        }else {
                            logistic.add("Thermometer" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }

                        ////////////////// Torch with Cell //////////////////////////

                        String mdata_TorchwithCell= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"0a5b4f372eb4c671e189bda11a696b57\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_TorchwithCell = ls.executeReader(mdata_TorchwithCell);
                        Boolean res48 = ls.executeNonQuery(mdata_TorchwithCell);
                        Log.d("000790", "Query TorchwithCell: " + mdata_TorchwithCell);
                        Log.d("000790", "Boolean TorchwithCell: " + res48.toString());

                        Log.d("000790", "TorchwithCell COUNT: " + mData_TorchwithCell[0][4]);
                        Log.d("000790", "TorchwithCell TotalAmount: " + mData_TorchwithCell[0][0]);
                        Log.d("000790", "TorchwithCell BALNCE: " + mData_TorchwithCell[0][1]);
                        Log.d("000790", "TorchwithCell USED_Medicines: " + mData_TorchwithCell[0][2]);
                        Log.d("000790", "TorchwithCell WASTAGE: " + mData_TorchwithCell[0][3]);


                        if (Integer.parseInt(mData_TorchwithCell[0][4]) > 0) {
                            logistic.add("Torch with Cell" + "@" + mData_TorchwithCell[0][1] + "/" + mData_TorchwithCell[0][0] + "@" + mData_TorchwithCell[0][2] + "@" + mData_TorchwithCell[0][3]);
                        }else {
                            logistic.add("Torch with Cell" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }


                        ////////////////// Scissors //////////////////////////

                        String mdata_Scissors= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"cc59974b387fec2331a1a5af748f638f\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_Scissors = ls.executeReader(mdata_Scissors);
                        Boolean res49 = ls.executeNonQuery(mdata_Scissors);
                        Log.d("000790", "Query Scissors: " + mdata_Scissors);
                        Log.d("000790", "Boolean Scissors: " + res49.toString());

                        Log.d("000790", "Scissors COUNT: " + mData_Scissors[0][4]);
                        Log.d("000790", "Scissors TotalAmount: " + mData_Scissors[0][0]);
                        Log.d("000790", "Scissors BALNCE: " + mData_Scissors[0][1]);
                        Log.d("000790", "Scissors USED_Medicines: " + mData_Scissors[0][2]);
                        Log.d("000790", "Scissors WASTAGE: " + mData_Scissors[0][3]);


                        if (Integer.parseInt(mData_Scissors[0][4]) > 0) {
                            logistic.add("Scissors" + "@" + mData_Scissors[0][1] + "/" + mData_Scissors[0][0] + "@" + mData_Scissors[0][2] + "@" + mData_Scissors[0][3]);
                        }else {
                            logistic.add("Scissors" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }

                        ////////////////// Syringe Cuttur //////////////////////////

                        String mdata_SyringeCuttur= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"2b3d6989de1dbdc2680d6f3607f1668d\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_SyringeCuttur = ls.executeReader(mdata_SyringeCuttur);
                        Boolean res50 = ls.executeNonQuery(mdata_SyringeCuttur);
                        Log.d("000790", "Query SyringeCuttur: " + mdata_SyringeCuttur);
                        Log.d("000790", "Boolean SyringeCuttur: " + res50.toString());

                        Log.d("000790", "SyringeCuttur COUNT: " + mData_SyringeCuttur[0][4]);
                        Log.d("000790", "SyringeCuttur TotalAmount: " + mData_SyringeCuttur[0][0]);
                        Log.d("000790", "SyringeCuttur BALNCE: " + mData_SyringeCuttur[0][1]);
                        Log.d("000790", "SyringeCuttur USED_Medicines: " + mData_SyringeCuttur[0][2]);
                        Log.d("000790", "SyringeCuttur WASTAGE: " + mData_SyringeCuttur[0][3]);


                        if (Integer.parseInt(mData_SyringeCuttur[0][4]) > 0) {
                            logistic.add("Syringe Cuttur" + "@" + mData_SyringeCuttur[0][1] + "/" + mData_SyringeCuttur[0][0] + "@" + mData_SyringeCuttur[0][2] + "@" + mData_SyringeCuttur[0][3]);
                        }else {
                            logistic.add("Syringe Cuttur" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }


                        ////////////////// Others //////////////////////////

                        String mdata_Other= "SELECT t0.balance as totalAmount,(t0.balance - t0.utilized- sum(t0.wastage)) as bal,t0.utilized as usedMedicine,sum(t0.wastage), count(*),max(added_on) from MEDICINE_STOCK t0" +
                                " INNER JOIN USERS t1 ON t0.added_by = t1.uid" +
                                " WHERE  t0.medicine_id =\"5c0a492ec571968db64d61d5b78a141a\" AND strftime(\"%Y-%m\",t0.record_data)= '" + selected_year + "-" + selected_month + "'" +
                                " AND t1.privilege = '1'";


                        String[][] mData_Other = ls.executeReader(mdata_Other);
                        Boolean res51= ls.executeNonQuery(mdata_Other);
                        Log.d("000790", "Query Other: " + mdata_Other);
                        Log.d("000790", "Boolean Other: " + res51.toString());

                        Log.d("000790", "Other COUNT: " + mData_Other[0][4]);
                        Log.d("000790", "Other TotalAmount: " + mData_Other[0][0]);
                        Log.d("000790", "Other BALNCE: " + mData_Other[0][1]);
                        Log.d("000790", "Other USED_Medicines: " + mData_Other[0][2]);
                        Log.d("000790", "Other WASTAGE: " + mData_Other[0][3]);


                        if (Integer.parseInt(mData_Other[0][4]) > 0) {
                            logistic.add("Others" + "@" + mData_Other[0][1] + "/" + mData_Other[0][0] + "@" + mData_Other[0][2] + "@" + mData_Other[0][3]);
                        }else {
                            logistic.add("Others" + "@" + "0"+ "/" + "0" + "@" + "0" + "@" + "0");
                        }

                       /* logistic.add("پیرا سیٹا مول گولیاں" + "@" + "0");
                        logistic.add("پیرا سیٹا مول سیرپ" + "@" + "0");
                        logistic.add("کلوروکین گولیاں" + "@" + "0");
                        logistic.add("کلوروکین سیرپ" + "@" + "0");
                        logistic.add("آنکھ کا مرہم" + "@" + "0");
                        logistic.add("او۔آر۔ایس (نمکول)" + "@" + "0");
                        logistic.add("زنک گولیاں" + "@" + "0");
                        logistic.add("زنک سیرپ" + "@" + "0");
                        logistic.add("ایموکسل سیرپ" + "@" + "0");
                        logistic.add("فولاد کی گولیاں" + "@" + "0");
                        logistic.add("بینزائیل بنزوایٹ لوشن" + "@" + "0");
                        logistic.add("اینٹی سیپٹیک لوشن" + "@" + "0");
                        logistic.add("پلاسٹر" + "@" + "0");
                        logistic.add("روئی کی پٹیاں" + "@" + "0");
                        logistic.add("روئی" + "@" + "0");
                        logistic.add("کنڈوم" + "@" + "0");
                        logistic.add("مانع حمل گولیاں" + "@" + "0");
                        logistic.add("مانع حمل ٹیکے" + "@" + "0");
                        logistic.add("دیگر" + "@" + "0");
                        logistic.add("لیڈی ہیلتھ ورکر کاکٹ بیگ" + "@" + "0");
                        logistic.add("وزن مشین" + "@" + "0");
                        logistic.add("تھر ما میٹر" + "@" + "0");
                        logistic.add("ٹارچ بمہ سیل" + "@" + "0");
                        logistic.add("قینچی" + "@" + "0");
                        logistic.add("مواک ٹیپ" + "@" + "0");
                        logistic.add("دیگر" + "@" + "0");*/


                        // Header, Child data
                        listDataChild.put(listDataHeader.get(7), logistic);


                    } catch (Exception e) {
                        Log.d("000788", "Error 8: " + e.getMessage());
                    }
                }
            }, 2000);

          /*
            List<String> maternal_health = new ArrayList<String>();
            maternal_health.add("No. of newly registered pregnant Women" + "@" + childhealth[0][15]);
            maternal_health.add("Total Pregnant Women (New+ Follow up)" + "@" + childhealth[0][16]);
            maternal_health.add("Total Pregnant Women visited (New + Followup)" + "@" + childhealth[0][17]);
            maternal_health.add("No. of Pregnant women supplied Iron tablets" + "@" + childhealth[0][18]);
            maternal_health.add("No. of Abortions (pregnancy less Than 7 Months)" + "@" + childhealth[0][19]);
            maternal_health.add("No of Women Delivered having 4 or >4 ANC Visits by SBAs (Dr, Nurse, LHV, Midwife/CMW)" + "@" + childhealth[0][20]);
            maternal_health.add("No of Women Delivered With TT completed Before Delivery" + "@" + childhealth[0][21]);
            maternal_health.add("No Of Deliveries by Skilled Birth Attendants (Dr, Nurse, LHV, Midwife/CMW)" + "@" + childhealth[0][22]);
            maternal_health.add("No. of Post-natal Cases Visited within 24 Hours" + "@" + "0");

            List<String> family_planning = new ArrayList<String>();
            family_planning.add("No of Eligible Couples (Age of Wife between 15-49 Years)" + "@" + childhealth[0][23]);
            family_planning.add("No of New Client of Family Planning (Modern + Traditional)" + "@" + childhealth[0][24]);
            family_planning.add("No of Follow-up Cases for Family Planning (Modern + Traditional)" + "@" + childhealth[0][25]);
            family_planning.add("Total No of Modern Contraceptive Method Users" + "@" + childhealth[0][26]);
            family_planning.add("No of Condom Users" + "@" + childhealth[0][27]);
            family_planning.add("No of Oral Pills Users" + "@" + childhealth[0][28]);
            family_planning.add("No of Injectable Contraceptive Users" + "@" + childhealth[0][29]);
            family_planning.add("No of IUCD Client" + "@" + childhealth[0][30]);
            family_planning.add("No of Surgical Clients" + "@" + childhealth[0][31]);
            family_planning.add("No of Other Modern Contraceptive Method Users" + "@" + childhealth[0][32]);
            family_planning.add("Total No of Traditional Method Users" + "@" + childhealth[0][33]);
            family_planning.add("No of Family Planning Clients referred" + "@" + childhealth[0][34]);
            family_planning.add("No of Clients Supplied condoms" + "@" + childhealth[0][35]);
            family_planning.add("No of Clients Supplied Oral pills" + "@" + childhealth[0][36]);
            family_planning.add("No of Clients administered Injectable Contraceptives" + "@" + childhealth[0][37]);

            List<String> common_aliments_a5 = new ArrayList<String>();
            common_aliments_a5.add("No of Cases of Diarrhoea" + "@" + childhealth[0][38]);
            common_aliments_a5.add("No of Cases of ARI" + "@" + childhealth[0][40]);
            common_aliments_a5.add("No of Cases of Fever" + "@" + childhealth[0][42]);
            common_aliments_a5.add("No of Cases of Injuries/burns" + "@" + childhealth[0][44]);
            common_aliments_a5.add("No of Cases of Anaemia" + "@" + childhealth[0][46]);
            common_aliments_a5.add("No of Cases of Scabies" + "@" + childhealth[0][48]);
            common_aliments_a5.add("No of Cases of Eye Infections" + "@" + childhealth[0][50]);
            common_aliments_a5.add("No of Female Cases of reproductive tract infections" + "@" + childhealth[0][52]);
            common_aliments_a5.add("No of Cases of Worm Infestation" + "@" + childhealth[0][54]);
            common_aliments_a5.add("No of Cases of Malaria" + "@" + childhealth[0][56]);
            common_aliments_a5.add("No of Referral Cases" + "@" + childhealth[0][58]);
            common_aliments_a5.add("No of of Suspected Cases of TB Referred" + "@" + childhealth[0][60]);
            common_aliments_a5.add("No of Diagnosed Cases of TB" + "@" + childhealth[0][62]);
            common_aliments_a5.add("No of TB Patients followed by LHW (as T/M Support)" + "@" + childhealth[0][64]);

            List<String> common_aliments_u5 = new ArrayList<String>();
            common_aliments_u5.add("No of Cases of Diarrhoea" + "@" + childhealth[0][39]);
            common_aliments_u5.add("No of Cases of ARI" + "@" + childhealth[0][41]);
            common_aliments_u5.add("No of Cases of Fever" + "@" + childhealth[0][43]);
            common_aliments_u5.add("No of Cases of Injuries/burns" + "@" + childhealth[0][45]);
            common_aliments_u5.add("No of Cases of Anaemia" + "@" + childhealth[0][47]);
            common_aliments_u5.add("No of Cases of Scabies" + "@" + childhealth[0][49]);
            common_aliments_u5.add("No of Cases of Eye Infections" + "@" + childhealth[0][51]);
            common_aliments_u5.add("No of Female Cases of reproductive tract infections" + "@" + childhealth[0][53]);
            common_aliments_u5.add("No of Cases of Worm Infestation" + "@" + childhealth[0][55]);
            common_aliments_u5.add("No of Cases of Malaria" + "@" + childhealth[0][57]);
            common_aliments_u5.add("No of Referral Cases" + "@" + childhealth[0][59]);
            common_aliments_u5.add("No of of Suspected Cases of TB Referred" + "@" + childhealth[0][61]);
            common_aliments_u5.add("No of Diagnosed Cases of TB" + "@" + childhealth[0][63]);
            common_aliments_u5.add("No of TB Patients followed by LHW (as T/M Support)" + "@" + childhealth[0][65]);

            List<String> births_death = new ArrayList<String>();
            births_death.add("No of Live Births" + "@" + childhealth[0][66]);
            births_death.add("No of Still Births (Pregnancy More than 07 Months)" + "@" + childhealth[0][67]);
            births_death.add("No of All Deaths" + "@" + childhealth[0][68]);
            births_death.add("No of neo-natal Deaths (Within 1 Week of Birth)" + "@" + childhealth[0][69]);
            births_death.add("No of Infant Deaths (Age More than 1 Week but Less than 1 Year)" + "@" + childhealth[0][70]);
            births_death.add("No of Children Deaths (Age More than 1 Year but Less than 05 Years)" + "@" + childhealth[0][71]);
            births_death.add("No of Maternal Deaths" + "@" + childhealth[0][72]);

            List<String> logistics = new ArrayList<String>();
            logistics.add("Tab. Paracetamol" + "@" + childhealth[0][73]);
            logistics.add("Syp. Paracetamol" + "@" + childhealth[0][74]);
            logistics.add("Tab. Choloroquin" + "@" + childhealth[0][75]);
            logistics.add("Syp. Choloroquin" + "@" + childhealth[0][76]);
            logistics.add("Tab. Mebendazole" + "@" + childhealth[0][77]);
            logistics.add("Syp. Pipearzine" + "@" + childhealth[0][78]);
            logistics.add("ORS" + "@" + childhealth[0][79]);
            logistics.add("Eye Ontiment" + "@" + childhealth[0][80]);
            logistics.add("Syp. Contrimexazole" + "@" + childhealth[0][81]);
            logistics.add("Iron Tab." + "@" + childhealth[0][82]);
            logistics.add("Antiseptic Lotion" + "@" + childhealth[0][83]);
            logistics.add("Benzyle Benzoate Lotion" + "@" + childhealth[0][84]);
            logistics.add("Sticking Plaster" + "@" + childhealth[0][85]);
            logistics.add("B. Complex Syp" + "@" + childhealth[0][86]);
            logistics.add("Cotton Bandages" + "@" + childhealth[0][87]);
            logistics.add("Cotton Wool" + "@" + childhealth[0][88]);
            logistics.add("Condoms" + "@" + childhealth[0][89]);
            logistics.add("Oral Pills" + "@" + childhealth[0][90]);
            logistics.add("Contraceptive Inj." + "@" + childhealth[0][91]);
            logistics.add("Others" + "@" + childhealth[0][92]);

            List<String> miscellanous = new ArrayList<String>();
            miscellanous.add("LHW Kit Bag" + "@" + childhealth[0][93]);
            miscellanous.add("Weighing Machine" + "@" + childhealth[0][94]);
            miscellanous.add("Thermometer" + "@" + childhealth[0][95]);
            miscellanous.add("Torch with Cell" + "@" + childhealth[0][96]);
            miscellanous.add("Scissors" + "@" + childhealth[0][97]);
            miscellanous.add("Syringe Cuttur" + "@" + childhealth[0][98]);
            miscellanous.add("Others" + "@" + childhealth[0][99]);

            List<String> supervision = new ArrayList<String>();
            supervision.add("No of visits by LHS" + "@" + childhealth[0][100]);
            supervision.add("No of Visit by DCO (NP)" + "@" + childhealth[0][101]);
            supervision.add("No of Visit by ADC (NP)" + "@" + childhealth[0][102]);
            supervision.add("No of Visit by FPO" + "@" + childhealth[0][103]);
            supervision.add("No # of Visit by PPIU" + "@" + childhealth[0][104]);*/


          /*  listDataChild.put(listDataHeader.get(3), null); // Header, Child data
            listDataChild.put(listDataHeader.get(4), null);
            listDataChild.put(listDataHeader.get(5), null);
            listDataChild.put(listDataHeader.get(6), null);
            listDataChild.put(listDataHeader.get(7), null); // Header, Child data
            listDataChild.put(listDataHeader.get(8), null);
            listDataChild.put(listDataHeader.get(9), null);
            listDataChild.put(listDataHeader.get(10), null);*/

            listAdapter = new Adt_ExpMonthlyReport(this, listDataHeader, listDataChild);
            listAdapter.notifyDataSetChanged();
            expListView.setAdapter(listAdapter);

        } catch (Exception e) {
            Log.d("000789", "Err PrepareListData: " + e.getMessage());
        }
    }

    String formatMonthYear(String str) {
        Date date = null;
        try {
            date = input.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(date);
    }


    @Override
    protected void onResume() {
        super.onResume();

        prepareListData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
