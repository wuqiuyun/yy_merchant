package com.yiyue.store.module.im.imsearch;

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
import com.yiyue.store.model.vo.bean.daobean.GroupBean;
import com.yiyue.store.module.im.chat.ChatActivity;
import com.yiyue.store.module.im.daoutil.GroupDaoUtils;
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
public class ImSearchGroupActivity extends BaseMvpAppCompatActivity<ImSearchView, ImSearchPresenter> implements ImSearchView {
    private BaseQuickAdapter groupAdapter;
    private List<GroupBean> groupLists = new ArrayList<>();

    private GroupDaoUtils groupDaoUtils;
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
        mBinding.imsEdit.setHint("请输入群ID或者群名称");
        groupAdapter = new SearchLocalGroupAdapter(R.layout.adapter_addfriend);
        groupAdapter.openLoadAnimation();
        mBinding.rvFriendList.setAdapter(groupAdapter);

        groupDaoUtils = new GroupDaoUtils(this);

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

        groupAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (groupLists != null && groupLists.size() > 0 && groupLists.get(position) != null) {
                    ChatActivity.startFromGroupActivity(ImSearchGroupActivity.this, groupLists.get(position));
                }
            }
        });
    }

    private void loadLocalData() {
        Task.callInBackground(new Callable<List<GroupBean>>() {
            @Override
            public List<GroupBean> call() throws Exception {
                groupLists = groupDaoUtils.query(mBinding.imsEdit.getText().toString());
                return groupLists;
            }
        }).continueWith(new Continuation<List<GroupBean>, Object>() {
            @Override
            public Object then(Task<List<GroupBean>> task) throws Exception {
                if (groupLists != null && groupLists.size() > 0) {
                    groupAdapter.setNewData(groupLists);
                } else {
                    showToast("换个条件搜索试试");
                }
                return groupLists;
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
        if (groupDaoUtils != null) {
            groupDaoUtils.closeConnection();
        }
    }
}
