package com.yiyue.store.module.mine.works.details;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yl.core.component.image.ImageLoader;
import com.yiyue.store.R;
import com.yiyue.store.databinding.ItemWorksListBinding;

import java.util.List;

/**
 * Created by zm on 2018/10/12.
 */
public class WorksPageAdapter extends PagerAdapter{

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mWorksBeans;

    public WorksPageAdapter(Context context, List<String> worksBeans) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mWorksBeans = worksBeans;
    }


    @Override
    public int getCount() {
        return mWorksBeans.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.item_works_list, container, false);
        ItemWorksListBinding binding = DataBindingUtil.bind(view);
//        binding.ivWorks.setImageResource(R.drawable.meizi);
        ImageLoader.loadImage(binding.ivWorks, mWorksBeans.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
