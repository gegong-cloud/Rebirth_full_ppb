package com.google.android.di.module;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

import com.google.android.mvp.contract.TyHistoryContract;
import com.google.android.mvp.model.TyHistoryModel;


@Module
public class TyHistoryModule {
    private TyHistoryContract.View view;

    /**
     * 构建TyHistoryModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public TyHistoryModule(TyHistoryContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    TyHistoryContract.View provideTyHistoryView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    TyHistoryContract.Model provideTyHistoryModel(TyHistoryModel model) {
        return model;
    }
}