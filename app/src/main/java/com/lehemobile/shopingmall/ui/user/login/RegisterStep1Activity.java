package com.lehemobile.shopingmall.ui.user.login;

import android.text.Editable;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.tgh.devkit.core.text.SpannableStringHelper;
import com.tgh.devkit.core.text.TextHelper;
import com.tgh.devkit.core.utils.Strings;
import com.tgh.devkit.core.utils.TextWatcherAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.util.Map;

import static com.tgh.devkit.core.utils.Strings.isNullOrEmpty;


/**
 * 用户注册第一步，验证手机号
 * Created by albert on 11/27/14.
 */
@EActivity(R.layout.activity_register_step1)
public class RegisterStep1Activity extends BaseActivity {
    public static final int TYPE_REGISTER = 1;
    public static final int TYPE_RESET_PASSWORD = 2;
    @ViewById(R.id.btnNext)
    Button nextBtn;
    @ViewById
    EditText inputMobile;

    @ViewById
    CheckBox licence;

    @ViewById
    TextView login;

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

        String info = "已有账号？直接登录";
        new SpannableStringHelper(info)
                .foregroundColor("直接登录", getResources().getColor(R.color.colorAccent))
                .attachToTextView(login);

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
        if (!licence.isChecked()) {
            showToast("请选择同意条款");
            return;
        }
        //TODO  调用该接口获取验证码
        showLoading("正在发送验证码...");
        BaseRequest<Map<String, Object>> request = UserApi.requestSmsCode(mobile, UserApi.TYPE_REGISTER, new Response.Listener<Map<String, Object>>() {
            @Override
            public void onResponse(Map<String, Object> response) {
                dismissLoading();
                RegisterStep2Activity_.intent(RegisterStep1Activity.this).mobile(mobile).start();
            }
        }, new AppErrorListener(this) {
            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
            }
        });
        VolleyHelper.execute(request, this);
    }

    @Click(R.id.login)
    void onLogin() {
        LoginActivity_.intent(this).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleyHelper.cancel(this);
    }
}
