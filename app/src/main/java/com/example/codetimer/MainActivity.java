package com.example.codetimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.codetimer.Support.ItemType;
import com.example.codetimer.Support.ListElement;
import com.example.codetimer.Support.ListElementAdapter;

import java.util.Collections;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ListElementAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private LinkedList<ListElement> elements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        elements = new LinkedList<>();
        ListElement end = new ListElement("LOOPEND", ItemType.LOOPEND);
        ListElement temp = new ListElement("LOOP", ItemType.LOOPSTART);
        temp.setNumber(3);
        temp.setEndElement(end);
        elements.add(temp);
        elements.add(new ListElement("NiceTimer", ItemType.TIMER));
        elements.add(new ListElement("NiceTimer", ItemType.TIMER));

        elements.add(end);

        mAdapter = new ListElementAdapter(elements);
        recyclerView.setAdapter(mAdapter);


        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP,0){
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int pos_dragged = viewHolder.getAdapterPosition();
                int pos_target = target.getAdapterPosition();

                if (elements.get(pos_dragged).getEndElement() == elements.get(pos_target)){
                    Log.v("Overstepped Boudnds","AWPDOJAWDOPAJWDAPWODJAWD");
                }
                //Swap Items
                Collections.swap(elements, pos_dragged, pos_target);


                mAdapter.notifyItemMoved(pos_dragged, pos_target);
                Log.v("ItemTouchHelper", elements.toString());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        helper.attachToRecyclerView(recyclerView);

        RecyclerView.ItemDecoration margin = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = 20;
                outRect.left = 40 * elements.get(parent.getChildAdapterPosition(view)).getDepth();
                outRect.right = 20;
                if (parent.getChildAdapterPosition(view) == 0){
                    outRect.top = 20;
                }
            }
        };
        recyclerView.addItemDecoration(margin);
    }
}
