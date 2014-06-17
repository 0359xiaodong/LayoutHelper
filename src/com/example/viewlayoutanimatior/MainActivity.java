
package com.example.viewlayoutanimatior;


import android.app.Activity;
import android.os.Bundle;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.util.Log;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.MotionEvent;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View.OnTouchListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.myview.Workspace;



import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaActionSound;
import android.net.ConnectivityManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.UserHandle;
import android.os.Vibrator;
import android.os.WorkSource;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
    GLView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mainIntent.setPackage("com.android.stk");
        List<ResolveInfo> lists = getPackageManager().queryIntentActivities(mainIntent, PackageManager.GET_ACTIVITIES);
        for(ResolveInfo r : lists){
            Log.d("yzy","r="+r.activityInfo.name);
        }
      //  this.setContentView(R.layout.activity_main);
//        this.setContentView(R.layout.view);
        this.getWindow().setContentView(LayoutInflater.from(this).inflate(R.layout.view, null));
      //  this.setContentView(new Workspace(this, arrributeSet));
       // this.setContentView(new GLView(this));
    }

    @Override
	protected void onStop() {
    	
    	Log.d("yzy","onStop..........................."+getTopActivity(this));

		super.onStop();
	}
    String getTopActivity(Activity context)
    {
         ActivityManager manager = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE) ;
         List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1) ;
             
         if(runningTaskInfos != null)
           return (runningTaskInfos.get(0).topActivity).toString() ;
              else
           return null ;
    }
    

	@Override
	protected void onResume() {
		Log.d("yzy","onResume...........................");
		super.onResume();
	}


	class GLView extends GLSurfaceView{
        private final GLRenderer renderer;
        public GLView(Context context) {
            super(context);
            renderer = new GLRenderer(context);
            setRenderer(renderer);
        }
    }
    class GLRenderer implements GLSurfaceView.Renderer{
       
        private  Context context;
        private GLCube cube = new GLCube();
        private long startTime;
        private long fpsStartTime;
        private long numFrames;
        public GLRenderer(Context context) {
              this.context = context;
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glTranslatef(0, 0, -3.0f);
            
            long elapsed = System.currentTimeMillis() - startTime;
            gl.glRotatef(elapsed *(30f/1000f), 0, 1, 0);
            gl.glRotatef(elapsed *(15f/1000f),  1, 0,0);
            
            cube.draw(gl);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            gl.glViewport(0, 0, width, height);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            float ratio = (float)width/height;
            //最后两个参数分别表示 眼睛 与 近处剪切面 和 远处剪切面之间的距离
            GLU.gluPerspective(gl, 45.0f, ratio, 1, 100f);
            //设置光源
            float lightAmbient[] = new float[]{0.2f,0.2f,0.2f,1};
            float lightDiffuse[] = new float[]{1,1,1,1};
            float lightPos[] = new float[]{1,1,1,1};
            gl.glEnable(GL10.GL_LIGHT0);
            gl.glEnable(GL10.GL_LIGHTING);
            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbient,0);
            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuse,0);
            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPos,0);
            //设置材质
            float matAmbient[] = new float[]{1,1,1,1};
            float matDiffuse[] = new float[]{1,1,1,1};
            gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, matAmbient,0);
         //   gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT_AND_DIFFUSE, matDiffuse,0);
            gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, matDiffuse,0);
            //
            startTime = System.currentTimeMillis();
            fpsStartTime = startTime;
            numFrames = 0;
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            gl.glEnable(GL10.GL_DEPTH_TEST);
            gl.glDepthFunc(GL10.GL_LEQUAL);
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisable(GL10.GL_DITHER);
            //应用纹理
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl.glEnable(GL10.GL_TEXTURE_2D);
            GLCube.loadTexture(gl, context, R.drawable.ic_launcher);
        }
    }
}

