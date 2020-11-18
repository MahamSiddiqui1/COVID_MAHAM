package com.akdndhrc.covid_module;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.akdndhrc.covid_module.R;

public class MainActivity extends AppCompatActivity {

    Context ctx = MainActivity.this;
    Button btn_jamaa_kre;

   /* private FusedLocationProviderClient client;
    LocationManager locationManager;
    String lattitude,longitude;*/

    GPSTracker gps;
    double latitude;
    double longitude;
    ServiceLocation serviceLocation;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_ask_expert_form);
        btn_jamaa_kre = findViewById(R.id.submit);

        //  startService(new Intent(ctx, ServiceLocation.class).addFlags(Service.START_STICKY));

        //GPS\
      /*  gps = new GPSTracker(ctx);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.d("000555", "latitude value: " + latitude);
            Log.d("000555", "longitude value: " + longitude);
        } else {
            gps.showSettingsAlert();
            Toast.makeText(ctx, "برائے مہربانی جی پی ایس پوزیشن کو آن کریں", Toast.LENGTH_LONG).show();
        }*/


 /*       try {
            serviceLocation = new ServiceLocation(ctx);
            serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            serviceLocation.callAsynchronousTask();
        } catch (Exception e) {
            Log.d("000555", "GPS Service Err:  " + e.getMessage());
        }


        if (serviceLocation.showCurrentLocation() == false) {

        }
        /* ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);*/

        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*View toastView = getLayoutInflater().inflate(R.layout.lay_dialog_loading2, null);

                // Initiate the Toast instance.
                Toast toast = new Toast(getApplicationContext());
                // Set custom view in toast.
                toast.setView(toastView);
                toast.setGravity(Gravity.CENTER, 10, 10);
                toast.show();*/

                if (serviceLocation.showCurrentLocation() == true) {
                    latitude = serviceLocation.getLatitude();
                    longitude = serviceLocation.getLongitude();
                    Log.d("000555", " latitude: " + latitude);
                    Log.d("000555", " longitude: " + longitude);
                } else {
                }

             /*   locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();

                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                }*/
            }
        });


      /*  ImageView imageView = (ImageView) findViewById(R.id.iv);

       try {
           File file = new File(Environment.getExternalStorageDirectory() + File.separator + "HayatPK" + File.separator + "Vaccines" + File.separator + "IMG_28072019_060138_746f3c0cc736457eaacfca24600f8e1b9109574490928777411.jpg");
           imageView.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(file)));


       }
       catch (Exception e)
       {
           Log.d("Erhhhr",e.getMessage());
       }*/


    }

   /* private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);



                Log.d("000555","1: ");
                Log.d("000555","Lat: " +lattitude);
                Log.d("000555","Lng: " +longitude);

            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Log.d("000555","2: " );
                Log.d("000555","Lat: " +lattitude);
                Log.d("000555","Lng: " +longitude);

            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Log.d("000555","3: " );
                Log.d("000555","Lat: " +lattitude);
                Log.d("000555","Lng: " +longitude);
            }else{

                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



    private void requestpermissin()

    {

        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);


    }
        /*MultiLineChart multiLineChart = (MultiLineChart) findViewById(R.id.chart);


        ArrayList<ChartData> value1 = new ArrayList<ChartData>();
        value1.add(new ChartData(4f, 0.5f)); //values.add(new ChartData(y,x));<br />
        value1.add(new ChartData(9f, 1f));
        value1.add(new ChartData(18f, 2f));
        value1.add(new ChartData(2f, 4f));
        value1.add(new ChartData(12f, 5f));
        value1.add(new ChartData(9f, 7f));


        ArrayList<ChartData> value2 = new ArrayList<ChartData>();
        value2.add(new ChartData(5f, 1f)); //values.add(new ChartData(y,x));<br />
        value2.add(new ChartData(6f, 2f));
        value2.add(new ChartData(15f, 3f));
        value2.add(new ChartData(2f, 5f));
        value2.add(new ChartData(3f, 8f));


        ArrayList<ChartData> value3 = new ArrayList<ChartData>();
                value3.add(new ChartData(value1));
                value3.add(new ChartData(value2));

                multiLineChart.setData(value3);



               ArrayList<String>h_lables = new ArrayList<String>();
                h_lables.add("Mn");
                h_lables.add("Tues");
                h_lables.add("WEd");
                h_lables.add("TH");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");
                h_lables.add("Fr");

 multiLineChart.setHorizontal_label(h_lables);

    }*/
}
