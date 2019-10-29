package com.google.android.di.component;

import com.google.android.mvp.ui.activity.VipQrcodeActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.VipQrcodeModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = VipQrcodeModule.class, dependencies = AppComponent.class)
public interface VipQrcodeComponent {
    void inject(VipQrcodeActivity activity);
}