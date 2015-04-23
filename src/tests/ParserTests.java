package tests;

import URL.Parser;
import org.junit.Test;

public class ParserTests {
    public Parser parser = new Parser();

    private String testURL = "http://www.amazon.com/gp/product/B00IOY8XWQ/ref=s9_psimh_gw_p349_d0_i2?pf_rd_m=ATVPDKIKX0DER&pf_rd_s=desktop-1&pf_rd_r=1GFJPB3F15ZGZ6GGH4AV&pf_rd_t=36701&pf_rd_p=2079475182&pf_rd_i=desktop";

    @Test
    public void testPrint() {
        parser.parse(testURL);
    }
}