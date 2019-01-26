package com.axfex.dorkout.views.workouts;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;

import java.util.List;
import java.util.Objects;

public class EditExerciseFragment extends Fragment {
    public static final String TAG = "EDIT_EXERCISE_FRAGMENT";
    private static final String WORKOUT_ID = "EXERCISE_ID";
    private EditExerciseViewModel mViewModel;
    private Menu mMenu;
    private RecyclerView mRecyclerView;
//    private EditWorkoutFragment.ExercisesAdapter mAdapter;
    private MainViewModel mMainViewModel;
    private EditWorkoutViewModel mEditWorkoutViewModel;
    private Long mWorkoutId;
    private Workout mWorkout;
    private List<Exercise> mExercises;


    public static EditExerciseFragment newInstance() {
        return new EditExerciseFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (mWorkoutId == null)
            if (getArguments() != null) {
                mWorkoutId = getArguments().getLong(WORKOUT_ID);
            } else {
                throw new IllegalArgumentException("Not have a workout id for edit");
            }
//        ((WorkoutApplication) Objects.requireNonNull(getActivity()).getApplication())
//                .getAppComponent()
//                .inject(this);
    }

//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.edit_exercise_fragment, container, false);
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(EditExerciseViewModel.class);
        // TODO: Use the ViewModel
    }

}
