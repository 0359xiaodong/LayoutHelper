package com.example.viewlayoutanimatior;

public class Folder {
      public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPresets_container() {
		return presets_container;
	}
	public void setPresets_container(String presets_container) {
		this.presets_container = presets_container;
	}
	public int getScreen() {
		return screen;
	}
	public void setScreen(int screen) {
		this.screen = screen;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	String title = "";
      String presets_container = "false";
      int screen;
      int x;
      int y;
}
