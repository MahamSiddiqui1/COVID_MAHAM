package com.akdndhrc.covid_module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.Adt_ExpandableList;
import com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.Child_Dashboard_Activity;
import com.akdndhrc.covid_module.LHW_App.LHW_MotherDashboardActivities.Mother_Dashboard_Activity;
import com.akdndhrc.covid_module.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainPage_Activity extends AppCompatActivity {

    Context ctx = MainPage_Activity.this;

    Adt_ExpandableList listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    ArrayList<String> mArrayListChild  = new ArrayList<>();
    HashMap<String, List<String>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.exp_list);

        // preparing list data
        prepareListData();


        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                /*Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                /*Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();*/

            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                if (childPosition == 0)
                {
                    Intent intent = new Intent(ctx, Mother_Dashboard_Activity.class);
                    startActivity(intent);
                }
                else  if (childPosition == 1)
                {
                    Intent intent = new Intent(ctx, Child_Dashboard_Activity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(ctx, getString(R.string.position)+childPosition, Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

        listAdapter = new Adt_ExpandableList(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
       listDataHeader.add("Mother");
        listDataHeader.add("Child");
        listDataHeader.add("Pregnancy");

        // Adding child data
        List<String> mother_child = new ArrayList<String>();
        mother_child.add("کرن اقبال ");
        mother_child.add("سلمان اقبال");

        List<String> child = new ArrayList<String>();
       child.add("کرن اقبال ");
        child.add("سلمان اقبال");

        List<String> preg = new ArrayList<String>();
        preg.add("کرن اقبال ");
        preg.add("سلمان اقبال");



       listDataChild.put(listDataHeader.get(0), mother_child); // Header, Child data
        listDataChild.put(listDataHeader.get(1), child);
        listDataChild.put(listDataHeader.get(2), preg);


    }
}