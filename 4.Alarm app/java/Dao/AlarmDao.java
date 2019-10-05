package com.sayed.alarm_app.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.sayed.alarm_app.Entity.Alarm;

import java.util.List;

@Dao
public interface AlarmDao {

    @Query("SELECT * FROM alarm")
    List<Alarm> getAll();

    @Query("SELECT * FROM alarm where id = :id")
    Alarm findById(int id);

    @Query("SELECT COUNT(*) from alarm")
    int countAlarms();

    @Insert
    long insertAll(Alarm alarm);

    @Delete
    void delete(Alarm alarm);

    @Query("DELETE FROM alarm WHERE id = :id")
    void deleteById(int id);

    @Query("UPDATE alarm SET minute = :minute, hour = :hour, am_pm = :am_pm WHERE id =:id")
    void update(String minute, String hour, String am_pm,  int id);
}
