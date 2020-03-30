package com.wondersgroup.cardverification.model.bus;

/**
 * Description 发送下载完成进行安装
 */
public class AppUpdateBus {
    private boolean isSuccess;//是否成功

    public AppUpdateBus(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
