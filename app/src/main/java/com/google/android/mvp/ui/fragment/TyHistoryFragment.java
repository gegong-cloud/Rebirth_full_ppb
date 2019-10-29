package com.google.android.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.di.component.DaggerTyHistoryComponent;
import com.google.android.di.module.TyHistoryModule;
import com.google.android.mvp.contract.TyHistoryContract;
import com.google.android.mvp.presenter.TyHistoryPresenter;
import com.google.android.mvp.ui.adapter.HistoryAdapter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class TyHistoryFragment extends BaseFragment<TyHistoryPresenter> implements TyHistoryContract.View {


    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout smartRefresh;
    @BindView(R.id.history_bottom)
    LinearLayout historyBottom;
    @BindView(R.id.history_bottom_all)
    TextView historyBottomAll;
    Unbinder unbinder;


    private int type = 1;//1今天，2 7日，3更早

    public static TyHistoryFragment newInstance() {
        TyHistoryFragment fragment = new TyHistoryFragment();
        return fragment;
    }

    public static TyHistoryFragment newInstance(int typeId) {
        TyHistoryFragment fragment = new TyHistoryFragment(typeId);
        return fragment;
    }

    public TyHistoryFragment() {
    }

    @SuppressLint("ValidFragment")
    public TyHistoryFragment(int typeId) {
        this.type = typeId;
    }


    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerTyHistoryComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .tyHistoryModule(new TyHistoryModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ty_history, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.setType(type);
        mPresenter.userView(1, smartRefresh);
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPresenter.userView(1, refreshLayout);
            }
        });
        smartRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mPresenter.loadMore(smartRefresh);
            }
        });
    }

    /**
     *
     * @param data 当不需要参数时 {@code data} 可以为 {@code null}
     */
    @Override
    public void setData(@Nullable Object data) {

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Subscriber(tag = EventBusTags.EDIT_SHOW)
    public void showEdit(String event) {
        if(mPresenter!=null&&smartRefresh!=null){
            mPresenter.userViewDel(smartRefresh);
        }
    }


    @OnClick({R.id.history_bottom_all, R.id.history_bottom_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.history_bottom_all:
//                if("全选".equals(historyBottomAll.getText().toString().trim())){
//                    historyBottomAll.setText("全部取消");
//                    mPresenter.addShowItemAll(true);
//                }else{
//                    historyBottomAll.setText("全选");
//                    mPresenter.addShowItemAll(false);
//                }
                break;
            case R.id.history_bottom_del:
                mPresenter.userViewDel(smartRefresh);
                break;
        }
    }

    @Override
    public void editShow(boolean isShow) {
        historyBottom.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showAdapter(HistoryAdapter historyAdapter) {
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(historyAdapter);
    }
}
