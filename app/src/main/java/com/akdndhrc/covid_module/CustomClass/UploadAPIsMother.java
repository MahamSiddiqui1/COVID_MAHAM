package com.akdndhrc.covid_module.CustomClass;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UploadAPIsMother {
    @Multipart
    @POST("/sync/save/mother/vaccinations/new/image")
    Call<ResponseBody> uploadImage(
            @Part("file") RequestBody file,
            @Part("member_id") RequestBody member_id,
            @Part("vaccine_id") RequestBody vaccine_id,
            @Part("added_by") RequestBody added_by,
            @Part("added_on") RequestBody added_on
           /* @Query("member_id") String member_id,
            @Query("vaccine_id") String vaccine_id,
            @Query("added_by") String added_by,
            @Query("added_on") String added_on*/


    );

    @POST("/sync/save/child/vaccinations/image")
    Call<ResponseBody> upload(
            @Body MultipartBody parts
    );

}