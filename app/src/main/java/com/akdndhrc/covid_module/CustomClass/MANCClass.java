package com.akdndhrc.covid_module.CustomClass;

import java.io.Serializable;

public class MANCClass implements Serializable {

    public int id;
    public String member_uid;
    public String pregnancy_id;
    public String record_data;
    public String type;
    public String data;
    public String added_by;
    public String is_synced;
    public String added_on;


    public MANCClass(int id, String member_uid, String pregnancy_id, String record_data, String type, String data, String added_by, String is_synced, String added_on) {
        this.id = id;
        this.member_uid = member_uid;
        this.pregnancy_id = pregnancy_id;
        this.record_data = record_data;
        this.type = type;
        this.data = data;
        this.added_by = added_by;
        this.is_synced = is_synced;
        this.added_on = added_on;
    }
}
