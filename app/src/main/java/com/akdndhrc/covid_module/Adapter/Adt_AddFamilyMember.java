package com.akdndhrc.covid_module.Adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akdndhrc.covid_module.R;

import java.util.ArrayList;
import java.util.HashMap;



public class Adt_AddFamilyMember extends BaseAdapter {
    private Context ctx;
    ArrayList<HashMap<String, String>> mArrayList = new ArrayList<HashMap<String, String>>();

    private LayoutInflater inflater = null;

    // Constructor
    public Adt_AddFamilyMember(Context ctx, ArrayList<HashMap<String, String>> mArrayList) {
        this.ctx = ctx;
        this.mArrayList = mArrayList;
        Log.d("adapter", String.valueOf(mArrayList.size()));
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public int getCount() {
        return mArrayList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int pos, View row, ViewGroup viewGroup) {

        final ViewHolder holder;
        if (row == null) {
            holder = new ViewHolder();

            Log.d("adapter", String.valueOf(pos));
            row = inflater.inflate(R.layout.custom_listview_addfamilymember_layout, null);
            holder.afm_lv_tvNaam = row.findViewById(R.id.afm_lv_tvNaam);
            holder.afm_lv_tvDateTime = row.findViewById(R.id.afm_lv_tvDateTime);
            holder.afm_lv_iv_gender_icon = row.findViewById(R.id.afm_lv_iv_gender_icon);
            holder.afm_lv_rl_background = row.findViewById(R.id.afm_lv_rl_background);


            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }


        holder.afm_lv_tvNaam.setText(Html.fromHtml("" + mArrayList.get(pos).get("child_name")));
        holder.afm_lv_tvDateTime.setText(Html.fromHtml("" + mArrayList.get(pos).get("child_date")));


        if (Integer.parseInt(mArrayList.get(pos).get("age")) <= 2)
        {
            if (pos % 2 == 0)
            {
                if (mArrayList.get(pos).get("gender").equalsIgnoreCase("1"))
                {

                    holder.afm_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.afm_lv_iv_gender_icon.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
                    }
                }
                else{
                    holder.afm_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_adult_female_50dp));
                }
                holder.afm_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.afm_rl_background_color));
            }

            else {
                if (mArrayList.get(pos).get("gender").equalsIgnoreCase("1"))
                {
                    holder.afm_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.afm_lv_iv_gender_icon.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
                    }
                }
                else{
                    holder.afm_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_adult_female_50dp));
                }
                holder.afm_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.light_color_white));
            }
        }

        else if (Integer.parseInt(mArrayList.get(pos).get("age")) >= 3 && Integer.parseInt(mArrayList.get(pos).get("age")) <= 14)
        {
            if (pos % 2 == 0)
            {
                if (mArrayList.get(pos).get("gender").equalsIgnoreCase("1"))
                {

                    holder.afm_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.afm_lv_iv_gender_icon.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
                    }
                }
                else{
                    holder.afm_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_adult_female_50dp));
                }
                holder.afm_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.afm_rl_background_color));
            }

            else {
                if (mArrayList.get(pos).get("gender").equalsIgnoreCase("1"))
                {
                    holder.afm_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.afm_lv_iv_gender_icon.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
                    }
                }
                else{
                    holder.afm_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_adult_female_50dp));
                }
                holder.afm_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.light_color_white));
            }
        }

        else {
          //  holder.afm_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_woman_icon));

            if (pos % 2 == 0)
            {
                if (mArrayList.get(pos).get("gender").equalsIgnoreCase("1"))
                {

                    holder.afm_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.afm_lv_iv_gender_icon.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
                    }
                }
                else{
                    holder.afm_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.woman_icon));
                }
                holder.afm_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.afm_rl_background_color));
            }

            else {
                if (mArrayList.get(pos).get("gender").equalsIgnoreCase("1"))
                {
                    holder.afm_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.afm_lv_iv_gender_icon.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
                    }
                }
                else{
                    holder.afm_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.woman_icon));
                }
                holder.afm_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.light_color_white));
            }

          /*  if (pos % 2 == 0) {
                holder.afm_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.afm_rl_background_color));
            } else {
                holder.afm_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.light_color_white));
            }*/
        }


        return row;
    }



    static class ViewHolder {
        TextView afm_lv_tvNaam, afm_lv_tvDateTime;
        ImageView afm_lv_iv_gender_icon;
        RelativeLayout afm_lv_rl_background;

    }


}
