package com.google.android.di.component;

import com.google.android.mvp.ui.activity.LoginActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.LoginModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = LoginModule.class, dependencies = AppComponent.class)
public interface LoginComponent {
    void inject(LoginActivity activity);
}