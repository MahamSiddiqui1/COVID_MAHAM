package com.akdndhrc.covid_module.Adapter.Adt_Search;

import android.content.Context;
import android.os.Build;
import android.text.Html;
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



public class Adt_SearchFamilyMembersList extends BaseAdapter {
    private Context ctx;
    ArrayList<HashMap<String, String>> mArrayList = new ArrayList<HashMap<String, String>>();

    private LayoutInflater inflater = null;

    int newHeight = 80; // New height in pixels
    int newWidth = 80;

    int babyboy_height = 50;
    int babyboy_width = 45;

    // Constructor
    public Adt_SearchFamilyMembersList(Context ctx, ArrayList<HashMap<String, String>> mArrayList) {
        this.ctx = ctx;
        this.mArrayList = mArrayList;
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

            row = inflater.inflate(R.layout.custom_lv_search_family_member_list_layout, null);
            holder.sfml_lv_tvNaam = row.findViewById(R.id.sfml_lv_tvNaam);
            holder.sfml_lv_tvBirthDate = row.findViewById(R.id.sfml_lv_tvBirthDate);
            holder.sfml_lv_tvUmer = row.findViewById(R.id.sfml_lv_tvUmer);
            holder.sfml_lv_iv_gender_icon = row.findViewById(R.id.sfml_lv_iv_gender_icon);
            holder.sfml_lv_male_female_icon = row.findViewById(R.id.sfml_lv_male_female_icon);
            holder.sfml_lv_rl_background = row.findViewById(R.id.sfml_lv_rl_background);



            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.sfml_lv_tvNaam.setText(Html.fromHtml("" + mArrayList.get(pos).get("full_name")));
        holder.sfml_lv_tvBirthDate.setText(Html.fromHtml(""+ mArrayList.get(pos).get("age")) +" سال ");




        if (Integer.parseInt(mArrayList.get(pos).get("age")) <= 2)
            {
                if (pos % 2 == 0)
                {
                    if (mArrayList.get(pos).get("gender").equalsIgnoreCase("1"))
                    {
                        holder.sfml_lv_male_female_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_maleblue_icon));
                        holder.sfml_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.baby_boy_icon));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.sfml_lv_iv_gender_icon.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
                        }
                        holder.sfml_lv_iv_gender_icon.requestLayout();
                        holder.sfml_lv_iv_gender_icon.getLayoutParams().height = babyboy_height;
                        holder.sfml_lv_iv_gender_icon.getLayoutParams().width = babyboy_width;
                    }
                    else{
                        holder.sfml_lv_male_female_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_femalepink_icon));
                      //  holder.sfml_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_adult_female_50dp));
                        holder.sfml_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.baby_boy_icon));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.sfml_lv_iv_gender_icon.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.pink_color));
                        }

                        holder.sfml_lv_iv_gender_icon.requestLayout();
                        holder.sfml_lv_iv_gender_icon.getLayoutParams().height = babyboy_height;
                        holder.sfml_lv_iv_gender_icon.getLayoutParams().width = babyboy_width;
                    }
                    holder.sfml_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.afm_rl_background_color));
                }

                else {

                    if (mArrayList.get(pos).get("gender").equalsIgnoreCase("1"))
                    {
                        holder.sfml_lv_male_female_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_maleblue_icon));
                        holder.sfml_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.baby_boy_icon));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.sfml_lv_iv_gender_icon.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
                        }
                        holder.sfml_lv_iv_gender_icon.requestLayout();
                        holder.sfml_lv_iv_gender_icon.getLayoutParams().height = babyboy_height;
                        holder.sfml_lv_iv_gender_icon.getLayoutParams().width = babyboy_width;
                    }
                    else{

                        holder.sfml_lv_male_female_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_femalepink_icon));
                        holder.sfml_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.baby_boy_icon));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.sfml_lv_iv_gender_icon.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.pink_color));
                        }

                        holder.sfml_lv_iv_gender_icon.requestLayout();
                        holder.sfml_lv_iv_gender_icon.getLayoutParams().height = babyboy_height;
                        holder.sfml_lv_iv_gender_icon.getLayoutParams().width = babyboy_width;
                    }
                    holder.sfml_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.light_color_white));
                }
            }

        else if (Integer.parseInt(mArrayList.get(pos).get("age")) >= 3 && Integer.parseInt(mArrayList.get(pos).get("age")) <= 14)
        {
            if (pos % 2 == 0)
            {
                if (mArrayList.get(pos).get("gender").equalsIgnoreCase("1"))
                {
                    holder.sfml_lv_male_female_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_maleblue_icon));
                    holder.sfml_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.sfml_lv_iv_gender_icon.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
                    }
                }
                else{
                    holder.sfml_lv_male_female_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_femalepink_icon));
                    holder.sfml_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_adult_female_50dp));
                    holder.sfml_lv_iv_gender_icon.requestLayout();
                    holder.sfml_lv_iv_gender_icon.getLayoutParams().height = newHeight;
                    holder.sfml_lv_iv_gender_icon.getLayoutParams().width = newWidth;
                }
                holder.sfml_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.afm_rl_background_color));
            }

            else {
                if (mArrayList.get(pos).get("gender").equalsIgnoreCase("1"))
                {
                    holder.sfml_lv_male_female_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_maleblue_icon));
                    holder.sfml_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.sfml_lv_iv_gender_icon.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
                    }
                }
                else{
                    holder.sfml_lv_male_female_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_femalepink_icon));
                    holder.sfml_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_adult_female_50dp));
                    holder.sfml_lv_iv_gender_icon.requestLayout();
                    holder.sfml_lv_iv_gender_icon.getLayoutParams().height = newHeight;
                    holder.sfml_lv_iv_gender_icon.getLayoutParams().width = newWidth;

                }
                holder.sfml_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.light_color_white));
            }
        }


        else {

            if (pos % 2 == 0)
            {
                if (mArrayList.get(pos).get("gender").equalsIgnoreCase("1"))
                {

                    holder.sfml_lv_male_female_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.sfml_lv_male_female_icon.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
                    }
                    holder.sfml_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.sfml_lv_iv_gender_icon.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
                    }
                }
                else{
                    holder.sfml_lv_male_female_icon.setBackground(ctx.getResources().getDrawable(R.drawable.woman_icon));
                    holder.sfml_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_woman_icon));

                }

                holder.sfml_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.afm_rl_background_color));
            }

            else {
                if (mArrayList.get(pos).get("gender").equalsIgnoreCase("1"))
                {
                    holder.sfml_lv_male_female_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.sfml_lv_male_female_icon.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
                    }
                    holder.sfml_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.sfml_lv_iv_gender_icon.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
                    }
                }
                else{
                    holder.sfml_lv_male_female_icon.setBackground(ctx.getResources().getDrawable(R.drawable.woman_icon));
                    holder.sfml_lv_iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.ic_woman_icon));
                }
                holder.sfml_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.light_color_white));
            }

        }

        return row;
    }


    static class ViewHolder {
        TextView sfml_lv_tvNaam, sfml_lv_tvBirthDate , sfml_lv_tvUmer ;
        ImageView sfml_lv_iv_gender_icon , sfml_lv_male_female_icon;
        RelativeLayout sfml_lv_rl_background;

    }


}
