/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.mvp.model.api.service;

import java.util.List;

import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.DisCommission;
import com.google.android.mvp.model.back_entity.HornEntity;
import com.google.android.mvp.model.back_entity.MoneyShow;
import com.google.android.mvp.model.back_entity.PayMoneyResult;
import com.google.android.mvp.model.back_entity.PaySuccessResult;
import com.google.android.mvp.model.back_entity.UserEntity;
import com.google.android.mvp.model.upload.CardPayUpload;
import com.google.android.mvp.model.upload.TokenEntity;
import com.google.android.mvp.model.upload.WithDrowAppUpload;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * ================================================
 * 支付选择接口
 * <p>
 * ================================================
 */
public interface PayService {


    //1.1	支付方式列表
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/charge/interfaceList")
    Observable<CommonEntity<List<MoneyShow>>> chargeInterfaceList();


    //1.2	支付金额列表
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/charge/index")
    Observable<CommonEntity<List<MoneyShow>>> chargeIndex(@Body TokenEntity tokenEntity);

    // 1.3	发起支付（预支付）
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/charge/recharge")
    Observable<CommonEntity<PayMoneyResult>> recharge(@Body TokenEntity tokenEntity);


    // 1.4	支付结果查询
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/charge/rechargeQuery")
    Observable<CommonEntity<PaySuccessResult>> rechargeQuery(@Body TokenEntity tokenEntity);


    // 1.4	1.5	卡密充值接口
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/card/recharge")
    Observable<CommonEntity<UserEntity>> cardRecharge(@Body TokenEntity tokenEntity);


    // 2.1	分销提成
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/bonus/index")
    Observable<CommonEntity<DisCommission>> bonusIndex();

    // 2.4	申请提现
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/withdrow/apply")
    Observable<CommonEntity> withDrowApply(@Body WithDrowAppUpload withDrowAppUpload);


    // 2.4	申请提现
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/charge/unlockMoney")
    Observable<CommonEntity> unlockMoney(@Body TokenEntity tokenEntity);


    // 支付小喇叭
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/charge/broadcast")
    Observable<CommonEntity<List<HornEntity>>> broadcast();


    // 支付小喇叭
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/charge/cardpay")
    Observable<CommonEntity> cardPay(@Body CardPayUpload cardPayUpload);

}



