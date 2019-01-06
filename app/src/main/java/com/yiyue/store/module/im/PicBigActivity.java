package com.yiyue.store.module.im;

import android.text.TextUtils;
import android.widget.ImageView;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseAppCompatActivity;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.component.image.glide.GlideImageLoaderStrategy;
import com.yl.core.util.StatusBarUtil;

import java.io.File;

public class PicBigActivity extends BaseAppCompatActivity {
    private ImageView iv_pic;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pic_big;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        String localUrl = getIntent().getStringExtra("localUrl");
        String remoteUrl = getIntent().getStringExtra("remoteUrl");

        iv_pic = findViewById(R.id.iv_pic);
        ImageLoaderConfig config = null;
        if (config == null){
            config = new ImageLoaderConfig.Builder().
                    setAsBitmap(true).
                    setPlaceHolderResId(R.drawable.im_avatar).
                    setErrorResId(R.drawable.im_avatar).
                    setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE).
                    setPrioriy(ImageLoaderConfig.LoadPriority.HIGH).build();
        }


        if (!TextUtils.isEmpty(localUrl)) {
            File file = new File(localUrl);
            if (file.exists()) {
                ImageLoader.loadImage(iv_pic,localUrl, config, null);
            } else {
                if (!TextUtils.isEmpty(remoteUrl)) {
                    ImageLoader.loadImage(iv_pic,remoteUrl, config, null);
                }
            }
        } else if (!TextUtils.isEmpty(remoteUrl)) {
            ImageLoader.loadImage(iv_pic,remoteUrl, config, null);
        }


        iv_pic.setOnClickListener(v->{
            finish();
        });
    }


}
