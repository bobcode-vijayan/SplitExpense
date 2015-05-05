package com.bobcode.splitexpense.activities;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.bobcode.splitexpense.helpers.DateAndTimeHelper;
import com.bobcode.splitexpense.helpers.GetContact;
import com.bobcode.splitexpense.helpers.SplitExpenseSQLiteHelper;
import com.bobcode.splitexpense.models.MemberDetailModel;
import com.bobcode.splitexpense.models.MemberProfileModel;
import com.bobcode.splitexpense.utils.MyUtils;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class AddOREditMemberActivity extends ActionBarActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private ImageView imgViewMemberPhoto;

    private EditText editTextMemberName;

    private EditText editTextMemberDisplayName;

    private EditText editTextMemberComments;

    private Button btnPickFromContact;

    private FloatingActionButton fabtnOK;

    private String from;

    private String memeberNameToEdit;

    private SplitExpenseSQLiteHelper splitExpenseSQLiteHelper;
    private List<MemberProfileModel> allAddedMembersList;
    private List<MemberDetailModel> memberDetailModelList;

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
        if (memberAction != null) {
            toolbar.setTitle(memberAction + " Member");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //if the action is to edit the existing member detail
            //pre-populate the selected member detail by default
            if (memberAction.equals("Edit")) {
                imgViewMemberPhoto.setImageBitmap(MyUtils.getPhoto(getIntent().getByteArrayExtra(Constants.MEMBER_PHOTO)));
                memeberNameToEdit = getIntent().getStringExtra(Constants.MEMBER_NAME);
                editTextMemberName.setText(memeberNameToEdit);
                editTextMemberDisplayName.setText(getIntent().getStringExtra(Constants.MEMBER_DISPLAY_NAME));
                editTextMemberComments.setText(getIntent().getStringExtra(Constants.MEMBER_COMMENTS));
            }
        }

        splitExpenseSQLiteHelper = new SplitExpenseSQLiteHelper(this);
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
                if(toolbar.getTitle().toString().trim().equals("Add Member")){
                    addAMember();
                }else if(toolbar.getTitle().toString().trim().equals("Edit Member")){
                    editAMember();
                }

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
                if(toolbar.getTitle().toString().trim().equals("Add Member")){
                    addAMember();
                }else if(toolbar.getTitle().toString().trim().equals("Edit Member")){
                    editAMember();
                }

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

    public void addAMember() {
        //validate name, display name and comments fields are not empty
        //if all the above said fields are not empty add the member
        //else provide the appropriate toast message to user

        if (editTextMemberName.getText().toString().trim().isEmpty()) {
            MyUtils.showToast(this, "member name should not be empty");
            editTextMemberName.setFocusable(true);
            editTextMemberName.requestFocus();
        } else if (editTextMemberDisplayName.getText().toString().trim().isEmpty()) {
            MyUtils.showToast(this, "member display name should not be empty");
            editTextMemberDisplayName.setFocusable(true);
            editTextMemberDisplayName.requestFocus();
        } else if (editTextMemberComments.getText().toString().trim().isEmpty()) {
            MyUtils.showToast(this, "member comments should not be empty");
            editTextMemberComments.setFocusable(true);
            editTextMemberComments.requestFocus();
        } else {
            try {
                //validate whether this member already exists by name field
                //if already exists show the appropriate toast message
                //else add the member
                String name = editTextMemberName.getText().toString().trim();
                allAddedMembersList = splitExpenseSQLiteHelper.getAllMembers();
                memberDetailModelList = new ArrayList<>();
                for (MemberProfileModel currentMemberDetail : allAddedMembersList) {
                    String currentName = currentMemberDetail.getName().trim();
                    if (currentName.equals(name)) {
                        MyUtils.showToast(this, "member already exits");

                        return;
                    }
                }

                imgViewMemberPhoto.buildDrawingCache();
                Bitmap photo = imgViewMemberPhoto.getDrawingCache();
                String displayName = editTextMemberDisplayName.getText().toString().trim();
                String comments = editTextMemberComments.getText().toString().trim();

                String todayDate = DateAndTimeHelper.getRawCurrentDate();
                todayDate = todayDate.replace(" ", "-");

                MemberProfileModel memberProfileModel = new MemberProfileModel(photo, name, displayName, comments, todayDate, todayDate);
                splitExpenseSQLiteHelper.addAMember(memberProfileModel);

                MyUtils.showToast(this, "member added successfully");

                finish();

                Intent intentAllMembers = new Intent(this, AllMembersActivity.class);
                startActivity(intentAllMembers);
                MyUtils.myPendingTransitionLeftInRightOut(this);
            } catch (Exception e) {
                MyUtils.showToast(this, "error adding member");
                e.printStackTrace();
            }
        }
    }

    //This function is to update the existing member detail
    public void editAMember(){
        try{
            //take the user entered data
            //compare with the "member_profile" table that whether the user exist or not by comparing name
            //if exist, show proper toast message
            //else update the member data with the entered values

            //member photo
            imgViewMemberPhoto.buildDrawingCache();
            Bitmap photo = imgViewMemberPhoto.getDrawingCache();

            //member name
            String name = editTextMemberName.getText().toString().trim();
            allAddedMembersList = splitExpenseSQLiteHelper.getAllMembers();
            memberDetailModelList = new ArrayList<>();
            for (MemberProfileModel currentMemberDetail : allAddedMembersList) {
                String currentName = currentMemberDetail.getName().trim();
                if (currentName.equals(name)) {
                    MyUtils.showToast(this, "member already exits");

                    return;
                }
            }

            //member display name
            String displayName = editTextMemberDisplayName.getText().toString().trim();

            //member comments
            String comments = editTextMemberComments.getText().toString().trim();

            //getting the current date. this is to update the "last modified" column in "member_profile" table
            String todayDate = DateAndTimeHelper.getRawCurrentDate();
            todayDate = todayDate.replace(" ", "-");

            MemberProfileModel memberProfileModel = new MemberProfileModel(photo, name, displayName, comments, todayDate);
            splitExpenseSQLiteHelper.updateAMember(memeberNameToEdit, memberProfileModel);

            MyUtils.showToast(this, "member updated successfully");

            finish();

            Intent intentAllMembers = new Intent(this, AllMembersActivity.class);
            startActivity(intentAllMembers);
            MyUtils.myPendingTransitionLeftInRightOut(this);

        }catch(Exception e){
            MyUtils.showToast(this, "error updating member");
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        MyUtils.myPendingTransitionLeftInRightOut(this);
    }
}
