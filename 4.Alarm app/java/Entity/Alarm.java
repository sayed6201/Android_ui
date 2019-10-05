package com.sayed.alarm_app.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarm")
public class Alarm {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "minute")
    private String minute;

    @ColumnInfo(name = "hour")
    private String hour;

    @ColumnInfo(name = "am_pm")
    private String shift;

    public Alarm(String minute, String hour, String shift) {
        this.minute = minute;
        this.hour = hour;
        this.shift = shift;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinutet(String minute) {
        this.minute = minute;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }


}