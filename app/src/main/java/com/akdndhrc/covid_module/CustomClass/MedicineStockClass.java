package com.akdndhrc.covid_module.CustomClass;

import java.io.Serializable;

public class MedicineStockClass implements Serializable {

    public int id;
    public String medicine_id;
    public String medicine_name;
    public String balance;
    public String utilized;
    public String received;
    public String wastage;
    public String record_data;
    public String added_by;
    public String is_synced;
    public String added_on;


    public MedicineStockClass(int id, String medicine_id, String medicine_name, String balance, String utilized, String received, String wastage, String record_data, String added_by, String is_synced, String added_on) {
        this.id = id;
        this.medicine_id = medicine_id;
        this.medicine_name = medicine_name;
        this.balance = balance;
        this.utilized = utilized;
        this.received = received;
        this.wastage = wastage;
        this.record_data = record_data;
        this.added_by = added_by;
        this.is_synced = is_synced;
        this.added_on = added_on;
    }
}
