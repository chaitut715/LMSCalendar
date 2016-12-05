package com.chaitu.lmscalendar.sakai.calendar.model;
public class CalendarResponse
{
    private Calendar_collection[] calendar_collection;

    private String entityPrefix;

    public Calendar_collection[] getCalendar_collection ()
    {
        return calendar_collection;
    }

    public void setCalendar_collection (Calendar_collection[] calendar_collection)
    {
        this.calendar_collection = calendar_collection;
    }

    public String getEntityPrefix ()
    {
        return entityPrefix;
    }

    public void setEntityPrefix (String entityPrefix)
    {
        this.entityPrefix = entityPrefix;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [calendar_collection = "+calendar_collection+", entityPrefix = "+entityPrefix+"]";
    }
}

