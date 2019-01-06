package com.yiyue.store.module.im.redpacket;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.hyphenate.chat.EMMessage;
import com.jungly.gridpasswordview.GridPasswordView;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityRedPacketSendBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.CashInfoBean;
import com.yiyue.store.model.vo.bean.ChatAdapterItemTypeBean;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.SelfDefinedInfoBean;
import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;
import com.yiyue.store.model.vo.result.RedBagSendResult;
import com.yiyue.store.module.common.h5.WebActivity;
import com.yiyue.store.module.im.groups.creategroup.CreateGroupActivity;
import com.yiyue.store.module.im.redpacket.redrecords.RedRecordsActivity;
import com.yiyue.store.module.mine.settings.security.paypassword.forgetpwd.ForgetPayPasswordActivity;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.util.MathematicsUtils;
import com.yiyue.store.util.easyutils.EasyUtil;
import com.yiyue.store.widget.dialog.BaseEasyDialog;
import com.yiyue.store.widget.dialog.EasyDialog;
import com.yiyue.store.widget.dialog.ViewConvertListener;
import com.yiyue.store.widget.dialog.ViewHolder;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhangzz on 2018/11/6
 */
@CreatePresenter(RedPacketSendPresenter.class)
public class RedPacketSendActivity extends BaseMvpAppCompatActivity<RedPacketSendView, RedPacketSendPresenter> implements RedPacketSendView, View.OnClickListener {
    private ActivityRedPacketSendBinding mBinding;

    protected UserFriendsBean friendUserBean;//保存好友头像使用的本地查询的bean

