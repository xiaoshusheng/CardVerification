package com.wondersgroup.cardverification.widget;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;


/**
 * Great by YangZL
 * created on 2019/5/24
 * description: glide配置类
 */
@GlideModule
public class MyAppGlideModule extends AppGlideModule {

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
