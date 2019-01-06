package com.yiyue.store.module.im.sysnotice;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiyue.store.model.vo.bean.BulletinBean;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.component.image.glide.GlideImageLoaderStrategy;
import com.yiyue.store.R;
import com.yiyue.store.model.vo.bean.MsgNoticeBean;
import com.yl.core.util.DateUtil;


/**
 */
public class MsgNoticeAdapter extends BaseQuickAdapter<BulletinBean, BaseViewHolder> {

    private boolean isRead; //true 已读 false 未读

    public MsgNoticeAdapter(int layoutResId) {
        super(layoutResId, null);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BulletinBean bean) {
        if (bean != null) {
            isRead = bean.getIsRead() != 0;

            ImageLoaderConfig config = new ImageLoaderConfig.Builder().
                    setCropType(ImageLoaderConfig.CENTER_INSIDE).
                    setAsBitmap(true).
                    setRoundedCorners(true).
                    setPlaceHolderResId(R.drawable.icon_head_pic_def).
                    setErrorResId(R.drawable.icon_head_pic_def).
                    setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE).
                    setPrioriy(ImageLoaderConfig.LoadPriority.HIGH).build();
            ImageLoader.loadImage(baseViewHolder.getView(R.id.avatar), bean.getPath(), config, null);

            baseViewHolder.setText(R.id.name, bean.getTitle());
            baseViewHolder.setText(R.id.time, DateUtil.getTime(bean.getCreatetime(), DateUtil.FORMAT_HM));
            baseViewHolder.setText(R.id.message, bean.getDesc());

            baseViewHolder.setGone(R.id.unread_msg_number, !isRead);
        }
    }
}
