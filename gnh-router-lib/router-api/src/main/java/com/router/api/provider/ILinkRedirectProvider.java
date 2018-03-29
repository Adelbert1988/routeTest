package com.router.api.provider;

import android.content.Context;
import android.net.Uri;

/**
 * User: chw
 * Date: 2018/3/23
 * link跳转重定向
 */

public abstract class ILinkRedirectProvider implements IProvider {

    /**
     * 重定向内部path
     * @param originalPath
     * @return
     */
    public String redirectPath(String originalPath) {
        return originalPath;
    };

    /**
     * 重定向外部uri
     * @param originalUri
     * @return
     */
    public Uri redirectUri(Uri originalUri) {
        return originalUri;
    };

    @Override
    public void init(Context context) {

    }
}
