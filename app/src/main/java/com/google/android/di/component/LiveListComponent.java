package com.google.android.di.component;

import com.google.android.mvp.ui.activity.LiveListActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.LiveListModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = LiveListModule.class, dependencies = AppComponent.class)
public interface LiveListComponent {
    void inject(LiveListActivity activity);
}