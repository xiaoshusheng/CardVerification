package com.wondersgroup.cardverification.view;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.wondersgroup.cardverification.R;


/**
 * 自定义进度loadingDialog
 * 携带logo外带旋转的对话框
 */ 
public class ProgressDialog extends Dialog {

    private Context mContext;
    private static ProgressDialog mLoadingDialog;
    private ImageView mIvProgress;
    private TextView mTvTip;

    public ProgressDialog(Context context) {
        super(context);
        mContext = context;
    }

    public ProgressDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;

    }

    //显示dialog的方法
    public static ProgressDialog getInstance(Context context) {
        mLoadingDialog = new ProgressDialog(context, R.style.loadingDialog);//dialog样式
        mLoadingDialog.setContentView(R.layout.loading_dialog_layout);//dialog布局文件
        mLoadingDialog.setCanceledOnTouchOutside(false);//点击外部不允许关闭dialog
        return mLoadingDialog;
    }

    public static ProgressDialog getInstance(Context context, String content) {
        mLoadingDialog = new ProgressDialog(context, R.style.loadingDialog);//dialog样式
        mLoadingDialog.setContentView(R.layout.loading_dialog_layout);//dialog布局文件
        mLoadingDialog.mTvTip = mLoadingDialog.findViewById(R.id.dialog_tvText);
        mLoadingDialog.mTvTip.setText(content);
        mLoadingDialog.setCanceledOnTouchOutside(false);//点击外部不允许关闭dialog
        return mLoadingDialog;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && mLoadingDialog != null) {
            mIvProgress = mLoadingDialog.findViewById(R.id.ivProgress);
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.dialog_progress_anim);
            mIvProgress.startAnimation(animation);
        }
    }
}
