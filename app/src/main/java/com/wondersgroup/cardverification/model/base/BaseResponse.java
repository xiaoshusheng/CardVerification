package com.wondersgroup.cardverification.model.base;


import com.wondersgroup.cardverification.net.StatusCode;

import java.io.Serializable;

/**
 * Great by YangZL
 * created on 2019/5/21
 * description: 返回体基础类
 */
public class BaseResponse<T> implements IResponse, Serializable {
    private int code;

    private String msg;

    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean isSuccess() {
        return code == StatusCode.SUCCESS;
    }
}
