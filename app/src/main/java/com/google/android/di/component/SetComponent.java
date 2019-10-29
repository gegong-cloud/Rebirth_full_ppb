package com.google.android.di.component;

import com.google.android.mvp.ui.activity.SetActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.SetModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = SetModule.class, dependencies = AppComponent.class)
public interface SetComponent {
    void inject(SetActivity activity);
}