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

import com.google.android.mvp.model.back_entity.BannerEntity;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.JionLiveEntity;
import com.google.android.mvp.model.back_entity.LiveEntity;
import com.google.android.mvp.model.back_entity.LiveInfo;
import com.google.android.mvp.model.back_entity.VIPVideoEntity;
import com.google.android.mvp.model.upload.LiveUpload;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * ================================================
 * 直播盒子相关接口
 * <p>
 * ================================================
 */
public interface LiveBoxService {

//
//    @GET("/api/common/mobilecode")//获取手机验证码（非注册适用）
//    Observable<CommonEntity> getMobileCode(@Query("mobileCode") String mobileCode,@Query("token") String token,@Query("timestamp") String timestamp);

    //获取直播平台列表  list
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/live/index")
    Observable<CommonEntity<LiveEntity>> liveIndex();



    // 获取平台直播房间列表  list
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/live/anchors")
    Observable<CommonEntity<LiveEntity>> liveAnchors(@Body LiveUpload liveUpload);

    // 直播播放明细  list
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/live/view")
    Observable<CommonEntity<LiveInfo>> liveView(@Body LiveUpload liveUpload);



    //获取手机验证码（非注册适用）
    @Headers({"Domain-Name: douban"})
    @GET("/mobile/index/index")
    Observable<CommonEntity<BannerEntity>> bannerList();


    // 观众加入直播，校验会员是否过期
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/index/checkUser")
    Observable<CommonEntity<JionLiveEntity>> checkUser();


    // 观众加入直播，校验会员是否过期
    @Headers({"Domain-Name: douban"})
    @GET("/mobile/index/checkUserSpecial")
    Observable<CommonEntity<JionLiveEntity>> checkUserSpecial(@Query("devicecode")String devicecode);


    // vip影院url查询
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/tv/vip")
    Observable<CommonEntity<VIPVideoEntity>> tvVip(@Body LiveUpload liveUpload);



}



