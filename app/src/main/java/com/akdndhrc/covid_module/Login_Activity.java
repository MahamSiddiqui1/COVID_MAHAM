package com.akdndhrc.covid_module;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.CustomClass.UrlClass;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.RoomDB.Country;
import com.akdndhrc.covid_module.RoomDB.DatabaseClient;
import com.akdndhrc.covid_module.RoomDB.MainActivity;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.akdndhrc.covid_module.DatabaseFiles.Helper;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.VAC_App.HomePageVacinator_Activity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.akdndhrc.covid_module.DatabaseFiles.Helper.CREATE_TABLE_COUNTRY;
import static com.akdndhrc.covid_module.Utils.haveNetworkConnection;

public class Login_Activity extends Activity {

    Context ctx = Login_Activity.this;

    Button btn_Login;
    public String user_previlage, login_useruid, login_username;
    EditText et_username, et_password;
    public static final int RequestPermissionCodeLogin = 1;
    AlertDialog alertDialog;
    Dialog dialog;
    TextView txt_app_version_name, txt_last_sync_datettime;
    public static String username, TodayDate;

    GPSTracker gps;
    double latitude;
    double longitude;
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    ServiceLocation_Login serviceLocationLogin;
    String temp_gps_var = "0";

    int myLatitude, myLongitude;
    int versionNumber;
    String versionName;

    ImageView iv_logo;

    static boolean btnLoginIsClicked = false;

