/*
 * Copyright 2017 GcsSloop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.danmo.commonapi.base;

import android.content.Context;
import androidx.annotation.NonNull;

import com.danmo.commonapi.converter.StringConverterFactory;

import java.lang.reflect.ParameterizedType;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * 瀹炵幇绫伙紝鍏蜂綋瀹炵幇鍦ㄦ澶? *
 * @param <Service>
 */
public class BaseImpl<Service> {
    private static Retrofit mRetrofit;
    protected CacheUtil mCacheUtil;
    protected Service mService;
    protected static int currentParse;
    private static String currentBaseUrl;

    public BaseImpl(@NonNull Context context, String baseUrl, int currentParse) {
        mCacheUtil = new CacheUtil(context.getApplicationContext());
        if (null != mRetrofit && this.currentParse == currentParse && this.currentBaseUrl.equals(baseUrl)) {
            return;
        }
        initRetrofit(baseUrl, currentParse);
        this.mService = mRetrofit.create(getServiceClass());
        this.currentParse = currentParse;
        this.currentBaseUrl = baseUrl;
    }

    private Class<Service> getServiceClass() {
        return (Class<Service>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private void initRetrofit(String baseUrl, int currentParse) {

        // 璁剧疆 Log 鎷︽埅鍣紝鍙互鐢ㄤ簬浠ュ悗澶勭悊涓€浜涘紓甯告儏鍐?        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 閰嶇疆 client
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)                // 璁剧疆鎷︽埅鍣?                .retryOnConnectionFailure(true)             // 鏄惁閲嶈瘯
                .connectTimeout(5, TimeUnit.SECONDS)        // 杩炴帴瓒呮椂浜嬩欢
                .readTimeout(5, TimeUnit.SECONDS)           // 璇诲彇瓒呮椂鏃堕棿
                .build();

        // 閰嶇疆 Retrofit

        if (currentParse == Constant.PARSE_GSON) {
            Retrofit.Builder builder = new Retrofit.Builder();
            mRetrofit = builder.baseUrl(baseUrl)                         // 璁剧疆 base url
                    .client(client)                                     // 璁剧疆 client
                    .addConverterFactory(GsonConverterFactory.create()) // 璁剧疆 Json 杞崲宸ュ叿
                    .build();
        } else if (currentParse == Constant.PARSE_XML) {
            Retrofit.Builder builder = new Retrofit.Builder();
            mRetrofit = builder.baseUrl(baseUrl)                         // 璁剧疆 base url
                    .client(client)                                     // 璁剧疆 client
                    .addConverterFactory(SimpleXmlConverterFactory.create())// 璁剧疆xml杞崲宸ュ叿
                    .build();
        } else {
            Retrofit.Builder builder = new Retrofit.Builder();
            mRetrofit = builder.baseUrl(baseUrl)                         // 璁剧疆 base url
                    .client(client)                                     // 璁剧疆 client
                    .addConverterFactory(StringConverterFactory.create()) // 璁剧疆 string 杞崲宸ュ叿
                    .build();
        }

    }


}




