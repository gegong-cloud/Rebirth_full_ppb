package com.google.android.di.component;

import com.google.android.mvp.ui.activity.MyApprenticeActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.MyApprenticeModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = MyApprenticeModule.class, dependencies = AppComponent.class)
public interface MyApprenticeComponent {
    void inject(MyApprenticeActivity activity);
}