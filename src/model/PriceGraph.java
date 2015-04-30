package model;

import java.util.ArrayList;

public class PriceGraph {
	public String itemName;
	public ArrayList<Point> points;
	
	public PriceGraph(String name) {
		itemName = name;
	}
	
	public void addPoint(int index, float price) {
		Point p = new Point(index, price);
		points.add(p);
	}
	
	public ArrayList<Point> getLastOneHundredPoints() {
		ArrayList<Point> lastPoints = new ArrayList<Point>();
		for (int i = points.size()-101; i<points.size(); i++) {
			lastPoints.add(points.get(i));
		}return lastPoints;
	}
}
