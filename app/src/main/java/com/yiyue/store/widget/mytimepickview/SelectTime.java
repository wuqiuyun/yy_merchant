package com.yiyue.store.widget.mytimepickview;

/**
 * Created by Lizhuo on 2018/10/11.
 */
public class SelectTime
{
    private String startTime;
    private String endTime;

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public SelectTime(String startTime, String endTime)
    {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
