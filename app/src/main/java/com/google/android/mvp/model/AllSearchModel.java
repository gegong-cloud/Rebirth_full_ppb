package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import com.google.android.mvp.contract.AllSearchContract;
import com.google.android.mvp.model.api.service.HomeService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.SerachHistory;
import com.google.android.mvp.model.back_entity.YunboList;
import com.google.android.mvp.model.upload.ClsSearch;
import io.reactivex.Observable;


@ActivityScope
public class AllSearchModel extends BaseModel implements AllSearchContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public AllSearchModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<YunboList>> qSearch(ClsSearch clsSearch) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).qSearch(clsSearch);
    }

    @Override
    public Observable<CommonEntity<SerachHistory>> searchHot() {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).searchHot();
    }
}