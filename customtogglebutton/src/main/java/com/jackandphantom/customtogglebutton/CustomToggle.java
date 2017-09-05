package com.jackandphantom.customtogglebutton;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;


public class CustomToggle extends View {

   /* This paint is used to draw button */
   private Paint roundPaint = new Paint();

   /* changeBitmapColor is used to draw the color on bitmap */
   private Paint changeBitmapColor = new Paint();
   private Paint changeBitmapColor1 = new Paint();
   private boolean toggle = true;

   /* These are some other paint object which are used to draw the view*/
   private Paint firstIconPaint = new Paint();
   private Bitmap firstIcon, secondIcon;

   /* This width and height is equivalent to view width and height measure in onMeausure  */
   private int width, height;

   private RectF rect, roundRect,lastRect;
   private int commanFact;
   private Drawable drawable;
   private Drawable secondDrawable;

   /* These are the factor used to draw the bitmap on custom view */
   private int min, bitmapFact;
   private int axs, secondAxs;
   private int last;
   private boolean drawInside = false;
   private RectF rectF = new RectF();

   private float left , top, right, bottom;
   private OnToggleClickListener onToggleClickListener;
   private AnimatorSet animatorSet = new AnimatorSet();

   /*
   * These variable sets are used for public getter and setter method
   * */
   private int slideBackgroundColor, slideColor;
   private int magnification = 8, animationTime = 600;
   private int val;
   private int scalingFactor, commanfHalf;


   private AnimatorSet set = new AnimatorSet();
   private int roX=0,roY=0;
   private int animationType;
   private boolean checkAnimationType = false;

   private Context context;


    public CustomToggle(Context context) {
        super(context);
        this.context = context;
    }

