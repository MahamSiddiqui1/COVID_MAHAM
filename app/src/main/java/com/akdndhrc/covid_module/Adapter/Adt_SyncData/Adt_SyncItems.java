package com.akdndhrc.covid_module.Adapter.Adt_SyncData;

import android.content.Context;
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

/**
 * Created by mani on 25/08/2017.
 */

public class Adt_SyncItems extends BaseAdapter {

    Context ctx;
    ArrayList<HashMap<String, String>> arrayList;
    LayoutInflater inflater;


    public Adt_SyncItems(Context ctx, ArrayList<HashMap<String, String>> arrayList) {
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

            view = inflater.inflate(R.layout.custom_lv_item_sync_module, null);
            holder.tvID = (TextView) view.findViewById(R.id.tvID);
            holder.tvFullName = (TextView) view.findViewById(R.id.tvName);
            holder.tvDateTime = (TextView) view.findViewById(R.id.tvDateTime);
            holder.rl = (RelativeLayout) view.findViewById(R.id.rl);
            holder.iv_synced_status = (ImageView) view.findViewById(R.id.iv_synced_status);

            view.setTag(holder);
        } else {

            holder = (ViewHolder) view.getTag();

        }

        holder.tvFullName.setText(arrayList.get(position).get("name"));
        holder.tvID.setText("ID: " + arrayList.get(position).get("Id"));
        holder.tvDateTime.setText(arrayList.get(position).get("datetime"));


        try {
            if (arrayList.get(position).get("is_synced").equalsIgnoreCase("0"))
            {
                holder.iv_synced_status.setVisibility(View.GONE);
            }
            else if (arrayList.get(position).get("is_synced").equalsIgnoreCase("0,3"))
            {
                holder.iv_synced_status.setVisibility(View.VISIBLE);
                holder.iv_synced_status.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_sms_black_24dp));
            }
            /*else if (arrayList.get(position).get("is_synced").equalsIgnoreCase("1,0"))
            {
                holder.iv_synced_status.setVisibility(View.VISIBLE);
                holder.iv_synced_status.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_wifi_black_24dp));
            }*/
            else {
                    holder.iv_synced_status.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            Log.d("000123","AdtSync Err: " +e.getMessage());
        }

        return view;
    }

    public static class ViewHolder {

        protected TextView tvID, tvFullName, tvDateTime;
        RelativeLayout rl;
        ImageView iv_synced_status;
    }
}

