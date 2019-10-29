package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import com.google.android.mvp.contract.MyLikeMovieContract;
import com.google.android.mvp.model.api.service.HomeService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.YunboList;
import com.google.android.mvp.model.upload.UserDeleteUpload;
import com.google.android.mvp.model.upload.UserViewUpload;
import io.reactivex.Observable;


@ActivityScope
public class MyLikeMovieModel extends BaseModel implements MyLikeMovieContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public MyLikeMovieModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<YunboList>> userFavo(UserViewUpload userViewUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).userFavo(userViewUpload);
    }

    @Override
    public Observable<CommonEntity> userFavoDel(UserDeleteUpload userDeleteUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).userFavoDel(userDeleteUpload);
    }
}