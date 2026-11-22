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
 * Last modified 2017-04-09 21:16:47
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.danmo.ithouse.fragment.refresh;

import android.content.Context;
import androidx.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.danmo.commonapi.base.BaseEvent;
import com.danmo.commonapi.bean.hotgoods.HotGoodsTopNode;
import com.danmo.commonapi.bean.newest.NewestTopNode;
import com.danmo.commonutil.recyclerview.adapter.base.RecyclerViewHolder;
import com.danmo.commonutil.recyclerview.adapter.multitype.HeaderFooterAdapter;
import com.danmo.ithouse.R;
import com.danmo.ithouse.base.BaseFragment;
import com.danmo.ithouse.base.ViewHolder;
import com.danmo.ithouse.util.EventBusMsg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 鍏锋湁涓嬫媺鍒锋柊鍜屼笂鎷夊姞杞界殑Fragment
 */
public abstract class RefreshRecyclerFragment<T, Event extends BaseEvent<T>> extends
        BaseFragment {
    // 璇锋眰鐘舵€?- 涓嬫媺鍒锋柊 杩樻槸 鍔犺浇鏇村
    public static final String POST_HEADER = "load_header";
    public static final String POST_LOAD_MORE = "load_more";
    public static final String POST_REFRESH = "refresh";
    public static final String POST_MID = "mid";
    // 褰撳墠鐘舵€?    private static final int STATE_NORMAL = 0;      // 姝ｅ父
    private static final int STATE_NO_MORE = 1;     // 姝ｅ湪
    private static final int STATE_LOADING = 2;     // 鍔犺浇
    private static final int STATE_REFRESH = 3;     // 鍒锋柊
    // 鍒嗛〉鍔犺浇
    protected int pageIndex = 0;                      // 褰撻潰椤电爜
    protected int pageCount = 20;                     // 姣忛〉涓暟
    protected RecyclerView mRecyclerView;
    // 閫傞厤鍣?    protected HeaderFooterAdapter mAdapter;
    protected FooterProvider mFooterProvider;
    //    protected boolean isFirstAddFooter = true;
    private ArrayMap<String, String> mPostTypes = new ArrayMap<>();    // 璇锋眰绫诲瀷
    private int mState = STATE_NORMAL;
    // View
    private SwipeRefreshLayout mRefreshLayout;
    // 鐘舵€?    private boolean refreshEnable = true;           // 鏄惁鍏佽鍒锋柊
    private boolean loadMoreEnable = true;          // 鏄惁鍏佽鍔犺浇

    private Boolean isRequestShowed = true;
    public static int SCROLL_STATE_DOWN = 0;
    public static int SCROLL_STATE_UP = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refresh_recycler;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        // 閫傞厤鍣?        mAdapter = new HeaderFooterAdapter();
        mFooterProvider = new FooterProvider(getContext()) {
            @Override
            public void needLoadMore(RecyclerViewHolder holder) {
                loadMore();
            }
        };
        mFooterProvider.setFooterNormal();
        mAdapter.registerFooter(new Footer(), mFooterProvider);
        // refreshLayout
        mRefreshLayout = holder.get(R.id.refresh_layout);
        mRefreshLayout.setProgressViewOffset(false, -20, 80);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mRefreshLayout.setEnabled(true);
        // RecyclerView
        mRecyclerView = holder.get(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(getRecyclerViewLayoutManager());
        mRecyclerView.setOnScrollListener(mOnScrollListener);
        setAdapterRegister(getContext(), mRecyclerView, mAdapter);
        // 鐩戝惉 RefreshLayout 涓嬫媺鍒锋柊
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(POST_HEADER);
                refresh(POST_REFRESH);
                refresh(POST_MID);
            }
        });
        initData(mAdapter);
    }

    protected void refresh(String freshType) {
        if (!refreshEnable) return;
        if (freshType.equals(POST_REFRESH)) {
            pageIndex = 0;
            String uuid = request(pageIndex * pageCount, pageCount);
            mPostTypes.put(uuid, POST_REFRESH);
            pageIndex++;
            mState = STATE_REFRESH;
        } else if (freshType.equals(POST_HEADER)) {
            loadHeader();
        } else if (freshType.equals(POST_MID)) {
            loadMiddle();
        }
    }

    protected void loadMore() {
        if (!loadMoreEnable) return;
        if (mState == STATE_NO_MORE) return;
        String uuid = request(pageIndex * pageCount, pageCount);
        mPostTypes.put(uuid, POST_LOAD_MORE);
        pageIndex++;
        mState = STATE_LOADING;
        mFooterProvider.setFooterLoading();
    }

    protected void loadHeader() {
        String uuid = requestHeader();
        mPostTypes.put(uuid, POST_HEADER);
    }

    protected void loadMiddle() {
        String uuid = requestMiddle();
        mPostTypes.put(uuid, POST_MID);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultEvent(Event event) {
        String postType = mPostTypes.get(event.getUUID());
        if (event.isOk()) {
            if (postType.equals(POST_HEADER)) {
                onLoadHeader(event, mAdapter);
            } else if (postType.equals(POST_MID)) {
                onLoadMiddle(event, mAdapter);
            } else if (postType.equals(POST_LOAD_MORE)) {
                onLoadMore(event);
            } else if (postType.equals(POST_REFRESH)) {
                onRefresh(event);
            }
        } else {
            onError(event);
        }
        mPostTypes.remove(event.getUUID());
    }

    protected void onRefresh(Event event) {
        mState = STATE_NORMAL;
        mRefreshLayout.setRefreshing(false);
        onRefresh(event, mAdapter);
    }

    protected void onLoadMore(Event event) {
        if (event.getBean() instanceof NewestTopNode) {//xml瑙ｆ瀽
            if (((NewestTopNode) (event.getBean())).newest.item.size() < pageCount) {
                mState = STATE_NO_MORE;
                mFooterProvider.setFooterNormal();
            } else {
                mState = STATE_NORMAL;
                mFooterProvider.setFooterNormal();
            }

        } else if (event.getBean() instanceof HotGoodsTopNode) {
            if (((HotGoodsTopNode) event.getBean()).content.size() < pageCount) {
                mState = STATE_NO_MORE;
                mFooterProvider.setFooterNormal();
            } else {
                mState = STATE_NORMAL;
                mFooterProvider.setFooterNormal();
            }
        }
        onLoadMore(event, mAdapter);
    }

    protected void onError(Event event) {
        mState = STATE_NORMAL;  // 鐘舵€侀噸缃负姝ｅ父锛屼互渚垮彲浠ラ噸璇曪紝鍚﹀垯杩涘叆寮傚父鐘舵€佸悗鏃犳硶鍐嶅彉涓烘甯哥姸鎬?        String postType = mPostTypes.get(event.getUUID());
        if (postType.equals(POST_LOAD_MORE)) {
            mFooterProvider.setFooterError(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pageIndex--;
                    loadMore();
                }
            });
        } else if (postType.equals(POST_REFRESH)) {
            mRefreshLayout.setRefreshing(false);
            mFooterProvider.setFooterNormal();
        }
        onError(event, postType);
    }

    public void setRefreshEnable(boolean refreshEnable) {
        this.refreshEnable = refreshEnable;
        mRefreshLayout.setEnabled(refreshEnable);
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        this.loadMoreEnable = loadMoreEnable;
    }

    public void quickToTop() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 閫氱煡瀵艰埅鏍忔樉绀烘垨闅愯棌
     */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int mScrollState = recyclerView.getScrollState();
            if (mScrollState == RecyclerView.SCROLL_STATE_DRAGGING || mScrollState == RecyclerView.SCROLL_STATE_SETTLING) {
                if (dy > 50) {//up -> hide
                    if (isRequestShowed) {
                        EventBus.getDefault().post(new EventBusMsg(SCROLL_STATE_UP));
                        isRequestShowed = false;
                    }
                } else if (dy < -50) {//down -> show
                    if (!isRequestShowed) {
                        EventBus.getDefault().post(new EventBusMsg(SCROLL_STATE_DOWN));
                        isRequestShowed = true;
                    }
                }
            }
        }
    };

    //--- 闇€瑕佺户鎵跨被澶勭悊鐨勯儴鍒?----------------------------------------------------------------------

    /**
     * 鍔犺浇鏁板垵濮嬪寲鏁版嵁锛屽彲浠ヤ粠缂撳瓨鎴栬€呭叾浠栧湴鏂瑰姞杞斤紝
     * 濡傛灉娌℃湁鍒濆鏁版嵁锛屼竴鑸皟鐢?loadMore() 鍗冲彲銆?     *
     * @param adapter 閫傞厤鍣?     */
    public abstract void initData(HeaderFooterAdapter adapter);

    /**
     * 涓?RecyclerView 鐨?Adapter 娉ㄥ唽鏁版嵁绫诲瀷
     * 渚嬪锛?adapter.register(Bean.class, new BeanProvider(getContext()));
     *
     * @param context      涓婁笅鏂?     * @param recyclerView RecyclerView
     * @param adapter      Adapter
     */
    protected abstract void setAdapterRegister(Context context, androidx.recyclerview.widget.RecyclerView recyclerView,
                                               HeaderFooterAdapter adapter);

    /**
     * 鑾峰彇 RecyclerView 鐨?LayoutManager
     * 渚嬪锛?return new LinerLayoutManager(context);
     *
     * @return LayoutManager
     */
    @NonNull
    protected abstract RecyclerView.LayoutManager getRecyclerViewLayoutManager();

    /**
     * 璇锋眰鏁版嵁锛屽苟杩斿洖璇锋眰鐨?uuid
     * 渚嬪锛歳eturn mDiycode.getTopicsList(null, mNodeId, offset, limit);
     *
     * @param offset 鍋忕Щ閲?     * @param limit  璇锋眰鏁伴噺
     * @return uuid
     */
    @NonNull
    protected abstract String request(int offset, int limit);

    /**
     * 璇锋眰recyclerView鐨勫ご閮ㄦ暟鎹紝骞惰繑鍥炶姹傜殑 uuid
     * 渚嬪锛歳eturn mDiycode.getTopicsList(null, mNodeId, offset, limit);
     *
     * @return uuid
     */
    @NonNull
    protected abstract String requestHeader();

    /**
     * 璇锋眰recyclerView鐨勪腑闂撮儴鍒嗗唴瀹规暟鎹紝骞惰繑鍥炶姹傜殑 uuid
     * 渚嬪锛歳eturn mDiycode.getTopicsList(null, mNodeId, offset, limit);
     *
     * @return uuid
     */
    @NonNull
    protected abstract String requestMiddle();


    /**
     * 鏁版嵁鍒锋柊鎴愬姛鐨勫洖璋冿紝鐢变簬涓嶅悓椤甸潰鍙兘瑕佸鏁版嵁杩涜澶勭悊锛屼緥濡傞噸鏂版帓搴忥紝娓呯悊鎺変竴浜涙棤鏁堟暟鎹瓑锛屾墍浠ョ敱瀛愮被鑷繁瀹炵幇锛?     * 濡傛灉涓嶉渶瑕佺壒娈婂鐞嗭紝涓€鑸儚涓嬮潰杩欐牱鍐欏氨琛?
     * adapter.clearDatas();
     * adapter.addDatas(event.geiBean());
     *
     * @param event   Event
     * @param adapter Adapter
     */
    protected abstract void onRefresh(Event event, HeaderFooterAdapter adapter);

    /**
     * 鏁版嵁鍔犺浇鎴愬姛鏃惰皟鐢紝濡傛灉涓嶉渶瑕佸鏁版嵁杩涜鐗规畩澶勭悊锛岃繖鏍峰啓灏辫锛?     * adapter.addDatas(event.getBean());
     *
     * @param event   Event
     * @param adapter Adapter
     */
    protected abstract void onLoadMore(Event event, HeaderFooterAdapter adapter);

    /**
     * 缁欏垪琛ㄥ鍔犲ご閮紝濡傚够鐏墖绛?
     * adapter.addDatas(event.getBean());
     *
     * @param event   Event
     * @param adapter Adapter
     */
    protected abstract void onLoadHeader(Event event, HeaderFooterAdapter adapter);

    /**
     * 缁欏垪琛ㄥ鍔犱腑闂撮儴鍒?
     * adapter.addDatas(event.getBean());
     *
     * @param event   Event
     * @param adapter Adapter
     */
    protected abstract void onLoadMiddle(Event event, HeaderFooterAdapter adapter);

    /**
     * 鏁版嵁鍔犺浇閿欒鏃惰皟鐢紝浣犲彲浠ュ湪杩欓噷鑾峰彇閿欒绫诲瀷骞惰繘琛屽鐞嗭紝濡傛灉涓嶉渶瑕佺壒娈婂鐞嗭紝寮瑰嚭涓€涓?toast 鎻愰啋鐢ㄦ埛鍗冲彲銆?     * if (postType.equals(POST_LOAD_MORE)) {
     * toast("鍔犺浇鏇村澶辫触");
     * } else if (postType.equals(POST_REFRESH)) {
     * toast("鍒锋柊鏁版嵁澶辫触");
     * }
     *
     * @param event
     * @param postType
     */
    protected abstract void onError(Event event, String postType);


}
