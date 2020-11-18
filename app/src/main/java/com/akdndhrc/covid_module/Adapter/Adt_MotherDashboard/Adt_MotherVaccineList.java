package com.akdndhrc.covid_module.Adapter.Adt_MotherDashboard;


import android.content.Context;
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



public class Adt_MotherVaccineList extends BaseAdapter {
    private Context ctx;
    ArrayList<String> arrayList_vaccine_name = new ArrayList<String>();
    ArrayList<String> arrayList_vaccine_date = new ArrayList<String>();

    private LayoutInflater inflater = null;

    // Constructor
    public Adt_MotherVaccineList(Context ctx, ArrayList<String> arrayList_vaccine_name,ArrayList<String> arrayList_vaccine_date) {
        this.ctx = ctx;
        this.arrayList_vaccine_name = arrayList_vaccine_name;
        this.arrayList_vaccine_date = arrayList_vaccine_date;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public int getCount() {
        return arrayList_vaccine_name.size();
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

            row = inflater.inflate(R.layout.custom_lv_mother_vaccine_list_layout, null);
            holder.lv_vaccine_name = row.findViewById(R.id.lv_vaccine_name);
            holder.lv_tvDateTime = row.findViewById(R.id.lv_tvDateTime);
            holder.lv_iv_vaccineIcon = row.findViewById(R.id.lv_iv_vaccineIcon);
            holder.mref_lv_rl_background = row.findViewById(R.id.mref_lv_rl_background);


            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }



        String[] vacine_name = arrayList_vaccine_date.get(pos).split("@");
        Log.d("000987","pos: " + vacine_name[0]);
        Log.d("000987","vaccinated_on: " + vacine_name[1]);
        Log.d("000987","type: " + vacine_name[2]);

        if (pos % 2 == 0) {

            if (vacine_name[2].equalsIgnoreCase("0")) {
                holder.lv_vaccine_name.setText(arrayList_vaccine_name.get(pos));
                holder.lv_tvDateTime.setText(vacine_name[1]);
                holder.lv_vaccine_name.setTextColor(ctx.getResources().getColor(R.color.purple_color));
                holder.lv_tvDateTime.setTextColor(ctx.getResources().getColor(R.color.purple_color));
                holder.lv_iv_vaccineIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.circle_shape_purple));

            } else if (vacine_name[2].equalsIgnoreCase("1")) {
                holder.lv_vaccine_name.setText(arrayList_vaccine_name.get(pos));
                holder.lv_tvDateTime.setText(vacine_name[1]);
                holder.lv_vaccine_name.setTextColor(ctx.getResources().getColor(R.color.graph_green_color));
                holder.lv_tvDateTime.setTextColor(ctx.getResources().getColor(R.color.graph_green_color));
                holder.lv_iv_vaccineIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.circle_shape_green));

            }
            else if (vacine_name[2].equalsIgnoreCase("2")) {
                holder.lv_vaccine_name.setText(arrayList_vaccine_name.get(pos));
                holder.lv_tvDateTime.setText(vacine_name[1]);
                holder.lv_vaccine_name.setTextColor(ctx.getResources().getColor(R.color.graph_red_color));
                holder.lv_tvDateTime.setTextColor(ctx.getResources().getColor(R.color.graph_red_color));
                holder.lv_iv_vaccineIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.circle_shape_red));
            }
            else{
                holder.lv_vaccine_name.setText(arrayList_vaccine_name.get(pos));
                holder.lv_tvDateTime.setText(vacine_name[1]);
                holder.lv_vaccine_name.setTextColor(ctx.getResources().getColor(R.color.grey_color));
                holder.lv_tvDateTime.setTextColor(ctx.getResources().getColor(R.color.grey_color));
                holder.lv_iv_vaccineIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.circle_shape_grey));
            }


        } else {
            holder.mref_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.color_white));

            if (vacine_name[2].equalsIgnoreCase("0")) {
                holder.lv_vaccine_name.setText(arrayList_vaccine_name.get(pos));
                holder.lv_tvDateTime.setText(vacine_name[1]);
                holder.lv_vaccine_name.setTextColor(ctx.getResources().getColor(R.color.purple_color));
                holder.lv_tvDateTime.setTextColor(ctx.getResources().getColor(R.color.purple_color));
                holder.lv_iv_vaccineIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.circle_shape_purple));

            } else if (vacine_name[2].equalsIgnoreCase("1")) {
                holder.lv_vaccine_name.setText(arrayList_vaccine_name.get(pos));
                holder.lv_tvDateTime.setText(vacine_name[1]);
                holder.lv_vaccine_name.setTextColor(ctx.getResources().getColor(R.color.graph_green_color));
                holder.lv_tvDateTime.setTextColor(ctx.getResources().getColor(R.color.graph_green_color));
                holder.lv_iv_vaccineIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.circle_shape_green));

            }
            else if (vacine_name[2].equalsIgnoreCase("2")) {
                holder.lv_vaccine_name.setText(arrayList_vaccine_name.get(pos));
                holder.lv_tvDateTime.setText(vacine_name[1]);
                holder.lv_vaccine_name.setTextColor(ctx.getResources().getColor(R.color.graph_red_color));
                holder.lv_tvDateTime.setTextColor(ctx.getResources().getColor(R.color.graph_red_color));
                holder.lv_iv_vaccineIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.circle_shape_red));
            }
            else{
                holder.lv_vaccine_name.setText(arrayList_vaccine_name.get(pos));
                holder.lv_tvDateTime.setText(vacine_name[1]);
                holder.lv_vaccine_name.setTextColor(ctx.getResources().getColor(R.color.grey_color));
                holder.lv_tvDateTime.setTextColor(ctx.getResources().getColor(R.color.grey_color));
                holder.lv_iv_vaccineIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.circle_shape_grey));
            }

        }


        return row;
    }


    static class ViewHolder {
        TextView lv_vaccine_name, lv_tvDateTime;
        ImageView lv_iv_vaccineIcon;
        RelativeLayout mref_lv_rl_background;

    }


}
