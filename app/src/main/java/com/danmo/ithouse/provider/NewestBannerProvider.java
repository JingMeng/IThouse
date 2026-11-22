package com.danmo.ithouse.provider;

import android.content.Context;
import androidx.annotation.NonNull;

import com.danmo.commonapi.bean.newest.Newest;
import com.danmo.commonutil.UrlUtil;
import com.danmo.commonutil.recyclerview.adapter.base.RecyclerViewHolder;
import com.danmo.commonutil.recyclerview.adapter.multitype.BaseViewProvider;
import com.danmo.ithouse.R;
import com.danmo.ithouse.activity.NewsDetailActivity;
import com.danmo.ithouse.activity.WebViewActivity;
import com.danmo.ithouse.util.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 璧勮banner鏁版嵁缁戝畾
 */

public class NewestBannerProvider extends BaseViewProvider<Newest> {

    public NewestBannerProvider(@NonNull Context context) {
        super(context, R.layout.item_fragment_newest_banner);
    }

    @Override
    public void onBindView(RecyclerViewHolder holder, final Newest bean) {
        List<String> images = new ArrayList<String>();
        List<String> titles = new ArrayList<String>();
        for (int i = 0; i < bean.item.size(); i++) {
            titles.add(bean.item.get(i).title);
            images.add(bean.item.get(i).image);
        }
        Banner banner = (Banner) holder.get(R.id.banner);
        //璁剧疆banner鏍峰紡
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //璁剧疆鏍囬闆嗗悎锛堝綋banner鏍峰紡鏈夋樉绀簍itle鏃讹級
        banner.setBannerTitles(titles);
        //璁剧疆鍥剧墖鍔犺浇鍣?        banner.setImageLoader(new GlideImageLoader());
        //璁剧疆鍥剧墖闆嗗悎
        banner.setImages(images);
        //banner璁剧疆鏂规硶鍏ㄩ儴璋冪敤瀹屾瘯鏃舵渶鍚庤皟鐢?        banner.setDelayTime(4 * 1000);
        banner.start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (position >= 0) {
                    String link = bean.item.get(position).link;
                    if (UrlUtil.isUrlPrefix(link)) {
                        WebViewActivity.start(mContext, link);
                    } else {
                        NewsDetailActivity.start(mContext, NewsDetailActivity.TYPE_BANNER, link);
                    }
                }
            }
        });

    }
}

