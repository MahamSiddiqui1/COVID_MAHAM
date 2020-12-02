package com.akdndhrc.covid_module.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akdndhrc.covid_module.R;

import java.util.HashMap;
import java.util.List;

public class Adt_ExpandableList extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public Adt_ExpandableList(Context context, List<String> listDataHeader,
                              HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        final ViewHolder holder;
        final String childText = (String) getChild(groupPosition, childPosition);


        Log.d("000999","Name: "+childText.split("@")[1]);
        Log.d("000999","Gender: "+childText.split("@")[2]);

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.custom_lv_gaon_talash_kre_red_layout, null);

            holder.hp_lv_tvNaam = (TextView) convertView.findViewById(R.id.hp_lv_tvNaam);
            holder.hp_lv_tvGaon = (TextView) convertView.findViewById(R.id.hp_lv_tvGaon);
           // holder.hp_lv_tvGaon = (TextView) convertView.findViewById(R.id.hp_lv_tvGaon);
            holder.iv_gender_icon = (ImageView) convertView.findViewById(R.id.iv_gender_icon);
            holder.rl_background = (RelativeLayout) convertView.findViewById(R.id.rl_background);
            holder.rl_left_topbottomcorner = (RelativeLayout) convertView.findViewById(R.id.rl_left_topbottomcorner);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();}



        holder.hp_lv_tvNaam.setText(childText.split("@")[1]);
        //holder.hp_lv_tvNaam.setTextColor(_context.getResources().getColor(R.color.dark_blue_color));

      //  holder.hp_lv_tvGaon.setText(childText.split("@")[4]);
        holder.hp_lv_tvGaon.setVisibility(View.GONE);

        if (Integer.parseInt(childText.split("@")[3]) < 3)
        {
            if (childPosition % 2 == 0)
            {
                if (childText.split("@")[2].equalsIgnoreCase("1"))
                {
                    holder.iv_gender_icon.setBackground(_context.getResources().getDrawable(R.drawable.ic_maleblue_icon));
                }
                else{
                    holder.iv_gender_icon.setBackground(_context.getResources().getDrawable(R.drawable.ic_femalepink_icon));
                }

                holder.rl_background.setBackgroundColor(_context.getResources().getColor(R.color.color_white));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.rl_left_topbottomcorner.setBackgroundTintList(_context.getResources().getColorStateList(R.color.light_blue_color));
                }
            }

            else {

                if (childText.split("@")[2].equalsIgnoreCase("1"))
                {
                    holder.iv_gender_icon.setBackground(_context.getResources().getDrawable(R.drawable.ic_maleblue_icon));
                }

                else{
                    holder.iv_gender_icon.setBackground(_context.getResources().getDrawable(R.drawable.ic_femalepink_icon));
                }

                holder.rl_background.setBackgroundColor(_context.getResources().getColor(R.color.light_pink_color));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.rl_left_topbottomcorner.setBackgroundTintList(_context.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                }
            }

        }

        else {

            if (groupPosition==2)
            {
                holder.iv_gender_icon.setBackground(_context.getResources().getDrawable(R.drawable.ic_pregnancy_icon));
                if (childPosition % 2 == 0) {

                    holder.rl_background.setBackgroundColor(_context.getResources().getColor(R.color.light_pink_color));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.rl_left_topbottomcorner.setBackgroundTintList(_context.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                    }
                } else {

                    holder.rl_background.setBackgroundColor(_context.getResources().getColor(R.color.color_white));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.rl_left_topbottomcorner.setBackgroundTintList(_context.getResources().getColorStateList(R.color.hp_listview_textview_bluecolor));
                    }
                }
            }

            else{
                holder.iv_gender_icon.setBackground(_context.getResources().getDrawable(R.drawable.ic_woman_icon));
                if (childPosition % 2 == 0) {

                    holder.rl_background.setBackgroundColor(_context.getResources().getColor(R.color.light_pink_color));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.rl_left_topbottomcorner.setBackgroundTintList(_context.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                    }
                } else {

                    holder.rl_background.setBackgroundColor(_context.getResources().getColor(R.color.color_white));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.rl_left_topbottomcorner.setBackgroundTintList(_context.getResources().getColorStateList(R.color.hp_listview_textview_bluecolor));
                    }
                }
            }

        }


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        //return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
        List childList = _listDataChild.get(_listDataHeader.get(groupPosition));
        if (childList != null && ! childList.isEmpty()) {
            return childList.size();
        }
        return 0;

    }


    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvCount = (TextView) convertView.findViewById(R.id.tvCount);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        //lblListHeader.setText(getGroup(groupPosition).toString());
        lblListHeader.setText(getGroup(groupPosition).toString().split("@")[0]);
        tvCount.setText(getGroup(groupPosition).toString().split("@")[1]);


        //lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    static class ViewHolder {
        TextView hp_lv_tvNaam , hp_lv_tvGaon;
        RelativeLayout  rl_background ,rl_left_topbottomcorner;
        ImageView iv_gender_icon;

    }
}