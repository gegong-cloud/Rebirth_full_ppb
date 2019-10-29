package com.google.android.di.component;

import com.google.android.mvp.ui.fragment.MineFragment;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.MineModule;

import com.jess.arms.di.scope.FragmentScope;

@FragmentScope
@Component(modules = MineModule.class, dependencies = AppComponent.class)
public interface MineComponent {
    void inject(MineFragment fragment);
}