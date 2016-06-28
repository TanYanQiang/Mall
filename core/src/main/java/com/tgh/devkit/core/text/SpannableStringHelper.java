package com.tgh.devkit.core.text;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.tgh.devkit.core.text.SSAction.ClickAction;
import com.tgh.devkit.core.text.SSAction.ClickAllAction;
import com.tgh.devkit.core.text.SSAction.ForegroundColorAction;
import com.tgh.devkit.core.text.SSAction.ForegroundColorAllAction;
import com.tgh.devkit.core.text.SSAction.InsertImageAction;
import com.tgh.devkit.core.text.SSAction.InsertImageAfterAction;
import com.tgh.devkit.core.text.SSAction.InsertImageAfterAllAction;
import com.tgh.devkit.core.text.SSAction.InsertImageBeforeAction;
import com.tgh.devkit.core.text.SSAction.InsertImageBeforeAllAction;
import com.tgh.devkit.core.text.SSAction.RelativeSizeAction;
import com.tgh.devkit.core.text.SSAction.RelativeSizeAllAction;
import com.tgh.devkit.core.utils.Strings;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 设置SpannableString的帮助类，链式调用
 * Created by albert on 15/12/31.
 */
public class SpannableStringHelper {

    private String info;
    ArrayList<SSAction> list1 = new ArrayList<>();
    ArrayList<InsertImageAction> list2 = new ArrayList<>();

    public SpannableStringHelper(String info) {
        this.info = info;
    }

    public SpannableStringHelper bold(String regularExpression){
        list1.add(new SSAction.BoldAction(regularExpression));
        return this;
    }

    public SpannableStringHelper boldAll(String regularExpression){
        list1.add(new SSAction.BoldAllAction(regularExpression));
        return this;
    }

    public SpannableStringHelper foregroundColor(String regularExpression,int color){
        list1.add(new ForegroundColorAction(color, regularExpression));
        return this;
    }

    public SpannableStringHelper foregroundColorAll(String regularExpression,int color){
        list1.add(new ForegroundColorAllAction(color, regularExpression));
        return this;
    }

    public SpannableStringHelper relativeSize(String regularExpression,float relativeSize){
        list1.add(new RelativeSizeAction(relativeSize, regularExpression));
        return this;
    }

    public SpannableStringHelper relativeSizeAll(String regularExpression,float relativeSize){
        list1.add(new RelativeSizeAllAction(relativeSize, regularExpression));
        return this;
    }

    public SpannableStringHelper click(String regularExpression,View.OnClickListener listener){
        list1.add(new ClickAction(regularExpression, Color.BLUE, true, listener));
        return this;
    }


    public SpannableStringHelper click(String regularExpression,int color,View.OnClickListener listener){
        list1.add(new ClickAction(regularExpression, color, true, listener));
        return this;
    }


    public SpannableStringHelper click(String regularExpression,
                                       int color,
                                       boolean underLine,
                                       View.OnClickListener listener){
        list1.add(new ClickAction(regularExpression, color, underLine, listener));
        return this;
    }

    public SpannableStringHelper clickAll(String regularExpression,View.OnClickListener listener){
        list1.add(new ClickAllAction(regularExpression, Color.BLUE, true, listener));
        return this;
    }

    public SpannableStringHelper clickAll(String regularExpression,int color,
                                           View.OnClickListener listener){
        list1.add(new ClickAllAction(regularExpression, color, true, listener));
        return this;
    }

    public SpannableStringHelper clickAll(String regularExpression,int color,
                                           boolean underLine,
                                           View.OnClickListener listener){
        list1.add(new ClickAllAction(regularExpression, color, underLine, listener));
        return this;
    }

    public SpannableStringHelper imgBefore(String regularExpression, Drawable drawable){
        list2.add(new InsertImageBeforeAction(regularExpression, drawable));
        return this;
    }


    public SpannableStringHelper imgBeforeAll(String regularExpression, Drawable drawable){
        list2.add(new InsertImageBeforeAllAction(regularExpression, drawable));
        return this;
    }


    public SpannableStringHelper imgAfter(String regularExpression, Drawable drawable){
        list2.add(new InsertImageAfterAction(regularExpression, drawable));
        return this;
    }

    public SpannableStringHelper imgAfterAll(String regularExpression, Drawable drawable){
        list2.add(new InsertImageAfterAllAction(regularExpression, drawable));
        return this;
    }

    public void attachToTextView(TextView textView){

        int imgCount = 1;
        for (InsertImageAction action : list2) {
            String insert = "#<img"+imgCount+">#";
            info = action.insertReplaceFlag(info,insert);
            imgCount++;
        }

        LinkedList<int[]> skipIndexes = Strings.findAll(info, "#<img\\d+?>#");

        SpannableString ss = new SpannableString(info);
        boolean setMovementMethod = false;

        for (SSAction action : list1) {
            LinkedList<int[]> indexes = action.findIndexes(info,skipIndexes );
            for (int[] pair : indexes) {
                Object span = action.createSpan();
                ss.setSpan(span,pair[0], pair[1],
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                if (span instanceof ClickableSpan){
                    setMovementMethod = true;
                }
            }
        }

        for (SSAction action : list2) {
            LinkedList<int[]> indexes = action.findIndexes(info,skipIndexes );
            for (int[] pair : indexes) {
                ss.setSpan(action.createSpan(),pair[0], pair[1],
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        textView.setText(ss);
        if (setMovementMethod){
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

}
