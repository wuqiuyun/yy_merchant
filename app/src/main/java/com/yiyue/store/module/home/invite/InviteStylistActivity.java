package com.yiyue.store.module.home.invite;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityStylistInviteBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.AreaBean;
import com.yiyue.store.model.vo.bean.StylistBean;
import com.yiyue.store.model.vo.bean.StylistNumBean;
import com.yiyue.store.module.mine.stylist.IStylistView;
import com.yiyue.store.module.mine.stylist.StylistFragment;
import com.yiyue.store.module.mine.stylist.StylistPresenter;
import com.yiyue.store.widget.filter.FilterView;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 邀请美发师
 * <p>
 * Created by zm on 2018/10/10.
 */
@CreatePresenter(StylistPresenter.class)
public class InviteStylistActivity extends BaseMvpAppCompatActivity<IStylistView, StylistPresenter>
        implements IStylistView{
    private static final String BUNDLE_FRAGMENT = "stylistFragment";
    private Fragment fragment;
    private ActivityStylistInviteBinding binding;
    private IUpDataFragment mIUpDataFragment;
    private List<AreaBean> areaBeans=new ArrayList<>();

    public void setIUpDataFragment(IUpDataFragment IUpDataFragment) {
        mIUpDataFragment = IUpDataFragment;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, BUNDLE_FRAGMENT, fragment);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_stylist_invite;
    }

    @Override
    protected void init() {
        initView();
        initData();
    }

    private void initData() {
        getMvpPresenter().getAreaByStoreId(AccountManager.getInstance().getStoreId());

    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        binding = (ActivityStylistInviteBinding) viewDataBinding;
        binding.titleView.setLeftClickListener(v -> finish());
        //搜索
        binding.titleView.setRightImgClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.STORE_TYPE,Constants.ACTIVITY_INVITE_STYLIST);
            startActivity(InviteStylistActivity.this,SearchStylistActivity.class,bundle);
        });
        binding.vBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.viewFilter.showView(binding.viewFilter.getTempPosition());
            }
        });


        binding.viewFilter.setIFilterDataCallBack(new FilterView.IFilterDataCallBack() {

            @Override
            public void onFilterNearbyCallBack(Map<String, String> screenings) {
                if (areaBeans==null||areaBeans.size()==0){
                    getMvpPresenter().getAreaByStoreId(AccountManager.getInstance().getStoreId());
                }

                if (mIUpDataFragment!=null){
                    mIUpDataFragment.onUpData(Constants.ACTIVITY_FILTER_STYLIST_1,screenings);
                }
                //附近数据回调
            }
            @Override
            public void onSynthesisCallBack(String sortType) {
                //综合排序数据回调
                int mSortType = 0;
                switch (sortType){
                    case "综合排序":
                        mSortType=0;
                        break;
                    case "距离最近":
                        mSortType=1;
                        break;
                    case "月接单最多":
                        mSortType=2;
                        break;
                    case "评论量最多":
                        mSortType=3;
                        break;
                    case "价格最低":
                        mSortType=4;
                        break;
                    case "价格最高":
                        mSortType=5;
                        break;
                }
                if (mIUpDataFragment!=null){
                    mIUpDataFragment.onUpData(Constants.ACTIVITY_FILTER_STYLIST_2,mSortType);
                }
            }

            @Override
            public void onFilterCallBack(Map<String, String> screenings) {
                //综合筛选数据回调
                mIUpDataFragment.onUpData(Constants.ACTIVITY_FILTER_STYLIST_3,screenings);
            }

            @Override
            public void setDimBackground(boolean b) {
                //背景变暗
                if (b){
                    binding.vBg.setVisibility(View.VISIBLE);
                }else {
                    binding.vBg.setVisibility(View.GONE);
                }

            }
        });

        initFragment();
    }



    private void initFragment() {
        if (savedInstanceState != null) {
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, BUNDLE_FRAGMENT);
        }
        if (fragment == null) {
            fragment = StylistFragment.newInstance(Constants.ACTIVITY_INVITE_STYLIST);
            fragment.setUserVisibleHint(true);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_content, fragment)
                .commit();
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }



    @Override
    public void getStylistSuccess(List<StylistBean> stylistBeanList) {



    }

    @Override
    public void getAreaByStoreId(List<AreaBean> areaBeans) {
        this.areaBeans=areaBeans;
        binding.viewFilter.setNearbyArea(areaBeans);
    }

    @Override
    public void getStylistFail() {
    }

    @Override
    public void onGetStoreStylistNumber(StylistNumBean bean) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
