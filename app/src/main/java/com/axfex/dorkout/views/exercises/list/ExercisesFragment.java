package com.axfex.dorkout.views.exercises.list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.views.exercises.addedit.AddEditExerciseActivity;
import com.axfex.dorkout.vm.ExercisesViewModel;
import com.axfex.dorkout.vm.ViewModelFactory;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static android.support.v4.view.MotionEventCompat.getActionMasked;

public class ExercisesFragment extends Fragment {
    private static final int TAG_EXERCISE_ID =503;


    @Inject
    ViewModelFactory viewModelFactory;

    private MenuItem mEditMenu;
    private FloatingActionButton mAddButton;

    private ExercisesViewModel exercisesViewModel;
    private RecyclerView recyclerView;
    private LayoutInflater layoutInflater;
    private ExercisesAdapter mAdapter;
    private List<Exercise> mExercises;
    private ItemTouchHelper mItemTouchHelper;
    private static final String WORKOUT_ID = "workout_id";
    private static final String EXERCISE_ID = "exercise_id";
    private Boolean mEditMode=false;


    private Long workoutId;

    public ExercisesFragment() {
        // Required empty public constructor
    }

    public static ExercisesFragment newInstance(Long workoutId) {
        ExercisesFragment fragment = new ExercisesFragment();
        Bundle args = new Bundle();
        args.putLong(WORKOUT_ID, workoutId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            workoutId = getArguments().getLong(WORKOUT_ID);
        }
        ((WorkoutApplication) getActivity().getApplication())
                .getAppComponent()
                .inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        exercisesViewModel = ViewModelProviders.of(this, viewModelFactory).get(ExercisesViewModel.class);
        exercisesViewModel.getExercises(workoutId).observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(@Nullable List<Exercise> exercises) {
                    setExercises(exercises);
            }
        });
    }

    private void setExercises(List<Exercise> exercises) {
        this.mExercises = exercises;
        mAdapter = new ExercisesAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exercises, container,false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = v.findViewById(R.id.rv_exercises);
        recyclerView.setLayoutManager(layoutManager);
        layoutInflater = getActivity().getLayoutInflater();
        mAddButton = v.findViewById(R.id.fab_add_exercise);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddEditExerciseActivity();
            }
        });
        //mAddButton.hide();
        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_exercises,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch( item.getItemId()){
            case R.id.menu_exercises_edit: {
                mEditMenu=item;
                switchToEditMode();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void startAddEditExerciseActivity() {
        Intent i = new Intent(getActivity(), AddEditExerciseActivity.class);
        i.putExtra(WORKOUT_ID, workoutId);
        startActivityForResult(i,AddEditExerciseActivity.REQUEST_ADD_TASK);
    }

    private void startAddEditActivity(Long exerciseId){
        Intent i = new Intent(getActivity(), AddEditExerciseActivity.class);
        i.putExtra(WORKOUT_ID, workoutId);
        i.putExtra(EXERCISE_ID, exerciseId);
        startActivityForResult(i,AddEditExerciseActivity.REQUEST_ADD_TASK);
    }


    private void updateExercises(){
        exercisesViewModel.updateExercises(mExercises);
    }

    public Boolean switchToEditMode(){
        mEditMode=!mEditMode;
        if (mEditMode) {
            showEdit();
        } else {
            hideEdit();
        }
        return mEditMode;

    }

    private void showEdit(){

        mEditMenu.setTitle(R.string.menu_exercises_done);
        mEditMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        mItemTouchHelper= new ItemTouchHelper(new TouchHelperCallback());
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        mAdapter.notifyDataSetChanged();
        mAddButton.show();
    }

    private void hideEdit(){
        mEditMenu.setTitle(R.string.menu_exercises_edit);
        //mEditMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        mAdapter.notifyDataSetChanged();
        mItemTouchHelper=null;
        mAddButton.hide();
    }


    /**------------------------------------------**/

    private class ExercisesAdapter extends RecyclerView.Adapter<ExercisesViewHolder> {

        @Override
        public ExercisesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.item_exercise, parent, false);
            ExercisesViewHolder viewHolder = new ExercisesViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ExercisesViewHolder holder, int position) {
            if (mExercises.size() == 0) {
                return;
            }
            Exercise exercise = mExercises.get(position);
            if (exercise == null) {
                return;
            }
            if (mEditMode){
                holder.collapseButton.setImageResource(R.drawable.ic_headline_24dp);
                holder.collapseButton.setOnTouchListener(
                        new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                                    mItemTouchHelper.startDrag(holder);
                                }
                                return false;
                            }


                        });
            } else {

                holder.collapseButton.setImageResource(R.drawable.ic_expand_less_24dp);
                holder.collapseButton.setOnTouchListener(null);
            }

            holder.nameView.setText(exercise.getName());
            holder.descView.setText(exercise.getDescription());
            holder.numberView.setText(Integer.toString(position+1));
            holder.itemView.setTag(exercise.getId());



        }


        public boolean onItemMoved(int base,int target){

            //Toast.makeText(getContext(), "from:"+base+" to:"+target, Toast.LENGTH_SHORT).show();
            if (base < target) {
                for (int i = base; i < target; i++) {
                    Collections.swap(mExercises, i, i + 1);
                }
            } else {
                for (int i = base; i > target; i--) {
                    Collections.swap(mExercises, i, i - 1);
                }
            }
            notifyItemMoved(base, target);
            return true;
        }

        @Override
        public int getItemCount() {
                return mExercises.size();
        }
    }

    /**------------------------------------------**/

    private class ExercisesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView nameView;
        TextView descView;
        TextView numberView;
        ImageButton collapseButton;

        public ExercisesViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.exercise_title);
            descView = itemView.findViewById(R.id.exercise_desc);
            numberView = itemView.findViewById(R.id.exercise_number);
            collapseButton= itemView.findViewById(R.id.exercise_collapse);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            startAddEditActivity((Long)view.getTag());
            return true;
        }
    }

    /**------------------------------------------**/

    private class TouchHelperCallback extends ItemTouchHelper.Callback{

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags,swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            mAdapter.onItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
            return true;

        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            exercisesViewModel.deleteExercise((Long)viewHolder.itemView.getTag());
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            updateExercises();
        }

//        @Override
//        public boolean isItemViewSwipeEnabled() {
//            return false;
//        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }
    }




}
