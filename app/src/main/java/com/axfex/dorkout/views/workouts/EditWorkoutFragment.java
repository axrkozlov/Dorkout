package com.axfex.dorkout.views.workouts;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.vm.ViewModelFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class EditWorkoutFragment extends Fragment {
    @Inject
    ViewModelFactory<EditWorkoutViewModel> mViewModelFactory;

    public static final String TAG = "EDIT_WORKOUT_FRAGMENT";
    private static final String WORKOUT_ID = "WORKOUT_ID";

    private Menu mMenu;
    private View mExerciseEditLayout;
    private RecyclerView mRecyclerView;
    private ExercisesAdapter mAdapter;
    private MainViewModel mMainViewModel;
    private EditWorkoutViewModel mEditWorkoutViewModel;
    private Long mWorkoutId;
    private Workout mWorkout;
    private List<Exercise> mExercises;
    private List<String> mExerciseNames = new ArrayList<>();
    private boolean whenExerciseAddedScrollDown = false;
    private ItemTouchHelper mItemTouchHelper;
    private Button mButtonOk;
    ArrayAdapter<String> mAdapterExerciseName;
    private AutoCompleteTextView mExerciseName;
    private Spinner mSpinnerExerciseNames;
    private boolean isSecondExerciseNameClick = false;
    private NumberPicker mExerciseTime;

    public static EditWorkoutFragment newInstance() {
        return new EditWorkoutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (mWorkoutId == null)
            if (getArguments() != null) {
                mWorkoutId = getArguments().getLong(WORKOUT_ID);
            } else {
                throw new IllegalArgumentException("Not have a workout id for edit");
            }
        ((WorkoutApplication) Objects.requireNonNull(getActivity()).getApplication())
                .getAppComponent()
                .inject(this);
    }

    public void setWorkoutId(Long id) {
        Log.i(TAG, "setWorkoutId: " + id);
        mWorkoutId = id;
        Bundle args = new Bundle();
        args.putLong(WORKOUT_ID, id);
        setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_workout_fragment, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mExerciseEditLayout = v.findViewById(R.id.layout_edit_exercise);
        mButtonOk = v.findViewById(R.id.button_ok);
        mButtonOk.setOnClickListener(this::onNewExerciseOk);
        mExerciseName = v.findViewById(R.id.et_exercise_type);
//        mExerciseName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mExerciseName.showDropDown();
//            }
//        });


        mExerciseName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (!hasFocus) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    mExerciseName.setVisibility(View.GONE);
                    mSpinnerExerciseNames.setVisibility(View.VISIBLE);
                } else {
                    imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);}
            }
        });



        mSpinnerExerciseNames = v.findViewById(R.id.sp_exercise_type);
        mSpinnerExerciseNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSpinnerExerciseNames.getSelectedItem().toString().equals("New name...")) {
                    mExerciseName.setVisibility(View.VISIBLE);
                    mExerciseName.requestFocus();
                    mSpinnerExerciseNames.setVisibility(View.GONE);
                    mAdapterExerciseName.remove("New name...");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        mExerciseTime=v.findViewById(R.id.np_exercise_time);

        mExerciseTime.setMinValue(0);
        mExerciseTime.setMaxValue(59);

        mRecyclerView = v.findViewById(R.id.rv_exercises);
        mRecyclerView.setLayoutManager(layoutManager);
        mItemTouchHelper = new ItemTouchHelper(new TouchHelperCallback());
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEditWorkoutViewModel = ViewModelProviders.of(this, mViewModelFactory).get(EditWorkoutViewModel.class);
        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mEditWorkoutViewModel.getWorkout(mWorkoutId).observe(this, this::onWorkoutLoaded);
        mEditWorkoutViewModel.getExercises(mWorkoutId).observe(this, this::onListLoaded);
        mEditWorkoutViewModel.getAllExerciseNames().observe(this, this::onExerciseNamesListLoaded);
    }

    private void onExerciseNamesListLoaded(List<String> names) {
        mExerciseNames = names;
        mExerciseNames.add("New name...");
        //Delete later
        onNewExercise();
        Log.i(TAG, "onExerciseNamesListLoaded: ");
    }

    private void onNewExercise() {
        mExerciseEditLayout.setVisibility(View.VISIBLE);
        Log.i(TAG, "onNewExercise: " + mExerciseNames.get(0));


        mAdapterExerciseName = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, mExerciseNames);
        mAdapterExerciseName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mExerciseName.setAdapter(mAdapterExerciseName);
        mSpinnerExerciseNames.setAdapter(mAdapterExerciseName);



    }

    private void onNewExerciseOk(View v) {
//        mExerciseName.showDropDown();

//        Exercise exercise=new Exercise(mSpinnerExerciseNames.getSelectedItem().toString(),mWorkoutId);
        String newName = mExerciseName.getText().toString();
        newName = newName.trim()
                .replaceAll("\\s+", " ")
                .replaceAll("\\.+|\\s\\.", ".")
                .replaceAll(",+|\\s,", ",")
                ;

        mExerciseNames.set(0,newName);
        Exercise exercise = new Exercise(newName, mWorkoutId);
        exercise.setOrderNumber(mExercises != null ? mExercises.size() + 1 : 0);
        mEditWorkoutViewModel.createExercise(exercise);
        mExerciseEditLayout.setVisibility(View.GONE);
        whenExerciseAddedScrollDown = true;
    }

    //STOP
    //------------------------------------------------------------------


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        Log.i(TAG, "onCreateOptionsMenu: ");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit_workout_menu, menu);
        mMenu = menu;
        updateActionBar();
    }

    private void updateActionBar() {
//        if (mWorkout != null)
//            Log.i(TAG, "updateActionBar: " + mWorkout.getName());
        ActionBar actionBar = ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (mWorkout != null) actionBar.setTitle(mWorkout.getName());

//        mMenu.findItem(R.id.menu_workouts_add).setVisible(!isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_workouts_copy).setVisible(isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_workouts_edit).setVisible(isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_workouts_rename).setVisible(isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_workouts_delete).setVisible(isWorkoutMenuShown);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                break;
            }
            case R.id.menu_exercises_add: {
                onNewExercise();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private void onWorkoutLoaded(Workout workout) {
        Log.i(TAG, "onWorkoutLoaded: " + workout + workout.getName());
        mWorkout = workout;
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
    }

    private void onListLoaded(List<Exercise> exercises) {
        this.mExercises = exercises;
        if (mAdapter == null) swapAdapter();
        else notifyAdapter();
        if (whenExerciseAddedScrollDown) ScrollToEnd();
    }

    private void swapAdapter() {
        mAdapter = new ExercisesAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void notifyAdapter() {
        if (mAdapter != null) mAdapter.notifyDataSetChanged();
    }

    private void ScrollToEnd() {
        if (mRecyclerView != null) mRecyclerView.smoothScrollToPosition(mExercises.size());
    }

    private class ExercisesAdapter extends RecyclerView.Adapter<ExerciseViewHolder> {

        @NonNull
        @Override
        public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.exercise_item, parent, false);
            return new ExerciseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
            if (mExercises.size() == 0) {
                return;
            }
            holder.setExercise(mExercises.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mExercises.size();
        }

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

    private class ExerciseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {
        TextView nameView;
        TextView descView;
        TextView numberView;
        ImageButton collapseButton;
        TextView setsView;
        Exercise mExercise;
        Integer mExpandedPosition = -1;
        int mPosition;
        private boolean mEditMode;


        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.exercise_title);
            descView = itemView.findViewById(R.id.exercise_desc);
            numberView = itemView.findViewById(R.id.exercise_number);
            collapseButton = itemView.findViewById(R.id.exercise_collapse);
            setsView = itemView.findViewById(R.id.exercise_sets);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);


        }

        public void setExercise(Exercise exercise, int position) {
            mExercise = exercise;
            mPosition = position;
            bindData();
        }

        void bindData() {
            collapseButton.setOnTouchListener(this);
            final boolean isExpanded = mPosition == mExpandedPosition && !mEditMode;
            collapseButton.setImageResource(isExpanded ? R.drawable.ic_collapse_24dp : R.drawable.ic_expand_24dp);
            itemView.setActivated(isExpanded);
            setsView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

            itemView.setOnClickListener(view -> {
//                mExpandedPosition = isExpanded ? -1 : mPosition;
//                TransitionManager.beginDelayedTransition(rvExercises);
//                notifyDataSetChanged();
            });

            nameView.setText(mExercise.getName());
            descView.setText(mExercise.getDescription());
            numberView.setText(Integer.toString(mPosition + 1));
            itemView.setTag(mExercise.getId());
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.performClick();
            mItemTouchHelper.startDrag(this);
            return false;
        }

    }

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
            mEditWorkoutViewModel.deleteExercise(((ExerciseViewHolder) viewHolder).mExercise);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            mEditWorkoutViewModel.updateExercises(mExercises);
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }


    }
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.bt_workout_start_time:
//                break;
////            case R.id.bt_workout_delete:
////                onDeleteWorkout();
////                break;
//        }
//    }

    public static class KeyboardHelper {

        public static void hideSoftKeyboard(Context context, View view) {
            if (context == null) {
                return;
            }

            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }

        public static void hideSoftKeyboard(Context context, EditText editText) {

            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }


        public static void openSoftKeyboard(Context context, EditText editText) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }

    }
}
