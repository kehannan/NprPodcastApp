package com.hannan.kevin.model;

import android.media.Rating;

/**
 * Created by khannan on 7/3/16.
 */



public class Attributes {

    private String type;
    private String uid;
    private String title;
    private String audioTitle;
    private Boolean primary;
    private Boolean skippable;
    private String provider;
    private String program;
    private Integer duration;
    private String date;
    private String description;
    private String rationale;
    private Rating rating;

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The uid
     */
    public String getUid() {
        return uid;
    }

    /**
     *
     * @param uid
     * The uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The audioTitle
     */
    public String getAudioTitle() {
        return audioTitle;
    }

    /**
     *
     * @param audioTitle
     * The audioTitle
     */
    public void setAudioTitle(String audioTitle) {
        this.audioTitle = audioTitle;
    }

    /**
     *
     * @return
     * The primary
     */
    public Boolean getPrimary() {
        return primary;
    }

    /**
     *
     * @param primary
     * The primary
     */
    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }


    /**
     *
     * @return
     * The skippable
     */
    public Boolean getSkippable() {
        return skippable;
    }

    /**
     *
     * @param skippable
     * The skippable
     */
    public void setSkippable(Boolean skippable) {
        this.skippable = skippable;
    }

    /**
     *
     * @return
     * The provider
     */
    public String getProvider() {
        return provider;
    }

    /**
     *
     * @param provider
     * The provider
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     *
     * @return
     * The program
     */
    public String getProgram() {
        return program;
    }

    /**
     *
     * @param program
     * The program
     */
    public void setProgram(String program) {
        this.program = program;
    }

    /**
     *
     * @return
     * The duration
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     *
     * @param duration
     * The duration
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     *
     * @return
     * The date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The rationale
     */
    public String getRationale() {
        return rationale;
    }

    /**
     *
     * @param rationale
     * The rationale
     */
    public void setRationale(String rationale) {
        this.rationale = rationale;
    }

    /**
     *
     * @return
     * The rating
     */
    public Rating getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     * The rating
     */
    public void setRating(Rating rating) {
        this.rating = rating;
    }

}
