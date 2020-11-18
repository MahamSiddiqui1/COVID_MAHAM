package com.akdndhrc.covid_module.CustomClass;

import java.io.Serializable;

public class SchoolMeetingClass implements Serializable {

    public String male_count;
    public String female_count;


    public SchoolMeetingClass(String male_count, String female_count) {
        this.male_count = male_count;
        this.female_count = female_count;
    }

    public String getMale_count() {
        return male_count;
    }

    public void setMale_count(String male_count) {
        this.male_count = male_count;
    }

    public String getFemale_count() {
        return female_count;
    }

    public void setFemale_count(String female_count) {
        this.female_count = female_count;
    }
}

