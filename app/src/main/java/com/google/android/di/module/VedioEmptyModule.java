package com.google.android.di.module;

import com.jess.arms.di.scope.FragmentScope;

import com.google.android.mvp.contract.VedioEmptyContract;
import com.google.android.mvp.model.VedioEmptyModel;
import dagger.Module;
import dagger.Provides;


@Module
public class VedioEmptyModule {
    private VedioEmptyContract.View view;

    /**
     * 构建VedioEmptyModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public VedioEmptyModule(VedioEmptyContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    VedioEmptyContract.View provideVedioEmptyView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    VedioEmptyContract.Model provideVedioEmptyModel(VedioEmptyModel model) {
        return model;
    }
}