package com.google.android.mvp.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.AdapterViewPager;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.yanzhenjie.sofia.Sofia;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.di.component.DaggerMyHistoryComponent;
import com.google.android.di.module.MyHistoryModule;
import com.google.android.mvp.contract.MyHistoryContract;
import com.google.android.mvp.presenter.MyHistoryPresenter;
import com.google.android.mvp.ui.fragment.TyHistoryFragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 历史记录
 */
public class MyHistoryActivity extends BaseActivity<MyHistoryPresenter> implements MyHistoryContract.View {

    @BindView(R.id.toolbar_right)
    TextView toolbarRight;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.record_in)
    TextView recordIn;
    @BindView(R.id.record_in_line)
    View recordInLine;
    @BindView(R.id.record_center)
    TextView recordCenter;
    @BindView(R.id.record_center_line)
    View recordCenterLine;
    @BindView(R.id.record_out)
    TextView recordOut;
    @BindView(R.id.record_out_line)
    View recordOutLine;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private List<Fragment> mFragments;
    String[] titles = new String[]{"今日", "7日", "更早"};

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMyHistoryComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myHistoryModule(new MyHistoryModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_my_history; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //状态栏
        Sofia.with(this)
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT);
        toolbarTitle.setText("历史记录");
        toolbarRight.setVisibility(View.VISIBLE);
        toolbarRight.setText("清空");
        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(TyHistoryFragment.newInstance(1));
            mFragments.add(TyHistoryFragment.newInstance(2));
            mFragments.add(TyHistoryFragment.newInstance(3));
        }
        viewPager.setAdapter(new AdapterViewPager(getSupportFragmentManager(), mFragments, titles));
        viewPager.setOnPageChangeListener(new PagerChangeListener());
        viewPager.setOffscreenPageLimit(mFragments.size());
        setCheck(0);
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

    @OnClick({R.id.toolbar_back, R.id.toolbar_right, R.id.record_in, R.id.record_center, R.id.record_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                killMyself();
                break;
            case R.id.toolbar_right:
                new AlertDialog.Builder(this).
                        setMessage("是否清空历史观看记录？")//提示内容
                        .setPositiveButton("清空", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EventBus.getDefault().post("", EventBusTags.EDIT_SHOW);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create()
                        .show();
                break;
            case R.id.record_in:
                setCheck(0);
                break;
            case R.id.record_center:
                setCheck(1);
                break;
            case R.id.record_out:
                setCheck(2);
                break;
        }
    }


    public class PagerChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    setCheck(0);
                    break;
                case 1:
                    setCheck(1);
                    break;
                case 2:
                    setCheck(2);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    private void setCheck(int flag) {
        int colorId1 = ContextCompat.getColor(this, R.color.white);
        int colorId2 = ContextCompat.getColor(this, R.color.font_78788E);
        recordInLine.setVisibility(View.GONE);
        recordCenterLine.setVisibility(View.GONE);
        recordOutLine.setVisibility(View.GONE);
        recordIn.setTextColor(colorId2);
        recordCenter.setTextColor(colorId2);
        recordOut.setTextColor(colorId2);
        switch (flag) {
            case 0:
                recordInLine.setVisibility(View.VISIBLE);
                recordIn.setTextColor(colorId1);
                break;
            case 1:
                recordCenterLine.setVisibility(View.VISIBLE);
                recordCenter.setTextColor(colorId1);
                break;
            case 2:
                recordOutLine.setVisibility(View.VISIBLE);
                recordOut.setTextColor(colorId1);
                break;
        }
        //这里是编辑的逻辑，切换界面需要重置编辑
        toolbarRight.setText("清空");
        viewPager.setCurrentItem(flag);
    }
}
