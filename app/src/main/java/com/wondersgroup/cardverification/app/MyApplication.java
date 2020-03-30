package com.wondersgroup.cardverification.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.StrictMode;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.Utils;
import com.wondersgroup.cardverification.utils.net.NetUtil;

/**
 * Great by YangZL
 * created on 2019/8/29
 * description: 全局app
 */
public class MyApplication extends Application {

    //ARouter 调试开关
    private boolean isDebugARouter = true;
    private int mCurrentNetType = NetUtil.NET_NO_CONNECTION;

    /**
     * 全局的上下文
     */
    private static Context mContext;
    protected static MyApplication mInstance;

    public static MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mInstance = this;
        mCurrentNetType = NetUtil.checkNetworkType(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        if (isDebugARouter) {//开启调试模式
            ARouter.openLog();//打印日志
            ARouter.openDebug();//线上版本需关闭否则有风险
        }
        ARouter.init(MyApplication.this);

        //初始化工具包
        Utils.init(this);
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * 下面两个方法是控制字体不跟随系统字体变化的
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ARouter.getInstance().destroy();//释放资源
    }

    public boolean isNetConnection() {
        return mCurrentNetType != NetUtil.NET_NO_CONNECTION;
    }

}
