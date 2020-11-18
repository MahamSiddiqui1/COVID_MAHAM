package com.akdndhrc.covid_module.CustomClass;

import java.io.Serializable;

public class CVaccinationClass implements Serializable {

    public int id;
    public String member_uid;
    public String vaccine_id;
    public String record_data;
    public String type;
    public String due_date;
    public String vaccinated_on;
    public String image_location;
    public String metadata;
    public String added_by;
    public String is_synced;
    public String added_on;

    public CVaccinationClass(int id, String member_uid, String vaccine_id, String record_data, String type, String due_date, String vaccinated_on, String image_location, String metadata, String added_by, String is_synced, String added_on) {
        this.id = id;
        this.member_uid = member_uid;
        this.vaccine_id = vaccine_id;
        this.record_data = record_data;
        this.type = type;
        this.due_date = due_date;
        this.vaccinated_on = vaccinated_on;
        this.image_location = image_location;
        this.metadata = metadata;
        this.added_by = added_by;
        this.is_synced = is_synced;
        this.added_on = added_on;
    }
}
