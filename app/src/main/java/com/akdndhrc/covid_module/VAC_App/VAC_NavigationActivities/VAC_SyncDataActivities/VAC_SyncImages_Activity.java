package com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities.VAC_SyncDataActivities;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.Adt_SyncImages.Adt_SyncImages;
import com.akdndhrc.covid_module.CustomClass.NetworkClient;
import com.akdndhrc.covid_module.CustomClass.UploadAPIs;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;



public class VAC_SyncImages_Activity extends AppCompatActivity {

    Context ctx = VAC_SyncImages_Activity.this;
    ListView lv;
    ImageView iv_navigation_drawer, iv_sync;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
    Adt_SyncImages adt;
    Dialog alertDialog;
    private Handler mHandler = new Handler();
    TextView tv_record;
    String temp = "0";
    String mData[][], pos_value;

    String login_useruid;
    LinearLayout ll_pbProgress;
    String var ="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_images_vac);

        temp="0";
        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode

            String shared_useruid = prefelse.getString((R.string.loginUserIDEng), null); // getting String
            login_useruid = shared_useruid;
            Log.d("000555", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000555", "Shared Err:" + e.getMessage());
        }

        //ListView
        lv = findViewById(R.id.lv);

        //TextView
        tv_record = findViewById(R.id.tv_record);
        TextView tv = findViewById(R.id.tv);
        //tv.setText("سینک تصاویر");

        ll_pbProgress = findViewById(R.id.ll_pbProgress);

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_sync = findViewById(R.id.iv_sync);

