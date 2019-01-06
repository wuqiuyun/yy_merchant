package com.yiyue.store.module.im.redpacket.redrecords;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityTransferRecordsBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.RedBagBean;
import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;
import com.yiyue.store.model.vo.result.RedRecordResult;
import com.yiyue.store.util.RefreshLayoutUtil;
import com.yiyue.store.widget.dialog.BaseEasyDialog;
import com.yiyue.store.widget.dialog.EasyDialog;
import com.yiyue.store.widget.dialog.ViewConvertListener;
import com.yiyue.store.widget.dialog.ViewHolder;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.util.List;

/**
 * Created by zhangzz on 2018/11/9
 */
@CreatePresenter(RedRecordsPresenter.class)
public class RedRecordsActivity extends BaseMvpAppCompatActivity<RedRecordsView, RedRecordsPresenter> implements RedRecordsView, View.OnClickListener, OnLoadMoreListener, OnRefreshListener {
    private ActivityTransferRecordsBinding mBinding;
    protected UserFriendsBean friendUserBean;//保存好友头像使用的本地查询的bean

    private SmartRefreshLayout refreshLayout;
    private RedRecordsAdapter redRecordsAdapter;

    private int page = 1;//页数
    private int size = 10;//每页数量
    private int status = 1;//1 发出 2 接收

