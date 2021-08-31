package com.fall.crazyfall.game;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class MyDialogFragment extends DialogFragment {
    GameActivity activity;
    int level;

    public MyDialogFragment(GameActivity activity , int level) {
        this.activity = activity;
        this.level = level;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle("well done ! ")
                .setMessage("you pass " + level + " level , next . . . ")
                .setPositiveButton("ОК", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(false);
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        activity.isWin = false;
        super.onCancel(dialog);
    }
}
