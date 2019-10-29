package com.google.android.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

import javax.inject.Inject;

import com.google.android.R;
import com.google.android.app.utils.RxUtils;
import com.google.android.app.utils.TextConstant;
import com.google.android.mvp.contract.AllSearchContract;
import com.google.android.mvp.model.back_entity.CommonEntity;
import com.google.android.mvp.model.back_entity.SerachHistory;
import com.google.android.mvp.model.back_entity.YunboList;
import com.google.android.mvp.model.back_entity.YunboMov;
import com.google.android.mvp.model.upload.ClsSearch;
import com.google.android.mvp.ui.activity.MinePlayActivity;
import com.google.android.mvp.ui.adapter.TagSearchAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class AllSearchPresenter extends BasePresenter<AllSearchContract.Model, AllSearchContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;


    LayoutInflater mInflater;

    //搜索关键字
    String searchText = "";

    /**
     * 搜索结果
     */
    TagSearchAdapter tagSearchAdapter;



    /**
     * 热门标签
     */
    SerachHistory.SearchWord[] hotTagList;

    /**
     * 热门标签
     */
    TagAdapter<SerachHistory.SearchWord> hotTagAdapter;


    /**
     * 搜索接口
     */
    public void qSearch(int page,String textStr, RefreshLayout refreshLayout){
        if(TextUtils.isEmpty(textStr)){
            ArmsUtils.makeText(mApplication,"请输入关键字");
            return;
        }
        searchText = textStr;
        ClsSearch tokenEntity = new ClsSearch();
        tokenEntity.setPage(""+page);
        tokenEntity.setLimit(""+TextConstant.PAGE_SIZE);
        tokenEntity.setQ(searchText);
        mModel.qSearch(tokenEntity)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                })//显示进度条
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                })
                .compose(RxUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<CommonEntity<YunboList>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<YunboList> commonEntity) {
                        loadComplete(refreshLayout);
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode())) {
                            if (commonEntity.getData().getList() != null && commonEntity.getData().getList().size() > 0) {
                                mRootView.showSearchResult(true);
                                allPage = page + 1;
                                showAdapterInfo(commonEntity.getData().getList());
//                                searchHis();
                            } else {
                                mRootView.showSearchResult(true);
                                mRootView.showNoData(loadMore);
                                loadMore = false;//加载更多没有数据的时候需要把加载更多重置
                                allPage = page;
                            }
                        } else {
                            mRootView.showSearchResult(true);
                            mRootView.showNoData(false);
                            loadMore = false;//加载更多没有数据的时候需要把加载更多重置
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }


    /**
     * 热门搜索
     */
    public void searchHot(){
        mModel.searchHot()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                })//显示进度条
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                })
                .compose(RxUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<CommonEntity<SerachHistory>>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull CommonEntity<SerachHistory> commonEntity) {
                        if (TextConstant.RequestSuccess.equals(commonEntity.getCode())&&commonEntity.getData()!=null) {
                            initHotTagAdapter(commonEntity.getData().getList());
                        } else {
                            ArmsUtils.makeText(mApplication, commonEntity.getMsg());
                        }
                    }
                });
    }





    /**
     *
     * @param list 初始化历史标签
     */
    public void initHotTagAdapter(SerachHistory.SearchWord[] list){
        if(mInflater==null){
            mInflater = LayoutInflater.from(mApplication);
        }
        if(list!=null&&list.length>0){
            hotTagList = list;
        }else{
            return;
        }
        hotTagAdapter = new TagAdapter<SerachHistory.SearchWord>(hotTagList) {
            @Override
            public View getView(FlowLayout parent, int position, SerachHistory.SearchWord hVideo) {
                TextView textView = (TextView) mInflater.inflate(R.layout.textview_search,
                        mRootView.getHotTag(), false);
                textView.setText(hVideo.getQ_word());
                return textView;
            }
        };
        mRootView.showHotAdapter(hotTagAdapter);
    }


    /**
     * 点击标签搜索
     * @param isHot
     * @param position
     * @param smartRefresh
     */
    public void tagSearch(boolean isHot, int position, SmartRefreshLayout smartRefresh){
        String tagText = "";
        if(isHot){
            if(hotTagList!=null&&hotTagList.length>position){
                tagText = hotTagList[position].getQ_word();
            }
        }else{
            if(hotTagList!=null&&hotTagList.length>position){
                tagText = hotTagList[position].getQ_word();
            }
        }
        mRootView.setClickStr(tagText);
        qSearch(1,tagText,smartRefresh);
    }



    /**
     * @param list 显示item
     */
    private void showAdapterInfo(List<YunboMov> list) {
        if (list != null && list.size() > 0) {
            if (tagSearchAdapter == null) {
                tagSearchAdapter = new TagSearchAdapter(list, mAppManager.getCurrentActivity());
                mRootView.showAdapter(tagSearchAdapter);
                tagSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        castVipVideo(list.get(position));
                    }
                });
            } else {//更新数据
                initInfo(list);
            }
        } else {
            tagSearchAdapter = null;
            mRootView.showAdapter(tagSearchAdapter);
        }
    }

    void castVipVideo(YunboMov yunboMov){
        mRootView.launchActivity(new Intent(mApplication, MinePlayActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("movId",yunboMov.getId())
        );
    }


    private void initInfo(List<YunboMov>  data) {
        if (loadMore) {
            loadMore = false;
            tagSearchAdapter.addData(data);
        } else {
            if (tagSearchAdapter != null) {
                tagSearchAdapter.replaceData(data);
            }
        }
    }


    int page = 1;//分页
    int allPage = 1;//总的页数
    boolean loadMore = false;//是否加载更多


    /**
     * @param refreshLayout 加载更多
     */
    public void loadMore(RefreshLayout refreshLayout) {
        if (page < allPage) {
            loadMore = true;
            page++;
            qSearch(page, searchText,refreshLayout);
        } else {
            loadMore = false;
            refreshLayout.finishLoadmore();
            if (tagSearchAdapter != null) {
                tagSearchAdapter.loadMoreEnd();
            }
        }
    }

    private void loadComplete(RefreshLayout refreshLayout) {
        if (refreshLayout != null) {
            if (loadMore) {//是不是加载更多
                refreshLayout.finishLoadmore();
            } else {
                page = 1;//如果是刷新，页码归一
                refreshLayout.finishRefresh();
            }
        }
    }


    @Inject
    public AllSearchPresenter(AllSearchContract.Model model, AllSearchContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}
