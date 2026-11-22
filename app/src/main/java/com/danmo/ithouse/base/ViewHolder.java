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
 * Last modified 2017-03-11 22:24:54
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.danmo.ithouse.base;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.danmo.commonutil.log.Logger;

/**
 * 鏅€歷iew鐨勮幏鍙栵紝绠＄悊锛岀紦瀛? */
public class ViewHolder {

    private SparseArray<View> mViews;
    private View mRootView;

    public ViewHolder(LayoutInflater inflater, ViewGroup parent, int layoutId) {
        this.mViews = new SparseArray<View>();
        mRootView = inflater.inflate(layoutId, parent, false);
    }

    /**
     * 閫氳繃View鐨刬d鏉ヨ幏鍙栧瓙View
     *
     * @param resId view鐨刬d
     * @param <T>   娉涘瀷
     * @return 瀛怴iew
     */
    public <T extends View> T get(int resId) {
        View view = mViews.get(resId);
        //濡傛灉璇iew娌℃湁缂撳瓨杩囷紝鍒欐煡鎵綱iew骞剁紦瀛?        if (view == null) {
            view = mRootView.findViewById(resId);
            mViews.put(resId, view);
        }
        if (view == null) {
            Logger.e("view is null...");
        }
        return (T) view;
    }

    /**
     * 鑾峰彇甯冨眬View
     *
     * @return 甯冨眬View
     */
    public View getRootView() {
        return mRootView;
    }

    /**
     * 璁剧疆鏂囨湰
     *
     * @param res_id view 鐨?id
     * @param text   鏂囨湰鍐呭
     * @return 鏄惁鎴愬姛
     */
    public boolean setText(CharSequence text, @NonNull int res_id) {
        try {
            TextView textView = get(res_id);
            textView.setText(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean setText(@NonNull int res_id, CharSequence text) {
        return setText(text, res_id);
    }

    public void loadImage(Context context, String url, int res_id) {
        ImageView imageView = get(res_id);
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }

    /**
     * 璁剧疆鐩戝惉鍣?     *
     * @param l   鐩戝惉鍣?     * @param ids view 鐨?id
     */
    public void setOnClickListener(View.OnClickListener l, int... ids) {
        if (ids == null) {
            return;
        }
        for (int id : ids) {
            get(id).setOnClickListener(l);
        }
    }
}
