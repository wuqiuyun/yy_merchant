package com.yiyue.store.module.mine.settings.feedback;

import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityFeedbackBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.bean.FeedBackBean;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.util.PhoneUtil;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

/**
 * Created by lvlong on 2018/10/8.
 *
 * 体验反馈
 *
 */
@CreatePresenter(FeedbackPresenter.class)
public class FeedbackActivity extends BaseMvpAppCompatActivity<IFeedbackView, FeedbackPresenter> implements ClickHandler , IFeedbackView {

    ActivityFeedbackBinding mBinding;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityFeedbackBinding) viewDataBinding;
        mBinding.setClick(this);

        //设置客服电话的下划线
        mBinding.tvServicePhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        setTitleView();
        //为EditText设置监听，注意监听类型为TextWatcher
        mBinding.etFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBinding.tvCurrentNum.setText(FormatUtil.Formatstring(s.length()+""));
                if (s.length()>0){
                    mBinding.tvEditHint.setVisibility(View.GONE);
                } else{
                    mBinding.tvEditHint.setVisibility(View.VISIBLE);
                }
                if (s.length()==200){
                    ToastUtils.shortToast("已达到最大内容限制!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getMvpPresenter().initAppFeedback(this);
    }

    private void setTitleView() {
        mBinding.titleView.setLeftClickListener(view -> finish());
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.tv_service_phone :        //客服电话
                PhoneUtil.call(this,mBinding.tvServicePhone.getText().toString());
                break;
            case R.id.tv_submit :               //提交
                String content = mBinding.etFeedback.getText().toString().trim();
                if (content.equals("")){
                }else {
//                    getMvpPresenter().submitFeedback(content, "1");
                    getMvpPresenter().submitFeedback(content, AccountManager.getInstance().getUserId(),this);
                }
                break;
        }

    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void submitSuccess() {
        ToastUtils.shortToast("提交成功,感谢您的宝贵意见!");
        finish();
    }

    @Override
    public void submitFail(String s) {
//        ToastUtils.shortToast("提交失败,请联系客服"+s);
    }

    @Override
    public void initSuccess(FeedBackBean feedBackBean) {
        if (feedBackBean!=null){
            mBinding.tvServicePhone.setText(feedBackBean.getTelephone());
            mBinding.tvEditHint.setHint(feedBackBean.getTip());
        }
    }
}
