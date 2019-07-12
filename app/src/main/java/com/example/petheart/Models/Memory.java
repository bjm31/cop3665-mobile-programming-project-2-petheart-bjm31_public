package com.example.petheart.Models;


import java.util.Date;
import java.util.UUID;


public class Memory {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mFavorite;

    public Memory() {
        this(UUID.randomUUID());
    }

    public Memory(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public UUID getId() {
        return mId;
    }

    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(boolean favorite) {
        mFavorite = favorite;
    }
}
