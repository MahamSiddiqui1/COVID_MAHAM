package com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities.VAC_SearchActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.akdndhrc.covid_module.Adapter.Adt_Search.Adt_SearchFamilyMembersList;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.VAC_App.HomePageVacinator_Activity;
import com.akdndhrc.covid_module.VAC_App.VAC_InsideOutsideUC.VAC_Child_HifazitiTeekeyRecordList2_Activity;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class VAC_Search_MemberList_Activity extends AppCompatActivity {

    Context ctx = VAC_Search_MemberList_Activity.this;

    ListView lv;
    ImageView iv_arrow_back, iv_home;
    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    Adt_SearchFamilyMembersList adt;

    String vaccination_card_no, shared_data, qrcode_value, cnic_number, phone_number;

    String[][] mData;
    public static String temp_var = "0";
    TextView txt_name;
    HashMap<String, String> map = new HashMap<>();
    HashMap<Integer, ArrayList<String>> array_map = new HashMap<Integer, ArrayList<String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_khandan_number_list);

        //ListView
        lv = findViewById(R.id.lv);

        //ImageView
        iv_home = findViewById(R.id.iv_home);
        iv_arrow_back = findViewById(R.id.iv_arrow_back);
        iv_home.setVisibility(View.GONE);
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
                Intent newIntent = new Intent(ctx, HomePageVacinator_Activity.class);
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
            cnic_number = settings.getString("nic_number", "");
            phone_number = settings.getString("phone_number", "");
            qrcode_value = settings.getString("qrcode_value", "");
            vaccination_card_no = settings.getString("vaccination_card_no", "");

            editor.apply();

            Lister ls = new Lister(VAC_Search_MemberList_Activity.this);
            ls.createAndOpenDB();

            if (shared_data.equalsIgnoreCase("srch_nicnumber")) {
                Log.d("000888", "  IF: ");
                // mData = ls.executeReader("Select *from MEMBER where khandan_id = '" + khandan_id + "' AND nicnumber LIKE '"+cnic_number+"%'");

                try {

                    //txt_name.setText("دو سال سے کم عمر کے بچوں کی لسٹ");
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

                    adt = new Adt_SearchFamilyMembersList(ctx, hashMapArrayList);
                    adt.notifyDataSetChanged();
                    lv.setAdapter(adt);


                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(ctx, VAC_Child_HifazitiTeekeyRecordList2_Activity.class);
                            intent.putExtra("u_id", mData[position][0]);
                            intent.putExtra("child_name", mData[position][1]);
                            intent.putExtra("child_gender", mData[position][2]);
                            startActivity(intent);

                            Log.d("000888", "u_id : " + mData[position][0]);
                            Log.d("000888", "child_name : " + mData[position][1]);


                            temp_var = "child_record_list";
                        }
                    });


                } catch (Exception e) {
                    Log.d("000888", " Khandan Error: " + e.getMessage());
                }

            } else if (shared_data.equalsIgnoreCase("srch_phonenumber")) {
                Log.d("000888", "  Else IF 1: ");
                // mData = ls.executeReader("Select *from MEMBER where khandan_id = '" + khandan_id + "' AND nicnumber LIKE '"+cnic_number+"%'");

                try {
                    // txt_name.setText("دو سال سے کم عمر کے بچوں کی لسٹ");
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

                    adt = new Adt_SearchFamilyMembersList(ctx, hashMapArrayList);
                    adt.notifyDataSetChanged();
                    lv.setAdapter(adt);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(ctx, VAC_Child_HifazitiTeekeyRecordList2_Activity.class);
                            intent.putExtra("u_id", mData[position][0]);
                            intent.putExtra("child_name", mData[position][1]);
                            intent.putExtra("child_gender", mData[position][2]);
                            startActivity(intent);

                            Log.d("000888", "u_id : " + mData[position][0]);
                            Log.d("000888", "child_name : " + mData[position][1]);


                            temp_var = "child_record_list";
                        }
                    });

                } catch (Exception e) {
                    Log.d("000888", " Khandan Error: " + e.getMessage());
                }
            } else if (shared_data.equalsIgnoreCase("vaccination_cardno")) {
                Log.d("000888", "  Else IF 2: ");
                // mData = ls.executeReader("Select *from MEMBER where khandan_id = '" + khandan_id + "' AND nicnumber LIKE '"+cnic_number+"%'");

                try {
                    //txt_name.setText("دو سال سے کم عمر کے بچوں کی لسٹ");
                    txt_name.setText("افراد کی لسٹ");

                    mData = ls.executeReader("Select uid,full_name,gender,age,added_on,data from MEMBER where data LIKE '%" + vaccination_card_no + "%'");
                    Log.d("000888", "VAC DATA LEN: " + String.valueOf(mData.length));

                    for (int a = 0; a < mData.length; a++) {

                        JSONObject jsonObject = new JSONObject(mData[a][5]);

                        try {
                            if (jsonObject.has("vaccination_card_number")) {
                                if (jsonObject.getString("vaccination_card_number").contains(vaccination_card_no.toString())) {
                                    Log.d("000888", "DATA:" + mData[a][5]);
                                    Log.d("000888", "IF vaccination_card_number : " + jsonObject.getString("vaccination_card_number"));

                                    map = new HashMap<>();

                                    Log.d("000888", "uid : " + mData[a][0]);
                                    Log.d("000888", "full_name : " + mData[a][1]);
                                    Log.d("000888", "gender : " + mData[a][2]);
                                    Log.d("000888", "age : " + mData[a][3]);


                                    map.put("u_id", "" + mData[a][0]);
                                    map.put("full_name", "" + mData[a][1]);
                                    map.put("gender", "" + mData[a][2]);
                                    map.put("age", "" + mData[a][3]);


                                    hashMapArrayList.add(map);

                                    continue;
                                } else {
                                    Log.d("000888", "ELSE 1: ");
                                    continue;
                                }
                            } else {
                                Log.d("000888", "ELSE 11: ");
                                continue;
                            }
                        } catch (Exception e) {
                            Log.d("000888", "CATCH: " + e.getMessage());
                            continue;
                        }
                    }
                    adt = new Adt_SearchFamilyMembersList(ctx, hashMapArrayList);
                    adt.notifyDataSetChanged();
                    lv.setAdapter(adt);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(ctx, VAC_Child_HifazitiTeekeyRecordList2_Activity.class);
                            intent.putExtra("u_id", hashMapArrayList.get(position).get("u_id"));
                            intent.putExtra("child_name", hashMapArrayList.get(position).get("full_name"));
                            intent.putExtra("child_gender", hashMapArrayList.get(position).get("gender"));
                            startActivity(intent);
                            Log.d("000888", "u_id : " + hashMapArrayList.get(position).get("u_id"));
                            Log.d("000888", "child_name : " + hashMapArrayList.get(position).get("full_name"));
                            temp_var = "child_record_list";
                        }
                    });

                } catch (Exception e) {
                    Log.d("000888", " VAC CArd Error: " + e.getMessage());
                }
            } else if (shared_data.equalsIgnoreCase("qrcode")) {
                Log.d("000888", "  Else IF 3: ");
                // mData = ls.executeReader("Select *from MEMBER where khandan_id = '" + khandan_id + "' AND nicnumber LIKE '"+cnic_number+"%'");

                try {
                    // txt_name.setText("دو سال سے کم عمر کے بچوں کی لسٹ");
                    //txt_name.setText("بچوں کی لسٹ");
                    txt_name.setText("افراد کی لسٹ");

                    mData = ls.executeReader("Select uid,full_name,gender,age,added_on from MEMBER where  qr_code = '" + qrcode_value + "'");
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
                        map.put("u_id", "" + mData[i][0]);
                        map.put("full_name", "" + mData[i][1]);
                        map.put("gender", "" + mData[i][2]);
                        map.put("age", "" + mData[i][3]);

                        hashMapArrayList.add(map);

                    }

                    adt = new Adt_SearchFamilyMembersList(ctx, hashMapArrayList);
                    adt.notifyDataSetChanged();
                    lv.setAdapter(adt);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(ctx, VAC_Child_HifazitiTeekeyRecordList2_Activity.class);
                            intent.putExtra("u_id", hashMapArrayList.get(position).get("u_id"));
                            intent.putExtra("child_name", hashMapArrayList.get(position).get("full_name"));
                            intent.putExtra("child_gender", hashMapArrayList.get(position).get("gender"));
                            startActivity(intent);
                            Log.d("000888", "u_id : " + hashMapArrayList.get(position).get("u_id"));
                            Log.d("000888", "child_name : " + hashMapArrayList.get(position).get("full_name"));
                            temp_var = "child_record_list";
                        }
                    });

                } catch (Exception e) {
                    Log.d("000888", " QR Error: " + e.getMessage());
                }
            } else {

            }
        } catch (Exception e) {
            Log.d("000888", "Error: " + e.getMessage());
            //    Toast.makeText(ctx, "Err", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(ctx, VAC_Search_Activity.class));

    }
}
