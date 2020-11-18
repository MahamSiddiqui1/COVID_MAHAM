package com.akdndhrc.covid_module.Adapter;

import android.content.Context;
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



public class Adt_Homepage extends BaseAdapter {
    private Context ctx;
    ArrayList<HashMap<String, String>> mArrayList = new ArrayList<HashMap<String, String>>();

    private LayoutInflater inflater = null;

    // Constructor
    public Adt_Homepage(Context ctx, ArrayList<HashMap<String, String>> mArrayList) {
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

            row = inflater.inflate(R.layout.custom_lv_gaon_talash_kre_red_layout, null);
            holder.hp_lv_tvNaam = row.findViewById(R.id.hp_lv_tvNaam);
            holder.hp_lv_tvTareekh = row.findViewById(R.id.hp_lv_tvTareekh);
            holder.hp_lv_tvTareekh_Adaegi = row.findViewById(R.id.hp_lv_tvTareekh_Adaegi);
            holder.hp_lv_tvGaon = row.findViewById(R.id.hp_lv_tvGaon);
            holder.iv_gender_icon = row.findViewById(R.id.iv_gender_icon);


            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        if (pos % 2 == 0) {

            holder.iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.mother_icon));
            holder.hp_lv_tvNaam.setText(Html.fromHtml("" + mArrayList.get(pos).get("mother_name")));

        } else {

            holder.iv_gender_icon.setBackground(ctx.getResources().getDrawable(R.drawable.baby_boy_icon));
            holder.hp_lv_tvNaam.setText(Html.fromHtml("" + mArrayList.get(pos).get("child_name")));
        }


        return row;
    }


    static class ViewHolder {
        TextView hp_lv_tvNaam, hp_lv_tvTareekh, hp_lv_tvTareekh_Adaegi, hp_lv_tvGaon;
        RelativeLayout rl_left_topbottomcorner, rl_background;
        ImageView iv_gender_icon;

    }


}
