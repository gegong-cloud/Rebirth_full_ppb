package com.google.android.di.component;

import com.google.android.mvp.ui.fragment.VedioEmptyFragment;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.VedioEmptyModule;

import com.jess.arms.di.scope.FragmentScope;

@FragmentScope
@Component(modules = VedioEmptyModule.class, dependencies = AppComponent.class)
public interface VedioEmptyComponent {
    void inject(VedioEmptyFragment fragment);
}