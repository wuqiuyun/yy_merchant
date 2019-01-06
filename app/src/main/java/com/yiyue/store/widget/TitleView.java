package com.yiyue.store.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import android.view.View;
import android.widget.LinearLayout;

import android.widget.ImageView;
import android.widget.TextView;

import com.yiyue.store.R;
import com.yiyue.store.databinding.ViewTitleBinding;
import com.yiyue.store.util.FormatUtil;

/**
 * 标题栏
 * <p>
 * Created by zm on 2018/9/26.
 */
public class TitleView extends LinearLayout {

    private ViewTitleBinding binding;

    private String subTitle; // 子标题
    private String leftText;
    private String rightText;
    private String titleText; // 标题
    private Drawable leftIcon;
    private Drawable rightIcon;
    private Drawable subRightIcon;
    private Drawable bgDrawable;
    private boolean isHome; // 是否首页
    @ColorInt
    private int titleTextColor;
    @ColorInt
    private int rightTextColor;
    @ColorInt
    private int bgColor;
    @ColorInt
    private int leftTextColor;

    public TitleView(@NonNull Context context) {
        this(context, null);
    }

    public TitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleView);
        subTitle = ta.getString(R.styleable.TitleView_title_text_sub);
        subRightIcon = ta.getDrawable(R.styleable.TitleView_right_icon_sub);
        rightText = ta.getString(R.styleable.TitleView_right_text);
        leftIcon = ta.getDrawable(R.styleable.TitleView_left_icon);
        rightIcon = ta.getDrawable(R.styleable.TitleView_right_icon);
        titleText = ta.getString(R.styleable.TitleView_title_text);
        isHome = ta.getBoolean(R.styleable.TitleView_isHome, false);
        titleTextColor = ta.getColor(R.styleable.TitleView_title_text_color, 0);
        rightTextColor = ta.getColor(R.styleable.TitleView_right_text_color, 0);
        bgColor = ta.getColor(R.styleable.TitleView_bg_color, 0);
        leftText = ta.getString(R.styleable.TitleView_left_text);
        leftTextColor = ta.getColor(R.styleable.TitleView_left_text_color, 0);
        bgDrawable = ta.getDrawable(R.styleable.TitleView_bg_drawable);
        ta.recycle();
        init();
    }

    private void init() {
       initView();
       initData();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = DataBindingUtil.inflate(inflater, R.layout.view_title, this, true);
        // 背景颜色
        if (bgColor != 0) {
            binding.viewItem.setBackgroundColor(bgColor);
        }

        if (bgDrawable !=null) {
            binding.viewItem.setBackground(bgDrawable);
        }

    }

    private void initData() {
        setHome(isHome);
        setLeftText(leftText);
        setLeftIcon(leftIcon);
        setRightText(rightText);
        setTitleText(titleText);
        setRightIcon(rightIcon);
        setSubRightIcon(subRightIcon);
        setSubTitleText(subTitle);
    }

    /**
     * @param leftText
     */
    private void setLeftText(String leftText) {
        if (!TextUtils.isEmpty(leftText))
            binding.btnLeft.setText(leftText);
        if (leftTextColor != 0)
            binding.btnLeft.setTextColor(leftTextColor);
    }

    /**
     * @param leftIcon
     */
    private void setLeftIcon(Drawable leftIcon) {
        if (null == leftIcon) return;
        leftIcon.setBounds(0, 0, leftIcon.getMinimumWidth(), leftIcon.getMinimumHeight());
        binding.btnLeft.setCompoundDrawables(leftIcon, null, null, null);
    }

    /**
     * @param rightText
     */
    public void setRightText(String rightText) {
        binding.tvRight.setText(FormatUtil.Formatstring(rightText));
        binding.tvRight.setVisibility(TextUtils.isEmpty(rightText) ? GONE : VISIBLE);
        if (rightTextColor != 0)
            binding.tvRight.setTextColor(rightTextColor);
    }

    /**
     * @param isVisib
     */
    public void setRightTextVisib(boolean isVisib) {
        if (isVisib) {
            binding.tvRight.setVisibility(View.VISIBLE);
        } else {
            binding.tvRight.setVisibility(View.GONE);
        }
    }

    /**
     * 设置标题
     * @param titleText
     */
    public void setTitleText(String titleText) {
        binding.tvTitle.setText(FormatUtil.Formatstring(titleText));
        if (titleTextColor != 0)
            binding.tvTitle.setTextColor(titleTextColor);
    }

    /**
     * 设置标题
     * @param subTitleText
     */
    public void setSubTitleText(String subTitleText) {
        binding.tvTitleSub.setText(FormatUtil.Formatstring(subTitleText));
    }

    /**
     *
     * @param rightIcon
     */
    public void setRightIcon(Drawable rightIcon) {
        if (rightIcon == null) return;
        binding.ivRight.setVisibility(VISIBLE);
        binding.ivRight.setImageDrawable(rightIcon);
    }
    /**
     * @param isVisib
     */
    public void setRightIconVisib(boolean isVisib) {
        if (isVisib) {
            binding.ivRight.setVisibility(View.VISIBLE);
        } else {
            binding.ivRight.setVisibility(View.GONE);
        }
    }
    /**
     *
     * @param subRightIcon
     */
    public void setSubRightIcon(Drawable subRightIcon) {
        if (subRightIcon == null) return;
        binding.ivRightSub.setVisibility(VISIBLE);
        binding.ivRightSub.setImageDrawable(subRightIcon);
    }

    /**
     *
     * @param isVisib
     */
    public void setSubRightIconVisib(boolean isVisib) {
        if (isVisib){
            binding.ivRightSub.setVisibility(View.GONE);
        }else {
            binding.ivRightSub.setVisibility(View.GONE);
        }
    }

    /**
     *
     */
    public ImageView getSubRightIcon() {
        return binding.ivRightSub;
    }

    public ImageView getRightIcon() {
        return binding.ivRight;
    }

    public TextView getRightText() {
        return binding.tvRight;
    }

    public TextView geTitleText() {
        return binding.tvTitle;
    }


    public void setHome(boolean home) {
        binding.btnLeft.setVisibility(home ? INVISIBLE : VISIBLE);
    }

    /**
     * 按钮事件
     * @param clickListener
     */
    public void setLeftClickListener(@Nullable OnClickListener clickListener) {
        binding.btnLeft.setOnClickListener(clickListener);
    }

    public void setRightTextClickListener(@Nullable OnClickListener clickListener) {
        binding.tvRight.setOnClickListener(clickListener);
    }

    public void setRightImgClickListener(@Nullable OnClickListener clickListener) {
        binding.ivRight.setOnClickListener(clickListener);
    }

    public void setSubRightImgClickListener(@Nullable OnClickListener clickListener) {
        binding.ivRightSub.setOnClickListener(clickListener);
    }

}
