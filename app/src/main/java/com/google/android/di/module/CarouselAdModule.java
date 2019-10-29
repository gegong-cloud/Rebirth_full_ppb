package com.google.android.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.google.android.mvp.contract.CarouselAdContract;
import com.google.android.mvp.model.CarouselAdModel;
import dagger.Module;
import dagger.Provides;


@Module
public class CarouselAdModule {
    private CarouselAdContract.View view;

    /**
     * 构建CarouselAdModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CarouselAdModule(CarouselAdContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CarouselAdContract.View provideCarouselAdView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CarouselAdContract.Model provideCarouselAdModel(CarouselAdModel model) {
        return model;
    }
}