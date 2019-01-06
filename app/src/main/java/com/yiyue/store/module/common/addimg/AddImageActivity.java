package com.yiyue.store.module.common.addimg;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityAddImageBinding;
import com.yiyue.store.module.login.information.PerfectInformationActivity;
import com.yiyue.store.module.mine.info.UserInfoActivity;
import com.yiyue.store.util.FilePathUtil;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.util.PhoneUtil;
import com.yiyue.store.util.compressutil.CompressPicUtil;
import com.yiyue.store.util.compressutil.OnCompressListener;
import com.yiyue.store.widget.PhotoView.PhotoViewActivity;
import com.yiyue.store.widget.bottomview.SelectPhotoView;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 添加图片
 * <p>
 * Create by zm on 2018/10/23
 */
@CreatePresenter(AddImagePresenter.class)
public class AddImageActivity extends BaseMvpAppCompatActivity<IAddImageView, AddImagePresenter>
        implements ClickHandler, IAddImageView{
    private static final String EXTRAS_DATA = "extras_data";
    private static final String EXTRAS_NAME = "extras_name";
    private static final String EXTRAS_NUMBER = "extras_number";
    private ActivityAddImageBinding mBinding;
    private AddImageAdapter mAdapter;
    private ArrayList<String> images;
    private String name; // 图类型名
    private int number; //限制数量->多少张

    /**
     * @param activity
     * @param requestCode
     * @param title
     * @param number      限制数量->多少张
     * @param images
     */
    public static void startForResultActivity(Activity activity, int requestCode, String title, int number, ArrayList<String> images) {
        Intent intent = new Intent();
        intent.setClass(activity, AddImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(EXTRAS_DATA, images);
        bundle.putString(EXTRAS_NAME, title);
        bundle.putInt(EXTRAS_NUMBER, number);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_image;
    }

    @Override
    protected void init() {
        mBinding = (ActivityAddImageBinding) viewDataBinding;
        mBinding.setClick(this);
        hasExtras();
        initView();
    }

    private void hasExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            images = bundle.getStringArrayList(EXTRAS_DATA);
            name = bundle.getString(EXTRAS_NAME);
            number = bundle.getInt(EXTRAS_NUMBER);

            mBinding.tvTitle.setText(FormatUtil.Formatstring(name));
            mBinding.tvNumber.setText(String.format(getString(R.string.image_select_number), String.valueOf(number)));
        }
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        // titleview
        mBinding.titleView.setLeftClickListener(v -> finish());

        // recycleview
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mAdapter = new AddImageAdapter(this);
        mAdapter.setOnDeleteItemListener((view, position) -> {
            mAdapter.getDatas().remove(position);
            mAdapter.notifyDataSetChanged();
        });
        mBinding.recyclerView.setAdapter(mAdapter);
        if (images != null) {
            mAdapter.setDatas(images, true);
        }
        mAdapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(PhotoViewActivity.IMAGE_URL,mAdapter.getDatas());
                bundle.putInt(PhotoViewActivity.SHOW_POSITION,position);
                PhotoViewActivity.startActivity(AddImageActivity.this,PhotoViewActivity.class,bundle);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add: // 添加
                if (mAdapter.getDatas().size() == number) {
                    ToastUtils.shortToast("最多只能选择8张.");
                    return;
                }
                new RxPermissions(this)
                        .request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(grant -> {
                            if (grant) {
                                openPhotoAlbum();
                            }
                        });
                break;
            case R.id.btn_save: // 保存
                if (mAdapter.getDatas().size() == 0) {
                    ToastUtils.shortToast("请添加至少一张图片.");
                    return;
                }

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("data", mAdapter.getDatas());
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();
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
                         PhoneUtil.toCrop(AddImageActivity.this, getImageContentUri(result.get(0).getPath()), FilePathUtil.getCacheCrop() + "image_photo.jpg", 800, 500);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PhoneUtil.REQUESTCODE_SYS_CROP: // 裁剪
//                    compressPicAndUpload(FilePathUtil.getPath(data.getData()));
                    compressPicAndUpload(FilePathUtil.getCacheCrop() + "image_photo.jpg");
                    break;
            }
        }
    }

    /**
     * 压缩后上传
     *
     * @param
     */
    private void compressPicAndUpload(String path) {
        ArrayList<String> filePaths = new ArrayList<>();
        CompressPicUtil.with()
                    .load(path)
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                            // 压缩开始前调用，可以在方法内启动 loading UI
                        }
                        @Override
                        public void onSuccess(File file) {
                            // 压缩成功后调用，返回压缩后的图片文件
                            //压缩完添加入上传数组,size等于选择的就开始上传
                            filePaths.add(file.getPath());
                            getMvpPresenter().uploadImage(filePaths, AddImageActivity.this);
                        }

                        @Override
                        public void onError(Throwable e) {
                            // 当压缩过程出现问题时调用 上传原图
                            filePaths.add(path);
                            getMvpPresenter().uploadImage(filePaths, AddImageActivity.this);
                        }
                    }).launch();
    }

    @Override
    public void onUploadFileSuccess(List<String> filePath) {
        mAdapter.getDatas().addAll(filePath);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUploadFileFail() {

    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(FormatUtil.Formatstring(message));
    }

    /**
     * 通过图片地址获取Uri
     *
     * @param path
     * @return
     */
    private Uri getImageContentUri(String path) {
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{path}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, path);
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        }
    }

}
