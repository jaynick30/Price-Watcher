package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Item;

import java.sql.*;
import java.util.ArrayList;

import javax.naming.spi.DirStateFactory.Result;

public class Manager {

	protected Connection itemDB;
	protected String table;
	protected Statement statement;
	private String errorMessage;
	
	public Manager(String tableName){
		try {
			Class.forName("org.sqlite.JDBC");
			itemDB = DriverManager.getConnection("jdbc:sqlite:ItemDB");
			statement = itemDB.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		table = tableName;
	}
	
	public void createTable() {
		String create = "CREATE TABLE " + table + " (name STRING, price STRING, shipping STRING, url STRING, idx REAL)";
		try{statement.execute(create);}
		catch (SQLException e) {
			errorMessage = "unable to create table, may already exist";
			System.out.println(errorMessage);
		}
	}
	
	private void updateTable(Item item) {
		String update = "UPDATE " + table + " SET idx = idx + 1 WHERE name = '" + item.title + "' AND url = '" + item.url + "'";
		try{statement.execute(update);}
		catch (SQLException e) {
			errorMessage = "unable to update table";
		}
	}
	
	public void addItem(Item item) {
		updateTable(item);
		String insert = "INSERT INTO " + table + " VALUES ('" + item.title + "', '"  + item.price + "', '" + item.shipping.isFree() + "', '" + item.url + "', 0)";
		try{statement.execute(insert);}
		catch (SQLException e) {
			errorMessage = "unable to add this item";
			System.out.println(errorMessage);
			e.printStackTrace();
		}
	}
	
	public void deleteItem(Item item) {
		String delete = "DELETE FROM " + table + " WHERE name = '" + item.title + "' AND url = '" + item.url + "'";
		try{statement.execute(delete);}
		catch (SQLException e) {
			errorMessage = "unable to delete item";
			e.printStackTrace();
		}
	}
	
	public ArrayList<Item> getAll(Item item) {
		ArrayList<Item> items = new ArrayList<>();
		String query = "SELECT * FROM " + table + " WHERE name = '" + item.title + "' AND url = '" + item.url + "' ORDER BY idx desc";
		try{
			ResultSet result = statement.executeQuery(query);
			while(result.next()){
				Item resultItem = new Item(result.getString("url"));
				resultItem.title = result.getString("name");
				resultItem.price = result.getString("price");
				resultItem.setShipping(result.getBoolean("shipping"));
				items.add(resultItem);
			}
			return items;
		}
		catch (SQLException e) {errorMessage = "unable to find items";}
		return items;
	}
	
	public ArrayList<Item> getAll() {
		ArrayList<Item> items = new ArrayList<>();
		String query = "SELECT * FROM " + table;
		try{
			ResultSet result = statement.executeQuery(query);
			while(result.next()){
				Item resultItem = new Item(result.getString("url"));
				resultItem.title = result.getString("name");
				resultItem.price = result.getString("price");
				resultItem.setShipping(result.getBoolean("shipping"));
				items.add(resultItem);
			}
			return items;
		}
		catch (SQLException e) {errorMessage = "unable to find items";}
		return items;
	}
	
	public Item getAtIndex(Item item, Integer index) {
		String query = "SELECT * FROM " + table + " WHERE name = '" + item.title + "' AND url = '" + item.url + "' AND idx = " + index;
		try{
			ResultSet result = statement.executeQuery(query);
			result.next();
			Item resultItem = new Item(result.getString("url"));
			resultItem.title = result.getString("name");
			resultItem.price = result.getString("price");
			resultItem.setShipping(result.getBoolean("shipping"));
			return resultItem;
		}
		catch (SQLException e) {
			errorMessage = "unable to find item";
			System.out.println(errorMessage);
		}
		return null;
		}
	
	public Item getMostRecent(Item item) {
		return getAtIndex(item, 0);
	}
	
	
	public ArrayList<Item> getAllRecent() {
		ArrayList<Item> items = new ArrayList<Item>();
		String query = "SELECT * FROM " + table + " WHERE idx = 0";
		try{
			ResultSet result = statement.executeQuery(query);
			while(result.next()){
				Item item = new Item(result.getString("url"));
				item.title = result.getString("name");
				item.price = result.getString("price");
				item.setShipping(result.getBoolean("shipping"));
				items.add(item);
			}
		}
		catch (SQLException e) {
			errorMessage = "unable to find items";
			System.out.println(errorMessage);
		}
		return items;
	}
	
	public void dropTable() {
		String query = "DROP TABLE " + table;
		try{ statement.executeQuery(query);}
		catch (SQLException e) {
			errorMessage = "unable to drop table";
			System.out.println(errorMessage);
		}
	}
	
	
}