    private String editContent;//备注
    private String money;//金额

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_red_packet_send;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityRedPacketSendBinding) viewDataBinding;
        mBinding.btnInsetMoney.setOnClickListener(this);
        mBinding.tvWatchHistory.setOnClickListener(this);
        mBinding.titleView.setRightTextClickListener(v->{
            WebActivity.startActivity(RedPacketSendActivity.this, Constants.WEB_REDPACKAT_EXOLAIN, "红包说明");
        });

        mBinding.edtInputMoney.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                int posDot = temp.indexOf(".");
                int index = mBinding.edtInputMoney.getSelectionStart();

                if (posDot < 0) {
                    if (temp.length() <= 5) {
                        return;
                    } else {
                        s.delete(index - 1, index);
                        return;
                    }
                }
                if (posDot > 5) {
                    s.delete(index - 1, index);
                    return;
                }
                if (temp.length() - posDot - 1 > 2) {
                    s.delete(index - 1, index);
                    return;
                }
            }
        });

        mBinding.titleView.setLeftClickListener(view -> finish());

        friendUserBean = (UserFriendsBean) getIntent().getSerializableExtra(Constants.EXTRA_FRIEND_USER_BEAN);
        getMvpPresenter().getCashInfo(this);
    }

    @Override
    public void requestSuccess(RedBagSendResult redBagSendResult) {
        if (friendUserBean != null && redBagSendResult != null && redBagSendResult.getData() != null) {
            SelfDefinedInfoBean chatNoFriendBean = new SelfDefinedInfoBean();
            chatNoFriendBean.setImusername(AccountManager.getInstance().getAccount().getImusername());
            chatNoFriendBean.setNickname(TextUtils.isEmpty(AccountManager.getInstance().getAccount().getNickname()) ? AccountManager.getInstance().getUsername() : AccountManager.getInstance().getAccount().getNickname());
            chatNoFriendBean.setPath(AccountManager.getInstance().getAccount().getHeadImg());

            String userId = AccountManager.getInstance().getUserId();
            if (!TextUtils.isEmpty(userId)) {
                chatNoFriendBean.setUserId(Long.parseLong(userId));
            }

            chatNoFriendBean.setToImusername(friendUserBean.getImusername());
            chatNoFriendBean.setToNickname(friendUserBean.getNickname());
            chatNoFriendBean.setToPath(friendUserBean.getPath());
            chatNoFriendBean.setToLabel(friendUserBean.getLabel());
            chatNoFriendBean.setToGender(friendUserBean.getGender());
            chatNoFriendBean.setToUserId(friendUserBean.getUserId());

            chatNoFriendBean.setRecvImusername(friendUserBean.getImusername());
            chatNoFriendBean.setRecvUserId(friendUserBean.getUserId());
            chatNoFriendBean.setRecvPath(friendUserBean.getPath());
            chatNoFriendBean.setRecvNickname(friendUserBean.getNickname());


            chatNoFriendBean.setDefinedMsgType(201);//红包
            chatNoFriendBean.setRedPacketStatus(1);//发送未领取
            chatNoFriendBean.setTradeId(redBagSendResult.getData().getId());//红包id设置

            chatNoFriendBean.setMoney(Double.parseDouble(money));
            chatNoFriendBean.setContent(TextUtils.isEmpty(editContent) ? getResources().getString(R.string.words_numal) : editContent);

            EMMessage message = EasyUtil.getEmManager().sendOrderAddMoneyMes(chatNoFriendBean, friendUserBean.getImusername());
            EventBus.getDefault().post(new EventBean.RedPacketMsgUpdate(0, chatNoFriendBean, message, ChatAdapterItemTypeBean.CHAT_SEND_RED_PACKET));
            finish();
        }
    }

    @Override
    public void checkPasswordSuccess() {
        getMvpPresenter().sendRedBagReq(friendUserBean.getUserId() + "", TextUtils.isEmpty(editContent) ? getResources().getString(R.string.words_numal) : editContent,
                AccountManager.getInstance().getUserId(), money, this);
    }


    @Override
    public void onGetCashInfoSuccess(CashInfoBean bean) {
        try {
            if (!TextUtils.isEmpty(bean.getBalance()) && !TextUtils.isEmpty(bean.getFreezeBalance())) {
                double mMoney = Double.parseDouble(bean.getBalance());
                double mMoney1 = Double.parseDouble(bean.getFreezeBalance());
                double mMoneyNow = MathematicsUtils.sub(mMoney, mMoney1);//可用余额
                mBinding.tvShowMoney.setText(FormatUtil.FormatDouble(mMoneyNow) + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_inset_money:
                money = mBinding.edtInputMoney.getText().toString();
                if (TextUtils.isEmpty(money)) {
                    showToast(getResources().getString(R.string.rp_cannot_null));
                    return;
                }
                editContent = mBinding.edtInputMessage.getText().toString();
                showPayDialog();
                break;
            case R.id.tv_watch_history:
                Intent intent = new Intent(this, RedRecordsActivity.class);
                intent.putExtra("flag", 1);//1红包记录  2转账记录
                startActivity(intent);
                break;
        }
    }


    /**
     * 支付密码弹窗
     */
    private void showPayDialog() {
        EasyDialog.init()
                .setLayoutId(R.layout.dialog_input_paypassword)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseEasyDialog dialog) {
                        holder.getView(R.id.ll_pay_cancle).setOnClickListener(v -> {
                            dialog.dismiss();
                        });
                        holder.getView(R.id.tv_pay_forget).setOnClickListener(v -> {
                            startActivity(RedPacketSendActivity.this, ForgetPayPasswordActivity.class);
                            dialog.dismiss();
                        });
                        GridPasswordView pswView = (GridPasswordView) holder.getView(R.id.pswView);
                        pswView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
                            @Override
                            public void onTextChanged(String psw) {
                            }

                            @Override
                            public void onInputFinish(String psw) {
                                //输入法取消
                                Log.e("showPayDialog", "结束-输入的密码为: " + psw.toString().trim() + "");
                                String pwd = psw.toString().trim();
                                getMvpPresenter().checkPayWord(pwd, RedPacketSendActivity.this);
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setPosition(Gravity.CENTER)
                .setMargin(0)
                .setOutCancel(true)
                .show(this.getSupportFragmentManager());
    }

}
