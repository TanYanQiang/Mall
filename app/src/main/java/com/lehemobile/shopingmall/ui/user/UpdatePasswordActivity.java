package com.lehemobile.shopingmall.ui.user;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.android.volley.Response;
import com.lehemobile.shopingmall.MyApplication;
import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.api.UserApi;
import com.lehemobile.shopingmall.api.base.AppErrorListener;
import com.lehemobile.shopingmall.api.base.BaseRequest;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.ui.MainActivity_;
import com.lehemobile.shopingmall.ui.user.login.LoginActivity_;
import com.lehemobile.shopingmall.utils.Validation;
import com.lehemobile.shopingmall.utils.VolleyHelper;
import com.tgh.devkit.core.utils.Strings;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tanyq on 21/7/16.
 */
@EActivity(R.layout.activity_update_password)
public class UpdatePasswordActivity extends BaseActivity {
    @ViewById
    EditText oldPassword;
    @ViewById
    EditText newPassword;
    @ViewById
    EditText newPassword2;

    @Click(R.id.submit)
    void onSubmit() {
        if (validate()) {
            updatePassword();
        }
    }

    private void updatePassword() {
        String oldPasswordStr = getInputText(oldPassword);
        String newPasswordStr = getInputText(newPassword);
        //TODO 修改密码
        showLoading("正在提交数据...");
        BaseRequest<Void> request = UserApi.updatePassword(oldPasswordStr, newPasswordStr, new Response.Listener<Void>() {
            @Override
            public void onResponse(Void response) {
                showToast("密码修改成功，请重新登录");
                Intent[] intents = new Intent[2];
                intents[0] = MainActivity_.intent(UpdatePasswordActivity.this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK).get();
                intents[1] = LoginActivity_.intent(UpdatePasswordActivity.this).get();
                startActivities(intents);
                MyApplication.getInstance().onUserLogout();
                finish();
            }
        }, new AppErrorListener(this));
        VolleyHelper.execute(request);
    }

    boolean validate() {
        String info = oldPassword.getText().toString().trim();
        if (Strings.isNullOrEmpty(info)) {
            showToast("请输入原密码");
            return false;
        }
        info = newPassword.getText().toString().trim();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleyHelper.cancel();
    }
}
