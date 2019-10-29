package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.PayCodeShowContract;
import com.google.android.mvp.model.PayCodeShowModel;
import dagger.Module;
import dagger.Provides;


@Module
public class PayCodeShowModule {
    private PayCodeShowContract.View view;

    /**
     * 构建PayCodeShowModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PayCodeShowModule(PayCodeShowContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PayCodeShowContract.View providePayCodeShowView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PayCodeShowContract.Model providePayCodeShowModel(PayCodeShowModel model) {
        return model;
    }
}