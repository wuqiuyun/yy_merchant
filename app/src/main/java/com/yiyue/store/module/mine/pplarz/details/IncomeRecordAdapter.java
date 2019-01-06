package com.yiyue.store.module.mine.pplarz.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiyue.store.R;
import com.yiyue.store.model.vo.bean.IncomeRecordBean;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.widget.stickylistview.StickyHeaderAdapter;
import com.yl.core.util.DateUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zm on 2019/1/4.
 */
public class IncomeRecordAdapter extends StickyHeaderAdapter {

    private Context mContext;
    private static AssetListener assetListener;
    private List<IncomeRecordBean> entityRows;
    private LinkedHashMap<String, Double> map2 = new LinkedHashMap<>();
    private LinkedHashMap<String, List<IncomeRecordBean>> map = new LinkedHashMap<>();
    private String curYM = DateUtil.date2Str(System.currentTimeMillis(), DateUtil.FORMAT_YM_CN);

    public IncomeRecordAdapter(Context mContext, List<IncomeRecordBean> entityRows) {
        this.mContext = mContext;
        this.entityRows = entityRows;
        addData(entityRows);
    }

    public void setAssetListener(AssetListener assetListener) {
        IncomeRecordAdapter.assetListener = assetListener;
    }

    /**
     * 清除数据
     */
    public void clear() {
        map.clear();
        map2.clear();
    }

    /**
     * 设置头部标题
     * @param date
     */
    public void setMapData(String date) {
        map.put(date, new ArrayList<>());
        map2.put(date, 0.00);
        notifyDataSetChanged();
    }

    /**
     * 添加数据并进行分类
     * @param list
     */
    public void addData(List<IncomeRecordBean> list) {
        for (IncomeRecordBean bean : list) {
            String head = DateUtil.date2Str(bean.getTime(), DateUtil.FORMAT_YM_CN);
            if (map.get(head) == null) {
                List<IncomeRecordBean> newBean = new ArrayList<>();
                newBean.add(bean);
                map.put(head, newBean);
            }else {
                List<IncomeRecordBean> newBean = map.get(head);
                newBean.add(bean);
            }
            // 月收入
            if (map2.get(head) == null) {
                map2.put(head, bean.getMonthincome());
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int sectionCounts() {
        return map.keySet().toArray().length;
    }

    @Override
    public int rowCounts(int section) {
        if (section < 0)
            return 0;
        Object[] key = map.keySet().toArray();
        return map.get(key[section]).size();
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_income_title, null);
        Object[] keys = map.keySet().toArray();
        String key = (String) keys[section];
        TextView tvMonth = view.findViewById(R.id.tv_month);
        tvMonth.setVisibility(View.VISIBLE);
        if (curYM.equals(key)){
            tvMonth.setText("本月");
        }else {
            tvMonth.setText(key);
        }
        tvMonth.setOnClickListener(v -> {
            if (assetListener != null)
                assetListener.onClickTitle(key);
        });
        ((TextView)view.findViewById(R.id.tv_content)).setText("获得收益¥" +map2.get(key));
        return view;
    }

    @Override
    public View getRowView(int section, int row, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_income_role, null);
        Object[] keys = map.keySet().toArray();
        String key = (String) keys[section];
        final IncomeRecordBean item = map.get(key).get(row);
        ((TextView)view.findViewById(R.id.tv_time)).setText(DateUtil.date2Str(item.getTime(), DateUtil.FORMAT_MDHM));
        ((TextView)view.findViewById(R.id.tv_income)).setText(item.getIncome() + "元");
        ((TextView)view.findViewById(R.id.tv_name)).setText(FormatUtil.Formatstring(item.getName()));
        return view;
    }

    @Override
    public Object getRowItem(int section, int row) {
        if (section < 0)
            return null;
        Object[] key = map.keySet().toArray();
        return map.get(key[section]).get(row);
    }

    @Override
    public boolean hasSectionHeaderView(int section) {
        return true;
    }

    public interface AssetListener {
        void onClickTitle(String date);
    }
}
