package com.akdndhrc.covid_module.Adapter.Adt_SocialContact;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.akdndhrc.covid_module.R;

import java.util.ArrayList;
import java.util.HashMap;


public class Adt_HESchoolList extends BaseAdapter {
    private Context ctx;
    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    private LayoutInflater inflater = null;

    // Constructor
    public Adt_HESchoolList(Context ctx, ArrayList<HashMap<String, String>> hashMapArrayList) {
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int pos, View row, ViewGroup viewGroup) {

        final ViewHolder holder;
        if (row == null) {
            holder = new ViewHolder();

            row = inflater.inflate(R.layout.custom_lv_hes_school_list_layout, null);

            //TextView
            holder.tvSchoolName = row.findViewById(R.id.tvSchoolName);
            holder.tvSchoolHeadName = row.findViewById(R.id.tvSchoolHeadName);
            holder.iv_icon = row.findViewById(R.id.iv_icon);

            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

            holder.tvSchoolName.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("school_name")));
            holder.tvSchoolHeadName.setText(Html.fromHtml("" +hashMapArrayList.get(pos).get("school_headname")));


        return row;
    }


    static class ViewHolder {
        TextView tvSchoolName, tvSchoolHeadName;
        ImageView iv_icon;

    }


}
