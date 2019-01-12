package com.axfex.dorkout.vm;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * Created by RV on 19/07/17.
 * <p>
 * A provider factory that persists ViewModels {@link ViewModel}.
 * Used if the view model has a parameterized constructor.
 */
public class ViewModelFactory<V> implements ViewModelProvider.Factory {

    private V viewModel;

    public ViewModelFactory(V viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(viewModel.getClass())) {
            return Objects.requireNonNull(modelClass.cast(viewModel));
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}