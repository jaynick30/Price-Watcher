package database;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.PriceGraph;

public class PriceGraphMaker {
	
	private Manager itemBase;
	
	public PriceGraphMaker() {
		try {itemBase = new Manager("Items");}
		catch (ClassNotFoundException e) {e.printStackTrace();}
		catch (SQLException e) {e.printStackTrace();}
	}
	
	public PriceGraph makePriceGraph(String url, String itemName) throws SQLException {
		PriceGraph graph = new PriceGraph(itemName);
		ResultSet itemData = itemBase.getAllFromUrlNamed(url, itemName);
		int idx = 0;
		while(itemData.next()) {
			float price = itemData.getFloat("price");
			int date = idx;
			graph.addPoint(date, price);
			idx++;
		}
		return graph;
	}
	
}
