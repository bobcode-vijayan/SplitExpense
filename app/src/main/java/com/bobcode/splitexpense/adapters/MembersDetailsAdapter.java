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
import com.bobcode.splitexpense.activities.AddOREditMemberActivity;
import com.bobcode.splitexpense.constants.Constants;
import com.bobcode.splitexpense.helpers.SplitExpenseSQLiteHelper;
import com.bobcode.splitexpense.models.MemberDetailModel;
import com.bobcode.splitexpense.utils.MyUtils;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by vijayananjalees on 4/22/15.
 */
public class MembersDetailsAdapter extends RecyclerView.Adapter<MembersDetailsAdapter.MembersDetailsItemViewHolder> {

    private Context context;

    private Activity activity;

    private ArrayList<MemberDetailModel> memberDetailModelArrayList;

    private MemberDetailModel memberDetailModel;

    public MembersDetailsAdapter(Context context, ArrayList<MemberDetailModel> memberDetailModelArrayList) {
        this.context = context;
        this.activity = (Activity) context;
        this.memberDetailModelArrayList = memberDetailModelArrayList;
    }

    @Override
    public int getItemCount() {
        return memberDetailModelArrayList.size();
    }

    public void remove(int position) {
        memberDetailModelArrayList.remove(position);
        notifyItemRemoved(position);

        //This is to ensure floating action button will not hide away
        FloatingActionButton floatingActionButton = (FloatingActionButton)activity.findViewById(R.id.fabtnAddMember);
        floatingActionButton.show();

        //This is to display a no member exists message if no member exits
        if(memberDetailModelArrayList.size()==0){
            Activity activity = (Activity) context;
            TextView textViewNoMemberExists = (TextView) activity.findViewById(R.id.textViewNoMemberExists);
            textViewNoMemberExists.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBindViewHolder(MembersDetailsItemViewHolder holder, int position) {
        memberDetailModel = memberDetailModelArrayList.get(position);

        holder.imgViewMemberPhoto.setImageBitmap(memberDetailModel.photo);
        holder.txtViewMembersName.setText(memberDetailModel.name);
        holder.txtViewMembersDisplayName.setText(memberDetailModel.displayName);
        holder.txtViewMembersComments.setText(memberDetailModel.comments);
    }

    @Override
    public MembersDetailsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_member_detail_cardview, parent, false);
        return new MembersDetailsItemViewHolder(itemView, context, memberDetailModelArrayList);
    }

    public class MembersDetailsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        protected ImageView imgViewMemberPhoto;
        protected TextView txtViewMembersName;
        protected TextView txtViewMembersDisplayName;
        protected TextView txtViewMembersComments;

        protected ImageView imgDeleteMember;
        protected ImageView imgEditMemberDetail;

        private Context context;

        private String memberName;

        private ArrayList<MemberDetailModel> memberDetailModelArrayList;

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getMemberName() {
            return this.memberName;
        }

        public MembersDetailsItemViewHolder(View itemView, Context context, ArrayList<MemberDetailModel> memberDetailModelArrayList) {
            super(itemView);
            this.context = context;
            this.memberDetailModelArrayList = memberDetailModelArrayList;

            imgViewMemberPhoto = (ImageView) itemView.findViewById(R.id.imgViewMemberPhoto);
            txtViewMembersName = (TextView) itemView.findViewById(R.id.editTextMemberName);
            txtViewMembersDisplayName = (TextView) itemView.findViewById(R.id.editTextMemberDisplayName);
            txtViewMembersComments = (TextView) itemView.findViewById(R.id.editTextMemberComments);

            imgDeleteMember = (ImageView) itemView.findViewById(R.id.imgDeleteMember);
            imgEditMemberDetail = (ImageView) itemView.findViewById(R.id.imgEditMemberDetail);

            imgDeleteMember.setOnClickListener(this);
            imgEditMemberDetail.setOnClickListener(this);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int position = getPosition();
            setMemberName(memberDetailModelArrayList.get(position).name);

            switch (v.getId()) {
                case R.id.imgDeleteMember:
                    //AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog);
                    deleteMemberDialog();

                    break;

                case R.id.imgEditMemberDetail:
                    Intent intentEditMember = new Intent(context, AddOREditMemberActivity.class);
                    intentEditMember.putExtra(Constants.MEMBER_PHOTO, MyUtils.getBytes(memberDetailModelArrayList.get(position).photo));
                    intentEditMember.putExtra(Constants.MEMBER_ACTION, "Edit");
                    intentEditMember.putExtra(Constants.MEMBER_NAME, memberDetailModelArrayList.get(position).name);
                    intentEditMember.putExtra(Constants.MEMBER_DISPLAY_NAME, memberDetailModelArrayList.get(position).displayName);
                    intentEditMember.putExtra(Constants.MEMBER_COMMENTS, memberDetailModelArrayList.get(position).comments);
                    context.startActivity(intentEditMember);
                    MyUtils.myPendingTransitionRightInLeftOut((Activity) context);

                    break;
            }
        }

        public void deleteMemberDialog(){
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AboutDialog);
            builder.setTitle("Delete " + getMemberName() + " ?");
            builder.setMessage("You'll lose account data");
            builder.setIcon(R.drawable.ic_dialog_alert);

            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteMemberFromTable(getMemberName());
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

        @Override
        public boolean onLongClick(View v) {
            final int position = getPosition();
            setMemberName(memberDetailModelArrayList.get(position).name);

            deleteMemberDialog();
            return true;
        }

        public void deleteMemberFromTable(String memberName){
            try{
                //remove the user from recycler view list
                remove(getPosition());

                //delete the user from table
                SplitExpenseSQLiteHelper splitExpenseSQLiteHelper = new SplitExpenseSQLiteHelper(context);
                splitExpenseSQLiteHelper.deleteAMember(memberName);

                MyUtils.showToast(context, memberName + " deleted successfully");
            }catch (Exception e){
                MyUtils.showToast(context, "error deleting member");
                e.printStackTrace();
            }
        }
    }
}
