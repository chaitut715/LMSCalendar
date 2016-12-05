package com.chaitu.lmscalendar.sakai.sync;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.chaitu.lmscalendar.sakai.calendar.model.AndroidEvent;
import com.chaitu.lmscalendar.sakai.calendar.model.CalendarResponse;
import com.chaitu.lmscalendar.sakai.calendar.model.Calendar_collection;
import com.chaitu.lmscalendar.sakai.util.EventUtil;
import com.chaitu.lmscalendar.sakai.util.SakaiUtil;
import com.chaitu.lmscalendar.settings.Settings;

import java.util.Collection;
import java.util.Map;

/**
 * Created by CHAITU on 11/16/2016.
 */

public class EventsSyncService  extends IntentService{
    public EventsSyncService(){
        super("SyncEventsService");
    }
    public EventsSyncService(String name){
        super(name);
    }
    public static final String TAG="EventSyncService";

    private Settings mSettings;


    @Override
    protected void onHandleIntent(Intent intent) {
        //Here you will receive the result fired from async class
        //of syncCalendarEvents(result) method.
        try{

            Log.i(TAG,"onHandleIntent");
            mSettings = new Settings(PreferenceManager.getDefaultSharedPreferences(this));
            int calID=  mSettings.getInt(Settings.PREF_LASTCALENDARID);
            String url= mSettings.getString(Settings.PREF_LASTURL);
            String username= mSettings.getString(Settings.PREF_LASTURLUSERNAME);
            String password= mSettings.getString(Settings.PREF_LASTURLPASSWORD);
            String timezone= mSettings.getString(Settings.PREF_LASTSAKTZ);
            Log.i(TAG,timezone);
            String output = SakaiUtil.getEventsResponse(url, username, password);
            //Log.i(TAG, "output:"+output);
            CalendarResponse calendarResponse= EventUtil.formatJSON(output);
            //Log.i(TAG, "calendarResponse:"+calendarResponse);
            Map<String, Long> existingEvents = EventUtil.getEventsOfCalendar(getApplicationContext(), getContentResolver(), calID);
                    Calendar_collection[] calendar_collections = calendarResponse.getCalendar_collection();
            for(Calendar_collection calendar_collection: calendar_collections){
                AndroidEvent androidEvent= EventUtil.getAndroidEvent(calendar_collection, calID);
                androidEvent.setEventTimezone(timezone);
                ContentResolver cr = getContentResolver();
                if(existingEvents.containsKey(androidEvent.getUid())){
                    androidEvent.setId(existingEvents.get(androidEvent.getUid()));
                }
                Log.i(TAG,"androidEvent:"+androidEvent);
                long eventID = EventUtil.addEvent(getApplicationContext(), cr,  androidEvent);
                existingEvents.remove(androidEvent.getUid());
                //Log.i(TAG, "eventID:"+eventID);
            }
            Collection<Long>  deleteEvents = existingEvents.values();
            for(Long eventID: deleteEvents){
                EventUtil.deleteEvent(getApplicationContext(), getContentResolver(), eventID);
            }

            stopSelf();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
