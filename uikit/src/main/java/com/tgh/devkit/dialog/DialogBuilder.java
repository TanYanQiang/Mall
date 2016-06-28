package com.tgh.devkit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tgh.devkit.list.R;

/**
 *
 * Created by albert on 16/1/14.
 */
public class DialogBuilder {

    private View contentView;
    private int gravity = Gravity.BOTTOM;
    private int width;
    private int height;
    private int theme = R.style.BaseDialog;
    private int animationStyle = R.style.BottomSlideAnimation;
    private boolean cancelable = true;
    private boolean canceledOnTouchOutside = true;
    private Context context;

    public DialogBuilder(Context context) {
        this.context = context;
    }

    public DialogBuilder contentView(View contentView){
        this.contentView = contentView;
        return this;
    }

    public DialogBuilder gravity(int gravity){
        this.gravity = gravity;
        return this;
    }

    public DialogBuilder width(int width){
        this.width = width;
        return this;
    }

    public DialogBuilder height(int height){
        this.height = height;
        return this;
    }

    public DialogBuilder theme(int theme){
        this.theme = theme;
        return this;
    }

    public DialogBuilder animationStyle(int animationStyle){
        this.animationStyle = animationStyle;
        return this;
    }

    public DialogBuilder cancelable(boolean cancelable){
        this.cancelable = cancelable;
        return this;
    }

    public DialogBuilder canceledOnTouchOutside(boolean canceledOnTouchOutside){
        this.canceledOnTouchOutside = canceledOnTouchOutside;
        return this;
    }

    public Dialog build(){
        Dialog dialog = new Dialog(context, theme);
        dialog.setContentView(contentView);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        Window dialogWindow = dialog.getWindow();
        if(animationStyle!=0) {
            dialogWindow.setWindowAnimations(animationStyle);
        }
        WindowManager.LayoutParams attributes = dialogWindow.getAttributes();
        attributes.gravity = gravity;
        if (width == 0){
            width = context.getResources().getDisplayMetrics().widthPixels;
        }
        attributes.width = width;

        if (height!=0){
            attributes.height = height;
        }
        return dialog;
    }

}
