package com.akdndhrc.covid_module;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.R;

import java.util.ArrayList;

public class PostNatalCare_Activity extends Activity implements View.OnClickListener {

    Context ctx =PostNatalCare_Activity.this;

    EfficientAdapter adapter;
    ArrayList<String> arrayList = new ArrayList<>();

    private static class EfficientAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        ArrayList<String> arrayList = new ArrayList<>();
        Context context;

        public EfficientAdapter(Context context,ArrayList<String> arrayList) {
            this.context = context;
            mInflater = LayoutInflater.from(context);

        }

        public int getCount() {
            return arrayList.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.listcontent, null);
                holder = new ViewHolder();
                holder.text = (TextView) convertView
                        .findViewById(R.id.txt1);
                holder.text2 = (TextView) convertView
                        .findViewById(R.id.txt2);
                holder.text3 = (TextView) convertView
                        .findViewById(R.id.txt3);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text.setText(arrayList.get(position).toString());

            return convertView;
        }

        static class ViewHolder {
            TextView text;
            TextView text2;
            TextView text3;
        }
    }//end of efficient Adapter Class

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_khandan_reg_);
        adapter = new EfficientAdapter(ctx,arrayList);

        ListView l1 = (ListView) findViewById(R.id.lv);
        View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_footerview, null, false);


        l1.addFooterView(footerView);


        l1.setAdapter(adapter);
        LinearLayout mLayout = (LinearLayout) footerView.findViewById(R.id.footer_layout);
        Button more = (Button) footerView.findViewById(R.id.moreButton);
        more.setOnClickListener(this);

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Toast.makeText(getBaseContext(), "You clciked " + arrayList.get(arg2).toString(), Toast.LENGTH_LONG).show();

            }
        });
        add_items();
    }

    private void add_items() {

        arrayList.add("A");
        arrayList.add("B");
        arrayList.add("C");
        arrayList.add("C");
        arrayList.add("C");
        arrayList.add("C");
        arrayList.add("C");
        arrayList.add("C");
        arrayList.add("C");
        arrayList.add("C");
        arrayList.add("C");
        arrayList.add("C");
        arrayList.add("C");
        arrayList.add("C");
        arrayList.add("E");
        arrayList.add("V");
        arrayList.add("C");
        arrayList.add("C");
        arrayList.add("C");
        arrayList.add("3");
        arrayList.add("r");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.moreButton:
                //Your code to add some more data into list and then call the following to refresh your lits
                adapter.notifyDataSetChanged();
                break;
        }//end of switch
    }//end of onClick


}//end of Custom List view class