package com.yiyue.store.module.home;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.yiyue.store.R;
import com.yiyue.store.databinding.ViewHomeButtonBinding;
import com.yiyue.store.util.FormatUtil;

/**
 * Created by zm on 2018/9/26.
 */
public class HomeButtonView extends FrameLayout{

    private ViewHomeButtonBinding binding;

    private String label;
    @ColorInt
    private int labelColor;
    private Drawable icon;

    public HomeButtonView(@NonNull Context context) {
        this(context, null);
    }

    public HomeButtonView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeButtonView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HomeButtonView);
        label = ta.getString(R.styleable.HomeButtonView_label_title);
        labelColor = ta.getColor(R.styleable.HomeButtonView_label_color, 0);
        icon = ta.getDrawable(R.styleable.HomeButtonView_icon);
        ta.recycle();
        init();
    }

    private void init() {
        initView();
        initData();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = DataBindingUtil.inflate(inflater, R.layout.view_home_button, this, true);
    }

    private void initData() {
        setLabel(FormatUtil.Formatstring(label));
        setIcon(icon);
    }

    /**
     * 设置标题
     * @param label
     */
    public void setLabel(String label) {
        binding.tvLabel.setText(FormatUtil.Formatstring(label));
        if (labelColor != 0)
            binding.tvLabel.setTextColor(labelColor);
    }

    public void setIcon(Drawable drawable) {
        if (drawable != null) {
            binding.ivIcon.setImageDrawable(drawable);
            binding.ivIcon.setVisibility(VISIBLE);
        }

    }

    public void setNumber(int number) {
        binding.tvNumber.setText(number + "");
        binding.tvNumber.setVisibility(VISIBLE);
        binding.tvNumber.setTextColor(ContextCompat.getColor(getContext(),
                number == 0 ? R.color.color_343434 : R.color.color_FFA200));
    }
}
