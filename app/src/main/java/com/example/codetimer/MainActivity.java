package com.example.codetimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.codetimer.Support.ItemType;
import com.example.codetimer.Support.ListElement;
import com.example.codetimer.Support.ListElementAdapter;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ListElementAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private LinkedList<ListElement> elements;

    private static int INDENT_INCEREMNT = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        elements = new LinkedList<>();
        elements.add(new ListElement("NiceTimer", ItemType.TIMER));
        elements.add(new ListElement("NiceTimer", ItemType.TIMER));

        ListElement end = new ListElement("LOOPEND", ItemType.LOOPEND);
        ListElement temp = new ListElement("LOOP", ItemType.LOOPSTART);

        temp.setNumber(3);
        temp.setrelatedElement(end);
        end.setrelatedElement(temp);
        elements.add(temp);
        elements.add(end);

        ListElement loop = new ListElement("Reapeat", ItemType.LOOPSTART);
        ListElement loopend = new ListElement("End Reapeat", ItemType.LOOPEND);
        loop.setrelatedElement(loopend);
        loopend.setrelatedElement(loop);
        loop.setNumber(10);
        elements.add(loop);
        elements.add(loopend);

        mAdapter = new ListElementAdapter(elements);
        recyclerView.setAdapter(mAdapter);


        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP,0){
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //Element that is moved
                int pos_dragged = viewHolder.getAdapterPosition();
                //Element that gets switched
                int pos_target = target.getAdapterPosition();

                //If moved Element ist a LOOPSTART
                if (elements.get(pos_dragged).getType() == ItemType.LOOPSTART){
                    //MAKE STARTLOOPS NOT FUCK THEM SELF
                    if (elements.get(pos_dragged).getrelatedElement() == elements.get(pos_target)) {
                        //If its at the Bottom
                        if (elements.size() - 1 == pos_target) {
                            return true;
                        }
                        //Check indent
                        if (elements.get(pos_target + 1).getType() == ItemType.LOOPSTART){
                            elements.get(pos_target).incDepthBy(INDENT_INCEREMNT);
                        }
                        if (elements.get(pos_target + 1).getType() == ItemType.LOOPEND){
                            elements.get(pos_target).incDepthBy(-INDENT_INCEREMNT);
                        }


                        //Don't let the loop fuck up
                        Collections.swap(elements, pos_target, pos_target + 1);
                        mAdapter.notifyItemMoved(pos_target, pos_target + 1);
                    //Fix indent of Elements
                    } else {
                        if (pos_dragged < pos_target){
                            elements.get(pos_target).incDepthBy(-INDENT_INCEREMNT);
                        } else {
                            elements.get(pos_target).incDepthBy(INDENT_INCEREMNT);
                        }
                    }


                }
                //If Element is LOOPEND
                if (elements.get(pos_dragged).getType() == ItemType.LOOPEND){
                    //Make Loop not fuck itself
                    if (elements.get(pos_dragged).getrelatedElement() == elements.get(pos_target)) {
                        if (pos_target == 0) {
                            return true;
                        }
                        //CHeck Indent
                        //Check indent
                        if (elements.get(pos_target - 1).getType() == ItemType.LOOPSTART){
                            elements.get(pos_target).incDepthBy(-INDENT_INCEREMNT);
                        }
                        if (elements.get(pos_target - 1).getType() == ItemType.LOOPEND){
                            elements.get(pos_target).incDepthBy(INDENT_INCEREMNT);
                        }

                        //Push Loopstart up
                        Collections.swap(elements, pos_target, pos_target - 1);
                        mAdapter.notifyItemMoved(pos_target, pos_target - 1);

                    //Fix Element indent
                    } else {
                        if (pos_dragged < pos_target){
                            elements.get(pos_target).incDepthBy(INDENT_INCEREMNT);
                        } else {
                            elements.get(pos_target).incDepthBy(-INDENT_INCEREMNT);
                        }
                    }
                }

                //Fix Element indent
                if (elements.get(pos_target).getType() == ItemType.LOOPSTART){
                    if (pos_target > pos_dragged){
                        elements.get(pos_dragged).incDepthBy(INDENT_INCEREMNT);
                    } else {
                        elements.get(pos_dragged).incDepthBy(-INDENT_INCEREMNT);
                    }
                }

                if (elements.get(pos_target).getType() == ItemType.LOOPEND){
                    if (pos_target > pos_dragged){
                        elements.get(pos_dragged).incDepthBy(-INDENT_INCEREMNT);
                    } else {
                        elements.get(pos_dragged).incDepthBy(INDENT_INCEREMNT);
                    }
                }


                //Swap Items
                Collections.swap(elements, pos_dragged, pos_target);
                mAdapter.notifyItemMoved(pos_dragged, pos_target);
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
                outRect.left = 20 * elements.get(parent.getChildAdapterPosition(view)).getDepth();
                outRect.right = 20;
                if (parent.getChildAdapterPosition(view) == 0){
                    outRect.top = 20;
                }
            }
        };
        recyclerView.addItemDecoration(margin);
    }
}
