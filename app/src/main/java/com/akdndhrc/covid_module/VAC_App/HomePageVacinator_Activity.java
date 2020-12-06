package com.akdndhrc.covid_module.VAC_App;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.Adt_Homepage;
import com.akdndhrc.covid_module.Adapter.Adt_VACExpandableList;
import com.akdndhrc.covid_module.DatabaseFiles.Helper;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.Login_Activity;
import com.akdndhrc.covid_module.VAC_App.VAC_InsideOutsideUC.VAC_AboveTwoProfile_Activity;
import com.akdndhrc.covid_module.VAC_App.covid_dashboard.CovidModule_Dashboard_Activity;
import com.akdndhrc.covid_module.VAC_App.covid_register_activity.register_individual_covid;
import com.akdndhrc.covid_module.VAC_App.VAC_InsideOutsideUC.VAC_BelowTwoProfileWithKhandan_Activity;
import com.akdndhrc.covid_module.VAC_App.VAC_InsideOutsideUC.VAC_BelowTwoProfile_Activity;
import com.akdndhrc.covid_module.VAC_App.VAC_InsideOutsideUC.VAC_BelowTwoRegister_Activity;
import com.akdndhrc.covid_module.VAC_App.VAC_InsideOutsideUC.VAC_Child_HifazitiTeekeyRecordList2_Activity;
import com.akdndhrc.covid_module.VAC_App.VAC_InsideOutsideUC.VAC_Mother_HifazitiTeekeyRecordList_Activity;
import com.akdndhrc.covid_module.VAC_App.VAC_InsideOutsideUC.VAC_QRCode_Activity;
import com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities.VAC_SearchActivities.VAC_Search_Activity;
import com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities.VAC_VideoActivities.VAC_VideoList_Activity;
import com.akdndhrc.covid_module.VAC_App.covid_register_activity.Register_House_covid;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.akdndhrc.covid_module.slider.SlideMenu;
import com.akdndhrc.covid_module.R;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;


public class HomePageVacinator_Activity extends Activity implements View.OnClickListener {

    Context ctx = HomePageVacinator_Activity.this;

    private SlideMenu mSlideMenu;

    RelativeLayout rl_navigation_drawer, rl_options;
    ListView lv;

    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    Adt_Homepage adt;
    ImageView iv_options, iv_navigation_drawer, iv_searchvillage;

    Adt_VACExpandableList listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    RelativeLayout rl_register, rl_search, rl_videos;
    String[][] mDatachild_without_qrcode;
    String[][] mDatachild_without_vacinated;
    String[][] mDatachild;
    String[][] mDatafemale;
    TextView date_dashboard;
    TextView username;
    String login_useruid, login_username, file_date, temp_khandan_value;
    LinearLayout ll_search_gaon;
    Lister ls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_page_vac);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, HomePageVacinator_Activity.class));

        Log.d("000777 ", "OnCreate_VAC: ");


        ls = new Lister(ctx);
        ls.createAndOpenDB();

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences("UserLogin", 0); // 0 - for private mode
            String usernaame = prefelse.getString("username", null); // getting String
            String shared_useruid = prefelse.getString("login_userid", null); // getting String
            login_useruid = shared_useruid;
            login_username = usernaame;
            Log.d("000654", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000654", "Shared Err:" + e.getMessage());
        }

        check_copy_dbfiles();

        //ListView
        lv = findViewById(R.id.lv);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.exp_list);

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_searchvillage = findViewById(R.id.iv_searchvillage);

        //Relativelayout
        rl_register = findViewById(R.id.rl_register);
        rl_search = findViewById(R.id.rl_search);
      //  rl_videos = findViewById(R.id.rl_videos);


        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        //TodayDate = dates.format(c.getTime());

        date_dashboard = findViewById(R.id.iv_date);
        username = findViewById(R.id.et_talash_gaon);

        Date date = new Date();
        // date_dashboard.setText(dates.format(c.getTime()));
        date_dashboard.setText(DateFormat.getDateInstance(DateFormat.FULL).format(date));
        // date_dashboard.setText(DateFormat.getDateInstance(DateFormat.FULL).format(date));
        // username.setText(login_username);

        ll_search_gaon = findViewById(R.id.ll_search_gaon);

        iv_navigation_drawer.setOnClickListener(this);

        rl_register.setOnClickListener(this);
        rl_search.setOnClickListener(this);
