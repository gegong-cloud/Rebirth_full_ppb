package com.google.android.di.component;

import com.google.android.di.module.VipVideoModule;
import com.google.android.mvp.ui.activity.VipVideoActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = VipVideoModule.class, dependencies = AppComponent.class)
public interface VipVideoComponent {
    void inject(VipVideoActivity activity);
}