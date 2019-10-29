package com.google.android.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.SerachHistory;
import com.google.android.mvp.model.back_entity.YunboList;
import com.google.android.mvp.model.upload.ClsSearch;
import com.google.android.mvp.ui.adapter.TagSearchAdapter;
import io.reactivex.Observable;


public interface AllSearchContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void showSearchResult(boolean isShow);
        void showHotAdapter(TagAdapter<SerachHistory.SearchWord> tagAdapter);
        TagFlowLayout getHotTag();
        void showAdapter(TagSearchAdapter tagSearchAdapter);
        void showNoData(boolean dataFlag);
        void setClickStr(String textStr);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<CommonEntity<YunboList>> qSearch(ClsSearch clsSearch);
        Observable<CommonEntity<SerachHistory>> searchHot();
    }
}
