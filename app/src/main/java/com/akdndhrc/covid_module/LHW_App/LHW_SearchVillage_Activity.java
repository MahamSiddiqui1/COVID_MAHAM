package com.akdndhrc.covid_module.LHW_App;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.Child_Dashboard_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MaleDashboardActivities.AboveTwoMaleProfile_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MaleDashboardActivities.MaleBemaariRecordActivities.Male_BemaariRecordList_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MaleDashboardActivities.MaleReferralActivities.Male_RefferalRecordList_Activity;
import com.akdndhrc.covid_module.Adapter.Adt_SearchVillages;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.Mother_Dashboard_Activity;
import com.akdndhrc.covid_module.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.oshi.libsearchtoolbar.SearchAnimationToolbar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LHW_SearchVillage_Activity extends AppCompatActivity implements SearchAnimationToolbar.OnSearchQueryChangedListener {

    Context ctx = LHW_SearchVillage_Activity.this;

    public SearchAnimationToolbar toolbar;
    Adt_SearchVillages adt;
    ListView lv;
    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    String[][] mData_search;
    TextView tv_record, tv_NoRecord;
    RelativeLayout rl_record, rl_home_top;
    LinearLayout ll_pbProgress;
    String temp_var = "null";

    private ShimmerFrameLayout mShimmerViewContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lhw_search_village);

        temp_var = "0";


        //Shimmer
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        //ListView
        lv = findViewById(R.id.lv);

        tv_record = findViewById(R.id.tv_record);
        tv_NoRecord = findViewById(R.id.tv_NoRecord);

        //Relative Layout
        rl_record = findViewById(R.id.rl_record);
        rl_home_top = findViewById(R.id.rl_home_top);

        toolbar = (SearchAnimationToolbar) findViewById(R.id.toolbar);
        toolbar.setSupportActionBar((AppCompatActivity) ctx);
        toolbar.setOnSearchQueryChangedListener(this);

        ((AppCompatActivity) ctx).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        ((AppCompatActivity) ctx).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) ctx).getSupportActionBar().setDisplayShowTitleEnabled(false);

       /* InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(toolbar.getWindowToken(), 0);*/

        ll_pbProgress = findViewById(R.id.ll_pbProgress);
        ll_pbProgress.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        // toolbar.expandAndSearch("");


        adt = new Adt_SearchVillages(ctx, hashMapArrayList);
        lv.setAdapter(adt);
        new Task().execute();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (Integer.parseInt(mData_search[position][2]) < 5) {
                    Intent intent = new Intent(ctx, Child_Dashboard_Activity.class);
                    intent.putExtra("child_name", mData_search[position][0]);
                    intent.putExtra("u_id", mData_search[position][3]);
                    intent.putExtra("child_gender", mData_search[position][4]);
                    startActivity(intent);

                    Log.d("000330", "1");
                }
                else if (Integer.parseInt(mData_search[position][2]) >=5 && Integer.parseInt(mData_search[position][2]) <=14) {
                    Male_BottomDialog(position);
                    Log.d("000330", "2");
                }

                else if (Integer.parseInt(mData_search[position][2]) > 14) {
                    if (mData_search[position][4].equalsIgnoreCase("0")){
                        Intent intent = new Intent(ctx, Mother_Dashboard_Activity.class);
                        intent.putExtra("mother_name", mData_search[position][0]);
                        intent.putExtra("u_id", mData_search[position][3]);
                        startActivity(intent);
                        Log.d("000330", "3");
                    }else if (mData_search[position][4].equalsIgnoreCase("1")) {
                        Male_BottomDialog(position);
                        Log.d("000330", "4");
                    }
                    else {
                        Log.d("000330", "5");
                        //Toast.makeText(ctx, "sd", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(ctx, "something wrong", Toast.LENGTH_SHORT).show();
                }


              /*  if (Integer.parseInt(mData_search[position][2]) < 3) {
                    Intent intent = new Intent(ctx, Child_Dashboard_Activity.class);
                    intent.putExtra("child_name", mData_search[position][0]);
                    intent.putExtra("u_id", mData_search[position][3]);
                    intent.putExtra("child_gender", mData_search[position][4]);
                    startActivity(intent);
                }
                else {
                    if (mData_search[position][4].equalsIgnoreCase("0")) {
                        Intent intent = new Intent(ctx, Mother_Dashboard_Activity.class);
                        intent.putExtra("mother_name", mData_search[position][0]);
                        intent.putExtra("u_id", mData_search[position][3]);
                        startActivity(intent);
                    } else if (mData_search[position][4].equalsIgnoreCase("1")) {
                        Male_BottomDialog(position);
                    } else {
                        Toast.makeText(ctx, "something wrong", Toast.LENGTH_SHORT).show();
                    }
                }*/
            }
        });


    }

    private void Male_BottomDialog(final int position) {

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(ctx, R.style.AppBottomSheetDialogTheme);
        final View sheetView = getLayoutInflater().inflate(R.layout.dialog_male_dashboard_bottomsheet_layout, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCanceledOnTouchOutside(false);
        mBottomSheetDialog.setCancelable(false);
        mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mBottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_SlideUp; //style id
        mBottomSheetDialog.show();

        //ImageView
        ImageView iv_close_dilaog = (ImageView) sheetView.findViewById(R.id.iv_close_dilaog);
        ImageView image_profile_gender = sheetView.findViewById(R.id.image_profile_gender);

        //Relative Layout
      /*  LinearLayout ll_illness = sheetView.findViewById(R.id.ll_illness);
        LinearLayout ll_profile = sheetView.findViewById(R.id.ll_profile);
        LinearLayout ll_referral = sheetView.findViewById(R.id.ll_referral); */

        RelativeLayout rl_referral = sheetView.findViewById(R.id.rl_referral);
        RelativeLayout rl_bemari_record = sheetView.findViewById(R.id.rl_bemari_record);
        RelativeLayout rl_profile = sheetView.findViewById(R.id.rl_profile);

        int newHeight = 70; // New height in pixels
        int newWidth = 65;


        try{
                 if (Integer.parseInt(mData_search[position][2]) >=5 && Integer.parseInt(mData_search[position][2]) <=14) {

                     if (mData_search[position][4].equalsIgnoreCase("0")){
                         image_profile_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_adult_female_50dp));
                        image_profile_gender.requestLayout();
                        image_profile_gender.getLayoutParams().height = newHeight;
                        image_profile_gender.getLayoutParams().width = newWidth;
                         Log.d("000330", "5 To 14 FEMALE");
                     }
                     else {
                         Log.d("000330", "5 To 14 MALE");
                         image_profile_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                     }

            }

            else if (Integer.parseInt(mData_search[position][2]) > 14) {
                     Log.d("000330", ">14 MALE");
                     image_profile_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
            }

        }catch (Exception e)
        {
            Log.d("000330", "Ex: " +e.getMessage());
        }


        iv_close_dilaog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });


        rl_bemari_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();

                        Intent intent2 = new Intent(ctx, Male_BemaariRecordList_Activity.class);
                        intent2.putExtra("u_id", mData_search[position][3]);
                        startActivity(intent2);
            }
        });

        rl_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();

                        Intent intent = new Intent(ctx, AboveTwoMaleProfile_Activity.class);
                        intent.putExtra("u_id", mData_search[position][3]);
                        startActivity(intent);
            }
        });

        rl_referral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();

                Intent intent3 = new Intent(ctx, Male_RefferalRecordList_Activity.class);
                intent3.putExtra("u_id", mData_search[position][3]);
                intent3.putExtra("child_age", mData_search[position][2]);
                intent3.putExtra("child_name", mData_search[position][0]);
                intent3.putExtra("child_gender", mData_search[position][4]);
                startActivity(intent3);
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
        globalSearchQuery(query);
    }

    @Override
    public void onSearchExpanded() {

    }

    @Override
    public void onSearchSubmitted(String query) {

    }

    private void globalSearchQuery(String query) {

        hashMapArrayList.clear();
        try {

            Lister ls = new Lister(this);
            ls.createAndOpenDB();
            //String[][] data = ls.executeReader("Select *from HealthFacilities where favById = '"+Utils.getSharedPrefUsers(ctx).getId()+"' AND isFav = '" + true + "' order by LOWER(name) LIKE ?   ", new String[]{"%" + favById.toLowerCase() + "%"});
            mData_search = ls.executeReader("SELECT t1.full_name,t3.name, t1.age,t1.uid,t1.gender,t1.data, t4.privilege from MEMBER t1 " +
                    "INNER JOIN KHANDAN t2 ON t1.khandan_id = t2.uid " +
                    "INNER JOIN VILLAGES t3 ON t2.village_id = t3.uid " +
                    "INNER JOIN USERS t4 ON t1.added_by = t4.uid " +
                    "WHERE t1.full_name LIKE  '%" + query.toString() + "%'  OR t3.name LIKE  '%" + query.toString() + "%' OR t1.phone_number LIKE  '%" + query.toString() + "%' ORDER BY UPPER(t1.full_name)");

            Log.d("000123", "DATA LEN: " + String.valueOf(mData_search.length));

            if (mData_search != null) {

                rl_record.setVisibility(View.GONE);

                HashMap<String, String> map;
                for (int i = 0; i < mData_search.length; i++) {
                    map = new HashMap<>();

                    Log.d("000123", "Srch Name: " + mData_search[i][0]);
                    Log.d("000123", "Srch VillgaeName: " + mData_search[i][1]);
                    Log.d("000123", "Srch Age: " + mData_search[i][2]);
                    Log.d("000123", "Srch Uid: " + mData_search[i][3]);
                    Log.d("000123", "Srch Gender: " + mData_search[i][4]);

                    map = new HashMap<>();
                    if (mData_search[i][6].equalsIgnoreCase("2")) {
                        map.put("village_name", "" + mData_search[i][1] + " /" + " Vac ");
                    } else {
                        map.put("village_name", "" + mData_search[i][1]);
                    }

                    try {
                        JSONObject jsonObject = new JSONObject(mData_search[i][5]);
                        if (jsonObject.has("father_name")) {
                            if (jsonObject.getString("father_name").isEmpty()) {
                                map.put("name", "" + mData_search[i][0] + " " + "");
                            } else {
                                map.put("name", "" + mData_search[i][0] + " " + jsonObject.getString("father_name"));
                            }
                        } else {
                            map.put("name", "" + mData_search[i][0] + " " + "");
                        }
                    } catch (Exception e) {
                        Log.d("000123", "JSONError: " + e.getMessage());
                    }

                    map.put("age", "" + mData_search[i][2]);

                 /*   if (mData_search[i][4].equalsIgnoreCase("0")) {
                        map.put("gender", "" + "عورت");
                    } else if (mData_search[i][4].equalsIgnoreCase("1")) {
                        map.put("gender", "" + "مرد");
                    } else {
                        map.put("gender", "" + "unknown");
                    }*/


                    if (Integer.parseInt(mData_search[i][2]) >= 3 && Integer.parseInt(mData_search[i][2]) <= 14) {
                        if (mData_search[i][4].equalsIgnoreCase("0")) {
                            map.put("gender", "" + "لڑکی");
                        } else if (mData_search[i][4].equalsIgnoreCase("1")) {
                            map.put("gender", "" + "لڑکا");
                        } else {
                            map.put("gender", "" + "unknown");
                        }
                    }
                    else if (Integer.parseInt(mData_search[i][2]) >= 15) {
                            if (mData_search[i][4].equalsIgnoreCase("0")) {
                                map.put("gender", "" + "عورت");
                            } else if (mData_search[i][4].equalsIgnoreCase("1")) {
                                map.put("gender", "" + "مرد");
                            } else {
                                map.put("gender", "" + "unknown");
                            }
                        }
                        else {
                        if (mData_search[i][4].equalsIgnoreCase("0")) {
                            map.put("gender", "" + "لڑکی");
                        } else if (mData_search[i][4].equalsIgnoreCase("1")) {
                            map.put("gender", "" + "لڑکا");
                        } else {
                            map.put("gender", "" + "unknown");
                        }
                    }

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
            tv_record.setText(Html.fromHtml("&ldquo; " + query.toString() + " &rdquo;"));
            rl_record.setVisibility(View.VISIBLE);

        }
    }

    private void read_all_data() {

        hashMapArrayList.clear();

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            mData_search = ls.executeReader("SELECT t1.full_name,t3.name,t1.age,t1.uid,t1.gender,t1.data,t4.privilege from MEMBER t1 " +
                    "INNER JOIN KHANDAN t2 ON t1.khandan_id = t2.uid " +
                    "INNER JOIN VILLAGES t3 ON t2.village_id = t3.uid " +
                    "INNER JOIN USERS t4 ON t1.added_by = t4.uid " +
                    "ORDER BY UPPER(t1.full_name)");

            Log.d("000123", "ONRESUME LEN: " + String.valueOf(mData_search.length));

            temp_var = "1";


            HashMap<String, String> map;
            for (int i = 0; i < mData_search.length; i++) {
                Log.d("000123", "NAme: " + mData_search[i][0]);
                Log.d("000123", "village_name: " + mData_search[i][1]);
                Log.d("000123", "Age: " + mData_search[i][2]);
                Log.d("000123", "UID: " + mData_search[i][3]);
                Log.d("000123", "Gender: " + mData_search[i][4]);

                map = new HashMap<>();
                if (mData_search[i][6].equalsIgnoreCase("2")) {

                    map.put("village_name", "" + mData_search[i][1] + " /" + " Vac ");
                } else {
                    map.put("village_name", "" + mData_search[i][1]);
                }

                try {
                    JSONObject jsonObject = new JSONObject(mData_search[i][5]);
                    if (jsonObject.has("father_name")) {
                        if (jsonObject.getString("father_name").isEmpty()) {
                            map.put("name", "" + mData_search[i][0] + " " + "");
                        } else {
                            map.put("name", "" + mData_search[i][0] + " " + jsonObject.getString("father_name"));
                        }
                    } else {
                        map.put("name", "" + mData_search[i][0] + " " + "");
                    }
                } catch (Exception e) {
                    Log.d("000123", "JSONError: " + e.getMessage());
                }

             /*   if (mData_search[i][4].equalsIgnoreCase("0")) {
                    map.put("gender", "" + "عورت");
                } else if (mData_search[i][4].equalsIgnoreCase("1")) {
                    map.put("gender", "" + "مرد");
                } else {
                    map.put("gender", "" + "unknown");
                }*/

                if (Integer.parseInt(mData_search[i][2]) >= 3 && Integer.parseInt(mData_search[i][2]) <= 14) {
                    if (mData_search[i][4].equalsIgnoreCase("0")) {
                        map.put("gender", "" + "لڑکی");
                    } else if (mData_search[i][4].equalsIgnoreCase("1")) {
                        map.put("gender", "" + "لڑکا");
                    } else {
                        map.put("gender", "" + "unknown");
                    }
                }
                else if (Integer.parseInt(mData_search[i][2]) >= 15) {
                    if (mData_search[i][4].equalsIgnoreCase("0")) {
                        map.put("gender", "" + "عورت");
                    } else if (mData_search[i][4].equalsIgnoreCase("1")) {
                        map.put("gender", "" + "مرد");
                    } else {
                        map.put("gender", "" + "unknown");
                    }
                }
                else {
                    if (mData_search[i][4].equalsIgnoreCase("0")) {
                        map.put("gender", "" + "لڑکی");
                    } else if (mData_search[i][4].equalsIgnoreCase("1")) {
                        map.put("gender", "" + "لڑکا");
                    } else {
                        map.put("gender", "" + "unknown");
                    }
                }

                map.put("age", "" + mData_search[i][2]);

                hashMapArrayList.add(map);
            }


       /*    adt = new Adt_SearchVillages(ctx, hashMapArrayList);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);*/


        } catch (Exception e) {
            toolbar.setVisibility(View.GONE);
            Log.d("000123", "Error: " + e.getMessage());
            Toast.makeText(ctx, "کوئی ریکارڈ نہیں", Toast.LENGTH_SHORT).show();
        }
    }

    class Task extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            Log.d("000333", "ON PREEEEE: ");
            mShimmerViewContainer.startShimmerAnimation();
            //ll_pbProgress.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Log.d("000333", "ON BACkGROUND: ");
                read_all_data();
                Thread.sleep(4000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("000333", "ON EXECUTE: ");
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);

            //  ll_pbProgress.setVisibility(View.GONE);
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

    /*   @Override
       protected void onResume() {
           super.onResume();

           hashMapArrayList.clear();

           try {
               Lister ls = new Lister(ctx);
               ls.createAndOpenDB();


               mData_search = ls.executeReader("SELECT t1.full_name,t3.name,t1.age,t1.uid,t1.gender from MEMBER t1 " +
                       "INNER JOIN KHANDAN t2 ON t1.khandan_id = t2.uid " +
                       "INNER JOIN VILLAGES t3 ON t2.village_id = t3.uid ");

               Log.d("000123", "ONRESUME LEN: " + String.valueOf(mData_search.length));


               HashMap<String, String> map;
               for (int i = 0; i < mData_search.length; i++) {
                   Log.d("000123", "NAme: " + mData_search[i][0]);
                   Log.d("000123", "village_name: " + mData_search[i][1]);
                   Log.d("000123", "Age: " + mData_search[i][2]);
                   Log.d("000123", "UID: " + mData_search[i][3]);
                   Log.d("000123", "Gender: " + mData_search[i][4]);

                   map = new HashMap<>();
                   map.put("name", "" + mData_search[i][0]);
                   map.put("village_name", "" + mData_search[i][1]);

                   if (Integer.parseInt(mData_search[i][2]) > 3) {
                       map.put("age", "" + "عورت");
                   } else {
                       if (mData_search[i][4].equalsIgnoreCase("1")) {
                           map.put("age", "" + "بچا");
                       }
                       else {
                           map.put("age", "" + "بچی");
                       }

                   }

                   hashMapArrayList.add(map);
               }


               adt = new Adt_SearchVillages(ctx, hashMapArrayList);
               adt.notifyDataSetChanged();
               lv.setAdapter(adt);


           } catch (Exception e) {
               toolbar.setVisibility(View.GONE);
               Log.d("000123", "Error: " + e.getMessage());
               Toast.makeText(ctx, "کوئی ریکارڈ نہیں", Toast.LENGTH_SHORT).show();
           }
       }*/
    @Override
    protected void onRestart() {
        super.onRestart();
        if (AboveTwoMaleProfile_Activity.var_aboveTwo_profile.equalsIgnoreCase("1")) {
            Log.d("000123", "RESTART IF : ");
            finish();
            startActivity(getIntent());
        } else {
            Log.d("000123", "RESTART ELSE IF: ");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
