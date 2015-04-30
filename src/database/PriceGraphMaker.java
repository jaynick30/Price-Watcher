package database;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Item;
import model.PriceGraph;

public class PriceGraphMaker {
	
	private Manager itemBase;
	
	public PriceGraphMaker() {
		itemBase = new Manager("Items");
	}
	
	public PriceGraph makePriceGraph(Item item) throws SQLException {
		PriceGraph graph = new PriceGraph(item.title);
		ArrayList<Item> items = itemBase.getAll(item);
		for (int i=0; i<items.size(); i++) {
			graph.addPoint(i, Integer.decode(item.price));
		}
		return graph;
	}
	
}
