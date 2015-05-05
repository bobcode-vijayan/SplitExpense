package com.bobcode.splitexpense.models;

import android.graphics.Bitmap;

/**
 * Created by vijayananjalees on 4/22/15.
 */
public class MemberDetailModel {
    public Bitmap photo;
    public String name;
    public String displayName;
    public String comments;

    public MemberDetailModel(Bitmap photo, String name, String displayName, String comments) {
        this.photo = photo;
        this.name = name;
        this.displayName = displayName;
        this.comments = comments;
    }
}
