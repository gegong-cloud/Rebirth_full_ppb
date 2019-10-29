package com.google.android.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.AllSearchModule;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.ui.activity.AllSearchActivity;

@ActivityScope
@Component(modules = AllSearchModule.class, dependencies = AppComponent.class)
public interface AllSearchComponent {
    void inject(AllSearchActivity activity);
}