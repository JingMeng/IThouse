package com.danmo.ithouse.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.danmo.commonutil.leak.IMMLeaks;
import com.danmo.ithouse.R;
import com.danmo.ithouse.activity.MainActivity;
import com.danmo.ithouse.util.Config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public abstract class BaseActivity extends AppCompatActivity {

    protected DrawerLayout mRootView;
    protected FrameLayout flActivityContainer;
    private Toast mToast;
    private Fragment mFragment;
    private List<TurnBackListener> mTurnBackListeners = new ArrayList<>();
    private NavigationView.OnNavigationItemSelectedListener mListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_git_hub:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.GITHUB)));
                    break;
                case R.id.action_blog:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.BLOG)));
                    break;
                case R.id.action_wp:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.WORDPRESS)));
            }
            return false;
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        initDatas();
        initViews();
        IMMLeaks.fixFocusedViewLeak(this.getApplication()); // 淇 InputMethodManager 寮曞彂鐨勫唴瀛樻硠婕?    }

    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 鍒濆鍖栨暟鎹紝璋冪敤浣嶇疆鍦?initViews 涔嬪墠
     */
    protected void initDatas() {
    }

    /**
     * 鍒濆鍖?View锛?璋冪敤浣嶇疆鍦?initDatas 涔嬪悗
     */
    protected void initViews() {
        flActivityContainer = findViewById(R.id.activity_container);
        flActivityContainer.addView(LayoutInflater.from(this).inflate(getLayoutId(), flActivityContainer, false));
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(mListener);
        mRootView = findViewById(R.id.root_layout);
    }

    // 榛樿鐐瑰嚮宸︿笂瑙掓槸缁撴潫褰撳墠 Activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 鍙戝嚭涓€涓煭Toast
     *
     * @param text 鍐呭
     */
    public void toastShort(String text) {
        toast(text, Toast.LENGTH_SHORT);
    }

    /**
     * 鍙戝嚭涓€涓暱toast鎻愰啋
     *
     * @param text 鍐呭
     */
    public void toastLong(String text) {
        toast(text, Toast.LENGTH_LONG);
    }

    private void toast(final String text, final int duration) {
        if (!TextUtils.isEmpty(text)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mToast == null) {
                        mToast = Toast.makeText(getApplicationContext(), text, duration);
                    } else {
                        mToast.setText(text);
                        mToast.setDuration(duration);
                    }
                    mToast.show();
                }
            });
        }
    }

    /**
     * 鎵撳紑 Activity 鐨勫悓鏃朵紶閫掍竴涓暟鎹?     */
    protected <V extends Serializable> void openActivity(Class<?> cls, String key, V value) {
        openActivity(this, cls, key, value);
    }

    /**
     * 鎵撳紑 Activity 鐨勫悓鏃朵紶閫掍竴涓暟鎹?     */
    public <V extends Serializable> void openActivity(Context context, Class<?> cls, String key, V value) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(key, value);
        context.startActivity(intent);
    }

    @SuppressWarnings("RestrictedApi")
    protected void clearOldFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (transaction == null || fragments == null || fragments.size() == 0)
            return;
        boolean doCommit = false;
        for (Fragment fragmentOld : fragments) {
            if (fragmentOld != fragment && fragmentOld != null) {
                transaction.remove(fragmentOld);
                doCommit = true;
            }
        }
        if (doCommit)
            transaction.commitNow();
    }

    protected void addFragment(int frameLayoutId, Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (fragment.isAdded()) {
                if (mFragment != null) {
                    transaction.hide(mFragment).show(fragment);
                } else {
                    transaction.show(fragment);
                }
            } else {
                if (mFragment != null) {
                    transaction.hide(mFragment).add(frameLayoutId, fragment);
                } else {
                    transaction.add(frameLayoutId, fragment);
                }
            }
            mFragment = fragment;
            transaction.commit();
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    public interface TurnBackListener {
        boolean onTurnBack();
    }

    public void addOnTurnBackListener(TurnBackListener l) {
        this.mTurnBackListeners.add(l);
    }

    private long mBackPressedTime;

    @Override
    public void onBackPressed() {
        for (TurnBackListener l : mTurnBackListeners) {
            if (l.onTurnBack()) return;
        }

        if (this instanceof MainActivity) {
            long curTime = SystemClock.uptimeMillis();
            if ((curTime - mBackPressedTime) < (3 * 1000)) {
                finish();
            } else {
                mBackPressedTime = curTime;
                toastLong(this.getString(R.string.tip_double_click_exit));
            }
        } else {
            super.onBackPressed();
        }
    }
}

