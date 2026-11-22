package com.danmo.ithouse.fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.danmo.ithouse.R;
import com.danmo.ithouse.base.BaseFragment;
import com.danmo.ithouse.base.ViewHolder;
import com.danmo.ithouse.fragment.sub.CommunityContentFragment;

/**
 * 鍦堝瓙
 */

public class CommunityFragment extends BaseFragment {
    private FragmentManager fm;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_community;
    }

    @Override
    protected void initViews(ViewHolder holder, View root) {
        Toolbar toolbar = holder.get(R.id.toolbar);
        toolbar.setTitle("鍦堝瓙");
        setHasOptionsMenu(true);
        ((AppCompatActivity) mContext).setSupportActionBar(toolbar);

        CommunityContentFragment fragment = new CommunityContentFragment();
        fm = this.getChildFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.quan_content, fragment);
        ft.show(fragment);
        ft.commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.quan_toolbar_menu, menu);
    }


}

