package com.google.android.di.component;

import com.google.android.mvp.ui.activity.CarouselAdActivity;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.CarouselAdModule;

import com.jess.arms.di.scope.ActivityScope;

@ActivityScope
@Component(modules = CarouselAdModule.class, dependencies = AppComponent.class)
public interface CarouselAdComponent {
    void inject(CarouselAdActivity activity);
}