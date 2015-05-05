package com.bobcode.splitexpense.tableschema;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by vijayananjalees on 5/2/15.
 */
public class MembersProfileTableSchema {

    public MembersProfileTableSchema(){
    }

    public static abstract class MembersProfile implements BaseColumns {
        //This is the table name
        public static final String TABLE_NAME = "members_profile";

        //This is the table fields (ie column name)
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_PHOTO = "photo";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DISPLAY_NAME = "displayName";
        public static final String COLUMN_NAME_COMMENTS = "comments";
        public static final String COLUMN_NAME_CREATED_ON = "createdOn";
        public static final String COLUMN_NAME_LAST_MODIFIED = "lastModified";
    }

    //This is to form a query to create a table
    private static final String SQL_CREATE_MEMBERS_PROFILE_TABLE = "CREATE TABLE " + MembersProfile.TABLE_NAME +
            "(" + MembersProfile.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
            MembersProfile.COLUMN_NAME_PHOTO + " BLOB, " +
            MembersProfile.COLUMN_NAME_NAME + " TEXT NOT NULL UNIQUE, " +
            MembersProfile.COLUMN_NAME_DISPLAY_NAME + " TEXT NOT NULL, " +
            MembersProfile.COLUMN_NAME_COMMENTS + " TEXT NOT NULL, " +
            MembersProfile.COLUMN_NAME_CREATED_ON + " TEXT NOT NULL, " +
            MembersProfile.COLUMN_NAME_LAST_MODIFIED + " TEXT NOT NULL)";

    //This is to form a query to delete a table
    private static final String SQL_DELETE_MEMBERS_PROFILE_TABLE = "DROP TABLE IF EXISTS " + MembersProfile.TABLE_NAME;

    // It is good practice to create a separate class per table.
    // This class defines static onCreate() and onUpgrade() methods.
    // These methods are called in the corresponding methods of SQLiteOpenHelper
    public static void onCreate(SQLiteDatabase database) {
        try {
            database.execSQL(SQL_CREATE_MEMBERS_PROFILE_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        try{
            database.execSQL(SQL_DELETE_MEMBERS_PROFILE_TABLE);
            onCreate(database);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
