package com.akdndhrc.covid_module;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.R;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ServiceLocation_Login extends Service {


    private final Context mContext;

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 100; // in Meters

   //private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000 * 10 * 1; // 1 minute
   private static final long MINIMUM_TIME_BETWEEN_UPDATES = 3000; // 1 minute

    int mCount = 0;
    // flag for GPS status
    public boolean canGetLocation = false;
    Location location; // location
    double latitude; // latitude
    double longitude;

    public LocationManager locationManager;
    MyLocationListener myLocationListener = new MyLocationListener();

    String temp_var = "0";
    String temp_toast = "0";


    AlertDialog alertDialog;
    Dialog dialog;
   static TimerTask doAsynchronousTask;

    TextView txt_timer;
    private static final long START_TIME_IN_MILLIS = 90000; // 1min
    private CountDownTimer mCountDownTimer;
    String timeLeftFormatted;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    public ServiceLocation_Login(Context context) {
        this.mContext = context;

        Log.d("000777", "CTXXXXXXXX ");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

       /* locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        callAsynchronousTask();
*/
        Log.d("000777", "Service On Create: ");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d("000777", "Service Start ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("000777", "Service Start command");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("000777", "Destroy Called ");
    }

    public void callAsynchronousTask() {

        dialog = new Dialog(mContext);
        LayoutInflater layout = LayoutInflater.from(mContext);
        final View dialogView = layout.inflate(R.layout.lay_dialog_loading2, null);

        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        txt_timer = dialogView.findViewById(R.id.txt_timer);

        startTimer();

        Log.d("000777", "Timer Strat");


        final Handler handler = new Handler();
        final Timer timer = new Timer();
        doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {

                        mCount = mCount + 1;
                        Log.d("000777", "count: " + mCount);

                        try {
                            if (mCount == 1) {

                                  /*  if (isGPSEnabled)
                                    {*/
                                Log.d("000777", "GPS ENABLE");

                                locationManager.requestLocationUpdates(
                                        LocationManager.GPS_PROVIDER,
                                        MINIMUM_TIME_BETWEEN_UPDATES,
                                        MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                                        myLocationListener
                                );
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


                                if (!showCurrentLocation()) {

                                    Log.d("000777", "!showCurrentLocation");
                                    mCount = 0;
                                } else {
                                    locationManager.removeUpdates(myLocationListener);
                                    doAsynchronousTask.cancel();
                                    timer.cancel();
                                    mCountDownTimer.cancel();
                                    mCount = 0;
                                    Log.d("000777", "Timer Stop");
                                }


                            } else if (mCount > 300) {
                                mCount = 0;
                            } else {
                                Log.d("000777", "ELSE COUNT");
                            }


                        } catch (Exception e) {
                            timer.cancel();
                            doAsynchronousTask.cancel();
                            dialog.dismiss();
                            // TODO Auto-generated catch block
                            Log.d("000777", "Exp: " + e.getMessage());
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 2000); //2000 hr dou dou sec sy ye method chle ga

    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                Log.d("000777","Time Finish !!!!!!!!!!!!!!!!!!!!!!!!!");
                Toast.makeText(mContext, "Login Now !!", Toast.LENGTH_SHORT).show();
               dialog.dismiss();
                doAsynchronousTask.cancel();
                mCount=0;
            }
        }.start();

    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        txt_timer.setText(timeLeftFormatted);
        Log.d("000777","Timer Value: " +timeLeftFormatted);

    }


    public boolean showCurrentLocation() {

        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // Log.d("000777", "Called: ");

        if (location != null) {
            String message = String.format(
                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            Log.d("000777", "Ser-Lat: " + location.getLatitude());
            Log.d("000777", "Ser-Longi: " + location.getLongitude());
            Log.d("000777", "getProvider: " + location.getProvider());
            Log.d("000777", "getAccuracy: " + location.getAccuracy());
            Log.d("000777", "getTime: " + location.getTime());

            getLatitude();
            getLongitude();

            dialog.dismiss();

          /*  if (temp_toast.equalsIgnoreCase("1")){
                Log.d("000777", "IFFFFFFFFF !!!!!!!!!!!");
                float lat = Float.parseFloat(String.format("%.5f", latitude));
                float lng = Float.parseFloat(String.format("%.5f", longitude));
                Toast.makeText(mContext, "Latitude: "+lat + "\n"+"Longitude: " + lng, Toast.LENGTH_SHORT).show();

                //temp_toast = "0";
            }
            else{ Log.d("000777", "ELSEEEEEEEEEEEEE !!!!!!!!!!!");

            }*/

                if (Login_Activity.btnLoginIsClicked == true)
                {
                    Log.d("000777", "IS CLICK !!!!!!!!!!!");
                    Log.d("000777", "IFFFFF !!!!!!!!!!!");
                    Log.d("000777", "جی پی ایس پوزیشن آن ہے");
                }
                else{
                    Log.d("000777", "BUTTON NOT CLICK !!!!!!!!!!!");
                    Log.d("000777", "ELSEEEEEEEEEEEEE !!!!!!!!!!!");
                    float lat = Float.parseFloat(String.format("%.5f", latitude));
                    float lng = Float.parseFloat(String.format("%.5f", longitude));
                    Toast.makeText(mContext, "Latitude: "+lat + "\n"+"Longitude: " + lng, Toast.LENGTH_SHORT).show();

                }


            onDestroy();

            return true;

        } else {
            temp_toast = "1";
            Log.d("000777", "Location Null");
            return false;
        }

    }


    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
            Log.d("000777", "Get Latitude: " + latitude);
        }
        // return latitude
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
            Log.d("000777", "Get longitude: " + longitude);
        }
        // return longitude
        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation();
    }

    public class MyLocationListener implements android.location.LocationListener {


        public void onLocationChanged(Location location) {
            String message = String.format("Current Location \n Longitude: %1$s \n Latitude: %2$s", location.getLongitude(), location.getLatitude());

            /*double speed = location.getSpeed(); //spedd in meter/minute
            speed = (speed*3600)/1000;      // speed in km/minute
            Log.d("000777", "Speeed !!!!!:" + speed);*/
        }

        public void onStatusChanged(String s, int i, Bundle b) {
            //Toast.makeText(ctx, "Provider status changed",Toast.LENGTH_LONG).show();
            Log.d("000777", "status changed:" + "");

            // Toast.makeText(mContext, R.string.GPSonMessage, Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
//            Toast.makeText(ServiceLocation.this,"Please turn on GPS",Toast.LENGTH_SHORT).show();

           /* if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )

            Log.d("000777", "GPS Disable:!!!!!!!!!!!!!! ");
            else
            Log.d("000777", "GPS Enable: !!!!");*/

            dialog.dismiss();
            mCountDownTimer.cancel();

            if (temp_var.equalsIgnoreCase("0")) {
                showSettingsAlert();
                Log.d("000777", "GPS Disable !!!!!!!!!!!!!!!!!!!: ");
           //     Log.d("000777", "Turn on GPS: ");

            } else {
                Log.d("000777", "GPS Disable ELSEEEEEEE !!!!!!!!!!!!!!!!!!! ");
            }
            dialog.dismiss();

        }

        public void onProviderEnabled(String s) {

            alertDialog.dismiss();
            dialog.show();

            startTimer();

            Log.d("000777", "GPS ENABLE NOW !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            mCount = 0;
            //Toast.makeText(ctx,"Provider enabled by the user. GPS turned on",Toast.LENGTH_LONG).show();
        }
    }

    public void showSettingsAlert() {
        temp_var = "1";

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
      //dialogBuilder.setTitle("جی پی ایس");
      //  dialogBuilder.setIcon(mContext.getResources().getDrawable(R.drawable.ic_gps_off_black_24dp));
        dialogBuilder.setMessage("برائے مہربانی اپنے موبائل کے جی پی ایس کو فعال کریں؟");
        dialogBuilder.setPositiveButton("سیٹنگ", null);
     //   dialogBuilder.setNegativeButton("منسوخ کریں", null);


        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        TextView messageView = (TextView)alertDialog.findViewById(android.R.id.message);
        messageView.setTextSize(18);


        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        // override the text color of positive button
        positiveButton.setTextColor(mContext.getResources().getColor(R.color.pink_color));
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
                temp_var = "0";
            }
        });

        /*negativeButton.setTextColor(mContext.getResources().getColor(R.color.pink_color));
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                temp_var = "0";
            }
        });*/
    }
}