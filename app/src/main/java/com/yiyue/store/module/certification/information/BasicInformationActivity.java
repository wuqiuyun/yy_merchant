package com.yiyue.store.module.certification.information;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityBasicInformationBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.bean.AddrInfoBean;
import com.yiyue.store.model.vo.bean.ProvinceBean;
import com.yiyue.store.model.vo.requestbody.DoStoreDataRequestBody;
import com.yiyue.store.module.certification.CertificationActivity;
import com.yiyue.store.module.common.addr.AddressSelectActivity;
import com.yiyue.store.util.ColorUtil;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.util.GetJsonDataUtil;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import org.json.JSONArray;

import java.util.ArrayList;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 基本信息
 * <p>
 * Created by zm on 2018/10/9.
 */
@CreatePresenter(BasicInformationPresenter.class)
public class BasicInformationActivity extends BaseMvpAppCompatActivity<BasicInformationView , BasicInformationPresenter>
        implements ClickHandler , BasicInformationView {
    private static final int REQUEST_CODE_ADDR = 101;

    ActivityBasicInformationBinding mBinding;

    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<ProvinceBean.CityBean>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<ProvinceBean.AreaBean>>> options3Items = new ArrayList<>();

    private AddrInfoBean mAddrInfoBean = null; // 选择地址信息

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_basic_information;
    }

    @Override
    protected void init() {

        initView();
        initData();
    }

    private void initData() {
        Flowable.create(emitter -> {
            initDataJson();
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> DLog.i("省市区数据初始化完成"));
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityBasicInformationBinding) viewDataBinding;
        mBinding.setClick(this);

        mBinding.titleView.setLeftClickListener(view -> finish());
        // 修改状态栏字体颜色
        StatusBarUtil.setLightMode(this);
    }

    private void initDataJson() {
        String jsonData = GetJsonDataUtil.getJson(this, "province.json");
        ArrayList<ProvinceBean> provinceBeans = parseData(jsonData);

        options1Items = provinceBeans;

        for (int i = 0; i < provinceBeans.size(); i++) {//遍历省份
            ArrayList<ProvinceBean.CityBean> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<ProvinceBean.AreaBean>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < provinceBeans.get(i).getAreaList().size(); c++) {//遍历该省份的所有城市
                CityList.add(provinceBeans.get(i).getAreaList().get(c));//添加城市

                ArrayList<ProvinceBean.AreaBean> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (provinceBeans.get(i).getAreaList().get(c).getAreaList() == null
                        || provinceBeans.get(i).getAreaList().get(c).getAreaList().size() == 0) {
                    City_AreaList.add(new ProvinceBean.AreaBean());
                } else {

                    for (int d = 0; d < provinceBeans.get(i).getAreaList().get(c).getAreaList().size(); d++) {//该城市对应地区所有数据
                        City_AreaList.add(provinceBeans.get(i).getAreaList().get(c).getAreaList().get(d));//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }

    private ArrayList<ProvinceBean> parseData(String result) {//Gson 解析
        ArrayList<ProvinceBean> provinceBeans = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                ProvinceBean entity = gson.fromJson(data.optJSONObject(i).toString(), ProvinceBean.class);
                provinceBeans.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provinceBeans;
    }

    private void showPickerView() {// 弹出选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            mAddrInfoBean = new AddrInfoBean();
            mAddrInfoBean.setProvinceId(options1Items.get(options1).getAreaId());
            mAddrInfoBean.setCityId(options2Items.get(options1).get(options2).getAreaId());
            mAddrInfoBean.setAdId(options3Items.get(options1).get(options2).get(options3).getAreaId());
            //返回的分别是三个级别的选中位置
            String tx = options1Items.get(options1).getPickerViewText()
                    + "-"
                    + options2Items.get(options1).get(options2).getName()
                    + "-"
                    + options3Items.get(options1).get(options2).get(options3).getName();
            mBinding.tvStoresLocation.setText(tx);
        }).setDividerColor(Color.BLACK)
                .setTextColorCenter(ColorUtil.getColor(R.color.color_343434))
                .setContentTextSize(16)
                .setLineSpacingMultiplier(2.0f)
                .build();


        pvOptions.setPicker(options1Items, options2Items, options3Items); //三级选择器
        pvOptions.show();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.tv_stores_location: //选择地区
                /*KeyboardUtil.closeSoftKeyboard(this);
                showPickerView();*/
                AddressSelectActivity.startActivity(this, REQUEST_CODE_ADDR);
                break;

            case R.id.rl_location: //选择位置信息

                AddressSelectActivity.startActivity(this, REQUEST_CODE_ADDR);
                break;

            case R.id.btn_next: //下一步
                DoStoreDataRequestBody.Builder builder
                        = new DoStoreDataRequestBody.Builder()
                        .location(mBinding.etDetailedAddress.getText().toString().trim())
                        .storename(mBinding.etStoresName.getText().toString().trim())
                        .userId(AccountManager.getInstance().getUserId())
                        .mobile(AccountManager.getInstance().getMobile());

                if (mAddrInfoBean != null) {
                    builder.latitude(mAddrInfoBean.getLat());
                    builder.longitude(mAddrInfoBean.getLon());
                    builder.provinceGDId(mAddrInfoBean.getProvinceId());
                    builder.cityGDId(mAddrInfoBean.getCityId());
                    builder.districtGDId(mAddrInfoBean.getAdId());
                }
                getMvpPresenter().doStoreData(builder.build());
                break;

        }

    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(FormatUtil.Formatstring(message));
    }

    @Override
    public void onSubmitDataSuccess() {
        AccountManager.getInstance().setStoreName(mBinding.etStoresName.getText().toString().trim());
        CertificationActivity.startActivity(this, CertificationActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADDR) { // 选择地址
            if (resultCode == Activity.RESULT_OK) {
                if (data == null || data.getExtras() == null) return;
                mAddrInfoBean = data.getExtras().getParcelable("data");
                if (mAddrInfoBean == null) return;
                mBinding.etDetailedAddress.setText(TextUtils.isEmpty(mAddrInfoBean.getAddrDetail())
                        ? mAddrInfoBean.getAddr() : mAddrInfoBean.getAddrDetail());
                StringBuilder addrss = new StringBuilder();
                addrss.append(mAddrInfoBean.getProvinceName());
                addrss.append("-");
                addrss.append(mAddrInfoBean.getCity());
                addrss.append("-");
                addrss.append(mAddrInfoBean.getAdName());
                mBinding.tvStoresLocation.setText(addrss);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
