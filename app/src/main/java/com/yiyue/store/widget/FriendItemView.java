package com.yiyue.store.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyue.store.R;

/**
 *
 * 、好友列表使用的item
 *
 * Created by zhangzz on 2018/10/12
 */
public class FriendItemView extends FrameLayout {
    private TextView mVwTvContent;//右侧按钮内容
    private TextView mVwTvMessage;//内容
    private TextView mVwName;//名称
    private ImageView mVwIvPic;//右侧图像
    private ImageView mVwAvatar;//左侧头像

    private String tvContent;
    private String tvMessage;
    private String tvName;
    private Drawable vwIvPic;

    public FriendItemView(@NonNull Context context) {
        super(context);
    }

    public FriendItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FriendItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FriendItemView);
        tvContent = typedArray.getString(R.styleable.FriendItemView_vw_tv_content);
        tvMessage = typedArray.getString(R.styleable.FriendItemView_vw_tv_message);
        tvName = typedArray.getString(R.styleable.FriendItemView_vw_name);
        vwIvPic = typedArray.getDrawable(R.styleable.FriendItemView_vw_iv_pic);
        typedArray.recycle();

        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_friend_item, this, true);
        mVwTvContent = findViewById(R.id.vw_tv_content);
        mVwTvMessage = findViewById(R.id.vw_tv_message);
        mVwName= findViewById(R.id.vw_name);
        mVwIvPic = findViewById(R.id.vw_iv_pic);
        mVwAvatar = findViewById(R.id.vw_avatar);

        if (TextUtils.isEmpty(tvContent)){
            mVwTvContent.setVisibility(GONE);
            mVwIvPic.setVisibility(VISIBLE);
            mVwIvPic.setImageDrawable(vwIvPic);
        } else if (mVwIvPic != null){
            mVwTvContent.setVisibility(VISIBLE);
            mVwIvPic.setVisibility(GONE);
            mVwTvContent.setText(tvContent);
        } else{
            mVwTvContent.setVisibility(GONE);
            mVwIvPic.setVisibility(GONE);
        }

        mVwTvMessage.setText(tvMessage);
        mVwName.setText(tvName);
    }

    public TextView getmVwTvContent() {
        return mVwTvContent;
    }

    public TextView getmVwTvMessage() {
        return mVwTvMessage;
    }

    public TextView getmVwName() {
        return mVwName;
    }

    public ImageView getmVwIvPic() {
        return mVwIvPic;
    }

    public ImageView getmVwAvatar() {
        return mVwAvatar;
    }
}
