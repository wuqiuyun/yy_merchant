package com.yiyue.store.module.mine.stylist.invitejoin;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;

import com.hyphenate.chat.EMMessage;
import com.jungly.gridpasswordview.GridPasswordView;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.greendao.DaoCallBackInterface;
import com.yiyue.store.databinding.ActivityInviteJoinBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.SelfDefinedInfoBean;
import com.yiyue.store.model.vo.bean.SendMsgBean;
import com.yiyue.store.model.vo.bean.daobean.ChatNoFriendBean;
import com.yiyue.store.model.vo.result.SendMsgResult;
import com.yiyue.store.model.vo.result.StoreInfoResult;
import com.yiyue.store.module.common.h5.WebActivity;
import com.yiyue.store.module.im.daoutil.ChatNoFriendDaoUtils;
import com.yiyue.store.module.im.daoutil.UserFriendDaoUtils;
import com.yiyue.store.module.im.redpacket.RedPacketSendActivity;
import com.yiyue.store.module.mine.settings.security.paypassword.forgetpwd.ForgetPayPasswordActivity;
import com.yiyue.store.util.easyutils.EasyUtil;
import com.yiyue.store.widget.dialog.BaseEasyDialog;
import com.yiyue.store.widget.dialog.EasyDialog;
import com.yiyue.store.widget.dialog.ViewConvertListener;
import com.yiyue.store.widget.dialog.ViewHolder;
import com.yiyue.store.widget.dialog.YLDialog;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

@CreatePresenter(InviteJoinPresenter.class)
public class InviteJoinActivity extends BaseMvpAppCompatActivity<IInviteJoinView, InviteJoinPresenter> implements IInviteJoinView {
    ActivityInviteJoinBinding mBinding;
    private SelfDefinedInfoBean toPersonBean;
    private ChatNoFriendDaoUtils chatNoFriendDaoUtils;
    private UserFriendDaoUtils userFriendDaoUtils;
    private String path;
    private String stylistId;//美发师id


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_invite_join;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        // 111.邀请平台美发师加入 112.邀请门店美发师加入  113平台美发师加入申请 114门店美发师加入申请
        mBinding = (ActivityInviteJoinBinding) viewDataBinding;
        chatNoFriendDaoUtils = new ChatNoFriendDaoUtils(this);
        userFriendDaoUtils = new UserFriendDaoUtils(this);

        mBinding.titleView.setRightTextClickListener(v->{
            WebActivity.startActivity(InviteJoinActivity.this, Constants.WEB_ENTER_EXOLAIN, "说明");
        });


        chatNoFriendDaoUtils.setOnIsertInterface(new DaoCallBackInterface.OnIsertInterface() {
            @Override
            public void onIsertInterface(boolean type) {
                if (type) {
                    DLog.d("发送保存成功");
                } else {
                    DLog.d("发送保存失败");
                }
            }
        });

        userFriendDaoUtils.setOnQuerySingleBackInterface(new DaoCallBackInterface.OnQuerySingleBackInterface() {

            @Override
            public void onQuerySingleBackInterface(Object entry, String id) {
                if (entry == null) {
                    chatNoFriendDaoUtils.queryWhereUser(toPersonBean.getImusername());
                }
            }
        });

        chatNoFriendDaoUtils.setOnQuerySingleBackInterface(new DaoCallBackInterface.OnQuerySingleBackInterface() {
            @Override
            public void onQuerySingleBackInterface(Object entry, String id) {
                if (entry == null) {
                    ChatNoFriendBean chatnobean = new ChatNoFriendBean();
                    chatnobean.setNickname(toPersonBean.getNickname());
                    chatnobean.setUserId(toPersonBean.getUserId());
                    chatnobean.setImusername(toPersonBean.getImusername());
                    chatnobean.setPath(toPersonBean.getPath());

                    chatNoFriendDaoUtils.insertUser(chatnobean);
                }
            }
        });


        mBinding.titleView.setLeftClickListener(v -> finish());
        toPersonBean = getIntent().getParcelableExtra(Constants.IM_SELF_BEAN);
        stylistId = getIntent().getStringExtra("stylistId");
        mBinding.tvPlatJoin.setOnClickListener(v -> {
            showTagDialog(111);

        });
        mBinding.tvSelfJoin.setOnClickListener(v -> {
            showTagDialog(112);
        });

