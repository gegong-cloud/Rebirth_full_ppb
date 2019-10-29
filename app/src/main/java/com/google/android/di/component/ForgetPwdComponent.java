package com.google.android.di.component;

import com.google.android.mvp.ui.activity.ForgetPwdActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.ForgetPwdModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = ForgetPwdModule.class, dependencies = AppComponent.class)
public interface ForgetPwdComponent {
    void inject(ForgetPwdActivity activity);
}