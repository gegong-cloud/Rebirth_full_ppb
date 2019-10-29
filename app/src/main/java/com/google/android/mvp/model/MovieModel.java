package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import java.util.List;

import javax.inject.Inject;

import com.google.android.mvp.contract.MovieContract;
import com.google.android.mvp.model.api.service.HomeService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.google.android.mvp.model.back_entity.HomeVipMov;
import com.google.android.mvp.model.back_entity.ScrollMsg;
import com.google.android.mvp.model.upload.HomeAdvUpload;
import com.google.android.mvp.model.upload.UserDeleteUpload;
import io.reactivex.Observable;


@FragmentScope
public class MovieModel extends BaseModel implements MovieContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public MovieModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<List<HomeVipMov.HomeVipMovList>>> homeIndex(HomeAdvUpload homeAdvUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).homeIndex(homeAdvUpload);
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
    public Observable<CommonEntity> userfavor(UserDeleteUpload userDeleteUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).userfavor(userDeleteUpload);
    }

    @Override
    public Observable<CommonEntity> userFavoDel(UserDeleteUpload userDeleteUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).userFavoDel(userDeleteUpload);
    }
}