        getMvpPresenter().getStylistCardList(AccountManager.getInstance().getStoreId(), this);
    }

    @Override
    public void sendMsg(SendMsgResult sendMsgResult, int type) {
        if (sendMsgResult != null) {
            SendMsgBean sendMsgBean = sendMsgResult.getData();
            if (sendMsgBean != null) {
                sendInviteMsg(sendMsgBean.getMsgId(), type);
                showDLDialog();
            }
        }
    }

    private void sendInviteMsg(String msgId, int type) {
        userFriendDaoUtils.queryWhereUser(toPersonBean.getImusername());

        SelfDefinedInfoBean chatNoFriendBean = new SelfDefinedInfoBean();
        chatNoFriendBean.setImusername(AccountManager.getInstance().getAccount().getImusername());
        chatNoFriendBean.setNickname(TextUtils.isEmpty(AccountManager.getInstance().getAccount().getNickname()) ? AccountManager.getInstance().getUsername() : AccountManager.getInstance().getAccount().getNickname());
        chatNoFriendBean.setPath(AccountManager.getInstance().getAccount().getHeadImg());
        chatNoFriendBean.setGender(AccountManager.getInstance().getAccount().getGender());
        String userId = AccountManager.getInstance().getUserId();
        if (!TextUtils.isEmpty(userId)) {
            chatNoFriendBean.setUserId(Long.parseLong(userId));
            chatNoFriendBean.setToUserId(Long.parseLong(userId));
        }

        chatNoFriendBean.setToImusername(AccountManager.getInstance().getAccount().getImusername());
        chatNoFriendBean.setToNickname(TextUtils.isEmpty(AccountManager.getInstance().getAccount().getNickname()) ? AccountManager.getInstance().getUsername() : AccountManager.getInstance().getAccount().getNickname());
        chatNoFriendBean.setToPath(AccountManager.getInstance().getAccount().getHeadImg());
        chatNoFriendBean.setToGender(AccountManager.getInstance().getAccount().getGender());
        //111.邀请平台美发师加入 112.邀请门店美发师加入
        // 113平台美发师加入申请 114门店美发师加入申请 只有这种时候 门店端才能点击操作
        chatNoFriendBean.setDetailId(AccountManager.getInstance().getAccount().getStoreId());
        chatNoFriendBean.setEnterRecordID(msgId);
        chatNoFriendBean.setDefinedMsgType(type);
        chatNoFriendBean.setMsgStatus(1);
        chatNoFriendBean.setAddress(path);//接口请求获取当前门店地址
        chatNoFriendBean.setContent("邀请美发师加入消息");

        if (toPersonBean != null) {
            chatNoFriendBean.setRecvImusername(toPersonBean.getImusername());
            chatNoFriendBean.setRecvNickname(toPersonBean.getNickname());
            chatNoFriendBean.setRecvPath(toPersonBean.getPath());
            chatNoFriendBean.setRecvUserId(toPersonBean.getUserId());


            EMMessage message = EasyUtil.getEmManager().sendOrderAddMoneyMes(chatNoFriendBean, toPersonBean.getImusername());
            EventBus.getDefault().post(new EventBean.ConversationRefreshEvent(2));
        }
    }


    @Override
    public void getStoreInforamtion(StoreInfoResult storeInfoResult) {
        if (storeInfoResult != null && storeInfoResult.getData() != null) {
            path = storeInfoResult.getData().getLocation();
        }

    }

    @Override
    public void showToast(String message) {

    }

    /**
     * 加入门店弹窗
     */
    private void showDLDialog() {
        new YLDialog.Builder(this)
                .setTitle("提示")
                .setType(YLDialog.DIALOG_TYPE_NORMAL)
                .setMessage("申请已发送给对方,请等待对方同意")
                .setPositiveButton("确定", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    /**
     * 入驻提示弹框确认
     */
    private void showTagDialog(int type) {
        EasyDialog.init()
                .setLayoutId(R.layout.dialog_join_store_desc)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseEasyDialog dialog) {
                        if (type == 111){
                            holder.setText(R.id.tv_join_title, R.string.im_platform_join);
                            holder.setText(R.id.tv_join_desc, R.string.im_platform_join_desc);
                            holder.setText(R.id.tv_join_money, R.string.im_platform_join_money);
                        } else {
                            holder.setText(R.id.tv_join_title, R.string.im_store_join);
                            holder.setText(R.id.tv_join_desc, R.string.im_store_join_desc);
                            holder.setText(R.id.tv_join_money, R.string.im_store_join_money);
                        }
                        holder.getView(R.id.tv_join_cancel).setOnClickListener(v -> {
                            dialog.dismiss();
                        });
                        holder.getView(R.id.tv_join_ok).setOnClickListener(v -> {
                            getMvpPresenter().sendMsg(AccountManager.getInstance().getStoreId(), stylistId, type, InviteJoinActivity.this);
                            dialog.dismiss();
                        });
                    }
                })
                .setPosition(Gravity.CENTER)
                .setMargin(30)
                .setOutCancel(true)
                .show(this.getSupportFragmentManager());
    }

}
