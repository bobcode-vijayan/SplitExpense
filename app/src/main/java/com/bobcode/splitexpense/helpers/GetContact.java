package com.bobcode.splitexpense.helpers;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by vijayananjalees on 3/26/15.
 * <p/>
 * implementation
 * Intent intentContact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
 * startActivityForResult(intentContact, 1);
 * <p/>
 * GetContact getContact = new GetContact(data, this);
 * getContact.getContactDetails();
 */

public class GetContact {

    //return intent with the selected contact data from contact app
    private Intent data;

    //context of the calling activity to access its resources
    private Context context;

    //place holder for contact name, phone number and thumbnail image
    private String name;
    //private String phoneNumber;
    private Bitmap thumbnailSizedImage;


    public GetContact(Intent data, Context context) {
        this.data = data;
        this.context = context;
    }

    public String getName() {
        return name;
    }

//    public String getPhoneNumber() {
//        return phoneNumber;
//    }

    public Bitmap getThumbnailSizedImage() {
        return thumbnailSizedImage;
    }

    public void getContactDetails() {

        //once contact is selected, the contact app send back the data via intent
        //get the data from the intent which was returned by contact app
        Uri uri = data.getData();

        //Gets the decoded last segment in the path
        String contactID = uri.getLastPathSegment();


        //CONTENT_URI	The content:// style URI for all data records of the CONTENT_ITEM_TYPE MIME type,
        // combined with the associated raw contact and aggregate contact data.
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone._ID + "=?",
                new String[]{contactID}, null);

        //Move the cursor to the first row.
        //This method will return false if the cursor is empty.
        //meaning if no data from contact app exit form this function without doing any further activity
        if (!cursor.moveToFirst())
            return;

        //String contactDisplayName = cursor.getString(cursor.getColumnIndex("display_name"));
        name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

        //phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

        try {
            //Retrieve the thumbnail phone (it will be a byte input stream)
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                            Long.parseLong(cursor.getString(cursor.getColumnIndex("contact_id")))));

            //validate the selected contact has photo
            if (inputStream != null) {
                //If yes, convert the byte input stream into bitmap
                thumbnailSizedImage = BitmapFactory.decodeStream(inputStream);

                //close the input stream to avoid memory leakage
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

