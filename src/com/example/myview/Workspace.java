package com.example.myview;

import com.example.viewlayoutanimatior.R;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;

public class Workspace  extends ViewGroup{

	public Workspace(Context context) {
		super(context);
	}
	public Workspace(Context context , AttributeSet s) {
		super(context,s);
	}

	//决定子view的位置
	@Override  //覆盖viewGroup
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
	//	super.onLayout(changed, l, t, r, b);//如果继承LinearLayout，LinearLayout 的 onLayout 方法解析布局xml中 TAG "com.example.myview.Workspace",把各个子View添加
	//如果继承的是ViewGroup onLayout 是抽象方法，空实现，要自己来指定子view的位置
	    Child child = (Child) this.findViewById(R.id.child01);
	    child.layout(0, 0, 40, 50);
	    
	}
	@Override//覆盖view
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	@Override//覆盖view
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		//当可以明确每个子View的高宽的时候，在onLayout中完成就可以了。
		
		//child.measure(childWidthMeasureSpec,
        //        MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY));
	//	this.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}
//	  Android系统调用onMeasure来定义view的大小，很长时间理解不是很透彻，今天花了些时间打日志来理解它。总结如下。
//
//      1. widthMeasureSpec和heightMeasureSpec这两个值是android:layout_width="200dp" android:layout_height="80dp"来定义的，它由两部分构成，可通过int specModeHeight = MeasureSpec.getMode(heightMeasureSpec); int specSizeHeight = MeasureSpec.getSize(heightMeasureSpec)来得到各自的值。
//
//如果android:layout_width="wrap_content"或android:layout_width="fill_parent"，哪么得到的specMode为MeasureSpec.AT_MOST，如果为精确的值则为MeasureSpec.EXACTLY。另外，specSize要想得到合适的值需要在Androidmanifest.xml中添加<uses-sdk android:minSdkVersion="10" />
//
//      2.系统默认的onMeasure调用方法是getDefaultSize来实现，有时候在自定义控件的时候多数采用        
//	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
	}
	

}
