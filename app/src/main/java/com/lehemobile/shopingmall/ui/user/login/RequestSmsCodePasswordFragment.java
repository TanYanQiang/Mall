package com.lehemobile.shopingmall.ui.user.login;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.api.UserApi;
import com.lehemobile.shopingmall.api.base.AppErrorListener;
import com.lehemobile.shopingmall.api.base.BaseRequest;
import com.lehemobile.shopingmall.model.User;
import com.lehemobile.shopingmall.ui.BaseFragment;
import com.lehemobile.shopingmall.utils.Validation;
import com.lehemobile.shopingmall.utils.VolleyHelper;
import com.tgh.devkit.core.utils.Strings;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.Map;

/**
 * Created by tanyq on 9/7/16.
 */
@EFragment(R.layout.fragment_requst_sms_code_password)
public class RequestSmsCodePasswordFragment extends BaseFragment {
    @FragmentArg
    String mobile;
    @FragmentArg
    int type;
    @FragmentArg
    String passwordLabelText;


    @ViewById
    EditText inputSmsCode;
    @ViewById
    EditText inputPassword;
    @ViewById
    TextView requestSmsCode;

    @ViewById
    TextView passwordLabel;

    private CountDownTimer countDownTimer;

    @AfterViews
    void init() {
        initWaitingUI();
        startCounting();
        setPasswordLabelText(passwordLabelText);
    }


    private void startCounting() {
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateCountingUI((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                initNormalUI();
            }
        };
        countDownTimer.start();
    }

    private void initNormalUI() {
        requestSmsCode.setEnabled(true);
        requestSmsCode.setText("重新获取验证码");
        requestSmsCode.setTextColor(getResources().getColor(R.color.orange));
    }

    private void initWaitingUI() {
        requestSmsCode.setEnabled(false);
        updateCountingUI(59);
    }

    private void updateCountingUI(int second) {
        requestSmsCode.setText(second + "秒后重新获取");
        requestSmsCode.setTextColor(getResources().getColor(R.color.text_color_lv2));
    }


    @Click
    void requestSmsCode() {
        inputSmsCode.setText("");
        //TODO  调用该接口获取验证码
        showLoading("正在发送验证码...");
        BaseRequest<Map<String, Object>> request = UserApi.requestSmsCode(mobile, type, new Response.Listener<Map<String, Object>>() {
            @Override
            public void onResponse(Map<String, Object> response) {
                dismissLoading();
                initWaitingUI();
                startCounting();
            }
        }, new AppErrorListener(getActivity()) {
            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
            }
        });
        VolleyHelper.execute(request, this);
    }

    public void setPasswordLabelText(String text) {
        if (passwordLabel != null && !TextUtils.isEmpty(text)) {
            passwordLabel.setText(text);
        }
    }

    public String getSmsCode() {
        return getInputText(inputSmsCode);
    }

    public String getPassword() {
        return getInputText(inputPassword);
    }

    @Override
    public void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
        VolleyHelper.cancel(this);
    }

}
