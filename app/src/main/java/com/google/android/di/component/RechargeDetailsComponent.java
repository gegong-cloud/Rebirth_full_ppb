package com.google.android.di.component;

import com.google.android.mvp.ui.activity.RechargeDetailsActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.RechargeDetailsModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = RechargeDetailsModule.class, dependencies = AppComponent.class)
public interface RechargeDetailsComponent {
    void inject(RechargeDetailsActivity activity);
}