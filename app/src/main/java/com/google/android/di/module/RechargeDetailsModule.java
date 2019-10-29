package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.RechargeDetailsContract;
import com.google.android.mvp.model.RechargeDetailsModel;
import dagger.Module;
import dagger.Provides;


@Module
public class RechargeDetailsModule {
    private RechargeDetailsContract.View view;

    /**
     * 构建RechargeDetailsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public RechargeDetailsModule(RechargeDetailsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    RechargeDetailsContract.View provideRechargeDetailsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    RechargeDetailsContract.Model provideRechargeDetailsModel(RechargeDetailsModel model) {
        return model;
    }
}