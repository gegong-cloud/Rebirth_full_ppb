package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import com.google.android.mvp.contract.TyHistoryContract;
import com.google.android.mvp.model.api.service.HomeService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.YunboList;
import com.google.android.mvp.model.upload.UserViewUpload;
import io.reactivex.Observable;


@FragmentScope
public class TyHistoryModel extends BaseModel implements TyHistoryContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public TyHistoryModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<YunboList>> userView(UserViewUpload userViewUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).userView(userViewUpload);
    }

    @Override
    public Observable<CommonEntity> userViewDel() {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).userViewDel();
    }
}