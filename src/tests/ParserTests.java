package tests;

import URL.Parser;
import model.Item;

import org.junit.Before;
import org.junit.Test;

import database.Manager;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParserTests {
    private Parser parser = new Parser();
    private Item item;
    private Item test;
    private Item book;
    private Item game;
    private Item backpack;
    private Item art;
    private Manager manager = new Manager("Test");

    
    @Before
    public void initialize() {
    	manager.createTable();
    	
        test = new Item("http://www.amazon.com/gp/product/B00IOY8XWQ/ref=s9_psimh_gw_p349_d0_i2?pf_rd_m=ATVPDKIKX0DER&pf_rd_s=desktop-1&pf_rd_r=1GFJPB3F15ZGZ6GGH4AV&pf_rd_t=36701&pf_rd_p=2079475182&pf_rd_i=desktop");
        test.price = "$199.00";
        test.title = "Kindle Voyage, 6\" High-Resolution Display (300 ppi) with Adaptive Built-in Light, PagePress Sensors, Wi-Fi - Includes Special Offers";
        test.setShipping(true);

        book = new Item("http://www.amazon.com/Memory-Amos-Decker-David-Baldacci/dp/1455559822/ref=sr_1_1?s=books&ie=UTF8&qid=1429838562&sr=1-1&keywords=book");
        book.price = "$14.67";
        book.title = "Memory Man (Amos Decker series)";
        book.setShipping(false);

        game = new Item("http://www.amazon.com/Bloodborne-PlayStation-4/dp/B00KVR4HEC/ref=sr_1_1?ie=UTF8&qid=1429839651&sr=8-1&keywords=bloodborne");
        game.price = "$56.99";
        game.title = "Bloodborne";
        game.setShipping(true);

        backpack = new Item("http://www.amazon.com/JanSport-Superbreak-Classic-Backpack-Black/dp/B0007QCQGI/ref=sr_1_1?ie=UTF8&qid=1430147820&sr=8-1&keywords=backpack");
        backpack.price = "$26.88";
        backpack.title = "Classic SuperBreak Backpack";
        backpack.setShipping(true);

        art = new Item("http://www.amazon.com/The-Artist-and-His-Wife/dp/B00E9EC28G/ref=sr_1_1?ie=UTF8&qid=1430271197&sr=8-1&keywords=fine+art");
        art.price = "$285,000.00";
        art.title = "The Artist and His Wife";
        art.setShipping(true);
    }
    

    @Test
    public void testPrint() {
        item = parser.parse(test.url);
        checkItem(test);
        manager.addItem(item);
        assertEquals(manager.getMostRecent(item), item);
    }

    @Test
    public void testBookURL() {
        item = parser.parse(book.url);
        checkItem(book);
    }

    @Test
    public void testGameURL() {
        item = parser.parse(game.url);
        checkItem(game);
    }

    @Test
    public void testBackpackURL() {
        item = parser.parse(backpack.url);
        printItemValues(item);
    }

    @Test
    public void testArtURL() {
        item = parser.parse(art.url);
        checkItem(art);
    }


    private void printItemValues(Item item) {
        System.out.println(item.title);
        System.out.println(item.price);
        System.out.println(item.shipping);
    }

    private void checkItem(Item other) {
        assertEquals(other.title, item.title);
        assertEquals(other.price, item.price);
        assertEquals(other.shipping, item.shipping);
        assertTrue(item.hasPrice());
        assertTrue(item.hasTitle());
    }
}