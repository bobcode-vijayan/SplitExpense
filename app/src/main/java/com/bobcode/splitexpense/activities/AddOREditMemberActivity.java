package com.bobcode.splitexpense.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bobcode.splitexpense.R;
import com.bobcode.splitexpense.helpers.GetContact;
import com.bobcode.splitexpense.utils.MyUtils;
import com.melnykov.fab.FloatingActionButton;


public class AddOREditMemberActivity extends ActionBarActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private ImageView imgViewMemberPhoto;

    private EditText editTextMemberName;

    private EditText editTextMemberDisplayName;

    private EditText editTextMemberComments;

    private Button btnPickFromContact;

    private FloatingActionButton fabtnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_member);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Member");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgViewMemberPhoto = (ImageView) findViewById(R.id.imgViewMemberPhoto);

        editTextMemberName = (EditText) findViewById(R.id.editTextMemberName);
        editTextMemberDisplayName = (EditText) findViewById(R.id.editTextMemberDisplayName);
        editTextMemberComments = (EditText) findViewById(R.id.editTextMemberComments);

        btnPickFromContact = (Button) findViewById(R.id.btnPickFromContact);
        btnPickFromContact.setOnClickListener(this);

        fabtnOK = (FloatingActionButton) findViewById(R.id.fabtnAllAccounts);
        fabtnOK.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPickFromContact:
                Intent intentContact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intentContact, 1);

                break;

            case R.id.fabtnAllAccounts:
                Intent intentAllMembers = new Intent(this, AllMembersActivity.class);
                startActivity(intentAllMembers);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_person, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                super.onBackPressed();
                break;

            case R.id.action_settings:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            GetContact getContact = new GetContact(data, this);
            getContact.getContactDetails();
            editTextMemberName.setText(getContact.getName());
            editTextMemberDisplayName.setText(getContact.getName());
            //txtVewContactNumber.setText(getContact.getPhoneNumber());
            if (getContact.getThumbnailSizedImage() != null)
                imgViewMemberPhoto.setImageBitmap(getContact.getThumbnailSizedImage());
            else {
                MyUtils.showToast(this, "no photo found");
                imgViewMemberPhoto.setImageResource(R.drawable.ic_image_holder);
            }
        } else if (resultCode == RESULT_CANCELED) {
            MyUtils.showToast(this, "Oops..... You have not selected any contact");
        }
    }
}
