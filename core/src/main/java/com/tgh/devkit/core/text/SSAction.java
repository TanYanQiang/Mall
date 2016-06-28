package com.tgh.devkit.core.text;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;

import com.tgh.devkit.core.primitives.Ints;
import com.tgh.devkit.core.utils.Strings;

import java.util.LinkedList;

/**
 *
 * Created by albert on 16/1/29.
 */
abstract class SSAction {

    protected String content;


    SSAction(String content) {
        this.content = content;
    }

    abstract Object createSpan();

    abstract LinkedList<int[]> findIndexes(String info, LinkedList<int[]> skipIndexes);

    protected LinkedList<int[]> findFirst(String info, LinkedList<int[]> skipIndexes){
        int[] first = null;
        LinkedList<int[]> all = Strings.findAll(info, content);

        boolean flag;
        for (int[] item : all) {
            flag = true;
            for (int[] skipItem : skipIndexes) {
                if (Ints.isIn(item,skipItem)){
                    flag = false;
                    break;
                }
            }
            if (flag){
                first = item;
                break;
            }
        }

        LinkedList<int[]> result = new LinkedList<>();
        if (first!=null){
            result.add(first);
        }
        return result;
    }

    protected LinkedList<int[]> findAll(String info){
        return Strings.findAll(info,content);
    }

    static class BoldAction extends SSAction{

        BoldAction(String content) {
            super(content);
        }

        @Override
        Object createSpan() {
            return new StyleSpan(Typeface.BOLD);
        }

        @Override
        LinkedList<int[]> findIndexes(String info, LinkedList<int[]> skipIndexes) {
            return findFirst(info,skipIndexes);
        }
    }

    static class BoldAllAction extends SSAction{

        BoldAllAction(String content) {
            super(content);
        }

        @Override
        Object createSpan() {
            return new StyleSpan(Typeface.BOLD);
        }

        @Override
        LinkedList<int[]> findIndexes(String info, LinkedList<int[]> skipIndexes) {
            return findAll(info);
        }
    }

    static class ForegroundColorAction extends SSAction{

        protected final int color;

        ForegroundColorAction(int color,String content) {
            super(content);
            this.color = color;
        }

        @Override
        Object createSpan() {
            return new ForegroundColorSpan(color);
        }

        @Override
        LinkedList<int[]> findIndexes(String info, LinkedList<int[]> skipIndexes) {
            return findFirst(info,skipIndexes );
        }
    }

    static class ForegroundColorAllAction extends ForegroundColorAction{


        ForegroundColorAllAction(int color,String content) {
            super(color,content);
        }

        @Override
        LinkedList<int[]> findIndexes(String info, LinkedList<int[]> skipIndexes) {
            return findAll(info);
        }
    }

    static class RelativeSizeAction extends SSAction{

        protected final float size;

        RelativeSizeAction(float size, String content) {
            super(content);
            this.size = size;
        }

        @Override
        Object createSpan() {
            return new RelativeSizeSpan(size);
        }

        @Override
        LinkedList<int[]> findIndexes(String info, LinkedList<int[]> skipIndexes) {
            return findFirst(info,skipIndexes );
        }
    }

    static class RelativeSizeAllAction extends RelativeSizeAction{

        RelativeSizeAllAction(float size, String content) {
            super(size, content);
        }

        @Override
        LinkedList<int[]> findIndexes(String info, LinkedList<int[]> skipIndexes) {
            return findAll(info);
        }
    }

    static class ClickAction extends SSAction{

        final int color;
        final boolean underLine;
        final View.OnClickListener listener;

        ClickAction(String content,
                    int color, boolean underLine,
                    View.OnClickListener listener) {
            super(content);
            this.color = color;
            this.underLine = underLine;
            this.listener = listener;
        }

        @Override
        Object createSpan() {
            return new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    listener.onClick(widget);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(color);
                    ds.setUnderlineText(underLine);
                }
            };
        }

        @Override
        LinkedList<int[]> findIndexes(String info, LinkedList<int[]> skipIndexes) {
            return findFirst(info, skipIndexes);
        }

    }

    static class ClickAllAction extends ClickAction{

        ClickAllAction(String content,int color, boolean underLine,
                       View.OnClickListener listener) {
            super(content, color, underLine, listener);
        }

        @Override
        LinkedList<int[]> findIndexes(String info, LinkedList<int[]> skipIndexes) {
            return findAll(info);
        }
    }

    static abstract class InsertImageAction extends SSAction{

        final Drawable drawable;
        String replaceFlag;

        InsertImageAction(String content, Drawable drawable) {
            super(content);
            this.drawable = drawable;
        }

        abstract String insertReplaceFlag(String info, String replaceFlag);

        protected LinkedList<int[]> findFirst(String info, LinkedList<int[]> skipIndexes){
            int[] first = Strings.findFirst(info,replaceFlag);
            LinkedList<int[]> result = new LinkedList<>();
            if (first!=null){
                result.add(first);
            }
            return result;
        }

        protected LinkedList<int[]> findAll(String info){
            return Strings.findAll(info,replaceFlag);
        }

        @Override
        Object createSpan() {
            return new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        }
    }


    static class InsertImageBeforeAction extends InsertImageAction{

        InsertImageBeforeAction(String content, Drawable drawable) {
            super(content, drawable);
        }

        String insertReplaceFlag(String info, String replaceFlag){
            this.replaceFlag = replaceFlag;
            return Strings.insertBefore(info, content, replaceFlag);
        }

        @Override
        LinkedList<int[]> findIndexes(String info, LinkedList<int[]> skipIndexes) {
            return findFirst(info,skipIndexes );
        }
    }

    static class InsertImageBeforeAllAction extends InsertImageAction{

        InsertImageBeforeAllAction(String content, Drawable drawable) {
            super(content,drawable);
        }

        String insertReplaceFlag(String info, String replaceFlag){
            this.replaceFlag = replaceFlag;
            return Strings.insertBeforeAll(info, content, replaceFlag);
        }

        @Override
        LinkedList<int[]> findIndexes(String info, LinkedList<int[]> skipIndexes) {
            return findAll(info);
        }
    }


    static class InsertImageAfterAction extends InsertImageAction{

        InsertImageAfterAction(String content, Drawable drawable) {
            super(content, drawable);
        }

        String insertReplaceFlag(String info, String replaceFlag){
            this.replaceFlag = replaceFlag;
            return Strings.insertAfter(info, content, replaceFlag);
        }

        @Override
        LinkedList<int[]> findIndexes(String info, LinkedList<int[]> skipIndexes) {
            return findFirst(info, skipIndexes);
        }
    }


    static class InsertImageAfterAllAction extends InsertImageAction{

        InsertImageAfterAllAction(String content, Drawable drawable) {
            super(content,drawable);
        }

        String insertReplaceFlag(String info, String replaceFlag){
            this.replaceFlag = replaceFlag;
            return Strings.insertAfterAll(info, content, replaceFlag);
        }

        @Override
        LinkedList<int[]> findIndexes(String info, LinkedList<int[]> skipIndexes) {
            return findAll(info);
        }

    }

}
