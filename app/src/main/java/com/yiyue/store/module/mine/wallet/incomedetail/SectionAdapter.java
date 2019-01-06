package com.yiyue.store.module.mine.wallet.incomedetail;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyue.store.R;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.bean.IncomeBean;
import com.yiyue.store.widget.stickylistview.StickyHeaderAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by wqy on 2018/12/29.
 */


public class SectionAdapter extends StickyHeaderAdapter {
    private static final int TYPE_ASSET = 0;
    private static final int TYPE_COIN = 1;
    private final boolean isNotData = false;
    private int type;
    private Context mContext;
    private LinkedHashMap<String, List<IncomeBean>> map;

    private static AssetListener assetListener;
    private static AssetListener coinListener;

    public static void setListener(AssetListener listener) {
       assetListener = listener;
    }

    public static void setCoinListener(AssetListener coinli) {
        coinListener = coinli;
    }

    public interface AssetListener {
        void onClickTitle(String date);

        void onClickChild(IncomeBean incomeBean);
    }

    public SectionAdapter(Context context, List<IncomeBean> entityRows, int type) {
        this.mContext = context;
        this.type = type;

        setData(entityRows, isNotData);
    }

    /**
     * 添加数据，并进行分类
     *
     * @param list
     */
    public void setData(List<IncomeBean> list, boolean isNotData) {

        map = new LinkedHashMap<String, List<IncomeBean>>();
        for (IncomeBean row : list) {
            String head = "";
            if (!TextUtils.isEmpty(row.getCreatetime())) {
                String time = row.getCreatetime().substring(0, 7);
                time.replace("-", "年");
                head = time + "月";
            }

            if (map.get(head) == null) {
                List<IncomeBean> newRows = new ArrayList<>();
                newRows.add(row);
                if (isNotData) {
                    map.put(head, null);
                } else {
                    map.put(head, newRows);
                }
            } else {
                List<IncomeBean> newRows = map.get(head);
                newRows.add(row);
            }
        }

        notifyDataSetChanged();
    }

    // 标题项size
    @Override
    public int sectionCounts() {
        return map.keySet().toArray().length;
    }

    // 内容项size
    @Override
    public int rowCounts(int section) {
        if (section < 0) {
            return 0;
        }
        Object[] key = map.keySet().toArray();
        if (map.get(key[section]) == null) {
            return 0;
        }
        return map.get(key[section]).size();
    }

    @Override
    public View getRowView(int section, final int row, View convertView, ViewGroup parent) {
        //View view = LayoutInflater.from(mContext).inflate(R.layout.item_asset_child, null);
        View view = null;
        ViewContentHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_asset_child, parent, false);
            viewHolder = new ViewContentHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewContentHolder) view.getTag();
        }

        Object[] keys = map.keySet().toArray();
        String key = (String) keys[section];
        final IncomeBean bean = map.get(key).get(row);

        if (type == TYPE_ASSET) {//余额
            if (bean.getStatus() == 0) { // status 0:减少 1：增加
                viewHolder.tv_amount.setText("-" + bean.getChangebalance());
                viewHolder.tv_amount.setTextColor(mContext.getResources().getColor(R.color.color_green));
                viewHolder.iv_pic.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_deduction_dis));
            } else {
                viewHolder.tv_amount.setText("+" + bean.getChangebalance());
                viewHolder.tv_amount.setTextColor(mContext.getResources().getColor(R.color.color_red));
                viewHolder.iv_pic.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_income_dis));
            }
        } else {//积分
            //如果toUserId等于你上传的userId，那就是收入。
            if (AccountManager.getInstance().getUserId().equals(String.valueOf(bean.getToUserId()))) {//增加
                viewHolder.tv_amount.setText("+" + bean.getAmount());
                viewHolder.tv_amount.setTextColor(mContext.getResources().getColor(R.color.color_red));
                viewHolder.iv_pic.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_increase));
            } else {//减少
                viewHolder.tv_amount.setText("-" + bean.getAmount());
                viewHolder.tv_amount.setTextColor(mContext.getResources().getColor(R.color.color_green));
                viewHolder.iv_pic.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_reduce));
            }
        }
        viewHolder.tv_day.setText(bean.getCreatetime().substring(0, 10));
        viewHolder.tv_remark.setText(String.valueOf(bean.getRemark()));

        if (type == TYPE_ASSET) {
            viewHolder.ll_child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    assetListener.onClickChild(bean);
                }
            });
        } else {
            viewHolder.ll_child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    coinListener.onClickChild(bean);
                }
            });
        }

        return view;
    }

    @Override
    public Object getRowItem(int section, int row) {
        if (section < 0)
            return null;
        Object[] key = map.keySet().toArray();
        return map.get((String) key[section]).get(row);
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_expand_group, parent, false);
//        TextView tv_month = (TextView) view.findViewById(R.id.tv_month);

        View view = null;
        ViewSectionHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_expand_group, parent, false);
            viewHolder = new ViewSectionHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewSectionHolder) view.getTag();
        }

        Object[] keys = map.keySet().toArray();
        final String key = (String) keys[section];

        viewHolder.tv_month.setText(key);
        if (type == TYPE_ASSET) {
            viewHolder.tv_month.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (assetListener != null) {
                        assetListener.onClickTitle(key);
                    }
                }
            });
        } else {
            viewHolder.tv_month.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (coinListener != null) {
                        coinListener.onClickTitle(key);
                    }
                }
            });
        }

        return view;
    }

    @Override
    public boolean hasSectionHeaderView(int section) {
        return true;
    }

    private class ViewSectionHolder {

        private TextView tv_month;

        ViewSectionHolder(View v) {
            tv_month = (TextView) v.findViewById(R.id.tv_month);
        }
    }

    private class ViewContentHolder {

        private TextView tv_day;
        private ImageView iv_pic;
        private TextView tv_amount;
        private TextView tv_remark;
        private LinearLayout ll_child;

        ViewContentHolder(View v) {
            tv_day = (TextView) v.findViewById(R.id.tv_day);
            tv_amount = (TextView) v.findViewById(R.id.tv_amount);
            tv_remark = (TextView) v.findViewById(R.id.tv_remark);
            iv_pic = (ImageView) v.findViewById(R.id.iv_pic);
            ll_child = (LinearLayout) v.findViewById(R.id.ll_child);
        }
    }
}