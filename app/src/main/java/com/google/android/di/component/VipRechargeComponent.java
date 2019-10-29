package com.google.android.di.component;

import com.google.android.mvp.ui.activity.VipRechargeActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.VipRechargeModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = VipRechargeModule.class, dependencies = AppComponent.class)
public interface VipRechargeComponent {
    void inject(VipRechargeActivity activity);
}