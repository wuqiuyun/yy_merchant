package com.yiyue.store.module.im;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.YLApplication;
import com.yiyue.store.base.fragment.BaseMvpFragment;
import com.yiyue.store.databinding.FragmentImBinding;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.module.im.addfriend.AddFriendActivity;
import com.yiyue.store.module.im.conversation.ConversationFragment;
import com.yiyue.store.module.im.friends.FriendsFragment;
import com.yiyue.store.module.im.groups.GroupsFragment;
import com.yiyue.store.module.im.groups.creategroup.CreateGroupActivity;
import com.yiyue.store.util.Utils;
import com.yiyue.store.widget.popwindow.PopupUtil;
import com.yiyue.store.widget.popwindow.TriangleDrawable;
import com.yiyue.store.widget.popwindow.XGravity;
import com.yiyue.store.widget.popwindow.YGravity;
import com.yl.core.component.mvp.factory.CreatePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息
 * <p>
 * Created by zm on 2018/9/19.
 */
@CreatePresenter(IMPresenter.class)
public class IMFragment extends BaseMvpFragment<IMView, IMPresenter> implements IMView {
    private FragmentImBinding imBinding;

    private List<String> mTitles;
    private List<Fragment> mFragments;
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;
    private ConversationFragment conversationFrg;
    private PopupUtil mPopWindow;

    public static Fragment newInstance() {
        return new IMFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadcastReceiver();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_im;
    }

    @Override
    protected void initView() {
        imBinding = (FragmentImBinding) viewDataBinding;

        mTitles = new ArrayList<>();
        mTitles.add(getString(R.string.oicq_msg));
        mTitles.add(getString(R.string.oicq_friend));
        mTitles.add(getString(R.string.oicq_group));

        mFragments = new ArrayList<>();
        mFragments.add(conversationFrg = ConversationFragment.getInstance());
        mFragments.add(FriendsFragment.getInstance());
        mFragments.add(GroupsFragment.getInstance());

        imBinding.vpContent.setOffscreenPageLimit(3);
        initPop();
        initEvent();
    }

    private void initEvent() {
        imBinding.vpContent.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles.get(position);
            }
        });

        imBinding.tlTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        imBinding.tlTab.setupWithViewPager(imBinding.vpContent);

        imBinding.titleView.setRightImgClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击加号 选择加好友或者加群
                showPop(imBinding.titleView.getRightIcon());
            }
        });
    }

    private void initPop() {
        mPopWindow = PopupUtil.create()
                .setContext(getActivity())
                .setContentView(R.layout.popwin_im_layout)
                .setAnimationStyle(R.style.AnimImPopwindow)
                .setOnViewListener(new PopupUtil.OnViewListener() {
                    @Override
                    public void initViews(View view, PopupUtil basePopup) {
                        View arrowView = view.findViewById(R.id.v_arrow);
                        arrowView.setBackground(new TriangleDrawable(TriangleDrawable.TOP, Color.WHITE));
                        view.findViewById(R.id.add_friend).setOnClickListener(v -> {
                            startActivity(new Intent(getActivity(), AddFriendActivity.class));
                            mPopWindow.dismiss();
                        });

                        view.findViewById(R.id.add_group).setOnClickListener(v -> {
                            startActivity(new Intent(getActivity(), CreateGroupActivity.class));
                            mPopWindow.dismiss();
                        });
                    }
                })
                .setFocusAndOutsideEnable(true)
                .setBackgroundDimEnable(true)
                .setDimValue(0.4f)
                .apply();
    }

    private void showPop(View view) {
        int offsetX = Utils.dp2px(YLApplication.getContext(), 20) - view.getWidth() / 2;
        int offsetY = (imBinding.titleView.getHeight() - view.getHeight()) / 2;
        mPopWindow.showAtAnchorView(view, YGravity.BELOW, XGravity.ALIGN_RIGHT, offsetX, offsetY - 10);
    }


    @Override
    protected void loadData() {
        // 判断环信sdk是否登录成功过，并没有退出和被踢，否则跳转到登陆界面
//        if (!EasyUtil.getEmManager().isLoggedInBefore()) {
//            startActivity(new Intent(getContext(), ImLoginActivity.class));
//        }
    }

    @Override
    public void showToast(String message) {

    }

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constants.ACTION_GROUP_CHANAGED);
        intentFilter.addAction(Constants.ACTION_CONVERSION_COMING);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(Constants.ACTION_GROUP_CHANAGED)) {
//                   刷新群组fragment数据监听
                }

                conversationFrg.refresh();//会话fragment刷新数据
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBroadcastReceiver();
    }
}
