package com.bobcode.splitexpense.tableschema;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by vijayananjalees on 5/2/15.
 */
public class UserProfileTableSchema {

    public UserProfileTableSchema() {
    }

    public static abstract class UserProfile implements BaseColumns{
        //This is the table name
        public static final String TABLE_NAME = "user_profile";

        //This is the table fields (ie column name)
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_USER_NAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_ANSWER = "answer";
        public static final String COLUMN_NAME_CREATED_ON = "createdOn";
    }

    //This is to form a query to create a table
    private static final String SQL_CREATE_USER_PROFILE_TABLE = "CREATE TABLE " + UserProfile.TABLE_NAME +
            "(" + UserProfile.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
            UserProfile.COLUMN_NAME_USER_NAME + " TEXT NOT NULL UNIQUE, " +
            UserProfile.COLUMN_NAME_PASSWORD + " TEXT NOT NULL, " +
            UserProfile.COLUMN_NAME_ANSWER + " TEXT NOT NULL, " +
            UserProfile.COLUMN_NAME_CREATED_ON + " TEXT NOT NULL)";

    //This is to form a query to delete a table
    private static final String SQL_DELETE_USER_PROFILE_TABLE = "DROP TABLE IF EXISTS " + UserProfile.TABLE_NAME;


    // It is good practice to create a separate class per table.
    // This class defines static onCreate() and onUpgrade() methods.
    // These methods are called in the corresponding methods of SQLiteOpenHelper
    public static void onCreate(SQLiteDatabase database) {
        try {
            database.execSQL(SQL_CREATE_USER_PROFILE_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        try{
            database.execSQL(SQL_DELETE_USER_PROFILE_TABLE);
            onCreate(database);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
