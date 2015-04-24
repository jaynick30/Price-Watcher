package database;

import model.Item;

import java.sql.*;

public class Manager {

	protected Connection itemDB;
	protected String table;
	protected Statement statement;
	private String errorMessage;
	
	public Manager(String tableName) throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		itemDB = DriverManager.getConnection("jdbc:sqlite:ItemDB");
		statement = itemDB.createStatement();
		table = tableName;
	}
	
	public void addItem( Item item) {
		String insert = "INSERT INTO" + table + "VALUES (" + item.title + ", "  + item.price + ", " + item.shipping + ", " + item.url + ", " + "datetime(now))";
		try{
			statement.execute(insert);
		}catch (SQLException e) {
			errorMessage = "unable to add this item";
			e.printStackTrace();
		}
	}
	
	public ResultSet getItem(Item item) {
		String query = "SELECT * FROM" + table + "WHERE name = " + item.title;
		try{
			return statement.executeQuery(query);
		}catch (SQLException e) {
			errorMessage = "unable to find item";
		}
		return null;
	}
}
