package com.yiyue.store.module.certification;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.recycleview.GridSpacingItemDecoration;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityCertificationBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.requestbody.StoreAuthApplyRequestBody;
import com.yiyue.store.module.common.addimg.AddImageActivity;
import com.yiyue.store.module.main.MainActivity;
import com.yiyue.store.module.mine.info.UserInfoActivity;
import com.yiyue.store.util.BitmapUtils;
import com.yiyue.store.util.FilePathUtil;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.util.PhoneUtil;
import com.yiyue.store.util.compressutil.CompressPicUtil;
import com.yiyue.store.util.compressutil.OnCompressListener;
import com.yiyue.store.widget.bottomview.BottomViewFactory;
import com.yiyue.store.widget.bottomview.SelectPhotoView;
import com.yiyue.store.widget.bottomview.base.BaseBottomView;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 认证信息
 * <p>
 * Create by zm on 2019/10/22
 */
@CreatePresenter(CertificationPresenter.class)
public class CertificationActivity extends BaseMvpAppCompatActivity<ICertificationView, CertificationPresenter>
        implements ICertificationView, ClickHandler {
    private static final int REQUEST_CODE_IMAGE_ADD = 101;

    private ActivityCertificationBinding mBinding;

    private BaseBottomView mBaseBottomView;

    // 门店封面
    private String bannerPath;
    // 手持身份证路径
    private String handIDcardPath;
    // 门店环境照片
    private ArrayList<String> storephotosUrls = new ArrayList<>();
    // 营业执照
    private String businesslicensePath;
    // 门店环境照片适配器
    private ImageAdapter mStoreAdapter;

    public int imageType = -1; // 0营业执照 1门店封面 2手持身份证照 3门店照片

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_certification;
    }

    @Override
    protected void init() {
        mBinding = (ActivityCertificationBinding) viewDataBinding;
        mBinding.setClick(this);

        // 修改状态栏字体颜色
        StatusBarUtil.setLightMode(this);
        setRecyclerView();
    }

    private void setRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        mBinding.recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, 20, false));
        mStoreAdapter = new ImageAdapter(this);
        mBinding.recyclerView.setAdapter(mStoreAdapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PhoneUtil.REQUESTCODE_SYS_CAMERA: // 相机
                    if (mBaseBottomView == null) return;
                    String imagePath = ((SelectPhotoView) mBaseBottomView).getImagePath();
                    switch (imageType) {
                        case 0: // 营业执照
                            mBinding.ivUpdateLicense.setImageBitmap(
                                    BitmapUtils.decodeSampledBitmapFromFile(imagePath,
                                            mBinding.ivUpdateLicense.getWidth(), mBinding.ivUpdateLicense.getHeight()));
                            compressPicAndUpload(imagePath);
                            break;
                        case 1: // 门店封面
                            Uri uri = null;
                            if (mBaseBottomView instanceof SelectPhotoView) {
                                uri = ((SelectPhotoView) mBaseBottomView).getUri();
                            }
                            if (uri == null) {
                                return;
                            }
                            PhoneUtil.toCrop(CertificationActivity.this, uri, FilePathUtil.getCacheCrop() + "image_photo.jpg", 8, 5);
                            break;
                        case 2: // 手持证件照
                            mBinding.ivHandIdcardPhoto.setImageBitmap(
                                    BitmapUtils.decodeSampledBitmapFromFile(imagePath,
                                            mBinding.ivHandIdcardPhoto.getWidth(), mBinding.ivHandIdcardPhoto.getHeight()));
                            compressPicAndUpload(imagePath);
                            break;
                    }

                    break;

                case PhoneUtil.REQUESTCODE_SYS_PICK_IMAGE: // 图库
                    if (data == null || data.getData() == null) return;
                    switch (imageType) {
                        case 0: // 营业执照
                            mBinding.ivUpdateLicense.setImageBitmap(
                                    BitmapUtils.decodeSampledBitmapFromFile(FilePathUtil.getPath(data.getData()),
                                            mBinding.ivUpdateLicense.getWidth(), mBinding.ivUpdateLicense.getHeight()));
                            compressPicAndUpload(FilePathUtil.getPath(data.getData()));
                            break;
                        case 1: // 门店封面
                            PhoneUtil.toCrop(CertificationActivity.this, data.getData(), FilePathUtil.getCacheCrop() + "image_photo.jpg", 8, 5);
                            break;
                        case 2: // 手持证件照q
                            mBinding.ivHandIdcardPhoto.setImageBitmap(
                                    BitmapUtils.decodeSampledBitmapFromFile(FilePathUtil.getPath(data.getData()),
                                            mBinding.ivHandIdcardPhoto.getWidth(), mBinding.ivHandIdcardPhoto.getHeight()));
                            compressPicAndUpload(FilePathUtil.getPath(data.getData()));
                            break;
                    }

                    break;
                case REQUEST_CODE_IMAGE_ADD:
                    if (data == null) return;
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        storephotosUrls = bundle.getStringArrayList("data");
                        if (storephotosUrls != null) {
                            mStoreAdapter.setDatas(storephotosUrls, true);
                        }
                    }
                    break;

                case PhoneUtil.REQUESTCODE_SYS_CROP: // 裁剪
                    if (TextUtils.isEmpty(FilePathUtil.getCacheCrop() + "image_photo.jpg")) return;
                    mBinding.ivUpdateCover.setImageBitmap(
                            BitmapUtils.decodeSampledBitmapFromFile(FilePathUtil.getCacheCrop() + "image_photo.jpg",
                                    mBinding.ivUpdateCover.getWidth(), mBinding.ivUpdateCover.getHeight()));
                    compressPicAndUpload(FilePathUtil.getCacheCrop() + "image_photo.jpg");
                    break;
            }
        }
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(FormatUtil.Formatstring(message));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_update_license: //上传营业执照
                this.imageType = 0;
                if (mBaseBottomView == null) {
                    mBaseBottomView = BottomViewFactory.create(this, BottomViewFactory.Type.Avatar);
                }
                mBaseBottomView.showBottomView(true);
                break;

            case R.id.iv_update_cover: //上传门店封面
                this.imageType = 1;

                if (mBaseBottomView == null) {
                    mBaseBottomView = BottomViewFactory.create(this, BottomViewFactory.Type.Avatar);
                }
                mBaseBottomView.showBottomView(true);
                break;

            case R.id.view_add_store: //上传门店照片
                this.imageType = 3;
                AddImageActivity.startForResultActivity(CertificationActivity.this, REQUEST_CODE_IMAGE_ADD,
                        getString(R.string.image_select_title_store), 8, storephotosUrls);
                break;

            case R.id.iv_hand_idcard_photo: //上传手持身份照
                this.imageType = 2;

                if (mBaseBottomView == null) {
                    mBaseBottomView = BottomViewFactory.create(this, BottomViewFactory.Type.Avatar);
                }
                mBaseBottomView.showBottomView(true);
                break;

            case R.id.btn_next: //下一步
                StoreAuthApplyRequestBody requestBody
                        = new StoreAuthApplyRequestBody.Builder()
                        .storeId(AccountManager.getInstance().getStoreId())
                        .realname(mBinding.etRealName.getText().toString().trim())
                        .cardno(mBinding.etIdNumber.getText().toString().trim())
                        .licenseno(mBinding.etCertificateNumber.getText().toString())
                        .bannerPath(bannerPath)
                        .businesslicensePath(businesslicensePath)
                        .handIDcardPath(handIDcardPath)
                        .storephotosPaths(storephotosUrls)
                        .build();
                getMvpPresenter().submitCertiData(this, requestBody);
                break;
        }
    }

    @Override
    public void onSubmitCertiDataSuccess() {
        AccountManager.getInstance().setUserStatus(0);
        // 跳转至首页
        MainActivity.startActivity(this, MainActivity.class);
        finish();
    }

    @Override
    public void onUploadFileSuccess(List<String> filePaths) {
        switch (imageType) {
            case 0: // 营业执照
                businesslicensePath = filePaths.get(0);
                break;
            case 1: // 门店封面
                bannerPath = filePaths.get(0);
                break;
            case 2: // 手持身份证照
                handIDcardPath = filePaths.get(0);
                break;
            case 3: // 门店照片
                break;
        }
    }

    /**
     * 压缩后上传
     *
     * @param filePath
     */
    private void compressPicAndUpload(String filePath) {
        CompressPicUtil.with()
                .load(filePath)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // 压缩成功后调用，返回压缩后的图片文件
                        List<String> filePaths = new ArrayList<>();
                        filePaths.add(file.getPath());
                        getMvpPresenter().uploadImage(CertificationActivity.this, filePaths);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 当压缩过程出现问题时调用 上传原图
                        List<String> filePaths = new ArrayList<>();
                        filePaths.add(filePath);
                        getMvpPresenter().uploadImage(CertificationActivity.this, filePaths);
                    }
                }).launch();
    }
}
