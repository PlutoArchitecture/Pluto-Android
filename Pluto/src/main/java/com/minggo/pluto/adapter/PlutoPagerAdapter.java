package com.minggo.pluto.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.List;


/**
 * 首页的viewpager适配器
 * @author minggo
 * @time 2015-3-16上午11:12:12
 */
public class PlutoPagerAdapter extends FragmentStatePagerAdapter {

	private List<Fragment> fragmentList;
	private List<String> titleList;

	public PlutoPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
		super(fm);
		this.fragmentList = fragmentList;
		this.titleList = titleList;
	}

	/**
	 * 得到每个页面
	 */
	 @Override
	 public Fragment getItem(int arg0) {
		 return (fragmentList ==null || fragmentList.size() == 0) ? null: fragmentList.get(arg0);
	 }


	/**
	 * 页面的总个数
	 */
	@Override
	public int getCount() {
		return fragmentList == null ? 0 : fragmentList.size();
	}


	@Override
	public int getItemPosition(Object object) {
		//return PagerAdapter.POSITION_NONE;
		return PagerAdapter.POSITION_NONE;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
        //LogUtils.info("pager", "No." + position + "被删除了");
	}

	@Override
	public CharSequence getPageTitle(int position) {
		//得到对应position的Fragment的title
		return titleList.get(position);
	}

}
