package com.google.android.di.module;

import com.jess.arms.di.scope.FragmentScope;

import com.google.android.mvp.contract.CloudContract;
import com.google.android.mvp.model.CloudModel;
import dagger.Module;
import dagger.Provides;


@Module
public class CloudModule {
    private CloudContract.View view;

    /**
     * 构建CloudModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CloudModule(CloudContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    CloudContract.View provideCloudView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    CloudContract.Model provideCloudModel(CloudModel model) {
        return model;
    }
}