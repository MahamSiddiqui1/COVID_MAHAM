package com.akdndhrc.covid_module.Adapter.Adt_ChildDashboard;

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


public class Adt_ChildBemaariRecordList extends BaseAdapter {
    private Context ctx;
    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    private LayoutInflater inflater = null;

    // Constructor
    public Adt_ChildBemaariRecordList(Context ctx, ArrayList<HashMap<String, String>> hashMapArrayList) {
        this.ctx = ctx;
        this.hashMapArrayList = hashMapArrayList;
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

    @Override
    public View getView(final int pos, View row, ViewGroup viewGroup) {

        final ViewHolder holder;
        if (row == null) {
            holder = new ViewHolder();

            row = inflater.inflate(R.layout.custom_lv_child_bemaari_record_list_layout, null);
            holder.cbr_lv_tvNaam = row.findViewById(R.id.mkmbl_lv_tvNaam);
            holder.cbr_lv_tvDateTime = row.findViewById(R.id.mkmbl_lv_tvDateTime);
            holder.cbr_lv_rl_background = row.findViewById(R.id.mkmbl_lv_rl_background);




            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }


        if (pos % 2 == 0) {



            holder.cbr_lv_tvNaam.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("i")));
            holder.cbr_lv_tvDateTime.setText(Html.fromHtml("" +  hashMapArrayList.get(pos).get("nasho_numa")));

        }
        else {

            holder.cbr_lv_tvNaam.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("i")));
            holder.cbr_lv_tvDateTime.setText(Html.fromHtml("" +hashMapArrayList.get(pos).get("nasho_numa")) );
            holder.cbr_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.color_white));

        }

        return row;
    }


    static class ViewHolder {
        TextView cbr_lv_tvNaam, cbr_lv_tvDateTime;
        RelativeLayout cbr_lv_rl_background;
        ImageView cbr_lv_iv_tablet_reffer , cbr_lv_iv_refer;

    }


}
