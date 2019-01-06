package com.yiyue.store.widget.filter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.databinding.ItemFilterNearbyBinding;
import com.yiyue.store.model.vo.bean.AreaBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 *  @描述：    附近的适配器
 */

public class FilterNearbyAdapter extends BaseRecycleViewAdapter{
    private Context mContext;
    private SynthesisAdapter mSynthesisAdapter;
    private List<AreaBean> areaList;
    private ArrayList<String> areaNames;

    public FilterNearbyAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public FilterNearbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_filter_nearby, parent, false);
        return new FilterNearbyAdapter.FilterNearbyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FilterNearbyViewHolder filterNearbyViewHolder= (FilterNearbyViewHolder) holder;
        filterNearbyViewHolder.mBinding.rvParentMenu.setLayoutManager(new LinearLayoutManager(mContext));

        if (mSynthesisAdapter==null){
            areaNames = new ArrayList<>();
            for (AreaBean areaBean :areaList) {
                areaNames.add(areaBean.getName());
            }

            mSynthesisAdapter = new SynthesisAdapter(mContext,1);

            mSynthesisAdapter.setDatas(areaNames,true);

            mSynthesisAdapter.setItemListener(new RecycleViewItemListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (mINearbySelectListener!=null){
                        HashMap<String, String> hashMap = new HashMap<>();
                        if (position==0){
                        }else if (position==1){
                            hashMap.put("cityId",areaList.get(1).getId()+"");
                            mINearbySelectListener.callBack(hashMap);
                        }else {
                            hashMap.put("districtId",areaList.get(position).getId()+"");
                            mINearbySelectListener.callBack(hashMap);
                        }
                    }
                }
                @Override
                public void OnItemLongClickListener(View view, int position) {
                }
            });
        }
        filterNearbyViewHolder.mBinding.rvParentMenu.setAdapter(mSynthesisAdapter);
    }
    public void setAreaList(List<AreaBean> areaList) {
        this.areaList=areaList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class FilterNearbyViewHolder extends BaseViewHolder {
        private ItemFilterNearbyBinding mBinding;

        public FilterNearbyViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            mBinding.rvSubtMenu.setLayoutManager(new LinearLayoutManager(mContext));
            ArrayList<String> distances = new ArrayList<>();
                distances.add("0.5km");
                distances.add("1km");
                distances.add("2km");
                distances.add("3km");
                distances.add("4km");
                distances.add("5km");
            SynthesisAdapter synthesisAdapter2 = new SynthesisAdapter(mContext,0);
            synthesisAdapter2.setItemListener(new RecycleViewItemListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (mINearbySelectListener!=null){
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("distance",distances.get(position).replace("km",""));
                        mINearbySelectListener.callBack(hashMap);
                    }
                }

                @Override
                public void OnItemLongClickListener(View view, int position) {

                }
            });
            mBinding.rvSubtMenu.setAdapter(synthesisAdapter2);
            synthesisAdapter2.setDatas(distances,true);
        }
    }
    private INearbySelectListener mINearbySelectListener;

    public interface INearbySelectListener {
        void callBack( Map<String, String> screenings );
//        void areanCallBack( Map<String, String> screenings );
//        void distancesCallBack( Map<String, String> screenings );
    }

    public void setINearbySelectListener(INearbySelectListener INearbySelectListener) {
        mINearbySelectListener = INearbySelectListener;
    }
}
