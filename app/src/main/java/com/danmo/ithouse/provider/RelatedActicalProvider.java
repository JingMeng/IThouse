package com.danmo.ithouse.provider;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;

import com.danmo.commonapi.bean.newest.detail.DetailRelatedItem;
import com.danmo.commonutil.recyclerview.adapter.base.RecyclerViewHolder;
import com.danmo.commonutil.recyclerview.adapter.multitype.BaseViewProvider;
import com.danmo.ithouse.R;

/**
 * 鍐呭璇︽儏椤电浉鍏虫枃绔犳暟鎹粦瀹? */

public class RelatedActicalProvider extends BaseViewProvider<DetailRelatedItem> {
    public RelatedActicalProvider(@NonNull Context context) {
        super(context, R.layout.item_detail_related_layout);
    }

    @Override
    public void onBindView(RecyclerViewHolder holder, DetailRelatedItem bean) {

        if (bean == null) {
            return;
        }
        if (holder.getLayoutPosition() == 0) {//todo
            holder.get(R.id.detail_related_head).setVisibility(View.VISIBLE);
            holder.setText(R.id.detail_related_head, bean.category);
        } else {
            holder.get(R.id.detail_related_head).setVisibility(View.GONE);
        }

        holder.setText(R.id.detail_related_item_time, bean.postdate);
        holder.setText(R.id.detail_related_item_title, bean.newstitle);

        if (holder.getLayoutPosition() == 2) {//todo
            holder.get(R.id.detail_related_item_line).setVisibility(View.INVISIBLE);
        }


    }
}

