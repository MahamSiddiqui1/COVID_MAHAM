package com.akdndhrc.covid_module;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.akdndhrc.covid_module.Adapter.Practise_Adptr;
import com.akdndhrc.covid_module.R;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    ListView lv;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayList2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        lv = findViewById(R.id.list);


        arrayList.add("Heading 1"+"@"+",0");
        for (int i = 0; i < 5; i++) {
            arrayList.add("Counting " + i+"@"+",1");
        }
        arrayList.add("Heading 2"+"@"+",0");
        for (int i = 0; i < 5; i++) {
            arrayList.add("Counting Change" + i+"@"+",1");
        }
        arrayList.add("Heading 3"+"@"+",0");
        for (int i = 0; i < 5; i++) {
            arrayList.add("Count " + i+"@"+",1");
        }
        Practise_Adptr practise_adptr = new Practise_Adptr(getApplicationContext(),arrayList);
        lv.setAdapter(practise_adptr);

        ObjectAnimator anim = ObjectAnimator.ofInt(lv, "scrollY", 9000);
        anim.start();


    }
}
