package com.google.android.app.utils.down;

public interface DownloadListener {
    void onProgress(int progress);//`通知当前进度

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();
}
