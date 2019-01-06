package com.yiyue.store.module.mine.works;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.base.fragment.BaseMvpFragment;
import com.yiyue.store.component.recycleview.GridSpacingItemDecoration;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentWorksBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.OpusBean;
import com.yiyue.store.module.mine.works.details.WorksDetailsActivity;
import com.yiyue.store.util.RefreshLayoutUtil;
import com.yl.core.component.mvp.factory.CreatePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作品
 * <p>
 * Created by zm on 2018/10/10.
 */
@CreatePresenter(WorksPresenter.class)
public class WorksFragment extends BaseMvpFragment<IWorksView, WorksPresenter> implements IWorksView, OnLoadMoreListener, OnRefreshListener {

    FragmentWorksBinding binding;
    private WorksAdapter adapter;

    private SmartRefreshLayout refreshLayout;

    private String mUserId;

    private int page = 1;//页数
    private int size = 10;//每页数量

    private int fromActivity;//从哪个页面来的

    public static Fragment newInstance(int from) {
        WorksFragment worksFragment = new WorksFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("from", from);
        worksFragment.setArguments(bundle);
        return worksFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_works;
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        fromActivity = bundle.getInt("from", 0);
        binding = (FragmentWorksBinding) viewDataBinding;
        // init recycleview
        binding.recycleView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.recycleView.addItemDecoration(new GridSpacingItemDecoration(2, 30, false));

        refreshLayout = binding.refreshLayout;
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

        adapter = new WorksAdapter(getContext());
        adapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                OpusBean opus = adapter.getDatas().get(position);
                
                int type = 1;//todo 这个type是判断是查看还是转发
                getMvpPresenter().opusCount(opus.getOpusId(),type);
                Intent intent = new Intent();
                Bundle opusBundle = new Bundle();
                opusBundle.putString(Constants.OPUS_ID, opus.getOpusId()+"");
                bundle.putString(Constants.HEAD_PORTRAIT, opus.getStylistHeadImg());
                bundle.putString(Constants.NICK_NAME, opus.getStylistNickname());
                intent.putExtras(opusBundle);
                intent.setClass(getContext(),WorksDetailsActivity.class);
                startActivityForResult(intent,Constants.RESULT_REFRESH_CODE);
            }
        });
        binding.recycleView.setAdapter(adapter);
    }


    private void getOpusList(int page, int size, String mUserId) {
        switch (fromActivity) {
            case Constants.ACTIVITY_COLLECT:
                getMvpPresenter().getOpusCollection(page, size, mUserId);
                break;
            case Constants.ACTIVITY_FOOTPRINT:
                getMvpPresenter().getOpusFoot(page, size, mUserId);
                break;
            case Constants.ACTIVITY_MANY_WORKS:
                break;
            case 0:
                showToast("来源页获取错误");
                break;
        }
    }

    @Override
    protected void loadData() {
        mUserId = AccountManager.getInstance().getUserId();
        getOpusList(page, size, mUserId);
    }


    @Override
    public void getOpusListSuccess(List<OpusBean> list) {
        RefreshLayoutUtil.finishRefreshLayout(refreshLayout);
        ArrayList<OpusBean> newData = (ArrayList<OpusBean>) list;

        if (page == 1) {
            adapter.setDatas(newData, true);
        } else {
            adapter.addDatas(newData, true);
        }

        if (list.size() < size) {// 加载的数据不够页面数量 则认为没有下一页
            refreshLayout.setNoMoreData(true);
        } else {
            refreshLayout.setNoMoreData(false);
        }
    }

    @Override
    public void getOpusListFail() {
        RefreshLayoutUtil.finishRefreshLayout(refreshLayout);
        refreshLayout.setNoMoreData(true);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        page++;
        getOpusList(page, size, mUserId);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        page = 1;
        getOpusList(page, size, mUserId);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== Constants.RESULT_REFRESH_CODE&&resultCode==Constants.RESULT_REFRESH_CODE){
            refreshLayout.autoRefresh();
        }
    }
}
