package com.lehemobile.shopingmall.ui.user.login;

import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;

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
    }

    void onMobileChanged() {
        String mobile = inputMobile.getText().toString().trim();
        nextBtn.setEnabled(!isNullOrEmpty(mobile));
        ConfigManager.Device.setLastMobile(mobile);
    }

    @Click(R.id.btnNext)
    void onNext() {

        final String mobile = inputMobile.getText().toString().trim();
        if (!Validation.isMobileNO(mobile)) {
            showToast("请输入正确的手机格式");
            return;
        }
        showLoading("正在发送验证码...");
        BaseRequest<Map<String, Object>> request = UserApi.requestSmsCode(mobile, UserApi.TYPE_RESET_PASSWORD, new Response.Listener<Map<String, Object>>() {
            @Override
            public void onResponse(Map<String, Object> response) {
                dismissLoading();
                RestPasswordStep2Activity_.intent(RestPasswordStep1Activity.this).mobile(mobile).start();
            }
        }, new AppErrorListener(this) {
            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
            }
        });
        VolleyHelper.execute(request, this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleyHelper.cancel(this);
    }
}
