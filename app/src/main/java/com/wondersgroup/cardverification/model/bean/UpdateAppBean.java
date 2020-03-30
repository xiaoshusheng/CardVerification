package com.wondersgroup.cardverification.model.bean;

import java.io.Serializable;

/**
 * 版本更新的bean
 */
public class UpdateAppBean implements Serializable {
    private String versionNumber;
    private String latestVersion;
    private String mustUpdate;
    private String url;
    private String context;

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getMustUpdate() {
        return mustUpdate;
    }

    public void setMustUpdate(String mustUpdate) {
        this.mustUpdate = mustUpdate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
