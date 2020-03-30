package com.hua.note.config;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import cn.wl.android.lib.config.WLConfig;

/**
 * Created by Iaovy on 2020/3/23 10:23
 *
 * @email Cymbidium@outlook.com
 */
public class App extends MultiDexApplication {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        WLConfig.init(mContext, false, "");
    }
}
