package com.google.android.di.component;

import com.google.android.mvp.ui.fragment.LiveFragment;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.LiveModule;

import com.jess.arms.di.scope.FragmentScope;

@FragmentScope
@Component(modules = LiveModule.class, dependencies = AppComponent.class)
public interface LiveComponent {
    void inject(LiveFragment fragment);
}