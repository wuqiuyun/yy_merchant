package com.yiyue.store.util;

import android.text.InputFilter;
import android.text.Spanned;

import com.yiyue.store.component.toast.ToastUtils;

/**
 * Created by zhangzz on 2018/10/12
 * EditText最多字数限制 构造传参字数
 */
public class ETlengthFilter implements InputFilter {

    private int mMaxLength;

    //最多字数
    public ETlengthFilter(int max) {
        mMaxLength = max;
    }

    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int keep = mMaxLength - (dest.length() - (dend - dstart));
        if (keep < (end - start)) {
            ToastUtils.shortToast("最多只能输入" + mMaxLength + "个字");
        }
        if (keep <= 0) {
            return "";
        } else if (keep >= end - start) {
            return null;
        } else {
            return source.subSequence(start, start + keep);
        }
    }
}

