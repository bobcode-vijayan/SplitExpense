package com.bobcode.splitexpense.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobcode.splitexpense.R;
import com.bobcode.splitexpense.activities.AddOREditAccountActivity;
import com.bobcode.splitexpense.activities.AddOREditEventActivity;
import com.bobcode.splitexpense.activities.AllEventsActivity;
import com.bobcode.splitexpense.constants.Constants;
import com.bobcode.splitexpense.models.AccountSummaryModel;
import com.bobcode.splitexpense.utils.MyUtils;

/**
 * Created by vijayananjalees on 4/17/15.
 */
public class AccountSummaryAdapter extends RecyclerView.Adapter<AccountSummaryAdapter.AccountSummaryAdapterViewHolder> {

//    private List<AccountSummaryModel>  accountSummaryModelList;
//    public AccountSummaryAdapter(List<AccountSummaryModel> accountSummaryModelList) {
//        this.accountSummaryModelList = accountSummaryModelList;
//    }

    private AccountSummaryModel[] accountSummaryModels;
    private AccountSummaryModel accountSummaryModel;

    private Context context;

    public AccountSummaryAdapter(Context contexts, AccountSummaryModel[] accountSummaryModels) {
        this.context = contexts;
        this.accountSummaryModels = accountSummaryModels;
    }

    @Override
    public int getItemCount() {
        //return accountSummaryModelList.size();
        return accountSummaryModels.length;
    }

    @Override
    public void onBindViewHolder(AccountSummaryAdapterViewHolder holder, int position) {
        //AccountSummaryModel accountSummaryModel = accountSummaryModelList.get(position);

        accountSummaryModel = accountSummaryModels[position];
        holder.accountName.setText(accountSummaryModel.accountName);
        holder.members.setText(accountSummaryModel.members);
        holder.description.setText(accountSummaryModel.description);
        holder.createdOn.setText(accountSummaryModel.createdOn);
        holder.noOfExpenses.setText(accountSummaryModel.noOfExpenses + "");
        holder.totalSpend.setText(accountSummaryModel.totalSpend + "");

        //set the currency icon based on the particular account currency
        String imgName = null;
        String currency = accountSummaryModel.currency;
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
            holder.currencyImage.setImageResource(context.getResources().getIdentifier(imgName, "drawable", context.getPackageName()));
        }

        String currentAccountStatus = accountSummaryModel.status;
        if (currentAccountStatus.toLowerCase().equals("settled")) {
            holder.expenseStatus.setTextColor(context.getResources().getColor(R.color.greenPrimaryDark));
            holder.expenseStatus.setTypeface(holder.expenseStatus.getTypeface(), Typeface.BOLD_ITALIC);
        } else if (currentAccountStatus.toLowerCase().equals("pending")) {
            holder.expenseStatus.setTextColor(context.getResources().getColor(R.color.accent));
            holder.expenseStatus.setTypeface(holder.expenseStatus.getTypeface(), Typeface.BOLD_ITALIC);
        }

