package com.example.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class Child  extends ViewGroup{

	public Child(Context context) {
		super(context);
	}
	public Child(Context context,AttributeSet s) {
		super(context,s);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		
	}

}
