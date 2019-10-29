package com.google.android.di.component;

import com.google.android.mvp.ui.activity.MainActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.MainModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = MainModule.class, dependencies = AppComponent.class)
public interface MainComponent {
    void inject(MainActivity activity);
}