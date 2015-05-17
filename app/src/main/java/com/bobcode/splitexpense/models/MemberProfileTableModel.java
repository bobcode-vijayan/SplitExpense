package com.bobcode.splitexpense.models;

import android.graphics.Bitmap;

/**
 * Created by vijayananjalees on 5/2/15.
 */
public class MemberProfileTableModel {

    private Bitmap photo;
    private String name;
    private String displayName;
    private String comments;
    private String createdOn;
    private String lastModified;

    public MemberProfileTableModel(Bitmap photo, String name, String displayName, String comments, String lastModified) {
        this.photo = photo;
        this.name = name;
        this.displayName = displayName;
        this.comments = comments;
        this.lastModified = lastModified;
    }

    public MemberProfileTableModel(Bitmap photo,
                                   String name,
                                   String displayName,
                                   String comments,
                                   String createdOn,
                                   String lastModified) {
        this.photo = photo;
        this.name = name;
        this.displayName = displayName;
        this.comments = comments;
        this.createdOn = createdOn;
        this.lastModified = lastModified;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
}
