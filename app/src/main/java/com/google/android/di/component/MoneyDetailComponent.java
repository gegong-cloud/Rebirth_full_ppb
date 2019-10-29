package com.google.android.di.component;

import com.google.android.mvp.ui.activity.MoneyDetailActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.MoneyDetailModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = MoneyDetailModule.class, dependencies = AppComponent.class)
public interface MoneyDetailComponent {
    void inject(MoneyDetailActivity activity);
}