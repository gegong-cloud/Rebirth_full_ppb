package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.google.android.mvp.contract.CardPayContract;
import com.google.android.mvp.model.CardPayModel;


@Module
public class CardPayModule {
    private CardPayContract.View view;

    /**
     * 构建CardPayModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CardPayModule(CardPayContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CardPayContract.View provideCardPayView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CardPayContract.Model provideCardPayModel(CardPayModel model) {
        return model;
    }
}