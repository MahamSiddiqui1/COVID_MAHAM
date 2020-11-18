package com.akdndhrc.covid_module.Adapter.Adt_SocialContact;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akdndhrc.covid_module.CustomClass.SchoolMeetingClass;
import com.akdndhrc.covid_module.R;

import java.util.ArrayList;
import java.util.HashMap;


public class Adt_HESStartMeetingList extends BaseAdapter {
    private Context ctx;
    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    ArrayList<SchoolMeetingClass> mArrayList_count = new ArrayList<>();


    private LayoutInflater inflater = null;

    public static ArrayList<HashMap<String, String>> mapArrayHash_1 = new ArrayList<>();
    public static HashMap<String, String> hashMap_1 = new HashMap<>();

    int counter_male=0, counter_female=0;


    // Constructor
    public Adt_HESStartMeetingList(Context ctx, ArrayList<HashMap<String, String>> hashMapArrayList, ArrayList<SchoolMeetingClass> mArrayList_count) {
        this.ctx = ctx;
        this.hashMapArrayList = hashMapArrayList;
        this.mArrayList_count = mArrayList_count;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public int getCount() {
        return hashMapArrayList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int pos, View row, ViewGroup viewGroup) {

        final ViewHolder holder;
        if (row == null) {
            holder = new ViewHolder();

            row = inflater.inflate(R.layout.custom_lv_hes_start_meeting_list_layout, null);

            //TextView
            holder.tvSchoolName = row.findViewById(R.id.tvSchoolName);
            holder.tv_count_male = row.findViewById(R.id.tv_count_male);
            holder.tv_count_female = row.findViewById(R.id.tv_count_female);

            //RelativeLayout
            holder.rl_add_male = row.findViewById(R.id.rl_add_male);
            holder.rl_sub_male = row.findViewById(R.id.rl_sub_male);
            holder.rl_add_female = row.findViewById(R.id.rl_add_female);
            holder.rl_sub_female = row.findViewById(R.id.rl_sub_female);




            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.tvSchoolName.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("school_name")));

        holder.tv_count_male.setText("" + counter_male);
        holder.tv_count_female.setText("" + counter_female);


      /*  try {

                if (mArrayList_status.get(pos).getMember_status().equalsIgnoreCase("0")) {
                    holder.checkbox.setChecked(false);
                } else {
                    holder.checkbox.setChecked(true);
                }


        }catch (Exception e)
        {
            Log.d("000999", "ERRRRRRRRRR : " +e.getMessage());
        }*/

        holder.rl_add_male.setTag(pos);
        holder.rl_add_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter_male < 10) {
                    counter_male += 1;
                    holder.tv_count_male.setText("" + counter_male);

                    Log.d("000999", ": " + counter_male);
                } else {
                    holder.tv_count_male.setText("" + counter_male);
                    Log.d("000999", ": " + counter_male);
                }

                hashMap_1.put("male_count"+pos, "" +counter_male);
                mArrayList_count.get(pos).setMale_count("" + counter_male);

                mapArrayHash_1.add(hashMap_1);
                mapArrayHash_1.clear();
                mapArrayHash_1.add(hashMap_1);


                Log.d("000999", "HASH_MAP 1: " + mapArrayHash_1);
            }
        });


        holder.rl_sub_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter_male > 0) {
                    counter_male -= 1;
                    holder.tv_count_male.setText("" + counter_male);
                    Log.d("000999", ": " + counter_male);
                } else {
                    holder.tv_count_male.setText("" + counter_male);
                    Log.d("000999", ": " + counter_male);
                }

                hashMap_1.put("male_count"+pos, "" +counter_male);
                mArrayList_count.get(pos).setMale_count("" + counter_male);

                mapArrayHash_1.add(hashMap_1);
                mapArrayHash_1.clear();
                mapArrayHash_1.add(hashMap_1);


                Log.d("000999", "HASH_MAP 2: " + mapArrayHash_1);
            }


        });


        holder.rl_add_female.setTag(pos);
        holder.rl_add_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (counter_female < 10) {
                    counter_female += 1;
                    holder.tv_count_female.setText("" + counter_female);

                    Log.d("000999", ": " + counter_female);
                } else {
                    holder.tv_count_female.setText("" + counter_female);
                    Log.d("000999", ": " + counter_female);
                }
                hashMap_1.put("female_count"+pos, "" +counter_female);
                mArrayList_count.get(pos).setFemale_count("" + counter_female);

                mapArrayHash_1.add(hashMap_1);
                mapArrayHash_1.clear();
                mapArrayHash_1.add(hashMap_1);


                Log.d("000999", "HASH_MAP 3: " + mapArrayHash_1);
            }
        });


        holder.rl_sub_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter_female > 0) {
                    counter_female -= 1;
                    holder.tv_count_female.setText("" + counter_female);
                    Log.d("000999", ": " + counter_female);
                } else {
                    holder.tv_count_female.setText("" + counter_female);
                    Log.d("000999", ": " + counter_female);
                }

                hashMap_1.put("female_count"+pos, "" +counter_female);
                mArrayList_count.get(pos).setFemale_count("" + counter_female);


                mapArrayHash_1.add(hashMap_1);
                mapArrayHash_1.clear();
                mapArrayHash_1.add(hashMap_1);


                Log.d("000999", "HASH_MAP 4: " + mapArrayHash_1);

            }


        });



              /*  mapArrayHash_1.add(hashMap_1);
                mapArrayHash_1.clear();
                mapArrayHash_1.add(hashMap_1);


                Log.d("000999", "HASH_MAP 1: " + mapArrayHash_1);*/


        return row;
    }


    static class ViewHolder {
        TextView tvSchoolName,tv_count_male,tv_count_female;
        RelativeLayout rl_add_male,rl_sub_male,rl_sub_female,rl_add_female;

    }


}
