package com.akdndhrc.covid_module.CustomClass;

import java.io.Serializable;

public class VaccineStockClass implements Serializable {

    public int id;
    public String vaccine_id;
    public String vaccine_name;
    public String balance;
    public String utilized;
    public String received;
    public String wastage;
    public String return_vaccine;
    public String record_data;
    public String added_by;
    public String is_synced;
    public String added_on;

    public VaccineStockClass(int id, String vaccine_id, String vaccine_name, String balance, String utilized, String received, String wastage, String return_vaccine, String record_data, String added_by, String is_synced, String added_on) {
        this.id = id;
        this.vaccine_id = vaccine_id;
        this.vaccine_name = vaccine_name;
        this.balance = balance;
        this.utilized = utilized;
        this.received = received;
        this.wastage = wastage;
        this.return_vaccine = return_vaccine;
        this.record_data = record_data;
        this.added_by = added_by;
        this.is_synced = is_synced;
        this.added_on = added_on;
    }
}
