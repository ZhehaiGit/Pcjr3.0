package com.pcjinrong.pcjr.ui.views.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import com.pcjinrong.pcjr.R;
import com.pcjinrong.pcjr.bean.FinanceRecords;
import com.pcjinrong.pcjr.core.BaseAppCompatActivity;
import com.pcjinrong.pcjr.core.BaseToolbarActivity;

import butterknife.BindView;

/**
 * 资金记录
 * Created by Mario on 2016/5/20.
 */
public class FinancialRecordsActivity extends BaseToolbarActivity{

    @BindView(R.id.available_balance) TextView available_balance;
    @BindView(R.id.capital) TextView capital;
    @BindView(R.id.interest) TextView interest;
    @BindView(R.id.earned_interest) TextView earned_interest;
    @BindView(R.id.reward_amount) TextView reward_amount;
    @BindView(R.id.total_reward_amount) TextView total_reward_amount;
    @BindView(R.id.total_amount) TextView total_amount;
    @BindView(R.id.recharge_success_amount) TextView recharge_success_amount;
    @BindView(R.id.invest_success_amount) TextView invest_success_amount;
    @BindView(R.id.withdraw_success_amount) TextView withdraw_success_amount;

    @Override
    protected int getLayoutId() {
        return R.layout.member_financial_records;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        this.showBack();
        this.setTitle("资金记录");
    }


    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {
        FinanceRecords data = (FinanceRecords) getIntent().getSerializableExtra("data");

        total_amount.setText(data.getTotal_amount()+"元");
        available_balance.setText(data.getAvailable_balance()+"元");
        capital.setText(data.getCapital()+"元");
        interest.setText(data.getInterest()+"元");
        earned_interest.setText(data.getEarned_interest()+"元");
        reward_amount.setText(data.getReward_amount()+"元");
        total_reward_amount.setText(data.getTotal_reward_amount()+"元");
        recharge_success_amount.setText(data.getRecharge_success_amount()+"元");
        invest_success_amount.setText(data.getInvest_success_amount()+"元");
        withdraw_success_amount.setText(data.getWithdraw_success_amount()+"元");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return false;

    }

}