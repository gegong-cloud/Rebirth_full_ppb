package com.google.android.di.component;

import com.google.android.mvp.ui.activity.LivePlayActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.LivePlayModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = LivePlayModule.class, dependencies = AppComponent.class)
public interface LivePlayComponent {
    void inject(LivePlayActivity activity);
}