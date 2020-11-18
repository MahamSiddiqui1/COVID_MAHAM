package com.akdndhrc.covid_module;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.akdndhrc.covid_module.Adapter.CustomGridViewAdapter;
import com.akdndhrc.covid_module.R;

import java.io.File;
import java.util.ArrayList;

public class GridVideo_Activity extends AppCompatActivity {

    GridView gridView;
    ArrayList<Item> gridArray = new ArrayList<Item>();
    CustomGridViewAdapter customGridAdapter;

    final ArrayList<String> arrayListPatientFile = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_video_);


        gridView = (GridView) findViewById(R.id.gridView1);



        ListDir();
    }


    void ListDir() {
        File root = new File(Environment.getExternalStorageDirectory() + "/HayatPK/Videos/");

        final ArrayList<String> arrayListPatient = new ArrayList<>();

        File[] files = root.listFiles();

        for (File file : files) {

            Log.d("000111", String.valueOf(file.getAbsoluteFile()));
            Log.d("000000", String.valueOf(file.getPath()));

            Log.d("000121", "URLLLLLLL: " + file);
            Log.d("000121", "FILES 2* *: " + files.length);
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(String.valueOf(file));

            int timeInSeconds = 5;
            Bitmap thumb = ThumbnailUtils.extractThumbnail(retriever.getFrameAtTime(timeInSeconds * 1000000,
                    MediaMetadataRetriever.OPTION_CLOSEST_SYNC), 500, 500);

            gridArray.add(new Item(thumb, file.getName()));

            arrayListPatientFile.add(file.getName());
            arrayListPatient.add(file.getName());//0

            Log.d("000111", "Name:" + arrayListPatient);
        }



        customGridAdapter = new CustomGridViewAdapter(this, R.layout.custom_lv_item_video_list, gridArray);
        gridView.setAdapter(customGridAdapter);


    }

}
