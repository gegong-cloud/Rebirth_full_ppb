package com.google.android.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.MyLikeMovieModule;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.ui.activity.MyLikeMovieActivity;

@ActivityScope
@Component(modules = MyLikeMovieModule.class, dependencies = AppComponent.class)
public interface MyLikeMovieComponent {
    void inject(MyLikeMovieActivity activity);
}