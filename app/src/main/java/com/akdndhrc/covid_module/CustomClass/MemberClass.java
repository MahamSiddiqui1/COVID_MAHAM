package com.akdndhrc.covid_module.CustomClass;

import java.io.Serializable;

public class MemberClass implements Serializable {

    public int id;
    public String manual_id;
    public String uid;
    public String khandan_id;
    public String full_name;
    public String nicnumber;
    public String phone_number;
    public String data;
    public String gender;
    public String age;
    public String dob;
    public String bio_code;
    public String qr_code;
    public String added_by;
    public String is_synced;
    public String added_on;



    public MemberClass(int id, String manual_id, String uid, String khandan_id, String full_name, String nicnumber, String phone_number, String data, String gender, String age, String dob, String bio_code, String qr_code,
                       String added_by, String is_synced, String added_on) {
        this.id = id;
        this.manual_id = manual_id;
        this.uid = uid;
        this.khandan_id = khandan_id;
        this.full_name = full_name;
        this.nicnumber = nicnumber;
        this.phone_number = phone_number;
        this.data = data;
        this.gender = gender;
        this.age = age;
        this.dob = dob;
        this.bio_code = bio_code;
        this.qr_code = qr_code;
        this.added_by = added_by;
        this.is_synced = is_synced;
        this.added_on = added_on;
    }
}
