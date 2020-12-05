package com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_VideoActivities.VideoList_Activity;
import com.akdndhrc.covid_module.ServiceLocation;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.VAC_App.HomePageVacinator_Activity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class VAC_StockActivity extends AppCompatActivity {

    Context ctx = VAC_StockActivity.this;

    ListView lv;
    ImageView iv_navigation_drawer, iv_home;

    String TodayDate, login_useruid;
    double latitude;
    double longitude;
    ServiceLocation serviceLocation;


    final ArrayList<String> arrayList_VaccineNames = new ArrayList<>();

    Button btn_jamaa_kre;
    RelativeLayout rl_add_received_vac, rl_sub_received_vac, rl_add_wastage_vac, rl_sub_wastage_vac, rl_add_return_vac, rl_sub_return_vac;
    TextView tvTotalBalance, tv_count_return_vac, tv_count_wastage_vac, tv_count_received_vac;
    int counter_receive_vac, counter_wastage_vac, counter_return_vac;

    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    ADT adapter;
    int total_balancedoses, received_vials, utilized_vials;
    String[][] mData_VaccineStock;

    EditText etReceived, etWastage, etReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vac__stock);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, VideoList_Activity.class));


        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());


        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
            String shared_useruid = prefelse.getString((R.string.loginUserIDEng), null); // getting String
            login_useruid = shared_useruid;
            Log.d("123456", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("123456", "Shared Err:" + e.getMessage());
        }


        //ListView
        lv = (ListView) findViewById(R.id.lv);

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        //  iv_home.setVisibility(View.GONE);


        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(ctx, HomePageVacinator_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
            }
        });


        adapter = new ADT(VAC_StockActivity.this, arrayList_VaccineNames, hashMapArrayList);
        lv.setAdapter(adapter);


        read_data();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mData_VaccineStock.equals("null")) {
                    Toast.makeText(ctx, "NULLL", Toast.LENGTH_SHORT).show();
                } else {
                    Dialog_MaleFemaleCount(position);
                }

            }
        });


    }


    private void read_data() {

        arrayList_VaccineNames.clear();

        try {
            arrayList_VaccineNames.add("BCG" + "@" + "20" + "@" + "c943f19af2bb57d3");
            arrayList_VaccineNames.add("HEP-B" + "@" + "2" + "@" + "c943f32a378068b8");
            arrayList_VaccineNames.add("OPV" + "@" + "20" + "@" + "c943f47ebbcf238e");
            arrayList_VaccineNames.add("IPV" + "@" + "10" + "@" + "c943f5beaf261e80");
            arrayList_VaccineNames.add("Rota" + "@" + "1" + "@" + "c943f78a5e633d7e");
            arrayList_VaccineNames.add("PCVIO" + "@" + "4" + "@" + "c943f8de129ec8f7");
            arrayList_VaccineNames.add("Penta" + "@" + "1" + "@" + "c943fa1eadf7b207");
            arrayList_VaccineNames.add("Measles" + "@" + "10" + "@" + "b7ebd2184a08e8d9");
            arrayList_VaccineNames.add("Tetanus" + "@" + "20" + "@" + "b7ebd498c54dadd5");
            arrayList_VaccineNames.add("0.05 ML Syring (BCG)" + "@" + "1" + "@" + "b7ebd5f6ce0ed965"); //new + open
            arrayList_VaccineNames.add(".5 ML Syring" + "@" + "1" + "@" + "b7ebd72c8bbbbd9d");  //overall
            arrayList_VaccineNames.add("2 ML Syring (BCG)" + "@" + "1" + "@" + "b7ebdc68e83145a9"); //new vile only
            arrayList_VaccineNames.add("5 ML Syring (Measles)" + "@" + "1" + "@" + "b7ebdda8609d4131");//meas
            arrayList_VaccineNames.add("Saftey Box" + "@" + "1" + "@" + "5a74cea4517611ea");//meas
            arrayList_VaccineNames.add("Vaccine Cards" + "@" + "1" + "@" + "5a74d0fc517611ea");//meas


            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            Log.d("123456", "Stock Err:  " + e.getMessage());
        }

    }

    public void Dialog_MaleFemaleCount(final int pos) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.dialog_add_stocks, null);

        final AlertDialog alertD = new AlertDialog.Builder(this).create();

        ImageView iv_close = (ImageView) promptView.findViewById(R.id.iv_close);

        btn_jamaa_kre = promptView.findViewById(R.id.submit);


        //TextView
        tvTotalBalance = promptView.findViewById(R.id.tvTotalBalance);

        //EditText
        etReceived = promptView.findViewById(R.id.etReceived);
        etWastage = promptView.findViewById(R.id.etWastage);
        etReturn = promptView.findViewById(R.id.etReturn);

        counter_receive_vac = 0;
        counter_wastage_vac = 0;
        counter_return_vac = 0;


        try {

            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            String[][] mData_VaccineStock = ls.executeReader("SELECT vaccine_name,received,wastage,return,balance from VACCINE_STOCK where vaccine_name ='" + arrayList_VaccineNames.get(pos).split("@")[0] + "'");

            if (mData_VaccineStock != null) {
                Log.d("000159", "vaccine_name STOCK: " + mData_VaccineStock[0][0]);
                Log.d("000159", "received vaccine STOCk: " + mData_VaccineStock[0][1]);
                Log.d("000159", "wastage vaccine STOCk: " + mData_VaccineStock[0][2]);
                Log.d("000159", "return vaccine STOCk: " + mData_VaccineStock[0][3]);
                Log.d("000159", "balance vaccine STOCk: " + mData_VaccineStock[0][4]);


                tvTotalBalance.setText(hashMapArrayList.get(pos).get("received_vials") + " vials / " + hashMapArrayList.get(pos).get("balance_doses") + " doses");
                //tv_count_received_vac.setText(mData_VaccineStock[0][1]);
                // etReceived.setText(mData_VaccineStock[0][1]);

                //  tv_count_wastage_vac.setText(mData_VaccineStock[0][2]);
                //etWastage.setText(mData_VaccineStock[0][2]);

                // tv_count_return_vac.setText(mData_VaccineStock[0][3]);
                //   etReturn.setText(mData_VaccineStock[0][3]);

                // btn_jamaa_kre.setVisibility(View.GONE);

            } else {
                Toast.makeText(ctx, "No Previous stock found", Toast.LENGTH_SHORT).show();

                Log.d("000159", "ELSEEE: ");
                tvTotalBalance.setText("0");
                /*tv_count_received_vac.setText("0");
                tv_count_wastage_vac.setText("0");
                tv_count_return_vac.setText("0");*/


            }
        } catch (Exception e) {
            Log.d("000159", "ERRRRRRRRRR: " + e.getMessage());
        }


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


        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etReceived.getText().toString().isEmpty())
                {
                    Toast.makeText(ctx, "Enter received number.", Toast.LENGTH_SHORT).show();
                    return;
                }


                try {

                    Lister ls = new Lister(VAC_StockActivity.this);
                    ls.createAndOpenDB();

                    String etRec,etWas,etRetrn;

                    String added_on = String.valueOf(System.currentTimeMillis());


                    if (etReceived.getText().toString().isEmpty()) {
                        etRec = "0";
                    } else {
                        etRec = etReceived.getText().toString();
                    }
                      //int balance = Integer.parseInt(tv_count_received_vac.getText().toString()) * Integer.parseInt(arrayList_VaccineNames.get(pos).split("@")[1]);
                      int balance = Integer.parseInt(etRec) * Integer.parseInt(arrayList_VaccineNames.get(pos).split("@")[1]);


                    if (etWastage.getText().toString().isEmpty()) {
                        etWas = "0";
                    }
                    else {
                        etWas= etWastage.getText().toString();
                    }

                    if (etReturn.getText().toString().isEmpty()) {
                        etRetrn = "0";
                    }
                    else {
                        etRetrn= etReturn.getText().toString();
                    }

                    String ans1 = "insert into VACCINE_STOCK (vaccine_id, vaccine_name, balance, utilized,received,wastage,return,record_data,added_by,is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + arrayList_VaccineNames.get(pos).split("@")[2] + "'," +
                            "'" + arrayList_VaccineNames.get(pos).split("@")[0] + "'," +
                            "'" + balance + "'," +
                            "'" + arrayList_VaccineNames.get(pos).split("@")[1] + "'," +
                            "'" + etRec + "'," +
                            "'" + etWas + "'," +
                            "'" + etRetrn+ "'," +
                            "'" + TodayDate + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("123456", "Query: " + ans1);
                    Log.d("123456", "Json Data:" + res.toString());

                    Toast.makeText(ctx, "Vaccine stock Successfully Added", Toast.LENGTH_SHORT).show();


                } catch (Exception e) {
                    alertD.dismiss();
                    Log.d("123456", "Error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                } finally {
                    alertD.dismiss();
                    Intent intent = new Intent(ctx, VAC_StockActivity.class);
                    startActivity(intent);

                }


            }
        });


    }


