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

import com.google.android.mvp.model.back_entity.BaseUrl;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.google.android.mvp.model.back_entity.HomeVipMov;
import com.google.android.mvp.model.back_entity.LabelEntity;
import com.google.android.mvp.model.back_entity.ScrollMsg;
import com.google.android.mvp.model.back_entity.SerachHistory;
import com.google.android.mvp.model.back_entity.VersionEntity;
import com.google.android.mvp.model.back_entity.YunboList;
import com.google.android.mvp.model.back_entity.YunboMov;
import com.google.android.mvp.model.upload.ClsSearch;
import com.google.android.mvp.model.upload.HomeAdvUpload;
import com.google.android.mvp.model.upload.UserDeleteUpload;
import com.google.android.mvp.model.upload.UserViewUpload;
import com.google.android.mvp.model.upload.VersionUpload;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * ================================================
 * 直播盒子相关接口
 * <p>
 * ================================================
 */
public interface HomeService {

//
//    @GET("/api/common/mobilecode")//获取手机验证码（非注册适用）
//    Observable<CommonEntity> getMobileCode(@Query("mobileCode") String mobileCode,@Query("token") String token,@Query("timestamp") String timestamp);

    //3.3	Vip影视9宫格和推荐服务  list
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/index/getUrl")
    Observable<CommonEntity<List<BaseUrl>>> getUrl();

    //3.3	Vip影视9宫格和推荐服务  list
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/tv/vipCate")
    Observable<CommonEntity<List<HomeVipMov>>> vipCate();

    //3.2	页面轮播图和广告  list
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/index/getAdvList")
    Observable<CommonEntity<List<HomeAdv>>> getAdvList(@Body HomeAdvUpload homeAdvUpload);


    //3.4	云播顶级标签
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/yunbo/top")
    Observable<CommonEntity<List<LabelEntity>>> labelTop();


    //3.4	云播分类标签列表- 云播次级标签
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/yunbo/label")
    Observable<CommonEntity<List<LabelEntity>>> labelList(@Body HomeAdvUpload homeAdvUpload);


    //3.5	云播视频列表
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/yunbo/index")
    Observable<CommonEntity<YunboList>> yunboIndex(@Body HomeAdvUpload homeAdvUpload);



    //3.5	云播播放明细
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/yunbo/view")
    Observable<CommonEntity<YunboMov>> yunboView(@Body HomeAdvUpload homeAdvUpload);


    //相似影片(猜你喜欢)
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/yunbo/similar")
    Observable<CommonEntity<YunboList>> movSimilar(@Body UserDeleteUpload userDeleteUpload);


    //3.6	获取钱包首页的的温馨提示等系统基础设置
    @Headers({"Domain-Name: douban"})//获取钱包首页的温馨提示的name为bolletinfo
    @POST("/mobile/index/getConfig")//获取滚动公告的name为topAlert
    Observable<CommonEntity<List<ScrollMsg>>> getConfig(@Body HomeAdvUpload homeAdvUpload);



    //3.6	检查更新

    @Headers({"Domain-Name: douban"})//获取钱包首页的温馨提示的name为bolletinfo
    @POST("/mobile/index/checkUpdate")//获取滚动公告的name为topAlert
    Observable<CommonEntity<VersionEntity>> checkUpdate(@Body VersionUpload versionUpload);



//    //3.8	联系客服  list
//    @Headers({"Domain-Name: douban"})
//    @GET("/mobile/index/getConfig?name=serviceUrl")
//    Observable<CommonEntity<List<ServiceAddress>>> getServiceUrl();

    //3.9	首页内容
    @Headers({"Domain-Name: douban"})//获取钱包首页的温馨提示的name为bolletinfo
    @POST("/mobile/home/index")//获取滚动公告的name为topAlert
    Observable<CommonEntity<List<HomeVipMov.HomeVipMovList>>> homeIndex(@Body HomeAdvUpload homeAdvUpload);


    // 广告点击日志
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/log/ad")
    Observable<CommonEntity> logAd(@Body HomeAdvUpload homeAdvUpload);

    // 失败日志
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/log/error")
    Observable<CommonEntity> logError(@Body HomeAdvUpload homeAdvUpload);


    // 3.5 云播搜索
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/yunbo/search")
    Observable<CommonEntity<YunboList>> qSearch(@Body ClsSearch clsSearch);


    // 3.6 搜索热门列表
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/yunbo/hot")
    Observable<CommonEntity<SerachHistory>> searchHot();

    //浏览记录
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/yunbo/his")
    Observable<CommonEntity<YunboList>> userView(@Body UserViewUpload userViewUpload);


    //删除浏览记录
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/yunbo/his/clean")
    Observable<CommonEntity> userViewDel();

    //收藏记录
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/yunbo/favo/index")
    Observable<CommonEntity<YunboList>> userFavo(@Body UserViewUpload userViewUpload);


    //云播收藏
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/yunbo/favo")
    Observable<CommonEntity> userfavor(@Body UserDeleteUpload userDeleteUpload);


    //删除收藏记录
    @Headers({"Domain-Name: douban"})
    @POST("/mobile/yunbo/favo/del")
    Observable<CommonEntity> userFavoDel(@Body UserDeleteUpload userDeleteUpload);
}



