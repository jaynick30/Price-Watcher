package database;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Item;
import model.PriceGraph;

public class PriceGraphMaker {
	
	private Manager itemBase;
	
	public PriceGraphMaker() {
		itemBase = new Manager("Items");
	}
	
	public PriceGraph makePriceGraph(Item item) throws SQLException {
		PriceGraph graph = new PriceGraph(item.title);
		ResultSet itemData = itemBase.getItem(item);
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
