package com.yiyue.store.module.mine.pplarz.role;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiyue.store.R;
import com.yiyue.store.model.vo.bean.IncomeRecordBean;
import com.yiyue.store.widget.stickylistview.StickyHeaderAdapter;
import com.yl.core.util.DateUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zm on 2019/1/4.
 */
public class SectionAdapter extends StickyHeaderAdapter {
    public static String  FORMAT_YM = "yyyy年-MM月";

    private Context mContext;
    private List<IncomeRecordBean> entityRows;
    private LinkedHashMap<String, Double> map2 = new LinkedHashMap<>();
    private LinkedHashMap<String, List<IncomeRecordBean>> map = new LinkedHashMap<>();
    private String curYM = DateUtil.date2Str(System.currentTimeMillis(), FORMAT_YM);

    public SectionAdapter(Context mContext, List<IncomeRecordBean> entityRows) {
        this.mContext = mContext;
        this.entityRows = entityRows;
        addData(entityRows);
    }

    /**
     * 添加数据并进行分类
     * @param list
     */
    public void addData(List<IncomeRecordBean> list) {
        for (IncomeRecordBean bean : list) {
            String head = DateUtil.date2Str(bean.getTime(), FORMAT_YM);
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
        TextView tvTitle = view.findViewById(R.id.tv_title);
        if (key.equals(curYM)) {
            tvTitle.setText("本月");
        }else {
            tvTitle.setText(key.split("-")[1]);
        }
        ((TextView)view.findViewById(R.id.tv_content)).setText("获得收益¥" +map2.get(key));
        return view;
    }

    @Override
    public View getRowView(int section, int row, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_poplarz_income, null);
        Object[] keys = map.keySet().toArray();
        String key = (String) keys[section];
        final IncomeRecordBean item = map.get(key).get(row);
        ((TextView)view.findViewById(R.id.tv_time)).setText(DateUtil.date2Str(item.getTime(), DateUtil.FORMAT_MDHM));
        ((TextView)view.findViewById(R.id.tv_amount)).setText(item.getIncome() + "元");
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
}
