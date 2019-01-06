package com.yiyue.store.util;

import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.yiyue.store.BuildConfig;
import com.yiyue.store.YLApplication;

import java.io.File;

/**
 * Created by zm on 2018/10/18.
 */
public class UriHelper {

    public static Uri fromFile(File file){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(YLApplication.getContext(), BuildConfig.APPLICATION_ID + ".fileProvider", file);
        }else {
            return Uri.fromFile(file);
        }
    }
}
