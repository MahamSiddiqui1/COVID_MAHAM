package com.akdndhrc.covid_module.RoomDB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {
        Country.class, District.class, Facilities.class, Logins.class, Medicines.class, Province.class, Tehsil.class, UnionCouncil.class,
        Users.class, Vaccines.class, Villages.class
    },
        version = 1)

public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
