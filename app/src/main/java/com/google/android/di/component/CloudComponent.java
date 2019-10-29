package com.google.android.di.component;

import com.google.android.mvp.ui.fragment.CloudFragment;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.CloudModule;

import com.jess.arms.di.scope.FragmentScope;

@FragmentScope
@Component(modules = CloudModule.class, dependencies = AppComponent.class)
public interface CloudComponent {
    void inject(CloudFragment fragment);
}