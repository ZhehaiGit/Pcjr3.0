package com.pcjinrong.pcjr.ui.views.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.pcjinrong.pcjr.R;
import com.pcjinrong.pcjr.bean.BankCard;
import com.pcjinrong.pcjr.bean.BaseBean;
import com.pcjinrong.pcjr.bean.Withdraw;
import com.pcjinrong.pcjr.constant.Constant;
import com.pcjinrong.pcjr.core.BaseToolbarActivity;
import com.pcjinrong.pcjr.ui.presenter.WithdrawPresenter;
import com.pcjinrong.pcjr.ui.presenter.ivview.WithdrawView;
import com.pcjinrong.pcjr.utils.ViewUtil;
import com.pcjinrong.pcjr.widget.Dialog;

import java.util.List;

import butterknife.BindView;
import retrofit2.adapter.rxjava.HttpException;


/**
 * 提现
 * Created by Mario on 2016/5/24.
 */
public class WithdrawActivity extends BaseToolbarActivity implements WithdrawView {
    @BindView(R.id.btn_recharge) RelativeLayout btn_recharge;

    @BindView(R.id.btn_verify) Button btn_verify;
    @BindView(R.id.btn_apply) Button btn_apply;

    @BindView(R.id.txt_mention_amount) EditText txt_mention_amount;
    @BindView(R.id.txt_verify) EditText txt_verify;

    @BindView(R.id.txt_balance) TextView txt_balance;
    @BindView(R.id.txt_realname) TextView txt_realname;
    @BindView(R.id.txt_fee) TextView txt_fee;
    @BindView(R.id.txt_mobile) TextView txt_mobile;
    @BindView(R.id.explain) TextView explain;

    @BindView(R.id.info) ImageView info;

    @BindView(R.id.bank_spinner) Spinner bank_spinner;

    private TimeCount time;
    private WithdrawPresenter presenter;
    private ProgressDialog dialog;

    private List<BankCard> bankCards;
    private String bank_id;

    @Override
    protected int getLayoutId() {
        return R.layout.member_withdraw;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        showBack();
        setTitle("提现/充值");
        time = new TimeCount(60000, 1000);
        dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
    }

    @Override
    protected void initListeners() {

        btn_recharge.setOnClickListener(v -> Dialog.show("温馨提示", "皮城金融APP暂不支持此操作 ，请在电脑上充值，谢谢您的合作，给您带来的不便，敬请谅解", this));

        info.setOnClickListener(v -> Dialog.show("提现金额", "已赚取利息与到期本金之和即为您可免费提现的总额，充值未投资金额则需收取0.15%手续费", this));

        explain.setOnClickListener(v -> {
            if (ViewUtil.isFastDoubleClick()) return;
            Intent intent = new Intent(WithdrawActivity.this, WebViewActivity.class);
            intent.putExtra("title", Constant.WITHDRAW_RULES);
            intent.putExtra("url", Constant.WITHDRAW_RULES_URL);
            startActivity(intent);
        });


        bank_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bank_id = bankCards.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_verify.setOnClickListener(v -> {
            if (ViewUtil.isFastDoubleClick()) return;
            send_verify();
        });

        btn_apply.setOnClickListener(v -> {
            if (ViewUtil.isFastDoubleClick()) return;
            apply();
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        Withdraw withdraw = (Withdraw) intent.getSerializableExtra("data");

        txt_balance.setText(withdraw.getAvailable_balance());
        txt_mention_amount.setHint("免费可提" + withdraw.getFree_withdraw() + "元");
        txt_realname.setText(withdraw.getRealname());
        txt_mobile.setText(withdraw.getMobile());

        presenter = new WithdrawPresenter();
        presenter.attachView(this);

        presenter.getBankCardList();
    }

    public void apply() {
        String amount = txt_mention_amount.getText().toString().trim();
        String verify = txt_verify.getText().toString().trim();
        if (amount.equals("")) {
            Dialog.show("请输入提现金额", this);
            return;
        }
        if (Double.parseDouble(amount) <= 0) {
            Dialog.show("提现金额必须大于0", this);
            return;
        }
        if (verify.equals("")) {
            Dialog.show("请输入验证码", this);
            return;
        }

        presenter.withdraw(amount, bank_id, verify);
    }

    public void send_verify() {
        String amount = txt_mention_amount.getText().toString().trim();
        if (amount.equals("")) {
            Dialog.show("请输入提现金额", WithdrawActivity.this);
            return;
        }
        if (Double.parseDouble(amount) <= 0) {
            Dialog.show("提现金额必须大于0", WithdrawActivity.this);
            return;
        }
        btn_verify.setClickable(false);
        btn_verify.setBackgroundResource(R.drawable.btn_disable);
        time.start();
        presenter.withdrawVerify();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        return false;

    }


    @Override
    public void onFailure(Throwable e) {
        if(e instanceof HttpException){
            showToast(getString(R.string.login_expired));
            startActivity(new Intent(WithdrawActivity.this, LoginActivity.class));
            return;
        }
        showToast(R.string.network_anomaly);
    }

    @Override
    public void onSuccess(Object data) {

    }

    @Override
    public void onWithdrawVerifySuccess(BaseBean data) {
        Dialog.show(data.getMessage(), this);
    }

    @Override
    public void onWithdrawSuccess(BaseBean data) {
        if (data.isSuccess()) {
            showToast(data.getMessage());
            finish();
        } else {
            Dialog.show(data.getMessage(), this);
        }
    }

    @Override
    public void onBankCardListSuccess(List<BankCard> list) {
        bankCards = list;
        String[] mItems = new String[bankCards.size()];
        for (int i = 0; i < bankCards.size(); i++) {
            mItems[i] = bankCards.get(i).getBank() + " " + bankCards.get(i).getCard_no();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(WithdrawActivity.this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bank_spinner.setAdapter(adapter);
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btn_verify.setText("获取验证码");
            btn_verify.setClickable(true);
            btn_verify.setBackgroundResource(R.drawable.btn_primary);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_verify.setText(millisUntilFinished / 1000 + "秒后可重新获取");
        }
    }
}
