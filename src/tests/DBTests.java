package tests;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.util.ArrayList;

import model.Item;
import model.Shipping;

import org.junit.Before;
import org.junit.Test;

import URL.Parser;
import database.Manager;

public class DBTests {
	private Manager manager = new Manager("DBTest");
    private Parser parser = new Parser();

	
	private Item item;
    private Item kindle;
    private Item book;
	
	@Before
	public void initialize() {
		manager.createTable();
		
		kindle = new Item("http://www.amazon.com/gp/product/B00IOY8XWQ/ref=s9_psimh_gw_p349_d0_i2?pf_rd_m=ATVPDKIKX0DER&pf_rd_s=desktop-1&pf_rd_r=1GFJPB3F15ZGZ6GGH4AV&pf_rd_t=36701&pf_rd_p=2079475182&pf_rd_i=desktop");
        kindle.price = "$199.00";
        kindle.title = "Kindle Voyage";
        kindle.setShipping(Shipping.PAID);

        book = new Item("http://www.amazon.com/Memory-Amos-Decker-David-Baldacci/dp/1455559822/ref=sr_1_1?s=books&ie=UTF8&qid=1429838562&sr=1-1&keywords=book");
        book.price = "$14.67";
        book.title = "Memory Man (Amos Decker series)";
        book.setShipping(Shipping.PAID);
	}

	@Test
	public void testAdd() {
		manager.addItem(kindle);
		manager.addItem(kindle);
		manager.addItem(book);
		ArrayList<Item> items = manager.getAllRecent();
		checkItem(items.get(0), kindle);
		checkItem(items.get(1), book);
		manager.dropTable();
	}
	
	@Test
	public void testUpdate() {
		manager.addItem(kindle);
		manager.addItem(kindle);
		Item kindle2 = new Item("http://www.amazon.com/gp/product/B00IOY8XWQ/ref=s9_psimh_gw_p349_d0_i2?pf_rd_m=ATVPDKIKX0DER&pf_rd_s=desktop-1&pf_rd_r=1GFJPB3F15ZGZ6GGH4AV&pf_rd_t=36701&pf_rd_p=2079475182&pf_rd_i=desktop");
		kindle2.price = "$100.00";
		kindle2.title = "Kindle Voyage";
		kindle.setShipping(Shipping.PAID);
		manager.addItem(kindle2);
		ArrayList<Item> items = manager.getAll(kindle);
		checkItem(manager.getMostRecent(kindle), kindle2);
		manager.dropTable();
	}
	
	@Test
	public void testDelete() {
		manager.addItem(kindle);
		manager.addItem(kindle);
		manager.addItem(book);
		ArrayList<Item> threeItems = manager.getAll();
		assertEquals(threeItems.size(), 3);
		manager.deleteItem(kindle);
		ArrayList<Item> oneItem = manager.getAll();
		assertEquals(oneItem.size(), 1);
		manager.dropTable();
	}
	
	private void checkItem(Item item, Item other) {
        assertEquals(other.title, item.title);
        assertEquals(other.price, item.price);
        assertEquals(other.shipping, item.shipping);
        assertTrue(item.hasPrice());
        assertTrue(item.hasTitle());
        manager.dropTable();
    }

}
