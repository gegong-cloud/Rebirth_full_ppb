package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.UpdatePwdContract;
import com.google.android.mvp.model.UpdatePwdModel;
import dagger.Module;
import dagger.Provides;


@Module
public class UpdatePwdModule {
    private UpdatePwdContract.View view;

    /**
     * 构建UpdatePwdModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public UpdatePwdModule(UpdatePwdContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    UpdatePwdContract.View provideUpdatePwdView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    UpdatePwdContract.Model provideUpdatePwdModel(UpdatePwdModel model) {
        return model;
    }
}