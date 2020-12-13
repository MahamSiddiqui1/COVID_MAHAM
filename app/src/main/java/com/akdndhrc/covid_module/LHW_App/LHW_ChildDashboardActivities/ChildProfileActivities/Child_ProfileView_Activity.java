package com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildProfileActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.akdndhrc.covid_module.Adapter.CustomerAdapter;
import com.akdndhrc.covid_module.CustomClass.Customer;
import com.akdndhrc.covid_module.CustomClass.NothingSelectedSpinnerAdapter;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class Child_ProfileView_Activity extends AppCompatActivity {

    Context ctx = Child_ProfileView_Activity.this;

    Spinner sp_azdawaji_hasiyat, sp_jins;
    EditText et_naam_bimaah_waldiyat, et_walid_ka_naam, et_walida_ka_naam, et_tareekh_indraj, et_sarbarah_kay_sath_rishta, et_tareekh_pedaish, et_shanakhti_card_number,
            et_mobile_number, et_tareekh_nakal_makani, et_tareekh_wafaat, et_tabsarah_wajah_wafaat, et_khandan_number, et_infradi;

    Switch sw_biometric_faal_kre, sw_qrcode_faal_kre;
    Button btn_jamaa_kre;
    RelativeLayout rl_home, rl_navigation_drawer, rl_editform;
    ImageView iv_navigation_drawer, iv_home, iv_editprofile;

    CustomerAdapter adapter = null;
    ArrayList<Customer> customers = null;
    String child_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_profile);
        child_uid = getIntent().getExtras().getString("u_id");
        //Spinner
        sp_azdawaji_hasiyat = findViewById(R.id.sp_azdawaji_hasiyat);
        sp_jins = findViewById(R.id.sp_jins);

        //EditText
        et_naam_bimaah_waldiyat = findViewById(R.id.et_naam_bimaah_waldiyat);
        et_walid_ka_naam = findViewById(R.id.et_walid_ka_naam);
        et_walida_ka_naam = findViewById(R.id.et_walida_ka_naam);
        et_tareekh_indraj = findViewById(R.id.et_tareekh_indraj);
        et_sarbarah_kay_sath_rishta = findViewById(R.id.et_sarbarah_kay_sath_rishta);
        et_tareekh_pedaish = findViewById(R.id.et_tareekh_pedaish);
        et_shanakhti_card_number = findViewById(R.id.et_shanakhti_card_number);
        et_mobile_number = findViewById(R.id.et_mobile_number);
        et_tareekh_nakal_makani = findViewById(R.id.et_tareekh_nakal_makani);
        et_tareekh_wafaat = findViewById(R.id.et_tareekh_wafaat);
        et_tabsarah_wajah_wafaat = findViewById(R.id.et_tabsarah_wajah_wafaat);
        et_khandan_number = findViewById(R.id.et_khandan_number);
        et_infradi = findViewById(R.id.et_infradi);

        //Switch
        sw_biometric_faal_kre = findViewById(R.id.sw_biometric_faal_kre);
        sw_qrcode_faal_kre = findViewById(R.id.sw_qrcode_faal_kre);

        //Buttton
        btn_jamaa_kre = findViewById(R.id.submit);
        btn_jamaa_kre.setVisibility(View.GONE);
        //ImageView
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);
        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);
        iv_editprofile = findViewById(R.id.iv_editprofile);
        iv_editprofile.setVisibility(View.GONE);

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

        iv_editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, R.string.editEng, Toast.LENGTH_SHORT).show();
            }
        });

        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        et_naam_bimaah_waldiyat.setText("سلمان اقبال");
        et_walid_ka_naam.setText("اقبال");
        et_walida_ka_naam.setText("کرن اقبال");
        et_tareekh_indraj.setText("02/02/2019");
        et_sarbarah_kay_sath_rishta.setText("باپ/بیٹا");
        et_tareekh_pedaish.setText("10/02/2019");
        et_shanakhti_card_number.setText("42101-2365996-7");
        et_mobile_number.setText("0340-0007414");
        et_tareekh_nakal_makani.setText("20/02/2019");
        et_tareekh_wafaat.setText("25/02/2019");
        et_khandan_number.setText("123-456");
        et_infradi.setText("231-859");


        // Select sp_jins
      /*  customers = new ArrayList<>();
        customers = populateCustomerData(customers);

        adapter = new CustomerAdapter(this, customers);
        sp_jins.setAdapter(adapter);


        sp_jins.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Customer c = adapter.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        // Select sp_azdawaji_hasiyat
        final ArrayAdapter<CharSequence> adptr_azdawaji_hasiyat = ArrayAdapter.createFromResource(this, R.array.array_azdawaji_hasiyat, android.R.layout.simple_spinner_item);
        adptr_azdawaji_hasiyat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        sp_azdawaji_hasiyat.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adptr_azdawaji_hasiyat,
                        R.layout.spinner_azdawaji_hasiyat_layout,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));


        sp_azdawaji_hasiyat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                ((TextView) parent.getChildAt(0)).setTextColor(Color.GREEN);
                //Toast.makeText(parent.getContext(), "Selected: " + position, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Select sp_jins
        customers = new ArrayList<>();
        customers = populateCustomerData(customers);

        adapter = new CustomerAdapter(this, customers);
        sp_jins.setAdapter(adapter);


        sp_jins.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Customer c = adapter.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private ArrayList<Customer> populateCustomerData(ArrayList<Customer> customers) {
        customers.add(new Customer(getString(R.string.pleaseSelectOne), 1, android.R.color.transparent));
        customers.add(new Customer(getString(R.string.aurat), 2, R.drawable.ic_gender_women_icon));
        customers.add(new Customer(getString(R.string.murd), 3, R.drawable.ic_gender_male_icon));


        return customers;
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            Lister ls = new Lister(ctx);
          ls.createAndOpenDB();
            String mData[][] = ls.executeReader("Select data from MEMBER where uid = '" + child_uid + "'");


            String json = mData[0][0];
            JSONObject jsonObject = new JSONObject(json);


            et_naam_bimaah_waldiyat.setText(jsonObject.getString("full_name"));
            et_walid_ka_naam.setText(jsonObject.getString("father_name"));
            et_walida_ka_naam.setText(jsonObject.getString("mother_name"));
            et_tareekh_indraj.setText(jsonObject.getString("age"));
            et_sarbarah_kay_sath_rishta.setText(jsonObject.getString("relation_with_hof"));
            et_tareekh_pedaish.setText(jsonObject.getString("dob"));
            et_shanakhti_card_number.setText(jsonObject.getString("nic"));
            et_mobile_number.setText(jsonObject.getString("mobile"));
            et_tareekh_nakal_makani.setText(jsonObject.getString("date_of_transfer"));
            et_tareekh_wafaat.setText(jsonObject.getString("date_of_death"));
            et_khandan_number.setText(jsonObject.getString("khandan_number_manual"));
            et_infradi.setText(jsonObject.getString("manual_id"));

            sp_azdawaji_hasiyat.setSelection(Integer.parseInt(jsonObject.getString("maritalstatus")) + 1);
            sp_jins.setSelection(Integer.parseInt(jsonObject.getString("gender")) + 1);
            ls.closeDB();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public void onBackPressed() {

        finish();

    }
}
