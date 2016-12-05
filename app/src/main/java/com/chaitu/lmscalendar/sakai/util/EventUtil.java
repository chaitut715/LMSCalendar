package com.chaitu.lmscalendar.sakai.util;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.chaitu.lmscalendar.sakai.calendar.model.AndroidEvent;
import com.chaitu.lmscalendar.sakai.calendar.model.CalendarResponse;
import com.chaitu.lmscalendar.sakai.calendar.model.Calendar_collection;
import com.chaitu.lmscalendar.sakai.calendar.model.RecurrenceRule;
import com.chaitu.lmscalendar.sakai.calendar.model.Until;
import com.chaitu.lmscalendar.ui.CalendarModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;



/**
 * Created by CHAITU on 11/15/2016.
 */

public class EventUtil {

    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    public static final String TAG = "EventUtil";
    public static final String RR = "RRULE:";
    public static final String FREQ = "FREQ=";
    public static final String COUNT = "COUNT=";
    public static final String INTERVAL = "INTERVAL=";
    public static final String UNTIL = "UNTIL=";
    public static final String BYDAY = "BYDAY=";
    static ObjectMapper mapper = new ObjectMapper();
    private static Map<String, String> frequencies;
    private static Map<String, String> weekDays;

    static {

        Map<String, String> frequencyMap = new HashMap<>();
        frequencyMap.put("day", "DAILY");
        frequencyMap.put("week", "WEEKLY");
        frequencyMap.put("SMW", "WEEKLY");
        frequencyMap.put("SMTW", "WEEKLY");
        frequencyMap.put("STT", "WEEKLY");
        frequencyMap.put("MW", "WEEKLY");
        frequencyMap.put("MWF", "WEEKLY");
        frequencyMap.put("TTh", "WEEKLY");
        frequencyMap.put("month", "MONTHLY");
        frequencyMap.put("year", "YEARLY");

        frequencies = Collections.unmodifiableMap(frequencyMap);

        Map<String, String> daysMap = new HashMap<String, String>();
        daysMap.put("SMW", "SU,MO,WE");
        daysMap.put("SMTW", "SU,MO,TU,WE");
        daysMap.put("STT", "SU,TU,TH");
        daysMap.put("MW", "MO,WE");
        daysMap.put("MWF", "MO,WE,FR");
        daysMap.put("TTh", "TU,TH");

        weekDays = Collections.unmodifiableMap(daysMap);
    }

    public static AndroidEvent getAndroidEvent(Calendar_collection calendar_collection, int calendar_id) {
        AndroidEvent androidEvent = new AndroidEvent();
        androidEvent.setUid(calendar_collection.getEventId());
        androidEvent.setCalendar_id(calendar_id);
        androidEvent.setTitle(calendar_collection.getTitle() + "(" + calendar_collection.getSiteName()+")");
        androidEvent.setDtsatrt(calendar_collection.getFirstTime().getTime());
        androidEvent.setDuration(calendar_collection.getDuration());
        androidEvent.setDescription(getDescription(calendar_collection.getType(), calendar_collection.getSiteName()));
        if (calendar_collection.getRecurrenceRule() != null) {
            androidEvent.setRrule(getRecurrenceRule(calendar_collection.getRecurrenceRule()));
        }

        /*try {
            androidEvent.setEventTimezone(getTimezone(calendar_collection.getFirstTime().getDisplay(), calendar_collection.getFirstTime().getTime()));
        } catch (ParseException e) {
            androidEvent.setEventTimezone(TimeZone.getDefault().getID());
        }*/
        return androidEvent;
    }

    public static String getRecurrenceRule(RecurrenceRule recurrenceRule) {
        StringBuffer androidRR = new StringBuffer();
        //androidRR.append(RR);
        androidRR.append(FREQ + getFrequency(recurrenceRule.getFrequency()) + ";");
        if (getByDay(recurrenceRule.getFrequency()) != null) {
            androidRR.append(BYDAY + getByDay(recurrenceRule.getFrequency()) + ";");
        }

        if (recurrenceRule.getInterval() > 1) {
            androidRR.append(INTERVAL + recurrenceRule.getInterval() + ";");
        }
        Until until = recurrenceRule.getUntil();
        if (until != null) {
            Date date = new Date(until.getTime());
            String pattern = "yyyyMMdd'T'HHmmss'Z'";
            DateFormat dt = new SimpleDateFormat(pattern);
            androidRR.append(UNTIL + dt.format(date) + ";");
        } else {
            if (recurrenceRule.getCount() > 0) {
                androidRR.append(COUNT + recurrenceRule.getCount() + ";");
            }
        }
        return new String(androidRR.deleteCharAt(androidRR.length() - 1));
    }

    public static String getDescription(String type, String siteName) {
        return "Type:" + type + "\nCourse Name:" + siteName;
    }

    public static CalendarResponse formatJSON(String responseVal) throws IOException {
        CalendarResponse calendarResponse;
        calendarResponse = mapper.readValue(responseVal, CalendarResponse.class);
        return calendarResponse;
    }

    public static String getFrequency(String sakaiVal) {
        return frequencies.get(sakaiVal);
    }

    public static String getByDay(String sakaiVal) {
        return weekDays.get(sakaiVal);
    }
   /* public static long getNewEventId(Context context, ContentResolver cr, Uri cal_uri){
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_CALENDAR)
                == PackageManager.PERMISSION_GRANTED) {
            Cursor cursor = cr.query(CalendarContract.Events.CONTENT_URI, new String [] {"MAX(_id) as max_id"}, null, null, "_id");
            cursor.moveToFirst();
            long max_val = cursor.getLong(cursor.getColumnIndex("max_id"));
            return max_val+1;
        }

    }*/

