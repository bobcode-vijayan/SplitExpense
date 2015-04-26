package com.bobcode.splitexpense.helpers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.bobcode.splitexpense.R;
import com.bobcode.splitexpense.constants.Constants;

/**
 * Created by vijayananjalees on 4/20/15.
 */
public class ChooseCategoryDialogFragment extends DialogFragment {

    private EditText editTxtAEChooseCategory;

    private String[] category;

    private int lastChosenCategory;

    private String categoryChosen;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        category = getResources().getStringArray(R.array.chooseCategory);

        //this is to handle if user change the screen orientation
        if (savedInstanceState != null)
            lastChosenCategory = savedInstanceState.getInt(Constants.LAST_CHOSEN_CATEGORY);
        else {
            //read the last chosen category from the preference
            //and set the value to lastChosenCategory

            lastChosenCategory = 0;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Category")

                .setSingleChoiceItems(category, lastChosenCategory, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lastChosenCategory = which;
                    }
                })

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        categoryChosen = category[lastChosenCategory].toString();

                        editTxtAEChooseCategory = (EditText) getActivity().findViewById(R.id.editTxtAEEChooseCategory);
                        editTxtAEChooseCategory.setText(categoryChosen);

                        //save the chosen category to preferences
                        //this will be used to populate the default selection in choose category
                        // with the last chosen category

                    }
                })

                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btnOK = alert.getButton(Dialog.BUTTON_POSITIVE);
                btnOK.setTextSize(18);

                Button btnCancel = alert.getButton(Dialog.BUTTON_NEGATIVE);
                btnCancel.setTextSize(18);

                ListView textView = alert.getListView();
            }
        });

        return alert;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.LAST_CHOSEN_CATEGORY, lastChosenCategory);
        super.onSaveInstanceState(outState);
    }


}