    public CustomToggle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }


    public CustomToggle(Context context, AttributeSet attrs, int i) {
        super(context, attrs, i);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomToggle);
        drawable = array.getDrawable(R.styleable.CustomToggle_ctg_addLeftIcon);
        secondDrawable = array.getDrawable(R.styleable.CustomToggle_ctg_addRightIcon);
        slideBackgroundColor = array.getColor(R.styleable.CustomToggle_ctg_addSlideBackgroundColor, Color.DKGRAY);
        slideColor = array.getColor(R.styleable.CustomToggle_ctg_addSlideColor, Color.BLUE);
        magnification = array.getInt(R.styleable.CustomToggle_ctg_addMagnification, magnification);
        animationTime = array.getInt(R.styleable.CustomToggle_ctg_addAnimationTime, animationTime);
        animationType = array.getInt(R.styleable.CustomToggle_ctg_addAnimationType, 0);
        array.recycle();
        init();
        if (magnification != 8)
            requestLayout();
    } //

    // Here we initiliaze and calculates some values for animations
    private void init() {
        if (firstIcon == null || secondIcon == null && width  == 0 && height == 0)
            return;

        roundPaint.setAntiAlias(true);
        roundPaint.setStyle(Paint.Style.FILL);

        changeBitmapColor.setColor(Color.GREEN);

        roundPaint.setColor(slideBackgroundColor);
        firstIconPaint.setAntiAlias(true);
        firstIconPaint.setStyle(Paint.Style.FILL);
        firstIconPaint.setColor(slideColor);

        changeBitmapColor.setAntiAlias(true);
        changeBitmapColor.setStyle(Paint.Style.FILL);
        changeBitmapColor.setStrokeWidth(60);
        PorterDuffColorFilter p1 = new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        PorterDuffColorFilter p = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
        changeBitmapColor1.setColorFilter(p);
        changeBitmapColor.setColorFilter(p1);


        changeBitmapColor1.setAntiAlias(true);
        changeBitmapColor1.setStyle(Paint.Style.FILL);
        changeBitmapColor1.setStrokeWidth(30);

        rect = new RectF(0, 0 , width, commanFact);

        roundRect = new RectF(0, 0, commanFact, commanFact);

        last = width  - commanFact;
        commanfHalf = commanFact /2;
        val = width - last;
        scalingFactor = last + (val/2);
        lastRect = new RectF(last, 0, width , commanFact);
        secondAxs = (int) (width - (lastRect.width()/2));
        secondAxs -= bitmapFact/2;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {


        canvas.drawRoundRect(rect, width/5, width/5, roundPaint);
        if (drawInside) {

            canvas.drawRoundRect(rectF, width /6, width / 6, firstIconPaint);
        }
        if (!checkAnimationType)
            canvas.drawOval(roundRect, firstIconPaint);
        else
            canvas.drawRoundRect(roundRect,width/6,width/6,firstIconPaint);


        canvas.drawBitmap(firstIcon, axs/2,axs/2 , changeBitmapColor);
        canvas.drawBitmap(secondIcon, secondAxs, axs/2, changeBitmapColor1);


    }

   /**
    * Here we measure the size of the view and also get the icons from drawable
    * and one more important thing is to calculate the commanFact which is used to
    * draw the rectangle and bitmap poistion
    * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        min = Math.min(width , height);
        setMeasuredDimension(min, min);
        calculateSomeValue();
        if (drawable != null && secondDrawable != null) {
            firstIcon = getDrawableToBitmap(drawable);
            secondIcon = getDrawableToBitmap(secondDrawable);
        }
        init();

    }

    /* Here we make conditions for checking touch should be placed inside the button */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :

                float x = event.getX();
                float y = event.getY();
                if (x <= rect.width() && y <= rect.height()) {

                    clickOnButton();
                }


        }

        return true;
    }

    /*
    *  This method is triggerd whenever the touch event call to animate the button
    * */

    private void clickOnButton() {
        if (toggle) {
            toggle = false;
            PorterDuffColorFilter p1 = new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            PorterDuffColorFilter p = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
            changeBitmapColor1.setColorFilter(p1);
            changeBitmapColor.setColorFilter(p);    //

            if( onToggleClickListener != null) {
                onToggleClickListener.onLefToggleEnabled(false);
                onToggleClickListener.onRightToggleEnabled(true);
            }
            switch (animationType) {
                case 0 :
                    playAnimation1();
                    checkAnimationType = false;
                    break;
                case 1:
                    setProgressWithAnimation();
                    checkAnimationType = true;
                    break;
                case 2:
                    collectionAnimation();
                    checkAnimationType = false;
                    break;
            }




        } else {
            toggle = true;
            PorterDuffColorFilter p1 = new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            PorterDuffColorFilter p = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
            changeBitmapColor.setColorFilter(p1);
            changeBitmapColor1.setColorFilter(p);
            if( onToggleClickListener != null) {
                onToggleClickListener.onRightToggleEnabled(false);
                onToggleClickListener.onLefToggleEnabled(true);
            }
            switch (animationType) {
                case 0 :
                    playAnimation2();
                    checkAnimationType = false;
                    break;
                case 1:
                    setProgressWithAnimation2();
                    checkAnimationType = true;
                    break;
                case 2:
                    inverseCollection();
                    checkAnimationType = false;
                    break;
            }

        }

    }

    /* Interface for Checking which side of button  is enabled */
    interface OnToggleClickListener {
        void onLefToggleEnabled(boolean enabled);
        void onRightToggleEnabled(boolean enabled);
    }

    /* To add listener in your activity  */
    public void setOnToggleClickListener(OnToggleClickListener onToggleClickListener) {
        this.onToggleClickListener = onToggleClickListener;
    }



    /**
     * This is jack animation here we caculated distance and height and width of the button
     * and then according to their we make the animations
     * */
    private void playAnimation1() {
        // This is jack animation
        roundRect.setEmpty();
        rectF.setEmpty();
        AnimatorSet animatorSet = new AnimatorSet();
        if (animatorSet.isRunning()) {
            animatorSet.end();
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, width/9);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animationFactor = (int) valueAnimator.getAnimatedValue();
                if (animationFactor <= commanFact/4)
                    roundRect.set(animationFactor, animationFactor, commanFact - animationFactor, commanFact - animationFactor);
                left = roundRect.left;
                right = roundRect.right;
                top = roundRect.top;
                bottom = roundRect.bottom;
                invalidate();
            }
        });

        ValueAnimator valueAnimator1 = ValueAnimator.ofInt(0, 180);
        valueAnimator1.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int factor = (int) valueAnimator.getAnimatedValue();
                factor *= 6;
                rectF.set(left, top, right+ factor, bottom);
                drawInside = true;
                invalidate();
            }
        });


        ValueAnimator valueAnimator2 = ValueAnimator.ofInt(0, width/9) ;
        valueAnimator2.setInterpolator(new DecelerateInterpolator());
        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animationFactor = (int) valueAnimator.getAnimatedValue();
                animationFactor *= 6;
                rectF.set(right + animationFactor, rectF.top, rectF.right, rectF.bottom);
                drawInside = true;
                invalidate();

            }
        });

        ValueAnimator valueAnimator3 = ValueAnimator.ofInt(0, val/2);
        valueAnimator3.setInterpolator(new DecelerateInterpolator());
        valueAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int factor = (int) valueAnimator.getAnimatedValue();
                roundRect.set(scalingFactor - factor, commanfHalf - factor, scalingFactor + factor
                        , commanfHalf+factor);
                invalidate();
            }
        });
        animatorSet.play(valueAnimator).before(valueAnimator2).before(valueAnimator3);
        animatorSet.setDuration(animationTime);
        animatorSet.start();

    }

    /* This is back of jack animation */
    private void playAnimation2() {
        roundRect.setEmpty();
        rectF.setEmpty();
        AnimatorSet animatorSet = new AnimatorSet();
        if (animatorSet.isRunning())
            animatorSet.end();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, width/9);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animationFactor = (int) valueAnimator.getAnimatedValue();
                if (animationFactor <= commanFact/4)
                    roundRect.set(last + animationFactor, animationFactor, width - animationFactor, commanFact - animationFactor);
                left = roundRect.left;
                right = roundRect.right;
                top = roundRect.top;
                bottom = roundRect.bottom;

            }
        });

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int factor = (int) valueAnimator.getAnimatedValue();
                factor *= 6;
                rectF.set(left - factor, top, right, bottom);
                drawInside = true;
                invalidate();
            }
        });
        ValueAnimator valueAnimator2 = ValueAnimator.ofInt(0, width/8) ;
        valueAnimator2.setInterpolator(new DecelerateInterpolator());
        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animationFactor = (int) valueAnimator.getAnimatedValue();
                animationFactor *= 6;
                rectF.set(rectF.left , rectF.top, right - animationFactor, rectF.bottom);
                drawInside = true;
                invalidate();
            }
        });

        ValueAnimator valueAnimator3 = ValueAnimator.ofInt(0, commanfHalf);
        valueAnimator3.setInterpolator(new DecelerateInterpolator());
        valueAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animationFactor = (int) valueAnimator.getAnimatedValue();

                roundRect.set(commanfHalf - animationFactor, commanfHalf - animationFactor, commanfHalf + animationFactor
                        , commanfHalf + animationFactor);
                invalidate();
            }
        });




        animatorSet.play(valueAnimator).before(valueAnimator2).before(valueAnimator3);
        animatorSet.setDuration(animationTime);
        animatorSet.start();

    }

    /***
     * By using this method we are getting the bitmap from drawable
     */
    @NonNull
    private Bitmap getDrawableToBitmap(Drawable drawable) {
        try {
            Bitmap bitmap;

            bitmap = Bitmap.createBitmap(bitmapFact, bitmapFact, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            // Handle the error
            e.printStackTrace();
            return null;
        }

    }
    /* These values are used to draw circle and icon to it's exact position
     * Use these values if you want to make your own custom view
     * it's really gone help you
      * */
    private void calculateSomeValue() {
        commanFact = width/3;
        bitmapFact  = width/magnification;
        axs = commanFact - bitmapFact;

    }



    private void setScaleX0(int scale){
        roundRect.set(roX+scale,roY+scale,commanFact-scale,commanFact-scale);
        invalidate();
    }




    private void setScaleX1(int scale){


        roundRect.set(roX+(scale)/9,roY+(scale)/18,commanFact+commanFact/5+(scale)/2,commanFact-(scale)/18);


        invalidate();
    }




    private void setScaleX3(int scale){

       /*
          int last = width  - commanFact;
        lastRect = new RectF(last, 0, width, commanFact);
        */

        roundRect.set(commanFact/3+scale*5,commanFact/6-scale/2,commanFact-commanFact/6+(width-commanFact)+scale/2, commanFact-commanFact/6+(scale)/2);

        invalidate();

    }

    /**
     * This is phantom animation here we caculated distance and height and width of the button
     * and then according to their we make the animations
     * */
    private void setProgressWithAnimation(){



        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "scaleX0",0,commanFact/5);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofInt(this, "scaleX1",0,width);
        ObjectAnimator objectAnimator3 =  ObjectAnimator.ofInt(this, "scaleX3", 0,(commanFact)/3);
        objectAnimator3.setInterpolator(new BounceInterpolator());


        set.play(objectAnimator).with(objectAnimator1).before(objectAnimator3);
        set.setDuration(animationTime);
        set.start();

    }


    /**
     * back animation of phantom animation
     */

    private void setProgressWithAnimation2(){


        ObjectAnimator objectAnimatorO = ObjectAnimator.ofInt(this, "scaleX4",0,commanFact/5);
        ObjectAnimator objectAnimatorO1 = ObjectAnimator.ofInt(this, "scaleX5",0,commanFact/5);
        ObjectAnimator objectAnimatorO2 = ObjectAnimator.ofInt(this, "scaleX6",0,commanFact/5);
        objectAnimatorO2.setInterpolator(new BounceInterpolator());

        set.setDuration(animationTime);
        set.play(objectAnimatorO).with(objectAnimatorO1).before(objectAnimatorO2);

        set.start();
    }


    private void setScaleX4(int scale){
        roundRect.set((width-commanFact)+scale,roY+scale,width-scale,commanFact-scale);
        invalidate();
    }

    private void setScaleX5(int scale){
        roundRect.set((width-commanFact)-scale*10,roY+scale,width-scale,commanFact-scale);
        invalidate();
    }

    private void setScaleX6(int scale){
        roundRect.set(0,commanFact/5-scale,width-commanFact/5-scale*9,commanFact-commanFact/5+scale);
        invalidate();
    }



    /**
     * This is extra animation here we caculated distance and height and width of the button
     * and then according to their we make the animations
     * */

    private void collectionAnimation() {
        drawInside  =false;
        if (animatorSet.isRunning())
            animatorSet.end();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, commanfHalf);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animationFactor = (int) valueAnimator.getAnimatedValue();
                roundRect.set(animationFactor, animationFactor, commanFact - animationFactor, commanFact - animationFactor);
                invalidate();
            }
        });
        ValueAnimator valueAnimator2 = ValueAnimator.ofInt(0, val/2) ;
        valueAnimator2.setInterpolator(new DecelerateInterpolator());
        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animationFactor = (int) valueAnimator.getAnimatedValue();

                roundRect.set(scalingFactor - animationFactor, commanfHalf - animationFactor, scalingFactor + animationFactor
                        , commanfHalf+animationFactor);
                invalidate();
            }
        });

        animatorSet.play(valueAnimator).before(valueAnimator2);
        animatorSet.setDuration(500);

        animatorSet.start();
    }

    private void inverseCollection() {

        drawInside  =false;
        if (animatorSet.isRunning())
            animatorSet.end();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, commanfHalf);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animationFactor = (int) valueAnimator.getAnimatedValue();

                roundRect.set(commanfHalf - animationFactor, commanfHalf - animationFactor, commanfHalf + animationFactor
                        , commanfHalf + animationFactor);
                invalidate();
            }
        });

        ValueAnimator valueAnimator2 = ValueAnimator.ofInt(0, val/2) ;
        valueAnimator2.setInterpolator(new DecelerateInterpolator());
        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animationFactor = (int) valueAnimator.getAnimatedValue();

                roundRect.set(last + animationFactor, animationFactor, width - animationFactor, commanFact-animationFactor);
                invalidate();

            }
        });
        AnimatorSet set = new AnimatorSet();
        set.play(valueAnimator2).before(valueAnimator);
        set.setInterpolator(new AccelerateInterpolator());
        set.setDuration(600);
        set.start();
    }

    /*
    * This is getter and setter region
    * */

    public void setSlideBackgroundColor(int color) {
        roundPaint.setColor(color);
        invalidate();
    }

    public void setSlideColor(int color) {
        firstIconPaint.setColor(color);
        invalidate();
    }

    public void setMagnification(int magnify) {
        this.magnification = magnify;
        requestLayout();
    }

    public void setAnimationTime(int animationTime) {
        this.animationTime = animationTime;
    }

    public int getAnimationTime() {
        return this.animationTime;
    }

    public int getSlideColor() {
        return this.slideColor;
    }

    public int getSlideBackgroundColor() {
        return this.slideBackgroundColor;
    }

    public void addFirstIcon(Drawable drawable) {
        this.drawable = drawable;
        requestLayout();
    }

    public void addSecondIcon(Drawable drawable) {
        this.secondDrawable = drawable;
        requestLayout();
    }

    public void addFirstIcon(int resId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable =  context.getResources().getDrawable(resId);
        } else {
            drawable =  context.getResources().getDrawable(resId, null);
        }
        requestLayout();
    }

    public void addSecondIcon(int resId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            secondDrawable =  context.getResources().getDrawable(resId);
        } else {
            secondDrawable =  context.getResources().getDrawable(resId, null);
        }
        requestLayout();
    }
}
