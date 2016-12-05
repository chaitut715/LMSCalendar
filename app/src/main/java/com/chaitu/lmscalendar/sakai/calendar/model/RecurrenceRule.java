package com.chaitu.lmscalendar.sakai.calendar.model;

/**
 * Created by CHAITU on 11/15/2016.
 */

public class RecurrenceRule
{
    private int interval;

    private int count;

    private String frequencyDescription;

    private String frequency;

    private Until until;

    public int getInterval ()
    {
        return interval;
    }

    public void setInterval (int interval)
    {
        this.interval = interval;
    }

    public int getCount ()
    {
        return count;
    }

    public void setCount (int count)
    {
        this.count = count;
    }

    public String getFrequencyDescription ()
    {
        return frequencyDescription;
    }

    public void setFrequencyDescription (String frequencyDescription)
    {
        this.frequencyDescription = frequencyDescription;
    }

    public String getFrequency ()
    {
        return frequency;
    }

    public void setFrequency (String frequency)
    {
        this.frequency = frequency;
    }

    public Until getUntil ()
    {
        return until;
    }

    public void setUntil (Until until)
    {
        this.until = until;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [interval = "+interval+", count = "+count+", frequencyDescription = "+frequencyDescription+", frequency = "+frequency+", until = "+until+"]";
    }
}

