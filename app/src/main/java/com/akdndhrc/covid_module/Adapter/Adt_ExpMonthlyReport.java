package com.akdndhrc.covid_module.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akdndhrc.covid_module.R;

import java.util.HashMap;
import java.util.List;

public class Adt_ExpMonthlyReport extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    private List<String> _listCount; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public Adt_ExpMonthlyReport(Context context, List<String> listDataHeader,
                                HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
      //  this._listCount = listCount;

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

        final String childText = (String) getChild(groupPosition, childPosition);
        Log.d("000999", "getChildView: "+childText);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.custom_child_list, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.tvNaam);
        TextView txtListCount = (TextView) convertView.findViewById(R.id.tvcount);
        RelativeLayout rl_left_topbottomcorner = convertView.findViewById(R.id.rl_left_topbottomcorner);
        RelativeLayout rl_background =  convertView.findViewById(R.id.rl_background);
        RelativeLayout rl_1 =  convertView.findViewById(R.id.rl_1);
        RelativeLayout rl_medicine_stock =  convertView.findViewById(R.id.rl_medicine_stock);
        TextView tvMedicineName = (TextView) convertView.findViewById(R.id.tvMedicineName);
        TextView tvBalance = (TextView) convertView.findViewById(R.id.tvBalance);
        TextView tvUtilized = (TextView) convertView.findViewById(R.id.tvUtilized);
        TextView tvWastage = (TextView) convertView.findViewById(R.id.tvWastage);

        if (groupPosition == 7)
        {
            rl_1.setVisibility(View.GONE);
            rl_medicine_stock.setVisibility(View.VISIBLE);

            tvMedicineName.setText(childText.split("@")[0]);
            tvBalance.setText(childText.split("@")[1]);
            tvUtilized.setText(childText.split("@")[2]);
            tvWastage.setText(childText.split("@")[3]);

            if (childPosition % 2 == 0) {
                rl_background.setBackgroundColor(_context.getResources().getColor(R.color.color_white));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rl_left_topbottomcorner.setBackgroundTintList(_context.getResources().getColorStateList(R.color.light_blue_color));
                }

            } else {
                rl_background.setBackgroundColor(_context.getResources().getColor(R.color.light_pink_color));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rl_left_topbottomcorner.setBackgroundTintList(_context.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                }

            }

        }
        else {
            rl_medicine_stock.setVisibility(View.GONE);
            rl_1.setVisibility(View.VISIBLE);
            txtListChild.setText(childText.split("@")[0]);
            txtListCount.setText(childText.split("@")[1]);


            if (childPosition % 2 == 0) {
                txtListCount.setTextColor(_context.getResources().getColor(R.color.pink_color));
                rl_background.setBackgroundColor(_context.getResources().getColor(R.color.color_white));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rl_left_topbottomcorner.setBackgroundTintList(_context.getResources().getColorStateList(R.color.light_blue_color));
                }

            } else {
                txtListCount.setTextColor(_context.getResources().getColor(R.color.light_blue_color));
                rl_background.setBackgroundColor(_context.getResources().getColor(R.color.light_pink_color));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rl_left_topbottomcorner.setBackgroundTintList(_context.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                }

            }
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
       // return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
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
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.custom_parent_list, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.tvName);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

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
}