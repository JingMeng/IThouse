package com.danmo.ithouse.activity;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.danmo.ithouse.R;
import com.danmo.ithouse.base.BaseActivity;

/**
 * 鏅€氳鎯呴〉锛寃ebview绫诲瀷椤甸潰
 */
public class WebViewActivity extends BaseActivity {
    private WebView webView;
    private Toolbar toolbar;
    public static final String INTENT_DETAIL_CONTENT_LINK = "link";
    public static final String PAGE_TYPE = "page_type";
    public static int PAGE_TYPE_HOTGOODS = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        toolbar = findViewById(R.id.flexible_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.this.onBackPressed();
            }
        });

        webView = findViewById(R.id.wv_detail_content);
        WebSettings webSettings = webView.getSettings();//鑾峰彇webview璁剧疆灞炴€?        webSettings.setJavaScriptEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //璁剧疆鑷€傚簲灞忓箷锛屼袱鑰呭悎鐢?        webSettings.setUseWideViewPort(true); //灏嗗浘鐗囪皟鏁村埌閫傚悎webview鐨勫ぇ灏?        webSettings.setLoadWithOverviewMode(true); // 缂╂斁鑷冲睆骞曠殑澶у皬

        //缂╂斁鎿嶄綔
        webSettings.setSupportZoom(true); //鏀寔缂╂斁锛岄粯璁や负true銆傛槸涓嬮潰閭ｄ釜鐨勫墠鎻愩€?        webSettings.setBuiltInZoomControls(true); //璁剧疆鍐呯疆鐨勭缉鏀炬帶浠躲€傝嫢涓篺alse锛屽垯璇ebView涓嶅彲缂╂斁
        webSettings.setDisplayZoomControls(false); //闅愯棌鍘熺敓鐨勭缉鏀炬帶浠?
        //鍏朵粬缁嗚妭鎿嶄綔
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //鍏抽棴webview涓紦瀛?        webSettings.setAllowFileAccess(true); //璁剧疆鍙互璁块棶鏂囦欢
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //鏀寔閫氳繃JS鎵撳紑鏂扮獥鍙?        webSettings.setLoadsImagesAutomatically(true); //鏀寔鑷姩鍔犺浇鍥剧墖
        webSettings.setDefaultTextEncodingName("utf-8");//璁剧疆缂栫爜鏍煎紡

        webView.setBackgroundColor(0x00000000); // 璁剧疆鑳屾櫙鑹?        //璁剧疆Web瑙嗗浘
        webView.setWebViewClient(new webViewClient());
    }

    //璁剧疆涓嶇敤绯荤粺娴忚鍣ㄦ墦寮€,鐩存帴鏄剧ず鍦ㄥ綋鍓峎ebview
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("http:") || url.startsWith("https:")) {
                view.loadUrl(url);
                return false;
            }
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack(); //goBack()琛ㄧず杩斿洖WebView鐨勪笂涓€椤甸潰
//            return true;
        }
        finish();//缁撴潫閫€鍑虹▼搴?        return false;
    }

    public static void start(Context context, String link) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_DETAIL_CONTENT_LINK, link);
        context.startActivity(intent);
    }

    public static void start(Context context, String link, int type) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_DETAIL_CONTENT_LINK, link);
        intent.putExtra(PAGE_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String link = getIntent().getStringExtra(INTENT_DETAIL_CONTENT_LINK);
        if (!TextUtils.isEmpty(link)) {
            webView.loadUrl(link);
        }
        int type = getIntent().getIntExtra(PAGE_TYPE, 0);
        if (type == PAGE_TYPE_HOTGOODS) {
            toolbar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}

