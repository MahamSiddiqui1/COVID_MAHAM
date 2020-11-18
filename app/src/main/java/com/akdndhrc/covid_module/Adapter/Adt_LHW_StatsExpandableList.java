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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Adt_LHW_StatsExpandableList extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<String>> _listDataChild;

    public Adt_LHW_StatsExpandableList(Context context, List<String> listDataHeader,
                                       HashMap<String, ArrayList<String>> listChildData) {
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


        Log.d("000999", "Name: " + childText);
        /*Log.d("000999","Gender: "+childText.split("@")[2]);
        Log.d("000999","Age: "+childText.split("@")[3]);*/

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.custom_stats_child_layout, null);

            holder.hp_lv_tvNaam = (TextView) convertView.findViewById(R.id.hp_lv_tvNaam);
            holder.hp_lv_tvcount = (TextView) convertView.findViewById(R.id.hp_lv_tvcount);
            holder.rl_background = (RelativeLayout) convertView.findViewById(R.id.rl_background);
            holder.rl_left_topbottomcorner = (RelativeLayout) convertView.findViewById(R.id.rl_left_topbottomcorner);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


//        holder.hp_lv_tv_inside_outside.setText(childText.split("@")[0]);
        holder.hp_lv_tvNaam.setText(childText.split("@")[0]);
        holder.hp_lv_tvcount.setText(childText.split("@")[1]);

        if (childPosition % 2 == 0) {
            holder.hp_lv_tvcount.setTextColor(_context.getResources().getColor(R.color.pink_color));
            holder.rl_background.setBackgroundColor(_context.getResources().getColor(R.color.color_white));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.rl_left_topbottomcorner.setBackgroundTintList(_context.getResources().getColorStateList(R.color.light_blue_color));
            }

        } else {
            holder.hp_lv_tvcount.setTextColor(_context.getResources().getColor(R.color.light_blue_color));
            holder.rl_background.setBackgroundColor(_context.getResources().getColor(R.color.light_pink_color));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.rl_left_topbottomcorner.setBackgroundTintList(_context.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
            }

        }


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        //return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
        List childList = _listDataChild.get(_listDataHeader.get(groupPosition));
        if (childList != null && !childList.isEmpty()) {
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
            convertView = infalInflater.inflate(R.layout.custom_stats_parent_list, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.tvName);
        TextView tv_count = (TextView) convertView.findViewById(R.id.tv_count);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        tv_count.setText(getGroup(groupPosition).toString().split("@")[0]);
        lblListHeader.setText(getGroup(groupPosition).toString().split("@")[1]);

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
        TextView hp_lv_tvNaam, hp_lv_tvcount;
        RelativeLayout rl_background, rl_left_topbottomcorner;
        ImageView iv_gender_icon;

    }
}