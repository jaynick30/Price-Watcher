package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Item;

import java.sql.*;

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
		String create = "CREATE TABLE " + table + " ('name' STRING, 'price' STRING, 'shipping' STRING, 'url' STRING, 'index' REAL)";
		try{statement.executeQuery(create);}
		catch (SQLException e) {
			errorMessage = "unable to create table";
			System.out.println(errorMessage);
		}
	}
	
	public void updateTable(Item item) {
		String update = "UPDATE " + table + " SET index = index + 1 WHERE name = " + item.title + " AND url = " + item.url;
		try{statement.executeQuery(update);}
		catch (SQLException e) {
			errorMessage = "unable to update table";
		}
	}
	
	public void addItem(Item item) {
		updateTable(item);
		String insert = "INSERT INTO " + table + " VALUES (" + item.title + ", "  + item.price + ", " + item.shipping.isFree() + ", " + item.url + ", 0";
		try{statement.executeQuery(insert);}
		catch (SQLException e) {
			errorMessage = "unable to add this item";
			System.out.println(errorMessage);
			e.printStackTrace();
		}
	}
	
	public void deleteItem(Item item) {
		String delete = "DELETE FROM " + table + " WHERE name = " + item.title + " AND url = " + item.url;
		try{statement.executeQuery(delete);}
		catch (SQLException e) {
			errorMessage = "unable to delete item";
			e.printStackTrace();
		}
	}
	
	public ResultSet getItem(Item item) {
		String query = "SELECT * FROM " + table + " WHERE name = " + item.title + " AND url = " + item.url + " ORDER BY index DESCENDING";
		try{return statement.executeQuery(query);}
		catch (SQLException e) {errorMessage = "unable to find items";}
		return null;
	}
	
	public Item getMostRecent(Item item) {
		String query = "SELECT * FROM " + table + " WHERE name = " + item.title + " AND url = " + item.url + " AND index = 0";
		try{
			ResultSet result = statement.executeQuery(query);
			Item resultItem = new Item(result.getString("url"));
			resultItem.title = result.getString("name");
			resultItem.price = result.getString("price");
			resultItem.setShipping(result.getBoolean("shipping"));
			return resultItem;
		}
		catch (SQLException e) {errorMessage = "unable to find item";}
		finally{System.out.println(errorMessage);}
		return null;
		}
	
	public void dropTable() {
		String query = "DROP TABLE " + table;
		try{ statement.executeQuery(query);}
		catch (SQLException e) {
			errorMessage = "unable to drop table";
			System.out.println(errorMessage);
		}
	}
	
	
	
	
	
	
	
	public void deleteAllNamed(String name) {
		String delete = "DELETE FROM " + table + " WHERE name = " + name;
		try{statement.executeQuery(delete);}
		catch(SQLException e) {
			errorMessage = "unable to delete items";
			e.printStackTrace();
		}
	}
	
	public ResultSet getAllFromUrl(String url) {
		String query = "SELECT * FROM " + table + "WHERE url = " + url + " ORDER BY index ASCENDING";
		try{return statement.executeQuery(query);}
		catch (SQLException e) {errorMessage = "unable to find item";}
		finally {System.out.println(errorMessage);}
		return null;
	}
	
	public ResultSet getAllFromUrlNamed(String url, String name) {
		String query = "SELECT * FROM " + table + "WHERE name = " + name + " AND url = " + url + " ORDER BY index ASCENDING";
		try{return statement.executeQuery(query);}
		catch (SQLException e) {errorMessage = "unable to find item";}
		finally {System.out.println(errorMessage);}
		return null;
	}
	
	public ResultSet getMostRecentUpdate() {
		String query = "SELECT MAX(timeStamp) FROM " + table;
		try{return statement.executeQuery(query);}
		catch(SQLException e) {errorMessage = "unable to retrieve update";}
		finally {System.out.println(errorMessage);}
		return null;
	}
}
