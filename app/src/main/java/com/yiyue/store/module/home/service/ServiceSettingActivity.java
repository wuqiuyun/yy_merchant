package com.yiyue.store.module.home.service;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.recycleview.GridSpacingItemDecoration;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityServiceSettingBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.bean.CategoryBean;
import com.yiyue.store.model.vo.bean.ServiceSettingBean;
import com.yiyue.store.model.vo.bean.StoreSettingBean;
import com.yiyue.store.model.vo.requestbody.CommitSetServiceBody;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.widget.mytimepickview.CustomTimePicker;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 服务设置
 * Created by lvlong on 2018/10/9.
 */
@CreatePresenter(ServiceSettingPresenter.class)
public class ServiceSettingActivity extends BaseMvpAppCompatActivity<ServiceSettingView, ServiceSettingPresenter> implements ClickHandler, ServiceSettingView {

    ActivityServiceSettingBinding mBinding;

    private CustomTimePicker mTimePicker;

    private ServiceSettingAdapter timeAdapter;
    private ArrayList<ServiceSettingBean> timeData = new ArrayList<>();

    private ArrayList<ServiceSettingBean> facilitiesData = new ArrayList<>();
    private ServiceSettingAdapter facilitiesAdapter;
    private int mId=0;
    private StoreSettingBean storeSettingBean;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_service_setting;
    }

    @Override
    protected void init() {

        mBinding = (ActivityServiceSettingBinding) viewDataBinding;
        mBinding.setClick(this);

        initView();
        loadData();
        initTimerPicker();
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding.titleView.setLeftClickListener(view -> {
            finish();
        });

        //设置日期适配器
        setTimeAdapter();

        //设置美发设施适配器
        setFacilitiesAdapter();

    }

    private void loadData() {
        mBinding.titleView.setLeftClickListener(view -> finish());
        //添加日期数据
        addTimeData();
        //获取所有服务项目
        getMvpPresenter().getAll();
        getMvpPresenter().getStoreSetting(AccountManager.getInstance().getStoreId());
    }


    private void setTimeAdapter() {
        RecyclerView timeRecyclerView = mBinding.rvBusinessCycle;
        timeAdapter = new ServiceSettingAdapter(this);
        timeAdapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                timeData.get(position).setChecked(!timeData.get(position).isChecked());
                timeAdapter.setDatas(timeData,true);
            }
        });
        timeRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        timeRecyclerView.setAdapter(timeAdapter);
        timeRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4, 50, false));
    }



    private void setFacilitiesAdapter() {
        RecyclerView facilitiesRecyclerView = mBinding.serviceFacilitiesCycle;
        facilitiesAdapter = new ServiceSettingAdapter(this);
        facilitiesAdapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                facilitiesData.get(position ).setChecked(!facilitiesData.get(position).isChecked());
                facilitiesAdapter.setDatas(facilitiesData,true);
            }
        });
        facilitiesRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        facilitiesRecyclerView.setAdapter(facilitiesAdapter);
        facilitiesRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4, 50, false));


    }

    private void addTimeData() {

        timeData.add(new ServiceSettingBean(getString(R.string.monday), false,"1"));
        timeData.add(new ServiceSettingBean(getString(R.string.tuesday), false,"2"));
        timeData.add(new ServiceSettingBean(getString(R.string.wednesday), false,"3"));
        timeData.add(new ServiceSettingBean(getString(R.string.thursday), false,"4"));
        timeData.add(new ServiceSettingBean(getString(R.string.friday), false,"5"));
        timeData.add(new ServiceSettingBean(getString(R.string.saturday), false,"6"));
        timeData.add(new ServiceSettingBean(getString(R.string.sunday), false,"7"));
        timeAdapter.setDatas(timeData, true);

    }

    private void addFacilitiesData(List<CategoryBean> categoryBeans) {
        for (CategoryBean categoryBean:categoryBeans) {
            facilitiesData.add(new ServiceSettingBean(categoryBean.getName(), false,String.valueOf(categoryBean.getId())));
        }
        facilitiesAdapter.setDatas(facilitiesData, true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ll_stores_business_time:     //修改时间
            mTimePicker.show();
                break;

            case R.id.btn_ok:         //确定
                String times="";//一周营业时间
                String facilities="";//服务范围
                for (ServiceSettingBean settingBean:timeAdapter.getDatas()) {
                    if (settingBean.isChecked()){
                        times+=settingBean.getId()+",";
                    }
                }
                for (ServiceSettingBean settingBean: facilitiesAdapter.getDatas()) {
                    if (settingBean.isChecked()){
                        facilities+=settingBean.getId()+",";
                    }
                }
                if (times.length()!=0)times=times.substring(0,times.length()-1);

                if (facilities.length()!=0)facilities=facilities.substring(0,facilities.length()-1);
                //一天营业时间
                String[] time = mBinding.tvStoresBusinessTime.getText().toString().split("-");

                CommitSetServiceBody.Builder builder = new CommitSetServiceBody.Builder()
                        .storeId(AccountManager.getInstance().getStoreId())
                        .starttime(time[0] + ":00")
                        .endtime(time[1] + ":00")
                        .workday(times)
                        .station(mBinding.etStationNum.getText().toString().trim())
                        .categoryIds(facilities);
                if (mId!=0){
                    builder .id(String.valueOf(mId));
                }
                getMvpPresenter().commitServiceData(builder.build());

                break;
        }

    }

    private void initTimerPicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        String time = sdf.format(new Date());
        mTimePicker = new CustomTimePicker(this, "营业时间", time, time, mSelectTime ->
        {
            String businessTime = mSelectTime.getStartTime() + "-" + mSelectTime.getEndTime();
            mBinding.tvStoresBusinessTime.setText(businessTime);
        });
    }


    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void onCommitSuccess(List<CategoryBean> beans) {
        addFacilitiesData(beans);
        if (storeSettingBean!=null){
            initAdapterData(facilitiesAdapter,facilitiesData,storeSettingBean.getCategoryIds());
        }

    }

    @Override
    public void onSuccess(StoreSettingBean storeSettingBean) {
        this.storeSettingBean=storeSettingBean;
        mId = storeSettingBean.getStoreSettingInfo().getId();
        //开始时间
        String startTime = storeSettingBean.getStoreSettingInfo().getStarttime();
        if (startTime!=null&&startTime.length()!=0){
            startTime=startTime.substring(0,startTime.length()-3);
        }

        String endTime = storeSettingBean.getStoreSettingInfo().getEndtime();
        if (endTime!=null&&endTime.length()!=0){
            endTime=endTime.substring(0,endTime.length()-3);
        }
        mBinding.tvStoresBusinessTime.setText(startTime+"-"+endTime);
        //工数
        mBinding.etStationNum.setText(FormatUtil.Formatstring(storeSettingBean.getStoreSettingInfo().getStation()+""));
        //营业周期
        initAdapterData(timeAdapter,timeData,storeSettingBean.getStoreSettingInfo().getWorkday());
        //服务设施
        initAdapterData(facilitiesAdapter,facilitiesData,storeSettingBean.getCategoryIds());
    }

    @Override
    public void onCommitServiceData() {
        ToastUtils.shortToast("设置成功");
        finish();
    }

    private void initAdapterData(ServiceSettingAdapter adapter, ArrayList<ServiceSettingBean> list, String workday) {
        String[] strings = workday.split(",");
        for (String day : strings) {
            for (ServiceSettingBean bean : list) {
                if (day.equals(bean.getId())) {
                    bean.setChecked(true);
                    break;
                }
            }
        }

        adapter.setDatas(list,true);
    }
}
