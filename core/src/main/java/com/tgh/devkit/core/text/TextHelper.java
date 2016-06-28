package com.tgh.devkit.core.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 *
 * Created by albert on 16/1/8.
 */
public class TextHelper {

    private TextHelper() {}

    private static final Rect textBounds = new Rect();

    /**
     * 以一个中心点绘制文字
     */
    public static void drawTextCentred(Canvas canvas, Paint paint,
                                       String text, float cx, float cy){
        paint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, cx - textBounds.exactCenterX(),
                cy - textBounds.exactCenterY(),
                paint);
    }
}
