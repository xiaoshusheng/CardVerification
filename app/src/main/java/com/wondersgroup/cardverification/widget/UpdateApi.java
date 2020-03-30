package com.wondersgroup.cardverification.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wondersgroup.cardverification.R;
import com.wondersgroup.cardverification.app.MyApplication;
import com.wondersgroup.cardverification.model.bean.UpdateAppBean;
import com.wondersgroup.cardverification.model.bus.AppUpdateBus;
import com.wondersgroup.cardverification.view.update.CommonDialog;
import com.wondersgroup.cardverification.view.update.UpdateDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Great by YangZL
 * created on 2019/6/10
 * description: 用于检测版本升级的工具类
 * 1.检查网络是否可用
 * 2.检查权限是否允许
 * 3.本地版本和线上版本对比
 * 4.根据isFormSetting,mStatus和mIgnoreFlag三个字段灵活显示更新对话框(TODO 根据需求,需不需要在设置界面点击版本更新是有一点延迟效果)
 * 5.检查本地是否有和服务器上版本一直的apk安装包
 * 6.根据用户选择安装本地apk或者开启服务下载线上apk进行安装
 */
public class UpdateApi {
    private static final String TAG = UpdateApi.class.getSimpleName();
    private static UpdateApi mInstance;
    private static int APK_PARSED_ERROR = -1;//安装包解析失败,有可能是下载的apk大小为0
    private String mServerVersionName;//服务器版名称
    private Integer mServerVersionCode;//服务器版本号
    private File mDownloadPath;//安装包下载路径目录
    private String mArchiveFilePath;//安装包路径

    private UpdateApi() {
        mDownloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        mArchiveFilePath = mDownloadPath + File.separator + DownAPKService.APK_NAME;
    }

    /**
     * 创建单列模式
     */
    public static UpdateApi getInstance() {
        if (mInstance == null) {
            synchronized (UpdateApi.class) {
                if (mInstance == null) {
                    mInstance = new UpdateApi();
                }
            }
        }
        return mInstance;
    }


    /**
     * 版本检查
     *
     * @param context 上下文
     */
    public void checkVersion(Context context, UpdateAppBean updateInfo) {
        /**
         *  检查网络是否可用
         */
        if (!MyApplication.getInstance().isNetConnection()) {
            ToastUtils.showShort("当前网络不可用");
            return;
        }
        /**
         * 权限检查
         */
        _checkVersion(context, updateInfo);
    }

    /**
     * 检查版本
     *
     * @param context
     */
    private void _checkVersion(Context context, UpdateAppBean updateInfo) {
        // 代表设置界面点击的版本检查
        mServerVersionName = updateInfo.getLatestVersion();
        mServerVersionCode = Integer.parseInt(updateInfo.getVersionNumber() == null ? "100" : updateInfo.getVersionNumber());
        if (!AppUtils.getAppVersionName().equals(mServerVersionName)) {
            showUpdateDialog(context, updateInfo);
        }
    }


    /**
     * 显示版本升级的对话框
     *
     * @param context    上下文
     * @param updateInfo
     */
    private void showUpdateDialog(Context context, UpdateAppBean updateInfo) {

        UpdateDialog.getInstance(context, TextUtils.isEmpty(updateInfo.getContext()) ? "优化app内容" : updateInfo.getContext(), new UpdateDialog.UpdateCallback() {
            @Override
            public void cancel() {
                //忽略更新的操作
            }

            @Override
            public void updateNow() {
                //下载更新
                checkLocalHasSameVersionApk(context, updateInfo);
            }
        }).show();
    }

    /**
     * 检查本地是否存在相同版本的apk安装包
     *
     * @param context
     * @param updateInfo
     */
    private void checkLocalHasSameVersionApk(Context context, UpdateAppBean updateInfo) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (mDownloadPath.exists()) {
                boolean localHasSameApk = false;
                File[] mFiles = mDownloadPath.listFiles();
                for (File mFile : mFiles) {
                    if (mFile.isFile() && mFile.getName().equals(DownAPKService.APK_NAME)) {
                        int localApkVersionCode = getVersionNameFromApk(context, mArchiveFilePath);
                        if (localApkVersionCode != APK_PARSED_ERROR && localApkVersionCode == mServerVersionCode) {//本地存在相同版本的apk安装包
                            localHasSameApk = true;
                            break;
                        } else {//本地存在不同版本的apk安装包,则删除
                            localHasSameApk = false;
                            boolean mDelete = mFile.delete();
                            LogUtils.i(TAG, mFile.getName() + "删除成功了" + mDelete);
                        }
                    }
                }
                if (localHasSameApk) {
                    showInstallLocalApkDialog(context, updateInfo);
                } else {
                    checkNetwork(context, updateInfo);
                }
            } else {//文件夹不存在
                checkNetwork(context, updateInfo);
            }
        } else {
            Toast.makeText(context, "检测到你的手机未安装外置内存卡,暂时无法继续下载,请先安装,在尝试升级", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 如果本地存在安装包,提示用户直接安装
     *
     * @param context
     * @param updateInfo
     */
    private void showInstallLocalApkDialog(Context context, UpdateAppBean updateInfo) {
        CommonDialog mDialog = new CommonDialog(context, R.style.dialog, "检测到本地已存在安装包,是否从本地安装?");
        mDialog.setLeftName("否", R.color.color_F0F0F0, view -> {
            checkNetwork(context, updateInfo);
            mDialog.dismiss();
        });
        mDialog.setRightName("是", R.color.color_22AACB, view -> {
            EventBus.getDefault().post(new AppUpdateBus(true));
            mDialog.dismiss();
        });
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.show();
    }


    /**
     * 从一个apk文件去获取该文件的版本信息
     *
     * @param context         本应用程序上下文
     * @param archiveFilePath APK文件的路径。如：/sdcard/download/XX.apk
     * @return
     */
    private static int getVersionNameFromApk(Context context, String archiveFilePath) {
        PackageManager pm = context.getPackageManager();
        /**
         * getPackageArchiveInfo()
         * (A PackageInfo object containing information about the package
         *  archive. If the package could not be parsed, returns null.)
         *  通过源码说明,如果安装包有可能解析失败并返回null,所以做下空指针异常的处理
         */
        PackageInfo packInfo = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
        if (packInfo == null) {
            return APK_PARSED_ERROR;
        }
        return packInfo.versionCode;
    }

    /**
     * 检查网络
     * 1.当前网络可用并且为WIFI 时自动静默下载并安装
     * 2.当前网络可用并且为移动网络且用户设置了移动网络下允许更新 时自动静默下载并安装
     * 3.当前网络可用并且为移动网络且用户未设置了移动网络下允许更新 时显示提示对话框
     *
     * @param context 上下文
     * @param updateInfo
     */
    private void checkNetwork(Context context, UpdateAppBean updateInfo) {
        if (!MyApplication.getInstance().isNetConnection()) {
            ToastUtils.showShort("当前网络不可用");
            return;
        }
        startUpdateServer(context, updateInfo);
    }

    /**
     * 开启服务下载并更新apk
     *
     * @param context
     * @param updateInfo
     */
    private void startUpdateServer(Context context, UpdateAppBean updateInfo) {
        ToastUtils.showShort("已开启后台下载");
        Intent intent = new Intent(context, DownAPKService.class);
        intent.putExtra(DownAPKService.UPDATE_KEY, updateInfo);
        context.startService(intent);
    }

}
