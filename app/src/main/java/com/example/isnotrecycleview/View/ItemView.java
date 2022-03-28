package com.example.isnotrecycleview.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.isnotrecycleview.R;

public class ItemView extends View {
    private int head_image_width=120;
    private int head_image_height=120;
    private int describe_paint_size=22;

    private int right_slide_distance=0;

    public ItemView(Context context) {
        super(context);
    }

    public ItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float down_point=0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.e("jasper","cl-onTouchEvent ACTION_DOWN");
                down_point=event.getX();
                if(getWidth()-down_point<=right_slide_distance){
                    Log.e("jasper","删除");
                    IsNotRecycleView view= (IsNotRecycleView) getParent();
                    view.removeDesignPositionView(this);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("jasper","cl-onTouchEvent ACTION_MOVE");
                right_slide_distance= (int) (down_point-event.getX());
                Log.e("jasper","cl-right_slide_distance="+right_slide_distance);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.e("jasper","cl-onTouchEvent ACTION_UP");
                if(right_slide_distance>300){
                    right_slide_distance=200;
                }else{
                    right_slide_distance=0;
                }
                invalidate();
                break;
        }
        return true;
    }

    public void resetRightSlidePosition(){
        Log.e("jasper","cl-resetRightSlidePosition");
        if(right_slide_distance==200){
            return;
        }
        if(right_slide_distance>300){
            right_slide_distance=200;
        }else{
            right_slide_distance=0;
        }
        invalidate();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.e("jasper","cl-dispatchTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("jasper","cl-dispatchTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("jasper","cl-dispatchTouchEvent ACTION_UP");
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        if(heightMode==MeasureSpec.EXACTLY){
            height=MeasureSpec.getSize(heightMeasureSpec);
        }else{
            height=200;
        }
        if(widthMode==MeasureSpec.EXACTLY){
            width=MeasureSpec.getSize(widthMeasureSpec);
        }else{
            width=200;
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        head_image_width=(right-left)/10;
        head_image_height=(bottom-top)/2;
        describe_paint_size=(right-left)/45;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景
        Paint bg_paint=new Paint();
        bg_paint.setColor(Color.parseColor("#ffffff"));
        RectF bg_rect=new RectF();
        bg_rect.left=0;
        bg_rect.right=getWidth();
        bg_rect.top=0;
        bg_rect.bottom=getHeight();
        canvas.drawRoundRect(bg_rect,20,20,bg_paint);

        //画头像
        canvas.drawBitmap(fillet(getBitmap(),100),getWidth()/30,getHeight()/2-head_image_height/2,null);

        Paint describe_paint=new Paint();
        describe_paint.setColor(Color.parseColor("#FFBB86FC"));
        describe_paint.setTextSize(describe_paint_size);
        String describe_text="hi,guys,would your like happy write code with me?come on,take me flying!";
        canvas.drawText(describe_text,getWidth()/20+head_image_width+20,getHeight()/2-describe_paint.getFontMetrics().ascent/2,describe_paint);
        if(right_slide_distance>=0){
            //画右滑的背景
            Paint rightSlideBG=new Paint();
            rightSlideBG.setColor(Color.parseColor("#FF0000"));
            RectF bg_right_rect=new RectF();
            bg_right_rect.left=getWidth()-right_slide_distance;
            bg_right_rect.right=getWidth();
            bg_right_rect.top=0;
            bg_right_rect.bottom=getHeight();
            canvas.drawRoundRect(bg_right_rect,20,20,rightSlideBG);

            //画右滑的文字
            Paint rightSlideText=new Paint();
            rightSlideText.setTextSize(36);
            rightSlideText.setColor(Color.parseColor("#000000"));
            rightSlideText.setTypeface(Typeface.DEFAULT_BOLD);
            String delete="删除";
            canvas.drawText(delete,getWidth()-right_slide_distance/2,getHeight()/2-rightSlideText.getFontMetrics().ascent/2,rightSlideText);
        }

    }

    public static Bitmap fillet(Bitmap bitmap, int roundPx) {
        try {
            // 其原理就是：先建立一个与图片大小相同的透明的Bitmap画板
            // 然后在画板上画出一个想要的形状的区域。
            // 最后把源图片帖上。
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();

            Bitmap paintingBoard = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(paintingBoard);
            canvas.drawARGB(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);

            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);

            //画出4个圆角
            final RectF rectF = new RectF(0, 0, width, height);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            //帖子图
            final Rect src = new Rect(0, 0, width, height);
            final Rect dst = src;
            canvas.drawBitmap(bitmap, src, dst, paint);
            return paintingBoard;
        } catch (Exception exp) {
            return bitmap;
        }
    }


    private Bitmap getBitmap() {
        Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.mipmap.myself);
        Matrix matrix = new Matrix();
        float xl= (float) (Math.round(head_image_width*100/bitmap.getWidth())/100.0);
        float yl= Math.min((float) (Math.round(head_image_height * 100 / bitmap.getHeight()) / 100.0), xl);
        if(xl>0&&yl>0){
            matrix.setScale(xl, yl);
        }
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);

    }
}
