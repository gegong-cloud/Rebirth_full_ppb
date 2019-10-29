package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.google.android.mvp.contract.MainContract;
import com.google.android.mvp.model.api.service.HomeService;
import com.google.android.mvp.model.back_entity.BaseUrl;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.ScrollMsg;
import com.google.android.mvp.model.back_entity.VersionEntity;
import com.google.android.mvp.model.upload.HomeAdvUpload;
import com.google.android.mvp.model.upload.VersionUpload;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;


@ActivityScope
public class MainModel extends BaseModel implements MainContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public MainModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<VersionEntity>> checkUpdate(VersionUpload versionUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).checkUpdate(versionUpload);
    }

    @Override
    public Observable<CommonEntity<List<BaseUrl>>> getUrl() {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).getUrl();
    }

    @Override
    public Observable<CommonEntity<List<ScrollMsg>>> getConfig(HomeAdvUpload homeAdvUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).getConfig(homeAdvUpload);
    }

    @Override
    public Observable<CommonEntity> logAd(HomeAdvUpload homeAdvUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).logAd(homeAdvUpload);
    }

    @Override
    public Observable<CommonEntity> logError(HomeAdvUpload homeAdvUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).logError(homeAdvUpload);
    }
}