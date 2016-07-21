package com.lehemobile.shopingmall.ui.user.login;

import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.lehemobile.shopingmall.MyApplication;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.api.UserApi;
import com.lehemobile.shopingmall.api.base.AppErrorListener;
import com.lehemobile.shopingmall.api.base.BaseRequest;
import com.lehemobile.shopingmall.config.ConfigManager;
import com.lehemobile.shopingmall.model.User;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.utils.Validation;
import com.lehemobile.shopingmall.utils.VolleyHelper;
import com.tgh.devkit.core.utils.Strings;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 7/7/16.
 */
@EActivity(R.layout.activity_login)
@OptionsMenu(R.menu.menu_login)
public class LoginActivity extends BaseActivity {
    @ViewById
    EditText inputMobile;
    @ViewById
    EditText inputPassword;
    @ViewById
    Button loginButton;

    @Click
    void forgetPassword() {
        RestPasswordStep1Activity_.intent(this).start();
    }

    @Click(R.id.loginButton)
    void login() {
        String mobile = getInputText(inputMobile);
        if (!Validation.isMobileNO(mobile)) {
            showToast("请输入正确的手机格式");
            return;
        }
        String password = getInputText(inputPassword);
        if (Strings.isNullOrEmpty(password)) {
            showToast("请输入密码");
            return;
        }
        showLoading("正在登录...");
        BaseRequest<User> request = UserApi.login(mobile, password, new Response.Listener<User>() {
            @Override
            public void onResponse(User response) {
                dismissLoading();
                MyApplication.getInstance().onUserLogin(response);
                finish();
            }
        }, new AppErrorListener(this) {
            @Override
            public void onError(int code, String msg) {
                dismissLoading();
                super.onError(code, msg);
            }
        });
        VolleyHelper.execute(request, this);
    }

    @AfterTextChange({R.id.inputMobile, R.id.inputPassword})
    void onUserInputChanged() {
        String mobile = inputMobile.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        if (Strings.isNullOrEmpty(mobile) || Strings.isNullOrEmpty(password)) {
            loginButton.setEnabled(false);
        } else {
            loginButton.setEnabled(true);
        }

        ConfigManager.Device.setLastMobile(mobile);
    }

    @AfterViews
    void init() {
        String lastMobile = ConfigManager.Device.getLastMobile();
        inputMobile.setText(Strings.nullToEmpty(lastMobile));
        inputMobile.setSelection(inputMobile.length());
    }

    @OptionsItem(R.id.action_register)
    void register() {
        RegisterStep1Activity_.intent(this).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleyHelper.cancel(this);
    }
}
