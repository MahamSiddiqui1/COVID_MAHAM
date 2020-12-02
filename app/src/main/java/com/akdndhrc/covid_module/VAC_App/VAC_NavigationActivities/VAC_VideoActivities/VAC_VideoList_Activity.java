package com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities.VAC_VideoActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.CustomGridViewAdapter;
import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.GPSTracker;
import com.akdndhrc.covid_module.Item;
import com.akdndhrc.covid_module.ServiceLocation;
import com.akdndhrc.covid_module.Utils;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.VAC_App.HomePageVacinator_Activity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rey.material.widget.CheckBox;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class VAC_VideoList_Activity extends AppCompatActivity {

    Context ctx = VAC_VideoList_Activity.this;

    ListView lv;
    CheckBox checkbox_15to19_age, checkbox_20to49_age, checkbox_49plus, checkbox_community_leaders;
    ImageView iv_close, iv_navigation_drawer, iv_home;
    LinearLayout ll_15to19_age, ll_20to49_age, ll_49plus_age, ll_communityleaders;
    RelativeLayout rl_add_male_15to19_age, rl_sub_male_15to19_age, rl_add_female_15to19_age, rl_sub_female_15to19_age, rl_add_male_20to49_age, rl_sub_male_20to49_age, rl_add_female_20to49_age, rl_sub_female_20to49_age,
            rl_add_male_49plus_age, rl_sub_male_49plus_age, rl_add_female_49plus_age, rl_sub_female_49plus_age, rl_add_male_communityleaders, rl_sub_male_communityleaders, rl_add_female_communityleaders, rl_sub_female_communityleaders;

    TextView tv_count_male_15to19_age, tv_count_female_15to19_age, tv_count_male_20to49_age, tv_count_female_20to49_age, tv_count_male_49plus_age, tv_count_female_49plus_age, tv_count_male_communityleaders, tv_count_female_communityleaders, tv_record;

    Button btn_jamaa_kre;
    final ArrayList<String> arrayListPatientFile = new ArrayList<>();
    int counter_male1, counter_female1, counter_male2, counter_female2, counter_male3, counter_female3, counter_male4, counter_female4;
    String videoLink, TodayDate;
    double latitude;
    double longitude;
    // GPSTracker class
    GPSTracker gps;

    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid;
    TextView tv_NoVideoExists;

   ArrayList<Item> gridArray = new ArrayList<Item>();
    long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, VAC_VideoList_Activity.class));


        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences("UserLogin", 0); // 0 - for private mode
            String shared_useruid = prefelse.getString("login_userid", null); // getting String
            login_useruid = shared_useruid;
            Log.d("000555", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000555", "Shared Err:" + e.getMessage());
        }

        //ListView
        lv = (ListView) findViewById(R.id.lv);

        //TextView
        tv_NoVideoExists = (TextView) findViewById(R.id.tv_NoVideoExists);

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);

        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);


        try {
            serviceLocation = new ServiceLocation(ctx);
            serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            serviceLocation.callAsynchronousTask();
        } catch (Exception e) {
            Log.d("000555", "GPS Service Err:  " + e.getMessage());
        }

        // check_gps();

        //init_Directories();
        ListDir();

