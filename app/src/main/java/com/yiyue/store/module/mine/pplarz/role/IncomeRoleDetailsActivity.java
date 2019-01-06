package com.yiyue.store.module.mine.pplarz.role;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityIncomeRoleDetailsBinding;
import com.yiyue.store.model.vo.bean.IncomeRecordBean;
import com.yiyue.store.model.vo.bean.UserIncomeInfoBean;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.util.PhoneUtil;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.DateUtil;
import com.yl.core.util.StatusBarUtil;

import java.util.ArrayList;

/**
 * 推荐收益
 * <p>
 * Create by zm 2019/1/5
 */
@CreatePresenter(IncomeRoleDetailsPresenter.class)
public class IncomeRoleDetailsActivity extends BaseMvpAppCompatActivity<IncomeRoleDetailsView, IncomeRoleDetailsPresenter>
        implements IncomeRoleDetailsView, ClickHandler, OnLoadMoreListener{
    public static final String EXTRAS_ROLE = "ROLE";
    public static final String EXTRAS_INVITE_ROLE = "inviteRole";

    ActivityIncomeRoleDetailsBinding mBinding;
    private int roletype; // 角色
    private UserIncomeInfoBean bean;
    private int page = 1;
    private SectionAdapter adapter;

    public static void actionStart(Context context, int roletype, UserIncomeInfoBean bean) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRAS_ROLE, roletype);
        bundle.putParcelable(EXTRAS_INVITE_ROLE, bean);
        Intent intent = new Intent();
        intent.setClass(context, IncomeRoleDetailsActivity.class);
       intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_income_role_details;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
    }

    @Override
    protected void init() {
        hasExtras();
        initView();
        loadData();
    }

    private void hasExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            roletype = bundle.getInt(EXTRAS_ROLE, -1);
            bean = bundle.getParcelable(EXTRAS_INVITE_ROLE);
        }
        if (roletype == -1) {
            finish();
            return;
        }
    }

    private void initView() {
        StatusBarUtil.setFullDarkMode(this);
        mBinding = (ActivityIncomeRoleDetailsBinding) viewDataBinding;
        mBinding.setClick(this);
        initRefreshLayout();
        // 返回
        mBinding.titleView.setLeftClickListener(v -> {
            finish();
        });
        // 头像
        ImageLoaderConfig config = new ImageLoaderConfig.Builder()
                .setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE)
                .setCropCircle(true)
                .setCropType(ImageLoaderConfig.CENTER_INSIDE)
                .setErrorResId(R.drawable.icon_head_pic_def)
                .setPlaceHolderResId(R.drawable.icon_head_pic_def)
                .setAsBitmap(true)
                .build();
        ImageLoader.loadImage(mBinding.ivPhoto, bean.getHeadportrait(), config, null);
        // 名称
        mBinding.tvName.setText(FormatUtil.Formatstring(bean.getName()));
        //联系方式
        mBinding.tvContact.setText("联系方式：" + bean.getMobile());
        // 邀请时间
        mBinding.tvTime.setText(DateUtil.date2Str(bean.getInviteTime(), DateUtil.FORMAT_MDHM));
        // 佣金
        mBinding.tvIncomeTotal.setText(bean.getIncome()+"元");
    }

    private void loadData() {
        getMvpPresenter().recommendUserIncome(this, bean.getInviteUserId(), roletype, page);
    }

    protected void initRefreshLayout() {
        SmartRefreshLayout refreshLayout = mBinding.refreshLayout;
        if (refreshLayout != null) {
            refreshLayout.setRefreshFooter(new ClassicsFooter(getBaseContext()));
            refreshLayout.setHeaderHeight(0);
            refreshLayout.setOnLoadMoreListener(this);
        }
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(FormatUtil.Formatstring(message));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_tell:
                new RxPermissions(this)
                        .request(Manifest.permission.CALL_PHONE)
                        .subscribe( grant -> {
                           if (grant) {
                               PhoneUtil.toCallPhone(getApplicationContext(), bean.getMobile());
                           }
                        });
                break;
        }
    }

    @Override
    public void setData(ArrayList<IncomeRecordBean> datas) {
        if (adapter == null) {
            adapter = new SectionAdapter(this, datas);
            mBinding.shLv.setAdapter(adapter);
        }else {
            adapter.addData(datas);
        }
       mBinding.refreshLayout.setNoMoreData(datas.size()<10);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        page ++;
        loadData();
    }
}
