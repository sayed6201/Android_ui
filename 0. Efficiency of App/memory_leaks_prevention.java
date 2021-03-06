====================================================================================
Tips to prevent memory leaks

link1: https://android.jlelse.eu/9-ways-to-avoid-memory-leaks-in-android-b6d81648e35e
link2: 
====================================================================================

1. Use applicationContext() instead of activity context when possible. If you really have to use activity context, then when the activity is destroyed, ensure that the context you passed to the class is set to null.
2. Never use static variables to declare views or activity context.
3. Never reference a class inside the activity. If we need to, we should declare it as static, whether it is a thread or a handler or a timer or an asyncTask.
4. Always make sure to unregister broadcastReceivers or timers inside the activity. Cancel any asyncTasks or Threads inside onDestroy().
5. Always use a weakReference of the activity or view when needed.