package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.google.android.mvp.contract.VipVideoContract;
import com.google.android.mvp.model.api.service.LiveBoxService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.VIPVideoEntity;
import com.google.android.mvp.model.upload.LiveUpload;

import javax.inject.Inject;

import io.reactivex.Observable;


@ActivityScope
public class VipVideoModel extends BaseModel implements VipVideoContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public VipVideoModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<VIPVideoEntity>> tvVip(LiveUpload liveUpload) {
        return mRepositoryManager.obtainRetrofitService(LiveBoxService.class).tvVip(liveUpload);
    }
}