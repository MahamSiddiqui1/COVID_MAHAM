package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SearchActivities;

import android.app.Activity;
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
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.Child_Dashboard_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MaleDashboardActivities.AboveTwoMaleProfile_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MaleDashboardActivities.MaleBemaariRecordActivities.Male_BemaariRecordList_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MaleDashboardActivities.MaleReferralActivities.Male_RefferalRecordList_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_RegisterActivities.LHW_RegisterHouseView_Activity;
import com.akdndhrc.covid_module.Adapter.Adt_Search.Adt_SearchFamilyMembersList;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.Mother_Dashboard_Activity;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.SwipeDismissListViewTouchListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Search_FamilyMembersList extends AppCompatActivity {

    Context ctx = Search_FamilyMembersList.this;

    ListView lv;
    TextView txt_khandan_number;
    ImageView iv_navigation_drawer, iv_home;

    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    Adt_SearchFamilyMembersList adt;
    String khandan_id, khandan_number,login_useruid;
    String mData[][];
    public static String temp_var = "0";

    Button btn_dou_saal_sy_kum_umer, btn_dou_saal_sy_zayed_umer, btn_register_family_member;
    public Dialog alertDialog, alertProgressDialog;


    Button btn_shift_member, btn_delete_member, btnContinue;
    LinearLayout ll_btn, ll_khandanNumber;
    EditText et_khandan_number_search;
    RelativeLayout rl_khandanHead,rl_edit_khandan_form;
    TextView tv_KhandanHeadName, tvGaon, tv_KhandanNumber, tv_MemberName, tvMemberAge, tv_MemberKhandan;
    ImageView iv_member_icon;
    TextView tv_record;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_family_members_list);

        khandan_id = getIntent().getExtras().getString("khandan_id");
        khandan_number = getIntent().getExtras().getString("manual_id");


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

        //TextView
        txt_khandan_number = findViewById(R.id.txt_khandan_number);
        txt_khandan_number.setText(khandan_number);

        tv_record = findViewById(R.id.tv_record);

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        //    iv_home.setVisibility(View.GONE);


        btn_register_family_member = findViewById(R.id.btn_register_family_member);
        btn_register_family_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Register();
            }
        });


        rl_edit_khandan_form = findViewById(R.id.rl_edit_khandan_form);
        rl_edit_khandan_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, LHW_RegisterHouseView_Activity.class);
                intent.putExtra("khandan_uuid",khandan_id);
                startActivity(intent);
            }
        });



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (Integer.parseInt(mData[position][3]) < 5) {
                    Intent intent = new Intent(ctx, Child_Dashboard_Activity.class);
                    intent.putExtra("u_id", mData[position][0]);
                    intent.putExtra("child_name", mData[position][1]);
                    intent.putExtra("child_gender", mData[position][2]);
                    startActivity(intent);

                    temp_var = "child_dash";

                    Log.d("000330", "1");
                }
                else if (Integer.parseInt(mData[position][3]) >=5 && Integer.parseInt(mData[position][3]) <=14) {
                    Male_BottomDialog(position);
                    Log.d("000330", "2");
                }

                else if (Integer.parseInt(mData[position][3]) > 14) {
                    if (mData[position][2].equalsIgnoreCase("0")){
                        Intent intent = new Intent(ctx, Mother_Dashboard_Activity.class);
                        intent.putExtra("u_id", mData[position][0]);
                        intent.putExtra("mother_name", mData[position][1]);
                        startActivity(intent);
                        temp_var = "mother_dash";
                        Log.d("000330", "3");
                    }else if (mData[position][2].equalsIgnoreCase("1")) {
                        Male_BottomDialog(position);
                        Log.d("000330", "4");
                    }
                    else {
                        Log.d("000330", "5");
                       // Toast.makeText(ctx, "sd", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(ctx, "something wrong", Toast.LENGTH_SHORT).show();
                }


/*
                if (Integer.parseInt(mData[position][2]) == 1) {
                    if (Integer.parseInt(mData[position][3]) <= 2) {
                        Intent intent = new Intent(ctx, Child_Dashboard_Activity.class);
                        intent.putExtra("u_id", mData[position][0]);
                        intent.putExtra("child_gender", mData[position][2]);
                        startActivity(intent);

                        temp_var = "child_dash";
                    } else {
                        Toast.makeText(ctx, "No Feature defined", Toast.LENGTH_SHORT).show();
                    }
                } else if (Integer.parseInt(mData[position][2]) == 0) {
                    if (Integer.parseInt(mData[position][3]) <= 2) {
                        Intent intent = new Intent(ctx, Child_Dashboard_Activity.class);
                        intent.putExtra("u_id", mData[position][0]);
                        intent.putExtra("child_gender", mData[position][2]);
                        startActivity(intent);
                        temp_var = "child_dash";

                    } else {
                        Intent intent = new Intent(ctx, Mother_Dashboard_Activity.class);
                        intent.putExtra("u_id", mData[position][0]);
                        startActivity(intent);
                        temp_var = "mother_dash";

                    }
                } else {
                    //  Toast.makeText(ctx, "Position: " +position, Toast.LENGTH_SHORT).show();
                }*/
            }
        });


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


                                    /*BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(ctx);
                                    View sheetView = getLayoutInflater().inflate(R.layout.dialog_bottom_sheet_layout, null);
                                    mBottomSheetDialog.setContentView(sheetView);
                                    mBottomSheetDialog.show();

                                    LinearLayout edit = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_edit);
                                    LinearLayout delete = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_delete);


                                    mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            Toast.makeText(ctx, "Disi", Toast.LENGTH_SHORT).show();
                                        }
                                    });*/

                                    final Dialog alertDialog = new Dialog(ctx);
                                    LayoutInflater layout = LayoutInflater.from(ctx);
                                    final View dialogView = layout.inflate(R.layout.dialog_move_and_delete_member_layout, null);

                                    alertDialog.setContentView(dialogView);
                                    alertDialog.setCanceledOnTouchOutside(false);
                                    alertDialog.setCancelable(false);
                                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                    alertDialog.show();


                                    //ImageView
                                    final ImageView iv_close = dialogView.findViewById(R.id.iv_close);

                                    //Button
                                    btn_shift_member = dialogView.findViewById(R.id.btn_shift_member);
                                    btn_delete_member = dialogView.findViewById(R.id.btn_delete_member);
                                    btnContinue = dialogView.findViewById(R.id.btnContinue);

                                    //LinearLayout
                                    ll_btn = dialogView.findViewById(R.id.ll_btn);
                                    ll_khandanNumber = dialogView.findViewById(R.id.ll_khandanNumber);

                                    //EditText
                                    et_khandan_number_search = dialogView.findViewById(R.id.et_khandan_number_search);


                                    iv_close.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alertDialog.dismiss();
                                        }
                                    });


                                    btn_shift_member.setOnClickListener(
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    ll_btn.setVisibility(View.GONE);
                                                    ll_khandanNumber.setVisibility(View.VISIBLE);
                                                }
                                            });


                                    btnContinue.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View v) {


                                            if (et_khandan_number_search.getText().toString().isEmpty()) {
                                                Toast.makeText(ctx, "برائے مہربانی  خاندان نمبر درج کریں", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            if (et_khandan_number_search.getText().toString().equalsIgnoreCase(khandan_number)) {
                                                Toast.makeText(ctx, "برائے مہربانی دوسرا خاندان نمبر درج کریں.", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            InputMethodManager inputMethodManager = (InputMethodManager) ctx.getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            if (Search_FamilyMembersList.this.getCurrentFocus() != null) {
                                                inputMethodManager.hideSoftInputFromWindow(Search_FamilyMembersList.this.getCurrentFocus().getWindowToken(), 0);
                                            }

                                            alertDialog.dismiss();

                                            alertProgressDialog = new Dialog(ctx);
                                            LayoutInflater layout = LayoutInflater.from(ctx);
                                            final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

                                            alertProgressDialog.setContentView(dialogView);
                                            alertProgressDialog.setCanceledOnTouchOutside(false);
                                            alertProgressDialog.setCancelable(false);
                                            alertProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                            alertProgressDialog.show();


                                            new Handler().postDelayed(new Runnable() {
                                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                                @Override
                                                public void run() {
                                                    try {

                                                        Lister ls = new Lister(ctx);
                                                        ls.createAndOpenDB();

                                                        final String[][] mData_khandan = ls.executeReader("Select uid, family_head_name,village_id,max(added_on) from KHANDAN where manual_id = '" + et_khandan_number_search.getText().toString()+ "'");

                                                        if (mData_khandan != null) {

                                                            alertProgressDialog.dismiss();
                                                            //  alertDialog.show();

                                                            // for (int i = 0; i < mData_khandan.length; i++) {


                                                            Log.d("000777", "work");
                                                            Log.d("000777", "KHANDAN UID: " + mData_khandan[0][0]);
                                                            Log.d("000777", "KHANDAN HEAD NAME: " + mData_khandan[0][1]);


                                                            String[][] data = ls.executeReader("Select name from VILLAGES where uid = '" + mData_khandan[0][2] + "'");
                                                            String village_name;
                                                            if (data != null) {
                                                                village_name=data[0][0];
                                                                /*for (int a = 0; a < data.length; a++) {
                                                                    Log.d("000777", "NAME : " + data[a][0]);
                                                                    //  tvGaon.setText(data[a][0]);
                                                                }*/
                                                            }else {
                                                                village_name="no village";
                                                            }


                                                            final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(ctx, R.style.AppBottomSheetDialogTheme);
                                                            final View sheetView = getLayoutInflater().inflate(R.layout.dialog_bottom_sheet_layout, null);
                                                            mBottomSheetDialog.setContentView(sheetView);
                                                            mBottomSheetDialog.setCanceledOnTouchOutside(false);
                                                            mBottomSheetDialog.setCancelable(false);
                                                            mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                                            mBottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_SlideUp; //style id
                                                            mBottomSheetDialog.show();

                                                            //Relative Layout
                                                            rl_khandanHead = sheetView.findViewById(R.id.rl_khandanHead);

                                                            //TextView
                                                            tv_KhandanHeadName = sheetView.findViewById(R.id.tv_KhandanHeadName);
                                                            tv_KhandanNumber = sheetView.findViewById(R.id.tv_KhandanNumber);
                                                            tvGaon = sheetView.findViewById(R.id.tvGaon);

                                                            tv_MemberName = sheetView.findViewById(R.id.tv_MemberName);
                                                            tv_MemberKhandan = sheetView.findViewById(R.id.tv_MemberKhandan);
                                                            tvMemberAge = sheetView.findViewById(R.id.tvMemberAge);


                                                            //ImageView
                                                            iv_member_icon = sheetView.findViewById(R.id.iv_member_icon);
                                                            ImageView iv_close_dilaog = (ImageView) sheetView.findViewById(R.id.iv_close_dilaog);

                                                            //Button
                                                            Button btnNo = (Button) sheetView.findViewById(R.id.btnNo);
                                                            Button btnYes = (Button) sheetView.findViewById(R.id.btnYes);


                                                            tv_KhandanHeadName.setText(mData_khandan[0][1]);
                                                            tv_KhandanNumber.setText(et_khandan_number_search.getText().toString());
                                                            tvGaon.setText(village_name);

                                                            tv_MemberName.setText(list.get(position).get("full_name"));
                                                            tvMemberAge.setText(list.get(position).get("age") + " سال ");
                                                            tv_MemberKhandan.setText(khandan_number);


                                                            if (Integer.parseInt(list.get(position).get("age")) <= 2) {
                                                                if (list.get(position).get("gender").equalsIgnoreCase("0")) {
                                                                    iv_member_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.baby_boy_icon));
                                                                    iv_member_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.pink_color));
                                                                } else {
                                                                    iv_member_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.baby_boy_icon));
                                                                    iv_member_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.blue_text_color));
                                                                }
                                                            }
                                                            else if (Integer.parseInt(list.get(position).get("age")) >= 3 && Integer.parseInt(list.get(position).get("age")) <= 14)
                                                            {
                                                                if (list.get(position).get("gender").equalsIgnoreCase("0")) {
                                                                    iv_member_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_adult_female_50dp));
                                                                    iv_member_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.pink_color));
                                                                } else {
                                                                    iv_member_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                                                                    iv_member_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.blue_text_color));
                                                                }
                                                            }
                                                            else {
                                                                if (list.get(position).get("gender").equalsIgnoreCase("0")) {
                                                                    iv_member_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.woman_icon));
                                                                    iv_member_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.pink_color));
                                                                } else {
                                                                    iv_member_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                                                                    iv_member_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.blue_text_color));
                                                                }
                                                            }


                                                            iv_close_dilaog.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    mBottomSheetDialog.dismiss();
                                                                }
                                                            });


                                                            btnYes.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(final View v) {

                                                                    mBottomSheetDialog.dismiss();
                                                                    alertProgressDialog.show();

                                                                    try {

                                                                        JSONObject jsonObject = new JSONObject(list.get(position).get("data"));
                                                                        Log.d("000777", "JOSN DATA LENGTH: " +jsonObject.length());
                                                                        Log.d("000777", "JOSN DATA: " +jsonObject);

                                                                        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
                                                                        Calendar c = Calendar.getInstance();
                                                                        String datetime = dates.format(c.getTime());

                                                                        jsonObject.put("old_khandan_id",list.get(position).get("khandan_id"));
                                                                        jsonObject.put("khandan_shift_datetime",datetime);
                                                                        Log.d("000777", "Updated JOSN DATA LENGTH: " +jsonObject.length());
                                                                        Log.d("000777", "Updated JOSN DATA: " +jsonObject);


                                                                        Lister ls = new Lister(ctx);
                                                                        ls.createAndOpenDB();
                                                                        String update_record = "UPDATE MEMBER SET " +
                                                                                "khandan_id='" + mData_khandan[0][0] + "'," +
                                                                                "data='" + jsonObject.toString() + "'," +
                                                                                "added_by='" + login_useruid + "'," +
                                                                                "added_on='" + System.currentTimeMillis() + "'," +
                                                                                "is_synced='" + 0 + "'" +
                                                                                "WHERE uid = '" + list.get(position).get("uid") + "'";

                                                                        ls.executeNonQuery(update_record);

                                                                        Boolean res = ls.executeNonQuery(update_record);
                                                                        Log.d("000777", "MEMBER UPDATED Data: " + update_record);
                                                                        Log.d("000777", "MEMBER UPDATED Query: " + res.toString());


                                                                        if (res.toString().equalsIgnoreCase("true")) {
                                                                          //  Toast.makeText(Search_FamilyMembersList.this, "ممبر دوسرے  خاندان میں منتقل ہوگیا ہے.", Toast.LENGTH_LONG).show();
                                                                            Log.d("000777", "ABCCCC: ");
                                                                            final Snackbar snackbar = Snackbar.make(findViewById(R.id.search_family_mem), "ممبر دوسرے خاندان میں منتقل ہوگیا ہے.", Snackbar.LENGTH_LONG);
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

                                                                            Log.d("000777", "FINALLLaaaaaaaaaaaaaaaaaaa: ");

                                                                                 new Handler().postDelayed(new Runnable() {
                                                                                        @Override
                                                                                        public void run() {

                                                                                            Log.d("000777", "FINALLL: " );

                                                                                            alertProgressDialog.dismiss();

                                                                                            Intent intent = Search_FamilyMembersList.this.getIntent();
                                                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                                                            Search_FamilyMembersList.this.finish();
                                                                                            startActivity(intent);

                                                                                           /* Intent intent = new Intent(ctx, Search_FamilyMembersList.class);
                                                                                            intent.putExtra("khandan_id", khandan_id);
                                                                                            intent.putExtra("manual_id", khandan_number);
                                                                                            startActivity(intent);*/

                                                                                        }
                                                                                    },4000);


                                                                        } else {
                                                                            alertProgressDialog.dismiss();
                                                                           // mBottomSheetDialog.show();
                                                                            Toast.makeText(getApplicationContext(), R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    } catch (Exception e) {
                                                                        alertProgressDialog.dismiss();
                                                                       // mBottomSheetDialog.show();
                                                                        Log.d("000888", " Error" + e.getMessage());
                                                                        Toast.makeText(getApplicationContext(), R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });

                                                            btnNo.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    mBottomSheetDialog.dismiss();
                                                                }
                                                            });


                                                               /* mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                                    @Override
                                                                    public void onDismiss(DialogInterface dialog) {
                                                                        Toast.makeText(ctx, "Disi", Toast.LENGTH_SHORT).show();
                                                                        mBottomSheetDialog.dismiss();
                                                                    }
                                                                });*/

                                                            //  }

                                                        } else {
                                                            alertProgressDialog.dismiss();
                                                            alertDialog.show();
                                                            Log.d("000777", "ELSE Count NicNO: ");
                                                            Toast.makeText(ctx, "برائے مہربانی صحیح خاندان نمبر درج کریں.", Toast.LENGTH_SHORT).show();
                                                        }

                                                    } catch (Exception e) {
                                                        alertProgressDialog.dismiss();
                                                        alertDialog.show();
                                                        Log.d("000777", "Err: " + e.getMessage());
                                                    }

                                                }
                                            }, 2000);


                                        }
                                    });


                                    btn_delete_member.setOnClickListener(new View.OnClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                        @Override
                                        public void onClick(View v) {

                                            alertDialog.dismiss();

                                            final BottomSheetDialog mBottomSheetDialog_Delete = new BottomSheetDialog(ctx, R.style.AppBottomSheetDialogTheme);
                                            final View sheetView = getLayoutInflater().inflate(R.layout.dialog_delete_member_bottom_sheet_layout, null);
                                            mBottomSheetDialog_Delete.setContentView(sheetView);
                                            mBottomSheetDialog_Delete.setCanceledOnTouchOutside(false);
                                            mBottomSheetDialog_Delete.setCancelable(false);
                                            mBottomSheetDialog_Delete.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                            mBottomSheetDialog_Delete.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_SlideUp; //style id
                                            mBottomSheetDialog_Delete.show();


                                            //TextView
                                            tv_MemberName = sheetView.findViewById(R.id.tv_MemberName);
                                            tv_MemberKhandan = sheetView.findViewById(R.id.tv_MemberKhandan);
                                            tvMemberAge = sheetView.findViewById(R.id.tvMemberAge);


                                            //ImageView
                                            iv_member_icon = sheetView.findViewById(R.id.iv_member_icon);
                                            ImageView iv_close_dilaog = (ImageView) sheetView.findViewById(R.id.iv_close_dilaog);

                                            //Button
                                            Button btn_DeleteNo = (Button) sheetView.findViewById(R.id.btn_dNo);
                                            Button btn_DeleteYes = (Button) sheetView.findViewById(R.id.btn_dYes);


                                            tv_MemberName.setText(list.get(position).get("full_name"));
                                            tvMemberAge.setText(list.get(position).get("age") + " سال ");
                                            tv_MemberKhandan.setText(khandan_number);


                                            if (Integer.parseInt(list.get(position).get("age")) <= 2) {
                                                if (list.get(position).get("gender").equalsIgnoreCase("0")) {
                                                    iv_member_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.baby_boy_icon));
                                                    iv_member_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.pink_color));
                                                } else {
                                                    iv_member_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.baby_boy_icon));
                                                    iv_member_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.blue_text_color));
                                                }
                                            }
                                            else if (Integer.parseInt(list.get(position).get("age")) >= 3 && Integer.parseInt(list.get(position).get("age")) <= 14)
                                            {
                                                if (list.get(position).get("gender").equalsIgnoreCase("0")) {
                                                    iv_member_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_adult_female_50dp));
                                                    iv_member_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.pink_color));
                                                } else {
                                                    iv_member_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                                                    iv_member_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.blue_text_color));
                                                }
                                            }
                                            else {
                                                if (list.get(position).get("gender").equalsIgnoreCase("0")) {
                                                    iv_member_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.woman_icon));
                                                    iv_member_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.pink_color));
                                                } else {
                                                    iv_member_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                                                    iv_member_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.blue_text_color));
                                                }
                                            }


                                            iv_close_dilaog.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    mBottomSheetDialog_Delete.dismiss();
                                                }
                                            });


                                            btn_DeleteYes.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(final View v) {


                                                    mBottomSheetDialog_Delete.dismiss();


                                                    alertProgressDialog = new Dialog(ctx);
                                                    LayoutInflater layout = LayoutInflater.from(ctx);
                                                    final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

                                                    alertProgressDialog.setContentView(dialogView);
                                                    alertProgressDialog.setCanceledOnTouchOutside(false);
                                                    alertProgressDialog.setCancelable(false);
                                                    alertProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                                    alertProgressDialog.show();

                                                    Lister ls = new Lister(ctx);
                                                    ls.createAndOpenDB();

                                                    try {


                                                        String insert_data = "INSERT INTO DELETE_MEMBER SELECT * FROM MEMBER where uid = '" + list.get(position).get("uid") + "'";

                                                        Boolean res = ls.executeNonQuery(insert_data);
                                                        Log.d("000777", "INSERT MEMBER Data: " + insert_data);
                                                        Log.d("000777", "INSERT MEMBER Query: " + res.toString());


                                                        if (res.toString().equalsIgnoreCase("true")) {
                                                            Log.d("000777", "TRUE !!!!!!");

                                                            JSONObject jsonObject = new JSONObject(list.get(position).get("data"));
                                                            Log.d("000777", "JOSN DATA LENGTH: " +jsonObject.length());
                                                            Log.d("000777", "JOSN DATA: " +jsonObject);

                                                            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa");
                                                            Calendar c = Calendar.getInstance();
                                                            String datetime = dates.format(c.getTime());

                                                            jsonObject.put("member_deleted_datetime",datetime);
                                                            Log.d("000777", "Updated JOSN DATA LENGTH: " +jsonObject.length());
                                                            Log.d("000777", "Updated JOSN DATA: " +jsonObject);


                                                            String update_delete_member_record = "UPDATE MEMBER SET " +
                                                                    "data='" + jsonObject.toString() + "'," +
                                                                    "added_by='" + login_useruid + "'," +
                                                                    "added_on='" + System.currentTimeMillis() + "'," +
                                                                    "is_synced='" + 0 + "'" +
                                                                    "WHERE uid = '" + list.get(position).get("uid") + "'";

                                                            ls.executeNonQuery(update_delete_member_record);

                                                            Boolean resou = ls.executeNonQuery(update_delete_member_record);
                                                            Log.d("000777", "MEMBER JSON UPDATED Data: " + update_delete_member_record);
                                                            Log.d("000777", "MEMBER JSON UPDATED Query: " + res.toString());


                                                            if (resou.toString().equalsIgnoreCase("true"))
                                                            {
                                                                Log.d("000777", "TRUE !!!!!!!!!!!!!!!!!!");

                                                                String delete_record = "DELETE from MEMBER WHERE uid = '" + list.get(position).get("uid") + "'";
                                                                ls.executeNonQuery(delete_record);

                                                                Boolean res_del = ls.executeNonQuery(delete_record);
                                                                Log.d("000777", "DELETED MEMBER Data: " + delete_record);
                                                                Log.d("000777", "DELETED MEMBER Query: " + res_del.toString());


                                                                if (res_del.toString().equalsIgnoreCase("true")) {
                                                                    Log.d("000777", "TRUE !****************");
                                                                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.search_family_mem),  "ممبر اس خاندان سے ڈیلیٹ ہوگیا ہے.", Snackbar.LENGTH_SHORT);
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

                                                                            alertProgressDialog.dismiss();
                                                                            // mBottomSheetDialog_Delete.dismiss();
                                                                        /*Intent intent = new Intent(ctx, Search_FamilyMembersList.class);
                                                                        intent.putExtra("khandan_id", khandan_id);
                                                                        intent.putExtra("manual_id", khandan_number);
                                                                        startActivity(intent);*/

                                                                            Intent intent = Search_FamilyMembersList.this.getIntent();
                                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                                            Search_FamilyMembersList.this.finish();
                                                                            startActivity(intent);

                                                                        }
                                                                    }, 4000);
                                                                } else {
                                                                    alertProgressDialog.dismiss();
                                                                    mBottomSheetDialog_Delete.show();
                                                                    Toast.makeText(getApplicationContext(), R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                            else {
                                                                alertProgressDialog.dismiss();
                                                                mBottomSheetDialog_Delete.show();
                                                                Toast.makeText(getApplicationContext(), R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                                                            }

                                                            Log.d("000777", "ABCCCC: ");

                                                        } else {
                                                            alertProgressDialog.dismiss();
                                                            mBottomSheetDialog_Delete.show();
                                                            Toast.makeText(getApplicationContext(), R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                                                        }

                                                    } catch (Exception e) {
                                                        alertProgressDialog.dismiss();
                                                        mBottomSheetDialog_Delete.show();
                                                        Log.d("000888", " Error" + e.getMessage());
                                                        Toast.makeText(getApplicationContext(), R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                            btn_DeleteNo.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    mBottomSheetDialog_Delete.dismiss();
                                                }
                                            });
                                        }
                                    });

                                }
                            }
                        });

        lv.setOnTouchListener(touchListener);

        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Navigation", Toast.LENGTH_SHORT).show();
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


        try{
            if (Integer.parseInt(mData[position][3]) >=5 && Integer.parseInt(mData[position][3]) <=14) {

                if (mData[position][2].equalsIgnoreCase("0")){
                    image_profile_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_adult_female_50dp));
                    image_profile_gender.requestLayout();
                    image_profile_gender.getLayoutParams().height = newHeight;
                    image_profile_gender.getLayoutParams().width = newWidth;
                    Log.d("000330", "5 To 14 FEMALE");
                }
                else {
                    Log.d("000330", "5 To 14 MALE");
                    image_profile_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                }

            }

            else if (Integer.parseInt(mData[position][3]) > 14) {
                Log.d("000330", ">14 MALE");
                image_profile_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
            }

        }catch (Exception e)
        {
            Log.d("000330", "Ex: " +e.getMessage());
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


    public void Dialog_Register() {

        /*LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View promptView = layoutInflater.inflate(R.layout.dialog_dou_saal_sy_kum_umer_layout, null);

        final AlertDialog alertD = new AlertDialog.Builder(ctx).create();

        ImageView iv_close = (ImageView) promptView.findViewById(R.id.iv_close);

        btn_dou_saal_sy_kum_umer = promptView.findViewById(R.id.btn_dou_saal_sy_kum_umer);
        btn_dou_saal_sy_zayed_umer = promptView.findViewById(R.id.btn_dou_saal_sy_zayed_umer);


        alertD.setView(promptView);

        alertD.setCanceledOnTouchOutside(false);
        alertD.setCancelable(false);
        alertD.show();*/

        final Dialog dialog = new Dialog(ctx);
        LayoutInflater layout = LayoutInflater.from(ctx);
        final View dialogView = layout.inflate(R.layout.dialog_dou_saal_sy_kum_umer_layout, null);

        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
                dialog.dismiss();

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
                            Intent intent = new Intent(ctx, ChildRegister_Activity.class);
                            intent.putExtra("khandan_id", khandan_id);
                            intent.putExtra("khandan_number", khandan_number);
                            startActivity(intent);
                            alertDialog.dismiss();


                        } catch (Exception e) {
                            alertDialog.dismiss();
                            Log.d("000999", "Error" + e.getMessage());
                            // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 1500);

            }
        });

        btn_dou_saal_sy_zayed_umer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

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
                            Intent intent = new Intent(ctx, MotherRegister_Activity.class);
                            intent.putExtra("khandan_id", khandan_id);
                            intent.putExtra("khandan_number", khandan_number);
                            startActivity(intent);
                            alertDialog.dismiss();

                        } catch (Exception e) {
                            alertDialog.dismiss();
                            Log.d("000999", "Error" + e.getMessage());
                            // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 1500);


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        list.clear();

        try {

            Lister ls = new Lister(Search_FamilyMembersList.this);
            ls.createAndOpenDB();

            mData = ls.executeReader("Select uid,full_name,gender,age,added_on,data,khandan_id from MEMBER where khandan_id = '" + khandan_id + "'");
            Log.d("000888", String.valueOf(mData.length));


            HashMap<String, String> map;
            for (int i = 0; i < mData.length; i++) {
                Log.d("000888", "uid: " + mData[i][0]);
                Log.d("000888", "full_name: " + mData[i][1]);
                Log.d("000888", "gender: " + mData[i][2]);
                Log.d("000888", "age: " + mData[i][2]);

                tv_record.setVisibility(View.GONE);

                map = new HashMap<>();
                map.put("full_name", "" + mData[i][1]);
                map.put("gender", "" + mData[i][2]);
                map.put("age", "" + mData[i][3]);
                map.put("data", "" + mData[i][5]);
                map.put("uid", "" + mData[i][0]);
                map.put("khandan_id", "" + mData[i][6]);
                //  map.put("mother_name", "" +"کرن اقبال");

                list.add(map);
            }

            adt = new Adt_SearchFamilyMembersList(ctx, list);
            adt.notifyDataSetChanged();
            lv.setAdapter(adt);

        } catch (Exception e) {
           tv_record.setVisibility(View.VISIBLE);
            Log.d("000888", "Error: " + e.getMessage());
            //Toast.makeText(ctx, "اس خاندان کا کوئی رکن رجسٹر نہیں.", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

//        startActivity(new Intent(ctx, Register_Activity.class));
    }
}
