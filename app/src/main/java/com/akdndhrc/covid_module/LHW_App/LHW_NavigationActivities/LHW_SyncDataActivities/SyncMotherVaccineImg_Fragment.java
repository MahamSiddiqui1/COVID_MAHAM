package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SyncDataActivities;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.Adt_SyncImages.Adt_SyncImages;
import com.akdndhrc.covid_module.CustomClass.NetworkClientMother;
import com.akdndhrc.covid_module.CustomClass.UploadAPIsMother;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.R;

import org.json.JSONObject;

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


public class SyncMotherVaccineImg_Fragment extends Fragment {

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
    String var_asyn = "0";

    public SyncMotherVaccineImg_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.activity_sync_images, container, false);


        var_asyn = "0";

        //Get shared USer name
        try {
            SharedPreferences prefelse = getActivity().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
            String shared_useruid = prefelse.getString(("login_userid"), null); // getting String
            login_useruid = shared_useruid;

            Log.d("000666", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000666", "Shared Err:" + e.getMessage());
        }


        //ListView
        lv = rootView.findViewById(R.id.lv);

        //TextView
        tv_record = rootView.findViewById(R.id.tv_record);
//        TextView tv = rootView.findViewById(R.id.tv);
//      //  tv.setText("سینک تصاویر");

        //ImageView
        iv_sync = rootView.findViewById(R.id.iv_sync);
        iv_sync.setVisibility(View.GONE);

        ll_pbProgress = rootView.findViewById(R.id.ll_pbProgress);
        ll_pbProgress.setVisibility(View.VISIBLE);


        adt = new Adt_SyncImages(getActivity(), arrayList);
        lv.setAdapter(adt);


        final Handler handler = new Handler();

        //your code
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                read_data();
            }
        }, 1200);

        iv_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                if (isConnectingToInternet()) {

                    if (temp.equalsIgnoreCase("0")) {
                        alertDialog = new Dialog(getActivity());
                        LayoutInflater layout = LayoutInflater.from(getActivity());
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
                                    getActivity().onBackPressed();
                                    temp = "0";
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

                    // Toast.makeText(getActivity(), "برائے مہربانی انٹرنیٹ کنکشن چیک کریں۔", Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar.make(v, R.string.checkInternetPrompt, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(16);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error_outline_black_24dp, 0, 0, 0);
                    snackbar.setDuration(3000);
                    snackbar.show();
                    return;
                }


            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                if (isConnectingToInternet()) {

                    try {
                        alertDialog = new Dialog(getActivity());
                        LayoutInflater layout = LayoutInflater.from(getActivity());
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
                        Log.d("000666", "Catach Err: " + e.getMessage());
                    }

                } else {
                    // Toast.makeText(getActivity(), "برائے مہربانی انٹرنیٹ کنکشن چیک کریں۔", Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar.make(view, R.string.checkInternetPrompt, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(16);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error_outline_black_24dp, 0, 0, 0);
                    snackbar.setDuration(3000);
                    snackbar.show();
                    return;
                }
            }
        });


        return rootView;
    }

    private void uploadToServer(String filePath) throws IOException {

        try {

            final Retrofit retrofit = NetworkClientMother.getRetrofitClient(getActivity());
            UploadAPIsMother uploadAPIs = retrofit.create(UploadAPIsMother.class);

            //Create a file object using file path
            File file = new File(filePath);
            Log.d("000666", "FILE: " + file);
            // Create a request body with file and image media type

           /* RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/png"), file);
            RequestBody member_id = RequestBody.create(null, arrayList.get(Integer.parseInt(pos_value)).get("member_uid"));
            RequestBody vaccine_id = RequestBody.create(null, arrayList.get(Integer.parseInt(pos_value)).get("vaccine_id"));
            RequestBody added_by = RequestBody.create(null, login_useruid);
            RequestBody added_on = RequestBody.create(null, arrayList.get(Integer.parseInt(pos_value)).get("added_on"));*/

            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/png"), file);
          RequestBody member_id = RequestBody.create(null, arrayList.get(Integer.parseInt(pos_value)).get("member_uid"));
            RequestBody vaccine_id = RequestBody.create(null, arrayList.get(Integer.parseInt(pos_value)).get("vaccine_id"));
            RequestBody added_by = RequestBody.create(null, login_useruid);
            RequestBody added_on = RequestBody.create(null, arrayList.get(Integer.parseInt(pos_value)).get("added_on"));

            /*String member_id = arrayList.get(Integer.parseInt(pos_value)).get("member_uid");
            String vaccine_id = arrayList.get(Integer.parseInt(pos_value)).get("vaccine_id");
            String added_by =  login_useruid;
            String added_on = arrayList.get(Integer.parseInt(pos_value)).get("added_on");*/

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


                            Lister ls = new Lister(getActivity());
                            ls.createAndOpenDB();
                            String update_record = "UPDATE MVACINE SET " +
                                    "is_synced='" + String.valueOf(2) + "' " +
                                    "WHERE member_uid = '" + arrayList.get(Integer.parseInt(pos_value)).get("member_uid") + "' AND vaccine_id= '" + arrayList.get(Integer.parseInt(pos_value)).get("vaccine_id") + "'AND added_on= '" + arrayList.get(Integer.parseInt(pos_value)).get("added_on") + "'";

                            ls.executeNonQuery(update_record);

                            final Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.fragment_sync_image), R.string.picSynced, Snackbar.LENGTH_SHORT);
                            View mySbView = snackbar.getView();
                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(16);
                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                textView.setCompoundDrawableTintList(getContext().getResources().getColorStateList(R.color.green_color));
                            }
                            snackbar.setDuration(3000);
                            snackbar.show();

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

                                        getActivity().finish();
                                    }
                                }, 1500);

                            }

                        } else {
                            alertDialog.dismiss();
                            Log.d("000666", "else ");
                            Toast.makeText(getActivity(), R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        alertDialog.dismiss();
                        Log.d("000666", "catch: " + e.getMessage());
                        //Toast.makeText(getActivity(), "تصاویر سینک نہیں ہوئی", Toast.LENGTH_SHORT).show();

                        final Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.fragment_sync_image), R.string.picNotSynced, Snackbar.LENGTH_SHORT);
                        View mySbView = snackbar.getView();
                        mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
                        TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(16);
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            textView.setCompoundDrawableTintList(getContext().getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                        }
                        snackbar.setDuration(3000);
                        snackbar.show();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    alertDialog.dismiss();
                    Log.d("000666", "Failed: " + t.getMessage());
                    //Toast.makeText(getActivity(), "تصاویر سینک نہیں ہوئی", Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.fragment_sync_image), R.string.picNotSynced, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(16);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        textView.setCompoundDrawableTintList(getContext().getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                    }
                    snackbar.setDuration(3000);
                    snackbar.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().finish();

                        }
                    },2000);
                }
            });

        } catch (Exception e) {
            alertDialog.dismiss();
            Log.d("000666", "catch: " + e.getMessage());
        }

    }

    private void uploadToServer_Overall(String filePath) throws IOException {

        try {

            final Retrofit retrofit = NetworkClientMother.getRetrofitClient(getActivity());
            UploadAPIsMother uploadAPIs = retrofit.create(UploadAPIsMother.class);

            //Create a file object using file path
            File file = new File(filePath);
            Log.d("000666", "FILE: " + file);
            // Create a request body with file and image media type

          RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/png"), file);
            RequestBody member_id = RequestBody.create(null, arrayList.get(0).get("member_uid"));
            RequestBody vaccine_id = RequestBody.create(null, arrayList.get(0).get("vaccine_id"));
            RequestBody added_by = RequestBody.create(null, login_useruid);
            RequestBody added_on = RequestBody.create(null, arrayList.get(0).get("added_on"));

       /*     RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/png"), file);
            String member_id = arrayList.get(Integer.parseInt(pos_value)).get("member_uid");
            String vaccine_id = arrayList.get(Integer.parseInt(pos_value)).get("vaccine_id");
            String added_by =  login_useruid;
            String added_on = arrayList.get(Integer.parseInt(pos_value)).get("added_on");*/


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


                            Lister ls = new Lister(getActivity());
                            ls.createAndOpenDB();
                            String update_record = "UPDATE MVACINE SET " +
                                    "is_synced='" + String.valueOf(2) + "' " +
                                    "WHERE member_uid = '" + arrayList.get(0).get("member_uid") + "' AND vaccine_id= '" + arrayList.get(0).get("vaccine_id") + "'AND added_on= '" + arrayList.get(0).get("added_on") + "'";

                            ls.executeNonQuery(update_record);

//                            Toast.makeText(getActivity(), "تصاویر سنک ہوگئی ہے", Toast.LENGTH_SHORT).show();
                            final Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.fragment_sync_image), R.string.picSynced, Snackbar.LENGTH_SHORT);
                            View mySbView = snackbar.getView();
                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
                            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(16);
                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                textView.setCompoundDrawableTintList(getContext().getResources().getColorStateList(R.color.green_color));
                            }
                            snackbar.setDuration(3000);
                            snackbar.show();


                            arrayList.remove(0);
                            adt.notifyDataSetChanged();

                            reload();
                        } else {
                            alertDialog.dismiss();
                            Log.d("000666", "else ");
                            Toast.makeText(getActivity(), R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        alertDialog.dismiss();
                        Log.d("000666", "catch: " + e.getMessage());
                        //Toast.makeText(getActivity(), "تصاویر سینک نہیں ہوئی", Toast.LENGTH_SHORT).show();
                        final Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.fragment_sync_image), R.string.picNotSynced, Snackbar.LENGTH_SHORT);
                        View mySbView = snackbar.getView();
                        mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
                        TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(16);
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            textView.setCompoundDrawableTintList(getContext().getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                        }
                        snackbar.setDuration(3000);
                        snackbar.show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Log.d("000666", "Failed: " + t.getMessage());
                    alertDialog.dismiss();

                    //Toast.makeText(getActivity(), "تصاویر سینک نہیں ہوئی", Toast.LENGTH_SHORT).show();

                    final Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.fragment_sync_image), R.string.picNotSynced, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(getContext().getResources().getColor(android.R.color.black));
                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(16);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        textView.setCompoundDrawableTintList(getContext().getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                    }
                    snackbar.setDuration(3000);
                    snackbar.show();


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                              temp = "0";
                            getActivity().finish();

                        }
                    },2000);

                }
            });

        } catch (Exception e) {
            alertDialog.dismiss();
            Log.d("000666", "catch: " + e.getMessage());
        }

    }

    private void read_data() {

        arrayList.clear();

        try {
            Lister ls = new Lister(getActivity());
            ls.createAndOpenDB();

            mData = ls.executeReader("Select member_uid, vaccine_id, image_location, added_on,data from MVACINE where is_synced = '1' and type='0'");

            HashMap<String, String> map;

            for (int i = 0; i < mData.length; i++) {

                map = new HashMap<>();

                //  String mData_vacine[][] = ls.executeReader("Select name from VACCINES where uid = '" + mData[i][1] + "'");

                File file = new File(mData[i][2]);
                if (file.exists()) {

                    ll_pbProgress.setVisibility(View.GONE);
                    tv_record.setVisibility(View.GONE);
                    iv_sync.setVisibility(View.VISIBLE);

                    Log.d("000666", "File Exit");

                    Log.d("000666", "member_uid: " + mData[i][0]);
                    Log.d("000666", "vaccine_id: " + mData[i][1]);
                    Log.d("000666", "image_location: " + mData[i][2]);

                    map.put("member_uid", "" + mData[i][0]);
                    map.put("vaccine_id", "" + mData[i][1]);
                    map.put("image_location", "" + mData[i][2]);
                    map.put("added_on", "" + mData[i][3]);
                    map.put("data", "" + mData[i][4]);

                    JSONObject jsonObject = new JSONObject(mData[i][4]);

                    Log.d("000666", "Vaccine NAme: " + jsonObject.getString("vacine_name"));
                    map.put("vaccine_name", "" + jsonObject.getString("vacine_name"));

                    arrayList.add(map);

                } else {
                    Log.d("000666", "Not File Exit");
                    continue;
                }

            }
            if (var_asyn.equalsIgnoreCase("0")) {
                ll_pbProgress.setVisibility(View.GONE);
                tv_record.setVisibility(View.VISIBLE);
                adt.notifyDataSetChanged();
            } else {
                adt.notifyDataSetChanged();
            }

        } catch (Exception e) {
            Log.d("000777", " CatchError: " + e.getMessage());
            ll_pbProgress.setVisibility(View.GONE);
            tv_record.setVisibility(View.VISIBLE);
            iv_sync.setVisibility(View.GONE);
        }


    }


    public void reload() {

        if (arrayList.size() > 0) {
            temp = "1";
            iv_sync.performClick();
        } else {
            temp = "0";
            alertDialog.dismiss();
            //startActivity(new Intent(ctx, HomePage_Activity.class));

            tv_record.setVisibility(View.VISIBLE);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    getActivity().finish();
                }
            }, 1500);
        }
    }

    class Task extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            Log.d("000666", "ON PREEEEE: ");
            ll_pbProgress.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Log.d("000666", "ON BACkGROUND: ");
                read_data();
                Thread.sleep(1500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d("000666", "ON EXECUTE: ");
            ll_pbProgress.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            adt.notifyDataSetChanged();
            super.onPostExecute(result);
        }

    }

    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

  /*  @Override
    public void onResume() {
        super.onResume();

        arrayList.clear();

        try {
            Lister ls = new Lister(getActivity());
            ls.createAndOpenDB();


            mData = ls.executeReader("Select member_uid, vaccine_id, image_location, added_on,data from MVACINE where is_synced = '" + 1 + "' and type='0'");

            HashMap<String, String> map;

            for (int i = 0; i < mData.length; i++) {


                map = new HashMap<>();

              //  String mData_vacine[][] = ls.executeReader("Select name from VACCINES where uid = '" + mData[i][1] + "'");

                File file = new File(mData[i][2]);
                if (file.exists()) {

                    tv_record.setVisibility(View.GONE);
                    iv_sync.setVisibility(View.VISIBLE);

                    Log.d("000666", "File Exit");

                    Log.d("000666", "member_uid: " + mData[i][0]);
                    Log.d("000666", "vaccine_id: " + mData[i][1]);
                    Log.d("000666", "image_location: " + mData[i][2]);

                    map.put("member_uid", "" + mData[i][0]);
                    map.put("vaccine_id", "" + mData[i][1]);
                    map.put("image_location", "" + mData[i][2]);
                    map.put("added_on", "" + mData[i][3]);
                    map.put("data", "" + mData[i][4]);

                    JSONObject jsonObject = new JSONObject(mData[i][4]);

                    Log.d("000666", "Vaccine NAme: " + jsonObject.getString("vacine_name"));
                    map.put("vaccine_name", "" + jsonObject.getString("vacine_name"));



                    arrayList.add(map);

                } else {
                    Log.d("000666", "Not File Exit");
                    continue;
                }

            }
            adt = new Adt_SyncImages(getActivity(), arrayList);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);

        } catch (Exception e) {
            Log.d("000666", " Error" + e.getMessage());
        }
    }*/

}