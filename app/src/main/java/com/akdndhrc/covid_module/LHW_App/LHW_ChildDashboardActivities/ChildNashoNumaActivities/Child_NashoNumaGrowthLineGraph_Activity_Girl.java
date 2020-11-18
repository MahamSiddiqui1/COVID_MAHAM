package com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildNashoNumaActivities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONObject;

import java.util.ArrayList;

public class Child_NashoNumaGrowthLineGraph_Activity_Girl extends AppCompatActivity {

    Context ctx = Child_NashoNumaGrowthLineGraph_Activity_Girl.this;

    LineChart lineChart;
    String child_uid,child_age,child_name,child_gender;
    ArrayList<Entry> entries5 = new ArrayList<>();String[][] graph_plot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_child_nasho_numa_growth_line_graph);

        child_uid = getIntent().getExtras().getString("u_id");
        child_name = getIntent().getExtras().getString("child_name");
        child_age = getIntent().getExtras().getString("child_age");
        child_gender = getIntent().getExtras().getString("child_gender");
        lineChart = (LineChart) findViewById(R.id.linechart);

        // creating list of entry<br />
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(2.4f, 0));
        entries.add(new Entry(3.2f, 1));
        entries.add(new Entry(4.0f, 2));
        entries.add(new Entry(4.6f, 3));
        entries.add(new Entry(5.1f, 4));
        entries.add(new Entry(5.5f, 5));
        entries.add(new Entry(5.8f, 6));
        entries.add(new Entry(6.1f, 7));
        entries.add(new Entry(6.3f, 8));
        entries.add(new Entry(6.6f, 9));
        entries.add(new Entry(6.8f, 10));
        entries.add(new Entry(7.0f, 11));
        entries.add(new Entry(7.1f, 12));
        entries.add(new Entry(7.3f, 13));
        entries.add(new Entry(7.5f, 14));
        entries.add(new Entry(7.7f, 15));
        entries.add(new Entry(7.8f, 16));
        entries.add(new Entry(8.0f, 17));
        entries.add(new Entry(8.2f, 18));
        entries.add(new Entry(8.3f, 19));
        entries.add(new Entry(8.5f, 20));
        entries.add(new Entry(8.7f, 21));
        entries.add(new Entry(8.8f, 22));
        entries.add(new Entry(9.0f, 23));

        ArrayList<Entry> entries1 = new ArrayList<>();
        entries1.add(new Entry(2.8f, 0));
        entries1.add(new Entry(3.6f, 1));
        entries1.add(new Entry(4.6f, 2));
        entries1.add(new Entry(5.1f, 3));
        entries1.add(new Entry(5.6f, 4));
        entries1.add(new Entry(6.1f, 5));
        entries1.add(new Entry(6.4f, 6));
        entries1.add(new Entry(6.7f, 7));
        entries1.add(new Entry(7.0f, 8));
        entries1.add(new Entry(7.3f, 9));
        entries1.add(new Entry(7.5f, 10));
        entries1.add(new Entry(7.7f, 11));
        entries1.add(new Entry(8.1f, 12));
        entries1.add(new Entry(8.3f, 13));
        entries1.add(new Entry(8.5f, 14));
        entries1.add(new Entry(8.7f, 15));
        entries1.add(new Entry(9.0f, 16));
        entries1.add(new Entry(9.2f, 17));
        entries1.add(new Entry(9.4f, 18));
        entries1.add(new Entry(9.6f, 19));
        entries1.add(new Entry(9.8f, 20));
        entries1.add(new Entry(9.9f, 21));
        entries1.add(new Entry(10.1f, 22));
        entries1.add(new Entry(10.3f, 23));

        ArrayList<Entry> entries2 = new ArrayList<>();
        entries2.add(new Entry(3.2f, 0));
        entries2.add(new Entry(4.2f, 1));
        entries2.add(new Entry(5.1f, 2));
        entries2.add(new Entry(5.8f, 3));
        entries2.add(new Entry(6.4f, 4));
        entries2.add(new Entry(6.9f, 5));
        entries2.add(new Entry(7.3f, 6));
        entries2.add(new Entry(7.6f, 7));
        entries2.add(new Entry(7.9f, 8));
        entries2.add(new Entry(8.2f, 9));
        entries2.add(new Entry(8.5f, 10));
        entries2.add(new Entry(8.7f, 11));
        entries2.add(new Entry(8.9f, 12));
        entries2.add(new Entry(9.2f, 13));
        entries2.add(new Entry(9.4f, 14));
        entries2.add(new Entry(9.6f, 15));
        entries2.add(new Entry(9.8f, 16));
        entries2.add(new Entry(10.0f, 17));
        entries2.add(new Entry(10.2f, 18));
        entries2.add(new Entry(10.4f, 19));
        entries2.add(new Entry(10.6f, 20));
        entries2.add(new Entry(10.9f, 21));
        entries2.add(new Entry(11.1f, 22));
        entries2.add(new Entry(11.3f, 23));

        ArrayList<Entry> entries3 = new ArrayList<>();
        entries3.add(new Entry(3.7f, 0));
        entries3.add(new Entry(4.8f, 1));
        entries3.add(new Entry(5.9f, 2));
        entries3.add(new Entry(6.7f, 3));
        entries3.add(new Entry(7.3f, 4));
        entries3.add(new Entry(7.8f, 5));
        entries3.add(new Entry(8.3f, 6));
        entries3.add(new Entry(8.7f, 7));
        entries3.add(new Entry(9.0f, 8));
        entries3.add(new Entry(9.3f, 9));
        entries3.add(new Entry(9.6f, 10));
        entries3.add(new Entry(9.9f, 11));
        entries3.add(new Entry(10.3f, 12));
        entries3.add(new Entry(10.4f, 13));
        entries3.add(new Entry(10.7f, 14));
        entries3.add(new Entry(10.9f, 15));
        entries3.add(new Entry(11.2f, 16));
        entries3.add(new Entry(11.4f, 17));
        entries3.add(new Entry(11.6f, 18));
        entries3.add(new Entry(11.9f, 19));
        entries3.add(new Entry(12.1f, 20));
        entries3.add(new Entry(12.4f, 21));
        entries3.add(new Entry(12.6f, 22));
        entries3.add(new Entry(12.8f, 23));

        ArrayList<Entry> entries4 = new ArrayList<>();
        entries4.add(new Entry(4.2f, 0));
        entries4.add(new Entry(5.4f, 1));
        entries4.add(new Entry(6.5f, 2));
        entries4.add(new Entry(7.4f, 3));
        entries4.add(new Entry(8.1f, 4));
        entries4.add(new Entry(8.7f, 5));
        entries4.add(new Entry(9.2f, 6));
        entries4.add(new Entry(9.6f, 7));
        entries4.add(new Entry(10.0f, 8));
        entries4.add(new Entry(10.4f, 9));
        entries4.add(new Entry(10.7f, 10));
        entries4.add(new Entry(11.0f, 11));
        entries4.add(new Entry(11.3f, 12));
        entries4.add(new Entry(11.6f, 13));
        entries4.add(new Entry(11.9f, 14));
        entries4.add(new Entry(12.2f, 15));
        entries4.add(new Entry(12.5f, 16));
        entries4.add(new Entry(12.7f, 17));
        entries4.add(new Entry(13.0f, 18));
        entries4.add(new Entry(13.3f, 19));
        entries4.add(new Entry(13.5f, 20));
        entries4.add(new Entry(13.8f, 21));
        entries4.add(new Entry(14.1f, 22));
        entries4.add(new Entry(14.3f, 23));




       /* entries5.add(new Entry(0.3f, 0));
        entries5.add(new Entry(0.7f, 1));
        entries5.add(new Entry(0.0f, 2));
        entries5.add(new Entry(0.9f, 3));
        entries5.add(new Entry(0.6f, 4));
        entries5.add(new Entry(0.2f, 5));
        entries5.add(new Entry(0.7f, 6));
        entries5.add(new Entry(0.2f, 7));
        entries5.add(new Entry(0.5f, 8));
        entries5.add(new Entry(0.9f, 9));
        entries5.add(new Entry(0.2f, 10));
        entries5.add(new Entry(0.5f, 11));
        entries5.add(new Entry(0.8f, 12));
        entries5.add(new Entry(0.1f, 13));
        entries5.add(new Entry(0.4f, 14));
        entries5.add(new Entry(0.7f, 15));
        entries5.add(new Entry(0.9f, 16));
        entries5.add(new Entry(0.2f, 17));
        entries5.add(new Entry(0.5f, 18));
        entries5.add(new Entry(0.7f, 19));
        entries5.add(new Entry(0.0f, 20));
        entries5.add(new Entry(0.3f, 21));
        entries5.add(new Entry(0.5f, 22));
        entries5.add(new Entry(0.8f, 23));*/


        LineDataSet dataSet = new LineDataSet(entries, "");
        LineDataSet dataSet1 = new LineDataSet(entries1, "");



        ArrayList<String> labels = new ArrayList<>();
        labels.add("1 week");
        labels.add("2 week");
        labels.add("3 week");
        labels.add("4 week");
        labels.add("5 week");
        labels.add("6 week");
        labels.add("7 week");
        labels.add("8 week");
        labels.add("9 week");
        labels.add("10 week");
        labels.add("11 week");
        labels.add("12 week");
        labels.add("13 week");
        labels.add("14 week");
        labels.add("15 week");
        labels.add("16 week");
        labels.add("17 week");
        labels.add("18 week");
        labels.add("19 week");
        labels.add("20 week");
        labels.add("21 week");
        labels.add("22 week");
        labels.add("23 week");
        labels.add("24 week");

        String[] xAxis = new String[] {"0","1", "2", "3", "4", "5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};

      /*  ArrayList<LineData> lines = new ArrayList<LineData> ();
        LineData data = new LineData(labels, dataSet);
        lines.add(data);
        LineData data1 = new LineData(labels, dataSet1);
        lines.add(data1);
       // lineChart.setData(new LineData(xAxis, (ILineDataSet) lines));

        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet1.setColors(ColorTemplate.PASTEL_COLORS);
        lineChart.setData(lines.get(0));

        lineChart.setData(lines.get(1));
        Legend l = lineChart.getLegend();
        l.setEnabled(false);

        lineChart.setDescription("");
       */
        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet lineDataSet = new LineDataSet(entries,"3rd");
        lineDataSet.setDrawCircles(false);
        lineDataSet.setColor(Color.parseColor("#e03030"));

        LineDataSet lineDataSet1 = new LineDataSet(entries1,"15th");
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setColor(Color.parseColor("#FFC107"));

        LineDataSet lineDataSet2 = new LineDataSet(entries2,"50th");
        lineDataSet2.setDrawCircles(false);
        lineDataSet2.setColor(Color.parseColor("#469519"));

        LineDataSet lineDataSet3 = new LineDataSet(entries3,"85th");
        lineDataSet3.setDrawCircles(false);
        lineDataSet3.setColor(Color.parseColor("#FFC107"));

        LineDataSet lineDataSet4 = new LineDataSet(entries4,"97th");
        lineDataSet4.setDrawCircles(false);
        lineDataSet4.setColor(Color.parseColor("#e03030"));

        LineDataSet lineDataSet5 = new LineDataSet(entries5,"child");
        lineDataSet5.setDrawCircles(true);
        /*lineDataSet5.setColor(Color.WHITE);
        lineDataSet5.setCircleColor(Color.BLACK);*/
        lineDataSet5.setColor(ctx.getResources().getColor(R.color.dark_blue_color));
        lineDataSet5.setCircleColor(ctx.getResources().getColor(R.color.dark_blue_color));



        lineDataSets.add(lineDataSet);
        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);
        lineDataSets.add(lineDataSet3);
        lineDataSets.add(lineDataSet4);
        lineDataSets.add(lineDataSet5);

        lineChart.setData(new LineData(xAxis,lineDataSets));
        lineChart.setVisibleXRangeMaximum(65f);

    }
    @Override
    protected void onResume() {
        super.onResume();

        try {
            Lister ls = new Lister(ctx);
          ls.createAndOpenDB();

            String mData[][] = ls.executeReader("Select data from CGROWTH where member_uid = '" + child_uid + "'");
            Log.d("000999", "1: " + mData[0][0]);
            //String mData1[][] = ls.executeReader("Select data from CGROWTH where member_uid = '" + child_uid + "'AND data LIKE ");
            Log.d("000999", "1: " + String.valueOf(mData.length));
            //String mData2[][] = ls.executeReader("Select data from CGROWTH where member_uid = '" + child_uid + "'AND data LIKE ");
            //Log.d("000999", "1: " + mData2[0][0]);

            graph_plot = new String[24][24];
            for (int z = 0; z < mData.length; z++) {
                String json = mData[z][0];
                JSONObject jsonObject = new JSONObject(json);
                int i = 0;
                int count = mData.length;
                int remaining = 24 - count;
                graph_plot[Integer.parseInt(jsonObject.getString("age_month"))][1] = jsonObject.getString("wazan");
                graph_plot[Integer.parseInt(jsonObject.getString("age_month"))][0] = jsonObject.getString("age_month");
                // for (i = 0; i < 24; i++) {
                Log.d("000999", "1: " + i);
                Log.d("000999", "1: " + String.valueOf(Float.parseFloat(jsonObject.getString("wazan"))));
                Log.d("000999", graph_plot[Integer.parseInt(jsonObject.getString("age_month"))][0]+graph_plot[Integer.parseInt(jsonObject.getString("age_month"))][1]);
                // entries5.add(String.valueOf(jsonObject.getString("qad")),i);
                // if (i == Integer.parseInt(jsonObject.getString("age_month"))) {

                entries5.add(new Entry(Float.parseFloat(jsonObject.getString("wazan")), Integer.parseInt(jsonObject.getString("age_month"))));

                //} else {
                // entries5.add(new Entry(0.0f, i));
                //}
                // entries5.add(new Entry(0.8f, 23));

                // }
                Log.d("000999", "1: " + entries5);
                // et_tareekh_indraj.setText((jsonObject.getString("entrydate")));


            }





        } catch(Exception e){
            // Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            Log.d("000999", e.getMessage());
            for (int i = 0; i < 24; i++) {
                entries5.add(new Entry(0.0f, i));
            }
        }

    }
}