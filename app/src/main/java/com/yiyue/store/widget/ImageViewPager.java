package com.yiyue.store.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.yiyue.store.R;

import java.util.List;

/**
 * Created by zm on 2018/11/28.
 */

public class ImageViewPager extends RelativeLayout implements Runnable {

    private static final int DEFAULT_CHANGE_INTERVAL = 3000;
    private static final int DEFAULT_DOTS_PADDING = 10;
    private static final int DEFAULT_DOTS_HEIGHT = 40;
    private static final float DEFAULT_DOTS_BG_ALPHA = 0.5f;
    private static final int DEFAULT_DOTS_GRAVITY = Gravity.RIGHT;
    private static final int DEFAULT_PAGER_POSITION = 1000;
    private static final boolean DEFAULT_PAGER_AUTOCHANGE = false;
    private static final boolean DEFAULT_PAGER_RECYCLE = false;

    private ViewPager viewPager;
    private LinearLayout    viewDots;
    private ITouchPagerListener pagerClickListener;
    private ViewPager.OnPageChangeListener onPageChangeListener;

    /**
     * 页面个数
     */
    private int viewSize;
    /**
     * 记录上一个Page的位置
     */
    private int lastPagerPosition;
    /**
     * Page切换的间隔时间
     */
    private int changeInterval;
    /***/
    private boolean isRun = false;
    /**
     * 是否自动切换Pager
     */
    private boolean isAutoChange;
    /**
     * 防止MotionEvent.ACTION_UP 结束5秒内自动切换page
     */
    private boolean isReadyChange = true;
    /**
     * 是否无线循环
     */
    private boolean isRecycle;
    /**
     * 是否添加导航点
     */
    private boolean isAddDots;
    /**
     * 滑动到最后一个导航点时, 是否隐藏
     */
    private boolean isHideLastDots;

    private int dotsViewHeight;
    /**
     * 导航点 之间的左右间隔
     */
    private int dotsPadding;
    /**
     * 导航点 距父Layout左右两边的margin值
     */
    private int dotsMargin;
    /**
     * 导航点 选中背景图id
     */
    private int dotsFocusImageId;
    /**
     * 导航点 未选中背景图id
     */
    private int dotsBlurImageId;
    /**
     * 导航点 在水平方向的位置
     */
    private int dotsGravity;
    /**
     * 导航点 的背景图
     */
    private Drawable dotsBackground = null;
    /**
     * 导航点 背景图的透明度
     */
    private float dotsBgAlpha;

    private Context mContext;
    /**
     * 导航点 图标
     */
    private ImageView[] dotsImages;

    public ImageViewPager(Context context) {
        super(context);
        this.mContext = context;

        initDefaultValue();
    }

