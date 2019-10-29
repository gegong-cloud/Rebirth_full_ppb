package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.LiveListContract;
import com.google.android.mvp.model.LiveListModel;
import dagger.Module;
import dagger.Provides;


@Module
public class LiveListModule {
    private LiveListContract.View view;

    /**
     * 构建LiveListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public LiveListModule(LiveListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    LiveListContract.View provideLiveListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    LiveListContract.Model provideLiveListModel(LiveListModel model) {
        return model;
    }
}