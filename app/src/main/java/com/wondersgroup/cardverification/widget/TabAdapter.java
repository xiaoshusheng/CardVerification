package com.wondersgroup.cardverification.widget;


import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * 作者：create by YangZ on 2017/10/26 15:38
 * 邮箱：YangZL8023@163.com
 */

public class TabAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    private List<String> titles;

    public TabAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public TabAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    /**
     * 指定Position所对应的页面的Fragment内容
     */
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    /**
     * 决定ViewPager页数的总和
     */
    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if(this.titles != null || this.titles.size()> 0){

            return this.titles.get(position);
        }else {

            return super.getPageTitle(position);
        }
    }
}