    public ImageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageViewPagerAttrs);
        try {
            isAddDots = a.getBoolean(R.styleable.ImageViewPagerAttrs_dotsIsAdd, false);
            if (true == isAddDots) {
                dotsViewHeight = a.getDimensionPixelSize(R.styleable.ImageViewPagerAttrs_dotsViewHeight, DEFAULT_DOTS_HEIGHT);
                dotsPadding = a.getDimensionPixelSize(R.styleable.ImageViewPagerAttrs_dotsSpacing, DEFAULT_DOTS_PADDING);
                dotsMargin = a.getDimensionPixelSize(R.styleable.ImageViewPagerAttrs_dotsMargin, 0);
                dotsFocusImageId = a.getResourceId(R.styleable.ImageViewPagerAttrs_dotsFocusImage, R.drawable.shape_guide_select);
                dotsBlurImageId = a.getResourceId(R.styleable.ImageViewPagerAttrs_dotsBlurImage, R.drawable.shape_guide_unselect);
                dotsBackground = a.getDrawable(R.styleable.ImageViewPagerAttrs_dotsBackground);
                dotsBgAlpha = a.getFloat(R.styleable.ImageViewPagerAttrs_dotsBgAlpha, DEFAULT_DOTS_BG_ALPHA);
                dotsGravity = a.getInt(R.styleable.ImageViewPagerAttrs_dotsGravity, DEFAULT_DOTS_GRAVITY);
            }
            changeInterval = a.getInt(R.styleable.ImageViewPagerAttrs_changeInterval, DEFAULT_CHANGE_INTERVAL);
            isAutoChange = a.getBoolean(R.styleable.ImageViewPagerAttrs_autoChange, DEFAULT_PAGER_AUTOCHANGE);
            isRecycle = a.getBoolean(R.styleable.ImageViewPagerAttrs_recycleble, DEFAULT_PAGER_RECYCLE);
        } finally {
            a.recycle();
        }
        initView();
    }

    private void initDefaultValue() {
        isAddDots = false;
        if (true == isAddDots) {
            dotsViewHeight = DEFAULT_DOTS_HEIGHT;
            dotsPadding = DEFAULT_DOTS_PADDING;
            dotsMargin = 0;
            dotsFocusImageId = R.drawable.shape_guide_select;
            dotsBlurImageId = R.drawable.shape_guide_unselect;
            dotsBgAlpha = DEFAULT_DOTS_BG_ALPHA;
            dotsGravity = DEFAULT_DOTS_GRAVITY;
        }

        changeInterval = DEFAULT_CHANGE_INTERVAL;
        isAutoChange = DEFAULT_PAGER_AUTOCHANGE;
        isRecycle = DEFAULT_PAGER_RECYCLE;
    }

    public void initView() {
        viewPager = new ViewPager(getContext());
        LayoutParams paramsPager = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(viewPager, paramsPager);
        if (isAddDots)
            initDots();
    }

    /**
     * 添加并设置 导航点layout
     */
    private void initDots() {
        viewDots = new LinearLayout(getContext());
        if (dotsBackground != null) {
            dotsBackground.setAlpha((int) (dotsBgAlpha * 255));
            viewDots.setBackground(dotsBackground);
        }
        viewDots.setGravity(dotsGravity | Gravity.CENTER_VERTICAL);
        viewDots.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams paramsDost = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        viewDots.setPadding(dotsMargin, 0, dotsMargin, 0);
        addView(viewDots, paramsDost);
    }

    /**
     * 填充ViewPager
     *
     * @param views
     */
    public void setViewPagerViews(List<View> views) {

        if (views == null || views.size() == 0) {
            return;
        }
        viewSize = views.size();
        if (viewSize <= 2) {
            //当View个数小于2时，去掉无限循环
            isRecycle = false;
        }

        lastPagerPosition = isRecycle ? DEFAULT_PAGER_POSITION * viewSize : 0;
        viewPager.setAdapter(new ViewPagerAdapter(views, isRecycle));
        viewPager.addOnPageChangeListener(pageChangeListener);
        viewPager.setOnTouchListener(pagerTouchListener);
        if (isAddDots && viewSize != 1) {
            addDots(viewSize);
        }
        viewPager.setCurrentItem(getCurrentItem());

        if (isAutoChange && viewSize > 1) {
            try {
                if(runThread == null) {
                    runThread = new Thread(this);
                    isRun = true;
                    runThread.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        isRun = false;
        if(runThread != null) {
            runThread.interrupt();
            runThread = null;
        }
    }

    Thread runThread;
    @Override
    public void run() {
        while (isRun) {
            try {
                Thread.sleep(changeInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (isAutoChange && isReadyChange) {
                int nextPosition = getCurrentItem() + 1;
                nextPosition = isRecycle ? nextPosition : nextPosition % viewSize;
                pageHandler.sendEmptyMessage(nextPosition);
            } else {
                isReadyChange = true;
            }
        }
    }

    Handler pageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(msg.what);
        }
    };

    public void setPagerClickListener(ITouchPagerListener pagerClickListener) {
        this.pagerClickListener = pagerClickListener;
    }

    /**
     * @param onPageChangeListener
     */
    public void setOnPagerChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    /**
     * 返回 当前选择的Item
     *
     * @return
     */
    public synchronized int getCurrentItem() {
        return lastPagerPosition;
    }

    /**
     * ViewPager 默认PageChange监听器
     */
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            switchToDot(arg0);
            if (onPageChangeListener != null)
                onPageChangeListener.onPageSelected(arg0);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            if (onPageChangeListener != null)
                onPageChangeListener.onPageScrolled(arg0, arg1, arg2);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            if (onPageChangeListener != null)
                onPageChangeListener.onPageScrollStateChanged(arg0);
        }
    };

    /**
     * ViewPager 默认Touch监听器
     */
    private OnTouchListener pagerTouchListener = new OnTouchListener() {

        private boolean isMoved = false;
        private final float DISTANCE_XY = 40;
        private final float DISTANCE = 60;

        float X = 0;
        float Y = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            try {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_POINTER_DOWN:
                    case MotionEvent.ACTION_DOWN:
                        isMoved = false;
                        X = event.getX();
                        Y = event.getY();
                        isAutoChange = false;
                        isReadyChange = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!isMoved) {
                            isMoved = isMovedXY(event);
                        }
                        isAutoChange = false;
                        isReadyChange = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!isMoved && pagerClickListener != null) {
                            if (!isMoved(event)) {
                                //没有移动,则为单机动作
                                pagerClickListener.onPageClick(getCurrentItem() % viewSize);
                            }
                            isMoved = false;
                        }
                        isAutoChange = true;
                        break;
                    default:
                        isAutoChange = true;
                        break;
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        }

        private boolean isMovedXY(MotionEvent event) {
            return Math.abs(X - event.getX()) > DISTANCE_XY || Math.abs(Y - event.getY()) > DISTANCE_XY;
        }

        private boolean isMoved(MotionEvent event) {
            float XD = Math.abs(X - event.getX());
            float YD = Math.abs(Y - event.getY());
            if (XD > DISTANCE_XY || YD > DISTANCE_XY) {
                return true;
            }
            if (Math.sqrt(XD * XD + YD * YD) > DISTANCE) {
                return true;
            }
            return false;
        }
    };

    /**
     * 更换选中的导航点图标
     *
     * @param index
     */
    private synchronized void switchToDot(int index) {
        if (isAddDots) {
            if (isHideLastDots) {
                if (showOrHideDots(lastPagerPosition, index, viewSize)) {
                    lastPagerPosition = index;
                    return;
                }
            }
            dotsImages[lastPagerPosition % viewSize].setImageResource(dotsBlurImageId);
            dotsImages[index % viewSize].setImageResource(dotsFocusImageId);

            lastPagerPosition = index;
        }

        lastPagerPosition = index;
        for(int i = 0 ; i < viewDots.getChildCount() ; i++) {
            if(i == (index % viewSize)) {
                ((ImageView)viewDots.getChildAt(i)).setImageResource(dotsFocusImageId);
            } else {
                ((ImageView)viewDots.getChildAt(i)).setImageResource(dotsBlurImageId);
            }
        }
    }

    private boolean showOrHideDots(int lastPosition, int selectPosition, int size) {
        if (selectPosition == size - 1) {
            //选中 最后一个
            ObjectAnimator.ofFloat(viewDots, "alpha", 1f, 0f).setDuration(400).start();
            return true;
        } else if (lastPosition == size - 1 && selectPosition == size - 2) {
            //从最后一个到倒数第二个
            ObjectAnimator.ofFloat(viewDots, "alpha", 0f, 1f).setDuration(400).start();
            return true;
        }
        return false;
    }

    /**
     * 添加导航圆点 到LinearLayout
     *
     * @param size
     */
    private void addDots(int size) {
        if (size == 1)
            return;

        dotsImages = new ImageView[size];
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(mContext);
            // 设置小圆点imageview的参数
            imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            imageView.setPadding(dotsPadding, 0, dotsPadding, 0);
            // 将小圆点layout添加到数组中
            dotsImages[i] = imageView;
            // 默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
            if (i == 0) {
                dotsImages[i].setImageResource(dotsFocusImageId);
            } else {
                dotsImages[i].setImageResource(dotsBlurImageId);
            }
            // 将imageviews添加到小圆点视图组
            viewDots.addView(dotsImages[i]);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child = this.getChildAt(0);
        child.layout(0, 0, getWidth(), getHeight());
        if (changed && isAddDots) {
            child = this.getChildAt(1);
            child.measure(r - l, dotsViewHeight);
            child.layout(0, getHeight() - (int) dotsViewHeight, getWidth(), getHeight());
        }
    }

    public int getChangeInterval() {
        return changeInterval;
    }

    public void setChangeInterval(int changeInterval) {
        this.changeInterval = changeInterval;
    }

    public boolean isAutoChange() {
        return isAutoChange;
    }

    public void setAutoChange(boolean isAutoChange) {
        this.isAutoChange = isAutoChange;
    }

    public boolean isRecycle() {
        return isRecycle;
    }

    public void setRecycle(boolean isRecycle) {
        this.isRecycle = isRecycle;
    }

    public boolean isAddDots() {
        return isAddDots;
    }

    public void setAddDots(boolean isAddDots) {
        this.isAddDots = isAddDots;
    }

    public int getDotsViewHeight() {
        return dotsViewHeight;
    }

    public void setDotsViewHeight(int dotsViewHeight) {
        this.dotsViewHeight = dotsViewHeight;
    }

    public int getDotsPadding() {
        return dotsPadding;
    }

    public void setDotsPadding(int dotsPadding) {
        this.dotsPadding = dotsPadding;
    }

    public int getDotsMargin() {
        return dotsMargin;
    }

    public void setDotsMargin(int dotsMargin) {
        this.dotsMargin = dotsMargin;
    }

    public int getDotsFocusImageId() {
        return dotsFocusImageId;
    }

    public void setDotsFocusImageId(int dotsFocusImageId) { this.dotsFocusImageId = dotsFocusImageId; }

    public int getDotsBlurImageId() {
        return dotsBlurImageId;
    }

    public void setDotsBlurImageId(int dotsBlurImageId) {
        this.dotsBlurImageId = dotsBlurImageId;
    }

    public int getDotsGravity() {
        return dotsGravity;
    }

    public void setDotsGravity(int dotsGravity) {
        this.dotsGravity = dotsGravity;
    }

    public Drawable getDotsBackground() { return dotsBackground; }

    public void setDotsBackground(Drawable dotsBackground) {
        this.dotsBackground = dotsBackground;
    }

    public float getDotsBgAlpha() {
        return dotsBgAlpha;
    }

    public void setDotsBgAlpha(float dotsBgAlpha) {
        this.dotsBgAlpha = dotsBgAlpha;
    }

    public boolean isHideLastDots() {
        return isHideLastDots;
    }

    public void setIsHideLastDots(boolean isHideLastDots) {
        this.isHideLastDots = isHideLastDots;
    }

    public void setViewDotsVisibility(int v) {
        if (viewDots != null) {
            viewDots.setVisibility(v);
        }
    }

    public interface ITouchPagerListener {

        void onPageClick(int position);
    }
}
