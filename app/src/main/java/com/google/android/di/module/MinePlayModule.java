package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.MinePlayContract;
import com.google.android.mvp.model.MinePlayModel;
import dagger.Module;
import dagger.Provides;


@Module
public class MinePlayModule {
    private MinePlayContract.View view;

    /**
     * 构建MinePlayModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MinePlayModule(MinePlayContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MinePlayContract.View provideMinePlayView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MinePlayContract.Model provideMinePlayModel(MinePlayModel model) {
        return model;
    }
}