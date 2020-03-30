package com.wondersgroup.cardverification.mvp.activity.main;

import com.wondersgroup.cardverification.base.BaseContract;

/**
 * 主页配置约定
 */

public interface MainContract {
    interface View extends BaseContract.IView {
        /**
         * 查询权限进行登录
         */
        void updateVersion();


    }
    interface Presenter extends BaseContract.IPresenter<View> {
        /**
         * 检查版本更新
         */
        void checkVersion();

    }
}
