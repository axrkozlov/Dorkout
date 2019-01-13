package com.axfex.dorkout.util;

import com.axfex.dorkout.R;

import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by alexanderkozlov on 1/4/18.
 */

public abstract class BaseActivity<V extends ViewModel> extends AppCompatActivity {
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment,
                                             Integer frameId,
                                             String tag,
                                             Boolean isRootFragment) {

        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        fragmentTransaction.replace(frameId,fragment,tag);
        if (!isRootFragment) fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();

    }

    public abstract V getViewModel();


}
