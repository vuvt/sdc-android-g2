package com.nikmesoft.android.nearfood.models;

public class PointMap {
private double x,y;

public PointMap(){
	
}
public PointMap(double x, double y){
	this.x = x;
	this.y = y;
}
public double getPointX() {
	return x;
}
public void setPointX(double x) {
	this.x = x;
}
public double getPointY() {
	return y;
}
public void setPointY(double y) {
	this.y = y;
}
}
