package com.akdndhrc.covid_module.LHW_App;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildProfileActivities.Child_Profile_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.Child_Dashboard_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_RegisterActivities.AboveTwo_Register_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_RegisterActivities.BelowTwo_Register_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_RegisterActivities.Register_House;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SearchActivities.ChildRegister_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SearchActivities.MotherRegister_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SearchActivities.Search_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_VideoActivities.VideoList_Activity;
import com.akdndhrc.covid_module.Adapter.Adt_ExpandableList;
import com.akdndhrc.covid_module.Adapter.Adt_Homepage;
import com.akdndhrc.covid_module.DatabaseFiles.Helper;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildHifazitiTeekeyRecordActivities.Child_HifazitiTeekeyRecordList2_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.Mother_Dashboard_Activity;
import com.akdndhrc.covid_module.Login_Activity;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.akdndhrc.covid_module.slider.SlideMenu;

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

import static com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHaamlaRecordActivities.Mother_HaamlaDashboard_Activity.var_pregInActive;
import static com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHaamlaRecordActivities.Mother_HaamlaRecordList_Activity.var_add_preg;
import static com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherProfileActivities.Mother_Profile_Activity.var_mother_pro;


public class HomePage_Activity extends AppCompatActivity implements View.OnClickListener {

    Context ctx = HomePage_Activity.this;

    private SlideMenu mSlideMenu;

    RelativeLayout rl_navigation_drawer, rl_options, rl_register, rl_search, rl_videos;
    ListView lv;
    SimpleAdapter simpleAdapter;
    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    Adt_Homepage adt;
    ImageView iv_options, iv_navigation_drawer, iv_searchvillage;

    Adt_ExpandableList listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    String[][] mDatafemale;
    String[][] mDatachild;
    String[][] mDatapreg;
    TextView date_dashboard;
    TextView username;
    String login_useruid, login_username, file_date;

