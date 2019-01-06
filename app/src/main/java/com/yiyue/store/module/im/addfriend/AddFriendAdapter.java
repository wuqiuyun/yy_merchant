package com.yiyue.store.module.im.addfriend;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.component.image.glide.GlideImageLoaderStrategy;
import com.yiyue.store.R;
import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;


/**
 * Created by zhangzz on 2018/9/27
 */
public class AddFriendAdapter extends BaseQuickAdapter<UserFriendsBean, BaseViewHolder> {

    private ImageLoaderConfig config;
    
    public AddFriendAdapter(int layoutResId) {
        super(layoutResId, null);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, UserFriendsBean userFriendsBean) {
        if (userFriendsBean != null) {
            config = new ImageLoaderConfig.Builder().
                    setCropType(ImageLoaderConfig.CENTER_INSIDE).
                    setAsBitmap(true).
                    setCropCircle(true).
                    setPlaceHolderResId(R.drawable.im_avatar).
                    setErrorResId(R.drawable.im_avatar).
                    setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE).
                    setPrioriy(ImageLoaderConfig.LoadPriority.HIGH).build();

            baseViewHolder.setText(R.id.af_name, TextUtils.isEmpty(userFriendsBean.getRemarks()) ? userFriendsBean.getNickname() : userFriendsBean.getRemarks());
            baseViewHolder.setText(R.id.af_tv_message, TextUtils.isEmpty(userFriendsBean.getLabel()) ? "暂无标签" : userFriendsBean.getLabel());
            ImageLoader.loadImage(baseViewHolder.getView(R.id.af_avatar), userFriendsBean.getPath(), config, null);

            baseViewHolder.addOnClickListener(R.id.af_tv_addfriend);
        }
    }
}
