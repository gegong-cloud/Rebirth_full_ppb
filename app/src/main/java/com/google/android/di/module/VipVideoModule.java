package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.VipVideoContract;
import com.google.android.mvp.model.VipVideoModel;
import dagger.Module;
import dagger.Provides;


@Module
public class VipVideoModule {
    private VipVideoContract.View view;

    /**
     * 构建VipVideoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public VipVideoModule(VipVideoContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    VipVideoContract.View provideVipVideoView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    VipVideoContract.Model provideVipVideoModel(VipVideoModel model) {
        return model;
    }
}