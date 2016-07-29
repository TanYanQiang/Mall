package com.lehemobile.shopingmall.ui.user.login;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

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
    View userVerifyLayout;

    @ViewById
    EditText name;


    @ViewById
    EditText idCard;

    @ViewById
    CheckBox userVerify;


    private RequestSmsCodePasswordFragment smsCodePasswordFragment;

    @AfterViews
    void init() {
        mobileTips.setText(getString(R.string.register_sms_code_tips, mobile));

        smsCodePasswordFragment = RequestSmsCodePasswordFragment_.builder().mobile(mobile).type(UserApi.TYPE_REGISTER).build();

        getSupportFragmentManager().beginTransaction().add(R.id.container, smsCodePasswordFragment).commit();

        userVerify.setChecked(true);
    }

    @CheckedChange(R.id.userVerify)
    void userVerifyChange(CompoundButton button, boolean isChecked) {
        userVerifyLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    @Click(R.id.btnRegister)
    void onRegister() {
        String smsCode = smsCodePasswordFragment.getSmsCode();
        if (Strings.isNullOrEmpty(smsCode)) {
            showToast("请输入短信验证码");
            return;
        }
        String password = smsCodePasswordFragment.getPassword();
        if (Strings.isNullOrEmpty(password)) {
            showToast("请输入密码");
            return;
        }
        if (!Validation.isPassword(password)) {
            showToast("密码应为6-20为，且包含数字和字母");
            return;
        }
        String password2 = smsCodePasswordFragment.getPassword2();
        if (Strings.isNullOrEmpty(password)) {
            showToast("请输入验证密码");
            return;
        }
        if (!TextUtils.equals(password, password2)) {
            showToast("两次输入的密码不一致");
            return;
        }
        String _name = getInputText(name);
        if (Strings.isNullOrEmpty(_name)) {
            showToast("个人验证姓名不能为空");
            return;
        }
        String _idCard = getInputText(idCard);
        if (Strings.isNullOrEmpty(_idCard)) {
            showToast("身份证号码不能为空");
            return;
        }
        if (!Validation.isIDcard(_idCard)) {
            showToast("请输入正确的身份证号码");
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
        super.onDestroy();
        VolleyHelper.cancel(this);
    }

}
