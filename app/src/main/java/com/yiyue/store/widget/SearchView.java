package com.yiyue.store.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyue.store.R;

/**
 * Created by Lizhuo on 2018/10/15.
 */
public class SearchView extends LinearLayout
{
    /**
     * 返回键
     */
    private TextView tvLeft;
    
    /**
     * 输入框
     */
    private EditText etInput;

    /**
     * 删除键
     */
    private ImageView ivDelete;

    /**
     * 右侧键
     */
    private TextView tvRight;

    /**
     * 上下文对象
     */
    private Context mContext;

    
    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_search, this);
        initViews();
    }

    private void initViews() {
        tvLeft = findViewById(R.id.tv_left);
        tvRight = findViewById(R.id.tv_right);
        etInput = findViewById(R.id.search_et_input);
        ivDelete = findViewById(R.id.search_iv_delete);

        ivDelete.setOnClickListener(view -> {
            etInput.setText("");
            ivDelete.setVisibility(GONE);
        });

        etInput.addTextChangedListener(new EditChangedListener());
    }

    private class EditChangedListener implements TextWatcher
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!"".equals(charSequence.toString())) ivDelete.setVisibility(VISIBLE);
            else ivDelete.setVisibility(GONE);

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    /**
     * click事件
     * @param clickListener
     */
    public void setLeftClickListener(@Nullable OnClickListener clickListener) {
        tvLeft.setOnClickListener(clickListener);
    }

    public void setRightTextClickListener(@Nullable OnClickListener clickListener) {
        tvRight.setOnClickListener(clickListener);
    }

    /**
     * 设置搜索框提示
     */
    public void setHint(String hint) {
        if (!TextUtils.isEmpty(hint))
            etInput.setHint(hint);
    }

    /**
     * 设置左侧按钮文字
     */
    public void setTextLeft(String text) {
        if (!TextUtils.isEmpty(text)) 
            tvLeft.setText(text);
    }

    /**
     * 设置左侧可见
     */
    public void setVisibleLeft(boolean visible) {
        if (visible) tvLeft.setVisibility(View.VISIBLE);
        else tvLeft.setVisibility(View.GONE);
    }

    /**
     * 设置右侧按钮文字
     */
    public void setTextRight(String text) {
        if (!TextUtils.isEmpty(text))
            tvRight.setText(text);
    }

    /**
     * 设置右侧可见
     */
    public void setVisibleRight(boolean visible) {
        if (visible) tvRight.setVisibility(View.VISIBLE);
        else tvRight.setVisibility(View.GONE);
    }

    /**
     * 获取搜索框内容
     * @return
     */
    public String getSearchText() {
        return etInput.getText().toString();
    }




}
