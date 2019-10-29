package com.google.android.di.component;

import com.google.android.mvp.ui.activity.OfflineRechargeActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.OfflineRechargeModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = OfflineRechargeModule.class, dependencies = AppComponent.class)
public interface OfflineRechargeComponent {
    void inject(OfflineRechargeActivity activity);
}