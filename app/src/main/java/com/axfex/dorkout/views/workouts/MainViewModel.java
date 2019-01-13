package com.axfex.dorkout.views.workouts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;
import com.axfex.dorkout.util.FreshMutableLiveData;
import com.axfex.dorkout.util.ShowEvent;

import java.util.List;

/**
 * Created by alexanderkozlov on 1/7/18.
 */

public class MainViewModel extends ViewModel {
    private static final String TAG = "MAIN_VIEW_MODEL";

    private final MutableLiveData<ShowEvent> mShowEventSource = new FreshMutableLiveData<>();

    LiveData<ShowEvent> getShowEvent() {
        return mShowEventSource;
    }

    private void setShow(String tag, Long id){
        mShowEventSource.setValue(new ShowEvent(tag,id));
    }

    void openEditWorkout(Long id) {
        setShow(EditWorkoutFragment.TAG,id);
    }

    void openActionWorkout(Long id) {
        setShow(ActionWorkoutFragment.TAG,id);
    }

}
