package com.example.codetimer.Support;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.codetimer.R;

public class EditDIalog extends AppCompatDialogFragment {
    private EditText editName;
    private EditDialogListner listner;
    private ListElementViewHolder holder;

    public EditDIalog(ListElementViewHolder holder){
        super();
        this.holder = holder;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listner = (EditDialogListner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement EditDialogListner");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.edit_element_dialog, null);

        builder.setView(view);
        builder.setTitle("Login");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //empty
            }
        });

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editName.getText().toString();
                listner.applyData(name, holder);
            }
        });

        editName = view.findViewById(R.id.editText);
        return builder.create();
    }

    public interface EditDialogListner{
        void applyData(String name, ListElementViewHolder holder);
    }
}

