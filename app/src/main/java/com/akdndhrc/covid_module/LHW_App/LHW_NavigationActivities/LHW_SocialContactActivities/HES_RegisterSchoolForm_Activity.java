package com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_SocialContactActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.ServiceLocation;
import com.akdndhrc.covid_module.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.akdndhrc.covid_module.R.string.schoolRegistered;

public class HES_RegisterSchoolForm_Activity extends AppCompatActivity {

    Context ctx = HES_RegisterSchoolForm_Activity.this;

    ImageView iv_home;

    EditText et_SchoolName, et_SchoolHeadname, et_mobile_number, et_address;
    Button btn_jamaa_kre;

    ServiceLocation serviceLocation;
    double latitude;
    double longitude;

    String login_useruid,meeting_uid,TodayDate,type;
    public static String var_hes_register = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hes_register_form);

        type = getIntent().getExtras().getString("type");


        var_hes_register ="0";
        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
            String shared_useruid = prefelse.getString(("login_userid"), null); // getting String
            login_useruid = shared_useruid;

         /*   SharedPreferences pre = getApplicationContext().getSharedPreferences("meetingID", 0); // 0 - for private mode
            String shared_meetingId = pre.getString("meeting_uid", null); // getting String
            meeting_uid = shared_meetingId;
*/
            Log.d("000357", "USER UID: " + login_useruid);
          //  Log.d("000357", "Meeting UID: " + meeting_uid);

        } catch (Exception e) {
            Log.d("000357", "Shared Err:" + e.getMessage());
        }


        try {
            serviceLocation = new ServiceLocation(ctx);
            serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            serviceLocation.callAsynchronousTask();
        } catch (Exception e) {
            Log.d("000357", "GPS Service Err:  " + e.getMessage());
        }
        
        //ImageView
        iv_home = findViewById(R.id.iv_home);

        //EditText
        et_SchoolName = findViewById(R.id.et_SchoolName);
        et_SchoolHeadname = findViewById(R.id.et_SchoolHeadname);
        et_mobile_number = findViewById(R.id.et_mobile_number);
        et_address = findViewById(R.id.et_address);



        //Button
        btn_jamaa_kre = findViewById(R.id.submit);


        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(ctx, HomePage_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);

            }
        });


        ///////////////////// Mobile Number /////////////////////////
        et_mobile_number.addTextChangedListener(new TextWatcher() {
            int prev = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prev = et_mobile_number.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if ((prev < length) && (length == 4)) {
                    String data = et_mobile_number.getText().toString();
                    et_mobile_number.setText(data + "-");
                    et_mobile_number.setSelection(length + 1);
                }
            }
        });



        btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_SchoolName.getText().toString().isEmpty()) {

                    final Snackbar snackbar = Snackbar.make(v, R.string.schoolNamePrompt, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(15);
                    snackbar.setDuration(3000);
                    snackbar.show();
                    return;
                }


                if (et_SchoolHeadname.getText().toString().isEmpty()) {

                    final Snackbar snackbar = Snackbar.make(v, R.string.enterSchoolHeadNamePrompt, Snackbar.LENGTH_SHORT);
                    View mySbView = snackbar.getView();
                    mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                    TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(15);
                    snackbar.setDuration(3000);
                    snackbar.show();
                    return;
                }

                if (serviceLocation.showCurrentLocation() == true) {
                    latitude = serviceLocation.getLatitude();
                    longitude = serviceLocation.getLongitude();
                    Log.d("000357", " latitude: " + latitude);
                    Log.d("000357", " longitude: " + longitude);
                } else {

                    serviceLocation.doAsynchronousTask.cancel();
                    try {
                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String[][] mData = ls.executeReader("SELECT max(added_on),data,count(*) from MEMBER");

                        if (Integer.parseInt(mData[0][2]) >  0 ) {
                            JSONObject jsonObject = new JSONObject(mData[0][1]);
                            Log.d("000258", "  Last Latitude: " + jsonObject.getString("lat"));
                            Log.d("000258", "  Last Longitude: " + jsonObject.getString("lng"));

                            latitude = Double.parseDouble(jsonObject.getString("lat"));
                            longitude = Double.parseDouble(jsonObject.getString("lng"));

                            Toast.makeText(ctx, R.string.dataGPS, Toast.LENGTH_SHORT).show();
                        } else {
                            latitude = Double.parseDouble("0.0");
                            longitude = Double.parseDouble("0.0");
                            Toast.makeText(ctx, R.string.notDataGPS, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {

                        Log.d("000258", "Read Member Error Catch: " + e.getMessage());
                    }
                }


                try {
                    SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c = Calendar.getInstance();
                    TodayDate = dates.format(c.getTime());

                    Lister ls = new Lister(ctx);
                    ls.createAndOpenDB();


                    JSONObject jobj = new JSONObject();
                    jobj.put("lat", "" + String.valueOf(latitude));
                    jobj.put("lng", "" + String.valueOf(longitude));
                    jobj.put("school_name", "" + et_SchoolName.getText().toString());
                    jobj.put("school_headname", "" + et_SchoolHeadname.getText().toString());
                    jobj.put("mobile_number", "" + et_mobile_number.getText().toString());
                    jobj.put("address", "" + et_address.getText().toString());


                    String uuid = UUID.randomUUID().toString().replace("-", "");

                    String added_on = String.valueOf(System.currentTimeMillis());

                    //String[][] mData_billID = ls.executeReader("SELECT substr('000' || (SELECT count(*)+1 FROM MEETING_MEMBER where meeting_uid ='"+meeting_uid+"'), -3, 3)");


                    String ans1 = "insert into MEETING_MEMBER (meeting_uid, member_uid, type, metadata, record_data, month,member_status,added_by,is_synced,added_on)" +
                            "values" +
                            "(" +
                            "'" + uuid + "'," +
                            "'" + uuid + "'," +
                            "'" + type + "'," +
                            "'" + jobj + "'," +
                            "'" + TodayDate + "'," +
                            "'" + TodayDate.split("-")[1] + "'," +
                            "'0'," +
                            "'" + login_useruid + "'," +
                            "'0'," +
                            "'" + added_on + "'" +
                            ")";

                    Boolean res = ls.executeNonQuery(ans1);
                    Log.d("000357", "Data: " + ans1);
                    Log.d("000357", "Query:" + res.toString());

                    if (Utils.haveNetworkConnection(ctx) > 0) {
                        sendPostRequest(uuid,uuid, type,TodayDate, String.valueOf(jobj), login_useruid,added_on);
                    } else {
                        Toast.makeText(ctx, schoolRegistered, Toast.LENGTH_SHORT).show();
                    }
                    
                } catch (Exception e) {
                    Log.d("000357", "Err: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {
                    Intent intent = new Intent(ctx, HES_SchoolList_Activity.class);
                    intent.putExtra("type",type);
                    startActivity(intent);
                    var_hes_register ="1";
                }


            }
        });
    }



    private void sendPostRequest(final String meeting_id, final String member_id,final String type, final String record_data, final String metadata,final String added_by, final String added_on)
    {



        String url = "https://development.api.teekoplus.akdndhrc.org/sync/save/meeting/member";

        Log.d("000357", "mURL " + url);


        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {
                        Log.d("000357", "response:    " + response);

                        Lister ls = new Lister(ctx);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE MEETING_MEMBER SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE meeting_uid='"+meeting_id+"' AND member_uid = '" + member_id + "' AND type ='"+type+"'";
                        ls.executeNonQuery(update_record);

                        Toast.makeText(ctx, R.string.dataSynced, Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("000357", "else ");
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000357", "catch: " + e.getMessage());
                    Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000357", "onErrorResponse: " + error.getMessage());
                // Toast.makeText(BelowTwo_Register_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast.makeText(ctx, R.string.noDataSyncAlert, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                        
                Map<String, String> params = new HashMap<String, String>();
                params.put("meeting_id", meeting_id);
                params.put("member_id", member_id);
                params.put("type", type);
                params.put("record_data", record_data);
                params.put("metadata", metadata);
                params.put("added_by", added_by);
                params.put("added_on", added_on);

                Log.d("000357", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000357", "map ");
                Map<String, String> params = new HashMap<String, String>();
               // params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        var_hes_register ="0";
    }
}
