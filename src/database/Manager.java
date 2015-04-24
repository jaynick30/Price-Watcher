package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	
	public void addItem(Item item) {
		String insert = "INSERT INTO " + table + " VALUES (" + item.title + ", "  + item.price + ", " + item.shipping + ", " + item.url + ", " + "datetime(now))";
		try{statement.executeQuery(insert);}
		catch (SQLException e) {
			errorMessage = "unable to add this item";
			e.printStackTrace();
		}
	}
	
	public void deleteItem(Item item) {
		String delete = "DELETE FROM " + table + " WHERE name = " + item.title + " AND price = " + item.price + " AND url = " + item.url;
		try{statement.executeQuery(delete);}
		catch (SQLException e) {
			errorMessage = "unable to delete item";
			e.printStackTrace();
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
	
	public ResultSet getAllNamed(String name) {
		String query = "SELECT * FROM " + table + "WHERE name = " + name + " ORDER BY timeStamp ASCENDING";
		try{return statement.executeQuery(query);}
		catch (SQLException e) {errorMessage = "unable to find item";}
		finally {System.out.println(errorMessage);}
		return null;
	}
	
	public ResultSet getAllFromUrl(String url) {
		String query = "SELECT * FROM " + table + "WHERE url = " + url + " ORDER BY timeStamp ASCENDING";
		try{return statement.executeQuery(query);}
		catch (SQLException e) {errorMessage = "unable to find item";}
		finally {System.out.println(errorMessage);}
		return null;
	}
	
	public ResultSet getAllFromUrlNamed(String url, String name) {
		String query = "SELECT * FROM " + table + "WHERE name = " + name + " AND url = " + url + " ORDER BY timeStamp ASCENDING";
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
