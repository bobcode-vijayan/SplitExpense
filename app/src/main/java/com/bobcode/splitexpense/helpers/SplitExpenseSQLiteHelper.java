package com.bobcode.splitexpense.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import com.bobcode.splitexpense.models.AccountProfileTableModel;
import com.bobcode.splitexpense.models.AccountSummaryModel;
import com.bobcode.splitexpense.models.MemberProfileTableModel;
import com.bobcode.splitexpense.models.UserProfileTableModel;
import com.bobcode.splitexpense.tableschema.AccountProfileTableSchema;
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
        AccountProfileTableSchema.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        UserProfileTableSchema.onUpgrade(db, oldVersion, newVersion);
        MembersProfileTableSchema.onUpgrade(db, oldVersion, newVersion);
        AccountProfileTableSchema.onUpgrade(db, oldVersion, newVersion);
    }

//************************************** user_profile table ************************************************
// This table will hold the login user details such as
    //login name, password, answer to revel the password in forgot password page and created on date

    //Inserting data into table (ie Inserting new record)
    public void registerAUser(UserProfileTableModel userProfileTableModel) {
        // Open database connection in read and write mode
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //ContentValues can be used for inserts and updates of database entries
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserProfileTableSchema.UserProfile.COLUMN_NAME_USER_NAME, userProfileTableModel.getUserName());
        contentValues.put(UserProfileTableSchema.UserProfile.COLUMN_NAME_PASSWORD, userProfileTableModel.getPassword());
        contentValues.put(UserProfileTableSchema.UserProfile.COLUMN_NAME_ANSWER, userProfileTableModel.getAnswer());
        contentValues.put(UserProfileTableSchema.UserProfile.COLUMN_NAME_CREATED_ON, userProfileTableModel.getCreatedOn());

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
    public List<UserProfileTableModel> getAllRegisteredUser(){
        //this will hold all the registered user return from the "user-profile" table
        List<UserProfileTableModel> registeredUsersList = new ArrayList<UserProfileTableModel>();

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

                UserProfileTableModel userProfileTableModel = new UserProfileTableModel(tempUserName, tempPassword, answer);
                registeredUsersList.add(userProfileTableModel);
            } while(cursor.moveToNext());
        }
        return registeredUsersList;
    }
//************************************** user_profile table ************************************************


//************************************** member_profile table ************************************************
    public void addAMember(MemberProfileTableModel memberProfileTableModel){
        // Open database connection in read and write mode
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //ContentValues can be used for inserts and updates of database entries
        ContentValues contentValues = new ContentValues();
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_PHOTO, MyUtils.getBytes(memberProfileTableModel.getPhoto()));
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_NAME, memberProfileTableModel.getName());
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_DISPLAY_NAME, memberProfileTableModel.getDisplayName());
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_COMMENTS, memberProfileTableModel.getComments());
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_CREATED_ON, memberProfileTableModel.getCreatedOn());
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_LAST_MODIFIED, memberProfileTableModel.getLastModified());

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
    public List<MemberProfileTableModel> getAllMembers(){
        //this will hold all the members return from the "members-profile" table
        List<MemberProfileTableModel> allMembersList = new ArrayList<MemberProfileTableModel>();

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

                MemberProfileTableModel memberProfileTableModel = new MemberProfileTableModel(tempPhoto, tempName, tempDisplayName, tempComments, tempCreatedOn, tempLastModified);
                allMembersList.add(memberProfileTableModel);
            } while(cursor.moveToNext());
        }
        return allMembersList;
    }

    //This function is to get only the display name of all members
    public List<String> getDisplayNameOfAllMembers(){
        //this will hold all the members return from the "members-profile" table
        List<String> allMembersDisplayNameList = new ArrayList<String>();

        //This is to form a query to retrieve all record from "user_profile" table
        final String SQL_ALL_DATA_MEMBERS_PROFILE_TABLE = "select * from " + MembersProfileTableSchema.MembersProfile.TABLE_NAME;

        // Open database connection in read only mode
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(SQL_ALL_DATA_MEMBERS_PROFILE_TABLE, null);

        if(cursor.moveToFirst()){
            do{
                String tempDisplayNameName = cursor.getString(cursor.getColumnIndex(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_DISPLAY_NAME)).trim();
                allMembersDisplayNameList.add(tempDisplayNameName);
            } while(cursor.moveToNext());
        }
        return allMembersDisplayNameList;
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

    public void updateAMember(String name, MemberProfileTableModel memberProfileTableModel){
        //Open database connection in writable mode
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //Setup fields to update
        ContentValues contentValues = new ContentValues();
        byte[] tempPhoto = MyUtils.getBytes(memberProfileTableModel.getPhoto());
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_PHOTO, tempPhoto);
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_NAME, memberProfileTableModel.getName());
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_DISPLAY_NAME, memberProfileTableModel.getDisplayName());
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_COMMENTS, memberProfileTableModel.getComments());
        contentValues.put(MembersProfileTableSchema.MembersProfile.COLUMN_NAME_LAST_MODIFIED, memberProfileTableModel.getLastModified());

        //Updating row
        int result = sqLiteDatabase.update(MembersProfileTableSchema.MembersProfile.TABLE_NAME,
                contentValues,
                MembersProfileTableSchema.MembersProfile.COLUMN_NAME_NAME + " = ?",
                new String[]{name});

        //Close the database
        sqLiteDatabase.close();
    }
