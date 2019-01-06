package com.yiyue.store.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yiyue.store.R;
import com.yiyue.store.databinding.ViewUserMenuBinding;
import com.yiyue.store.util.FormatUtil;

/**
 * Created by zm on 2018/9/28.
 */
public class UserMenuView extends FrameLayout {
    private ViewUserMenuBinding binding;

    private Drawable leftIcon;
    private Drawable rightIcon;
    private String titleText;
    private float titleTextSize;
    private String contentSubText;
    private float contextTextSubSize;
    private String contentText;
    private float contextTextSize;
    private String rightText;
    private float rightTextSize;
    private boolean rightClickable;
    @ColorInt
    private int titleTextColor;
    @ColorInt
    private int contentTextColor;
    @ColorInt
    private int contentTextSubColor;
    @ColorInt
    private int rightTextColor;

    public UserMenuView(@NonNull Context context) {
        this(context, null);
    }

    public UserMenuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserMenuView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.UserMenuView);
        leftIcon = ta.getDrawable(R.styleable.UserMenuView_left_icon);
        rightIcon = ta.getDrawable(R.styleable.UserMenuView_right_icon);
        rightClickable = ta.getBoolean(R.styleable.UserMenuView_right_icon_clickable, false);

        titleText = ta.getString(R.styleable.UserMenuView_title_text);
        titleTextSize = ta.getDimension(R.styleable.UserMenuView_title_text_size, -1.0f);
        titleTextColor = ta.getColor(R.styleable.UserMenuView_title_text_color, -1);

        contentSubText = ta.getString(R.styleable.UserMenuView_content_text_sub);
        contextTextSubSize = ta.getDimension(R.styleable.UserMenuView_content_text_sub_size, -1.0f);
        contentTextSubColor = ta.getColor(R.styleable.UserMenuView_content_text_sub_color, -1);

        contentText = ta.getString(R.styleable.UserMenuView_content_text);
        contextTextSize = ta.getDimension(R.styleable.UserMenuView_content_text_size, -1.0f);
        contentTextColor = ta.getColor(R.styleable.UserMenuView_content_text_color, -1);

        rightText = ta.getString(R.styleable.UserMenuView_right_text);
        rightTextSize = ta.getDimension(R.styleable.UserMenuView_right_text_size, -1.0f);
        rightTextColor = ta.getColor(R.styleable.UserMenuView_right_text_color, -1);
        ta.recycle();
        init();
    }

    private void init() {
        initView();
        setData();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = DataBindingUtil.inflate(inflater, R.layout.view_user_menu, this, true);
    }

    private void setData() {

        // 左边图标
        if (leftIcon != null) {
            binding.ivIcon.setImageDrawable(leftIcon);
            binding.ivIcon.setVisibility(VISIBLE);
        }

        // 右边图标
        if (rightIcon != null) {
            binding.ivRight.setImageDrawable(rightIcon);
            binding.ivRight.setVisibility(VISIBLE);
            binding.ivRight.setClickable(rightClickable);
        }

        setText(binding.tvTitle, titleText, titleTextColor, titleTextSize);
        setText(binding.tvContent, contentText, contentTextColor, contextTextSize);
        setText(binding.tvRight, rightText, rightTextColor, rightTextSize);
        setText(binding.tvContentSub, contentSubText, contentTextSubColor, contextTextSubSize);
    }

    /**
     *
     * @param titleText
     */
    public void setTitleText(String titleText) {
        binding.tvTitle.setText(FormatUtil.Formatstring(titleText));
    }

    /**
     * @param contentText
     */
    public void setContentText(String contentText) {
        binding.tvContent.setText(FormatUtil.Formatstring(contentText));
    }

    /**
     * @param subContentText
     */
    public void setSubContentText(String subContentText) {
        binding.tvContentSub.setText(FormatUtil.Formatstring(subContentText));
    }
    
    /**
     * @param rightText
     */
    public void setRightText(String rightText) {
        binding.tvRight.setText(FormatUtil.Formatstring(rightText));
    }

    public void setRightIcon(Drawable rightIcon) {
        if (rightIcon != null)
            binding.ivRight.setImageDrawable(rightIcon);
    }

    public void setIconRightClickListener(OnClickListener clickListener) {
        binding.ivRight.setOnClickListener(clickListener);
    }

    /**
     * @param textView
     * @param text
     * @param textColor
     * @param textSize
     */
    private void setText(TextView textView, String text, @ColorInt int textColor, float textSize) {
        if (textView == null) return;

        textView.setText(FormatUtil.Formatstring(text));
        if (textColor != -1)
            textView.setTextColor(textColor);
        if (textSize != -1.0f) {
            textView.setTextSize(textSize);
        }
    }
}
