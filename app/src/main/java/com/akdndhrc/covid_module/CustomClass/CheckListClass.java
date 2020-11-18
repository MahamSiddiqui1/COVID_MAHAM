package com.akdndhrc.covid_module.CustomClass;

import java.io.Serializable;

public class CheckListClass implements Serializable {

    public String member_uid;
    public String member_status;


    public CheckListClass(String member_uid, String member_status) {
        this.member_uid = member_uid;
        this.member_status = member_status;
    }

    public String getMember_uid() {
        return member_uid;
    }

    public void setMember_uid(String member_uid) {
        this.member_uid = member_uid;
    }

    public String getMember_status() {
        return member_status;
    }

    public void setMember_status(String member_status) {
        this.member_status = member_status;
    }
}

