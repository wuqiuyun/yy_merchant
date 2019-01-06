package com.yiyue.store.module.mine.info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityUserInfoBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.bean.CategoryBean;
import com.yiyue.store.model.vo.result.GetStoreCenterInfoResult;
import com.yiyue.store.module.common.addimg.AddImageActivity;
import com.yiyue.store.module.mine.info.cover.CoverActivity;
import com.yiyue.store.module.mine.info.location.UpdateLocationActivity;
import com.yiyue.store.module.mine.info.service.ServicesScopeActivity;
import com.yiyue.store.util.FilePathUtil;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.util.PhoneUtil;
import com.yiyue.store.widget.bottomview.BottomViewFactory;
import com.yiyue.store.widget.bottomview.SelectPhotoView;
import com.yiyue.store.widget.bottomview.base.BaseBottomView;
import com.yiyue.store.widget.dialog.YLDialog;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人资料
 */
@CreatePresenter(UserInfoPresenter.class)
public class UserInfoActivity extends BaseMvpAppCompatActivity<IUserInfoView, UserInfoPresenter> implements IUserInfoView, ClickHandler {
    private static final int REQUEST_CODE_IMAGE_ADD = 101;
    private static final int REQUEST_CODE_SERVICE_SELECT = 102;

    ActivityUserInfoBinding mBinding;

    private BaseBottomView mBaseBottomView;


    // 门店环境照片
    private ArrayList<String> storephotosUrls = new ArrayList<>();

    private GetStoreCenterInfoResult.Data mData;
    private ImageLoaderConfig config;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void init() {

        mBinding = (ActivityUserInfoBinding) viewDataBinding;
        mBinding.setClick(this);

        initView();
    }

