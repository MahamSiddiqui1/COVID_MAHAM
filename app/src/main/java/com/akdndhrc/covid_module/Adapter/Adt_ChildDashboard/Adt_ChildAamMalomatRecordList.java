package com.akdndhrc.covid_module.Adapter.Adt_ChildDashboard;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akdndhrc.covid_module.R;

import java.util.ArrayList;
import java.util.HashMap;



public class Adt_ChildAamMalomatRecordList extends BaseAdapter {
    private Context ctx;
    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    private LayoutInflater inflater = null;

    // Constructor
    public Adt_ChildAamMalomatRecordList(Context ctx, ArrayList<HashMap<String, String>> hashMapArrayList) {
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

            row = inflater.inflate(R.layout.custom_lv_aam_malomat_record_list_layout, null);
            holder.cam_lv_tvNaam = row.findViewById(R.id.cam_lv_tvNaam);
            holder.cam_lv_tvDateTime = row.findViewById(R.id.cam_lv_tvDateTime);
            holder.cam_lv_rl_background = row.findViewById(R.id.cam_lv_rl_background);


            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        if (pos % 2 == 0) {

            holder.cam_lv_tvNaam.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("i")));
            holder.cam_lv_tvDateTime.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("aam_malomat")));

        } else {

            holder.cam_lv_tvNaam.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("i")));
            holder.cam_lv_tvDateTime.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("aam_malomat")));
            holder.cam_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.color_white));

        }


        return row;
    }


    static class ViewHolder {
        TextView cam_lv_tvNaam, cam_lv_tvDateTime;
        RelativeLayout cam_lv_rl_background;

    }


}
