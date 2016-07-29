package com.lehemobile.shopingmall.ui.user.login;

import android.os.CountDownTimer;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.api.UserApi;
import com.lehemobile.shopingmall.api.base.AppErrorListener;
import com.lehemobile.shopingmall.api.base.BaseRequest;
import com.lehemobile.shopingmall.config.ConfigManager;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.utils.Validation;
import com.lehemobile.shopingmall.utils.VolleyHelper;
import com.tgh.devkit.core.utils.Strings;
import com.tgh.devkit.core.utils.TextWatcherAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Map;

import static com.tgh.devkit.core.utils.Strings.isNullOrEmpty;

/**
 * Created by tanyq on 9/7/16.
 */
@EActivity(R.layout.activity_reset_passwrod_step1)
public class RestPasswordStep1Activity extends BaseActivity {

    @ViewById(R.id.btnNext)
    Button nextBtn;
    @ViewById
    EditText inputMobile;

    @ViewById
    EditText inputSmsCode;
    @ViewById
    TextView requestSmsCode;
    @ViewById
    EditText idCard;

    private CountDownTimer countDownTimer;

    @AfterViews
    void init() {
        String mobile = ConfigManager.Device.getLastMobile();
        inputMobile.setText(Strings.nullToEmpty(mobile));
        inputMobile.setSelection(inputMobile.length());
        inputMobile.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                onMobileChanged();
            }
        });
        initNormalUI();
    }


    void onMobileChanged() {
        String mobile = inputMobile.getText().toString().trim();
        nextBtn.setEnabled(!isNullOrEmpty(mobile));
        ConfigManager.Device.setLastMobile(mobile);
    }

    @Click(R.id.btnNext)
    void onNext() {

        String _idCard = getInputText(idCard);
        if (!Validation.isIDcard(_idCard)) {
            showToast("请输入正确的身份证号码");
            return;
        }

        final String mobile = inputMobile.getText().toString().trim();
        if (!Validation.isMobileNO(mobile)) {
            showToast("请输入正确的手机格式");
            return;
        }
        String smsCode = getInputText(inputSmsCode);
        if (Strings.isNullOrEmpty(smsCode)) {
            showToast("请输入短信验证码");
            return;
        }

        //TODO 找回密码

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer != null){
            countDownTimer.cancel();
            countDownTimer = null;
        }
        VolleyHelper.cancel(this);
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
        requestSmsCode.setText("获取验证码");
    }

    private void initWaitingUI() {
        requestSmsCode.setEnabled(false);
        updateCountingUI(59);
    }

    private void updateCountingUI(int second) {
        requestSmsCode.setText(second + "秒后重新获取");
    }


    @Click
    void requestSmsCode() {
        String mobile = getInputText(inputMobile);
        if (!Validation.isMobileNO(mobile)) {
            showToast("请输入正确的手机格式");
            return;
        }

        inputSmsCode.setText("");
        //TODO  调用该接口获取验证码
        showLoading("正在发送验证码...");
        BaseRequest<Map<String, Object>> request = UserApi.requestSmsCode(mobile, UserApi.TYPE_RESET_PASSWORD, new Response.Listener<Map<String, Object>>() {
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
}
