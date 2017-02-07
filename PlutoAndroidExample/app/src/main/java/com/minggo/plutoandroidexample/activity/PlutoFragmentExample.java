package com.minggo.plutoandroidexample.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mobstat.StatService;
import com.minggo.pluto.activity.PlutoActivity;
import com.minggo.pluto.adapter.PlutoPagerAdapter;
import com.minggo.plutoandroidexample.R;
import com.minggo.plutoandroidexample.fragment.BlogFragment;
import com.minggo.plutoandroidexample.fragment.ExampleFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by minggo on 2017/2/4.
 */
public class PlutoFragmentExample extends PlutoActivity {

    @BindView(R.id.viewpager)
    public ViewPager mViewPager;
    @BindView(R.id.tl_tab)
    public TabLayout mTabLayout;

    private PlutoPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pluto_fragment_example);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI(){
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        List<String> titleList = new ArrayList<String>();

        BlogFragment blogFragment1 = new BlogFragment();
        blogFragment1.type = 0;
        BlogFragment blogFragment2 = new BlogFragment();
        blogFragment2.type = 1;
        fragmentList.add(new ExampleFragment());
        fragmentList.add(blogFragment1);
        fragmentList.add(blogFragment2);

        titleList.add("Fragment 1");
        titleList.add("Fragment 2");
        titleList.add("Fragment 3");

        pagerAdapter = new PlutoPagerAdapter(getSupportFragmentManager(),fragmentList,titleList);
        mViewPager.setAdapter(pagerAdapter);//设置适配器
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(0);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.alpha_sllow_in, R.anim.push_right_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