    private void initView() {
        ActivityUserInfoBinding binding = (ActivityUserInfoBinding) viewDataBinding;
        // 返回
        binding.titleView.setLeftClickListener(v -> finish());
        // 修改状态栏字体颜色
        StatusBarUtil.setLightMode(this);

        config = new ImageLoaderConfig.Builder().
                setCropType(ImageLoaderConfig.CENTER_INSIDE).
                setAsBitmap(true).
                setCropCircle(true).
                setPlaceHolderResId(R.drawable.icon_head_pic_def).
                setErrorResId(R.drawable.icon_head_pic_def).
                setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE).
                setPrioriy(ImageLoaderConfig.LoadPriority.HIGH).build();
        ImageLoader.loadImage(mBinding.ivPhoto, AccountManager.getInstance().getAccount().getHeadImg(), config, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 获取店铺个人资料
        getMvpPresenter().getStoreCenterInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PhoneUtil.REQUESTCODE_SYS_CAMERA: // 相机
                    Uri uri = null;
                    if (mBaseBottomView instanceof SelectPhotoView) {
                        uri = ((SelectPhotoView) mBaseBottomView).getUri();
                    }
                    if (uri == null)return;
                        PhoneUtil.toCrop(UserInfoActivity.this, uri, FilePathUtil.getCacheCrop() + "image_photo.jpg");
                    break;

                case PhoneUtil.REQUESTCODE_SYS_PICK_IMAGE: // 图库
                        PhoneUtil.toCrop(UserInfoActivity.this, data.getData(), FilePathUtil.getCacheCrop() + "image_photo.jpg");
                    break;

                case PhoneUtil.REQUESTCODE_SYS_CROP: // 裁剪
                    if (TextUtils.isEmpty(FilePathUtil.getCacheCrop() + "image_photo.jpg")) return;
                        getMvpPresenter().uploadImage(FilePathUtil.getCacheCrop() + "image_photo.jpg");
                    break;

                case REQUEST_CODE_IMAGE_ADD:
                    if (data == null) return;
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        storephotosUrls = bundle.getStringArrayList("data");
                        getMvpPresenter().updateEnvironmentImg(storephotosUrls);
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.rl_head_photo: // 修改头像
                if (mBaseBottomView == null) {
                    mBaseBottomView = BottomViewFactory.create(this, BottomViewFactory.Type.Avatar);
                }
                mBaseBottomView.showBottomView(true);
                break;

            case R.id.rl_stores: // 修改门店名
                new YLDialog.Builder(this)
                        .setType(YLDialog.DIALOG_TYPE_EDITABLE)
                        .setTitle("修改门店名")
                        .setSubMessage("门店名：")
                        .setMessage("最多可填20字")
                        .setPositiveButton("确定", (dialog, which) -> {
                            String storeName = ((YLDialog) dialog).getInputText().getText().toString().trim();
                            if (TextUtils.isEmpty(storeName)) {
                                showToast("请输入门店名");
                                return;
                            }
                            if (storeName.length() > 20) {
                                showToast("最多可填20字");
                                return;
                            }
                            mBinding.tvStoreName.setText(storeName);
                            dialog.dismiss();
                            getMvpPresenter().updateStoreName(storeName);
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .create()
                        .show();
                break;

            case R.id.rl_location: // 修改地址

                UpdateLocationActivity.startActivity(this, UpdateLocationActivity.class);
                break;

            case R.id.rl_station: // 修改工位

                YLDialog ylDialog = new YLDialog.Builder(this)
                        .setType(YLDialog.DIALOG_TYPE_EDITABLE)
                        .setTitle("修改工位")
                        .setMessage("请输入工位")
                        .setSubMessage("工位：")
                        .setPositiveButton("确定", (dialog, which) -> {
                            String storeStation = ((YLDialog) dialog).getInputText().getText().toString().trim();
                            if (TextUtils.isEmpty(storeStation)) {
                                showToast("请输入工位数");
                                return;
                            }

                            if (storeStation.length() > 6) {
                                showToast("工位数最大为6位数");
                                return;
                            }
                            mBinding.tvStation.setText(storeStation);
                            dialog.dismiss();
                            getMvpPresenter().updateStation(storeStation);
                        })
                        .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                        .create();
                ylDialog.getInputText().setInputType(InputType.TYPE_CLASS_NUMBER);
                ylDialog.show();
                break;

            case R.id.rl_environment_photo: // 修改环境照
                if (mData != null) {
                    storephotosUrls = (ArrayList<String>) mData.getEnvironmentImg();
                }
                AddImageActivity.startForResultActivity(UserInfoActivity.this, REQUEST_CODE_IMAGE_ADD,
                        getString(R.string.image_select_title_store), 8, storephotosUrls);
                break;

            case R.id.rl_cover_photo: // 修改封面照
                startActivity(this, CoverActivity.class);
                break;

            case R.id.rl_service_scope: // 修改服务范围
                ServicesScopeActivity.startForResultActivity(this, REQUEST_CODE_SERVICE_SELECT,
                        mData == null ? null : (ArrayList<CategoryBean>) mData.getServices());
                break;
        }

    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(FormatUtil.Formatstring(message));
    }

    @Override
    public void onUploadFileSuccess(String filePath) {
            // 头像
            getMvpPresenter().updateHeadImg(filePath);
            ImageLoader.loadImage(mBinding.ivPhoto, filePath, config, null);
    }

    @Override
    public void onGetStoreCenterInfoSuccess(GetStoreCenterInfoResult.Data data) {
        this.mData = data;
        // 工位
        mBinding.tvStation.setText(String.format(getString(R.string.station_number), data.getStation()));
        // 门店名
        mBinding.tvStoreName.setText(FormatUtil.Formatstring(data.getStoreName()));
        // 地址
        mBinding.tvAddress.setText(FormatUtil.Formatstring(data.getShowLocation()));
        AccountManager.getInstance().setStoreName(FormatUtil.Formatstring(data.getStoreName()));
    }

    @Override
    public void onUploadStorePhotosSuccess(List<String> filePaths) {

    }

    @Override
    public void onUpdateCoverImgSuccess() {

    }

    @Override
    public void onUpdateEnvironmentImgSuccess() {

    }

    @Override
    public void onUpdateHeadImgSuccess() {

    }

    @Override
    public void onUpdateServicesSuccess() {

    }

    @Override
    public void onUpdateStationSuccess() {

    }

    @Override
    public void onUpdateStoreNameSuccess() {
        AccountManager.getInstance().setStoreName(mBinding.tvStoreName.getText().toString());
    }
}
