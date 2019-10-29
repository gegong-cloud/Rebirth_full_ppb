package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.google.android.mvp.contract.HomeContract;
import com.google.android.mvp.model.api.service.HomeService;
import com.google.android.mvp.model.api.service.LiveBoxService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.google.android.mvp.model.back_entity.HomeVipMov;
import com.google.android.mvp.model.back_entity.JionLiveEntity;
import com.google.android.mvp.model.back_entity.LiveEntity;
import com.google.android.mvp.model.back_entity.ScrollMsg;
import com.google.android.mvp.model.upload.HomeAdvUpload;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;


@FragmentScope
public class HomeModel extends BaseModel implements HomeContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public HomeModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<List<HomeVipMov>>> vipCate() {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).vipCate();
    }

    @Override
    public Observable<CommonEntity<List<HomeAdv>>> getAdvList(HomeAdvUpload homeAdvUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).getAdvList(homeAdvUpload);
    }

    @Override
    public Observable<CommonEntity<List<ScrollMsg>>> getConfig(HomeAdvUpload homeAdvUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).getConfig(homeAdvUpload);
    }

    @Override
    public Observable<CommonEntity<JionLiveEntity>> checkUser() {
        return mRepositoryManager.obtainRetrofitService(LiveBoxService.class).checkUser();
    }

    @Override
    public Observable<CommonEntity<LiveEntity>> liveIndex() {
        return mRepositoryManager.obtainRetrofitService(LiveBoxService.class).liveIndex();
    }
}