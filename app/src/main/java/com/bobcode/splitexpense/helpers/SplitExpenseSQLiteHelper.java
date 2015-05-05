package com.bobcode.splitexpense.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import com.bobcode.splitexpense.models.MemberProfileModel;
import com.bobcode.splitexpense.models.UserProfileModel;
import com.bobcode.splitexpense.tableschema.MembersProfileTableSchema;
import com.bobcode.splitexpense.tableschema.UserProfileTableSchema;
import com.bobcode.splitexpense.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vijayananjalees on 5/2/15.
 */
public class SplitExpenseSQLiteHelper extends SQLiteOpenHelper{

    //This is the database version
    public static final int DATABASE_VERSION = 1;

    //This is the database name for this app which may have several tables
    public static final String DATABASE_NAME = "SplitExpense.db";

    Context context;

    public SplitExpenseSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        //super.onOpen(db);
        //Log.d("vijayan", "inside onOpen");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        UserProfileTableSchema.onCreate(db);
        MembersProfileTableSchema.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        UserProfileTableSchema.onUpgrade(db, oldVersion, newVersion);
        MembersProfileTableSchema.onUpgrade(db, oldVersion, newVersion);
    }


    //Inserting data into table (ie Inserting new record)
    public void registerAUser(UserProfileModel userProfileModel) {
        // Open database connection in read and write mode
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //ContentValues can be used for inserts and updates of database entries
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserProfileTableSchema.UserProfile.COLUMN_NAME_USER_NAME, userProfileModel.getUserName());
        contentValues.put(UserProfileTableSchema.UserProfile.COLUMN_NAME_PASSWORD, userProfileModel.getPassword());
        contentValues.put(UserProfileTableSchema.UserProfile.COLUMN_NAME_ANSWER, userProfileModel.getAnswer());
        contentValues.put(UserProfileTableSchema.UserProfile.COLUMN_NAME_CREATED_ON, userProfileModel.getCreatedOn());

        long tag_ID = sqLiteDatabase.insertOrThrow(UserProfileTableSchema.UserProfile.TABLE_NAME, null, contentValues);

        sqLiteDatabase.close();
    }

    //This function is to get the registered user count of the app
    public int getRegisteredUserCount(){
        //This is to form a query to retrieve all record from "user_profile" table
        final String SQL_ALL_DATA_USER_PROFILE_TABLE = "select * from " + UserProfileTableSchema.UserProfile.TABLE_NAME;

        // Open database connection in read only mode
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(SQL_ALL_DATA_USER_PROFILE_TABLE, null);
        int count = cursor.getCount();
        cursor.close();
        sqLiteDatabase.close();

        return count;
    }

    //This function is to get all the registered user of the app
    public List<UserProfileModel> getAllRegisteredUser(){
        //this will hold all the registered user return from the "user-profile" table
        List<UserProfileModel> registeredUsersList = new ArrayList<UserProfileModel>();

        //This is to form a query to retrieve all record from "user_profile" table
        final String SQL_ALL_DATA_USER_PROFILE_TABLE = "select * from " + UserProfileTableSchema.UserProfile.TABLE_NAME;

        // Open database connection in read only mode
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(SQL_ALL_DATA_USER_PROFILE_TABLE, null);

        if(cursor.moveToFirst()){
            do{
                String tempUserName = cursor.getString(cursor.getColumnIndex(UserProfileTableSchema.UserProfile.COLUMN_NAME_USER_NAME));
                String tempPassword = cursor.getString(cursor.getColumnIndex(UserProfileTableSchema.UserProfile.COLUMN_NAME_PASSWORD));
                String answer = cursor.getString(cursor.getColumnIndex(UserProfileTableSchema.UserProfile.COLUMN_NAME_ANSWER));

                UserProfileModel userProfileModel = new UserProfileModel(tempUserName, tempPassword, answer);
                registeredUsersList.add(userProfileModel);
            } while(cursor.moveToNext());
        }
        return registeredUsersList;
    }




    public void addAMember(MemberProfileModel memberProfileModel){
        // Open database connection in read and write mode
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //ContentValues can be used for inserts and updates of database entries
        ContentValues contentValues = new ContentValues();
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_PHOTO, MyUtils.getBytes(memberProfileModel.getPhoto()));
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_NAME, memberProfileModel.getName());
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_DISPLAY_NAME, memberProfileModel.getDisplayName());
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_COMMENTS, memberProfileModel.getComments());
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_CREATED_ON, memberProfileModel.getCreatedOn());
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_LAST_MODIFIED, memberProfileModel.getLastModified());

        long tag_ID = sqLiteDatabase.insertOrThrow(MembersProfileTableSchema.MembersProfile.TABLE_NAME, null, contentValues);

        sqLiteDatabase.close();
    }

    //This function is to get the registered user count of the app
    public int getMembersCount(){
        //This is to form a query to retrieve all record from "user_profile" table
        final String SQL_ALL_DATA_MEMBERS_PROFILE_TABLE = "select * from " + MembersProfileTableSchema.MembersProfile.TABLE_NAME;

        // Open database connection in read only mode
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(SQL_ALL_DATA_MEMBERS_PROFILE_TABLE, null);
        int count = cursor.getCount();
        cursor.close();
        sqLiteDatabase.close();

        return count;
    }

    //This function is to get all members of the app
    public List<MemberProfileModel> getAllMembers(){
        //this will hold all the members return from the "members-profile" table
        List<MemberProfileModel> allMembersList = new ArrayList<MemberProfileModel>();

        //This is to form a query to retrieve all record from "user_profile" table
        final String SQL_ALL_DATA_MEMBERS_PROFILE_TABLE = "select * from " + MembersProfileTableSchema.MembersProfile.TABLE_NAME;

        // Open database connection in read only mode
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(SQL_ALL_DATA_MEMBERS_PROFILE_TABLE, null);

        if(cursor.moveToFirst()){
            do{
                Bitmap tempPhoto = MyUtils.getPhoto(cursor.getBlob(cursor.getColumnIndex(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_PHOTO)));
                String tempName = cursor.getString(cursor.getColumnIndex(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_NAME));
                String tempDisplayName = cursor.getString(cursor.getColumnIndex(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_DISPLAY_NAME));
                String tempComments = cursor.getString(cursor.getColumnIndex(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_COMMENTS));
                String tempCreatedOn = cursor.getString(cursor.getColumnIndex(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_CREATED_ON));
                String tempLastModified = cursor.getString(cursor.getColumnIndex(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_LAST_MODIFIED));

                MemberProfileModel memberProfileModel = new MemberProfileModel(tempPhoto, tempName, tempDisplayName, tempComments, tempCreatedOn, tempLastModified);
                allMembersList.add(memberProfileModel);
            } while(cursor.moveToNext());
        }
        return allMembersList;
    }

    //This function is to delete a member from "member_profile" table
    public void deleteAMember(String memberToDelete){
        // Open database connection in writable mode
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //Delete the record with the specified _id
//        sqLiteDatabase.delete(MembersProfileTableSchema.MembersProfile.TABLE_NAME,
//                MembersProfileTableSchema.MembersProfile.COLUMN_NAME_ID + " = ?", new String[]{"1"});

        //Delete the record with the specified name
        sqLiteDatabase.delete(MembersProfileTableSchema.MembersProfile.TABLE_NAME,
                MembersProfileTableSchema.MembersProfile.COLUMN_NAME_NAME + " = ?", new String[]{memberToDelete});

        //Close the database
        sqLiteDatabase.close();
    }

    public void updateAMember(String name, MemberProfileModel memberProfileModel){
        //Open database connection in writable mode
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //Setup fields to update
        ContentValues contentValues = new ContentValues();
        byte[] tempPhoto = MyUtils.getBytes(memberProfileModel.getPhoto());
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_PHOTO, tempPhoto);
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_NAME, memberProfileModel.getName());
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_DISPLAY_NAME, memberProfileModel.getDisplayName());
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_COMMENTS, memberProfileModel.getComments());
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_LAST_MODIFIED, memberProfileModel.getLastModified());

        //Updating row
        int result = sqLiteDatabase.update(MembersProfileTableSchema.MembersProfile.TABLE_NAME,
                contentValues,
                MembersProfileTableSchema.MembersProfile.COLUMN_NAME_NAME + " = ?",
                new String[]{name});

        //Close the database
        sqLiteDatabase.close();
    }
}
