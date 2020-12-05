package com.akdndhrc.covid_module.VAC_App.VAC_InsideOutsideUC;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.akdndhrc.covid_module.AppController;
import com.akdndhrc.covid_module.CustomClass.NetworkClient;
import com.akdndhrc.covid_module.CustomClass.UploadAPIs;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.GPSTracker;
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
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

public class VAC_Child_HifazitiTeekeyVaccineKoAnjamDy_Activity extends AppCompatActivity {


    Context ctx = VAC_Child_HifazitiTeekeyVaccineKoAnjamDy_Activity.this;

    private static final String TAG = "000555";
    public static final int MEDIA_TYPE_IMAGE = 1;
    ImageView iv, iv_navigation_drawer, iv_home;
    Button jamma_karayn;
    double latitude;
    double longitude;
    // GPSTracker class
    GPSTracker gps;
    String child_uid;
    String TodayDate, vacine_uid, vacine_name, vacine_place, file_value, vac_duedate, timeStamp, vac_place_pos;
    ContentValues values;
    Uri imageUri = Uri.parse(Environment.getExternalStorageDirectory().toString() + "/HayatPK/" + "Vaccines/");
    // Uri imageUri;
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
    File file;
    String login_useruid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_hifaziti_teekey_vaccine_ko_anjam_dy);

        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this, VAC_Child_HifazitiTeekeyVaccineKoAnjamDy_Activity.class));

        child_uid = getIntent().getExtras().getString("u_id");
        vacine_uid = getIntent().getExtras().getString("vac_id");
        vacine_name = getIntent().getExtras().getString("vac_name");
        vacine_place = getIntent().getExtras().getString("vac_place");
        vac_place_pos = getIntent().getExtras().getString("vac_place_pos");
        vac_duedate = getIntent().getExtras().getString("vac_duedate");


        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        TodayDate = dates.format(c.getTime());

        //Get shared USer name
        try {
            SharedPreferences prefelse = getApplicationContext().getSharedPreferences("UserLogin", 0); // 0 - for private mode
            String shared_useruid = prefelse.getString((R.string.loginUserIDEng), null); // getting String
            login_useruid = shared_useruid;
            Log.d("000555", "USER UID: " + login_useruid);

        } catch (Exception e) {
            Log.d("000555", "Shared Err:" + e.getMessage());
        }

        try {
            serviceLocation = new ServiceLocation(ctx);
            serviceLocation.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            serviceLocation.callAsynchronousTask();
        } catch (Exception e) {
            Log.d("000555", "GPS Service Err:  " + e.getMessage());
        }


        init_Directories();


        //ImageView
        iv = findViewById(R.id.iv);
        iv_navigation_drawer = findViewById(R.id.iv_navigation_drawer);
        iv_home = findViewById(R.id.iv_home);

        iv_navigation_drawer.setVisibility(View.GONE);
        iv_home.setVisibility(View.GONE);


        //Button
        jamma_karayn = (Button) findViewById(R.id.submit);

        LaunchCamera();


        jamma_karayn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (serviceLocation.showCurrentLocation() == true) {
                    latitude = serviceLocation.getLatitude();
                    longitude = serviceLocation.getLongitude();
                    Log.d("000555", " latitude: " + latitude);
                    Log.d("000555", " longitude: " + longitude);
                } else {
                }


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

                            insert_db();

                        }
                    }, 2000);


                } catch (Exception e) {
                    e.printStackTrace();
                    alertDialog.dismiss();
                    finish();
                    Toast.makeText(ctx, "Image not saved", Toast.LENGTH_SHORT).show();
                    Log.d("000555", "Img_Err:" + e.getMessage());

                }
            }
        });
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

    public void LaunchCamera() {
        try {

            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("000555", "Error:" + e.getMessage());
                }
            }

            values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, PICTURE_RESULT);

       /* Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT , "");
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);*/

          /*String timeStamp = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss aa", Locale.getDefault()).format(new Date());
          File cachePath = new File(Environment.getExternalStorageDirectory() + File.separator + "HayatPK" + File.separator + "Vaccines" + File.separator + "IMG_" + timeStamp + ".png");
          file_value = String.valueOf(cachePath);

         imageUri= Uri.fromFile(cachePath);
          Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
          startActivityForResult(i, PICTURE_RESULT);*/

            Log.d("000555", "try:");

        } catch (Exception e) {
            Log.d("000555", "Cam Err:" + e.getMessage());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("000555", "requestCode:" + requestCode);
        Log.d("000555", "resultCode:" + resultCode);
        if (requestCode == PICTURE_RESULT) {

//                 Log.d("000555", "A:" );
//
//                try {
//
//                   // mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(file_value));
//                    mImageBitmap= scaleDown(mImageBitmap, 640, true);
//                   /* iv.setImageBitmap((Bitmap) data.getExtras().get("data"));
//                    iv.setTag(true);*/
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    mImageBitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
//                    byte[] byteArray = stream.toByteArray();
//
//                   /* String timeStamp = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss aa", Locale.getDefault()).format(new Date());
//                    File cachePath = new File(Environment.getExternalStorageDirectory() + File.separator + "HayatPK" + File.separator + "Vaccines" + File.separator + "IMG_" + timeStamp + ".png");
//                    file_value = String.valueOf(cachePath);*/
//
//                    FileOutputStream fo = new FileOutputStream(new File(file_value));
//
//                    Log.d("000555", "B:" );
//
//                    fo.write(byteArray);
//                    fo.flush();
//                    fo.close();
//                    mImageBitmap.recycle();
//
//                    iv.setImageBitmap(decodeFile(String.valueOf(file_value)));
//                    photoViewAttacher = new PhotoViewAttacher(iv);
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                    Log.d("000555", "Img_Er11r:" + e.getMessage());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.d("000555", "Img_Err:" + e.getMessage());
//                }
//
//
//            } else if (resultCode == RESULT_CANCELED) {
//                // User cancelled the image capture
//                Log.d("000555", "ELSE IF:" );
//            } else {
//                // Image capture failed, advise user
//                Log.d("000555", "ELSE:" );
//            }
//        }

            Log.d("000555", "requestCode:" + requestCode);
            Log.d("000555", "resultCode:" + resultCode);
            // code to handle data from CAMERA_REQUEST
            try {

                File photoFile = null;
                photoFile = createImageFile();

//                if (Build.VERSION.SDK_INT < 24) {
//                    imageUri = Uri.fromFile(photoFile);
//                    Log.d("000555", "imageUri IF:" + imageUri);
//                } else {
//                    imageUri = Uri.parse(photoFile.getPath()); // My work-around for new SDKs, causes ActivityNotFoundException in API 10.
//                    Log.d("000555", "imageUri ELSE:" + imageUri);
//                }

                imageUri = Uri.fromFile(image_path);
                Log.d("000555", "IMAGEPATH: " + imageUri);

                if (imageUri != null) {
                    try {
                        URI uri;
                        if (imageUri.toString().startsWith("file://")) {
                            //normal path
                            uri = URI.create(imageUri.toString());
                            Log.d("000555", "uri IF : " + uri);
                        } else {
                            //support path
                            uri = URI.create("file://" + imageUri.toString());
                            Log.d("000555", "uri elseee : " + uri);
                        }
                        file = new File(uri);
                        if (file != null) {
                            if (file.canRead()) {
                                Log.d("000555", "file read : " + file);
                            } else {
                                Log.d("000555", "file not read : ");
                            }
                        }
                    } catch (Exception e) {
                        Log.d("000555", "err: " + e.getMessage());
                    }
                }

                thumbnail = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(file));
                Log.d("000555", "URiiiiiiii: " + Uri.fromFile(file));
                Log.d("000555", "URiiiiiiiiiiiiiiiiiiiii: " + Uri.fromFile(image_path));
                //thumbnail = scaleDown(thumbnail, 640, true);
                photoFile.createNewFile();
                Log.d("000555", "ssss:");
               /* ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.PNG, 60, stream);

                byte[] byteArray = stream.toByteArray();

                Log.d("000555", "File_Path:" + image_path);

                // Log.d("000555", "mCurrentPhotoPath:" + mCurrentPhotoPath);*/
            /*    FileOutputStream fo = new FileOutputStream(image_path);
                fo.write(byteArray);
                fo.flush();
                fo.close();
                thumbnail.recycle();*/

                Log.d("000555", "Created Image:");
                /*iv.setImageBitmap(decodeFile(String.valueOf(image_path)));
                photoViewAttacher = new PhotoViewAttacher(iv);*/

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ctx, "Image not saved", Toast.LENGTH_SHORT).show();
                Log.d("000555", "Img_Err:" + e.getMessage());
//                File file = new File(String.valueOf(image_path));
//                file.delete();
            }

            /*finally {
                finish();
            }*/
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(ctx, R.string.somethingWrong, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Log.d("000555", "Else");
        }


