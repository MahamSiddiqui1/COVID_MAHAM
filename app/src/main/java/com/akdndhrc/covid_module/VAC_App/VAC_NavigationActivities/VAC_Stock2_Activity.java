package com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class VAC_Stock2_Activity extends AppCompatActivity {

    Context ctx = VAC_Stock2_Activity.this;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vac_stock_2);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, VideoList_Activity.class));


        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());


        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
            String shared_useruid = prefelse.getString(("login_userid"), null); // getting String
            login_useruid = shared_useruid;
            Log.d("123456", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("123456", "Shared Err:" + e.getMessage());
        }


        try {
            serviceLocation = new ServiceLocation(ctx);
            serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            serviceLocation.callAsynchronousTask();
        } catch (Exception e) {
            Log.d("123456", "GPS Service Err:  " + e.getMessage());
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


        adapter = new ADT(VAC_Stock2_Activity.this, arrayList_VaccineNames,hashMapArrayList);
        lv.setAdapter(adapter);


        read_data();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog_MaleFemaleCount(position);
            }
        });

    }

    private void read_data() {

        arrayList_VaccineNames.clear();

        try {
            arrayList_VaccineNames.add("BCG" + "@" + "20");
            arrayList_VaccineNames.add("OPV"+ "@" + "20");
            arrayList_VaccineNames.add("IPV"+ "@" + "10");
            arrayList_VaccineNames.add("Rota"+ "@" + "1");
            arrayList_VaccineNames.add("PCV1O"+ "@" + "4");
            arrayList_VaccineNames.add("Penta"+ "@" + "1");
            arrayList_VaccineNames.add("Measles"+ "@" + "10");
            arrayList_VaccineNames.add("Tetanus"+ "@" + "20");
            arrayList_VaccineNames.add(".05 ML Syring (BCG)"+ "@" + "1");
            arrayList_VaccineNames.add(".5 ML Syring"+ "@" + "1");
            arrayList_VaccineNames.add("2 ML Syring (BCG)"+ "@" + "1");
            arrayList_VaccineNames.add("5 ML Syring (OPV,IPV,Measles,PCVIO,Tetanus)"+ "@" + "1");


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


        //RelativeLayout
        /*rl_add_received_vac = promptView.findViewById(R.id.rl_add_received_vac);
        rl_sub_received_vac = promptView.findViewById(R.id.rl_sub_received_vac);

        rl_add_wastage_vac = promptView.findViewById(R.id.rl_add_wastage_vac);
        rl_sub_wastage_vac = promptView.findViewById(R.id.rl_sub_wastage_vac);


        rl_add_return_vac = promptView.findViewById(R.id.rl_add_return_vac);
        rl_sub_return_vac = promptView.findViewById(R.id.rl_sub_return_vac);

        //TextView
        tvTotalBalance = promptView.findViewById(R.id.tvTotalBalance);
        tv_count_received_vac = promptView.findViewById(R.id.tv_count_received_vac);
        tv_count_wastage_vac = promptView.findViewById(R.id.tv_count_wastage_vac);
        tv_count_return_vac = promptView.findViewById(R.id.tv_count_return_vac);*/


        counter_receive_vac = Integer.parseInt("0");
        counter_wastage_vac = Integer.parseInt("0");
        counter_return_vac = Integer.parseInt("0");


        tv_count_received_vac.setText("" + counter_receive_vac);
        tv_count_wastage_vac.setText("" + counter_wastage_vac);
        tv_count_return_vac.setText("" + counter_return_vac);
        tvTotalBalance.setText("0");


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

        //////////////////// Received ///////////////////////////////////

        rl_add_received_vac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter_receive_vac < 100) {
                    counter_receive_vac += 1;
                    tv_count_received_vac.setText("" + counter_receive_vac);

                    Log.d("123456", ": " + counter_receive_vac);
                } else {
                    tv_count_received_vac.setText("" + counter_receive_vac);
                    Log.d("123456", ":: " + counter_receive_vac);
                }
            }
        });
        rl_sub_received_vac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter_receive_vac > 0) {
                    counter_receive_vac -= 1;
                    tv_count_received_vac.setText("" + counter_receive_vac);
                    Log.d("123456", ": " + counter_receive_vac);
                } else {
                    tv_count_received_vac.setText("" + counter_receive_vac);
                    Log.d("123456", ": " + counter_receive_vac);
                }
            }


        });

        ////////////////////// Wastage ////////////////////////

        rl_add_wastage_vac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter_wastage_vac < 100) {
                    counter_wastage_vac += 1;
                    tv_count_wastage_vac.setText("" + counter_wastage_vac);

                    Log.d("123456", ": " + counter_wastage_vac);
                } else {
                    tv_count_wastage_vac.setText("" + counter_wastage_vac);
                    Log.d("123456", ":: " + counter_wastage_vac);
                }
            }
        });
        rl_sub_wastage_vac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter_wastage_vac > 0) {
                    counter_wastage_vac -= 1;
                    tv_count_wastage_vac.setText("" + counter_wastage_vac);
                    Log.d("123456", ": " + counter_wastage_vac);
                } else {
                    tv_count_wastage_vac.setText("" + counter_wastage_vac);
                    Log.d("123456", ": " + counter_wastage_vac);
                }
            }


        });

        /////////////////////// Return ///////////////////////
        rl_add_return_vac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter_return_vac < 100) {
                    counter_return_vac += 1;
                    tv_count_return_vac.setText("" + counter_return_vac);

                    Log.d("123456", ": " + counter_return_vac);
                } else {
                    tv_count_return_vac.setText("" + counter_return_vac);
                    Log.d("123456", ":: " + counter_return_vac);
                }
            }
        });

        rl_sub_return_vac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter_return_vac > 0) {
                    counter_return_vac -= 1;
                    tv_count_return_vac.setText("" + counter_return_vac);
                    Log.d("123456", ": " + counter_return_vac);
                } else {
                    tv_count_return_vac.setText("" + counter_return_vac);
                    Log.d("123456", ": " + counter_return_vac);
                }
            }


        });


        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {

                    Lister ls = new Lister(VAC_Stock2_Activity.this);
                    ls.createAndOpenDB();


                    String added_on = String.valueOf(System.currentTimeMillis());

                    int balance = Integer.parseInt(tv_count_received_vac.getText().toString()) * Integer.parseInt(arrayList_VaccineNames.get(pos).split("@")[1]);

                    String ans1 = "insert into VACCINE_STOCK (vaccine_id, vaccine_name, balance, utilized,received,wastage,return,record_data,added_by,is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + pos + "'," +
                            "'" + arrayList_VaccineNames.get(pos) + "'," +
                            "'" +balance + "'," +
                            "'" + "0" + "'," +
                            "'" + tv_count_received_vac.getText().toString() + "'," +
                            "'" + tv_count_wastage_vac.getText().toString() + "'," +
                            "'" + tv_count_return_vac.getText().toString() + "'," +
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
                }


            }
        });


    }



    @Override
    protected void onResume() {
        super.onResume();

        hashMapArrayList.clear();
        try {


            Lister ls = new Lister(VAC_Stock2_Activity.this);
            ls.createAndOpenDB();

           // for (int i = 0; i < arrayList_VaccineNames.size(); i++) {
              //  Log.d("123456", "ARRAY SIZE LoOP :  " +i);

                String[][] mData = ls.executeReader("SELECT tx.*, used.open_vials as utilized FROM" +
                        " VACCINE_STOCK as tx" +
                        " LEFT JOIN" +
                        " (" +
                        " SELECT" +
                        " substr(" +
                        " t1.name, 0," +
                        " CASE WHEN instr(t1.name, '-') = 0" +
                        " THEN length(t1.name)+1" +
                        " ELSE instr(t1.name, '-')" +
                        " END" +
                        " ) as vac_vial," +
                        " SUM(t0.type) as open_vials" +
                        " FROM CVACCINE_VIALS as t0" +
                        " JOIN VACCINES as t1 ON t0.vaccine_id = t1.uid" +
                        " GROUP BY vac_vial, type" +
                        " ) as used ON used.vac_vial = tx.vaccine_name");

                if (mData != null) {
                    int received_vials=0;

                    HashMap<String, String> map;
                    for (int k = 0; k < mData.length; k++) {

                        map = new HashMap<>();

                        Log.d("123456", "1 :  " +mData[k][0]);
                        Log.d("123456", "2 :  " +mData[k][1]);
                        Log.d("123456", "3 :  " +mData[k][2]);
                        Log.d("123456", "4 :  " +mData[k][3]);
                        Log.d("123456", "5 :  " +mData[k][4]);
                        Log.d("123456", "6 :  " +mData[k][5]);


                        hashMapArrayList.add(map);
                    }

                    adapter.notifyDataSetChanged();
                }
                else {
                    Log.d("123456", "ARRAY NULL :  ");
                }
         //   }

        } catch (Exception e) {
            Log.d("12345", "Error: " + e.getMessage());
        }


    }



    public class ADT extends BaseAdapter {

        Context ctx;
        ArrayList<String> arrayList_VaccineNames;

        ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
        LayoutInflater inflater;


        public ADT(Context ctx, ArrayList<String> arrayList_VaccineNames,ArrayList<HashMap<String, String>> hashMapArrayList ) {
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

        @Override
        public View getView(final int position, View view, ViewGroup parent) {

            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();

                view = inflater.inflate(R.layout.custom_lv_vaccine_stocks, null);
                holder.tvVaccineName = (TextView) view.findViewById(R.id.tvVaccineName);
                holder.tvBalance = (TextView) view.findViewById(R.id.tvBalance);
                holder.tvUtilized = (TextView) view.findViewById(R.id.tvUtilized);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();

            }
            holder.tvVaccineName.setText(arrayList_VaccineNames.get(position).split("@")[0]);
//            holder.tvBalance.setText(hashMapArrayList.get(position).get("received_vials")+" vials " +" / " + hashMapArrayList.get(position).get("doses")+" doses");
         //   holder.tvUtilized.setText(arrayList_VaccineNames.get(position).split("@")[0]);


            return view;
        }

    }

    public static class ViewHolder {

        protected TextView tvVaccineName, tvUtilized, tvBalance;
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