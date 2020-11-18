package com.akdndhrc.covid_module.VAC_App.VAC_NavigationActivities.VAC_SearchActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class VAC_Search_WithFingerprintAndQrCode_Activity extends AppCompatActivity {

    Context ctx = VAC_Search_WithFingerprintAndQrCode_Activity.this;

    ImageView iv_fingerprint , iv_qrcode , iv_navigation_drawer , iv_home;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    String DirectoryName = "TeekoPlus";
    String path = Environment.getExternalStorageDirectory().toString()+"/Pictures/"+DirectoryName;
    private Uri mFileUri;
    public static String mImageName = "";
    ImageView iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_search_with_fingerprint_and_qrcode);

        //ImageView
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_fingerprint = findViewById(R.id.iv_fingerprint);
        iv_qrcode = findViewById(R.id.iv_qrcode);
        iv_home.setVisibility(View.GONE);
        iv_navigation_drawer.setVisibility(View.GONE);

        iv_navigation_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        iv_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ctx,VAC_Search_QRCode_Activity.class));
            }
        });
        iv_fingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LaunchCamer();           }
        });


    }

    private void LaunchCamer() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                try {

                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
                    iv.setImageBitmap((Bitmap) data.getExtras().get("data"));
                    iv.setTag(true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    FileOutputStream fo = new FileOutputStream(new File(path + "/"+mImageName));


                    fo.write(byteArray);
                    fo.flush();
                    fo.close();
                    bmp.recycle();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
