package com.yiyue.store.module.home.time;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.recycleview.GridSpacingItemDecoration;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityTimeManageBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.bean.ServiceSettingBean;
import com.yiyue.store.model.vo.bean.StoreSettingTimeBean;
import com.yiyue.store.module.home.service.ServiceSettingAdapter;
import com.yiyue.store.widget.dialog.BaseEasyDialog;
import com.yiyue.store.widget.dialog.EasyDialog;
import com.yiyue.store.widget.dialog.ViewConvertListener;
import com.yiyue.store.widget.dialog.ViewHolder;
import com.yiyue.store.widget.mytimepickview.CustomTimePicker;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 *
 * 时间管理
 * Created by lvlong on 2018/10/11.
 */
@CreatePresenter(TimeManagePresenter.class)
public class TimeManageActivity extends BaseMvpAppCompatActivity<TimeManageView , TimeManagePresenter> implements ClickHandler , TimeManageView {

    ActivityTimeManageBinding mBinding;

    private CustomTimePicker mTimePicker;

    private ServiceSettingAdapter timeAdapter;
    private ArrayList<ServiceSettingBean> timeData = new ArrayList<>();
    private int station,stationlock,open = 1;
    private String starttime,endtime, workday;
    private int mId;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_time_manage;
    }

    @Override
    protected void init() {

        mBinding = (ActivityTimeManageBinding) viewDataBinding;
        mBinding.setClick(this);

        initView();
        initData();

        initTimerPicker();

    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding.titleView.setLeftClickListener(view -> finish());

        RecyclerView timeRecyclerView = mBinding.rvBusinessCycle;
        timeAdapter = new ServiceSettingAdapter(this);
        timeAdapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                timeData.get(position).setChecked(!timeData.get(position).isChecked());
                timeAdapter.notifyDataSetChanged();
//                ToastUtils.shortToast("点击了第" + position + "条目");
            }
        });
        timeRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        timeRecyclerView.setAdapter(timeAdapter);
        timeRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4, 50, false));

    }

    private void initData() {

        timeData.add(new ServiceSettingBean(getString(R.string.monday), false));
        timeData.add(new ServiceSettingBean(getString(R.string.tuesday), false));
        timeData.add(new ServiceSettingBean(getString(R.string.wednesday), false));
        timeData.add(new ServiceSettingBean(getString(R.string.thursday), false));
        timeData.add(new ServiceSettingBean(getString(R.string.friday), false));
        timeData.add(new ServiceSettingBean(getString(R.string.saturday), false));
        timeData.add(new ServiceSettingBean(getString(R.string.sunday), false));
        timeAdapter.setDatas(timeData, true);

        getMvpPresenter().getStoreSettingByStoreId(AccountManager.getInstance().getStoreId());//获取店铺时间管理
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.img_business_switch:
                mBinding.imgBusinessSwitch.setSelected(!mBinding.imgBusinessSwitch.isSelected());
                if (!mBinding.imgBusinessSwitch.isSelected()){
                    showCloseDialog();
                }
                break;
            case R.id.rl_order_time:
            case R.id.ll_order_time :         //服务时间
                mTimePicker.show();
                break;

            case R.id.btn_save :                  //保存
                if (mBinding.imgBusinessSwitch.isSelected())  open = 1;
                else  open = 0;
                if (timeAdapter.getDatas().size()!=0&&timeAdapter.getDatas()!=null){
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 0;i<timeAdapter.getDatas().size();i++){
                        if (timeAdapter.getDatas().get(i).isChecked()){
                            stringBuffer.append((i+1)+",");
                        }
                    }
                   String workdays = stringBuffer.toString();
                    if (workdays!=null&&!TextUtils.isEmpty(workdays)){
                        workday = workdays.substring(0,workdays.length()-1);
                    }
                }
                getMvpPresenter().updateOrSaveStoreTime(open,
                        mBinding.tvStoresBusinessTime.getText().toString().trim(),
                        mBinding.tvStoresBusinessTime1.getText().toString().trim(),
                        0,0,AccountManager.getInstance().getStoreId(),workday ,mId);//设置店铺时间管理
                break;

        }

    }

    private void initTimerPicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        String time = sdf.format(new Date());
        mTimePicker = new CustomTimePicker(this, "营业时间", time, time, mSelectTime ->
        {
            mBinding.tvStoresBusinessTime.setText(mSelectTime.getStartTime());
            mBinding.tvStoresBusinessTime1.setText(mSelectTime.getEndTime());

        });
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void getStoreTimeSuccess(StoreSettingTimeBean storeSettingTimeBean) {
        if (storeSettingTimeBean!=null){
            DLog.e("*********",storeSettingTimeBean.toString());
             open = storeSettingTimeBean.getOpen();
             station = storeSettingTimeBean.getStation();
             stationlock = storeSettingTimeBean.getStationlock();
            starttime = storeSettingTimeBean.getStarttime();
            endtime = storeSettingTimeBean.getEndtime();
            workday = storeSettingTimeBean.getWorkday();
            mId = storeSettingTimeBean.getId();
            mBinding.tvStoresBusinessTime.setText(starttime.substring(0,starttime.length()-3));
            mBinding.tvStoresBusinessTime1.setText(endtime.substring(0,endtime.length()-3));
            if (open==1){
                mBinding.imgBusinessSwitch.setSelected(true);
            }else {
                mBinding.imgBusinessSwitch.setSelected(false);
            }
            if (workday!=null&&!TextUtils.isEmpty(workday)){
                timeData.clear();
                if (workday.contains("1")) timeData.add(new ServiceSettingBean(getString(R.string.monday), true));
                else timeData.add(new ServiceSettingBean(getString(R.string.monday), false));
                if (workday.contains("2")) timeData.add(new ServiceSettingBean(getString(R.string.tuesday), true));
                else timeData.add(new ServiceSettingBean(getString(R.string.tuesday), false));
                if (workday.contains("3")) timeData.add(new ServiceSettingBean(getString(R.string.wednesday), true));
                else timeData.add(new ServiceSettingBean(getString(R.string.wednesday), false));
                if (workday.contains("4")) timeData.add(new ServiceSettingBean(getString(R.string.thursday), true));
                else timeData.add(new ServiceSettingBean(getString(R.string.thursday), false));
                if (workday.contains("5")) timeData.add(new ServiceSettingBean(getString(R.string.friday), true));
                else timeData.add(new ServiceSettingBean(getString(R.string.friday), false));
                if (workday.contains("6")) timeData.add(new ServiceSettingBean(getString(R.string.saturday), true));
                else timeData.add(new ServiceSettingBean(getString(R.string.saturday), false));
                if (workday.contains("7")) timeData.add(new ServiceSettingBean(getString(R.string.sunday), true));
                else timeData.add(new ServiceSettingBean(getString(R.string.sunday), false));
                timeAdapter.setDatas(timeData, true);
            }
        }
    }

    @Override
    public void setStoreTimeSuccess(StoreSettingTimeBean storeSettingTimeBean) {
        if (storeSettingTimeBean!=null){
            DLog.e("*********",storeSettingTimeBean.toString());
            open = storeSettingTimeBean.getOpen();
            station = storeSettingTimeBean.getStation();
            stationlock = storeSettingTimeBean.getStationlock();
            starttime = storeSettingTimeBean.getStarttime();
            endtime = storeSettingTimeBean.getEndtime();
            workday = storeSettingTimeBean.getWorkday();
            mId = storeSettingTimeBean.getId();

            finish();

        }
    }

    //关闭营业对话框
    private void showCloseDialog() {
        EasyDialog.init()
                .setLayoutId(R.layout.dialog_group_remove_member)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseEasyDialog dialog) {
                        holder.setText(R.id.tv_remove_title,"提示");
                        holder.setText(R.id.tv_remove_cancel,"取消");
                        holder.setText(R.id.tv_remove_content,"关闭营业开关后门店不再接受新的订单有未完成核销订单请及时处理！");
                        holder.setText(R.id.tv_remove_cancel,"取消");
                        holder.getView(R.id.tv_remove_ok).setOnClickListener(v -> {
                            mBinding.imgBusinessSwitch.setSelected(false);
                            dialog.dismiss();
                        });
                        holder.getView(R.id.tv_remove_cancel).setOnClickListener(v -> {
                            mBinding.imgBusinessSwitch.setSelected(true);
                            dialog.dismiss();
                        });
                    }
                })
                .setPosition(Gravity.CENTER)
                .setMargin(45)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

}
