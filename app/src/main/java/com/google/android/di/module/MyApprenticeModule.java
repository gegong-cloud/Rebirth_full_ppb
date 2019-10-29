package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.MyApprenticeContract;
import com.google.android.mvp.model.MyApprenticeModel;
import dagger.Module;
import dagger.Provides;


@Module
public class MyApprenticeModule {
    private MyApprenticeContract.View view;

    /**
     * 构建MyApprenticeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MyApprenticeModule(MyApprenticeContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MyApprenticeContract.View provideMyApprenticeView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MyApprenticeContract.Model provideMyApprenticeModel(MyApprenticeModel model) {
        return model;
    }
}