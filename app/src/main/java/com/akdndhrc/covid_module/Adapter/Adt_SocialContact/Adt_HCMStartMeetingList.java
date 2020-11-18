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
import android.widget.ImageView;
import android.widget.TextView;

import com.akdndhrc.covid_module.CustomClass.CheckListClass;
import com.akdndhrc.covid_module.R;
import com.rey.material.widget.CheckBox;

import java.util.ArrayList;
import java.util.HashMap;


public class Adt_HCMStartMeetingList extends BaseAdapter {
    private Context ctx;
    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    ArrayList<CheckListClass> mArrayList_status = new ArrayList<>();
   /* public static ArrayList<CheckListClass> checkListClasses = new ArrayList<>();*/

    private LayoutInflater inflater = null;

    public static ArrayList<HashMap<String, String>> mapArrayHash_1 = new ArrayList<>();
    public static HashMap<String, String> hashMap_1 = new HashMap<>();


    // Constructor
    public Adt_HCMStartMeetingList(Context ctx, ArrayList<HashMap<String, String>> hashMapArrayList,ArrayList<CheckListClass> mArrayList_status) {
        this.ctx = ctx;
        this.hashMapArrayList = hashMapArrayList;
        this.mArrayList_status = mArrayList_status;
     /*   this.checkListClasses = checkListClasses;*/
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

            row = inflater.inflate(R.layout.custom_lv_hcm_start_meeting_list_layout, null);

            //TextView
            holder.tvMemberName = row.findViewById(R.id.tvMemberName);
            holder.checkbox = row.findViewById(R.id.checkbox);
            holder.iv_icon = row.findViewById(R.id.iv_icon);

            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.tvMemberName.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("full_name")));


            if (hashMapArrayList.get(pos).get("gender").equalsIgnoreCase("0")) {
                holder.iv_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.woman_icon));
                holder.iv_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.pink_color));

                Log.d("000999", "Pos: "+ pos +" @ " +hashMapArrayList.get(pos).get("gender"));
            }

            else   {
                holder.iv_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                holder.iv_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));

                Log.d("000999", "Pos: "+ pos +" @ " +hashMapArrayList.get(pos).get("gender"));
            }


        try {

                if (mArrayList_status.get(pos).getMember_status().equalsIgnoreCase("0")) {
                    holder.checkbox.setChecked(false);
                } else {
                    holder.checkbox.setChecked(true);
                }


        }catch (Exception e)
        {
            Log.d("000999", "ERRRRRRRRRR : " +e.getMessage());
        }

        holder.checkbox.setTag(pos);
        holder.checkbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //is chkIos checked?
                if (holder.checkbox.isChecked()) {

                    Log.d("000999", "IFFFFF: ");
                       //hashMap_1.put(String.valueOf(pos), "1");
                    hashMap_1.put(String.valueOf(pos), hashMapArrayList.get(pos).get("member_uid"));

                       mArrayList_status.get(pos).setMember_status("1");

                } else {
               hashMap_1.remove(String.valueOf(pos));
                    //hashMap_1.put(String.valueOf(pos), "0");
                    mArrayList_status.get(pos).setMember_status("0");

                    holder.checkbox.setChecked(false);
                    Log.d("000999", "LEEEEE: ");
                }

                mapArrayHash_1.add(hashMap_1);
                mapArrayHash_1.clear();
                mapArrayHash_1.add(hashMap_1);


                Log.d("000999", "HASH_MAP 1: " + mapArrayHash_1);

            }
        });


        return row;
    }


    static class ViewHolder {
        TextView tvMemberName;
        ImageView iv_icon;
        CheckBox checkbox;

    }


}
