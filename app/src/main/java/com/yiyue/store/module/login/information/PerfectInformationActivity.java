package com.yiyue.store.module.login.information;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityPerfectInformationBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.module.certification.information.BasicInformationActivity;
import com.yiyue.store.util.FilePathUtil;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.util.PhoneUtil;
import com.yiyue.store.util.compressutil.CompressPicUtil;
import com.yiyue.store.util.compressutil.OnCompressListener;
import com.yiyue.store.widget.bottomview.BottomViewFactory;
import com.yiyue.store.widget.bottomview.SelectPhotoView;
import com.yiyue.store.widget.bottomview.base.BaseBottomView;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * 完善资料
 * <p>
 * Created by lvlong on 2018/9/27.
 */
@CreatePresenter(PerfectInformationPresenter.class)
public class PerfectInformationActivity extends BaseMvpAppCompatActivity<IPerfectInformationView, PerfectInformationPresenter>
        implements ClickHandler , IPerfectInformationView {

    ActivityPerfectInformationBinding mBinding;

    private String headUrl = "";
    private ImageLoaderConfig config;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_perfect_information;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityPerfectInformationBinding) viewDataBinding;
        mBinding.setClick(this);
        // 修改状态栏字体颜色
        StatusBarUtil.setLightMode(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PhoneUtil.REQUESTCODE_SYS_CROP: // 裁剪
                    if (TextUtils.isEmpty(FilePathUtil.getCacheCrop() + "image_photo.jpg")) return;
                    getMvpPresenter().uploadImage(FilePathUtil.getCacheCrop() + "image_photo.jpg");
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_head_photo:    //上传头像
                new RxPermissions(this)
                        .request(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe( grant -> {
                            if (grant) {
                                openPhotoAlbum();
                            }
                        });
                break;

            case R.id.btn_ok:         //确定
                String loginPassword = mBinding.etLoginPassword.getText().toString().trim();
                String confirmPassword = mBinding.etConfirmPassword.getText().toString().trim();

                if (loginPassword.length() >= 6 && confirmPassword.length() >= 6){
                    if (loginPassword.equals(confirmPassword)){
                        if (!TextUtils.isEmpty(headUrl)&&headUrl!=""){
                            getMvpPresenter().doUserData(getSex(), headUrl,loginPassword,confirmPassword);
                        }else {
                            ToastUtils.shortToast("请上传头像");
                        }
                    }else {
                        ToastUtils.shortToast("密码不一致");
                    }
                }else {
                    ToastUtils.shortToast("密码至少6位");
                }

                break;
        }
    }

    /**
     * 打开相册
     */
    private void openPhotoAlbum() {
        Album.image(this) // Image selection.
                .singleChoice()
                .camera(true)
                .columnCount(3)
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        // 拿到用户选择的图片路径
                        PhoneUtil.toCrop(PerfectInformationActivity.this,getImageContentUri(result.get(0).getPath()) , FilePathUtil.getCacheCrop() + "image_photo.jpg");
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                    }
                })
                .start();
    }
    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(FormatUtil.Formatstring(message));
    }

    @Override
    public void onDoUserDataSuccess() {
        BasicInformationActivity.startActivity(this , BasicInformationActivity.class);
        AccountManager.getInstance().setHeadImg(headUrl);
        finish();
    }

    @Override
    public void onUploadFileSuccess(String filePath) {
        this.headUrl = filePath;
        if (config == null) {
            config = new ImageLoaderConfig.Builder().
                    setCropType(ImageLoaderConfig.CENTER_INSIDE).
                    setAsBitmap(true).
                    setCropCircle(true).
                    setPlaceHolderResId(R.drawable.icon_head_pic_def).
                    setErrorResId(R.drawable.icon_head_pic_def).
                    setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE).
                    setPrioriy(ImageLoaderConfig.LoadPriority.HIGH).build();
        }
        ImageLoader.loadImage(mBinding.ivHeadPhoto, filePath, config, null);
    }

    /**
     * 获取性别
     * @return 1男 2女 3人妖
     */
    private int getSex() {
        if (mBinding.rbMan.isChecked()) {
            return 1;
        }
        if (mBinding.rbWoman.isChecked()) {
            return 2;
        }
        return 3;
    }

    /**
     * 通过图片地址获取Uri
     * @param path
     * @return
     */
    private Uri getImageContentUri(String path){
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{path}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, ""+id);
        }else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, path);
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        }
    }
}
