package com.bobcode.splitexpense.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobcode.splitexpense.R;
import com.bobcode.splitexpense.models.MemberDetailModel;
import com.bobcode.splitexpense.utils.MyUtils;

/**
 * Created by vijayananjalees on 4/22/15.
 */
public class MembersDetailsAdapter extends RecyclerView.Adapter<MembersDetailsAdapter.MembersDetailsItemViewHolder> {

    private Context context;

    private MemberDetailModel[] memberDetailModels;

    public MembersDetailsAdapter(Context context, MemberDetailModel[] memberDetailModels) {
        this.context = context;
        this.memberDetailModels = memberDetailModels;
    }

    @Override
    public int getItemCount() {
        return memberDetailModels.length;
    }


    @Override
    public void onBindViewHolder(MembersDetailsItemViewHolder holder, int position) {
        holder.txtViewMembersName.setText(memberDetailModels[position].name);
        holder.txtViewMembersDisplayName.setText(memberDetailModels[position].displayName);
        holder.txtViewMembersComments.setText(memberDetailModels[position].comments);

    }

    @Override
    public MembersDetailsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_member_detail_cardview, parent, false);
        return new MembersDetailsItemViewHolder(itemView, context, memberDetailModels);
    }


    public static class MembersDetailsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView txtViewMembersName;
        protected TextView txtViewMembersDisplayName;
        protected TextView txtViewMembersComments;

        protected ImageView imgDeleteMember;
        protected ImageView imgEditMemberDetail;

        private Context context;

        private String memberName;

        private MemberDetailModel[] memberDetailModels;

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getMemberName() {
            return this.memberName;
        }

        public MembersDetailsItemViewHolder(View itemView, Context context, MemberDetailModel[] memberDetailModels) {
            super(itemView);
            this.context = context;
            this.memberDetailModels = memberDetailModels;

            txtViewMembersName = (TextView) itemView.findViewById(R.id.editTextMemberName);
            txtViewMembersDisplayName = (TextView) itemView.findViewById(R.id.editTextMemberDisplayName);
            txtViewMembersComments = (TextView) itemView.findViewById(R.id.editTextMemberComments);

            imgDeleteMember = (ImageView) itemView.findViewById(R.id.imgDeleteMember);
            imgEditMemberDetail = (ImageView) itemView.findViewById(R.id.imgEditMemberDetail);

            imgDeleteMember.setOnClickListener(this);
            imgEditMemberDetail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int position = getPosition();

            setMemberName(memberDetailModels[position].name);

            switch (v.getId()) {
                case R.id.imgDeleteMember:
                    //AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AboutDialog);
                    builder.setTitle("Delete " + getMemberName() + " ?");
                    builder.setMessage("You'll lose account data");
                    builder.setIcon(R.drawable.ic_dialog_alert);

                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyUtils.showToast(context, getMemberName() + " deleted.......");

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

                    break;

                case R.id.imgEditMemberDetail:
                    MyUtils.showToast(context, "Edit Event Icon clicked");
                    break;
            }
        }
    }


}
