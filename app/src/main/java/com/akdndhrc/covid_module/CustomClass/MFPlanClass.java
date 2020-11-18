package com.akdndhrc.covid_module.CustomClass;

import java.io.Serializable;

public class MFPlanClass implements Serializable {

    public int id;
    public String member_uid;
    public String record_data;
    public String data;
    public String added_by;
    public String is_synced;
    public String added_on;


    public MFPlanClass(int id, String member_uid, String record_data, String data, String added_by, String is_synced, String added_on) {
        this.id = id;
        this.member_uid = member_uid;
        this.record_data = record_data;
        this.data = data;
        this.added_by = added_by;
        this.is_synced = is_synced;
        this.added_on = added_on;
    }
}
