package com.yiyue.store.module.mine.works.many;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.base.adapter.BaseRecycleViewAdapter;
import com.yiyue.store.component.recycleview.GridSpacingItemDecoration;
import com.yiyue.store.databinding.ActivityManyWorksBinding;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.OpusFeaTureBean;
import com.yiyue.store.model.vo.bean.OpusHairstyleBean;
import com.yiyue.store.model.vo.bean.OpusListBean;
import com.yiyue.store.model.vo.bean.StylistOpusBean;
import com.yiyue.store.model.vo.bean.WorksLabelBean;
import com.yiyue.store.module.mine.works.details.WorksDetailsActivity;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.ArrayList;

@CreatePresenter(ManyWorksPrenster.class)
public class ManyWorksActivity extends BaseMvpAppCompatActivity<IManyWorksView, ManyWorksPrenster> implements IManyWorksView {

    ActivityManyWorksBinding mBinding;
    private ManyWorksAdapter adapter;

    private ArrayList<WorksLabelBean> mLabelBeans = new ArrayList<>();
    private ArrayList<StylistOpusBean> opusList = new ArrayList<>();

    private String stylistId;
    private String headPortrait = "";//头像
    private String nickName = "";//昵称

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_many_works;
    }

    @Override
    protected void init() {
        initView();
        loadData();
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle){
            stylistId = bundle.getString(Constants.STYLIST_ID);
            this.headPortrait = bundle.getString(Constants.HEAD_PORTRAIT);
            this.nickName = bundle.getString(Constants.NICK_NAME);
        }else {
            showToast("没有获取到美发师Id");
            finish();
        }
        
        mBinding = (ActivityManyWorksBinding) viewDataBinding;
        // titleview
        mBinding.titleView.setLeftClickListener(v -> finish());
        // flowlayput
        mBinding.flowLayout.setOnTagClickListener((view, position, parent) -> {
            // TODO 处理选中事件
            return false;
        });

        mBinding.recycleView.setLayoutManager(new GridLayoutManager(this, 2));
        mBinding.recycleView.addItemDecoration(new GridSpacingItemDecoration(2, 30, false));
        adapter = new ManyWorksAdapter(this);
        adapter.setItemListener(new BaseRecycleViewAdapter.SimpleRecycleViewItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                StylistOpusBean opus = adapter.getDatas().get(position);
                Bundle opusBundle = new Bundle();
                opusBundle.putString(Constants.OPUS_ID, String.valueOf(opus.getStylistOpusId()));
                opusBundle.putString(Constants.HEAD_PORTRAIT, headPortrait);
                opusBundle.putString(Constants.NICK_NAME, nickName);
                WorksDetailsActivity.startActivity(ManyWorksActivity.this, WorksDetailsActivity.class, opusBundle);
            }
        });
        mBinding.recycleView.setAdapter(adapter);

    }

    private void loadData() {
        getMvpPresenter().opusList(stylistId);
    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void onOpusList(OpusListBean opusListBean) {
        int total;
        //标题数目 + 作品列表展示
        if (null != opusListBean.getOpusList() && opusListBean.getOpusList().size() > 0) {
            opusList.addAll(opusListBean.getOpusList());
            adapter.setDatas(opusList, true);
            total = opusListBean.getOpusList().size();
            if (total > 99) {
                mBinding.titleView.setSubTitleText("(99+)");
            } else {
                mBinding.titleView.setSubTitleText("(" + total + ")");
            }
        }

        //label展示
        if (null != opusListBean.getOpusHairstyleList() && opusListBean.getOpusHairstyleList().size() > 0) {
            for (OpusHairstyleBean hair : opusListBean.getOpusHairstyleList()) {
                mLabelBeans.add(new WorksLabelBean(hair.getHairstyleName(), hair.getCount(), hair.getHairstyleId(), 1));
            }
        }

        if (null != opusListBean.getOpusFeaTureList() && opusListBean.getOpusFeaTureList().size() > 0) {
            for (OpusFeaTureBean feature : opusListBean.getOpusFeaTureList()) {
                mLabelBeans.add(new WorksLabelBean(feature.getFeaTureName(), feature.getCount(), feature.getFeaTureId(), 2));
            }
        }

        mBinding.flowLayout.setAdapter(new TagAdapter<WorksLabelBean>(mLabelBeans) {
            @Override
            public View getView(FlowLayout parent, int position, WorksLabelBean label) {
                View view = getLayoutInflater().inflate(R.layout.item_works_label, null, false);
                TextView tvName = view.findViewById(R.id.tv_name);
                tvName.setText(label.getLabel());
                TextView tvNum = view.findViewById(R.id.tv_number);
                tvNum.setText(label.getNumber() + "");
                return view;
            }
        });

        mBinding.flowLayout.setOnTagClickListener((view, position, parent) -> {
            WorksLabelBean label = mLabelBeans.get(position);
            getMvpPresenter().getOpusListScreen(stylistId, label.getId(),label.getType());
            return true;
        });

    }

    @Override
    public void getOpusListScreenSuc(OpusListBean opusListBean) {
        if (null != opusListBean.getOpusList() && opusListBean.getOpusList().size() > 0) {
            opusList.clear();
            opusList.addAll(opusListBean.getOpusList());
            adapter.setDatas(opusList, true);
        }
    }
}
