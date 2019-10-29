package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.VipQrcodeContract;
import com.google.android.mvp.model.VipQrcodeModel;
import dagger.Module;
import dagger.Provides;


@Module
public class VipQrcodeModule {
    private VipQrcodeContract.View view;

    /**
     * 构建VipQrcodeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public VipQrcodeModule(VipQrcodeContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    VipQrcodeContract.View provideVipQrcodeView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    VipQrcodeContract.Model provideVipQrcodeModel(VipQrcodeModel model) {
        return model;
    }
}