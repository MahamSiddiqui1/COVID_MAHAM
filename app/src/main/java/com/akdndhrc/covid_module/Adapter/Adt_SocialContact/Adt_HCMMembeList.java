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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akdndhrc.covid_module.R;

import java.util.ArrayList;
import java.util.HashMap;


public class Adt_HCMMembeList extends BaseAdapter {
    private Context ctx;
    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    private LayoutInflater inflater = null;

    // Constructor
    public Adt_HCMMembeList(Context ctx, ArrayList<HashMap<String, String>> hashMapArrayList) {
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

            row = inflater.inflate(R.layout.custom_lv_hcm_member_list_layout, null);

            //TextView
            holder.tvMemberName = row.findViewById(R.id.tvMemberName);
            holder.tvAge = row.findViewById(R.id.tvAge);
            holder.iv_icon = row.findViewById(R.id.iv_icon);

            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

            holder.tvMemberName.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("full_name")));
            holder.tvAge.setText(Html.fromHtml(hashMapArrayList.get(pos).get("age")+" سال " ));

            if (hashMapArrayList.get(pos).get("gender").equalsIgnoreCase("0"))
            {
                holder.iv_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.woman_icon));
                holder.iv_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.pink_color));
            }
            else
                {
                    holder.iv_icon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_man_icon));
                    holder.iv_icon.setImageTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));
            }

        return row;
    }


    static class ViewHolder {
        TextView tvMemberName, tvAge;
        ImageView iv_icon;

    }


}
