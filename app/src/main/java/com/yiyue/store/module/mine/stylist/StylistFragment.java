package com.yiyue.store.module.mine.stylist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yiyue.store.model.vo.bean.StylistNumBean;
import com.yiyue.store.module.mine.collect.CollectActivity;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.base.fragment.BaseMvpFragment;
import com.yiyue.store.component.recycleview.GridSpacingItemDecoration;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentStylistBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.AreaBean;
import com.yiyue.store.model.vo.bean.StylistBean;
import com.yiyue.store.module.home.invite.IUpDataFragment;
import com.yiyue.store.module.home.invite.InviteStylistActivity;
import com.yiyue.store.module.home.invite.SearchStylistActivity;
import com.yiyue.store.module.mine.stylist.details.StylistDetailsActivity;
import com.yiyue.store.util.RefreshLayoutUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zm on 2018/10/10.
 */
@CreatePresenter(StylistPresenter.class)
public class StylistFragment extends BaseMvpFragment<IStylistView,StylistPresenter>
        implements IStylistView,OnRefreshListener,OnLoadMoreListener,IUpDataFragment {
    private StylistAdapter adapter;
    private StylistMineAdapter mineAdapter;
    private ArrayList<StylistBean> data = new ArrayList<>();
    private String mUserId;
    private String mStoreId;
    private int page = 1;//页数
    private int size = 10;//每页数量
    
    private int fromActivity;//从哪个页面来的
    private FragmentStylistBinding mStylistBinding;
    private int filterType=2;
    private Object mFilterTypeBean;//不同筛选类型的条件
    private InputMethodManager mImm;
    private String stylistId = "";//美发师ID
    private List<StylistBean> stylistBeanLists = new ArrayList<StylistBean>();//列表数据

    private RefreshLayout refreshLayout;
    private StylistCollectAdapter mCollectAdapter;
    private Intent mStartIntent;
    private Bundle mStartBundle;

    public static Fragment newInstance(int from) {
        StylistFragment stylistFragment = new StylistFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("from",from);
        stylistFragment.setArguments(bundle);
        return stylistFragment;
    }

    protected void initRefreshLoadLayout() {
        SmartRefreshLayout refreshLayout = mStylistBinding.refreshLayout;
        if (refreshLayout != null) {
            refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
            refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
            refreshLayout.setOnLoadMoreListener(this);
            refreshLayout.setOnRefreshListener(this);
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_stylist;
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        fromActivity = bundle.getInt("from", 0);
        mStylistBinding = (FragmentStylistBinding) viewDataBinding;
        // init recycleview
        mStylistBinding.recycleView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mStylistBinding.recycleView.addItemDecoration(new GridSpacingItemDecoration(2, 30, false));
        initRefreshLoadLayout();
        switch (fromActivity) {
                //签约/入驻
            case Constants.ACTIVITY_JOIN_STYLIST_1 :
            case Constants.ACTIVITY_JOIN_STYLIST_2 :
                initStylistAdapter();
                break;
                //邀请
            case Constants.ACTIVITY_INVITE_STYLIST :
                InviteStylistActivity activity = (InviteStylistActivity) getActivity();
                activity.setIUpDataFragment(this);
                initStylistMineAdapter();
                break;
                //搜索
            case Constants.ACTIVITY_INVITE_SEARCH :
                SearchStylistActivity searchStylistActivity = (SearchStylistActivity) getActivity();
                searchStylistActivity.setIUpDataFragment(this);
                mImm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                initStylistMineAdapter();
                break;
               //收藏/足迹
            case Constants.ACTIVITY_COLLECT :
            case Constants.ACTIVITY_FOOTPRINT :
                initStylistCollectAdapter();
                break;
            case 0:
                showToast("来源页获取错误");
                break;
        }
    }

    private void initStylistAdapter() {
        adapter = new StylistAdapter(getContext());
        adapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener(){
            @Override
            public void onItemClick(View view, int position) {
                //把美发师ID传入下个页面
                stylistId = adapter.getDatas().get(position).getStylistId()+"";
                mStartBundle.putString(Constants.STYLIST_ID,stylistId);
                mStartIntent.putExtras(mStartBundle);
                mStartIntent.setClass(getContext(),StylistDetailsActivity.class);
                startActivityForResult(mStartIntent,Constants.RESULT_REFRESH_CODE);
            }
        });
        mStylistBinding.recycleView.setAdapter(adapter);
    }

    private void initStylistCollectAdapter() {
        mCollectAdapter = new StylistCollectAdapter(getContext());
        mCollectAdapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener(){
            @Override
            public void onItemClick(View view, int position) {
                //把美发师ID传入下个页面
                stylistId = mCollectAdapter.getDatas().get(position).getStylistId()+"";
                mStartBundle.putString(Constants.STYLIST_ID,stylistId);
                mStartIntent.putExtras(mStartBundle);
                mStartIntent.setClass(getContext(),StylistDetailsActivity.class);
                startActivityForResult(mStartIntent,Constants.RESULT_REFRESH_CODE);

            }
        });
        mStylistBinding.recycleView.setAdapter(mCollectAdapter);
    }

    private void initStylistMineAdapter() {
        mineAdapter = new StylistMineAdapter(getContext());
        mineAdapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener(){
            @Override
            public void onItemClick(View view, int position) {
                //把美发师ID传入下个页面
                stylistId = mineAdapter.getDatas().get(position).getStylistId()+"";
                mStartBundle.putString(Constants.STYLIST_ID,stylistId);
                mStartIntent.putExtras(mStartBundle);
                mStartIntent.setClass(getContext(),StylistDetailsActivity.class);
                startActivityForResult(mStartIntent,Constants.RESULT_REFRESH_CODE);
            }
        });
        mStylistBinding.recycleView.setAdapter(mineAdapter);
    }
    @Override
    protected void loadData() {
        mUserId = AccountManager.getInstance().getUserId();
        mStoreId = AccountManager.getInstance().getStoreId();
        mStartIntent = new Intent();
        mStartBundle = new Bundle();
        if (fromActivity!=Constants.ACTIVITY_INVITE_SEARCH )mStylistBinding.refreshLayout.autoRefresh();
    }

    @Override
    public void showToast(String message)
    {
        ToastUtils.shortToast(message);
    }


    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
        page=1;
        switch (fromActivity) {
            //签约/入驻
            case Constants.ACTIVITY_JOIN_STYLIST_1 :
            case Constants.ACTIVITY_JOIN_STYLIST_2 :
                getMvpPresenter().getMyStylist(fromActivity-1,mStoreId);
                break;
            //邀请/搜索
            case Constants.ACTIVITY_INVITE_STYLIST :
            case Constants.ACTIVITY_INVITE_SEARCH :
                loadFilterTypeData(filterType,mFilterTypeBean,page);
                break;
            // 收藏/足迹
            case Constants.ACTIVITY_COLLECT :
                getMvpPresenter().getStylistCollection(page, size, mUserId);
                break;
            case Constants.ACTIVITY_FOOTPRINT :
                getMvpPresenter().getStylistFoot(page, size, mUserId);
                break;
            default:
                showToast("来源页获取错误");
                break;
        }
    }

    @Override
    public void getStylistSuccess(List<StylistBean> stylistBeanList) {
        RefreshLayoutUtil.finishRefreshLayout(refreshLayout);
        if (stylistBeanList.size() < size) {// 加载的数据不够页面数量 则认为没有下一页
            refreshLayout.setNoMoreData(true);
        } else {
            refreshLayout.setNoMoreData(false);
        }

        switch (fromActivity) {
            //签约/入驻
            case Constants.ACTIVITY_JOIN_STYLIST_1 :
            case Constants.ACTIVITY_JOIN_STYLIST_2 :
                if (refreshLayout == null || refreshLayout.getState()== RefreshState.Refreshing) {
                    adapter.setDatas((ArrayList<StylistBean>) stylistBeanList, true);
                }else if (refreshLayout.getState() == RefreshState.Loading){
                    adapter.addDatas((ArrayList<StylistBean>) stylistBeanList, true);
                }
                refreshLayout.setNoMoreData(true);
                break;
            //邀请/搜索
            case Constants.ACTIVITY_INVITE_STYLIST :
                if (refreshLayout == null || refreshLayout.getState()== RefreshState.Refreshing) {
                    mineAdapter.setDatas((ArrayList<StylistBean>) stylistBeanList, true);
                }else if (refreshLayout.getState() == RefreshState.Loading){
                    mineAdapter.addDatas((ArrayList<StylistBean>) stylistBeanList, true);
                }
                break;
            //收藏/足迹
            case Constants.ACTIVITY_COLLECT :
            case Constants.ACTIVITY_FOOTPRINT :
                if (refreshLayout == null || refreshLayout.getState()== RefreshState.Refreshing) {
                    mCollectAdapter.setDatas((ArrayList<StylistBean>) stylistBeanList, true);
                }else if (refreshLayout.getState() == RefreshState.Loading){
                    mCollectAdapter.addDatas((ArrayList<StylistBean>) stylistBeanList, true);
                }
                break;
            case Constants.ACTIVITY_INVITE_SEARCH :
                if (refreshLayout == null || refreshLayout.getState()== RefreshState.Refreshing) { // 刷新
                    mineAdapter.setDatas((ArrayList<StylistBean>) stylistBeanList, true);
                }else if (refreshLayout.getState() == RefreshState.Loading){ // 加载
                    mineAdapter.addDatas((ArrayList<StylistBean>) stylistBeanList, true);
                }
                //隐藏键盘
                if(stylistBeanList.size()!=0)mImm.hideSoftInputFromWindow(mStylistBinding.refreshLayout.getWindowToken(), 0);
                break;
        }
    }

    @Override
    public void getAreaByStoreId(List<AreaBean> areaBeans) {

    }

    @Override
    public void getStylistFail() {
        RefreshLayoutUtil.finishRefreshLayout(mStylistBinding.refreshLayout);
    }

    @Override
    public void onGetStoreStylistNumber(StylistNumBean bean) {

    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
        page++;
        switch (fromActivity) {
            //签约/入驻
            case Constants.ACTIVITY_JOIN_STYLIST_1 :
            case Constants.ACTIVITY_JOIN_STYLIST_2 :
                getMvpPresenter().getMyStylist(fromActivity-1,mStoreId);
                break;
            //邀请/搜索
            case Constants.ACTIVITY_INVITE_STYLIST :
            case Constants.ACTIVITY_INVITE_SEARCH :
                loadFilterTypeData(filterType,mFilterTypeBean,page);
                break;
            // 收藏/足迹
            case Constants.ACTIVITY_COLLECT :
                getMvpPresenter().getStylistCollection(page, size, mUserId);
                break;
            case Constants.ACTIVITY_FOOTPRINT :
                getMvpPresenter().getStylistFoot(page, size, mUserId);
                break;
            default:
                showToast("来源页获取错误");
                break;
        }
    }

    /**
     *
     * @param filterType 筛选类型
     * @param o 筛选条件
     */
    @Override
    public void onUpData(int filterType, Object o) {
        this.filterType=filterType;
        mFilterTypeBean = o;
        page=1;
        mStylistBinding.refreshLayout.autoRefresh();
    }

    /**
     * 获取筛选后的数据
     * @param sortType
     * @param o
     * @param page
     */
    private void loadFilterTypeData(int sortType,Object o,int page) {
        switch (sortType) {
            case Constants.ACTIVITY_FILTER_STYLIST_1:
                //邀请美发师-附近
                Map<String, String> nears = (Map<String, String>) o;
                getMvpPresenter().getInviteNear(nears.get("cityId"),nears.get("districtId"),nears.get("distance"),mStoreId,page);
//                ToastUtils.shortToast(nears.toString());
                break;
            case Constants.ACTIVITY_FILTER_STYLIST_2:
                //邀请美发师-综合排序
                int srtType;
                if (o==null){
                    //第一次进入页面,默认综合排序
                     srtType=0;
                }else {
                    srtType = (int)o;
                }
                getMvpPresenter().getInviteSort(srtType,page,mStoreId);
                break;
            case Constants.ACTIVITY_FILTER_STYLIST_3:
                Map<String, String> screenings = (Map<String, String>) o;
                //邀请美发师-筛选
                getMvpPresenter().getInviteScreen(screenings.get("coupon"),screenings.get("setMeal"),page,mStoreId);
                break;
                case Constants.ACTIVITY_FILTER_STYLIST_4:
                //邀请美发师-搜索
                String content =(String)o;
                getMvpPresenter().getInviteSearch(content,page,mStoreId);
                break;
                case Constants.ACTIVITY_FILTER_STYLIST_5:
                //我的美发师-搜索
                String content2 =(String)o;
                getMvpPresenter().storeStylistSearch(content2,page,mStoreId);
                break;
            default:
                showToast("来源页获取错误");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== Constants.RESULT_REFRESH_CODE&&resultCode==Constants.RESULT_REFRESH_CODE){
            refreshLayout.autoRefresh();
        }
    }
}