//Working  code

       /* if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            try {

                File photoFile = null;
                photoFile = createImageFile();

                thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                thumbnail= scaleDown(thumbnail, 640, true);

                photoFile.createNewFile();
                Log.d("000555", "ssss:");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                byte[] byteArray = stream.toByteArray();

                Log.d("000555", "File_Path:" + image_path);

                // Log.d("000555", "mCurrentPhotoPath:" + mCurrentPhotoPath);
                FileOutputStream fo = new FileOutputStream(image_path);
                fo.write(byteArray);
                fo.flush();
                fo.close();
                thumbnail.recycle();

                Log.d("000555", "Created Image:");
                iv.setImageBitmap(decodeFile(String.valueOf(image_path)));
                photoViewAttacher = new PhotoViewAttacher(iv);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ctx, "Image not saved", Toast.LENGTH_SHORT).show();
                Log.d("000555", "Img_Err:" + e.getMessage());
            }

            /*finally {
                finish();
            }*/

      /*  } else {
            Log.d("000555", "Else");
            finish();
        }*/


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String uuid = UUID.randomUUID().toString().replace("-", "");
        Log.d("000555", "UUID:" + uuid);

        SimpleDateFormat dates = new SimpleDateFormat("ddMMyyyy_hhmmss");
        Calendar c = Calendar.getInstance();
        timeStamp = dates.format(c.getTime());
        Log.d("000555", "timestamp:" + timeStamp);


        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").toString();
        String imageFileName = "IMG_" + timeStamp + "_" + uuid;
        String folder_Vaccines = Environment.getExternalStorageDirectory() + File.separator + "HayatPK" + File.separator + "Vaccines";
        File storageDir = new File(folder_Vaccines);
        image_path = File.createTempFile(
                imageFileName,  // prefix
                ".png",         // suffix
                storageDir      // directory
        );
        mCurrentPhotoPath = image_path.getPath();
        Log.d("000555", "FilePath: " + image_path.getPath());

        // Save a file: path for use with ACTION_VIEW intents
        //imageUri = Uri.fromFile(image_path);
        Log.d("000555", "ImageURI (create file): " + mCurrentPhotoPath);
        return image_path;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());
        Log.d("000555", "Creatiiiii:");
        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }


    public void insert_db() {
        try {

            Lister ls = new Lister(VAC_Child_HifazitiTeekeyVaccineKoAnjamDy_Activity.this);
           ls.createAndOpenDB();

            // et_refferal_ki_waja = findViewById(R.id.et_refferal_ki_waja);
            // et_refferal_hospital = findViewById(R.id.et_refferal_hospital);

            //Edittext

            SimpleDateFormat dates = new SimpleDateFormat("dd-MM-yyyy_hh:mm:ss aa");
            Calendar c = Calendar.getInstance();
            String current_timeStamp = dates.format(c.getTime());
            Log.d("000555", "timestamp:" + current_timeStamp);


            JSONObject jobj = new JSONObject();
            jobj.put("lat", "" + String.valueOf(latitude));
            jobj.put("lng", "" + String.valueOf(longitude));
            jobj.put("type_name", "" + "vaccine_ko_anjam_dy_vac");
            jobj.put("vaccine_place", "" + vacine_place);
            jobj.put("vaccine_place_pos", "" + vac_place_pos);
            jobj.put("image_path", "" + image_path);
            jobj.put("datetime", "" + current_timeStamp);

            String added_on = String.valueOf(System.currentTimeMillis());

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
            Log.d("000555", "Data: " + ans1);
            Log.d("000555", "Query: " + res);

            //imageocation o be added
            if (Utils.haveNetworkConnection(ctx) > 0) {

                sendPostRequest(child_uid, vacine_uid, TodayDate, String.valueOf(jobj), login_useruid, added_on);
            } else {
                Toast.makeText(ctx, R.string.dataCollected, Toast.LENGTH_SHORT).show();
            }

            //  Toast.makeText(getApplicationContext(),String.valueOf(res)+String.valueOf(ans1),Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.d("000555", " Error: " + e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            Log.d("000555", " BAC: ");
//            alertDialog.dismiss();
            finish();
//            startActivity(new Intent(Child_HifazitiTeekeyVaccineKoAnjamDy_Activity.this, Mother_Dashboard_Activity.class));
        }

    }

    private void sendPostRequest(final String member_uid, final String vacine_uid, final String record_data,
                                 final String data, final String added_by, final String added_on) {

        String url = "https://pak.api.teekoplus.akdndhrc.org/sync/save/child/vaccinations";

        Log.d("000555", "mURL " + url);
        //  Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();

        String REQUEST_TAG = String.valueOf("volleyStringRequest");

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(ctx, response, Toast.LENGTH_SHORT).show();

                try {

                    JSONObject jobj = new JSONObject(response);

                    if (jobj.getBoolean("success")) {
                        Log.d("000555", "Response:    " + response);

                        Toast.makeText(VAC_Child_HifazitiTeekeyVaccineKoAnjamDy_Activity.this, "Data saved", Toast.LENGTH_SHORT).show();

                       /* Lister ls = new Lister(VAC_Child_HifazitiTeekeyVaccineKoAnjamDy_Activity.this);
                        SQLiteDatabase.loadLibs(ctx);
                        ls.createAndOpenDB("test123");

                        String update_record = "UPDATE CVACCINATION SET " +
                                "is_synced='" + String.valueOf(1) + "' " +
                                "WHERE member_uid = '" + child_uid + "'AND added_on= '" + added_on + "'AND vaccine_id= '" + vacine_uid + "'";

                        ls.executeNonQuery(update_record);*/


                    } else {
                        Log.d("000555", "else ");
                        //Toast.makeText(ctx, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(VAC_Child_HifazitiTeekeyVaccineKoAnjamDy_Activity.this, "Data has not been sent to the service.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.d("000555", " Error: " + e.getMessage());
                    Toast.makeText(VAC_Child_HifazitiTeekeyVaccineKoAnjamDy_Activity.this, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("000555", "error    " + error.getMessage());
                Toast.makeText(VAC_Child_HifazitiTeekeyVaccineKoAnjamDy_Activity.this, R.string.checkInternetPrompt, Toast.LENGTH_SHORT).show();
                finish();

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


                Log.d("000555", "mParam " + params);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("000555", "map ");
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
                        Log.d("000555", "Resp " + a);
                        Toast.makeText(VAC_Child_HifazitiTeekeyVaccineKoAnjamDy_Activity.this, "" + a, Toast.LENGTH_SHORT).show();


                    } catch (Exception e) {
                        Log.d("000555", "catch: " + e.getMessage());
                        Toast.makeText(VAC_Child_HifazitiTeekeyVaccineKoAnjamDy_Activity.this, R.string.incorrectDataSent, Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("000555", "Failed: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            Log.d("000555", "catch: " + e.getMessage());
        }

    }


    private void check_gps() {

        //GPS\
        gps = new GPSTracker(ctx);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.d("000555", "latitude value: " + latitude);
            Log.d("000555", "longitude value: " + longitude);
        } else {
            gps.showSettingsAlert();
            Toast.makeText(ctx, R.string.GPSonAlert, Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void check_gps2() {

        //GPS\
        gps = new GPSTracker(ctx);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            if (latitude <= 0 && longitude <= 0) {
               /* Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        check_gps2();
                    }
                }, 1500);*/

                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        check_gps2();
                    }
                };

                runnable.run();

                Log.d("000555", "IF lat lng: ");
                Log.d("000555", "latitude: " + latitude);
                Log.d("000555", "longitude: " + longitude);


                return;
            } else {
                snackbar.dismiss();
                Log.d("000555", "ELSE lat lng: ");
                Log.d("000555", "latitude: " + latitude);
                Log.d("000555", "longitude: " + longitude);

                Toast.makeText(ctx, R.string.GPSonMessage, Toast.LENGTH_SHORT).show();
            }

        } else {
            gps.showSettingsAlert();
            Toast.makeText(ctx, R.string.GPSonAlert, Toast.LENGTH_LONG).show();
            return;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("000555", "BACK ");
        File file = new File(String.valueOf(image_path));
        file.delete();


    }
}
