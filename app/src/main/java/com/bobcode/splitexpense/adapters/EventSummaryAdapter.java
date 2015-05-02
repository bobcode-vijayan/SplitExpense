package com.bobcode.splitexpense.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobcode.splitexpense.R;
import com.bobcode.splitexpense.activities.AddOREditEventActivity;
import com.bobcode.splitexpense.constants.Constants;
import com.bobcode.splitexpense.models.EventSummaryModel;
import com.bobcode.splitexpense.utils.MyUtils;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vijayananjalees on 4/21/15.
 */
public class EventSummaryAdapter extends RecyclerView.Adapter<EventSummaryAdapter.EventSummaryItemViewHolder> {

    private ArrayList<EventSummaryModel> eventSummaryModelList;

    private HashMap<String, String> selectedAccountDetails;

    private EventSummaryModel eventSummaryModel;

    private Context context;

    private Activity activity;

    public EventSummaryAdapter(Context context, ArrayList<EventSummaryModel> eventSummaryModelList, HashMap<String, String> selectedAccountDetails) {
        this.context = context;
        this.activity = (Activity) context;
        this.eventSummaryModelList = eventSummaryModelList;
        this.selectedAccountDetails = selectedAccountDetails;
    }

    @Override
    public int getItemCount() {
        return eventSummaryModelList.size();
    }

    public void remove(int position) {
        eventSummaryModelList.remove(position);
        notifyItemRemoved(position);

        //This is to ensure floating action button will not hide away
        FloatingActionButton floatingActionButton = (FloatingActionButton)activity.findViewById(R.id.fabtnAddEvent);
        floatingActionButton.show();
    }


    @Override
    public void onBindViewHolder(EventSummaryItemViewHolder holder, int position) {
        eventSummaryModel = eventSummaryModelList.get(position);

        holder.txtViewAEEventDate.setText(eventSummaryModel.eventDate);
        holder.txtViewAEDescription.setText(eventSummaryModel.description);
        holder.txtViewAECategory.setText(eventSummaryModel.category);
        holder.txtViewAEWhoPaid.setText(eventSummaryModel.whoPaid);

        //set the currency icon based on the particular account currency
        String imgName = null;
        String currency = eventSummaryModel.currency;
        if (currency != null) {
            currency = currency.toLowerCase();
            if (currency.equals("us dollar")) {
                imgName = "ic_currency_dollar";
            } else if (currency.equals("canadian dollar")) {
                imgName = "ic_currency_dollar";
            } else if (currency.equals("british pound")) {
                imgName = "ic_currency_pound";
            } else if (currency.equals("indian rupee")) {
                imgName = "ic_currency_indian_rupee";
            } else if (currency.equals("euro")) {
                imgName = "ic_currency_euro";
            } else if (currency.equals("australian dollar")) {
                imgName = "ic_currency_dollar";
            }
            holder.aeCurrencyImage.setImageResource(context.getResources().getIdentifier(imgName, "drawable", context.getPackageName()));
        }
        holder.txtViewAEAmount.setText(eventSummaryModel.amount);
        holder.txtViewAEForWhom.setText(eventSummaryModel.forWhom);
        holder.txtViewAECreatedOn.setText(eventSummaryModel.createdOn);
    }

