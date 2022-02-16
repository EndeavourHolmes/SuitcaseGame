package com.example.koffer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class RequestDeleteRanking extends AppCompatDialogFragment {
    private RequestDeleteRankingListener listener;

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.layout_ranking_popup, null);

        builder.setView(v)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.applyDelete();

                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (RequestDeleteRankingListener)context;
        } catch (ClassCastException e) {
             throw new ClassCastException(context.toString() + "must implement RequestDeleteRankingListener");
        }
    }

    public interface RequestDeleteRankingListener{
        void applyDelete();
    }

}
