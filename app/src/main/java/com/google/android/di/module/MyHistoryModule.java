package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.google.android.mvp.contract.MyHistoryContract;
import com.google.android.mvp.model.MyHistoryModel;


@Module
public class MyHistoryModule {
    private MyHistoryContract.View view;

    /**
     * 构建MyHistoryModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MyHistoryModule(MyHistoryContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MyHistoryContract.View provideMyHistoryView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MyHistoryContract.Model provideMyHistoryModel(MyHistoryModel model) {
        return model;
    }
}