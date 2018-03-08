package com.example.pcqpcq.imagevideoeffects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;


public class SlideShow {
    // effect
    public static final int NONE = 0;
    public static final int FADE_IN = 1;
    public static final int ROTATE = 2;
    public static final int SLIDE_IN = 3;

    // 위치
    private static int curStartX;
    private static int curStartY;
    private static int prevStartX;
    private static int prevStartY;

    //Variables used for effect
    private static float in_alpha = 0f;
    private static float rotate = 0;
    private static int slideX = SlideEncoder.WIDTH;
    private static int slideCount = 1;
    private static float out_alpha = 255f;

    /**
     *
     * @param canvas canvas
     * @param prevBm Background Bitmap
     * @param curBm  Foregound Bitmap
     */
    public static void draw(Canvas canvas, Bitmap prevBm, Bitmap curBm) {
        if (canvas == null || curBm == null) return;

        setLocation(prevBm, curBm);

        if (SlideApplication.SLIDE_EFFECT == ROTATE && prevBm == null)
            canvas.drawColor(Color.BLACK);

        if (prevBm != null) drawFadeOut(canvas, prevBm);
        switch (SlideApplication.SLIDE_EFFECT) {
            case NONE:
                drawNone(canvas, curBm);
                break;
            case FADE_IN:
                drawFadeIn(canvas, curBm);
                break;
            case ROTATE:
                drawRotate(canvas, curBm);
                break;
            case SLIDE_IN:
                drawSlideIn(canvas, curBm);
                break;
            default:
                throw new IllegalStateException("unexpected state");
        }
    }

    /*
     *Adjust position according to figure
     * @param prevBm Background Bitmap
     * @param curBm  Foreground Bitmap
     */
    private static void setLocation(Bitmap prevBm, Bitmap curBm) {
        if (curBm != null) {
            int cWidth = curBm.getWidth();
            int cHeight = curBm.getHeight();

            if (cWidth > cHeight) {
                curStartX = 0;
                curStartY = (SlideEncoder.HEIGHT - cHeight) / 2;
            } else if (cHeight > cWidth) {
                curStartX = (SlideEncoder.WIDTH - cWidth) / 2;
                curStartY = 0;
            } else {
                curStartX = 0;
                curStartY = 0;
            }
        }

        if (prevBm != null) {
            int pWidth = prevBm.getWidth();
            int pHeight = prevBm.getHeight();
            if (pWidth > pHeight) {
                prevStartX = 0;
                prevStartY = (SlideEncoder.HEIGHT - pHeight) / 2;
            } else if (pHeight > pWidth) {
                prevStartX = (SlideEncoder.WIDTH - pWidth) / 2;
                prevStartY = 0;
            } else {
                prevStartX = 0;
                prevStartY = 0;
            }
        }
    }

    /**
     */
    public static void init() {
        in_alpha = 0f;
        out_alpha = 255f;
        rotate = 0f;
        slideX = 800;
        slideCount = 1;
        curStartX = 0;
        curStartY = 0;
        prevStartX = 0;
        prevStartY = 0;
    }

    /**
     * drawNone
     *
     * @param c  canvas
     * @param bm bitmap
     */
    private static void drawNone(Canvas c, Bitmap bm) {
        c.drawBitmap(bm, curStartX, curStartY, null);
    }


//     * Fade in effect    *


    private static void drawFadeIn(Canvas c, Bitmap bm) {
        Paint p = new Paint();
        int ratio = (int) Math.ceil(255 / SlideApplication.FRAME_PER_SEC);
        in_alpha += ratio;
        if (in_alpha > 255f) in_alpha = 255f;
        p.setAlpha((int) in_alpha);
        c.drawBitmap(bm, curStartX, curStartY, p);
    }


//     Rotate effect

    private static void drawRotate(Canvas c, Bitmap bm) {
        Matrix matrix = new Matrix();
        matrix.preTranslate(curStartX, curStartY);
        float ratio = 360 / SlideApplication.FRAME_PER_SEC;
        rotate += Math.ceil(ratio);
        if (rotate > 360) rotate = 360;
        matrix.postRotate(rotate, SlideEncoder.WIDTH / 2, SlideEncoder.HEIGHT / 2);
        c.drawBitmap(bm, matrix, null);
    }

    /**
     * drawSlideIn drawSlideIn
     *
     * @param c  canvas
     * @param bm bitmap
     */

    private static void drawSlideIn(Canvas c, Bitmap bm) {
        Matrix matrix = new Matrix();
        int ratio = 1;
        if (slideCount < 30) ratio = (int) Math.pow(slideCount++, 1.4);
        slideX -= ratio;
        if (slideX < curStartX) slideX = curStartX;
        matrix.setTranslate(slideX, curStartY);
        c.drawBitmap(bm, matrix, null);

    }

    /**
     *
     Fade-out effect (applies only to pictures that are drawn behind)
     *
     * @param c  canvas
     * @param bm bitmap
     */

    private static void drawFadeOut(Canvas c, Bitmap bm) {
        c.drawColor(Color.BLACK);
        Paint p = new Paint();
        int ratio = (int) Math.ceil(255 / SlideApplication.FRAME_PER_SEC);
        out_alpha -= ratio;
        if (out_alpha < 0f) out_alpha = 0f;
        p.setAlpha((int) out_alpha);
        c.drawBitmap(bm, prevStartX, prevStartY, p);
    }
}