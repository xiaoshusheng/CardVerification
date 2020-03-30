package com.wondersgroup.cardverification.widget;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.wondersgroup.cardverification.app.Constant;
import com.wondersgroup.cardverification.model.bean.UpdateAppBean;
import com.wondersgroup.cardverification.model.bus.AppUpdateBus;
import com.wondersgroup.cardverification.utils.sp.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;


/**
 * Great by YangZL
 * created on 2019/6/10
 * description: 使用内置升级下载类下载apk
 */
public class DownAPKService extends Service {
    private static final String TAG = DownAPKService.class.getSimpleName();
    public static final String UPDATE_KEY = "update_key";

    private long mEnqueue;
    public static final String APK_NAME = "rsCard.apk";

    public DownAPKService() {
    }

    /**
     * 接收下载完的广播
     **/
    private DownloadCompleteReceiver mDownloadCompleteReceiver;
    /**
     * 安卓系统下载类
     **/
    private DownloadManager mDownloadManager;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            UpdateAppBean mUpdateAppBean = (UpdateAppBean) intent.getSerializableExtra(UPDATE_KEY);
            /**
             * 调用下载
             */
            initDownManager(mUpdateAppBean);
        }

        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 初始化下载器
     **/
    private void initDownManager(UpdateAppBean bean) {
        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        mDownloadCompleteReceiver = new DownloadCompleteReceiver();
        //1.设置下载地址
        DownloadManager.Request mRequest = new DownloadManager.Request(Uri.parse(bean.getUrl()));
        //2.设置通知标题
        mRequest.setTitle("社保卡核验");
        //3.设置通知内容
        mRequest.setDescription(TextUtils.isEmpty(bean.getContext()) ? "" : bean.getContext());
        //4.设置允许使用的网络类型
        if (SPUtils.getBoolean(this, Constant.SETTING_4G_DOWNLOAD_APK, false)) {
            mRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        } else {
            mRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        }
        //5.设置下载时通知栏是否显示下载进度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //5.1 DownloadManager.Request.VISIBILITY_VISIBLE:在下载进行的过程中，通知栏中会一直显示该下载的Notification，当下载完成时，该Notification会被移除，这是默认的参数值。
            //5.2 Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED：在下载过程中通知栏会一直显示该下载的Notification，在下载完成后该Notification会继续显示，直到用户点击该Notification或者消除该Notification。
            //5.3 Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION：只有在下载完成后该Notification才会被显示。
            //5.4 Request.VISIBILITY_HIDDEN：不显示该下载请求的Notification。如果要使用这个参数，需要在应用的清单文件中加上DOWNLOAD_WITHOUT_NOTIFICATION权限。
            mRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        }
        //6.如果我们希望下载的文件可以被系统的Downloads应用扫描到并管理，我们需要调用Request对象的setVisibleInDownloadsUi方法，传递参数true。
        mRequest.setVisibleInDownloadsUi(true);
        //7.设置下载后文件类型为apk
        mRequest.setMimeType("application/vnd.android.package-archive");
        //8.创建目录
        File mDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!mDirectory.exists()) {
            boolean success = mDirectory.mkdirs();
            LogUtils.i(TAG, "安装包下载目录创建是否成功" + success);
        } else {
            File[] mFiles = mDirectory.listFiles();
            for (File mFile : mFiles) {
                if (mFile.isFile() && mFile.getName().equals(DownAPKService.APK_NAME)) {
                    boolean mDelete = mFile.delete();
                    LogUtils.i(TAG, mFile.getName() + "删除成功了" + mDelete);
                }
            }
        }
        //9.设置下载后文件存放的位置(这里设置到外置内存卡的公共目录,可以避免android.permission.ACCESS_ALL_DOWNLOADS权限被拒绝的问题)
        mRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, APK_NAME);
        //10.将下载请求放入队列
        mEnqueue = mDownloadManager.enqueue(mRequest);
        //11.注册下载完成的广播
        registerReceiver(mDownloadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    /**
     * DownloadManager 内置下载器下载完成会发送系统广播DownloadManager.ACTION_DOWNLOAD_COMPLETE
     */
    class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            //判断是否下载完成的广播
            if (intent != null && intent.getAction() != null && intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                //获取下载的文件id
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downId == mEnqueue) {
                    //自动安装apk
                    EventBus.getDefault().post(new AppUpdateBus(true));
                    LogUtils.i(TAG, "DownAPKService关闭了");
                    //停止服务并关闭广播
                    stopSelf();
                }

            }
        }
    }

    @Override
    public void onDestroy() {
        // 注销下载广播
        if (mDownloadCompleteReceiver != null || mDownloadManager != null) {
            unregisterReceiver(mDownloadCompleteReceiver);
            mDownloadManager = null;
        }
        super.onDestroy();
    }

}
