package com.danmo.commonapi.event;

import androidx.annotation.Nullable;


public class GetHotGoodsRankEvent extends GetHotGoodsListEvent {
    public GetHotGoodsRankEvent(@Nullable String uuid) {
        super(uuid);
    }
}

