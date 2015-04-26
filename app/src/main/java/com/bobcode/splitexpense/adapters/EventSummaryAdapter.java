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

/**
 * Created by vijayananjalees on 4/21/15.
 */
public class EventSummaryAdapter extends RecyclerView.Adapter<EventSummaryAdapter.EventSummaryItemViewHolder> {


    private EventSummaryModel[] eventSummaryModels;

    private Context context;

    public EventSummaryAdapter(Context context, EventSummaryModel[] eventSummaryModels) {
        this.context = context;
        this.eventSummaryModels = eventSummaryModels;
    }

    @Override
    public int getItemCount() {
        return eventSummaryModels.length;
    }

    @Override
    public void onBindViewHolder(EventSummaryItemViewHolder holder, int position) {

        holder.txtViewAEEventDate.setText(eventSummaryModels[position].eventDate);

        holder.txtViewAEDescription.setText(eventSummaryModels[position].description);

        holder.txtViewAECategory.setText(eventSummaryModels[position].category);

        holder.txtViewAEWhoPaid.setText(eventSummaryModels[position].whoPaid);

        //set the currency icon based on the particular account currency
        String imgName = null;
        String currency = eventSummaryModels[position].currency;
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
        holder.txtViewAEAmount.setText(eventSummaryModels[position].amount);

        holder.txtViewAEForWhom.setText(eventSummaryModels[position].forWhom);

        holder.txtViewAECreatedOn.setText(eventSummaryModels[position].createdOn);
    }


    @Override
    public EventSummaryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_event_details_cardview, parent, false);
        return new EventSummaryItemViewHolder(itemView, context, eventSummaryModels);
    }


    public static class EventSummaryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
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

        private EventSummaryModel[] eventSummaryModels;

        public EventSummaryItemViewHolder(View itemView, Context context, EventSummaryModel[] eventSummaryModels) {
            super(itemView);
            this.context = context;
            this.eventSummaryModels = eventSummaryModels;

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
            switch (v.getId()) {
                case R.id.imgDeleteEvent:
                    deleteEventDialog();

                    break;

                case R.id.imgEditEventDetail:
                    Intent intentEditEventIcon = new Intent(context.getApplicationContext(), AddOREditEventActivity.class);
                    intentEditEventIcon.putExtra(Constants.EVENT_ACTION, "EDIT");
                    context.startActivity(intentEditEventIcon);
                    MyUtils.myPendingTransitionRightInLeftOut((Activity) context);
                    break;
            }

        }

        @Override
        public boolean onLongClick(View v) {
            deleteEventDialog();

            //return false -- will trigger the single click event as well
            //return true -- will not trigger the single click event
            return true;
        }

        public void deleteEventDialog(){
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AboutDialog);
            builder.setTitle("Delete the event? ");
            builder.setMessage("You'll lose the data and it may not be recovered");
            builder.setIcon(R.drawable.ic_dialog_alert);

            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MyUtils.showToast(context, "Event deleted.......");
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
    }

}
