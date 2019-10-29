package com.google.android.di.component;

import com.google.android.mvp.ui.activity.MinePlayActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.MinePlayModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = MinePlayModule.class, dependencies = AppComponent.class)
public interface MinePlayComponent {
    void inject(MinePlayActivity activity);
}