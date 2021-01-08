package com.akdndhrc.covid_module;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static com.akdndhrc.covid_module.DatabaseFiles.Helper.CREATE_TABLE_COUNTRY;
import static com.akdndhrc.covid_module.DatabaseFiles.Helper.CREATE_TABLE_DISTRICT;
import static com.akdndhrc.covid_module.DatabaseFiles.Helper.CREATE_TABLE_FACILITY;
import static com.akdndhrc.covid_module.DatabaseFiles.Helper.CREATE_TABLE_MEDICINE;
import static com.akdndhrc.covid_module.DatabaseFiles.Helper.CREATE_TABLE_PROVINCE;
import static com.akdndhrc.covid_module.DatabaseFiles.Helper.CREATE_TABLE_TEHSIL;
import static com.akdndhrc.covid_module.DatabaseFiles.Helper.CREATE_TABLE_UNIONCOUNCIL;
import static com.akdndhrc.covid_module.DatabaseFiles.Helper.CREATE_TABLE_USERS;
import static com.akdndhrc.covid_module.DatabaseFiles.Helper.CREATE_TABLE_VACCINES;
import static com.akdndhrc.covid_module.DatabaseFiles.Helper.CREATE_TABLE_VILLAGES;

public class OfflineSync_Activity extends AppCompatActivity {

    Context ctx = OfflineSync_Activity.this;


    RelativeLayout rl_sync_country, rl_sync_province, rl_sync_district, rl_sync_tehsil, rl_sync_unioncounil, rl_sync_facility, rl_sync_villages, rl_sync_users, rl_sync_medicines, rl_sync_vaccines;
    TextView txt_sync_country, txt_sync_province, txt_sync_district, txt_sync_tehsil, txt_sync_unioncounil, txt_sync_facility, txt_sync_villages, txt_sync_users, txt_sync_medicines, txt_sync_vaccines;
    ProgressBar pb_2, pb_3, pb_4, pb_5, pb_6, pb_7, pb_8, pb_9, pb_10;

