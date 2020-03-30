package com.wondersgroup.cardverification.mvp.activity.main;


import android.content.Context;

import com.blankj.utilcode.util.AppUtils;
import com.wondersgroup.cardverification.base.BasePresenter;
import com.wondersgroup.cardverification.model.bean.UpdateAppBean;
import com.wondersgroup.cardverification.net.RetrofitHelper;
import com.wondersgroup.cardverification.net.RxSchedulers;
import com.wondersgroup.cardverification.net.VObserver;
import com.wondersgroup.cardverification.widget.UpdateApi;


/**
 * 主页面presenter
 */

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    public MainPresenter(MainContract.View view) {
        super(view);
    }


    /**
     * 检查版本更新
     */
    @Override
    public void checkVersion() {
        //检查权限
        RetrofitHelper.getInstance().getServer()
                .checkVersion()
                .compose(RxSchedulers.applySchedulers())
                .as(bindToLife())
                .subscribe(new VObserver<UpdateAppBean>() {
                    @Override
                    protected void onSuccess(UpdateAppBean updateAppBean) {
                        String versionName = AppUtils.getAppVersionName();
                        if (updateAppBean != null) {
                            if (!versionName.equals(updateAppBean.getLatestVersion())) {
                                UpdateApi.getInstance().checkVersion((Context) mView, updateAppBean);
                            }
                        }
                    }

                });

    }

}
