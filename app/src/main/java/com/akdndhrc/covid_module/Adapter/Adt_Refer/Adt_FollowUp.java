package com.akdndhrc.covid_module.Adapter.Adt_Refer;

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

public class Adt_FollowUp extends BaseAdapter {
    Context ctx;
    ArrayList<HashMap<String, String>> arrayList;
    LayoutInflater inflater;


    public Adt_FollowUp(Context ctx, ArrayList<HashMap<String, String>> arrayList) {
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

            view = inflater.inflate(R.layout.custom_not_followup_child_layout, null);
            holder.tvName = (TextView) view.findViewById(R.id.tvName);
            holder.tvDate = (TextView) view.findViewById(R.id.tvDate);
            holder.tvTime = (TextView) view.findViewById(R.id.tvTime);
            holder.tvReferalReason = (TextView) view.findViewById(R.id.tvReferalReason);
            holder.tvType = (TextView) view.findViewById(R.id.tvType);

            view.setTag(holder);
        } else {

            holder = (ViewHolder) view.getTag();

        }

        holder.tvName.setText(arrayList.get(position).get("full_name"));
        holder.tvDate.setText(arrayList.get(position).get("date"));
        holder.tvTime.setText(arrayList.get(position).get("time"));
        holder.tvReferalReason.setText(arrayList.get(position).get("referal_reason"));
        holder.tvType.setText(arrayList.get(position).get("type"));

        return view;
    }

    public static class ViewHolder {

        TextView tvName, tvDate, tvTime, tvReferalReason,tvType;
        RelativeLayout rl;
    }
}

