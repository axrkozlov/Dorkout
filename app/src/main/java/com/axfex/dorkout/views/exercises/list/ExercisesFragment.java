package com.axfex.dorkout.views.exercises.list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.views.exercises.addedit.AddEditExerciseActivity;
import com.axfex.dorkout.vm.ExercisesViewModel;
import com.axfex.dorkout.vm.ViewModelFactory;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class ExercisesFragment extends Fragment {
    private static final int TAG_EXERCISE_ID = 503;


    @Inject
    ViewModelFactory viewModelFactory;

    private MenuItem mEditMenu;
    private FloatingActionButton mAddButton;

    private ExercisesViewModel exercisesViewModel;
    private RecyclerView rvExercises;
    private ExercisesAdapter mAdapter;
    private List<Exercise> mExercises;

    private ItemTouchHelper mItemTouchHelper;
    private static final String WORKOUT_ID = "workout_id";
    private static final String EXERCISE_ID = "exercise_id";
    private Boolean mEditMode = false;


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
        exercisesViewModel.getExercises(workoutId).observe(this, exercises -> setExercises(exercises));
//        exercisesViewModel.getExercisesWithSets(workoutId).observe(this, new Observer<List<ExerciseWithSets>>() {
//            @Override
//            public void onChanged(@Nullable List<ExerciseWithSets> exercises) {
//                setmExercisesWithSets(exercises);
//            }
//        });
    }
//
    private void setExercises(List<Exercise> exercises) {
        this.mExercises = exercises;
        mAdapter = new ExercisesAdapter();
        rvExercises.setAdapter(mAdapter);
    }

//    private void setmExercisesWithSets(List<ExerciseWithSets> exercises) {
//        this.mExercisesWithSets = exercises;
//        mAdapter = new ExercisesAdapter();
//        rvExercises.setAdapter(mAdapter);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exercises, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvExercises = v.findViewById(R.id.rv_exercises);
        rvExercises.setLayoutManager(layoutManager);
        mItemTouchHelper = new ItemTouchHelper(new TouchHelperCallback());
        mItemTouchHelper.attachToRecyclerView(rvExercises);
        mAddButton = v.findViewById(R.id.fab_add_exercise);
        mAddButton.setOnClickListener(v1 -> startAddEditExerciseActivity());
        //mAddButton.hide();
        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_exercises, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_exercises_edit: {
                mEditMenu = item;
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
        startActivityForResult(i, AddEditExerciseActivity.REQUEST_ADD_TASK);
    }

    private void startAddEditActivity(Long exerciseId) {
        Intent i = new Intent(getActivity(), AddEditExerciseActivity.class);
        i.putExtra(WORKOUT_ID, workoutId);
        i.putExtra(EXERCISE_ID, exerciseId);
        startActivityForResult(i, AddEditExerciseActivity.REQUEST_ADD_TASK);
    }


    private void updateExercises() {
        exercisesViewModel.updateExercises(mExercises);
    }

    public Boolean switchToEditMode() {
        mEditMode = !mEditMode;
        if (mEditMode) {
            showEdit();
        } else {
            hideEdit();
        }
        return mEditMode;

    }

    private void showEdit() {

        mEditMenu.setTitle(R.string.menu_exercises_done);
        mEditMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        TransitionManager.beginDelayedTransition(rvExercises);
        mAdapter.notifyDataSetChanged();
        mAddButton.show();
    }

    private void hideEdit() {
        mEditMenu.setTitle(R.string.menu_exercises_edit);
        //mEditMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        TransitionManager.beginDelayedTransition(rvExercises);
        mAdapter.notifyDataSetChanged();
        mAddButton.hide();
    }


    /**
     * ------------------------------------------
     **/

    private class ExercisesAdapter extends RecyclerView.Adapter<ExercisesViewHolder> {

        @Override
        public ExercisesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_exercise, parent, false);
            return new ExercisesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ExercisesViewHolder holder, int position) {
            if (mExercises.size() == 0) {
                return;
            }
            Exercise exercise = mExercises.get(position);
//            List<Eset> esets = mExercisesWithSets.get(position).esets;
//            if (exercise == null) {
//                return;
//            }
            holder.collapseButton.setOnTouchListener(holder);
            final boolean isExpanded = position == holder.mExpandedPosition && !mEditMode;
            holder.collapseButton.setImageResource(isExpanded ? R.drawable.ic_collapse_24dp : R.drawable.ic_expand_24dp);
            holder.itemView.setActivated(isExpanded);
            holder.setsView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

            holder.itemView.setOnClickListener(view -> {
                holder.mExpandedPosition = isExpanded ? -1 : position;
                TransitionManager.beginDelayedTransition(rvExercises);
                notifyDataSetChanged();
            });
            if (mEditMode) {

            } else {

            }
            holder.nameView.setText(exercise.getName());
            holder.descView.setText(exercise.getDescription());
            holder.numberView.setText(Integer.toString(position + 1));
            holder.itemView.setTag(exercise.getId());


            //bindExercise(holder,exercise,position);

        }

        @Override
        public int getItemCount() {
            return mExercises.size();
        }

//        private void bindExercise(ExercisesViewHolder holder, Exercise exercise, Integer position) {
//
//
//        }

        private void onItemMoved(int base, int target) {
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
        }


    }

    /**
     * ------------------------------------------
     **/

    private class ExercisesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {
        TextView nameView;
        TextView descView;
        TextView numberView;
        ImageButton collapseButton;
        TextView setsView;
        Integer mExpandedPosition = -1;

        public ExercisesViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.exercise_title);
            descView = itemView.findViewById(R.id.exercise_desc);
            numberView = itemView.findViewById(R.id.exercise_number);
            collapseButton = itemView.findViewById(R.id.exercise_collapse);
            setsView = itemView.findViewById(R.id.exercise_sets);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            startAddEditActivity((Long) view.getTag());
            return true;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            view.performClick();
            if (!mEditMode) return false;
            mItemTouchHelper.startDrag(this);
            return true;
        }
    }

    /**
     * ------------------------------------------
     **/

    private class TouchHelperCallback extends ItemTouchHelper.Callback {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            mAdapter.onItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;

        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            exercisesViewModel.deleteExercise((Long) viewHolder.itemView.getTag());
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            updateExercises();
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return mEditMode;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }
    }


}
