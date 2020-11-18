package com.akdndhrc.covid_module.Adapter.Adt_SocialContact;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
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


public class Adt_MeetingRecordList extends BaseAdapter {
    private Context ctx;
    ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

    private LayoutInflater inflater = null;

    // Constructor
    public Adt_MeetingRecordList(Context ctx, ArrayList<HashMap<String, String>> hashMapArrayList) {
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

            row = inflater.inflate(R.layout.custom_lv_meeting_layout, null);

            //TextView
            holder.tvMeetingName = row.findViewById(R.id.tvMeetingName);
            holder.tvCountMember = row.findViewById(R.id.tvCountMember);
            holder.tvDateTime = row.findViewById(R.id.tvDateTime);
            holder.tvStatus = row.findViewById(R.id.tvStatus);

            //Relative
            holder.rl_background = row.findViewById(R.id.rl_background);
            holder.rl_left_topbottomcorner = row.findViewById(R.id.rl_left_topbottomcorner);

            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.tvMeetingName.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("i")));
        holder.tvCountMember.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("member_count")));
        holder.tvDateTime.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("record_data")));

        if (hashMapArrayList.get(pos).get("meeting_status").equalsIgnoreCase("null"))
        {

            holder.tvStatus.setText(Html.fromHtml("InProgress"));
            holder.tvStatus.setTextColor(ctx.getResources().getColor(R.color.hp_listview_textview_redcolor));
        }
        else{
            holder.tvStatus.setText(Html.fromHtml("Completed"));
            holder.tvStatus.setTextColor(ctx.getResources().getColor(R.color.green_color));
        }

/*   if (pos % 2 == 0) {

            holder.tvMeetingName.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("i")));
            holder.tvDateTime.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("datetime")));

        } else {

            holder.tvMeetingName.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("i")));
            holder.tvDateTime.setText(Html.fromHtml("" + hashMapArrayList.get(pos).get("refferal_text")));
            holder.rl_background.setBackgroundColor(ctx.getResources().getColor(R.color.color_white));
            holder.rl_left_topbottomcorner.setBackgroundTintList(ctx.getResources().getColorStateList(R.color.light_blue_color));

        }*/

        return row;
    }


    static class ViewHolder {
        TextView tvMeetingName, tvCountMember,tvDateTime,tvStatus;
        RelativeLayout rl_background,rl_left_topbottomcorner;

    }


}
