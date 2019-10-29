package com.google.android.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.CardPayModule;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.ui.activity.CardPayActivity;

@ActivityScope
@Component(modules = CardPayModule.class, dependencies = AppComponent.class)
public interface CardPayComponent {
    void inject(CardPayActivity activity);
}