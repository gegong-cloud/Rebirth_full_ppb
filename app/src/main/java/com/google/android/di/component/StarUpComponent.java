package com.google.android.di.component;

import com.google.android.mvp.ui.activity.StarUpActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.StarUpModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = StarUpModule.class, dependencies = AppComponent.class)
public interface StarUpComponent {
    void inject(StarUpActivity activity);
}