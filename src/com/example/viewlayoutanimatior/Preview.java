package com.example.viewlayoutanimatior;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class Preview extends ViewGroup {

    public Preview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //继承自viewgroup
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }
   /* @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    //继承自view
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
