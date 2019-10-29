package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.ForgetPwdContract;
import com.google.android.mvp.model.ForgetPwdModel;
import dagger.Module;
import dagger.Provides;


@Module
public class ForgetPwdModule {
    private ForgetPwdContract.View view;

    /**
     * 构建ForgetPwdModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ForgetPwdModule(ForgetPwdContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ForgetPwdContract.View provideForgetPwdView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ForgetPwdContract.Model provideForgetPwdModel(ForgetPwdModel model) {
        return model;
    }
}