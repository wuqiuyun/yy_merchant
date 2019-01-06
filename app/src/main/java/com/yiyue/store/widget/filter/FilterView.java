package com.yiyue.store.widget.filter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ItemFilterBinding;
import com.yiyue.store.model.vo.bean.AreaBean;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 筛选
 * <p>
 * Created by zm on 2018/10/12.
 */
public class FilterView extends LinearLayout implements ClickHandler{

    private IFilterDataCallBack mIFilterDataCallBack;
    private ItemFilterBinding mFilterBinding;
    private Context mContext;
    private ArrayList<TextView> mTextViews;
    private int tempPosition=-1;
    private boolean isShow=true;
    public static final int NEARBY = 0;
    public static final int SORT = 1;
    public static final int FILTER = 2;
    private SynthesisAdapter mSynthesisAdapter;
    private FilterAdapter mFilterAdapter;
    private FilterNearbyAdapter mFilterNearbyAdapter;
    private CustomLinearLayoutManager mCustomLinearLayoutManager;
    private List<AreaBean> mNearbyArea;
    private ArrayList<Object> mObjects;
    private ArrayList<String> mSubData;
    private ArrayList<String> mSubData2;

    public int getTempPosition() {
        return tempPosition;
    }

    public void setIFilterDataCallBack(IFilterDataCallBack IFilterDataCallBack) {
        mIFilterDataCallBack = IFilterDataCallBack;
    }

    public FilterView(Context context) {
        this(context, null);
    }

    public FilterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFilterBinding = DataBindingUtil.inflate(inflater, R.layout.item_filter, this, true);
        mTextViews = new ArrayList<>();
        mTextViews.add(mFilterBinding.tvNearby);
        mTextViews.add(mFilterBinding.tvSort);
        mTextViews.add(mFilterBinding.tvFilter);
        mCustomLinearLayoutManager = new CustomLinearLayoutManager(mContext);
//                禁止滑动
        mCustomLinearLayoutManager.setScrollEnabled(false);
        mFilterBinding.rvFilterCondition.setLayoutManager(mCustomLinearLayoutManager);
//        mFilterBinding.rvFilterCondition.addItemDecoration(new GridSpacingItemDecoration(2, 30, false));
        //综合排序
        mSynthesisAdapter = new SynthesisAdapter(mContext,0);
        final ArrayList<String> tempList = new ArrayList<>();
            tempList.add("综合排序");
            tempList.add("距离最近");
            tempList.add("月接单最多");
            tempList.add("评论量最多");
            tempList.add("价格最低");
            tempList.add("价格最高");
        mSynthesisAdapter.setDatas(tempList,true);
        mSynthesisAdapter.setItemListener(new BaseRecycleViewAdapter.RecycleViewItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                showView(SORT);
                mSynthesisAdapter.setTempPosition(position);
//                mFilterBinding.tvSort.setText(tempList.get(position));
                if (mIFilterDataCallBack!=null){
                    mIFilterDataCallBack.onSynthesisCallBack(mSynthesisAdapter.getDatas().get(position));
                }
            }

