package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.google.android.mvp.contract.LiveListContract;
import com.google.android.mvp.model.api.service.LiveBoxService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.JionLiveEntity;
import com.google.android.mvp.model.back_entity.LiveEntity;
import com.google.android.mvp.model.upload.LiveUpload;

import javax.inject.Inject;

import io.reactivex.Observable;


@ActivityScope
public class LiveListModel extends BaseModel implements LiveListContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public LiveListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<LiveEntity>> liveAnchors(LiveUpload liveUpload) {
        return mRepositoryManager.obtainRetrofitService(LiveBoxService.class).liveAnchors(liveUpload);
    }

    @Override
    public Observable<CommonEntity<JionLiveEntity>> checkUser() {
        return mRepositoryManager.obtainRetrofitService(LiveBoxService.class).checkUser();
    }

    @Override
    public Observable<CommonEntity<JionLiveEntity>> checkUserSpecial(String devicecode) {
        return mRepositoryManager.obtainRetrofitService(LiveBoxService.class).checkUserSpecial(devicecode);
    }
}