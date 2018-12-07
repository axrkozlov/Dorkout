package com.axfex.dorkout.util;

import android.arch.lifecycle.ViewModel;
import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {

    private ViewModel mViewModel;

    public BaseFragment() {
    }

    //    public static BaseFragment newInstance() {
//        return null;
//    }

    public void attachViewmodel(ViewModel viewmodel){
        this.mViewModel=viewmodel;
    }
}
