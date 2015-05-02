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
import com.bobcode.splitexpense.constants.Constants;
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

    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_member);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        imgViewMemberPhoto = (ImageView) findViewById(R.id.imgViewMemberPhoto);

        editTextMemberName = (EditText) findViewById(R.id.editTextMemberName);
        editTextMemberDisplayName = (EditText) findViewById(R.id.editTextMemberDisplayName);
        editTextMemberComments = (EditText) findViewById(R.id.editTextMemberComments);

        btnPickFromContact = (Button) findViewById(R.id.btnPickFromContact);
        btnPickFromContact.setOnClickListener(this);

        fabtnOK = (FloatingActionButton) findViewById(R.id.fabtnOkMember);
        fabtnOK.setOnClickListener(this);

        String memberAction = getIntent().getStringExtra(Constants.MEMBER_ACTION).trim();
        if (memberAction != null){
            toolbar.setTitle(memberAction + " Member");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //if the action is to edit the existing member detail
            //pre-populate the selected member detail by default
            if(memberAction.equals("Edit")){
                editTextMemberName.setText(getIntent().getStringExtra(Constants.MEMBER_NAME));
                editTextMemberDisplayName.setText(getIntent().getStringExtra(Constants.MEMBER_DISPLAY_NAME));
                editTextMemberComments.setText(getIntent().getStringExtra(Constants.MEMBER_COMMENTS));
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPickFromContact:
                Intent intentContact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intentContact, 1);

                break;

            case R.id.fabtnOkMember:
                from = "ok";

                onBackPressed();

                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_edit_member, menu);
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
                from = "cancel";
                onBackPressed();

                break;

            case R.id.action_tick:
                from = "tick";
                onBackPressed();
                break;

            default:
                //add account, add member, report, help & logout handled here
                MyUtils.commonMenuActions(this, item);

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

    @Override
    public void onBackPressed() {
        if((toolbar.getTitle().toString().toLowerCase().trim().equals("add member")) && (!from.equals("cancel"))){
            MyUtils.showToast(this, "Member added successfully");
        }else if((toolbar.getTitle().toString().toLowerCase().trim().equals("edit member")) && (!from.equals("cancel"))){
            MyUtils.showToast(this, "Member updated successfully");
        }

        super.onBackPressed();

        MyUtils.myPendingTransitionLeftInRightOut(this);
    }

}
