package com.wondersgroup.cardverification.base;


import org.greenrobot.greendao.annotation.NotNull;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * Great by YangZL
 * created on 2019/5/21
 * description: 基本配置约定
 */
public interface BaseContract {

    interface IPresenter<T extends IView> extends LifecycleObserver {

        void setLifecycleOwner(LifecycleOwner lifecycleOwner);

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        void onCreate(@NotNull LifecycleOwner owner);

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        void onDestroy(@NotNull LifecycleOwner owner);

        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        void onLifecycleChanged(@NotNull LifecycleOwner owner,
                                @NotNull Lifecycle.Event event);
    }

    interface IView {

        /**
         * 显示进度中
         */
        void showLoading(String msg, boolean cancelable);

        /**
         * 隐藏进度
         */
        void hideLoading();

        /**
         * 显示请求成功
         *
         * @param message
         */
        void showSuccess(String message);

        /**
         * 失败重试
         *
         * @param message
         */
        void showFailed(String message);

        /**
         * 显示当前网络不可用
         */
        void showNoNet();

        /**
         * 重试
         */
        void onRetry();

    }
    interface IModel{

    }
}
