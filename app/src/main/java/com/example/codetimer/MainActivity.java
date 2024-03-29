package com.example.codetimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

import com.example.codetimer.Support.ItemType;
import com.example.codetimer.Support.ListElement;
import com.example.codetimer.Support.ListElementAdapter;
import com.example.codetimer.Support.LoopEditDialog;
import com.example.codetimer.Support.TimerEditDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements TimerEditDialog.TimerEditListener, LoopEditDialog.LoopEditListener {
    private RecyclerView recyclerView;
    private ListElementAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ListElement> elements;
    private FloatingActionButton timer_button;

    private static int INDENT_INCREMENT = 2;
    public static String EXTRAMESSAGE = "MainActivity.ELEMENTS";
    private String FILENAME = "elements_file";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Toolbar mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        timer_button = findViewById(R.id.start_timer_button);
        timer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
               for (ListElement el : elements){
                   if (el.getType() == ItemType.LOOPEND | el.getType() == ItemType.LOOPSTART){
                       el.setRelatedIndex(elements.indexOf(el.getrelatedElement()));
                       el.setSave_number(el.getRepetition());
                   }
               }
                intent.putExtra(EXTRAMESSAGE, elements);
                startActivity(intent);
            }
        });


        elements = new ArrayList<>();
        //TODO Load on start

        try {
            FileInputStream fis = openFileInput(EXTRAMESSAGE);
            ObjectInputStream is = new ObjectInputStream(fis);
            boolean cond = true;
            while (cond){
                ListElement obj = (ListElement) is.readObject();
                if (obj != null){
                    elements.add(obj);
                }else {
                    cond = false;
                }
            }
            is.close();
            fis.close();
        } catch (Exception e) {
            ListElement el1 = new ListElement("Plank", ItemType.TIMER);
            el1.setNumber(60000);
            el1.incDepthBy(INDENT_INCREMENT);

            ListElement el2 = new ListElement("Side Plank", ItemType.TIMER);
            el2.setNumber(60000);
            el2.incDepthBy(INDENT_INCREMENT);

            ListElement el3 = new ListElement("Hollow Body Hold", ItemType.TIMER);
            el3.setNumber(60000);
            el3.incDepthBy(INDENT_INCREMENT);


            ListElement el4 = new ListElement("Rest", ItemType.TIMER);
            el4.setNumber(120000);
            el4.incDepthBy(INDENT_INCREMENT);

            ListElement end = new ListElement("Core Workout", ItemType.LOOPEND);
            ListElement start = new ListElement("Core Workout", ItemType.LOOPSTART);
            start.setNumber(3);
            start.setrelatedElement(end);
            end.setrelatedElement(start);

            elements.add(start);
            elements.add(el1);
            elements.add(el2);
            elements.add(el3);
            elements.add(el4);
            elements.add(end);
        }


        mAdapter = new ListElementAdapter(elements, getSupportFragmentManager());
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP,ItemTouchHelper.LEFT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //Element that is moved
                int pos_dragged = viewHolder.getAdapterPosition();
                //Element that gets switched
                int pos_target = target.getAdapterPosition();

                //If moved Element ist a LOOPSTART
                if (elements.get(pos_dragged).getType() == ItemType.LOOPSTART) {
                    //MAKE STARTLOOPS NOT FUCK THEM SELF
                    if (elements.get(pos_dragged).getrelatedElement() == elements.get(pos_target)) {
                        //If its at the Bottom
                        if (elements.size() - 1 == pos_target) {
                            return true;
                        }
                        //Check indent
                        if (elements.get(pos_target + 1).getType() == ItemType.LOOPSTART) {
                            elements.get(pos_target).incDepthBy(INDENT_INCREMENT);
                        }
                        if (elements.get(pos_target + 1).getType() == ItemType.LOOPEND) {
                            elements.get(pos_target).incDepthBy(-INDENT_INCREMENT);
                        }


                        //Don't let the loop fuck up
                        Collections.swap(elements, pos_target, pos_target + 1);
                        mAdapter.notifyItemMoved(pos_target, pos_target + 1);
                        //Fix indent of Elements
                    } else {
                        if (pos_dragged < pos_target) {
                            elements.get(pos_target).incDepthBy(-INDENT_INCREMENT);
                        } else {
                            elements.get(pos_target).incDepthBy(INDENT_INCREMENT);
                        }
                    }
                }

                //If Element is LOOPEND
                if (elements.get(pos_dragged).getType() == ItemType.LOOPEND) {
                    //Make Loop not fuck itself
                    if (elements.get(pos_dragged).getrelatedElement() == elements.get(pos_target)) {
                        if (pos_target == 0) {
                            return true;
                        }
                        //Check indent
                        if (elements.get(pos_target - 1).getType() == ItemType.LOOPSTART) {
                            elements.get(pos_target).incDepthBy(-INDENT_INCREMENT);
                        }
                        if (elements.get(pos_target - 1).getType() == ItemType.LOOPEND) {
                            elements.get(pos_target).incDepthBy(INDENT_INCREMENT);
                        }

                        //Push Loopstart up
                        Collections.swap(elements, pos_target, pos_target - 1);
                        mAdapter.notifyItemMoved(pos_target, pos_target - 1);

                        //Fix Element indent
                    } else {
                        if (pos_dragged < pos_target) {
                            elements.get(pos_target).incDepthBy(INDENT_INCREMENT);
                        } else {
                            elements.get(pos_target).incDepthBy(-INDENT_INCREMENT);
                        }
                    }
                }

                //TODO Fix nested loops, rn can fuck themself
                /*
                if (elements.get(pos_dragged).getType() == ItemType.LOOPEND | elements.get(pos_dragged).getType() == ItemType.LOOPSTART){
                    if (elements.get(pos_target).getType() == ItemType.LOOPEND | elements.get(pos_target).getType() == ItemType.LOOPSTART) {
                        int pos_dragged_relative = elements.indexOf(elements.get(pos_dragged).getrelatedElement());
                        int pos_target_relative = elements.indexOf(elements.get(pos_target).getrelatedElement());

                        if (pos_dragged < pos_target){
                            if (pos_dragged_relative < pos_target_relative){
                                Collections.swap(elements, pos_dragged_relative, pos_target_relative);
                                mAdapter.notifyItemMoved(pos_dragged_relative, pos_target_relative);
                            }
                        }else {

                        }
                    }
                }

                 */
                //Fix Element indent
                if (elements.get(pos_target).getType() == ItemType.LOOPSTART) {
                    if (pos_target > pos_dragged) {
                        elements.get(pos_dragged).incDepthBy(INDENT_INCREMENT);
                    } else {
                        elements.get(pos_dragged).incDepthBy(-INDENT_INCREMENT);
                    }
                }

                if (elements.get(pos_target).getType() == ItemType.LOOPEND) {
                    if (pos_target > pos_dragged) {
                        elements.get(pos_dragged).incDepthBy(-INDENT_INCREMENT);
                    } else {
                        elements.get(pos_dragged).incDepthBy(INDENT_INCREMENT);
                    }
                }

                //Swap Items
                Collections.swap(elements, pos_dragged, pos_target);
                mAdapter.notifyItemMoved(pos_dragged, pos_target);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //pos of deleted Element
                int pos = viewHolder.getAdapterPosition();

                if (elements.get(pos).getType() == ItemType.LOOPSTART) {
                    //Get LOOPEND
                    int related_pos = elements.indexOf(elements.get(pos).getrelatedElement());

                    //set indent of Loopelement one back
                    for (int i = pos + 1; i < related_pos; i++) {
                        elements.get(i).incDepthBy(-INDENT_INCREMENT);
                    }
                    //delete both elements
                    elements.remove(related_pos);
                    mAdapter.notifyItemRemoved(related_pos);

                    elements.remove(pos);
                    mAdapter.notifyItemRemoved(pos);

                }else if (elements.get(pos).getType() == ItemType.LOOPEND){
                    int related_pos = elements.indexOf(elements.get(pos).getrelatedElement());
                    for (int i = related_pos + 1; i < pos; i++) {
                        elements.get(i).incDepthBy(-INDENT_INCREMENT);
                    }
                    elements.remove(pos);
                    mAdapter.notifyItemRemoved(pos);

                    elements.remove(related_pos);
                    mAdapter.notifyItemRemoved(related_pos);
                }else{
                    elements.remove(pos);
                    mAdapter.notifyItemRemoved(pos);
                }

            }
        });
        helper.attachToRecyclerView(recyclerView);

        RecyclerView.ItemDecoration margin = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = 20;
                if (parent.getChildAdapterPosition(view) != -1) {
                    outRect.left = 20 * elements.get(parent.getChildAdapterPosition(view)).getDepth();
                }
                outRect.right = 20;
                if (parent.getChildAdapterPosition(view) == 0){
                    outRect.top = 20;
                }
            }
        };
        recyclerView.addItemDecoration(margin);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:

                return true;

            case R.id.action_add:
                elements.add(new ListElement("NewTimer", ItemType.TIMER));
                mAdapter.notifyItemInserted(elements.size()-1);
                return true;

            case R.id.action_add_loop:
                ListElement start = new ListElement("LOOP", ItemType.LOOPSTART);
                ListElement end = new ListElement("LOOP END", ItemType.LOOPEND);
                start.setrelatedElement(end);
                end.setrelatedElement(start);
                start.setNumber(4);
                elements.add(start);
                elements.add(end);
                mAdapter.notifyItemInserted(elements.size()-1);
                mAdapter.notifyItemInserted(elements.size()-2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTimerEdit(DialogFragment dialog, int pos, String name, int minutes, int seconds) {
        ListElement element = elements.get(pos);
        element.setName(name);
        long milliseconds = TimeUnit.MILLISECONDS.convert(seconds, TimeUnit.SECONDS) + TimeUnit.MILLISECONDS.convert(minutes, TimeUnit.MINUTES);
        element.setNumber(milliseconds);
        mAdapter.notifyItemChanged(pos);
    }

    @Override
    public void onLoopEdit(int pos, String name, int number) {
        ListElement element = elements.get(pos);
        element.setName(name);
        element.setNumber(number);

        element.getrelatedElement().setName(name);
        mAdapter.notifyItemChanged(pos);
        mAdapter.notifyItemChanged(elements.indexOf(element.getrelatedElement()));
    }

    //TODO Save on Close
    public void onStop(){
        super.onStop();
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            for (ListElement el : elements){
                os.writeObject(el);
            }
            //Write Null as End Flag
            os.writeObject(null);
            os.close();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
