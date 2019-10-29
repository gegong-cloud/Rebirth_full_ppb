package com.google.android.di.module;

import com.jess.arms.di.scope.FragmentScope;

import com.google.android.mvp.contract.MineContract;
import com.google.android.mvp.model.MineModel;
import dagger.Module;
import dagger.Provides;


@Module
public class MineModule {
    private MineContract.View view;

    /**
     * 构建MineModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MineModule(MineContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    MineContract.View provideMineView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    MineContract.Model provideMineModel(MineModel model) {
        return model;
    }
}