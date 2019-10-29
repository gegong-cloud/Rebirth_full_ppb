package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.google.android.mvp.contract.RegContract;
import com.google.android.mvp.model.api.service.LoginService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.LoginEntity;
import com.google.android.mvp.model.upload.LoginUpload;

import javax.inject.Inject;

import io.reactivex.Observable;


@ActivityScope
public class RegModel extends BaseModel implements RegContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public RegModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<LoginEntity>> register(LoginUpload tokenEntity) {
        return mRepositoryManager.obtainRetrofitService(LoginService.class).register(tokenEntity);
    }

    @Override
    public Observable<CommonEntity<LoginEntity>> smsSend(LoginUpload tokenEntity) {
        return mRepositoryManager.obtainRetrofitService(LoginService.class).smsSend(tokenEntity);
    }
}