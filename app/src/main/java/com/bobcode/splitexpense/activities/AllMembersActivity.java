package com.bobcode.splitexpense.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bobcode.splitexpense.R;
import com.bobcode.splitexpense.adapters.MembersDetailsAdapter;
import com.bobcode.splitexpense.models.MemberDetailModel;
import com.bobcode.splitexpense.utils.MyUtils;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class AllMembersActivity extends ActionBarActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private FloatingActionButton fabtnAddMember;

    private TextView textViewNoMemberExists;

    private RecyclerView cardListForAllMembers;

    private List<MemberDetailModel> memberDetailModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_members);

        //Retrieve the user clicked account name and set the name as toolbar title
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("All Members");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //This is to display a no member exists message if no member exits
        // when user navigate to all member page
        textViewNoMemberExists = (TextView) findViewById(R.id.textViewNoMemberExists);

        //floating action button to add an event
        fabtnAddMember = (FloatingActionButton) findViewById(R.id.fabtnAddMember);
        fabtnAddMember.setOnClickListener(this);

//************************ Recycler view to display all the available members ****************************
        cardListForAllMembers = (RecyclerView) findViewById(R.id.cardListForAllMembers);
        cardListForAllMembers.setHasFixedSize(true);
        fabtnAddMember.attachToRecyclerView(cardListForAllMembers);

        //RecyclerView requires a layout manager
        //This component positions item views inside the row and determines when it is time to
        // recycle the views
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cardListForAllMembers.setLayoutManager(linearLayoutManager);

//------------------------------- Pending -------------------------------------------------------
        //validate if any member exits in "members profile" table
        //if any member exits display all the members details from the table
        //else display the text message to the user to create an member to begin with
        boolean isMemberExits = true;
        if (isMemberExits) {
            //Data
            MemberDetailModel member1 = new MemberDetailModel("Vijayan Anjalees", "Vijayan", "Friend");
            MemberDetailModel member2 = new MemberDetailModel("Senthil CRO", "Senthil", "Friend");
            MemberDetailModel member3 = new MemberDetailModel("Amitesh Dixit", "Amitesh", "Friend");
            MemberDetailModel member4 = new MemberDetailModel("Shanmuga", "Shanmuga", "Friend");
            MemberDetailModel member5 = new MemberDetailModel("Shanmuga Vadivel", "Shanmuga Vadivel", "Friend");
            MemberDetailModel member6 = new MemberDetailModel("Vinod Dewangan", "Vinod", "Friend");

            memberDetailModelList = new ArrayList<>();
            memberDetailModelList.add(member1);
            memberDetailModelList.add(member2);
            memberDetailModelList.add(member3);
            memberDetailModelList.add(member4);
            memberDetailModelList.add(member5);
            memberDetailModelList.add(member6);

            MembersDetailsAdapter membersDetailsAdapter = new MembersDetailsAdapter(this, (ArrayList) memberDetailModelList);
            cardListForAllMembers.setAdapter(membersDetailsAdapter);
        } else {
            cardListForAllMembers.setVisibility(View.VISIBLE);
        }
//------------------------------- Pending -------------------------------------------------------
//************************ Recycler view to display all the available members ****************************

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabtnAddMember:
                Intent intentAddMember = new Intent(this, AddOREditMemberActivity.class);
                startActivity(intentAddMember);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_members, menu);
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

                MyUtils.myPendingTransitionLeftInRightOut(this);
                break;

            case R.id.action_all_accounts:
                Intent intentAllAccounts = new Intent(this, AllAccountsActivity.class);
                startActivity(intentAllAccounts);

                MyUtils.myPendingTransitionRightInLeftOut(this);
                break;

            case R.id.action_settings:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        MyUtils.myPendingTransitionLeftInRightOut(this);
    }

}
