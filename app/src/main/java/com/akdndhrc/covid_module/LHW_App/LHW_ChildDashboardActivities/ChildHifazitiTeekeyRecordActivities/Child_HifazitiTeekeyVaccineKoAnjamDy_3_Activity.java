package com.akdndhrc.covid_module.LHW_App.LHW_ChildDashboardActivities.ChildHifazitiTeekeyRecordActivities;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.CustomClass.NetworkClient;
import com.akdndhrc.covid_module.CustomClass.UploadAPIs;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.GPSTracker;
import com.akdndhrc.covid_module.LHW_App.HomePage_Activity;
import com.akdndhrc.covid_module.ServiceLocation;
import com.akdndhrc.covid_module.Utils;
import com.akdndhrc.covid_module.slider.DefaultExceptionHandler;
import com.akdndhrc.covid_module.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import uk.co.senab.photoview.PhotoViewAttacher;

import static android.graphics.BitmapFactory.decodeFile;
import static com.akdndhrc.covid_module.R.string.dataGPS;
import static com.akdndhrc.covid_module.R.string.imageNotSaveSomethingWrong;


public class Child_HifazitiTeekeyVaccineKoAnjamDy_3_Activity extends AppCompatActivity {

    Context ctx = Child_HifazitiTeekeyVaccineKoAnjamDy_3_Activity.this;

    private static final String TAG = "000269";
    public static final int MEDIA_TYPE_IMAGE = 1;
    ImageView iv, iv_navigation_drawer, iv_home;
    Button btn_submit_and_back, btn_submit_and_next_vaccine;
    double latitude;
    double longitude;
    // GPSTracker class
    GPSTracker gps;
    String child_uid, child_name, child_gender;
    String TodayDate, vacine_uid, vacine_name, vacine_place, file_value, vac_duedate, timeStamp, vac_place_pos, vial_type, vac_groupId, added_on;
    ContentValues values;
    Uri imageUri = Uri.parse(Environment.getExternalStorageDirectory().toString() + "/HayatPK/" + "Vaccines/");
    private static final int PICTURE_RESULT = 100;
    Bitmap thumbnail;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    File image_path;
    Dialog alertDialog;

