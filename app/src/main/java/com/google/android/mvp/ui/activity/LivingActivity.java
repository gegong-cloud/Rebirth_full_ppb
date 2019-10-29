package com.google.android.mvp.ui.activity;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.AdapterViewPager;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.mvp.ui.fragment.VedioEmptyFragment;
import com.google.android.mvp.ui.widget.player.CustomerVideo;
import com.google.android.mvp.ui.widget.player.OnTransitionListener;
import com.yanzhenjie.sofia.Sofia;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 直播
 */
public class LivingActivity extends AppCompatActivity {

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

    /**
     * 播放器相关参数
     */
    public final static String IMG_TRANSITION = "IMG_TRANSITION";
    public final static String TRANSITION = "TRANSITION";



    private boolean isTransition;

    private boolean isPlay;
    private boolean isPause;

    private Transition transition;

    String playTitleStr = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liveing);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getSupportActionBar().hide();
        loadCastInfo();
        initAnchorLive();
    }

    void loadCastInfo(){
        //状态栏
        Sofia.with(this)
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT);
        playTitleStr = getIntent().getStringExtra("path");
        String urlStr = getIntent().getStringExtra("img");
        if (!TextUtils.isEmpty(urlStr)) {
            ArmsUtils.obtainAppComponentFromContext(LivingActivity.this).imageLoader().loadImage(LivingActivity.this,
                    ImageConfigImpl
                            .builder()
                            .url(urlStr)
                            .imageView(ivCover)
                            .build());
        }
        tvTitle.setText(getIntent().getStringExtra("title"));
        tvSysMsg.setText(Html.fromHtml("系统提示：<font color='#ffffff'>" + getIntent().getStringExtra("sysmsg") + "</font>"));

        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(VedioEmptyFragment.newInstance());
        mFragments.add(VedioEmptyFragment.newInstance());
        playTop.setAdapter(new AdapterViewPager(getSupportFragmentManager(),mFragments,null));
        playTop.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(0==position){
                    dissMissAll();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        playTop.setCurrentItem(1);
    }


    @OnClick({R.id.iv_report, R.id.tv_close_sys_msg, R.id.iv_finish, R.id.mine_new_btn})
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
                videoPlayer.startWindowFullscreen(LivingActivity.this, true, true);
                break;
        }
    }

    /**
     * 初始化直播数据
     */
    private void initAnchorLive() {
        //初始化不打开外部的旋转
        getOrientationUtils().setEnable(false);


        videoPlayer.getTitleTextView().setVisibility(View.GONE);//隐藏title
        videoPlayer.getBackButton().setVisibility(View.GONE);//隐藏返回
        videoPlayer.getFullscreenButton().setVisibility(View.GONE);//隐藏返回
        videoPlayer.getStartButton().setVisibility(View.GONE);//隐藏返回
        videoPlayer.setBottomProgressBarDrawable(null);
        videoPlayer.setBottomShowProgressBarDrawable(null, null);


//        if(anchorDo!=null&&!TextUtils.isEmpty(anchorDo.getLive_addr())){
        if (!TextUtils.isEmpty(playTitleStr)) {
//            String url = "rtmp://live.yanhoulive.com/live/36Dmi";
            videoPlayer.setUp(playTitleStr, false, "");
//            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);//设置全屏显示
            videoPlayer.setVideoAllCallBack(new VideoAllCallBack() {
                @Override
                public void onStartPrepared(String url, Object... objects) {
                    Log.e("lhw---", "onStartPrepared,url=" + url);

                }

                @Override
                public void onPrepared(String url, Object... objects) { //开始播放了
                    Log.e("lhw---", "onPrepared,url=" + url);
                    //开始播放了才能旋转和全屏
                    getOrientationUtils().setEnable(true);
                    isPlay = true;
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
    }

    @Subscriber(tag = EventBusTags.QUIT_FULL)
    public void quitFull(String event) {
        hideOld();
    }

    void hideOld() {
        getSupportActionBar().hide();
        //状态栏
        Sofia.with(this)
//                .statusBarDarkFont()//深色字体
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
        if (videoPlayer!=null&&videoPlayer.orientationUtils == null) {
            videoPlayer.orientationUtils = new OrientationUtils(this, videoPlayer);
        }
        return videoPlayer==null?null:videoPlayer.orientationUtils;
    }
}
