package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.VipRechargeContract;
import com.google.android.mvp.model.VipRechargeModel;
import dagger.Module;
import dagger.Provides;


@Module
public class VipRechargeModule {
    private VipRechargeContract.View view;

    /**
     * 构建VipRechargeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public VipRechargeModule(VipRechargeContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    VipRechargeContract.View provideVipRechargeView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    VipRechargeContract.Model provideVipRechargeModel(VipRechargeModel model) {
        return model;
    }
}