    PhotoViewAttacher photoViewAttacher;
    Snackbar snackbar;
    ServiceLocation serviceLocation;
    String login_useruid, image_base64;
    TextView tv_vaccineName;
    String[][] mData;
    LinearLayout ll_buttons;
    int diffInDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_hifaziti_teekey_vaccine_ko_anjam_dy_2);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, Child_HifazitiTeekeyVaccineKoAnjamDy_3_Activity.class));

        child_uid = getIntent().getExtras().getString("u_id");
        vacine_uid = getIntent().getExtras().getString("vac_id");
        vacine_name = getIntent().getExtras().getString("vac_name");
        vacine_place = getIntent().getExtras().getString("vac_place");
        vac_place_pos = getIntent().getExtras().getString("vac_place_pos");
        vac_duedate = getIntent().getExtras().getString("vac_duedate");
        child_name = getIntent().getExtras().getString("child_name");
        child_gender = getIntent().getExtras().getString("child_gender");
        vial_type = getIntent().getExtras().getString("vial_type");
        vac_groupId = getIntent().getExtras().getString("vac_groupId");
        diffInDays = getIntent().getExtras().getInt("diffInDays");

        Object[] objectArray = (Object[]) getIntent().getExtras().getSerializable("mData");
        if (objectArray != null) {
            mData = new String[objectArray.length][];
            for (int i = 0; i < objectArray.length; i++) {
                mData[i] = (String[]) objectArray[i];
                Log.d("000269", "mData[i] : " + mData[i].toString());
            }
        }

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences(getString(R.string.userLogin), 0); // 0 - for private mode
            String shared_useruid = prefelse.getString(("login_userid"), null); // getting String
            login_useruid = shared_useruid;
            Log.d("000269", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000269", "Shared Err:" + e.getMessage());
        }

        try {
            serviceLocation = new ServiceLocation(ctx);
            serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            serviceLocation.callAsynchronousTask();
        } catch (Exception e) {
            Log.d("000269", "GPS Service Err:  " + e.getMessage());
        }


        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

        //ImageView
        iv = findViewById(R.id.iv);
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);

        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);

        //TextView
        tv_vaccineName = findViewById(R.id.tv_vaccineName);
        tv_vaccineName.setText(vacine_name + getString(R.string.vaccName));

        //LinearLayout
        ll_buttons = findViewById(R.id.ll_buttons);

        init_Directories();

        //Button
        btn_submit_and_next_vaccine = (Button) findViewById(R.id.btn_submit_and_next_vaccine);
        btn_submit_and_back = (Button) findViewById(R.id.btn_submit_and_back);


        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(ctx, HomePage_Activity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
            }
        });

        LaunchCamera();


        btn_submit_and_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                alertDialog = new Dialog(ctx);
                LayoutInflater layout = LayoutInflater.from(ctx);
                final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

                alertDialog.setContentView(dialogView);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();


                try {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            insert_db(v);

                        }
                    }, 2000);

                } catch (Exception e) {
                    e.printStackTrace();
                    alertDialog.dismiss();
                    File file = new File(String.valueOf(image_path));
                    Log.d("000269", "Delete Path :" + image_path);
                    file.delete();
                    finish();
                    Toast.makeText(ctx, imageNotSaveSomethingWrong, Toast.LENGTH_SHORT).show();
                    Log.d("000269", "Img_Err:" + e.getMessage());
                }
            }
        });


        btn_submit_and_next_vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                alertDialog = new Dialog(ctx);
                LayoutInflater layout = LayoutInflater.from(ctx);
                final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

                alertDialog.setContentView(dialogView);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();

                try {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            insert_db_with_next_vaccines(v);

                        }
                    }, 2000);

                } catch (Exception e) {
                    e.printStackTrace();
                    File file = new File(String.valueOf(image_path));
                    Log.d("000269", "Delete Path :" + image_path);
                    file.delete();
                    finish();
                    Toast.makeText(ctx, imageNotSaveSomethingWrong, Toast.LENGTH_SHORT).show();

                    Log.d("000269", "Img_Err:" + e.getMessage());
                }
            }
        });

    }


    public void LaunchCamera() {
        try {

            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("000269", "Error:" + e.getMessage());
                }
            }


       /* Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, PICTURE_RESULT);*/


           /* String timeStamp = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss aa", Locale.getDefault()).format(new Date());
            File cachePath = new File(Environment.getExternalStorageDirectory() + File.separator + "HayatPK" + File.separator + "Vaccines" + File.separator + "IMG_" + timeStamp + ".png");
            file_value = String.valueOf(cachePath);
            Log.d("000269", "Path:" + file_value);*/

            File cachePath = createImageFile();
            cachePath.createNewFile();
            imageUri = Uri.fromFile(cachePath);
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(i, PICTURE_RESULT);

            Log.d("000269", "try:");

        } catch (Exception e) {
            Log.d("000269", "Cam Err:" + e.getMessage());
        }
    }

    private void init_Directories() {

        try {

            String folder_Vaccines = Environment.getExternalStorageDirectory() + File.separator + "HayatPK" + File.separator + "Vaccines";
            File directory = new File(folder_Vaccines);


            boolean success = true;
            if (!directory.exists()) {
                directory.mkdirs();
            }

            if (success) {
                // Toast.makeText(ctx, "Suceess", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(ctx, "Failed", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            // Toast.makeText(this, "Faild to Create Folder", Toast.LENGTH_SHORT).show();
            Log.d("Folder", "Failed" + e.getMessage());
        }
    }

    private void check_other_vaccines() {

        try {

            Lister ls = new Lister(ctx);
            ls.createAndOpenDB();


            String abc = "SELECT t0.name,t0.uid,t1.vaccinated_on from VACCINES t0" +
                    " LEFT JOIN CVACCINATION t1 ON t0.uid = t1.vaccine_id  AND t1.member_uid = '" + child_uid + "'" +
                    " where t0.due_date='" + vac_groupId + "' AND t1.vaccinated_on is NULL ORDER BY t0.name LIMIT 1";

            Boolean res = ls.executeNonQuery(abc);
            String[][] mData_vaccines_left = ls.executeReader(abc);
            Log.d("000269", "Data: " + abc);
            Log.d("000269", "Query: " + res);

            //  Log.d("000269", "LENGTH: " + mData_vaccines_left.length);

            if (mData_vaccines_left != null) {
                vacine_name = mData_vaccines_left[0][0];
                vacine_uid = mData_vaccines_left[0][1];
                //tv_vaccineName.setText(mData_vaccines_left[0][0]);

                tv_vaccineName.setText(mData_vaccines_left[0][0] + getString(R.string.newVaccName));
                Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blinking_animation);
                tv_vaccineName.startAnimation(startAnimation);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        tv_vaccineName.clearAnimation();
                    }
                }, 5000);

                Log.d("000269", "Vacc Name: " + mData_vaccines_left[0][0]);

                /*if (mData_vaccines_left[0][0].equalsIgnoreCase("OPV-0")) {

                    if (diffInDays > 45) {
                        Log.d("000269", "OPV-0 Disable Now!!!!!!! ");
                        tv_vaccineName.setText(" ویکسین اب ہمیشہ کے لئے غیر فعال ہے OPV-0");
                        ll_buttons.setVisibility(View.GONE);

                        final Snackbar snackbar = Snackbar.make(findViewById(R.id.vaccine_ko_anjaam_dy), R.string.vaccinationCompleted, Snackbar.LENGTH_SHORT);
                        View mySbView = snackbar.getView();
                        mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                        TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(16);
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                        }
                        snackbar.setDuration(4000);
                        snackbar.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 3000);
                    } else {
                        tv_vaccineName.setText(mData_vaccines_left[0][0] + " ویکسین نام  ");
                        Log.d("000269", "Vacc Name: " + mData_vaccines_left[0][0]);
                    }
                } else {
                    tv_vaccineName.setText(mData_vaccines_left[0][0] + " ویکسین نام  ");
                    Log.d("000269", "Vacc Name: " + mData_vaccines_left[0][0]);
                }*/

            } else {
                Log.d("000269", "ELSE Vacc Name: ");

                ll_buttons.setVisibility(View.GONE);

                final Snackbar snackbar = Snackbar.make(findViewById(R.id.vaccine_ko_anjaam_dy), R.string.vaccinationCompleted, Snackbar.LENGTH_SHORT);
                View mySbView = snackbar.getView();
                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(16);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
                }
                snackbar.setDuration(4000);
                snackbar.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();

                    }
                }, 3000);
            }

        } catch (Exception e) {
            Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();

            Log.d("000269", "Check vaccine Err : " + e.getMessage());

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("000269", "requestCode:" + requestCode);
        Log.d("000269", "resultCode:" + resultCode);
        if (requestCode == PICTURE_RESULT) {
            if (resultCode == RESULT_OK) {
                Log.d("000269", "A:");
                try {
                    mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    mImageBitmap = scaleDown(mImageBitmap, 640, true);
                    iv.setTag(true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                    byte[] byteArray = stream.toByteArray();


                    //COnvert imagebyteArray into strings
                  /*  String image_bytearray = new String(byteArray);
                    Log.d("000269", "Image BYTE :" + image_bytearray);*/

                    try {
                        //Convert image into Base64
                        image_base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        Log.d("000269", "Image BAse 64:" + image_base64.toString());
                    } catch (Exception e) {
                        Log.d("000269", "Image Base64 :" + e.getMessage());
                    }


                    Log.d("000269", "B:");
                    FileOutputStream fo = new FileOutputStream(new File(mCurrentPhotoPath));

                    Log.d("000269", "C:");

                    fo.write(byteArray);
                    fo.flush();
                    fo.close();
                    mImageBitmap.recycle();

                    iv.setImageBitmap(decodeFile(String.valueOf(mCurrentPhotoPath)));
                    photoViewAttacher = new PhotoViewAttacher(iv);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.d("000269", "Img_Er11r:" + e.getMessage());
                    Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();

                    File file = new File(String.valueOf(image_path));
                    Log.d("000269", "Delete Path :" + image_path);
                    file.delete();
                    Intent newIntent = new Intent(ctx, Child_HifazitiTeekeyRecordList2_Activity.class);
                    newIntent.putExtra("u_id", child_uid);
                    newIntent.putExtra("child_name", child_name);
                    newIntent.putExtra("child_gender", child_gender);
                    startActivity(newIntent);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                    Log.d("000269", "Img_Err:" + e.getMessage());
                    File file = new File(String.valueOf(image_path));
                    Log.d("000269", "Delete Path :" + image_path);
                    file.delete();
                    Intent newIntent = new Intent(ctx, Child_HifazitiTeekeyRecordList2_Activity.class);
                    newIntent.putExtra("u_id", child_uid);
                    newIntent.putExtra("child_name", child_name);
                    newIntent.putExtra("child_gender", child_gender);
                    startActivity(newIntent);
                }


            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
                Log.d("000269", "ELSE IF:");
                File file = new File(String.valueOf(image_path));
                Log.d("000269", "Delete Path :" + image_path);
                file.delete();
                Intent newIntent = new Intent(ctx, Child_HifazitiTeekeyRecordList2_Activity.class);
                newIntent.putExtra("u_id", child_uid);
                newIntent.putExtra("child_name", child_name);
                newIntent.putExtra("child_gender", child_gender);
                startActivity(newIntent);
            } else {
                // Image capture failed, advise user
                Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
                Log.d("000269", "ELSE:");
                File file = new File(String.valueOf(image_path));
                Log.d("000269", "Delete Path :" + image_path);
                file.delete();
                Intent newIntent = new Intent(ctx, Child_HifazitiTeekeyRecordList2_Activity.class);
                newIntent.putExtra("u_id", child_uid);
                newIntent.putExtra("child_name", child_name);
                newIntent.putExtra("child_gender", child_gender);
                startActivity(newIntent);
            }
        }


//Working  code

       /* if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            try {

                File photoFile = null;
                photoFile = createImageFile();

                thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                thumbnail= scaleDown(thumbnail, 640, true);

                photoFile.createNewFile();
                Log.d("000269", "ssss:");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                byte[] byteArray = stream.toByteArray();

                Log.d("000269", "File_Path:" + image_path);

                // Log.d("000269", "mCurrentPhotoPath:" + mCurrentPhotoPath);
                FileOutputStream fo = new FileOutputStream(image_path);
                fo.write(byteArray);
                fo.flush();
                fo.close();
                thumbnail.recycle();

                Log.d("000269", "Created Image:");
                iv.setImageBitmap(decodeFile(String.valueOf(image_path)));
                photoViewAttacher = new PhotoViewAttacher(iv);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ctx, "Image not saved", Toast.LENGTH_SHORT).show();
                Log.d("000269", "Img_Err:" + e.getMessage());
            }

            /*finally {
                finish();
            }*/

      /*  } else {
            Log.d("000269", "Else");
            finish();
        }*/


    }


    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String uuid = UUID.randomUUID().toString().replace("-", "");
        Log.d("000269", "UUID:" + uuid);

        SimpleDateFormat dates = new SimpleDateFormat("ddMMyyyy_hhmmss");
        Calendar c = Calendar.getInstance();
        timeStamp = dates.format(c.getTime());
        Log.d("000269", "timestamp:" + timeStamp);


        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").toString();
        String imageFileName = "IMG_" + timeStamp + "_" + uuid;
        String folder_Vaccines = Environment.getExternalStorageDirectory() + File.separator + "HayatPK" + File.separator + "Vaccines";
        File storageDir = new File(folder_Vaccines);
        image_path = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );
        mCurrentPhotoPath = image_path.getPath();
        Log.d("000269", "FilePath: " + image_path.getPath());

        // Save a file: path for use with ACTION_VIEW intents
        //imageUri = Uri.fromFile(image_path);
        Log.d("000269", "ImageURI (create file): " + mCurrentPhotoPath);
        return image_path;
    }

    public void insert_db(View v) {

        Lister ls = new Lister(Child_HifazitiTeekeyVaccineKoAnjamDy_3_Activity.this);
        ls.createAndOpenDB();

        try {

            String ans1 = "insert into VACCINE_IMAGE (member_uid,vaccine_id, type, image_data)" +
                    "values" +
                    "(" +
                    "'" + child_uid + "'," +
                    "'" + vacine_uid + "'," +
                    "'" + "0" + "'," +
                    "'" + image_base64 + "'" +
                    ")";

            Boolean res = ls.executeNonQuery(ans1);
            Log.d("000269", "Data Added to Vaccine Image: " + ans1);
            Log.d("000269", "Query: " + res);

        } catch (Exception e) {
            Log.d("000269", " Error: " + e.getMessage());
        }


        try {
            if (serviceLocation.showCurrentLocation() == true) {

                latitude = serviceLocation.getLatitude();
                longitude = serviceLocation.getLongitude();

                Log.d("000269", " latitude: " + latitude);
                Log.d("000269", " longitude: " + longitude);
            } else {

                try {
                    serviceLocation.doAsynchronousTask.cancel();
                } catch (Exception e) {
                }
                try {
                    /*Lister ls = new Lister(ctx);*
                    ls.createAndOpenDB();
*/
                    String[][] mData = ls.executeReader("SELECT max(added_on),metadata,count(*) from CVACCINATION");

                    if (Integer.parseInt(mData[0][2]) > 0) {
                        JSONObject jsonObject = new JSONObject(mData[0][1]);
                        Log.d("000269", "  Last Latitude: " + jsonObject.getString("lat"));
                        Log.d("000269", "  Last Longitude: " + jsonObject.getString("lng"));

                        latitude = Double.parseDouble(jsonObject.getString("lat"));
                        longitude = Double.parseDouble(jsonObject.getString("lng"));

                        Toast.makeText(ctx, R.string.dataGPS, Toast.LENGTH_SHORT).show();
                    } else {
                        latitude = Double.parseDouble("0.0");
                        longitude = Double.parseDouble("0.0");
                        Toast.makeText(ctx, R.string.notDataGPS, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000269", "Read CVACCINE Error: " + e.getMessage());
                }
            }

            /*Lister ls = new Lister(Child_HifazitiTeekeyVaccineKoAnjamDy_3_Activity.this);
            ls.createAndOpenDB();*/


            SimpleDateFormat dates = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss aa");
            Calendar c = Calendar.getInstance();
            String current_timeStamp = dates.format(c.getTime());
            Log.d("000269", "timestamp:" + current_timeStamp);


            JSONObject jobj = new JSONObject();
            jobj.put("lat", "" + String.valueOf(latitude));
            jobj.put("lng", "" + String.valueOf(longitude));
            jobj.put("type_name", "" + "vaccine_ko_anjam_dy");
            jobj.put("vacine_name", "" + vacine_name);
            jobj.put("vaccine_place", "" + vacine_place);
            jobj.put("vaccine_place_pos", "" + vac_place_pos);
            jobj.put("image_path", "" + image_path);
            jobj.put("datetime", "" + current_timeStamp);

            added_on = String.valueOf(System.currentTimeMillis());

            // jobjMain.put("data", jobj);
            String ans1 = "insert into CVACCINATION (member_uid,vaccine_id, record_data, type, due_date,vaccinated_on,image_location,metadata,added_by, is_synced,added_on)" +
                    "values" +
                    "(" +
                    "'" + child_uid + "'," +
                    "'" + vacine_uid + "'," +
                    "'" + TodayDate + "'," +
                    "'" + "0" + "'," +
                    "'" + vac_duedate + "'," +
                    "'" + TodayDate + "'," +
                    "'" + image_path + "'," +
                    "'" + jobj + "'," +
                    "'" + login_useruid + "'," +
                    "'0'," +
                    "'" + added_on + "'" +
                    ")";

            Boolean res = ls.executeNonQuery(ans1);
            Log.d("000269", "Data: " + ans1);
            Log.d("000269", "Query: " + res);


            final Snackbar snackbar = Snackbar.make(v, R.string.vaccDataSubmitted, Snackbar.LENGTH_SHORT);

            View mySbView = snackbar.getView();
            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(16);
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
            }
            snackbar.setDuration(2000);
            snackbar.show();

            if (Utils.haveNetworkConnection(ctx) > 0) {

                sendPostRequest(child_uid, vacine_uid, TodayDate, String.valueOf(jobj), login_useruid, added_on);
            } else {
                //Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();
            }

                if (vacine_name.equalsIgnoreCase("BCG")) {
                    if (vial_type.equalsIgnoreCase("1")) {
                        try {
                        String[] x = new String[3];
                        x[0] =  "f11b36cbab03a185"+ "@" + "BCG";
                        x[1] = "b7ebd5f6ce0ed965" + "@" + "0.05 ML Syring (BCG)";
                        x[2] = "b7ebdc68e83145a9" + "@" + "2 ML Syring (BCG)";

                        for (int i = 0; i < x.length; i++) {

                            String vial = "insert into CVACCINE_VIALS (member_uid,vaccine_id, vaccine_name, type,added_by,added_on)" +
                                    "values" +
                                    "(" +
                                    "'" + child_uid + "'," +
                                    "'" + x[i].split("@")[0] + "'," +
                                    "'" + x[i].split("@")[1] + "'," +
                                    "'" + vial_type + "'," +
                                    "'" + login_useruid + "'," +
                                    "'" + added_on + "'" +
                                    ")";

                            Boolean res_vial = ls.executeNonQuery(vial);
                            Log.d("000269", "BCG Data New VIALS: " + vial);
                            Log.d("000269", "Query: " + res_vial);

                            Log.d("000269", "BCG NEW CVACINE VIAL ADDED SUCCESSFULLY !!!!!!!!!!!!: " + res);
                        }
                        } catch (Exception e) {
                            Log.d("000269", "BCG NEW  VIAL Error: " + e.getMessage());
                        }
                    }
                    else {
                        try {
                            String[] x = new String[2];
                            x[0] =  "f11b36cbab03a185"+ "@" + "BCG";
                            x[1] = "b7ebd5f6ce0ed965" + "@" + "0.05 ML Syring (BCG)";

                            for (int i = 0; i < x.length; i++) {

                                String vial = "insert into CVACCINE_VIALS (member_uid,vaccine_id, vaccine_name, type,added_by,added_on)" +
                                        "values" +
                                        "(" +
                                        "'" + child_uid + "'," +
                                        "'" + x[i].split("@")[0] + "'," +
                                        "'" + x[i].split("@")[1] + "'," +
                                        "'" + vial_type + "'," +
                                        "'" + login_useruid + "'," +
                                        "'" + added_on + "'" +
                                        ")";

                                Boolean res_vial = ls.executeNonQuery(vial);
                                Log.d("000269", "BCG Data OPEN VIALS: " + vial);
                                Log.d("000269", "Query: " + res_vial);

                                Log.d("000269", "BCG OPEN CVACINE VIAL ADDED SUCCESSFULLY !!!!!!!!!!!!: " + res);
                            }
                        } catch (Exception e) {
                            Log.d("000269", "BCG OPEN VIAL Error: " + e.getMessage());
                        }

                    }


                } else if (vacine_name.startsWith("Measles")) {
                    try {
                        String[] x = new String[2];
                        x[0] =  vacine_uid+ "@" + vacine_name;
                        x[1] = "b7ebdda8609d4131" + "@" + "5 ML Syring (Measles)";

                        for (int i = 0; i < x.length; i++) {

                            String vial = "insert into CVACCINE_VIALS (member_uid,vaccine_id, vaccine_name, type,added_by,added_on)" +
                                    "values" +
                                    "(" +
                                    "'" + child_uid + "'," +
                                    "'" + x[i].split("@")[0] + "'," +
                                    "'" + x[i].split("@")[1] + "'," +
                                    "'" + vial_type + "'," +
                                    "'" + login_useruid + "'," +
                                    "'" + added_on + "'" +
                                    ")";

                            Boolean res_vial = ls.executeNonQuery(vial);
                            Log.d("000269", "MEASLES Data New VIALS: " + vial);
                            Log.d("000269", "Query: " + res_vial);

                            Log.d("000269", "MEASLES NEW CVACINE VIAL ADDED SUCCESSFULLY !!!!!!!!!!!!: " + res);
                        }
                    } catch (Exception e) {
                        Log.d("000269", "MEASLES NEW  VIAL Error: " + e.getMessage());
                    }
                }

                else {
                    try {
                        String[] x = new String[2];
                        x[0] =  vacine_uid+ "@" + vacine_name;
                        x[1] = "b7ebd72c8bbbbd9d" + "@" + ".5 ML Syring";

                        for (int i = 0; i < x.length; i++) {

                            Log.d("000269", "Array: " + i + "@" + x[i].split("@")[0]);
                            Log.d("000269", "Array: " + i + "@" + x[i].split("@")[1]);

                        String vial = "insert into CVACCINE_VIALS (member_uid,vaccine_id, vaccine_name, type,added_by,added_on)" +
                                "values" +
                                "(" +
                                "'" + child_uid + "'," +
                                "'" + x[i].split("@")[0] + "'," +
                                "'" + x[i].split("@")[1] + "'," +
                                "'2'," +
                                "'" + login_useruid + "'," +
                                "'" + added_on + "'" +
                                ")";

                        Boolean res_vial = ls.executeNonQuery(vial);
                        Log.d("000269", vacine_name+" Data VIALS: " + vial);
                        Log.d("000269", "Query: " + res_vial);

                        Log.d("000269", vacine_name+" CVACINE VIAL ADDED SUCCESSFULLY !!!!!!!!!!!!: " + res);
                        }
                    } catch (Exception e) {
                        Log.d("000269", vacine_name+ "Vaccine VIAL Error: " + e.getMessage());
                    }
                }

            //  Toast.makeText(getApplicationContext(),String.valueOf(res)+String.valueOf(ans1),Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            alertDialog.dismiss();
            Log.d("000269", " Error: " + e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                    finish();
                }
            },2000);
        }
    }

    public void insert_db_with_next_vaccines(View v) {


        Lister ls = new Lister(Child_HifazitiTeekeyVaccineKoAnjamDy_3_Activity.this);
        ls.createAndOpenDB();

        try {

            String ans1 = "insert into VACCINE_IMAGE (member_uid,vaccine_id, type, image_data)" +
                    "values" +
                    "(" +
                    "'" + child_uid + "'," +
                    "'" + vacine_uid + "'," +
                    "'" + "0" + "'," +
                    "'" + image_base64 + "'" +
                    ")";

            Boolean res = ls.executeNonQuery(ans1);
            Log.d("000269", "Data Added to Vaccine Image: " + ans1);
            Log.d("000269", "Query: " + res);

        } catch (Exception e) {
            Log.d("000269", " Error: " + e.getMessage());
        }


        try {
            if (serviceLocation.showCurrentLocation() == true) {

                latitude = serviceLocation.getLatitude();
                longitude = serviceLocation.getLongitude();

                Log.d("000269", " latitude: " + latitude);
                Log.d("000269", " longitude: " + longitude);
            } else {

                try {
                    serviceLocation.doAsynchronousTask.cancel();
                } catch (Exception e) {
                }
                try {
//                    Lister ls = new Lister(ctx);
//                    ls.createAndOpenDB();

                    String[][] mData = ls.executeReader("SELECT max(added_on),metadata,count(*) from CVACCINATION");

                    if (Integer.parseInt(mData[0][2]) > 0) {
                        JSONObject jsonObject = new JSONObject(mData[0][1]);
                        Log.d("000269", "  Last Latitude: " + jsonObject.getString("lat"));
                        Log.d("000269", "  Last Longitude: " + jsonObject.getString("lng"));

                        latitude = Double.parseDouble(jsonObject.getString("lat"));
                        longitude = Double.parseDouble(jsonObject.getString("lng"));

                        Toast.makeText(ctx, R.string.dataGPS, Toast.LENGTH_SHORT).show();
                    } else {
                        latitude = Double.parseDouble("0.0");
                        longitude = Double.parseDouble("0.0");
                        Toast.makeText(ctx, R.string.notDataGPS, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000269", "Read CVACCINE Error: " + e.getMessage());
                }
            }



            /*Lister ls = new Lister(Child_HifazitiTeekeyVaccineKoAnjamDy_3_Activity.this);
            ls.createAndOpenDB();*/


            SimpleDateFormat dates = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss aa");
            Calendar c = Calendar.getInstance();
            String current_timeStamp = dates.format(c.getTime());
            Log.d("000269", "timestamp:" + current_timeStamp);


            JSONObject jobj = new JSONObject();
            jobj.put("lat", "" + String.valueOf(latitude));
            jobj.put("lng", "" + String.valueOf(longitude));
            jobj.put("type_name", "" + "vaccine_ko_anjam_dy");
            jobj.put("vacine_name", "" + vacine_name);
            jobj.put("vaccine_place", "" + vacine_place);
            jobj.put("vaccine_place_pos", "" + vac_place_pos);
            jobj.put("image_path", "" + image_path);
            jobj.put("datetime", "" + current_timeStamp);

            added_on = String.valueOf(System.currentTimeMillis());

            // jobjMain.put("data", jobj);
            String ans1 = "insert into CVACCINATION (member_uid,vaccine_id, record_data, type, due_date,vaccinated_on,image_location,metadata,added_by, is_synced,added_on)" +
                    "values" +
                    "(" +
                    "'" + child_uid + "'," +
                    "'" + vacine_uid + "'," +
                    "'" + TodayDate + "'," +
                    "'" + "0" + "'," +
                    "'" + vac_duedate + "'," +
                    "'" + TodayDate + "'," +
                    "'" + image_path + "'," +
                    "'" + jobj + "'," +
                    "'" + login_useruid + "'," +
                    "'0'," +
                    "'" + added_on + "'" +
                    ")";

            Boolean res = ls.executeNonQuery(ans1);
            Log.d("000269", "Data: " + ans1);
            Log.d("000269", "Query: " + res);


            final Snackbar snackbar = Snackbar.make(v, R.string.vaccDataSubmitted, Snackbar.LENGTH_SHORT);
            View mySbView = snackbar.getView();
            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            mySbView.setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
            TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(16);
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setCompoundDrawableTintList(ctx.getResources().getColorStateList(R.color.green_color));
            }
            snackbar.setDuration(2000);
            snackbar.show();

            if (Utils.haveNetworkConnection(ctx) > 0) {

                sendPostRequest(child_uid, vacine_uid, TodayDate, String.valueOf(jobj), login_useruid, added_on);
            } else {
               // Toast.makeText(ctx, R.string.dataSubmissionMessage, Toast.LENGTH_SHORT).show();
            }

            if (vacine_name.equalsIgnoreCase("BCG")) {
                if (vial_type.equalsIgnoreCase("1")) {
                    try {
                        String[] x = new String[3];
                        x[0] =  "f11b36cbab03a185"+ "@" + "BCG";
                        x[1] = "b7ebd5f6ce0ed965" + "@" + "0.05 ML Syring (BCG)";
                        x[2] = "b7ebdc68e83145a9" + "@" + "2 ML Syring (BCG)";

                        for (int i = 0; i < x.length; i++) {

                            String vial = "insert into CVACCINE_VIALS (member_uid,vaccine_id, vaccine_name, type,added_by,added_on)" +
                                    "values" +
                                    "(" +
                                    "'" + child_uid + "'," +
                                    "'" + x[i].split("@")[0] + "'," +
                                    "'" + x[i].split("@")[1] + "'," +
                                    "'" + vial_type + "'," +
                                    "'" + login_useruid + "'," +
                                    "'" + added_on + "'" +
                                    ")";

                            Boolean res_vial = ls.executeNonQuery(vial);
                            Log.d("000269", "BCG Data New VIALS: " + vial);
                            Log.d("000269", "Query: " + res_vial);

                            Log.d("000269", "BCG NEW CVACINE VIAL ADDED SUCCESSFULLY !!!!!!!!!!!!: " + res);
                        }
                    } catch (Exception e) {
                        Log.d("000269", "BCG NEW  VIAL Error: " + e.getMessage());
                    }
                }
                else {
                    try {
                        String[] x = new String[2];
                        x[0] =  "f11b36cbab03a185"+ "@" + "BCG";
                        x[1] = "b7ebd5f6ce0ed965" + "@" + "0.05 ML Syring (BCG)";

                        for (int i = 0; i < x.length; i++) {

                            String vial = "insert into CVACCINE_VIALS (member_uid,vaccine_id, vaccine_name, type,added_by,added_on)" +
                                    "values" +
                                    "(" +
                                    "'" + child_uid + "'," +
                                    "'" + x[i].split("@")[0] + "'," +
                                    "'" + x[i].split("@")[1] + "'," +
                                    "'" + vial_type + "'," +
                                    "'" + login_useruid + "'," +
                                    "'" + added_on + "'" +
                                    ")";

                            Boolean res_vial = ls.executeNonQuery(vial);
                            Log.d("000269", "BCG Data OPEN VIALS: " + vial);
                            Log.d("000269", "Query: " + res_vial);

                            Log.d("000269", "BCG OPEN CVACINE VIAL ADDED SUCCESSFULLY !!!!!!!!!!!!: " + res);
                        }
                    } catch (Exception e) {
                        Log.d("000269", "BCG OPEN VIAL Error: " + e.getMessage());
                    }

                }


            } else if (vacine_name.startsWith("Measles")) {
                try {
                    String[] x = new String[2];
                    x[0] =  vacine_uid+ "@" + vacine_name;
                    x[1] = "b7ebdda8609d4131" + "@" + "5 ML Syring (Measles)";

                    for (int i = 0; i < x.length; i++) {

                        String vial = "insert into CVACCINE_VIALS (member_uid,vaccine_id, vaccine_name, type,added_by,added_on)" +
                                "values" +
                                "(" +
                                "'" + child_uid + "'," +
                                "'" + x[i].split("@")[0] + "'," +
                                "'" + x[i].split("@")[1] + "'," +
                                "'" + vial_type + "'," +
                                "'" + login_useruid + "'," +
                                "'" + added_on + "'" +
                                ")";

                        Boolean res_vial = ls.executeNonQuery(vial);
                        Log.d("000269", "MEASLES Data New VIALS: " + vial);
                        Log.d("000269", "Query: " + res_vial);

                        Log.d("000269", "MEASLES NEW CVACINE VIAL ADDED SUCCESSFULLY !!!!!!!!!!!!: " + res);
                    }
                } catch (Exception e) {
                    Log.d("000269", "MEASLES NEW  VIAL Error: " + e.getMessage());
                }
            }

            else {
                try {
                    String[] x = new String[2];
                    x[0] =  vacine_uid + "@" + vacine_name;
                    x[1] = "b7ebd72c8bbbbd9d" + "@" + ".5 ML Syring";

                    for (int i = 0; i < x.length; i++) {

                        Log.d("000269", "Array: " + i + "@" + x[i].split("@")[0]);
                        Log.d("000269", "Array: " + i + "@" + x[i].split("@")[1]);

                        String vial = "insert into CVACCINE_VIALS (member_uid,vaccine_id, vaccine_name, type,added_by,added_on)" +
                                "values" +
                                "(" +
                                "'" + child_uid + "'," +
                                "'" + x[i].split("@")[0] + "'," +
                                "'" + x[i].split("@")[1] + "'," +
                                "'2'," +
                                "'" + login_useruid + "'," +
                                "'" + added_on + "'" +
                                ")";

                        Boolean res_vial = ls.executeNonQuery(vial);
                        Log.d("000269", vacine_name+" Data VIALS: " + vial);
                        Log.d("000269", "Query: " + res_vial);

                        Log.d("000269", vacine_name+" CVACINE VIAL ADDED SUCCESSFULLY !!!!!!!!!!!!: " + res);
                    }
                } catch (Exception e) {
                    Log.d("000269", vacine_name+ "Vaccine VIAL Error: " + e.getMessage());
                }
            }

            //  Toast.makeText(getApplicationContext(),String.valueOf(res)+String.valueOf(ans1),Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            alertDialog.dismiss();
            Log.d("000269", " Error: " + e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            Log.d("000987", " BAC: ");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    alertDialog.dismiss();
                    check_other_vaccines();
                }
            },2000);


        }
    }


    private void sendPostRequest(final String member_uid, final String vacine_uid, final String record_data,
                                 final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/child/vaccinations";

        Log.d("000269", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {
                        Log.d("000269", "Response:    " + response);

                        //  Toast.makeText(Child_HifazitiTeekeyVaccineKoAnjamDy_2_Activity.this, "Data has been saved", Toast.LENGTH_SHORT).show();

                        Lister ls = new Lister(Child_HifazitiTeekeyVaccineKoAnjamDy_3_Activity.this);
                        ls.createAndOpenDB();

                        String update_record = "UPDATE CVACCINATION SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + child_uid + "'AND added_on= '" + added_on + "'AND vaccine_id= '" + vacine_uid + "'";

                        ls.executeNonQuery(update_record);

                        Toast tt  =Toast.makeText(ctx, R.string.vaccineDataSynced, Toast.LENGTH_SHORT);
                        tt.setGravity(Gravity.CENTER, 0, 0);
                        tt.show();

                    } else {
                        Log.d("000269", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        // Toast.makeText(Child_HifazitiTeekeyVaccineKoAnjamDy_2_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, R.string.noDataSyncServerAlert, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000269", " Error: " + e.getMessage());
                    //       Toast.makeText(Child_HifazitiTeekeyVaccineKoAnjamDy_2_Activity.this, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();
                    Toast tt  =Toast.makeText(ctx, R.string.vaccineDataSynced, Toast.LENGTH_SHORT);

                    tt.setGravity(Gravity.CENTER, 0, 0);
                    tt.show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000269", "onErrorResponse: " + error.getMessage());
                //  Toast.makeText(Child_HifazitiTeekeyVaccineKoAnjamDy_2_Activity.this, "برائے مہربانی انٹرنیٹ کنکشن چیک کریں", Toast.LENGTH_SHORT).show();
                Toast tt  =Toast.makeText(ctx, R.string.vaccineDataSynced, Toast.LENGTH_SHORT);
                tt.setGravity(Gravity.CENTER, 0, 0);
                tt.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("member_id", member_uid);
                params.put("vaccine_id", vacine_uid);
                params.put("type", "0");
                params.put("record_data", record_data);
                params.put("vaccinated_on", TodayDate);
                params.put("image_location", mCurrentPhotoPath);
                params.put("metadata", data);
                params.put("added_by", added_by);
                params.put("added_on", added_on);


                Log.d("000269", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000269", "map ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, REQUEST_TAG);
    }


    private void uploadToServer(String filePath) throws IOException {

        try {

            final Retrofit retrofit = NetworkClient.getRetrofitClient(this);
            UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);

            //Create a file object using file path
            File file = new File(filePath);
            // Create a request body with file and image media type

            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/png"), file);
            RequestBody member_id = RequestBody.create(null, child_uid);
            RequestBody vaccine_id = RequestBody.create(null, vacine_uid);
            RequestBody added_by = RequestBody.create(null, login_useruid);
            RequestBody added_on = RequestBody.create(null, String.valueOf(System.currentTimeMillis()));

            // Create MultipartBody.Part using file request-body,file name and part name
            // MultipartBody.Part part = MultipartBody.Part.createFormData("upload", file.getName(), fileReqBody);
//        //Create request body with text description and text media type
//        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
//        Headers h = new Headers.Builder().add("content-type", "multipart/form-data").build();
//
//        MultipartBody body =  new MultipartBody.Builder()
//                .addFormDataPart("member_id", "a14d2f5sdfdf55200002")
//                .addFormDataPart("vaccine_id", vacine_uid)
//                .addFormDataPart("added_by", user_uuid)
//                .addFormDataPart("added_on", String.valueOf(System.currentTimeMillis()))
//                .addPart(h, fileReqBody).build();
//        Call call = uploadAPIs.upload(body);

//        MultipartBody body = MultipartBody.Builder()
            //
//        Call call = uploadAPIs.uploadImage(fileReqBody);


            Call<ResponseBody> call = uploadAPIs.uploadImage(fileReqBody, member_id, vaccine_id, added_by, added_on);
            call.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {

                        String a = response.body().string();
                        Log.d("000269", "Resp " + a);
                        Toast.makeText(Child_HifazitiTeekeyVaccineKoAnjamDy_3_Activity.this, "" + a, Toast.LENGTH_SHORT).show();


                    } catch (Exception e) {
                        Log.d("000269", "catch: " + e.getMessage());
                        Toast.makeText(Child_HifazitiTeekeyVaccineKoAnjamDy_3_Activity.this, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();


                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("000269", "Failed: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.d("000269", "catch: " + e.getMessage());
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("000269", "BACK ");
        File file = new File(String.valueOf(image_path));
        Log.d("000269", "Delete Path :" + image_path);
        file.delete();
        Intent newIntent = new Intent(ctx, Child_HifazitiTeekeyDueDateList_Activity.class);
        newIntent.putExtra("u_id", child_uid);
        newIntent.putExtra("child_name", child_name);
        newIntent.putExtra("child_gender", child_gender);
        startActivity(newIntent);

    }


    /*    public void LaunchCamera() {
        try {

            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.d("000269", "IOException");
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {

                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);

                }

            }
        }
             catch (Exception e) {
            Log.d("000269", "Cam Err:" + e.getMessage());
        }
    }*/


   /* protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICTURE_RESULT && resultCode == Activity.RESULT_OK) {

            try {

                String timeStamp = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss aa", Locale.getDefault()).format(new Date());
                File cachePath = new File(Environment.getExternalStorageDirectory() + File.separator + "HayatPK" + File.separator + "Vaccines" + File.separator + "IMG_" + timeStamp + ".png");
                file_value = String.valueOf(cachePath);

                thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//               iv.setImageBitmap(thumbnail);

                //Another Way to show Image
                //  mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                //iv.setImageBitmap(mImageBitmap);


                Log.d("000269", "LOC: " + file_value);

                cachePath.createNewFile();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                FileOutputStream fo = new FileOutputStream(cachePath);
                fo.write(byteArray);
                fo.flush();
                fo.close();
                thumbnail.recycle();

                //  insert_db();

                //  uploadToServer(file_value);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("000269", "ERROR: " + e.getMessage());
            }


        } else {
            Log.d("000269", "CANACEL 3");
        }

    }*/

}
