package com.yiyue.store.module.im.msgnotice.msgdetail;

import android.os.Bundle;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.databinding.ActivityMsgDetailBinding;
import com.yiyue.store.model.vo.bean.BulletinDetailBean;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.DateUtil;
import com.yl.core.util.StatusBarUtil;

/**
 * 公告详情
 */
@CreatePresenter(MsgDetailPresenter.class)
public class MsgDetailActivity extends BaseMvpAppCompatActivity<IMsgDetailView, MsgDetailPresenter> implements IMsgDetailView{

    private long id;//公告id
    private String content;//String类型的html
    ActivityMsgDetailBinding mBinding;
    
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_msg_detail;
    }

    @Override
    protected void init() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle){
            id = bundle.getLong("id");
        }

        StatusBarUtil.setLightMode(this);
        
        mBinding = (ActivityMsgDetailBinding) viewDataBinding;
        mBinding.titleView.setLeftClickListener(view -> finish());
        getMvpPresenter().getDetail(id);
    }

    @Override
    public void getDetailSuc(BulletinDetailBean detail) {
        mBinding.tvAuthor.setText(detail.getAuthor());
        mBinding.tvCreateTime.setText(DateUtil.getTime(detail.getCreatetime(), DateUtil.FORMAT_YMDHM));
        content = detail.getContent();
        content = content.replaceAll("\n", "<br>");//换行
        content = content.replaceAll("<img", "<img width=\"100%\"");//图片不超出屏幕
        mBinding.webView.getSettings().setJavaScriptEnabled(true);
        mBinding.webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
    }

    @Override
    public void getDetailFail() {

    }

    @Override
    public void showToast(String message) {

    }
}
