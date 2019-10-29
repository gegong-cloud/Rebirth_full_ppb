package com.google.android.di.component;

import com.google.android.mvp.ui.activity.ChooseAccoutActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.ChooseAccoutModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = ChooseAccoutModule.class, dependencies = AppComponent.class)
public interface ChooseAccoutComponent {
    void inject(ChooseAccoutActivity activity);
}