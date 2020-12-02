package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_StockActivities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class  LHW_MedicinesStock_Activity extends AppCompatActivity {

    Context ctx = LHW_MedicinesStock_Activity.this;

    ListView lv;
    ImageView iv_navigation_drawer, iv_home;

    String TodayDate, login_useruid;

    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    String[][] mDataStock;
    ADT adapter;
    String type;

    Button btn_jamaa_kre;
    RelativeLayout rl_add_received, rl_sub_received, rl_add_wastage, rl_sub_wastage;
    TextView tvTotalBalance, tv_count_received, tv_count_wastage;
    int counter_receive_vac, counter_wastage_vac;
    EditText etReceived, etWastage;
    TextView tv_tablets;
    String multiple_tablets;
    long mLastClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lhw_stock);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, LHW_MedicinesStock_Activity.class));

        type = getIntent().getExtras().getString("type");

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());


        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences("UserLogin", 0); // 0 - for private mode
            String shared_useruid = prefelse.getString("login_userid", null); // getting String
            login_useruid = shared_useruid;
            Log.d("0006326", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("0006326", "Shared Err:" + e.getMessage());
        }

        //ListView
        lv = (ListView) findViewById(R.id.lv);

        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);


        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(ctx, HomePage_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Dialog_AddMedicines(position);

            }
        });

        adapter = new ADT(ctx, hashMapArrayList);
        lv.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        hashMapArrayList.clear();
        try {


            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            mDataStock = ls.executeReader("Select uid,name from MEDICINE where type ='" + type + "' ");

            HashMap<String, String> map;
            for (int i = 0; i < mDataStock.length; i++) {

                map = new HashMap<>();

                map.put("medicines_name", "" + mDataStock[i][1]);
                // map.put("received", "" + "0");

                String query = "SELECT t0.medicine_id,t0.medicine_name,t0.balance,t0.utilized,t0.received, t1.total, (t0.balance - t1.quantity - sum(t0.wastage)),max(added_on),t1.types, t1.quantity,sum(t0.wastage) from MEDICINE_STOCK t0" +
                        " INNER JOIN " +
                        " (" +
                        " SELECT medicine_id, record_data, count(*) as total,JSON_EXTRACT(metadata , '$.medicine_type') as types," +
                        " SUM(JSON_EXTRACT(metadata , '$.medicine_quantity')) as quantity FROM MEDICINE_LOG" +
                        " GROUP BY medicine_id" +
                        " ) " +
                        " t1 ON t0.medicine_id = t1.medicine_id" +
                        " where t1.medicine_id= '" + mDataStock[i][0] + "' ";


                String[][] mData_MedStock = ls.executeReader(query);
                Boolean res = ls.executeNonQuery(query);
                Log.d("000632", "Query: " + query);


                if (mData_MedStock[0][0] != null) {
                    Log.d("000632", "medicine_id: " + mData_MedStock[0][0]);
                    Log.d("000632", "medicine_name: " + mData_MedStock[0][1]);
                    Log.d("000632", "balance: " + mData_MedStock[0][2]);
                    Log.d("000632", "utilized: " + mData_MedStock[0][3]);
                    Log.d("000632", "received: " + mData_MedStock[0][4]);
                    Log.d("000632", "total: " + mData_MedStock[0][5]);
                    Log.d("000632", "total-received: " + mData_MedStock[0][6]);
                    Log.d("000632", "types: " + mData_MedStock[0][8]);
                    Log.d("000632", "quantity: " + mData_MedStock[0][9]);

                  /*   int aa=    Integer.parseInt(mData_MedStock[0][2]) -  Integer.parseInt(mData_MedStock[0][5]) * Integer.parseInt(String.valueOf(0.1));
                    Log.d("000632", "CALCULATION: " + aa);*/


                    map.put("balance", "" + mData_MedStock[0][6] + "/" + mData_MedStock[0][2]);
                    map.put("utilize", "" + mData_MedStock[0][9]);
                    map.put("wastage", "" + mData_MedStock[0][10]);


                } else {

                    Log.d("000632", "NULL: ");

                    String[][] another_query = ls.executeReader("Select balance from MEDICINE_STOCK where medicine_id ='" + mDataStock[i][0] + "' ");

                    if (another_query != null) {
                        map.put("balance", "" + "0" + "/" + another_query[0][0]);
                        map.put("utilize", "" + "0");
                        map.put("wastage", "" + "0");
                    } else {
                        map.put("balance", "" + "0" + "/" + "0");
                        map.put("utilize", "" + "0");
                        map.put("wastage", "" + "0");
                    }
                }


                hashMapArrayList.add(map);
            }

            adapter.notifyDataSetChanged();


        } catch (Exception e) {
            Log.d("000632", "Error: " + e.getMessage());
            Toast.makeText(ctx, "Something wrong !!", Toast.LENGTH_SHORT).show();
        }

    }

    public void Dialog_AddMedicines(final int pos) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.dialog_add_medicines_stocks, null);

        final AlertDialog alertD = new AlertDialog.Builder(this).create();

        ImageView iv_close = (ImageView) promptView.findViewById(R.id.iv_close);

        btn_jamaa_kre = promptView.findViewById(R.id.submit);
        tv_tablets = promptView.findViewById(R.id.tv_tablets);

        //EditText
        etReceived = promptView.findViewById(R.id.etReceived);
        etWastage = promptView.findViewById(R.id.etWastage);


        //TextView
         TextView tvWastage = promptView.findViewById(R.id.tvWastage);


        String temp = "0";


        //RelativeLayout
      /*  rl_add_received = promptView.findViewById(R.id.rl_add_received);
        rl_sub_received= promptView.findViewById(R.id.rl_sub_received);

        rl_add_wastage = promptView.findViewById(R.id.rl_add_wastage);
        rl_sub_wastage = promptView.findViewById(R.id.rl_sub_wastage);*/


        //TextView
        tvTotalBalance = promptView.findViewById(R.id.tvTotalBalance);
       /* tv_count_received = promptView.findViewById(R.id.tv_count_received);
        tv_count_wastage = promptView.findViewById(R.id.tv_count_wastage);
*/
        counter_receive_vac = 0;
        counter_wastage_vac = 0;


        alertD.setView(promptView);

        alertD.setCanceledOnTouchOutside(false);
        alertD.setCancelable(false);
        alertD.show();

        if (hashMapArrayList.get(pos).get("medicines_name").contains("Tab.")) {
            Log.d("0006326", "IFFFFF");
            temp = "1";

            etReceived.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    calculateTablets();

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else {
            Log.d("0006326", "ELSEEEEEEEEEEEEE");
            temp = "0";
        }


        tvTotalBalance.setText(hashMapArrayList.get(pos).get("balance").split("/")[0]);

        if (hashMapArrayList.get(pos).get("balance").split("/")[1].equalsIgnoreCase("0"))
        {
            Log.d("000666", "IFFF BALNCE IS 0");
            etWastage.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                etWastage.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                tvWastage.setTextColor(ctx.getResources().getColor(R.color.grey_color));
            }
        }
        else {
            Log.d("000666", "ELSE BALNCE IS N0T 0");
            etWastage.setEnabled(true);

        }

        /*etReceived.setText("0");
        etWastage.setText("0");*/

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertD.dismiss();
            }
        });

        //////////////////// Received ///////////////////////////////////

    /*    rl_add_received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter_receive_vac < 100) {
                    counter_receive_vac += 1;
                    tv_count_received.setText("" + counter_receive_vac);

                    Log.d("0006326", ": " + counter_receive_vac);
                } else {
                    tv_count_received.setText("" + counter_receive_vac);
                    Log.d("0006326", ":: " + counter_receive_vac);
                }
            }
        });
        rl_sub_received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter_receive_vac > 0) {
                    counter_receive_vac -= 1;
                    tv_count_received.setText("" + counter_receive_vac);

                    Log.d("0006326", ": " + counter_receive_vac);
                } else {
                    tv_count_received.setText("" + counter_receive_vac);
                    Log.d("0006326", ": " + counter_receive_vac);
                }
            }


        });

        ////////////////////// Wastage ////////////////////////

        rl_add_wastage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter_wastage_vac < 100) {
                    counter_wastage_vac += 1;
                    tv_count_wastage.setText("" + counter_wastage_vac);

                    Log.d("0006326", ": " + counter_wastage_vac);
                } else {
                    tv_count_wastage.setText("" + counter_wastage_vac);
                    Log.d("0006326", ":: " + counter_wastage_vac);
                }
            }
        });
        rl_sub_wastage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter_wastage_vac > 0) {
                    counter_wastage_vac -= 1;
                    tv_count_wastage.setText("" + counter_wastage_vac);
                    Log.d("0006326", ": " + counter_wastage_vac);
                } else {
                    tv_count_wastage.setText("" + counter_wastage_vac);
                    Log.d("0006326", ": " + counter_wastage_vac);
                }
            }


        });*/


        final String finalTemp = temp;
        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etReceived.getText().toString().isEmpty()) {
                    Toast.makeText(ctx, "Please enter received value.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Log.d("0006326", " mLastClickTime: " + mLastClickTime);

                if (finalTemp.equalsIgnoreCase("0")) {

                    Log.d("0006326", "TEMP IFFF");
                    multiple_tablets = etReceived.getText().toString();
                }
                else {
                    Log.d("0006326", "TEMP ELSEE");
                }



                try {

                    Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();

                    String added_on = String.valueOf(System.currentTimeMillis());

                    //int balance = Integer.parseInt(hashMapArrayList.get(pos).get("balance").split("/")[0]) + Integer.parseInt(etReceived.getText().toString());
                   // int balance = Integer.parseInt(hashMapArrayList.get(pos).get("balance").split("/")[0]) + Integer.parseInt(multiple_tablets);
                    int balance = Integer.parseInt(hashMapArrayList.get(pos).get("balance").split("/")[1]) + Integer.parseInt(multiple_tablets);

                    String wastage;
                    if (etWastage.getText().toString().isEmpty())
                    {
                        wastage = "0";
                    }
                    else
                    {
                        wastage = etWastage.getText().toString();
                    }

                    String ans1 = "insert into MEDICINE_STOCK (medicine_id, medicine_name, balance, utilized,received,wastage,record_data,added_by,is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + mDataStock[pos][0] + "'," +
                            "'" + mDataStock[pos][1] + "'," +
                            "'" + balance + "'," +
                            "'" + hashMapArrayList.get(pos).get("utilize") + "'," +
                            "'" + multiple_tablets + "'," +
                            "'" + wastage + "'," +
                            "'" + TodayDate + "'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("0006326", "Query: " + ans1);
                    Log.d("0006326", "Json Data:" + res.toString());

                    if (res.toString().equalsIgnoreCase("true"))
                    {
                        final Snackbar snackbar = Snackbar.make(v, "اسٹاک جمع ہوگیا ہے.", Snackbar.LENGTH_SHORT);
                        View mySbView = snackbar.getView();
                        mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                        TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(16);
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                        }
                        snackbar.setDuration(3000);
                        snackbar.show();

                    }
                    else {
                        final Snackbar snackbar = Snackbar.make(v, "اسٹاک جمع نہیں ہوا.", Snackbar.LENGTH_SHORT);
                        View mySbView = snackbar.getView();
                        mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                        TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(16);
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                        }
                        snackbar.setDuration(2000);
                        snackbar.show();
                    }


                } catch (Exception e) {
                    alertD.dismiss();
                    Log.d("0006326", "Error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                } finally {

                    InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if (LHW_MedicinesStock_Activity.this.getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(LHW_MedicinesStock_Activity.this.getCurrentFocus().getWindowToken(), 0);
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            alertD.dismiss();
                            Intent intent = new Intent(ctx, LHW_MedicinesStock_Activity.class);
                            intent.putExtra("type", type);
                            startActivity(intent);
                        }
                    },2500);

                }

            }
        });


    }

    private void calculateTablets() {

        try {

            if (etReceived.getText().toString().length() > 0) {
                tv_tablets.setVisibility(View.VISIBLE);
                int count = Integer.parseInt(etReceived.getText().toString());
                multiple_tablets = String.valueOf(count * 10);
                tv_tablets.setText(String.valueOf(count * 10) + " Tablets");
                Log.d("000632", "IF  : ");
            } else {
                tv_tablets.setVisibility(View.GONE);
                Log.d("000632", "ELSE  : ");
            }
        } catch (Exception e) {
            tv_tablets.setVisibility(View.GONE);
            Log.d("000632", " Error: " + e.getMessage());
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());

    }


    public class ADT extends BaseAdapter {

        Context ctx;


        ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
        LayoutInflater inflater;


        public ADT(Context ctx, ArrayList<HashMap<String, String>> hashMapArrayList) {
            this.ctx = ctx;

            this.hashMapArrayList = hashMapArrayList;
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return hashMapArrayList.size();
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

                view = inflater.inflate(R.layout.custom_lv_medicines_stocks, null);
                holder.tvMedicineName = (TextView) view.findViewById(R.id.tvMedicineName);
                holder.tvBalance = (TextView) view.findViewById(R.id.tvBalance);
                holder.tvUtilized = (TextView) view.findViewById(R.id.tvUtilized);
                holder.tvWastage = (TextView) view.findViewById(R.id.tvWastage);


                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();

            }


            holder.tvMedicineName.setText(hashMapArrayList.get(position).get("medicines_name"));
            holder.tvBalance.setText(hashMapArrayList.get(position).get("balance"));
            holder.tvUtilized.setText(hashMapArrayList.get(position).get("utilize"));
            holder.tvWastage.setText(hashMapArrayList.get(position).get("wastage"));

            return view;
        }

    }

    public static class ViewHolder {

        protected TextView tvMedicineName, tvUtilized, tvBalance,tvWastage;
    }
}



    /*   String q = "SELECT \":med_uid\" as medicine_id, record_data, count(*) FROM MBEMARI "+
                        "WHERE JSON_EXTRACT(data, \"$.sp_ishaal\") = \":med_name\" "+
                        "GROUP BY record_data " +
                        "UNION " +
                        "SELECT \":med_uid\" as medicine_id, record_data, count(*) FROM MBEMARI "+
                        "WHERE JSON_EXTRACT(data, \"$.sp_khansi_sans\") = \":med_name\" "+
                        "GROUP BY record_data " +
                        "UNION " +
                        "SELECT \":med_uid\" as medicine_id, record_data, count(*) FROM MBEMARI "+
                        "WHERE JSON_EXTRACT(data, \"$.sp_bukhar\") = \":med_name\" "+
                        "GROUP BY record_data " +
                        "UNION " +
                        "SELECT \":med_uid\" as medicine_id, record_data, count(*) FROM MBEMARI "+
                        "WHERE JSON_EXTRACT(data, \"$.sp_khoon_ki_kami\") = \":med_name\" "+
                        "GROUP BY record_data " +
                        "UNION " +
                        "SELECT \":med_uid\" as medicine_id, record_data, count(*) FROM MBEMARI "+
                        "WHERE JSON_EXTRACT(data, \"$.sp_ankhon_ki_bemaari\") = \":med_name\" "+
                        "GROUP BY record_data " +
                        "UNION " +
                        "SELECT \":med_uid\" as medicine_id, record_data, count(*) FROM MBEMARI "+
                        "WHERE JSON_EXTRACT(data, \"$.sp_jins_zanana_amraz\") = \":med_name\" "+
                        "GROUP BY record_data "+
                        "UNION " +
                        "SELECT \":med_uid\" as medicine_id, record_data, count(*) FROM MBEMARI "+
                        "WHERE JSON_EXTRACT(data, \"$.sp_malaria\") = \":med_name\" "+
                        "GROUP BY record_data " +
                        "UNION " +
                        "SELECT \":med_uid\" as medicine_id, record_data, count(*) FROM MBEMARI "+
                        "WHERE JSON_EXTRACT(data, \"$.sp_mumkinatb\") = \":med_name\" "+
                        "GROUP BY record_data "+
                        "UNION " +
                        "SELECT \":med_uid\" as medicine_id, record_data, count(*) FROM MBEMARI "+
                        "WHERE JSON_EXTRACT(data, \"$.sp_tashkhees\") = \":med_name\" "+
                        "GROUP BY record_data " +
                        "UNION " +
                        "SELECT \":med_uid\" as medicine_id, record_data, count(*) FROM MBEMARI "+
                        "WHERE JSON_EXTRACT(data, \"$.sp_muawnat\") = \":med_name\" "+
                        "GROUP BY record_data "+
                        "UNION " +
                        "SELECT \":med_uid\" as medicine_id, record_data, count(*) FROM MBEMARI "+
                        "WHERE JSON_EXTRACT(data, \"$.sp_elaaj\") = \":med_name\" "+
                        "GROUP BY record_data " ;*/