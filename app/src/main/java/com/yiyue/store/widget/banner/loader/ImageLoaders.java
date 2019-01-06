package com.yiyue.store.widget.banner.loader;

import android.content.Context;
import android.widget.ImageView;


public abstract class ImageLoaders implements ImageLoaderInterface<ImageView> {

    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        return imageView;
    }

}
