package com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHaamlaRecordActivities.MotherZichgiActivities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.Adt_MotherDashboard.Adt_HaamlaDashboard.Adt_MotherZichgiRecordList;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHaamlaRecordActivities.Mother_HaamlaDashboard_Activity;
import com.akdndhrc.covid_module.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Mother_ZichgiList_Activity extends AppCompatActivity {

    Context ctx = Mother_ZichgiList_Activity.this;

    TextView txt_mother_age, txt_mother_name;
    ListView lv;
    Button btn_naya_form_shamil_kre, btn_zichgi_ki_mansobabandi, btn_bachey_ki_zichgi;


    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    Adt_MotherZichgiRecordList adt;

    ImageView iv_navigation_drawer, iv_home;
    String mother_uid;
    String[][] mData;
    String preg_id, mother_name, mother_age;
    Switch swfistula;
    JSONObject jsonObject;
    Dialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_zichgi_list);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Mother_ZichgiList_Activity.class));

        mother_uid = getIntent().getExtras().getString("u_id");
        preg_id = getIntent().getExtras().getString("preg_id");
        mother_age = getIntent().getExtras().getString("mother_age");
        mother_name = getIntent().getExtras().getString("mother_name");

        //ListView
        lv = findViewById(R.id.lv);

        //Switch
        swfistula = findViewById(R.id.swfistula);

        //TextView
        txt_mother_name = findViewById(R.id.txt_mother_name);
        txt_mother_age = findViewById(R.id.txt_mother_age);

        txt_mother_name.setText(mother_name);
        txt_mother_age.setText(mother_age);

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);
        //Button
        btn_naya_form_shamil_kre = findViewById(R.id.btn_naya_form_shamil_kre);


        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, R.string.navigation, Toast.LENGTH_SHORT).show();
            }
        });

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(ctx, Mother_HaamlaDashboard_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
            }
        });

        btn_naya_form_shamil_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog_ZichgiButton();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mData[position][2].equalsIgnoreCase("0")) {
                    Intent newIntent = new Intent(ctx, Mother_ZichgiKiMansobabandiFormView_Activity.class);
                    newIntent.putExtra("u_id", mother_uid);
                    newIntent.putExtra("record_date", mData[position][0]);
                    newIntent.putExtra("added_on", mData[position][1]);
                    newIntent.putExtra("preg_id", preg_id);
                    startActivity(newIntent);
                } else if (mData[position][2].equalsIgnoreCase("1")) {
                    Intent newIntent = new Intent(ctx, Mother_BacheyKiZichgiFormView_Activity.class);
                    newIntent.putExtra("u_id", mother_uid);
                    newIntent.putExtra("record_date", mData[position][0]);
                    newIntent.putExtra("added_on", mData[position][1]);
                    newIntent.putExtra("preg_id", preg_id);
                    startActivity(newIntent);
                }
            }
        });


        swfistula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                swfistula.setChecked(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    swfistula.setThumbTintList(ctx.getResources().getColorStateList(R.color.switch_green_color));
                    swfistula.setTrackTintList(ctx.getResources().getColorStateList(R.color.green_color));
                }
                update_pregnancy_data();
            }
        });


    }


    public void Dialog_ZichgiButton() {


        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.dialog_mother_zichgi_button_layout, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        ImageView iv_close = (ImageView) promptView.findViewById(R.id.iv_close);
        btn_zichgi_ki_mansobabandi = (Button) promptView.findViewById(R.id.btn_zichgi_ki_mansobabandi);
        btn_bachey_ki_zichgi = (Button) promptView.findViewById(R.id.btn_bachey_ki_zichgi);


        alertDialog.setView(promptView);

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_zichgi_ki_mansobabandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, Mother_ZichgiKiMansobabandiForm_Activity.class);
                intent.putExtra("u_id", mother_uid);
                intent.putExtra("preg_id", preg_id);
                startActivity(intent);

                alertDialog.dismiss();

            }
        });

        btn_bachey_ki_zichgi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, Mother_BacheyKiZichgiForm_Activity.class);
                intent.putExtra("u_id", mother_uid);
                intent.putExtra("preg_id", preg_id);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });

    }


    private void update_pregnancy_data() {

        alertDialog = new Dialog(ctx);
        LayoutInflater layout = LayoutInflater.from(ctx);
        final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

        alertDialog.setContentView(dialogView);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
        try {

            final String updated_added_on = String.valueOf(System.currentTimeMillis());

            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            String TodayDate = dates.format(c.getTime());


            if (jsonObject.has("lat")) {
                jsonObject.put("fistula", "" + "On");
                jsonObject.put("fistula_status", "" + "1");
                jsonObject.put("updated_record_date", "" + TodayDate);
                jsonObject.put("added_on", "" + updated_added_on);
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();
                        //   ls.createAndOpenDB(String.valueOf(R.string.database_password));

                        String update_record = "UPDATE MPREGNANCY SET " +
                                "metadata='" + jsonObject.toString() + "'," +
                                "is_synced='" + 0 + "'" +
                                "WHERE member_uid = '" + mother_uid + "' AND pregnancy_id='" + preg_id + "'";
                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d("000333", "Data: " + update_record);
                        Log.d("000333", "Query: " + res.toString());


                    } catch (Exception e) {
                        alertDialog.dismiss();
                        Log.d("000333", " Error" + e.getMessage());
                        // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    } finally {
                        alertDialog.dismiss();
                        Intent intent = new Intent(ctx, Mother_ZichgiList_Activity.class);
                        intent.putExtra("u_id", mother_uid);
                        intent.putExtra("preg_id", preg_id);
                        intent.putExtra("mother_name", mother_name);
                        intent.putExtra("mother_age", mother_age);
                        startActivity(intent);
                    }
                }
            }, 2000);

        } catch (Exception e) {
            alertDialog.dismiss();
            //  Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
            Log.d("000333", " Error" + e.getMessage());
        }


    }


    @Override
    protected void onResume() {
        super.onResume();


        try {

            hashMapArrayList.clear();
            Lister ls = new Lister(Mother_ZichgiList_Activity.this);
            ls.createAndOpenDB();

            /*SharedPreferences settings = getSharedPreferences("Khandanuuid", MODE_PRIVATE);
            // Reading from SharedPreferences
            String khandan_uuid_sp = settings.getString("k_uuid", "");
            // Toast.makeText(getApplicationContext(),khandan_uuid_sp,Toast.LENGTH_SHORT).show();*/

            try {
                //String[][] data = ls.executeReader("Select* from KHANDAN "); pregnancy_id,
                mData = ls.executeReader("Select record_data,added_on,type from MDELIV where member_uid = '" + mother_uid + "' AND pregnancy_id= '" + preg_id + "' ORDER BY added_on DESC");
                Log.d("mother_data", String.valueOf(mData.length));
            } catch (Exception e) {
                Log.d("mother_data", String.valueOf(e.getMessage()));
            }

            HashMap<String, String> map;
            for (int i = 0; i < mData.length; i++) {

                map = new HashMap<>();
                map.put("i", "" + ":" + String.valueOf(i + 1));
                map.put("zichgi_ki_mansoobahbandi_txt", "" + mData[i][0]);
                //  map.put("bemaari_record_date", "" +mData[i][1]);
                //  map.put("mother_name", "" +"کرن اقبال");

                hashMapArrayList.add(map);
            }
            adt = new Adt_MotherZichgiRecordList(ctx, hashMapArrayList);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);


        } catch (Exception e) {
            Log.d("12345", "Error: " + e.getMessage());
            Toast.makeText(ctx, R.string.noRecord, Toast.LENGTH_SHORT).show();
        }


        // Check Pregnancy Fistula

        try {

            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            String[][] mData = ls.executeReader("Select metadata from MPREGNANCY where member_uid = '" + mother_uid + "' AND pregnancy_id ='" + preg_id + "' AND is_synced NOT IN ('-1') ORDER BY added_on DESC");

            Log.d("000333", "Preg Data: " + mData[0][0]);

            jsonObject = new JSONObject(mData[0][0]);

            if (jsonObject.has("fistula") || jsonObject.has("fistula_status")) {
                if (jsonObject.getString("fistula_status").equalsIgnoreCase("1")) {
                    Log.d("000333", "111111111 ");
                    swfistula.setEnabled(false);
                    swfistula.setChecked(true);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        swfistula.setThumbTintList(ctx.getResources().getColorStateList(R.color.switch_green_color));
                        swfistula.setTrackTintList(ctx.getResources().getColorStateList(R.color.green_color));
                    }
                } else {
                    Log.d("000333", "22222222 ");
                    swfistula.setChecked(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        swfistula.setThumbTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                        swfistula.setTrackTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                    }

                }

            } else {
                Log.d("000333", "33333 ");

                swfistula.setChecked(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    swfistula.setThumbTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                    swfistula.setTrackTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                }
            }

        } catch (Exception e) {
            Log.d("000333", "Preg Fistula Error: " + e.getMessage());
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }
}
