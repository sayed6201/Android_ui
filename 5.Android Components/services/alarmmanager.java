
====================================================================================
Repeat alarm manager task ,[ outdated ] use jobschedular
====================================================================================
Intent intent = new Intent(this, MyReceiver.class);
intent.putExtra("key", "Alert");
pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, 0);

alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
Calendar calendar=Calendar.getInstance();

// Calendar.set(int year, int month, int day, int hourOfDay, int minute, int second)
calendar.set(2013, Calendar.OCTOBER, 13, 18, 55, 40);

//repeats task after every 5*1000 seconds++++++++++++++++++++++
alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5*1000, pendingIntent);

//only once the task is executed++++++++++++++++++
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
//task is executed INTERVAL_DAY ++++++++++++++++++
//alarmManager.setRepeating(AlarmManager.RTC, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY ,pendingIntent);