package com.google.android.di.component;

import com.google.android.mvp.ui.activity.DisCommissionActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.DisCommissionModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = DisCommissionModule.class, dependencies = AppComponent.class)
public interface DisCommissionComponent {
    void inject(DisCommissionActivity activity);
}