            @Override
            public void OnItemLongClickListener(View view, int position) {

            }
        });
        //筛选........
        if (mFilterAdapter==null){
            mFilterAdapter = new FilterAdapter(mContext);
        }
        mObjects = new ArrayList<>();
        mSubData = new ArrayList<>();
        mSubData2 = new ArrayList<>();
        mObjects.add("美发师等级");
        mSubData.add("高级");
        mSubData.add("资深");
        mSubData.add("首席");
        mSubData.add("总监");
        mSubData.add("督导");
        mObjects.add(mSubData);
        mObjects.add("优惠活动");
        mSubData2.add("优惠劵");
        mSubData2.add("套餐劵");
        mObjects.add(mSubData2);
        mFilterAdapter.setDatas(mObjects,true);
        mFilterAdapter.setIOkButtonListener(new FilterAdapter.IOkButtonListener() {
            @Override
            public void onOkButtonClick(Map<Integer, Integer> screenings) {
                showView(FILTER);
                HashMap<String, String> temp = new HashMap<>();
                if(screenings.get(1)!=null){
                    String setMeal = (String) mSubData.get(screenings.get(1));
                    temp.put("setMeal",setMeal);
                }else {
                    temp.put("setMeal","-1");
                }

                if(screenings.get(3)!=null){
                    String coupon = (String) mSubData.get(screenings.get(3));
                    if (coupon.equals("优惠劵")){
                        temp.put("coupon", "1");//活动类型 1 优惠卷 2 套餐卷
                    }else {
                        temp.put("coupon", "2");
                    }
                }else {
                    temp.put("coupon","-1");
                }


                if (mIFilterDataCallBack!=null){
                    mIFilterDataCallBack.onFilterCallBack(temp);
                }
            }
        });
        mFilterBinding.setClick(this);
    }


    private void setDrawableRight(TextView textView, @NotNull Drawable drawableRight) {
        drawableRight.setBounds(0, 0,drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawableRight, null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_nearby: // 附近
                if (mFilterNearbyAdapter!=null){
                    mFilterBinding.rvFilterCondition.setAdapter(mFilterNearbyAdapter);
                    showView(NEARBY);
                }else {
                    ToastUtils.shortToast("区域加载异常");
                }
                break;
            case R.id.tv_sort: // 综合排序
                mFilterBinding.rvFilterCondition.setAdapter(mSynthesisAdapter);
                showView(SORT);
                break;
            case R.id.tv_filter: // 筛选
                mFilterBinding.rvFilterCondition.setAdapter(mFilterAdapter);
                showView(FILTER);
                break;
        }
    }

    public void showView(int p) {
        for (int i = 0; i < mTextViews.size(); i++) {
            if (p==i){
                if (tempPosition==p){
                    isShow=!isShow;
                }else {
                    isShow=true;
                }
                if (isShow){
//                show
                    if (mIFilterDataCallBack!=null){
                        mIFilterDataCallBack.setDimBackground(true);
                    }
                    mFilterBinding.rvFilterCondition.setVisibility(VISIBLE);
                    setDrawableRight(mTextViews.get(i), ContextCompat.getDrawable(mContext, R.drawable.icon_up));
                }else {
//                hide
                    mIFilterDataCallBack.setDimBackground(false);
                    mFilterBinding.rvFilterCondition.setVisibility(GONE);
                    setDrawableRight(mTextViews.get(i), ContextCompat.getDrawable(mContext, R.drawable.icon_down));
                }
            }else {
                setDrawableRight(mTextViews.get(i), ContextCompat.getDrawable(mContext, R.drawable.icon_down));
            }
        }
        tempPosition=p;
    }

    public void setNearbyArea(List<AreaBean> nearbyArea) {
        //附近
        mFilterNearbyAdapter = new FilterNearbyAdapter(mContext);
        mFilterNearbyAdapter.setINearbySelectListener(new FilterNearbyAdapter.INearbySelectListener() {
            @Override
            public void callBack(Map<String, String> screenings) {
                showView(NEARBY);
                if (mIFilterDataCallBack!=null){
                    mIFilterDataCallBack.onFilterNearbyCallBack(screenings);

                }
            }
        });
        if (nearbyArea.size()!=0){
            nearbyArea.get(0).setName("全部区域");
        }
        nearbyArea.add(0,new AreaBean("附近"));

        mFilterNearbyAdapter.setAreaList(nearbyArea);


    }

    public class CustomLinearLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomLinearLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
    }


    public interface IFilterDataCallBack {
        void onFilterNearbyCallBack( Map<String, String> screenings );
        void onSynthesisCallBack(String sortType );
        void onFilterCallBack(Map<String, String> screenings );
        void setDimBackground(boolean b);
    }

}
