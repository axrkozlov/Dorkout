package com.axfex.dorkout.util;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.axfex.dorkout.WorkoutApplication;

/**
 * Created by alexanderkozlov on 1/4/18.
 */

public abstract class BaseActivity<V extends ViewModel> extends AppCompatActivity {

    private V mViewModel;

    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment,
                                             Integer frameId,
                                             String tag,
                                             Boolean addToBackStack) {

        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        fragmentTransaction.replace(frameId,fragment,tag);
        if (addToBackStack)
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();

    }

    public abstract V getViewModel();


}