    Lister ls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_page);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, HomePage_Activity.class));

      /*  setSlideRole(R.layout.activity_home_page);
        setSlideRole(R.layout.layout_primary_menu);

       ClassListener classListener = new ClassListener(HomePage_Activity.this, getSlideMenu());
        classListener.init();
        mSlideMenu = getSlideMenu();
        mSlideMenu.setEdgeSlideEnable(true);*/


        Log.d("000654 ", "OnCreate: ");


        ls = new Lister(ctx);
        ls.createAndOpenDB();

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences("UserLogin", 0); // 0 - for private mode
            String usernaame = prefelse.getString("username", null); // getting String
            String shared_useruid = prefelse.getString("login_userid", null); // getting String
            login_useruid = shared_useruid;
            login_username = usernaame;
            Log.d("000222", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000222", "Shared Err:" + e.getMessage());
        }


        check_copy_dbfiles();

        //ListView
        lv = findViewById(R.id.lv);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.exp_list);


        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_searchvillage = findViewById(R.id.iv_searchvillage);

        //RelativeLayout
        rl_register = findViewById(R.id.rl_register);
        rl_search = findViewById(R.id.rl_search);
        rl_videos = findViewById(R.id.rl_videos);


        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        //TodayDate = dates.format(c.getTime());

        date_dashboard = findViewById(R.id.iv_date);
        username = findViewById(R.id.et_talash_gaon);
        Date date = new Date();
        date_dashboard.setText(DateFormat.getDateInstance(DateFormat.FULL).format(date));


        iv_navigation_drawer.setOnClickListener(this);
        rl_register.setOnClickListener(this);
        rl_search.setOnClickListener(this);
        rl_videos.setOnClickListener(this);
        iv_searchvillage.setOnClickListener(this);


        // preparing list data

        update_tables();

        prepareListData();


        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                try {
                    if (groupPosition == 0) {
                        Intent intent = new Intent(ctx, Mother_Dashboard_Activity.class);
                        intent.putExtra("u_id", mDatafemale[childPosition][2]);
                        intent.putExtra("mother_name", mDatafemale[childPosition][4]);
                        startActivity(intent);
                    } else if (groupPosition == 1) {

                        JSONObject js = new JSONObject(mDatachild[childPosition][7]);
                        if (js.getString("member_type").equalsIgnoreCase("unlisted")) {
                            //   Intent intent = new Intent(ctx, Child_HifazitiTeekeyRecordList_Activity.class);
                            Intent intent = new Intent(ctx, Child_HifazitiTeekeyRecordList2_Activity.class);
                            intent.putExtra("u_id", mDatachild[childPosition][2]);
                            intent.putExtra("child_name", mDatachild[childPosition][4]);
                            intent.putExtra("child_gender", mDatachild[childPosition][8]);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(ctx, Child_Dashboard_Activity.class);
                            intent.putExtra("u_id", mDatachild[childPosition][2]);
                            intent.putExtra("child_name", mDatachild[childPosition][4]);
                            intent.putExtra("child_gender", mDatachild[childPosition][8]);
                            startActivity(intent);
                        }

                    } else if (groupPosition == 2) {
                        Intent intent = new Intent(ctx, Mother_Dashboard_Activity.class);
                        intent.putExtra("u_id", mDatapreg[childPosition][2]);
                        intent.putExtra("mother_name", mDatapreg[childPosition][4]);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ctx, "Position:  " + childPosition, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(ctx, "Something wrong!!", Toast.LENGTH_SHORT).show();
                }


                return false;
            }
        });

        listAdapter = new Adt_ExpandableList(this, listDataHeader, listDataChild);
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

                Intent intent = new Intent(HomePage_Activity.this, LHW_Navigation_Activity.class);
                startActivity(intent);
                //  overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);


                break;

            case R.id.rl_register:
                HomePage_Activity.this.startActivity(new Intent(HomePage_Activity.this, Register_House.class));
                break;

            case R.id.rl_search:
                HomePage_Activity.this.startActivity(new Intent(HomePage_Activity.this, Search_Activity.class));
                break;

            case R.id.iv_searchvillage:
                HomePage_Activity.this.startActivity(new Intent(HomePage_Activity.this, LHW_SearchVillage_Activity.class));
                break;

            case R.id.rl_videos:

                HomePage_Activity.this.startActivity(new Intent(HomePage_Activity.this, VideoList_Activity.class));

                break;
            default:
        }


    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        List<String> mother = new ArrayList<String>();
        List<String> child = new ArrayList<String>();
        List<String> preg_mother = new ArrayList<String>();

        try {
            // Adding child data
            Lister ls = new Lister(HomePage_Activity.this);
            ls.createAndOpenDB();


            //Women age from Fourteen to 48
            String[][] mData_count_mother = ls.executeReader("Select count(*) from MEMBER where gender = '0' AND age >= 15 and age <=49");
            listDataHeader.add("15 سے 49 سال کی عمر تک کی خواتین" + "@" + mData_count_mother[0][0]);
            Log.d("000654", "MotherCount: " + mData_count_mother[0][0]);
            mDatafemale = ls.executeReader("Select *from MEMBER where gender = '0' AND age >= 15 and age <=49 ORDER BY added_on DESC ");

            if (mDatafemale != null) {
                for (int i = 0; i < mDatafemale.length; i++) {
                    Log.d("000654", "ID:: " + mDatafemale[i][0]);
                    Log.d("000654", "Manual_ID: " + mDatafemale[i][1]);
                    Log.d("000654", "uid: " + mDatafemale[i][2]);
                    Log.d("000654", "khandan_id: " + mDatafemale[i][3]);
                   /* Log.d("000654", "FullName: " + mDatafemale[i][4]);
                    Log.d("000654", "NicNumber: " + mDatafemale[i][5]);
                    Log.d("000654", "PhoneNumber: " + mDatafemale[i][6]);*/
                    Log.d("000654", "JSON Data: " + mDatafemale[i][7]);
                    /*Log.d("000654", "Gender: " + mDatafemale[i][8]);
                    Log.d("000654", "Age: " + mDatafemale[i][9]);
                    Log.d("000654", "DOB: " + mDatafemale[i][10]);
                    Log.d("000654", "BioCode: " + mDatafemale[i][11]);
                    Log.d("000654", "QRCode: " + mDatafemale[i][12]);
                    Log.d("000654", "AddedBy: " + mDatafemale[i][13]);
                    Log.d("000654", "isSynced: " + mDatafemale[i][14]);
                    Log.d("000654", "AddedOn: " + mDatafemale[i][15]);*/


//                    String mother_name;
//                    if (mDatafemale[i][4].isEmpty())
//                    {
//                        mother_name="Unknown Mother";
//                    }
//                    else{
//                        mother_name=mDatafemale[i][4];
//                    }
                    mother.add(mDatafemale[i][2] + "@" + mDatafemale[i][4] + "@" + mDatafemale[i][8] + "@" + mDatafemale[i][9]);

                    // list.add(map);
                }
            }


            //Children under age two
            String[][] mData_count_child = ls.executeReader("Select count(*) from MEMBER where age < 3");
            Log.d("000654", "ChildCount: " + mData_count_child[0][0]);
            listDataHeader.add("2 سال سے کم عمر کے بچے" + "@" + mData_count_child[0][0]);
            mDatachild = ls.executeReader("Select *from MEMBER where age < 3 ORDER BY added_on DESC ");

            if (mDatachild != null) {

                for (int i = 0; i < mDatachild.length; i++) {
                    Log.d("000654", "ID:: " + mDatachild[i][0]);
                    Log.d("000654", "Manual_ID: " + mDatachild[i][1]);
                    Log.d("000654", "uid: " + mDatachild[i][2]);
                    Log.d("000654", "khandan_id: " + mDatachild[i][3]);
                   /* Log.d("000654", "FullName: " + mDatachild[i][4]);
                    Log.d("000654", "NicNumber: " + mDatachild[i][5]);
                    Log.d("000654", "PhoneNumber: " + mDatachild[i][6]);*/
                    Log.d("000654", "JSON Data: " + mDatachild[i][7]);
                   /* Log.d("000654", "Gender: " + mDatachild[i][8]);
                    Log.d("000654", "Age: " + mDatachild[i][9]);
                    Log.d("000654", "DOB: " + mDatachild[i][10]);
                    Log.d("000654", "BioCode: " + mDatachild[i][11]);
                    Log.d("000654", "QRCode: " + mDatachild[i][12]);
                    Log.d("000654", "AddedBy: " + mDatachild[i][13]);
                    Log.d("000654", "isSynced: " + mDatachild[i][14]);
                    Log.d("000654", "AddedOn: " + mDatachild[i][15]);*/


                    //jsonObject = new JSONObject(json);
                    //Log.d("000654", "lat: " + jsonObject.getString("father_name"));
                    // child.add(mDatachild[i][2] + "@" + mDatachild[i][4]+ "@" + mDatachild[i][8]+ "@" + mDatachild[i][9] + "@" +String.valueOf(jsonObject.getString("father_name")));

                   /* try {
                        String[][] data_village_name = ls.executeReader("SELECT t3.name from KHANDAN t1" +
                                " INNER JOIN VILLAGES t3 ON t1.village_id = t3.uid" +
                                " where  t1.uid ='" + mDatachild[i][3] + "' ");
                        Log.d("000654", "Villages NAme: " + data_village_name[0][0]);


                    }catch(Exception e)
                    {
                        Log.d("000666","Err village: " +e.getMessage());
                        continue;
                    }*/
                    /*String child_name;
                    if (mDatachild[i][4].isEmpty())
                    {
                        child_name="Unknown";
                    }
                    else{
                        child_name=mDatachild[i][4];
                    }*/
                    child.add(mDatachild[i][2] + "@" + mDatachild[i][4] + "@" + mDatachild[i][8] + "@" + mDatachild[i][9]);
                }

            }


            //Pregnant ladies
            String[][] mData_count_preg = ls.executeReader("Select count(*) from MEMBER where uid IN (SELECT member_uid FROM MPREGNANCY WHERE status = 1 AND JSON_EXTRACT(metadata,'$.status') IS '0')");
            Log.d("000654", "PregMotherCount: " + mData_count_preg[0][0]);
            listDataHeader.add("حاملہ خواتین" + "@" + mData_count_preg[0][0]);
            mDatapreg = ls.executeReader("Select *from MEMBER where uid IN (SELECT member_uid FROM MPREGNANCY WHERE status = 1 AND JSON_EXTRACT(metadata,'$.status') IS '0') ORDER BY added_on DESC ");


            if (mDatapreg != null) {
                for (int i = 0; i < mDatapreg.length; i++) {
                    Log.d("000654", "ID:: " + mDatapreg[i][0]);
                    Log.d("000654", "Manual_ID: " + mDatapreg[i][1]);
                    Log.d("000654", "uid: " + mDatapreg[i][2]);
                    Log.d("000654", "khandan_id: " + mDatapreg[i][3]);
                    Log.d("000654", "JSON Data: " + mDatapreg[i][7]);
                   /* Log.d("000654", "FullName: " + mDatapreg[i][4]);
                    Log.d("000654", "NicNumber: " + mDatapreg[i][5]);
                    Log.d("000654", "PhoneNumber: " + mDatapreg[i][6]);
                    Log.d("000654", "Gender: " + mDatapreg[i][8]);
                    Log.d("000654", "Age: " + mDatapreg[i][9]);
                    Log.d("000654", "DOB: " + mDatapreg[i][10]);
                    Log.d("000654", "BioCode: " + mDatapreg[i][11]);
                    Log.d("000654", "QRCode: " + mDatapreg[i][12]);
                    Log.d("000654", "AddedBy: " + mDatapreg[i][13]);
                    Log.d("000654", "isSynced: " + mDatapreg[i][14]);
                    Log.d("000654", "AddedOn: " + mDatapreg[i][15]);*/


                    preg_mother.add(mDatapreg[i][2] + "@" + mDatapreg[i][4] + "@" + mDatapreg[i][8] + "@" + mDatapreg[i][9]);
                }
            }


            listDataChild.put(listDataHeader.get(0), mother); // Header, Child data
            listDataChild.put(listDataHeader.get(1), child);
            listDataChild.put(listDataHeader.get(2), preg_mother);

            expListView.setClickable(true);

        } catch (Exception e) {

            Log.d("000654", "Error: " + e.getMessage());

          /*listDataHeader = new ArrayList<String>();
          listDataHeader.add("List of Mothers");
          listDataHeader.add("List of Child");
          listDataHeader.add("List of Pregnant Mothers");
          /*mother_child.add("one");
          child.add("two");
          preg.add("three");
          listDataChild.put(listDataHeader.get(0), mother_child); // Header, Child data
          listDataChild.put(listDataHeader.get(1), child);
          listDataChild.put(listDataHeader.get(2), preg);*/

        }
    }

    public void update_tables() {


        try {

            //////////////////// Update KHANDAN
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

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


            //////////////////// Update MEMBER //////////////////////

            Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {

                   /* Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/

                    try {
                        String update_Member_addedby = "UPDATE MEMBER SET " +
                                "added_by='" + login_useruid + "'" +
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


            Handler handler111 = new Handler();
            handler111.postDelayed(new Runnable() {
                @Override
                public void run() {

                   /* Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/

                    try {
                        String update_Member_name = "UPDATE MEMBER SET " +
                                "full_name='Unknown Name'" +
                                "WHERE full_name = ''";

                        ls.executeNonQuery(update_Member_name);

                        Boolean res = ls.executeNonQuery(update_Member_name);
                        Log.d("000369", "update_Member_name: " + update_Member_name);
                        Log.d("000369", "Query: " + res.toString());
                    } catch (Exception e) {
                        Log.d("000369", "MEMBER FullName Catch: " + e.getMessage());
                    }

                }
            }, 500);


            //////////////////// Update CBEMARI ////////////////
            Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {

            /*        Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/
                    try {
                        String update_CBEMARI_addedby = "UPDATE CBEMARI SET " +
                                "added_by='" + login_useruid + "' " +
                                "WHERE added_by = 'null'";

                        ls.executeNonQuery(update_CBEMARI_addedby);

                        Boolean res = ls.executeNonQuery(update_CBEMARI_addedby);
                        Log.d("000369", "update_CBEMARI_addedby: " + update_CBEMARI_addedby);
                        Log.d("000369", "Query: " + res.toString());
                    } catch (Exception e) {
                        Log.d("000369", "CBEMARI Added_By Catch: " + e.getMessage());
                    }
                }
            }, 1000);

            //////////////////// Update CGROWTH ////////////////////
            Handler handler3 = new Handler();
            handler3.postDelayed(new Runnable() {
                @Override
                public void run() {

                   /* Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/

                    try {
                        String update_CGROWTH_addedby = "UPDATE CGROWTH SET " +
                                "added_by='" + login_useruid + "' " +
                                "WHERE added_by = 'null'";

                        ls.executeNonQuery(update_CGROWTH_addedby);

                        Boolean res = ls.executeNonQuery(update_CGROWTH_addedby);
                        Log.d("000369", "update_CGROWTH_addedby: " + update_CGROWTH_addedby);
                        Log.d("000369", "Query: " + res.toString());
                    } catch (Exception e) {
                        Log.d("000369", "CGROWTH Added_By Catch: " + e.getMessage());
                    }

                }
            }, 1000);


            //////////////////// Update CMALOOM ///////////////////////
            Handler handler4 = new Handler();
            handler4.postDelayed(new Runnable() {
                @Override
                public void run() {

                  /*  Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/
                    try {
                        String update_CMALOOM_addedby = "UPDATE CMALOOM SET " +
                                "added_by='" + login_useruid + "' " +
                                "WHERE added_by = 'null'";

                        ls.executeNonQuery(update_CMALOOM_addedby);

                        Boolean res = ls.executeNonQuery(update_CMALOOM_addedby);
                        Log.d("000369", "update_CMALOOM_addedby: " + update_CMALOOM_addedby);
                        Log.d("000369", "Query: " + res.toString());
                    } catch (Exception e) {
                        Log.d("000369", "CMALOOM Added_By Catch: " + e.getMessage());
                    }

                }
            }, 1000);

            //////////////////// Update CVACCINATION //////////
            Handler handler5 = new Handler();
            handler5.postDelayed(new Runnable() {
                @Override
                public void run() {

                   /* Lister ls = new Lister(ctx);
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
            }, 1000);


//////////////////// Update MANC ////////////////////

            Handler handler6 = new Handler();
            handler6.postDelayed(new Runnable() {
                @Override
                public void run() {

                    /*Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/
                    try {
                        String update_MANC_addedby = "UPDATE MANC SET " +
                                "added_by='" + login_useruid + "' " +
                                "WHERE added_by = 'null'";

                        ls.executeNonQuery(update_MANC_addedby);

                        Boolean res = ls.executeNonQuery(update_MANC_addedby);
                        Log.d("000369", "update_MANC_addedby: " + update_MANC_addedby);
                        Log.d("000369", "Query: " + res.toString());
                    } catch (Exception e) {
                        Log.d("000369", "MANC Added_By Catch: " + e.getMessage());
                    }
                }
            }, 1000);


            //////////////////// Update MBEMARI ////////////////////

            Handler handler7 = new Handler();
            handler7.postDelayed(new Runnable() {
                @Override
                public void run() {

                    /*Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/
                    try {
                        String update_MBEMARI_addedby = "UPDATE MBEMARI SET " +
                                "added_by='" + login_useruid + "' " +
                                "WHERE added_by = 'null'";

                        ls.executeNonQuery(update_MBEMARI_addedby);

                        Boolean res = ls.executeNonQuery(update_MBEMARI_addedby);
                        Log.d("000369", "update_MBEMARI_addedby: " + update_MBEMARI_addedby);
                        Log.d("000369", "Query: " + res.toString());
                    } catch (Exception e) {
                        Log.d("000369", "MBEMARI Added_By Catch: " + e.getMessage());
                    }
                }
            }, 1000);


            //////////////////// Update MDELIV ////////////////////
            Handler handler8 = new Handler();
            handler8.postDelayed(new Runnable() {
                @Override
                public void run() {

                    /*Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/
                    try {
                        String update_MDELIV_addedby = "UPDATE MDELIV SET " +
                                "added_by='" + login_useruid + "' " +
                                "WHERE added_by = 'null'";

                        ls.executeNonQuery(update_MDELIV_addedby);

                        Boolean res = ls.executeNonQuery(update_MDELIV_addedby);
                        Log.d("000369", "update_MDELIV_addedby: " + update_MDELIV_addedby);
                        Log.d("000369", "Query: " + res.toString());
                    } catch (Exception e) {
                        Log.d("000369", "MDELIV Added_By Catch: " + e.getMessage());
                    }
                }
            }, 1000);


            //////////////////// Update MFPLAN //////////////////////

            Handler handler9 = new Handler();
            handler9.postDelayed(new Runnable() {
                @Override
                public void run() {
/*
                    Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/
                    try {
                        String update_MFPLAN_addedby = "UPDATE MFPLAN SET " +
                                "added_by='" + login_useruid + "' " +
                                "WHERE added_by = 'null'";

                        ls.executeNonQuery(update_MFPLAN_addedby);

                        Boolean res = ls.executeNonQuery(update_MFPLAN_addedby);
                        Log.d("000369", "update_MFPLAN_addedby: " + update_MFPLAN_addedby);
                        Log.d("000369", "Query: " + res.toString());
                    } catch (Exception e) {
                        Log.d("000369", "MFPLAN Added_By Catch: " + e.getMessage());
                    }
                }
            }, 1000);


            //////////////////// Update MMALOOM //////////////////////
            Handler handler10 = new Handler();
            handler10.postDelayed(new Runnable() {
                @Override
                public void run() {

                   /* Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/
                    try {
                        String update_MMALOOM_addedby = "UPDATE MMALOOM SET " +
                                "added_by='" + login_useruid + "' " +
                                "WHERE added_by = 'null'";

                        ls.executeNonQuery(update_MMALOOM_addedby);

                        Boolean res = ls.executeNonQuery(update_MMALOOM_addedby);
                        Log.d("000369", "update_MFPLAN_addedby: " + update_MMALOOM_addedby);
                        Log.d("000369", "Query: " + res.toString());
                    } catch (Exception e) {
                        Log.d("000369", "MMALOOM Added_By Catch: " + e.getMessage());
                    }
                }
            }, 1000);


            //////////////////// Update MPNC /////////////////
            Handler handler11 = new Handler();
            handler11.postDelayed(new Runnable() {
                @Override
                public void run() {

                    /*Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/
                    try {
                        String update_MPNC_addedby = "UPDATE MPNC SET " +
                                "added_by='" + login_useruid + "' " +
                                "WHERE added_by = 'null'";

                        ls.executeNonQuery(update_MPNC_addedby);

                        Boolean res = ls.executeNonQuery(update_MPNC_addedby);
                        Log.d("000369", "update_MPNC_addedby: " + update_MPNC_addedby);
                        Log.d("000369", "Query: " + res.toString());
                    } catch (Exception e) {
                        Log.d("000369", "MPNC Added_By Catch: " + e.getMessage());
                    }
                }
            }, 1000);


            //////////////////// Update MPREGNANCY //////////////////
            Handler handler12 = new Handler();
            handler12.postDelayed(new Runnable() {
                @Override
                public void run() {

                    /*Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/
                    try {
                        String update_MPREGNANCY_addedby = "UPDATE MPREGNANCY SET " +
                                "added_by='" + login_useruid + "' " +
                                "WHERE added_by = 'null'";

                        ls.executeNonQuery(update_MPREGNANCY_addedby);

                        Boolean res = ls.executeNonQuery(update_MPREGNANCY_addedby);
                        Log.d("000369", "update_MPREGNANCY_addedby: " + update_MPREGNANCY_addedby);
                        Log.d("000369", "Query: " + res.toString());
                    } catch (Exception e) {
                        Log.d("000369", "MPREGNANCY Added_By Catch: " + e.getMessage());
                    }
                }
            }, 1000);


            //////////////////// Update MVACINE ///////////////////
            Handler handler13 = new Handler();
            handler13.postDelayed(new Runnable() {
                @Override
                public void run() {

                    /*Lister ls = new Lister(ctx);
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
            }, 1000);


            //////////////////// Update REFERAL ////////////
            Handler handler14 = new Handler();
            handler14.postDelayed(new Runnable() {
                @Override
                public void run() {

                  /*  Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/
                    try {
                        String update_REFERAL_addedby = "UPDATE REFERAL SET " +
                                "added_by='" + login_useruid + "' " +
                                "WHERE added_by = 'null'";

                        ls.executeNonQuery(update_REFERAL_addedby);

                        Boolean res = ls.executeNonQuery(update_REFERAL_addedby);
                        Log.d("000369", "update_REFERAL_addedby: " + update_REFERAL_addedby);
                        Log.d("000369", "Query: " + res.toString());
                    } catch (Exception e) {
                        Log.d("000369", "REFERAL Added_By Catch: " + e.getMessage());
                    }
                }
            }, 1000);


            //////////////////// Update VIDEOS //////////
            Handler handler15 = new Handler();
            handler15.postDelayed(new Runnable() {
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
            }, 1000);


            //////////////////// Update LOGINS ///////////////////////////
            Handler handler16 = new Handler();
            handler16.postDelayed(new Runnable() {
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
            }, 1000);


            //////////////////// Update FEEDBACK /////////////////
            Handler handler17 = new Handler();
            handler17.postDelayed(new Runnable() {
                @Override
                public void run() {

                    /*Lister ls = new Lister(ctx);
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
            }, 1000);

        } catch (Exception e) {
            Log.d("000369", "Updated Table Catch: " + e.getMessage());
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        ls = new Lister(ctx);
        ls.createAndOpenDB();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            if (BelowTwo_Register_Activity.var_reg_below.equalsIgnoreCase("1")) {
                Log.d("000987", "RESTART IF: ");
                finish();
                startActivity(getIntent());
                BelowTwo_Register_Activity.var_reg_below = "0";
            } else if (AboveTwo_Register_Activity.var_reg_above.equalsIgnoreCase("1")) {
                Log.d("000987", "RESTART ELSE IF: ");
                finish();
                startActivity(getIntent());
                AboveTwo_Register_Activity.var_reg_above = "0";
            } else if (Child_Profile_Activity.var_child_pro.equalsIgnoreCase("1")) {
                Log.d("000987", "RESTART ELSE IF 2: ");
                finish();
                startActivity(getIntent());
                Child_Profile_Activity.var_child_pro = "0";
            } else if (var_mother_pro.equalsIgnoreCase("1")) {
                Log.d("000987", "RESTART ELSE IF 3: ");
                finish();
                startActivity(getIntent());
                var_mother_pro = "0";
            } else if (ChildRegister_Activity.temp_searchchildreg.equalsIgnoreCase("1")) {
                Log.d("000987", "RESTART ELSE IF 4: ");
                finish();
                startActivity(getIntent());
                ChildRegister_Activity.temp_searchchildreg = "0";
            } else if (MotherRegister_Activity.temp_search_motherreg.equalsIgnoreCase("1")) {
                Log.d("000987", "RESTART ELSE IF 5: ");
                finish();
                startActivity(getIntent());
                MotherRegister_Activity.temp_search_motherreg = "0";
            } else if (var_pregInActive.equalsIgnoreCase("1")) {
                Log.d("000987", "RESTART ELSE IF 6: ");
                finish();
                startActivity(getIntent());
                var_pregInActive = "0";
            }else if (var_add_preg.equalsIgnoreCase("1")) {
                Log.d("000987", "RESTART ELSE IF 7: ");
                finish();
                startActivity(getIntent());
                var_add_preg = "0";
            } else {
                Log.d("000987", "RESTART ELSEEEEE: ");
            }
        } catch (Exception e) {
            Log.d("000987", "ON-RESTART Error: " + e.getMessage());
        }

    }


   /* @Override
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