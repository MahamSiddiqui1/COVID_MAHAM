package com.akdndhrc.covid_module.CustomClass;

import java.io.Serializable;

public class SocialContactMeetingClass implements Serializable {

    public int id;
    public String uid;
    public String type;
    public String metadata;
    public String meeting_topic;
    public String record_data;
    public String meeting_status;
    public String added_by;
    public int is_synced;
    public String added_on;

    public SocialContactMeetingClass(int id, String uid, String type, String metadata, String meeting_topic, String record_data, String meeting_status, String added_by, int is_synced, String added_on) {
        this.id = id;
        this.uid = uid;
        this.type = type;
        this.metadata = metadata;
        this.meeting_topic = meeting_topic;
        this.record_data = record_data;
        this.meeting_status = meeting_status;
        this.added_by = added_by;
        this.is_synced = is_synced;
        this.added_on = added_on;
    }
}
