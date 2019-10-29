package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.google.android.mvp.contract.CloudContract;
import com.google.android.mvp.model.api.service.HomeService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.google.android.mvp.model.back_entity.LabelEntity;
import com.google.android.mvp.model.upload.HomeAdvUpload;

import java.util.List;

import javax.inject.Inject;

import com.google.android.mvp.model.upload.UserDeleteUpload;
import io.reactivex.Observable;


@FragmentScope
public class CloudModel extends BaseModel implements CloudContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public CloudModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<List<HomeAdv>>> getAdvList(HomeAdvUpload homeAdvUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).getAdvList(homeAdvUpload);
    }

    @Override
    public Observable<CommonEntity<List<LabelEntity>>> labelTop() {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).labelTop();
    }

    @Override
    public Observable<CommonEntity<List<LabelEntity>>> labelList(HomeAdvUpload homeAdvUpload) {
        return mRepositoryManager.obtainRetrofitService(HomeService.class).labelList(homeAdvUpload);
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