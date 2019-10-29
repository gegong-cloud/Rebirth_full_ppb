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

import com.google.android.mvp.model.back_entity.AccountDetail;
import com.google.android.mvp.model.back_entity.AccountReceipt;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.MoneyTakeDetail;
import com.google.android.mvp.model.back_entity.MyTeam;
import com.google.android.mvp.model.back_entity.RechargeDetails;
import com.google.android.mvp.model.back_entity.UserEntity;
import com.google.android.mvp.model.back_entity.UserInvite;
import com.google.android.mvp.model.upload.AccountUpload;
import com.google.android.mvp.model.upload.LoginUpload;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * ================================================
 * 存放通用的一些 API
 * <p>
 * ================================================
 */
public interface MineService {


    //获取用户信息
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/user/index")
    Observable<CommonEntity<UserEntity>> userInfo(@Body LoginUpload loginUpload);


    //4.1	收支明细
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/bonus/accountlog")
    Observable<CommonEntity<List<MoneyTakeDetail>>> accountLog();


    //4.1	用户账户明细
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/user/index")
    Observable<CommonEntity<AccountDetail>> userIndex();


    //4.2	我的一级团队
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/bonus/myteam")
    Observable<CommonEntity<List<MyTeam>>> myTeam(@Body AccountUpload accountUpload);


    //4.5	获取用户收款账号
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/user/getUserBank")
    Observable<CommonEntity<AccountReceipt>> getUserBank();



    //4.6	更新用户收款账号
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/user/updateUserBank")
    Observable<CommonEntity> updateUserBank(@Body AccountUpload accountUpload);

    //4.8	推广链接
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/user/invite")
    Observable<CommonEntity<UserInvite>> userInvite();

    //4.9	充值记录列表
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/charge/chargelog")
    Observable<CommonEntity<List<RechargeDetails>>> chargeLog(@Body AccountUpload accountUpload);



//
//
//    // 设置-查看新版本
//    @Headers({"Domain-Name: douban"})
//    @POST("/api/init/version")
//    Observable<CommonEntity<VersionEntity>> version(@Body TokenEntity tokenEntity);



}



