package com.sayed.alarm_app;
import com.sayed.alarm_app.Entity.Alarm;

import java.util.List;

public class DbOperations {

    public static long addAlarm(final AppDatabase db, Alarm alarm) {
        return db.alarmDao().insertAll(alarm);
    }

    public static List<Alarm> getAllAlarms(final AppDatabase db) {
        return db.alarmDao().getAll();
    }

    public static void deleteAlarmById(final AppDatabase db, int id) {
         db.alarmDao().deleteById(id);
    }

    public static void updateById(final AppDatabase db, String time, int id) {
        db.alarmDao().update(time,"","",id);
    }

    public static void populateWithTestData(AppDatabase db) {
        Alarm alarm = new Alarm("32","3","pm");
        addAlarm(db, alarm);
    }

}
