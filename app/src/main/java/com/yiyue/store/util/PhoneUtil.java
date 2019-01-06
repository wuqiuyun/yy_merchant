package com.yiyue.store.util;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;

/**
 * 调用系统组件(相机、图库、电话等)
 * <p>
 * Created by zm on 2018/9/25.
 */
public class PhoneUtil {
    // 系统文件选择返回
    public static final int REQUESTCODE_SYS_PICK_IMAGE = 901;
    // 系统相机拍照返回
    public static final int REQUESTCODE_SYS_CAMERA = 902;
    // 裁剪
    public static final int REQUESTCODE_SYS_CROP = 903;

    /**
     * Don't let anyone instantiate this class.
     */
    private PhoneUtil() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * 调用系统发短信界面
     *
     * @param activity    Activity
     * @param phoneNumber 手机号码
     * @param smsContent  短信内容
     */
    public static void sendMessage(Context activity, String phoneNumber, String smsContent) {
        if (phoneNumber == null || phoneNumber.length() < 4) {
            return;
        }
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", smsContent);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(it);
    }

    /**
     * 调用拨打电话界面
     *
     * @param context     上下文
     * @param phoneNumber 电话号码
     */
    public static void toCallPhone(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNumber);
        intent.setData(data);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(intent);
    }

    /**
     * 调用拨号界面
     *
     * @param context 上下文
     * @param phone   电话号码
     */
    public static void call(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 相册
     *
     * @param context
     */
    public static void takePicFromGallery(Context context) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        ((Activity) context).startActivityForResult(photoPickerIntent, REQUESTCODE_SYS_PICK_IMAGE);
    }

    /**
     * 拍照
     *
     * @param context
     * @param imagePath
     * @return
     */
    public static Uri takePickByCamera(Context context, String imagePath) {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (TextUtils.isEmpty(imagePath)) {
                    return null;
                }
                File f = new File(imagePath);
                Uri mUri = UriHelper.fromFile(f);
                // 添加权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                ((Activity) context).startActivityForResult(intent, REQUESTCODE_SYS_CAMERA);
                return mUri;
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "没有找到储存目录", Toast.LENGTH_LONG).show();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Toast.makeText(context, "没有找到储存目录", Toast.LENGTH_LONG).show();
        }
        return null;
    }


    private static int aspectX = 1;
    private static int aspectY = 1;

    /**
     * 调用系统裁剪
     *
     * @param context
     * @param photoUri
     */
    public static Uri toCrop(Context context, Uri photoUri, String cropImagePath) {
        return toCrop(context, photoUri, cropImagePath, aspectX, aspectY);
    }


    /**
     * 调用系统裁剪
     *
     * @param context
     * @param photoUri
     * @param aspectX 裁剪比例
     * @param aspectY
     */
    public static Uri toCrop(Context context, Uri photoUri, String cropImagePath, int aspectX, int aspectY) {

        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(photoUri, "image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                Uri cropImageUri = Uri.parse("file://" + cropImagePath);
                intent.putExtra("crop", "true");
                intent.putExtra("scale", true);
                intent.putExtra("aspectX", aspectX);
                intent.putExtra("aspectY", aspectY);
//                intent.putExtra("outputX", 200);
//                intent.putExtra("outputY", 200);
                intent.putExtra("return-data", false);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImageUri);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                intent.putExtra("noFaceDetection", true);
                ((Activity) context).startActivityForResult(intent, REQUESTCODE_SYS_CROP);
                return cropImageUri;
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "没有找到储存目录", Toast.LENGTH_LONG).show();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Toast.makeText(context, "没有找到储存目录", Toast.LENGTH_LONG).show();
        }
        return null;
    }
}
