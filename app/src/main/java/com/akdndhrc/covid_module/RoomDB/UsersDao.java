package com.akdndhrc.covid_module.RoomDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UsersDao {

    @Query("SELECT * FROM Users")
    List<Users> getAll();

    @Insert
    void insert(Users users);

    @Delete
    void delete(Users users);

    @Update
    void update(Users users);

}
