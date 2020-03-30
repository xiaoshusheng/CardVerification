package com.wondersgroup.cardverification.view.update;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wondersgroup.cardverification.R;

/**
 * Created by fjw on 2017/11/1.
 * 通用dialog
 */

public class CommonDialog extends Dialog {

    private TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;
    private View mViewLineH;
    private View mViewLineV;


    private Context mContext;
    private String content;
    private CharSequence content1;
    private View.OnClickListener leftBtnListener;
    private View.OnClickListener rightBtnListener;
    private String leftName;
    private String rightName;
    private String title;
    private int leftColorId;
    private int rightColorId;
    private boolean mLineH = true;
    private boolean mLineV = true;
    private boolean mLeftName = true;
    private boolean mRightName = true;

    public CommonDialog(Context context) {
        super(context, R.style.dialog);
        this.mContext = context;
    }

    public CommonDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public CommonDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
    }

    public CommonDialog(Context context, int themeResId, CharSequence content) {
        super(context, themeResId);
        this.mContext = context;
        this.content1 = content;
    }

    public CommonDialog(Context context, int themeResId, String content, String title) {
        super(context, themeResId);
        this.mContext = context;
        this.content = content;
        this.title = title;
    }

    public CommonDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getLeftName() {
        return leftName;
    }

    /**
     * 设置左按钮
     *
     * @param leftName
     * @param colorId         按钮文字颜色id 0 保持原色
     * @param leftBtnListener
     */
    public void setLeftName(String leftName, int colorId, View.OnClickListener leftBtnListener) {
        this.leftName = leftName;
        this.leftBtnListener = leftBtnListener;
        this.leftColorId = colorId;
    }

    public String getRightName() {
        return rightName;
    }

    /**
     * 设置右按钮
     *
     * @param rightName
     * @param colorId          按钮文字颜色 0 保持原色
     * @param rightBtnListener
     */
    public void setRightName(String rightName, int colorId, View.OnClickListener rightBtnListener) {
        this.rightName = rightName;
        this.rightBtnListener = rightBtnListener;
        this.rightColorId = colorId;
    }

    /**
     * 设置分割线的显隐性
     *
     * @param lineH 水平分割线
     * @param lineV 垂直分割线
     */
    public void setLineVisibility(boolean lineH, boolean lineV, boolean leftName, boolean rightName) {
        mLineH = lineH;
        mLineV = lineV;
        mLeftName = leftName;
        mRightName = rightName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_common_dialog);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        contentTxt = findViewById(R.id.content);
        titleTxt = findViewById(R.id.title);
        submitTxt = findViewById(R.id.submitBtn);
        cancelTxt = findViewById(R.id.cancelBtn);
        mViewLineH = findViewById(R.id.view_line_h);
        mViewLineV = findViewById(R.id.view_line_v);

        if (leftColorId > 0) {
            ColorStateList csl = this.mContext.getResources().getColorStateList(leftColorId);
            if (csl != null) {
                cancelTxt.setTextColor(csl);
            }
        }
        if (rightColorId > 0) {
            ColorStateList csl = this.mContext.getResources().getColorStateList(rightColorId);
            if (csl != null) {
                submitTxt.setTextColor(csl);
            }
        }
        if (rightBtnListener != null) {
            submitTxt.setOnClickListener(rightBtnListener);
        }

        if (leftBtnListener != null) {
            cancelTxt.setOnClickListener(leftBtnListener);
        }
        if (TextUtils.isEmpty(content)) {
            contentTxt.setText(content1);
        } else {
            contentTxt.setText(content);
        }
        if (!TextUtils.isEmpty(rightName)) {
            submitTxt.setText(rightName);
        }

        if (!TextUtils.isEmpty(leftName)) {
            cancelTxt.setText(leftName);
        }

        if (!TextUtils.isEmpty(title)) {
            titleTxt.setText(title);
            titleTxt.setVisibility(View.VISIBLE);
        } else {
            titleTxt.setVisibility(View.GONE);
        }
        mViewLineH.setVisibility(mLineH ? View.VISIBLE : View.GONE);
        mViewLineV.setVisibility(mLineV ? View.VISIBLE : View.GONE);
        cancelTxt.setVisibility(mLeftName ? View.VISIBLE : View.GONE);
        submitTxt.setVisibility(mRightName ? View.VISIBLE : View.INVISIBLE);

    }
}


