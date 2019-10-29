package com.google.android.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.R;
import com.google.android.di.component.DaggerInviteFriendComponent;
import com.google.android.di.module.InviteFriendModule;
import com.google.android.mvp.contract.InviteFriendContract;
import com.google.android.mvp.model.back_entity.UserInvite;
import com.google.android.mvp.presenter.InviteFriendPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 邀请好友
 */
public class InviteFriendActivity extends BaseActivity<InviteFriendPresenter> implements InviteFriendContract.View {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.invite_code)
    TextView inviteCode;
    @BindView(R.id.invite_qrcode)
    ImageView inviteQrcode;
    @BindView(R.id.save_qrcode_bg)
    LinearLayout saveQrcodeBg;
    @BindView(R.id.invite_tishi)
    WebView inviteTishi;
    @BindView(R.id.all_search_nested)
    NestedScrollView allSearchNested;

    //进度框
    private MaterialDialog materialDialog;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerInviteFriendComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .inviteFriendModule(new InviteFriendModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_invite_friend; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        materialDialog = new MaterialDialog.Builder(this).content("正在加载...").progress(true, 0).build();
        //状态栏
//        Sofia.with(this)
//                .statusBarDarkFont()//深色字体
//                .invasionStatusBar()
//                .statusBarBackground(Color.TRANSPARENT);
        toolbarTitle.setText("邀请好友");
        if (mPresenter != null) {
            mPresenter.userInvite();
        }
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
        getSupportActionBar().hide();
        ImmersionBar.with(this).init();
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.1f)
                .init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }

    @OnClick({R.id.toolbar_back, R.id.invite_copy_link, R.id.invite_friend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                killMyself();
                break;
            case R.id.invite_copy_link://保存二维码
//                mPresenter.shareWx();
                if (saveQrcodeBg == null) {
                    saveQrcodeBg = findViewById(R.id.save_qrcode_bg);
                    return;
                }
                saveQrcode(InviteFriendActivity.this, saveQrcodeBg);
                break;
            case R.id.invite_friend:
                mPresenter.copyLink(InviteFriendActivity.this);
                break;

        }
    }

    @Override
    public void showInfo(UserInvite userInvite) {
        if (userInvite != null && !TextUtils.isEmpty(userInvite.getInvateTip())) {
            inviteTishi.setBackgroundColor(0);
            WebSettings webSettings = inviteTishi.getSettings();
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setSavePassword(false);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setAllowFileAccess(false);
            inviteTishi.setVerticalScrollbarOverlay(true);
            inviteTishi.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                }
            });
            inviteTishi.loadDataWithBaseURL(null, userInvite.getInvateTip(), "text/html", "utf-8", null);
        }

    }

    @Override
    public void showQrImg(String qrPath) {
        if (!TextUtils.isEmpty(qrPath)) {
            inviteQrcode.setVisibility(View.VISIBLE);
            ArmsUtils.obtainAppComponentFromContext(InviteFriendActivity.this).imageLoader().loadImage(InviteFriendActivity.this,
                    ImageConfigImpl
                            .builder()
                            .url(qrPath)
                            .imageView(inviteQrcode)
                            .build());
        }
    }


    public void saveQrcode(Activity activity, LinearLayout relativeLayout) {
        savePic2Phone(activity, getBitmapByView(allSearchNested), relativeLayout);
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

    /**
     * 布局超出了屏幕处理
     *
     * @param scrollView
     * @return
     */
    public static Bitmap getBitmapByView(NestedScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    private void savePic2Phone(Context context, Bitmap bmp, LinearLayout view) {
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
        ArmsUtils.makeText(InviteFriendActivity.this, "保存成功！");
//        ArmsUtils.makeText(InviteFriendActivity.this, "保存成功,正在打开微信扫一扫...");
//        openWeixinToQE_Code(InviteFriendActivity.this);
    }


}
