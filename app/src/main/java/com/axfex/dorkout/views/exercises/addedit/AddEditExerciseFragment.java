package com.axfex.dorkout.views.exercises.addedit;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Eset;
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
    private EditText mDesc;
    private FloatingActionButton mDone;
    private ImageButton mAddEset;
    private Workout mWorkout;
    private Integer mExercisesCount;
    private List<Eset> mEsets;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LayoutInflater layoutInflater;
    private Long workoutId;
    private Long exerciseId;
    private static final String WORKOUT_ID = "workout_id";
    private static final String EXERCISE_ID = "exercise_id";

    @Inject
    ViewModelFactory viewModelFactory;
    AddEditSetViewModel addEditSetViewModel;
    SetsViewModel setsViewModel;
    AddEditExerciseViewModel addEditExerciseViewModel;
    AddEditWorkoutViewModel addEditWorkoutViewModel;

    public static AddEditExerciseFragment newInstance(Long workoutId,Long exerciseId) {
        AddEditExerciseFragment fragment = new AddEditExerciseFragment();
        Bundle args = new Bundle();
        if (workoutId != null) {
            args.putLong(WORKOUT_ID, workoutId);
        }
        if (exerciseId != null) {
            args.putLong(EXERCISE_ID, exerciseId);
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            workoutId = getArguments().getLong(WORKOUT_ID);
            exerciseId = getArguments().getLong(EXERCISE_ID);
        }
        ((WorkoutApplication) getActivity().getApplication())
                .getAppComponent()
                .inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //At least one must have.
        if (mEsets == null) createOneEset();
        addEditSetViewModel= ViewModelProviders.of(this,viewModelFactory).get(AddEditSetViewModel.class);
        //addEditWorkoutViewModel= ViewModelProviders.of(this,viewModelFactory).get(AddEditWorkoutViewModel.class);
        addEditExerciseViewModel= ViewModelProviders.of(this,viewModelFactory).get(AddEditExerciseViewModel.class);
        setsViewModel=ViewModelProviders.of(this,viewModelFactory).get(SetsViewModel.class);

        //addEditWorkoutViewModel.getWorkout(workoutId).observe(this, workout -> setWorkout(workout));
        //addEditExerciseViewModel.getExercisesCount(workoutId).observe(this, exercisesCount ->  setExercisesCount(exercisesCount));

        if (exerciseId!=0) {
            addEditExerciseViewModel.getExercise(exerciseId).observe(this, exercise -> bindExercise(exercise));
            setsViewModel.getSets(exerciseId).observe(this, sets -> bindSets(sets));
        }
    }

    private void bindSets(List<Eset> esets){
        this.mEsets = esets;
        swapAdapter();
    }

    private void bindExercise(Exercise exercise) {

        mName.setText(exercise.getName());
        mDesc.setText(exercise.getDescription());
    }

    private void createOneEset(){
        mEsets =new ArrayList<>();
        Eset eset =new Eset();
        eset.setNormWeight(999);
        mEsets.add(eset);
        swapAdapter();
    }

    private void buildEset(@Nullable Eset previousEset){
        Eset eset =new Eset();

        if (exerciseId != null) {
            eset.setExerciseId(exerciseId);
        }
        eset.setNormWeight(999);
        eset.setNormRepeats(0);
        eset.setNormTime(0);
        mEsets.add(eset);
        swapAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_add_edit_exercise, container, false);
        mName =v.findViewById(R.id.et_exercise_name);
        mDesc = v.findViewById(R.id.et_exercise_desc);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = v.findViewById(R.id.rv_sets);
        recyclerView.setLayoutManager(layoutManager);
        layoutInflater = getActivity().getLayoutInflater();
        swapAdapter();
        //mDone=v.findViewById(R.id.fab_edit_exercise_done);
        //mDone.setOnClickListener(this);
        mAddEset=v.findViewById(R.id.ib_addeditexercises_add_set);
        mAddEset.setOnClickListener(this);
        v.findViewById(R.id.bt_exercise_create).setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_exercise_create:
                addExercise();
                break;
            case R.id.ib_addeditexercises_add_set:
                addEset();
                break;
            case R.id.bt_workout_update:
                updateExercise();
                break;
            case R.id.bt_workout_delete:
                deleteExercise();
                break;
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
        newExercise.setEsets(mEsets);

        return newExercise;
    }


    private void addExercise(){
        if (!checkNameField()) return;
        addEditExerciseViewModel.addExercise(buildExercise());

        close();
    }

    private void addEset(){
        buildEset(null);

    }


    private void updateExercise(){}

    private void deleteExercise(){}


    private void close() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    private void swapAdapter(){
        adapter = new SetsAdapter();
        recyclerView.setAdapter(adapter);
    }


    private class SetsAdapter extends RecyclerView.Adapter<SetsViewHolder> implements View.OnClickListener {


        @Override
        public SetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getContext());
            View view=inflater.inflate(R.layout.item_edit_exercise_set,parent,false);
            SetsViewHolder viewHolder=new SetsViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(SetsViewHolder holder, int position) {
            Eset eset = mEsets.get(position);

            if (mEsets.size()-position>0){}

            holder.bt_set_add.setOnClickListener(this);

            if (eset.getNormWeight() != null) {
                holder.et_set_weight.setText(eset.getNormWeight().toString());
            }

        }

        @Override
        public int getItemCount() {
            return mEsets.size();
        }

        @Override
        public void onClick(View view) {
            buildEset(null);
        }

    }


    private class SetsViewHolder extends RecyclerView.ViewHolder{
        Button bt_set_add;
        EditText et_set_weight;
        public SetsViewHolder(View itemView) {
            super(itemView);
            et_set_weight =itemView.findViewById(R.id.et_set_weight);
            bt_set_add =itemView.findViewById(R.id.bt_set_add);

        }
    }

    //
//    private void setWorkout(Workout workout){
//        this.mWorkout=workout;
//    }


//    private void updateWorkout(){
//        if (mWorkout == null) {
//            return;
//        }
//        mWorkout.setExercisesCount(++mExercisesCount);
//        addEditWorkoutViewModel.updateWorkout(mWorkout);
//    }

//    private void setExercisesCount(Integer exercisesCount){
//        this.mExercisesCount=exercisesCount;
//    }
}