    Lister ls;
    Dialog progressDialog;
    TextView tvsync_msg, tvcount;
    ImageView image_done;
    RelativeLayout rl_prProgress;
    ProgressBar pbProgress;
    private Handler handler = new Handler();
    int i;
    int pStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_sync);


        ls = new Lister(ctx);

        //RelativeLayout
        rl_sync_country = findViewById(R.id.rl_sync_country);
        rl_sync_province = findViewById(R.id.rl_sync_province);
        rl_sync_district = findViewById(R.id.rl_sync_district);
        rl_sync_tehsil = findViewById(R.id.rl_sync_tehsil);
        rl_sync_unioncounil = findViewById(R.id.rl_sync_unioncounil);
        rl_sync_facility = findViewById(R.id.rl_sync_facility);
        rl_sync_villages = findViewById(R.id.rl_sync_villages);
        rl_sync_users = findViewById(R.id.rl_sync_users);
        rl_sync_medicines = findViewById(R.id.rl_sync_medicines);
        rl_sync_vaccines = findViewById(R.id.rl_sync_vaccines);

        //TextView
        txt_sync_country = findViewById(R.id.txt_sync_country);
        txt_sync_province = findViewById(R.id.txt_sync_province);
        txt_sync_district = findViewById(R.id.txt_sync_district);
        txt_sync_tehsil = findViewById(R.id.txt_sync_tehsil);
        txt_sync_unioncounil = findViewById(R.id.txt_sync_unioncounil);
        txt_sync_facility = findViewById(R.id.txt_sync_facility);
        txt_sync_villages = findViewById(R.id.txt_sync_villages);
        txt_sync_users = findViewById(R.id.txt_sync_users);
        txt_sync_medicines = findViewById(R.id.txt_sync_medicines);
        txt_sync_vaccines = findViewById(R.id.txt_sync_vaccines);

        //ProgressBar
        pb_2 = findViewById(R.id.pb_2);
        pb_3 = findViewById(R.id.pb_3);
        pb_4 = findViewById(R.id.pb_4);
        pb_5 = findViewById(R.id.pb_5);
        pb_6 = findViewById(R.id.pb_6);
        pb_7 = findViewById(R.id.pb_7);
        pb_8 = findViewById(R.id.pb_8);
        pb_9 = findViewById(R.id.pb_9);
        pb_10 = findViewById(R.id.pb_10);

        rl_sync_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sync_CountryData(v);
            }
        });

        rl_sync_province.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sync_ProvinceData(v);
            }
        });

        rl_sync_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sync_DistrictData(v);
            }
        });

        rl_sync_tehsil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sync_TehsilData(v);
            }
        });

        rl_sync_unioncounil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sync_UnionCouncilData(v);
            }
        });

        rl_sync_facility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sync_FacilityData(v);
            }
        });

        rl_sync_villages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sync_VillagesData(v);
            }
        });

        rl_sync_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sync_UsersData(v);
            }
        });

        rl_sync_medicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sync_MedicinesData(v);
            }
        });

        rl_sync_vaccines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sync_VaccinesData(v);
            }
        });
    }

    private void Sync_CountryData(final View v) {

        try {
            final File directory = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "HayatPK"
                    + File.separator + "Dictionaries"
                    + File.separator + "list_countries_pk.csv");
            if (!directory.exists()) {
                final Snackbar snackbar = Snackbar.make(v, R.string.noFileInFolder, Snackbar.LENGTH_SHORT);
                View mySbView = snackbar.getView();
                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(16);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                }
                snackbar.setDuration(3000);
                snackbar.show();
                return;
            } else {

                progressDialog = new Dialog(ctx);
                LayoutInflater layout = LayoutInflater.from(ctx);
                final View dialogView = layout.inflate(R.layout.lay_dialog_syncing_loading2, null);
                progressDialog.setContentView(dialogView);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.show();


                //TextView
                tvcount = dialogView.findViewById(R.id.tvcount);
                tvcount.setVisibility(View.GONE);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        InputStream inputStream = null;
                        try {
                            inputStream = getContentResolver().openInputStream(Uri.fromFile(directory));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        CSVFile csvFile = new CSVFile(inputStream);
                        List<String[]> countryList = csvFile.read();
                        Log.d("000122", "CountryList SIZE:" + countryList.size());

                        ls.createAndOpenDB();
                        boolean m1 = ls.executeNonQuery("DROP TABLE IF EXISTS COUNTRY ");
                        Log.d("000122", "COUNTRY TABLE DROP: " + m1);
                        boolean m2 = ls.executeNonQuery(CREATE_TABLE_COUNTRY);
                        Log.d("000122", "COUNTRY TABLE CREATED: " + m2);

                        for (int i = 0; i < countryList.size(); i++) {
                            Log.d("000122", String.format("CountryData %s: %s, %s", String.valueOf(i+1), countryList.get(i)[0], countryList.get(i)[1]));

                            boolean mFlag_Country = ls.executeNonQuery("insert or ignore into COUNTRY(uid, " +
                                    "name) values " +
                                    "(" +
                                    "'" + countryList.get(i)[0] + "'," +
                                    "'" + countryList.get(i)[1] + "'" +
                                    ")");

                            Log.d("000122", "COUNTRY Query:" + String.valueOf(i+1) + "-" + mFlag_Country);
                            if (mFlag_Country == true) {
                                if (i == countryList.size() - 1) {
                                    Log.d("000122", "IF LENGTH ");
                                    Log.d("000122", "ALL DATA INSERTED SUCCESSFULLY !!!!!!!!!!!!!!!!!!!!!");

                                    progressDialog.dismiss();

                                    final Snackbar snackbar = Snackbar.make(v, R.string.countrySynced, Snackbar.LENGTH_SHORT);
                                    View mySbView = snackbar.getView();
                                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.WHITE);
                                    textView.setTextSize(16);
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                    }
                                    snackbar.setDuration(3000);
                                    snackbar.show();

                                    Count_CountryRecord();

                                } else {
                                    Log.d("000122", "ELSEEE LENGTH ");
                                }
                            } else {
                                progressDialog.dismiss();

                                Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                                Log.d("000122", "ELSEEE NOT TRUE");
                            }


                        }
                    }
                }, 2000);
            }

        } catch (Exception e) {
            Toast.makeText(ctx, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            Log.d("000122", "COUNTRY FILE ERROR: " + e.getMessage());
        }
    }

    private void Sync_ProvinceData(final View v) {

        try {
            final File directory = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "HayatPK"
                    + File.separator + "Dictionaries"
                    + File.separator + "list_provinces_pk.csv");
            if (!directory.exists()) {
                final Snackbar snackbar = Snackbar.make(v, R.string.noFileInFolder, Snackbar.LENGTH_SHORT);
                View mySbView = snackbar.getView();
                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(16);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                }
                snackbar.setDuration(3000);
                snackbar.show();
                return;
            } else {

                progressDialog = new Dialog(ctx);
                LayoutInflater layout = LayoutInflater.from(ctx);
                final View dialogView = layout.inflate(R.layout.lay_dialog_syncing_loading2, null);
                progressDialog.setContentView(dialogView);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.show();


                //TextView
                tvcount = dialogView.findViewById(R.id.tvcount);
                tvcount.setVisibility(View.VISIBLE);

                //ImageView
                image_done = dialogView.findViewById(R.id.image_done);

                //ProgressBar
                pbProgress = dialogView.findViewById(R.id.pbProgress);



                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {*/


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        InputStream inputStream = null;
                        try {
                            inputStream = getContentResolver().openInputStream(Uri.fromFile(directory));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }

                        CSVFile csvFile = new CSVFile(inputStream);
                        final List<String[]> provinceList = csvFile.read();
                        Log.d("000122", "ProvinceList SIZE:" + provinceList.size());

                        tvcount.setText("0" + "/" + provinceList.size());

                        ls.createAndOpenDB();
                        boolean m1 = ls.executeNonQuery("DROP TABLE IF EXISTS PROVINCE ");
                        Log.d("000122", "PROVINCE TABLE DROP: " + m1);
                        boolean m2 = ls.executeNonQuery(CREATE_TABLE_PROVINCE);
                        Log.d("000122", "PROVINCE TABLE CREATED: " + m2);

                        for (i = 0; i < provinceList.size(); i++) {
                            Log.d("000122", String.format("ProvinceData %s: %s, %s, %s", String.valueOf(i+1), provinceList.get(i)[0], provinceList.get(i)[1], provinceList.get(i)[2]));

                            boolean mFlag_Province = ls.executeNonQuery("insert or ignore into PROVINCE(uid, country_id, " +
                                    "name) values " +
                                    "(" +
                                    "'" + provinceList.get(i)[0] + "'," +
                                    "'" + provinceList.get(i)[1] + "'," +
                                    "'" + provinceList.get(i)[2] + "'" +
                                    ")");

                            Log.d("000122", "Province Query:" + String.valueOf(i+1) + "-" + mFlag_Province);

                            if (mFlag_Province == true) {
                                Log.d("000122", "********** TRUE ********* ");


                                // Update the progress bar
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("000122", "********** HANDLERRRRRR ********* ");

                                        tvcount.setText(String.valueOf(i+1) + "/" + provinceList.size());
                                    }
                                });

                                try {
                                    Thread.sleep(2000);
                                    Log.d("000122", "************************* ");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                                if (i == provinceList.size() - 1) {
                                    Log.d("000122", "IF LENGTH ");
                                    Log.d("000122", "ALL DATA INSERTED SUCCESSFULLY !!!!!!!!!!!!!!!!!!!!!");

                                    final Snackbar snackbar = Snackbar.make(v, R.string.provinceSynced, Snackbar.LENGTH_SHORT);
                                    View mySbView = snackbar.getView();
                                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.WHITE);
                                    textView.setTextSize(16);
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                    }
                                    snackbar.setDuration(3000);
                                    snackbar.show();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("000122", "FINISH !!!!!");

                                            pbProgress.setVisibility(View.GONE);
                                            image_done.setVisibility(View.VISIBLE);

                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d("000122", "+++++++++++++++++++ ");
                                                    progressDialog.dismiss();

                                                    Count_ProvinceRecord();
                                                }
                                            }, 2000);
                                        }
                                    });

                                } else {
                                    Log.d("000122", "ELSEEE LENGTH ");
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                                Log.d("000122", "********** FALSE ********* ");
                            }
                        }
                    /*}
                }, 2000);*/

                    }
                }).start(); // Start the operation
            }

        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(ctx, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("000122", "Province FILE ERROR: " + e.getMessage());
        }


    }

    private void Sync_DistrictData(final View v) {

        try {
            final File directory = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "HayatPK"
                    + File.separator + "Dictionaries"
                    + File.separator + "list_districts_pk.csv");
            if (!directory.exists()) {
                final Snackbar snackbar = Snackbar.make(v, R.string.noFileInFolder, Snackbar.LENGTH_SHORT);
                View mySbView = snackbar.getView();
                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(16);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                }
                snackbar.setDuration(3000);
                snackbar.show();
                return;
            } else {

                progressDialog = new Dialog(ctx);
                LayoutInflater layout = LayoutInflater.from(ctx);
                final View dialogView = layout.inflate(R.layout.lay_dialog_syncing_loading2, null);
                progressDialog.setContentView(dialogView);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.show();


                //TextView
                tvcount = dialogView.findViewById(R.id.tvcount);
                tvcount.setVisibility(View.VISIBLE);

                //ImageView
                image_done = dialogView.findViewById(R.id.image_done);

                //ProgressBar
                pbProgress = dialogView.findViewById(R.id.pbProgress);


               /* new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {*/

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        InputStream inputStream = null;
                        try {
                            inputStream = getContentResolver().openInputStream(Uri.fromFile(directory));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        CSVFile csvFile = new CSVFile(inputStream);
                        final List<String[]> districtList = csvFile.read();
                        Log.d("000122", "DistrictList SIZE:" + districtList.size());

                        tvcount.setText("0" + "/" + districtList.size());

                        ls.createAndOpenDB();
                        boolean m1 = ls.executeNonQuery("DROP TABLE IF EXISTS DISTRICT ");
                        Log.d("000122", "DISTRICT TABLE DROP: " + m1);
                        boolean m2 = ls.executeNonQuery(CREATE_TABLE_DISTRICT);
                        Log.d("000122", "DISTRICT TABLE CREATED: " + m2);

                        for (i = 0; i < districtList.size(); i++) {
                            Log.d("000122", String.format("DistrictData %s: %s, %s, %s", String.valueOf(i+1), districtList.get(i)[0], districtList.get(i)[1], districtList.get(i)[2]));

                            boolean mFlag_District = ls.executeNonQuery("insert or ignore into DISTRICT(uid,country_id,province_id, " +
                                    "name) values " +
                                    "(" +
                                    "'" + districtList.get(i)[0] + "'," +
                                    "'" + districtList.get(i)[1] + "'," +
                                    "'" + districtList.get(i)[2] + "'," +
                                    "'" + districtList.get(i)[3] + "'" +
                                    ")");

                            Log.d("000122", "DISTRICT Query:" + String.valueOf(i+1)+ "-" + mFlag_District);

                            if (mFlag_District == true) {
                                Log.d("000122", "********** TRUE ********* ");


                                // Update the progress bar
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("000122", "********** HANDLERRRRRR ********* ");

                                        tvcount.setText(String.valueOf(i+1) + "/" + districtList.size());
                                    }
                                });

                                try {
                                    Thread.sleep(2000);
                                    Log.d("000122", "************************* ");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                                if (i == districtList.size() - 1) {
                                    Log.d("000122", "IF LENGTH ");
                                    Log.d("000122", "DISTRICT DATA INSERTED SUCCESSFULLY !!!!!!!!!!!!!!!!!!!!!");

                                  //  progressDialog.dismiss();

                                    final Snackbar snackbar = Snackbar.make(v, R.string.districtSynced, Snackbar.LENGTH_SHORT);
                                    View mySbView = snackbar.getView();
                                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.WHITE);
                                    textView.setTextSize(16);
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                    }
                                    snackbar.setDuration(3000);
                                    snackbar.show();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("000122", "FINISH !!!!!");

                                            pbProgress.setVisibility(View.GONE);
                                            image_done.setVisibility(View.VISIBLE);

                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d("000122", "+++++++++++++++++++ ");
                                                    progressDialog.dismiss();

                                                    Count_DistrictRecord();
                                                }
                                            }, 2000);
                                        }
                                    });

                                } else {
                                    Log.d("000122", "ELSEEE LENGTH ");
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                                Log.d("000122", "********** FALSE ********* ");
                            }
                        }
                    /*}
                }, 2000);*/
                    }
                }).start(); // Start the operation


            }

        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(ctx, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("000122", "DISTRICT FILE ERROR: " + e.getMessage());
        }

    }

    private void Sync_TehsilData(final View v) {
        try {
            final File directory = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "HayatPK"
                    + File.separator + "Dictionaries"
                    + File.separator + "list_tehsils_pk.csv");
            if (!directory.exists()) {
                final Snackbar snackbar = Snackbar.make(v, R.string.noFileInFolder, Snackbar.LENGTH_SHORT);
                View mySbView = snackbar.getView();
                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(16);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                }
                snackbar.setDuration(3000);
                snackbar.show();
                return;
            } else {

                progressDialog = new Dialog(ctx);
                LayoutInflater layout = LayoutInflater.from(ctx);
                final View dialogView = layout.inflate(R.layout.lay_dialog_syncing_loading2, null);
                progressDialog.setContentView(dialogView);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.show();


                //TextView
                tvcount = dialogView.findViewById(R.id.tvcount);
                tvcount.setVisibility(View.VISIBLE);

                //ImageView
                image_done = dialogView.findViewById(R.id.image_done);

                //ProgressBar
                pbProgress = dialogView.findViewById(R.id.pbProgress);


               /* new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {*/

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        InputStream inputStream = null;
                        try {
                            inputStream = getContentResolver().openInputStream(Uri.fromFile(directory));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        CSVFile csvFile = new CSVFile(inputStream);
                        final List<String[]> tehsilsList = csvFile.read();
                        Log.d("000122", "TehsilsList SIZE:" + tehsilsList.size());

                        tvcount.setText("0" + "/" + tehsilsList.size());

                        ls.createAndOpenDB();
                        boolean m1 = ls.executeNonQuery("DROP TABLE IF EXISTS TEHSIL ");
                        Log.d("000122", "TEHSIL TABLE DROP: " + m1);
                        boolean m2 = ls.executeNonQuery(CREATE_TABLE_TEHSIL);
                        Log.d("000122", "TEHSIL TABLE CREATED: " + m2);


                        for (i = 0; i < tehsilsList.size(); i++) {
                            Log.d("000122", String.format("TehsilData %s: %s, %s, %s", String.valueOf(i+1), tehsilsList.get(i)[0], tehsilsList.get(i)[1], tehsilsList.get(i)[2]));

                            boolean mFlag_Tehsil = ls.executeNonQuery("insert or ignore into TEHSIL(uid,country_id,province_id, " +
                                    "district_id,name) values " +
                                    "(" +
                                    "'" + tehsilsList.get(i)[0] + "'," +
                                    "'" + tehsilsList.get(i)[1] + "'," +
                                    "'" + tehsilsList.get(i)[2] + "'," +
                                    "'" + tehsilsList.get(i)[3] + "'," +
                                    "'" + tehsilsList.get(i)[4] + "'" +
                                    ")");

                            Log.d("000122", "TEHSIL Query:" + String.valueOf(i+1) + "-" + mFlag_Tehsil);

                            if (mFlag_Tehsil == true) {
                                Log.d("000122", "********** TRUE ********* ");


                                // Update the progress bar
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("000122", "********** HANDLERRRRRR ********* ");

                                        tvcount.setText(String.valueOf(i+1) + "/" + tehsilsList.size());
                                    }
                                });

                                try {
                                    Thread.sleep(2000);
                                    Log.d("000122", "************************* ");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (i == tehsilsList.size() - 1) {
                                    Log.d("000122", "IF LENGTH ");
                                    Log.d("000122", "TEHSIL DATA INSERTED SUCCESSFULLY !!!!!!!!!!!!!!!!!!!!!");

                                    //progressDialog.dismiss();

                                    final Snackbar snackbar = Snackbar.make(v, R.string.tehsilSynced, Snackbar.LENGTH_SHORT);
                                    View mySbView = snackbar.getView();
                                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.WHITE);
                                    textView.setTextSize(16);
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                    }
                                    snackbar.setDuration(3500);
                                    snackbar.show();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("000122", "FINISH !!!!!");

                                            //tvcount.setText(i +" / "+ucsList.size());

                                            pbProgress.setVisibility(View.GONE);
                                            image_done.setVisibility(View.VISIBLE);

                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d("000122", "+++++++++++++++++++ ");
                                                    progressDialog.dismiss();

                                                    Count_TehsilRecord();
                                                }
                                            }, 2000);
                                        }
                                    });
                                } else {
                                    Log.d("000122", "ELSEEE LENGTH ");
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                                Log.d("000122", "********** FALSE ********* ");
                            }
                        }
                   /* }
                },2000);*/
                    }
                }).start(); // Start the operation
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(ctx, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("000122", "TEHSIL FILE ERROR: " + e.getMessage());
        }

    }

    private void Sync_UnionCouncilData(final View v) {

        try {
            final File directory = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "HayatPK"
                    + File.separator + "Dictionaries"
                    + File.separator + "list_ucs_pk.csv");
            if (!directory.exists()) {
                final Snackbar snackbar = Snackbar.make(v, R.string.noFileInFolder, Snackbar.LENGTH_SHORT);
                View mySbView = snackbar.getView();
                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(16);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                }
                snackbar.setDuration(3000);
                snackbar.show();
                return;
            } else {

                progressDialog = new Dialog(ctx);
                LayoutInflater layout = LayoutInflater.from(ctx);
                final View dialogView = layout.inflate(R.layout.lay_dialog_syncing_loading2, null);
                progressDialog.setContentView(dialogView);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.show();


                //TextView
                tvcount = dialogView.findViewById(R.id.tvcount);
                tvcount.setVisibility(View.VISIBLE);

                //ImageView
                image_done = dialogView.findViewById(R.id.image_done);

                //ProgressBar
                pbProgress = dialogView.findViewById(R.id.pbProgress);

                //ImageView
                image_done = dialogView.findViewById(R.id.image_done);

               /* new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {*/

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        InputStream inputStream = null;
                        try {
                            inputStream = getContentResolver().openInputStream(Uri.fromFile(directory));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        CSVFile csvFile = new CSVFile(inputStream);
                        final List<String[]> ucsList = csvFile.read();
                        Log.d("000122", "UCsList SIZE:" + ucsList.size());

                        tvcount.setText("0" + "/" + ucsList.size());

                        ls.createAndOpenDB();
                        boolean m1 = ls.executeNonQuery("DROP TABLE IF EXISTS UNIONCOUNCIL ");
                        Log.d("000122", "UNIONCOUNCIL TABLE DROP: " + m1);
                        boolean m2 = ls.executeNonQuery(CREATE_TABLE_UNIONCOUNCIL);
                        Log.d("000122", "UNIONCOUNCIL TABLE CREATED: " + m2);


                        for (i = 0; i < ucsList.size(); i++) {
                            Log.d("000122", String.format("UcsData %s: %s, %s, %s", String.valueOf(i+1), ucsList.get(i)[0], ucsList.get(i)[1], ucsList.get(i)[2]));

                            boolean mFlag_Ucs=  ls.executeNonQuery("insert or ignore into UNIONCOUNCIL(uid,country_id,province_id, " +
                                    "district_id,tehsil_id,name) values " +
                                    "(" +
                                    "'" + ucsList.get(i)[0] + "'," +
                                    "'" + ucsList.get(i)[1] + "'," +
                                    "'" + ucsList.get(i)[2] + "'," +
                                    "'" + ucsList.get(i)[3] + "'," +
                                    "'" + ucsList.get(i)[4] + "'," +
                                    "'" + ucsList.get(i)[5] + "'" +
                                    ")");

                            Log.d("000122", "UNIONCOUNCIL Query:" +String.valueOf(i+1) + "-" + mFlag_Ucs);

                            if (mFlag_Ucs == true) {
                                Log.d("000122", "********** TRUE ********* ");

                                // Update the progress bar
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("000122", "********** HANDLERRRRRR ********* ");
                                        tvcount.setText(String.valueOf(i + 1) + "/" + ucsList.size());
                                    }
                                });

                                try {
                                    Thread.sleep(1000);
                                    Log.d("000122", "************************* ");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (i == ucsList.size() - 1) {
                                    Log.d("000122", "IF LENGTH ");
                                    Log.d("000122", "UNIONCOUNCIL DATA INSERTED SUCCESSFULLY !!!!!!!!!!!!!!!!!!!!!");

                                    //progressDialog.dismiss();

                                    final Snackbar snackbar = Snackbar.make(v, R.string.ucSynced, Snackbar.LENGTH_SHORT);
                                    View mySbView = snackbar.getView();
                                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.WHITE);
                                    textView.setTextSize(16);
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                    }
                                    snackbar.setDuration(3500);
                                    snackbar.show();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("000122", "FINISH");

                                            pbProgress.setVisibility(View.GONE);
                                            image_done.setVisibility(View.VISIBLE);

                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d("000122", "+++++++++++++++++++ ");
                                                    progressDialog.dismiss();

                                                    Count_UnionCouncilRecord();
                                                }
                                            }, 2000);
                                        }
                                    });
                                } else {
                                    Log.d("000122", "ELSEEE LENGTH ");
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                                Log.d("000122", "********** FALSE ********* ");
                            }
                        }
                   /* }
                },2000);*/
                    }
                }).start(); // Start the operation
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(ctx, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("000122", "UNIONCOUNCIL FILE ERROR: " + e.getMessage());
        }
    }

    private void Sync_FacilityData(final View v) {


        try {
            final File directory = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "HayatPK"
                    + File.separator + "Dictionaries"
                    + File.separator + "list_facilities_pk.csv");
            if (!directory.exists()) {
                final Snackbar snackbar = Snackbar.make(v, R.string.noFileInFolder, Snackbar.LENGTH_SHORT);
                View mySbView = snackbar.getView();
                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(16);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                }
                snackbar.setDuration(3000);
                snackbar.show();
                return;
            } else {

                progressDialog = new Dialog(ctx);
                LayoutInflater layout = LayoutInflater.from(ctx);
                final View dialogView = layout.inflate(R.layout.lay_dialog_syncing_loading2, null);
                progressDialog.setContentView(dialogView);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.show();


                //TextView
                tvcount = dialogView.findViewById(R.id.tvcount);
                tvcount.setVisibility(View.VISIBLE);

                //ImageView
                image_done = dialogView.findViewById(R.id.image_done);

                //ProgressBar
                pbProgress = dialogView.findViewById(R.id.pbProgress);

                //ImageView
                image_done = dialogView.findViewById(R.id.image_done);

               /* new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {*/

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        InputStream inputStream = null;
                        try {
                            inputStream = getContentResolver().openInputStream(Uri.fromFile(directory));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        CSVFile csvFile = new CSVFile(inputStream);
                        final List<String[]> facilitiesList = csvFile.read();
                        Log.d("000122", "FacilitiesList SIZE:" + facilitiesList.size());

                        tvcount.setText("0" + "/" + facilitiesList.size());

                        ls.createAndOpenDB();
                        boolean m1 = ls.executeNonQuery("DROP TABLE IF EXISTS FACILITY ");
                        Log.d("000122", "FACILITY TABLE DROP: " + m1);
                        boolean m2 = ls.executeNonQuery(CREATE_TABLE_FACILITY);
                        Log.d("000122", "FACILITY TABLE CREATED: " + m2);


                        for (i = 0; i < facilitiesList.size(); i++) {
                            Log.d("000122", String.format("FACILITYData %s: %s, %s, %s", String.valueOf(i+1), facilitiesList.get(i)[0], facilitiesList.get(i)[1], facilitiesList.get(i)[2]));

                            boolean mFlag_Facilities=  ls.executeNonQuery("insert or ignore into FACILITY(uid,country_id,province_id, " +
                                    "district_id,tehsil_id,uc_id,name) values " +
                                    "(" +
                                    "'" + facilitiesList.get(i)[0] + "'," +
                                    "'" + facilitiesList.get(i)[1] + "'," +
                                    "'" + facilitiesList.get(i)[2] + "'," +
                                    "'" + facilitiesList.get(i)[3] + "'," +
                                    "'" + facilitiesList.get(i)[4] + "'," +
                                    "'" + facilitiesList.get(i)[5] + "'," +
                                    "'" + facilitiesList.get(i)[6] + "'" +
                                    ")");

                            Log.d("000122", "FACILITY Query:" +String.valueOf(i+1) + "-" +mFlag_Facilities);

                            if (mFlag_Facilities == true) {
                                Log.d("000122", "********** TRUE ********* ");

                                // Update the progress bar
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("000122", "********** HANDLERRRRRR ********* ");
                                        tvcount.setText(String.valueOf(i+1) + "/" + facilitiesList.size());
                                    }
                                });

                                try {
                                    Thread.sleep(1000);
                                    Log.d("000122", "************************* ");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (i == facilitiesList.size() - 1) {
                                    Log.d("000122", "IF LENGTH ");
                                    Log.d("000122", "FACILITY DATA INSERTED SUCCESSFULLY !!!!!!!!!!!!!!!!!!!!!");

                                    //progressDialog.dismiss();

                                    final Snackbar snackbar = Snackbar.make(v, R.string.healthCenterSynced, Snackbar.LENGTH_SHORT);
                                    View mySbView = snackbar.getView();
                                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.WHITE);
                                    textView.setTextSize(16);
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                    }
                                    snackbar.setDuration(3500);
                                    snackbar.show();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("000122", "FINISH");

                                            pbProgress.setVisibility(View.GONE);
                                            image_done.setVisibility(View.VISIBLE);

                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d("000122", "+++++++++++++++++++ ");
                                                    progressDialog.dismiss();

                                                    Count_FacilityRecord();
                                                }
                                            }, 2000);
                                        }
                                    });
                                } else {
                                    Log.d("000122", "ELSEEE LENGTH ");
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                                Log.d("000122", "********** FALSE ********* ");
                            }
                        }
                   /* }
                },2000);*/
                    }
                }).start(); // Start the operation
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(ctx, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("000122", "FACILITY FILE ERROR: " + e.getMessage());
        }

    }

    private void Sync_VillagesData(final View v) {

        try {
            final File directory = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "HayatPK"
                    + File.separator + "Dictionaries"
                    + File.separator + "list_villages_pk.csv");
            if (!directory.exists()) {
                final Snackbar snackbar = Snackbar.make(v, R.string.noFileInFolder, Snackbar.LENGTH_SHORT);
                View mySbView = snackbar.getView();
                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(16);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                }
                snackbar.setDuration(3000);
                snackbar.show();
                return;
            } else {

                progressDialog = new Dialog(ctx);
                LayoutInflater layout = LayoutInflater.from(ctx);
                final View dialogView = layout.inflate(R.layout.lay_dialog_syncing_loading2, null);
                progressDialog.setContentView(dialogView);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.show();


                //TextView
                tvcount = dialogView.findViewById(R.id.tvcount);
                tvcount.setVisibility(View.VISIBLE);

                //ImageView
                image_done = dialogView.findViewById(R.id.image_done);

                //ProgressBar
                pbProgress = dialogView.findViewById(R.id.pbProgress);

                //ImageView
                image_done = dialogView.findViewById(R.id.image_done);

               /* new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {*/

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        InputStream inputStream = null;
                        try {
                            inputStream = getContentResolver().openInputStream(Uri.fromFile(directory));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        CSVFile csvFile = new CSVFile(inputStream);
                        final List<String[]> villagesList = csvFile.read();
                        Log.d("000122", "VillagesList SIZE:" + villagesList.size());

                        tvcount.setText("0" + "/" + villagesList.size());

                        ls.createAndOpenDB();
                        boolean m1 = ls.executeNonQuery("DROP TABLE IF EXISTS VILLAGES ");
                        Log.d("000122", "VILLAGES TABLE DROP: " + m1);
                        boolean m2 = ls.executeNonQuery(CREATE_TABLE_VILLAGES);
                        Log.d("000122", "VILLAGES TABLE CREATED: " + m2);


                        for (i = 0; i < villagesList.size(); i++) {
                            Log.d("000122", String.format("VILLAGESData %s: %s, %s, %s", String.valueOf(i+1), villagesList.get(i)[0], villagesList.get(i)[1], villagesList.get(i)[2]));

                            boolean mFlag_Villages= ls.executeNonQuery("insert or ignore into VILLAGES(uid,country_id,province_id, " +
                                    "district_id,tehsil_id,uc_id,name) values " +
                                    "(" +
                                    "'" + villagesList.get(i)[0] + "'," +
                                    "'" + villagesList.get(i)[1] + "'," +
                                    "'" + villagesList.get(i)[2] + "'," +
                                    "'" + villagesList.get(i)[3] + "'," +
                                    "'" + villagesList.get(i)[4] + "'," +
                                    "'" + villagesList.get(i)[5] + "'," +
                                    "'" + villagesList.get(i)[6] + "'" +
                                    ")");

                            Log.d("000122", "VILLAGES Query:" +String.valueOf(i+1) + "-" +mFlag_Villages);

                            if (mFlag_Villages == true) {
                                Log.d("000122", "********** TRUE ********* ");

                                // Update the progress bar
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("000122", "********** HANDLERRRRRR ********* ");
                                        tvcount.setText(String.valueOf(i+1) + "/" + villagesList.size());
                                    }
                                });

                                try {
                                    Thread.sleep(700);
                                    Log.d("000122", "************************* ");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (i == villagesList.size() - 1) {
                                    Log.d("000122", "IF LENGTH ");
                                    Log.d("000122", "VILLAGES DATA INSERTED SUCCESSFULLY !!!!!!!!!!!!!!!!!!!!!");

                                    //progressDialog.dismiss();

                                    final Snackbar snackbar = Snackbar.make(v, R.string.villageSynced, Snackbar.LENGTH_SHORT);
                                    View mySbView = snackbar.getView();
                                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.WHITE);
                                    textView.setTextSize(16);
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                    }
                                    snackbar.setDuration(3500);
                                    snackbar.show();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("000122", "FINISH");

                                            pbProgress.setVisibility(View.GONE);
                                            image_done.setVisibility(View.VISIBLE);

                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d("000122", "+++++++++++++++++++ ");
                                                    progressDialog.dismiss();

                                                    Count_VillagesRecord();
                                                }
                                            }, 2000);
                                        }
                                    });
                                } else {
                                    Log.d("000122", "ELSEEE LENGTH ");
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                                Log.d("000122", "********** FALSE ********* ");
                            }
                        }
                   /* }
                },2000);*/
                    }
                }).start(); // Start the operation
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(ctx, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("000122", "VILLAGES FILE ERROR: " + e.getMessage());
        }


    }

    private void Sync_UsersData(final View v) {

        try {
            final File directory = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "HayatPK"
                    + File.separator + "Dictionaries"
                    + File.separator + "list_users_pk.csv");
            if (!directory.exists()) {
                final Snackbar snackbar = Snackbar.make(v, R.string.noFileInFolder, Snackbar.LENGTH_SHORT);
                View mySbView = snackbar.getView();
                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(16);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                }
                snackbar.setDuration(3000);
                snackbar.show();
                return;
            } else {

                progressDialog = new Dialog(ctx);
                LayoutInflater layout = LayoutInflater.from(ctx);
                final View dialogView = layout.inflate(R.layout.lay_dialog_syncing_loading2, null);
                progressDialog.setContentView(dialogView);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.show();


                //TextView
                tvcount = dialogView.findViewById(R.id.tvcount);
                tvcount.setVisibility(View.VISIBLE);

                //ImageView
                image_done = dialogView.findViewById(R.id.image_done);

                //ProgressBar
                pbProgress = dialogView.findViewById(R.id.pbProgress);

                //ImageView
                image_done = dialogView.findViewById(R.id.image_done);

               /* new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {*/

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        InputStream inputStream = null;
                        try {
                            inputStream = getContentResolver().openInputStream(Uri.fromFile(directory));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        CSVFile csvFile = new CSVFile(inputStream);
                        final List<String[]> usersList = csvFile.read();
                        Log.d("000122", "UsersList SIZE:" + usersList.size());

                        tvcount.setText("0" + "/" + usersList.size());

                        ls.createAndOpenDB();
                        boolean m1 = ls.executeNonQuery("DROP TABLE IF EXISTS USERS ");
                        Log.d("000122", "USERS TABLE DROP: " + m1);
                        boolean m2 = ls.executeNonQuery(CREATE_TABLE_USERS);
                        Log.d("000122", "USERS TABLE CREATED: " + m2);


                        for (i = 0; i < usersList.size(); i++) {
                            Log.d("000122", String.format("UsersData %s: %s, %s, %s", String.valueOf(i+1), usersList.get(i)[0], usersList.get(i)[1], usersList.get(i)[2]));

                            boolean mFlag_Users= ls.executeNonQuery("insert or ignore into USERS(uid,privilege,username,password,salt,district_id,country_id,province_id, " +
                                    "uc_id) values " +
                                    "(" +
                                    "'" + usersList.get(i)[0] + "'," +
                                    "'" + usersList.get(i)[1] + "'," +
                                    "'" + usersList.get(i)[2] + "'," +
                                    "'" + usersList.get(i)[3] + "'," +
                                    "'" + usersList.get(i)[4] + "'," +
                                    "'" + usersList.get(i)[5] + "'," +
                                    "'" + usersList.get(i)[6] + "'," +
                                    "'" + usersList.get(i)[7] + "'," +
                                    "'" + usersList.get(i)[8] + "'" +
                                    ")");

                            Log.d("000122", "USERS Query:" +String.valueOf(i+1) + "-" +mFlag_Users);

                            if (mFlag_Users == true) {
                                Log.d("000122", "********** TRUE ********* ");

                                // Update the progress bar
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("000122", "********** HANDLERRRRRR ********* ");
                                        tvcount.setText(String.valueOf(i+1)  + "/" + usersList.size());
                                    }
                                });

                                try {
                                    Thread.sleep(600);
                                    Log.d("000122", "************************* ");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (i == usersList.size() - 1) {
                                    Log.d("000122", "IF LENGTH ");
                                    Log.d("000122", "USERS DATA INSERTED SUCCESSFULLY !!!!!!!!!!!!!!!!!!!!!");

                                    //progressDialog.dismiss();

                                    final Snackbar snackbar = Snackbar.make(v, R.string.userSynced, Snackbar.LENGTH_SHORT);
                                    View mySbView = snackbar.getView();
                                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.WHITE);
                                    textView.setTextSize(16);
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                    }
                                    snackbar.setDuration(3500);
                                    snackbar.show();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("000122", "FINISH");

                                            pbProgress.setVisibility(View.GONE);
                                            image_done.setVisibility(View.VISIBLE);

                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d("000122", "+++++++++++++++++++ ");
                                                    progressDialog.dismiss();

                                                    Count_UserRecord();
                                                }
                                            }, 2000);
                                        }
                                    });
                                } else {
                                    Log.d("000122", "ELSEEE LENGTH ");
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                                Log.d("000122", "********** FALSE ********* ");
                            }
                        }
                   /* }
                },2000);*/
                    }
                }).start(); // Start the operation
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(ctx, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("000122", "USERS FILE ERROR: " + e.getMessage());
        }

    }

    private void Sync_VaccinesData(final View v) {

        try {
            final File directory = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "HayatPK"
                    + File.separator + "Dictionaries"
                    + File.separator + "list_vaccines_pk.csv");
            if (!directory.exists()) {
                final Snackbar snackbar = Snackbar.make(v, R.string.noFileInFolder, Snackbar.LENGTH_SHORT);
                View mySbView = snackbar.getView();
                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(16);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                }
                snackbar.setDuration(3000);
                snackbar.show();
                return;
            } else {

                progressDialog = new Dialog(ctx);
                LayoutInflater layout = LayoutInflater.from(ctx);
                final View dialogView = layout.inflate(R.layout.lay_dialog_syncing_loading2, null);
                progressDialog.setContentView(dialogView);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.show();


                //TextView
                tvcount = dialogView.findViewById(R.id.tvcount);
                tvcount.setVisibility(View.VISIBLE);

                //ImageView
                image_done = dialogView.findViewById(R.id.image_done);

                //ProgressBar
                pbProgress = dialogView.findViewById(R.id.pbProgress);

                //ImageView
                image_done = dialogView.findViewById(R.id.image_done);

               /* new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {*/

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        InputStream inputStream = null;
                        try {
                            inputStream = getContentResolver().openInputStream(Uri.fromFile(directory));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                        CSVFile csvFile = new CSVFile(inputStream);
                        final List<String[]> vaccinesList = csvFile.read();
                        Log.d("000122", "VaccinesList SIZE:" + vaccinesList.size());

                        tvcount.setText("0" + "/" + vaccinesList.size());

                        ls.createAndOpenDB();
                        boolean m1 = ls.executeNonQuery("DROP TABLE IF EXISTS VACCINES ");
                        Log.d("000122", "VACCINES TABLE DROP: " + m1);
                        boolean m2 = ls.executeNonQuery(CREATE_TABLE_VACCINES);
                        Log.d("000122", "VACCINES TABLE CREATED: " + m2);

                        for (i = 0; i < vaccinesList.size(); i++) {
                            Log.d("000122", String.format("Vaccines Data %s: %s, %s, %s", String.valueOf(i+1), vaccinesList.get(i)[0], vaccinesList.get(i)[1], vaccinesList.get(i)[2], vaccinesList.get(i)[3]));

                            boolean mFlag_Vaccines = ls.executeNonQuery("insert or ignore into VACCINES(uid, defaulter_date,due_date, " +
                                    "name) values " +
                                    "(" +
                                    "'" + vaccinesList.get(i)[0] + "'," +
                                    "'" + vaccinesList.get(i)[1] + "'," +
                                    "'" + vaccinesList.get(i)[2] + "'," +
                                    "'" + vaccinesList.get(i)[3] + "'" +
                                    ")");

                            Log.d("000122", "VACCINES Query:" + String.valueOf(i+1) + "-" + mFlag_Vaccines);

                            if (mFlag_Vaccines == true) {
                                Log.d("000122", "********** TRUE ********* ");

                                // Update the progress bar
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("000122", "********** HANDLERRRRRR ********* ");
                                        tvcount.setText(String.valueOf(i+1)  + "/" + vaccinesList.size());
                                    }
                                });

                                try {
                                    Thread.sleep(800);
                                    Log.d("000122", "************************* ");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (i == vaccinesList.size() - 1) {
                                    Log.d("000122", "IF LENGTH ");
                                    Log.d("000122", "Vaccines DATA INSERTED SUCCESSFULLY !!!!!!!!!!!!!!!!!!!!!");

                                    //progressDialog.dismiss();

                                    final Snackbar snackbar = Snackbar.make(v, R.string.vaccSynced, Snackbar.LENGTH_SHORT);
                                    View mySbView = snackbar.getView();
                                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.WHITE);
                                    textView.setTextSize(16);
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                    }
                                    snackbar.setDuration(3500);
                                    snackbar.show();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("000122", "FINISH");

                                            pbProgress.setVisibility(View.GONE);
                                            image_done.setVisibility(View.VISIBLE);

                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d("000122", "+++++++++++++++++++ ");
                                                    progressDialog.dismiss();

                                                    Count_VaccineRecord();
                                                }
                                            }, 2000);
                                        }
                                    });
                                } else {
                                    Log.d("000122", "ELSEEE LENGTH ");
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                                Log.d("000122", "********** FALSE ********* ");
                            }
                        }
                   /* }
                },2000);*/
                    }
                }).start(); // Start the operation
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(ctx, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("000122", "Vaccines FILE ERROR: " + e.getMessage());
        }

    }

    private void Sync_MedicinesData(final View v) {

        try {
            final File directory = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "HayatPK"
                    + File.separator + "Dictionaries"
                    + File.separator + "list_medicines_pk.csv");
            if (!directory.exists()) {
                final Snackbar snackbar = Snackbar.make(v, R.string.noFileInFolder, Snackbar.LENGTH_SHORT);
                View mySbView = snackbar.getView();
                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(16);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                }
                snackbar.setDuration(3000);
                snackbar.show();
                return;
            } else {

                progressDialog = new Dialog(ctx);
                LayoutInflater layout = LayoutInflater.from(ctx);
                final View dialogView = layout.inflate(R.layout.lay_dialog_syncing_loading2, null);
                progressDialog.setContentView(dialogView);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.show();


                //TextView
                tvcount = dialogView.findViewById(R.id.tvcount);
                tvcount.setVisibility(View.VISIBLE);

                //ImageView
                image_done = dialogView.findViewById(R.id.image_done);

                //ProgressBar
                pbProgress = dialogView.findViewById(R.id.pbProgress);

                //ImageView
                image_done = dialogView.findViewById(R.id.image_done);

               /* new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {*/

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        InputStream inputStream = null;
                        try {
                            inputStream = getContentResolver().openInputStream(Uri.fromFile(directory));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }

                        CSVFile csvFile = new CSVFile(inputStream);
                        final List<String[]> medicinesList = csvFile.read();
                        Log.d("000122", "Medicines SIZE:" + medicinesList.size());

                        ls.createAndOpenDB();
                        boolean m1 = ls.executeNonQuery("DROP TABLE IF EXISTS MEDICINE ");
                        Log.d("000122", "MEDICINE TABLE DROP: " + m1);
                        boolean m2 = ls.executeNonQuery(CREATE_TABLE_MEDICINE);
                        Log.d("000122", "MEDICINE TABLE CREATED: " + m2);


                        tvcount.setText("0" + "/" + medicinesList.size());


                        for (i = 0; i < medicinesList.size(); i++) {
                            Log.d("000122", String.format("row %s: %s, %s, %s", i, medicinesList.get(i)[0], medicinesList.get(i)[1], medicinesList.get(i)[2]));

                            boolean mFlag_Medicine = ls.executeNonQuery("insert or ignore into MEDICINE(uid, name, " +
                                    "type) values " +
                                    "(" +
                                    "'" + medicinesList.get(i)[0] + "'," +
                                    "'" + medicinesList.get(i)[1] + "'," +
                                    "'" + medicinesList.get(i)[2] + "'" +
                                    ")");

                            Log.d("000122", "Medicines Query:" + String.valueOf(i+1) + "-" + mFlag_Medicine);


                            if (mFlag_Medicine == true) {
                                Log.d("000122", "********** TRUE ********* ");

                                // Update the progress bar
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("000122", "********** HANDLERRRRRR ********* ");
                                        tvcount.setText(String.valueOf(i+1)  + "/" + medicinesList.size());
                                    }
                                });

                                try {
                                    Thread.sleep(1000);
                                    Log.d("000122", "************************* ");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (i == medicinesList.size() - 1) {
                                    Log.d("000122", "IF LENGTH ");
                                    Log.d("000122", "Medicines DATA INSERTED SUCCESSFULLY !!!!!!!!!!!!!!!!!!!!!");

                                    //progressDialog.dismiss();

                                    final Snackbar snackbar = Snackbar.make(v, R.string.medSynced, Snackbar.LENGTH_SHORT);
                                    View mySbView = snackbar.getView();
                                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(Color.WHITE);
                                    textView.setTextSize(16);
                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                    }
                                    snackbar.setDuration(3500);
                                    snackbar.show();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("000122", "FINISH");

                                            pbProgress.setVisibility(View.GONE);
                                            image_done.setVisibility(View.VISIBLE);

                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.d("000122", "+++++++++++++++++++ ");
                                                    progressDialog.dismiss();

                                                    Count_MedicinesRecord();
                                                }
                                            }, 2000);
                                        }
                                    });
                                } else {
                                    Log.d("000122", "ELSEEE LENGTH ");
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                                Log.d("000122", "********** FALSE ********* ");
                            }
                        }
                   /* }
                },2000);*/
                    }
                }).start(); // Start the operation
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(ctx, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("000122", "Medicines FILE ERROR: " + e.getMessage());
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        try {

            txt_sync_country.setVisibility(View.GONE);
            txt_sync_province.setVisibility(View.GONE);
            txt_sync_district.setVisibility(View.GONE);
            txt_sync_tehsil.setVisibility(View.GONE);
            txt_sync_unioncounil.setVisibility(View.GONE);
            txt_sync_facility.setVisibility(View.GONE);
            txt_sync_villages.setVisibility(View.GONE);
            txt_sync_users.setVisibility(View.GONE);
            txt_sync_medicines.setVisibility(View.GONE);
            txt_sync_vaccines.setVisibility(View.GONE);


            pb_2.setVisibility(View.VISIBLE);
            pb_3.setVisibility(View.VISIBLE);
            pb_4.setVisibility(View.VISIBLE);
            pb_5.setVisibility(View.VISIBLE);
            pb_6.setVisibility(View.VISIBLE);
            pb_7.setVisibility(View.VISIBLE);
            pb_8.setVisibility(View.VISIBLE);
            pb_9.setVisibility(View.VISIBLE);
            pb_10.setVisibility(View.VISIBLE);

            Log.d("000122", "On Resume");

            Count_CountryRecord();

        } catch (Exception e) {
            Log.d("000122", "Catach Err: " + e.getMessage());
        }
    }

    private void Count_CountryRecord() {

        try {
            ls.createAndOpenDB();
            String[][] data_country = ls.executeReader("Select count(*) from COUNTRY");

            if (Integer.parseInt(data_country[0][0]) > 0) {

                txt_sync_country.setVisibility(View.VISIBLE);
                txt_sync_country.setText(data_country[0][0]);
                Log.d("000122", "COUNTRY Count: " + data_country[0][0]);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Count_ProvinceRecord();
                    }
                }, 300);

            } else {
                Log.d("000122", "Country ELSE: ");
                txt_sync_country.setVisibility(View.VISIBLE);
                txt_sync_country.setText("0");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Count_ProvinceRecord();
                    }
                }, 300);
            }


        } catch (Exception e) {

            txt_sync_country.setVisibility(View.VISIBLE);
            txt_sync_country.setText("0");
            Log.d("000122", "COUNTRY Err: " + e.getMessage());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Count_ProvinceRecord();
                }
            }, 300);
        }
    }

    private void Count_ProvinceRecord() {

        try {
            ls.createAndOpenDB();
            String[][] data_province = ls.executeReader("Select count(*) from PROVINCE");

            if (Integer.parseInt(data_province[0][0]) > 0) {
                pb_2.setVisibility(View.GONE);
                txt_sync_province.setVisibility(View.VISIBLE);
                txt_sync_province.setText(data_province[0][0]);
                Log.d("000122", "PROVINCE Count: " + data_province[0][0]);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Count_DistrictRecord();
                    }
                }, 300);
            } else {
                Log.d("000122", "Province ELSE: ");
                pb_2.setVisibility(View.GONE);
                txt_sync_province.setVisibility(View.VISIBLE);
                txt_sync_province.setText("0");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Count_DistrictRecord();
                    }
                }, 300);
            }

        } catch (Exception e) {
            pb_2.setVisibility(View.GONE);
            txt_sync_province.setVisibility(View.VISIBLE);
            txt_sync_province.setText("0");
            Log.d("000122", "PROVINCE Err: " + e.getMessage());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Count_DistrictRecord();
                }
            }, 300);
        }
    }

    private void Count_DistrictRecord() {

        try {
            ls.createAndOpenDB();
            String[][] data_district = ls.executeReader("Select count(*) from DISTRICT");

            if (Integer.parseInt(data_district[0][0]) > 0) {
                pb_3.setVisibility(View.GONE);
                txt_sync_district.setVisibility(View.VISIBLE);
                txt_sync_district.setText(data_district[0][0]);
                Log.d("000122", "DISTRICT Count: " + data_district[0][0]);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Count_TehsilRecord();
                    }
                }, 300);
            } else {
                Log.d("000122", "District ELSE: ");
                pb_3.setVisibility(View.GONE);
                txt_sync_district.setVisibility(View.VISIBLE);
                txt_sync_district.setText("0");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Count_TehsilRecord();
                    }
                }, 300);
            }

        } catch (Exception e) {
            pb_3.setVisibility(View.GONE);
            txt_sync_district.setVisibility(View.VISIBLE);
            txt_sync_district.setText("0");
            Log.d("000122", "DISTRICT Err: " + e.getMessage());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Count_TehsilRecord();
                }
            }, 300);
        }
    }

    private void Count_TehsilRecord() {

        try {
            ls.createAndOpenDB();
            String[][] data_tehsil = ls.executeReader("Select count(*) from TEHSIL");

            if (Integer.parseInt(data_tehsil[0][0]) > 0) {
                pb_4.setVisibility(View.GONE);
                txt_sync_tehsil.setVisibility(View.VISIBLE);
                txt_sync_tehsil.setText(data_tehsil[0][0]);
                Log.d("000122", "TEHSIL Count: " + data_tehsil[0][0]);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Count_UnionCouncilRecord();
                    }
                }, 300);
            } else {
                Log.d("000122", "TEHSIL ELSE: ");
                pb_4.setVisibility(View.GONE);
                txt_sync_tehsil.setVisibility(View.VISIBLE);
                txt_sync_tehsil.setText("0");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Count_UnionCouncilRecord();
                    }
                }, 300);
            }

        } catch (Exception e) {
            pb_4.setVisibility(View.GONE);
            txt_sync_tehsil.setVisibility(View.VISIBLE);
            txt_sync_tehsil.setText("0");
            Log.d("000122", "TEHSIL Err: " + e.getMessage());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Count_UnionCouncilRecord();
                }
            }, 300);
        }
    }

    private void Count_UnionCouncilRecord() {
        try {
            ls.createAndOpenDB();
            String[][] data_uc = ls.executeReader("Select count(*) from UNIONCOUNCIL");

            if (Integer.parseInt(data_uc[0][0]) > 0) {
                pb_5.setVisibility(View.GONE);
                txt_sync_unioncounil.setVisibility(View.VISIBLE);
                txt_sync_unioncounil.setText(data_uc[0][0]);
                Log.d("000122", "UNION-COUNCIL Count: " + data_uc[0][0]);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Count_FacilityRecord();
                    }
                }, 300);
            } else {
                Log.d("000122", "UC ELSE: ");
                pb_5.setVisibility(View.GONE);
                txt_sync_unioncounil.setVisibility(View.VISIBLE);
                txt_sync_unioncounil.setText("0");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Count_FacilityRecord();
                    }
                }, 300);
            }
        } catch (Exception e) {
            pb_5.setVisibility(View.GONE);
            txt_sync_unioncounil.setVisibility(View.VISIBLE);
            txt_sync_unioncounil.setText("0");
            Log.d("000122", "UNION-COUNCIL Err: " + e.getMessage());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Count_FacilityRecord();
                }
            }, 300);
        }
    }

    private void Count_FacilityRecord() {
        try {
            ls.createAndOpenDB();
            String[][] data_facility = ls.executeReader("Select count(*) from FACILITY");

            if (Integer.parseInt(data_facility[0][0]) > 0) {
                pb_6.setVisibility(View.GONE);
                txt_sync_facility.setVisibility(View.VISIBLE);
                txt_sync_facility.setText(data_facility[0][0]);
                Log.d("000122", "FACILITY Count: " + data_facility[0][0]);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Count_VillagesRecord();
                    }
                }, 300);
            } else {
                Log.d("000122", "FACILITY ELSE: ");
                pb_6.setVisibility(View.GONE);
                txt_sync_facility.setVisibility(View.VISIBLE);
                txt_sync_facility.setText("0");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Count_VillagesRecord();
                    }
                }, 300);
            }

        } catch (Exception e) {
            pb_6.setVisibility(View.GONE);
            txt_sync_facility.setVisibility(View.VISIBLE);
            txt_sync_facility.setText("0");
            Log.d("000122", "FACILITY Err: " + e.getMessage());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Count_VillagesRecord();
                }
            }, 300);
        }

    }

    private void Count_VillagesRecord() {
        try {
            ls.createAndOpenDB();
            String[][] data_villages = ls.executeReader("Select count(*) from VILLAGES");

            if (Integer.parseInt(data_villages[0][0]) > 0) {
                pb_7.setVisibility(View.GONE);
                txt_sync_villages.setVisibility(View.VISIBLE);
                txt_sync_villages.setText(data_villages[0][0]);
                Log.d("000122", "VILLAGES Count: " + data_villages[0][0]);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Count_UserRecord();
                    }
                }, 300);
            } else {
                Log.d("000122", "VILLAGES ELSE: ");
                pb_7.setVisibility(View.GONE);
                txt_sync_villages.setVisibility(View.VISIBLE);
                txt_sync_villages.setText("0");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Count_UserRecord();
                    }
                }, 300);
            }

        } catch (Exception e) {
            pb_7.setVisibility(View.GONE);
            txt_sync_villages.setVisibility(View.VISIBLE);
            txt_sync_villages.setText("0");
            Log.d("000122", "VILLAGES Err: " + e.getMessage());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Count_UserRecord();
                }
            }, 300);
        }

    }

    private void Count_UserRecord() {

        try {
            ls.createAndOpenDB();
            String[][] data_users = ls.executeReader("Select count(*) from USERS");

            if (Integer.parseInt(data_users[0][0]) > 0) {
                pb_8.setVisibility(View.GONE);
                txt_sync_users.setVisibility(View.VISIBLE);
                txt_sync_users.setText(data_users[0][0]);
                Log.d("000122", "USERS Count: " + data_users[0][0]);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Count_MedicinesRecord();
                    }
                }, 300);
            } else {
                Log.d("000122", "USERS ELSE: ");
                pb_8.setVisibility(View.GONE);
                txt_sync_users.setVisibility(View.VISIBLE);
                txt_sync_users.setText("0");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Count_MedicinesRecord();
                    }
                }, 300);
            }

        } catch (Exception e) {
            pb_8.setVisibility(View.GONE);
            txt_sync_users.setVisibility(View.VISIBLE);
            txt_sync_users.setText("0");
            Log.d("000122", "USERS Err: " + e.getMessage());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Count_MedicinesRecord();
                }
            }, 300);
        }


    }

    private void Count_MedicinesRecord() {
        try {
            ls.createAndOpenDB();
            String[][] data_medicine = ls.executeReader("Select count(*) from MEDICINE");

            if (Integer.parseInt(data_medicine[0][0]) > 0) {
                pb_9.setVisibility(View.GONE);
                txt_sync_medicines.setVisibility(View.VISIBLE);
                txt_sync_medicines.setText(data_medicine[0][0]);
                Log.d("000122", "MEDICINES Count: " + data_medicine[0][0]);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Count_VaccineRecord();
                    }
                }, 300);
            } else {
                Log.d("000122", "MEDICINES ELSE: ");
                pb_9.setVisibility(View.GONE);
                txt_sync_medicines.setVisibility(View.VISIBLE);
                txt_sync_medicines.setText("0");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Count_VaccineRecord();
                    }
                }, 300);
            }

        } catch (Exception e) {
            pb_9.setVisibility(View.GONE);
            txt_sync_medicines.setVisibility(View.VISIBLE);
            txt_sync_medicines.setText("0");
            Log.d("000122", "MEDICINES Err: " + e.getMessage());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Count_VaccineRecord();
                }
            }, 300);
        }

    }

    private void Count_VaccineRecord() {
        try {
            ls.createAndOpenDB();
            String[][] data_vaccines = ls.executeReader("Select count(*) from VACCINES");

            if (Integer.parseInt(data_vaccines[0][0]) > 0) {
                pb_10.setVisibility(View.GONE);
                txt_sync_vaccines.setVisibility(View.VISIBLE);
                txt_sync_vaccines.setText(data_vaccines[0][0]);
                Log.d("000122", "VACCINES Count: " + data_vaccines[0][0]);
            } else {
                Log.d("000122", "VACCINES ELSE: ");
                pb_10.setVisibility(View.GONE);
                txt_sync_vaccines.setVisibility(View.VISIBLE);
                txt_sync_vaccines.setText("0");
            }

        } catch (Exception e) {
            pb_10.setVisibility(View.GONE);
            txt_sync_vaccines.setVisibility(View.VISIBLE);
            txt_sync_vaccines.setText("0");
            Log.d("000122", "VACCINES Err: " + e.getMessage());
        }

    }

}
