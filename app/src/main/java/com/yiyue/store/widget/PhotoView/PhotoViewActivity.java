package com.yiyue.store.widget.PhotoView;

import android.content.Intent;
import android.support.v4.view.ViewPager;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseAppCompatActivity;
import com.yiyue.store.databinding.ViewPhotoviewBinding;
import com.yl.core.util.StatusBarUtil;


import java.util.List;

/**
 * 图片查看器
 * <p>
 * Create by zm on 2018/10/12Ø
 */

public class PhotoViewActivity extends BaseAppCompatActivity {

    ViewPhotoviewBinding mBinding;
    private List<String> images;
    public static String IMAGE_URL="image_url";
    public static String SHOW_POSITION="show_position";//第一张显示
    private int mPosition;

    @Override
    protected int getLayoutResId() {
        return R.layout.view_photoview;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        images = (List<String>)intent.getStringArrayListExtra(IMAGE_URL);
        mPosition = intent.getIntExtra(SHOW_POSITION, 0);
        initView();
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding =  (ViewPhotoviewBinding) viewDataBinding;
        mBinding.viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if (images.size() == 0) {
                    return;
                }
                mBinding.tvPageNum.setText(String.format(getString(R.string.opus_page), position + 1, images.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        if (images==null) return;
        PhotoAdapter   mPhotoAdapter = new PhotoAdapter(PhotoViewActivity.this, images);
        mBinding.viewPage.setAdapter(mPhotoAdapter);
        mBinding.viewPage.setCurrentItem(mPosition);
        mBinding.tvPageNum.setText(String.format(getString(R.string.opus_page), mPosition + 1, images.size()));
    }

}
