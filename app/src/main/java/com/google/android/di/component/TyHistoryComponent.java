package com.google.android.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.TyHistoryModule;

import com.jess.arms.di.scope.FragmentScope;

import com.google.android.mvp.ui.fragment.TyHistoryFragment;

@FragmentScope
@Component(modules = TyHistoryModule.class, dependencies = AppComponent.class)
public interface TyHistoryComponent {
    void inject(TyHistoryFragment fragment);
}