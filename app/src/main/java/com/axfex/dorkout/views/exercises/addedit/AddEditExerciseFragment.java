package com.axfex.dorkout.views.exercises.addedit;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Set;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.vm.AddEditExerciseViewModel;
import com.axfex.dorkout.vm.AddEditSetViewModel;
import com.axfex.dorkout.vm.SetsViewModel;
import com.axfex.dorkout.vm.ViewModelFactory;
import com.axfex.dorkout.vm.AddEditWorkoutViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;




public class AddEditExerciseFragment extends Fragment implements View.OnClickListener {

    private EditText mName;
    Long workoutId;
    Long exerciseId;
    private Workout mWorkout;
    private Integer mExercisesCount;
    private List<Set> mSets;

    private RecyclerView recyclerView;
    private  RecyclerView.Adapter adapter;
    private LayoutInflater layoutInflater;
    private static final String WORKOUT_ID = "workout_id";
    private static final String EXERCISE_ID = "exercise_id";



    @Inject
    ViewModelFactory viewModelFactory;
    AddEditSetViewModel addEditSetViewModel;
    SetsViewModel setsViewModel;
    AddEditExerciseViewModel addEditExerciseViewModel;
    AddEditWorkoutViewModel addEditWorkoutViewModel;

    public static AddEditExerciseFragment newInstance(Long workoutId) {
        AddEditExerciseFragment fragment = new AddEditExerciseFragment();
        Bundle args = new Bundle();
        args.putLong(WORKOUT_ID, workoutId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
        setsViewModel=ViewModelProviders.of(this,viewModelFactory).get(SetsViewModel.class);
        addEditSetViewModel= ViewModelProviders.of(this,viewModelFactory).get(AddEditSetViewModel.class);
        addEditExerciseViewModel= ViewModelProviders.of(this,viewModelFactory).get(AddEditExerciseViewModel.class);
        addEditWorkoutViewModel= ViewModelProviders.of(this,viewModelFactory).get(AddEditWorkoutViewModel.class);

        setsViewModel.getSets(exerciseId).observe(this, new Observer<List<Set>>() {
            @Override
            public void onChanged(@Nullable List<Set> sets) {
                if (mSets!=null) {
                    mSets = sets;
                    swapAdapter();
                } else {
                    mSets=new ArrayList<Set>();
                    addset();
                }
            }
        });

        addEditExerciseViewModel.getExercisesCount(workoutId).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer exercisesCount) {
                if (exercisesCount != null) {
                    mExercisesCount=exercisesCount;
                } else {
                        mExercisesCount=0;
                }
            }
        });
        addEditExerciseViewModel.getExercise(exerciseId).observe(this, new Observer<Exercise>() {
            @Override
            public void onChanged(@Nullable Exercise exercise) {
                if (exercise != null) {
                     bindExerciseToEdit(exercise);
                }
            }
        });

        addEditWorkoutViewModel.getWorkout(workoutId).observe(this, new Observer<Workout>() {
            @Override
            public void onChanged(@Nullable Workout workout) {
                if (workout != null) {
                    mWorkout=workout;
                }
            }
        });
    }

    private void swapAdapter(){
        adapter = new SetsAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_add_edit_exercise, container, false);
        mName =v.findViewById(R.id.et_exercise_name);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = v.findViewById(R.id.rv_sets);
        recyclerView.setLayoutManager(layoutManager);
        layoutInflater = getActivity().getLayoutInflater();

        v.findViewById(R.id.bt_exercise_create).setOnClickListener(this);
        //v.findViewById(R.id.bt_set_add).setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_exercise_create:
                addExercise();
                break;
            case R.id.bt_workout_start_time:
                //showTimePickerDialog();
                break;
            case R.id.bt_workout_update:
                updateExercise();
                break;
            case R.id.bt_workout_delete:
                deleteExercise();
                break;
            case R.id.bt_set_add:
                addset();
        }
    }

    private boolean checkNameField(){
        if (mName.getText().length() == 0) {
            Toast.makeText(getContext(), "Please, type Name of workout", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Exercise buildExercise(){
        Exercise newExercise=new Exercise(mName.getText().toString(),workoutId);
        newExercise.setSets(mSets);

        return newExercise;
    }

    private void bindExerciseToEdit(Exercise exercise) {

        if (exercise == null) {
            return;
        }
    }

    private void addExercise(){
        if (!checkNameField()){
            return;
        }

        Exercise newExercise= buildExercise();
        if (newExercise == null) {
            return;
        }

        addEditExerciseViewModel.addExercise(newExercise);


        updateWorkout();
        //Log.e("ADDD", "addExercise: "+result.toString());
        //Toast.makeText(getContext().getApplicationContext(), "added id:"+result.toString(), Toast.LENGTH_SHORT).show();
        close();
    }

    private void showId(Long insertedId){
        Toast.makeText(getContext(), "added id:"+insertedId.toString(), Toast.LENGTH_SHORT).show();
    }

    private void updateExercise(){

    }

    private void updateWorkout(){
        if (mWorkout == null) {
            return;
        }
        mWorkout.setExercisesCount(++mExercisesCount);
        addEditWorkoutViewModel.updateWorkout(mWorkout);
    }

    private void deleteExercise(){

    }


    private void close() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    private void addset(){
        mSets.add(new Set(0L));
        swapAdapter();

    }

    private class SetsAdapter extends RecyclerView.Adapter<SetsViewHolder>{


        @Override
        public SetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getContext());
            View view=inflater.inflate(R.layout.item_set,parent,false);
            SetsViewHolder viewHolder=new SetsViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(SetsViewHolder holder, int position) {
            holder.bt_add_set.setOnClickListener(AddEditExerciseFragment.this);
        }

        @Override
        public int getItemCount() {
            return mSets.size();
        }
    }


    private class SetsViewHolder extends RecyclerView.ViewHolder{
        Button bt_add_set;
        public SetsViewHolder(View itemView) {
            super(itemView);
            bt_add_set=itemView.findViewById(R.id.bt_set_add);

        }
    }

}
