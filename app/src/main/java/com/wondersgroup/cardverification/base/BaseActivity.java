package com.wondersgroup.cardverification.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

import com.blankj.utilcode.util.ToastUtils;
import com.wondersgroup.cardverification.view.ProgressDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Great by YangZL
 * created on 2019/5/21
 * description: 界面基类
 */
public abstract class BaseActivity<P extends BaseContract.IPresenter> extends AppCompatActivity implements BaseContract.IView {
    protected Activity mContext;
    protected P mPresenter;
    private Unbinder mUnBinder;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getActivityLayoutID());
        //强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;
        //初始化ButterKnife
        mUnBinder = ButterKnife.bind(this);
        mPresenter = initPresenter();
        mPresenter.setLifecycleOwner(this);
        getLifecycle().addObserver(mPresenter);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnBinder != Unbinder.EMPTY) {
            mUnBinder.unbind();
        }
    }
    /**
     * 初始化View
     */
    protected abstract void initView();
    /**
     * 在子View中初始化Presenter
     *
     * @return
     */
    protected abstract P initPresenter();
    /**
     * 设置Activity的布局ID
     *
     * @return
     */
    protected abstract int getActivityLayoutID();

    @Override
    public void showLoading(String msg, boolean cancelable) {
        if (mDialog == null) {
            mDialog = ProgressDialog.getInstance(this, msg);//实例化progressDialog
            mDialog.setCanceledOnTouchOutside(cancelable);
        }
        if (!mDialog.isShowing()) {//如果进度条没有显示
            mDialog.show();//显示进度条
        }
    }

    @Override
    public void hideLoading() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    public void showSuccess(String message) {
        ToastUtils.showShort(message);
    }

    @Override
    public void showFailed(String message) {
        ToastUtils.showShort(message);
    }

    @Override
    public void showNoNet() {
        ToastUtils.showShort("无网络");
    }

    @Override
    public void onRetry() {

    }
}
