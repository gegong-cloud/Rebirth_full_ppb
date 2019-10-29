package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.OfflineRechargeContract;
import com.google.android.mvp.model.OfflineRechargeModel;
import dagger.Module;
import dagger.Provides;


@Module
public class OfflineRechargeModule {
    private OfflineRechargeContract.View view;

    /**
     * 构建OfflineRechargeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public OfflineRechargeModule(OfflineRechargeContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    OfflineRechargeContract.View provideOfflineRechargeView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    OfflineRechargeContract.Model provideOfflineRechargeModel(OfflineRechargeModel model) {
        return model;
    }
}