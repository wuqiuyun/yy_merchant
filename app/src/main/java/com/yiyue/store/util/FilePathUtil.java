package com.yiyue.store.util;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.yiyue.store.YLApplication;

import java.io.File;

/**
 * 文件路径
 * <p>
 * Created by zm on 2018/9/10.
 */

public class FilePathUtil {

    private static String getBaseDir(String childDir) {
        return Environment.getExternalStorageDirectory().getPath() + "/yyl/" + childDir;
    }

    public static String getAppUpdatePath() {
        return checkAndMkdirs(getBaseDir("/AppUpdate"));
    }

    public static String getInvoicePdfDir() {
        return checkAndMkdirs(getBaseDir("cache/invoice/"));
    }

    public static String getCacheCrop() {
        return checkAndMkdirs(getBaseDir("cache/crop/"));
    }

    public static String getCacheImage() {
        return checkAndMkdirs(getBaseDir("cache/image/"));
    }


    public static String getCacheImagePick() {
        return checkAndMkdirs(getBaseDir("cache/image/")) + System.currentTimeMillis() + ".jpg";
    }

    public static String getCacheImageCrop() {
        return checkAndMkdirs(getBaseDir("cache/crop/")) + System.currentTimeMillis() + ".jpg";
    }

    public static String getCacheWeb() {
        return checkAndMkdirs(getBaseDir("cache/web/"));
    }

    public static String getCache() {
        return checkAndMkdirs(getBaseDir("cache/"));
    }

    public static String getImage() {
        return checkAndMkdirs(getBaseDir("image/"));
    }

    public static String getAdImage() {
        return checkAndMkdirs(getBaseDir("image/ad/"));
    }

    /**
     * 检查文件夹是否存在
     *
     * @param dir
     * @return
     */
    public static String checkAndMkdirs(String dir) {
        File file = new File(dir);
        if (file.exists() == false) {
            file.mkdirs();
        }
        return dir;
    }

    public static String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = YLApplication.getContext().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    /**
     * 图片压缩后的文件保存地址,清楚缓存时记得清理
     *
     * @return
     */
    public static String getCompressPath() {
        String path = Environment.getExternalStorageDirectory() + "/yiyue/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    public static int[] computeSize(File srcImg) {
        int[] size = new int[2];

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;

        BitmapFactory.decodeFile(srcImg.getAbsolutePath(), options);
        size[0] = options.outWidth;
        size[1] = options.outHeight;

        return size;
    }
}
