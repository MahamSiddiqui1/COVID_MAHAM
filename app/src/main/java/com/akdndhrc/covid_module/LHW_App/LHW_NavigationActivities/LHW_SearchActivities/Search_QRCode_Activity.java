package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SearchActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.akdndhrc.covid_module.R.string.noQRcodeRecord;

public class Search_QRCode_Activity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    Context ctx = Search_QRCode_Activity.this;

    private ZXingScannerView mScannerView;
    ImageView iv_navigation_drawer, iv_home;
    String[][] mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_qrcode);

        // Programmatically initialize the scanner view
       /* mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);*/

        //ScannerView
        mScannerView = findViewById(R.id.scanner);

        //ImageView
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_navigation_drawer.setVisibility(View.GONE);

        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, Search_MemberAndKhandanList_Activity.class);
                startActivity(intent);
            }
        });

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(ctx, HomePage_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
            }
        });

    }

    @Override
    public void handleResult(final Result rawResult) {
        // Do something with the result here
        Log.v("Search_code", rawResult.getText()); // Prints scan results
        // Prints the scan format (qrcode, pdf417 etc.)
        Log.v("Search_code", rawResult.getBarcodeFormat().toString());
        mScannerView.stopCamera();

        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(Search_QRCode_Activity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog_title_layout, null);
        view.setMinimumHeight(10);
        dialogBuilder.setCustomTitle(view);
        dialogBuilder.setMessage(rawResult.getText());
        dialogBuilder.setPositiveButton(R.string.continue, null);
        dialogBuilder.setNegativeButton(R.string.cancel, null);
        dialogBuilder.setNeutralButton("R.string.rescan", null);

        final android.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button neutral_button = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        // override the text color of positive button
        positiveButton.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                  //  alertDialog.dismiss();
                    // startActivity(new Intent(ctx, Search_MemberAndKhandanList_Activity.class));

                    Lister ls = new Lister(Search_QRCode_Activity.this);
                   ls.createAndOpenDB();
                    mData = ls.executeReader("Select khandan_id from MEMBER where qr_code = '" + rawResult.getText() + "'");
                    if (mData != null) {
                        Log.d("Search_code", mData[0][0]);

                        SharedPreferences settings = getSharedPreferences("shared_preference", MODE_PRIVATE);
                        // Writing data to SharedPreferences
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("qrcode_value", rawResult.getText().toString());
                        editor.putString("type", "qrcode");
                        editor.commit();

                        Log.d("000777", "work");

                        Intent intent = new Intent(ctx, Search_MemberAndKhandanList_Activity.class);
                        startActivity(intent);
                    //    alertDialog.dismiss();
                    } else {
                        Toast.makeText(ctx, noQRcodeRecord, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Log.d("000777", "QR Err: " +e.getMessage());
                    Toast.makeText(ctx, noQRcodeRecord, Toast.LENGTH_LONG).show();
                }
            }

        });

        negativeButton.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                onBackPressed();
            }
        });


        neutral_button.setTextColor(getResources().getColor(R.color.grey_color));
        neutral_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                mScannerView.startCamera();
            }
        });


        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        // Start camera on resume
        mScannerView.startCamera();
    }


    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}