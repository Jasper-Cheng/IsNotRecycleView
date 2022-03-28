package com.example.isnotrecycleview.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.example.isnotrecycleview.Data.ViewData;
import com.example.isnotrecycleview.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IsNotRecycleView extends ViewGroup {
    private float allow_scroll_distance;
    private float allow_up_distance=0;
    private float scroll_balance_factor=1.5f;
    private List<Integer> showIndex=new ArrayList<>();

    private List<ViewData> dataList=new ArrayList<>();

    public IsNotRecycleView(Context context) {
        super(context);
    }

    public IsNotRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IsNotRecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDataList(List<ViewData> list){
        showIndex.clear();
        dataList.clear();
        dataList=list;
        for(int i=0;i<dataList.size();i++){
            LinearLayout view= (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_view,null);
            ItemView itemView=view.findViewById(R.id.itemView);
            view.removeAllViews();
            addView(itemView);
        }
        requestLayout();
    }

    public void removeDesignPositionView(View view){
        Log.e("jasper","removeDesignPositionView");
        for(int i=0;i<getChildCount();i++){
            if(getChildAt(i)==view){
                Log.e("jasper","removeDesignPositionView view="+view);
                removeView(view);
                break;
            }
        }
    }

    public void setShowIndex(List<Integer> showIndex){
        this.showIndex=showIndex;
        requestLayout();
    }

    public void setCertainlyCount(int count){
        showIndex.clear();
        for(int i=0;i<count;i++){
            showIndex.add(i);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.e("jasper","dispatchTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("jasper","dispatchTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("jasper","dispatchTouchEvent ACTION_UP");
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private int mLastX;
    private int mLastY;
    boolean isIntercepted = false;
    private int arrayLength=3;
    private float[] lastXs=new float[arrayLength];
    private float[] lastYs=new float[arrayLength];
    private int count=0;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                down_position=ev.getRawY();
                Log.e("jasper","onInterceptTouchEvent ACTION_DOWN");
                mLastX = (int) ev.getX();
                mLastY = (int) ev.getY();
                for(int i=0;i<arrayLength;i++){
                    lastXs[i]=mLastX;
                    lastYs[i]=mLastY;
                    count++;
                }
                isIntercepted=false;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("jasper","onInterceptTouchEvent ACTION_MOVE");
                lastXs[count%arrayLength]=ev.getX();
                lastYs[count%arrayLength]=ev.getY();
                isIntercepted=needEvent(ev);
                count++;
                break;
            case MotionEvent.ACTION_UP:
                Log.e("jasper","onInterceptTouchEvent ACTION_UP");
                break;
        }
        Log.e("jasper","onInterceptTouchEvent lastX = " + mLastX + " lastY = " + mLastY);
        Log.e("jasper","onInterceptTouchEvent isIntercepted="+isIntercepted);
        return isIntercepted;
    }

    private boolean needEvent(MotionEvent ev) {
        //垂直滚动距离大于水平滚动距离则将事件交由ViewGroup处理
        mLastY= (int) lastYs[(count-2)%arrayLength];
        mLastX= (int) lastXs[(count-2)%arrayLength];
        return Math.abs(ev.getY() - mLastY)>Math.abs(ev.getX() - mLastX);
    }

    private void resetChildRightView(){
        for(int i=0;i<getChildCount();i++){
            ItemView itemView= (ItemView) getChildAt(i);
            itemView.resetRightSlidePosition();
        }
    }


    private float down_position;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.e("jasper","onTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("jasper","onTouchEvent ACTION_MOVE");
                float distance=(down_position-event.getRawY())/scroll_balance_factor;
                Log.e("jasper","distance="+distance);
                if(allow_up_distance<=allow_scroll_distance){
                    if(allow_up_distance+distance<=0){
                        scrollTo(0, 0);
                    }else if(allow_up_distance+distance<allow_scroll_distance){
                        scrollTo(0, (int) (allow_up_distance+distance));
                    }else{
                        scrollTo(0, (int) allow_scroll_distance);
                    }
                }
//                scroller.startScroll(scroller.getCurrX(),scroller.getCurrY(),0, (int) move_distance);
//                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.e("jasper","onTouchEvent ACTION_UP");
                float distance2=(down_position-event.getRawY())/scroll_balance_factor;
                Log.e("jasper","distance2="+distance2);
                if(allow_up_distance<=allow_scroll_distance){
                    if(allow_up_distance+distance2<=0){
                        scrollTo(0, 0);
                        allow_up_distance=0;
                    }else if(allow_up_distance+distance2<allow_scroll_distance){
                        scrollTo(0, (int) (allow_up_distance+distance2));
                        allow_up_distance=allow_up_distance+distance2;
                    }else{
                        scrollTo(0, (int) allow_scroll_distance);
                        allow_up_distance=allow_scroll_distance;
                    }
                }

                resetChildRightView();
                count=0;
                break;
        }
        return true;
    }

    public static int getScreenHeightPx(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
    /**
     * 获取状态栏高度
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    public int getTitleHeight(){
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)){
            return TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        return 0;
    }
//    @Override
//    public void computeScroll() {
//        if (scroller.computeScrollOffset()) {
//
//            //这里调用View的scrollTo()完成实际的滚动
//            scrollTo(scroller.getCurrX(), scroller.getCurrY());
//
//            //必须调用该方法，否则不一定能看到滚动效果
//            postInvalidate();
//        }
//        super.computeScroll();
//    }

    //    @Override
//    public LayoutParams generateLayoutParams(AttributeSet attrs) {
//        return new MarginLayoutParams(getContext(),attrs);
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight= MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec,heightMeasureSpec);

        int width = 0;
        int height = 0;
        int childCount=getChildCount();

        int cWidth;
        int cHeight;
        LinearLayout.LayoutParams cParams;

        for(int i=0;i<childCount;i++){
            if(showIndex.size()==0||showIndex.contains(i)){
                View view=getChildAt(i);
                cWidth=view.getMeasuredWidth();
                cHeight=view.getMeasuredHeight();
                cParams= (LinearLayout.LayoutParams) view.getLayoutParams();
                int w=cWidth+cParams.leftMargin+cParams.rightMargin;
                if(w>width)width=w;
                height=height+cHeight+cParams.topMargin+cParams.bottomMargin;
            }
        }

        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth
                : width, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
                : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childCount = getChildCount();
        int total = 0;

        for(int i=0;i<childCount;i++){
            if(showIndex.size()==0||showIndex.contains(i)){
                View childView=getChildAt(i);
                int cWidth = childView.getMeasuredWidth();
                int cHeight = childView.getMeasuredHeight();
                LinearLayout.LayoutParams cParams = (LinearLayout.LayoutParams) childView.getLayoutParams();
                int cl,ct,cr,cb;
                ct=total+cParams.topMargin;
                cb=ct+cHeight;
                cl=cParams.leftMargin;
                cr= cWidth-cParams.rightMargin;
                total=cb+cParams.bottomMargin;
                childView.layout(cl,ct,cr,cb);
            }
        }
        allow_scroll_distance=total-getScreenHeightPx(getContext())+getStatusBarHeight()+getTitleHeight();
        if(allow_scroll_distance<0)allow_scroll_distance=0;
        Log.e("jasper","allow_scroll_distance="+allow_scroll_distance);
    }
}
