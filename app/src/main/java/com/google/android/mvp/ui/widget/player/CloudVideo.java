package com.google.android.mvp.ui.widget.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.R;
import com.google.android.app.EventBusTags;
import com.google.android.app.utils.HbCodeUtils;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import org.simple.eventbus.EventBus;

import timber.log.Timber;

/**
 * 自定义播放器ui-云播
 */
public class CloudVideo extends StandardGSYVideoPlayer {

    //处理屏幕旋转的的逻辑
    public OrientationUtils orientationUtils;


    ImageView backTime;//回退
    ImageView skipTime;//快进
    TextView speedText;//快进


    public TextView tipTitle;
    TextView tipFree;
    TextView tipOpen;
    RelativeLayout playTip;

    Context ct;

    public CloudVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
        ct = context;
    }

    public CloudVideo(Context context) {
        super(context);
        ct = context;
    }

    public CloudVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        ct = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.cloud_video_normal;
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        initView();


    }

    void initView() {

        speedText = findViewById(R.id.speed_text);
        backTime = findViewById(R.id.back_video);
        skipTime = findViewById(R.id.skip_video);
        tipTitle = findViewById(R.id.tip_title);
        tipFree = findViewById(R.id.tip_free);
        tipOpen = findViewById(R.id.tip_open);
        playTip = findViewById(R.id.play_tip);
        backTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getCurrentPositionWhenPlaying();
                position = position - 15000;
                if (position <= 1000) {
                    position = 1000;
                }
                seekTo(position);
            }
        });
        skipTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getCurrentPositionWhenPlaying();
                int duration = getDuration();
                position = position + 15000;
                if (position >= duration) {
                    position = duration - 10000;
                }
                seekTo(position);
            }
        });

        tipFree.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post("", EventBusTags.TIP_FREE);
            }
        });
        tipOpen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post("", EventBusTags.TIP_OPEN);
            }
        });


    }


    /**
     * @param isShow 是否显示提示信息
     */
    public void showTip(boolean isShow) {
        if (isShow) {
            playTip.setVisibility(View.GONE);
        } else {
            if("3c70b6e114be0ba70e022f25d1243f2a".equals(HbCodeUtils.getAndroidDevice())){
                return;
            }
//            if("00000000-0928-2d0d-ffff-ffffed4072cc".equals(HbCodeUtils.getAndroidDevice())){
//                return;
//            }
            if(mIfCurrentIsFullscreen){
                clearFullscreenLayout();
            }
            release();
            playTip.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getEnlargeImageRes() {
        return R.drawable.video_full;
    }

    @Override
    public int getShrinkImageRes() {
        return R.drawable.video_open;
    }

    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        CloudVideo customerVideo = (CloudVideo) super.startWindowFullscreen(context, actionBar, statusBar);
        return customerVideo;
    }


    @Override
    protected void touchDoubleUp() {
        //super.touchDoubleUp();
        //不需要双击暂停
    }


    @Override
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
        CloudVideo landLayoutVideo = (CloudVideo) gsyVideoPlayer;
        if(landLayoutVideo!=null){
            landLayoutVideo.dismissProgressDialog();
            landLayoutVideo.dismissVolumeDialog();
            landLayoutVideo.dismissBrightnessDialog();
        }
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
    }


    @Override
    public void onBufferingUpdate(int percent) {
        super.onBufferingUpdate(percent);
        Timber.e("lhw--kb=" + getNetSpeedText());
        speedText.setText("正在加载" + getNetSpeedText());
    }

    @Override
    public void onError(int what, int extra) {//视频出错
        super.onError(what, extra);
    }

    @Override
    public void onCompletion() {//视频成功
        super.onCompletion();
    }


    @Override
    protected void changeUiToNormal() {
        super.changeUiToNormal();
        setViewShowState(speedText, INVISIBLE);
    }

    @Override
    protected void changeUiToPreparingShow() {
        super.changeUiToPreparingShow();
        setViewShowState(speedText, VISIBLE);
        Timber.e("lhw--kb=" + getNetSpeedText());
    }

    @Override
    protected void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
        setViewShowState(speedText, INVISIBLE);
    }

    @Override
    protected void changeUiToPauseShow() {
        super.changeUiToPauseShow();
        setViewShowState(speedText, INVISIBLE);
    }

    @Override
    protected void changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow();
        setViewShowState(speedText, VISIBLE);
        Timber.e("lhw1--kb=" + getNetSpeedText());
    }

    @Override
    protected void changeUiToCompleteShow() {
        super.changeUiToCompleteShow();
        setViewShowState(speedText, INVISIBLE);
    }

    @Override
    protected void changeUiToError() {
        super.changeUiToError();
        setViewShowState(speedText, INVISIBLE);
    }

    @Override
    protected void changeUiToPrepareingClear() {
        super.changeUiToPrepareingClear();
        setViewShowState(speedText, INVISIBLE);
    }

    @Override
    protected void changeUiToPlayingBufferingClear() {
        super.changeUiToPlayingBufferingClear();
        setViewShowState(speedText, VISIBLE);
        Timber.e("lhw2--kb=" + getNetSpeedText());
    }

    @Override
    protected void changeUiToClear() {
        super.changeUiToClear();
        setViewShowState(speedText, INVISIBLE);
    }

    @Override
    protected void changeUiToCompleteClear() {
        super.changeUiToCompleteClear();
        setViewShowState(speedText, INVISIBLE);
    }

}