        holder.expenseStatus.setText(accountSummaryModel.status);
    }

    @Override
    public AccountSummaryAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_account_detail_cardview, parent, false);
        return new AccountSummaryAdapterViewHolder(itemView, context, accountSummaryModels);
    }


    public static class AccountSummaryAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        protected TextView accountName;
        protected TextView members;
        protected TextView description;
        protected TextView createdOn;
        protected TextView noOfExpenses;

        protected ImageView currencyImage;
        protected TextView totalSpend;

        protected TextView expenseStatus;

        protected ImageView imgAddEvent;
        protected ImageView imgAllEvents;
        protected ImageView imgEditAccountDetail;
        protected ImageView imgDeleteAccount;

        private Context context;

        private AccountSummaryModel[] accountSummaryModels;
        private AccountSummaryModel accountSummaryModel;

        public AccountSummaryAdapterViewHolder(View itemView, Context context, AccountSummaryModel[] accountSummaryModels) {
            super(itemView);
            this.context = context;
            this.accountSummaryModels = accountSummaryModels;

            accountName = (TextView) itemView.findViewById(R.id.allAccountAccountName);
            members = (TextView) itemView.findViewById(R.id.allAccountMembers);
            description = (TextView) itemView.findViewById(R.id.allAccountDescription);
            createdOn = (TextView) itemView.findViewById(R.id.allAccountCreatedOn);
            noOfExpenses = (TextView) itemView.findViewById(R.id.allAccountNoOfExpenses);

            currencyImage = (ImageView) itemView.findViewById(R.id.currencyImage);
            totalSpend = (TextView) itemView.findViewById(R.id.allAccountTotalSpend);

            expenseStatus = (TextView) itemView.findViewById(R.id.allAccountExpenseStatus);

            imgAddEvent = (ImageView) itemView.findViewById(R.id.imgAddEvent);
            imgAllEvents = (ImageView) itemView.findViewById(R.id.imgAllEvents);
            imgEditAccountDetail = (ImageView) itemView.findViewById(R.id.imgEditAccountDetail);
            imgDeleteAccount = (ImageView) itemView.findViewById(R.id.imgDeleteAccount);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            imgAddEvent.setOnClickListener(this);
            imgAllEvents.setOnClickListener(this);
            imgEditAccountDetail.setOnClickListener(this);
            imgDeleteAccount.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String message = null;

            final int position = getPosition();
            accountSummaryModel = accountSummaryModels[position];
            final String clickedAccountName = accountSummaryModel.accountName;
            final String clickedAccountDescription = accountSummaryModel.description;
            final String clickedAccountCurrency = accountSummaryModel.currency;
            final String clickedAccountMembers = accountSummaryModel.members;
            final String clickedAccountStatus = accountSummaryModel.status;

            switch (v.getId()) {
                case R.id.imgAddEvent:
                    //First icon on the individual account detail
                    message = "adding event";

                    Intent intentAddEventIcon = new Intent(context.getApplicationContext(), AddOREditEventActivity.class);
                    intentAddEventIcon.putExtra(Constants.EVENT_ACTION, "ADD");
                    intentAddEventIcon.putExtra(Constants.ADD_EVENT_ACTION_SOURCE, "ACCOUNTDETAIL");
                    intentAddEventIcon.putExtra(Constants.ACCOUNT_NAME, clickedAccountName);
                    intentAddEventIcon.putExtra(Constants.ACCOUNT_CURRENCY, clickedAccountCurrency);
                    intentAddEventIcon.putExtra(Constants.ACCOUNT_MEMBERS, clickedAccountMembers);
                    context.startActivity(intentAddEventIcon);

                    break;

                case R.id.imgAllEvents:
                    //Second icon on the individual account detail
                    message = " : viewing all event";

                    Intent intentAllEventIcon = new Intent(context.getApplicationContext(), AllEventsActivity.class);
                    intentAllEventIcon.putExtra(Constants.ACCOUNT_NAME, clickedAccountName);
                    intentAllEventIcon.putExtra(Constants.ACCOUNT_MEMBERS, clickedAccountMembers);
                    intentAllEventIcon.putExtra(Constants.ACCOUNT_CURRENCY, clickedAccountCurrency);
                    intentAllEventIcon.putExtra(Constants.ACCOUNT_STATUS, clickedAccountStatus);
                    context.startActivity(intentAllEventIcon);

                    break;

                case R.id.imgEditAccountDetail:
                    //third icon on the individual account
                    //click on edit account icon will take the user to edit the existing account detail
                    message = " : editing existing account details";

                    Intent intentEditAccountIcon = new Intent(context.getApplicationContext(), AddOREditAccountActivity.class);
                    intentEditAccountIcon.putExtra(Constants.ACCOUNT_ACTION, "EDIT");
                    intentEditAccountIcon.putExtra(Constants.ACCOUNT_NAME, clickedAccountName);
                    intentEditAccountIcon.putExtra(Constants.ACCOUNT_MEMBERS, clickedAccountMembers);
                    intentEditAccountIcon.putExtra(Constants.ACCOUNT_DESCRIPTION, clickedAccountDescription);
                    intentEditAccountIcon.putExtra(Constants.ACCOUNT_CURRENCY, clickedAccountCurrency);
                    intentEditAccountIcon.putExtra(Constants.ACCOUNT_STATUS, clickedAccountStatus);
                    context.startActivity(intentEditAccountIcon);

                    break;

                case R.id.imgDeleteAccount:
                    //fourth icon on the individual account
                    //click on delete account icon will show the alert dialog to user
                    // and delete the clicked account based on user input

                    deleteAccountDialog(clickedAccountName);
                    break;

                default:
                    //single press will take the user to All Event of the clicked account
                    message = " : all events";

                    Intent intentAllEventSinglePress = new Intent(context.getApplicationContext(), AllEventsActivity.class);
                    intentAllEventSinglePress.putExtra(Constants.ACCOUNT_NAME, clickedAccountName);
                    intentAllEventSinglePress.putExtra(Constants.ACCOUNT_CURRENCY, clickedAccountCurrency);
                    intentAllEventSinglePress.putExtra(Constants.ACCOUNT_MEMBERS, clickedAccountMembers);
                    intentAllEventSinglePress.putExtra(Constants.ACCOUNT_STATUS, clickedAccountStatus);

                    context.startActivity(intentAllEventSinglePress);

                    break;

            }
            if (message != null)
                MyUtils.showToast(context.getApplicationContext(), "Account Name : " + clickedAccountName + message);

            MyUtils.myPendingTransitionRightInLeftOut((Activity) context);
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getPosition();
            String clickedAccountName = accountSummaryModels[position].accountName;

            deleteAccountDialog(clickedAccountName);

            //return false -- will trigger the single click event as well
            //return true -- will not trigger the single click event
            return true;
        }

        public void deleteAccountDialog(final String clickedAccountName) {
            //AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog);
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AboutDialog);
            builder.setTitle("Delete " + clickedAccountName + " Account? ");
            builder.setMessage("You'll lose account data");
            builder.setIcon(R.drawable.ic_dialog_alert);

            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteAccountFromTable(clickedAccountName);
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

        public void deleteAccountFromTable(String clickedAccountName){
//------------------------------- Pending -------------------------------------------------------
            MyUtils.showToast(context, "account " + clickedAccountName +" deleted.......");
        }

    }
}
