package com.axfex.dorkout.views.trash.edit.exercises.addedit;

import android.app.Activity;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.axfex.dorkout.R;
import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.vm.AddEditExerciseViewModel;
import com.axfex.dorkout.vm.ViewModelFactory;

import javax.inject.Inject;




public class AddEditExerciseFragment extends Fragment implements View.OnClickListener {

    private EditText mName;
    private EditText mDesc;
    private FloatingActionButton mDone;
    private Workout mWorkout;
    private Integer mExercisesCount;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LayoutInflater layoutInflater;
    private Long workoutId;
    private Long exerciseId;
    private static final String WORKOUT_ID = "workout_id";
    private static final String EXERCISE_ID = "exercise_id";

    @Inject
    ViewModelFactory viewModelFactory;
    AddEditExerciseViewModel addEditExerciseViewModel;

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
//        ((WorkoutApplication) getActivity().getApplication())
//                .getAppComponent()
//                .inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addEditExerciseViewModel= ViewModelProviders.of(this,viewModelFactory).get(AddEditExerciseViewModel.class);

        //addEditExerciseViewModel.getExercisesCountLD(workoutId).observe(this, exercisesCount ->  setExercisesCount(exercisesCount));

        if (exerciseId!=0) {
            addEditExerciseViewModel.getExercise(exerciseId).observe(this, exercise -> bindExercise(exercise));
        }
    }


    private void bindExercise(Exercise exercise) {

        mName.setText(exercise.getName());
        mDesc.setText(exercise.getDescription());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.edit_exercise_fragment, container, false);
        mName =v.findViewById(R.id.et_exercise_name);
        mDesc = v.findViewById(R.id.et_exercise_desc);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutInflater = getActivity().getLayoutInflater();
        //mDone=v.findViewById(R.id.fab_edit_exercise_done);
        //mDone.setOnClickListener(this);
        v.findViewById(R.id.bt_exercise_create).setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_exercise_create:
                addExercise();
                break;
//            case R.id.bt_workout_update:
//                updateExercise();
//                break;
//            case R.id.bt_workout_delete:
//                deleteExercise();
//                break;
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
        return newExercise;
    }


    private void addExercise(){
        if (!checkNameField()) return;
        addEditExerciseViewModel.addExercise(buildExercise());
        close();
    }



    private void updateExercise(){}

    private void deleteExercise(){}


    private void close() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

//    private class SetsAdapter extends RecyclerView.Adapter<SetsViewHolder> implements View.OnClickListener {
//
//
//        @Override
//        public SetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater inflater=LayoutInflater.from(getContext());
//            View view=inflater.inflate(R.layout.item_edit_exercise_set,parent,false);
//            SetsViewHolder viewHolder=new SetsViewHolder(view);
//
//            return viewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(SetsViewHolder holder, int position) {
////            Eset eset = mEsets.get(position);
////
////            if (mEsets.size()-position>0){}
////
////            holder.bt_set_add.setOnClickListener(this);
////
////            if (eset.getWeight() != null) {
////                holder.et_set_weight.setText(eset.getWeight().toString());
////            }
//
//        }
//
////        @Override
////        public int getItemCount() {
////            return mEsets.size();
////        }
////
////        @Override
////        public void onClick(View view) {
////            buildEset(null);
////        }
//
//    }
//
//
//    private class SetsViewHolder extends RecyclerView.ViewHolder{
//        Button bt_set_add;
//        EditText et_set_weight;
//        public SetsViewHolder(View itemView) {
//            super(itemView);
//            et_set_weight =itemView.findViewById(R.id.et_set_weight);
//            bt_set_add =itemView.findViewById(R.id.bt_set_add);
//
//        }
//    }
//
//    //
////    private void setExercise(Workout workout){
////        this.mWorkout=workout;
////    }
//
//
////    private void updateWorkout(){
////        if (mWorkout == null) {
////            return;
////        }
////        mWorkout.setExercisesCount(++mExercisesCount);
////        addEditWorkoutViewModel.updateWorkout(mWorkout);
////    }
//
////    private void setExercisesCount(Integer exercisesCount){
////        this.mExercisesCount=exercisesCount;
////    }
}
