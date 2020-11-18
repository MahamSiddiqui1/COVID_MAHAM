package com.akdndhrc.covid_module.CustomClass;

import java.io.Serializable;

public class MVaccinationClass implements Serializable {

    public int id;
    public String member_uid;
    public String record_data;
    public String data;
    public String vaccine_id;
    public String type;
    public String vaccinated_on;
    public String image_location;
    public String added_by;
    public String is_synced;
    public String added_on;


    public MVaccinationClass(int id, String member_uid, String record_data, String data, String vaccine_id, String type, String vaccinated_on, String image_location, String added_by, String is_synced, String added_on) {
        this.id = id;
        this.member_uid = member_uid;
        this.record_data = record_data;
        this.data = data;
        this.vaccine_id = vaccine_id;
        this.type = type;
        this.vaccinated_on = vaccinated_on;
        this.image_location = image_location;
        this.added_by = added_by;
        this.is_synced = is_synced;
        this.added_on = added_on;
    }
}
