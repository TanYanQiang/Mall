package com.lehemobile.shopingmall.ui.user.login;

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

/**
 * Created by tanyq on 8/7/16.
 */
@EActivity(R.layout.activity_reset_password_step2)
public class RestPasswordStep2Activity extends BaseActivity {
    @Extra
    String mobile;

    @ViewById
    TextView mobileTips;


    private RequestSmsCodePasswordFragment smsCodePasswordFragment;

    @AfterViews
    void init() {
        mobileTips.setText(getString(R.string.register_sms_code_tips, mobile));

        smsCodePasswordFragment = RequestSmsCodePasswordFragment_.builder().mobile(mobile).passwordLabelText("新密码")
                .type(UserApi.TYPE_REGISTER).build();

        getSupportFragmentManager().beginTransaction().add(R.id.container, smsCodePasswordFragment).commit();
    }


    @Click(R.id.btnComplete)
    void onRestPassword() {
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

        //TODO 调用接口注册
        showLoading("正在提交信息...");
        BaseRequest<User> request = UserApi.restPassword(mobile, smsCode, password, new Response.Listener<User>() {
            @Override
            public void onResponse(User response) {
                dismissLoading();
                showToast("密码设置成功");
                LoginActivity_.intent(RestPasswordStep2Activity.this).start();
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
