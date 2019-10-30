
/*
======================================================================================================
Playing Rigntones............
======================================================================================================
*/ 
Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
this.ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
this.ringtone.play();




/*
======================================================================================================
Getting List of All rigntones
======================================================================================================
*/ 
         public Uri[] getAllRigntomes() {
        RingtoneManager ringtoneMgr = new RingtoneManager(this);
        ringtoneMgr.setType(RingtoneManager.TYPE_ALARM);
        Cursor alarmsCursor = ringtoneMgr.getCursor();
        int alarmsCount = alarmsCursor.getCount();
        if (alarmsCount == 0 && !alarmsCursor.moveToFirst()) {
            return null;
        }
        Uri[] alarms = new Uri[alarmsCount];
        while(!alarmsCursor.isAfterLast() && alarmsCursor.moveToNext()) {
            int currentPosition = alarmsCursor.getPosition();
            alarms[currentPosition] = ringtoneMgr.getRingtoneUri(currentPosition);
        }
        alarmsCursor.close();
        return alarms;
    }
