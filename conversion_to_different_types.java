
====================================================================================
Map -> Array, getting first element of a map
====================================================================================

String uri = (String) App.ringtoneList.values().toArray()[0];
                String title = App.ringtoneList.get(App.ringtoneList.keySet().toArray()[0]);

                Log.i("title",title);
                Log.i("uri",uri);

====================================================================================
String -> URi
====================================================================================                

                Uri ringtoneUri = Uri.parse(uri);
                ringtone = RingtoneManager.getRingtone(RingtoneActivity.this, ringtoneUri);
                ringtone.play();