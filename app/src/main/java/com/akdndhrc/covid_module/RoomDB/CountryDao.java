package com.akdndhrc.covid_module.RoomDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CountryDao {

    @Query("SELECT * FROM Country")
    List<Country> getAll();

    @Insert
    void insert(Country country);

    @Delete
    void delete(Country country);

    @Update
    void update(Country country);

}
