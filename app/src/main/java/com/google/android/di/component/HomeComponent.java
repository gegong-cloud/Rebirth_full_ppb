package com.google.android.di.component;

import com.google.android.mvp.ui.fragment.HomeFragment;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.HomeModule;

import com.jess.arms.di.scope.FragmentScope;

@FragmentScope
@Component(modules = HomeModule.class, dependencies = AppComponent.class)
public interface HomeComponent {
    void inject(HomeFragment fragment);
}