package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.DisCommissionContract;
import com.google.android.mvp.model.DisCommissionModel;
import dagger.Module;
import dagger.Provides;


@Module
public class DisCommissionModule {
    private DisCommissionContract.View view;

    /**
     * 构建DisCommissionModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DisCommissionModule(DisCommissionContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    DisCommissionContract.View provideDisCommissionView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    DisCommissionContract.Model provideDisCommissionModel(DisCommissionModel model) {
        return model;
    }
}