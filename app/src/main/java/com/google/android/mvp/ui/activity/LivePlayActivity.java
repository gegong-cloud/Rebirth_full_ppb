package com.google.android.mvp.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jess.arms.base.AdapterViewPager;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView;
import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.app.utils.StringUtils;
import com.google.android.di.component.DaggerLivePlayComponent;
import com.google.android.di.module.LivePlayModule;
import com.google.android.mvp.contract.LivePlayContract;
import com.google.android.mvp.model.back_entity.LiveInfo;
import com.google.android.mvp.presenter.LivePlayPresenter;
import com.google.android.mvp.ui.fragment.VedioEmptyFragment;
import com.google.android.mvp.ui.widget.downtimer.DownTimer;
import com.google.android.mvp.ui.widget.downtimer.DownTimerListener;
import com.google.android.mvp.ui.widget.player.CustomerVideo;
import com.google.android.mvp.ui.widget.player.OnTransitionListener;
import com.yanzhenjie.sofia.Sofia;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static com.google.android.app.utils.StringUtils.getStrHashCode;


public class LivePlayActivity extends BaseActivity<LivePlayPresenter> implements LivePlayContract.View, DownTimerListener {

    @BindView(R.id.video_player)
    CustomerVideo videoPlayer;
    @BindView(R.id.iv_cover)
    CircleImageView ivCover;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_report)
    ImageView ivReport;
    @BindView(R.id.tv_close_sys_msg)
    TextView tvCloseSysMsg;
    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.tv_sys_msg)
    TextView tvSysMsg;
    @BindView(R.id.play_top)
    ViewPager playTop;
    @BindView(R.id.tip_title)
    TextView tipTitle;
    @BindView(R.id.play_tip)
    RelativeLayout playTip;
    @BindView(R.id.tip_second)
    TextView tipSecond;
    @BindView(R.id.mine_new_btn)
    TextView mineNewBtn;

    /**
     * 播放器相关参数
     */
    public final static String IMG_TRANSITION = "IMG_TRANSITION";
    public final static String TRANSITION = "TRANSITION";
    private boolean isTransition;
    private boolean isPlay;
    private boolean isPause;

    private Transition transition;

    /**
     * 主播的id
     */
    String liveId = "";

    //进度框
    private MaterialDialog materialDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLivePlayComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .livePlayModule(new LivePlayModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_liveing; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        materialDialog = new MaterialDialog.Builder(this).content("正在加载...").progress(true, 0).build();
        //状态栏
        Sofia.with(this)
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT);
        liveId = getIntent().getStringExtra("liveId");
        mPresenter.loadData(liveId);
        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(VedioEmptyFragment.newInstance());
        mFragments.add(VedioEmptyFragment.newInstance());
        playTop.setAdapter(new AdapterViewPager(getSupportFragmentManager(), mFragments, null));
        playTop.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (0 == position) {
                    dissMissAll();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        playTop.setCurrentItem(1);
    }

    @Override
    public void showLoading() {
        materialDialog.show();
    }

    @Override
    public void hideLoading() {
        materialDialog.dismiss();
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
        EventBus.getDefault().register(this);
        getSupportActionBar().hide();
    }


    @OnClick({R.id.iv_report, R.id.tv_close_sys_msg, R.id.iv_finish, R.id.mine_new_btn, R.id.tip_free, R.id.tip_open})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_report:
                ArmsUtils.makeText(this, "举报成功");
                break;
            case R.id.tv_close_sys_msg:
                tvSysMsg.setVisibility(tvSysMsg.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                tvCloseSysMsg.setText(tvSysMsg.getVisibility() == View.VISIBLE ? "关字幕" : "开字幕");
                break;
            case R.id.iv_finish:
                dissMissAll();
                break;
            case R.id.mine_new_btn:
                //直接横屏
                getOrientationUtils().resolveByClick();
                videoPlayer.startWindowFullscreen(LivePlayActivity.this, true, true);
                break;
            case R.id.tip_free:
                launchActivity(new Intent(this, InviteFriendActivity.class));
                break;
            case R.id.tip_open:
                mPresenter.castRecharge(this);
                break;
        }
    }

    /**
     * @param liveInfo 显示主播信息
     */
    void showInfo(LiveInfo liveInfo) {
        tvTitle.setText(!TextUtils.isEmpty(liveInfo.getName()) ? liveInfo.getName() : "");
        if (!TextUtils.isEmpty(liveInfo.getImg())) {

            StringUtils.showImgView(ArmsUtils.obtainAppComponentFromContext(LivePlayActivity.this),ivCover,LivePlayActivity.this,liveInfo.getImg(),getStrHashCode(liveInfo.getImg()));

        }
        if (!TextUtils.isEmpty(liveInfo.getApp_sys_msg())) {
            tvSysMsg.setText(Html.fromHtml("系统提示：<font color='#ffffff'>" + liveInfo.getApp_sys_msg() + "</font>"));
        }

    }

    /**
     * 初始化直播数据
     */
    @Override
    public void initAnchorLive(LiveInfo liveInfo) {
        showInfo(liveInfo);
        //初始化不打开外部的旋转
        if(getOrientationUtils()!=null){
            getOrientationUtils().setEnable(false);
        }
        videoPlayer.getTitleTextView().setVisibility(View.GONE);//隐藏title
        videoPlayer.getBackButton().setVisibility(View.GONE);//隐藏返回
        videoPlayer.getFullscreenButton().setVisibility(View.GONE);//隐藏返回
        videoPlayer.getStartButton().setVisibility(View.GONE);//隐藏返回
        videoPlayer.setBottomProgressBarDrawable(null);
        videoPlayer.setBottomShowProgressBarDrawable(null, null);

        if (!TextUtils.isEmpty(liveInfo.getPlay_url())) {
//            String url = "rtmp://live.yanhoulive.com/live/36Dmi";
            videoPlayer.setUp(liveInfo.getPlay_url(), false, "");
            videoPlayer.setVideoAllCallBack(new VideoAllCallBack() {
                @Override
                public void onStartPrepared(String url, Object... objects) {
                    Log.e("lhw---", "onStartPrepared,url=" + url);

                }

                @Override
                public void onPrepared(String url, Object... objects) { //开始播放了
                    Log.e("lhw---", "onPrepared,url=" + url);
                    //开始播放了才能旋转和全屏
                    if(getOrientationUtils()!=null){
                        getOrientationUtils().setEnable(true);
                    }
                    isPlay = true;
                    if (liveInfo != null && !TextUtils.isEmpty(liveInfo.getView_time())) {
                        int viewTime = Integer.parseInt(liveInfo.getView_time());
                        tipTitle.setText(liveInfo.getTip());
                        if (viewTime > 0) {
                            initDownTimer(viewTime);
                        }
                    }
                }

                @Override
                public void onClickStartIcon(String url, Object... objects) {
                    Log.e("lhw---", "onClickStartIcon,url=" + url);
                }

                @Override
                public void onClickStartError(String url, Object... objects) {
                    Log.e("lhw---", "onClickStartError,url=" + url);
                }

                @Override
                public void onClickStop(String url, Object... objects) {
                    Log.e("lhw---", "onClickStop,url=" + url);
                }

                @Override
                public void onClickStopFullscreen(String url, Object... objects) {
                    Log.e("lhw---", "onClickStopFullscreen,url=" + url);
                }

                @Override
                public void onClickResume(String url, Object... objects) {
                    Log.e("lhw---", "onClickResume,url=" + url);
                }

                @Override
                public void onClickResumeFullscreen(String url, Object... objects) {
                    Log.e("lhw---", "onClickResumeFullscreen,url=" + url);
                }

                @Override
                public void onClickSeekbar(String url, Object... objects) {
                    Log.e("lhw---", "onClickSeekbar,url=" + url);
                }

                @Override
                public void onClickSeekbarFullscreen(String url, Object... objects) {
                    Log.e("lhw---", "onClickSeekbarFullscreen,url=" + url);
                }

                @Override
                public void onAutoComplete(String url, Object... objects) {
                    Log.e("lhw---", "onAutoComplete,url=" + url);
                }

                @Override
                public void onEnterFullscreen(String url, Object... objects) {
                    Log.e("lhw---", "onEnterFullscreen,url=" + url);
                }

                @Override
                public void onQuitFullscreen(String url, Object... objects) {
                    Log.e("lhw---", "onQuitFullscreen,url=" + url);
                    if (getOrientationUtils() != null) {
                        getOrientationUtils().backToProtVideo();
                    }
                }

                @Override
                public void onQuitSmallWidget(String url, Object... objects) {
                    Log.e("lhw---", "onQuitSmallWidget,url=" + url);
                }

                @Override
                public void onEnterSmallWidget(String url, Object... objects) {
                    Log.e("lhw---", "onEnterSmallWidget,url=" + url);
                }

                @Override
                public void onTouchScreenSeekVolume(String url, Object... objects) {
                    Log.e("lhw---", "onTouchScreenSeekVolume,url=" + url);
                }

                @Override
                public void onTouchScreenSeekPosition(String url, Object... objects) {
                    Log.e("lhw---", "onTouchScreenSeekPosition,url=" + url);
                }

                @Override
                public void onTouchScreenSeekLight(String url, Object... objects) {
                    Log.e("lhw---", "onTouchScreenSeekLight,url=" + url);
                }

                @Override
                public void onPlayError(String url, Object... objects) {
                    Log.e("lhw---", "onPlayError,url=" + url);
//                    ArmsUtils.makeText(LivingActivity.this, "主播已经离开了");
                }

                @Override
                public void onClickStartThumb(String url, Object... objects) {
                    Log.e("lhw---", "onClickStartThumb,url=" + url);
                }

                @Override
                public void onClickBlank(String url, Object... objects) {
                    Log.e("lhw---", "onClickBlank,url=" + url);
                }

                @Override
                public void onClickBlankFullscreen(String url, Object... objects) {
                    Log.e("lhw---", "onClickBlankFullscreen,url=" + url);
                }
            });//设置视频播放回调
            //过渡动画
            initTransition();
            isPlayMovie(liveInfo);
        } else {
            ArmsUtils.makeText(this, "主播已经离开了");
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (videoPlayer != null) {
            videoPlayer.release();
        }
        if (getOrientationUtils() != null)
            getOrientationUtils().releaseListener();
        if (downTimer != null) {
            downTimer.stopDown();
            downTimer = null;
        }
    }

    @Subscriber(tag = EventBusTags.QUIT_FULL)
    public void quitFull(String event) {
        hideOld();
    }

    void hideOld() {
        getSupportActionBar().hide();
        //状态栏
        Sofia.with(this)
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT);
    }

    @Override
    protected void onPause() {
        videoPlayer.onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        videoPlayer.onVideoResume();
        super.onResume();
        isPause = false;
    }

    @Override
    public void onBackPressed() {
        if (getOrientationUtils() != null) {
            getOrientationUtils().backToProtVideo();
        }

        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        GSYVideoManager.releaseAllVideos();
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onBackPressed();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            }, 500);
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            videoPlayer.onConfigurationChanged(this, newConfig, getOrientationUtils(), true, true);
        } else {
            hideOld();
        }
    }

    private void initTransition() {
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
            ViewCompat.setTransitionName(videoPlayer, IMG_TRANSITION);
            addTransitionListener();
            startPostponedEnterTransition();
        } else {
            videoPlayer.startPlayLogic();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean addTransitionListener() {
        transition = getWindow().getSharedElementEnterTransition();
        if (transition != null) {
            transition.addListener(new OnTransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    super.onTransitionEnd(transition);
                    videoPlayer.startPlayLogic();
                    transition.removeListener(this);
                }
            });
            return true;
        }
        return false;
    }

    private void dissMissAll() {
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        GSYVideoManager.releaseAllVideos();
        finish();
    }

    private OrientationUtils getOrientationUtils() {
        if (videoPlayer != null && videoPlayer.orientationUtils == null) {
            videoPlayer.orientationUtils = new OrientationUtils(this, videoPlayer);
        }
        return videoPlayer == null ? null : videoPlayer.orientationUtils;
    }

    DownTimer downTimer;

    /**
     * @param mTime 开启倒计时
     */
    void initDownTimer(int mTime) {
        downTimer = new DownTimer();
        downTimer.setListener(this);
        downTimer.startDown(mTime * 1000);
    }

    @Override
    public void onTick(long millisUntilFinished) {

    }

    @Override
    public void onFinish() {
        if (videoPlayer.isIfCurrentIsFullscreen()) {
//            videoPlayer.tipTitle.setText(!TextUtils.isEmpty(tipStr)?tipStr:"");
            videoPlayer.showTip(false);
        }
        showTip(false);
    }

    /**
     * 用户是否可以播放影片
     */
    String tipStr = "";

    void isPlayMovie(LiveInfo movieInfo) {
        if (movieInfo != null && !TextUtils.isEmpty(movieInfo.getView_time())) {
            int viewTime = Integer.parseInt(movieInfo.getView_time());
            tipStr = movieInfo.getTip();
            tipTitle.setText(movieInfo.getTip());
            tipSecond.setVisibility(viewTime > 0 ? View.VISIBLE : View.GONE);
            mineNewBtn.setVisibility(viewTime == 0 ? View.VISIBLE : View.GONE);
            tipSecond.setText("试看" + viewTime + "秒");
            showTip(viewTime >= 0);
        }
    }

    /**
     * @param isShow 是否显示提示信息
     */
    void showTip(boolean isShow) {
        if (isShow) {
            playTip.setVisibility(View.GONE);
            if (videoPlayer != null) {
                if (videoPlayer.getCurrentState() == GSYVideoView.CURRENT_STATE_PAUSE) {
                    videoPlayer.onVideoResume();
                }
                videoPlayer.setVisibility(View.VISIBLE);
            }
        } else {
//            if("ffffffff-f99b-90f6-ffff-ffffa9788efd".equals(HbCodeUtils.getAndroidDevice())){
//                return;
//            }
            if (videoPlayer != null) {
                videoPlayer.release();
                videoPlayer.setVisibility(View.GONE);
            }
            playTip.setVisibility(View.VISIBLE);
        }
    }

}
