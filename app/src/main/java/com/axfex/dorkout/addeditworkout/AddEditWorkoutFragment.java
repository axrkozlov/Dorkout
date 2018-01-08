package com.axfex.dorkout.addeditworkout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axfex.dorkout.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEditWorkoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditWorkoutFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AddEditWorkoutFragment() {
        // Required empty public constructor
    }


    public static AddEditWorkoutFragment newInstance() {
        AddEditWorkoutFragment fragment = new AddEditWorkoutFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

//        ((RoomDemoApplication) getActivity().getApplication())
//                .getApplicationComponent()
//                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_edit_workout, container, false);
    }

}