    @Override
    public EventSummaryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_event_details_cardview, parent, false);
        return new EventSummaryItemViewHolder(itemView, context, eventSummaryModelList);
    }

    public class EventSummaryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        protected TextView txtViewAEEventDate;
        protected TextView txtViewAEDescription;
        protected TextView txtViewAECategory;
        protected TextView txtViewAEWhoPaid;
        protected TextView txtViewAEAmount;
        protected TextView txtViewAECreatedOn;
        protected TextView txtViewAEForWhom;

        protected ImageView aeCurrencyImage;

        protected ImageView imgDeleteEvent;
        protected ImageView imgEditEventDetail;

        private Context context;

        private ArrayList<EventSummaryModel> eventSummaryModelList;

        private EventSummaryModel eventSummaryModel;

        public EventSummaryItemViewHolder(View itemView, Context context, ArrayList<EventSummaryModel> eventSummaryModelList) {
            super(itemView);

            this.context = context;
            this.eventSummaryModelList = eventSummaryModelList;

            txtViewAEEventDate = (TextView) itemView.findViewById(R.id.txtViewAEEventDate);
            txtViewAEDescription = (TextView) itemView.findViewById(R.id.txtViewAEDescription);
            txtViewAECategory = (TextView) itemView.findViewById(R.id.txtViewAECategory);
            txtViewAEWhoPaid = (TextView) itemView.findViewById(R.id.txtViewAEWhoPaid);
            txtViewAEAmount = (TextView) itemView.findViewById(R.id.txtViewAEAmount);
            txtViewAECreatedOn = (TextView) itemView.findViewById(R.id.txtViewAECreatedOn);
            txtViewAEForWhom = (TextView) itemView.findViewById(R.id.txtViewAEForWhom);

            aeCurrencyImage = (ImageView) itemView.findViewById(R.id.aeCurrencyImage);

            imgDeleteEvent = (ImageView) itemView.findViewById(R.id.imgDeleteEvent);
            imgEditEventDetail = (ImageView) itemView.findViewById(R.id.imgEditEventDetail);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            imgDeleteEvent.setOnClickListener(this);
            imgEditEventDetail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int position = getPosition();
            eventSummaryModel = eventSummaryModelList.get(position);

            switch (v.getId()) {
                case R.id.imgDeleteEvent:
                    deleteEventDialog(eventSummaryModel.eventID);

                    break;

                case R.id.imgEditEventDetail:
                    String accountName = selectedAccountDetails.get(Constants.ACCOUNT_NAME);
                    String accountCurrency = selectedAccountDetails.get(Constants.ACCOUNT_CURRENCY);
                    String accountMembers = selectedAccountDetails.get(Constants.ACCOUNT_MEMBERS);

                    Intent intentEditEventIcon = new Intent(context.getApplicationContext(), AddOREditEventActivity.class);
                    intentEditEventIcon.putExtra(Constants.EVENT_ACTION, "EDIT");

                    //Account level data
                    intentEditEventIcon.putExtra(Constants.ACCOUNT_NAME, accountName);
                    intentEditEventIcon.putExtra(Constants.ACCOUNT_CURRENCY, accountCurrency);
                    intentEditEventIcon.putExtra(Constants.ACCOUNT_MEMBERS, accountMembers);

                    //Event level data
                    intentEditEventIcon.putExtra(Constants.EDIT_EVENT_DATE, eventSummaryModel.eventDate);
                    intentEditEventIcon.putExtra(Constants.EDIT_EVENT_DESCRIPTION, eventSummaryModel.description);
                    intentEditEventIcon.putExtra(Constants.EDIT_EVENT_CATEGORY, eventSummaryModel.category);
                    intentEditEventIcon.putExtra(Constants.EDIT_EVENT_WHO_PAID, eventSummaryModel.whoPaid);
                    intentEditEventIcon.putExtra(Constants.EDIT_EVENT_AMOUNT, eventSummaryModel.amount);
                    intentEditEventIcon.putExtra(Constants.EDIT_EVENT_FOR_WHOM, eventSummaryModel.forWhom);

                    context.startActivity(intentEditEventIcon);
                    MyUtils.myPendingTransitionRightInLeftOut((Activity) context);

                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            final int position = getPosition();
            eventSummaryModel = eventSummaryModelList.get(position);
            deleteEventDialog(eventSummaryModel.eventID);

            //return false -- will trigger the single click event as well
            //return true -- will not trigger the single click event
            return true;
        }

        public void deleteEventDialog(final String eventID){
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AboutDialog);
            builder.setTitle("Delete the event? ");
            builder.setMessage("You'll lose the data and it may not be recovered");
            builder.setIcon(R.drawable.ic_dialog_alert);

            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteEventFromTable(eventID);
                }
            });

            builder.setNegativeButton("No, Thanks", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //do nothing
                }
            });

            AlertDialog dialog = builder.show();
            dialog.show();
            Button btnPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button btnNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            btnPositive.setTextSize(16);
            btnPositive.setTextColor(context.getResources().getColor(R.color.primary));
            btnNegative.setTextSize(16);
            btnNegative.setTextColor(context.getResources().getColor(R.color.primary));
        }

        public void deleteEventFromTable(String eventID){
            MyUtils.showToast(context, "Event " + eventID + " deleted.......");

            remove(getPosition());
        }
    }
}
