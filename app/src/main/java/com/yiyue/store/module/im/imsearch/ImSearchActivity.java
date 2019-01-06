package com.yiyue.store.module.im.imsearch;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityImSearchBinding;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;
import com.yiyue.store.module.im.daoutil.UserFriendDaoUtils;
import com.yiyue.store.module.im.friendinfo.FriendInfoActivity;
import com.yl.core.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by zhangzz on 2018/9/27
 * 搜索本地好友页面
 */
public class ImSearchActivity extends BaseMvpAppCompatActivity<ImSearchView, ImSearchPresenter> implements ImSearchView {
    private BaseQuickAdapter addFriendAdapter;
    private List<UserFriendsBean> frindLists = new ArrayList<>();

    private UserFriendDaoUtils userFriendDaoUtils;
    private ActivityImSearchBinding mBinding;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_im_search;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityImSearchBinding) viewDataBinding;

        mBinding.rvFriendList.setHasFixedSize(true);
        mBinding.rvFriendList.setNestedScrollingEnabled(false);
        mBinding.rvFriendList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.imsEdit.setHint("请输入昵称或备注名");
        addFriendAdapter = new SearchLocalFriendAdapter(R.layout.adapter_addfriend);
        addFriendAdapter.openLoadAnimation();
        mBinding.rvFriendList.setAdapter(addFriendAdapter);

        userFriendDaoUtils = new UserFriendDaoUtils(this);

        mBinding.titleView.setLeftClickListener(v -> {
            finish();
        });

        mBinding.imsEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    loadLocalData();
                    return true;
                }
                return false;
            }
        });

        mBinding.tvSearch.setOnClickListener(v->{
            loadLocalData();
        });

        addFriendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (frindLists.get(position) != null) {
                    Intent intent = new Intent(ImSearchActivity.this, FriendInfoActivity.class);
                    intent.putExtra(Constants.EXTRA_CHAT_TYPE, Constants.CHATTYPE_SINGLE);
                    intent.putExtra(Constants.EXTRA_USER_ID, frindLists.get(position).getImusername());
                    intent.putExtra(Constants.EXTRA_FRIEND_ID, frindLists.get(position).getFriendId() + "");//朋友的用户id
                    startActivity(intent);
                }
            }
        });
    }

    private void loadLocalData() {
        Task.callInBackground(new Callable<List<UserFriendsBean>>() {
            @Override
            public List<UserFriendsBean> call() throws Exception {
                frindLists = userFriendDaoUtils.query(mBinding.imsEdit.getText().toString());
                return frindLists;
            }
        }).continueWith(new Continuation<List<UserFriendsBean>, Object>() {
            @Override
            public Object then(Task<List<UserFriendsBean>> task) throws Exception {
                if (frindLists!=null && frindLists.size()>0) {
                    addFriendAdapter.setNewData(frindLists);
                } else {
                    showToast("换个条件搜索试试");
                }
                return frindLists;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userFriendDaoUtils != null) {
            userFriendDaoUtils.closeConnection();
        }
    }
}
