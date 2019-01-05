package com.axfex.dorkout.views.reminder;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;

import com.axfex.dorkout.R;
import com.axfex.dorkout.util.BaseActivity;

public class ReminderActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ReminderFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    public ViewModel getViewModel() {
        return null;
    }
}
