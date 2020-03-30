package com.wondersgroup.cardverification.mvp.activity.splash;


import com.wondersgroup.cardverification.base.BaseContract;

/**
 * Great by YangZL
 * created on 2019/5/21
 * description: 启动页契约接口
 */
public interface ISplashContract {
    interface ISplashView extends BaseContract.IView {
        /**
         * 检查是否有版本升级
         */
        void checkOut();
    }

    interface ISplashPresenter extends BaseContract.IPresenter<ISplashView> {
        /**
         * 获取版本数据
         */
        void getVersionInfo();
    }
}
