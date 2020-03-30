package com.wondersgroup.cardverification.view.update;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.wondersgroup.cardverification.R;


/**
 * Great by YangZL
 * created on 2019/6/10
 * description: 用于app更新的对话框
 */
public class UpdateDialog extends Dialog {
    private static final String TAG = UpdateDialog.class.getSimpleName();
    private static UpdateDialog mUpdateDialog;
    private ImageView mIvCancel;
    private TextView mTvContent;
    private TextView mTvUpdateNow;
    private UpdateCallback mCallback;

    private UpdateDialog(Context context, int themeResId, UpdateCallback callback) {
        super(context, themeResId);
        mCallback = callback;

    }

    public static UpdateDialog getInstance(Context context, String content, UpdateCallback callback) {
        mUpdateDialog = new UpdateDialog(context, R.style.loadingDialog, callback);//dialog样式
        mUpdateDialog.setContentView(R.layout.diag_update);//dialog布局文件
        mUpdateDialog.mIvCancel = mUpdateDialog.findViewById(R.id.iv_update_cancel);
        mUpdateDialog.mTvContent = mUpdateDialog.findViewById(R.id.tv_update_content);
        mUpdateDialog.mTvUpdateNow = mUpdateDialog.findViewById(R.id.tv_update_now);
        String[] intros = content.split("#");
        for (int i = 0; i < intros.length; i++) {
            mUpdateDialog.mTvContent.append(intros[i]);
            mUpdateDialog.mTvContent.append("\n\n");
        }
//        mUpdateDialog.mTvContent.setText(content);
        mUpdateDialog.setCancelable(false);
        mUpdateDialog.setCanceledOnTouchOutside(false);
        mUpdateDialog.mIvCancel.setOnClickListener(v -> {
            mUpdateDialog.mCallback.cancel();
            mUpdateDialog.dismiss();
        });
        mUpdateDialog.mTvUpdateNow.setOnClickListener(v -> {
            mUpdateDialog.mCallback.updateNow();
            mUpdateDialog.dismiss();
        });
        return mUpdateDialog;
    }

    /**
     * 用于点击叉号和立即更新的接口回调
     */
    public interface UpdateCallback {

        void cancel();

        void updateNow();

    }
}
