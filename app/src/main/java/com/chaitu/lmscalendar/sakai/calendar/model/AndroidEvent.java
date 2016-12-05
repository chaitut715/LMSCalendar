package com.chaitu.lmscalendar.sakai.calendar.model;

/**
 * Created by CHAITU on 11/15/2016.
 */

public class AndroidEvent {
    long id;
    boolean allDay;
    int availability;
    int calendar_id;
    boolean canInviteOthers;
    String description;
    String displayColor;
    long dtend;
    long dtsatrt;
    long duration;
    String eventColor;
    String eventColor_index;
    String eventEndTimezone;
    String eventLocation;
    String eventTimezone;
    String exdate;
    String exrule;
    boolean hasAlarm;
    boolean isOrganizer;
    String lastDate;
    String organizer;
    String originalAllDay;
    String rdate;
    String rrule;
    String selfAttendeeStatus;
    int eventStatus;
    String title;
    String uid;

    @Override
    public String toString() {
        return "AndroidEvent{" +
                "id=" + id +
                ", allDay=" + allDay +
                ", availability=" + availability +
                ", calendar_id=" + calendar_id +
                ", canInviteOthers=" + canInviteOthers +
                ", description='" + description + '\'' +
                ", displayColor='" + displayColor + '\'' +
                ", dtend=" + dtend +
                ", dtsatrt=" + dtsatrt +
                ", duration=" + duration +
                ", eventColor='" + eventColor + '\'' +
                ", eventColor_index='" + eventColor_index + '\'' +
                ", eventEndTimezone='" + eventEndTimezone + '\'' +
                ", eventLocation='" + eventLocation + '\'' +
                ", eventTimezone='" + eventTimezone + '\'' +
                ", exdate='" + exdate + '\'' +
                ", exrule='" + exrule + '\'' +
                ", hasAlarm=" + hasAlarm +
                ", isOrganizer=" + isOrganizer +
                ", lastDate='" + lastDate + '\'' +
                ", organizer='" + organizer + '\'' +
                ", originalAllDay='" + originalAllDay + '\'' +
                ", rdate='" + rdate + '\'' +
                ", rrule='" + rrule + '\'' +
                ", selfAttendeeStatus='" + selfAttendeeStatus + '\'' +
                ", eventStatus=" + eventStatus +
                ", title='" + title + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public int getCalendar_id() {
        return calendar_id;
    }

    public void setCalendar_id(int calendar_id) {
        this.calendar_id = calendar_id;
    }

    public boolean isCanInviteOthers() {
        return canInviteOthers;
    }

    public void setCanInviteOthers(boolean canInviteOthers) {
        this.canInviteOthers = canInviteOthers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayColor() {
        return displayColor;
    }

    public void setDisplayColor(String displayColor) {
        this.displayColor = displayColor;
    }

    public long getDtend() {
        return dtend;
    }

    public void setDtend(long dtend) {
        this.dtend = dtend;
    }

    public long getDtsatrt() {
        return dtsatrt;
    }

    public void setDtsatrt(long dtsatrt) {
        this.dtsatrt = dtsatrt;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getEventColor() {
        return eventColor;
    }

    public void setEventColor(String eventColor) {
        this.eventColor = eventColor;
    }

    public String getEventColor_index() {
        return eventColor_index;
    }

    public void setEventColor_index(String eventColor_index) {
        this.eventColor_index = eventColor_index;
    }

    public String getEventEndTimezone() {
        return eventEndTimezone;
    }

    public void setEventEndTimezone(String eventEndTimezone) {
        this.eventEndTimezone = eventEndTimezone;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventTimezone() {
        return eventTimezone;
    }

    public void setEventTimezone(String eventTimezone) {
        this.eventTimezone = eventTimezone;
    }

    public String getExdate() {
        return exdate;
    }

    public void setExdate(String exdate) {
        this.exdate = exdate;
    }

    public String getExrule() {
        return exrule;
    }

    public void setExrule(String exrule) {
        this.exrule = exrule;
    }

    public boolean isHasAlarm() {
        return hasAlarm;
    }

    public void setHasAlarm(boolean hasAlarm) {
        this.hasAlarm = hasAlarm;
    }

    public boolean isOrganizer() {
        return isOrganizer;
    }

    public void setOrganizer(boolean organizer) {
        isOrganizer = organizer;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getOriginalAllDay() {
        return originalAllDay;
    }

    public void setOriginalAllDay(String originalAllDay) {
        this.originalAllDay = originalAllDay;
    }

    public String getRdate() {
        return rdate;
    }

    public void setRdate(String rdate) {
        this.rdate = rdate;
    }

    public String getRrule() {
        return rrule;
    }

    public void setRrule(String rrule) {
        this.rrule = rrule;
    }

    public String getSelfAttendeeStatus() {
        return selfAttendeeStatus;
    }

    public void setSelfAttendeeStatus(String selfAttendeeStatus) {
        this.selfAttendeeStatus = selfAttendeeStatus;
    }

    public int getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(int eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
