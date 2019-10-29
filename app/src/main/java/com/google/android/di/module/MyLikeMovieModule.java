package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.google.android.mvp.contract.MyLikeMovieContract;
import com.google.android.mvp.model.MyLikeMovieModel;


@Module
public class MyLikeMovieModule {
    private MyLikeMovieContract.View view;

    /**
     * 构建MyLikeMovieModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MyLikeMovieModule(MyLikeMovieContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MyLikeMovieContract.View provideMyLikeMovieView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MyLikeMovieContract.Model provideMyLikeMovieModel(MyLikeMovieModel model) {
        return model;
    }
}