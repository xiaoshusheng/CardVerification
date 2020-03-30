package com.wondersgroup.cardverification.model.bus;

/**
 * Description 发送拍照成功后返回的base64
 */
public class CropCardBus {
    private boolean isSuccess;//是否成功
    private String imageBase64;

    public CropCardBus(boolean isSuccess, String imageBase64) {
        this.isSuccess = isSuccess;
        this.imageBase64 = imageBase64;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}
