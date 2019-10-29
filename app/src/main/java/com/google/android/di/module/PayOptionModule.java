package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.PayOptionContract;
import com.google.android.mvp.model.PayOptionModel;
import dagger.Module;
import dagger.Provides;


@Module
public class PayOptionModule {
    private PayOptionContract.View view;

    /**
     * 构建PayOptionModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PayOptionModule(PayOptionContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PayOptionContract.View providePayOptionView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PayOptionContract.Model providePayOptionModel(PayOptionModel model) {
        return model;
    }
}