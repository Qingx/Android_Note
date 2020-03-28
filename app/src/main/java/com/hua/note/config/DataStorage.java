package com.hua.note.config;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Iaovy on 2020/3/24 9:42
 *
 * @email Cymbidium@outlook.com
 */
public final class DataStorage {
    private static final String EMPTY_USER_NAME = "";

    private static final AtomicReference<String> mSignName = new AtomicReference<>(EMPTY_USER_NAME);

    public static void saveSignName(String sign) {
        mSignName.set(sign);
    }

    public static String getSignName() {
        return mSignName.get();
    }

    public static void restSignName() {
        mSignName.set(EMPTY_USER_NAME);
    }
}