        iv_sync.setVisibility(View.GONE);
        iv_navigation_drawer.setVisibility(View.GONE);


        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, R.string.navigation, Toast.LENGTH_SHORT).show();
            }
        });


        ll_pbProgress.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                read_data();
            }
        }, 1000);


        iv_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                if (isConnectingToInternet()) {

                    if (temp.equalsIgnoreCase("0")) {
                        alertDialog = new Dialog(ctx);
                        LayoutInflater layout = LayoutInflater.from(ctx);
                        final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

                        alertDialog.setContentView(dialogView);
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialog.show();

                        Log.d("000666", "IFFFF");
                    } else {
                        Log.d("000666", "ELssssss");
                    }

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                if (arrayList.size() > 0) {

                                    Log.d("000456", "Member_id: " + arrayList.get(0).get("member_uid"));
                                    Log.d("000456", "vaccine_id: " + arrayList.get(0).get("vaccine_id"));

                                    String link = arrayList.get(0).get("image_location");
                                    Log.d("000456", link);

                                    uploadToServer_Overall(link);

                                } else {
                                    alertDialog.dismiss();
                                    onBackPressed();
                                    //  Toast.makeText(ctx, "No data found", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                alertDialog.dismiss();
                                Log.d("000666", "Error:" + e.getMessage());
                            }


                        }
                    }, 1500);


                } else {
                    if (temp.equalsIgnoreCase("1")) {
                        alertDialog.dismiss();

                    } else {

                    }

                    Toast.makeText(ctx, R.string.checkInternetPrompt, Toast.LENGTH_SHORT).show();
                }


            }
        });

       /* lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    String link = arrayList.get(position).get("image_location");
                    Log.d("000666", link);
                    uploadToServer(link);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("000666", "Error:" + e.getMessage());
                }
            }
        });*/

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                if (isConnectingToInternet()) {

                    try {
                        alertDialog = new Dialog(ctx);
                        LayoutInflater layout = LayoutInflater.from(ctx);
                        final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

                        alertDialog.setContentView(dialogView);
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialog.show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    pos_value = String.valueOf(position);
                                    String link = arrayList.get(position).get("image_location");
                                    Log.d("000666", link);
                                    uploadToServer(link);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                    alertDialog.dismiss();
                                    Log.d("000666", "Error:" + e.getMessage());
                                }

                            }
                        }, 1000);

                    } catch (Exception e) {
                        Log.d("000333", "Catach Err: " + e.getMessage());
                    }

                } else {
                    Toast.makeText(ctx, R.string.checkInternetPrompt, Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*adt = new Adt_SyncImages(ctx, arrayListFile);
        lv.setAdapter(adt);*/


    }

    private void read_data() {

        arrayList.clear();

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();


            mData = ls.executeReader("Select member_uid, vaccine_id, image_location, added_on from CVACCINATION where is_synced = '1' AND type='0'");

            HashMap<String, String> map;

            for (int i = 0; i < mData.length; i++) {


                map = new HashMap<>();

                String mData_vacine[][] = ls.executeReader("Select name from VACCINES where uid = '" + mData[i][1] + "'");

                File file = new File(mData[i][2]);
                if (file.exists()) {

                    tv_record.setVisibility(View.GONE);
                    iv_sync.setVisibility(View.VISIBLE);

                    Log.d("000666", "File Exit");

                    map.put("member_uid", "" + mData[i][0]);
                    map.put("vaccine_id", "" + mData[i][1]);
                    map.put("image_location", "" + mData[i][2]);
                    map.put("added_on", "" + mData[i][3]);

                    for (int j = 0; j < mData_vacine.length; j++) {

                        Log.d("000666", "Vaccine NAme: " + mData_vacine[j][0]);
                        map.put("vaccine_name", "" + mData_vacine[j][0]);
                    }

                    Log.d("000666", "member_uid: " + mData[i][0]);
                    Log.d("000666", "vaccine_id: " + mData[i][1]);
                    Log.d("000666", "image_location: " + mData[i][2]);

                    arrayList.add(map);
                    var="1";


                } else {
                    Log.d("000666", "Not File Exit");
                    continue;
                }
            }

            if (var.equalsIgnoreCase("1"))
            {
            }
            else{
                tv_record.setVisibility(View.VISIBLE);
            }
            adt = new Adt_SyncImages(ctx, arrayList);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);
            ll_pbProgress.setVisibility(View.GONE);


    } catch(Exception e)
    {
        Log.d("000666", " Error" + e.getMessage());
        iv_sync.setVisibility(View.GONE);
        tv_record.setVisibility(View.VISIBLE);
        ll_pbProgress.setVisibility(View.GONE);
    }

}

    private void uploadToServer(String filePath) throws IOException {

        try {

            final Retrofit retrofit = NetworkClient.getRetrofitClient(this);
            UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);

            //Create a file object using file path
            File file = new File(filePath);
            Log.d("000666", "FILE: " + file);
            // Create a request body with file and image media type

            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/png"), file);
            RequestBody member_id = RequestBody.create(null, arrayList.get(Integer.parseInt(pos_value)).get("member_uid"));
            RequestBody vaccine_id = RequestBody.create(null, arrayList.get(Integer.parseInt(pos_value)).get("vaccine_id"));
            RequestBody added_by = RequestBody.create(null, login_useruid);
            RequestBody added_on = RequestBody.create(null, arrayList.get(Integer.parseInt(pos_value)).get("added_on"));


            Call<ResponseBody> call = uploadAPIs.uploadImage(fileReqBody, member_id, vaccine_id, added_by, added_on);
            call.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {

                       /* String resp = response.body().string();
                        Log.d("000666", "Resp " + resp);
                        Log.d("000666", "Response: " + response.isSuccessful());
                       // Toast.makeText(ctx, "" + resp, Toast.LENGTH_SHORT).show();*/

                        if (response.isSuccessful() == true) {
                            String resp = response.body().string();
                            Log.d("000666", "Resp: " + resp);
                            Log.d("000666", "Response: " + response.isSuccessful());


                            Lister ls = new Lister(ctx);
                            ls.createAndOpenDB();

                            String update_record = "UPDATE CVACCINATION SET " +
                                    "is_synced='" + String.valueOf(2) + "' " +
                                    "WHERE member_uid = '" + arrayList.get(Integer.parseInt(pos_value)).get("member_uid") + "' AND vaccine_id= '" + arrayList.get(Integer.parseInt(pos_value)).get("vaccine_id") + "'AND added_on= '" + arrayList.get(Integer.parseInt(pos_value)).get("added_on") + "'";

                            ls.executeNonQuery(update_record);
                            Toast.makeText(ctx, R.string.picSynced, Toast.LENGTH_SHORT).show();

                            arrayList.remove(Integer.parseInt(pos_value));
                            if (arrayList.size() > 0) {
                                alertDialog.dismiss();
                                adt.notifyDataSetChanged();

                            } else {
                                adt.notifyDataSetChanged();
                                alertDialog.dismiss();
                                tv_record.setVisibility(View.VISIBLE);

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        finish();
                                    }
                                },1500);
                            }

                        } else {
                            alertDialog.dismiss();
                            Log.d("000555", "else ");
                            Toast.makeText(VAC_SyncImages_Activity.this, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        alertDialog.dismiss();
                        Log.d("000666", "catch: " + e.getMessage());
                        Toast.makeText(ctx, R.string.picNotSynced, Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    alertDialog.dismiss();
                    Log.d("000666", "Failed: " + t.getMessage());
                    Toast.makeText(ctx, R.string.picNotSynced, Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            alertDialog.dismiss();
            Log.d("000666", "catch: " + e.getMessage());
        }

    }

    private void uploadToServer_Overall(String filePath) throws IOException {

        try {

            final Retrofit retrofit = NetworkClient.getRetrofitClient(this);
            UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);

            //Create a file object using file path
            File file = new File(filePath);
            Log.d("000666", "FILE: " + file);
            // Create a request body with file and image media type

            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/png"), file);
            RequestBody member_id = RequestBody.create(null, arrayList.get(0).get("member_uid"));
            RequestBody vaccine_id = RequestBody.create(null, arrayList.get(0).get("vaccine_id"));
            RequestBody added_by = RequestBody.create(null, login_useruid);
            RequestBody added_on = RequestBody.create(null, arrayList.get(0).get("added_on"));


            Call<ResponseBody> call = uploadAPIs.uploadImage(fileReqBody, member_id, vaccine_id, added_by, added_on);
            call.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {

                       /* String resp = response.body().string();
                        Log.d("000666", "Resp " + resp);
                        Log.d("000666", "Response: " + response.isSuccessful());
                       // Toast.makeText(ctx, "" + resp, Toast.LENGTH_SHORT).show();*/

                        if (response.isSuccessful() == true) {
                            String resp = response.body().string();
                            Log.d("000666", "Resp: " + resp);
                            Log.d("000666", "Response: " + response.isSuccessful());


                            Lister ls = new Lister(ctx);
                            ls.createAndOpenDB();
                            String update_record = "UPDATE CVACCINATION SET " +
                                    "is_synced='" + String.valueOf(2) + "' " +
                                    "WHERE member_uid = '" + arrayList.get(0).get("member_uid") + "' AND vaccine_id= '" + arrayList.get(0).get("vaccine_id") + "'AND added_on= '" + arrayList.get(0).get("added_on") + "'";

                            ls.executeNonQuery(update_record);

                            Toast.makeText(ctx, R.string.picSynced, Toast.LENGTH_SHORT).show();

                            arrayList.remove(0);
                            adt.notifyDataSetChanged();

                            reload();
                        } else {
                            alertDialog.dismiss();
                            Log.d("000555", "else ");
                            Toast.makeText(VAC_SyncImages_Activity.this, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        alertDialog.dismiss();
                        Log.d("000666", "catch: " + e.getMessage());
                        Toast.makeText(ctx, R.string.picNotSynced, Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("000666", "Failed: " + t.getMessage());
                    Toast.makeText(ctx, R.string.picNotSynced, Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    temp = "0";
                    var="0";
                    finish();
                }
            });

        } catch (Exception e) {
            alertDialog.dismiss();
            Log.d("000666", "catch: " + e.getMessage());
        }

    }

    public void reload() {

       /* mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {*/

        if (arrayList.size() > 0) {
            temp = "1";
            iv_sync.performClick();
        } else {
            temp = "0";
            var = "0";
            alertDialog.dismiss();
            tv_record.setVisibility(View.VISIBLE);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    finish();
                }
            },1500);
            //startActivity(new Intent(ctx, HomePageVacinator_Activity.class));
        }

            /*}
        }, 1000);*/

    }

  /*  @Override
    protected void onResume() {
        super.onResume();

        arrayList.clear();

        try {
            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();


            mData = ls.executeReader("Select member_uid, vaccine_id, image_location, added_on from CVACCINATION where is_synced = '1' AND type='0'");

            HashMap<String, String> map;

            for (int i = 0; i < mData.length; i++) {


                map = new HashMap<>();

                String mData_vacine[][] = ls.executeReader("Select name from VACCINES where uid = '" + mData[i][1] + "'");

                File file = new File(mData[i][2]);
                if (file.exists()) {

                    tv_record.setVisibility(View.GONE);
                    iv_sync.setVisibility(View.VISIBLE);

                    Log.d("000666", "File Exit");

                    map.put("member_uid", "" + mData[i][0]);
                    map.put("vaccine_id", "" + mData[i][1]);
                    map.put("image_location", "" + mData[i][2]);
                    map.put("added_on", "" + mData[i][3]);

                    for (int j = 0; j < mData_vacine.length; j++) {

                        Log.d("000666", "Vaccine NAme: " + mData_vacine[j][0]);
                        map.put("vaccine_name", "" + mData_vacine[j][0]);
                    }

                    Log.d("000666", "member_uid: " + mData[i][0]);
                    Log.d("000666", "vaccine_id: " + mData[i][1]);
                    Log.d("000666", "image_location: " + mData[i][2]);

                    arrayList.add(map);


                } else {
                    Log.d("000666", "Not File Exit");
                    continue;
                }
            }

            adt = new Adt_SyncImages(ctx, arrayList);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);

        } catch (Exception e) {

            Log.d("000666", " Error" + e.getMessage());
        }

    }*/


    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        startActivity(new Intent(ctx, HomePageVacinator_Activity.class));
        finish();
        temp = "0";
        var="0";
    }
}
