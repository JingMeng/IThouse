package com.danmo.commonapi.event;

import androidx.annotation.Nullable;


public class GetHotGoodsBannerEvent extends GetHotGoodsListEvent {
    public GetHotGoodsBannerEvent(@Nullable String uuid) {
        super(uuid);
    }
}