//        rl_videos.setOnClickListener(this);
        iv_searchvillage.setOnClickListener(this);


        update_tables();

        // preparing list data
        prepareListData();


        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {



                    Intent intent = new Intent(ctx, CovidModule_Dashboard_Activity.class);
                    intent.putExtra("u_id", mDatafemale[childPosition][2]);
                intent.putExtra("gender", mDatafemale[childPosition][8]);


                startActivity(intent);
                    return true;


            }
        });

        listAdapter = new Adt_VACExpandableList(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private void check_copy_dbfiles() {
        try {
            String folder = Environment.getExternalStorageDirectory() + File.separator + "HayatPK" + File.separator + "Backup DB Files" + File.separator;
            File root = new File(folder);

            final ArrayList<String> arrayList_dbfiles = new ArrayList<>();

            File[] files = root.listFiles();

            for (File file : files) {

                Log.d("000222", String.valueOf(file.getAbsoluteFile()));
                Log.d("000222", String.valueOf(file.getPath()));
                Log.d("000222", String.valueOf(file.getName()));

                arrayList_dbfiles.add(file.getName());

                StringTokenizer stringTokenizer = new StringTokenizer(file.getName(), "-");
                String name = stringTokenizer.nextToken();
                String name_year = stringTokenizer.nextToken();
                String name_month = stringTokenizer.nextToken();
                String name_date = stringTokenizer.nextToken();

                Log.d("000222", "Name :" + name);
                Log.d("000222", "Name Year :" + name_year);
                Log.d("000222", "Name Month :" + name_month);
                Log.d("000222", "Name Date :" + name_date);
                StringTokenizer stringTokenizer1 = new StringTokenizer(name_date, ".");
                String date = stringTokenizer1.nextToken();
                Log.d("000222", "Date:" + date);

                file_date = name_year + "-" + name_month + "-" + date;
                Log.d("000222", "file_date:" + file_date);
            }
            Log.d("000222", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            try {

                SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                String TodayDate = dates.format(c.getTime());
                String DateFrom = file_date;
                Date date1;
                Date date2;
                date1 = dates.parse(TodayDate);
                date2 = dates.parse(DateFrom);
                long difference = Math.abs(date1.getTime() - date2.getTime());
                long differenceDates = (difference / (24 * 60 * 60 * 1000));
                String dayDifference = Long.toString(differenceDates);

                Log.e("000222", "TodayDate: " + TodayDate);
                Log.e("000222", "Register Date: " + DateFrom);
                Log.e("000222", "Days_Differernce: " + dayDifference);

                if (Integer.parseInt(dayDifference) == 7) {
                    Log.d("000222", "IF:");
                    copy_database_file("HayatPKDB");
                } else {
                    Log.d("000222", "IELSEEEEF:");
                }

            } catch (Exception e) {
                Log.d("000222", "Error" + e.getMessage());
            }
        } catch (Exception e) {
            Log.d("000222", "Copy DB Error:  " + e.getMessage());
        }
    }

    public void copy_database_file(String DATABASE_NAME) {
        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String timeStamp = dates.format(cal.getTime());
        Log.d("000222", "Today Date : " + timeStamp);

        OutputStream myOutput = null;
        InputStream myInput = null;

        try {
            File directory = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "HayatPK"
                    + File.separator + "Backup DB Files"
                    + File.separator);
            if (!directory.exists()) {
                directory.mkdir();
            }
            Log.d("000222", " testing db path: " + directory);

              /*  myOutput = new FileOutputStream(directory.getAbsolutePath()
                        + "/" + DATABASE_NAME+"-"+user_name+"-"+String.valueOf(System.currentTimeMillis()));*/

            myOutput = new FileOutputStream(Environment.getExternalStorageDirectory()
                    + File.separator + "HayatPK"
                    + File.separator + "Backup DB Files"
                    + File.separator + DATABASE_NAME + "_" + "Ver_" + Helper.DATABASE_VERSION + "-" + timeStamp + ".db");

            Log.d("000222", "Copy File: " + myOutput);

            myInput = new FileInputStream(Environment.getExternalStorageDirectory()
                    + File.separator + "HayatPK"
                    + File.separator + DATABASE_NAME);

            Log.d("000222", "PUTPUT: " + myInput);

            Log.d("000222", " testing db path 1" + String.valueOf(myInput));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
        } catch (Exception e) {
            Log.d("000222", " testing db path 2 " + e.getMessage());
        } finally {
            try {
                Log.d("000222", " testing 1");
                if (myOutput != null) {
                    myOutput.close();
                    myOutput = null;
                }
                if (myInput != null) {
                    myInput.close();
                    myInput = null;
                }
            } catch (Exception e) {
                Log.d("000222", " testing 2");
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_navigation_drawer:
                Intent intent = new Intent(HomePageVacinator_Activity.this, VAC_Navigation_Activity.class);
                startActivity(intent);
                //  overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                break;

            case R.id.iv_options:
                Toast.makeText(ctx, "Options", Toast.LENGTH_SHORT).show();
                break;

            case R.id.rl_register:


                Intent intent_reg = new Intent(ctx, register_individual_covid.class);
                startActivity(intent_reg);

           /*     final Dialog dialog = new Dialog(ctx);
                LayoutInflater layout = LayoutInflater.from(ctx);
                final View dialogView = layout.inflate(R.layout.fragment_register, null);

                dialog.setContentView(dialogView);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();

                ImageView iv_close = (ImageView) dialogView.findViewById(R.id.iv_close);

                Button btn_newFam = dialogView.findViewById(R.id.newFam);
                Button btn_newInd = dialogView.findViewById(R.id.newInd);


               /* iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

         /*       btn_newFam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ctx, Register_House_covid.class);
                        startActivity(intent);
                        dialog.dismiss();

                    }
                });

                btn_newInd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ctx, register_individual_covid.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
*/
                break;

            case R.id.rl_search:
                HomePageVacinator_Activity.this.startActivity(new Intent(HomePageVacinator_Activity.this, VAC_Search_Activity.class));
                break;

            case R.id.iv_searchvillage:
                HomePageVacinator_Activity.this.startActivity(new Intent(HomePageVacinator_Activity.this, VAC_SearchVillage_Activity.class));
                break;

          /*  case R.id.rl_videos:

                HomePageVacinator_Activity.this.startActivity(new Intent(HomePageVacinator_Activity.this, VAC_VideoList_Activity.class));
                break;
            default:*/
        }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        List<String> vacA_list = new ArrayList<String>();
        List<String> vacB_list = new ArrayList<String>();
        List<String> vacA_followup_list = new ArrayList<String>();
        List<String> vacB_followup_list = new ArrayList<String>();
        List<String> NotVaccinated_list = new ArrayList<String>();

        try {
            // Adding child data

            Lister ls = new Lister(HomePageVacinator_Activity.this);
            ls.createAndOpenDB();


            //Women age from Fourteen to 48
            String[][] mData_count_vacA = ls.executeReader("Select count(*) from MEMBER");
            listDataHeader.add("Shot due for Vaccine A" + "@" + mData_count_vacA[0][0]);
            Log.d("000654", "MotherCount: " + mData_count_vacA[0][0]);
            mDatafemale = ls.executeReader("Select *from MEMBER");
            mDatachild = ls.executeReader("Select *from MEMBER");



            if (mDatafemale != null) {
                for (int i = 0; i < mDatafemale.length; i++) {
                    Log.d("000654", "ID:: " + mDatafemale[i][0]);
                    Log.d("000654", "Manual_ID: " + mDatafemale[i][1]);
                    Log.d("000654", "uid: " + mDatafemale[i][2]);
                    Log.d("000654", "khandan_id: " + mDatafemale[i][3]);
                   /* Log.d("000654", "FullName: " + mDatafemale[i][4]);
                    Log.d("000654", "NicNumber: " + mDatafemale[i][5]);
                    Log.d("000654", "PhoneNumber: " + mDatafemale[i][6]);
                    Log.d("000654", "JSON Data: " + mDatafemale[i][7]);
                    /*Log.d("000654", "Gender: " + mDatafemale[i][8]);
                    Log.d("000654", "Age: " + mDatafemale[i][9]);
                    Log.d("000654", "DOB: " + mDatafemale[i][10]);
                    Log.d("000654", "BioCode: " + mDatafemale[i][11]);
                    Log.d("000654", "QRCode: " + mDatafemale[i][12]);
                    Log.d("000654", "AddedBy: " + mDatafemale[i][13]);
                    Log.d("000654", "isSynced: " + mDatafemale[i][14]);
                    Log.d("000654", "AddedOn: " + mDatafemale[i][15]);*/

                    // Log.d("000654", "lat: " + jsonObject.getString("father_name"));
                    // mother.add(mDatafemale[i][2] + "@" + mDatafemale[i][4]+ "@" + mDatafemale[i][8]+ "@" + mDatafemale[i][9] + "@" +String.valueOf(jsonObject.getString("father_name")));
                    vacA_list.add(mDatafemale[i][2] + "@" + mDatafemale[i][4] + "@" + mDatafemale[i][8] + "@" + mDatafemale[i][9]);

                    // list.add(map);
                }


            }




            //Women age from Fourteen to 48
            String[][] mData_count_vacB = ls.executeReader("Select count(*) from MEMBER");
            listDataHeader.add("Shot due for Vaccine B" + "@" + mData_count_vacB[0][0]);
            Log.d("000654", "MotherCount: " + mData_count_vacB[0][0]);
            mDatafemale = ls.executeReader("Select *from MEMBER");
            mDatachild = ls.executeReader("Select *from MEMBER");



            if (mDatafemale != null) {
                for (int i = 0; i < mDatafemale.length; i++) {
                    Log.d("000654", "ID:: " + mDatafemale[i][0]);
                    Log.d("000654", "Manual_ID: " + mDatafemale[i][1]);
                    Log.d("000654", "uid: " + mDatafemale[i][2]);
                    Log.d("000654", "khandan_id: " + mDatafemale[i][3]);
                   /* Log.d("000654", "FullName: " + mDatafemale[i][4]);
                    Log.d("000654", "NicNumber: " + mDatafemale[i][5]);
                    Log.d("000654", "PhoneNumber: " + mDatafemale[i][6]);
                    Log.d("000654", "JSON Data: " + mDatafemale[i][7]);
                    /*Log.d("000654", "Gender: " + mDatafemale[i][8]);
                    Log.d("000654", "Age: " + mDatafemale[i][9]);
                    Log.d("000654", "DOB: " + mDatafemale[i][10]);
                    Log.d("000654", "BioCode: " + mDatafemale[i][11]);
                    Log.d("000654", "QRCode: " + mDatafemale[i][12]);
                    Log.d("000654", "AddedBy: " + mDatafemale[i][13]);
                    Log.d("000654", "isSynced: " + mDatafemale[i][14]);
                    Log.d("000654", "AddedOn: " + mDatafemale[i][15]);*/

                    // Log.d("000654", "lat: " + jsonObject.getString("father_name"));
                    // mother.add(mDatafemale[i][2] + "@" + mDatafemale[i][4]+ "@" + mDatafemale[i][8]+ "@" + mDatafemale[i][9] + "@" +String.valueOf(jsonObject.getString("father_name")));
                    vacB_list.add(mDatafemale[i][2] + "@" + mDatafemale[i][4] + "@" + mDatafemale[i][8] + "@" + mDatafemale[i][9]);

                    // list.add(map);
                }


            }






            //Women age from Fourteen to 48
            String[][] mData_count_vacA_followup = ls.executeReader("Select count(*) from MEMBER");
            listDataHeader.add("Follow-up due for Vaccine A" + "@" + mData_count_vacA_followup[0][0]);
            Log.d("000654", "MotherCount: " + mData_count_vacA_followup[0][0]);
            mDatafemale = ls.executeReader("Select *from MEMBER");
            mDatachild = ls.executeReader("Select *from MEMBER");



            if (mDatafemale != null) {
                for (int i = 0; i < mDatafemale.length; i++) {
                    Log.d("000654", "ID:: " + mDatafemale[i][0]);
                    Log.d("000654", "Manual_ID: " + mDatafemale[i][1]);
                    Log.d("000654", "uid: " + mDatafemale[i][2]);
                    Log.d("000654", "khandan_id: " + mDatafemale[i][3]);
                   /* Log.d("000654", "FullName: " + mDatafemale[i][4]);
                    Log.d("000654", "NicNumber: " + mDatafemale[i][5]);
                    Log.d("000654", "PhoneNumber: " + mDatafemale[i][6]);
                    Log.d("000654", "JSON Data: " + mDatafemale[i][7]);
                    /*Log.d("000654", "Gender: " + mDatafemale[i][8]);
                    Log.d("000654", "Age: " + mDatafemale[i][9]);
                    Log.d("000654", "DOB: " + mDatafemale[i][10]);
                    Log.d("000654", "BioCode: " + mDatafemale[i][11]);
                    Log.d("000654", "QRCode: " + mDatafemale[i][12]);
                    Log.d("000654", "AddedBy: " + mDatafemale[i][13]);
                    Log.d("000654", "isSynced: " + mDatafemale[i][14]);
                    Log.d("000654", "AddedOn: " + mDatafemale[i][15]);*/

                    // Log.d("000654", "lat: " + jsonObject.getString("father_name"));
                    // mother.add(mDatafemale[i][2] + "@" + mDatafemale[i][4]+ "@" + mDatafemale[i][8]+ "@" + mDatafemale[i][9] + "@" +String.valueOf(jsonObject.getString("father_name")));
                    vacA_followup_list.add(mDatafemale[i][2] + "@" + mDatafemale[i][4] + "@" + mDatafemale[i][8] + "@" + mDatafemale[i][9]);

                    // list.add(map);
                }


            }







            //Women age from Fourteen to 48
            String[][] mData_count_vacB_followup = ls.executeReader("Select count(*) from MEMBER");
            listDataHeader.add("Follow-up due for Vaccine B" + "@" + mData_count_vacB_followup[0][0]);
            Log.d("000654", "MotherCount: " + mData_count_vacB_followup[0][0]);
            mDatafemale = ls.executeReader("Select *from MEMBER");
            mDatachild = ls.executeReader("Select *from MEMBER");



            if (mDatafemale != null) {
                for (int i = 0; i < mDatafemale.length; i++) {
                    Log.d("000654", "ID:: " + mDatafemale[i][0]);
                    Log.d("000654", "Manual_ID: " + mDatafemale[i][1]);
                    Log.d("000654", "uid: " + mDatafemale[i][2]);
                    Log.d("000654", "khandan_id: " + mDatafemale[i][3]);
                   /* Log.d("000654", "FullName: " + mDatafemale[i][4]);
                    Log.d("000654", "NicNumber: " + mDatafemale[i][5]);
                    Log.d("000654", "PhoneNumber: " + mDatafemale[i][6]);
                    Log.d("000654", "JSON Data: " + mDatafemale[i][7]);
                    /*Log.d("000654", "Gender: " + mDatafemale[i][8]);
                    Log.d("000654", "Age: " + mDatafemale[i][9]);
                    Log.d("000654", "DOB: " + mDatafemale[i][10]);
                    Log.d("000654", "BioCode: " + mDatafemale[i][11]);
                    Log.d("000654", "QRCode: " + mDatafemale[i][12]);
                    Log.d("000654", "AddedBy: " + mDatafemale[i][13]);
                    Log.d("000654", "isSynced: " + mDatafemale[i][14]);
                    Log.d("000654", "AddedOn: " + mDatafemale[i][15]);*/

                    // Log.d("000654", "lat: " + jsonObject.getString("father_name"));
                    // mother.add(mDatafemale[i][2] + "@" + mDatafemale[i][4]+ "@" + mDatafemale[i][8]+ "@" + mDatafemale[i][9] + "@" +String.valueOf(jsonObject.getString("father_name")));
                    vacB_followup_list.add(mDatafemale[i][2] + "@" + mDatafemale[i][4] + "@" + mDatafemale[i][8] + "@" + mDatafemale[i][9]);

                    // list.add(map);
                }


            }



            //Women age from Fourteen to 48
            String[][] mData_count_unvaccinated = ls.executeReader("Select count(*) from MEMBER");
            listDataHeader.add("Individuals not yet vaccinated" + "@" + mData_count_unvaccinated[0][0]);
            Log.d("000654", "MotherCount: " + mData_count_unvaccinated[0][0]);
            mDatafemale = ls.executeReader("Select *from MEMBER");
            mDatachild = ls.executeReader("Select *from MEMBER");



            if (mDatafemale != null) {
                for (int i = 0; i < mDatafemale.length; i++) {
                    Log.d("000654", "ID:: " + mDatafemale[i][0]);
                    Log.d("000654", "Manual_ID: " + mDatafemale[i][1]);
                    Log.d("000654", "uid: " + mDatafemale[i][2]);
                    Log.d("000654", "khandan_id: " + mDatafemale[i][3]);
                   /* Log.d("000654", "FullName: " + mDatafemale[i][4]);
                    Log.d("000654", "NicNumber: " + mDatafemale[i][5]);
                    Log.d("000654", "PhoneNumber: " + mDatafemale[i][6]);
                    Log.d("000654", "JSON Data: " + mDatafemale[i][7]);
                    /*Log.d("000654", "Gender: " + mDatafemale[i][8]);
                    Log.d("000654", "Age: " + mDatafemale[i][9]);
                    Log.d("000654", "DOB: " + mDatafemale[i][10]);
                    Log.d("000654", "BioCode: " + mDatafemale[i][11]);
                    Log.d("000654", "QRCode: " + mDatafemale[i][12]);
                    Log.d("000654", "AddedBy: " + mDatafemale[i][13]);
                    Log.d("000654", "isSynced: " + mDatafemale[i][14]);
                    Log.d("000654", "AddedOn: " + mDatafemale[i][15]);*/

                    // Log.d("000654", "lat: " + jsonObject.getString("father_name"));
                    // mother.add(mDatafemale[i][2] + "@" + mDatafemale[i][4]+ "@" + mDatafemale[i][8]+ "@" + mDatafemale[i][9] + "@" +String.valueOf(jsonObject.getString("father_name")));
                    NotVaccinated_list.add(mDatafemale[i][2] + "@" + mDatafemale[i][4] + "@" + mDatafemale[i][8] + "@" + mDatafemale[i][9]);

                    // list.add(map);
                }


            }









         /*   //Children under age two
            String[][] mData_count_child = ls.executeReader("Select count(*) from MEMBER where age <3 AND uid NOT IN (Select member_uid_2 from MEMBER_MERGERD)");
            Log.d("000654", "ChildCount: " + mData_count_child[0][0]);
            listDataHeader.add("2 سال سے کم عمر کے بچے" + "@" + mData_count_child[0][0]);
            mDatachild = ls.executeReader("Select *from MEMBER where age < 3 AND uid NOT IN (Select member_uid_2 from MEMBER_MERGERD) ORDER BY added_on DESC ");

            if (mDatachild != null) {

                for (int i = 0; i < mDatachild.length; i++) {
                    Log.d("000999", "ID:: " + mDatachild[i][0]);
                    Log.d("000999", "Manual_ID: " + mDatachild[i][1]);
                    Log.d("000999", "uid: " + mDatachild[i][2]);
                    Log.d("000999", "khandan_id: " + mDatachild[i][3]);
                    Log.d("000999", "JSON Data: " + mDatachild[i][7]);
                 *//*  Log.d("000999", "FullName: " + mDatachild[i][4]);
                    Log.d("000999", "NicNumber: " + mDatachild[i][5]);
                    Log.d("000999", "PhoneNumber: " + mDatachild[i][6]);
                    Log.d("000999", "Gender: " + mDatachild[i][8]);
                    Log.d("000999", "Age: " + mDatachild[i][9]);
                    Log.d("000999", "DOB: " + mDatachild[i][10]);
                    Log.d("000999", "BioCode: " + mDatachild[i][11]);
                    Log.d("000999", "QRCode: " + mDatachild[i][12]);
                    Log.d("000999", "AddedBy: " + mDatachild[i][13]);
                    Log.d("000999", "isSynced: " + mDatachild[i][14]);
                    Log.d("000999", "AddedOn: " + mDatachild[i][15]);*//*

                    // Log.d("000999", "lat: " + jsonObject.getString("father_name"));


                    final String mData_Khandan[][] = ls.executeReader("SELECT t1.full_name,t2.province_id,t2.district_id,t2.subdistrict_id,t2.uc_id,t2.village_id from MEMBER t1" +
                            " LEFT JOIN KHANDAN t2 On t1.khandan_id=t2.uid" +
                            " where t1.uid='" + mDatachild[i][2] + "'");

                    if (mData_Khandan[0][1] != null && mData_Khandan[0][2] != null && mData_Khandan[0][3] != null && mData_Khandan[0][4] != null && mData_Khandan[0][4] != null) {
                        Log.d("000123", "KHANDAN Already Register:");

                        temp_khandan_value = "0";

                    } else {
                        Log.d("000123", "KHANDAN Not Register:");
                        temp_khandan_value = "1";
                    }
                    child.add(mDatachild[i][2] + "@" + mDatachild[i][4] + "@" + mDatachild[i][8] + "@" + mDatachild[i][9] + "@" + temp_khandan_value);

                    // child.add(mDatachild[i][2] + "@" + mDatachild[i][4]+ "@" + mDatachild[i][8]+ "@" + mDatachild[i][9] + "@" +String.valueOf(jsonObject.getString("father_name")));
                }

            }*/

////////////////////            //Children without QR Code //////////////////////////
           /* String[][] mData_count_qr_child = ls.executeReader("Select count(*) from MEMBER where age <3 AND uid NOT IN (Select member_uid_2 from MEMBER_MERGERD) AND qr_code  LIKE 'no%'");
            Log.d("000654", "ChildCount: " + mData_count_qr_child[0][0]);
            listDataHeader.add("بنا QR کوڈ والے بچے" + "@" + mData_count_qr_child[0][0]);
            mDatachild_without_qrcode = ls.executeReader("Select *from MEMBER where age < 3 AND uid NOT IN (Select member_uid_2 from MEMBER_MERGERD) AND qr_code  LIKE 'no%' ORDER BY added_on DESC ");

            if (mDatachild_without_qrcode != null) {

                for (int i = 0; i < mDatachild_without_qrcode.length; i++) {
                *//*    Log.d("000999", "ID:: " + mDatachild_without_qrcode[i][0]);
                    Log.d("000999", "Manual_ID: " + mDatachild_without_qrcode[i][1]);
                    Log.d("000999", "uid: " + mDatachild_without_qrcode[i][2]);
                    Log.d("000999", "khandan_id: " + mDatachild_without_qrcode[i][3]);
                    Log.d("000999", "JSON Data: " + mDatachild_without_qrcode[i][7]);*//*

                    child_without_qrcode.add(mDatachild_without_qrcode[i][2] + "@" + mDatachild_without_qrcode[i][4] + "@" + mDatachild_without_qrcode[i][8] + "@" + mDatachild_without_qrcode[i][9]);
                }
            }


            //Children without any vaccine
            String[][] mData_count_vaccinated_child = ls.executeReader("Select count(*) from MEMBER where age < 2 AND uid NOT IN (Select member_uid_2 from MEMBER_MERGERD) AND uid NOT IN (Select member_uid from CVACCINATION)");
            Log.d("000654", "ChildCount: " + mData_count_vaccinated_child[0][0]);
            listDataHeader.add("بنا ویکسین انجام والے بچے" + "@" + mData_count_vaccinated_child[0][0]);
            mDatachild_without_vacinated = ls.executeReader("Select *from MEMBER where age < 2 AND uid NOT IN (Select member_uid_2 from MEMBER_MERGERD) AND uid NOT IN (Select member_uid from CVACCINATION) ORDER BY added_on DESC ");

            if (mDatachild_without_vacinated != null) {

                for (int i = 0; i < mDatachild_without_vacinated.length; i++) {
              *//*      Log.d("000999", "ID:: " + mDatachild_without_vacinated[i][0]);
                    Log.d("000999", "Manual_ID: " + mDatachild_without_vacinated[i][1]);
                    Log.d("000999", "uid: " + mDatachild_without_vacinated[i][2]);
                    Log.d("000999", "khandan_id: " + mDatachild_without_vacinated[i][3]);
                    Log.d("000999", "JSON Data: " + mDatachild_without_vacinated[i][7]);*//*

                    child_without_vacinated.add(mDatachild_without_vacinated[i][2] + "@" + mDatachild_without_vacinated[i][4] + "@" + mDatachild_without_vacinated[i][8] + "@" + mDatachild_without_vacinated[i][9]);
                }
            }
*/
            listDataChild.put(listDataHeader.get(0), vacA_list); // Header, Child data
            listDataChild.put(listDataHeader.get(1), vacB_list); // Header, Child dataa
            listDataChild.put(listDataHeader.get(2), vacA_followup_list); // Header, Child dataa
            listDataChild.put(listDataHeader.get(3), vacB_followup_list);
            listDataChild.put(listDataHeader.get(4), NotVaccinated_list); // Header, Child dataa
            expListView.setClickable(true);

        } catch (Exception e) {
            Log.d("000999", "Error: " + e.getMessage());

        }
    }

    public void update_tables() {

        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //////////////////// Update KHANDAN
                    /*Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/
                    try {
                        String update_khandan_addedby = "UPDATE KHANDAN SET " +
                                "added_by='" + login_useruid + "' " +
                                "WHERE added_by = 'null'";

                        ls.executeNonQuery(update_khandan_addedby);

                        Boolean res = ls.executeNonQuery(update_khandan_addedby);
                        Log.d("000369", "Update KHANDAN Added_by: " + update_khandan_addedby);
                        Log.d("000369", "Query: " + res.toString());
                    } catch (Exception e) {
                        Log.d("000369", "KHANDNA Added_By Catch: " + e.getMessage());
                    }
                }
            }, 500);

            //////////////////// Update MEMBER ////////////////////////////////
            Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    /*Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/
                    try {

                        String data[][] = ls.executeReader("Select data,uid from MEMBER");
                        for (int i = 0; i < data.length; i++) {

                            JSONObject jsonObject = new JSONObject(data[i][0]);
                            if (jsonObject.has("vaination_card_number")) {
                                Log.d("000963", "Data: " + data[i][0]);
                                jsonObject.put("vaccination_card_number", jsonObject.remove("vaination_card_number"));
                                Log.d("000963", "VAC RENAME: ");

                                String update_Member_json = "UPDATE MEMBER SET " +
                                        "data='" + jsonObject.toString() + "' " +
                                        "WHERE uid = '" + data[i][1] + "'";

                                ls.executeNonQuery(update_Member_json);

                                Boolean res = ls.executeNonQuery(update_Member_json);
                                Log.d("000963", "update_Member_json: " + update_Member_json);
                                Log.d("000963", "Query: " + res.toString());

                                continue;

                            } else {
                                Log.d("000963", "NO Vac RENAME : ");
                                continue;
                            }
                        }

                        String update_Member_addedby = "UPDATE MEMBER SET " +

                                "added_by='" + login_useruid + "' " +
                                "WHERE added_by = 'null'";

                        ls.executeNonQuery(update_Member_addedby);

                        Boolean res = ls.executeNonQuery(update_Member_addedby);
                        Log.d("000369", "Update_Member_addedby: " + update_Member_addedby);
                        Log.d("000369", "Query: " + res.toString());
                    } catch (Exception e) {
                        Log.d("000369", "MEMBER Added_By Catch: " + e.getMessage());
                    }

                }
            }, 1000);

            ////////////////////  Update Member Name /////////////////////////////////
            Handler handler22 = new Handler();
            handler22.postDelayed(new Runnable() {
                @Override
                public void run() {
                   /* Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/
                    try {
                        String update_Member_Name = "UPDATE MEMBER SET " +
                                "full_name='Unknown Child' " +
                                "WHERE full_name = ''";

                        ls.executeNonQuery(update_Member_Name);

                        Boolean res = ls.executeNonQuery(update_Member_Name);
                        Log.d("000369", "update_Member_Name: " + update_Member_Name);
                        Log.d("000369", "Query: " + res.toString());
                    } catch (Exception e) {
                        Log.d("000369", "update_Member_Name Name Catch: " + e.getMessage());
                    }
                }
            }, 500);


            ////////////////////  Update CVACCINATION /////////////////////////////////
            Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    /*Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/
                    try {
                        String update_CVACCINATION_addedby = "UPDATE CVACCINATION SET " +
                                "added_by='" + login_useruid + "' " +
                                "WHERE added_by = 'null'";

                        ls.executeNonQuery(update_CVACCINATION_addedby);

                        Boolean res = ls.executeNonQuery(update_CVACCINATION_addedby);
                        Log.d("000369", "update_CVACCINATION_addedby: " + update_CVACCINATION_addedby);
                        Log.d("000369", "Query: " + res.toString());
                    } catch (Exception e) {
                        Log.d("000369", "CVACCINATION Added_By Catch: " + e.getMessage());
                    }
                }
            }, 1500);


            //////////////////// Update MVACINE

            Handler handler3 = new Handler();
            handler3.postDelayed(new Runnable() {
                @Override
                public void run() {
                   /* Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/
                    try {
                        String update_MVACINE_addedby = "UPDATE MVACINE SET " +
                                "added_by='" + login_useruid + "' " +
                                "WHERE added_by = 'null'";

                        ls.executeNonQuery(update_MVACINE_addedby);

                        Boolean res = ls.executeNonQuery(update_MVACINE_addedby);
                        Log.d("000369", "update_MVACINE_addedby: " + update_MVACINE_addedby);
                        Log.d("000369", "Query: " + res.toString());
                    } catch (Exception e) {
                        Log.d("000369", "MVACINE Added_By Catch: " + e.getMessage());
                    }
                }
            }, 1500);


            //////////////////// Update VIDEOS ///////////////

            Handler handler4 = new Handler();
            handler4.postDelayed(new Runnable() {
                @Override
                public void run() {
                  /*  Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/
                    try {
                        String update_VIDEOS_addedby = "UPDATE VIDEOS SET " +
                                "added_by='" + login_useruid + "' " +
                                "WHERE added_by = 'null'";

                        ls.executeNonQuery(update_VIDEOS_addedby);

                        Boolean res = ls.executeNonQuery(update_VIDEOS_addedby);
                        Log.d("000369", "update_VIDEOS_addedby: " + update_VIDEOS_addedby);
                        Log.d("000369", "Query: " + res.toString());
                    } catch (Exception e) {
                        Log.d("000369", "VIDEOS Added_By Catch: " + e.getMessage());
                    }


                }
            }, 1500);


            //////////////////// Update LOGINS /////////////////

            Handler handler5 = new Handler();
            handler5.postDelayed(new Runnable() {
                @Override
                public void run() {
                   /* Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/
                    try {
                        String update_LOGINS_addedby = "UPDATE LOGINS SET " +
                                "uid='" + login_useruid + "' " +
                                "WHERE uid = 'null'";

                        ls.executeNonQuery(update_LOGINS_addedby);

                        Boolean res = ls.executeNonQuery(update_LOGINS_addedby);
                        Log.d("000369", "update_LOGINS_addedby: " + update_LOGINS_addedby);
                        Log.d("000369", "Query: " + res.toString());
                    } catch (Exception e) {
                        Log.d("000369", "LOGINS Added_By Catch: " + e.getMessage());
                    }
                }
            }, 1500);


            //////////////////// Update FEEDBACK /////////////////////

            Handler handler6 = new Handler();
            handler6.postDelayed(new Runnable() {
                @Override
                public void run() {
                 /*   Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/
                    try {
                        String update_FEEDBACK_addedby = "UPDATE FEEDBACK SET " +
                                "added_by='" + login_useruid + "' " +
                                "WHERE added_by = 'null'";

                        ls.executeNonQuery(update_FEEDBACK_addedby);

                        Boolean res = ls.executeNonQuery(update_FEEDBACK_addedby);
                        Log.d("000369", "update_FEEDBACK_addedby: " + update_FEEDBACK_addedby);
                        Log.d("000369", "Query: " + res.toString());
                    } catch (Exception e) {
                        Log.d("000369", "FEEDBACK Added_By Catch: " + e.getMessage());
                    }
                }
            }, 1500);

        } catch (Exception e) {
            Log.d("000369", "Updated Table Catch: " + e.getMessage());
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        try {
            if (VAC_BelowTwoRegister_Activity.var_regtemp_belowtwo.equalsIgnoreCase("1")) {
                Log.d("000987", "RESTART IF: ");
                finish();
                startActivity(getIntent());
                VAC_BelowTwoRegister_Activity.var_regtemp_belowtwo = "0";
            } else if (register_individual_covid.var_regtemp_abovetwo.equalsIgnoreCase("1")) {
                Log.d("000987", "RESTART IF: ");
                finish();
                startActivity(getIntent());
                register_individual_covid.var_regtemp_abovetwo = "0";
            } else if (VAC_BelowTwoProfile_Activity.var_updateprofile_belowtwotemp.equalsIgnoreCase("1")) {
                Log.d("000987", "RESTART ELSE IF: ");
                finish();
                startActivity(getIntent());
                VAC_BelowTwoProfile_Activity.var_updateprofile_belowtwotemp = "0";
            } else if (VAC_BelowTwoProfileWithKhandan_Activity.var_updateprofilekhandan_belowtwotemp.equalsIgnoreCase("1")) {
                Log.d("000987", "RESTART ELSE IF: ");
                finish();
                startActivity(getIntent());
                VAC_BelowTwoProfileWithKhandan_Activity.var_updateprofilekhandan_belowtwotemp = "0";
            } else if (VAC_AboveTwoProfile_Activity.var_updateprofile_abovetwotemp.equalsIgnoreCase("1")) {
                Log.d("000987", "RESTART ELSE IF: ");
                finish();
                startActivity(getIntent());
                VAC_AboveTwoProfile_Activity.var_updateprofile_abovetwotemp = "0";
            } else if (VAC_QRCode_Activity.vac_qr_merged.equalsIgnoreCase("1")) {
                Log.d("000987", "RESTART ELSE IF: ");
                finish();
                startActivity(getIntent());
                VAC_QRCode_Activity.vac_qr_merged = "0";
            } else {
                Log.d("000987", "RESTART ELSEEEEE: ");
                Log.d("000987", "RESTART ELSE IF: ");
                finish();
                startActivity(getIntent());
                VAC_BelowTwoProfile_Activity.var_updateprofile_belowtwotemp = "0";
            }
        } catch (Exception e) {
            Log.d("000987", "ON-RESTART: " + e.getMessage());
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        ls = new Lister(ctx);
        ls.createAndOpenDB();
    }


    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

       /* final Dialog logout_dialog = new Dialog(ctx);
        LayoutInflater layout = LayoutInflater.from(ctx);
        final View dialogView = layout.inflate(R.layout.dialog_logout, null);

        logout_dialog.setContentView(dialogView);
        logout_dialog.setCanceledOnTouchOutside(false);
        logout_dialog.setCancelable(false);
        logout_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id*
        logout_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        logout_dialog.show();*/


        final BottomSheetDialog mBottomLogout_dialog = new BottomSheetDialog(ctx, R.style.AppBottomSheetDialogTheme);
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_logout, null);
        mBottomLogout_dialog.setContentView(dialogView);
        mBottomLogout_dialog.setCanceledOnTouchOutside(false);
        mBottomLogout_dialog.setCancelable(false);
        mBottomLogout_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mBottomLogout_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_SlideUp; //style id
        mBottomLogout_dialog.show();


        TextView tvYesLogout = dialogView.findViewById(R.id.tvYesLogout);
        TextView tvNoLogout = dialogView.findViewById(R.id.tvNoLogout);
        final ProgressBar pbProgress = dialogView.findViewById(R.id.pbProgress);

        tvYesLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbProgress.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent newIntent = new Intent(ctx, Login_Activity.class);
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        finish();
                        startActivity(newIntent);
                    }
                }, 500);

            }
        });

        tvNoLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomLogout_dialog.dismiss();
            }
        });

        //(super.onBackPressed();

    }

}
