package com.lehemobile.shopingmall.ui.user;

import com.lehemobile.shopingmall.R;
import com.lehemobile.shopingmall.ui.BaseActivity;
import com.lehemobile.shopingmall.ui.user.login.LoginActivity_;
import com.lehemobile.shopingmall.ui.user.login.RegisterStep1Activity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;

/**
 * Created by tanyq on 17/7/16.
 */
@EActivity(R.layout.activity_profile)
@OptionsMenu(R.menu.menu_profile)
public class ProfileActivity extends BaseActivity {

    @Click(R.id.tv_register)
    void doRegister(){
        RegisterStep1Activity_.intent(this).start();
    }

    @Click(R.id.tv_login)
    void doLogin(){

        LoginActivity_.intent(this).start();
    }

}