//        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//
//        iv_home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent newIntent = new Intent(ctx, HomePage_Activity.class);
//                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(newIntent);
//            }
//        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                videoLink = arrayListPatientFile.get(position);
                Dialog_MaleFemaleCount();
            }
        });
    }


    void ListDir() {

        try{
            File root = new File(Environment.getExternalStorageDirectory() + "/HayatPK/Videos/");

            final ArrayList<String> arrayListPatient = new ArrayList<>();

            File[] files = root.listFiles();

            if (files.length > 0)
            {
                for (File file : files) {

                    Log.d("000200", "************ VIDEOS AVAILABLE **************");
                    tv_NoVideoExists.setVisibility(View.GONE);

                    Log.d("000111", String.valueOf(file.getAbsoluteFile()));
                    Log.d("000111", String.valueOf(file.getPath()));

                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(String.valueOf(file));

                    int timeInSeconds = 25;
                    Bitmap thumb = ThumbnailUtils.extractThumbnail(retriever.getFrameAtTime(timeInSeconds * 1000000,
                            MediaMetadataRetriever.OPTION_CLOSEST_SYNC), 500, 500);

                    gridArray.add(new Item(thumb, file.getName()));

                    arrayListPatientFile.add(file.getName());
                    arrayListPatient.add(file.getName());//0

                    Log.d("000111", "Name:" + arrayListPatient);
                    Log.d("0001110", "FILE Names:" + file.getName());
                }
            }
            else {
                Log.d("000200", "++++++++ NO VIDEO AVAILABLE +++++++++++");
                tv_NoVideoExists.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            Log.d("000200", "Read VIDEOS Error: " + e.getMessage());
        }

        /*ADT adt = new ADT(VideoList_Activity.this, arrayListPatient);
        lv.setAdapter(adt);*/

        CustomGridViewAdapter customGridAdapter = new CustomGridViewAdapter(this, R.layout.custom_lv_item_video_list, gridArray);
        lv.setAdapter(customGridAdapter);

    }

    private void init_Directories() {

        try {

            String folder = Environment.getExternalStorageDirectory() + File.separator + "HayatPK" + File.separator + "VAC_Videos";

            File directory = new File(folder);

            boolean success = true;
            if (!directory.exists()) {
                directory.mkdirs();
            }

            if (success) {
                // Toast.makeText(ctx, "Suceess", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(ctx, "Failed", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            // Toast.makeText(this, "Faild to Create Folder", Toast.LENGTH_SHORT).show();
            Log.d("000999", "Failed" + e.getMessage());
        }
    }


    public void Dialog_MaleFemaleCount() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.dialog_video_male_and_female_layout, null);

        final AlertDialog alertD = new AlertDialog.Builder(this).create();

        iv_close = (ImageView) promptView.findViewById(R.id.iv_close);

        btn_jamaa_kre = promptView.findViewById(R.id.submit);

        //CheckBox
        checkbox_15to19_age = promptView.findViewById(R.id.checkbox_15to19_age);
        checkbox_20to49_age = promptView.findViewById(R.id.checkbox_20to49_age);
        checkbox_49plus = promptView.findViewById(R.id.checkbox_49plus);
        checkbox_community_leaders = promptView.findViewById(R.id.checkbox_community_leaders);

        //LinearLayout
        ll_15to19_age = promptView.findViewById(R.id.ll_15to19_age);
        ll_20to49_age = promptView.findViewById(R.id.ll_20to49_age);
        ll_49plus_age = promptView.findViewById(R.id.ll_49plus_age);
        ll_communityleaders = promptView.findViewById(R.id.ll_communityleaders);


        //RelativeLayout
        rl_add_male_15to19_age = promptView.findViewById(R.id.rl_add_male_15to19_age);
        rl_sub_male_15to19_age = promptView.findViewById(R.id.rl_sub_male_15to19_age);

        rl_add_female_15to19_age = promptView.findViewById(R.id.rl_add_female_15to19_age);
        rl_sub_female_15to19_age = promptView.findViewById(R.id.rl_sub_female_15to19_age);

        rl_add_male_20to49_age = promptView.findViewById(R.id.rl_add_male_20to49_age);
        rl_sub_male_20to49_age = promptView.findViewById(R.id.rl_sub_male_20to49_age);

        rl_add_female_20to49_age = promptView.findViewById(R.id.rl_add_female_20to49_age);
        rl_sub_female_20to49_age = promptView.findViewById(R.id.rl_sub_female_20to49_age);

        rl_add_male_49plus_age = promptView.findViewById(R.id.rl_add_male_49plus_age);
        rl_sub_male_49plus_age = promptView.findViewById(R.id.rl_sub_male_49plus_age);

        rl_add_female_49plus_age = promptView.findViewById(R.id.rl_add_female_49plus_age);
        rl_sub_female_49plus_age = promptView.findViewById(R.id.rl_sub_female_49plus_age);

        rl_add_male_communityleaders = promptView.findViewById(R.id.rl_add_male_communityleaders);
        rl_sub_male_communityleaders = promptView.findViewById(R.id.rl_sub_male_communityleaders);

        rl_add_female_communityleaders = promptView.findViewById(R.id.rl_add_female_communityleaders);
        rl_sub_female_communityleaders = promptView.findViewById(R.id.rl_sub_female_communityleaders);

        //TextView
        tv_count_male_15to19_age = promptView.findViewById(R.id.tv_count_male_15to19_age);
        tv_count_female_15to19_age = promptView.findViewById(R.id.tv_count_female_15to19_age);

        tv_count_male_20to49_age = promptView.findViewById(R.id.tv_count_male_20to49_age);
        tv_count_female_20to49_age = promptView.findViewById(R.id.tv_count_female_20to49_age);

        tv_count_male_49plus_age = promptView.findViewById(R.id.tv_count_male_49plus_age);
        tv_count_female_49plus_age = promptView.findViewById(R.id.tv_count_female_49plus_age);

        tv_count_male_communityleaders = promptView.findViewById(R.id.tv_count_male_communityleaders);
        tv_count_female_communityleaders = promptView.findViewById(R.id.tv_count_female_communityleaders);


        counter_male1 = Integer.parseInt("0");
        counter_female1 = Integer.parseInt("0");

        counter_male2 = Integer.parseInt("0");
        counter_female2 = Integer.parseInt("0");

        counter_male3 = Integer.parseInt("0");
        counter_female3 = Integer.parseInt("0");

        counter_male4 = Integer.parseInt("0");
        counter_female4 = Integer.parseInt("0");


        tv_count_male_15to19_age.setText("" + counter_male1);
        tv_count_female_15to19_age.setText("" + counter_female1);

        tv_count_male_20to49_age.setText("" + counter_male2);
        tv_count_female_20to49_age.setText("" + counter_female2);

        tv_count_male_49plus_age.setText("" + counter_male3);
        tv_count_female_49plus_age.setText("" + counter_female3);

        tv_count_male_communityleaders.setText("" + counter_male4);
        tv_count_female_communityleaders.setText("" + counter_female4);


        alertD.setView(promptView);

        alertD.setCanceledOnTouchOutside(false);
        alertD.setCancelable(false);
        alertD.show();

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
            }
        });

        checkbox_15to19_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox_15to19_age.isChecked()) {
                    ll_15to19_age.setVisibility(View.VISIBLE);
                } else {
                    ll_15to19_age.setVisibility(View.GONE);
                    tv_count_male_15to19_age.setText("0");
                    tv_count_female_15to19_age.setText("0");

                }
            }
        });

        checkbox_20to49_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox_20to49_age.isChecked()) {
                    ll_20to49_age.setVisibility(View.VISIBLE);
                } else {
                    ll_20to49_age.setVisibility(View.GONE);
                    tv_count_male_20to49_age.setText("0");
                    tv_count_female_20to49_age.setText("0");
                }
            }
        });

        checkbox_49plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox_49plus.isChecked()) {
                    ll_49plus_age.setVisibility(View.VISIBLE);
                } else {
                    ll_49plus_age.setVisibility(View.GONE);
                    tv_count_male_49plus_age.setText("0");
                    tv_count_female_49plus_age.setText("0");
                }
            }
        });


        checkbox_community_leaders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox_community_leaders.isChecked()) {
                    ll_communityleaders.setVisibility(View.VISIBLE);
                } else {
                    ll_communityleaders.setVisibility(View.GONE);
                    tv_count_male_communityleaders.setText("0");
                    tv_count_female_communityleaders.setText("0");
                }
            }
        });


        //Counter Male 15 t0 19 Age
        rl_add_male_15to19_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter_male1 < 10) {
                    counter_male1 += 1;
                    tv_count_male_15to19_age.setText("" + counter_male1);

                    Log.d("123456", ": " + counter_male1);
                } else {
                    tv_count_male_15to19_age.setText("" + counter_male1);
                    Log.d("123456", ":: " + counter_male1);
                }
            }
        });
        rl_sub_male_15to19_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter_male1 > 0) {
                    counter_male1 -= 1;
                    tv_count_male_15to19_age.setText("" + counter_male1);
                    Log.d("123456", ": " + counter_male1);
                } else {
                    tv_count_male_15to19_age.setText("" + counter_male1);
                    Log.d("123456", ": " + counter_male1);
                }
            }


        });

        //Counter Female 15 t0 19 Age
        rl_add_female_15to19_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter_female1 < 10) {
                    counter_female1 += 1;
                    tv_count_female_15to19_age.setText("" + counter_female1);
                    Log.d("123456", "* " + counter_female1);
                } else {
                    tv_count_female_15to19_age.setText("" + counter_female1);
                    Log.d("123456", "* " + counter_female1);
                }
            }
        });
        rl_sub_female_15to19_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter_female1 > 0) {
                    counter_female1 -= 1;
                    tv_count_female_15to19_age.setText("" + counter_female1);
                    Log.d("123456", "* " + counter_female1);
                } else {
                    tv_count_female_15to19_age.setText("" + counter_female1);
                    Log.d("123456", "* " + counter_female1);
                }
            }
        });

        //Counter Male 20 to 49 Age
        rl_add_male_20to49_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter_male2 < 10) {
                    counter_male2 += 1;
                    tv_count_male_20to49_age.setText("" + counter_male2);
                } else {
                    tv_count_male_20to49_age.setText("" + counter_male2);
                }
            }
        });
        rl_sub_male_20to49_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter_male2 > 0) {
                    counter_male2 -= 1;
                    tv_count_male_20to49_age.setText("" + counter_male2);
                } else {
                    tv_count_male_20to49_age.setText("" + counter_male2);
                }
            }
        });

        //Counter Female 20 to 49 Age
        rl_add_female_20to49_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter_female2 < 10) {
                    counter_female2 += 1;
                    tv_count_female_20to49_age.setText("" + counter_female2);
                } else {
                    tv_count_female_20to49_age.setText("" + counter_female2);
                }
            }
        });
        rl_sub_female_20to49_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter_female2 > 0) {
                    counter_female2 -= 1;
                    tv_count_female_20to49_age.setText("" + counter_female2);
                } else {
                    tv_count_female_20to49_age.setText("" + counter_female2);
                }
            }
        });

        //Counter Male 49+ Age
        rl_add_male_49plus_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter_male3 < 10) {
                    counter_male3 += 1;
                    tv_count_male_49plus_age.setText("" + counter_male3);
                } else {
                    tv_count_male_49plus_age.setText("" + counter_male3);
                }
            }
        });
        rl_sub_male_49plus_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter_male3 > 0) {
                    counter_male3 -= 1;
                    tv_count_male_49plus_age.setText("" + counter_male3);
                } else {
                    tv_count_male_49plus_age.setText("" + counter_male3);
                }
            }
        });


        //Counter Female 49+ Age
        rl_add_female_49plus_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter_female3 < 10) {
                    counter_female3 += 1;
                    tv_count_female_49plus_age.setText("" + counter_female3);
                } else {
                    tv_count_female_49plus_age.setText("" + counter_female3);
                }
            }
        });
        rl_sub_female_49plus_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter_female3 > 0) {
                    counter_female3 -= 1;
                    tv_count_female_49plus_age.setText("" + counter_female3);
                } else {
                    tv_count_female_49plus_age.setText("" + counter_female3);
                }
            }
        });

        //Counter Male Community Leaders
        rl_add_male_communityleaders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter_male4 < 10) {
                    counter_male4 += 1;
                    tv_count_male_communityleaders.setText("" + counter_male4);
                } else {
                    tv_count_male_communityleaders.setText("" + counter_male4);
                }
            }
        });
        rl_sub_male_communityleaders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter_male4 > 0) {
                    counter_male4 -= 1;
                    tv_count_male_communityleaders.setText("" + counter_male4);
                } else {
                    tv_count_male_communityleaders.setText("" + counter_male4);
                }
            }
        });

        //Counter Female Community Leaders
        rl_add_female_communityleaders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter_female4 < 10) {
                    counter_female4 += 1;
                    tv_count_female_communityleaders.setText("" + counter_female4);
                } else {
                    tv_count_female_communityleaders.setText("" + counter_female4);
                }
            }
        });
        rl_sub_female_communityleaders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter_female4 > 0) {
                    counter_female4 -= 1;
                    tv_count_female_communityleaders.setText("" + counter_female4);
                } else {
                    tv_count_female_communityleaders.setText("" + counter_female4);
                }
            }
        });

        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!checkbox_15to19_age.isChecked() && !checkbox_20to49_age.isChecked() && !checkbox_49plus.isChecked() && !checkbox_community_leaders.isChecked()) {
                    //Toast.makeText(ctx, "Please select age checkbox", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ctx, "برائے مہربانی عمر کے چیک باکس کو منتخب کریں", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tv_count_male_15to19_age.getText().equals("0") && tv_count_female_15to19_age.getText().equals("0") &&
                        tv_count_male_20to49_age.getText().equals("0") && tv_count_female_20to49_age.getText().equals("0") &&
                        tv_count_male_49plus_age.getText().equals("0") && tv_count_female_49plus_age.getText().equals("0") &&
                        tv_count_male_communityleaders.getText().equals("0") && tv_count_female_communityleaders.getText().equals("0")) {
                    //  Toast.makeText(ctx, "Please enter number of males and females", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ctx, "برائے مہربانی مرد اور خواتین کی تعداد درج کریں", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (serviceLocation.showCurrentLocation() == true) {
                    latitude = serviceLocation.getLatitude();
                    longitude = serviceLocation.getLongitude();
                    Log.d("000555", " latitude: " + latitude);
                    Log.d("000555", " longitude: " + longitude);
                } else {
                    try {
                        serviceLocation.doAsynchronousTask.cancel();
                    }catch (Exception e){}
                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from VIDEOS");

                        if (Integer.parseInt(mData[0][2]) >  0 ) {
                            JSONObject jsonObject = new JSONObject(mData[0][1]);
                            Log.d("000258", "  Last Latitude: " + jsonObject.getString("lat"));
                            Log.d("000258", "  Last Longitude: " + jsonObject.getString("lng"));

                            latitude = Double.parseDouble(jsonObject.getString("lat"));
                            longitude = Double.parseDouble(jsonObject.getString("lng"));

                            Toast.makeText(ctx, R.string.dataGPS, Toast.LENGTH_SHORT).show();
                        } else {
                            latitude = Double.parseDouble("0.0");
                            longitude = Double.parseDouble("0.0");
                            Toast.makeText(ctx, R.string.notDataGPS, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.d("000258", "Read VIDEOS Error: " + e.getMessage());
                    }
                }

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Log.d("000555", " mLastClickTime: " + mLastClickTime);



                try {


                    Lister ls = new Lister(VAC_VideoList_Activity.this);
                    ls.createAndOpenDB();

                    // et_refferal_ki_waja = findViewById(R.id.et_refferal_ki_waja);
                    // et_refferal_hospital = findViewById(R.id.et_refferal_hospital);

                    //Edittext
                    JSONObject jobj = new JSONObject();
                    //TextView

                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("type", "" + "VAC");
                    jobj.put("tv_count_male_15to19_age", "" + tv_count_male_15to19_age.getText().toString());
                    jobj.put("tv_count_female_15to19_age", "" + tv_count_female_15to19_age.getText().toString());
                    jobj.put("tv_count_male_20to49_age", "" + tv_count_male_20to49_age.getText().toString());//spinner
                    jobj.put("tv_count_female_20to49_age", "" + tv_count_female_20to49_age.getText().toString());
                    jobj.put("tv_count_male_49plus_age", "" + tv_count_male_49plus_age.getText().toString());
                    jobj.put("tv_count_female_49plus_age", "" + tv_count_female_49plus_age.getText().toString());
                    jobj.put("tv_count_male_communityleaders", "" + tv_count_male_communityleaders.getText().toString());
                    jobj.put("tv_count_female_communityleaders", "" + tv_count_female_communityleaders.getText().toString());

                    String added_on = String.valueOf(System.currentTimeMillis());


                    // jobjMain.put("data", jobj);
                    String ans1 = "insert into VIDEOS (video_name, record_data, data,added_by, is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + videoLink + "'," +
                            "'" + TodayDate + "'," +
                            "'" + jobj + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000555", "Query: " + ans1);
                    Log.d("000555", "Json Data:" + res.toString());

                    if (Utils.haveNetworkConnection(ctx) > 0) {

                        sendPostRequest(videoLink, TodayDate, String.valueOf(jobj), login_useruid, added_on);
                    } else {
                    }

                } catch (Exception e) {
                    Log.d("000888", "Error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                } finally {
                    Intent i = new Intent(ctx, VAC_VideoPlay_Activity.class);
                    i.putExtra("KeyVideoLink", videoLink);
                    startActivity(i);
                    alertD.dismiss();

                }


            }
        });

    }

    private void sendPostRequest(final String video_name, final String record_data,
                                 final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/generic/videos";

        Log.d("000555", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = "volleyStringRequest";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {


                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {

                        Log.d("000555", "Response:    " + response);

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE VIDEOS SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE added_by = '" + added_by + "' AND added_on= '" + added_on + "' ";

                        ls.executeNonQuery(update_record);

                        Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000555", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", "Catch:  " + e.getMessage());
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "error    " + error.getMessage());
                // Toast.makeText(ctx, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("video_name", video_name);
                params.put("record_data", record_data);
                params.put("data", data);
                params.put("added_by", added_by);
                params.put("added_on", added_on);


                Log.d("000555", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000555", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }


    private void check_gps() {

        //GPS\
        gps = new GPSTracker(ctx);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.d("000555", "latitude value: " + latitude);
            Log.d("000555", "longitude value: " + longitude);
        } else {
            gps.showSettingsAlert();
            Toast.makeText(ctx, R.string.GPSonAlert, Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void check_gps2() {

        //GPS\
        gps = new GPSTracker(ctx);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            if (latitude <= 0 && longitude <= 0) {
               /* Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        check_gps2();
                    }
                }, 1500);*/

                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        check_gps2();
                    }
                };

                runnable.run();

                Log.d("000555", "IF lat lng: ");
                Log.d("000555", "latitude: " + latitude);
                Log.d("000555", "longitude: " + longitude);


                return;
            } else {
                snackbar.dismiss();
                Log.d("000555", "ELSE lat lng: ");
                Log.d("000555", "latitude: " + latitude);
                Log.d("000555", "longitude: " + longitude);

                Toast.makeText(ctx, R.string.GPSonMessage, Toast.LENGTH_SHORT).show();
            }

        } else {
            gps.showSettingsAlert();
            Toast.makeText(ctx, R.string.GPSonAlert, Toast.LENGTH_LONG).show();
            return;
        }
    }


    public class ADT extends BaseAdapter {

        Context ctx;
        ArrayList<String> arrayListPatient;
        LayoutInflater inflater;


        public ADT(Context ctx, ArrayList<String> arrayListPatient) {
            this.ctx = ctx;
            this.arrayListPatient = arrayListPatient;
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return arrayListPatient.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {

            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();

                view = inflater.inflate(R.layout.custom_lv_item_video_list, null);
                holder.tvComments = (TextView) view.findViewById(R.id.tvComments);
                holder.rl = (RelativeLayout) view.findViewById(R.id.rl);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();

            }
            holder.tvComments.setText(arrayListPatient.get(position));


            return view;
        }

    }

    public static class ViewHolder {

        protected TextView tvComments;
        RelativeLayout rl;
    }


    @Override
    public void onBackPressed() {
        Intent newIntent = new Intent(ctx, HomePageVacinator_Activity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
    }
}
