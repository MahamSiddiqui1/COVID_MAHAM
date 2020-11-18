package com.akdndhrc.covid_module.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akdndhrc.covid_module.R;

import java.util.ArrayList;
import java.util.HashMap;



public class Practise_Adptr extends BaseAdapter {
    private Context ctx;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayList2 = new ArrayList<>();

    private LayoutInflater inflater = null;

    // Constructor
    public Practise_Adptr(Context ctx, ArrayList<String> arrayList) {
        this.ctx = ctx;
        this.arrayList = arrayList;
       // this.arrayList2 = arrayList2;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public int getCount() {
        return arrayList.size();
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

            row = inflater.inflate(R.layout.custom_lv_child_refferal_record_list_layout, null);
            holder.crl_lv_tvRefferalNumber = row.findViewById(R.id.crl_lv_tvRefferalNumber);
            holder.crl_lv_tvDateTime = row.findViewById(R.id.crl_lv_tvDateTime);
            holder.crl_lv_rl_background = row.findViewById(R.id.crl_lv_rl_background);

            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }


        String[] abc = arrayList.get(pos).split("@");

        if (abc[1].equalsIgnoreCase(",0")){
            holder.crl_lv_tvDateTime.setVisibility(View.GONE);
            holder.crl_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.light_blue_color));
            holder.crl_lv_tvRefferalNumber.setText(Html.fromHtml("" + abc[0]));
            holder.crl_lv_tvRefferalNumber.setTextColor(ctx.getResources().getColor(R.color.graph_orange_color));
            holder.crl_lv_tvRefferalNumber.setGravity(Gravity.CENTER);
            holder.crl_lv_tvRefferalNumber.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            holder.crl_lv_tvRefferalNumber.setTextSize(20);
        }
        else   if (abc[1].equalsIgnoreCase(",1")){
            holder.crl_lv_tvRefferalNumber.setText(Html.fromHtml("" + abc[0]));
            holder.crl_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.color_white));
            holder.crl_lv_tvRefferalNumber.setTextColor(ctx.getResources().getColor(R.color.green_color));

            //   holder.crl_lv_tvRefferalNumber.setText(Html.fromHtml("" + arrayList2.get(pos)));
        }
        return row;
    }


    static class ViewHolder {
        TextView crl_lv_tvRefferalNumber, crl_lv_tvDateTime;
        RelativeLayout crl_lv_rl_background;

    }


}
