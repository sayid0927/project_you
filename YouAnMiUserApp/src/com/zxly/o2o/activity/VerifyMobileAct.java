package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zxly.o2o.fragment.VerifyIdentityFragment;
import com.zxly.o2o.fragment.VerifyMobileFragment;
import com.zxly.o2o.fragment.VerifySelectFragment;
import com.zxly.o2o.fragment.VerifyServiceFragment;
import com.zxly.o2o.fragment.VerifySuccessFragment;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ViewUtils;

public class VerifyMobileAct extends BasicAct {
    private FragmentManager fragmentManager;
    private VerifySelectFragment verifySelectFragment;
    private VerifyIdentityFragment verifyIdentityFragment;
    private VerifyMobileFragment verifyMobileFragment;
    private VerifySuccessFragment verifySuccessFragment;
    private VerifyServiceFragment verifyServiceFragment;

    int type; // 0：选择页面 1：验证本人 2：验证新手机号 3：成功 4：客服
    int lastIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_fragment_activity_main);
        setPageShow(type);
    }

    public void setPageShow(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (index > lastIndex) {
            transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        } else {
            transaction.setCustomAnimations(R.anim.slide_right_in,R.anim.slide_right_out);
        }
        hideFragments(transaction);
        switch (index) {
            case 0:
                if (verifySelectFragment == null) {
                    verifySelectFragment = new VerifySelectFragment();
                    transaction.add(R.id.fragment_main, verifySelectFragment);
                } else {
                    transaction.show(verifySelectFragment);
                }
                break;
            case 1:
                if (verifyIdentityFragment == null) {
                    verifyIdentityFragment = new VerifyIdentityFragment();
                    transaction.add(R.id.fragment_main, verifyIdentityFragment);
                } else {
                    transaction.show(verifyIdentityFragment);
                }
                break;
            case 2:
                if (verifyMobileFragment == null) {
                    verifyMobileFragment = new VerifyMobileFragment();
                    transaction.add(R.id.fragment_main, verifyMobileFragment);
                } else {
                    transaction.show(verifyMobileFragment);
                }
                break;
            case 3:
                if (verifySuccessFragment == null) {
                    verifySuccessFragment = new VerifySuccessFragment();
                    transaction.add(R.id.fragment_main, verifySuccessFragment);
                } else {
                    transaction.show(verifySuccessFragment);
                }
                break;
            case 4:
                if (verifyServiceFragment == null) {
                    verifyServiceFragment = new VerifyServiceFragment();
                    transaction.add(R.id.fragment_main, verifyServiceFragment);
                } else {
                    transaction.show(verifyServiceFragment);
                }
                break;
            default:
                break;
        }
        lastIndex = index;
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (verifyIdentityFragment != null) {
            transaction.hide(verifyIdentityFragment);
        }
        if (verifyMobileFragment != null) {
            transaction.hide(verifyMobileFragment);
        }
        if (verifySuccessFragment != null) {
            transaction.hide(verifySuccessFragment);
        }
        if (verifySelectFragment != null) {
            transaction.hide(verifySelectFragment);
        }
        if (verifyServiceFragment != null) {
            transaction.hide(verifyServiceFragment);
        }
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, VerifyMobileAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

}
