package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.google.android.mvp.contract.AllSearchContract;
import com.google.android.mvp.model.AllSearchModel;


@Module
public class AllSearchModule {
    private AllSearchContract.View view;

    /**
     * 构建AllSearchModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public AllSearchModule(AllSearchContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    AllSearchContract.View provideAllSearchView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    AllSearchContract.Model provideAllSearchModel(AllSearchModel model) {
        return model;
    }
}