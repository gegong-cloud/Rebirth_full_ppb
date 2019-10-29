package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.RegContract;
import com.google.android.mvp.model.RegModel;
import dagger.Module;
import dagger.Provides;


@Module
public class RegModule {
    private RegContract.View view;

    /**
     * 构建RegModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public RegModule(RegContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    RegContract.View provideRegView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    RegContract.Model provideRegModel(RegModel model) {
        return model;
    }
}