package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.SetContract;
import com.google.android.mvp.model.SetModel;
import dagger.Module;
import dagger.Provides;


@Module
public class SetModule {
    private SetContract.View view;

    /**
     * 构建SetModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SetModule(SetContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SetContract.View provideSetView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SetContract.Model provideSetModel(SetModel model) {
        return model;
    }
}