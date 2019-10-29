package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.ChooseAccoutContract;
import com.google.android.mvp.model.ChooseAccoutModel;
import dagger.Module;
import dagger.Provides;


@Module
public class ChooseAccoutModule {
    private ChooseAccoutContract.View view;

    /**
     * 构建ChooseAccoutModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ChooseAccoutModule(ChooseAccoutContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ChooseAccoutContract.View provideChooseAccoutView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ChooseAccoutContract.Model provideChooseAccoutModel(ChooseAccoutModel model) {
        return model;
    }
}