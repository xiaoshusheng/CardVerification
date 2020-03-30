package com.wondersgroup.cardverification.utils.permisstion;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.wondersgroup.cardverification.R;
import com.wondersgroup.cardverification.view.update.CommonDialog;

import io.reactivex.functions.Consumer;

/**
 * Created by on 2019/5/21
 * description:
 * 多个权限回调统一处理类
 */
public abstract class CustomConsumer implements Consumer<Boolean> {
    private Activity mActivity;

    public CustomConsumer(Activity activity) {
        mActivity = activity;
    }

    /**
     * 权限被允许的回调
     */
    public abstract void onGranted();

    @Override
    public void accept(Boolean granted) throws Exception {
        if (granted) {
            // 用户已经同意该权限
            onGranted();
        } else {
            // 用户拒绝了该权限，并且选中『不再询问』
            CommonDialog mDialog = new CommonDialog(mActivity, R.style.dialog, "未取得此功能的相关权限，为了保证此功能的正常使用，请前往应用权限设置打开相关权限");
            mDialog.setLeftName("取消", R.color.color_22AACB, view -> mDialog.dismiss());
            mDialog.setRightName("去打开", R.color.color_22AACB, view -> {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                intent.setData(uri);
                mActivity.startActivity(intent);
                mDialog.dismiss();
            });
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.setCancelable(true);
            mDialog.show();
        }
    }


}
