package com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildHifazitiTeekeyRecordActivities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.akdndhrc.covid_module.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Child_HifazitiTeekeyGroupList_Activity extends ListActivity {

    Context ctx = Child_HifazitiTeekeyGroupList_Activity.this;

    String[][] mData, mData_dob, mData_duedate;
    int diffInDays;
    String child_uid, child_name, child_gender,child_age;
    Lister ls;
    List<Item> t = new ArrayList<>();
    TextView txt_age, txt_naam;
    ImageView iv_navigation_drawer, iv_home, iv_close, image_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.temp_list);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Child_HifazitiTeekeyGroupList_Activity.class));
        Log.d("000555", "RESTARRRRRRRRRRRRR");


        child_uid = getIntent().getExtras().getString("u_id");
        child_name = getIntent().getExtras().getString("child_name");
        child_gender = getIntent().getExtras().getString("child_gender");

        //TextView
        txt_age = findViewById(R.id.txt_age);
        txt_naam = findViewById(R.id.txt_naam);


        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        //  iv_home.setVisibility(View.GONE);
        image_gender = findViewById(R.id.image_gender);

        calculateAge();

     if (child_gender.equalsIgnoreCase("0")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                image_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_adult_female_50dp));
                image_gender.setImageTintList(ctx.getResources().getColorStateList(R.color.pink_color));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    image_gender.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                    image_gender.setImageTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
                }
            }
        }

       /* TextView tv1 = (TextView)findViewById(R.id.tv1 );
        TextView tv2 = (TextView)findViewById(R.id.tv2 );
        TextView tv3 = (TextView)findViewById(R.id.tv3 );
        TextView tv4 = (TextView)findViewById(R.id.tv4 );
        TextView tv5 = (TextView)findViewById(R.id.tv5);
        tv1.startAnimation(AnimationUtils.loadAnimation( this, R.anim.vertical_text ) );
        tv2.startAnimation(AnimationUtils.loadAnimation( this, R.anim.vertical_text ) );
        tv3.startAnimation(AnimationUtils.loadAnimation( this, R.anim.vertical_text ) );
        tv4.startAnimation(AnimationUtils.loadAnimation( this, R.anim.vertical_text ) );
        tv5.startAnimation(AnimationUtils.loadAnimation( this, R.anim.vertical_text ) );*/

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(ctx, HomePage_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
            }
        });


        setListAdapter(new ItemAdapter(getItems_another()));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


        // ListView Clicked item index
        try {

            String[] vac_name= t.get(position).text().split(",");
            Log.d("000357", " " + t.get(position).text());

            if (vac_name[1].length() > 0)
            {
                Intent intent2 = new Intent(ctx, Child_HifazitiTeekeyDueDateList_Activity.class);
                intent2.putExtra("u_id", child_uid);
                intent2.putExtra("child_name", child_name);
                intent2.putExtra("child_age", child_age);
                intent2.putExtra("child_gender", child_gender);
                intent2.putExtra("vaccine_name", vac_name[0]);
                startActivity(intent2);

            }
            else {

            }
            

        } catch (Exception e) {
            Log.d("000357", "End Exception:" + e.getMessage());
        }

    }

    class ItemAdapter extends ArrayAdapter<Item> {
        final private List<Class<?>> viewTypes;

        ItemAdapter(List<Item> items) {
            super(Child_HifazitiTeekeyGroupList_Activity.this, 0, items);
            if (items.contains(null))
                throw new IllegalArgumentException("null item");
            viewTypes = getViewTypes(items);

        }

        private List<Class<?>> getViewTypes(List<Item> items) {
            Set<Class<?>> set = new HashSet<>();
            for (Item i : items)
                set.add(i.getClass());
            List<Class<?>> list = new ArrayList<>(set);
            return Collections.unmodifiableList(list);
        }

        @Override
        public int getViewTypeCount() {
            return viewTypes.size();
        }

        @Override
        public int getItemViewType(int position) {
            Item t = getItem(position);
            return viewTypes.indexOf(t.getClass());
        }

        @Override
        public View getView(int position, View v, ViewGroup unused) {
            return getItem(position).getView(getLayoutInflater(), v);
        }
    }

    abstract private class Item {
        //final private String text, date;
        final private int pos;
        final private String text, date;

        public Item(int pos, String text, String date) {
            this.pos = pos;
            this.text = text;
            this.date = date;
        }

        Integer pos() {
            return pos;
        }

        String text() {
            return text;
        }

        String date() {
            return date;
        }

        abstract View getView(LayoutInflater i, View v);
    }


    private class HeaderItem extends Item {
        public HeaderItem(int pos, String text, String date) {
            super(pos, text, date);
        }

        @Override
        View getView(LayoutInflater i, View v) {
            ViewHolder h;
            if (v == null) {
                v = i.inflate(R.layout.header, null);
                h = new ViewHolder(v);
                v.setTag(h);
            } else {
                h = (ViewHolder) v.getTag();
            }
            h.tvGroupName.setText(text());
            return v;
        }

        private class ViewHolder {
            final TextView tvGroupName;

            ViewHolder(View v) {
                tvGroupName = v.findViewById(R.id.tvGroupName);
            }
        }
    }

    private class RowItem extends Item {
        public RowItem(int pos, String text, String date) {
            super(pos, text, date);
        }

        @Override
        View getView(LayoutInflater i, View v) {
            ViewHolder h;
            if (v == null) {
                v = i.inflate(R.layout.custom_lv_child_hifaziti_teekey_record_list_layout, null);
                h = new ViewHolder(v);
                v.setTag(h);
            } else {
                h = (ViewHolder) v.getTag();
            }

            Log.d("000357", "VAC NAME: " + text());
            String[] vacine_name = text().split(",");

            Log.d("000357", "VAC Split 1: " + vacine_name[0]);
            Log.d("000357", "VAC Split 2: " + vacine_name[1]);
            try {

                h.chtr_lv_rl_background.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.color_white));

                if (vacine_name[1].equalsIgnoreCase("0")) {
                    Log.d("000369", "VAC NAME: " + vacine_name[0] + " VAC TYPE: " + vacine_name[1] + " VAC DATE: " + vacine_name[2]);
                    h.chtr_lv_tvVaccineName.setText(vacine_name[0] + "/" + vacine_name[2]);
                    h.chtr_lv_tvTareekh.setText(date());
                    h.chtr_lv_tvVaccineName.setTextColor(getApplicationContext().getResources().getColor(R.color.graph_green_color));
                    h.chtr_lv_tvTareekh.setTextColor(getApplicationContext().getResources().getColor(R.color.graph_green_color));
                    h.chtr_lv_iv_vaccineIcon.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.circle_shape_green));
                } else if (vacine_name[1].equalsIgnoreCase("1")) {
                    h.chtr_lv_tvVaccineName.setText(vacine_name[0]);
                    h.chtr_lv_tvTareekh.setText(date());
                    h.chtr_lv_tvVaccineName.setTextColor(getApplicationContext().getResources().getColor(R.color.graph_orange_color));
                    h.chtr_lv_tvTareekh.setTextColor(getApplicationContext().getResources().getColor(R.color.graph_orange_color));
                    h.chtr_lv_iv_vaccineIcon.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.circle_shape_orange));

                } else if (vacine_name[1].equalsIgnoreCase("2")) {
                    h.chtr_lv_tvVaccineName.setText(vacine_name[0]);
                    h.chtr_lv_tvTareekh.setText(date());
                    h.chtr_lv_tvVaccineName.setTextColor(getApplicationContext().getResources().getColor(R.color.graph_red_color));
                    h.chtr_lv_tvTareekh.setTextColor(getApplicationContext().getResources().getColor(R.color.graph_red_color));
                    h.chtr_lv_iv_vaccineIcon.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.circle_shape_red));

                } else if (vacine_name[1].equalsIgnoreCase("3")) {
                    h.chtr_lv_tvVaccineName.setText(vacine_name[0]);
                    h.chtr_lv_tvTareekh.setText(date());
                    h.chtr_lv_tvVaccineName.setTextColor(getApplicationContext().getResources().getColor(R.color.grey_color));
                    h.chtr_lv_tvTareekh.setTextColor(getApplicationContext().getResources().getColor(R.color.grey_color));
                    h.chtr_lv_iv_vaccineIcon.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.circle_shape_grey));

                } else if (vacine_name[1].equalsIgnoreCase("5")) {
                    Log.d("000369", "VAC NAME: " + vacine_name[0] + " VAC TYPE: " + vacine_name[1] + " VAC DATE: " + vacine_name[2]);
                    h.chtr_lv_tvVaccineName.setText(vacine_name[0] + "/" + vacine_name[2]);
                    h.chtr_lv_tvTareekh.setText(date());
                    h.chtr_lv_tvVaccineName.setTextColor(getApplicationContext().getResources().getColor(R.color.purple_color));
                    h.chtr_lv_tvTareekh.setTextColor(getApplicationContext().getResources().getColor(R.color.purple_color));
                    h.chtr_lv_iv_vaccineIcon.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.circle_shape_purple));

                } else {
                    h.chtr_lv_tvVaccineName.setText(vacine_name[0]);
                    h.chtr_lv_tvTareekh.setText(date());
                    h.chtr_lv_tvVaccineName.setTextColor(getApplicationContext().getResources().getColor(R.color.grey_color));
                    h.chtr_lv_tvTareekh.setTextColor(getApplicationContext().getResources().getColor(R.color.grey_color));
                    h.chtr_lv_iv_vaccineIcon.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.circle_shape_grey));

                }
            } catch (Exception e) {
                Log.d("000357", "Adptr Error: " + e.getMessage());
            }

            return v;
        }

        private class ViewHolder {
            TextView chtr_lv_tvTareekh, chtr_lv_tvDate, chtr_lv_tvVaccineName;
            RelativeLayout chtr_lv_rl_background;
            ImageView chtr_lv_iv_vaccineIcon;

            ViewHolder(View row) {
                chtr_lv_tvTareekh = row.findViewById(R.id.chtr_lv_tvTareekh);
                chtr_lv_tvDate = row.findViewById(R.id.chtr_lv_tvDate);
                chtr_lv_tvVaccineName = row.findViewById(R.id.chtr_lv_tvVaccineName);
                chtr_lv_rl_background = row.findViewById(R.id.chtr_lv_rl_background);
                chtr_lv_iv_vaccineIcon = row.findViewById(R.id.chtr_lv_iv_vaccineIcon);

            }
        }
    }


    private List<Item> getItems_another() {

        t = new ArrayList<>();
        t.clear();
        try {

            ls = new Lister(Child_HifazitiTeekeyGroupList_Activity.this);
            ls.createAndOpenDB();

            mData_dob = ls.executeReader("Select dob from MEMBER where uid = '" + child_uid + "'");
            Log.d("000357", "DOB:" + mData_dob[0][0]);
            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            String TodayDate = dates.format(c.getTime());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


            //Convert to Date
            Date startDate = dates.parse(TodayDate);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(startDate);

            //Convert to Date
            Date endDate = dates.parse(mData_dob[0][0]);
            Calendar c2 = Calendar.getInstance();
            c2.setTime(endDate);

            //get Time in milli seconds
            long ms1 = c1.getTimeInMillis();
            long ms2 = c2.getTimeInMillis();
            //get difference in milli seconds
            long diff = ms1 - ms2;
            diffInDays = (int) (diff / (24 * 60 * 60 * 1000));
            Log.d("000357", String.valueOf(diffInDays));

            Lister ls = new Lister(Child_HifazitiTeekeyGroupList_Activity.this);
            ls.createAndOpenDB();

            mData_duedate = ls.executeReader("Select CAST(due_date as INT) as x from VACCINES GROUP BY x ORDER By x");

            for (int a = 0; a < mData_duedate.length; a++) {

                switch (mData_duedate[a][0]) {
                    case ("0"):
                        t.add(new HeaderItem(0, "With-in 24 Hours", ""));
                        Log.d("000357", "CASE 0   !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        break;


                    case ("45"):
                        t.add(new HeaderItem(0, "In 6 Weeks", ""));
                        Log.d("000357", "CASE 45    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        break;

                    case ("73"):
                        t.add(new HeaderItem(0, "In 10 Weeks", ""));
                        Log.d("000357", "CASE  73  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        break;

                    case ("101"):
                        t.add(new HeaderItem(0, "In 14 Weeks", ""));
                        Log.d("000357", "CASE  101  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        break;

                    case ("270"):
                        t.add(new HeaderItem(0, "In 9 Months", ""));
                        Log.d("000357", "CASE  270  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        break;

                    case ("450"):
                        t.add(new HeaderItem(0, "In 15 Months", ""));
                        Log.d("000357", "CASE  450  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        break;

                    default:
                        Log.d("000357", "CASE DEFAULTT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        break;
                }
                Log.d("000357", "Vac DueDate !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + mData_duedate[a][0]);

                mData= ls.executeReader("Select uid,name,due_date from VACCINES where due_date = '" + mData_duedate[a][0] + "'");
                if (mData != null) {
                    Log.d("000357", "Vac LENGTH !!!!!!!!!!!!!!!: " + mData.length);

                    for (int i = 0; i < mData.length; i++) {
                        Log.d("000357", "Vac UID: " + mData[i][0]);
                        Log.d("000357", "Vac Name: " + mData[i][1]);
                        Log.d("000357", "Vac DueDate: " + mData[i][2]);


                        if (diffInDays == Integer.parseInt(mData[i][2])) {
                            Log.d("000357", "IFFFFF: ");
                            Calendar cvac = new GregorianCalendar();
                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                            cvac.setTime(dateFormatcvac.parse(mData_dob[0][0]));
                            cvac.add(Calendar.DATE, Integer.parseInt(mData[i][2]));
                            Date dvac = cvac.getTime();
                            dateFormat.format(dvac.getTime());

                            String[][] mDatavac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                            if (mDatavac != null) {
                                String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                if (mData_refuse != null) {

                                    t.add(new RowItem(i, mData[i][1] + "," + "5" + "," + mData_refuse[0][0], dateFormat.format(dvac.getTime())));
                                    //t.add(new RowItem(mDataVac[i][1] + "," + mData_refuse[0][0] + "," + "5", dateFormat.format(dvac.getTime())));
                                    Log.d("000357", "1000: ");
                                } else {
                                    t.add(new RowItem(i, mData[i][1] + "," + "0" + "," + mDatavac[0][0], dateFormat.format(dvac.getTime())));
                                    Log.d("000357", "1: ");
                                }
                            } else {

                                t.add(new RowItem(i, mData[i][1] + "," + "1", dateFormat.format(dvac.getTime())));

                                Log.d("000357", "2: ");
                            }
                            Log.d("000522", "Vaccines Name 1: " + mData[i][1]);


                        }

                        /*else if (diffInDays < Integer.parseInt(mDataVac[i][2]) && diffInDays > (Integer.parseInt(mDataVac[i][2]) - 15)) {
                            //arrayListVaccine.add(i, mData[i][1] + ",1");
                            Calendar cvac = new GregorianCalendar();
                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                            cvac.setTime(dateFormatcvac.parse(mData_dob[0][0]));
                            cvac.add(Calendar.DATE, Integer.parseInt(mDataVac[i][2]));
                            Date dvac = cvac.getTime();
                            dateFormat.format(dvac.getTime());

                            //  t.add(new RowItem(i,",",dateFormat.format(dvac.getTime())));

                            String[][] mDatavac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mDataVac[i][0]) + "'");
                            if (mDatavac != null) {

                                String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mDataVac[i][0]) + "' AND type ='3' ");
                                if (mData_refuse != null) {

                                    t.add(new RowItem(i, mDataVac[i][1] + "," + "5" + "," + mData_refuse[0][0], dateFormat.format(dvac.getTime())));
                                    Log.d("000357", "101: ");
                                } else {

                                    t.add(new RowItem(i, mDataVac[i][1] + "," + "0" + "," + mDatavac[0][0], dateFormat.format(dvac.getTime())));
                                    Log.d("000357", "3: ");
                                }
                            } else {

                                t.add(new RowItem(i, mDataVac[i][1] + "," + "1", dateFormat.format(dvac.getTime())));
                                Log.d("000357", "4: ");
                            }
                        } */

                        else if (diffInDays < Integer.parseInt(mData[i][2])) {

                            Log.d("000522", "DAYS LESS THAN DUE DATEEE: " +diffInDays +" < " + mData[i][2]);
                            Log.d("000522", "Vaccines Name 2: " + mData[i][1]);

                            switch (mData[i][1])
                            {
                                case "OPV-2":
                                    String[][] mData_opv1 = ls.executeReader("Select t1.name ,t0.vaccinated_on from CVACCINATION t0" +
                                            " INNER JOIN VACCINES t1 ON t0.vaccine_id=t1.uid" +
                                            " where t0.member_uid = '" + child_uid + "' AND t0.vaccine_id = 'c6a25e916636f934'");
                                    if (mData_opv1 != null) {
                                        Log.d("000522", "OPV-1 PERFORMED : " );
                                        Log.d("000522", "Vaccinated VACCINE : " + mData_opv1[0][0]);
                                        Log.d("000522", "Vaccinated Date : " + mData_opv1[0][1]);

                                        String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = 'c6a25e916636f934' AND type ='3' ");
                                        if (mData_refuse != null) {
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_refuse[0][0]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000522", "OPV 1 DayDiff REFUSE : " + dateFormat.format(dvac.getTime()));

                                            // arrayListVaccine.add(i, mData[i][1] + ",5"+ "," + mData_refuse[0][0]);
                                            t.add(new RowItem(i, " " + "," + " " + "," , dateFormat.format(dvac.getTime())));

                                            Log.d("000522", "OPV 1 REFUSE PERFORMED: ");
                                        } else {
                                            Calendar cvac = new GregorianCalendar();
                                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                                            cvac.setTime(dateFormatcvac.parse(mData_opv1[0][1]));
                                            cvac.add(Calendar.DATE, 28);
                                            Date dvac = cvac.getTime();
                                            dateFormat.format(dvac.getTime());
                                            Log.d("000522", "OPV 1 DayDiff : " + dateFormat.format(dvac.getTime()));

                                            // arrayListVaccine.add(i, mData[i][1] + ",0"+ "," + mData_opv2[0][1]);
                                            t.add(new RowItem(i, " " + "," + " " + "," , dateFormat.format(dvac.getTime())));
                                            Log.d("000522", "OPV 1 REFUSE NOT  PERFORMED: ");
                                        }

                                        String[][] mData_opv2 = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                                        if (mData_opv2 != null) {
                                            String[][] mData_opv2_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                            if (mData_opv2_refuse != null) {
                                                t.add(new RowItem(i, mData[i][1] + "," + "5" + "," + mData_refuse[0][0],null));
                                                Log.d("000522", "OPV-2 PERFORMED REFUSE VAC : ");
                                            } else {
                                                t.add(new RowItem(i, mData[i][1] + "," + "0" + "," + mData_opv2[0][0],null));
                                                Log.d("000522", "OPV-2 PERFORMED : ");
                                            }
                                        } else {

                                            t.add(new RowItem(i, mData[i][1] + "," + "3" + "," ,null));
                                            Log.d("000522", "OPV-2 NOT PERFORMED: ");
                                        }

                                    } else {
                                        Log.d("000522", "ELSEEEEEE OPV-1 NOT PERFORMED: " );
                                        t.add(new RowItem(i, mData[i][1] + "," + "3" + "," ,"OPV-1 Pending"));
                                    }
                                    break;

                                    default:
                                        Log.d("000522", "DEFAULT : ");
                                        break;
                            }


                        }


                        else if (diffInDays > Integer.parseInt(mData[i][2])) {
                            // arrayListVaccine.add(i, mData[i][1] + ",2");
                            Calendar cvac = new GregorianCalendar();
                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                            cvac.setTime(dateFormatcvac.parse(mData_dob[0][0]));
                            cvac.add(Calendar.DATE, Integer.parseInt(mData[i][2]));
                            Date dvac = cvac.getTime();
                            dateFormat.format(dvac.getTime());

                            //       t.add(new RowItem(i,",",dateFormat.format(dvac.getTime())));

                            String[][] mDatavac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                            if (mDatavac != null) {
//                                arrayListVaccine.add(i, mData[i][1] + ",0");
//                                Log.d("000357", "5: " );
                                String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                if (mData_refuse != null) {
                                    t.add(new RowItem(i, mData[i][1] + "," + "5" + "," + mData_refuse[0][0], dateFormat.format(dvac.getTime())));
                                    Log.d("000357", "102: ");
                                } else {
                                    t.add(new RowItem(i, mData[i][1] + "," + "0" + "," + mDatavac[0][0], dateFormat.format(dvac.getTime())));
                                    Log.d("000357", "5: ");
                                }

                            } else {
                                t.add(new RowItem(i, mData[i][1] + "," + "2", dateFormat.format(dvac.getTime())));
                                Log.d("000357", "6: ");
                            }

                        }



                        else {
                            Calendar cvac = new GregorianCalendar();
                            java.text.DateFormat dateFormatcvac = new SimpleDateFormat("yyyy-MM-dd");
                            cvac.setTime(dateFormatcvac.parse(mData_dob[0][0]));
                            cvac.add(Calendar.DATE, Integer.parseInt(mData[i][2]));
                            Date dvac = cvac.getTime();
                            dateFormat.format(dvac.getTime());
                            //   t.add(new RowItem(i,",",dateFormat.format(dvac.getTime())));

                            String[][] mDatavac = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "'");
                            if (mDatavac != null) {
                               /* arrayListVaccine.add(i, mData[i][1] + ",0");
                                Log.d("000357", "9: " );*/
                                String[][] mData_refuse = ls.executeReader("Select vaccinated_on from CVACCINATION where member_uid = '" + child_uid + "' AND vaccine_id = '" + String.valueOf(mData[i][0]) + "' AND type ='3' ");
                                if (mData_refuse != null) {
                                    t.add(new RowItem(i, mData[i][1] + "," + "5" + "," + mData_refuse[0][0], dateFormat.format(dvac.getTime())));
                                    Log.d("000357", "104: ");
                                } else {
                                    t.add(new RowItem(i, mData[i][1] + "," + "0" + "," + mDatavac[0][0], dateFormat.format(dvac.getTime())));
                                    Log.d("000357", "9: ");
                                }

                            } else {

                                t.add(new RowItem(i, mData[i][1] + "," + "4", dateFormat.format(dvac.getTime())));
                                Log.d("000357", "10 ");
                            }
                        }

                    }
                } else {
                    Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
                }


            }

        } catch (Exception e) {
            Log.d("000357", "End Exception:" + e.getMessage());
        }
        return t;
    }

    private void calculateAge() {

        Lister ls = new Lister(ctx);
        ls.createAndOpenDB();

        String mData[][] = ls.executeReader("Select full_name,dob from MEMBER where uid = '" + child_uid + "'");

        child_name = mData[0][0];
        txt_naam.setText(mData[0][0]);

        Log.d("000555", "DOB: " + mData[0][1]);

        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = parse.parse(mData[0][1]);
            Date n = new Date();
            long dob = d.getTime();
            long now = n.getTime();
            long diff = (now - dob) / 1000;
            long days = diff / 86400;
            int dob_days = 0;
            int dob_weeks = 0;
            int dob_months = 0;
            int dob_years = 0;
            while (days > 7) {
                if ((days - 365) > 0) {
                    days -= 365;
                    dob_years++;
                    continue;
                }
                if ((days - 30) > 0) {
                    days -= 30;
                    dob_months++;

                    continue;
                }
                if ((days - 7) > 0) {
                    days -= 7;
                    dob_weeks++;
                }
            }


            dob_days = Integer.valueOf((int) days);

            Log.d("000555", "Saal: " + String.valueOf(dob_years));
            Log.d("000555", "Maah: " + String.valueOf(dob_months));

            child_age = String.valueOf(dob_years) + " سال " + String.valueOf(dob_months) + " مہ ";
            txt_age.setText(child_age);
//            Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            Log.d("000555", "DOB Error: " + e.getMessage());
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
        super.onBackPressed();
        finish();
        //  startActivity(new Intent(ctx, Child_Dashboard_Activity.class));
    }
}

