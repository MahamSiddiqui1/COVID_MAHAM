package com.akdndhrc.covid_module.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
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



public class Adt_VACSearchVillages extends BaseAdapter {
    Context ctx;
    ArrayList<HashMap<String, String>> arrayList;
    LayoutInflater inflater;

    int newHeight = 34; // New height in pixels
    int newWidth = 40;


    public Adt_VACSearchVillages(Context ctx, ArrayList<HashMap<String, String>> arrayList) {
        this.ctx = ctx;
        this.arrayList = arrayList;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
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

            view = inflater.inflate(R.layout.custom_lv_item_main_search, null);
            holder.tvID = (TextView) view.findViewById(R.id.tvID);
            holder.tvFullName = (TextView) view.findViewById(R.id.tvName);
            holder.tvDateTime = (TextView) view.findViewById(R.id.tvDateTime);
            holder.gender_icon = (ImageView) view.findViewById(R.id.gender_icon);
            holder.rl = (RelativeLayout) view.findViewById(R.id.rl);

            view.setTag(holder);
        } else {

            holder = (ViewHolder) view.getTag();

        }


        try{

            holder.tvFullName.setText(arrayList.get(position).get("name"));
            holder.tvID.setText(arrayList.get(position).get("village_name"));
            holder.tvDateTime.setText(arrayList.get(position).get("gender")+" / "+arrayList.get(position).get("age") +" سال");
            holder.tvDateTime.setTypeface(Typeface.DEFAULT_BOLD);

            if (Integer.parseInt(arrayList.get(position).get("age")) <=2)
            {
                if (arrayList.get(position).get("gender").equalsIgnoreCase("لڑکا"))
                {
                    holder.gender_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.baby_boy_icon));
                }
                else {
                    holder.gender_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.baby_boy_icon));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.gender_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.pink_color));
                    }
                }
            }
            else if (Integer.parseInt(arrayList.get(position).get("age")) >= 3 && Integer.parseInt(arrayList.get(position).get("age")) <= 14)
            {
                if (arrayList.get(position).get("gender").equalsIgnoreCase("لڑکا"))
                {
                    holder.gender_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.gender_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.blue_text_color));
                    }
                }
                else {
                    holder.gender_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_adult_female_50dp));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.gender_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.pink_color));
                    }
                    holder.gender_icon.requestLayout();
                    holder.gender_icon.getLayoutParams().height = newHeight;
                    holder.gender_icon.getLayoutParams().width = newWidth;
                }
            }
            else {
                if (arrayList.get(position).get("gender").equalsIgnoreCase("مرد"))
                {
                    holder.gender_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.gender_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.blue_text_color));
                    }
                }
                else {
                    holder.gender_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.woman_icon));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.gender_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.pink_color));
                    }
                }
            }

            //Text COlor Chnage

          /*  if (arrayList.get(position).get("search_type").equalsIgnoreCase("0"))
            {
                Log.d("0001233","TYPE 0 ******");
                holder.tvFullName.setTextColor(ctx.getResources().getColor(R.color.dark_blue_color));
            }
            else if (arrayList.get(position).get("search_type").equalsIgnoreCase("1"))
            {
                Log.d("0001233","TYPE 1 ******");
                holder.tvFullName.setTextColor(ctx.getResources().getColor(R.color.dark_blue_color));

            }
           else if (arrayList.get(position).get("search_type").equalsIgnoreCase("2"))
            {
                Log.d("0001233","TYPE 2 ******");
                if (arrayList.get(position).get("vaccinated_on").equalsIgnoreCase("null"))
                {
                    holder.tvFullName.setTextColor(ctx.getResources().getColor(R.color.hp_listview_textview_redcolor));
                }
                else {
                    holder.tvFullName.setTextColor(ctx.getResources().getColor(R.color.green_color));
                }
            }*/
        }catch (Exception e)
        {
            Log.d("0001233","Adt Error: " +e.getMessage());
        }


        return view;
    }

    public static class ViewHolder {

        protected TextView tvID, tvFullName, tvDateTime;
        RelativeLayout rl;
        ImageView gender_icon;
    }
}

