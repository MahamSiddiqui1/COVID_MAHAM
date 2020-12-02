package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_RegisterActivities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.Child_Dashboard_Activity;
import com.akdndhrc.covid_module.Adapter.Adt_AddFamilyMember;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.Mother_Dashboard_Activity;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.slider.BaseSlideMenuActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Add_Family_Member_Activity extends BaseSlideMenuActivity {

    Context ctx = Add_Family_Member_Activity.this;

    Button btn_khandan_kay_rukan_shamil_kre, btn_dou_saal_sy_kum_umer, btn_dou_saal_sy_zayed_umer;
    ImageView iv_pen;
    TextView txt_khandan_number;
    ListView lv;
    SimpleAdapter simpleAdapter;
    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

    RelativeLayout rl_navigation_drawer, rl_home,rl_khandan_edit;
    Adt_AddFamilyMember adt;
    String[][] mData;
    String khandan_manual_id,khandan_uuid;
    String json;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__family__member_);

        //TextView
        txt_khandan_number = findViewById(R.id.txt_khandan_number);


        SharedPreferences settings = getSharedPreferences("Khandanuuid", MODE_PRIVATE);
        // Reading from SharedPreferences
        khandan_uuid = settings.getString("k_uuid", "");
        khandan_manual_id = settings.getString("k_uuid_m", "");

        Log.d("000147", "khandan_uuid: " + khandan_uuid);
        Log.d("000147", "khandan_manual_id: " + khandan_manual_id);

        txt_khandan_number.setText(khandan_manual_id);

        //ImageView
        iv_pen = findViewById(R.id.iv_pen);

        //Relative Layout
        rl_navigation_drawer = findViewById(R.id.rl_navigation_drawer);
        rl_home = findViewById(R.id.rl_home);
        rl_khandan_edit = findViewById(R.id.rl_khandan_edit);

        rl_navigation_drawer.setVisibility(View.GONE);
        rl_home.setVisibility(View.GONE);

        //Button
        btn_khandan_kay_rukan_shamil_kre = findViewById(R.id.btn_khandan_kay_rukan_shamil_kre);

        //ListView
        lv = findViewById(R.id.lv);

        rl_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, R.string.navigation, Toast.LENGTH_SHORT).show();
            }
        });

        rl_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Home", Toast.LENGTH_SHORT).show();
            }
        });

       rl_khandan_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, LHW_RegisterHouseView_Activity.class);
                intent.putExtra("khandan_uuid",khandan_uuid);
                startActivity(intent);
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (Integer.parseInt(mData[position][2]) == 1) {
                    if (Integer.parseInt(mData[position][3]) <= 2) {
                        Intent intent = new Intent(ctx, Child_Dashboard_Activity.class);
                        intent.putExtra("u_id", mData[position][0]);
                        intent.putExtra("child_gender", mData[position][2]);

                        startActivity(intent);
                    } else {
                        Toast.makeText(ctx, "No Feature defined", Toast.LENGTH_SHORT).show();
                    }
                } else if (Integer.parseInt(mData[position][2]) == 0) {
                    if (Integer.parseInt(mData[position][3]) <= 2) {
                        Intent intent = new Intent(ctx, Child_Dashboard_Activity.class);
                        intent.putExtra("u_id", mData[position][0]);
                        intent.putExtra("child_gender", mData[position][2]);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(ctx, Mother_Dashboard_Activity.class);
                        intent.putExtra("u_id", mData[position][0]);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(ctx, "Position: " + position, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_khandan_kay_rukan_shamil_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_DoSaalSyKumUmerBachoKaiLye();
            }
        });


    }

    public void Dialog_DoSaalSyKumUmerBachoKaiLye() {


       /* final Dialog dialog = new Dialog(ctx, android.R.style.Theme_DeviceDefault_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_dou_saal_sy_kum_umer_layout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        btn_dou_saal_sy_kum_umer = (Button) dialog.findViewById(R.id.btn_dou_saal_sy_kum_umer);
        btn_dou_saal_sy_zayed_umer = (Button) dialog.findViewById(R.id.btn_dou_saal_sy_zayed_umer);

//        dialog.setContentView(dialog);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
        dialog.show();*/

        final Dialog dialog = new Dialog(ctx);
        LayoutInflater layout = LayoutInflater.from(ctx);
        final View dialogView = layout.inflate(R.layout.dialog_dou_saal_sy_kum_umer_layout, null);

        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        dialog.show();

        ImageView iv_close = (ImageView) dialogView.findViewById(R.id.iv_close);

        btn_dou_saal_sy_kum_umer = dialogView.findViewById(R.id.btn_dou_saal_sy_kum_umer);
        btn_dou_saal_sy_zayed_umer = dialogView.findViewById(R.id.btn_dou_saal_sy_zayed_umer);


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_dou_saal_sy_kum_umer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, BelowTwo_Register_Activity.class);
                startActivity(intent);
                dialog.dismiss();

            }
        });

        btn_dou_saal_sy_zayed_umer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, AboveTwo_Register_Activity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();


        try {

            list.clear();
            Lister ls = new Lister(Add_Family_Member_Activity.this);
            ls.createAndOpenDB();


            try {
                //String[][] data = ls.executeReader("Select* from KHANDAN ");
                mData = ls.executeReader("Select uid,full_name,gender,age,added_on,data from MEMBER where khandan_id = '" + khandan_uuid + "'");
                json = mData[0][5];
                jsonObject = new JSONObject(json);


                Log.d("000555", "khandan length: " + String.valueOf(mData.length));
            } catch (Exception e) {
                Log.d("000555", "khandan Err: " + e.getMessage());
            }

            HashMap<String, String> map;

            iv_pen.setVisibility(View.GONE);
            rl_khandan_edit.setEnabled(false);

            for (int i = 0; i < mData.length; i++) {

                Log.d("khandan_data", "onResume: " + mData[i][1]);

                map = new HashMap<>();
                map.put("child_name", "" + mData[i][1]);
                map.put("gender", "" + mData[i][2]);
                map.put("age", "" + mData[i][3]);
                map.put("child_date", "" + jsonObject.getString("record_date"));

                list.add(map);
            }
            adt = new Adt_AddFamilyMember(ctx, list);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);

        } catch (Exception e) {
            iv_pen.setVisibility(View.VISIBLE);
            rl_khandan_edit.setEnabled(true);
            Log.d("12345", "Error: " + e.getMessage());
            //Toast.makeText(ctx, "کوئی ریکارڈ نہیں", Toast.LENGTH_SHORT).show();
            Toast tt = Toast.makeText(ctx,"خاندان کا کوئی رکن رجسٹر نہیں", Toast.LENGTH_SHORT);
            tt.setGravity(Gravity.CENTER, 0, 0);
            tt.show();
        }

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }


    @Override
    public void onBackPressed() {
        ///  super.onBackPressed();

        Log.d("00077", "ONBAck: ");
        Intent newIntent = new Intent(ctx, HomePage_Activity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
    }
}
