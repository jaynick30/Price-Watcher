package tests;

import static org.junit.Assert.*;
import model.Item;
import model.Shipping;

import org.junit.Before;
import org.junit.Test;

import database.Manager;

public class DBTests {
	Manager manager = new Manager("DBTest");
	
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
		item = kindle;
		item.price = "$1.00";
	}

}
