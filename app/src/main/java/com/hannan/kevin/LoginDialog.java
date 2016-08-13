package com.hannan.kevin;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;

public class LoginDialog extends DialogFragment implements DialogInterface.OnClickListener
 {

     LoginDialogListener activity;

     public interface LoginDialogListener {
        void onClickLogin();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_body)
                .setPositiveButton(R.string.dialog_ok, this);
//                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Intent intent = new Intent(getActivity(), LoginActivity.class);
//                        startActivity(intent);
//                    }
//                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

     @Override
     public void onClick(DialogInterface dialog, int which) {
         activity = (LoginDialogListener) getActivity();
         activity.onClickLogin();
     }
 }