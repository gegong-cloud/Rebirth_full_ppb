package com.google.android.di.component;

import com.google.android.mvp.ui.activity.RegActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.RegModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = RegModule.class, dependencies = AppComponent.class)
public interface RegComponent {
    void inject(RegActivity activity);
}