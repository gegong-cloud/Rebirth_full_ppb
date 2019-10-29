package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.google.android.mvp.contract.LivePlayContract;
import com.google.android.mvp.model.api.service.LiveBoxService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.LiveInfo;
import com.google.android.mvp.model.upload.LiveUpload;

import io.reactivex.Observable;


@ActivityScope
public class LivePlayModel extends BaseModel implements LivePlayContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public LivePlayModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<LiveInfo>> liveView(LiveUpload liveUpload) {
        return mRepositoryManager.obtainRetrofitService(LiveBoxService.class).liveView(liveUpload);
    }
}