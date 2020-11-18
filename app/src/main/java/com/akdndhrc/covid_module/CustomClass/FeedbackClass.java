package com.akdndhrc.covid_module.CustomClass;

import java.io.Serializable;

public class FeedbackClass implements Serializable {

    public String rating;
    public String record_data;
    public String data;
    public String added_by;
    public String is_synced;
    public String added_on;

    public FeedbackClass(String rating, String record_data, String data, String added_by, String is_synced, String added_on) {
        this.rating = rating;
        this.record_data = record_data;
        this.data = data;
        this.added_by = added_by;
        this.is_synced = is_synced;
        this.added_on = added_on;
    }

}
