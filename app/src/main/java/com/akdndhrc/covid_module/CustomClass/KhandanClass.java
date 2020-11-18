package com.akdndhrc.covid_module.CustomClass;

import java.io.Serializable;

public class KhandanClass implements Serializable {

    public int id;
    public String manual_id;
    public String uid;
    public String province_id;
    public String district_id;
    public String subdistrict_id;
    public String uc_id;
    public String village_id;
    public String family_head_name;
    public String family_address;
    public String water_source;
    public String toilet_facility;
    public String added_by;
    public String is_synced;
    public String added_on;

    public KhandanClass(int id, String manual_id, String uid, String province_id, String district_id, String subdistrict_id, String uc_id, String village_id, String family_head_name, String family_address, String water_source,
                        String toilet_facility, String added_by, String is_synced, String added_on) {
        this.id = id;
        this.manual_id = manual_id;
        this.uid = uid;
        this.province_id = province_id;
        this.district_id = district_id;
        this.subdistrict_id = subdistrict_id;
        this.uc_id = uc_id;
        this.village_id = village_id;
        this.family_head_name = family_head_name;
        this.family_address = family_address;
        this.water_source = water_source;
        this.toilet_facility = toilet_facility;
        this.added_by = added_by;
        this.is_synced = is_synced;
        this.added_on = added_on;
    }
}

