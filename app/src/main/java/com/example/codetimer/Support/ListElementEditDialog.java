package com.example.codetimer.Support;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.example.codetimer.R;

public class ListElementEditDialog extends AppCompatDialogFragment {


    public interface  ListElementEditDialogListener{
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    ListElementEditDialogListener listener;

    public void onAttach(Context context){
        super.onAttach(context);
        try {
            listener = (ListElementEditDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " no ListElementEditDialogListener");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.edit_element_dialog, null);
        builder.setView(v);
        builder.setNegativeButton(R.string.edit_dialog_negativ, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogNegativeClick(ListElementEditDialog.this);
            }
        });
        builder.setPositiveButton(R.string.edit_dialog_positiv, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogPositiveClick(ListElementEditDialog.this);
            }
        });
        return builder.create();
    }
}
