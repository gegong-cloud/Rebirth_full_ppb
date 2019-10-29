package com.google.android.di.component;

import com.google.android.mvp.ui.activity.MyWalletActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.MyWalletModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = MyWalletModule.class, dependencies = AppComponent.class)
public interface MyWalletComponent {
    void inject(MyWalletActivity activity);
}