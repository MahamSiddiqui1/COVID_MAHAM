package com.akdndhrc.covid_module.Adapter.Adt_ChildDashboard;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.akdndhrc.covid_module.R;

import java.util.ArrayList;



public class Adt_ChildHifazatiTeekeyRecordList_KahiAurSy extends BaseAdapter {
    private Context ctx;
    //    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> arrayListVaccine;
    ArrayList<String> arrayListDate;

    private LayoutInflater inflater = null;

    // Constructor
    public Adt_ChildHifazatiTeekeyRecordList_KahiAurSy(Context ctx, ArrayList<String> arrayListVaccine, ArrayList<String> arrayListDate) {
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

            row = inflater.inflate(R.layout.custom_lv_child_hifazati_record_list_kahi_aur_sy_layout, null);
            holder.chtr_lv_tvTareekh = row.findViewById(R.id.chtr_lv_tvTareekh);

            holder.chtr_lv_tvVaccineName = row.findViewById(R.id.chtr_lv_tvVaccineName);
            holder.chtr_lv_rl_background = row.findViewById(R.id.chtr_lv_rl_background);
            holder.chtr_lv_rl_checkbox = row.findViewById(R.id.checkbox);

            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        if (pos % 2 == 0) {

            String[] vacine_name = arrayListVaccine.get(pos).split(",");

            /*Log.d("000999", "VACCINE_NAM: " + vacine_name[0].split("@")[0]);
            Log.d("000999", "VACCINE_NAME PERSIAN: " + vacine_name[0].split("@")[1]);
            Log.d("000999", "VACCINE NUMBR:" + vacine_name[1]);*/

//            vaccine_name[0] is Vaccines Name
//            vaccine_name[1] is POsition


            if (vacine_name[1].equalsIgnoreCase("0")) {
                Log.d("000369", "VAC NAME: " + vacine_name[0] + " VAC TYPE: " + vacine_name[1] + " VAC DATE: " + vacine_name[2]);
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0] + "/" + vacine_name[2]);
                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_rl_checkbox.setChecked(true);
                holder.chtr_lv_rl_checkbox.setClickable(false);
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.green_color));

                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.green_color));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.chtr_lv_rl_checkbox.setButtonTintList(ctx.getResources().getColorStateList(R.color.green_color));
                }
                holder.chtr_lv_rl_checkbox.setHighlightColor(0x469519);
            } else if (vacine_name[1].equalsIgnoreCase("1"))

            {
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0]);

                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.graph_orange_color));

                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.graph_orange_color));
                holder.chtr_lv_rl_checkbox.setChecked(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.chtr_lv_rl_checkbox.setButtonTintList(ctx.getResources().getColorStateList(R.color.graph_orange_color));
                }

            } else if (vacine_name[1].equalsIgnoreCase("2")) {
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0]);

                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.hp_listview_textview_redcolor));

                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.hp_listview_textview_redcolor));
                holder.chtr_lv_rl_checkbox.setChecked(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.chtr_lv_rl_checkbox.setButtonTintList(ctx.getResources().getColorStateList(R.color.chtr_poliyou_red_txt_color));
                }

            } else if (vacine_name[1].equalsIgnoreCase("3"))

            {
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0]);

                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.grey_color));

                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.grey_color));
                holder.chtr_lv_rl_checkbox.setChecked(false);
                holder.chtr_lv_rl_checkbox.setHighlightColor(Color.parseColor("#929292"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.chtr_lv_rl_checkbox.setButtonTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                }


            }

            else if (vacine_name[1].equalsIgnoreCase("5")) {
                Log.d("000369", "VAC NAME: " + vacine_name[0] + " VAC TYPE: " + vacine_name[1] + " VAC DATE: " + vacine_name[2]);
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0] + "/" + vacine_name[2]);
                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_rl_checkbox.setChecked(true);
                holder.chtr_lv_rl_checkbox.setClickable(false);
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.purple_color));

                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.purple_color));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.chtr_lv_rl_checkbox.setButtonTintList(ctx.getResources().getColorStateList(R.color.purple_color));
                }
            }
            else {
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0].split("@")[0]);

                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.grey_color));

                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.grey_color));
                holder.chtr_lv_rl_checkbox.setHighlightColor(Color.parseColor("#929292"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.chtr_lv_rl_checkbox.setButtonTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                }

                //   holder.chtr_lv_rl_checkbox.setButtonDrawable(R.drawable.circle_shape_grey);

            }

            //  holder.chtr_lv_tvVaccineName.setText(arrayListVaccine.get(pos));
            //  holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));

        } else {


            String[] vacine_name = arrayListVaccine.get(pos).split(",");

           /* Log.d("000999", "VACCINE_NAM: " + vacine_name[0].split("@")[0]);
            Log.d("000999", "VACCINE_NAME PERSIAN: " + vacine_name[0].split("@")[1]);
            Log.d("000999", "VACCINE NUMBR:" + vacine_name[1]);*/

            if (vacine_name[1].equalsIgnoreCase("0")) {
                Log.d("000369", "VAC NAME: " + vacine_name[0] + " VAC TYPE: " + vacine_name[1] + " VAC DATE: " + vacine_name[2]);
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0] + "/" + vacine_name[2]);

                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_rl_checkbox.setChecked(true);
                holder.chtr_lv_rl_checkbox.setClickable(false);
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.green_color));

                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.green_color));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.chtr_lv_rl_checkbox.setButtonTintList(ctx.getResources().getColorStateList(R.color.green_color));
                }

            } else if (vacine_name[1].equalsIgnoreCase("1")) {
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0]);

                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.graph_orange_color));

                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.graph_orange_color));
                holder.chtr_lv_rl_checkbox.setChecked(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.chtr_lv_rl_checkbox.setButtonTintList(ctx.getResources().getColorStateList(R.color.graph_orange_color));
                }

                //holder.chtr_lv_rl_checkbox.setButtonDrawable(R.drawable.circle_shape_orange);
            } else if (vacine_name[1].equalsIgnoreCase("2")) {
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0]);

                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.hp_listview_textview_redcolor));

                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.hp_listview_textview_redcolor));
                holder.chtr_lv_rl_checkbox.setChecked(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.chtr_lv_rl_checkbox.setButtonTintList(ctx.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                }
               // holder.chtr_lv_rl_checkbox.setButtonDrawable(R.drawable.circle_shape_red);
            } else if (vacine_name[1].equalsIgnoreCase("3")) {
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0]);

                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.grey_color));

                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.grey_color));
                holder.chtr_lv_rl_checkbox.setChecked(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.chtr_lv_rl_checkbox.setButtonTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                }

                //holder.chtr_lv_rl_checkbox.setButtonDrawable(R.drawable.circle_shape_grey);
            }


            else if (vacine_name[1].equalsIgnoreCase("5")) {
                Log.d("000369", "VAC NAME: " + vacine_name[0] + " VAC TYPE: " + vacine_name[1] + " VAC DATE: " + vacine_name[2]);
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0] + "/" + vacine_name[2]);
                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_rl_checkbox.setChecked(true);
                holder.chtr_lv_rl_checkbox.setClickable(false);
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.purple_color));

                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.purple_color));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.chtr_lv_rl_checkbox.setButtonTintList(ctx.getResources().getColorStateList(R.color.purple_color));
                }
            }
            else {
                holder.chtr_lv_tvVaccineName.setText(vacine_name[0]);

                holder.chtr_lv_tvTareekh.setText(arrayListDate.get(pos));
                holder.chtr_lv_tvVaccineName.setTextColor(ctx.getResources().getColor(R.color.grey_color));

                holder.chtr_lv_tvTareekh.setTextColor(ctx.getResources().getColor(R.color.grey_color));
                holder.chtr_lv_rl_checkbox.setChecked(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.chtr_lv_rl_checkbox.setButtonTintList(ctx.getResources().getColorStateList(R.color.grey_color));
                }
                //    holder.chtr_lv_rl_checkbox.setButtonDrawable(R.drawable.circle_shape_grey);
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
        TextView chtr_lv_tvTareekh, chtr_lv_tvDate, chtr_lv_tvVaccineName;
        RelativeLayout chtr_lv_rl_background;
        AppCompatCheckBox chtr_lv_rl_checkbox;

        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_empty}, // red
                new int[]{-android.R.attr.state_active}, // orange
                new int[]{-android.R.attr.state_checked}, //green
                new int[]{}  // grey
        };

        int[] colors = new int[]{
                0xCC4747,
                0xffff8800,
                0x469519,
                0x929292
        };

        ColorStateList colorStateList = new ColorStateList(states, colors);

    }


}

