package com.bobcode.splitexpense.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bobcode.splitexpense.R;
import com.bobcode.splitexpense.adapters.EventSummaryAdapter;
import com.bobcode.splitexpense.constants.Constants;
import com.bobcode.splitexpense.models.EventSummaryModel;
import com.bobcode.splitexpense.utils.MyUtils;
import com.melnykov.fab.FloatingActionButton;


public class AllEventsActivity extends ActionBarActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private TextView textViewNoEventExists;

    private FloatingActionButton fabtnAddEvent;

    private RecyclerView recyclerViewAllEvents;

    private String accountName;
    private String accountCurrency;
    private String accountMembers;
    private String accountStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);

        //Retrieve the user clicked account details
        accountName = getIntent().getStringExtra(Constants.ACCOUNT_NAME);
        accountCurrency = getIntent().getStringExtra(Constants.ACCOUNT_CURRENCY);
        accountMembers = getIntent().getStringExtra(Constants.ACCOUNT_MEMBERS);
        accountStatus = getIntent().getStringExtra(Constants.ACCOUNT_STATUS);

        //Retrieve the user clicked account name and set the name as toolbar title
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(accountName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //This is to display a no event exists message if no event exits
        // when user navigate to all event page
        textViewNoEventExists = (TextView) findViewById(R.id.textViewNoEventExists);

        //floating action button to add an event
        fabtnAddEvent = (FloatingActionButton) findViewById(R.id.fabtnAddEvent);
        fabtnAddEvent.setOnClickListener(this);


//************************ Recycler view to display all the available event for an account ****************************
        recyclerViewAllEvents = (RecyclerView) findViewById(R.id.cardListForAnEvent);
        recyclerViewAllEvents.setHasFixedSize(true);
        fabtnAddEvent.attachToRecyclerView(recyclerViewAllEvents);

        //RecyclerView requires a layout manager
        //This component positions item views inside the row and determines when it is time to
        // recycle the views
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewAllEvents.setLayoutManager(linearLayoutManager);

//------------------------------- Pending -------------------------------------------------------
        //validate if any event exits for the selected "Account" in table
        //if any event exits display all the events details from the table
        //else display the text message to the user to create an event to begin with

        boolean isEventExits = true;
        if (isEventExits) {
            //Data
            EventSummaryModel event1 = new EventSummaryModel("Month 01 2015", "Shop & Shop", "Grocery", "Vijayan", "120", "vijayan, senthil, amitesh", "Month 02 2015", accountCurrency);
            EventSummaryModel event2 = new EventSummaryModel("Month 01 2015", "Shop & Shop", "Grocery", "Vijayan", "120", "vijayan, senthil, amitesh", "Month 02 2015", accountCurrency);
            EventSummaryModel event3 = new EventSummaryModel("Month 01 2015", "Shop & Shop", "Grocery", "Vijayan", "120", "vijayan, senthil, amitesh", "Month 02 2015", accountCurrency);
            EventSummaryModel event4 = new EventSummaryModel("Month 01 2015", "Shop & Shop", "Grocery", "Vijayan", "120", "vijayan, senthil, amitesh", "Month 02 2015", accountCurrency);
            EventSummaryModel event5 = new EventSummaryModel("Month 01 2015", "Shop & Shop", "Grocery", "Vijayan", "120", "vijayan, senthil, amitesh", "Month 02 2015", accountCurrency);
            EventSummaryModel event6 = new EventSummaryModel("Month 01 2015", "Shop & Shop", "Grocery", "Vijayan", "120", "vijayan, senthil, amitesh", "Month 02 2015", accountCurrency);
            EventSummaryModel event7 = new EventSummaryModel("Month 01 2015", "Shop & Shop", "Grocery", "Vijayan", "120", "vijayan, senthil, amitesh", "Month 02 2015", accountCurrency);

            EventSummaryModel[] events = {event1, event2, event3, event4, event5, event6, event7};
            EventSummaryAdapter eventSummaryAdapter = new EventSummaryAdapter(this, events);
            recyclerViewAllEvents.setAdapter(eventSummaryAdapter);
        } else {
            textViewNoEventExists.setVisibility(View.VISIBLE);
        }
//************************ Recycler view to display all the available event for an account ****************************
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabtnAddEvent:
                Intent intentAddEventFAB = new Intent(this, AddOREditEventActivity.class);
                intentAddEventFAB.putExtra(Constants.EVENT_ACTION, "ADD");
                intentAddEventFAB.putExtra(Constants.ADD_EVENT_ACTION_SOURCE, "ALLEVENTSFAB");
                intentAddEventFAB.putExtra(Constants.ACCOUNT_NAME, accountName);
                intentAddEventFAB.putExtra(Constants.ACCOUNT_CURRENCY, accountCurrency);
                intentAddEventFAB.putExtra(Constants.ACCOUNT_MEMBERS, accountMembers);
                startActivity(intentAddEventFAB);

                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_events, menu);
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
                NavUtils.navigateUpTo(this, new Intent(this, AllAccountsActivity.class));
                MyUtils.myPendingTransitionLeftInRightOut(this);
                break;

            case R.id.action_report:
                Intent intentReport = new Intent(this, PayoutActivity.class);
                startActivity(intentReport);
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
