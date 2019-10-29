package com.google.android.di.component;

import com.google.android.mvp.ui.activity.PayCodeShowActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.PayCodeShowModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = PayCodeShowModule.class, dependencies = AppComponent.class)
public interface PayCodeShowComponent {
    void inject(PayCodeShowActivity activity);
}