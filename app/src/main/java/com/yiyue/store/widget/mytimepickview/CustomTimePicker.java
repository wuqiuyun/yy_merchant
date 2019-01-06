package com.yiyue.store.widget.mytimepickview;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yiyue.store.R;
import com.yiyue.store.component.toast.ToastUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomTimePicker
{

    /**
     * 定义结果回调接口
     */
    public interface ResultHandler {
        void handle(SelectTime mSelectTime);
    }

    public enum SCROLL_TYPE {
        HOUR(1),
        MINUTE(2);

        SCROLL_TYPE(int value) {
            this.value = value;
        }

        public int value;
    }

    private int scrollUnits = SCROLL_TYPE.HOUR.value + SCROLL_TYPE.MINUTE.value;
    private ResultHandler handler;
    private Context context;

    private Dialog datePickerDialog;
    private MyTimePickerView startHourPv, startMinutePv, endHourPv, endMinutePv;

    private static final int MAX_MINUTE = 59;
    private static final int MAX_HOUR = 23;
    private static final int MIN_MINUTE = 0;
    private static final int MIN_HOUR = 0;
    

    private ArrayList<String> startHourArr, startMinArr, endHourArr, endMinArr;
    private String title;
    private Calendar startCalendar, endCalendar;
    private int startHour, startMin, endHour, endMin;
    private TextView tv_title, tv_cancle, tv_select;

    public CustomTimePicker(Context context, String title, String startTime, String endTime, ResultHandler resultHandler) {
        this.context = context;
        this.handler = resultHandler;
        this.title = title;
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        try {
            Date start = sdf.parse(startTime);
            Date ent = sdf.parse(endTime);
            startCalendar.setTime(sdf.parse(start.getHours()+":00"));
            endCalendar.setTime(sdf.parse(ent.getHours()+":00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        initDialog();
        initView();
    }

    /**
     * 重设Calendar时间
     * 
     * @param startTime
     * @param endTime
     */
    public void reSetTime(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        try {
            startCalendar.setTime(sdf.parse(startTime));
            endCalendar.setTime(sdf.parse(endTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void initDialog() {
        if (datePickerDialog == null) {
            datePickerDialog = new Dialog(context, R.style.TimePickerDialog);
            datePickerDialog.setCancelable(true);
            datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            datePickerDialog.setContentView(R.layout.view_custom_time_picker);
            Window window = datePickerDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = dm.widthPixels;
            window.setAttributes(lp);
        }
    }

    private void initView() {
        startHourPv = datePickerDialog.findViewById(R.id.hour_start_pv);
        startMinutePv = datePickerDialog.findViewById(R.id.minute_start_pv);
        endHourPv = datePickerDialog.findViewById(R.id.hour_end_pv);
        endMinutePv = datePickerDialog.findViewById(R.id.minute_end_pv);
        tv_title = datePickerDialog.findViewById(R.id.tv_time_pv_title);
        tv_cancle = datePickerDialog.findViewById(R.id.tv_time_pv_cancel);
        tv_select = datePickerDialog.findViewById(R.id.tv_time_pv_select);

        tv_title.setText(title);
        tv_cancle.setOnClickListener(view -> datePickerDialog.dismiss());

        tv_select.setOnClickListener(view ->
        {
            //判断结束时间不能小于开始时间
            if (!startCalendar.before(endCalendar)){
                ToastUtils.shortToast("结束时间不能小于开始时间");
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
            handler.handle(new SelectTime(sdf.format(startCalendar.getTime()),sdf.format(endCalendar.getTime())));
            datePickerDialog.dismiss();
        });
    }

    private void initTimer() {
        initArrayList();
        for (int i = MIN_HOUR; i <= MAX_HOUR; i++)
        {
            startHourArr.add(formatTimeUnit(i));
            endHourArr.add(formatTimeUnit(i));
        }

        startMinArr.add(formatTimeUnit(0));
        startMinArr.add(formatTimeUnit(30));
        endMinArr.add(formatTimeUnit(0));
        endMinArr.add(formatTimeUnit(30));
        
        loadComponent();
    }

    /**
     * 将“0-9”转换为“00-09”
     */
    private String formatTimeUnit(int unit) {
        return unit < 10 ? "0" + String.valueOf(unit) : String.valueOf(unit);
    }

    private void initArrayList() {
        if (startHourArr == null) startHourArr = new ArrayList<>();
        if (startMinArr == null) startMinArr = new ArrayList<>();
        if (endHourArr == null) endHourArr = new ArrayList<>();
        if (endMinArr == null) endMinArr = new ArrayList<>();
        startHourArr.clear();
        startMinArr.clear();
        endHourArr.clear();
        endMinArr.clear();
    }

    private void loadComponent() {
        startHourPv.setData(startHourArr);
        startHourPv.setSelected(0);
        
        startMinutePv.setData(startMinArr);
        startMinutePv.setSelected(0);
        
        endHourPv.setData(endHourArr);
        endHourPv.setSelected(0);
        
        endMinutePv.setData(endMinArr);
        endMinutePv.setSelected(0);
        executeScroll();
    }

    private void addListener() {
        startHourPv.setOnSelectListener(text ->
        {
            startCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(text));
            startHour = Integer.parseInt(text); //保存选择的小时
        });

        startMinutePv.setOnSelectListener(text ->
        {
            startCalendar.set(Calendar.MINUTE, Integer.parseInt(text));
            startMin = Integer.parseInt(text); //保存选择的分钟
        });

        endHourPv.setOnSelectListener(text ->
        {
            endCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(text));
            endHour = Integer.parseInt(text); //保存选择的小时
        });

        endMinutePv.setOnSelectListener(text ->
        {
            endCalendar.set(Calendar.MINUTE, Integer.parseInt(text));
            endMin = Integer.parseInt(text); //保存选择的分钟
        });
    }

    private void executeScroll() {
        startHourPv.setCanScroll(startHourArr.size() > 1 && (scrollUnits & SCROLL_TYPE.HOUR.value) == SCROLL_TYPE.HOUR.value);
        endHourPv.setCanScroll(startHourArr.size() > 1 && (scrollUnits & SCROLL_TYPE.HOUR.value) == SCROLL_TYPE.HOUR.value);
        startMinutePv.setCanScroll(startMinArr.size() > 1 && (scrollUnits & SCROLL_TYPE.MINUTE.value) == SCROLL_TYPE.MINUTE.value);
        endMinutePv.setCanScroll(startMinArr.size() > 1 && (scrollUnits & SCROLL_TYPE.MINUTE.value) == SCROLL_TYPE.MINUTE.value);
    }

    private int disScrollUnit(SCROLL_TYPE... scroll_types) {
        if (scroll_types == null || scroll_types.length == 0) {
            scrollUnits = SCROLL_TYPE.HOUR.value + SCROLL_TYPE.MINUTE.value;
        } else {
            for (SCROLL_TYPE scroll_type : scroll_types) {
                scrollUnits ^= scroll_type.value;
            }
        }
        return scrollUnits;
    }

    public void show() {
        initTimer();
        addListener();
        setSelectedTime();
        datePickerDialog.show();
    }

    /**
     * 设置日期控件是否可以循环滚动
     */
    public void setIsLoop(boolean isLoop) {
        this.startHourPv.setIsLoop(isLoop);
        this.startMinutePv.setIsLoop(isLoop);
        this.endHourPv.setIsLoop(isLoop);
        this.endMinutePv.setIsLoop(isLoop);
    }

    public void setStartHourIsLoop(boolean isLoop) {
        this.startHourPv.setIsLoop(isLoop);
    }

    public void setStartMinIsLoop(boolean isLoop) {
        this.startMinutePv.setIsLoop(isLoop);
    }

    public void setEndHourIsLoop(boolean isLoop) {
        this.endHourPv.setIsLoop(isLoop);
    }

    public void setEndMinIsLoop(boolean isLoop) {
        this.endMinutePv.setIsLoop(isLoop);
    }

    /**
     * 设置日期控件默认选中的时间
     */
    public void setSelectedTime() {
        startHourPv.setData(startHourArr);
        startHourPv.setSelected(formatTimeUnit(startCalendar.get(Calendar.HOUR_OF_DAY)));

        startMinutePv.setData(startMinArr);
        startMinutePv.setSelected(formatTimeUnit(startCalendar.get(Calendar.MINUTE)));
        
        endHourPv.setData(endHourArr);
        endHourPv.setSelected(formatTimeUnit(endCalendar.get(Calendar.HOUR_OF_DAY)));
        
        endMinutePv.setData(endMinArr);
        endMinutePv.setSelected(formatTimeUnit(endCalendar.get(Calendar.MINUTE)));
        
        executeScroll();
    }
}
