package com.bobcode.splitexpense.tableschema;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by vijayananjalees on 5/5/15.
 */
public class AccountProfileTableSchema {

    public AccountProfileTableSchema(){}

    public static abstract class AccountProfile implements BaseColumns{
        //This is the table name
        public static final String TABLE_NAME = "account_profile";

        //This is the table fields (ie column name)
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_ACCOUNT_NAME = "accountName";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_CURRENCY = "currency";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_MEMBERS = "members";
        public static final String COLUMN_NAME_CREATED_ON = "createdOn";
        public static final String COLUMN_NAME_LAST_MODIFIED = "lastModified";
    }

    //This is to form a query to create a table
    private static final String SQL_CREATE_ACCOUNT_PROFILE_TABLE = "CREATE TABLE " + AccountProfile.TABLE_NAME +
            "(" + AccountProfile.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
            AccountProfile.COLUMN_NAME_ACCOUNT_NAME + " TEXT NOT NULL UNIQUE, " +
            AccountProfile.COLUMN_NAME_DESCRIPTION + " TEXT NOT NULL, " +
            AccountProfile.COLUMN_NAME_CURRENCY + " TEXT NOT NULL, " +
            AccountProfile.COLUMN_NAME_STATUS + " TEXT NOT NULL DEFAULT New, " +
            AccountProfile.COLUMN_NAME_MEMBERS + " TEXT NOT NULL, " +
            AccountProfile.COLUMN_NAME_CREATED_ON + " TEXT NOT NULL, " +
            AccountProfile.COLUMN_NAME_LAST_MODIFIED + " TEXT NOT NULL)";

    //This is to form a query to delete a table
    private static final String SQL_DELETE_ACCOUNT_PROFILE_TABLE = "DROP TABLE IF EXISTS " + AccountProfile.TABLE_NAME;

    // It is good practice to create a separate class per table.
    // This class defines static onCreate() and onUpgrade() methods.
    // These methods are called in the corresponding methods of SQLiteOpenHelper
    public static void onCreate(SQLiteDatabase database) {
        try {
            database.execSQL(SQL_CREATE_ACCOUNT_PROFILE_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        try{
            database.execSQL(SQL_DELETE_ACCOUNT_PROFILE_TABLE);
            onCreate(database);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