    private int uiType = 1;//1红包记录  2转账记录
    private boolean isSend = true;//是否选择的是发出状态  区别展示发出和收到切换时 adapter的数据展示是添加还是新刷新

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_transfer_records;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityTransferRecordsBinding) viewDataBinding;
        mBinding.titleView.setLeftClickListener(view -> finish());
        friendUserBean = (UserFriendsBean) getIntent().getSerializableExtra(Constants.EXTRA_FRIEND_USER_BEAN);
        uiType = getIntent().getIntExtra("flag", 1);

        mBinding.tvNick.setText(TextUtils.isEmpty(AccountManager.getInstance().getUsername()) ? AccountManager.getInstance().getAccount().getNickname() : AccountManager.getInstance().getUsername());

        Drawable drawable = getResources().getDrawable(R.drawable.icon_down);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mBinding.titleView.geTitleText().setCompoundDrawables(null, null, drawable, null);
        if (uiType == 2) {
            mBinding.titleView.geTitleText().setText(R.string.transfer_send_record);
            mBinding.tvMoneySelf.setText(R.string.transfer_records_receive);
        } else {
            mBinding.titleView.geTitleText().setText(R.string.rp_send_record);
            mBinding.tvMoneySelf.setText(R.string.rp_records_receive);
        }

        mBinding.titleView.geTitleText().setOnClickListener(v -> {
            showChooseDialog();
        });

        ImageLoaderConfig config = null;
        if (config == null) {
            config = new ImageLoaderConfig.Builder().
                    setAsBitmap(true).
                    setPlaceHolderResId(R.drawable.im_avatar).
                    setErrorResId(R.drawable.im_avatar).
                    setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE).
                    setPrioriy(ImageLoaderConfig.LoadPriority.HIGH).build();
        }
        ImageLoader.loadImage(mBinding.ivAvatar, AccountManager.getInstance().getAccount().getHeadImg(), config, null);

        refreshLayout = mBinding.refreshLayout;
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

        mBinding.rvRpRecord.setHasFixedSize(true);
        mBinding.rvRpRecord.setNestedScrollingEnabled(false);
        mBinding.rvRpRecord.setLayoutManager(new LinearLayoutManager(this));

        redRecordsAdapter = new RedRecordsAdapter(R.layout.adapter_red_records, null);
        redRecordsAdapter.openLoadAnimation();
        mBinding.rvRpRecord.setAdapter(redRecordsAdapter);

        getReqList(page, true);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        page++;
        getReqList(page, false);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        page = 1;
        getReqList(page, true);
    }

    private void getReqList(int page, boolean isRefresh) {//1 发出 2 接收
        getMvpPresenter().findRedBagLogReq(uiType, AccountManager.getInstance().getUserId(), status + "", page + "", size + "", this, isRefresh);
    }

    @Override
    public void requestSuccess(RedRecordResult redRecordResult, boolean isRefresh) {
        RefreshLayoutUtil.finishRefreshLayout(refreshLayout);
        if (redRecordResult != null && redRecordResult.getData() != null) {
            mBinding.tvRpMoney.setText(String.format(getResources().getString(R.string.rp_records_money), redRecordResult.getData().getTotal()));
            if (uiType == 1) {
                mBinding.tvMoneySelf.setText(String.format(getResources().getString(R.string.rp_records_receive), redRecordResult.getData().getQuantity(), redRecordResult.getData().getTotal()));
            } else {
                mBinding.tvMoneySelf.setText(String.format(getResources().getString(R.string.transfer_records_receive), redRecordResult.getData().getQuantity(), redRecordResult.getData().getTotal()));
            }
            List<RedBagBean> list = redRecordResult.getData().getLsit();
            if (list != null && list.size() > 0) {
                if (null == redRecordsAdapter.getData() || redRecordsAdapter.getData().size() == 0) {
                    redRecordsAdapter.setNewData(list);
                } else {
                    if (isRefresh){
                        redRecordsAdapter.setNewData(list);
                    } else {
                        redRecordsAdapter.addData(list);
                    }
                }
            }
            if (list != null && list.size() < size) {// 加载的数据不够页面数量 则认为没有下一页
                refreshLayout.setNoMoreData(true);
            } else {
                refreshLayout.setNoMoreData(false);
            }
        }
    }

    @Override
    public void requestFail() {
        RefreshLayoutUtil.finishRefreshLayout(refreshLayout);
        refreshLayout.setNoMoreData(true);
    }


    /**
     * 底部弹框
     */
    public void showChooseDialog() {
        EasyDialog.init()
                .setLayoutId(R.layout.dialog_send_or_receive)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseEasyDialog dialog) {
                        if (uiType == 1) {
                            holder.setText(R.id.tv_dialog_recv, R.string.rp_recv_record);
                            holder.setText(R.id.tv_dialog_send, R.string.rp_send_record);
                        } else {
                            holder.setText(R.id.tv_dialog_recv, R.string.transfer_recv_record);
                            holder.setText(R.id.tv_dialog_send, R.string.transfer_send_record);
                        }
                        holder.getView(R.id.tv_dialog_recv).setOnClickListener(v -> {
                            //收到
                            if (uiType == 2) {
                                mBinding.titleView.geTitleText().setText(R.string.transfer_recv_record);
                            } else {
                                mBinding.titleView.geTitleText().setText(R.string.rp_recv_record);
                            }

                            mBinding.tvNickSend.setText(R.string.rp_total_recv);
                            status = 2;//1 发出 2 接收
                            if (isSend) {
                                page = 1;
                                redRecordsAdapter.setNewData(null);
                                getReqList(page, true);
                            }
                            isSend = false;

                            dialog.dismiss();
                        });
                        holder.getView(R.id.tv_dialog_send).setOnClickListener(v -> {
                            //发出
                            if (uiType == 2) {
                                mBinding.titleView.geTitleText().setText(R.string.transfer_send_record);
                            } else {
                                mBinding.titleView.geTitleText().setText(R.string.rp_send_record);
                            }
                            mBinding.tvNickSend.setText(R.string.rp_total_send);
                            status = 1;//1 发出 2 接收
                            if (!isSend) {
                                page = 1;
                                redRecordsAdapter.setNewData(null);
                                getReqList(page, true);
                            }
                            isSend = true;

                            dialog.dismiss();
                        });
                        holder.getView(R.id.dialog_cancle).setOnClickListener(v -> {
                            dialog.dismiss();
                        });
                    }
                })
                .setPosition(Gravity.BOTTOM)
                .setOutCancel(true)
                .show(getSupportFragmentManager());
    }

}
