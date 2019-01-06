package com.yiyue.store.module.im.sysnotice;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yiyue.store.R;
import com.yiyue.store.model.vo.bean.SysNoticeBean;
import com.yiyue.store.model.vo.bean.daobean.SysMessageBean;
import com.yl.core.util.DateUtil;


/**
 */
public class SysNoticeAdapter extends BaseQuickAdapter<SysMessageBean, BaseViewHolder> {

    public SysNoticeAdapter(int layoutResId) {
        super(layoutResId, null);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SysMessageBean msg) {
        if (msg != null) {
            baseViewHolder.setText(R.id.tv_notice_title, msg.getTitle());
            baseViewHolder.setText(R.id.tv_notice_time, DateUtil.getTime(msg.getCreatetime(), DateUtil.FORMAT_HM));
            baseViewHolder.setText(R.id.tv_notice_desc, msg.getContent());
        }
    }
}
