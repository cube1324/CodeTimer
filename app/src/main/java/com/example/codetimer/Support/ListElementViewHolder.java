package com.example.codetimer.Support;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.codetimer.R;


public class ListElementViewHolder extends RecyclerView.ViewHolder {
    public TextView name_view;
    public TextView duration_view;
    public TextView repeat_view;
    public ListElementViewHolder(View v){
        super(v);
        name_view = v.findViewById(R.id.name_view);
        duration_view = v.findViewById(R.id.duration_view);
        repeat_view = v.findViewById(R.id.repeat_view);
    }
}
