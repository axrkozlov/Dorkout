package com.axfex.dorkout.views.workouts;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.CheckBox;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
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
    private CheckBox mTimeCheckBox;

    private Button mButtonOk;
    NamesAdapter mAdapterExerciseName;
    private AutoCompleteTextView mExerciseName;
    private Spinner mSpinnerExerciseNames;

    private NumberPicker mEditNormTime;
    private NumberPicker mEditRestTime;
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
        setupEditWidgets(v);
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
        mEditWorkoutViewModel.getExercises(mWorkoutId).observe(this, this::onExerciseListLoaded);
        mEditWorkoutViewModel.getAllExerciseNames().observe(this, this::onExerciseNamesListLoaded);
    }

    private void setupEditWidgets(View v) {
        mButtonOk = v.findViewById(R.id.button_ok);

        mExerciseName = v.findViewById(R.id.et_exercise_type);
        mSpinnerExerciseNames = v.findViewById(R.id.sp_exercise_type);
        mEditNormTime = v.findViewById(R.id.np_norm_time);
        mEditRestTime = v.findViewById(R.id.np_rest_time);
        mTimeCheckBox = v.findViewById(R.id.time_checkBox);
        mButtonOk.setOnClickListener(this::onNewExerciseOk);

        mExerciseName.setOnFocusChangeListener((v1, hasFocus) -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!hasFocus) {
                imm.hideSoftInputFromWindow(v1.getWindowToken(), 0);
            } else {
                imm.showSoftInput(v1, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        mSpinnerExerciseNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSpinnerExerciseNames.getSelectedItem().toString().equals("New name...")) {
                    mExerciseName.setVisibility(View.VISIBLE);
                    mExerciseName.requestFocus();
                    mSpinnerExerciseNames.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mEditNormTime.setMinValue(0);
        mEditNormTime.setMaxValue(99);
        mEditRestTime.setMinValue(0);
        mEditRestTime.setMaxValue(99);

    }

    private void onExerciseNamesListLoaded(List<String> names) {
        mExerciseNames = names;

        //Delete later!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        onNewExercise();
        Log.i(TAG, "onExerciseNamesListLoaded: ");
    }

    private void onNewExercise() {
        mAdapterExerciseName = new NamesAdapter(getActivity(),
                android.R.layout.simple_spinner_item, mExerciseNames);
//        mAdapterExerciseName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mExerciseName.setAdapter(mAdapterExerciseName);
        mSpinnerExerciseNames.setAdapter(mAdapterExerciseName);
    }

    private void onNewExerciseOk(View v) {
//        Exercise exercise=new Exercise(mSpinnerExerciseNames.getSelectedItem().toString(),mWorkoutId);

        String newName;
        if (mSpinnerExerciseNames.getVisibility() == View.VISIBLE) {
            newName = mSpinnerExerciseNames.getSelectedItem().toString();
        } else {
            newName = mExerciseName.getText().toString();
            mSpinnerExerciseNames.setSelected(false);
        }

        newName = newName.trim()
                .replaceAll("\\s+", " ")
                .replaceAll("\\.+|\\s\\.", ".")
                .replaceAll(",+|\\s,", ",")
        ;

        long time = mEditNormTime.getValue()*1000;
        long restTime = mEditRestTime.getValue()*1000;

        Exercise exercise = new Exercise(newName, mWorkoutId);
        if (mTimeCheckBox.isChecked()) exercise.setTimePlan(time);
        exercise.setRestTimePlan(restTime);

        exercise.setOrderNumber(mExercises != null ? mExercises.size() + 1 : 0);
        mEditWorkoutViewModel.createExercise(exercise);
        whenExerciseAddedScrollDown = true;
        mExerciseName.setVisibility(View.GONE);
        mSpinnerExerciseNames.setVisibility(View.VISIBLE);
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
        ActionBar actionBar = ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (mWorkout != null) actionBar.setTitle(mWorkout.getName());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().onBackPressed();
            }

        }

        return super.onOptionsItemSelected(item);
    }


    private void onWorkoutLoaded(Workout workout) {
        mWorkout = workout;
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
    }


    private void onExerciseListLoaded(List<Exercise> exercises) {
        this.mExercises = exercises;
        if (mAdapter == null) setupAdapter();
        else notifyAdapter();
        if (whenExerciseAddedScrollDown) ScrollToEnd();
    }

    private void setupAdapter() {
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
            View view = LayoutInflater.from(getContext()).inflate(R.layout.action_workout_exercise_item, parent, false);
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
        TextView mName;
        TextView mDesc;

        View mInfoBar;
        TextView mNormTime;
        TextView mRestTime;
        TextView mOrderNumber;
        ImageButton mOrderButton;
        Exercise mExercise;
        int mPosition;
        TextView setsView;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            mDesc = itemView.findViewById(R.id.desc);
            mInfoBar = itemView.findViewById(R.id.info_panel);
            mNormTime = mInfoBar.findViewById(R.id.time);
            mOrderNumber = itemView.findViewById(R.id.order);
            mOrderButton = itemView.findViewById(R.id.change_order);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setExercise(Exercise exercise, int position) {
            mExercise = exercise;
            mPosition = position;
            bindData();
        }

        void bindData() {
            mOrderButton.setOnTouchListener(this);
//            final boolean isExpanded = mPosition == mExpandedPosition ;
//            mOrderButton.setImageResource(isExpanded ? R.drawable.ic_collapse_24dp : R.drawable.ic_expand_24dp);
//            itemView.setActivated(isExpanded);
//            setsView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

            itemView.setOnClickListener(view -> {
//                mExpandedPosition = isExpanded ? -1 : mPosition;
//                TransitionManager.beginDelayedTransition(rvExercises);
//                notifyDataSetChanged();
            });


            mName.setText(mExercise.getName());
            if (mExercise.getNote() != null) mDesc.setText(mExercise.getNote());
            else mDesc.setVisibility(View.GONE);

            if (mExercise.getTimePlan() != null) {
                mNormTime.setText(String.format(Locale.getDefault(), "%d", mExercise.getTimePlan()));
            }
//            if (mExercise.getRestTimePlan() != null) {
//                mRestTime.setText(String.format(Locale.getDefault(), "%d", mExercise.getRestTimePlan()));
//            }
            mOrderNumber.setText(String.format(Locale.getDefault(), "%d", mPosition + 1));
//            mOrderNumber.setVisibility(View.GONE);
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


    /**
     * ====================Helpers==================================================
     */


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

    private class NamesAdapter extends ArrayAdapter<String> {
        public NamesAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            insert("New name...",objects.size()>0?1:0);
        }
//        public NamesAdapter(@NonNull Context context, int resource, @NonNull List objects) {
//            super(context, resource, objects);

//        }

        @NonNull
        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String text=getItem(position);
            if (convertView==null){
                convertView=LayoutInflater.from(getContext()).inflate(R.layout.edit_workout_name_item, parent, false);
            }
            TextView name =(TextView) convertView;
            name.setText(text);
            if (text.equals("New name...")) {
                name.setTextColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
                name.setTypeface(null, Typeface.ITALIC);
            } else {
                name.setTextColor(ContextCompat.getColor(getContext(),R.color.secondary_white_text));
                name.setTypeface(null, Typeface.NORMAL);
            }
            return convertView;
        }

    }
}
