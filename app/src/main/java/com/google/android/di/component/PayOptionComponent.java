package com.google.android.di.component;

import com.google.android.mvp.ui.activity.PayOptionActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.PayOptionModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = PayOptionModule.class, dependencies = AppComponent.class)
public interface PayOptionComponent {
    void inject(PayOptionActivity activity);
}