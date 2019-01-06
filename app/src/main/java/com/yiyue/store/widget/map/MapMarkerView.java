package com.yiyue.store.widget.map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.yiyue.store.R;


/**
 * Created by zm on 2018/10/19.
 */

public class MapMarkerView extends LinearLayout {

    public MapMarkerView(Context context) {
        this(context, null);
    }

    public MapMarkerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapMarkerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mLayoutInflater.inflate(R.layout.view_mapmarker, this);
    }

    public void startAnim(Animation.AnimationListener animationListener) {
        clearAnimation();
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -30);
        translateAnimation.setDuration(500);
        translateAnimation.setRepeatCount(1);
        translateAnimation.setRepeatMode(translateAnimation.REVERSE);
        if(animationListener != null) {
            translateAnimation.setAnimationListener(animationListener);
        }
        setAnimation(translateAnimation);
        translateAnimation.startNow();
    }


}
