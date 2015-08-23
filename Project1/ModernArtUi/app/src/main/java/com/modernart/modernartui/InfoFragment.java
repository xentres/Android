package com.modernart.modernartui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;

/**
 * Setup the more information dialog displayed within the fragment
 * @author xentres
 */
public class InfoFragment extends DialogFragment {

    // Create all fields
    private Button positiveBtn = null;
    private Button negativeBtn = null;

    /**
     * Setup the Information Dialog Fragment
     * A Dialog Fragment with two TextViews and two Buttons is build
     *
     * @param savedInstanceState Last saved instance state of the Fragment
     *
     * @return Return the Dialog instance
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // start building the AlertDialog
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(getActivity());

        // inflate the dialog layout and set the view
        LayoutInflater inflater = getActivity().getLayoutInflater();
        adBuilder.setView(inflater.inflate(R.layout.moma_dialog, null));

        // implementation of the visit moma button and invoked onClick Method
        adBuilder.setPositiveButton(R.string.visit_now, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.moma.org"));
            startActivity(intent);
            }
        });

        // implementation of the visit not now button and invoked onClick Method
        adBuilder.setNegativeButton(R.string.visit_not_now, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        // finally create the dialog
        return adBuilder.create();
    }

    /**
     * Create the button layout onStart
     */
    public void onStart() {
        super.onStart();
        positiveBtn =  ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
        negativeBtn =  ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE);

        positiveBtn.setBackgroundColor(getResources().getColor(R.color.dialogBG));
        positiveBtn.setTextColor(Color.WHITE);
        negativeBtn.setBackgroundColor(getResources().getColor(R.color.dialogBG));
        negativeBtn.setTextColor(Color.WHITE);
    }
}