    RelativeLayout rl_SyncOffline;
    Lister ls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Login_Activity.class));

        Log.d("000258", "ONCREATE");
        temp_gps_var = "1";

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());
        Log.d("000258", "Today Date : " + TodayDate);


        //  check_gps();

        if (checkPermission()) {
            Log.d("000258", "All permission allowed now");

            serviceLocationLogin = new ServiceLocation_Login(ctx);
            serviceLocationLogin.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            serviceLocationLogin.callAsynchronousTask();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    serviceLocation = new ServiceLocation(ctx);
                    serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    serviceLocation.callAsynchronousTask();

                }
            }, 1500);


            init_Directories();
            init_DatabaseDirectories();
            init_CSVFilesDirectories();

        } else {
            requestPermission();
            Log.d("000258", "Request Permission");
        }

        // ImportDateDictionary();
        if (haveNetworkConnection(ctx) > 0) {
            GooglePlayStoreUpdate();
        } else {
        }


        //TextVIew
        txt_last_sync_datettime = findViewById(R.id.txt_last_sync_datettime);
        txt_app_version_name = findViewById(R.id.txt_app_version_name);

        //ImageView
        iv_logo = findViewById(R.id.iv_logo);

        //Edittext
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        // et_password.setText("bibi.haj123");

        //et_password.setText("lhw1");

        //Button
        btn_Login = findViewById(R.id.btn_Login);

        //RelativeLayout
        rl_SyncOffline = findViewById(R.id.rl_SyncOffline);

        final TextView txt_date = findViewById(R.id.txt_date);
        txt_date.setText("18-05-2020");

       /* final TextView tvOfflineSync = findViewById(R.id.tvOfflineSync);
        tvOfflineSync.setVisibility(View.VISIBLE);
        Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation);
        tvOfflineSync.startAnimation(startAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                tvOfflineSync.clearAnimation();
//                tvOfflineSync.setVisibility(View.GONE);
            }
        }, 5000);*/

        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionNumber = pinfo.versionCode;
            versionName = pinfo.versionName;
            txt_app_version_name.setText( " Version: "+ versionName);
            Log.d("000258" ," ورژن : "+versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.d("000258", "Ver Err:" + e.getMessage());
        }

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
            String shared_username = prefelse.getString((String.valueOf(R.string.username)), null); // getting String
            et_username.setText(shared_username);
            Log.d("000258", "Last UserName: " + shared_username);
        } catch (Exception e) {
            Log.d("000258", "Shared Err:" + e.getMessage());
        }




        /*TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
        int cellid = cellLocation.getCid();
        int celllac = cellLocation.getLac();


        Log.d("000258", "CellLocation" + cellLocation.toString());
        Log.d("000258", "GSM CELL ID" + String.valueOf(cellid));
        Log.d("000258", "GSM Location Code" + String.valueOf(celllac));
        Log.d("000258", "GSM Location Code" + String.valueOf(celllac));*/


        RelativeLayout  rl_SyncOfflines = findViewById(R.id.rl_SyncOfflines);
        rl_SyncOfflines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx,OfflineSync_Activity.class));
                overridePendingTransition(0, 0);
            }
        });

        rl_SyncOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(ctx,OfflineSync_Activity.class));

                Intent intent = new Intent(Login_Activity.this, OfflineSync_Activity.class);
                startActivity(intent);
            }
        });

        iv_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "" + et_password.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });


        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                btnLoginIsClicked = true;


                ls = new Lister(ctx);
                ls.createAndOpenDB();


                if (et_username.getText().toString().isEmpty()) {
                    // Toast.makeText(getApplicationContext(), "برائے مہربانی صارف کا نام درج کریں", Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar.make(v, R.string.enterUsernameEng, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(15);
                    snackbar.setDuration(3000);
                    snackbar.show();
                    return;
                }


                if (et_password.getText().toString().isEmpty()) {
                    final Snackbar snackbar = Snackbar.make(v, R.string.enterPassEng, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(15);
                    snackbar.setDuration(3000);
                    snackbar.show();
                    return;
                }


                LayoutInflater li = LayoutInflater.from(ctx);
                View promptsView = li.inflate(R.layout.lay_dialog_loading, null);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
                alertDialogBuilder.setView(promptsView);

                alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                saveTask();

//                try {
//                   /* Lister ls = new Lister(ctx);
//                    ls.createAndOpenDB();*/
//
//
//                    String[][] data_check = ls.executeReader("Select count(*) from USERS");
//                    if (Integer.parseInt(data_check[0][0]) > 0) {
//                        Log.d("000258", "USER COUNT IFFF: " + data_check[0][0]);
//                        if (haveNetworkConnection(ctx) > 0) {
//                            Log.d("000258", "INTERNET AVAILAB");
//                            sendPostRequest(et_username.getText().toString(), et_password.getText().toString());
//                        } else {
//
//                            Log.d("000258", "NO INTERNET OFFLINE LOGIN");
//                        }
//
//                    } else {
//                        Log.d("000258", "USER COUNT ELSE: " + data_check[0][0]);
//                        if (haveNetworkConnection(ctx) > 0) {
//                            sendPostRequest(et_username.getText().toString(), et_password.getText().toString());
//
//                        } else {
//                            alertDialog.dismiss();
//
//                            Dialog dialog1 = new Dialog(ctx);
//                            LayoutInflater layout = LayoutInflater.from(ctx);
//                            final View dialogView = layout.inflate(R.layout.lay_dialog_loading4, null);
//
//                            dialog1.setContentView(dialogView);
//                            dialog1.setCanceledOnTouchOutside(true);
//                            dialog1.setCancelable(true);
//                            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                            dialog1.show();
//                            return;
//                        }
//
//                    }
//
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            try {
//                                checkLoginCreden(et_username.getText().toString(), et_password.getText().toString());
//
//                            } catch (Exception e) {
//                                alertDialog.dismiss();
//                                Log.d("000258", "Er" + e.getMessage());
//                            } finally {
//
//                                alertDialog.dismiss();
//                            }
//                        }
//
//                    }, 4500);
//                }
//                catch (Exception e) {
//                    Log.d("000258", "Er: " + e.getMessage());
//
//                    if (haveNetworkConnection(ctx) > 0) {
//                        sendPostRequest(et_username.getText().toString(), et_password.getText().toString());
//                    }
//                    else {
//                        alertDialog.dismiss();
//                        Dialog dialog1 = new Dialog(ctx);
//                        LayoutInflater layout = LayoutInflater.from(ctx);
//                        final View dialogView = layout.inflate(R.layout.lay_dialog_loading4, null);
//
//                        dialog1.setContentView(dialogView);
//                        dialog1.setCanceledOnTouchOutside(true);
//                        dialog1.setCancelable(true);
//                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                        dialog1.show();
//                        return;
//                    }
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            try {
//                                checkLoginCreden(et_username.getText().toString(), et_password.getText().toString());
//                            } catch (Exception e) {
//                                Log.d("000258", "Er" + e.getMessage());
//                            } finally {
//                                alertDialog.dismiss();
//                            }
//                        }
//
//                    }, 4500);
//                }

            }
        });
    }

    private void saveLo() {
        final String sTask = et_username.getText().toString().trim();
        final String sDesc = editTextDesc.getText().toString().trim();
        final String sFinishBy = editTextFinishBy.getText().toString().trim();

        if (sTask.isEmpty()) {
            editTextTask.setError("Task required");
            editTextTask.requestFocus();
            return;
        }

        if (sDesc.isEmpty()) {
            editTextDesc.setError("Desc required");
            editTextDesc.requestFocus();
            return;
        }

        if (sFinishBy.isEmpty()) {
            editTextFinishBy.setError("Finish by required");
            editTextFinishBy.requestFocus();
            return;
        }

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                Country country = new Country();
                country.setTask(sTask);
                country.setDesc(sDesc);
                country.setFinishBy(sFinishBy);
                country.setFinished(false);

                //adding to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .insert(country);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }

    private void init_Directories() {

        try {

            String folder = Environment.getExternalStorageDirectory() + File.separator + "HayatPK" + File.separator + "Videos";

            File directory = new File(folder);

            boolean success = true;
            if (!directory.exists()) {
                directory.mkdirs();
                Log.d("000258", " Video Folder Created");
            } else {
                Log.d("000258", "Video Folder Exit");
            }
            if (success) {
                Log.d("000258", " IF Success");
            } else {
                Log.d("000258", "Else Success");
            }

        } catch (Exception e) {
            // Toast.makeText(this, "Faild to Create Folder", Toast.LENGTH_SHORT).show();
            Log.d("000258", "Failed" + e.getMessage());
        }
    }

    private void init_DatabaseDirectories() {

        try {

            String folder = Environment.getExternalStorageDirectory() + File.separator + "HayatPK" + File.separator + "Backup DB Files";

            File directory = new File(folder);

            boolean success = true;
            if (!directory.exists()) {
                directory.mkdirs();
                Log.d("000258", "Backup Folder Created");
                copy_database_file(ctx, "HayatPKDB");
            } else {
                Log.d("000258", "Backup Folder EXIT");
            }

            if (success) {
                Log.d("000258", " IF Success");
            } else {
                Log.d("000258", "Else Success");
            }


        } catch (Exception e) {
            // Toast.makeText(this, "Faild to Create Folder", Toast.LENGTH_SHORT).show();
            Log.d("000258", "Failed" + e.getMessage());
        }
    }

    private void init_CSVFilesDirectories() {

        try {

            String folder = Environment.getExternalStorageDirectory() + File.separator + "HayatPK" + File.separator + "Dictionaries";

            File directory = new File(folder);

            boolean success = true;
            if (!directory.exists()) {
                directory.mkdirs();
                Log.d("000258", "Dictionaries Folder Created");
            } else {
                Log.d("000258", "Dictionaries Folder EXIT");
            }

            if (success) {
                Log.d("000258", " IF Success");
            } else {
                Log.d("000258", "Else Success");
            }


        } catch (Exception e) {
            // Toast.makeText(this, "Faild to Create Folder", Toast.LENGTH_SHORT).show();
            Log.d("000258", "Failed" + e.getMessage());
        }
    }

    public void copy_database_file(Context ctx, String DATABASE_NAME) {
        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String timeStamp = dates.format(cal.getTime());
        Log.d("000258", "Today Date : " + timeStamp);

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
            Log.d("000258", " testing db path: " + directory);

              /*  myOutput = new FileOutputStream(directory.getAbsolutePath()
                        + "/" + DATABASE_NAME+"-"+user_name+"-"+String.valueOf(System.currentTimeMillis()));*/

            myOutput = new FileOutputStream(Environment.getExternalStorageDirectory()
                    + File.separator + "HayatPK"
                    + File.separator + "Backup DB Files"
                    + File.separator + DATABASE_NAME + "-" + timeStamp + ".db");

            Log.d("000258", "Copy File: " + myOutput);

            myInput = new FileInputStream(Environment.getExternalStorageDirectory()
                    + File.separator + "HayatPK"
                    + File.separator + DATABASE_NAME);

            Log.d("000258", "PUTPUT: " + myInput);

            Log.d("000258", " testing db path 1" + String.valueOf(myInput));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
        } catch (Exception e) {
            Log.d("000258", " testing db path 2 " + e.getMessage());
        } finally {
            try {
                Log.d("000258", " testing 1");
                if (myOutput != null) {
                    myOutput.close();
                    myOutput = null;
                }
                if (myInput != null) {
                    myInput.close();
                    myInput = null;
                }
            } catch (Exception e) {
                Log.d("000258", " testing 2");
            }
        }
    }

