package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.MyWalletContract;
import com.google.android.mvp.model.MyWalletModel;
import dagger.Module;
import dagger.Provides;


@Module
public class MyWalletModule {
    private MyWalletContract.View view;

    /**
     * 构建MyWalletModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MyWalletModule(MyWalletContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MyWalletContract.View provideMyWalletView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MyWalletContract.Model provideMyWalletModel(MyWalletModel model) {
        return model;
    }
}