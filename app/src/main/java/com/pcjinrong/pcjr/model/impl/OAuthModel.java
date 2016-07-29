package com.pcjinrong.pcjr.model.impl;

import com.pcjinrong.pcjr.App;
import com.pcjinrong.pcjr.api.RetrofitManager;
import com.pcjinrong.pcjr.bean.FinanceRecords;
import com.pcjinrong.pcjr.bean.MemberIndex;
import com.pcjinrong.pcjr.model.IOAuthModel;
import com.pcjinrong.pcjr.utils.SPUtils;

import java.util.List;

import rx.Observable;

/**
 * Created by Mario on 2016/7/25.
 */
public class OAuthModel implements IOAuthModel {

    private static final OAuthModel mInstance = new OAuthModel();


    public static OAuthModel getInstance() {
        return mInstance;
    }


    private OAuthModel() {}


    @Override
    public Observable<MemberIndex> getMemberIndex() {
        return RetrofitManager.getInstance().getAuthService().getMemberIndex();
    }

    @Override
    public Observable<FinanceRecords> getMemberFinanceData() {
        return RetrofitManager.getInstance().getAuthService().getMemberFinanceData();
    }

}
