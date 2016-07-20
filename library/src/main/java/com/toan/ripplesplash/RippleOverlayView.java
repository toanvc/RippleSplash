package com.toan.ripplesplash;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;

/**
 * Created by Toan Vu on 7/19/16.
 */
public class RippleOverlayView extends View {
    private final float INIT_RADIUS = 10f;

    private Bitmap bitmap;
    private AnimatorSet animatorSet;

    private boolean animationRunning;
    private int rippleColor;
    private int alpha;
    private int duration = 1000; //default for animation duration

    private int width;
    private int height;

    public RippleOverlayView(Context context) {
        super(context);
        init(context, null);
    }

    public RippleOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RippleOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RippleOverlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (bitmap == null) {
            createWindowFrame();
        }
        canvas.drawBitmap(bitmap, 0, 0, null);
        startAnim();
    }

    protected void createWindowFrame() {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas osCanvas = new Canvas(bitmap);

        RectF outerRectangle = new RectF(0, 0, width, height);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(rippleColor);
        paint.setAlpha(alpha);
        osCanvas.drawRect(outerRectangle, paint);

        paint.setColor(Color.TRANSPARENT);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        float centerX = width / 2;
        float centerY = height / 2;
        float radius = INIT_RADIUS;
        osCanvas.drawCircle(centerX, centerY, radius, paint);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        bitmap = null;
        width = getWidth();
        height = getHeight();
        prepareAnimator();
    }

    public void release() {
        stopAnim();
        animatorSet = null;
        bitmap = null;
    }

    private void init(Context context, AttributeSet attrs) {
        if (isInEditMode())
            return;

        if (attrs != null) {

            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleBackground);
            //default ripple color is white
            rippleColor = typedArray.getColor(R.styleable.RippleBackground_rp_color, Color.WHITE);
            alpha = typedArray.getInt(R.styleable.RippleBackground_rp_alpha, 255);
            duration = typedArray.getInt(R.styleable.RippleBackground_rp_duration, 1000);
            if (duration > 5000) {
                duration = 5000;
            }
            typedArray.recycle();
        }

    }

    private void prepareAnimator() {
        ArrayList<Animator> animatorList = new ArrayList<>();
        animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        int scale = max(width, height);
        final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(this, "ScaleX", 1.0f, 2 * scale / INIT_RADIUS);
        scaleXAnimator.setDuration(duration);
        animatorList.add(scaleXAnimator);
        final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(this, "ScaleY", 1.0f, 2 * scale / INIT_RADIUS);
        scaleYAnimator.setDuration(duration);
        animatorList.add(scaleYAnimator);

        animatorSet.playTogether(animatorList);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setVisibility(View.GONE);
                animationRunning = false;
                bitmap = null;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void startAnim() {
        stopAnim();
        animatorSet.start();
        animationRunning = true;
    }


    private void stopAnim() {
        if (isRippleAnimationRunning()) {
            animatorSet.end();
            animationRunning = false;
        }
    }

    private boolean isRippleAnimationRunning() {
        return animationRunning;
    }

    private int max(int w, int h) {
        return w < h ? h : w;
    }
}