package com.axfex.dorkout.util;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by alexanderkozlov on 1/4/18.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment,
                                             Integer frameId,
                                             String tag) {

        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameId,fragment,tag);
        fragmentTransaction.commit();

    }
}
