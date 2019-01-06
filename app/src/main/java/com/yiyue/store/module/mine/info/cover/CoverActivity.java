package com.yiyue.store.module.mine.info.cover;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityCoverPhotoBinding;
import com.yiyue.store.model.vo.result.GetStoreCenterInfoResult;
import com.yiyue.store.util.FilePathUtil;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.util.PhoneUtil;
import com.yiyue.store.widget.bottomview.BottomViewFactory;
import com.yiyue.store.widget.bottomview.SelectPhotoView;
import com.yiyue.store.widget.bottomview.base.BaseBottomView;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.util.List;

/**
 * 封面照
 */
@CreatePresenter(CoverPresenter.class)
public class CoverActivity extends BaseMvpAppCompatActivity<ICoverView, CoverPresenter> implements ICoverView, ClickHandler {
    private BaseBottomView mBaseBottomView;
    private ActivityCoverPhotoBinding mBinding;
    private ImageLoaderConfig config;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_cover_photo;
    }

    @Override
    protected void init() {


        initView();
    }

    private void initView() {
        mBinding = (ActivityCoverPhotoBinding) viewDataBinding;
        mBinding.setClick(this);
        // 返回
        mBinding.titleView.setLeftClickListener(v -> finish());
        // 修改状态栏字体颜色
        StatusBarUtil.setLightMode(this);
         config = new ImageLoaderConfig.Builder().
                setAsBitmap(true).
                setPlaceHolderResId(R.drawable.home_bg).
                setErrorResId(R.drawable.home_bg).
                setCropType(ImageLoaderConfig.FIT_CENTER).
                setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE).
                setPrioriy(ImageLoaderConfig.LoadPriority.HIGH).build();
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
                    if (uri == null) {
                        return;
                    }
                        PhoneUtil.toCrop(CoverActivity.this, uri, FilePathUtil.getCacheCrop() + "cover_photo.jpg", 8, 5);
                    break;

                case PhoneUtil.REQUESTCODE_SYS_PICK_IMAGE: // 图库
                        PhoneUtil.toCrop(CoverActivity.this, data.getData(), FilePathUtil.getCacheCrop() + "cover_photo.jpg", 8, 5);
                    break;
                case PhoneUtil.REQUESTCODE_SYS_CROP: // 裁剪
                    if (TextUtils.isEmpty(FilePathUtil.getCacheCrop() + "cover_photo.jpg")) return;
                    getMvpPresenter().uploadImage(FilePathUtil.getCacheCrop() + "cover_photo.jpg",this);
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_photo: // 修改封面照
                if (mBaseBottomView == null) {
                    mBaseBottomView = BottomViewFactory.create(this, BottomViewFactory.Type.Avatar);
                }
                mBaseBottomView.showBottomView(true);
                break;
        }

    }
    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(FormatUtil.Formatstring(message));
    }

    @Override
    public void onUploadFileSuccess(String filePath) {
        ImageLoader.loadImage(mBinding.ivPhoto,filePath,config,null);
        getMvpPresenter().updateCoverImg(filePath);
    }

    @Override
    public void onGetStoreCenterInfoSuccess(GetStoreCenterInfoResult.Data data) {
        if (data!=null&&data.getCoverImg()!=null&&data.getCoverImg().size()!=0)ImageLoader.loadImage(mBinding.ivPhoto,data.getCoverImg().get(0),config,null);
    }

    @Override
    public void onUploadStorePhotosSuccess(List<String> filePaths) {

    }

    @Override
    public void onUpdateCoverImgSuccess() {

    }

}
