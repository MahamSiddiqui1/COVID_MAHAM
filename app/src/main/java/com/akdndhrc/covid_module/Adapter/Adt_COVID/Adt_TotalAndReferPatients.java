package com.akdndhrc.covid_module.Adapter.Adt_COVID;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akdndhrc.covid_module.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Adt_TotalAndReferPatients extends BaseAdapter {
    Context ctx;
    ArrayList<HashMap<String, String>> arrayList;
    LayoutInflater inflater;


    public Adt_TotalAndReferPatients(Context ctx, ArrayList<HashMap<String, String>> arrayList) {
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

            view = inflater.inflate(R.layout.custom_total_and_refer_pat_layout, null);
            holder.tvCountTotalPatient = (TextView) view.findViewById(R.id.tvCountTotalPatient);
            holder.tvDate = (TextView) view.findViewById(R.id.tvDate);
            holder.tvTime = (TextView) view.findViewById(R.id.tvTime);
            holder.tvCountReferPatient = (TextView) view.findViewById(R.id.tvCountReferPatient);


            view.setTag(holder);
        } else {

            holder = (ViewHolder) view.getTag();

        }

        holder.tvCountTotalPatient.setText(arrayList.get(position).get("total_patients"));
        holder.tvCountReferPatient.setText(arrayList.get(position).get("refer_patients"));
        holder.tvDate.setText(arrayList.get(position).get("date"));
        holder.tvTime.setText(arrayList.get(position).get("time"));

        return view;
    }

    public static class ViewHolder {

        TextView tvCountTotalPatient, tvDate, tvTime, tvCountReferPatient;
        RelativeLayout rl;
    }
}

