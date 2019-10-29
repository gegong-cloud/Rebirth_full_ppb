package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.CloudListContract;
import com.google.android.mvp.model.CloudListModel;
import dagger.Module;
import dagger.Provides;


@Module
public class CloudListModule {
    private CloudListContract.View view;

    /**
     * 构建CloudListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CloudListModule(CloudListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CloudListContract.View provideCloudListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CloudListContract.Model provideCloudListModel(CloudListModel model) {
        return model;
    }
}