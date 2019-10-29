package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.google.android.mvp.contract.MineContract;
import com.google.android.mvp.model.api.service.LiveBoxService;
import com.google.android.mvp.model.api.service.LoginService;
import com.google.android.mvp.model.api.service.MineService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.JionLiveEntity;
import com.google.android.mvp.model.back_entity.UserEntity;
import com.google.android.mvp.model.upload.LoginUpload;

import javax.inject.Inject;

import io.reactivex.Observable;


@FragmentScope
public class MineModel extends BaseModel implements MineContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public MineModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<UserEntity>> userInfo(LoginUpload loginUpload) {
        return mRepositoryManager.obtainRetrofitService(MineService.class).userInfo(loginUpload);
    }

    @Override
    public Observable<CommonEntity> logout() {
        return mRepositoryManager.obtainRetrofitService(LoginService.class).logout();
    }

    @Override
    public Observable<CommonEntity<JionLiveEntity>> checkUser() {
        return mRepositoryManager.obtainRetrofitService(LiveBoxService.class).checkUser();
    }
}