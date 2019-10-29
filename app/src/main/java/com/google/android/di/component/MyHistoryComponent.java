package com.google.android.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.MyHistoryModule;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.ui.activity.MyHistoryActivity;

@ActivityScope
@Component(modules = MyHistoryModule.class, dependencies = AppComponent.class)
public interface MyHistoryComponent {
    void inject(MyHistoryActivity activity);
}