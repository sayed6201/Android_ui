Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        this.ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
        this.ringtone.play();