    public static long addEvent(Context context, ContentResolver cr, AndroidEvent androidEvent) {
        long eventID = -1;
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_CALENDAR)
                == PackageManager.PERMISSION_GRANTED) {
            ContentValues values = new ContentValues();
            Date startDate = new Date(androidEvent.getDtsatrt());
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(startDate);
            values.put(CalendarContract.Events.DTSTART, cal1.getTimeInMillis());


            values.put(CalendarContract.Events.TITLE, androidEvent.getTitle());
            values.put(CalendarContract.Events.DESCRIPTION, androidEvent.getDescription());
            values.put(CalendarContract.Events.CALENDAR_ID, androidEvent.getCalendar_id());
            values.put(CalendarContract.Events.EVENT_TIMEZONE, androidEvent.getEventTimezone());
            if (androidEvent.getRrule() != null) {
                values.put(CalendarContract.Events.RRULE, androidEvent.getRrule());
            }

            if (androidEvent.getId() <= 0) {
                Date endDate = new Date(androidEvent.getDtsatrt() + androidEvent.getDuration());
                cal1 = Calendar.getInstance();
                cal1.setTime(endDate);
                values.put(CalendarContract.Events.DTEND, endDate.getTime());
                values.put(CalendarContract.Events.UID_2445, androidEvent.getUid());
                Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
                Log.i(TAG, "uri:" + uri);
                // get the event ID that is the last element in the Uri
                eventID = Long.parseLong(uri.getLastPathSegment());
                return eventID;
            } else {
                values.put(CalendarContract.Events.TITLE, androidEvent.getTitle());
                Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, androidEvent.getId());

                int rows = cr.update(uri, values, null, null);
                eventID = androidEvent.getId();
            }

        }
        return eventID;
    }


    public static String getTimezone(String display, long time) throws ParseException, ParseException {

        DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy h:mm a");
        System.out.println(dateFormat.parse(display));
        Date displayDate = dateFormat.parse(display);
        Date longDate = new Date(time);
        System.out.println("offset:" + (displayDate.getTime() - time));
        String[] timezones = TimeZone.getAvailableIDs((int) (displayDate.getTime() - time));
        if (timezones != null) {
            for (String timezone : timezones) {
                /*TimeZone timeZone = TimeZone.getTimeZone(timezone);
                System.out.println(timeZone);*/
                return timezone;
            }
        }
        return TimeZone.getDefault().getID();
    }

    public static Map<String, Long> getEventsOfCalendar(Context context, ContentResolver cr, long calendarId) {
        Uri.Builder builder = Uri.parse("content://com.android.calendar/instances/when").buildUpon();
        ContentUris.appendId(builder, Long.MIN_VALUE);
        ContentUris.appendId(builder, Long.MAX_VALUE);
        String selection = "(calendar_id = ?)";
        String[] selectionArgs = new String[]{"" + calendarId};

        Cursor eventCursor = cr.query(builder.build(),
                new String[]{CalendarContract.Events.UID_2445, "event_id"},
                selection, selectionArgs, "startDay ASC");
        Map<String, Long> existingEvents = new HashMap<String, Long>();
        while (eventCursor.moveToNext()) {

            //Log.i(TAG, "uid:" + eventCursor.getString(0) + "\t id:" + eventCursor.getLong(1) );
            if (eventCursor.getLong(1) > 0) {
                String uuid = eventCursor.getString(0);
                if(uuid!=null&& !uuid.isEmpty()){
                    if(existingEvents.containsKey(uuid)){
                        deleteEvent(context,cr, eventCursor.getLong(1));
                    }else{
                        existingEvents.put(eventCursor.getString(0), eventCursor.getLong(1));
                    }

                }

                //deleteEvent(context,cr,eventCursor.getLong(1));
            }
            //deleteEvent(context, cr,eventCursor.getLong(1));
            //Log.i(TAG,"id:"+eventCursor.getLong(1));
            //Log.i(TAG,"TITLE:"+eventCursor.getLong(2));
            //uids.add(eventCursor.getString(0));
        }
        return existingEvents;
    }

    public static void deleteEvent(Context context, ContentResolver cr, long eventId) {
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);
        int rows = cr.delete(deleteUri, null, null);
        Log.i(TAG, "Rows deleted: " + rows + "\t eventId:" + eventId);
    }

    public static List<CalendarModel> readCalendars(Context context, ContentResolver cr){
        List<CalendarModel> cals = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            //requestCalendarPermission();
            Log.i(TAG, "No permission");
        }else{
            //read calendars
            Cursor cur = null;
            Uri uri = CalendarContract.Calendars.CONTENT_URI;
            String selection = "(("
                    + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) )";
            //Log.i(TAG, "selection:"+selection);
            String[] selectionArgs = new String[] {"com.google"};
            // Submit the query and get a Cursor object back.
            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
            while (cur.moveToNext()) {
                int calID = 0;
                String displayName = null;
                String accountName = null;
                String ownerName = null;



                // Get the field values
                calID = cur.getInt(PROJECTION_ID_INDEX);
                displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
                accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

                CalendarModel calendar = new CalendarModel(calID, displayName);
                cals.add(calendar);

                // Do something with the values...
                //Log.i(TAG, "calID:"+calID+"\t displayName"+displayName+"\t accountName"+accountName+"\t ownerName"+ownerName);
                        /*ContentValues values = new ContentValues();
                        // The new display name for the calendar
                        values.put(CalendarContract..CalendarsCALENDAR_DISPLAY_NAME, "chaitanya kumar");
                        Uri updateUri = ContentUris.withAppendedId(CalendarContract.Calendars.CONTENT_URI, calID);
                        int rows = getContentResolver().update(updateUri, values, null, null);
                        Log.i(TAG, "Rows updated: " + rows);*/


            }

        }
        //show select option
        return cals;
    }
}
