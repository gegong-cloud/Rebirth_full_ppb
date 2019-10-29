package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.StarUpContract;
import com.google.android.mvp.model.StarUpModel;
import dagger.Module;
import dagger.Provides;


@Module
public class StarUpModule {
    private StarUpContract.View view;

    /**
     * 构建StarUpModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public StarUpModule(StarUpContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    StarUpContract.View provideStarUpView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    StarUpContract.Model provideStarUpModel(StarUpModel model) {
        return model;
    }
}