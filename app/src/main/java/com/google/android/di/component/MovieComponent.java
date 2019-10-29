package com.google.android.di.component;

import com.google.android.mvp.ui.fragment.MovieFragment;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.google.android.di.module.MovieModule;

import com.jess.arms.di.scope.FragmentScope;

@FragmentScope
@Component(modules = MovieModule.class, dependencies = AppComponent.class)
public interface MovieComponent {
    void inject(MovieFragment fragment);
}