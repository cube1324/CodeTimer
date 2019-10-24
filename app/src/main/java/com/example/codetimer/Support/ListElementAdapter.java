package com.example.codetimer.Support;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codetimer.R;

import java.util.Collections;
import java.util.LinkedList;

public class ListElementAdapter extends RecyclerView.Adapter<ListElementViewHolder> implements EditDIalog.EditDialogListner {
    public LinkedList<ListElement> elements;
    private FragmentManager mFragementManager;
    public ListElementAdapter(LinkedList<ListElement> initialElements, FragmentManager manager){
        elements = initialElements;
        mFragementManager = manager;
    }

    @Override
    public int getItemViewType(int position) {
        int r;
        switch (elements.get(position).getType()){
            case TIMER:
                r = 0;
                break;
            case LOOPSTART:
                r = 1;
                break;
            case LOOPEND:
                r = 2;
                break;
            default:
                r = 0;
                break;
        }
        return r;
    }

    @NonNull
    @Override
    public ListElementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        View v;
        switch (viewType){
            case 0:
                v = inf.inflate(R.layout.list_element_timer_view, parent, false);
                break;
            case 1:
                v = inf.inflate(R.layout.list_element_loopstart_view, parent, false);
                break;
            case 2:
                v = inf.inflate(R.layout.list_element_loopend_view, parent, false);
                break;
            default:
                v = inf.inflate(R.layout.list_element_timer_view, parent, false);
                break;
        }
        return new ListElementViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListElementViewHolder holder, int position) {
        ListElement currentElement = elements.get(position);

        switch (currentElement.getType()){
            case TIMER:
                holder.duration_view.setText(currentElement.getNumber());
                holder.name_view.setText(currentElement.getName());
                holder.edit_button.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        openDialog(holder);
                    }
                });
                break;
            case LOOPSTART:
                holder.repeat_view.setText(currentElement.getNumber());
                holder.name_view.setText(currentElement.getName());
                break;
            case LOOPEND:
                holder.name_view.setText(currentElement.getName());
        }

    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public void openDialog(ListElementViewHolder holder){
        EditDIalog d = new EditDIalog(holder);
        d.show(mFragementManager, "Text");
    }


    @Override
    public void applyData(String name, ListElementViewHolder holder) {

    }
}
