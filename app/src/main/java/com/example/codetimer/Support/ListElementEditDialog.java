package com.example.codetimer.Support;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.example.codetimer.R;

import java.text.Normalizer;

public class ListElementEditDialog extends AppCompatDialogFragment {
    private NumberPicker hour_picker;
    private NumberPicker seconds_picker;
    public EditText edit_name;
    public int hour;
    public int seconds;

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
        hour_picker = v.findViewById(R.id.hour_picker);
        seconds_picker = v.findViewById(R.id.seconds_picker);
        edit_name = v.findViewById(R.id.edit_name);

        hour_picker.setMaxValue(60);
        hour_picker.setMinValue(0);
        hour_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                hour = newVal;
            }
        });
        hour_picker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

        seconds_picker.setMaxValue(60);
        seconds_picker.setMinValue(0);
        seconds_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                seconds = newVal;
            }
        });
        seconds_picker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });




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
