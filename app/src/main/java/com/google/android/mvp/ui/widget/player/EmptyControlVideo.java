package com.google.android.mvp.ui.widget.player;

import android.content.Context;
import android.util.AttributeSet;
import com.google.android.R;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

/**
 * 无任何控制ui的播放
 */

public class EmptyControlVideo extends StandardGSYVideoPlayer {

    public EmptyControlVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public EmptyControlVideo(Context context) {
        super(context);
    }

    public EmptyControlVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.empty_control_video;
    }



    @Override
    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
        //不给触摸快进，如果需要，屏蔽下方代码即可
        mChangePosition = false;

        //不给触摸音量，如果需要，屏蔽下方代码即可
        mChangeVolume = false;

        //不给触摸亮度，如果需要，屏蔽下方代码即可
        mBrightness = false;
    }

    @Override
    protected void touchDoubleUp() {
        //super.touchDoubleUp();
        //不需要双击暂停
    }

    @Override
    public void onError(int what, int extra) {//视频出错
        super.onError(what, extra);
    }

    @Override
    public void onCompletion() {//视频成功
        super.onCompletion();
    }
}
