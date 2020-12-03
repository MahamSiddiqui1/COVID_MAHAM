package com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHaamlaRecordActivities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHaamlaRecordActivities.MotherBadAzPedaishActivities.Mother_BadAzPedaishList_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHaamlaRecordActivities.MotherQabalAzPedaishActivities.Mother_QabalAzPedaishForm_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHaamlaRecordActivities.MotherQabalAzPedaishActivities.Mother_QabalAzPedaishList_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.MotherHaamlaRecordActivities.MotherZichgiActivities.Mother_ZichgiList_Activity;
import com.akdndhrc.covid_module.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Mother_HaamlaDashboard_Activity extends AppCompatActivity implements View.OnClickListener {

    Context ctx = Mother_HaamlaDashboard_Activity.this;

    TextView txt_mother_name, txt_mother_age;
    RelativeLayout relative_antenatal, relative_delivery, relative_postnatal;
    ImageView iv_navigation_drawer, iv_home, iv_antenatal, iv_delivery, iv_postnatal, iv_close;

    Button btn_jamaa_kre;
    RadioGroup radioGroup;
    RadioButton rb_LiveBirth, rb_Abortion, rb_StillBirth, rb_NeonatalDeath, rb_InfantDeath, rb_WrongEntry, rb_DeliveryCompleted;

    String mother_uid, mother_name, mother_age;
    String preg_id, radio_inactive_reason;
    Switch sw_preg_status;
    JSONObject jsonObject;
    Dialog alertDialog;
    public static String var_pregInActive = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_haamla_dashboard);

        mother_uid = getIntent().getExtras().getString("u_id");
        preg_id = getIntent().getExtras().getString("preg_id");

        mother_name = getIntent().getExtras().getString("mother_name");
        mother_age = getIntent().getExtras().getString("mother_age");

        //RelativeLayout
        relative_antenatal = findViewById(R.id.relative_antenatal);
        relative_delivery = findViewById(R.id.relative_delivery);
        relative_postnatal = findViewById(R.id.relative_postnatal);

        //TextView
        txt_mother_name = findViewById(R.id.txt_mother_name);
        txt_mother_age = findViewById(R.id.txt_mother_age);

        //Switch
        sw_preg_status = findViewById(R.id.sw_preg_status);

        txt_mother_name.setText(mother_name);
        txt_mother_age.setText(mother_age);


        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        //    iv_home.setVisibility(View.GONE);
        iv_antenatal = findViewById(R.id.iv_antenatal);
        iv_delivery = findViewById(R.id.iv_delivery);
        iv_postnatal = findViewById(R.id.iv_postnatal);


        iv_navigation_drawer.setOnClickListener(this);
        iv_home.setOnClickListener(this);
        relative_antenatal.setOnClickListener(this);
        relative_delivery.setOnClickListener(this);
        relative_postnatal.setOnClickListener(this);
        sw_preg_status.setOnClickListener(this);

       
                  /*  PopupMenu popupMenu = new PopupMenu(ctx,sw_preg_status);
                    popupMenu.getMenuInflater().inflate(R.menu.reason_inactive_preg,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Toast.makeText(ctx, ""+item.getTitle(), Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                    popupMenu.show();*/

                 /*sw_preg_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                      @Override
                      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                          if (!isChecked)
                          {
                              Log.d("000333","444444 ");
                          }
                          else{
                              Log.d("000333","55555555 ");
                          }
                      }
                  });*/


        sw_preg_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        /*sw_preg_status.setText(R.string.active);
                        sw_preg_status.setChecked(true);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            sw_preg_status.setThumbTintList(ctx.getResources().getColorStateList(R.color.switch_green_color));
                            sw_preg_status.setTrackTintList(ctx.getResources().getColorStateList(R.color.green_color));
                        }*/
                sw_preg_status.setChecked(false);
                sw_preg_status.setText(R.string.inactive);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    sw_preg_status.setThumbTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                    sw_preg_status.setTrackTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                }
                Dialog_ReasonInActivePregnancy();
            }
        });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.iv_navigation_drawer:

                break;

            case R.id.iv_home:
                Intent newIntent = new Intent(ctx, HomePage_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
                break;

            case R.id.relative_antenatal:
                // startActivity(new Intent(ctx, Mother_QabalAzPedaishForm_Activity.class));
                Lister ls = new Lister(Mother_HaamlaDashboard_Activity.this);
                ls.createAndOpenDB();

                String[][] mData = ls.executeReader("Select count(member_uid) from MANC where member_uid = '" + mother_uid + "' AND pregnancy_id= '" + preg_id + "' AND is_synced NOT IN ('-1') ");
                if (mData[0][0].equalsIgnoreCase("0")) {
                    Intent intent1 = new Intent(ctx, Mother_QabalAzPedaishForm_Activity.class);
                    intent1.putExtra("u_id", mother_uid);
                    intent1.putExtra("preg_id", preg_id);
                    intent1.putExtra("mother_age", mother_age);
                    intent1.putExtra("mother_name", mother_name);
                    startActivity(intent1);
                } else {
                    Intent intent1 = new Intent(ctx, Mother_QabalAzPedaishList_Activity.class);
                    intent1.putExtra("u_id", mother_uid);
                    intent1.putExtra("preg_id", preg_id);
                    intent1.putExtra("mother_age", mother_age);
                    intent1.putExtra("mother_name", mother_name);
                    startActivity(intent1);
                }
                break;


            case R.id.relative_delivery:
                //Dialog_ZichgiButton();
                Intent intent5 = new Intent(ctx, Mother_ZichgiList_Activity.class);
                intent5.putExtra("u_id", mother_uid);
                intent5.putExtra("preg_id", preg_id);
                intent5.putExtra("mother_age", mother_age);
                intent5.putExtra("mother_name", mother_name);
                startActivity(intent5);

                break;

            case R.id.relative_postnatal:
                // startActivity(new Intent(ctx, Mother_BadAzPedaishList_Activity.class));
                Intent intent = new Intent(ctx, Mother_BadAzPedaishList_Activity.class);
                intent.putExtra("u_id", mother_uid);
                intent.putExtra("preg_id", preg_id);
                intent.putExtra("mother_age", mother_age);
                intent.putExtra("mother_name", mother_name);
                startActivity(intent);
                break;


        }
    }


    public void Dialog_ReasonInActivePregnancy() {
        final Dialog dialog = new Dialog(ctx);
        LayoutInflater layout = LayoutInflater.from(ctx);

        View dialogView = layout.inflate(R.layout.dialog_mpregnancy_inactive_layout, null);

        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        iv_close = (ImageView) dialogView.findViewById(R.id.iv_close);

        //Radio Group
        radioGroup = dialogView.findViewById(R.id.radioGroup);

        //RadioButton
        rb_LiveBirth = dialogView.findViewById(R.id.rb_LiveBirth);
        rb_Abortion = dialogView.findViewById(R.id.rb_Abortion);
        rb_StillBirth = dialogView.findViewById(R.id.rb_StillBirth);
        rb_NeonatalDeath = dialogView.findViewById(R.id.rb_NeonatalDeath);
        rb_InfantDeath = dialogView.findViewById(R.id.rb_InfantDeath);
        rb_WrongEntry = dialogView.findViewById(R.id.rb_WrongEntry);
        rb_DeliveryCompleted = dialogView.findViewById(R.id.rb_DeliveryCompleted);

        btn_jamaa_kre = (Button) dialogView.findViewById(R.id.submit);
        btn_jamaa_kre.setVisibility(View.GONE);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sw_preg_status.setText(R.string.active);
                sw_preg_status.setChecked(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    sw_preg_status.setThumbTintList(ctx.getResources().getColorStateList(R.color.switch_green_color));
                    sw_preg_status.setTrackTintList(ctx.getResources().getColorStateList(R.color.green_color));
                }
            }
        });

        var_pregInActive = "0";

       /* radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_LiveBirth:
                        radio_inactive_reason="Live birth";

                        rb_Abortion.setChecked(false);
                        rb_StillBirth.setChecked(false);
                        rb_NeonatalDeath.setChecked(false);
                        rb_InfantDeath.setChecked(false);
                        rb_WrongEntry.setChecked(false);

                        btn_jamaa_kre.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_Abortion:
                        radio_inactive_reason="Abortion";
                        rb_LiveBirth.setChecked(false);
                        rb_StillBirth.setChecked(false);
                        rb_NeonatalDeath.setChecked(false);
                        rb_InfantDeath.setChecked(false);
                        rb_WrongEntry.setChecked(false);
                        btn_jamaa_kre.setVisibility(View.VISIBLE);
                        break;

                        case R.id.rb_StillBirth:
                            radio_inactive_reason="Still birth";
                            rb_Abortion.setChecked(false);
                            rb_LiveBirth.setChecked(false);
                            rb_NeonatalDeath.setChecked(false);
                            rb_InfantDeath.setChecked(false);
                            rb_WrongEntry.setChecked(false);
                            btn_jamaa_kre.setVisibility(View.VISIBLE);
                        break;

                        case R.id.rb_NeonatalDeath:
                            radio_inactive_reason="Neo-Natal death(< 1 week of Birth)";
                            rb_Abortion.setChecked(false);
                            rb_StillBirth.setChecked(false);
                            rb_LiveBirth.setChecked(false);
                            rb_InfantDeath.setChecked(false);
                            rb_WrongEntry.setChecked(false);
                            btn_jamaa_kre.setVisibility(View.VISIBLE);
                        break;

                        case R.id.rb_InfantDeath:
                            rb_Abortion.setChecked(false);
                            rb_StillBirth.setChecked(false);
                            rb_NeonatalDeath.setChecked(false);
                            rb_LiveBirth.setChecked(false);
                            rb_WrongEntry.setChecked(false);
                            radio_inactive_reason="Infant death (1+ week but <1 year)";
                            btn_jamaa_kre.setVisibility(View.VISIBLE);
                        break;

                        case R.id.rb_WrongEntry:
                            radio_inactive_reason="Wrong Entry";
                            if (rb_LiveBirth.isChecked())
                            {
                                Toast.makeText(ctx, "ID", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(ctx, "EW", Toast.LENGTH_SHORT).show();
                            }

                            btn_jamaa_kre.setVisibility(View.VISIBLE);
                        break;
                        default:
                }
            }
        });*/

        rb_LiveBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_inactive_reason = "Live birth";
                rb_LiveBirth.setChecked(true);
                rb_Abortion.setChecked(false);
                rb_StillBirth.setChecked(false);
                rb_NeonatalDeath.setChecked(false);
                rb_InfantDeath.setChecked(false);
                rb_WrongEntry.setChecked(false);
                rb_DeliveryCompleted.setChecked(false);

                btn_jamaa_kre.setVisibility(View.VISIBLE);
            }
        });

        rb_Abortion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_inactive_reason = "Abortion";
                rb_LiveBirth.setChecked(false);
                rb_Abortion.setChecked(true);
                rb_StillBirth.setChecked(false);
                rb_NeonatalDeath.setChecked(false);
                rb_InfantDeath.setChecked(false);
                rb_WrongEntry.setChecked(false);
                rb_DeliveryCompleted.setChecked(false);
                btn_jamaa_kre.setVisibility(View.VISIBLE);

            }
        });

        rb_StillBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_inactive_reason = "Still birth";
                rb_LiveBirth.setChecked(false);
                rb_Abortion.setChecked(false);
                rb_StillBirth.setChecked(true);
                rb_NeonatalDeath.setChecked(false);
                rb_InfantDeath.setChecked(false);
                rb_WrongEntry.setChecked(false);
                rb_DeliveryCompleted.setChecked(false);
                btn_jamaa_kre.setVisibility(View.VISIBLE);
            }
        });

        rb_NeonatalDeath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_inactive_reason = "Neo-Natal death(< 1 week of Birth)";
                rb_LiveBirth.setChecked(false);
                rb_Abortion.setChecked(false);
                rb_StillBirth.setChecked(false);
                rb_NeonatalDeath.setChecked(true);
                rb_InfantDeath.setChecked(false);
                rb_WrongEntry.setChecked(false);
                rb_DeliveryCompleted.setChecked(false);
                btn_jamaa_kre.setVisibility(View.VISIBLE);
            }
        });

        rb_InfantDeath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_inactive_reason = "Infant death (1+ week but <1 year)";
                rb_LiveBirth.setChecked(false);
                rb_Abortion.setChecked(false);
                rb_StillBirth.setChecked(false);
                rb_NeonatalDeath.setChecked(false);
                rb_InfantDeath.setChecked(true);
                rb_WrongEntry.setChecked(false);
                rb_DeliveryCompleted.setChecked(false);
                btn_jamaa_kre.setVisibility(View.VISIBLE);
            }
        });

        rb_WrongEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_inactive_reason = "Wrong Entry";
                rb_LiveBirth.setChecked(false);
                rb_Abortion.setChecked(false);
                rb_StillBirth.setChecked(false);
                rb_NeonatalDeath.setChecked(false);
                rb_InfantDeath.setChecked(false);
                rb_WrongEntry.setChecked(true);
                rb_DeliveryCompleted.setChecked(false);
                btn_jamaa_kre.setVisibility(View.VISIBLE);
            }
        });

        rb_DeliveryCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_inactive_reason = "Delivery Completed";
                rb_LiveBirth.setChecked(false);
                rb_Abortion.setChecked(false);
                rb_StillBirth.setChecked(false);
                rb_NeonatalDeath.setChecked(false);
                rb_InfantDeath.setChecked(false);
                rb_WrongEntry.setChecked(false);
                rb_DeliveryCompleted.setChecked(true);
                btn_jamaa_kre.setVisibility(View.VISIBLE);
            }
        });

        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                update_data();
            }
        });

    }


    private void update_data() {


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
                jsonObject.put("status", "" + "1");
                jsonObject.put("mpregnancy_status", "" + "InActive");
                jsonObject.put("preg_inactive_reason", "" + radio_inactive_reason);
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
                                "status='" + 0 + "'," +
                                "is_synced='" + 0 + "'" +
                                "WHERE member_uid = '" + mother_uid + "' AND pregnancy_id='" + preg_id + "'";
                        ls.executeNonQuery(update_record);

                        Boolean res = ls.executeNonQuery(update_record);
                        Log.d("000333", "Data: " + update_record);
                        Log.d("000333", "Query: " + res.toString());

                        var_pregInActive = "1";

                    } catch (Exception e) {
                        alertDialog.dismiss();
                        Log.d("000333", " Error" + e.getMessage());
                        // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    } finally {
                        alertDialog.dismiss();
                        Intent intent = new Intent(ctx, Mother_HaamlaDashboard_Activity.class);
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

            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            String[][] mData = ls.executeReader("Select metadata from MPREGNANCY where member_uid = '" + mother_uid + "' AND pregnancy_id ='" + preg_id + "'");

            Log.d("000333", "Preg Data: " + mData[0][0]);

            jsonObject = new JSONObject(mData[0][0]);

            if (jsonObject.has("mpregnancy_status") || jsonObject.has("status")) {
                if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                    Log.d("000333", "111111111 ");
                    sw_preg_status.setEnabled(false);
                    sw_preg_status.setChecked(false);
                    sw_preg_status.setText(R.string.inactive);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        sw_preg_status.setThumbTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                        sw_preg_status.setTrackTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                    }
                } else {
                    Log.d("000333", "22222222 ");
                    sw_preg_status.setText(R.string.active);
                    sw_preg_status.setChecked(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        sw_preg_status.setThumbTintList(ctx.getResources().getColorStateList(R.color.switch_green_color));
                        sw_preg_status.setTrackTintList(ctx.getResources().getColorStateList(R.color.green_color));
                    }

                }

            } else {
                Log.d("000333", "33333 ");
                sw_preg_status.setText(R.string.active);
                sw_preg_status.setChecked(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    sw_preg_status.setThumbTintList(ctx.getResources().getColorStateList(R.color.switch_green_color));
                    sw_preg_status.setTrackTintList(ctx.getResources().getColorStateList(R.color.green_color));
                }
            }

        } catch (Exception e) {
            Log.d("000333", "Preg Status Error: " + e.getMessage());
        }

    }
}
