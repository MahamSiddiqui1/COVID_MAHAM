package com.akdndhrc.covid_module.Adapter.Adt_ChildDashboard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akdndhrc.covid_module.R;

import java.util.ArrayList;



public class Adt_ChildHifazitiTeekeyRecordList extends BaseAdapter {
    private Context ctx;
    //    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> arrayListVaccine;
    ArrayList<String> arrayListDate;

    private LayoutInflater inflater = null;

    // Constructor
    public Adt_ChildHifazitiTeekeyRecordList(Context ctx, ArrayList<String> arrayListVaccine, ArrayList<String> arrayListDate) {
        this.ctx = ctx;
        this.arrayListVaccine = arrayListVaccine;
        this.arrayListDate = arrayListDate;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public int getCount() {
        return arrayListDate.size();
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

            row = inflater.inflate(R.layout.custom_lv_child_hifaziti_teekey_record_list_layout, null);
            holder.chtr_lv_tvTareekh = row.findViewById(R.id.chtr_lv_tvTareekh);
            holder.chtr_lv_tvDate = row.findViewById(R.id.chtr_lv_tvDate);
            holder.chtr_lv_tvVaccineName = row.findViewById(R.id.chtr_lv_tvVaccineName);
            holder.chtr_lv_rl_background = row.findViewById(R.id.chtr_lv_rl_background);
            holder.chtr_lv_iv_vaccineIcon = row.findViewById(R.id.chtr_lv_iv_vaccineIcon);

            holder.view = row.findViewById(R.id.view);
            holder.tvHeading = row.findViewById(R.id.tvHeading);


            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }


        /*if (pos == 0)
        {
            holder.tvHeading.setText("With-in 24 Hours");
            holder.tvHeading.setVisibility(View.VISIBLE);
        }
        else  if (pos == 3)
        {
            holder.tvHeading.setText("In 6 Weeks");
            holder.tvHeading.setVisibility(View.VISIBLE);
        }
        else  if (pos == 7)
        {
            holder.tvHeading.setText("In 10 Weeks");
            holder.tvHeading.setVisibility(View.VISIBLE);
        }
        else  if (pos == 11)
        {
            holder.tvHeading.setText("In 14 Weeks");
            holder.tvHeading.setVisibility(View.VISIBLE);
        }

        else  if (pos == 16)
        {
            holder.tvHeading.setText("In 9 Months");
            holder.tvHeading.setVisibility(View.VISIBLE);
        }
       else  if (pos == 17)
        {
            holder.tvHeading.setText("In 15 Months");
            holder.tvHeading.setVisibility(View.VISIBLE);
        }
       else
        {
            holder.tvHeading.setVisibility(View.GONE);
            holder.tvHeading.setText("");
        }
*/
     /*   if (pos == 2) {
            holder.view.setVisibility(View.VISIBLE);
            // holder.view.setMinimumHeight(2);
        } else if (pos == 6) {
            holder.view.setVisibility(View.VISIBLE);
        } else if (pos == 10) {
            holder.view.setVisibility(View.VISIBLE);
        } else if (pos == 15) {
            holder.view.setVisibility(View.VISIBLE);

        } else if (pos == 16) {
            holder.view.setVisibility(View.VISIBLE);

        } else {
            holder.view.setVisibility(View.GONE);
        }*/

        if (pos % 2 == 0) {
            String[] vacine_name = arrayListVaccine.get(pos).split(",");

            if (vacine_name[1].equalsIgnoreCase("0")) {
                Log.d("000369", "VAC NAME: " + vacine_name[0] + " VAC TYPE: " + vacine_name[1] + " VAC DATE: " + vacine_name[2]);
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0] + "/" + vacine_name[2]);
                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.graph_green_color));
                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.graph_green_color));
                holder.chtr_lv_iv_vaccineIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.circle_shape_green));
            } else if (vacine_name[1].equalsIgnoreCase("1")) {
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0]);
                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.graph_orange_color));
                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.graph_orange_color));
                holder.chtr_lv_iv_vaccineIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.circle_shape_orange));

            } else if (vacine_name[1].equalsIgnoreCase("2")) {
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0]);
                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.graph_red_color));
                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.graph_red_color));
                holder.chtr_lv_iv_vaccineIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.circle_shape_red));

            } else if (vacine_name[1].equalsIgnoreCase("3")) {
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0]);
                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.grey_color));
                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.grey_color));
                holder.chtr_lv_iv_vaccineIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.circle_shape_grey));

            } else if (vacine_name[1].equalsIgnoreCase("5")) {
                Log.d("000369", "VAC NAME: " + vacine_name[0] + " VAC TYPE: " + vacine_name[1] + " VAC DATE: " + vacine_name[2]);
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0] + "/" + vacine_name[2]);
                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.purple_color));
                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.purple_color));
                holder.chtr_lv_iv_vaccineIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.circle_shape_purple));

            } else {
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0]);
                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.grey_color));
                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.grey_color));
                holder.chtr_lv_iv_vaccineIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.circle_shape_grey));

            }

            //  holder.chtr_lv_tvVaccineName.setText(arrayListVaccine.get(pos));
            //  holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));

        } else {

            String[] vacine_name = arrayListVaccine.get(pos).split(",");
            Log.d("000999", vacine_name[0] + vacine_name[1]);
            if (vacine_name[1].equalsIgnoreCase("0")) {
                Log.d("000369", "VAC NAME: " + vacine_name[0] + " VAC TYPE: " + vacine_name[1] + " VAC DATE: " + vacine_name[2]);
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0] + "/" + vacine_name[2]);
                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.graph_green_color));
                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.graph_green_color));
                holder.chtr_lv_iv_vaccineIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.circle_shape_green));

            } else if (vacine_name[1].equalsIgnoreCase("1")) {
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0]);
                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.graph_orange_color));
                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.graph_orange_color));
                holder.chtr_lv_iv_vaccineIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.circle_shape_orange));
            } else if (vacine_name[1].equalsIgnoreCase("2")) {
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0]);
                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.graph_red_color));
                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.graph_red_color));
                holder.chtr_lv_iv_vaccineIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.circle_shape_red));
            } else if (vacine_name[1].equalsIgnoreCase("3")) {
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0]);
                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.grey_color));
                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.grey_color));
                holder.chtr_lv_iv_vaccineIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.circle_shape_grey));
            } else if (vacine_name[1].equalsIgnoreCase("5")) {
                Log.d("000369", "VAC NAME: " + vacine_name[0] + " VAC TYPE: " + vacine_name[1] + " VAC DATE: " + vacine_name[2]);
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0] + "/" + vacine_name[2]);
                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.purple_color));
                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.purple_color));
                holder.chtr_lv_iv_vaccineIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.circle_shape_purple));

            } else {
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0]);
                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.grey_color));
                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.grey_color));
                holder.chtr_lv_iv_vaccineIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.circle_shape_grey));
            }

            //          holder.chtr_lv_tvVaccineName.setText(arrayListVaccine.get(pos));
//            holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));

            // holder.chtr_lv_tvVaccineName.setText(arrayListVaccine.get(pos));
            // holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
            holder.chtr_lv_rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.color_white));


        }


        return row;
    }


    static class ViewHolder {
        TextView chtr_lv_tvTareekh, chtr_lv_tvDate, chtr_lv_tvVaccineName,tvHeading;
        RelativeLayout chtr_lv_rl_background;
        ImageView chtr_lv_iv_vaccineIcon;
        View view;

    }


}
