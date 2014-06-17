package com.example.viewlayoutanimatior;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.xmlpull.v1.XmlSerializer;

import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaActionSound;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.UserHandle;
import android.os.Vibrator;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
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
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends Activity {
	private ContentResolver mContentResolver;
	public static final String AUTHORITY = "com.miui.home.launcher.settings";
	public static final String TABLE_FAVORITES = "favorites";
	private static final String CONTENT_URI_STRING = "content://" + AUTHORITY
			+ "/" + TABLE_FAVORITES;

	private ArrayList<Favorite> mFavrotes = new ArrayList<Favorite>();
	private ArrayList<Folder> mFolders = new ArrayList<Folder>();
	private ArrayList<ShortCut> mShortCuts = new ArrayList<ShortCut>();
	private ArrayList<Wiget> mWigets = new ArrayList<Wiget>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		mContentResolver = this.getContentResolver();
		
		Cursor cursor = mContentResolver.query(getContentUri(), null, null,
				null, null);
		while (cursor.moveToNext()) {
			String type = cursor.getString(cursor.getColumnIndex("itemType"));
			int itype = Integer.parseInt(type);
			switch (itype) {
			case Setting.FOLDER:
				Folder folder = new Folder();
				String title = cursor.getString(cursor.getColumnIndex("title"));
				folder.setScreen(cursor.getInt(cursor.getColumnIndex("screen"))-1);
				folder.setX(cursor.getInt(cursor.getColumnIndex("cellX")));
				folder.setY(cursor.getInt(cursor.getColumnIndex("cellY")));
				folder.setTitle(title);
				mFolders.add(folder);
				break;
			case Setting.FAVORITE:

				Favorite favorite = new Favorite();
				String intent = cursor.getString(cursor
						.getColumnIndex("intent"));

				String pkgName = parsePkgNameFromIntent(intent);
				String className = parseClassNameFromIntent(intent);
				favorite.setClassName(className);
				favorite.setPackageName(pkgName);
				favorite.setScreen(cursor.getInt(cursor
						.getColumnIndex("screen"))-1);
				favorite.setX(cursor.getInt(cursor.getColumnIndex("cellX")));
				favorite.setY(cursor.getInt(cursor.getColumnIndex("cellY")));
				favorite.setContainer(cursor.getInt(cursor
						.getColumnIndex("container")));
				mFavrotes.add(favorite);
				break;
			case Setting.SHORTCUT:
				ShortCut shortcut = new ShortCut();
				String shortcutPkgName = parsePkgNameFromIntent(cursor
						.getString(cursor.getColumnIndex("intent")));
				String shortcutClassName = parseClassNameFromIntent(cursor
						.getString(cursor.getColumnIndex("intent")));
				shortcut.setClassName(shortcutClassName);
				shortcut.setPackageName(shortcutPkgName);
				mShortCuts.add(shortcut);

				break;
			case Setting.WIGET:

				break;

			default:
				break;
			}
		}
		this.findViewById(R.id.layout45).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							generateWorkspaceLayoutXml("default_workspace4x5.xml");
							Toast.makeText(MainActivity2.this, "/sdcard/default_workspace4x5.xml", Toast.LENGTH_LONG).show();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				});
		this.findViewById(R.id.layout34).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							generateWorkspaceLayoutXml("default_workspace3x4.xml");
							Toast.makeText(MainActivity2.this, "/sdcard/default_workspace3x4.xml", Toast.LENGTH_LONG).show();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				});
		this.findViewById(R.id.layout44).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							generateWorkspaceLayoutXml("default_workspace.xml");
							Toast.makeText(MainActivity2.this, "/sdcard/default_workspace.xml", Toast.LENGTH_LONG).show();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				});
	}

	public static Uri getJoinContentUri(String join) {
		return Uri.parse(CONTENT_URI_STRING + join);
	}

	public static Uri getContentUri() {
		return Uri.parse(CONTENT_URI_STRING);
	}

	//

	// String xx =
	// "#Intent;action=android.intent.action.MAIN;category=android.intent.category.LAUNCHER;launchFlags=0x10200000;component=com.android.stk/.StkLauncherActivity;end";

	public static String parsePkgNameFromIntent(String intent) {
		String[] component = intent.split(";");
		for (String temp : component) {
			if (temp.contains("component")) {
				String[] pkgAndClass = temp.split("=");
				return pkgAndClass[1].split("/.")[0];
			}
		}
		return intent;
	}

	public static String parseClassNameFromIntent(String intent) {
		String[] component = intent.split(";");
		for (String temp : component) {
			if (temp.contains("component")) {
				String[] pkgAndClass = temp.split("=");
				if(pkgAndClass[1].split("/.").length > 1){
					return pkgAndClass[1].split("/.")[1];
				}
				
			}
		}
		return intent;
	}