//************************************** member_profile table ************************************************


//************************************** account_profile table ************************************************
            //********************* Add an account (ie insert a row) *****************************
    public void addAnAccount(AccountProfileTableModel accountProfileTableModel){
        // Open database connection in read and write mode
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //ContentValues can be used for inserts and updates of database entries
        ContentValues contentValues = new ContentValues();
        contentValues.put(AccountProfileTableSchema.AccountProfile.COLUMN_NAME_ACCOUNT_NAME, accountProfileTableModel.getAccountName());
        contentValues.put(AccountProfileTableSchema.AccountProfile.COLUMN_NAME_DESCRIPTION, accountProfileTableModel.getDescription());
        contentValues.put(AccountProfileTableSchema.AccountProfile.COLUMN_NAME_CURRENCY, accountProfileTableModel.getCurrency());
        contentValues.put(AccountProfileTableSchema.AccountProfile.COLUMN_NAME_STATUS, accountProfileTableModel.getStatus());
        contentValues.put(AccountProfileTableSchema.AccountProfile.COLUMN_NAME_MEMBERS, accountProfileTableModel.getMembers());
        contentValues.put(AccountProfileTableSchema.AccountProfile.COLUMN_NAME_CREATED_ON, accountProfileTableModel.getCreatedOn());
        contentValues.put(AccountProfileTableSchema.AccountProfile.COLUMN_NAME_LAST_MODIFIED, accountProfileTableModel.getLastModified());

        long tag_ID = sqLiteDatabase.insertOrThrow(AccountProfileTableSchema.AccountProfile.TABLE_NAME, null, contentValues);

        sqLiteDatabase.close();
    }

    //********************* get the account count *****************************
    //This function is to get the account count in "account_profile" table
    public int getAccountProfileAccountCount(){
        //This is to form a query to retrieve all record from "account_profile" table
        final String SQL_ALL_DATA_ACCOUNT_PROFILE_TABLE = "select * from " + AccountProfileTableSchema.AccountProfile.TABLE_NAME;

        // Open database connection in read only mode
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(SQL_ALL_DATA_ACCOUNT_PROFILE_TABLE, null);
        int count = cursor.getCount();
        cursor.close();
        sqLiteDatabase.close();

        return count;
    }

    //********************* get all the account details *****************************
    //This function is to get all the added accounts of the app
    public List<AccountSummaryModel> getAllAccounts(){
        //this will hold all the added accounts return from the "account-profile" table
        List<AccountSummaryModel> accountSummaryModelList = new ArrayList<AccountSummaryModel>();

        //This is to form a query to retrieve all record from "user_profile" table
        final String SQL_ALL_DATA_ACCOUNT_PROFILE_TABLE = "select * from " + AccountProfileTableSchema.AccountProfile.TABLE_NAME;

        // Open database connection in read only mode
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(SQL_ALL_DATA_ACCOUNT_PROFILE_TABLE, null);

        if(cursor.moveToFirst()){
            do{
                //Data from two tables (1. account_profile table and 2. account_master table)

                //1.account_profile table data (account name, description, currency, status, members, createdOn)
                String tempAccountName = cursor.getString(cursor.getColumnIndex(AccountProfileTableSchema.AccountProfile.COLUMN_NAME_ACCOUNT_NAME));
                String tempMembers = cursor.getString(cursor.getColumnIndex(AccountProfileTableSchema.AccountProfile.COLUMN_NAME_MEMBERS));
                String tempDescription = cursor.getString(cursor.getColumnIndex(AccountProfileTableSchema.AccountProfile.COLUMN_NAME_DESCRIPTION));
                String tempCreatedOn = cursor.getString(cursor.getColumnIndex(AccountProfileTableSchema.AccountProfile.COLUMN_NAME_CREATED_ON));
                String tempStatus = cursor.getString(cursor.getColumnIndex(AccountProfileTableSchema.AccountProfile.COLUMN_NAME_STATUS));
                String tempCurrency = cursor.getString(cursor.getColumnIndex(AccountProfileTableSchema.AccountProfile.COLUMN_NAME_CURRENCY));

                //2.account_master table data (no of expenses and total spend for a particular account)
                int tempNoOfExpense = 101;
                float tempTotalSpend = (float) 671.89;

                AccountSummaryModel accountSummaryModel = new AccountSummaryModel(tempAccountName, tempMembers, tempDescription, tempCreatedOn, tempNoOfExpense, tempTotalSpend, tempStatus, tempCurrency);
                accountSummaryModelList.add(accountSummaryModel);
            } while(cursor.moveToNext());
        }
        return accountSummaryModelList;
    }

    //********************* update an account details *****************************
    //This function is to update a existing account details
    public void updateAnAccount(String name, AccountProfileTableModel accountProfileTableModel){
        //Open database connection in writable mode
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //Setup fields to update
        ContentValues contentValues = new ContentValues();
        contentValues.put(AccountProfileTableSchema.AccountProfile.COLUMN_NAME_ACCOUNT_NAME, accountProfileTableModel.getAccountName());
        contentValues.put(AccountProfileTableSchema.AccountProfile.COLUMN_NAME_DESCRIPTION, accountProfileTableModel.getDescription());
        contentValues.put(AccountProfileTableSchema.AccountProfile.COLUMN_NAME_CURRENCY, accountProfileTableModel.getCurrency());
        contentValues.put(AccountProfileTableSchema.AccountProfile.COLUMN_NAME_STATUS, accountProfileTableModel.getStatus());
        contentValues.put(AccountProfileTableSchema.AccountProfile.COLUMN_NAME_MEMBERS, accountProfileTableModel.getMembers());
        contentValues.put(AccountProfileTableSchema.AccountProfile.COLUMN_NAME_LAST_MODIFIED, accountProfileTableModel.getLastModified());
        //Updating row
        sqLiteDatabase.update(AccountProfileTableSchema.AccountProfile.TABLE_NAME,
                contentValues,
                AccountProfileTableSchema.AccountProfile.COLUMN_NAME_ACCOUNT_NAME + " = ?",
                new String[] {String.valueOf(name)});

        //Close the database
        sqLiteDatabase.close();
    }

    //********************* update all account's account name *****************************
    //This function is to get only the account name of all account
    public List<String> getAccountNameOfAllAccount(){
        //this will hold all the account name return from the "account-profile" table
        List<String> allAccountNameList = new ArrayList<String>();

        //This is to form a query to retrieve all record from "user_profile" table
        final String SQL_ALL_ACCOUNT_NAME_ACCOUNT_PROFILE_TABLE = "select * from " + AccountProfileTableSchema.AccountProfile.TABLE_NAME;

        // Open database connection in read only mode
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(SQL_ALL_ACCOUNT_NAME_ACCOUNT_PROFILE_TABLE, null);

        if(cursor.moveToFirst()){
            do{
                String tempAccountName = cursor.getString(cursor.getColumnIndex(AccountProfileTableSchema.AccountProfile.COLUMN_NAME_ACCOUNT_NAME)).trim();
                allAccountNameList.add(tempAccountName);
            } while(cursor.moveToNext());
        }
        return allAccountNameList;
    }

    //********************* update an account details *****************************
    //This function is to delete an existing account details
    public void deleteAnAccount(String accountToDelete){
        //Open database connection in writable mode
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //delete row
        sqLiteDatabase.delete(AccountProfileTableSchema.AccountProfile.TABLE_NAME,
                AccountProfileTableSchema.AccountProfile.COLUMN_NAME_ACCOUNT_NAME + " = ?",
                new String[]{String.valueOf(accountToDelete)});

        //Close the database
        sqLiteDatabase.close();
    }

//************************************** account_profile table ************************************************
}
