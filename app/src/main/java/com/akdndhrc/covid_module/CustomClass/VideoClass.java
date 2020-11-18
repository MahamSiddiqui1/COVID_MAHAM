package com.akdndhrc.covid_module.CustomClass;

import java.io.Serializable;

public class VideoClass implements Serializable {

    public int id;
    public String video_name;
    public String record_data;
    public String data;
    public String added_by;
    public String is_synced;
    public String added_on;


    public VideoClass(int id, String video_name, String record_data, String data, String added_by, String is_synced, String added_on) {
        this.id = id;
        this.video_name = video_name;
        this.record_data = record_data;
        this.data = data;
        this.added_by = added_by;
        this.is_synced = is_synced;
        this.added_on = added_on;
    }
}
