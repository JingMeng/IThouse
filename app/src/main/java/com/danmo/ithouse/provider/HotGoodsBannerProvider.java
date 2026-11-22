package com.danmo.ithouse.provider;

import android.content.Context;
import androidx.annotation.NonNull;

import com.danmo.commonapi.base.Constant;
import com.danmo.commonapi.bean.hotgoods.HotGoodsItem;
import com.danmo.commonutil.UrlUtil;
import com.danmo.commonutil.recyclerview.adapter.base.RecyclerViewHolder;
import com.danmo.commonutil.recyclerview.adapter.multitype.BaseViewProvider;
import com.danmo.ithouse.R;
import com.danmo.ithouse.activity.WebViewActivity;
import com.danmo.ithouse.util.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 杈ｅ搧banner 鏁版嵁缁戝畾
 */

public class HotGoodsBannerProvider extends BaseViewProvider<List<HotGoodsItem>> {
    public HotGoodsBannerProvider(@NonNull Context context) {
        super(context, R.layout.item_hotgoods_banner);
    }

    @Override
    public void onBindView(RecyclerViewHolder holder, final List<HotGoodsItem> bean) {
        if (bean == null) {
            return;
        }
        List<String> images = new ArrayList<String>();
        for (HotGoodsItem item : bean) {
            images.add(Constant.LAPIN_PIC_URL + item.Picture);
        }
        Banner banner = (Banner) holder.get(R.id.banner);
        //璁剧疆banner鏍峰紡
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        //璁剧疆鍥剧墖鍔犺浇鍣?        banner.setImageLoader(new GlideImageLoader());
        //璁剧疆鍥剧墖闆嗗悎
        banner.setImages(images);
        //banner璁剧疆鏂规硶鍏ㄩ儴璋冪敤瀹屾瘯鏃舵渶鍚庤皟鐢?        banner.setDelayTime(4 * 1000);
        banner.start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (position >= 0) {
                    String link = bean.get(position).Url;
                    if (UrlUtil.isUrlPrefix(link)) {
                        WebViewActivity.start(mContext, link, WebViewActivity.PAGE_TYPE_HOTGOODS);
                    }
                }
            }
        });

    }
}

