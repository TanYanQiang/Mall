package com.lehemobile.shopingmall.ui.user.login;

import android.os.CountDownTimer;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.api.UserApi;
import com.lehemobile.shopingmall.api.base.AppErrorListener;
import com.lehemobile.shopingmall.api.base.BaseRequest;
import com.lehemobile.shopingmall.model.User;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.utils.Validation;
import com.lehemobile.shopingmall.utils.VolleyHelper;
import com.tgh.devkit.core.utils.Strings;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.Map;

/**
 * Created by tanyq on 8/7/16.
 */
@EActivity(R.layout.activity_register_step2)
public class RegisterStep2Activity extends BaseActivity {
    @Extra
    String mobile;

    @ViewById
    TextView mobileTips;

    @ViewById
    EditText inputSmsCode;
    @ViewById
    EditText inputPassword;
    @ViewById
    TextView requestSmsCode;

    private CountDownTimer countDownTimer;

    @AfterViews
    void init() {
        mobileTips.setText(getString(R.string.register_sms_code_tips, mobile));
        initWaitingUI();
        startCounting();
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
        BaseRequest<Map<String, Object>> request = UserApi.requestSmsCode(mobile, 0, new Response.Listener<Map<String, Object>>() {
            @Override
            public void onResponse(Map<String, Object> response) {
                dismissLoading();
                initWaitingUI();
                startCounting();
            }
        }, new AppErrorListener(this) {
            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
            }
        });
        VolleyHelper.execute(request, this);
    }

    @Click(R.id.btnRegister)
    void onRegister() {
        String smsCode = getInputText(inputSmsCode);
        if (Strings.isNullOrEmpty(smsCode)) {
            showToast("请输入短信验证码");
            return;
        }
        String password = getInputText(inputPassword);
        if (Strings.isNullOrEmpty(password)) {
            showToast("请输入密码");
            return;
        }
        if (!Validation.isPassword(password)) {
            showToast("密码应为6-20为，且包含数字和字母");
            return;
        }

        //TODO 调用接口注册
        showLoading("正在提交信息...");
        BaseRequest<User> request = UserApi.register(mobile, smsCode, password, new Response.Listener<User>() {
            @Override
            public void onResponse(User response) {
                dismissLoading();
                showToast("注册成功");
            }
        }, new AppErrorListener(this) {
            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                dismissLoading();
            }
        });
        VolleyHelper.execute(request, this);
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
