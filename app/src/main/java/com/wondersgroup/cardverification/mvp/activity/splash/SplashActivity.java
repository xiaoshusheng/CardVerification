package com.wondersgroup.cardverification.mvp.activity.splash;


import android.os.Handler;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.AppUtils;
import com.wondersgroup.cardverification.R;
import com.wondersgroup.cardverification.app.ActivityContracts;
import com.wondersgroup.cardverification.base.BaseActivity;

import butterknife.BindView;


public class SplashActivity extends BaseActivity<ISplashContract.ISplashPresenter> implements ISplashContract.ISplashView {

    @BindView(R.id.tv_version_code)
    TextView tvVersionCode;
    @BindView(R.id.tv_version_name)
    TextView tvVersionName;

    @Override
    protected ISplashContract.ISplashPresenter initPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    protected int getActivityLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        mPresenter.getVersionInfo();
    }

    /**
     * 检查版本更新
     */
    @Override
    public void checkOut() {
        int versionCode = AppUtils.getAppVersionCode();
        String versionName = AppUtils.getAppVersionName();
        tvVersionCode.setText("版本号： ".concat(String.valueOf(versionCode)));
        tvVersionName.setText("版本名称： ".concat(versionName));

        new Handler().postDelayed(() -> {
            ARouter.getInstance()
                    .build(ActivityContracts.ACTIVITY_MAIN)
                    .navigation();
            finish();
        }, 2000);
    }

}
