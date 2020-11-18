package com.akdndhrc.covid_module.Adapter.Adt_MotherDashboard;

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



public class Adt_MotherBemaariRecordList extends BaseAdapter {
    private Context ctx;
    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    private LayoutInflater inflater = null;

    // Constructor
    public Adt_MotherBemaariRecordList(Context ctx, ArrayList<HashMap<String, String>> hashMapArrayList) {
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

            row = inflater.inflate(R.layout.custom_lv_mother_bemaari_record_list_layout, null);
            holder.mbr_lv_tvNaam = row.findViewById(R.id.mbr_lv_tvNaam);
            holder.mbr_lv_tvDateTime = row.findViewById(R.id.mbr_lv_tvDateTime);
            holder.mbr_lv_rl_background = row.findViewById(R.id.mbr_lv_rl_background);


            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }


        if (pos % 2 == 0) {


            holder.mbr_lv_tvNaam.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("i")));
            holder.mbr_lv_tvDateTime.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("bemaari_record_date")));

        }
        else {


            holder.mbr_lv_tvNaam.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("i")));
            holder.mbr_lv_tvDateTime.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("bemaari_record_date")));
            holder.mbr_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.color_white));

        }



        return row;
    }


    static class ViewHolder {
        TextView mbr_lv_tvNaam, mbr_lv_tvDateTime;
        RelativeLayout mbr_lv_rl_background;
        ImageView mbr_lv_iv_tablet_reffer , mbr_lv_iv_refer;

    }


}
