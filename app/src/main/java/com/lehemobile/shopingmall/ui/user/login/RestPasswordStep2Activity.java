package com.lehemobile.shopingmall.ui.user.login;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.lehemobile.shopingmall.MyApplication;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.api.UserApi;
import com.lehemobile.shopingmall.api.base.AppErrorListener;
import com.lehemobile.shopingmall.api.base.BaseRequest;
import com.lehemobile.shopingmall.model.User;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.ui.MainActivity_;
import com.lehemobile.shopingmall.utils.Validation;
import com.lehemobile.shopingmall.utils.VolleyHelper;
import com.tgh.devkit.core.utils.Strings;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 8/7/16.
 */
@EActivity(R.layout.activity_reset_password_step2)
public class RestPasswordStep2Activity extends BaseActivity {

    @Extra
    String mobile;
    @Extra
    String token;

    @ViewById
    EditText newPassword;
    @ViewById
    EditText newPassword2;


    boolean validate() {

        String info = newPassword.getText().toString().trim();
        if (!Validation.isPassword(info)) {
            showToast("密码应为6-20为，且包含数字和字母");
            return false;
        }
        String password2 = getInputText(newPassword2);
        if (!TextUtils.equals(info, password2)) {
            showToast("两次输入的密码不一致");
            return false;
        }
        return true;
    }


    @Click(R.id.btnComplete)
    void onRestPassword() {

        String password = getInputText(newPassword);
        if (Strings.isNullOrEmpty(password)) {
            showToast("请输入密码");
            return;
        }
        if (!validate()) {
            return;
        }

        showLoading("正在提交信息...");
        BaseRequest<Void> request = UserApi.restPassword(mobile, token, password, new Response.Listener<Void>() {
            @Override
            public void onResponse(Void response) {
                dismissLoading();
                showToast("密码设置成功");
                Intent[] intents = new Intent[2];
                intents[0] = MainActivity_.intent(RestPasswordStep2Activity.this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK).get();
                intents[1] = LoginActivity_.intent(RestPasswordStep2Activity.this).get();
                startActivities(intents);
                MyApplication.getInstance().onUserLogout();
                finish();
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
        super.onDestroy();
        VolleyHelper.cancel(this);
    }

}
