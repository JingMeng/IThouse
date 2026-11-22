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
 *
 * Last modified 2017-03-08 01:01:18
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.danmo.commonapi.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 鎵€鏈?Event 鐨勫熀绫? * <p>
 * T 涓哄搴旂殑瀹炰綋绫? * <p>
 * HTTP Status
 * -1 - 鍙兘鏄綉缁滄湭杩炴帴
 * 200, 201 - 璇锋眰鎴愬姛锛屾垨鎵ц鎴愬姛
 * 400 - 鍙傛暟涓嶇鍚?API 鐨勮姹傘€佹垨鑰呮暟鎹牸寮忛獙璇佹病鏈夐€氳繃锛岃閰嶅悎 Response Body 閲岄潰鐨?error 淇℃伅纭畾闂銆? * 401 - 鐢ㄦ埛璁よ瘉澶辫触锛屾垨缂哄皯璁よ瘉淇℃伅锛屾瘮濡?access_token 杩囨湡锛屾垨娌′紶锛屽彲浠ュ皾璇曠敤 refresh_token 鏂瑰紡鑾峰緱鏂扮殑 access_token銆? * 403 - 褰撳墠鐢ㄦ埛瀵硅祫婧愭病鏈夋搷浣滄潈闄? * 404 - 璧勬簮涓嶅瓨鍦ㄣ€? * 500 - 鏈嶅姟鍣ㄥ紓甯? */
public class BaseEvent<T> {
    protected String uuid = "";         // 閫氱敤鍞竴璇嗗埆鐮?(Universally Unique Identifier)
    protected boolean ok = false;       // 鏄惁鑾峰彇瀹炰綋鏁版嵁(T)鎴愬姛
    protected Integer code = -1;        // 鐘舵€佺爜
    protected T t;                      // 瀹炰綋绫?
    /**
     * @param uuid 鍞竴璇嗗埆鐮?     */
    public BaseEvent(@Nullable String uuid) {
        this.ok = false;
        this.uuid = uuid;
    }

    /**
     * @param uuid 鍞竴璇嗗埆鐮?     * @param code 缃戠粶杩斿洖鐮?     * @param t    瀹炰綋鏁版嵁
     */
    public BaseEvent(@Nullable String uuid, @NonNull Integer code, @Nullable T t) {
        this.ok = null != t;
        this.uuid = uuid;
        this.code = code;
        this.t = t;
    }

    /**
     * @param code 缃戠粶杩斿洖鐮?     * @param t    瀹炰綋鏁版嵁
     */
    public BaseEvent setEvent(@NonNull Integer code, @Nullable T t) {
        this.ok = null != t;
        this.code = code;
        this.t = t;
        return this;
    }

    /**
     * 鍒ゆ柇鏄惁鑾峰彇瀹炰綋绫绘垚鍔?     *
     * @return ok
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * 鑾峰彇瀹炰綋绫?     *
     * @return 瀹炰綋绫绘暟鎹?鍙兘涓簄ull)
     */
    public T getBean() {
        return t;
    }


    /**
     * 鑾峰彇褰撳墠 Event 鐨?uuid
     *
     * @return uuid
     */
    public String getUUID() {
        return uuid;
    }

    /**
     * 鍒ゆ柇鐘舵€?     *
     * @return 鐘舵€佸悧
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 鑾峰彇杩斿洖鐮佽鎯?     *
     * @return 鎻忚堪淇℃伅
     */
    public String getCodeDescribe() {
        switch (code) {
            case -1:
                return "鍙兘鏄綉缁滄湭杩炴帴锛屾垨鑰呮暟鎹浆鍖栧け璐?;
            case 200:
            case 201:
                return "璇锋眰鎴愬姛锛屾垨鎵ц鎴愬姛銆?;
            case 400:
                return "鍙傛暟涓嶇鍚?API 鐨勮姹傘€佹垨鑰呮暟鎹牸寮忛獙璇佹病鏈夐€氳繃";
            case 401:
                return "鐢ㄦ埛璁よ瘉澶辫触锛屾垨缂哄皯璁よ瘉淇℃伅锛屾瘮濡?access_token 杩囨湡锛屾垨娌′紶锛屽彲浠ュ皾璇曠敤 refresh_token 鏂瑰紡鑾峰緱鏂扮殑 access_token";
            case 403:
                return "褰撳墠鐢ㄦ埛瀵硅祫婧愭病鏈夋搷浣滄潈闄?;
            case 404:
                return "璧勬簮涓嶅瓨鍦?;
            case 500:
                return "鏈嶅姟鍣ㄥ紓甯?;
            case 402:
                return "鐢ㄦ埛灏氭湭鐧诲綍";
            default:
                return "鏈煡寮傚父(" + code + ")";
        }
    }
}

