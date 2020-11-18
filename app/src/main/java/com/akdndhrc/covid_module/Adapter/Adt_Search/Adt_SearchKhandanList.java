package com.akdndhrc.covid_module.Adapter.Adt_Search;

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



public class Adt_SearchKhandanList extends BaseAdapter {
    private Context ctx;
    ArrayList<HashMap<String, String>> mArrayList = new ArrayList<HashMap<String, String>>();

    private LayoutInflater inflater = null;

    // Constructor
    public Adt_SearchKhandanList(Context ctx, ArrayList<HashMap<String, String>> mArrayList) {
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

                row = inflater.inflate(R.layout.custom_lv_search_khandan_number_list_layout, null);
                holder.sknl_lv_tvNaam = row.findViewById(R.id.sknl_lv_tvNaam);
                holder.sknl_lv_tvkhandanNumber = row.findViewById(R.id.sknl_lv_tvkhandanNumber);
                holder.sknl_lv_tvfaAl = row.findViewById(R.id.sknl_lv_tvfaAl);
                holder.sknl_lv_tvGaon = row.findViewById(R.id.sknl_lv_tvGaon);

                holder.rl_left_topbottomcorner = row.findViewById(R.id.rl_left_topbottomcorner);
                holder.sknl_lv_rl_background = row.findViewById(R.id.sknl_lv_rl_background);


            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        if (pos % 2 == 0) {

            holder.sknl_lv_tvkhandanNumber.setText(Html.fromHtml("" + mArrayList.get(pos).get("manual_id")));
            holder.sknl_lv_tvGaon.setText(Html.fromHtml("" + mArrayList.get(pos).get("village_name")));
           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.sknl_lv_rl_background.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.color_white));
            }*/

        } else {

            holder.sknl_lv_tvkhandanNumber.setText(Html.fromHtml("" + mArrayList.get(pos).get("manual_id")));
            holder.sknl_lv_tvGaon.setText(Html.fromHtml("" + mArrayList.get(pos).get("village_name")));

           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.sknl_lv_rl_background.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.afm_rl_background_color));
            }*/
        }



        return row;
    }


    static class ViewHolder {
        TextView sknl_lv_tvNaam, sknl_lv_tvGaon, sknl_lv_tvfaAl, sknl_lv_tvkhandanNumber;
       RelativeLayout sknl_lv_rl_background,rl_left_topbottomcorner;

    }


}
