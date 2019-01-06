package com.yiyue.store.module.im.chat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiyue.store.model.vo.bean.ChatMoreChooseBean;
import com.yiyue.store.R;


/**
 */
public class ChatMoreChooseAdapter extends BaseQuickAdapter<ChatMoreChooseBean, BaseViewHolder> {
    public ChatMoreChooseAdapter(int layoutResId) {
        super(layoutResId, null);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ChatMoreChooseBean bean) {
        if (bean != null) {
            baseViewHolder.setText(R.id.text, mContext.getString(bean.getName()));
            baseViewHolder.setBackgroundRes(R.id.image, bean.getResouseId());
        }
    }
}
