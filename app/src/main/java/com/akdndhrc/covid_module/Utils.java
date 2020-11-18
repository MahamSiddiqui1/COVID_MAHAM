package com.akdndhrc.covid_module;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.R;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static void setSpinnerHealthFacilityReferredFiltered(Context ctx, Spinner spHealthFacility,String fordateraw,String facilities_user) {

        try {

            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            // String[][] mData = ls.executeReader("select name from  FACILITY  where id in (49, 50)  order by name ");"f0c51f51e2f511df,9841c19dde2dad48,0d27a3c273de8bf6"
            //String[][] mData = ls.executeReader("select name from  FACILITY  where id in ("+ActLogin.Referal_Facility_ID+")  order by name ");
            //String[][] mData = ls.executeReader("select name from FACILITY where uid in (\"f0c51f51e2f511df\", \"9841c19dde2dad48\", \"0d27a3c273de8bf6\")  order by name ");

            String[][] mData = ls.executeReader("SELECT name FROM FACILITY WHERE uid IN ("+facilities_user+") AND uid NOT IN ( SELECT hfid FROM Forms WHERE fordateraw = \""+fordateraw+"\" GROUP BY hfid ) ");
            List a = new ArrayList();

            //a.add("Select Health Facility");
            for(int i=0; i<mData.length; i++){
                a.add(mData[i][0]);
            }

            Log.d("0000999", "spHealthFacility name "+a);

            String[] area = (String[]) a.toArray(new String[0]);

//            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, area); //selected item will look like a spinner set from XML
//            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spHealthFacility.setAdapter(spinnerArrayAdapter);

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, area); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spHealthFacility.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            spinnerArrayAdapter,
                            R.layout.spinner_tehseel_layout,
                            ctx));



        } catch (Exception ex) {
            Log.d("0000999", "Exception spHealthFacility "+ex);
            ex.printStackTrace();
        }

    }



    public static void setSpinnerHealthFacilityReferred(Context ctx, Spinner spHealthFacility) {

        try {

            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

			String[][] mData = ls.executeReader("select name from FACILITY order by name ");

            List a = new ArrayList();

            //a.add("Select Health Facility");
            for(int i=0; i<mData.length; i++){
                a.add(mData[i][0]);
            }
            a.add("Other");
            Log.d("0000999", "spHealthFacility name "+a);

            String[] area = (String[]) a.toArray(new String[0]);

//            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, area); //selected item will look like a spinner set from XML
//            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spHealthFacility.setAdapter(spinnerArrayAdapter);

			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ctx, R.layout.sp_title_facilityname, area); //selected item will look like a spinner set from XML
			spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spHealthFacility.setAdapter(
					new NothingSelectedSpinnerAdapter(
							spinnerArrayAdapter,
							R.layout.sp_title_facilityname,
							ctx));

        } catch (Exception ex) {
            Log.d("0000999", "Exception spHealthFacility "+ex);
            ex.printStackTrace();
        }

    }

    public static void setSpinnerTehsel(Context ctx, Spinner spHealthFacility, String[][] dataArray ) {

        try {

            //Lister ls = new Lister(ctx);
            //SQLiteDatabase.loadLibs(ctx);                    ls.createAndOpenDB("test123");();
            // String[][] mData = ls.executeReader("select name from  FACILITY  where id in (49, 50)  order by name ");"f0c51f51e2f511df,9841c19dde2dad48,0d27a3c273de8bf6"
            //String[][] mData = ls.executeReader("select name from  FACILITY  where id in ("+ActLogin.Referal_Facility_ID+")  order by name ");
            //String[][] mData = ls.executeReader("select name from TEHSIL order by name ");

            List a = new ArrayList();
           // a.add("All");
            //a.add("Select Health Facility");
            for(int i=0; i<dataArray.length; i++){
                a.add(dataArray[i][0]);
            }

            Log.d("0000999", "spHealthFacility name "+a);

            String[] area = (String[]) a.toArray(new String[0]);

//            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, area); //selected item will look like a spinner set from XML
//            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spHealthFacility.setAdapter(spinnerArrayAdapter);

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, area); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spHealthFacility.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            spinnerArrayAdapter,
                            R.layout.spinner_tehseel_layout,
                            ctx));



        } catch (Exception ex) {
            Log.d("0000999", "Exception spHealthFacility "+ex);
            ex.printStackTrace();
        }

    }
    public static void setSpinnerUnionCouncil(Context ctx, Spinner spHealthFacility) {

        try {

            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            // String[][] mData = ls.executeReader("select name from  FACILITY  where id in (49, 50)  order by name ");"f0c51f51e2f511df,9841c19dde2dad48,0d27a3c273de8bf6"
            //String[][] mData = ls.executeReader("select name from  FACILITY  where id in ("+ActLogin.Referal_Facility_ID+")  order by name ");
            String[][] mData = ls.executeReader("select name from UNIONCOUNCIL  order by name ");

            List a = new ArrayList();
           // a.add("All");
            //a.add("Select Health Facility");
            for(int i=0; i<mData.length; i++){
                a.add(mData[i][0]);
            }

            Log.d("0000999", "spHealthFacility name "+a);

            String[] area = (String[]) a.toArray(new String[0]);

//            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, area); //selected item will look like a spinner set from XML
//            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spHealthFacility.setAdapter(spinnerArrayAdapter);

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, area); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spHealthFacility.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            spinnerArrayAdapter,
                            R.layout.spinner_tehseel_layout,
                            ctx));



        } catch (Exception ex) {
            Log.d("0000999", "Exception spHealthFacility "+ex);
            ex.printStackTrace();
        }

    }
    public static void setSpinnerdistrict(Context ctx, Spinner spHealthFacility) {

        try {

            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();
            // String[][] mData = ls.executeReader("select name from  FACILITY  where id in (49, 50)  order by name ");"f0c51f51e2f511df,9841c19dde2dad48,0d27a3c273de8bf6"
            //String[][] mData = ls.executeReader("select name from  FACILITY  where id in ("+ActLogin.Referal_Facility_ID+")  order by name ");
            String[][] mData = ls.executeReader("select name from DISTRICT order by name ");

            List a = new ArrayList();
            //a.add("All");
            //a.add("Select Health Facility");
            for(int i=0; i<mData.length; i++){
                a.add(mData[i][0]);
            }

            Log.d("0000999", "spHealthFacility name "+a);

            String[] area = (String[]) a.toArray(new String[0]);

//            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, area); //selected item will look like a spinner set from XML
//            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spHealthFacility.setAdapter(spinnerArrayAdapter);

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, area); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spHealthFacility.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            spinnerArrayAdapter,
                            R.layout.spinner_tehseel_layout,
                            ctx));



        } catch (Exception ex) {
            Log.d("0000999", "Exception spHealthFacility "+ex);
            ex.printStackTrace();
        }

    }

    public static void setSpinnervillage(Context ctx, Spinner spHealthFacility) {

        try {

            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            // String[][] mData = ls.executeReader("select name from  FACILITY  where id in (49, 50)  order by name ");"f0c51f51e2f511df,9841c19dde2dad48,0d27a3c273de8bf6"
            //String[][] mData = ls.executeReader("select name from  FACILITY  where id in ("+ActLogin.Referal_Facility_ID+")  order by name ");
            String[][] mData = ls.executeReader("select name from VILLAGES  order by name ");

            List a = new ArrayList();
            //a.add("All");
            //a.add("Select Health Facility");
            for(int i=0; i<mData.length; i++){
                a.add(mData[i][0]);
            }

            Log.d("0000999", "spHealthFacility name "+a);

            String[] area = (String[]) a.toArray(new String[0]);

//            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, area); //selected item will look like a spinner set from XML
//            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spHealthFacility.setAdapter(spinnerArrayAdapter);

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, area); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spHealthFacility.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            spinnerArrayAdapter,
                            R.layout.spinner_tehseel_layout,
                            ctx));



        } catch (Exception ex) {
            Log.d("0000999", "Exception spHealthFacility "+ex);
            ex.printStackTrace();
        }

    }


    public static void setSpinnerMedicines(Context ctx, Spinner spMedicines) {

        try {

            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            String[][] mData = ls.executeReader("Select name from MEDICINE where type ='0' ");

            List a = new ArrayList();
            //a.add("All");
            //a.add("Select Health Facility");
            for(int i=0; i<mData.length; i++){
                a.add(mData[i][0]);
            }

            Log.d("0000999", "spMedicines name "+a);

            String[] medicines = (String[]) a.toArray(new String[0]);

//            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, area); //selected item will look like a spinner set from XML
//            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spHealthFacility.setAdapter(spinnerArrayAdapter);

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, medicines); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spMedicines.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            spinnerArrayAdapter,
                            R.layout.spinner_illness_medicine_layout,
                            ctx));



        } catch (Exception ex) {
            Log.d("0000999", "Exception spMedicines "+ex);
            ex.printStackTrace();
        }

    }

    ////////////////////////Activity Postnatal Care /////////////////////


    public static void setSpinnerVaccineName(Context ctx, Spinner spVaccine) {

        try {

            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();

            String[][] mData = ls.executeReader("Select name from VACCINES");

            List a = new ArrayList();
            //a.add("All");
            //a.add("Select Health Facility");
            for(int i=0; i<mData.length; i++){
                a.add(mData[i][0]);
            }

            Log.d("0000999", "spVaccine name "+a);

            String[] vaccine_names = (String[]) a.toArray(new String[0]);

//            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, area); //selected item will look like a spinner set from XML
//            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spHealthFacility.setAdapter(spinnerArrayAdapter);

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(ctx, R.layout.sp_title_topic_layout, vaccine_names); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spVaccine.setAdapter(
                    new NothingSelectedSpinnerAdapter(
                            spinnerArrayAdapter,
                            R.layout.sp_title_topic_layout,
                            ctx));



        } catch (Exception ex) {
            Log.d("0000999", "Exception spVaccine "+ex);
            ex.printStackTrace();
        }

    }

  public static int haveNetworkConnection(Context ctx) {

        int status =0;

        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    status = 1;
         //   Log.d("000333","WIFI");
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    status = 2;
          //  Log.d("000333","Mobie");
        }

        return status;
    }

}
