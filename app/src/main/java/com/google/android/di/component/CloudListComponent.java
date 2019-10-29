package com.google.android.di.component;

import com.google.android.mvp.ui.activity.CloudListActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.CloudListModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = CloudListModule.class, dependencies = AppComponent.class)
public interface CloudListComponent {
    void inject(CloudListActivity activity);
}