//    public void Dialog_MaleFemaleCount(final int pos) {
//
//        LayoutInflater layoutInflater = LayoutInflater.from(this);
//        View promptView = layoutInflater.inflate(R.layout.dialog_add_stocks, null);
//
//        final AlertDialog alertD = new AlertDialog.Builder(this).create();
//
//        ImageView iv_close = (ImageView) promptView.findViewById(R.id.iv_close);
//
//        btn_jamaa_kre = promptView.findViewById(R.id.submit);
//
//
//        //RelativeLayout
//      /*  rl_add_received_vac = promptView.findViewById(R.id.rl_add_received_vac);
//        rl_sub_received_vac = promptView.findViewById(R.id.rl_sub_received_vac);
//
//        rl_add_wastage_vac = promptView.findViewById(R.id.rl_add_wastage_vac);
//        rl_sub_wastage_vac = promptView.findViewById(R.id.rl_sub_wastage_vac);
//
//
//        rl_add_return_vac = promptView.findViewById(R.id.rl_add_return_vac);
//        rl_sub_return_vac = promptView.findViewById(R.id.rl_sub_return_vac);
//
//        //TextView
//        tvTotalBalance = promptView.findViewById(R.id.tvTotalBalance);
//     //   tv_count_received_vac = promptView.findViewById(R.id.tv_count_received_vac);
//        tv_count_wastage_vac = promptView.findViewById(R.id.tv_count_wastage_vac);
//        tv_count_return_vac = promptView.findViewById(R.id.tv_count_return_vac);*/
//
//
//        //TextView
//        tvTotalBalance = promptView.findViewById(R.id.tvTotalBalance);
//
//        counter_receive_vac = 0;
//        counter_wastage_vac = 0;
//        counter_return_vac = 0;
//
//
//       /* tv_count_received_vac.setText("" + counter_receive_vac);
//        tv_count_wastage_vac.setText("" + counter_wastage_vac);
//        tv_count_return_vac.setText("" + counter_return_vac);
//        tvTotalBalance.setText("0");*/
//
//
//        try {
//
//            Lister ls = new Lister(ctx);
//            ls.createAndOpenDB();
//
//            String[][] mData_VaccineStock = ls.executeReader("SELECT vaccine_name,received,wastage,return,balance from VACCINE_STOCK where vaccine_name ='" + arrayList_VaccineNames.get(pos).split("@")[0] + "'");
//
//            if (mData_VaccineStock != null) {
//                Log.d("000159", "vaccine_name STOCK: " + mData_VaccineStock[0][0]);
//                Log.d("000159", "received vaccine STOCk: " + mData_VaccineStock[0][1]);
//                Log.d("000159", "wastage vaccine STOCk: " + mData_VaccineStock[0][2]);
//                Log.d("000159", "return vaccine STOCk: " + mData_VaccineStock[0][3]);
//                Log.d("000159", "balance vaccine STOCk: " + mData_VaccineStock[0][4]);
//
////                int numa = Integer.parseInt(mData_VaccineStock[0][1]);
////                float numb = Float.parseFloat("0.1");
////                float ans = (numa - numb);
////
////                Toast.makeText(ctx, ""+ans, Toast.LENGTH_SHORT).show();
//
//
//                tvTotalBalance.setText(hashMapArrayList.get(pos).get("received_vials") + " vials / " + hashMapArrayList.get(pos).get("balance_doses") + " doses");
//                tv_count_received_vac.setText(mData_VaccineStock[0][1]);
//                tv_count_wastage_vac.setText(mData_VaccineStock[0][2]);
//                tv_count_return_vac.setText(mData_VaccineStock[0][3]);
//                rl_add_received_vac.setEnabled(false);
//                rl_sub_received_vac.setEnabled(false);
//                rl_add_wastage_vac.setEnabled(false);
//                rl_sub_wastage_vac.setEnabled(false);
//                rl_add_return_vac.setEnabled(false);
//                rl_sub_return_vac.setEnabled(false);
//                btn_jamaa_kre.setVisibility(View.GONE);
//
//            } else {
//                Toast.makeText(ctx, "No Previous stock found", Toast.LENGTH_SHORT).show();
//
//                Log.d("000159", "ELSEEE: ");
//                tvTotalBalance.setText("0");
//                tv_count_received_vac.setText("0");
//                tv_count_wastage_vac.setText("0");
//                tv_count_return_vac.setText("0");
//
//
//            }
//        } catch (Exception e) {
//            Log.d("000159", "ERRRRRRRRRR: " + e.getMessage());
//        }
//
//
//        alertD.setView(promptView);
//
//        alertD.setCanceledOnTouchOutside(false);
//        alertD.setCancelable(false);
//        alertD.show();
//
//        iv_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertD.dismiss();
//            }
//        });
//
//        //////////////////// Received ///////////////////////////////////
//
//        rl_add_received_vac.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (counter_receive_vac < 100) {
//                    counter_receive_vac += 1;
//                    tv_count_received_vac.setText("" + counter_receive_vac);
//
//                    int number_of_doses = counter_receive_vac * Integer.parseInt(arrayList_VaccineNames.get(pos).split("@")[1]);
//                    tvTotalBalance.setText(counter_receive_vac + " vials / " +number_of_doses + " doses");
//
//                    Log.d("123456", ": " + counter_receive_vac);
//                } else {
//                    tv_count_received_vac.setText("" + counter_receive_vac);
//                    Log.d("123456", ":: " + counter_receive_vac);
//                }
//            }
//        });
//        rl_sub_received_vac.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (counter_receive_vac > 0) {
//                    counter_receive_vac -= 1;
//                    tv_count_received_vac.setText("" + counter_receive_vac);
//
//                    int number_of_doses = counter_receive_vac * Integer.parseInt(arrayList_VaccineNames.get(pos).split("@")[1]);
//                    tvTotalBalance.setText(counter_receive_vac + " vials / " +number_of_doses + " doses");
//
//
//                    Log.d("123456", ": " + counter_receive_vac);
//                } else {
//                    tv_count_received_vac.setText("" + counter_receive_vac);
//                    Log.d("123456", ": " + counter_receive_vac);
//                }
//            }
//
//
//        });
//
//        ////////////////////// Wastage ////////////////////////
//
//        rl_add_wastage_vac.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (counter_wastage_vac < 100) {
//                    counter_wastage_vac += 1;
//                    tv_count_wastage_vac.setText("" + counter_wastage_vac);
//
//                    Log.d("123456", ": " + counter_wastage_vac);
//                } else {
//                    tv_count_wastage_vac.setText("" + counter_wastage_vac);
//                    Log.d("123456", ":: " + counter_wastage_vac);
//                }
//            }
//        });
//        rl_sub_wastage_vac.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (counter_wastage_vac > 0) {
//                    counter_wastage_vac -= 1;
//                    tv_count_wastage_vac.setText("" + counter_wastage_vac);
//                    Log.d("123456", ": " + counter_wastage_vac);
//                } else {
//                    tv_count_wastage_vac.setText("" + counter_wastage_vac);
//                    Log.d("123456", ": " + counter_wastage_vac);
//                }
//            }
//
//
//        });
//
//        /////////////////////// Return ///////////////////////
//        rl_add_return_vac.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (counter_return_vac < 100) {
//                    counter_return_vac += 1;
//                    tv_count_return_vac.setText("" + counter_return_vac);
//
//                    Log.d("123456", ": " + counter_return_vac);
//                } else {
//                    tv_count_return_vac.setText("" + counter_return_vac);
//                    Log.d("123456", ":: " + counter_return_vac);
//                }
//            }
//        });
//
//        rl_sub_return_vac.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (counter_return_vac > 0) {
//                    counter_return_vac -= 1;
//                    tv_count_return_vac.setText("" + counter_return_vac);
//                    Log.d("123456", ": " + counter_return_vac);
//                } else {
//                    tv_count_return_vac.setText("" + counter_return_vac);
//                    Log.d("123456", ": " + counter_return_vac);
//                }
//            }
//
//
//        });
//
//
//        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                try {
//
//                    Lister ls = new Lister(VAC_StockActivity.this);
//                    ls.createAndOpenDB();
//
//
//                    String added_on = String.valueOf(System.currentTimeMillis());
//
//                    int balance = Integer.parseInt(tv_count_received_vac.getText().toString()) * Integer.parseInt(arrayList_VaccineNames.get(pos).split("@")[1]);
//
//                    String ans1 = "insert into VACCINE_STOCK (vaccine_id, vaccine_name, balance, utilized,received,wastage,return,record_data,added_by,added_on)" +
//                            "values" +
//                            "(" +
//                            "'" + pos + "'," +
//                            "'" + arrayList_VaccineNames.get(pos).split("@")[0] + "'," +
//                            "'" + balance + "'," +
//                            "'" + arrayList_VaccineNames.get(pos).split("@")[1] + "'," +
//                            "'" + tv_count_received_vac.getText().toString() + "'," +
//                            "'" + tv_count_wastage_vac.getText().toString() + "'," +
//                            "'" + tv_count_return_vac.getText().toString() + "'," +
//                            "'" + TodayDate + "'," +
//                            "'" + login_useruid + "'," +
//                            "'" + added_on + "'" +
//                            ")";
//
//                    Boolean res = ls.executeNonQuery(ans1);
//                    Log.d("123456", "Query: " + ans1);
//                    Log.d("123456", "Json Data:" + res.toString());
//
//                    Toast.makeText(ctx, "Vaccine stock Successfully Added", Toast.LENGTH_SHORT).show();
//
//
//                } catch (Exception e) {
//                    alertD.dismiss();
//                    Log.d("123456", "Error: " + e.getMessage());
//                    Toast.makeText(getApplicationContext(), "Something wrong!", Toast.LENGTH_SHORT).show();
//                } finally {
//                    alertD.dismiss();
//                    Intent intent = new Intent(ctx, VAC_StockActivity.class);
//                    startActivity(intent);
//
//                }
//
//
//            }
//        });
//
//
//    }



    @Override
    protected void onResume() {
        super.onResume();

        hashMapArrayList.clear();


        try {


            Lister ls = new Lister(VAC_StockActivity.this);
            ls.createAndOpenDB();

            HashMap<String, String> map;
            for (int i = 0; i < arrayList_VaccineNames.size(); i++) {

                received_vials = 0;
                total_balancedoses = 0;
                utilized_vials = 0;


                map = new HashMap<>();


                Log.d("123456", "ARRAY SIZE LoOP :  " + i);

                mData_VaccineStock = ls.executeReader("SELECT max(added_on),vaccine_name,received from VACCINE_STOCK where vaccine_name ='" + arrayList_VaccineNames.get(i).split("@")[0] + "'");

                if (mData_VaccineStock[0][0] != null) {

                    Log.d("000159", "vaccine_name STOCK: " + mData_VaccineStock[0][1]);
                    Log.d("000159", "received vaccine STOCk: " + mData_VaccineStock[0][2]);

                    received_vials = Integer.parseInt(mData_VaccineStock[0][2]);
                    total_balancedoses = Integer.parseInt(mData_VaccineStock[0][2]) * Integer.parseInt(arrayList_VaccineNames.get(i).split("@")[1]);

                    String vac_name;

                    if (i == 8 || i == 10) {
                        vac_name = "BCG";
                    } else {
                        vac_name = mData_VaccineStock[0][1];
                    }

                    String[][] temp_data = ls.executeReader("SELECT count(*) from CVACCINE_VIALS where vaccine_name LIKE  '%" + vac_name + "%'");
                    if (Integer.parseInt(temp_data[0][0]) > 0) {
                        Log.d("000159", "vaccine_name : " + mData_VaccineStock[0][1]);
                        Log.d("000159", "COUNT: " + temp_data[0][0]);

                        utilized_vials = Integer.parseInt(temp_data[0][0]);
                        total_balancedoses = total_balancedoses - Integer.parseInt(temp_data[0][0]);

                     /*   int num1 = (Integer.parseInt(mData_VaccineStock[0][1]));
                        double num2 = 0.1;
                       // int b = (int) (num1 + num2);

                        //   float final_ans = (num1 - num2);
                        Log.d("000159", "ANSSSSS: " +Math.ceil(num2*5));*/


                        int temp = (Integer.parseInt(mData_VaccineStock[0][2]));
                        int temp2 = (Integer.parseInt(temp_data[0][0]));
                        BigDecimal num1 = BigDecimal.valueOf(temp - 0.1 * temp2);
                        // BigDecimal num2 = BigDecimal.valueOf(0.1);
                        // BigDecimal res = num1.subtract(num2).multiply(num1);       // 5 - 0.1

                        //  double intResult = num1.intValue();
                        //Log.d("000159", "ANSSSSS: " +num1);
                        //Log.d("000159", "ANSSSSS: " +intResult);

                        double doubleResult = num1.doubleValue();

                        Log.d("000159", "ANSSSSS: " + doubleResult);


                        map.put("received_vials", "" + doubleResult);
                        map.put("balance_doses", "" + total_balancedoses);
                        map.put("utilized_vials", "" + utilized_vials);


                    } else {
                        Log.d("000159", "ANOTHER NULL: ");

                        map.put("received_vials", "" + received_vials);
                        map.put("balance_doses", "" + total_balancedoses);
                        map.put("utilized_vials", "" + utilized_vials);
                    }
                } else {
                    Log.d("000159", "NULLLLL: " + i);
                    map.put("received_vials", "" + "0");
                    map.put("balance_doses", "" + "0");
                    map.put("utilized_vials", "" + "0");
                }
                hashMapArrayList.add(map);
            }

            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            Log.d("12345", "Error: " + e.getMessage());
        }


    }


    /*@Override
    protected void onResume() {
        super.onResume();

        hashMapArrayList.clear();


        try {


            Lister ls = new Lister(VAC_StockActivity.this);
            ls.createAndOpenDB();

            HashMap<String, String> map;
            for (int i = 0; i < arrayList_VaccineNames.size(); i++) {

                received_vials = 0;
                total_balancedoses = 0;
                utilized_vials = 0;


                map = new HashMap<>();


                Log.d("123456", "ARRAY SIZE LoOP :  " + i);

                mData_VaccineStock = ls.executeReader("SELECT max(added_on),vaccine_name,received from VACCINE_STOCK where vaccine_name ='" + arrayList_VaccineNames.get(i).split("@")[0] + "'");

                if (mData_VaccineStock[0][0] != null) {

                    Log.d("000159", "vaccine_name STOCK: " + mData_VaccineStock[0][1]);
                    Log.d("000159", "received vaccine STOCk: " + mData_VaccineStock[0][2]);

                    received_vials = Integer.parseInt(mData_VaccineStock[0][2]);
                    total_balancedoses = Integer.parseInt(mData_VaccineStock[0][2]) * Integer.parseInt(arrayList_VaccineNames.get(i).split("@")[1]);

                    String vac_name;

                    if (i == 8 || i == 10) {
                        vac_name = "BCG";
                    } else {
                        vac_name = mData_VaccineStock[0][1];
                    }

                    String[][] temp_data = ls.executeReader("SELECT count(*) from CVACCINE_VIALS where vaccine_name LIKE  '%" + vac_name + "%'");
                    if (Integer.parseInt(temp_data[0][0]) > 0) {
                        Log.d("000159", "vaccine_name : " + mData_VaccineStock[0][1]);
                        Log.d("000159", "COUNT: " + temp_data[0][0]);

                        utilized_vials = Integer.parseInt(temp_data[0][0]);
                        total_balancedoses = total_balancedoses - Integer.parseInt(temp_data[0][0]);

                     *//*   int num1 = (Integer.parseInt(mData_VaccineStock[0][1]));
                        double num2 = 0.1;
                       // int b = (int) (num1 + num2);

                        //   float final_ans = (num1 - num2);
                        Log.d("000159", "ANSSSSS: " +Math.ceil(num2*5));*//*


                        int temp = (Integer.parseInt(mData_VaccineStock[0][2]));
                        int temp2 = (Integer.parseInt(temp_data[0][0]));
                        BigDecimal num1 = BigDecimal.valueOf(temp - 0.1 * temp2);
                        // BigDecimal num2 = BigDecimal.valueOf(0.1);
                        // BigDecimal res = num1.subtract(num2).multiply(num1);       // 5 - 0.1

                        //  double intResult = num1.intValue();
                        //Log.d("000159", "ANSSSSS: " +num1);
                        //Log.d("000159", "ANSSSSS: " +intResult);

                        double doubleResult = num1.doubleValue();

                        Log.d("000159", "ANSSSSS: " + doubleResult);


                        map.put("received_vials", "" + doubleResult);
                        map.put("balance_doses", "" + total_balancedoses);
                        map.put("utilized_vials", "" + utilized_vials);


                    } else {
                        Log.d("000159", "ANOTHER NULL: ");

                        map.put("received_vials", "" + received_vials);
                        map.put("balance_doses", "" + total_balancedoses);
                        map.put("utilized_vials", "" + utilized_vials);
                    }
                } else {
                    Log.d("000159", "NULLLLL: " + i);
                    map.put("received_vials", "" + "0");
                    map.put("balance_doses", "" + "0");
                    map.put("utilized_vials", "" + "0");
                }
                hashMapArrayList.add(map);
            }

            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            Log.d("12345", "Error: " + e.getMessage());
        }


    }*/




    public class ADT extends BaseAdapter {

        Context ctx;
        ArrayList<String> arrayList_VaccineNames;

        ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
        LayoutInflater inflater;


        public ADT(Context ctx, ArrayList<String> arrayList_VaccineNames, ArrayList<HashMap<String, String>> hashMapArrayList) {
            this.ctx = ctx;
            this.arrayList_VaccineNames = arrayList_VaccineNames;
            this.hashMapArrayList = hashMapArrayList;
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return arrayList_VaccineNames.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(final int position, View view, ViewGroup parent) {

            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();

                view = inflater.inflate(R.layout.custom_lv_vaccine_stocks, null);
                holder.tvVaccineName = (TextView) view.findViewById(R.id.tvVaccineName);
                holder.tvBalance = (TextView) view.findViewById(R.id.tvBalance);
                holder.tvUtilized = (TextView) view.findViewById(R.id.tvUtilized);
                holder.tvDosesPerUnit = (TextView) view.findViewById(R.id.tvDosesPerUnit);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();

            }


            holder.tvVaccineName.setText(arrayList_VaccineNames.get(position).split("@")[0]);
            holder.tvUtilized.setText(hashMapArrayList.get(position).get("utilized_vials"));
            holder.tvDosesPerUnit.setText("Doses per unit: " + arrayList_VaccineNames.get(position).split("@")[1]);

            if (position == 8 || position == 9 || position == 10 || position == 11) {
                holder.tvBalance.setText(hashMapArrayList.get(position).get("balance_doses") + " doses");
            }
            /*else   if (position == 9)
            {
                holder.tvBalance.setText(hashMapArrayList.get(position).get("balance_doses") + " doses");
            }
            else    if (position == 10)
            {
                holder.tvBalance.setText(hashMapArrayList.get(position).get("balance_doses") + " doses");
            }
            else   if (position ==11)
            {
                holder.tvBalance.setText(hashMapArrayList.get(position).get("balance_doses") + " doses");

            }*/
            else {
                holder.tvBalance.setText(hashMapArrayList.get(position).get("received_vials") + " vials " + " / " + hashMapArrayList.get(position).get("balance_doses") + " doses");

            }
            return view;
        }

    }

    public static class ViewHolder {

        protected TextView tvVaccineName, tvUtilized, tvBalance, tvDosesPerUnit;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());

    }


    @Override
    public void onBackPressed() {
        Intent newIntent = new Intent(ctx, HomePageVacinator_Activity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
    }
}