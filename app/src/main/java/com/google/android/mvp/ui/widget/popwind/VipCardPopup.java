package com.google.android.mvp.ui.widget.popwind;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.google.android.R;
import com.google.android.app.utils.JumpUtils;
import com.google.android.app.utils.StringUtils;
import com.google.android.mvp.model.back_entity.MoneyShow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import razerdp.basepopup.BasePopupWindow;

/**
 * VIP 卡密支付
 */
public class VipCardPopup extends BasePopupWindow implements View.OnClickListener {

    MoneyShow showPop;
    Activity activity;
    private AppComponent mAppComponent;
    public VipCardPopup(Activity context, MoneyShow showPop) {
        super(context);
        this.activity = context;
        if (mAppComponent == null) {
            mAppComponent = ArmsUtils.obtainAppComponentFromContext(activity);
        }
        setPopupGravity(Gravity.CENTER);
        this.showPop = showPop;
        findViewById(R.id.online_close).setOnClickListener(this);
        findViewById(R.id.save_qrcode).setOnClickListener(this);
        findViewById(R.id.copy_accout_open).setOnClickListener(this);

        ((TextView)findViewById(R.id.qrcode_text)).setText(showPop.getAccountno());
        ((TextView)findViewById(R.id.online_context)).setText(showPop.getRemark());
        ((TextView)findViewById(R.id.qrcode_no)).setText(showPop.getAccountname());
        showQrcode(showPop.getUrl(),findViewById(R.id.qrcode_img));


    }

    void showQrcode(String url, ImageView imageView){
        if (!TextUtils.isEmpty(url)) {
            mAppComponent
                    .imageLoader()
                    .loadImage(activity, ImageConfigImpl
                            .builder()
                            .imageView(imageView)
                            .url(StringUtils.getBaseUrl()+url)
                            .build());
        }
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_online_service);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.online_close:
                dismiss();
                break;
            case R.id.save_qrcode:
                saveQrcode(activity,findViewById(R.id.popup_bg));
                break;
            case R.id.copy_accout_open:
                JumpUtils.copyUrlOpenWx(activity,showPop.getAccountno());
                break;
        }
    }

    public void saveQrcode(Activity activity, LinearLayout relativeLayout){
        savePic2Phone(activity,getCacheBitmapFromView(relativeLayout),relativeLayout);
    }

    /**
     * 获取一个 View 的缓存视图
     *
     * @param view
     * @return
     */
    private Bitmap getCacheBitmapFromView(LinearLayout view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        return drawingCache;
    }

    private  void savePic2Phone(Context context, Bitmap bmp, LinearLayout view) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "mans");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        view.setDrawingCacheEnabled(false);
        ArmsUtils.makeText(activity,"保存成功");
    }

}
