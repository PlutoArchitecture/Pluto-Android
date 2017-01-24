package com.minggo.pluto.fragment;

import android.support.v4.app.Fragment;


import com.minggo.pluto.util.LogUtils;

import java.util.List;

/**
 *  * Fragment的mUserVisibleHint属性控制器，用于准确的监听Fragment是否对用户可见
 * <br>
 * <br>mUserVisibleHint属性有什么用？
 * <br>* 使用ViewPager时我们可以通过Fragment的getUserVisibleHint()&&isResume()方法来判断用户是否能够看见某个Fragment
 * <br>* 利用这个特性我们可以更精确的统计页面的显示事件和标准化页面初始化流程（真正对用户可见的时候才去请求数据）
 * <br>
 * <br>解决BUG
 * <br>* FragmentUserVisibleController还专门解决了在Fragment或ViewPager嵌套ViewPager时子Fragment的mUserVisibleHint属性与父Fragment的mUserVisibleHint属性不同步的问题
 * <br>* 例如外面的Fragment的mUserVisibleHint属性变化时，其包含的ViewPager中的Fragment的mUserVisibleHint属性并不会随着改变，这是ViewPager的BUG
 * <br>
 * <br>使用方式（假设你的基类Fragment是MyFragment）：
 * <br>1. 在你的MyFragment的构造函数中New一个FragmentUserVisibleController（一定要在构造函数中new）
 * <br>2. 重写Fragment的onActivityCreated()、onResume()、onPause()、setUserVisibleHint(boolean)方法，分别调用FragmentUserVisibleController的activityCreated()、resume()、pause()、setUserVisibleHint(boolean)方法
 * <br>3. 实现FragmentUserVisibleController.UserVisibleCallback接口并实现以下方法
 * <br>&nbsp&nbsp&nbsp&nbsp* void setWaitingShowToUser(boolean)：直接调用FragmentUserVisibleController的setWaitingShowToUser(boolean)即可
 * <br>&nbsp&nbsp&nbsp&nbsp* void isWaitingShowToUser()：直接调用FragmentUserVisibleController的isWaitingShowToUser()即可
 * <br>&nbsp&nbsp&nbsp&nbsp* void callSuperSetUserVisibleHint(boolean)：调用父Fragment的setUserVisibleHint(boolean)方法即可
 * <br>&nbsp&nbsp&nbsp&nbsp* void onVisibleToUserChanged(boolean, boolean)：当Fragment对用户可见或不可见的就会回调此方法，你可以在这个方法里记录页面显示日志或初始化页面
 * <br>&nbsp&nbsp&nbsp&nbsp* boolean isVisibleToUser()：判断当前Fragment是否对用户可见，直接调用FragmentUserVisibleController的isVisibleToUser()即可
 *文／浮游大虾（简书作者）
 *原文链接：http://www.jianshu.com/p/e7449278e33d
 *著作权归作者所有，转载请联系作者获得授权，并标注“简书作者”。
 *
 * 解决Fragment嵌套Fragment  setUserVisibleHint调用异常
 * @author ouweiming
 * @time 2016-04-29 22:35
 */
public class FragmentUserVisibleController {
    private static final boolean DEBUG = true;
    private static final String TAG = "FragmentUserVisibleController";

    @SuppressWarnings("FieldCanBeLocal")
    private String fragmentName;
    private boolean waitingShowToUser;
    private Fragment fragment;
    private UserVisibleCallback userVisibleCallback;

    public FragmentUserVisibleController(Fragment fragment, UserVisibleCallback userVisibleCallback) {
        this.fragment = fragment;
        this.userVisibleCallback = userVisibleCallback;
        //noinspection ConstantConditions
        this.fragmentName = DEBUG ? fragment.getClass().getSimpleName() : null;
    }

    public void activityCreated(){
        // 如果自己是显示状态，但父Fragment却是隐藏状态，就把自己也改为隐藏状态，并且设置一个等待显示的标记
        if(fragment.getUserVisibleHint()){
            Fragment parentFragment = fragment.getParentFragment();
            if(parentFragment != null && !parentFragment.getUserVisibleHint()){
                if(DEBUG){
                    LogUtils.info(TAG, fragmentName + ": activityCreated, parent " + parentFragment.getClass().getSimpleName() + "is hidden, therefore hidden self");
                }
                userVisibleCallback.setWaitingShowToUser(true);
                userVisibleCallback.callSuperSetUserVisibleHint(false);
            }
        }
    }

    public void resume(){
        if (fragment.getUserVisibleHint()) {
            userVisibleCallback.onVisibleToUserChanged(true, true);
        }
    }

    public void pause(){
        if (fragment.getUserVisibleHint()) {
            userVisibleCallback.onVisibleToUserChanged(false, true);
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser){
        if (fragment.isResumed()) {
            userVisibleCallback.onVisibleToUserChanged(isVisibleToUser, false);
        }
        if(fragment.getActivity() != null) {
            List<Fragment> childFragmentList = fragment.getChildFragmentManager().getFragments();
            if (isVisibleToUser) {
                // 将所有正等待显示的子Fragment设置为显示状态，并取消等待显示标记
                if (childFragmentList != null && childFragmentList.size() > 0) {
                    for (Fragment childFragment : childFragmentList) {
                        if (childFragment instanceof UserVisibleCallback) {
                            UserVisibleCallback userVisibleCallback = (UserVisibleCallback) childFragment;
                            if (userVisibleCallback.isWaitingShowToUser()) {
                                if (DEBUG) {
                                    LogUtils.info(TAG, fragmentName + ": setUserVisibleHint, show child " + childFragment.getClass().getSimpleName());
                                }
                                userVisibleCallback.setWaitingShowToUser(false);
                                childFragment.setUserVisibleHint(true);
                            }
                        }
                    }
                }
            } else {
                // 将所有正在显示的子Fragment设置为隐藏状态，并设置一个等待显示标记
                if (childFragmentList != null && childFragmentList.size() > 0) {
                    for (Fragment childFragment : childFragmentList) {
                        if (childFragment instanceof UserVisibleCallback) {
                            UserVisibleCallback userVisibleCallback = (UserVisibleCallback) childFragment;
                            if (childFragment.getUserVisibleHint()) {
                                if (DEBUG) {
                                    LogUtils.info(TAG, fragmentName + ": setUserVisibleHint, hidden child " + childFragment.getClass().getSimpleName());
                                }
                                userVisibleCallback.setWaitingShowToUser(true);
                                childFragment.setUserVisibleHint(false);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 当前Fragment是否对用户可见
     */
    @SuppressWarnings("unused")
    public boolean isVisibleToUser(){
        return fragment.isResumed() && fragment.getUserVisibleHint();
    }

    public boolean isWaitingShowToUser() {
        return waitingShowToUser;
    }

    public void setWaitingShowToUser(boolean waitingShowToUser) {
        this.waitingShowToUser = waitingShowToUser;
    }

    public interface UserVisibleCallback {
        void setWaitingShowToUser(boolean waitingShowToUser);
        boolean isWaitingShowToUser();
        boolean isVisibleToUser();
        void callSuperSetUserVisibleHint(boolean isVisibleToUser);
        void onVisibleToUserChanged(boolean isVisibleToUser, boolean invokeInResumeOrPause);
    }
}