package com.google.android.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.R;
import com.google.android.app.utils.StringUtils;
import com.google.android.di.component.DaggerAllSearchComponent;
import com.google.android.di.module.AllSearchModule;
import com.google.android.mvp.contract.AllSearchContract;
import com.google.android.mvp.model.back_entity.SerachHistory;
import com.google.android.mvp.presenter.AllSearchPresenter;
import com.google.android.mvp.ui.adapter.TagSearchAdapter;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 搜索按钮
 */
public class AllSearchActivity extends BaseActivity<AllSearchPresenter> implements AllSearchContract.View {

    @BindView(R.id.all_search_edit)
    EditText allSearchEdit;
    @BindView(R.id.all_search_hot)
    RelativeLayout allSearchHot;
    @BindView(R.id.all_search_hot_tag)
    TagFlowLayout allSearchHotTag;
    @BindView(R.id.all_search_recyclerview)
    RecyclerView allSearchRecyclerview;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout smartRefresh;
    @BindView(R.id.all_search_nested)
    NestedScrollView allSearchNested;
    @BindView(R.id.all_search_nodata)
    RelativeLayout allSearchNodata;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAllSearchComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .allSearchModule(new AllSearchModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_all_search; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.searchHot();
        //刷新加载更多
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPresenter.qSearch(1, allSearchEdit.getText().toString(), refreshLayout);
            }
        });
        smartRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mPresenter.loadMore(smartRefresh);
            }
        });
        //键盘搜索
        allSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {//搜索按键action
                    StringUtils.hideKeyboard(allSearchEdit, AllSearchActivity.this);
                    String content = allSearchEdit.getText().toString();
                    if (TextUtils.isEmpty(content)) {
                        return true;
                    }
                    mPresenter.qSearch(1, content, smartRefresh);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }


    @OnClick({R.id.all_search_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.all_search_cancel:
                killMyself();
                break;
        }
    }

    @Override
    public void showSearchResult(boolean isShow) {
        allSearchNested.setVisibility(isShow ? View.GONE : View.VISIBLE);
        smartRefresh.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showNoData(boolean dataFlag) {
        smartRefresh.setVisibility(dataFlag ? View.VISIBLE : View.GONE);
        allSearchNodata.setVisibility(dataFlag ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setClickStr(String textStr) {
        allSearchEdit.setText(!TextUtils.isEmpty(textStr)?textStr:"");
    }

    @Override
    public void showHotAdapter(TagAdapter<SerachHistory.SearchWord> tagAdapter) {
        allSearchHotTag.setMaxSelectCount(1);
        allSearchHotTag.setAdapter(tagAdapter);
        allSearchHotTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                mPresenter.tagSearch(true, position, smartRefresh);
                return false;
            }
        });
    }

    @Override
    public TagFlowLayout getHotTag() {
        return allSearchHotTag;
    }


    @Override
    public void showAdapter(TagSearchAdapter tagSearchAdapter) {
        allSearchRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        allSearchRecyclerview.setAdapter(tagSearchAdapter);
    }

}