//Stk应用 retained=true
	public void generateWorkspaceLayoutXml(String layout)
			throws FileNotFoundException {
		FileOutputStream out = new FileOutputStream(
				android.os.Environment.getExternalStorageDirectory() + "/"
						+ layout, false);
		XmlSerializer serializer = Xml.newSerializer();
		try {
			serializer.setOutput(out, "UTF-8");
			serializer.startDocument("UTF-8", true);
			serializer.startTag(null, "favorites");
			serializer.attribute(null, "xmlns:launcher",
					"http://schemas.android.com/apk/res/com.miui.home");
			serializer.setFeature(
					"http://xmlpull.org/v1/doc/features.html#indent-output",
					true);// 设置换行

			serializer.startTag(null, "default");
			serializer.attribute(null, "launcher:screen", "2");
			serializer.endTag(null, "default");
			

			for (Folder f : mFolders) {
				serializer.startTag(null, "folder");
				serializer.attribute(null, "launcher:screen", f.getScreen()
						+ "");
				serializer.attribute(null, "launcher:title", f.getTitle());
				
				if(f.getTitle().equals("com.miui.home:string/default_folder_title_recommend")){
					serializer.attribute(null, "launcher:presets_container", "true");
				}
				serializer.attribute(null, "launcher:x", f.getX() + "");
				serializer.attribute(null, "launcher:y", f.getY() + "");
				serializer.endTag(null, "folder");
			}
			
			serializer.startTag(null, "search");
			serializer.attribute(null, "launcher:screen", "0");
			serializer.attribute(null, "launcher:x", "0");
			serializer.attribute(null, "launcher:y", "0");
			serializer.endTag(null, "search");

			serializer.startTag(null, "clock2x4");
			serializer.attribute(null, "launcher:screen", "1");
			serializer.attribute(null, "launcher:x", "0");
			serializer.attribute(null, "launcher:y", "0");
			serializer.endTag(null, "clock2x4");
			
			for (ShortCut s : mShortCuts) {
				serializer.startTag(null, "shortcut");
				serializer.attribute(null, "launcher:packageName",
						s.getPackageName());
				serializer.attribute(null, "launcher:className",
						s.getPackageName() + "." + s.getClassName());
				serializer.attribute(null, "launcher:screen", s.getScreen()
						+ "");
				serializer.attribute(null, "launcher:container",
						s.getContainer() + "");
				serializer.attribute(null, "launcher:x", s.getX() + "");
				serializer.attribute(null, "launcher:y", s.getY() + "");
				serializer.endTag(null, "shortcut");
			}

			for (Favorite f : mFavrotes) {
				if (f.getPackageName().contains(this.getPackageName())) {
					continue;
				}
				serializer.startTag(null, "favorite");
				serializer.attribute(null, "launcher:packageName",
						f.getPackageName());
				serializer.attribute(null, "launcher:className",
						f.getPackageName() + "." + f.getClassName());
				if(f.getScreen() >= 0){
					serializer.attribute(null, "launcher:screen", f.getScreen()+ "");
				}
				if(f.getContainer() != -100 ){
					serializer.attribute(null, "launcher:container",
							f.getContainer() + "");
					f.setY(0);
				}
				serializer.attribute(null, "launcher:x", f.getX() + "");
				serializer.attribute(null, "launcher:y", f.getY() + "");
				serializer.endTag(null, "favorite");
			}

			serializer.endTag(null, "favorites");
			serializer.endDocument();
			out.flush();
			out.close();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
