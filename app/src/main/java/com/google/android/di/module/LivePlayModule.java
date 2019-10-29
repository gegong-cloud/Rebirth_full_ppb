package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.LivePlayContract;
import com.google.android.mvp.model.LivePlayModel;
import dagger.Module;
import dagger.Provides;


@Module
public class LivePlayModule {
    private LivePlayContract.View view;

    /**
     * 构建LivePlayModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public LivePlayModule(LivePlayContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    LivePlayContract.View provideLivePlayView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    LivePlayContract.Model provideLivePlayModel(LivePlayModel model) {
        return model;
    }
}