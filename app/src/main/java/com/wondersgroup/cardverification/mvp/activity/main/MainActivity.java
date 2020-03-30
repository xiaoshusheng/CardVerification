package com.wondersgroup.cardverification.mvp.activity.main;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.wondersgroup.cardverification.BuildConfig;
import com.wondersgroup.cardverification.R;
import com.wondersgroup.cardverification.app.ActivityContracts;
import com.wondersgroup.cardverification.app.Constant;
import com.wondersgroup.cardverification.base.BaseActivity;
import com.wondersgroup.cardverification.model.bus.AppUpdateBus;
import com.wondersgroup.cardverification.model.bus.CropCardBus;
import com.wondersgroup.cardverification.utils.GlideImageUtils;
import com.wondersgroup.cardverification.utils.io.FaceBase64Util;
import com.wondersgroup.cardverification.utils.permisstion.CustomConsumer;
import com.wondersgroup.cardverification.utils.permisstion.PermissionApi;
import com.wondersgroup.cardverification.widget.DownAPKService;
import com.wondersgroup.cardverification.widget.camera.CameraConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import androidx.core.content.FileProvider;
import butterknife.BindView;

@Route(path = "/main/MainActivity")
public class MainActivity extends BaseActivity<MainContract.Presenter> implements MainContract.View {
    private int mClickTimes;//连续点击的次数
    private static final long MSG_DURATION = (long) (1.5 * 1000);//两次点击的时间间隔
    private static final int MSG_QUIT = 0x00;//退出程序的消息
    public static final int CODE_CROP_PIC = 3;//使用带裁剪框的图片

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.tv_camera)
    TextView tvCamera;

    File cameraSavePath = null;
    Uri uri = null;

    /**
     * 点击两次退出程序的handler
     */
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_QUIT:
                    mClickTimes = 0;
                    break;
            }
            return false;
        }
    });


    @SuppressLint({"MissingPermission", "SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        //初始化webView
        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportZoom(false); // support zoom
        webSettings.setBuiltInZoomControls(false);
        //适应屏幕
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webView.getSettings().setTextZoom(100);
        // 开启web和js通信的通道。
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(this),"mobile");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return webView.getHitTestResult() == null;
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){

        });
        webView.loadUrl(Constant.HOME_WEB_URL);
        //检查版本
        checkVersion();
        //隐藏测试拍照功能
        tvCamera.setVisibility(View.GONE);
        tvCamera.setOnClickListener(v -> {
            ARouter.getInstance()
                    .build(ActivityContracts.ACTIVITY_CAMERA)
                    .navigation();
        });
    }


    /**
     * 权限检查
     */
    @SuppressLint({"CheckResult", "AutoDispose"})
    public void checkVersion() {
        PermissionApi.getInstance()
                .getRxPermissions(this)
                .request(Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(new CustomConsumer(this) {
                    @Override
                    public void onGranted() {
                        mPresenter.checkVersion();
                    }
                });

    }



    @Override
    protected MainContract.Presenter initPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected int getActivityLayoutID() {
        return R.layout.activity_main;
    }


    @Override
    public void updateVersion() {

    }

    @Override
    public void onBackPressed() {
        //退出程序
        mClickTimes++;
        if (webView.canGoBack()) {
            webView.goBack();
        }else {
            if (mClickTimes == 1) {
                ToastUtils.showShort("是否退出应用...");
                mHandler.sendEmptyMessageDelayed(MSG_QUIT, MSG_DURATION);
            } else {
                finishAffinity();
                ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE); //获取应用程序管理器
                manager.killBackgroundProcesses(getPackageName()); //强制结束当前应用程序
            }
        }
    }

    /**
     * 用于升级时的调用方法
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusReceived(AppUpdateBus event) {
        if (event.isSuccess()) {
            installApk();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusReceived(CropCardBus event) {
        if (event.isSuccess()) {
            String method ="javascript:cropCard(\""+event.getImageBase64()+"\")" ;
            webView.loadUrl(method);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.evaluateJavascript(method, value -> {
                    //ToastUtils.showShort("拍照成功2");
                    Log.e("onReceiveValue: ", "拍照成功2");
                });
            }
        }
    }

    /**
     * 安装apk
     */
    private void installApk() {
        File mDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File outputFile = new File(mDirectory + File.separator + DownAPKService.APK_NAME);
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //兼容7.0及以上版本(7.0以后权限变更 需要使用使用 FileProvider获取uri)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri mUri = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".rsCard", outputFile);
            intent.setDataAndType(mUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.parse("file://" + outputFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }

}
