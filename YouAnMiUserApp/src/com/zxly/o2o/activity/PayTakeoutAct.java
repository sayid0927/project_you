package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.adapter.PayBankcardAdapter;
import com.zxly.o2o.dialog.PaySetDialog;
import com.zxly.o2o.model.UserBankCard;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PayTakeoutRequest;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.PayUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;

/**
 * @author fengrongjian 2015-5-21
 * @description 提现
 */
public class PayTakeoutAct extends BasicAct implements View.OnClickListener {
    private Context context;
    private TextView txtBalance;
    private EditText editTakeoutMoney;
    private ListView listView;
    private View mFooterView;
    private View btnNewBankcard, btnTakeout;
    private PayBankcardAdapter adapter;
    private float userBalance;
    private static ArrayList<UserBankCard> userBankcardList;
    private UserBankCard selectedBankCard = null;
    private float takeoutMoney;
    private PayTakeoutRequest payTakeoutRequest;
    private PaySetDialog setDialog;
    private String moneyInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_takeout_main);
        userBalance = getIntent().getFloatExtra("userBalance", 0f);
        context = this;
        initViews();
    }

    /**
     * @param curAct
     * @param userBalance  用户余额
     * @param bankcardList 绑定银行卡列表
     */
    public static void start(Activity curAct, float userBalance, ArrayList<UserBankCard> bankcardList) {
        userBankcardList = bankcardList;
        Intent intent = new Intent(curAct, PayTakeoutAct.class);
        intent.putExtra("userBalance", userBalance);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "提现");
        txtBalance = (TextView) findViewById(R.id.txt_user_balance);
        ViewUtils.setTextPrice(txtBalance, userBalance);
        editTakeoutMoney = (EditText) findViewById(R.id.edit_money);
        editTakeoutMoney.setHint("≤ " + userBalance);

        listView = (ListView) findViewById(R.id.list_bankcard);
        LayoutInflater layoutInflater =
                (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        adapter = new PayBankcardAdapter(context);
        mFooterView = layoutInflater.inflate(R.layout.pay_takeout_list_footer, null);
        btnNewBankcard = mFooterView.findViewById(R.id.btn_add_new);
        btnTakeout = mFooterView.findViewById(R.id.btn_takeout);
        btnNewBankcard.setOnClickListener(this);
        btnTakeout.setOnClickListener(this);
        listView.addFooterView(mFooterView, null, false);
        adapter.setSelectedShow(true);
        adapter.setFogShow(true);
        listView.setAdapter(adapter);
        adapter.addItem(userBankcardList, true);
        if(userBankcardList!= null && !userBankcardList.isEmpty()){
            String bankNo = ((UserBankCard) adapter.getItem(0)).getBankNo();
            int withdrawType = ((UserBankCard) adapter.getItem(0)).getWithdrawType();
            if (1 == withdrawType) {
                selectedBankCard = (UserBankCard) adapter.getItem(0);
                adapter.setSelected(0);
                adapter.notifyDataSetChanged();
            }
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                String bankNo = ((UserBankCard) adapter.getItem(position)).getBankNo();
                int withdrawType = ((UserBankCard) adapter.getItem(position)).getWithdrawType();
                if (1 == withdrawType) {
                    selectedBankCard = (UserBankCard) adapter.getItem(position);
                    adapter.setSelected(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void doTakeout(final String moneyInput, final UserBankCard userBankCard, int takeoutType) {
        payTakeoutRequest = new PayTakeoutRequest(moneyInput, userBankCard.getUserName(), userBankCard.getIdCard(), userBankCard.getBankNumber(), takeoutType);
        payTakeoutRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                String payNo = payTakeoutRequest.getPayNo();
                PayUtil.confirmTakeout(PayTakeoutAct.this, Constants.TYPE_TAKEOUT, payNo, Constants.PAY_TYPE_LIANLIAN, Float.parseFloat(moneyInput), userBankCard, callBack, null);
            }

            @Override
            public void onFail(int code) {
            }
        });
        payTakeoutRequest.start(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_add_new:
                moneyInput = editTakeoutMoney.getText().toString().trim();
                if (!"".equals(moneyInput)) {
                    takeoutMoney = Float.parseFloat(moneyInput);
                    if (takeoutMoney > userBalance) {
                        ViewUtils.showToast("提现金额不能超过余额");
                        return;
                    } else if (takeoutMoney < 10) {
                        ViewUtils.showToast("提现金额不能小于10元");
                        return;
                    }
                }
                if (Constants.isUserPaw != 2) {
                    setUserPaw(true);
                } else {
                    PayIdentityCheckAct.start(PayTakeoutAct.this, Constants.PAY_TYPE_LIANLIAN, takeoutMoney, Constants.TYPE_TAKEOUT, null, callBack);
                }
                break;
            case R.id.btn_takeout:
                if(selectedBankCard == null){
                    ViewUtils.showToast("请新增银行卡提现");
                    return;
                }
                moneyInput = editTakeoutMoney.getText().toString().trim();
                if ("".equals(moneyInput)) {
                    ViewUtils.showToast("请输入提现金额");
                    return;
                } else {
                    takeoutMoney = Float.parseFloat(moneyInput);
                    if (takeoutMoney > userBalance) {
                        ViewUtils.showToast("提现金额不能超过余额");
                        return;
                    } else if (takeoutMoney < 10) {
                        ViewUtils.showToast("提现金额不能小于10元");
                        return;
                    }
                    if (Constants.isUserPaw != 2) {
                        setUserPaw(false);
                    } else {
                        doTakeout(moneyInput, selectedBankCard, 2);
                    }
                }
                break;
        }
    }

    /**
     * @param isNewAdd 是否新增银行卡
     * @description 设置交易密码
     */
    private void setUserPaw(final boolean isNewAdd) {
        setDialog = new PaySetDialog(PayTakeoutAct.this, new ParameCallBack() {
            @Override
            public void onCall(Object object) {
                String userPwd = (String) object;
                if (isNewAdd) {
                    PayAddBankcardAct.start(PayTakeoutAct.this, Constants.PAY_TYPE_LIANLIAN, takeoutMoney, Constants.TYPE_TAKEOUT, null, userPwd, callBack);
                } else {
                    doTakeout(moneyInput, selectedBankCard, 2);
                }
            }
        });
        setDialog.show();
    }

    /**
     * @description 提现成功后回调关闭页面
     */
    private CallBack callBack = new CallBack() {
        @Override
        public void onCall() {
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userBankcardList = null;
    }
}
