package com.danmo.commonapi.event;

import androidx.annotation.Nullable;


public class GetCommunityCatetoryEvent extends GetCommunityListEvent {
    public GetCommunityCatetoryEvent(@Nullable String uuid) {
        super(uuid);
    }
}

