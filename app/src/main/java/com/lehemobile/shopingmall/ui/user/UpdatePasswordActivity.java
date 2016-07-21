package com.lehemobile.shopingmall.ui.user;

import android.text.TextUtils;
import android.widget.EditText;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.utils.Validation;
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
}
