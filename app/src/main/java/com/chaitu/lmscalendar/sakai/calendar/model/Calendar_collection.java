package com.chaitu.lmscalendar.sakai.calendar.model;

/**
 * Created by CHAITU on 11/15/2016.
 */

public class Calendar_collection {

    private String entityReference;
    private String entityURL;
    private String entityTitle;
    private String title;
    private FirstTime firstTime;
    private long duration;
    private String eventId;
    private String assignmentId;
    private RecurrenceRule recurrenceRule;
    private String type;
    private String siteId;
    private String siteName;
    private String reference;
    private String creator;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FirstTime getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(FirstTime firstTime) {
        this.firstTime = firstTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public RecurrenceRule getRecurrenceRule() {
        return recurrenceRule;
    }

    public void setRecurrenceRule(RecurrenceRule recurrenceRule) {
        this.recurrenceRule = recurrenceRule;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getEntityReference() {
        return entityReference;
    }

    public void setEntityReference(String entityReference) {
        this.entityReference = entityReference;
    }

    public String getEntityURL() {
        return entityURL;
    }

    public void setEntityURL(String entityURL) {
        this.entityURL = entityURL;
    }

    public String getEntityTitle() {
        return entityTitle;
    }

    public void setEntityTitle(String entityTitle) {
        this.entityTitle = entityTitle;
    }

    @Override
    public String toString() {
        return "Calendar_collection{" +
                "entityReference='" + entityReference + '\'' +
                ", entityURL='" + entityURL + '\'' +
                ", entityTitle='" + entityTitle + '\'' +
                ", title='" + title + '\'' +
                ", firstTime=" + firstTime +
                ", duration=" + duration +
                ", eventId='" + eventId + '\'' +
                ", assignmentId='" + assignmentId + '\'' +
                ", recurrenceRule=" + recurrenceRule +
                ", type='" + type + '\'' +
                ", siteId='" + siteId + '\'' +
                ", siteName='" + siteName + '\'' +
                ", reference='" + reference + '\'' +
                ", creator='" + creator + '\'' +
                '}';
    }
}
