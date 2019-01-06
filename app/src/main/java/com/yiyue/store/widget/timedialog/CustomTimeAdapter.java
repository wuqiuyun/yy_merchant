package com.yiyue.store.widget.timedialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiyue.store.R;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.module.home.service.BusinessTimeBean;

/**
 * Created by Lizhuo on 2018/10/11.
 */
public class CustomTimeAdapter extends BaseRecycleViewAdapter<BusinessTimeBean>
{
    private Context context;
    private TextView day,time;

    public CustomTimeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimeViewHolder(LayoutInflater.from(context).inflate(R.layout.item_custom_time, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TimeViewHolder viewHolder = (TimeViewHolder) holder;
        day = viewHolder.itemView.findViewById(R.id.tv_dialog_time_day);
        time = viewHolder.itemView.findViewById(R.id.tv_dialog_time);
        
        BusinessTimeBean data = getDatas().get(position);
        String twoTime = data.getBeginTime() + "-" + data.getCloseTime();
        time.setText(twoTime);
        
        switch (position) {
            case 0 :
                day.setText(context.getString(R.string.monday));
                break;
            case 1 :
                day.setText(context.getString(R.string.tuesday));
                break;
            case 2 :
                day.setText(context.getString(R.string.wednesday));
                break;
            case 3 :
                day.setText(context.getString(R.string.thursday));
                break;
            case 4 :
                day.setText(context.getString(R.string.friday));
                break;
            case 5 :
                day.setText(context.getString(R.string.saturday));
                break;
            case 6 :
                day.setText(context.getString(R.string.sunday));
                break;
            default:
                day.setText(context.getString(R.string.monday));
                break;
        }
    }

    public class TimeViewHolder extends BaseViewHolder {

        public TimeViewHolder(View itemView) {
            super(itemView);
        }
    }
}
