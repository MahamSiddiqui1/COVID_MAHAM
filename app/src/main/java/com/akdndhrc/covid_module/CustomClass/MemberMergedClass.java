package com.akdndhrc.covid_module.CustomClass;

import java.io.Serializable;

public class MemberMergedClass implements Serializable {

    public String member_uid_1;
    public String member_uid_2;
    public String metadata;
    public String record_data;
    public String added_by;
    public String is_synced;
    public String added_on;


    public MemberMergedClass(String member_uid_1, String member_uid_2, String metadata, String record_data, String added_by, String is_synced, String added_on) {
        this.member_uid_1 = member_uid_1;
        this.member_uid_2 = member_uid_2;
        this.metadata = metadata;
        this.record_data = record_data;
        this.added_by = added_by;
        this.is_synced = is_synced;
        this.added_on = added_on;
    }
}
