package com.yiyue.store.module.im.joingroup;

import android.text.InputFilter;
import android.text.TextUtils;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityJoinGroupBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.UserBean;
import com.yiyue.store.model.vo.bean.daobean.GroupBean;
import com.yiyue.store.util.ETlengthFilter;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * 申请加入群页面
 * Created by zzz on 2018/10/12.
 */
@CreatePresenter(JoinGroupPresenter.class)
public class JoinGroupActivity extends BaseMvpAppCompatActivity<JoinGroupView, JoinGroupPresenter> implements JoinGroupView {
    private int maxEditLength = 20;//最大输入字数为20
    private ActivityJoinGroupBinding mBinding;

    private String mUesrId; //当前用户的id
    private String mImusername;//当前用户的环信用户名
    private String mNickname;//当前用户的昵称
    private String mPath;//当前用户的头像地址

    private long mGroupId; //当前群的id
    private long mGroupMasterId; //当前群老大的id
    private String mImgroup;//当前群组的编号
    private GroupBean mGroup;//当前群

    private String mRemark;//我的入党申请书

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_join_group;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityJoinGroupBinding) viewDataBinding;

        mBinding.giTvApplySend.setOnClickListener(v -> request(mUesrId, mNickname, mPath, mImusername, String.valueOf(mGroupId), String.valueOf(mGroupMasterId), mImgroup));

        mBinding.afEdit.setFilters(new InputFilter[]{new ETlengthFilter(maxEditLength)});

        initData();
    }

    private void initData() {
        UserBean user = AccountManager.getInstance().getAccount();
        mGroup = (GroupBean) getIntent().getSerializableExtra(Constants.EXTRA_GROUP);
        if (null != user) {
            mUesrId = user.getId();
            mImusername = user.getImusername();
            mNickname = user.getNickname();
            mPath = user.getHeadImg();//暂时搞不到头像路径
        }
        if (null != mGroup) {
            mGroupId = mGroup.getId();
            mGroupMasterId = mGroup.getUserId();
            mImgroup = mGroup.getImgroup();

            ImageLoaderConfig config = new ImageLoaderConfig.Builder().
                    setCropType(ImageLoaderConfig.CENTER_INSIDE).
                    setAsBitmap(true).
                    setCropCircle(true).
                    setPlaceHolderResId(R.drawable.im_avatar).
                    setErrorResId(R.drawable.im_avatar).
                    setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE).
                    setPrioriy(ImageLoaderConfig.LoadPriority.HIGH).build();
            ImageLoader.loadImage(mBinding.jgIvPhoto, mGroup.getPath(), config, null);

            mBinding.jgTvGroupName.setText(TextUtils.isEmpty(mGroup.getName())?"无":mGroup.getName());
            mBinding.jgTvGroupid.setText(TextUtils.isEmpty(mGroup.getId()+"")?"无":mGroup.getId()+"");
        }
    }

    /**
     * 发出申请
     */
    private void request(String userId, String nickname, String path, String imusername, String groupId, String friendId, String imgroup) {
        mRemark = mBinding.afEdit.getText().toString();
        getMvpPresenter().requestAddGroup(userId, nickname, path, imusername, groupId, friendId, mRemark, imgroup);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void requestSuccess() {
        if (mGroup != null) {
            showToast(getString(R.string.ag_apply_send_suc));
            EventBus.getDefault().post(new EventBean.GroupJoinOrOut(0, mGroup.getId()));
            finish();
        }
    }
}
