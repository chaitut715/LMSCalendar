package com.chaitu.lmscalendar.sakai.calendar.model;

/**
 * Created by CHAITU on 11/15/2016.
 */

public class FirstTime
{
    private long time;

    private String display;

    public long getTime ()
    {
        return time;
    }

    public void setTime (long time)
    {
        this.time = time;
    }

    public String getDisplay ()
    {
        return display;
    }

    public void setDisplay (String display)
    {
        this.display = display;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [time = "+time+", display = "+display+"]";
    }
}

