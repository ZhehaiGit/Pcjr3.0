package com.pcjinrong.pcjr.ui.views.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.pcjinrong.pcjr.R;
import com.pcjinrong.pcjr.api.ApiConstant;
import com.pcjinrong.pcjr.bean.BaseBean;
import com.pcjinrong.pcjr.bean.ProductTradingRecord;
import com.pcjinrong.pcjr.bean.RedPacket;
import com.pcjinrong.pcjr.core.BaseSwipeFragment;
import com.pcjinrong.pcjr.core.mvp.MvpView;
import com.pcjinrong.pcjr.ui.adapter.ProductTradingRecordListAdapter;
import com.pcjinrong.pcjr.ui.adapter.RedPacketListAdapter;
import com.pcjinrong.pcjr.ui.presenter.ProductTradingRecordPresenter;
import com.pcjinrong.pcjr.ui.presenter.RedPacketPresenter;
import com.pcjinrong.pcjr.ui.presenter.ivview.RedPacketView;
import com.pcjinrong.pcjr.utils.ViewUtil;
import com.pcjinrong.pcjr.widget.Dialog;

import java.util.List;

import butterknife.BindView;


/**
 * 投资项目详情-投资记录
 * Created by Mario on 2016/5/12.
 */
public class InvestDetailRecordFragment extends BaseSwipeFragment implements MvpView<BaseBean<List<ProductTradingRecord>>> {

    @BindView(R.id.total) TextView total;

    private ProductTradingRecordPresenter presenter;
    private ProductTradingRecordListAdapter adapter;

    private boolean isPrepared;
    private boolean mHasLoadedOnce;
    private String id;

    @Override
    protected int getLayoutId() {
        return R.layout.invest_detail_record;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        dividerHeight = (int) getResources().getDimension(R.dimen.list_divider_height_1dp);
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        id = bundle.getString("id");

        this.presenter = new ProductTradingRecordPresenter();
        this.presenter.attachView(this);
        this.adapter = new ProductTradingRecordListAdapter();
        rv_list.setAdapter(this.adapter);

        isPrepared = true;
        lazyLoad();
    }

    @Override
    public void refresh() {
        refresh = true;
        emptyCount = 0;
        this.presenter.setPage(1);
        this.presenter.getProductTradingRecordList(id, ApiConstant.DEFAULT_PAGE_SIZE);
    }

    @Override
    public void loadMore() {
        if(emptyCount < EMPTY_LIMIT && !mPtrFrame.isRefreshing()) {
            refresh = false;
            this.presenter.setPage(this.presenter.getPage() + 1);
            this.presenter.getProductTradingRecordList(id, ApiConstant.DEFAULT_PAGE_SIZE);
        }
    }

    public static Fragment newInstance(String id) {
        InvestDetailRecordFragment fragment = new InvestDetailRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        mPtrFrame.post(() -> mPtrFrame.autoRefresh());
    }

    @Override
    public void onFailure(Throwable e) {
        mPtrFrame.refreshComplete();
        showToast(getString(R.string.network_anomaly));
    }

    @Override
    public void onSuccess(BaseBean<List<ProductTradingRecord>> data) {
        mHasLoadedOnce = true;
        mPtrFrame.refreshComplete();
        total.setText(String.valueOf(data.getPager().getTotal()));
        List<ProductTradingRecord> list = data.getData();
        if(refresh){
            if (list.size() == 0) {
                empty.setVisibility(View.VISIBLE);
                rv_list.setVisibility(View.INVISIBLE);
            } else {
                empty.setVisibility(View.INVISIBLE);
                rv_list.setVisibility(View.VISIBLE);
                adapter.setData(list);
            }
        }else{
            adapter.addAll(list);
        }

        if(list.size() == 0 ) emptyCount++;
    }

}
