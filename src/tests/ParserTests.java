package tests;

import URL.Parser;
import model.Item;
import org.junit.Test;

public class ParserTests {
    private Parser parser = new Parser();
    private Item item;
    private String testURL = "http://www.amazon.com/gp/product/B00IOY8XWQ/ref=s9_psimh_gw_p349_d0_i2?pf_rd_m=ATVPDKIKX0DER&pf_rd_s=desktop-1&pf_rd_r=1GFJPB3F15ZGZ6GGH4AV&pf_rd_t=36701&pf_rd_p=2079475182&pf_rd_i=desktop";
    private String testPrice = "$199.00";
    private String testTitle = "Kindle Voyage, 6\" High-Resolution Display (300 ppi) with Adaptive Built-in Light, PagePress Sensors, Wi-Fi - Includes Special Offers";
    private String testBookURL = "http://www.amazon.com/Memory-Amos-Decker-David-Baldacci/dp/1455559822/ref=sr_1_1?s=books&ie=UTF8&qid=1429838562&sr=1-1&keywords=book";

    /*@Test
    public void testPrint() {
        item = parser.parse(testURL);
        assertEquals(item.price, testPrice);
        assertEquals(item.title, testTitle);
        assertTrue(item.shipping);
        assertTrue(item.hasPrice());
        assertTrue(item.hasTitle());
    }*/

    @Test
    public void testBookURL() {
        item = parser.parse(testBookURL);
        printItemValues(item);
    }


    private void printItemValues(Item item) {
        System.out.println(item.price);
        System.out.println(item.title);
        System.out.println(item.shipping);
    }
}