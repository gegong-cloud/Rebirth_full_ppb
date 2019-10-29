package com.google.android.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.google.android.mvp.model.back_entity.BaseUrl;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.HomeAdv;
import com.google.android.mvp.model.back_entity.UserEntity;
import com.google.android.mvp.model.upload.HomeAdvUpload;
import com.google.android.mvp.model.upload.LoginUpload;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;


public interface StarUpContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        RxPermissions getRxPermissions(); //申请权限
        void setMaterContent(String materContent);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<CommonEntity<UserEntity>> userInfo(LoginUpload loginUpload);
        Observable<CommonEntity<List<HomeAdv>>> getAdvList(HomeAdvUpload homeAdvUpload);
        Observable<ResponseBody> getImgFile(String fileName);//下载文件
        Observable<CommonEntity<List<BaseUrl>>> getUrl();
    }
}
