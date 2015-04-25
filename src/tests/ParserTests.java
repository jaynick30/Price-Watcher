package tests;

import URL.Parser;
import model.Item;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParserTests {
    private Parser parser = new Parser();
    private Item item;
    private String testURL = "http://www.amazon.com/gp/product/B00IOY8XWQ/ref=s9_psimh_gw_p349_d0_i2?pf_rd_m=ATVPDKIKX0DER&pf_rd_s=desktop-1&pf_rd_r=1GFJPB3F15ZGZ6GGH4AV&pf_rd_t=36701&pf_rd_p=2079475182&pf_rd_i=desktop";
    private String testPrice = "$199.00";
    private String testTitle = "Kindle Voyage, 6\" High-Resolution Display (300 ppi) with Adaptive Built-in Light, PagePress Sensors, Wi-Fi - Includes Special Offers";
    private String testBookURL = "http://www.amazon.com/Memory-Amos-Decker-David-Baldacci/dp/1455559822/ref=sr_1_1?s=books&ie=UTF8&qid=1429838562&sr=1-1&keywords=book";
    private String testGameURL = "http://www.amazon.com/Bloodborne-PlayStation-4/dp/B00KVR4HEC/ref=sr_1_1?ie=UTF8&qid=1429839651&sr=8-1&keywords=bloodborne";

    @Test
    public void testPrint() {
        item = parser.parse(testURL);
        checkItem(testPrice, testTitle, "1");
    }

    @Test
    public void testBookURL() {
        item = parser.parse(testBookURL);
        printItemValues(item);
    }

    @Test
    public void testGameURL() {
        item = parser.parse(testGameURL);
        printItemValues(item);
    }


    private void printItemValues(Item item) {
        System.out.println(item.price);
        System.out.println(item.title);
        System.out.println(item.shipping);
    }

    private void checkItem(String price, String title, String shipping) {
        assertEquals(price, item.price);
        assertEquals(title, item.title);
        assertTrue(item.shipping == shipping);
        assertTrue(item.hasPrice());
        assertTrue(item.hasTitle());
    }
}