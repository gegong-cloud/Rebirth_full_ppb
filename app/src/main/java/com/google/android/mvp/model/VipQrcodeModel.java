package com.google.android.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.google.android.mvp.contract.VipQrcodeContract;
import com.google.android.mvp.model.api.service.LoginService;
import com.google.android.mvp.model.api.service.PayService;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.PaySuccessResult;
import com.google.android.mvp.model.upload.TokenEntity;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;


@ActivityScope
public class VipQrcodeModel extends BaseModel implements VipQrcodeContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public VipQrcodeModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<CommonEntity<PaySuccessResult>> rechargeQuery(TokenEntity tokenEntity) {
        return mRepositoryManager.obtainRetrofitService(PayService.class).rechargeQuery(tokenEntity);
    }

    @Override
    public Observable<CommonEntity> unlockMoney(TokenEntity tokenEntity) {
        return mRepositoryManager.obtainRetrofitService(PayService.class).unlockMoney(tokenEntity);
    }

    @Override
    public Observable<ResponseBody> getImgFile(String fileName) {
        return mRepositoryManager.obtainRetrofitService(LoginService.class).getImg(fileName);
    }
}