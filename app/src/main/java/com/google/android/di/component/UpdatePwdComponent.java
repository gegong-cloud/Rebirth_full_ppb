package com.google.android.di.component;

import com.google.android.mvp.ui.activity.UpdatePwdActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.UpdatePwdModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = UpdatePwdModule.class, dependencies = AppComponent.class)
public interface UpdatePwdComponent {
    void inject(UpdatePwdActivity activity);
}