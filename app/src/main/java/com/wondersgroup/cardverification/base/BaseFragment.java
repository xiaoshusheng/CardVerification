package com.wondersgroup.cardverification.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.wondersgroup.cardverification.view.ProgressDialog;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Great by YangZL
 * created on 2019/5/21
 * description: 全局基类Fragment，所有的Fragment继承这个Fragment
 */
public abstract class BaseFragment<P extends BaseContract.IPresenter> extends Fragment implements BaseContract.IView {
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    protected View mView;
    protected Activity mActivity;
    protected Context mContext;
    private Unbinder mUnBinder;
    protected P mPresenter;
    private ProgressDialog mDialog;

    public final String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
        mPresenter.setLifecycleOwner(this);
        getLifecycle().addObserver(mPresenter);
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getFragmentLayoutID(), container, false);
        mActivity = getActivity();
        mContext = mActivity;
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //绑定
        mUnBinder = ButterKnife.bind(this, view);
        initView(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    /**
     * 初始化Presenter
     *
     * @return
     */
    protected abstract P initPresenter();

    /**
     * 获取Fragment的LayoutID
     *
     * @return
     */
    protected abstract int getFragmentLayoutID();

    /**
     * 初始化View
     *
     * @param view
     * @param savedInstanceState
     */
    protected abstract void initView(View view, Bundle savedInstanceState);

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void showLoading(String msg, boolean cancelable) {
        if (mDialog == null) {
            mDialog = ProgressDialog.getInstance(mActivity, msg);//实例化progressDialog
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
    public void showSuccess(String successMsg) {
        ToastUtils.showShort(successMsg);
    }

    @Override
    public void showFailed(String errorMsg) {
        ToastUtils.showShort(errorMsg);
    }

    @Override
    public void showNoNet() {
    }

    @Override
    public void onRetry() {
        ToastUtils.showShort("onRetry");
    }


}
