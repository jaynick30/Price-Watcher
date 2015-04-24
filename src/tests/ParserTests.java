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

    @Test
    public void testPrint() {
        item = parser.parse(testURL);
        System.out.println(item.shipping);
        //assertEquals(item.price, )
    }
}