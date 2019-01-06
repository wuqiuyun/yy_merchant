package com.yiyue.store.widget.PhotoView;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyue.store.R;
import com.yiyue.store.databinding.ItemPhotoViewBinding;
import com.yl.core.component.image.ImageLoader;

import java.util.List;

/**
 * Created by zm on 2018/10/12.
 */
public class PhotoAdapter extends PagerAdapter{

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String>  images;

    public PhotoAdapter(Context context, List<String> images) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.images = images;
    }


    @Override
    public int getCount() {
        return images.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.item_photo_view, container, false);
        ItemPhotoViewBinding binding = DataBindingUtil.bind(view);
        ImageLoader.loadImage(binding.ivPhoto, images.get(position));
        binding.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PhotoViewActivity)mContext).finish();
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        // 最简单解决 notifyDataSetChanged() 页面不刷新问题的方法
        return POSITION_NONE;
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
