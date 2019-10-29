package com.google.android.di.module;

import com.jess.arms.di.scope.FragmentScope;

import com.google.android.mvp.contract.LiveContract;
import com.google.android.mvp.model.LiveModel;
import dagger.Module;
import dagger.Provides;


@Module
public class LiveModule {
    private LiveContract.View view;

    /**
     * 构建LiveModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public LiveModule(LiveContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    LiveContract.View provideLiveView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    LiveContract.Model provideLiveModel(LiveModel model) {
        return model;
    }
}