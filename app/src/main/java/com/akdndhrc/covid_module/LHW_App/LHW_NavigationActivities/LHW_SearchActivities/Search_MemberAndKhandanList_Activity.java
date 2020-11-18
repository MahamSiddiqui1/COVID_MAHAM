package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SearchActivities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.Child_Dashboard_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MaleDashboardActivities.AboveTwoMaleProfile_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MaleDashboardActivities.MaleBemaariRecordActivities.Male_BemaariRecordList_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MaleDashboardActivities.MaleReferralActivities.Male_RefferalRecordList_Activity;
import com.akdndhrc.covid_module.Adapter.Adt_Search.Adt_SearchFamilyMembersList;
import com.akdndhrc.covid_module.Adapter.Adt_Search.Adt_SearchKhandanList;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.Mother_Dashboard_Activity;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.SwipeDismissListViewTouchListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Search_MemberAndKhandanList_Activity extends AppCompatActivity {

    Context ctx = Search_MemberAndKhandanList_Activity.this;

    ListView lv;
    ImageView iv_arrow_back, iv_home;
    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    Adt_SearchKhandanList adt;
    Adt_SearchFamilyMembersList adt_member;
    String khandan_number, shared_data, qrcode_value, cnic_number, phone_number,login_useruid;
    String[][] mData;
    public static String temp_variable = "0";
    TextView txt_name;

    public Dialog alertProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_khandan_number_list);


        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences("UserLogin", 0); // 0 - for private mode
            String shared_useruid = prefelse.getString("login_userid", null); // getting String
            login_useruid = shared_useruid;

            Log.d("000222", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000222", "Shared Err:" + e.getMessage());
        }

        //ListView
        lv = findViewById(R.id.lv);


        //ImageView
        iv_home = findViewById(R.id.iv_home);
        iv_arrow_back = findViewById(R.id.iv_arrow_back);
        //  iv_home.setVisibility(View.GONE);

        //txt_name
        txt_name = findViewById(R.id.txt_name);


        iv_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(ctx, HomePage_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();


        hashMapArrayList.clear();

        try {

            SharedPreferences settings = getSharedPreferences("shared_preference", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            // Reading from SharedPreferences
            shared_data = settings.getString("type", "");
            qrcode_value = settings.getString("qrcode_value", "");
            cnic_number = settings.getString("nic_number", "");
            phone_number = settings.getString("phone_number", "");
            khandan_number = settings.getString("khandan_number", "");
            editor.apply();

            Lister ls = new Lister(Search_MemberAndKhandanList_Activity.this);
            ls.createAndOpenDB();

            if (shared_data.equalsIgnoreCase("srch_nicnumber")) {
                Log.d("000888", "  IF ");
                // mData = ls.executeReader("Select *from MEMBER where khandan_id = '" + khandan_id + "' AND nicnumber LIKE '"+cnic_number+"%'");

                try {

                    txt_name.setText("افراد کی لسٹ");

                    mData = ls.executeReader("Select uid,full_name,gender,age,added_on from MEMBER where nicnumber LIKE '%" + cnic_number + "%'");
                    Log.d("000888", "LEN: " + String.valueOf(mData.length));
                    HashMap<String, String> map;

                    ArrayList<String> temp_list = new ArrayList<String>();

                    for (int i = 0; i < mData.length; i++) {

                        map = new HashMap<>();

                        Log.d("000888", "uid : " + mData[i][0]);
                        Log.d("000888", "full_name : " + mData[i][1]);
                        Log.d("000888", "gender : " + mData[i][2]);
                        Log.d("000888", "age : " + mData[i][3]);

                       /* if (temp_list.contains(mData[i][0]))
                            continue;
                        temp_list.add("" + mData[i][0]);*/
                        map.put("full_name", "" + mData[i][1]);
                        map.put("gender", "" + mData[i][2]);
                        map.put("age", "" + mData[i][3]);

                        hashMapArrayList.add(map);

                    }

                    adt_member = new Adt_SearchFamilyMembersList(ctx, hashMapArrayList);
                    adt_member.notifyDataSetChanged();
                    lv.setAdapter(adt_member);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                            if (Integer.parseInt(mData[position][3]) < 5) {
                                Intent intent = new Intent(ctx, Child_Dashboard_Activity.class);
                                intent.putExtra("u_id", mData[position][0]);
                                intent.putExtra("child_name", mData[position][1]);
                                intent.putExtra("child_gender", mData[position][2]);
                                startActivity(intent);

                                temp_variable = "child_dashboard";

                                Log.d("000330", "1");
                            } else if (Integer.parseInt(mData[position][3]) >= 5 && Integer.parseInt(mData[position][3]) <= 14) {
                                Male_BottomDialog(position);
                                Log.d("000330", "2");
                            } else if (Integer.parseInt(mData[position][3]) > 14) {
                                if (mData[position][2].equalsIgnoreCase("0")) {
                                    Intent intent = new Intent(ctx, Mother_Dashboard_Activity.class);
                                    intent.putExtra("u_id", mData[position][0]);
                                    intent.putExtra("mother_name", mData[position][1]);
                                    startActivity(intent);
                                    temp_variable = "mother_dashboard";
                                    Log.d("000330", "3");
                                } else if (mData[position][2].equalsIgnoreCase("1")) {
                                    Male_BottomDialog(position);
                                    Log.d("000330", "4");
                                } else {
                                    Log.d("000330", "5");
                                    // Toast.makeText(ctx, "sd", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ctx, "something wrong", Toast.LENGTH_SHORT).show();
                            }


                           /* if (Integer.parseInt(mData[position][2]) == 1) {
                                if (Integer.parseInt(mData[position][3]) <= 2) {
                                    Intent intent = new Intent(ctx, Child_Dashboard_Activity.class);
                                    intent.putExtra("u_id", mData[position][0]);
                                    intent.putExtra("child_gender", mData[position][2]);
                                    startActivity(intent);

                                    temp_variable = "child_dashboard";
                                } else {
                                    Toast.makeText(ctx, "No Feature defined", Toast.LENGTH_SHORT).show();
                                }
                            } else if (Integer.parseInt(mData[position][2]) == 0) {
                                if (Integer.parseInt(mData[position][3]) <= 2) {
                                    Intent intent = new Intent(ctx, Child_Dashboard_Activity.class);
                                    intent.putExtra("u_id", mData[position][0]);
                                    intent.putExtra("child_gender", mData[position][2]);
                                    startActivity(intent);
                                    temp_variable = "child_dashboard";

                                } else {
                                    Intent intent = new Intent(ctx, Mother_Dashboard_Activity.class);
                                    intent.putExtra("u_id", mData[position][0]);
                                    startActivity(intent);
                                    temp_variable = "mother_dashboard";

                                }
                            } else {
                                //  Toast.makeText(ctx, "Position: " +position, Toast.LENGTH_SHORT).show();
                            }*/
                        }
                    });


                } catch (Exception e) {
                    Log.d("000888", " Err Search With NIC-Number: " + e.getMessage());
                    txt_name.setVisibility(View.GONE);
                    Toast.makeText(ctx, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            } else if (shared_data.equalsIgnoreCase("srch_phonenumber")) {
                Log.d("000888", "  Else IF 1: ");
                // mData = ls.executeReader("Select *from MEMBER where khandan_id = '" + khandan_id + "' AND nicnumber LIKE '"+cnic_number+"%'");

                try {
                    txt_name.setText("افراد کی لسٹ");

                    mData = ls.executeReader("Select uid,full_name,gender,age,added_on from MEMBER where phone_number LIKE '%" + phone_number + "%'");
                    Log.d("000888", "LEN: " + String.valueOf(mData.length));
                    HashMap<String, String> map;

                    ArrayList<String> temp_list = new ArrayList<String>();

                    for (int i = 0; i < mData.length; i++) {

                        map = new HashMap<>();

                        Log.d("000888", "uid : " + mData[i][0]);
                        Log.d("000888", "full_name : " + mData[i][1]);
                        Log.d("000888", "gender : " + mData[i][2]);
                        Log.d("000888", "age : " + mData[i][3]);

                       /* if (temp_list.contains(mData[i][0]))
                            continue;
                        temp_list.add("" + mData[i][0]);*/
                        map.put("full_name", "" + mData[i][1]);
                        map.put("gender", "" + mData[i][2]);
                        map.put("age", "" + mData[i][3]);

                        hashMapArrayList.add(map);

                    }

                    adt_member = new Adt_SearchFamilyMembersList(ctx, hashMapArrayList);
                    adt_member.notifyDataSetChanged();
                    lv.setAdapter(adt_member);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (Integer.parseInt(mData[position][3]) < 5) {
                                Intent intent = new Intent(ctx, Child_Dashboard_Activity.class);
                                intent.putExtra("u_id", mData[position][0]);
                                intent.putExtra("child_name", mData[position][1]);
                                intent.putExtra("child_gender", mData[position][2]);
                                startActivity(intent);

                                temp_variable = "child_dashboard";

                                Log.d("000330", "1");
                            } else if (Integer.parseInt(mData[position][3]) >= 5 && Integer.parseInt(mData[position][3]) <= 14) {
                                Male_BottomDialog(position);
                                Log.d("000330", "2");
                            } else if (Integer.parseInt(mData[position][3]) > 14) {
                                if (mData[position][2].equalsIgnoreCase("0")) {
                                    Intent intent = new Intent(ctx, Mother_Dashboard_Activity.class);
                                    intent.putExtra("u_id", mData[position][0]);
                                    intent.putExtra("mother_name", mData[position][1]);
                                    startActivity(intent);
                                    temp_variable = "mother_dashboard";
                                    Log.d("000330", "3");
                                } else if (mData[position][2].equalsIgnoreCase("1")) {
                                    Male_BottomDialog(position);
                                    Log.d("000330", "4");
                                } else {
                                    Log.d("000330", "5");
                                    // Toast.makeText(ctx, "sd", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ctx, "something wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (Exception e) {
                    txt_name.setVisibility(View.GONE);
                    Toast.makeText(ctx, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("000888", " Error Search With PhoneNumber: " + e.getMessage());
                }
            } else if (shared_data.equalsIgnoreCase("srch_khandan_number")) {
                Log.d("000888", "  Else IF 2: ");
                // mData = ls.executeReader("Select *from MEMBER where khandan_id = '" + khandan_id + "' AND nicnumber LIKE '"+cnic_number+"%'");

                try {

                    txt_name.setText("خاندان کی لسٹ");

                    mData = ls.executeReader("Select family_head_name, uid , manual_id , village_id from KHANDAN where manual_id LIKE '%" + khandan_number + "%'");
                    Log.d("000888", "LEN: " + String.valueOf(mData.length));
                    HashMap<String, String> temp_map;

                    ArrayList<String> temp_list = new ArrayList<String>();
                    for (int i = 0; i < mData.length; i++) {

                        temp_map = new HashMap<>();

                        Log.d("000888", "NAME : " + mData[i][0]);
                        Log.d("000888", "UID : " + mData[i][1]);
                        Log.d("000888", "MANUAL ID : " + mData[i][2]);

                       /* if (temp_list.contains(mData[i][0]))
                            continue;
                        temp_list.add("" + mData[i][0]);*/
                        temp_map.put("family_head_name", "" + mData[i][0]);
                        temp_map.put("uid", "" + mData[i][1]);
                        temp_map.put("manual_id", "" + mData[i][2]);


                        String[][] data = ls.executeReader("Select name from VILLAGES where uid = '" + mData[i][3] + "'");
                        if (data != null)
                        {

                        for (int a = 0; a < data.length; a++) {
                            Log.d("000888", "IF Village : ");
                            Log.d("000888", "NAME : " + data[a][0]);

                            temp_map.put("village_name", "" + data[a][0]);
                        }
                        }else {
                            Log.d("000888", "ELSEE Village : ");
                            temp_map.put("village_name", "" +"no village");
                        }
                        hashMapArrayList.add(temp_map);
                    }

                    adt = new Adt_SearchKhandanList(ctx, hashMapArrayList);
                    adt.notifyDataSetChanged();
                    lv.setAdapter(adt);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent(ctx, Search_FamilyMembersList.class);
                            intent.putExtra("khandan_id", hashMapArrayList.get(position).get("uid"));
                            intent.putExtra("manual_id", hashMapArrayList.get(position).get("manual_id"));
                            startActivity(intent);

                         /*   try {
                                Lister ls = new Lister(ctx);
                               ls.createAndOpenDB();

                                String[][] a = ls.executeReader("Select *from MEMBER where khandan_id = '" + hashMapArrayList.get(position).get("uid") + "'");

                                Log.d("000888", "" + a.length);

                                Intent intent = new Intent(ctx, Search_FamilyMembersList.class);
                                intent.putExtra("khandan_id", hashMapArrayList.get(position).get("uid"));
                                intent.putExtra("manual_id", hashMapArrayList.get(position).get("manual_id"));
                                startActivity(intent);


                            } catch (Exception e) {
                                Log.d("000888", "Err: " + e.getMessage());
                                Toast.makeText(ctx, "اس خاندان کا کوئی رکن رجسٹر نہیں", Toast.LENGTH_SHORT).show();
                            }*/
                        }
                    });


                    //Swipe ListView
                    SwipeDismissListViewTouchListener touchListener =
                            new SwipeDismissListViewTouchListener(
                                    lv,
                                    new SwipeDismissListViewTouchListener.DismissCallbacks() {
                                        @Override
                                        public boolean canDismiss(int position) {
                                            return true;
                                        }

                                        @Override
                                        public void onDismiss(final ListView listView, int[] reverseSortedPositions) {
                                            for (final int position : reverseSortedPositions) {

                                                try {

                                                    Lister ls = new Lister(ctx);
                                                    ls.createAndOpenDB();

                                                    final String[][] mData_khandan = ls.executeReader("Select t0.manual_id,t0.village_id,t1.name from KHANDAN t0" +
                                                            " INNER JOIN VILLAGES t1 ON t0.village_id = t1.uid" +
                                                            " where t0.uid = '" + hashMapArrayList.get(position).get("uid") + "'");

                                                    if (mData_khandan != null) {

                                                        Log.d("000777", "work");
                                                        Log.d("000777", "KHANDAN MANUAL ID: " + mData_khandan[0][0]);
                                                        Log.d("000777", "KHANDAN VILLAGE NAME: " + mData_khandan[0][2]);


                                                        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(ctx, R.style.AppBottomSheetDialogTheme);
                                                        final View sheetView = getLayoutInflater().inflate(R.layout.dialog_delete_khandan_bottom_sheet_layout, null);
                                                        mBottomSheetDialog.setContentView(sheetView);
                                                        mBottomSheetDialog.setCanceledOnTouchOutside(false);
                                                        mBottomSheetDialog.setCancelable(false);
                                                        mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                                        mBottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_SlideUp; //style id
                                                        mBottomSheetDialog.show();

                                                        ImageView iv_close_dilaog = (ImageView) sheetView.findViewById(R.id.iv_close_dilaog);


                                                        //TextView
                                                        TextView tvKhandanNumber = sheetView.findViewById(R.id.tvKhandanNumber);
                                                        TextView tvGaonName = sheetView.findViewById(R.id.tvGaonName);


                                                        //Button
                                                        Button btnNo = (Button) sheetView.findViewById(R.id.btnNo);
                                                        Button btnYes = (Button) sheetView.findViewById(R.id.btnYes);

                                                        tvKhandanNumber.setText(hashMapArrayList.get(position).get("manual_id"));
                                                        tvGaonName.setText(mData_khandan[0][2]);

                                                        iv_close_dilaog.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                mBottomSheetDialog.dismiss();
                                                            }
                                                        });



                                                        btnNo.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                mBottomSheetDialog.dismiss();
                                                            }
                                                        });


                                                        btnYes.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                mBottomSheetDialog.dismiss();

                                                                alertProgressDialog = new Dialog(ctx);
                                                                LayoutInflater layout = LayoutInflater.from(ctx);
                                                                final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

                                                                alertProgressDialog.setContentView(dialogView);
                                                                alertProgressDialog.setCanceledOnTouchOutside(false);
                                                                alertProgressDialog.setCancelable(false);
                                                                alertProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                                                alertProgressDialog.show();


                                                                new Handler().postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {

                                                                        try {
                                                                            Lister ls = new Lister(ctx);
                                                                            ls.createAndOpenDB();

                                                                            String insert_data = "INSERT INTO DELETE_MEMBER SELECT * FROM MEMBER where khandan_id = '" + hashMapArrayList.get(position).get("uid") + "'";

                                                                            Boolean res = ls.executeNonQuery(insert_data);
                                                                            Log.d("000777", "INSERT DELETE_MEMBER Data: " + insert_data);
                                                                            Log.d("000777", "INSERT DELETE_MEMBER Query: " + res.toString());


                                                                            if (res.toString().equalsIgnoreCase("true")) {
                                                                                Log.d("000777", "TRUE INSERT INTO DELETE MEMBER !!!!!!");

                                                                                String update_record = "UPDATE DELETE_MEMBER SET " +
                                                                                        "added_by='" + login_useruid + "'," +
                                                                                        "added_on='" + System.currentTimeMillis() + "'," +
                                                                                        "is_synced='" + 0 + "'" +
                                                                                        "WHERE khandan_id = '" + hashMapArrayList.get(position).get("uid") + "'";

                                                                                ls.executeNonQuery(update_record);

                                                                                Boolean res12 = ls.executeNonQuery(update_record);
                                                                                Log.d("000777", "DELETE_MEMBER UPDATED Data: " + update_record);
                                                                                Log.d("000777", "DELETE_MEMBER UPDATED Query: " + res12.toString());

                                                                                if (res12.toString().equalsIgnoreCase("true"))
                                                                                {
                                                                                    Log.d("000777", "DELETE MEMBER ADDED_ON UPDATED !!!!!!");

                                                                                    String delete_member = "DELETE from MEMBER WHERE khandan_id = '" + hashMapArrayList.get(position).get("uid") + "'";
                                                                                    ls.executeNonQuery(delete_member);

                                                                                    Boolean res_del = ls.executeNonQuery(delete_member);
                                                                                    Log.d("000777", "DELETED MEMBER Data: " + delete_member);
                                                                                    Log.d("000777", "DELETED MEMBER Query: " + res_del.toString());

                                                                                    if (res_del.toString().equalsIgnoreCase("true")) {

                                                                                        Log.d("000777", "TRUE DELETE FROM MEMBER **********");

                                                                                        String insert_data_khandaan = "INSERT INTO DELETE_KHANDAN SELECT * FROM KHANDAN where uid = '" + hashMapArrayList.get(position).get("uid") + "'";

                                                                                        Boolean res_insert_khandan = ls.executeNonQuery(insert_data_khandaan);
                                                                                        Log.d("000777", "INSERT DELETED_KHANDAN Data: " + insert_data_khandaan);
                                                                                        Log.d("000777", "INSERT DELETED_KHANDAN Query: " + res_insert_khandan.toString());

                                                                                        if (res_insert_khandan.toString().equalsIgnoreCase("true")) {

                                                                                            Log.d("000777", "TRUE INSERT INTO DELETE KHANDAN !!!!!!");


                                                                                            String update_records = "UPDATE DELETE_KHANDAN SET " +
                                                                                                    "added_by='" + login_useruid + "'," +
                                                                                                    "added_on='" + System.currentTimeMillis() + "'," +
                                                                                                    "is_synced='" + 0 + "'" +
                                                                                                    "WHERE uid = '" + hashMapArrayList.get(position).get("uid") + "'";

                                                                                            ls.executeNonQuery(update_records);

                                                                                            Boolean res122 = ls.executeNonQuery(update_records);
                                                                                            Log.d("000777", "DELETE_KHANDAN UPDATED Data: " + update_records);
                                                                                            Log.d("000777", "DELETE_KHANDAN UPDATED Query: " + res122.toString());


                                                                                            if (res122.toString().equalsIgnoreCase("true"))
                                                                                            {
                                                                                                String delete_khandan = "DELETE from KHANDAN WHERE uid = '" + hashMapArrayList.get(position).get("uid") + "'";
                                                                                                ls.executeNonQuery(delete_khandan);

                                                                                                Boolean res_del_khandan = ls.executeNonQuery(delete_khandan);
                                                                                                Log.d("000777", "DELETED KHANDAN Data: " + delete_khandan);
                                                                                                Log.d("000777", "DELETED KHANDAN Query: " + res_del_khandan.toString());

                                                                                                if (res_del_khandan.toString().equalsIgnoreCase("true")) {
                                                                                                    Log.d("000777", "TRUE !****************");
                                                                                                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.search_khandan), "خاندان ڈیلیٹ ہوگیا ہے.", Snackbar.LENGTH_SHORT);
                                                                                                    View mySbView = snackbar.getView();
                                                                                                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                                                                                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                                                                                                    TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                                                                                    textView.setTextColor(Color.WHITE);
                                                                                                    textView.setTypeface(Typeface.DEFAULT_BOLD);
                                                                                                    textView.setTextSize(16);
                                                                                                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                                                                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                                                                        textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                                                                                                    }
                                                                                                    snackbar.setDuration(4000);
                                                                                                    snackbar.show();


                                                                                                    new Handler().postDelayed(new Runnable() {
                                                                                                        @Override
                                                                                                        public void run() {

                                                                                                            Log.d("000777", "FINALLL: ");

                                                                                                            Intent intent = Search_MemberAndKhandanList_Activity.this.getIntent();
                                                                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                                                                            Search_MemberAndKhandanList_Activity.this.finish();
                                                                                                            startActivity(intent);

                                                                                                        }
                                                                                                    }, 4000);
                                                                                                } else {
                                                                                                    alertProgressDialog.dismiss();
                                                                                                    mBottomSheetDialog.show();
                                                                                                    Toast.makeText(getApplicationContext(), "Something wrong!!", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            }
                                                                                            else {
                                                                                                Toast.makeText(getApplicationContext(), "Something wrong!!", Toast.LENGTH_SHORT).show();
                                                                                                alertProgressDialog.dismiss();
                                                                                                mBottomSheetDialog.show();
                                                                                            }

                                                                                        } else {
                                                                                            alertProgressDialog.dismiss();
                                                                                            mBottomSheetDialog.show();
                                                                                            Toast.makeText(getApplicationContext(), "Something wrong!!", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    } else {
                                                                                        Toast.makeText(getApplicationContext(), "Something wrong!!", Toast.LENGTH_SHORT).show();
                                                                                        alertProgressDialog.dismiss();
                                                                                        mBottomSheetDialog.show();
                                                                                    }
                                                                                }
                                                                                else {
                                                                                    Toast.makeText(getApplicationContext(), "Something wrong!!", Toast.LENGTH_SHORT).show();
                                                                                    alertProgressDialog.dismiss();
                                                                                    mBottomSheetDialog.show();
                                                                                }


                                                                            } else {
                                                                                Toast.makeText(getApplicationContext(), "Something wrong!!", Toast.LENGTH_SHORT).show();
                                                                                alertProgressDialog.dismiss();
                                                                                mBottomSheetDialog.show();
                                                                            }
                                                                        } catch (Exception e) {
                                                                            alertProgressDialog.dismiss();
                                                                            mBottomSheetDialog.show();
                                                                            Log.d("000888", " Khandan DELETE Error: " + e.getMessage());
                                                                            Toast.makeText(getApplicationContext(), "Something wrong!!", Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    }
                                                                }, 1500);

                                                            }
                                                        });

                                                    } else {
                                                        Log.d("000777", "ELSE NO KHANDAN DATA-------: ");
                                                        Toast.makeText(getApplicationContext(), "Something wrong!!", Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (Exception e) {
                                                    Log.d("000777", "OVERALL Err: " + e.getMessage());
                                                    Toast.makeText(getApplicationContext(), "Something wrong!!", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        }
                                    });

                    lv.setOnTouchListener(touchListener);


                } catch (Exception e) {
                    txt_name.setVisibility(View.GONE);
                    Toast.makeText(ctx, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("000888", " Error Search With KHANDAN: " + e.getMessage());
                }

            } else if (shared_data.equalsIgnoreCase("qrcode")) {
                Log.d("000888", "  Else IF 3: ");
                // mData = ls.executeReader("Select *from MEMBER where khandan_id = '" + khandan_id + "' AND nicnumber LIKE '"+cnic_number+"%'");

                try {
                    txt_name.setText("افراد کی لسٹ");

                    mData = ls.executeReader("Select uid,full_name,gender,age,added_on from MEMBER where  qr_code = '" + qrcode_value + "'");
                    Log.d("000888", "QR Data LEN: " + String.valueOf(mData.length));
                    HashMap<String, String> map;

                    ArrayList<String> temp_list = new ArrayList<String>();
                    for (int i = 0; i < mData.length; i++) {

                        map = new HashMap<>();

                        Log.d("000888", "uid : " + mData[i][0]);
                        Log.d("000888", "full_name : " + mData[i][1]);
                        Log.d("000888", "gender : " + mData[i][2]);
                        Log.d("000888", "age : " + mData[i][3]);

                       /* if (temp_list.contains(mData[i][0]))
                            continue;
                        temp_list.add("" + mData[i][0]);*/
                        map.put("full_name", "" + mData[i][1]);
                        map.put("gender", "" + mData[i][2]);
                        map.put("age", "" + mData[i][3]);

                        hashMapArrayList.add(map);

                    }

                    adt_member = new Adt_SearchFamilyMembersList(ctx, hashMapArrayList);
                    adt_member.notifyDataSetChanged();
                    lv.setAdapter(adt_member);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (Integer.parseInt(mData[position][3]) < 5) {
                                Intent intent = new Intent(ctx, Child_Dashboard_Activity.class);
                                intent.putExtra("u_id", mData[position][0]);
                                intent.putExtra("child_name", mData[position][1]);
                                intent.putExtra("child_gender", mData[position][2]);
                                startActivity(intent);

                                temp_variable = "child_dashboard";

                                Log.d("000330", "1");
                            } else if (Integer.parseInt(mData[position][3]) >= 5 && Integer.parseInt(mData[position][3]) <= 14) {
                                Male_BottomDialog(position);
                                Log.d("000330", "2");
                            } else if (Integer.parseInt(mData[position][3]) > 14) {
                                if (mData[position][2].equalsIgnoreCase("0")) {
                                    Intent intent = new Intent(ctx, Mother_Dashboard_Activity.class);
                                    intent.putExtra("u_id", mData[position][0]);
                                    intent.putExtra("mother_name", mData[position][1]);
                                    startActivity(intent);
                                    temp_variable = "mother_dashboard";
                                    Log.d("000330", "3");
                                } else if (mData[position][2].equalsIgnoreCase("1")) {
                                    Male_BottomDialog(position);
                                    Log.d("000330", "4");
                                } else {
                                    Log.d("000330", "5");
                                    // Toast.makeText(ctx, "sd", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ctx, "something wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (Exception e) {
                    txt_name.setVisibility(View.GONE);
                    Toast.makeText(ctx, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("000888", "Error Search With QR: " + e.getMessage());
                }
            } else {

            }
        } catch (Exception e) {
            Log.d("000888", "Error: " + e.getMessage());
            Toast.makeText(ctx, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


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


        try {
            if (Integer.parseInt(mData[position][3]) >= 5 && Integer.parseInt(mData[position][3]) <= 14) {

                if (mData[position][2].equalsIgnoreCase("0")) {
                    image_profile_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_adult_female_50dp));
                    image_profile_gender.requestLayout();
                    image_profile_gender.getLayoutParams().height = newHeight;
                    image_profile_gender.getLayoutParams().width = newWidth;
                    Log.d("000330", "5 To 14 FEMALE");
                } else {
                    Log.d("000330", "5 To 14 MALE");
                    image_profile_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                }

            } else if (Integer.parseInt(mData[position][3]) > 14) {
                Log.d("000330", ">14 MALE");
                image_profile_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
            }

        } catch (Exception e) {
            Log.d("000330", "Ex: " + e.getMessage());
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
                intent2.putExtra("u_id", mData[position][0]);
                startActivity(intent2);
            }
        });

        rl_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();

                Intent intent = new Intent(ctx, AboveTwoMaleProfile_Activity.class);
                intent.putExtra("u_id", mData[position][0]);
                startActivity(intent);
            }
        });

        rl_referral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();

                Intent intent3 = new Intent(ctx, Male_RefferalRecordList_Activity.class);
                intent3.putExtra("u_id", mData[position][0]);
                intent3.putExtra("child_name", mData[position][1]);
                intent3.putExtra("child_gender", mData[position][2]);
                intent3.putExtra("child_age", mData[position][3]);
                startActivity(intent3);
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(ctx, Search_Activity.class));

      /*  Intent newIntent = new Intent(ctx, HomePage_Activity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);*/
    }
}
