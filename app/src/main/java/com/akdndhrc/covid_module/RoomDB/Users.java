package com.akdndhrc.covid_module.RoomDB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Users implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "uid")
    private String uid;

    @ColumnInfo(name = "privilege")
    private String privilege;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "salt")
    private String salt;

    @ColumnInfo(name = "district_id")
    private String district_id;

    @ColumnInfo(name = "country_id")
    private String country_id;

    @ColumnInfo(name = "province_id")
    private String province_id;

    @ColumnInfo(name = "uc_id")
    private String uc_id;

    @ColumnInfo(name = "metadata")
    private String metadata;

    /*
     * Getters and Setters
     * */

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getUc_id() {
        return uc_id;
    }

    public void setUc_id(String uc_id) {
        this.uc_id = uc_id;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
