package com.akdndhrc.covid_module.Adapter.Adt_MotherDashboard;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akdndhrc.covid_module.R;

import java.util.ArrayList;
import java.util.HashMap;



public class Adt_MotherHaamlaRecordList extends BaseAdapter {
    private Context ctx;
    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    private LayoutInflater inflater = null;

    // Constructor
    public Adt_MotherHaamlaRecordList(Context ctx, ArrayList<HashMap<String, String>> hashMapArrayList) {
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

            row = inflater.inflate(R.layout.custom_lv_mother_haamla_record_list_layout, null);
            holder.mhr_lv_tvHaaml = row.findViewById(R.id.mhr_lv_tvHaaml);
            holder.mhr_lv_tvFaAl = row.findViewById(R.id.mhr_lv_tvFaAl);
            holder.mhr_lv_tvLMP = row.findViewById(R.id.mhr_lv_tvLMP);
            holder.mhr_lv_tvEDD = row.findViewById(R.id.mhr_lv_tvEDD);
            holder.mhr_lv_rl_background = row.findViewById(R.id.mhr_lv_rl_background);
            holder.mhr_lv_imageFaAl = row.findViewById(R.id.mhr_lv_imageFaAl);
            holder.mhr_lv_tvLMPDate = row.findViewById(R.id.mhr_lv_tvLMPDate);
            holder.mhr_lv_tvDateTime = row.findViewById(R.id.mhr_lv_tvDateTime);
            holder.mhr_lv_tvEDDDate = row.findViewById(R.id.mhr_lv_tvEDDDate);
            holder.mhr_lv_tvTime = row.findViewById(R.id.mhr_lv_tvTime);



            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }


        if (pos % 2 == 0) {

           // holder.mhr_lv_tvHaaml.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("haaml_txt")));
            String[] break_preg = hashMapArrayList.get(pos).get("haaml_txt").split(",");
            holder.mhr_lv_tvLMPDate.setText(break_preg[0]);
            holder.mhr_lv_tvEDDDate.setText(break_preg[1]);
            holder.mhr_lv_tvDateTime.setText(break_preg[2]);
            holder.mhr_lv_tvTime.setText(hashMapArrayList.get(pos).get("time"));


        } else {


            String[] break_preg = hashMapArrayList.get(pos).get("haaml_txt").split(",");
            holder.mhr_lv_tvLMPDate.setText(break_preg[0]);
            holder.mhr_lv_tvEDDDate.setText(break_preg[1]);
            holder.mhr_lv_tvDateTime.setText(break_preg[2]);
            holder.mhr_lv_tvTime.setText(hashMapArrayList.get(pos).get("time"));


            holder.mhr_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.color_white));

        }

        return row;
    }


    static class ViewHolder {
        TextView mhr_lv_tvHaaml, mhr_lv_tvFaAl , mhr_lv_tvDateTime , mhr_lv_tvLMP , mhr_lv_tvLMPDate , mhr_lv_tvEDD , mhr_lv_tvEDDDate,mhr_lv_tvTime;
        ImageView mhr_lv_imageFaAl;
        LinearLayout mhr_lv_rl_background;

    }


}
