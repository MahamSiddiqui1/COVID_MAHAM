package com.akdndhrc.covid_module.VAC_App;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.Adt_VACSearchVillages;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.Utils;
import com.akdndhrc.covid_module.VAC_App.VAC_InsideOutsideUC.VAC_AboveTwoProfile_Activity;
import com.akdndhrc.covid_module.VAC_App.VAC_InsideOutsideUC.VAC_BelowTwoProfile_Activity;
import com.akdndhrc.covid_module.VAC_App.VAC_InsideOutsideUC.VAC_Child_HifazitiTeekeyRecordList2_Activity;
import com.akdndhrc.covid_module.VAC_App.VAC_InsideOutsideUC.VAC_Mother_HifazitiTeekeyRecordList_Activity;
import com.akdndhrc.covid_module.R;
import com.oshi.libsearchtoolbar.SearchAnimationToolbar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class VAC_SearchVillage_Activity extends AppCompatActivity implements SearchAnimationToolbar.OnSearchQueryChangedListener {

    Context ctx = VAC_SearchVillage_Activity.this;

    private SearchAnimationToolbar toolbar;
    Adt_VACSearchVillages adt;
    ListView lv;
    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    String[][] mData_search;
    TextView tv_record, tv_NoRecord;
    RelativeLayout rl_record;
    LinearLayout ll_pbProgress;
    String temp_var = "null";
    Spinner spVaccineName;
    LinearLayout ll_search_vaccine_name;
    String entered_query = "";
    String selected_vaccine_name = "";
    ImageView iv_clear_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vac_search_village);


        temp_var = "0";
        //ListView
        lv = findViewById(R.id.lv);

        tv_record = findViewById(R.id.tv_record);
        tv_NoRecord = findViewById(R.id.tv_NoRecord);

        //Relative Layout
        rl_record = findViewById(R.id.rl_record);

        //Spinner
        spVaccineName = findViewById(R.id.spVaccineName);

        //ImageView
        iv_clear_spinner = findViewById(R.id.iv_clear_spinner);

        //LinearLayout
        ll_search_vaccine_name = findViewById(R.id.ll_search_vaccine_name);

        toolbar = (SearchAnimationToolbar) findViewById(R.id.toolbar);
        toolbar.setSupportActionBar((AppCompatActivity) ctx);
        toolbar.setOnSearchQueryChangedListener(VAC_SearchVillage_Activity.this);

        ((AppCompatActivity) ctx).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ((AppCompatActivity) ctx).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) ctx).getSupportActionBar().setDisplayShowTitleEnabled(false);

        ll_pbProgress = findViewById(R.id.ll_pbProgress);
        ll_pbProgress.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);


        //toolbar.expandAndSearch("");

        spinner_data();


        iv_clear_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spVaccineName.setSelection(0);

                globalSearchQuery();
            }
        });


        adt = new Adt_VACSearchVillages(ctx, hashMapArrayList);
        lv.setAdapter(adt);
        new Task().execute();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (Integer.parseInt(mData_search[position][2]) < 3) {
                    Intent intent = new Intent(ctx, VAC_Child_HifazitiTeekeyRecordList2_Activity.class);
                    intent.putExtra("child_name", mData_search[position][0]);
                    intent.putExtra("u_id", mData_search[position][3]);
                    intent.putExtra("child_gender", mData_search[position][4]);
                    startActivity(intent);
                } else {
                    if (mData_search[position][4].equalsIgnoreCase("0")) {
                        Intent intent = new Intent(ctx, VAC_Mother_HifazitiTeekeyRecordList_Activity.class);
                        intent.putExtra("u_id", mData_search[position][3]);
                        startActivity(intent);
                    } else if (mData_search[position][4].equalsIgnoreCase("1")) {
                        Intent intent = new Intent(ctx, VAC_AboveTwoProfile_Activity.class);
                        intent.putExtra("u_id", mData_search[position][3]);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ctx, "something wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.action_search) {
            toolbar.onSearchIconClick();
            return true;
        } else if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchCollapsed() {

    }

    @Override
    public void onSearchQueryChanged(String query) {


        entered_query = query;
        globalSearchQuery();
        //globalSearchQuery(query);
    }

    @Override
    public void onSearchExpanded() {

    }

    @Override
    public void onSearchSubmitted(String query) {
    }

    private void globalSearchQuery() {

        hashMapArrayList.clear();
        try {

            Lister ls = new Lister(this);
            ls.createAndOpenDB();


            if (spVaccineName.getSelectedItemPosition() == 0) {

                mData_search = ls.executeReader("SELECT t1.full_name,t3.name, t1.age,t1.uid,t1.gender,t1.data,t4.privilege from MEMBER t1 " +
                        "LEFT JOIN KHANDAN t2 ON t1.khandan_id = t2.uid " +
                        "LEFT JOIN VILLAGES t3 ON t2.village_id = t3.uid " +
                        "LEFT JOIN USERS t4 ON t1.added_by = t4.uid " +
                        "WHERE t1.uid NOT IN (Select member_uid_2 from MEMBER_MERGERD) AND t1.full_name LIKE  '%" + entered_query.toString() + "%'  OR t3.name LIKE  '%" + entered_query.toString() + "%' OR t1.phone_number LIKE  '%" + entered_query.toString() + "%' ORDER BY UPPER(t1.full_name)");

                Log.d("000123", "IF DATA LEN: " + String.valueOf(mData_search.length));

                iv_clear_spinner.setVisibility(View.GONE);
            } else {

                mData_search = ls.executeReader("SELECT t0.full_name,t4.name,t0.age,t0.uid,t0.gender ,t0.data,t5.privilege , t2.uid, t2.name, t1.due_date, t1.vaccinated_on from MEMBER t0, VACCINES t2" +
                        " LEFT JOIN CVACCINATION t1 ON t0.uid = t1.member_uid AND t1.vaccine_id= t2.uid" +
                        " INNER JOIN KHANDAN t3 ON t0.khandan_id = t3.uid" +
                        " INNER JOIN VILLAGES t4 ON t3.village_id = t4.uid" +
                        " INNER JOIN USERS t5 ON t0.added_by = t5.uid" +
                        " WHERE t0.uid NOT IN (Select member_uid_2 from MEMBER_MERGERD) AND (t0.full_name LIKE  '%" + entered_query.toString() + "%'  OR  t4.name LIKE  '%" + entered_query.toString() + "%' OR  t0.phone_number LIKE  '%" + entered_query.toString() + "%') AND (t2.name = '" + spVaccineName.getSelectedItem().toString() + "' AND t1.due_date IS NULL) AND t0.age < 3" +
                        " ORDER BY UPPER(t0.full_name)");

                Log.d("000123", "ELSE IF DATA LEN: " + String.valueOf(mData_search.length));

                iv_clear_spinner.setVisibility(View.VISIBLE);
            }


            if (mData_search != null) {

                rl_record.setVisibility(View.GONE);

                HashMap<String, String> map;
                for (int i = 0; i < mData_search.length; i++) {
                    Log.d("000123", "Sr NAme: " + mData_search[i][0]);
                    Log.d("000123", "Sr village_name: " + mData_search[i][1]);
                    Log.d("000123", "Sr Age: " + mData_search[i][2]);
                  /*  Log.d("000123", "Sr UID: " + mData_search[i][3]);
                    Log.d("000123", "Sr Gender: " + mData_search[i][4]);*/

                    map = new HashMap<>();


                    if (mData_search[i][1] == null) {

                        if (mData_search[i][6].equalsIgnoreCase("1")) {
                            map.put("village_name", "" + "No Village" + " /" + " LHW ");
                        } else {
                            map.put("village_name", "" + "No Village");
                        }
                    } else {
                        if (mData_search[i][6].equalsIgnoreCase("1")) {
                            map.put("village_name", "" + mData_search[i][1] + " /" + " LHW ");
                        } else {
                            map.put("village_name", "" + mData_search[i][1]);
                        }
                    }


                    String name;
                    if (mData_search[i][0].isEmpty()) {
                        name = "Unknown";
                    } else {
                        name = mData_search[i][0];
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(mData_search[i][5]);
                        if (jsonObject.has("father_name")) {
                            if (jsonObject.getString("father_name").isEmpty()) {
                                map.put("name", "" + name + " " + "");
                            } else {
                                map.put("name", "" + name + " " + jsonObject.getString("father_name"));
                            }
                        } else {
                            map.put("name", "" + mData_search[i][0] + " " + "");
                        }
                    } catch (Exception e) {
                        Log.d("000123", "JSONError: " + e.getMessage());
                    }


                    if (Integer.parseInt(mData_search[i][2]) >= 3 && Integer.parseInt(mData_search[i][2]) <= 14) {
                        if (mData_search[i][4].equalsIgnoreCase("0")) {
                            map.put("gender", "" + "لڑکی");
                        } else if (mData_search[i][4].equalsIgnoreCase("1")) {
                            map.put("gender", "" + "لڑکا");
                        } else {
                            map.put("gender", "" + "unknown");
                        }
                    } else if (Integer.parseInt(mData_search[i][2]) >= 15) {

                        if (mData_search[i][4].equalsIgnoreCase("0")) {
                            map.put("gender", "" + "عورت");
                        } else if (mData_search[i][4].equalsIgnoreCase("1")) {
                            map.put("gender", "" + "مرد");
                        } else {
                            map.put("gender", "" + "unknown");
                        }
                    } else {
                        if (mData_search[i][4].equalsIgnoreCase("0")) {
                            map.put("gender", "" + "لڑکی");
                        } else if (mData_search[i][4].equalsIgnoreCase("1")) {
                            map.put("gender", "" + "لڑکا");
                        } else {
                            map.put("gender", "" + "unknown");
                        }
                    }

                    map.put("age", "" + mData_search[i][2]);

                    /*try {
                        if (spVaccineName.getSelectedItemPosition()== 0)
                        {
                            Log.d("000123", "IFFFFFFFFFFF ***********: ");
                            map.put("search_type", "" + "1");
                        }
                     else {
                            Log.d("000123", "ELSSSSSSSSSSSSSSSSSSSSS ***********: ");
                            map.put("search_type", "" + "2");
                            map.put("vaccinated_on", "" + mData_search[i][10]);
                        }
                    }catch (Exception e)
                    {
                        Log.d("000123", "ERRROR: ***********: " + e.getMessage());
                    }*/


                    hashMapArrayList.add(map);
                    adt.notifyDataSetChanged();
                }
            } else {
                adt.notifyDataSetChanged();
                //Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            adt.notifyDataSetChanged();
            Log.d("000123", "Search Exception" + e);
            //Toast.makeText(ctx, query.toString()+ "اس گاؤں کا کوئی ریکارڈ نہیں", Toast.LENGTH_SHORT).show();

            if (spVaccineName.getSelectedItemPosition() == 0) {
                tv_record.setText(Html.fromHtml("&ldquo; " + entered_query.toString() + " &rdquo;"));
            } else {
                tv_record.setText(Html.fromHtml("&ldquo; " + selected_vaccine_name.toString() + " &rdquo;"));
            }
            rl_record.setVisibility(View.VISIBLE);

        }
    }


    private void SearchVaccineQueryData(String vaccine_name) {

        hashMapArrayList.clear();
        try {

            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();


            mData_search = ls.executeReader("SELECT t0.full_name,t4.name,t0.age,t0.uid,t0.gender ,t0.data,t5.privilege , t2.uid, t2.name, t1.due_date, t1.vaccinated_on from MEMBER t0, VACCINES t2 " +
                    " LEFT JOIN CVACCINATION t1 ON t0.uid = t1.member_uid AND t1.vaccine_id= t2.uid" +
                    " INNER JOIN KHANDAN t3 ON t0.khandan_id = t3.uid" +
                    " INNER JOIN VILLAGES t4 ON t3.village_id = t4.uid" +
                    " INNER JOIN USERS t5 ON t0.added_by = t5.uid" +
                    " where t2.name = '" + vaccine_name + "' AND t1.due_date IS  NULL AND t0.uid NOT IN (Select member_uid_2 from MEMBER_MERGERD)" +
                    " ORDER BY UPPER(t0.full_name)");

            Log.d("000123", "Spinner SearchQueryData LEN: " + String.valueOf(mData_search.length));


            if (mData_search != null) {

                rl_record.setVisibility(View.GONE);

                HashMap<String, String> map;
                for (int i = 0; i < mData_search.length; i++) {
                    Log.d("0001233", "Sp Search Name: " + mData_search[i][0]);
                    Log.d("0001233", "Sp Search village_name: " + mData_search[i][1]);
                    Log.d("0001233", "Sp Search Age: " + mData_search[i][2]);
                  /*  Log.d("0001233", "Sp Search UID: " + mData_search[i][3]);
                    Log.d("0001233", "Sp Search Gender: " + mData_search[i][4]);
                    Log.d("0001233", "Sp Search Privilege: " + mData_search[i][6]);
                    Log.d("0001233", "Sp Search Vac Uid: " + mData_search[i][7]);
                    Log.d("0001233", "Sp Search Vac Name: " + mData_search[i][8]);
                    Log.d("0001233", "Sp Search Due_date: " + mData_search[i][9]);
                    Log.d("0001233", "Sp Search Vaccinated_on: " + mData_search[i][10]);*/

                    map = new HashMap<>();


                    if (mData_search[i][1] == null) {

                        if (mData_search[i][6].equalsIgnoreCase("1")) {
                            map.put("village_name", "" + "No Village" + " /" + " LHW ");
                        } else {
                            map.put("village_name", "" + "No Village");
                        }
                    } else {
                        if (mData_search[i][6].equalsIgnoreCase("1")) {
                            map.put("village_name", "" + mData_search[i][1] + " /" + " LHW ");
                        } else {
                            map.put("village_name", "" + mData_search[i][1]);
                        }
                    }


                    String name;
                    if (mData_search[i][0].isEmpty()) {
                        name = "Unknown";
                    } else {
                        name = mData_search[i][0];
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(mData_search[i][5]);
                        if (jsonObject.has("father_name")) {
                            if (jsonObject.getString("father_name").isEmpty()) {
                                map.put("name", "" + name + " " + "");
                            } else {
                                map.put("name", "" + name + " " + jsonObject.getString("father_name"));
                            }
                        } else {
                            map.put("name", "" + mData_search[i][0] + " " + "");
                        }
                    } catch (Exception e) {
                        Log.d("000123", "JSONError: " + e.getMessage());
                    }

                    if (Integer.parseInt(mData_search[i][2]) >= 3 && Integer.parseInt(mData_search[i][2]) <= 14) {
                        if (mData_search[i][4].equalsIgnoreCase("0")) {
                            map.put("gender", "" + "لڑکی");
                        } else if (mData_search[i][4].equalsIgnoreCase("1")) {
                            map.put("gender", "" + "لڑکا");
                        } else {
                            map.put("gender", "" + "unknown");
                        }
                    } else if (Integer.parseInt(mData_search[i][2]) >= 15) {
                        if (mData_search[i][4].equalsIgnoreCase("0")) {
                            map.put("gender", "" + "عورت");
                        } else if (mData_search[i][4].equalsIgnoreCase("1")) {
                            map.put("gender", "" + "مرد");
                        } else {
                            map.put("gender", "" + "unknown");
                        }
                    } else {
                        if (mData_search[i][4].equalsIgnoreCase("0")) {
                            map.put("gender", "" + "لڑکی");
                        } else if (mData_search[i][4].equalsIgnoreCase("1")) {
                            map.put("gender", "" + "لڑکا");
                        } else {
                            map.put("gender", "" + "unknown");
                        }
                    }

                    map.put("age", "" + mData_search[i][2]);
                    map.put("search_type", "" + "2");
                    map.put("vaccinated_on", "" + mData_search[i][10]);


                    hashMapArrayList.add(map);
                    adt.notifyDataSetChanged();
                }
            } else {
                adt.notifyDataSetChanged();
            }

        } catch (Exception e) {
            adt.notifyDataSetChanged();
            Log.d("0001233", "Spinner Search Exception: " + e.getMessage());
            // Toast.makeText(ctx, "کوئی ریکارڈ نہیں", Toast.LENGTH_SHORT).show();
            tv_record.setText(Html.fromHtml("&ldquo; " + vaccine_name.toString() + " &rdquo;"));
            rl_record.setVisibility(View.VISIBLE);

        }

    }


    private void spinner_data() {

        ///////////////////////Select Villagesssss ////////////////////////////////////
        try {

            Utils.setSpinnerVaccineName(ctx, spVaccineName);
            spVaccineName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (spVaccineName.getSelectedItemPosition() == 0) {
                        Log.d("000321", "spArea IF !!!!!!!!");
                    } else {
                        toolbar.clearFocus();
                        selected_vaccine_name = spVaccineName.getSelectedItem().toString();
                        globalSearchQuery();
                        //SearchVaccineQueryData(spVaccineName.getSelectedItem().toString());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }

            });
        } catch (Exception e) {
            Log.d("000321", "Error Village:  " + e.getMessage());
        }

    }

    private void read_all_data() {

        hashMapArrayList.clear();
        try {

            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();


            mData_search = ls.executeReader("SELECT t1.full_name,t3.name,t1.age,t1.uid,t1.gender,t1.data, t4.privilege from MEMBER t1 " +
                    "LEFT JOIN KHANDAN t2 ON t1.khandan_id = t2.uid " +
                    "LEFT JOIN VILLAGES t3 ON t2.village_id = t3.uid " +
                    "LEFT JOIN USERS t4 ON t1.added_by = t4.uid " +
                    " where t1.uid NOT IN (Select member_uid_2 from MEMBER_MERGERD)" +
                    "ORDER BY UPPER(t1.full_name)");

            Log.d("000123", "ONRESUME LEN: " + String.valueOf(mData_search.length));

            temp_var = "1";

            HashMap<String, String> map;
            for (int i = 0; i < mData_search.length; i++) {

                Log.d("000123", "NAme: " + mData_search[i][0]);
                Log.d("000123", "village_name: " + mData_search[i][1]);
                Log.d("000123", "Age: " + mData_search[i][2]);
               /* Log.d("000123", "UID: " + mData_search[i][3]);
                Log.d("000123", "Gender: " + mData_search[i][4]);
                Log.d("000123", "Gender: " + mData_search[i][6]);*/

                map = new HashMap<>();

                if (mData_search[i][1] == null) {

                    if (mData_search[i][6].equalsIgnoreCase("1")) {
                        map.put("village_name", "" + "No Village" + " /" + " LHW ");
                    } else {
                        map.put("village_name", "" + "No Village");
                    }
                } else {
                    if (mData_search[i][6].equalsIgnoreCase("1")) {
                        map.put("village_name", "" + mData_search[i][1] + " /" + " LHW ");
                    } else {
                        map.put("village_name", "" + mData_search[i][1]);
                    }
                }

               /* if (mData_search[i][1] == null) {
                    map.put("village_name", "" + "No Village");
                } else {

                    map.put("village_name", "" + mData_search[i][1]);
                }*/
                String name;
                if (mData_search[i][0].isEmpty()) {
                    name = "Unknown";
                } else {
                    name = mData_search[i][0];
                }

                try {
                    JSONObject jsonObject = new JSONObject(mData_search[i][5]);
                    if (jsonObject.has("father_name")) {
                        if (jsonObject.getString("father_name").isEmpty()) {
                            map.put("name", "" + name + " " + "");
                        } else {
                            map.put("name", "" + name + " " + jsonObject.getString("father_name"));
                        }
                    } else {
                        map.put("name", "" + mData_search[i][0] + " " + "");
                    }
                } catch (Exception e) {
                    Log.d("000123", "JSONError: " + e.getMessage());
                }

                if (Integer.parseInt(mData_search[i][2]) >= 3 && Integer.parseInt(mData_search[i][2]) <= 14) {
                    if (mData_search[i][4].equalsIgnoreCase("0")) {
                        map.put("gender", "" + "لڑکی");
                    } else if (mData_search[i][4].equalsIgnoreCase("1")) {
                        map.put("gender", "" + "لڑکا");
                    } else {
                        map.put("gender", "" + "unknown");
                    }
                } else if (Integer.parseInt(mData_search[i][2]) >= 15) {
                    if (mData_search[i][4].equalsIgnoreCase("0")) {
                        map.put("gender", "" + "عورت");
                    } else if (mData_search[i][4].equalsIgnoreCase("1")) {
                        map.put("gender", "" + "مرد");
                    } else {
                        map.put("gender", "" + "unknown");
                    }
                } else {
                    if (mData_search[i][4].equalsIgnoreCase("0")) {
                        map.put("gender", "" + "لڑکی");
                    } else if (mData_search[i][4].equalsIgnoreCase("1")) {
                        map.put("gender", "" + "لڑکا");
                    } else {
                        map.put("gender", "" + "unknown");
                    }
                }

                map.put("age", "" + mData_search[i][2]);
                //  map.put("search_type", "" + "0");



               /* if (Integer.parseInt(mData_search[i][2]) > 3) {
                    if (mData_search[i][4].equalsIgnoreCase("0")) {
                        map.put("gender", "" + "عورت");
                    } else if (mData_search[i][4].equalsIgnoreCase("1")) {
                        map.put("gender", "" + "مرد");
                    } else {
                        map.put("gender", "" + "none");
                    }

                } else {
                    if (mData_search[i][4].equalsIgnoreCase("0")) {
                        map.put("gender", "" + "بچی");
                    } else if (mData_search[i][4].equalsIgnoreCase("1")) {
                        map.put("gender", "" + "بچا");
                    } else {
                        map.put("gender", "" + "none");
                    }
                }*/

                hashMapArrayList.add(map);
            }


         /*  adt = new Adt_SearchVillages(ctx, hashMapArrayList);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);*/


        } catch (Exception e) {
            ll_search_vaccine_name.setVisibility(View.GONE);
            toolbar.setVisibility(View.GONE);
            Log.d("000123", "Error: " + e.getMessage());
            Toast.makeText(ctx, "کوئی ریکارڈ نہیں", Toast.LENGTH_SHORT).show();
        }
    }


    class Task extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            Log.d("000333", "ON PREEEEE: ");
            ll_pbProgress.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Log.d("000333", "ON BACkGROUND: ");
                read_all_data();
                Thread.sleep(1500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("000333", "ON EXECUTE: ");
            ll_pbProgress.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            if (temp_var.equalsIgnoreCase("1")) {
                toolbar.setVisibility(View.VISIBLE);
                toolbar.expandAndSearch("");

            } else {
                toolbar.setVisibility(View.GONE);
                tv_NoRecord.setVisibility(View.VISIBLE);
            }
            adt.notifyDataSetChanged();
            super.onPostExecute(result);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (VAC_BelowTwoProfile_Activity.var_updateprofile_belowtwotemp.equalsIgnoreCase("1")) {
            Log.d("000987", "RESTART ELSE IF: ");
            finish();
            startActivity(getIntent());
            VAC_BelowTwoProfile_Activity.var_updateprofile_belowtwotemp = "0";
        } else if (VAC_AboveTwoProfile_Activity.var_updateprofile_abovetwotemp.equalsIgnoreCase("1")) {
            Log.d("000987", "RESTART ELSE IF: ");
            finish();
            startActivity(getIntent());
            VAC_AboveTwoProfile_Activity.var_updateprofile_abovetwotemp = "0";
        } else {
            Log.d("000987", "RESTART ELSEEEEE: ");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