//    private void sendPostRequest(final String username, final String password) {
//
//            //String url = "https://pak.api.teekoplus.akdndhrc.org/sync/lists/download";
//            String url = "https://development.api.teekoplus.akdndhrc.org/sync/lists/download";
//            //String url = UrlClass.dicitonary_download;
//
//            Log.d("000258", "mURL " + url);
//            //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();
//
//            String REQUEST_TAG = String.valueOf("volleyStringRequest");
//
//            StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//
//                    Log.d("000258", "Response:    " + response);
//
//
//                    try {
//
//                        /////////////Current Date in Persian and save in Shared Pref
//                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPreferences", 0); // 0 - for private mode
//                        SharedPreferences.Editor editor = preferences.edit();
//
//                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//                        Log.d("000258", "Current  DateTime: " + timeStamp);
//                        editor.putString("last_sync_time", timeStamp); // Storing string
//                        editor.apply();
//
//                    } catch (Exception e) {
//                        Log.d("000258", "Syn ERr:    " + e.getMessage());
//                    }
//
//                    // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();
//
//                    try {
//                        // Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();
//
//                        JSONObject jobj = new JSONObject(response);
//                        Log.d("000258", response);
//                        // if(jobj.getBoolean("success")){
//                        Log.d("testlinksync", response);
//
//                        Lister ls = new Lister(ctx);
//                        // ls.closeDB();
//                        ls.createAndOpenDB();
//                        boolean m1 = ls.executeNonQuery("DROP TABLE IF EXISTS COUNTRY ");
//                        boolean m11 = ls.executeNonQuery("DROP TABLE IF EXISTS PROVINCE ");
//                        boolean m12 = ls.executeNonQuery("DROP TABLE IF EXISTS DISTRICT ");
//                        boolean m13 = ls.executeNonQuery("DROP TABLE IF EXISTS TEHSIL ");
//                        boolean m14 = ls.executeNonQuery("DROP TABLE IF EXISTS UNIONCOUNCIL ");
//                        boolean m15 = ls.executeNonQuery("DROP TABLE IF EXISTS VILLAGES ");
//                        boolean m16 = ls.executeNonQuery("DROP TABLE IF EXISTS FACILITY ");
//                        boolean m17 = ls.executeNonQuery("DROP TABLE IF EXISTS VACCINES ");
//                        boolean m18 = ls.executeNonQuery("DROP TABLE IF EXISTS USERS ");
//                        boolean m19 = ls.executeNonQuery("DROP TABLE IF EXISTS MEDICINE ");
//
//
//                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
//
//                        ////////////////////////////////////////////// Countries///////////////////////////////////////////////////////
//                        try {
//                            Log.d("000258", "RESPONSE: " + response.toString());
//
//                            JSONObject obj = new JSONObject(String.valueOf(response));
//                            //  Log.d("000258","Countries: " +obj.toString());
//
//                            JSONArray m_jArry = obj.getJSONArray("countries");
//                            Log.d("000258", "Countries: " + m_jArry.toString());
//
//
//                            boolean m2 = ls.executeNonQuery(CREATE_TABLE_COUNTRY);
//                            String date = "";
//                            for (int i = 0; i < m_jArry.length(); i++) {
//                                JSONObject jo_inside = m_jArry.getJSONObject(i);
//                                boolean mFlag = ls.executeNonQuery("insert or ignore into COUNTRY(uid, " +
//                                        "name) values " +
//                                        "(" +
//                                        "'" + jo_inside.getString("uid") + "'," +
//                                        "'" + jo_inside.getString("name") + "'" +
//
//                                        ")");
//
//                                Log.d("000258", "Countries_boolean: " + mFlag);
//
//                            }
//                            Log.d("000258", "************* ALL COUNTRIES DATA SYNCED ************");
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.d("000258", "Country Catch: " + e.getMessage());
//                            Toast.makeText(getApplicationContext(), getString(R.string.noDataForEng) + "country " + String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
//                        }
//
//                         ////////////////////////////////////////// Provinces ///////////////////////////////////////////////////////////////
//                        try {
//
//                            JSONObject obj = new JSONObject(String.valueOf(response));
//                            //  Log.d("000258","PROVINCES: " +obj.toString());
//
//                            JSONArray m_jArry = obj.getJSONArray("provinces");
//                            Log.d("000258", "PROVINCES: " + m_jArry.toString());
//
//                            boolean m2 = ls.executeNonQuery(Helper.CREATE_TABLE_PROVINCE);
//                            String date = "";
//                            for (int i = 0; i < m_jArry.length(); i++) {
//                                JSONObject jo_inside = m_jArry.getJSONObject(i);
//                                boolean mFlag = ls.executeNonQuery("insert or ignore into PROVINCE(uid, country_id, " +
//                                        "name) values " +
//                                        "(" +
//                                        "'" + jo_inside.getString("uid") + "'," +
//                                        "'" + jo_inside.getString("country_id") + "'," +
//                                        "'" + jo_inside.getString("name") + "'" +
//
//                                        ")");
//
//                                //Toast.makeText(getApplicationContext(), String.valueOf(mFlag), Toast.LENGTH_SHORT).show();
//
//                                Log.d("000258", "Province_boolean: " + mFlag);
//
//                            }
//                            Log.d("000258", "************* ALL PROVINCES DATA SYNCED ************");
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.d("000258", "Province Catch: " + e.getMessage());
//                            Toast.makeText(getApplicationContext(), getString(R.string.noDataForEng) + "province " + String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
//                        }
//
//                        ///////////////////////////////////////////// Districts ///////////////////////////////////////
//                        try {
//
//                            JSONObject obj = new JSONObject(String.valueOf(response));
//                            //   Log.d("000258","DISTRICTS: " +obj.toString());
//
//                            JSONArray m_jArry = obj.getJSONArray("district");
//                            Log.d("000258", "DISTRICTS: " + m_jArry.toString());
//
//                            boolean m2 = ls.executeNonQuery(Helper.CREATE_TABLE_DISTRICT);
//                            String date = "";
//                            for (int i = 0; i < m_jArry.length(); i++) {
//                                JSONObject jo_inside = m_jArry.getJSONObject(i);
//                                boolean mFlag = ls.executeNonQuery("insert or ignore into DISTRICT(uid,country_id,province_id, " +
//                                        "name) values " +
//                                        "(" +
//                                        "'" + jo_inside.getString("uid") + "'," +
//                                        "'" + jo_inside.getString("country_id") + "'," +
//                                        "'" + jo_inside.getString("province_id") + "'," +
//                                        "'" + jo_inside.getString("name") + "'" +
//
//                                        ")");
//
//
//                                Log.d("000258", "Districts_boolean: " + mFlag);
//
//                            }
//                            Log.d("000258", "************* ALL DISTRICTS DATA SYNCED ************");
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.d("000258", "DISTRICT Catch: " + e.getMessage());
//                            Toast.makeText(getApplicationContext(), getString(R.string.noDataForEng) + "district " + String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
//                        }
//
//                         ///////////////////////////////////////// TEHSILS ////////////////////////////////////////////////////
//                        try {
//
//                            JSONObject obj = new JSONObject(String.valueOf(response));
//                            //     Log.d("000258","TEHSIL: " +obj.toString());
//
//                            JSONArray m_jArry = obj.getJSONArray("tehsils");
//                            Log.d("000258", "TEHSIL: " + m_jArry.toString());
//
//
//                            boolean m2 = ls.executeNonQuery(Helper.CREATE_TABLE_TEHSIL);
//                            String date = "";
//                            for (int i = 0; i < m_jArry.length(); i++) {
//                                JSONObject jo_inside = m_jArry.getJSONObject(i);
//                                boolean mFlag = ls.executeNonQuery("insert or ignore into TEHSIL(uid,country_id,province_id, " +
//                                        "district_id,name) values " +
//                                        "(" +
//                                        "'" + jo_inside.getString("uid") + "'," +
//                                        "'" + jo_inside.getString("country_id") + "'," +
//                                        "'" + jo_inside.getString("province_id") + "'," +
//                                        "'" + jo_inside.getString("district_id") + "'," +
//                                        "'" + jo_inside.getString("name") + "'" +
//
//                                        ")");
//
//
//                                Log.d("000258", "Tehsils_boolean: " + mFlag);
//
//                            }
//                            Log.d("000258", "************* ALL TEHSILS DATA SYNCED ************");
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.d("000258", "TEHSIL Catch: " + e.getMessage());
//                            Toast.makeText(getApplicationContext(), getString(R.string.noDataForEng) + "Tehsil " + String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
//                        }
//
//                        ///////////////////////////////////////// UNIONCOUNCILS /////////////////////////////////////////////
//                        try {
//
//                            JSONObject obj = new JSONObject(String.valueOf(response));
//                            // Log.d("000258","UNIONCOUNCIL: " +obj.toString());
//
//                            JSONArray m_jArry = obj.getJSONArray("ucs");
//                            Log.d("000258", "UNIONCOUNCIL: " + m_jArry.toString());
//
//                            boolean m2 = ls.executeNonQuery(Helper.CREATE_TABLE_UNIONCOUNCIL);
//                            String date = "";
//                            for (int i = 0; i < m_jArry.length(); i++) {
//                                JSONObject jo_inside = m_jArry.getJSONObject(i);
//                                boolean mFlag = ls.executeNonQuery("insert or ignore into UNIONCOUNCIL(uid,country_id,province_id, " +
//                                        "district_id,tehsil_id,name) values " +
//                                        "(" +
//                                        "'" + jo_inside.getString("uid") + "'," +
//                                        "'" + jo_inside.getString("country_id") + "'," +
//                                        "'" + jo_inside.getString("province_id") + "'," +
//                                        "'" + jo_inside.getString("district_id") + "'," +
//                                        "'" + jo_inside.getString("tehsil_id") + "'," +
//                                        "'" + jo_inside.getString("name") + "'" +
//
//                                        ")");
//
//
//                                Log.d("000258", "UC_boolean: " + mFlag);
//
//                            }
//                            Log.d("000258", "************* ALL UNIONCOUNCIL DATA SYNCED ************");
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.d("000258", "UnionCouncil Catch: " + e.getMessage());
//                            Toast.makeText(getApplicationContext(), getString(R.string.noDataForEng) + "UnionCouncil " + String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
//                        }
//
//                        ////////////////////////////////// VILLAGES ////////////////////////////////////////////
//                        try {
//
//                            JSONObject obj = new JSONObject(String.valueOf(response));
//                            //Log.d("000258","VILLAGES: " +obj.toString());
//                            JSONArray m_jArry = obj.getJSONArray("villages");
//                            Log.d("000258", "VILLAGES: " + m_jArry.toString());
//
//                            boolean m2 = ls.executeNonQuery(Helper.CREATE_TABLE_VILLAGES);
//                            String date = "";
//                            for (int i = 0; i < m_jArry.length(); i++) {
//                                JSONObject jo_inside = m_jArry.getJSONObject(i);
//                                boolean mFlag = ls.executeNonQuery("insert or ignore into VILLAGES(uid,country_id,province_id, " +
//                                        "district_id,tehsil_id,uc_id,name) values " +
//                                        "(" +
//                                        "'" + jo_inside.getString("uid") + "'," +
//
//                                        "'" + jo_inside.getString("country_id") + "'," +
//                                        "'" + jo_inside.getString("province_id") + "'," +
//                                        "'" + jo_inside.getString("district_id") + "'," +
//                                        "'" + jo_inside.getString("tehsil_id") + "'," +
//                                        "'" + jo_inside.getString("uc_id") + "'," +
//                                        "'" + jo_inside.getString("name") + "'" +
//
//                                        ")");
//
//
//                                Log.d("000258", "Villages_boolean: " + mFlag);
//
//                            }
//                            Log.d("000258", "************* ALL VILLAGES DATA SYNCED ************");
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.d("000258", "VILLAGES Catch: " + e.getMessage());
//                            Toast.makeText(getApplicationContext(), getString(R.string.noDataForEng) + "Villages " + String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
//                        }
//
//                        ///////////////////////////////// Facilities ////////////////////////////////////////////
//                        try {
//
//                            JSONObject obj = new JSONObject(String.valueOf(response));
//                            // Log.d("000258","FACILITIES: " +obj.toString());
//                            JSONArray m_jArry = obj.getJSONArray("facilities");
//                            Log.d("000258", "FACILITIES: " + m_jArry.toString());
//
//                            boolean m2 = ls.executeNonQuery(Helper.CREATE_TABLE_FACILITY);
//                            String date = "";
//                            for (int i = 0; i < m_jArry.length(); i++) {
//                                JSONObject jo_inside = m_jArry.getJSONObject(i);
//                                boolean mFlag = ls.executeNonQuery("insert or ignore into FACILITY(uid,country_id,province_id, " +
//                                        "district_id,tehsil_id,uc_id,name) values " +
//                                        "(" +
//                                        "'" + jo_inside.getString("uid") + "'," +
//
//                                        "'" + jo_inside.getString("country_id") + "'," +
//                                        "'" + jo_inside.getString("province_id") + "'," +
//                                        "'" + jo_inside.getString("district_id") + "'," +
//                                        "'" + jo_inside.getString("tehsil_id") + "'," +
//                                        "'" + jo_inside.getString("uc_id") + "'," +
//                                        "'" + jo_inside.getString("name") + "'" +
//
//                                        ")");
//
//
//                                Log.d("000258", "Facilities_boolean: " + mFlag);
//
//                            }
//                            Log.d("000258", "************* ALL FACILITIES DATA SYNCED ************");
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.d("000258", "FACILITIES Catch: " + e.getMessage());
//                            Toast.makeText(getApplicationContext(), getString(R.string.noDataForEng) + "facilities " + String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
//                        }
//
//                        //////////////////////////////  VACCINES //////////////////////////////////////
//                        try {
//
//                            JSONObject obj = new JSONObject(String.valueOf(response));
//                            // Log.d("000258","VACCINES: " +obj.toString());
//                            JSONArray m_jArry = obj.getJSONArray("vaccines");
//                            Log.d("000258", "VACCINE: " + m_jArry.toString());
//
//                            boolean m2 = ls.executeNonQuery(Helper.CREATE_TABLE_VACCINES);
//                            String date = "";
//                            for (int i = 0; i < m_jArry.length(); i++) {
//                                JSONObject jo_inside = m_jArry.getJSONObject(i);
//                                boolean mFlag = ls.executeNonQuery("insert or ignore into VACCINES(uid, defaulter_date,due_date, " +
//                                        "name) values " +
//                                        "(" +
//                                        "'" + jo_inside.getString("uid") + "'," +
//                                        "'" + jo_inside.getInt("defaulter_date") + "'," +
//                                        "'" + jo_inside.getInt("due_date") + "'," +
//                                        "'" + jo_inside.getString("name") + "'" +
//
//                                        ")");
//
//                                Log.d("000258", "Vaccines_boolean: " + mFlag);
//
//                            }
//                            Log.d("000258", "************* ALL VACCINES DATA SYNCED ************");
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.d("000258", "VACCINE Catch: " + e.getMessage());
//                            Toast.makeText(getApplicationContext(), getString(R.string.noDataForEng) + "vacnines " + String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
//                        }
//
//                        /////////////////////////////////////// USERS ////////////////////////////////////
//
//                        try {
//
//                            JSONObject obj = new JSONObject(String.valueOf(response));
//                            ///  Log.d("000258","USERRS: " +obj.toString());
//                            JSONArray m_jArry = obj.getJSONArray("users");
//                            Log.d("000258", "USERS: " + m_jArry.toString());
//                            boolean m2 = ls.executeNonQuery(Helper.CREATE_TABLE_USERS);
//                            String date = "";
//                            for (int i = 0; i < m_jArry.length(); i++) {
//                                JSONObject jo_inside = m_jArry.getJSONObject(i);
//
//                                boolean mFlag = ls.executeNonQuery("insert or ignore into USERS(uid,username,password,privilege,salt,district_id,country_id,province_id, " +
//                                        "uc_id) values " +
//                                        "(" +
//                                        "'" + jo_inside.getString("uid") + "'," +
//                                        "'" + jo_inside.getString("username") + "'," +
//                                        "'" + jo_inside.getString("password") + "'," +
//                                        "'" + jo_inside.getInt("privilege") + "'," +
//                                        "'" + jo_inside.getString("salt") + "'," +
//                                        "'" + jo_inside.getString("district_id") + "'," +
//                                        "'" + jo_inside.getString("country_id") + "'," +
//                                        "'" + jo_inside.getString("province_id") + "'," +
//                                        "'" + jo_inside.getString("uc_id") + "'" +
//                                        ")");
//
//
//                                Log.d("000258", "Users_boolean: " + mFlag);
//
//                            }
//                            Log.d("000258", "************* ALL USERS DATA SYNCED ************");
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.d("000258", "USER Catch: " + e.getMessage());
//                            Toast.makeText(getApplicationContext(), getString(R.string.noDataForEng) + "users " + String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
//                        }
//
//
//                        //////////////////////// MEDICINES /////////////////////
//                        try {
//
//                            JSONObject obj = new JSONObject(String.valueOf(response));
//                            // Log.d("000258","MEDICINES: " +obj.toString());
//                            JSONArray m_jArry = obj.getJSONArray("medicines");
//                            Log.d("000258", "MEDICINES: " + m_jArry.toString());
//                            boolean m2 = ls.executeNonQuery(Helper.CREATE_TABLE_MEDICINE);
//                            String date = "";
//                            for (int i = 0; i < m_jArry.length(); i++) {
//                                JSONObject jo_inside = m_jArry.getJSONObject(i);
//                                boolean mFlag = ls.executeNonQuery("insert or ignore into MEDICINE(uid, name, " +
//                                        "type) values " +
//                                        "(" +
//                                        "'" + jo_inside.getString("uid") + "'," +
//                                        "'" + jo_inside.getString("name") + "'," +
//                                        "'" + jo_inside.getInt("type") + "'" +
//
//                                        ")");
//
//                                Log.d("000258", "Medicines_boolean: " + mFlag);
//
//                            }
//                            Log.d("000258", "************* ALL MEDICINES DATA SYNCED ************");
//
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.d("000258", "MEDICINES Err:    " + e.getMessage());
//                            Toast.makeText(getApplicationContext(), getString(R.string.noDataForEng) + "medicine " + String.valueOf(e.getMessage()), Toast.LENGTH_SHORT).show();
//                        }
//
//                    } catch (Exception e) {
//                        Log.d("000258", "Err:    " + e.getMessage());
//                        Toast.makeText(Login_Activity.this, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
//
//                    }
//                    Log.d("000258", "*************** ALL SYNC  SUCCESSFULLY*****************");
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                    Log.d("000258", "onErrorResponse: " + error.getMessage());
//                    Toast.makeText(ctx, "Login API Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
//                    //  Toast.makeText(Login_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
//
//
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() {
//
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("username", username);
//                    params.put("password", password);
//
//
//                    Log.d("000258", "mParam " + params);
//
//                    return params;
//                }
//
//                @Override
//                public Map<String, String> getHeaders() {
//                    Log.d("000258", "map ");
//                    Map<String, String> params = new HashMap<String, String>();
//                    //   params.put("Content-Type", "application/x-www-form-urlencoded");
//                    return params;
//                }
//            };
//
//            AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
//        }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public void checkLoginCreden(String userName, String pass) {
        try {
            Lister ls = new Lister(getApplicationContext());
            ls.createAndOpenDB();
            String[][] mData = ls.executeReader("Select password, salt, uid, privilege from USERS where username = '" + userName + "' LIMIT 1");
            if (mData.length > 0) {
                String password = mData[0][0];
                // String checkpassword = password;
                String salt = mData[0][1];
                // user_facilities =  mData[0][2];
                String salted = pass + salt;
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("SHA-1");
                    byte[] textBytes = salted.getBytes("iso-8859-1");
                    md.update(textBytes, 0, textBytes.length);
                    byte[] sha1hash = md.digest();
                    String vartodis = convertToHex(sha1hash);
                    Log.d("000258", "ConvertToHex: " + vartodis);
                    Log.d("000258", "USER-Salt: " + salt);
                    Log.d("000258", "USER-Password:" + password);
                    Log.d("000258", "USER-UID:" + mData[0][2]);
                    Log.d("000258", "USER-privilege:" + mData[0][3]);
                    user_previlage = mData[0][3];
                    if (vartodis.trim().equalsIgnoreCase(password.trim())) {
                        if (mData[0][3].equalsIgnoreCase("null")) {
                            login_useruid = mData[0][2];
                            login_username = userName;
                            Log.d("000258", "LOGIN-USER-NAME:" + login_username);
                            Log.d("000258", "LOGIN-USER-UID:" + login_useruid);
//                            insert_logins_db();

                          //  check_login_user_LHW();

                        } else {
                            login_useruid = mData[0][2];
                            login_username = userName;
                            Log.d("000258", "LOGIN-USER-NAME:" + login_username);
                            Log.d("000258", "LOGIN-USER-UID:" + login_useruid);

                            check_login_user_VAC();
                        }

//                        Toast.makeText(getApplicationContext(), "User logged in", Toast.LENGTH_SHORT).show();

                        ////////////Save user name in shared pref
                        SharedPreferences prefw = getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
                        SharedPreferences.Editor editorw = prefw.edit();
                        editorw.putString("username", userName);
                        editorw.putString("login_userid", login_useruid);

                        username = userName;
                        //  editorw.putString("password", mData[0][2]);
                        editorw.apply();
                    } else {
                        Log.d("000258", "USER CREDENTIALS FAILED");
//                        Toast.makeText(getApplicationContext(), "صارف کا نام اور پاسورڈ صحیح نہیں", Toast.LENGTH_SHORT).show();

                        final Snackbar snackbar = Snackbar.make(findViewById(R.id.login_layout), R.string.correctPassEng, Snackbar.LENGTH_SHORT);
                        /// ViewCompat.setLayoutDirection(snackbar.getView(), ViewCompat.LAYOUT_DIRECTION_LTR);
                        View mySbView = snackbar.getView();
                        mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                        TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(15);
                        snackbar.setDuration(4000);
                        snackbar.show();


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("000258", "FAILED" + e.getMessage());
                }
            } else {
                Log.d("000258", "USER NOT FOUND");
                alertDialog.dismiss();
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.login_layout), R.string.correctUsernameEng, Snackbar.LENGTH_SHORT);
                /// ViewCompat.setLayoutDirection(snackbar.getView(), ViewCompat.LAYOUT_DIRECTION_LTR);
                View mySbView = snackbar.getView();
                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(15);
                snackbar.setDuration(4000);
                snackbar.show();

            }


        } catch (Exception e) {
            alertDialog.dismiss();
            final Snackbar snackbar = Snackbar.make(findViewById(R.id.login_layout), R.string.correctUsernamePassEng, Snackbar.LENGTH_SHORT);
            /// ViewCompat.setLayoutDirection(snackbar.getView(), ViewCompat.LAYOUT_DIRECTION_LTR);
            View mySbView = snackbar.getView();
            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(15);
            snackbar.setDuration(4000);
            snackbar.show();
            Log.d("000258", "Exception checkLoginCreden: " + e.getMessage());

        }

    }

    private void check_login_user_LHW() {

        try {

            /*Lister ls = new Lister(ctx);
            ls.createAndOpenDB();*/

            String[][] mData_logins = ls.executeReader("Select count(*) from LOGINS where uid = '" + login_useruid + "' and record_data = '" + TodayDate + "'");
            if (Integer.parseInt(mData_logins[0][0]) > 0) {
                Log.d("000258", "LHW Record Date:" + mData_logins[0][0]);

                try {
                    if (serviceLocationLogin.showCurrentLocation() == true) {

                        latitude = serviceLocationLogin.getLatitude();
                        longitude = serviceLocationLogin.getLongitude();
                        Log.d("000258", " LHW IF Service latitude: " + latitude);
                        Log.d("000258", " LHW If Service longitude: " + longitude);

                    } else {
                        try {
                            ServiceLocation.doAsynchronousTask.cancel();
                            ServiceLocation_Login.doAsynchronousTask.cancel();
                        } catch (Exception e) {
                        }
                        try {
                            Lister lss = new Lister(ctx);
                            ls.createAndOpenDB();

                            String[][] mLogin_data = lss.executeReader("SELECT max(added_on),latitude, longitude,record_data,count(*) from LOGINS");

                            if (Integer.valueOf(mLogin_data[0][4]) > 0) {
                                Log.d("000258", " LW IF User Last Latitude: " + mLogin_data[0][1]);
                                Log.d("000258", " LW IF User Last Longitude: " + mLogin_data[0][2]);
                                Log.d("000258", " LW IF User Last Login Date: " + mLogin_data[0][3]);

                                latitude = Double.parseDouble(mLogin_data[0][1]);
                                longitude = Double.parseDouble(mLogin_data[0][2]);

                            } else {
                                Log.d("000258", " ELSE  LHW Last Login Date!!!!!! less than 0");
                                latitude = Double.parseDouble("0.0");
                                longitude = Double.parseDouble("0.0");
                            }

                        } catch (Exception e) {
                            Toast.makeText(ctx, R.string.loginError, Toast.LENGTH_SHORT).show();
                            Log.d("000258", "Read Login Error Catch: " + e.getMessage());
                        }
                    }

                    ////////////Save GPS latlng in shared pref
                    SharedPreferences prefw = getApplicationContext().getSharedPreferences(getString(R.string.loginGPSeng), 0); // 0 - for private mode
                    SharedPreferences.Editor editorw = prefw.edit();
                    editorw.putString("gps_latitude", String.valueOf(latitude));
                    editorw.putString("gps_longitude", String.valueOf(longitude));
                    Log.d("000258", "LHW gps_latitude:" + String.valueOf(latitude));
                    Log.d("000258", "LHW gps_longitude:" + String.valueOf(longitude));
                    editorw.apply();

                } catch (Exception e) {
                    Log.d("000258", "SharedPred GPS Catch: " + e.getMessage());
                }


                Intent intent1 = new Intent(ctx, HomePage_Activity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);

                temp_gps_var = "0";

                View toastView = getLayoutInflater().inflate(R.layout.custom_toast_view_layout, null);
                // Initiate the Toast instance.
                Toast toast = new Toast(getApplicationContext());
                // Set custom view in toast.
                toast.setView(toastView);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
                toast.show();

                Log.d("000258", "IF LHW LOGIN");

            } else {

                insert_logins_db();
                //startActivity(new Intent(getApplicationContext(), HomePage_Activity.class));
                Intent intent1 = new Intent(ctx, HomePage_Activity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                temp_gps_var = "0";

                // Toast.makeText(getApplicationContext(), "صارف لاگ ان", Toast.LENGTH_SHORT).show();
                View toastView = getLayoutInflater().inflate(R.layout.custom_toast_view_layout, null);
                // Initiate the Toast instance.
                Toast toast = new Toast(getApplicationContext());
                // Set custom view in toast.
                toast.setView(toastView);

                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
                toast.show();
                Log.d("000258", "ELSE LHW LOGIN");
            }
        } catch (Exception e) {
            Log.d("000258", "Check Login Err:  " + e.getMessage());

            //insert_logins_db();
        }

    }

    private void check_login_user_VAC() {

        try {

            /*Lister ls = new Lister(ctx);
            ls.createAndOpenDB();*/

            String[][] mData_logins = ls.executeReader("Select count(*) from LOGINS where uid = '" + login_useruid + "' and record_data = '" + TodayDate + "'");
            if (Integer.parseInt(mData_logins[0][0]) > 0) {
                Log.d("000258", "Login Record Date: " + mData_logins[0][0]);
                try {
                    if (serviceLocationLogin.showCurrentLocation() == true) {
                        latitude = serviceLocationLogin.getLatitude();
                        longitude = serviceLocationLogin.getLongitude();
                        Log.d("000258", " IF Service latitude: " + latitude);
                        Log.d("000258", " If Service longitude: " + longitude);


                    } else {
                        try {
                            ServiceLocation.doAsynchronousTask.cancel();
                            ServiceLocation_Login.doAsynchronousTask.cancel();
                        } catch (Exception e) {
                        }
                        try {
                            Lister lss = new Lister(ctx);
                            ls.createAndOpenDB();

                            String[][] mLogin_data = lss.executeReader("SELECT max(added_on),latitude, longitude,record_data,count(*) from LOGINS");

                            if (Integer.valueOf(mLogin_data[0][4]) > 0) {
                                Log.d("000258", " VAC IF User Last Latitude: " + mLogin_data[0][1]);
                                Log.d("000258", " VAC IF User Last Longitude: " + mLogin_data[0][2]);
                                Log.d("000258", " VAC IF User Last Login Date: " + mLogin_data[0][3]);

                                latitude = Double.parseDouble(mLogin_data[0][1]);
                                longitude = Double.parseDouble(mLogin_data[0][2]);

                            } else {
                                Log.d("000258", " ELSE  VAC Last Login Date!!!!!! less than 0");
                                latitude = Double.parseDouble("0.0");
                                longitude = Double.parseDouble("0.0");
                            }

                        } catch (Exception e) {
                            Toast.makeText(ctx, R.string.loginError, Toast.LENGTH_SHORT).show();
                            Log.d("000258", "VAC Read Login Error Catch: " + e.getMessage());
                        }
                    }

                    ////////////Save GPS latlng in shared pref
                    SharedPreferences prefw = getApplicationContext().getSharedPreferences(getString(R.string.loginGPSeng), 0); // 0 - for private mode
                    SharedPreferences.Editor editorw = prefw.edit();
                    editorw.putString("gps_latitude", String.valueOf(latitude));
                    editorw.putString("gps_longitude", String.valueOf(longitude));
                    editorw.apply();
                } catch (Exception e) {
                    Log.d("000258", "SharedPred GPS Catch: " + e.getMessage());
                }


                Intent intent1 = new Intent(ctx, HomePageVacinator_Activity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                temp_gps_var = "0";

                View toastView = getLayoutInflater().inflate(R.layout.custom_toast_view_layout, null);
                // Initiate the Toast instance.
                Toast toast = new Toast(getApplicationContext());
                // Set custom view in toast.
                toast.setView(toastView);

                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast.show();
                Log.d("000258", "IF VAC LOGIN");
            } else {

                insert_logins_db();
                // startActivity(new Intent(getApplicationContext(), HomePageVacinator_Activity.class));
                //Toast.makeText(getApplicationContext(), "صارف لاگ ان", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(ctx, HomePageVacinator_Activity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                temp_gps_var = "0";

                View toastView = getLayoutInflater().inflate(R.layout.custom_toast_view_layout, null);
                // Initiate the Toast instance.
                Toast toast = new Toast(getApplicationContext());
                // Set custom view in toast.
                toast.setView(toastView);

                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast.show();
                Log.d("000258", "ELSE VAC LOGIN");
            }

        } catch (Exception e) {
            Log.d("000258", "Check Login Err:  " + e.getMessage());
        }

    }

    private void insert_logins_db() {
        try {

            if (serviceLocationLogin.showCurrentLocation() == true) {
                latitude = serviceLocationLogin.getLatitude();
                longitude = serviceLocationLogin.getLongitude();
                Log.d("000258", " IF Service latitude: " + latitude);
                Log.d("000258", " If Service longitude: " + longitude);
            } else {
                try {
                    ServiceLocation.doAsynchronousTask.cancel();
                    ServiceLocation_Login.doAsynchronousTask.cancel();
                } catch (Exception e) {
                }
                try {
                   /* Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();*/

                    String[][] mLogin_data = ls.executeReader("SELECT max(added_on),latitude, longitude,record_data,count(*) from LOGINS");

                    if (Integer.valueOf(mLogin_data[0][4]) > 0) {
                        Log.d("000258", " IF User Last Latitude: " + mLogin_data[0][1]);
                        Log.d("000258", " IF User Last Longitude: " + mLogin_data[0][2]);
                        Log.d("000258", " IF User Last Login Date: " + mLogin_data[0][3]);

                        latitude = Double.parseDouble(mLogin_data[0][1]);
                        longitude = Double.parseDouble(mLogin_data[0][2]);

                        Toast.makeText(ctx, R.string.dataGPS, Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000258", " ELSE  Last Login Date");
                        latitude = Double.parseDouble("0.0");
                        longitude = Double.parseDouble("0.0");

                        Toast.makeText(ctx, R.string.notDataGPS, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(ctx, R.string.loginError, Toast.LENGTH_SHORT).show();
                    Log.d("000258", "Read Login Error Catch: " + e.getMessage());
                }
            }
            try {
                ////////////Save GPS latlng in shared pref
                SharedPreferences prefw = getApplicationContext().getSharedPreferences(getString(R.string.loginGPSeng), 0); // 0 - for private mode
                SharedPreferences.Editor editorw = prefw.edit();
                editorw.putString("gps_latitude", String.valueOf(latitude));
                editorw.putString("gps_longitude", String.valueOf(longitude));
                editorw.apply();
            } catch (Exception e) {
                Log.d("000258", "SharedPred GPS Catch: " + e.getMessage());
            }

            /*Lister ls = new Lister(ctx);
            ls.createAndOpenDB();*/

            String[][] db_version = ls.executeReader("PRAGMA USER_Version");
            Log.d("000390", " SQLite Database Version: " + db_version[0][0]);

            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
            Calendar c = Calendar.getInstance();
            String datetime = dates.format(c.getTime());


            JSONObject jobj = new JSONObject();
            jobj.put("version_name", "" + versionName);
            jobj.put("version_number", "" + versionNumber);
            jobj.put("sqlite_database_version", "" + db_version[0][0]);
            jobj.put("login_datetime", "" + datetime);

            String ans1 = "insert into LOGINS (uid, latitude, longitude,record_data, data, added_on)" +
                    "values" +
                    "(" +
                    "'" + login_useruid + "'," +
                    "'" + String.valueOf(latitude) + "'," +
                    "'" + String.valueOf(longitude) + "'," +
                    "'" + TodayDate + "'," +
                    "'" + jobj.toString() + "'," +
                    "'" + System.currentTimeMillis() + "'" +
                    ")";

            Boolean res = ls.executeNonQuery(ans1);
            Log.d("000258", "Data: " + ans1);
            Log.d("000258", "Query: " + res.toString());


            //  Toast.makeText(getApplicationContext(),String.valueOf(res)+String.valueOf(ans1),Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.d("000258", "Err: " + e.getMessage());
            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkPermission() {

        //  int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        //  int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), RECEIVE_SMS);
        //  int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_SYNC_SETTINGS);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        // int SixthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);
        int SixthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SeventhPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int EighthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), RECEIVE_SMS);
        int NinethPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS);
        int TenthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);


        return
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                        // ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                        //ForthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                        FifthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                        SixthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                        SeventhPermissionResult == PackageManager.PERMISSION_GRANTED &&
                        EighthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                        NinethPermissionResult == PackageManager.PERMISSION_GRANTED &&
                        TenthPermissionResult == PackageManager.PERMISSION_GRANTED;

        //EigthPermissionResult == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermission() {


        ActivityCompat.requestPermissions(Login_Activity.this, new String[]
                {
                        // SEND_SMS,READ_PHONE_STATE,WRITE_SYNC_SETTINGS
                        WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA, ACCESS_FINE_LOCATION, SEND_SMS, RECEIVE_SMS, READ_PHONE_STATE
                }, RequestPermissionCodeLogin);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCodeLogin:

                if (grantResults.length > 0) {

                    boolean WriteEXtPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExtPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean CameraPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean LocationStatePermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean RecvMSGPermission = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean SendMSGPermission = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadPhoneStatePermission = grantResults[6] == PackageManager.PERMISSION_GRANTED;
                    //  boolean RecvMSGPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    //  boolean WritesyncPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    //  boolean CallPhoneStatePermission = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                    // boolean SendSMSStatePermission = grantResults[7] == PackageManager.PERMISSION_GRANTED;

                    if (WriteEXtPermission && ReadExtPermission && CameraPermission && LocationStatePermission && RecvMSGPermission && SendMSGPermission && ReadPhoneStatePermission) {

                        Log.d("000258", "All Permission Allowed");

                        serviceLocationLogin = new ServiceLocation_Login(ctx);
                        serviceLocationLogin.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        serviceLocationLogin.callAsynchronousTask();

                        init_Directories();
                        init_DatabaseDirectories();
                        init_CSVFilesDirectories();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                serviceLocation = new ServiceLocation(ctx);
                                serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                serviceLocation.callAsynchronousTask();
                            }
                        }, 1500);


                    } else {
                        Log.d("000258", "All Permission Not Allowed");
                        serviceLocationLogin = new ServiceLocation_Login(ctx);
                        serviceLocationLogin.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        serviceLocationLogin.callAsynchronousTask();


                        init_Directories();
                        init_DatabaseDirectories();
                        init_CSVFilesDirectories();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                serviceLocation = new ServiceLocation(ctx);
                                serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                serviceLocation.callAsynchronousTask();
                            }
                        }, 1500);


                   /*     if (!WriteEXtPermission && !ReadExtPermission){
                            Toast.makeText(ctx, "فون سٹوریج کی پرمیشن کو اجازت دیں", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            init_Directories();
                            init_DatabaseDirectories();
                        }
                         if (!CameraPermission){
                            Toast.makeText(ctx, "کیمرے کی پرمیشن کو اجازت دیں", Toast.LENGTH_SHORT).show();
                        }

                        if(!LocationStatePermission){
                            Toast.makeText(ctx, "لوکیشن کی پرمیشن کو اجازت دیں", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            serviceLocation = new ServiceLocation(ctx);
                            serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            serviceLocation.callAsynchronousTask();
                        }*/
                    }
                }

                break;
        }
    }


    public void GooglePlayStoreUpdate() {
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String currentVersion = packageInfo.versionName;
        new ForceUpdateAsync(currentVersion, Login_Activity.this).execute();
    }

    public class ForceUpdateAsync extends AsyncTask<String, String, String> {

        private String playstoreVersion;
        private String currentVersion;
        private Context context;

        public ForceUpdateAsync(String currentVersion, Context context) {
            this.currentVersion = currentVersion;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                playstoreVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.akdndhrc.hayat_pk" + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();

                Log.d("123456", "playstore version " + playstoreVersion);
                Log.d("123456", "current version " + currentVersion);

            } catch (IOException e) {
                e.printStackTrace();

            }
            return "";
        }

        @Override
        protected void onPostExecute(String string) {
            if (currentVersion.equalsIgnoreCase(playstoreVersion)) {
            } else {
                //Toast.makeText(context, "برائے مہربانی ایپلیکیشن کو پلے اسٹور سے اپ ڈیٹ کریں", Toast.LENGTH_SHORT).show();

                final Snackbar snackbar = Snackbar.make(findViewById(R.id.login_layout), R.string.updateAppPlaystoreEng, Snackbar.LENGTH_LONG);
                ViewCompat.setLayoutDirection(snackbar.getView(), ViewCompat.LAYOUT_DIRECTION_LTR);
                snackbar.show();


                snackbar.setAction("update", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.akdndhrc.hayat_pk"));
                        startActivity(intent);

//                        Intent intent = new Intent(ctx,UpdateScreen_Activity.class);
//                        startActivity(intent);
                    }
                });

                snackbar.setActionTextColor(ctx.getResources().getColor(R.color.pink_color));

            }

            super.onPostExecute(string);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("000258", "ONRESUME");

        if (temp_gps_var.equalsIgnoreCase("0")) {
            Log.d("000258", "ONRESUME");

            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            TodayDate = dates.format(c.getTime());
            Log.d("000258", "Today Date : " + TodayDate);

            if (checkPermission()) {
                Log.d("000258", "All permission allowed now");
                serviceLocationLogin = new ServiceLocation_Login(ctx);
                serviceLocationLogin.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                serviceLocationLogin.callAsynchronousTask();

                init_Directories();
                init_DatabaseDirectories();
                init_CSVFilesDirectories();

            } else {
                requestPermission();
                Log.d("000258", "Request Permission");
            }
            Log.d("000258", "ONRESUME IFFFF");
        } else {
            Log.d("000258", "ONRESUME ELSEEEEE");
        }

        try {
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPreferences", 0); // 0 - for private mode
            SharedPreferences.Editor editor = preferences.edit();

            String last_sync_time = preferences.getString("last_sync_time", "");
            txt_last_sync_datettime.setText(last_sync_time);
            Log.d("000258", "Last_sync_time : " + last_sync_time);

        } catch (Exception e) {
            Log.d("000258", "ERRRROR : " + e.getMessage());
        }

    }


    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5 && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Log.d("000777", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        Log.d("000777", "onBackPressed Called");
        finishAffinity();
        temp_gps_var = "0";
    }

}
