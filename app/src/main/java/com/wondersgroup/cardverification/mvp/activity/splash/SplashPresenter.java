package com.wondersgroup.cardverification.mvp.activity.splash;


import com.wondersgroup.cardverification.base.BasePresenter;

/**
 * Great by YangZL
 * created on 2019/5/21
 * description:
 */
public class SplashPresenter extends BasePresenter<ISplashContract.ISplashView> implements ISplashContract.ISplashPresenter {

    public SplashPresenter(ISplashContract.ISplashView view) {
        super(view);
    }

    @Override
    public void getVersionInfo() {
        mView.checkOut();
    }
}
