package com.pcjinrong.pcjr.ui.presenter.ivview;

import com.pcjinrong.pcjr.bean.InvestTicket;
import com.pcjinrong.pcjr.core.mvp.MvpView;

import java.util.List;

/**
 * Created by Mario on 2016/7/26.
 */
public interface InvestTicketView extends MvpView {

    void onInvestTicketListSuccess(List<InvestTicket> list);

    void onInvestTicketDetailSuccess(InvestTicket data);

}
