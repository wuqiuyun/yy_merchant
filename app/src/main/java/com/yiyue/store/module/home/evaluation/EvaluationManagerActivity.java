package com.yiyue.store.module.home.evaluation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityEvaluationManagerBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.StoreCommentListBean;
import com.yiyue.store.model.vo.bean.StoreManageScopeBean;
import com.yiyue.store.model.vo.bean.StylistEvaluationBean;
import com.yiyue.store.util.RefreshLayoutUtil;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.KeyboardUtil;
import com.yl.core.util.StatusBarUtil;

import java.util.ArrayList;

import static com.yiyue.store.YLApplication.getContext;

/**
 * 评价管理
 * <p>
 * Created by lvlong on 2018/10/11.
 */
@CreatePresenter(EvaluationManagerPresenter.class)
public class EvaluationManagerActivity extends BaseMvpAppCompatActivity<EvaluationManagerView, EvaluationManagerPresenter>
        implements EvaluationManagerView, ClickHandler, OnLoadMoreListener, OnRefreshListener {

    private SmartRefreshLayout refreshLayout;
    ActivityEvaluationManagerBinding mBinding;
    private EvaluationManagerAdapter adapter;
    private ArrayList<StoreCommentListBean> storeCommentListBeanList = new ArrayList<>();

    protected int pageIndx = 1; //第几页
    protected int pageSize = 10; // 每页数量
    private String stylistId;
    private String storeId;

    private int type ; //1&2查看门店评论,3查看美发师评论

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_evaluation_manager;
    }

    @Override
    protected void init() {

        mBinding = (ActivityEvaluationManagerBinding) viewDataBinding;
        mBinding.setClick(this);

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            type = bundle.getInt(Constants.EVALUATION_TYPE);

            if (type==1){
                storeId = AccountManager.getInstance().getStoreId();
            }else if (type==2){
                storeId = bundle.getString(Constants.STORE_ID);
            }else if (type == 3){
                stylistId = bundle.getString(Constants.STYLIST_ID);
            }
        } else {
            finish();
        }

        initView();
        loadData();
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding.materialRatingBar.setEnabled(false);
        mBinding.titleView.setLeftClickListener(view -> finish());

        //设置适配器
        RecyclerView recyclerView = mBinding.recycleView;
        adapter = new EvaluationManagerAdapter(this, type);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if (type != 3) {
            adapter.setOnSendViewClick((mssage, position) -> {
                if (adapter.getDatas().size()>0){
                    getMvpPresenter().replyStoreComment(mssage, String.valueOf(adapter.getDatas().get(position).getId()),this);
                    KeyboardUtil.closeSoftKeyboard(EvaluationManagerActivity.this);
                }
            });
        }

        initRefreshLoadLayout();

    }

    private void loadData() {
        refreshLayout.autoRefresh();
    }

    private void initDate() {

        if (type == 3){
            getMvpPresenter().getEvaluate(stylistId,this);
            getMvpPresenter().getStylistCommentList(stylistId, pageIndx, pageSize,this);
        }else {
            getMvpPresenter().getStoreScore(storeId,this);//获取评分
            getMvpPresenter().getStoreCommentList(storeId,pageIndx, pageSize,this);
        }

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    protected void initRefreshLoadLayout() {
        refreshLayout = mBinding.refreshLayout;
        if (refreshLayout != null) {
            refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
            refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
            refreshLayout.setOnLoadMoreListener(this);
            refreshLayout.setOnRefreshListener(this);
        }
    }

    @Override
    public void onLoadMore(RefreshLayout mRefreshLayout) {
        pageIndx++;
        if (type != 3) {

            getMvpPresenter().getStoreCommentList(storeId,pageIndx, pageSize,this);
        } else {
            getMvpPresenter().getStylistCommentList(stylistId, pageIndx, pageSize,this);
        }

    }

    @Override
    public void onRefresh(RefreshLayout mRefreshLayout) {
        pageIndx = 1;
        if (type == 3){
            getMvpPresenter().getEvaluate(stylistId,this);
            getMvpPresenter().getStylistCommentList(stylistId, pageIndx, pageSize,this);
        }else {
            getMvpPresenter().getStoreScore(storeId,this);//获取评分
            getMvpPresenter().getStoreCommentList(storeId,pageIndx, pageSize,this);
        }
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void getStoreScoreSucceed(StoreManageScopeBean storeManageScopeBean) {
        if (storeManageScopeBean != null) {
            mBinding.titleView.setSubTitleText("(" + storeManageScopeBean.getScoretimes() +")");
            mBinding.materialRatingBar.setRating((float) storeManageScopeBean.getScore());
            mBinding.tvProfessionSkill.setText(String.format(getString(R.string.environment_grade), storeManageScopeBean.getEnvironmentScore()));
            mBinding.tvServiceAttitude.setText(String.format(getString(R.string.service_attitude), storeManageScopeBean.getServerScore()));

            int pareEnvirtAvg = storeManageScopeBean.getPareEnvirtAvg();    //环境平均分比较
            if (pareEnvirtAvg == 1){
                mBinding.tvRelativeRatio1.setText("高于平均水平");
            }else if (pareEnvirtAvg == 0){
                mBinding.tvRelativeRatio1.setText("等于平均水平");
            }else if(pareEnvirtAvg == -1){
                mBinding.tvRelativeRatio1.setText("低于平均水平");
            }else if (pareEnvirtAvg == 10){
                mBinding.tvRelativeRatio1.setText("等于平均水平");
            }

            int pareServerAvg = storeManageScopeBean.getPareServerAvg();    //服务平均分比较
            if (pareServerAvg == 1){
                mBinding.tvRelativeRatio2.setText("高于平均水平");
            }else if (pareServerAvg == 0){
                mBinding.tvRelativeRatio2.setText("等于平均水平");
            }else if(pareServerAvg == -1){
                mBinding.tvRelativeRatio2.setText("低于平均水平");
            }else if (pareServerAvg == 10){
                mBinding.tvRelativeRatio2.setText("等于平均水平");
            }
        }
    }

    @Override
    public void getStoreCommentListSucceed(ArrayList<StoreCommentListBean> storeCommentListBean) {
        RefreshLayoutUtil.finishRefreshLayout(refreshLayout);
//        if (refreshLayout == null || refreshLayout.getState() == RefreshState.Refreshing) {//刷新
//            adapter.setDatas(storeCommentListBean, true);
//            adapter.notifyDataSetChanged();
//        } else if (refreshLayout.getState() == RefreshState.Loading) {//加载
//            adapter.addDatas(storeCommentListBean, true);
//            adapter.notifyDataSetChanged();
//        }

        if(pageIndx==1){//刷新
            adapter.setDatas(storeCommentListBean, true);
        }else {
            adapter.addDatas(storeCommentListBean, true);
        }

        if (storeCommentListBean.size() < pageSize) {// 加载的数据不够页面数量 则认为没有下一页
            refreshLayout.setNoMoreData(true);
        } else {
            refreshLayout.setNoMoreData(false);
        }

    }

    @Override
    public void getStoreCommentListFail() {
        RefreshLayoutUtil.finishRefreshLayout(refreshLayout);
        refreshLayout.setNoMoreData(true);
    }

    @Override
    public void replyStoreCommentSucceed() {
        ToastUtils.shortToast("回复成功");
        pageIndx = 1;
        if (type != 3) {
            getMvpPresenter().getStoreCommentList(storeId,pageIndx, pageSize,this);
        } else {
            getMvpPresenter().getStylistCommentList(stylistId, pageIndx, pageSize,this);
        }
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onGetEvaluate(StylistEvaluationBean bean) {

        if (bean != null) {
            mBinding.titleView.setSubTitleText("(" + bean.getTimes() +")");
            mBinding.materialRatingBar.setRating((float) bean.getComprehensive());
            mBinding.tvProfessionSkill.setText("技能评分 "+bean.getSkill());
            mBinding.tvServiceAttitude.setText(String.format(getString(R.string.service_attitude), bean.getServer()));

            int pareEnvirtAvg = bean.getSkillavg();    //技能平均分比较
            if (pareEnvirtAvg == 1){
                mBinding.tvRelativeRatio1.setText("高于平均水平");
            }else if (pareEnvirtAvg == 0){
                mBinding.tvRelativeRatio1.setText("等于平均水平");
            }else if(pareEnvirtAvg == -1){
                mBinding.tvRelativeRatio1.setText("低于平均水平");
            }else if (pareEnvirtAvg == 10){
                mBinding.tvRelativeRatio1.setText("等于平均水平");
            }

            int pareServerAvg = bean.getServeravg();    //服务平均分比较
            if (pareServerAvg == 1){
                mBinding.tvRelativeRatio2.setText("高于平均水平");
            }else if (pareServerAvg == 0){
                mBinding.tvRelativeRatio2.setText("等于平均水平");
            }else if(pareServerAvg == -1){
                mBinding.tvRelativeRatio2.setText("低于平均水平");
            }else if (pareServerAvg == 10){
                mBinding.tvRelativeRatio2.setText("等于平均水平");
            }
        }

    }
}
