package com.yiyue.store.module.im.groups.creategroup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.greendao.DaoCallBackInterface;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityCreateGroupBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.daobean.GroupBean;
import com.yiyue.store.module.common.h5.WebActivity;
import com.yiyue.store.module.im.daoutil.GroupDaoUtils;
import com.yiyue.store.module.im.groups.groupsearch.GroupSearchActivity;
import com.yiyue.store.util.FilePathUtil;
import com.yiyue.store.util.compressutil.CompressPicUtil;
import com.yiyue.store.util.compressutil.OnCompressListener;
import com.yl.core.component.image.glide.GlideImageLoaderStrategy;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 新建群组页面
 */
@CreatePresenter(CreateGroupPresenter.class)
public class CreateGroupActivity extends BaseMvpAppCompatActivity<CreateGroupView, CreateGroupPresenter> implements CreateGroupView {
    private static final String TAG = "CreateGroupActivity";

    ActivityCreateGroupBinding mBinding;
    private int REQUEST_CODE_CHOOSE = 1001;

    List<String> mSelected;

    private String mUserId;
    private String mImusername;

    private String mGroupName;//群名称
    private String mGroupAvatarPath;//群图标路径
    private String mGroupDescribe;//群介绍
    private GroupDaoUtils groupDaoUtils;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_create_group;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityCreateGroupBinding) viewDataBinding;
        mBinding.titleView.setLeftClickListener(view -> finish());
        mBinding.titleView.setRightTextClickListener(view -> startActivity(this, GroupSearchActivity.class));
        groupDaoUtils = new GroupDaoUtils(this);
        groupDaoUtils.setOnIsertInterface(new DaoCallBackInterface.OnIsertInterface() {
            @Override
            public void onIsertInterface(boolean type) {
                showToast("创建群成功！");
            }
        });
        initView();
        initData();
    }

    private void initView() {
        mBinding.cgIvAddHead.setOnClickListener(v -> {
            new RxPermissions(this)
                    .request(Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    )
                    .subscribe(granted -> {
                        if (granted) { //相册选择照片的方法：
                            Matisse.from(CreateGroupActivity.this)
                                    .choose(MimeType.ofImage()) // 选择 mime 的类型
                                    .countable(true)
                                    .capture(true)  //是否可以拍照
                                    .captureStrategy(//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                                            new CaptureStrategy(true, "com.yiyue.store.fileProvider"))
                                    .maxSelectable(1) // 图片选择的最多数量
                                    .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.dp_120)) //设置列宽
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                    .thumbnailScale(0.85f) // 缩略图的比例
                                    .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                                    .forResult(REQUEST_CODE_CHOOSE);
                        } else {
                            ToastUtils.shortToast(getString(R.string.permissions_phone_state));
                        }
                    });

        });

        mBinding.cgTvCreateGroup.setOnClickListener(view -> {
            mGroupName = mBinding.cgEtName.getText().toString();
            mGroupDescribe = "";
            getMvpPresenter().createGroup(mGroupName, mGroupDescribe, mGroupAvatarPath, mImusername, mUserId);
        });

        mBinding.cgTrServeDeclare.setOnClickListener(v -> {
            WebActivity.startActivity(CreateGroupActivity.this, Constants.WEB_GROUP_SERVICE_EXOLAIN, "群组服务声明");
        });
    }

    private void initData() {
        mUserId = AccountManager.getInstance().getUserId();
        mImusername = AccountManager.getInstance().getAccount().getImusername();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            mSelected = Matisse.obtainPathResult(data);
            if (mSelected != null && mSelected.size() > 0) {
                String urlPath = mSelected.get(0);

                compressPicAndUpload(urlPath);
                DLog.d(TAG, "urlPath: " + urlPath);
            }
        }
    }

    /**
     * 压缩后上传
     *
     * @param urlPath
     */
    private void compressPicAndUpload(String urlPath) {
        CompressPicUtil.with()
                .load(urlPath)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // 压缩成功后调用，返回压缩后的图片文件
                        if (file != null) {
                            uploadPic(file.getPath());
                            int[] thumbSize = FilePathUtil.computeSize(file);
                            DLog.d(TAG, String.format(Locale.CHINA, "压缩后参数：%d*%d, %dk", thumbSize[0], thumbSize[1], file.length() >> 10));
                        } else {
                            uploadPic(urlPath);//上传原图
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 当压缩过程出现问题时调用 上传原图
                        uploadPic(urlPath);
                    }
                }).launch();
    }

    private void uploadPic(String urlPath) {
        List<String> filePaths = new ArrayList<>();
        filePaths.add(urlPath);
        getMvpPresenter().uploadImage(CreateGroupActivity.this, filePaths);
        GlideImageLoaderStrategy.getInstance().loadImage(mBinding.cgIvAddHead, urlPath);
    }

    @Override
    public void onSuccess(GroupBean groupbean) {
        try {
            groupDaoUtils.insertUser(groupbean);
            EventBus.getDefault().post(new EventBean.GroupChangeEvent(Constants.EVENT_GROUP_CHANGE));
        } catch (Exception e) {
            DLog.e(TAG, e.getMessage());
        }
        finish();
    }

    @Override
    public void onUploadFileSuccess(List<String> filePaths) {
        if (filePaths != null && filePaths.size() > 0) {
            mGroupAvatarPath = filePaths.get(0);
        }
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (groupDaoUtils != null) {
            groupDaoUtils.closeConnection();
        }
    }
}
