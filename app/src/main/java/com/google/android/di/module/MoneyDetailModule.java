package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.MoneyDetailContract;
import com.google.android.mvp.model.MoneyDetailModel;
import dagger.Module;
import dagger.Provides;


@Module
public class MoneyDetailModule {
    private MoneyDetailContract.View view;

    /**
     * 构建MoneyDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MoneyDetailModule(MoneyDetailContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MoneyDetailContract.View provideMoneyDetailView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MoneyDetailContract.Model provideMoneyDetailModel(MoneyDetailModel model) {
        return model;
    }
}